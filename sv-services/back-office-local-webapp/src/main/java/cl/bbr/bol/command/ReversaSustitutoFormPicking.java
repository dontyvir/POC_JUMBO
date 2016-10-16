package cl.bbr.bol.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPedDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPickDTO;
import cl.bbr.jumbocl.pedidos.dto.DetalleFormPickingDTO;
import cl.bbr.jumbocl.pedidos.dto.FPickDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Comando que permite reversar la sustitución de un producto.
 * @author BBR
 *
 */
public class ReversaSustitutoFormPicking extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		 // 1. seteo de Variables del método
		 boolean result = false;
	     String paramUrl   	 ="";
	     long	paramIdRow   = -1;
	     double	paramCantPed = 0;
	     double	paramCantSust= 0;
	     long	paramIdFpick = -1;
	     // 2. Procesa parámetros del request
	     logger.debug("Procesando parámetros...");

	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if ( req.getParameter("id_row") == null ){throw new ParametroObligatorioException("id_row es null");}
	     if ( req.getParameter("cant_relped") == null ){throw new ParametroObligatorioException("cant_ped es null");}
	     if ( req.getParameter("cant_sust") == null ){throw new ParametroObligatorioException("cant_sust es null");}
	     if ( req.getParameter("id_fpick") == null ){throw new ParametroObligatorioException("id_fpick es null");}
	     
	        // 2.2 obtiene parametros desde el request
	     paramUrl 	   = req.getParameter("url");
	     paramIdRow    = Long.parseLong(req.getParameter("id_row"));
	     paramCantPed  = Double.parseDouble(req.getParameter("cant_relped"));
	     paramCantSust = Double.parseDouble(req.getParameter("cant_sust"));
	     paramIdFpick  = Long.parseLong(req.getParameter("id_fpick"));
	     
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("id_row: " + paramIdRow);
	     logger.debug("cant_relped: " + paramCantPed);
	     logger.debug("cant_sust: " + paramCantSust);
	     logger.debug("id_fpick: " + paramIdFpick);

	     
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
	 		long id_det = -1;
	 		long id_ronda = -1;
	 		
	 		// Actualizamos las cantidades en el detalle de pedido (bo_fpickdetped)
	 		DetalleFormPickingDTO prod = new DetalleFormPickingDTO();
	 		prod = biz.getRelacionFormPickById(paramIdRow);
	 		id_det= prod.getId_detalle();
	 		id_ronda = prod.getId_ronda();
	 		
	 		//Obtenemos las cantidades actuales
	 		ProcFormPickDetPedDTO dp = new ProcFormPickDetPedDTO();
			dp.setId_detalle(id_det);
			dp.setId_ronda(id_ronda);
			dp.setPor_idDet(true);
			
			double cant_pick_act=0;
			double cant_spick_act=0;
			double cant_faltante=0;
			List listProdRonda = biz.getProductosCbarraRonda(dp);
			for (int i = 0; i < listProdRonda.size(); i++) {	
				ProductosPedidoDTO pr = (ProductosPedidoDTO)listProdRonda.get(i);	
				cant_faltante = pr.getCant_faltan();
				//Disminuimos la cantidad pickeada
				cant_pick_act = pr.getCant_pick() - paramCantPed;
				//Aumentamos la cantidad sin pickear
				cant_spick_act = pr.getCant_spick() + paramCantPed;
				
			}
			ProductosPedidoDTO datos_ped = new ProductosPedidoDTO();
			datos_ped.setCant_pick(cant_pick_act);
			datos_ped.setCant_spick(cant_spick_act);
			datos_ped.setMot_sustitucion(0);
			datos_ped.setSust_cformato("");
			datos_ped.setId_detalle(id_det);
			datos_ped.setCant_faltan(cant_faltante);
			logger.debug("id_det: " + id_det);
			
			//Actualizamos las cantidades pendientes en el picking (bo_fpick)
			
			//Obtenemos la cantidad pendiente actual
			ProcFormPickDetPickDTO pk = new ProcFormPickDetPickDTO();
			pk.setId_ronda(id_ronda);	
			pk.setPor_idpick(true);
			pk.setId_pick(paramIdFpick);
			
			List listadoPick = biz.getDetallePickFormPick(pk);
			double cant_pend = 0;
			for (int i = 0; i < listadoPick.size(); i++) {
				FPickDTO pick = (FPickDTO)listadoPick.get(i);
				//Aumentamos la cantidad pendiente
				cant_pend = pick.getCant_pend() + paramCantSust;
			}
			
	 		FPickDTO fpick = new FPickDTO();
			fpick.setId_fpick(paramIdFpick);
			fpick.setCant_pend(cant_pend);
	 		result= biz.doReversaSustitutoFormPick(datos_ped,fpick,paramIdRow);
			logger.debug("result?"+result);
			if(result){
				paramUrl += "&rc=" +  mensaje_exito;
			}else{
				paramUrl += "&rc=" +  mensaje_fracaso;
			}
			
	 	}catch(BolException e){
	 		logger.debug("Controlando excepción: " + e.getMessage());
			String UrlError = getServletConfig().getInitParameter("UrlError");
			if (e.getMessage().equals(Constantes._EX_VE_FPIK_NO_EXISTE_RELACION)){
				logger.debug(e.getMessage());
				fp.add( "rc" , Constantes._EX_VE_FPIK_NO_EXISTE_RELACION);
				fp.add( "id_row" , String.valueOf(paramIdRow) );
				paramUrl = UrlError + fp.forward(); 
			}
			else{
				logger.debug(e.getMessage());
				fp.add( "rc" , Constantes._EX_VE_FPIK_NO_REVERSA_SUST);
				fp.add( "id_row" , String.valueOf(paramIdRow) );
				paramUrl = UrlError + fp.forward(); 
			} 
	 	}
	 

	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);

	 }
	
	
}
