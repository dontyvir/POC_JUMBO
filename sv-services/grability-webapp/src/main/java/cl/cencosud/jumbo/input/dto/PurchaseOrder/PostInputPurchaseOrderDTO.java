package cl.cencosud.jumbo.input.dto.PurchaseOrder;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosException;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantAddress;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.Constant.ConstantPurchaseOrder;
import cl.cencosud.jumbo.bizdelegate.BizDelegatePurchaseOrder;
import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class PostInputPurchaseOrderDTO extends InputDTO implements Serializable{
	

	private static final long serialVersionUID = 4809269034712197454L;
	private int client_id; //user identifier",
    private String type; //": "alphanumeric value representing the origin of the request",
    private int delivery_journey_id; //": "delivery journey unique identifier, ie 123",
    private int picking_journey_id; //": "picking journey unique identifier, ie 111",
    private int address_id; //": "unique identifier for the address",
    private int local_id; //": "unique identifier of the local related to this address",
    private String shipping_type; //": "alphanumeric value representing delivery or click and collect",
    private long journey_price; //": "price of delivery for this window, ie 4990",
    private String order_observation; // "observations regarding the purchase order",
    private String payment_method;//": "payment method selected by the user, ie CAT or TBK",
    private String other_receiver; //": "indicates if the buyer will receive the order or if other person will receive it, ie: 0 if buyer receives 1 otherwise",
    private String receiver_observation;//": "this field is optional, the user can enter observations regarding the person receiving the order",
    private int receiver_rut;//": "this field is optional, the receiver's rut",
    private String receiver_dv;//": "this field is optional, the receiver's dv (digito verificador)",
    private long amount; // "amount to be reserved on the client's credit card",
    private String products; //Products
    private JSONObject jsProducts; //Products
    private String coupon_code; //coupon
	public PostInputPurchaseOrderDTO(HttpServletRequest request) throws ExceptionInParam, GrabilityException{
			super(request,ConstantPurchaseOrder.INTEGRATION_CODE_GENERATE_PURCHASE_ORDER,null);
		
			this.client_id = getParamInt(request, ConstantClient.CLIENT_ID);
			this.type = getParamString(request, Constant.TYPE);
			this.delivery_journey_id = getParamInt(request, ConstantPurchaseOrder.DELIVERY_JOURNEY_ID);		
			this.picking_journey_id = getParamInt(request, ConstantPurchaseOrder.PICKING_JOURNEY_ID);
			this.address_id = getParamInt(request, ConstantAddress.ADDRESS_ID);
			this.local_id = getParamInt(request, ConstantPurchaseOrder.LOCAL_ID);		
			this.shipping_type = getParamString(request, ConstantPurchaseOrder.SHIPPING_TYPE);
			
			this.journey_price = getParamInt(request, ConstantPurchaseOrder.JOURNEY_PRICE);
			this.order_observation = getParamString(request, ConstantPurchaseOrder.ORDER_OBSERVATION);
			this.payment_method = getParamString(request, ConstantPurchaseOrder.PAYMENT_METHOD);
			this.other_receiver = getParamString(request, ConstantPurchaseOrder.OTHER_RECEIVER);
			this.receiver_observation = getParamString(request, ConstantPurchaseOrder.RECEIVER_OBSERVATION);
			this.receiver_rut = getParamInt(request, ConstantPurchaseOrder.RECEIVER_RUT);
			this.receiver_dv = getParamString(request, ConstantPurchaseOrder.RECEIVER_DV);
			this.amount = getParamLong(request, ConstantPurchaseOrder.AMOUNT);
			this.products = getParamString(request, ConstantPurchaseOrder.PRODUCTS);
			this.coupon_code = getParamString(request, ConstantPurchaseOrder.COUPON_CODE);
		
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

	public int getDelivery_journey_id() {
		return delivery_journey_id;
	}

	public void setDelivery_journey_id(int delivery_journey_id) {
		this.delivery_journey_id = delivery_journey_id;
	}

	public int getPicking_journey_id() {
		return picking_journey_id;
	}

	public void setPicking_journey_id(int picking_journey_id) {
		this.picking_journey_id = picking_journey_id;
	}

	public int getAddress_id() {
		return address_id;
	}

	public void setAddress_id(int address_id) {
		this.address_id = address_id;
	}

	public int getLocal_id() {
		return local_id;
	}

	public void setLocal_id(int local_id) {
		this.local_id = local_id;
	}

	public String getShipping_type() {
		return shipping_type;
	}

	public void setShipping_type(String shipping_type) {
		this.shipping_type = shipping_type;
	}

	public long getJourney_price() {
		return journey_price;
	}

	public void setJourney_price(long journey_price) {
		this.journey_price = journey_price;
	}

	public String getOrder_observation() {
		return order_observation;
	}

	public void setOrder_observation(String order_observation) {
		this.order_observation = order_observation;
	}

	public String getPayment_method() {
		return payment_method;
	}

	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}

	public String getOther_receiver() {
		return other_receiver;
	}

	public void setOther_receiver(String other_receiver) {
		this.other_receiver = other_receiver;
	}

	public String getReceiver_observation() {
		return receiver_observation;
	}

	public void setReceiver_observation(String receiver_observation) {
		this.receiver_observation = receiver_observation;
	}

	public int getReceiver_rut() {
		return receiver_rut;
	}

	public void setReceiver_rut(int receiver_rut) {
		this.receiver_rut = receiver_rut;
	}

	public String getReceiver_dv() {
		return receiver_dv;
	}

	public void setReceiver_dv(String receiver_dv) {
		this.receiver_dv = receiver_dv;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}
	
	public JSONObject getJsProducts() {
		return jsProducts;
	}

	public void setJsProducts(JSONObject jsProducts) {
		this.jsProducts = jsProducts;
	}
	public String getCoupon_code() {
		return coupon_code;
	}

	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}

	
	public void isValid() throws ExceptionInParam, GrabilityException{
		//validacion id_cliente
		isValidClientById((long) getClient_id());		
		isValidLocalById((long)getLocal_id());
		
		if ((long)getClient_id() != this.getCliente().getId_cliente())
			throw new ExceptionInParam(ConstantPurchaseOrder.SC_ID_CLIENTE_INVALIDO, ConstantPurchaseOrder.MSG_ID_CLIENTE_INVALIDO);
		//validacion tipo de despacho
		Matcher matc = null;
		try{
			Pattern tip_desp = Pattern.compile("[CENRU]{1}");//despachos Economico Express Normal, Retiro, Umbral
			matc = tip_desp.matcher(getShipping_type());
		}catch (Exception e){
			throw new ExceptionInParam(ConstantPurchaseOrder.SC_TIPO_DESPACHO_INVALIDO, ConstantPurchaseOrder.MSG_TIPO_DESPACHO_INVALIDO);
		} 
		if (!matc.matches()){
			throw new ExceptionInParam(ConstantPurchaseOrder.SC_TIPO_DESPACHO_INVALIDO, ConstantPurchaseOrder.MSG_TIPO_DESPACHO_INVALIDO);
		}else{
			if ("R".equalsIgnoreCase(getShipping_type())){
				if (getReceiver_rut() < 1){
					throw new ExceptionInParam(ConstantPurchaseOrder.SC_RUT_RECIBIDOR_INVALIDO, ConstantPurchaseOrder.MSG_RUT_RECIBIDOR_INVALIDO);
				}
				Pattern pat_dv = Pattern.compile("[1234567890kK]{1}$");
				matc = pat_dv.matcher(getReceiver_dv());
				if (!matc.matches()){//o si es mayor a 9 y menor a 0 
					throw new ExceptionInParam(ConstantPurchaseOrder.SC_DV_RECIBIDOR_INVALIDO, ConstantPurchaseOrder.MSG_DV_RECIBIDOR_INVALIDO);
				}
				if (StringUtils.isBlank(getOther_receiver())){
					throw new ExceptionInParam(ConstantPurchaseOrder.SC_RECIBIDOR_INVALIDO, ConstantPurchaseOrder.MSG_RECIBIDOR_INVALIDO);
				}
				if (!Utils.verificarRutFO(getReceiver_rut(),getReceiver_dv().charAt(0)))
					throw new ExceptionInParam(ConstantPurchaseOrder.SC_VERIFICACION_RUT_RECIBIDOR_INVALIDA, ConstantPurchaseOrder.MSG_VERIFICACION_RUT_RECIBIDOR_INVALIDA);

				LocalDTO olocalDTO = getLocal();
				if(!StringUtils.equals("S", olocalDTO.getRetirolocal())){
					throw new ExceptionInParam(ConstantPurchaseOrder.SC_LOCAL_INVALIDO, ConstantPurchaseOrder.MSG_LOCAL_INVALIDO);
				}
			}else{
				isValidAddressById((long)getAddress_id());
				//validacion id_direccion
				if ((long)getClient_id() != this.getDireccion().getId_cliente())
					throw new ExceptionInParam(ConstantPurchaseOrder.SC_ID_DIRECCION_INVALIDA, ConstantPurchaseOrder.MSG_ID_DIRECCION_INVALIDA);		
			}
		}
		//validacion monto total
		if (!StringUtils.isNumeric(""+getAmount())){
			throw new ExceptionInParam(ConstantPurchaseOrder.SC_MONTO_TOTAL_INVALIDO, ConstantPurchaseOrder.MSG_MONTO_TOTAL_INVALIDO);
		}else{
			if (getAmount() < 1){
				throw new ExceptionInParam(ConstantPurchaseOrder.SC_ERROR_MONTO_TOTAL_ES_CERO, ConstantPurchaseOrder.MSG_ERROR_MONTO_TOTAL_ES_CERO);
			}
		}
		//validacion valores despacho
		if (!StringUtils.isNumeric(""+getJourney_price())){
			throw new ExceptionInParam(ConstantPurchaseOrder.SC_MONTO_SERVICIO_INVALIDO, ConstantPurchaseOrder.MSG_MONTO_SERVICIO_INVALIDO);
		}else{	
			if (getJourney_price() < 1){
				throw new ExceptionInParam(ConstantPurchaseOrder.SC_ERROR_MONTO_SERVICIO_ES_CERO, ConstantPurchaseOrder.MSG_ERROR_MONTO_SERVICIO_ES_CERO);
			}
		}
		//validacion local 
		if (!StringUtils.isNumeric(""+getLocal_id())){
			throw new ExceptionInParam(ConstantPurchaseOrder.SC_LOCAL_INVALIDO, ConstantPurchaseOrder.MSG_LOCAL_INVALIDO);
		}
		//Validacion medios de pago
		String [] medios = ConstantPurchaseOrder.MEDIOS_DE_PAGO_VALIDOS.split("-");
		boolean isMedioPagoValido = false;
		for (int i=0;i<medios.length;i++){
			if (medios[i].equals(getPayment_method())){
				isMedioPagoValido = true;
				break;
			}
		}
		if (!isMedioPagoValido)
			throw new ExceptionInParam(ConstantPurchaseOrder.SC_MEDIO_PAGO_INVALIDO, ConstantPurchaseOrder.MSG_MEDIO_PAGO_INVALIDO);
		
		BizDelegatePurchaseOrder biz = new BizDelegatePurchaseOrder();
		if(!biz.isValidJornadasLocal((long)getDelivery_journey_id(), (long)getPicking_journey_id(), (long)getLocal_id()))
			throw new ExceptionInParam(ConstantPurchaseOrder.SC_ERROR_JORNADAS_LOCAL, ConstantPurchaseOrder.MSG_ERROR_JORNADA_LOCAL);
	}
    
}