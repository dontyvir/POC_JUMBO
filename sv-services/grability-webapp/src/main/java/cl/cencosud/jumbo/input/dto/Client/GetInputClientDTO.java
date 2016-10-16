package cl.cencosud.jumbo.input.dto.Client;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import cl.bbr.jumbocl.common.utils.Utils;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.InputDTO;
import cl.cencosud.jumbo.util.RequestUtils;

public class GetInputClientDTO extends InputDTO implements Serializable{

	private static final long serialVersionUID = -8667469129830609717L;
	
	private int client_id;
	private int rut; //": "unique user identifier",
	private String  dv; //": "unique user identifier",
    private String type; //alphanumeric value representing the origin of the request"
    
    //helper encoderPass - decoderPass
	private String _pwd;
	private String _act;
	
	public GetInputClientDTO(HttpServletRequest request) throws ExceptionInParam, GrabilityException {
		super(request,ConstantClient.INTEGRATION_CODE_OBTENER_USUARIO,request.getParameter(ConstantClient.CLIENT_ID));
		
		String url = RequestUtils.requestToServerUriStringBuffer(request).toString();
		int tam =(Constant.enableUriDecodePass).length;
		for(int i = 0; i < tam ; i++){
			
			if(StringUtils.contains(url, Constant.enableUriDecodePass[i])){
				_pwd = getParamString(request, "_pwd");	
				_act = getParamString(request, "_act");	
				break;
			}
		}
		if(StringUtils.isBlank(get_pwd())){	
			client_id = getParamInt(request, ConstantClient.CLIENT_ID);		
			rut = getParamInt(request, ConstantClient.RUT);		 
			dv 	= getParamString(request, ConstantClient.DV);	
			type = getParamString(request, Constant.TYPE);
		}
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

	public int getClient_id() {
		return client_id;
	}

	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}
		
	public String get_act() {
		return _act;
	}

	public void set_act(String _act) {
		this._act = _act;
	}

	public String get_pwd() {
		return _pwd;
	}

	public void set_pwd(String _pwd) {
		this._pwd = _pwd;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void isValid() throws ExceptionInParam, GrabilityException {
		
		if(StringUtils.isBlank(get_pwd())){
			if(getClient_id() > 0){
				isValidClientById(getClient_id());
			}else if ( (getRut() != 0 && !Utils.isEmpty(getDv())) && Utils.verificarRutFO(getRut(), getDv().charAt(0))){
					isValidClientByRut(getRut());//lanza un excepcion si no existe			
			}else{
				if(getClient_id() == 0 && getRut() == 0){
					throw new ExceptionInParam(ConstantClient.SC_ID_CLIENTE_INVALIDO, ConstantClient.MSG_ID_CLIENTE_INVALIDO);
				}else if (getRut() == 0 || Utils.isEmpty(getDv()) || !Utils.verificarRutFO(getRut(), getDv().charAt(0))){
					throw new ExceptionInParam(ConstantClient.SC_RUT_INVALIDO, ConstantClient.MSG_RUT_INVALIDO);
				}else{
					throw new ExceptionInParam(ConstantClient.SC_ERROR_PARAM_CONSULTAR_CLIENTE, ConstantClient.MSG_ERROR_PARAM_CONSULTAR_CLIENTE);
				}
			}
			
			if (getType() == null || !Constant.SOURCE.equals(getType()))
				throw new ExceptionInParam(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
		}
	}

}
