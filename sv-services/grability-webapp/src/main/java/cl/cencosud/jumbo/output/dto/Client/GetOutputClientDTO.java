package cl.cencosud.jumbo.output.dto.Client;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.output.dto.OutputDTO;

public class GetOutputClientDTO extends OutputDTO implements Serializable{
	
	private static final long serialVersionUID = -6094862790743872813L;
		
	private long cliente_id;
	private int rut; //": "unique user identifier",
	private String  dv; //": "unique user identifier",
	private String name;				
	private String last_name;
	private String email;
	private String cod_phone_number;
	private String phone_number;
	private String cod_phone_number2;
	private String phone_number2;
	private int newsletter;
	
	private String _pwd = null;
	
	private ArrayList lists;
	/*	{
        			"list_id": "list unique identifier",
        			"list_name": "list name",
        			"created_at": "date of creation - used to order the lists",
        			"list_type": "M(manual) or W(generated by a purchase)"
        		}*/
	private ArrayList addresses;
	/*	
	 	* {
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
	
	public GetOutputClientDTO(){}
	
	public GetOutputClientDTO(String status, String error_message,
			long cliente_id, int rut, String dv, String name, String last_name, String email,
			String phone_number, String phone_number2, int newsletter,
			ArrayList lists, ArrayList addresses, String cod_phone_number, String cod_phone_number2) {

		setStatus(status);
		setError_message(error_message);
		this.cliente_id = cliente_id;
		this.rut=rut;
		this.dv=dv;
		this.name = name;
		this.last_name = last_name;
		this.email = email;
		this.cod_phone_number = cod_phone_number;
		this.phone_number = phone_number;		
		this.cod_phone_number2 = cod_phone_number2;
		this.phone_number2 = phone_number2;
		this.newsletter = newsletter;
		this.lists = lists;
		this.addresses = addresses;
	}

	public long getCliente_id() {
		return cliente_id;
	}

	public void setCliente_id(long cliente_id) {
		this.cliente_id = cliente_id;
	}	

	public int getRut() {
		return rut;
	}

	public void setRut(int rut) {
		this.rut = rut;
	}

	public String getDv() {
		return dv;
	}

	public void setDv(String dv) {
		this.dv = dv;
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

	public String get_pwd() {
		return _pwd;
	}

	public void set_pwd(String _pwd) {
		this._pwd = _pwd;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String toJson() {		
		
		JSONObject obj	= new JSONObject();		
		
		if(!StringUtils.isBlank(this.get_pwd())){
			obj.put("_pwd", this.get_pwd());
			obj.put(Constant.STATUS , String.valueOf(this.getStatus()));
			obj.put(Constant.ERROR_MESSAGE , String.valueOf(this.getError_message()));
		}else{
		
			obj.put(Constant.STATUS , String.valueOf(this.getStatus()));
			obj.put(Constant.ERROR_MESSAGE , String.valueOf(this.getError_message()));
			obj.put(ConstantClient.NAME , this.getName());
			
			obj.put(ConstantClient.CLIENT_ID , String.valueOf(this.getCliente_id()));	
			//obj.put(ConstantClient.RUT , String.valueOf(this.getRut()));	
			//obj.put(ConstantClient.DV , this.getDv());	
			
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