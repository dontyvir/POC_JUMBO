package cl.cencosud.jumbo.input.dto.Address;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantAddress;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class GetInputAddressDTO extends InputDTO implements Serializable{
	
	private static final long serialVersionUID = -8412498160729387618L;
	private int client_id; //user identifier"
	private String type; //alphanumeric value representing the origin of the request"
	
			
	public GetInputAddressDTO(HttpServletRequest request) throws ExceptionInParam, GrabilityException {
		
		super(request,ConstantAddress.INTEGRATION_CODE_OBTENER_DIR_CLIENTE, request.getParameter(ConstantClient.CLIENT_ID));
		this.client_id = getParamInt(request, ConstantClient.CLIENT_ID);
		this.type = getParamString(request, Constant.TYPE);
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

	public void isValid() throws ExceptionInParam, GrabilityException {

		this.isValidClientById(getClient_id());
		
		if (getType() == null || !Constant.SOURCE.equals(getType()))
			throw new ExceptionInParam(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
		
	}

}
