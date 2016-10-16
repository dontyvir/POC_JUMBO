package cl.cencosud.jumbo.input.dto.Client;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.InputDTO;
import cl.cencosud.jumbo.util.RequestUtils;

public class PutInputClientDTO extends InputDTO implements Serializable{

	private static final long serialVersionUID = 3456536105220503521L;
	private int client_id; //unique identifier for the client",
	private String password; //": "user password hashed with base64 md5",
	private String new_password; //": "new user password hashed with base64 md5",
	private String type; //": "alphanumeric value representing the origin of the request",
	private String name; //": "name of the client",
	private String last_name; //": "last name of the client",
	private String email; //": "email of the client",
	private String phone_number; //": "client phone number",
	private int cod;
	private int number;	
	private String phone_number2; //": "phone number of the recevier of the purchase order",
	private int cod2;
	private int number2;
	private int newsletter; //": "indicates if the user is subscribed to jumbo jumbo newsletter"
	
		
	public PutInputClientDTO(HttpServletRequest request) throws ExceptionInParam, GrabilityException {
		super(request,ConstantClient.INTEGRATION_CODE_UPDATE_USER,null);
		
		this.client_id = getParamInt(request, ConstantClient.CLIENT_ID);
			
		this.password 		= RequestUtils.decoderPass(getParamString(request, ConstantClient.PASSWORD));
		this.new_password	= RequestUtils.decoderPass(getParamString(request, ConstantClient.NEW_PASSWORD));
		
		//this.password		= getParamString(request, ConstantClient.PASSWORD);
		//this.new_password	= getParamString(request, ConstantClient.NEW_PASSWORD);
		
		this.type = getParamString(request, Constant.TYPE);
		this.name = getParamString(request, ConstantClient.NAME);
		this.last_name = getParamString(request, ConstantClient.LAST_NAME);
		this.email = getParamString(request, ConstantClient.EMAIL);;
		this.phone_number = getParamString(request, ConstantClient.PHONE_NUMBER);
		this.phone_number2 = getParamString(request, ConstantClient.PHONE_NUMBER2);
		this.newsletter = getParamInt(request, ConstantClient.NEWSLETTER);
	
	}
	
	public int getClient_id() {
		return client_id;
	}
	
	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getNew_password() {
		return new_password;
	}

	public void setNew_password(String new_password) {
		this.new_password = new_password;
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
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public int getCod() {
		return cod;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

	public int getNumber() {
		return number;
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
	
	public int getCod2() {
		return cod2;
	}

	public void setCod2(int cod2) {
		this.cod2 = cod2;
	}

	public int getNumber2() {
		return number2;
	}

	public void setNumber2(int number2) {
		this.number2 = number2;
	}

	public int getNewsletter() {
		return newsletter;
	}

	public void setNewsletter(int newsletter) {
		this.newsletter = newsletter;
	}
	
	public void isValid() throws ExceptionInParam, GrabilityException{
		
		if (getClient_id() == 0)
			throw new ExceptionInParam(ConstantClient.SC_ID_CLIENTE_INVALIDO, ConstantClient.MSG_ID_CLIENTE_INVALIDO);
		
		isValidClientById(getClient_id());//lanza un excepcion si no existe
					
		//password
		/*if (Utils.isEmpty(getPassword()) || Utils.isEmpty(getNew_password()))
			throw new ExceptionInParam(ConstantClient.SC_PASSWORD_INVALIDO, ConstantClient.MSG_PASSWORD_INVALIDO);
		
		if (!Utils.isEmpty(getPassword()) && Utils.isEmpty(getNew_password()))
			throw new ExceptionInParam(ConstantClient.SC_PASSWORD_INVALIDO, ConstantClient.MSG_PASSWORD_INVALIDO);
		
		if ((!Utils.isEmpty(getPassword()) && !Utils.isEmpty(getNew_password())) && !(getPassword().equals(getNew_password())) )
			throw new ExceptionInParam(ConstantClient.SC_PASSWORD_INVALIDO, ConstantClient.MSG_PASSWORD_INVALIDO);*/
		
		//fin password
		
		//Valida que nombre no este vacio
		/*if (Utils.isEmpty(getName()))
			throw new ExceptionInParam(ConstantClient.SC_NOMBRE_CLIENTE_INVALIDO, ConstantClient.MSG_NOMBRE_CLIENTE_INVALIDO);*/
		
		//valida que apellido no este vacio
		/*if (Utils.isEmpty(getLast_name()))
			throw new ExceptionInParam(ConstantClient.SC_APELLIDO_CLIENTE_INVALIDO, ConstantClient.SC_APELLIDO_CLIENTE_INVALIDO);*/
		
		//valida que email no este vacio y cumpla con el patron
		/*if (!Utils.validateEmailFO(getEmail()))
			throw new ExceptionInParam(ConstantClient.SC_MAIL_CLIENTE_INVALIDO, ConstantClient.MSG_MAIL_CLIENTE_INVALIDO);*/
		
		if (!Utils.isEmpty(getPhone_number()) && !getPhone_number().matches("^\\d{1,2}-\\d{1,10}$")){
			throw new ExceptionInParam(ConstantClient.SC_TELEFONO1_CLIENTE_INVALIDO, ConstantClient.MSG_TELEFONO1_CLIENTE_INVALIDO);
		}else{
			String[] numberAux=getPhone_number().split("-");
			try{
				setCod(Integer.parseInt(numberAux[0]));
				setNumber(Integer.parseInt(numberAux[1]));
			}catch(Exception e){}
		}
		
		if (!Utils.isEmpty(getPhone_number2()) && !getPhone_number2().matches("^\\d{1,2}-\\d{1,10}$")){
			throw new ExceptionInParam(ConstantClient.SC_TELEFONO2_CLIENTE_INVALIDO, ConstantClient.MSG_TELEFONO2_CLIENTE_INVALIDO);
		}else{
			String[] numberAux2=getPhone_number2().split("-");
			try{
				setCod2(Integer.parseInt(numberAux2[0]));
				setNumber2(Integer.parseInt(numberAux2[1]));
			}catch(Exception e){}
			
		}
		
		if (getNewsletter() != 0 && getNewsletter() != 1){
			throw new ExceptionInParam(ConstantClient.SC_NEWSLETTER_CLIENTE_INVALIDO, ConstantClient.MSG_NEWSLETTER_CLIENTE_INVALIDO);
		}
		
		if (getType() == null || !Constant.SOURCE.equals(getType()))
			throw new ExceptionInParam(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
	}


}
