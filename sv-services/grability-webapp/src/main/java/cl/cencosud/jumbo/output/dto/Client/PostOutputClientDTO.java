package cl.cencosud.jumbo.output.dto.Client;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.output.dto.OutputDTO;

public class PostOutputClientDTO extends OutputDTO implements Serializable{

	private static final long serialVersionUID = 2288006752229372053L;
		
	private long cliente_id;
	private String name;				
	private String last_name;
	private String email;
	private String cod_phone_number;
	private String phone_number;
	private String cod_phone_number2;
	private String phone_number2;
	private int newsletter;	
	private String action;
	
	private ArrayList lists;
	/*	{
        			"list_id": "list unique identifier",
        			"list_name": "list name",
        			"created_at": "date of creation - used to order the lists",
        			"list_type": "M(manual) or W(generated by a purchase)"
        		}*/
	private ArrayList addresses;
	/*	{
	    				"address_id": "unique identifier for the address",
	        			"name": "name of the address",
	        			"street": "street name",
	        			"number": "address number",
	        			"house_apt": "house/apt number",
	        			"municipality_id": "unique identifier for municipality (comuna)",
	        			"region_id": "unique identifier for the region",
	        			"observation": "any remark regarding the address (ie: right next to the minimarket)",
	        			"default": "indicates whether this is the default address (last used address)",
	        			"local_id": "unique identifier of the local related to this address",
	        			"dummy": "indicates whether this address is a dummy one, autocreated by the backend"
	        		}
	 * */
	
	public PostOutputClientDTO(String status, String error_message, long cliente_id, String name, String last_name,
			String email, String phone_number, String phone_number2,
			int newsletter, String action, ArrayList lists, ArrayList addresses, String cod_phone_number, String cod_phone_number2) {
		
		setStatus(status);
		setError_message(error_message);
		this.cliente_id = cliente_id;
		this.name = name;
		this.last_name = last_name;
		this.email = email;
		this.cod_phone_number = cod_phone_number;
		this.phone_number = phone_number;
		this.cod_phone_number2 = cod_phone_number2;
		this.phone_number2 = phone_number2;
		this.newsletter = newsletter;
		this.action=action;
		this.lists = lists;
		this.addresses = addresses;
	}
	
	public PostOutputClientDTO() {
		// TODO Apéndice de constructor generado automáticamente
	}

	public long getCliente_id() {
		return cliente_id;
	}

	public void setCliente_id(long cliente_id) {
		this.cliente_id = cliente_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getPhone_number2() {
		return phone_number2;
	}

	public void setPhone_number2(String phone_number2) {
		this.phone_number2 = phone_number2;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getNewsletter() {
		return newsletter;
	}

	public void setNewsletter(int newsletter) {
		this.newsletter = newsletter;
	}

	public ArrayList getLists() {
		return lists;
	}

	public void setLists(ArrayList lists) {
		this.lists = lists;
	}

	public ArrayList getAddresses() {
		return addresses;
	}

	public void setAddresses(ArrayList addresses) {
		this.addresses = addresses;
	}
	
	public String getCod_phone_number() {
		return cod_phone_number;
	}

	public void setCod_phone_number(String cod_phone_number) {
		this.cod_phone_number = cod_phone_number;
	}

	public String getCod_phone_number2() {
		return cod_phone_number2;
	}

	public void setCod_phone_number2(String cod_phone_number2) {
		this.cod_phone_number2 = cod_phone_number2;
	}

	public String toJson() {
		
		JSONObject obj	= new JSONObject();			
		
		obj.put(Constant.STATUS , String.valueOf(this.getStatus()));
		obj.put(Constant.ERROR_MESSAGE , String.valueOf(this.getError_message()));
		
		if( StringUtils.equals(ConstantClient.RECOVER, getAction())){
			obj.put(ConstantClient.EMAIL, this.getEmail());
		}
		
		if( !StringUtils.equals(ConstantClient.RECOVER, getAction())){
			obj.put(ConstantClient.CLIENT_ID , String.valueOf(this.getCliente_id()));
			obj.put(ConstantClient.NAME , this.getName());				
			obj.put(ConstantClient.LAST_NAME, this.getLast_name());
			obj.put(ConstantClient.EMAIL, this.getEmail());
			//obj.put(ConstantClient.COD_PHONE_NUMBER, this.getCod_phone_number());
			obj.put(ConstantClient.PHONE_NUMBER, this.getCod_phone_number()+"-"+this.getPhone_number());
			//obj.put(ConstantClient.COD_PHONE_NUMBER2, this.getCod_phone_number2());
			obj.put(ConstantClient.PHONE_NUMBER2, this.getCod_phone_number2()+"-"+this.getPhone_number2());
			
			obj.put(ConstantClient.NEWSLETTER, String.valueOf(this.getNewsletter()));
						
			JSONArray jsArray = new JSONArray();
			jsArray.addAll(this.getLists());
			obj.put(ConstantClient.LIST, jsArray.toString());
			
			JSONArray jsArrayAddresses = new JSONArray();
			jsArrayAddresses.addAll(this.getAddresses());
			obj.put(ConstantClient.ADDRESSES, jsArrayAddresses.toString());
		}
		
		
		return obj.toString();
	}

}
