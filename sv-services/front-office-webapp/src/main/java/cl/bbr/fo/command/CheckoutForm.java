package cl.bbr.fo.command;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;

/**
 * Formulario checkout
 *  
 * @author BBR e-commerce & retail
 *  
 */
public class CheckoutForm extends Command {

	private static final long serialVersionUID = -1949448298030210933L;
	
	public static final String REDIRECT_URL_PAYMENT_GRABILITY = "REDIRECT_URL_PAYMENT_GRABILITY";
	public static final String REDIRECT_URL_SUCCESS = "REDIRECT_URL_SUCCESS";
	public static final String REDIRECT_URL_ERROR = "REDIRECT_URL_ERROR";

	public static final String REDIRECT_URL_ERROR_TBRAINS = "REDIRECT_URL_ERROR_TBRAINS"; //2b
	
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		HttpSession session  = null;
		try {			
			//ResourceBundle rb = ResourceBundle.getBundle("fo");
			session = arg0.getSession();
			BizDelegate biz = new BizDelegate();
			
			String nameIn =REDIRECT_URL_PAYMENT_GRABILITY+"', '"+REDIRECT_URL_SUCCESS+"', '"+REDIRECT_URL_ERROR;
			Map mapUrlParams =  biz.getParametroByNameIn(nameIn);
			
			//ParametroDTO paramUrlPago 		= (ParametroDTO)mapUrlParams.get(REDIRECT_URL_PAYMENT_GRABILITY);
			ParametroDTO paramUrlSuccess 	= (ParametroDTO)mapUrlParams.get(REDIRECT_URL_SUCCESS);
			ParametroDTO paramError 		= (ParametroDTO)mapUrlParams.get(REDIRECT_URL_ERROR);
			
			PedidoDTO pedido = (PedidoDTO)session.getAttribute("ses_grability_pedido");
			
			if(pedido != null){				
        		try{
        			sesionGrabilityPedido(pedido, biz, session);
		    	} catch (Exception e) {
		    		logger.error(e);
		    	}
	        	arg1.sendRedirect(paramError.getValor()+"?err=1");
	        	return;
	        	
        	}else{
        		logger.info("CheckoutForm - Venta masiva");
        		PedidoDTO pedido_tbrains = (PedidoDTO)session.getAttribute("ses_tbrains_pedido");
        		Map mapUrlParamsTBrains = (Map) session.getAttribute("paramUrlSuccessTbrains");
        		ParametroDTO paramErrorTBarins = (ParametroDTO)mapUrlParamsTBrains.get(REDIRECT_URL_ERROR_TBRAINS);
        		if(pedido_tbrains != null){
        			try{
        				sesionTwoBrainsPedido(pedido, biz, session);
        			} catch (Exception e) {
    		    		logger.error("Error (CheckoutForm) - Venta masiva:" +e);
    		    	}
        			arg1.sendRedirect(paramErrorTBarins.getValor()+"?err=1");
        			
        		}  else{
        			
        			arg1.sendRedirect("Pago");  
    	        	return;
        		}      		        		        		
	        }
			
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}finally{		
		}
		
	}
	
	
	private void sesionGrabilityPedido(PedidoDTO pedido, BizDelegate biz , HttpSession session)throws Exception{		
    	//Si pedido existe, limpiamos capacidades tomadas y se borra el pedido en estado 1
    	biz.purgaPedidoPreIngresado(pedido, pedido.getId_cliente());
    	session.removeAttribute("ses_grability_pedido");    	
	}

	private void sesionTwoBrainsPedido(PedidoDTO pedido, BizDelegate biz , HttpSession session)throws Exception{		
		//Si pedido existe, limpiamos capacidades tomadas y se borra el pedido en estado 1
    	biz.purgaPedidoPreIngresado(pedido, pedido.getId_cliente());
    	session.removeAttribute("ses_tbrains_pedido"); 
    	session.removeAttribute("paramUrlSuccessTbrains");
	}
}