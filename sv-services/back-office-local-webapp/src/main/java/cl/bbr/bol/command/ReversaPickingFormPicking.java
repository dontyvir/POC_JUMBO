package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.jumbocl.common.codes.Constantes;


/**
 * Comando que permite eliminar productos pickeados
 * @author BBR
 *
 */
public class ReversaPickingFormPicking extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		 // 1. seteo de Variables del método
	     String paramUrl 			="";
	     long	paramIdPick  		= -1;
	     long cant = 0;
	     int alerta = 0;
	     boolean result=false;
	     // 2. Procesa parámetros del request
	     logger.debug("Procesando parámetros...");

	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if ( req.getParameter("id_pick") == null ){throw new ParametroObligatorioException("id_pick es null");}
	 	
	        // 2.2 obtiene parametros desde el request
	     paramUrl 	= req.getParameter("url");
	     paramIdPick = Long.parseLong(req.getParameter("id_pick"));
	     
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("id_pick: " + paramIdPick);
	     
	     if (req.getParameter("alerta") != null){
	    	 alerta = Integer.parseInt(req.getParameter("alerta"));
	     }
	     
	     ForwardParameters fp = new ForwardParameters();
	     fp.add( req.getParameterMap() );
	     
		//obtiene mensajes
		String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito"); 
		String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
	     /*
	      * 3. Procesamiento Principal
	      */
		
	   	BizDelegate biz = new BizDelegate();
	   	try{ 	
	 
	 		if (alerta == 0){
		 		cant = biz.getCountFormPickRelacion(paramIdPick);
		 		
		 		if (cant <= 0){
			 		result = biz.doDelPickFormPicking(paramIdPick);
					logger.debug("result?"+result);
					if(result){
						paramUrl += "&msje=" +  mensaje_exito;
					}else{
						paramUrl += "&msje=" +  mensaje_fracaso;
					}
		 		}else{
		 			paramUrl += "&mns=Advertencia: Al reversar picking eliminará las relaciones de sustitución y de picking." +
		 					    " ¿Desea Reversar Picking?";
		 			paramUrl += "&id_pick="+paramIdPick;
		 		}
	 		}else{
	 			logger.debug("Entro acá, Debiese eliminar las relaciones");
	 			//Eliminar picking
	 			result=biz.doReversaPickingFormPick(paramIdPick);
	 			if(result){
					paramUrl += "&msje=" +  mensaje_exito;
				}else{
					paramUrl += "&msje=" +  mensaje_fracaso;
				}
	 		}
	 	}catch(BolException e){
	 		logger.debug("Controlando excepción: " + e.getMessage());
			String UrlError = getServletConfig().getInitParameter("UrlError");
			if ( e.getMessage()!= null && !e.getMessage().equals("") ){
				logger.debug(e.getMessage());
				fp.add( "rc" ,Constantes._EX_VE_FPIK_NO_REVERSA_PICK );
				fp.add( "id_pick" , String.valueOf(paramIdPick) );
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
	 	}
	 

	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);

	 }
	
	
}
