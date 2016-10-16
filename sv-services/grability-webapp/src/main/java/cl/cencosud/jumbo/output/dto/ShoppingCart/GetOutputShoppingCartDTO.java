package cl.cencosud.jumbo.output.dto.ShoppingCart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.Constant.ConstantShoppingCart;
import cl.cencosud.jumbo.output.dto.OutputDTO;

public class GetOutputShoppingCartDTO extends OutputDTO implements Serializable{

	private static final long serialVersionUID = -4877677360212296495L;

	private ArrayList products;
	    				/*
						    {
						      "origin": "it indicates from where the product was added to the cart, ie: jumbo or grability",
						      "added_date": "date when the product was added to the cart",
						      "quantity": "how many products were added",
						      "product_id": "product id (FO id)",
						      "total_price": "unit price times quantity",
						      "unit_price": "price per unit",
						      "available": "indicates if the product is available to be purchased",
						      "substitution_criteria_id": "unique identifier for a substitution criteria, ie (1..6)",
						      "susbtitution_description": "one criteria is other and has a textbox so the user can enter text. This field represents that text"
						    }
						*/
		
	private Map total; 
					/*
						{
							"total": "shopping cart total paying with TBK card",
							"savings": "shopping cart savings due to promotions (paying with TBK card)",
							"total_cat": "shopping cart total paying with CAT card",
							"savings_cat": "shopping cart savings due to promotions (paying with CAT card)",
						}
					*/
	
	private ArrayList discounts;	
					/*
						{
							"discount_description": "text describing the discount, ie: 12% descuento colaborador",
							"discount_value": "value of the discount, ie: 3900"
						}
					*/
	private Map coupon;
					/*
						{
							"coupon_description": "text describing the coupon, ie: día de los enamorados, aplica solo en chocolates",
							"status": "status of the coupon, ie: valid/invalid/used"
						}		      
					 */

	public GetOutputShoppingCartDTO(String status, String error_message, ArrayList products, HashMap total,
			ArrayList discounts, HashMap coupon) {
		setStatus(status);
		setError_message(error_message);
		this.products = products;
		this.total = total;
		this.discounts = discounts;
		this.coupon = coupon;
	}
		
	public GetOutputShoppingCartDTO() {
		// TODO Apéndice de constructor generado automáticamente
	}

	public ArrayList getProducts() {
		return products;
	}

	public void setProducts(ArrayList products) {
		this.products = products;
	}

	public Map getTotal() {
		return total;
	}

	public void setTotal(Map total) {
		this.total = total;
	}

	public ArrayList getDiscounts() {
		return discounts;
	}

	public void setDiscounts(ArrayList discounts) {
		this.discounts = discounts;
	}

	public Map getCoupon() {
		return coupon;
	}

	public void setCoupon(Map coupon) {
		this.coupon = coupon;
	}

	public String toJson() {		
		
		JSONObject obj	= new JSONObject();			
		
		obj.put(Constant.STATUS , String.valueOf(this.getStatus()));
		obj.put(Constant.ERROR_MESSAGE , String.valueOf(this.getError_message()));	
		
		JSONArray jsArray = new JSONArray();
		jsArray.addAll(this.getProducts());
		obj.put(ConstantShoppingCart.PRODUCTS, jsArray.toString());
		
		/*JSONArray jsArrayTotal= new JSONArray();
		jsArrayTotal.addAll(this.getTotal());*/
		JSONObject jsontotal = new JSONObject();
		if(this.getTotal() != null){
			jsontotal.putAll(this.getTotal());
			obj.put(ConstantShoppingCart.TOTAL, jsontotal.toString());
		}else{
			obj.put(ConstantShoppingCart.TOTAL, new JSONObject());
		}
		
		if(this.getDiscounts() != null && this.getDiscounts().size() >0){
			JSONArray jsArrayDiscounts= new JSONArray();
			jsArrayDiscounts.addAll(this.getDiscounts());
			obj.put(ConstantShoppingCart.DISCOUNTS, jsArrayDiscounts.toString());
		}else{
			obj.put(ConstantShoppingCart.DISCOUNTS, new JSONObject());
		}
		
		JSONObject jsObject = null;
		if(getCoupon() != null)
			jsObject = (JSONObject) JSONSerializer.toJSON(getCoupon());  
		
		if(jsObject != null){
			obj.put(ConstantShoppingCart.COUPON, jsObject.toString());
		}else{
			obj.put(ConstantShoppingCart.COUPON, new JSONObject() );
		}
		
		return obj.toString();
	}

}
