package cl.cencosud.jumbo.output.dto.List;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.json.JSONObject;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantList;
import cl.cencosud.jumbo.Constant.ConstantPurchaseOrder;
import cl.cencosud.jumbo.output.dto.OutputDTO;

public class PostOutputListDTO extends OutputDTO implements Serializable {

	private static final long serialVersionUID = -9135026113528353976L;
	
	private String list_type;//"defines the type of lists, ie (m)anual, (w)eb",
	private String list_name; //"name of the list",

	private ArrayList products;
    		
    			/*{
	    			 "quantity": "how many of this product is in this list",
					 "product_id": "product id (FO id)"
				 }*/
	
	public PostOutputListDTO(String status, String error_message, String list_type, ArrayList products) {
		setStatus(status);
		setError_message(error_message);
		this.list_type = list_type;
		this.products = products;
	}
	
	public PostOutputListDTO() {
		// TODO Apéndice de constructor generado automáticamente
	}

	
	
	public String getList_type() {
		return list_type;
	}

	public void setList_type(String list_type) {
		this.list_type = list_type;
	}
	
	public String getList_name() {
		return list_name;
	}

	public void setList_name(String list_name) {
		this.list_name = list_name;
	}
	public ArrayList getProducts() {
		return products;
	}

	public void setProducts(ArrayList products) {
		this.products = products;
	}

	public String toJson() {
		JSONObject obj	= new JSONObject();			
		
		obj.put(Constant.STATUS , String.valueOf(this.getStatus()));
		obj.put(Constant.ERROR_MESSAGE , String.valueOf(this.getError_message()));
		obj.put(ConstantList.LIST_TYPE, getList_type());
		obj.put(ConstantList.LIST_NAME, getList_name());
		obj.put(ConstantList.PRODUCTS, getProducts());
						
		
		return obj.toString();
	}
}
