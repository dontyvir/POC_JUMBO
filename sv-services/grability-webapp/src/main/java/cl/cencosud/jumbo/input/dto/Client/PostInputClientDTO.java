package cl.cencosud.jumbo.input.dto.Client;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import cl.bbr.jumbocl.common.utils.Utils;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantAddress;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.InputDTO;
import cl.cencosud.jumbo.util.RequestUtils;

public class PostInputClientDTO extends InputDTO implements Serializable{

	private static final long serialVersionUID = 2487349299996873526L;
	private int rut; //": "unique user identifier",
	private String  dv; //": "unique user identifier",
	private String password; //": "user password hashed with base64 md5",   
	private String type; //": "alphanumeric value representing the origin of the request",
	private String name; //": "name of the client",
	private String last_name; //": "last name of the client",
	private String email; //": "email of the client",
	private String phone_number; //": "client phone number",
	private int cod;
	private int number;	
	private String phone_number2; //": "client phone number",
	private int cod2;
	private int number2;		
	private int municipality_id; //": "unique identifier for municipality (comuna)",
	private int region_id; //": "unique identifier for the region",
	private int newsletter; //": "indicates if the user is subscribed to jumbo jumbo newsletter"
	private String action;
	private String contextPath;

	public PostInputClientDTO(HttpServletRequest request) throws ExceptionInParam, GrabilityException, UnsupportedEncodingException{
		
		super(request,ConstantClient.INTEGRATION_CODE_AUTH_USU_INSERT_GUEST_LOGIN,null);
		
		rut 			= getParamInt(request, ConstantClient.RUT);		 
		dv 				= getParamString(request, ConstantClient.DV);	
		
		//Con URLDecoder.decode
		String pass = getParamString(request, ConstantClient.PASSWORD);		
		if(!StringUtils.isBlank(pass)){		
			String passDC 	= URLDecoder.decode(pass, Constant.ENCODING);
			password		= RequestUtils.decoderPass(passDC);
		}
		
		
		//Sin URLDecoder.decode
		//password		= RequestUtils.decoderPass(getParamString(request, ConstantClient.PASSWORD));
		
		type 			= getParamString(request, Constant.TYPE);		
		name 			= getParamString(request, ConstantClient.NAME);
		last_name 		= getParamString(request, ConstantClient.LAST_NAME);
		email 			= getParamString(request, ConstantClient.EMAIL);
		phone_number 	= getParamString(request, ConstantClient.PHONE_NUMBER);
		phone_number2	= getParamString(request, ConstantClient.PHONE_NUMBER2);
		municipality_id = getParamInt(request, ConstantAddress.MUNICIPALITY_ID);
		region_id 		= getParamInt(request, ConstantAddress.REGION_ID);
		newsletter 		= getParamInt(request, ConstantClient.NEWSLETTER);
		action 			= getParamString(request, Constant.ACTION);
		contextPath		= RequestUtils.requestToServerStringBuffer(request).toString()+"/FO";
		
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

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
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
	
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
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

	public void setNumber(int number) {
		this.number = number;
	}

	public String getPhone_number2() {
		return phone_number2;
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

	public void setPhone_number2(String phone_number2) {
		this.phone_number2 = phone_number2;
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
	
	public int getNewsletter() {
		return newsletter;
	}
	
	public void setNewsletter(int newsletter) {
		this.newsletter = newsletter;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public void isValid() throws ExceptionInParam, GrabilityException {
		
		if(ConstantClient.INSERT.equals(this.getAction())){
			
			if (getRut() == 0 || Utils.isEmpty(getDv()) || !Utils.verificarRutFO(getRut(), getDv().charAt(0)))
				throw new ExceptionInParam(ConstantClient.SC_RUT_INVALIDO, ConstantClient.MSG_RUT_INVALIDO);
			
			boolean rutExiste = false;
			try {//Si cliente existe, no es valido para ser creado
				rutExiste = isValidClientByRut(getRut());//lanza un excepcion si no existe
			} catch (ExceptionInParam e) {}			
			
			if(rutExiste)
				throw new ExceptionInParam(ConstantClient.SC_CLIENTE_EXISTE_AL_INSERTAR, ConstantClient.MSG_CLIENTE_EXISTE_AL_INSERTAR);
			
			if (Utils.isEmpty(getPassword()))
				throw new ExceptionInParam(ConstantClient.SC_PASSWORD_INVALIDO, ConstantClient.MSG_PASSWORD_INVALIDO);
			
			if (Utils.isEmpty(getName()))
				throw new ExceptionInParam(ConstantClient.SC_NOMBRE_CLIENTE_INVALIDO, ConstantClient.MSG_NOMBRE_CLIENTE_INVALIDO);
			
			if (Utils.isEmpty(getLast_name()))
				throw new ExceptionInParam(ConstantClient.SC_APELLIDO_CLIENTE_INVALIDO, ConstantClient.SC_APELLIDO_CLIENTE_INVALIDO);
			
			if (Utils.isEmpty(getEmail()) || !Utils.validateEmailFO(getEmail()))
				throw new ExceptionInParam(ConstantClient.SC_MAIL_CLIENTE_INVALIDO, ConstantClient.MSG_MAIL_CLIENTE_INVALIDO);
			
			if (Utils.isEmpty(getPhone_number()) || !getPhone_number().matches("^\\d{1,2}-\\d{1,10}$")){
				throw new ExceptionInParam(ConstantClient.SC_TELEFONO1_CLIENTE_INVALIDO, ConstantClient.MSG_TELEFONO1_CLIENTE_INVALIDO);
			}else{
				String[] numberAux=getPhone_number().split("-");
				try{
					setCod(Integer.parseInt(numberAux[0]));
					setNumber(Integer.parseInt(numberAux[1]));
				}catch(Exception e){}
			}
			
			/*
			if (Utils.isEmpty(getPhone_number2()) || !getPhone_number2().matches("^\\d{1,2}-\\d{1,10}$")){
				throw new ExceptionInParam(ConstantClient.SC_TELEFONO2_CLIENTE_INVALIDO, ConstantClient.MSG_TELEFONO2_CLIENTE_INVALIDO);
			}else{
				String[] numberAux2=getPhone_number2().split("-");
				try{
					setCod2(Integer.parseInt(numberAux2[0]));
					setNumber2(Integer.parseInt(numberAux2[1]));
				}catch(Exception e){}				
			}*/
			
			//Si comuna existe esta ok el id enviado.
			isValidComunaById(getMunicipality_id());//lanza un excepcion si no existe
			
			//Si comuna existe esta ok el id enviado.
			isValidRegionById(getRegion_id());//lanza un excepcion si no existe
						
			if (getNewsletter() != 0 && getNewsletter() != 1){
				throw new ExceptionInParam(ConstantClient.SC_NEWSLETTER_CLIENTE_INVALIDO, ConstantClient.MSG_NEWSLETTER_CLIENTE_INVALIDO);
			}
			
			if (getType() == null || !Constant.SOURCE.equals(getType()))
				throw new ExceptionInParam(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
			
		}else if(ConstantClient.RECOVER.equals(this.getAction())){
			
			if (getRut() == 0)
				throw new ExceptionInParam(ConstantClient.SC_RUT_INVALIDO, ConstantClient.MSG_RUT_INVALIDO);			

			//Si cliente NO existe no es valido para recuperar contraseña.
			isValidClientByRut(getRut());//lanza un excepcion si no existe
						
			if (getType() == null || !Constant.SOURCE.equals(getType()))
				throw new ExceptionInParam(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
		}
		else if(ConstantClient.GUEST.equals(this.getAction())){
			
			if (getRut() == 0 || !ConstantClient.RUT_INVITADO.equals(String.valueOf(getRut())))
				throw new ExceptionInParam(ConstantClient.SC_RUT_INVALIDO, ConstantClient.MSG_RUT_INVALIDO);				
						
			//Si comuna existe esta ok el id enviado.
			isValidComunaById(getMunicipality_id());//lanza un excepcion si no existe
			
			//Si comuna existe esta ok el id enviado.
			isValidRegionById(getRegion_id());//lanza un excepcion si no existe
			
			if (getType() == null || !Constant.SOURCE.equals(getType()))
				throw new ExceptionInParam(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
		}
		
		else if(ConstantClient.LOGIN.equals(this.getAction())){
			
			if (getRut() == 0 || Utils.isEmpty(getDv()) || !Utils.verificarRutFO(getRut(), getDv().charAt(0)))
				throw new ExceptionInParam(ConstantClient.SC_RUT_INVALIDO, ConstantClient.MSG_RUT_INVALIDO);
			
			//Si cliente NO existe no es valido para recuperar contraseña.
			isValidClientByRut(getRut());//lanza un excepcion si no existe			

			if (Utils.isEmpty(getPassword()))
				throw new ExceptionInParam(ConstantClient.SC_PASSWORD_INVALIDO, ConstantClient.MSG_PASSWORD_INVALIDO);
			
			if (getType() == null || !Constant.SOURCE.equals(getType()))
				throw new ExceptionInParam(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
		}
		
		else{
			throw new ExceptionInParam(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
		}		
	}

}
