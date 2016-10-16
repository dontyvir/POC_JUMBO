package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModCampElementoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que permite modificar la relacion entre campana y producto
 * @author bbr
 *
 */
public class ModCampElemento extends Command {
	private final static long serialVersionUID = 1;
 

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	     
	     // 1. seteo de Variables del método
	     String paramUrl = "";
	     long paramId_elemento=0L;
	     long paramId_campana=0L;
	     String paramAccion="";
	     String paramOrigen="";
	     long paramClicks=0L;
	     
	     // 2. Procesa parámetros del request
	     logger.debug("Procesando parámetros...");
	
	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es nulo");}
	     if ( req.getParameter("id_campana") == null ){throw new ParametroObligatorioException("id_campana es nulo");}
	     if ( req.getParameter("id_elemento") == null ){throw new ParametroObligatorioException("id_elemento es nulo");}
	     if ( req.getParameter("origen") == null ){throw new ParametroObligatorioException("origen es nulo");}
	     if ( req.getParameter("accion") == null ){throw new ParametroObligatorioException("accion es nulo");}
	     if ( req.getParameter("accion").equals("modificar")){
	    	 if ( req.getParameter("clicks")== null){
	    	 throw new ParametroObligatorioException("clicks es nulo");}
	     }
	
	     // 2.2 obtiene parametros desde el request
	     paramUrl = req.getParameter("url");
	     paramId_campana = Long.parseLong(req.getParameter("id_campana")); //long:obligatorio:si
	     paramId_elemento = Long.parseLong(req.getParameter("id_elemento"));
	     paramOrigen = req.getParameter("origen");
	     paramAccion = req.getParameter("accion");
	     
	     if ( paramAccion.equals("modificar")){
	    	 paramClicks = Long.parseLong(req.getParameter("clicks"));
	     }
	     
	
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("id_elemento" + paramId_elemento);
	     logger.debug("id_campana: " + paramId_campana);
	     logger.debug("origen: " + paramOrigen);
	     logger.debug("accion: " + paramAccion);

	     ForwardParameters fp = new ForwardParameters();
		 fp.add( req.getParameterMap() );
		 
	     /*
	      * 3. Procesamiento Principal
	      */
	     BizDelegate biz = new BizDelegate();
    	 String UrlError = "";
    	 if(paramOrigen.equals("CAMP")) UrlError = getServletConfig().getInitParameter("UrlErrorCamp");
    	 if(paramOrigen.equals("ELEM")) UrlError = getServletConfig().getInitParameter("UrlErrorElem");

	     try{
			 ProcModCampElementoDTO param = new ProcModCampElementoDTO();
			 //paramId_campana, paramId_elemento,usr.getLogin(), mensDesa
			 param.setId_campana(paramId_campana);
			 param.setId_elemento(paramId_elemento);
			 param.setAccion(paramAccion);
			 param.setClicks(paramClicks);
			 param.setMensaje("");
			 
			 boolean result = biz.setModCampanaElemento(param);
			 logger.debug("result:"+result);

	     }catch(BocException e) {
	    	 logger.debug("Controlando excepción del ModCampElemento: " + e.getMessage());
	    	 if ( e.getMessage().equals(Constantes._EX_CAM_ID_NO_EXISTE) ){
					logger.debug("El código de campaña ingresado no existe");
					fp.add( "rc" , Constantes._EX_CAM_ID_NO_EXISTE );
					paramUrl = UrlError + fp.forward(); 
			 } 
	    	 if ( e.getMessage().equals(Constantes._EX_ELM_ID_NO_EXISTE) ){
					logger.debug("El código del elemento no existe");
					fp.add( "rc" , Constantes._EX_ELM_ID_NO_EXISTE );
					paramUrl = UrlError + fp.forward(); 
			 } 
			 if ( e.getMessage().equals(Constantes._EX_CAM_ELEM_REL_EXISTE) ){
					logger.debug("La relación entre campaña y elemento existe. No se puede agregar.");
					fp.add( "rc" , Constantes._EX_CAM_ELEM_REL_EXISTE );
					paramUrl = UrlError + fp.forward(); 
			 }
			 if ( e.getMessage().equals(Constantes._EX_CAM_ELEM_REL_NO_EXISTE) ){
					logger.debug("La relación entre campaña y elemento no existe. No se puede eliminar.");
					fp.add( "rc" , Constantes._EX_CAM_ELEM_REL_NO_EXISTE );
					paramUrl = UrlError + fp.forward(); 
			 }
    		 else {
					logger.debug("Controlando excepción: " + e.getMessage());
			 }
	    	 
	     }
	
	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);
	
	}//execute

}
