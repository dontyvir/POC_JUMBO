package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.ProcModUserDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que permite agregar o eliminar la relacion entre usuario y local
 * @author bbr
 *
 */
public class ModLocUser extends Command {
	private final static long serialVersionUID = 1;
 
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		// 1. seteo de Variables del método
	    String paramUrl = "";
	    long paramId_user = 0L;
	    long paramId_loc = 0L;
	    String paramAction = "";
	     
	    String UrlError = getServletConfig().getInitParameter("UrlError");
	    
	    // 2. Procesa parámetros del request
	
	    logger.debug("Procesando parámetros...");
	
	    // 2.1 revision de parametros obligatorios
	    if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	    if ( req.getParameter("usu_cod") == null ){throw new ParametroObligatorioException("usu_cod es nulo");}
	    if ( req.getParameter("loc_id") == null ){throw new ParametroObligatorioException("loc_id es nulo");}
	    if ( req.getParameter("accion") == null ){throw new ParametroObligatorioException("accion es nulo");}
	
	    // 2.2 obtiene parametros desde el request
	    paramUrl = req.getParameter("url");
	    paramId_user = Long.parseLong(req.getParameter("usu_cod")); //long:obligatorio:si
	    paramId_loc = Long.parseLong(req.getParameter("loc_id")); //long:obligatorio:si
	    paramAction = req.getParameter("accion"); //string:obligatorio:si
		     // 2.3 log de parametros y valores
	    logger.debug("url: " + paramUrl);
	    logger.debug("usu_cod: " + paramId_user);
	    logger.debug("per_id: " + paramId_loc);
	    logger.debug("action: " + paramAction);

	     /*
	      * 3. Procesamiento Principal
	      */
	    BizDelegate biz = new BizDelegate();
	    
	    ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
	     
	    ProcModUserDTO prm = new ProcModUserDTO();
	    prm.setId_usuario(paramId_user);
	    prm.setId_local(paramId_loc);
	    prm.setAccion(paramAction);
	     
	    try{
	    	 
	    	boolean resb = biz.setModLocUser(prm);
			logger.debug("resultado:"+resb);
	    	 
	     }catch(BocException e){
	    	logger.debug("Controlando excepción del "+this.getServletName()+ ":" + e.getMessage());
	    	fp.add("usr_cod",paramId_user+"");
	    	if (  e.getMessage().equals(Constantes._EX_USR_ID_NO_EXISTE) ){
				logger.debug("El código del usuario no existe");
				fp.add( "rc" , Constantes._EX_USR_ID_NO_EXISTE );
				fp.add( "msg" , "El código del usuario no existe" );
				paramUrl = UrlError + fp.forward();
			} if (  e.getMessage().equals(Constantes._EX_USR_LOC_EXISTE) ){
				logger.debug("La relacion entre usuario y local si existe");
				fp.add( "rc" , Constantes._EX_USR_LOC_EXISTE );
				fp.add( "msg" , "La relacion entre usuario y local si existe" );
				paramUrl = UrlError + fp.forward();
			}  if (  e.getMessage().equals(Constantes._EX_USR_LOC_NO_EXISTE) ){
				logger.debug("La relacion entre usuario y local no existe");
				fp.add( "rc" , Constantes._EX_USR_LOC_NO_EXISTE );
				fp.add( "msg" , "La relacion entre usuario y local no existe" );
				paramUrl = UrlError + fp.forward();
			} else {
				logger.debug("Controlando excepción: " + e.getMessage());
			}
	     }

			
				
			
	     
	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);
	
	}//execute

}
