package cl.bbr.bol.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPedDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPickDTO;
import cl.bbr.jumbocl.pedidos.dto.DetalleFormPickingDTO;
import cl.bbr.jumbocl.pedidos.dto.FPickDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que permite hacer relacion manualmente entre el detalle de pedido y los productos pickeados 
 * 
 * @author BBR
 *
 */


public class RelacionFormPicking extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		 // 1. seteo de Variables del método
	     String paramUrl 			="";
	     double paramCantPedRel		= 0;
	     double paramCantPickRel    = 0;
	     String	paramDetalle 		= "";
	     String	paramDetPick 		= "";
	     String paramTipoRel		= "";
	     long   paramIdRonda        = -1;
	     int    paramMotSust 		= 0;
	     long id_det = -1;
	     double cant_spick = 0;
	     double cant_spick_act = 0;
	     long id_pick =0;
	     double cant_pick_pend=0;
	     
	     
	     
	     
	     String cambio_formato = "";
	     
	     // 2. Procesa parámetros del request
	     logger.debug("Procesando parámetros...");

	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if ( req.getParameter("id_ronda") == null ){throw new ParametroObligatorioException("id_ronda es null");}
	     if ( req.getParameter("pend_ped") == null ){throw new ParametroObligatorioException("pend_ped es null");}
	     if ( req.getParameter("tipo_relacion") == null ){throw new ParametroObligatorioException("tipo_relacion es null");}
	     if ( req.getParameter("cant_ped_rel") == null ){throw new ParametroObligatorioException("cant_ped_rel es null");}
	    // if ( req.getParameter("cant_pick_rel") == null ){throw new ParametroObligatorioException("cant_pick_rel es null");}
	     
	     
	     
	     
	        // 2.2 obtiene parametros desde el request
	     paramUrl 	     = req.getParameter("url");
	     paramDetalle    = req.getParameter("pend_ped");
	     paramDetPick    = req.getParameter("pend_pick");
	     paramIdRonda    = Long.parseLong(req.getParameter("id_ronda"));
	     
	     if (req.getParameter("cant_ped_rel")!= null && !req.getParameter("cant_ped_rel").equals(""))
	    	 paramCantPedRel = Double.parseDouble(req.getParameter("cant_ped_rel"));
	     if (req.getParameter("cant_pick_rel")!= null && !req.getParameter("cant_pick_rel").equals(""))
	    	 paramCantPickRel= Double.parseDouble(req.getParameter("cant_pick_rel"));

	     paramTipoRel   = req.getParameter("tipo_relacion");
	     if (req.getParameter("tipo_sust") != null && !req.getParameter("tipo_sust").equals("") ){
	    	 paramMotSust   = Integer.parseInt(req.getParameter("tipo_sust"));
	     }
	     
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("pend_ped: " + paramDetalle);
	     logger.debug("pend_pick: " + paramDetPick);
	     logger.debug("paramIdRonda: " + paramIdRonda);
	     
	     
	     ForwardParameters fp = new ForwardParameters();
	     fp.add( req.getParameterMap() );
	     
	     /*
	      * 3. Procesamiento Principal
	      */
	   	BizDelegate biz = new BizDelegate();
	     	
	 	try{
	 		
	 		String[] det = paramDetalle.split("-");
	 		id_det = Long.parseLong(det[0]);
	 		logger.debug("id_det: " + id_det );
	 		cant_spick = Double.parseDouble(det[1]);
	 		logger.debug("cant sin pickear: " + cant_spick);
	 		
	 		if (paramTipoRel.toUpperCase().equals("CAMBIOFORMATO"))
	 			cambio_formato = "S";
	 		else
	 			cambio_formato = "N";
	 		
	 		
	 		cant_spick_act = cant_spick - paramCantPedRel;
	 		
	 		ProductosPedidoDTO datos_ped = new ProductosPedidoDTO();
	 		datos_ped.setId_detalle(id_det);
	 		datos_ped.setCant_spick(cant_spick_act);
	 		datos_ped.setMot_sustitucion(paramMotSust);
	 		datos_ped.setSust_cformato(cambio_formato);
	 		datos_ped.setId_ronda(paramIdRonda);
	 		if (!paramTipoRel.toUpperCase().equals("FALTANTE")){
	 			//Sólo si el tipo de relacion es distinto a faltante se modifica la cantidad pickeada
	 			String pesable="";
	 			long id_dronda=-1;
	 			double cant_pick_act = 0;
				ProcFormPickDetPedDTO dp = new ProcFormPickDetPedDTO();
				dp.setId_detalle(datos_ped.getId_detalle());
				dp.setId_ronda(datos_ped.getId_ronda());
				dp.setPor_idDet(true);
				dp.setSin_pickear(true);
				
				List listProdRonda = biz.getProductosCbarraRonda(dp);
				for (int i = 0; i < listProdRonda.size(); i++) {	
					ProductosPedidoDTO pr = (ProductosPedidoDTO)listProdRonda.get(i);	
					cant_pick_act = pr.getCant_pick() + paramCantPedRel;
					pesable = pr.getPesable();
					id_dronda = pr.getId_dronda();
					
				}
				datos_ped.setCant_pick(cant_pick_act);
				
		 		String[] pick = paramDetPick.split("-");
		 		logger.debug("id_pick: " + pick[0]);
		 		id_pick = Long.parseLong(pick[0]);
		 		cant_pick_pend = Double.parseDouble(pick[1]);
		 		
		 		
		 		
		 		cant_pick_pend = cant_pick_pend - paramCantPickRel;
		 		FPickDTO fpick = new FPickDTO();
				fpick.setId_fpick(id_pick);
				fpick.setCant_pend(cant_pick_pend);
		 		
				
				
				ProcFormPickDetPickDTO pk = new ProcFormPickDetPickDTO();
				pk.setId_ronda(paramIdRonda);	
				pk.setPendientes(true);
				pk.setPor_idpick(true);
				pk.setId_pick(id_pick);
				logger.debug("pip: " + pk.isPor_idpick() );
				List listadoPick = biz.getDetallePickFormPick(pk);
				logger.debug("id_pick: " +fpick.getId_fpick() );
				String cbarra = "";
				long id_bin = -1;
				for (int i = 0; i < listadoPick.size(); i++) {
					
					FPickDTO prod = (FPickDTO)listadoPick.get(i);
					cbarra = prod.getCbarra();
					id_bin = prod.getId_form_bin();
					

				}
		 		DetalleFormPickingDTO datos = new DetalleFormPickingDTO();
	
		 		if (paramTipoRel.toUpperCase().equals("PICK_NORMAL")){
		 			datos.setTipo(Constantes.DET_PICK_NORMAL);
		 		}else{
		 			datos.setTipo(Constantes.DET_PICK_SUST);
		 		}

		 		
		 		datos.setId_detalle(id_det);
		 		datos.setCantidad(paramCantPickRel);
		 		datos.setId_ronda(paramIdRonda);
		 		datos.setMot_sust(paramMotSust);
		 		datos.setCBarra(cbarra);
				datos.setId_det_ronda(id_dronda);
				datos.setPesable(pesable);
				datos.setId_form_bin(id_bin);
		 		datos.setId_fpick(id_pick);
		 		datos.setCant_rel_ped(paramCantPedRel);
				
		 		//Se relaciona el detalle pedido con el detalle de picking
		 		biz.doRelacionFormPick(datos, datos_ped, fpick);
	 		}else{
	 			
	 			datos_ped.setCant_pick(0);
	 			
				//	Obtenemos el detalle del producto
				//la cantidad faltante debe incrementarse
				double cant_falt_act = 0;
				double cant_pick=0;
				ProcFormPickDetPedDTO dp = new ProcFormPickDetPedDTO();
				dp.setId_detalle(datos_ped.getId_detalle());
				dp.setPor_idDet(true);
				dp.setSin_pickear(true);
				dp.setId_ronda(datos_ped.getId_ronda());
				
				List listProdRonda = biz.getProductosCbarraRonda(dp);
				for (int i = 0; i < listProdRonda.size(); i++) {	
					ProductosPedidoDTO pr = (ProductosPedidoDTO)listProdRonda.get(i);	
					cant_falt_act = pr.getCant_faltan()+ paramCantPedRel;
					logger.debug("cant_falt_act: "+cant_falt_act);
					cant_pick = pr.getCant_pick();
					
					
				}	 			
				datos_ped.setCant_pick(cant_pick);
				datos_ped.setCant_faltan(cant_falt_act);
	 			biz.setActFormPickDetPed(datos_ped);
	 		}

	 	}catch(BolException e){
	 		logger.debug("Controlando excepción: " + e.getMessage());
			String UrlError = getServletConfig().getInitParameter("UrlError");
			if ( e.getMessage().equals(Constantes._EX_VE_FPIK_ID_DETALLE_INVALIDO)){
				logger.debug("Id Detalle invalido");
				fp.add( "rc" , Constantes._EX_VE_FPIK_ID_DETALLE_INVALIDO );
				paramUrl = UrlError + fp.forward();	
			}
			else{
				logger.debug(e.getMessage());
				fp.add( "rc" , Constantes._EX_VE_FPIK_NO_REALIZO_RELACION );
				paramUrl = UrlError + fp.forward(); 
			} 
	 	}
	 

	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);

	 }
	
	
}
