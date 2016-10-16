package cl.cencosud.jumbo.input.dto.Payment;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.Constant.ConstantPayment;
import cl.cencosud.jumbo.Constant.ConstantPurchaseOrder;
import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class PostInputPaymentDTO extends InputDTO implements Serializable {

	private static final long serialVersionUID = -682706456308810875L;
	
	private int order_id;//"user identifier",
	private String type;//"alphanumeric value representing the origin of the request"

	public PostInputPaymentDTO(HttpServletRequest request) throws ExceptionInParam, GrabilityException{
		
		super(request,ConstantPayment.INTEGRATION_CODE_CREATE_PAYMENT,null);
		
		this.order_id =  getParamInt(request, ConstantPurchaseOrder.ORDER_ID);
		this.type = getParamString(request, Constant.TYPE);;
	}

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void isValid() throws ExceptionInParam {
		
		if (getOrder_id() == 0)
			throw new ExceptionInParam(ConstantPayment.SC_ESTADO_PEDIDO_NO_VALIDO_PAGAR, ConstantPayment.MSG_ESTADO_PEDIDO_NO_VALIDO_PAGAR);
		
		if (!Constant.SOURCE.equalsIgnoreCase(getType()))
			throw new ExceptionInParam(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
			//throw new ExceptionInParam(ConstantPayment.SC_ERROR_ID_GRABILITY, ConstantPayment.MSG_ERROR_ID_GRABILITY);
	}

}
