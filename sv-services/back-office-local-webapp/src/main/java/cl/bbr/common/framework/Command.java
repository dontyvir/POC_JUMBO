package cl.bbr.common.framework;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.usuarios.dto.ComandoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

import cl.bbr.log.Logging;


public abstract class Command extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	/**
	 * Instancia un logger
	 */
	protected Logging logger = new Logging(this);
	protected String path_html = "";
	protected String cmd_dsp_error	= "";
	
	//protected Logging logger;
/*	 protected HttpServletRequest req;
	 protected HttpServletResponse res;
	 protected HttpSession session;
	 protected UserDTO usr = null;
	 protected String PAGERR = "/BO/BOC/ViewError";
	 */
	
	 protected final void doGet(HttpServletRequest req, HttpServletResponse res) 
	 	throws ServletException, IOException {
		
		// Logger 				logger;
		// HttpServletRequest 	req;
		// HttpServletResponse 	res;
		HttpSession 			session;
		UserDTO 				usr = null;
		
		ResourceBundle rb = ResourceBundle.getBundle("bo");
		String system_error_url = rb.getString("command.system_error_page");
		
		// Asigna ruta a la carpeta con los templates
		path_html = rb.getString("conf.path.html");
		
		// Asigna direcci�n url comando de despliegue de error
		cmd_dsp_error = rb.getString("command.cmd_dsp_error");
		
		
		//logger = Logger.getLogger(getClass());
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Inicio Ejecuci�n Comando: " + getClass().getName());
		logger.debug("-----------------------------------------------------------------");
		
	
		// Se recupera la sesi�n del usuario
		session = req.getSession();
			
		if ( session.getAttribute("user") != null ){
			usr = (UserDTO)session.getAttribute("user");
			logger.debug("user session encontrada");
		}else{
			usr = null;
			logger.debug("No se encuentra user session");
		}		
		
		
		// Verificaci�n de Control de Acceso
		if(VerifyAccessControl(usr, logger)){
			logger.debug("Verificaci�n de Control de Acceso OK");
		}else{
			logger.error("No autorizado a ejecutar el comando");
			String msg = "No est� autorizado a ejecutar el comando";
			String url = "";
			res.sendRedirect( cmd_dsp_error + "?mensaje="+msg+"&url="+url);
			return;
		}

		
		try{
			Execute(req,res,usr);
		}catch (ParametroObligatorioException e1){
			logger.error(e1.getMessage());
			logger.error("ERROR: Par�metro Requerido: " + e1.getMessage());
			//TaskError.ErrorTask("Par�metro Requerido: "+e1.getMessage(),req, res, logger);
			res.sendRedirect(system_error_url);
		}catch (SystemException e){
			logger.error("SystemException: " + e.getMessage());
			e.printStackTrace();
			res.sendRedirect(system_error_url);
		}catch (Exception e){
			logger.error("Excepcion No Controlada en Command: " + e.getMessage());
			e.printStackTrace();
			res.sendRedirect(system_error_url);
		}
		
		//TaskError.ErrorTask(e.getMessage(),req, res, logger);

		logger.debug("-----------------------------------------------------------------");
		logger.debug("Fin Ejecuci�n Comando");
		logger.debug("-----------------------------------------------------------------");
	 }
	
	 protected final void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		doGet(arg0,arg1);
	}
	 
	 
	// Implementar control de accesos
	private boolean VerifyAccessControl(UserDTO usr , Logging logger ){
		ComandoDTO 	cmd 			= null;
		boolean 	tienepermiso 	= false;
		int 		idx 			= getClass().getName().lastIndexOf(".");
		String 		comando 		= getClass().getName().substring(idx+1);

		logger.debug("Verificando control de acceso comando ... " + comando);

		// llamado al BizDelegate
		BizDelegate biz = new BizDelegate();
		
		try {
			cmd = biz.getComandoByName(comando);
		} catch (BolException e) {
			e.printStackTrace();
			logger.error("Error al obtener comando del bizdelegate");
			return false; //ver bien qu� hay que hacer ac�
		}
		
		// si no se encuentra el comando, entonces retorna falso
		if ( cmd == null ){
			logger.info("Comando no reconocido, verifique tabla de comandos. Por seguridad, se deniega el acceso.");
			return false;
		}
		
		// si el comando est� desactivo, entonces retorna falso
		if ( cmd.getActivo().equals("D") ){
			logger.info("Comando est� desactivo en la base de datos, se deniega la ejecuci�n.");
			return false;
		}
	
		// si el comando no requiere seguridad entonces retornamos true
		if ( cmd.getSeguridad().equals("N") ){
			logger.debug("Comando no requiere seguridad");
			return true;
		}
			
		// chequeamos si el perfil del usuario tiene acceso al comando
		try {
			
			// Si el usuario no ha iniciado sesi�n entonces lo sacamos
			if (usr == null){
				logger.info("El usuario no se ha autenticado y est� ejecutando un comando que requiere autenticaci�n");
				return false;
			}
			
			// Si el usuario ya ha iniciado sesi�n entonces hacemos la verificaci�n de permisos
			tienepermiso = biz.doCheckPermisoPerfilComando(usr,cmd.getId_cmd());
			logger.debug("Permiso de ejecuci�n del comando: " + tienepermiso);
			
		} catch (BolException e) {
			e.printStackTrace();
			logger.debug("Error al checkear permiso de ejecuci�n del comando");
			return false;
		}
		
		logger.debug("Fin verificaci�n de acceso.");
	
		return tienepermiso;
	}
	

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception{
		logger.debug("!!! No se ha sobrescrito el m�todo Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) !!!");
	}
	
}
