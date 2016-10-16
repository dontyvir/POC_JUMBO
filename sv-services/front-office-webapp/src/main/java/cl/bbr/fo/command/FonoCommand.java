package cl.bbr.fo.command;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.exception.SystemException;
import cl.bbr.fo.util.Cod_error;
import cl.bbr.log.Logging;

/**
 * Clase abstracta de la cual heredan todos los comandos. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public abstract class FonoCommand extends HttpServlet implements Servlet {

	/**
	 * Instancia un logger
	 */
	private Logging logger = new Logging(this);

	/**
	 * Código de error
	 */
	private String cod_error = "";

	/* (sin Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {

		// Escribe información en el log al iniciar el comando
		logger.inicio_comando(this);
		boolean bol_res = true;

		try {

			if (VerifyAccessControl(arg0, arg1) == false) {
				logger.error("No tiene permisos suficientes: "+ Cod_error.GEN_ERR_PERMISOS);
				this.cod_error = Cod_error.GEN_ERR_PERMISOS;	
				bol_res = false;
			} else if (this.ValidateParameters(arg0, arg1) == false) {
				logger.error("Faltan parámetros mínimos (FonoCommand): " + Cod_error.GEN_FALTAN_PARA);
				this.cod_error = Cod_error.GEN_FALTAN_PARA;
				bol_res = false;
			} else {
				arg1.setHeader("Cache-control","no-cache");
				arg1.setHeader("Pragma","no-cache");
				arg1.setDateHeader ("Expires", 0);
				arg1.setContentType("text/html");
				execute(arg0, arg1);
			}

			if (bol_res == false)
				ErrorTaskCommand(arg0, arg1);
			logger.fin_comando(this);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getmessage (FonoCommand): " + e.getMessage());
			logger.fin_comando(this);
			ResourceBundle rb = ResourceBundle.getBundle("fo");			
			arg1.sendRedirect(rb.getString("command.dir_error"));
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
	 * Valida parámetros mínimos
	 * @param arg0 HttpServletRequest
	 * @param arg1 HttpServletResponse
	 * @return True o false
	 */
	private boolean ValidateParameters(HttpServletRequest arg0,
			HttpServletResponse arg1) {
		return true;
	}

	/**
	 * Verifica control de acceso
	 * @param arg0 HttpServletRequest
	 * @param arg1 HttpServletResponse
	 * @return True o False
	 * @throws SystemException 
	 * @throws NumberFormatException 
	 * @throws IOException 
	 */
	private boolean VerifyAccessControl(HttpServletRequest arg0,
			HttpServletResponse arg1) throws NumberFormatException, SystemException, IOException {

		if ( arg0.getRequestURI().toString().indexOf("FonoLogonForm") > 0 
				|| arg0.getRequestURI().toString().indexOf("FonoLogon") > 0
				|| arg0.getRequestURI().toString().indexOf("FonoLogoff") > 0
				)
			return true;
				
		/**
		 * Revisar si existe o no sesión
		 */
		HttpSession session = arg0.getSession();
		if ( session.getAttribute("ses_eje_id") == null )
			return false;
		
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
		ResourceBundle rb = ResourceBundle.getBundle("fo");		
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