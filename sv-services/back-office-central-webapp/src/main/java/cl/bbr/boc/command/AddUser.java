package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.jumbo.ldap.beans.UsuarioLDAP;
import cl.jumbo.ldap.process.UsersLDAP;


/**
 * agrega un usuario
 * @author BBRI
 */

public class AddUser extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl 		= "";
     String paramLogin		= "";
     String paramNombre		= "";
     String paramApellido	= "";
     String paramApellidoM	= "";
     String paramClave		= "";
     String paramEstado		= "";
     //long paramLocal		= 0L;
     String paramEmail		= "";
     String paramFuente 	= "";

	// 1.1 Parámetros de inicialización servlet
	// Recupera pagina desde web.xml
	String UrlError = getServletConfig().getInitParameter("UrlError");
	logger.debug("UrlError: " + UrlError);

     
     
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") 		== null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("fuente") 	== null ){throw new ParametroObligatorioException("fuente es null");}
   	 if ( req.getParameter("login") 	== null ){throw new ParametroObligatorioException("login es null");}
     if ( req.getParameter("estado")	== null ){throw new ParametroObligatorioException("estado es null");}
     //if ( req.getParameter("local") 	== null ){throw new ParametroObligatorioException("local es null");}
     
     // obligatorios en caso de ser formulario ldap
     if ( req.getParameter("fuente").equals("ldap") ){
     }
     else if ( req.getParameter("fuente").equals("nuevo") ){
         if ( req.getParameter("nombre") == null ){throw new ParametroObligatorioException("nombre es null");}
         if ( req.getParameter("apellido") == null ){throw new ParametroObligatorioException("apellido es null");}
         if ( req.getParameter("clave") == null ){throw new ParametroObligatorioException("clave es null");}
         if ( req.getParameter("email") == null ){throw new ParametroObligatorioException("email es null");}
     }
     else{
    	 throw new ParametroObligatorioException("parametro fuente tiene un valor fuera de lo esperado");
     }
 
     
     // 2.2 obtiene parametros desde el request
     paramUrl 		= req.getParameter("url");
     paramLogin 	= req.getParameter("login");
     paramFuente 	= req.getParameter("fuente");
     paramEstado 	= req.getParameter("estado");
     //paramLocal 	= Long.parseLong(req.getParameter("local"));
     
     if ( paramFuente.equals("nuevo") ){
	     paramNombre 	= req.getParameter("nombre");
	     paramApellido 	= req.getParameter("apellido");
	     paramApellidoM = req.getParameter("apellido_mat");
	     paramClave 	= req.getParameter("clave");
	     paramEmail 	= req.getParameter("email");
     }

     // trae los usuarios ldap
     if (paramFuente.equals("ldap")){
    	 
    	 UsersLDAP a = new UsersLDAP();
    	 
    	 UsuarioLDAP ldap_usr = a.getInfoUsuarioByLogin( paramLogin );
    	  	 
 	     paramNombre 	= ldap_usr.getNombre();
 	     paramApellido 	= ldap_usr.getApellido();
 	     paramApellidoM = "-";
 	     paramClave 	= "1234567890";
 	     paramEmail 	= ldap_usr.getEmail();
     	 
     }
     
     
     // 2.3 log de parametros y valores
     logger.debug("url: " 		+ paramUrl);
     logger.debug("login: " 	+ paramLogin);
     logger.debug("nombre: " 	+ paramNombre);
     logger.debug("apellido: " 	+ paramApellido);
     logger.debug("apellido_mat: " + paramApellidoM);
     logger.debug("clave: " 	+ paramClave);
     logger.debug("estado: " 	+ paramEstado);
     //logger.debug("local: " 	+ paramLocal);
     logger.debug("email: " 	+ paramEmail);
     
     /*
      * 3. Procesamiento Principal
      */
	BizDelegate biz = new BizDelegate();
	UserDTO prm = new UserDTO();
	//prm.setId_local(paramLocal);
	prm.setLogin(paramLogin);
	prm.setNombre(paramNombre);
	prm.setApe_paterno(paramApellido);
	prm.setApe_materno(paramApellidoM);
	prm.setPassword(paramClave);
	prm.setEstado(paramEstado); 
	prm.setEmail(paramEmail);

	ForwardParameters fp = new ForwardParameters();
	fp.add( req.getParameterMap() );
	long id_usuario = 0;

	try {
		id_usuario = biz.addUser(prm);
		logger.debug("Se insertó usuario: " + id_usuario); 
		fp.add( "usr_cod" , id_usuario+"" );
		paramUrl = paramUrl + fp.forward();
		
	} catch (BocException e) {
		logger.debug("Controlando excepción: " + e.getMessage());
		if ( e.getMessage().equals(Constantes._EX_USR_LOGIN_DUPLICADO) ){
			logger.debug("El login ya existe en el sistema");
			fp.add( "rc" , Constantes._EX_USR_LOGIN_DUPLICADO );
			paramUrl = UrlError + fp.forward();
		}
		
	}
	
  		
     // 4. Redirecciona salida
	logger.debug("Redireccionando a: " + paramUrl);
	res.sendRedirect(paramUrl);

 }//execute

}
