package cl.cencosud.jumbo.output.dto.Address;

import java.io.Serializable;

import net.sf.json.JSONObject;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.output.dto.OutputDTO;

public class DeleteOutputAddressDTO extends OutputDTO implements Serializable{

	private static final long serialVersionUID = 4920212728138480196L;
	
	
	public DeleteOutputAddressDTO() {}	
	
	public DeleteOutputAddressDTO(String status, String error_message) {
		setStatus(status);
		setError_message(error_message);
	}	

	public String toJson() {
		JSONObject obj	= new JSONObject();			
		
		obj.put(Constant.STATUS , String.valueOf(this.getStatus()));
		obj.put(Constant.ERROR_MESSAGE , String.valueOf(this.getError_message()));
				
		return obj.toString();
	}

}
