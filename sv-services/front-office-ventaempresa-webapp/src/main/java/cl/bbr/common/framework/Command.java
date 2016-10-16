package cl.bbr.common.framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.exception.VteException;
import cl.bbr.vte.log.Logging;
import cl.bbr.vte.utils.Cod_error;

/**
 * Clase abstracta de la cual heredan todos los comandos. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public abstract class Command extends HttpServlet implements Servlet {

	/**
	 * Instancia un logger
	 */
	protected Logging logger = new Logging(this);
	
	/**
	 * Instancia para el directorio para el html del template
	 */
	protected String path_html = "";

	/**
	 * Código de error
	 */
	private String cod_error = "";

	/* (sin Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {

		// Seteamos el usuario de la session al log
		HttpSession session = arg0.getSession();
		if( session.getAttribute("ses_com_id") != null && session.getAttribute("ses_com_nombre") != null )
			logger.setUsuario( session.getAttribute("ses_com_id").toString(), session.getAttribute("ses_com_nombre").toString() );

		ResourceBundle rb = ResourceBundle.getBundle("vte");
		
		// Asigna ruta a la carpeta con los templates
		path_html = rb.getString("conf.path.html");
		
		// Escribe información en el log al iniciar el comando
		logger.inicio_comando(this);
		boolean bol_res = true;

		try {

			if (VerifyAccessControl(arg0, arg1) == false) {
				logger.error("No tiene permisos suficientes: "+ Cod_error.GEN_ERR_PERMISOS);
				this.cod_error = Cod_error.GEN_ERR_PERMISOS;	
				bol_res = false;
			} else if (ValidateParameters(arg0, arg1, new ArrayList()) == false) {
				logger.error("Faltan parámetros mínimos: " + Cod_error.GEN_FALTAN_PARA);
				this.cod_error = Cod_error.GEN_FALTAN_PARA;
				bol_res = false;
			} else {
				arg1.setHeader("Content-Type","text/html; charset=iso-8859-1");
				arg1.setHeader("Cache-control","no-cache");
				arg1.setHeader("Pragma","no-cache");
				arg1.setDateHeader ("Expires", 0);
				execute(arg0, arg1);
			}

			if (bol_res == false)
				ErrorTaskCommand(arg0, arg1);
			logger.fin_comando(this);

		} catch (ParametroObligatorioException e1) {
				logger.fatal("Error: " + e1.getMessage());
				logger.fatal("tostring: " + e1.toString());
				logger.fin_comando(this);
				arg1.sendRedirect(rb.getString("command.dir_error") + "?mensaje=" + rb.getString("general.mensaje.error.falta.parametro") + " " + e1.getMessage()+"&url=ViewLogonForm");
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal("Error: " + e.getMessage());
			logger.fatal("tostring: " + e.toString());
			logger.fin_comando(this);
			arg1.sendRedirect(rb.getString("command.dir_error")+"?mensaje="+e.getMessage()+"&url=ViewLogonForm");
		}

	}

	/* (sin Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		doGet(arg0, arg1);
	}

	/**
	 * Proceso que ejecuta el proceso, se implementa en las clases que heredan
	 * @param arg0
	 * @param arg1
	 * @throws Exception
	 */
	protected abstract void execute(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception;

	/**
	 * Retorna la instancia del logger
	 * @return logger
	 */
	protected Logging getLogger() {
		return this.logger;
	}

	/**
	 * Valida parámetros mínimos necesarios
	 * 
	 * @param arg0	Request recibido desde el navegador
	 * @param arg1	Response recibido desde el navegador
	 * @return		True: ok, False: faltan parámetros
	 */
	protected boolean ValidateParameters(HttpServletRequest arg0, HttpServletResponse arg1, ArrayList campos) {
	
		for (int i = 0; i < campos.size(); i++) {
			String campo = (String) campos.get(i);
			if (arg0.getParameter(campo) == null || arg0.getParameter(campo).compareTo("") == 0) {
				logger.error("Falta parámetro: " + campo);
				return false;
			}
		}

		return true;

	}	
	

	/**
	 * Verifica control de acceso
	 * @param arg0 HttpServletRequest
	 * @param arg1 HttpServletResponse
	 * @return True o false
	 * @throws SystemException 
	 * @throws NumberFormatException 
	 * @throws IOException 
	 * @throws VteException 
	 */
	private boolean VerifyAccessControl(HttpServletRequest arg0,
			HttpServletResponse arg1) throws NumberFormatException, SystemException, IOException, VteException {

		ResourceBundle rb = ResourceBundle.getBundle("vte");			
		String servlet = rb.getString("command.servlet.sin.control.acceso");
		String[] aux_text = servlet.split(",");
		
		for( int i = 0; i < aux_text.length; i++ ) {
			if (arg0.getRequestURI().toString().indexOf( aux_text[i] ) > 0 )
				return true;
		}
				
		/**
		 * Revisar si existe o no sesión
		 */
		HttpSession session = arg0.getSession();
		if (session.getAttribute("ses_com_id") == null )
			return false;
		
		/**
		 * Si esta conectado el cliente debe verificar su cambio de clave
		 */
		if( session.getAttribute("ses_com_id") != null ) {
			/**
			 * Revisar si debe cambiar la clave o no
			 */
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			CompradoresDTO comprador = (CompradoresDTO)biz.getCompradoresById( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			if( comprador.getCpr_estado().compareTo("C") == 0 ) {
				this.cod_error = Cod_error.GEN_CAM_CLAVE;
				arg1.sendRedirect(rb.getString("command.cambio_clave"));
			}
		}
		
		
		return true;
	}

	/**
	 * Reacciona ante errores del comando
	 * @param arg1
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void ErrorTaskCommand(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		ResourceBundle rb = ResourceBundle.getBundle("vte");		
		String cod_error = this.cod_error;
		HttpSession session = arg0.getSession();
		
		session.setAttribute( "cod_error", cod_error);
		
		if (cod_error == Cod_error.GEN_ERR_PERMISOS) {
			arg1.sendRedirect(rb.getString("command.sin_permisos"));
		}
		else {
			arg1.sendRedirect(rb.getString("command.dir_error"));
		}
	}

}