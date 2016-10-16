package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que modifica a los Usuarios
 * @author bbr
 *
 */
public class ModUser extends Command {
	private final static long serialVersionUID = 1;
 
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	 
     // 1. seteo de Variables del método
     String paramUrl = "";
     long paramUsu_cod = 0L;
     //String paramLog = "";
     String paramNombre = "";
     String paramApellido = "";
     String paramApellidoM = "";
     String paramClave = "";
     String paramEmail = "";
     String paramEstado = "";
     //long paramLocal = 0L;
     
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("usu_cod") == null ){throw new ParametroObligatorioException("usu_cod es nulo");}

     // 2.2 obtiene parametros desde el request
     paramUrl 		= req.getParameter("url");
     paramUsu_cod 	= Long.parseLong(req.getParameter("usu_cod")); //string:obligatorio:si
     //paramLog 	= req.getParameter("login"); //string:obligatorio:no
     paramNombre 	= req.getParameter("nombre"); //string:obligatorio:no
     paramApellido 	= req.getParameter("paterno"); //string:obligatorio:no
     paramApellidoM = req.getParameter("materno"); //string:obligatorio:no
     paramClave 	= req.getParameter("clave"); //long:obligatorio:no
     paramEmail 	= req.getParameter("email"); //long:obligatorio:no
     paramEstado	= req.getParameter("sel_est"); //string:obligatorio:no
     //paramLocal 	= Long.parseLong(req.getParameter("sel_loc")); //long:obligatorio:no

     // 2.3 log de parametros y valores
     logger.debug("url: " 		+ paramUrl);
     logger.debug("usu_cod: " 	+ paramUsu_cod);
     //logger.debug("login: " 	+ paramLog);
     logger.debug("nombre: " 	+ paramNombre);
     logger.debug("apellido: " 	+ paramApellido);
     logger.debug("apellido_mat: " + paramApellidoM);
     logger.debug("clave: " 	+ paramClave);
     logger.debug("email: " 	+ paramEmail);
     logger.debug("sel_est: " 	+ paramEstado);
     //logger.debug("sel_loc:" 	+ paramLocal);
     
     /*
      * 3. Procesamiento Principal
      */
     	BizDelegate bizDelegate = new BizDelegate();
     	UserDTO usrdto = new UserDTO();

     	usrdto.setId_usuario(paramUsu_cod);
     	usrdto.setNombre(paramNombre);     	
     	usrdto.setApe_paterno(paramApellido);
     	usrdto.setApe_materno(paramApellidoM);
     	usrdto.setPassword(paramClave);
     	usrdto.setEmail(paramEmail);
     	usrdto.setEstado(paramEstado);
     	//usrdto.setId_local(paramLocal);

		bizDelegate.setModUser(usrdto);

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
