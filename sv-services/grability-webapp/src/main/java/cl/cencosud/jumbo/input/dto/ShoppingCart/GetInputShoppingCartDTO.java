package cl.cencosud.jumbo.input.dto.ShoppingCart;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.Constant.ConstantShoppingCart;
import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class GetInputShoppingCartDTO extends InputDTO implements Serializable{

	private static final long serialVersionUID = 3807017330684950780L;
	
	private String coupon_code;//": "alphanumeric value representing the code for a coupon, if there is no coupon this field should not be sent",
	private int client_id;//": "user identifier",
	private String type;//"alphanumeric value representing the origin of the request"
	private String guest_user;
	private int local_id;
	
	public GetInputShoppingCartDTO(HttpServletRequest request) throws ExceptionInParam, GrabilityException{

		super(request,ConstantShoppingCart.INTEGRATION_CODE_OBTENER_CARRO_COMPRAS,request.getParameter(ConstantClient.CLIENT_ID));
		
		this.coupon_code 	= getParamString(request, ConstantShoppingCart.COUPON_CODE);
		this.client_id 		= getParamInt(request, ConstantClient.CLIENT_ID);
		this.type 			= getParamString(request, Constant.TYPE);
		this.guest_user 	= getParamString(request, ConstantShoppingCart.GUEST_USER);
		this.local_id		= getParamInt(request, Constant.LOCAL_ID);
	}
	
	public String getCoupon_code() {
		return coupon_code;
	}

	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}

	public int getClient_id() {
		return client_id;
	}

	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getGuest_user() {
		return guest_user;
	}

	public void setGuest_user(String guest_user) {
		this.guest_user = guest_user;
	}

	public int getLocal_id() {
		return local_id;
	}

	public void setLocal_id(int local_id) {
		this.local_id = local_id;
	}

	public void isValid() throws ExceptionInParam, GrabilityException{
		
		// TODO Apéndice de método generado automáticamente
		if(StringUtils.isEmpty(getGuest_user()) ||
				(!StringUtils.equals(getGuest_user(), "0") &&
				!StringUtils.equals(getGuest_user(), "1"))){	
			throw new ExceptionInParam(ConstantShoppingCart.SC_CLIENT_TYPE, ConstantShoppingCart.MSG_CLIENT_TYPE);
		}
		
		if(getLocal_id() == 0){
			throw new ExceptionInParam(ConstantShoppingCart.SC_LOCAL_INVALIDO, ConstantShoppingCart.MSG_LOCAL_INVALIDO);
		} 
		
		this.isValidLocalById(getLocal_id());
		
		if(StringUtils.equals(getGuest_user(), "0")){
			this.isValidClientById(getClient_id());
		}
		
		if(StringUtils.equals(getGuest_user(), "1") && getClient_id() == 0){
			//setClient_id(1);
			throw new ExceptionInParam(ConstantShoppingCart.SC_ID_CLIENT_GUEST, ConstantShoppingCart.MSG_ID_CLIENT_GUEST);
		}
		
		if (getType() == null || !Constant.SOURCE.equals(getType()))
			throw new ExceptionInParam(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
	}

}
