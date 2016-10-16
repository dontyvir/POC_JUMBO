package cl.cencosud.jumbo.output.dto.PurchaseOrder;

import java.io.Serializable;

import net.sf.json.JSONObject;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantPurchaseOrder;
import cl.cencosud.jumbo.output.dto.OutputDTO;

public class PostOutputPurchaseOrderDTO extends OutputDTO implements Serializable{

	private static final long serialVersionUID = 2750623510569056250L;

	private long order_id;
	private long journey_price;
	private long reserved_amount;
	private long cart_amount;
	
	public PostOutputPurchaseOrderDTO(String status, String error_message, int order_id, int journey_price, int reserved_amount, int cart_amount) {
		setStatus(status);
		setError_message(error_message);
		this.order_id= order_id;
		this.journey_price = journey_price;
		this.reserved_amount = reserved_amount;
		this.cart_amount = cart_amount;
	}

	
	public long getJourney_price() {
		return journey_price;
	}

	public void setJourney_price(long journey_price) {
		this.journey_price = journey_price;
	}

	public long getReserved_amount() {
		return reserved_amount;
	}

	public void setReserved_amount(long reserved_amount) {
		this.reserved_amount = reserved_amount;
	}

	public long getCart_amount() {
		return cart_amount;
	}

	public void setCart_amount(long cart_amount) {
		this.cart_amount = cart_amount;
	}

	public PostOutputPurchaseOrderDTO() {
		// TODO Apéndice de constructor generado automáticamente
	}

	public long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(long order_id) {
		this.order_id = order_id;
	}

	public String toJson() {		
		
		JSONObject obj	= new JSONObject();			
		
		obj.put(Constant.STATUS , String.valueOf(this.getStatus()));
		obj.put(Constant.ERROR_MESSAGE , String.valueOf(this.getError_message()));
		obj.put(ConstantPurchaseOrder.ORDER_ID , String.valueOf(this.getOrder_id()));	
		obj.put(ConstantPurchaseOrder.JOURNEY_PRICE, String.valueOf(this.journey_price));
		obj.put(ConstantPurchaseOrder.RESERVED_AMOUNT, String.valueOf(this.reserved_amount));
		obj.put(ConstantPurchaseOrder.CART_AMOUNT, String.valueOf(this.cart_amount));
			
		
		return obj.toString();
	}

}
