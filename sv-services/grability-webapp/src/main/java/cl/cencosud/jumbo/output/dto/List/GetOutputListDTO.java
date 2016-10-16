package cl.cencosud.jumbo.output.dto.List;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantDeliveryWindow;
import cl.cencosud.jumbo.Constant.ConstantList;
import cl.cencosud.jumbo.output.dto.OutputDTO;

public class GetOutputListDTO extends OutputDTO implements Serializable {

	private static final long serialVersionUID = 7978844573587136700L;

	private ArrayList lists; 
	/*
					{
		    			"list_id": "unique identifier for the list",
		    			"list_name": "name of the list, the user can edit this",
		    			"creation_date": "date when the list was created",
		    			"list_type": "defines the type of lists, ie (m)anual, (w)eb"
	    			}
	 */
	private int total;//"total number of lists, used for pagination"

	private String list_type;//"defines the type of lists, ie (m)anual, (w)eb",
	private ArrayList products;
	/*
    			{
	    			 "quantity": "how many of this product is in this list",
					 "product_id": "product id (FO id)"
				 }
	 */

	public GetOutputListDTO(String status, String error_message,
			ArrayList lists, int total, String list_type,
			ArrayList products) {
		setStatus(status);
		setError_message(error_message);
		this.lists = lists;
		this.total = total;
		this.list_type = list_type;
		this.products = products;
	}
	public GetOutputListDTO(){};

	public ArrayList getLists() {
		return lists;
	}

	public void setLists(ArrayList lists) {
		this.lists = lists;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getList_type() {
		return list_type;
	}

	public void setList_type(String list_type) {
		this.list_type = list_type;
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
		obj.put(ConstantList.LISTS , this.getLists());
		if ((this.getProducts()==null)||(this.getLists()!=null))
			obj.put(ConstantList.TOTAL, String.valueOf(this.getTotal()));
		obj.put(ConstantList.LIST_TYPE, this.getList_type());
		obj.put(ConstantList.PRODUCTS, this.products);
		
		
//		JSONArray jsArrayDayOfWeekForDelivery = new JSONArray();
//		jsArrayDayOfWeekForDelivery.add(this.getDay_of_week_for_delivery());
//		obj.put(ConstantDeliveryWindow.DAY_OF_WEEK_FOR_DELIVERY, jsArrayDayOfWeekForDelivery.toString());
		
		/*JSONArray jsArraDeliveryRanges= new JSONArray();
		jsArraDeliveryRanges.addAll(this.getDay_of_week_for_delivery());
		obj.put(ConstantDeliveryWindow.DELIVERY_RANGES, jsArraDeliveryRanges.toString());*/
		
		
		return obj.toString();
	}
}
