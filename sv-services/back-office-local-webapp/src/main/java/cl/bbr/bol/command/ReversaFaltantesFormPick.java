package cl.bbr.bol.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPedDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que permite reversar productos declarados como faltante, para que éstos puedan ser relacionados.
 * @author BBR
 *
 */

public class ReversaFaltantesFormPick extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		 // 1. seteo de Variables del método
		 boolean result = false;
	     String paramUrl 	  ="";
	     long	paramIdDet    = -1;
	     double paramCantFalt =0;
	     long   paramIdRonda  =-1;
	     // 2. Procesa parámetros del request
	     logger.debug("Procesando parámetros...");

	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if ( req.getParameter("id_det") == null ){throw new ParametroObligatorioException("id_det es null");}
	     if ( req.getParameter("cant_falt") == null ){throw new ParametroObligatorioException("cant_falt es null");}
	     if ( req.getParameter("id_ronda") == null ){throw new ParametroObligatorioException("id_ronda es null");}
	     
	        // 2.2 obtiene parametros desde el request
	     paramUrl 	   = req.getParameter("url");
	     paramIdDet    = Long.parseLong(req.getParameter("id_det"));
	     paramCantFalt = Double.parseDouble(req.getParameter("cant_falt"));
	     paramIdRonda  = Long.parseLong(req.getParameter("id_ronda"));
	     
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("id_det: " + paramIdDet);
	     logger.debug("paramCantFalt: " + paramCantFalt);
	     logger.debug("paramIdRonda: " + paramIdRonda);

	     
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
//	   			Obtenemos el detalle del producto
				//la cantidad faltante debe disminuir y la cantidad sin pickear aumentar
				double cant_falt_act = 0;
				double cant_spick_act = 0;
				double cant_pick=0;
				int mot_sust=0;
				ProcFormPickDetPedDTO dp = new ProcFormPickDetPedDTO();
				dp.setId_detalle(paramIdDet);
				dp.setPor_idDet(true);
				dp.setId_ronda(paramIdRonda);
				
				List listProdRonda = biz.getProductosCbarraRonda(dp);
				for (int i = 0; i < listProdRonda.size(); i++) {	
					ProductosPedidoDTO pr = (ProductosPedidoDTO)listProdRonda.get(i);
					cant_pick = pr.getCant_pick();
					cant_falt_act = pr.getCant_faltan() -  paramCantFalt;
					cant_spick_act = pr.getCant_spick() + paramCantFalt;
					mot_sust = pr.getMot_sustitucion();
					
				}	 			
	 			
	   			
	 		ProductosPedidoDTO datos_ped = new ProductosPedidoDTO();
	 		datos_ped.setCant_faltan(cant_falt_act);
	 		datos_ped.setId_detalle(paramIdDet);
	 		datos_ped.setCant_spick(cant_spick_act);
	 		datos_ped.setCant_pick(cant_pick);
	 		datos_ped.setMot_sustitucion(mot_sust);

	 		 result = biz.setActFormPickDetPed(datos_ped);
	 		 if (result)
	 			 paramUrl = paramUrl + "&msje=" +mensaje_exito;
	 		 else
	 			paramUrl = paramUrl + "&msje=" +mensaje_fracaso;
			
	 	}catch(BolException e){
	 		logger.debug("Controlando excepción: " + e.getMessage());
			String UrlError = getServletConfig().getInitParameter("UrlError");
			if ( e.getMessage()!= null && !e.getMessage().equals("") ){
				logger.debug(e.getMessage());
				fp.add( "msje" , e.getMessage() );
				fp.add( "id_det" , String.valueOf(paramIdDet) );
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
	 	}
	 

	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);

	 }
	
	
}
