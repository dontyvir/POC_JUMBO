package cl.cencosud.jumbo.ctrl;

import org.apache.commons.lang.StringUtils;

import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.pedidos.dto.PagoGrabilityDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantPayment;
import cl.cencosud.jumbo.bizdelegate.BizDelegatePayment;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.Payment.PostInputPaymentDTO;
import cl.cencosud.jumbo.output.dto.Payment.PostOutputPaymentDTO;

public class CtrlPayment extends Ctrl{
	
	private PostInputPaymentDTO postPaymentDTO;
	
	/**
	* Constructor para asignar el objeto del tipo PostInputClientDTO.
	*/
	public CtrlPayment(PostInputPaymentDTO oPostPaymentDTO) {
		super();
		this.postPaymentDTO = oPostPaymentDTO;
	}

	public PostOutputPaymentDTO postPayment() throws GrabilityException{
		
		PostOutputPaymentDTO outputDTO = new PostOutputPaymentDTO();
		BizDelegatePayment biz = new BizDelegatePayment();
		
		PedidoDTO pedido = biz.getPedido( postPaymentDTO.getOrder_id());
		
		if (pedido.getId_estado() == ConstantPayment.ESTADO_PEDIDO_PRE_INGRESADO  && StringUtils.equals(pedido.getDispositivo(), Constant.SOURCE)) {
			
			//Date d = new Date();			
			String tokenValidacionPagoWeb = ""+postPaymentDTO.getOrder_id()+System.currentTimeMillis();
			
			try{
				
				tokenValidacionPagoWeb=Utils.encriptarFO(tokenValidacionPagoWeb);
				if(tokenValidacionPagoWeb.length() > 250 )
					tokenValidacionPagoWeb = (tokenValidacionPagoWeb).substring(0,250).trim();
				
			} catch (Exception e) {
				throw new GrabilityException(ConstantPayment.SC_ERROR_CREATE_TOKEN_PAYMENT, ConstantPayment.MSG_ERROR_CREATE_TOKEN_PAYMENT, e);
			}
			
			String uri = "?id_pedido="+postPaymentDTO.getOrder_id()+"&"+System.currentTimeMillis();
			
			
			//
			if (StringUtils.equalsIgnoreCase(pedido.getMedio_pago(), Constant.CAT)){
								
				ParametroDTO oRedirectUrl =  biz.getParametroByName(ConstantPayment.REDIRECT_URL_PAYMENT_GRABILITY);
				ParametroDTO oSuccessUrl =  biz.getParametroByName(ConstantPayment.REDIRECT_URL_SUCCESS_CAT);
				ParametroDTO oErrorUrl =  biz.getParametroByName(ConstantPayment.REDIRECT_URL_ERROR_CAT);
				
				PagoGrabilityDTO oPagoGrabilityDTO= new PagoGrabilityDTO();
				oPagoGrabilityDTO.setIdPedido(postPaymentDTO.getOrder_id());
				oPagoGrabilityDTO.setTokenPago(tokenValidacionPagoWeb);
				
				biz.insertRegistroPago(oPagoGrabilityDTO);
				
				outputDTO.setStatus(Constant.SC_OK);
				outputDTO.setError_message(Constant.MSG_OK);
				outputDTO.setRedirect_url(oRedirectUrl.getValor()+uri);
				outputDTO.setSuccess_url(oSuccessUrl.getValor());
				outputDTO.setError_url(oErrorUrl.getValor());
				outputDTO.setToken(tokenValidacionPagoWeb);
				
			} else if (StringUtils.equalsIgnoreCase(pedido.getMedio_pago(), Constant.TBK)){
				
				ParametroDTO oRedirectUrl =  biz.getParametroByName(ConstantPayment.REDIRECT_URL_PAYMENT_GRABILITY);
				ParametroDTO oSuccessUrl =  biz.getParametroByName(ConstantPayment.REDIRECT_URL_SUCCESS_TBK);
				ParametroDTO oErrorUrl =  biz.getParametroByName(ConstantPayment.REDIRECT_URL_ERROR_TBK);
					
				PagoGrabilityDTO oPagoGrabilityDTO= new PagoGrabilityDTO();
				oPagoGrabilityDTO.setIdPedido(postPaymentDTO.getOrder_id());
				oPagoGrabilityDTO.setTokenPago(tokenValidacionPagoWeb);
				
				biz.insertRegistroPago(oPagoGrabilityDTO);
				
				outputDTO.setStatus(Constant.SC_OK);
				outputDTO.setError_message(Constant.MSG_OK);
				outputDTO.setRedirect_url(oRedirectUrl.getValor()+uri);
				outputDTO.setSuccess_url(oSuccessUrl.getValor());
				outputDTO.setError_url(oErrorUrl.getValor());
				outputDTO.setToken(tokenValidacionPagoWeb);
				
			} else {
				throw new GrabilityException(ConstantPayment.SC_MEDIO_PAGO_INVALIDO, ConstantPayment.MSG_MEDIO_PAGO_INVALIDO);
			}
			
		}else{
			throw new GrabilityException(ConstantPayment.SC_ESTADO_PEDIDO_NO_VALIDO_PAGAR, ConstantPayment.MSG_ESTADO_PEDIDO_NO_VALIDO_PAGAR);
		}
		
		return outputDTO;		
	}
}
