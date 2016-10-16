package cl.cencosud.jumbo.input.dto.ShoppingCart;

import java.io.Serializable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.Constant.ConstantPurchaseOrder;
import cl.cencosud.jumbo.Constant.ConstantShoppingCart;
import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class PutInputShoppingCartDTO extends InputDTO implements Serializable {

	private static final long serialVersionUID = 1101850747350476974L;

	private int client_id;//"user identifier",
	private String guest_user;//"0/1 value that indicates if the user is a guest user. If 1 then the parameter client_id represents te session_id to sync the cart",
	private int local_id;//"unique identifier of the local related to this address",
	private String type;//"alphanumeric value representing the origin of the request",
	
	private String products;//
	/*
    {
	      "quantity": "how many products were added",
	      "product_id": "product id (FO id)",
	      "substitution_criteria_id": "unique identifier for a substitution criteria, ie (1..6)",
	      "susbtitution_description": "one criteria is other and has a textbox so the user can enter text. This field represents that text"
	    }
	*/
	private JSONArray jsProducts; //Products
	
	
	public PutInputShoppingCartDTO(HttpServletRequest request) throws ExceptionInParam, GrabilityException{
		
		super(request,ConstantShoppingCart.INTEGRATION_CODE_MODIFICAR_CARRO_COMPRAS, request.getParameter(ConstantClient.CLIENT_ID));
		
		this.client_id	= getParamInt(request, ConstantClient.CLIENT_ID);
		this.guest_user = getParamString(request, ConstantShoppingCart.GUEST_USER);
		this.local_id	= getParamInt(request, Constant.LOCAL_ID);
		this.type 		= getParamString(request, Constant.TYPE);	
		this.products 	= getParamString(request, ConstantPurchaseOrder.PRODUCTS);
		try{
			this.jsProducts = (JSONArray) JSONSerializer.toJSON(this.products);
		}catch(Exception e){
			this.jsProducts = null;
		}
	}
			
	public int getClient_id() {
		return client_id;
	}

	public void setClient_id(int client_id) {
		this.client_id = client_id;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public JSONArray getJsProducts() {
		return jsProducts;
	}

	public void setJsProducts(JSONArray jsProducts) {
		this.jsProducts = jsProducts;
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

		if(getJsProducts()== null || getJsProducts().size() == 0){
			throw new ExceptionInParam(ConstantShoppingCart.SC_LISTA_PRODUCTOS_INVALIDA, ConstantShoppingCart.MSG_LISTA_PRODUCTOS_INVALIDA);
			
		}
		
	}

}
