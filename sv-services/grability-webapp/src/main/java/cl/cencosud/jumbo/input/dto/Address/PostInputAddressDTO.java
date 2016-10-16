package cl.cencosud.jumbo.input.dto.Address;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantAddress;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.bizdelegate.BizDelegateAddress;
import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class PostInputAddressDTO extends InputDTO implements Serializable {
	

	private static final long serialVersionUID = -3264515454458273182L;
	
	private int 	client_id;//user identifier",
	private String  type;//alphanumeric value representing the origin of the request",
	private String  name;//name of the address",
	private String  street;//street name",
	private String  number;//address number",
	private String  house_apt;//house/apt number",
	private int 	municipality_id;//unique identifier for municipality (comuna)",
	private int 	region_id;//unique identifier for the region",
	private String 	observation;//any remark regarding the address (ie: right next to the minimarket)"
	private int 	street_type;//Street represents the type associated with the address
	
	public PostInputAddressDTO(HttpServletRequest request) throws ExceptionInParam, GrabilityException {
		
		super(request,ConstantAddress.INTEGRATION_CODE_AGREGAR_DIR_NUEVA, request.getParameter(ConstantClient.CLIENT_ID));
		
		this.client_id = getParamInt(request, ConstantClient.CLIENT_ID);
		this.type = getParamString(request, Constant.TYPE);
		this.name = getParamString(request, ConstantAddress.NAME);
		this.street = getParamString(request, ConstantAddress.STREET);
		this.number = getParamString(request, ConstantAddress.NUMBER);
		this.house_apt = getParamString(request, ConstantAddress.HOUSE_APT);
		this.municipality_id = getParamInt(request, ConstantAddress.MUNICIPALITY_ID);
		this.region_id = getParamInt(request, ConstantAddress.REGION_ID);
		this.observation = getParamString(request, ConstantAddress.OBSERVATION);
		this.street_type = getParamInt(request, ConstantAddress.STREET_TYPE);
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getHouse_apt() {
		return house_apt;
	}

	public void setHouse_apt(String house_apt) {
		this.house_apt = house_apt;
	}

	public int getMunicipality_id() {
		return municipality_id;
	}

	public void setMunicipality_id(int municipality_id) {
		this.municipality_id = municipality_id;
	}

	public int getRegion_id() {
		return region_id;
	}

	public void setRegion_id(int region_id) {
		this.region_id = region_id;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}	

	public int getStreet_type() {
		return street_type;
	}

	public void setStreet_type(int street_type) {
		this.street_type = street_type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void isValid() throws ExceptionInParam, GrabilityException {
	
		//this.client_id = getParamInt(request, ConstantClient.CLIENT_ID);
		this.isValidClientById(getClient_id());
		
		//this.name = getParamString(request, ConstantAddress.NAME);
		if(StringUtils.isEmpty(getName()))
			throw new ExceptionInParam(ConstantAddress.SC_NOMBRE_DIR_INVALIDO, ConstantAddress.MSG_NOMBRE_DIR_INVALIDO);
			
		//this.street = getParamString(request, ConstantAddress.STREET);
		if(StringUtils.isEmpty(getStreet()))
			throw new ExceptionInParam(ConstantAddress.SC_CALLE_DIR_INVALIDA, ConstantAddress.MSG_CALLE_DIR_INVALIDA);
		
		BizDelegateAddress biz = new BizDelegateAddress();
		if(!biz.isValidTiposCalle(getStreet_type())){
			throw new ExceptionInParam(ConstantAddress.SC_ERROR_TIPO_CALLE_INVALIDO, ConstantAddress.MSG_ERROR_TIPO_CALLE_INVALIDO);
		}
		
		//this.number = getParamString(request, ConstantAddress.NUMBER);
		if(StringUtils.isEmpty(getNumber()))
			throw new ExceptionInParam(ConstantAddress.SC_NUM_DIR_INVALIDA, ConstantAddress.MSG_NUM_DIR_INVALIDA);
		
		/*
		 * 			
		 * this.house_apt = getParamString(request, ConstantAddress.HOUSE_APT);
		this.observation = getParamString(request, ConstantAddress.OBSERVATION);
		 * */
					
		//this.municipality_id = getParamInt(request, ConstantAddress.MUNICIPALITY_ID);
		//Si comuna existe esta ok el id enviado.
		isValidComunaById(getMunicipality_id());//lanza un excepcion si no existe
		
		//		this.region_id = getParamInt(request, ConstantAddress.REGION_ID);
		//Si comuna existe esta ok el id enviado.
		isValidRegionById(getRegion_id());//lanza un excepcion si no existe
		
		
		//this.type = getParamString(request, Constant.TYPE);
		if (getType() == null || !Constant.SOURCE.equals(getType()))
			throw new ExceptionInParam(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
				
	}
    

}
