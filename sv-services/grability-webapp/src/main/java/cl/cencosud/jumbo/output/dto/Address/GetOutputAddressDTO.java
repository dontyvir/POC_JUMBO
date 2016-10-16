package cl.cencosud.jumbo.output.dto.Address;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.output.dto.OutputDTO;

public class GetOutputAddressDTO extends OutputDTO implements Serializable{

	private static final long serialVersionUID = 1272419469199422569L;

	private ArrayList addresses; //HashMap of Addresses
	
	/*Object of addresses
	     "addresses":[ 
	    			{
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
                        "local_code": "string identifier for the local (ie J502)"
	        			"dummy": "indicates whether this address is a dummy one, autocreated by the backend"
	        		}
        		]   */	
 	
	public GetOutputAddressDTO(String status, String error_message,
			ArrayList addresses) {
		setStatus(status);
		setError_message(error_message);
		this.addresses = addresses;
	}

	public GetOutputAddressDTO() {
		// TODO Apéndice de constructor generado automáticamente
	}

	public ArrayList getAddresses() {
		return addresses;
	}

	public void setAddresses(ArrayList addresses) {
		this.addresses = addresses;
	}

	public String toJson() {
		
		JSONObject obj	= new JSONObject();			
		
		obj.put(Constant.STATUS , String.valueOf(this.getStatus()));
		obj.put(Constant.ERROR_MESSAGE , String.valueOf(this.getError_message()));
		
		JSONArray jsArrayAddresses = new JSONArray();
		jsArrayAddresses.addAll(this.getAddresses());
		obj.put(ConstantClient.ADDRESSES, jsArrayAddresses.toString());
		
		return obj.toString();
	}

}
