package cl.bbr.common.command;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Autenticador;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Registra sesi�n del usuario
 * @author jsepulveda
 * @param user
 * @param pass
 * @param url
 */
public class Logon extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		String paramUser	= "";
		String paramPass	= "";
		String paramUrl		= "";
		
		HttpSession session;
		session = req.getSession();
				
		// 1. Par�metros de inicializaci�n servlet
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);
		
		
		// 2. Procesa par�metros del request
		logger.debug("Procesando par�metros...");
		
		if ( req.getParameter("user") == null ) 
			throw new ParametroObligatorioException("user");
		
		if ( req.getParameter("pass") == null )
			throw new ParametroObligatorioException("pass");
		
		if ( req.getParameter("url") == null )
			throw new ParametroObligatorioException("url");						
		
		paramUser 	= new String(req.getParameter("user"));
		paramPass 	= new String(req.getParameter("pass"));
		paramUrl 	= new String(req.getParameter("url"));		
		
		logger.debug("User: " + paramUser);
		logger.debug("Pass: " + paramPass);
		logger.debug("Url: " + paramUrl);		

		
		// Proceso de autentificaci�n
		Autenticador auth = new Autenticador();
		
		boolean isAuth;
		
		// 
		ResourceBundle rb = ResourceBundle.getBundle("bo");
		String auth_method = rb.getString("conf.auth.method");


		if ( auth_method.equals("LDAP") )
			isAuth = auth.doLDAPAuth(paramUser,paramPass);
		else
			isAuth = auth.doAuth(paramUser,paramPass);
		
		if ( isAuth ){

			// si ya exist�a un user en la sesi�n lo borra
			if ( session.getAttribute("user") != null ){
				session.removeAttribute("user");
				logger.debug("Ya exist�a una sesi�n con ese nombre, la cual fue removida");
			}
			
			BizDelegate biz = new BizDelegate();
			
			// Obtiene datos del usuario
			UserDTO user = biz.getUserByLogin(paramUser);

			
			if ( user == null ){
				// Si es nulo, significa que el usuario se autentic� (LDAP) pero no existe en la base local
				logger.info("Usuario autenticado en LDAP pero no encontrado en BD Local: ");				
				paramUrl = UrlError + "?rc=3"; //lo redirecciona a la vista de error
			}
			else{
				// Registra variable de sesi�n user
				session.setAttribute("user",user);
				logger.debug("Se setea sesi�n de usuario");				
			}
		
		}
		else
		{
			logger.info("Autenticaci�n fallida: ");
			logger.info("Error: " + auth.getErrCode() + " : " + auth.getErrMsg());
			
			paramUrl = UrlError + "?rc=" + auth.getErrCode(); //lo redirecciona a la vista de error
			
		}

		
		// redirecciona salida
		res.sendRedirect(paramUrl);
	
	}
	
	
}
