package cl.cencosud.jumbo.input.dto.DeliveryWindow;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantAddress;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.Constant.ConstantDeliveryWindow;
import cl.cencosud.jumbo.Constant.ConstantPurchaseOrder;
import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class GetInputDeliveryWindowDTO extends InputDTO implements Serializable{

	private static final long serialVersionUID = -6704379884734677547L;

	private String shipping_type;//"alphanumeric value representing delivery or click and collect",
	private int local_id;// "unique identifier of the local related to this address",
	private int address_id;//"unique identifier for the address",
	private int client_id;//"user identifier",
	private int cart_value_cat;//"shopping cart value paying with Cencosud Credit Card",
	private String type;//"alphanumeric value representing the origin of the request"
	
	public GetInputDeliveryWindowDTO(HttpServletRequest request) throws ExceptionInParam, GrabilityException {
		super(request,ConstantDeliveryWindow.INTEGRATION_CODE_VENTANA_DESPACHO,request.getParameter(ConstantAddress.ADDRESS_ID));
		this.shipping_type = getParamString(request, ConstantDeliveryWindow.SHIPPING_TYPE);
		this.local_id = getParamInt(request, Constant.LOCAL_ID);
		this.address_id = getParamInt(request, ConstantAddress.ADDRESS_ID);
		this.client_id = getParamInt(request, ConstantClient.CLIENT_ID);
		this.cart_value_cat = getParamInt(request, ConstantDeliveryWindow.CART_VALUE_CAT);
		this.type = getParamString(request, Constant.TYPE);
	}
	
	public String getShipping_type() {
		return shipping_type;
	}

	public void setShipping_type(String shipping_type) {
		this.shipping_type = shipping_type;
	}

	public int getLocal_id() {
		return local_id;
	}

	public void setLocal_id(int local_id) {
		this.local_id = local_id;
	}

	public int getAddress_id() {
		return address_id;
	}

	public void setAddress_id(int address_id) {
		this.address_id = address_id;
	}

	public int getClient_id() {
		return client_id;
	}

	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}

	public int getCart_value_cat() {
		return cart_value_cat;
	}

	public void setCart_value_cat(int cart_value_cat) {
		this.cart_value_cat = cart_value_cat;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void isValid() throws ExceptionInParam, GrabilityException {
		
		if (getClient_id()==0)
			throw new ExceptionInParam(ConstantDeliveryWindow.SC_ID_CLIENTE_INVALIDO, ConstantDeliveryWindow.MSG_ID_CLIENTE_INVALIDO);
		if (getLocal_id()==0)
			throw new ExceptionInParam(ConstantDeliveryWindow.SC_LOCAL_INVALIDO, ConstantDeliveryWindow.MSG_LOCAL_INVALIDO);
		if (getCart_value_cat() < 1)
			throw new ExceptionInParam(ConstantDeliveryWindow.SC_MONTO_CARRO_INVALIDO, ConstantDeliveryWindow.MSG_MONTO_CARRO_INVALIDO);
		if (!Constant.SOURCE.equalsIgnoreCase(getType()))
			throw new ExceptionInParam(ConstantDeliveryWindow.SC_ERROR_ID_GRABILITY, ConstantDeliveryWindow.MSG_ERROR_ID_GRABILITY);
		String serviciosValidos [] = ConstantDeliveryWindow.TIPO_SERVICIO_VALIDOS.split("-");
		boolean isServicioValido = false;
		for (int i=0;i<serviciosValidos.length;i++){
			if (serviciosValidos[i].equalsIgnoreCase(getShipping_type())){
				isServicioValido = true;
				break;
			}
		}
		if (!isServicioValido)
			throw new ExceptionInParam(ConstantDeliveryWindow.SC_TIPO_SERVICIO_INVALIDO, ConstantDeliveryWindow.MSG_TIPO_SERVICIO_INVALIDO);
		if (getShipping_type().equalsIgnoreCase("D")){
			if (getAddress_id()==0){
				throw new ExceptionInParam(ConstantDeliveryWindow.SC_ID_DIRECCION_INVALIDA, ConstantDeliveryWindow.MSG_ID_DIRECCION_INVALIDA);
			}
			isValidAddressById((long)getAddress_id());
		}
		if (!getType().equals(Constant.SOURCE))
			throw new ExceptionInParam(ConstantDeliveryWindow.SC_TIPO_ORIGEN_INVALIDO, ConstantDeliveryWindow.MSG_TIPO_ORIGEN_INVALIDO);

		isValidClientById((long)getClient_id());			
			
		isValidLocalById ((long)getLocal_id());	
		
	}

}
