package cl.bbr.common.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

public class Logoff extends Command {

	private String paramUrl;
		
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		HttpSession session;
		session = req.getSession();
		
		// Valida Par�metros
		if ( req.getParameter("url") == null )
			throw new ParametroObligatorioException("url");						
		
		paramUrl = new String(req.getParameter("url"));		
		logger.debug("Url: " + paramUrl);
		
		//Matar sesi�n
		logger.debug("Terminando sessi�n http...");
		if ( session.getAttribute("user") != null ){
			session.removeAttribute("user");
			logger.debug("Se removi� variable de sesi�n user");
		}
		session.invalidate();
		
		//redirecciona
		res.sendRedirect(paramUrl);
	}



}
