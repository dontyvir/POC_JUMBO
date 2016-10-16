package cl.cencosud.jumbo.input.dto.Address;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantAddress;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class DeleteInputAddressDTO extends InputDTO implements Serializable {
	

	private static final long serialVersionUID = -3427709504948924858L;

	private int client_id; //user identifier"
	private int address_id; //"unique identifier for the address",
	private String type; //alphanumeric value representing the origin of the request"
	
	public DeleteInputAddressDTO(HttpServletRequest request) throws ExceptionInParam, GrabilityException {
		
		super(request,ConstantAddress.INTEGRATION_CODE_BORRAR_DIR, request.getParameter(ConstantClient.CLIENT_ID));
		
		this.client_id =  getParamInt(request, ConstantClient.CLIENT_ID);
		this.address_id = getParamInt(request, ConstantAddress.ADDRESS_ID);
		this.type = getParamString(request, Constant.TYPE);
	
	}
	
	public int getClient_id() {
		return client_id;
	}

	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}

	public int getAddress_id() {
		return address_id;
	}

	public void setAddress_id(int address_id) {
		this.address_id = address_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void isValid() throws ExceptionInParam, GrabilityException {
		
		//this.client_id = getParamInt(request, ConstantClient.CLIENT_ID);
		this.isValidClientById(getClient_id());
		
		this.isValidAddressById(getAddress_id());		
		DireccionesDTO oDireccionesDTO = this.getDireccion();
		
		if(oDireccionesDTO.getId_cliente() !=  getClient_id())
			throw new ExceptionInParam(ConstantAddress.SC_DIR_ID_NO_ASOCIADO_CLIENTE, ConstantAddress.MSG_DIR_ID_NO_ASOCIADO_CLIENTE);
		
		
		if (getType() == null || !Constant.SOURCE.equals(getType()))
			throw new ExceptionInParam(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
		
	}

}
