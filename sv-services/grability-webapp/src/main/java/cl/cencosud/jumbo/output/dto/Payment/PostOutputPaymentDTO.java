package cl.cencosud.jumbo.output.dto.Payment;

import java.io.Serializable;

import net.sf.json.JSONObject;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantPayment;
import cl.cencosud.jumbo.output.dto.OutputDTO;

public class PostOutputPaymentDTO extends OutputDTO implements Serializable {

	private static final long serialVersionUID = -6099658680662264018L;

	private String redirect_url;//"payment redirect url, to ben shown on the embedded webview",
	private String success_url;//"success url",
	private String error_url;//"error url" 
	private String token;//"hash validacion" 
	
	public PostOutputPaymentDTO(String status, String error_message, String redirect_url, String success_url,
			String error_url, String token) {
		setStatus(status);
		setError_message(error_message);
		this.redirect_url = redirect_url;
		this.success_url = success_url;
		this.error_url = error_url;
		this.token = token;
	}
	public PostOutputPaymentDTO(){}

	public String getRedirect_url() {
		return redirect_url;
	}

	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}

	public String getSuccess_url() {
		return success_url;
	}

	public void setSuccess_url(String success_url) {
		this.success_url = success_url;
	}

	public String getError_url() {
		return error_url;
	}

	public void setError_url(String error_url) {
		this.error_url = error_url;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
		
	public String toJson() {
		
		JSONObject obj	= new JSONObject();			
		
		obj.put(Constant.STATUS , String.valueOf(this.getStatus()));
		obj.put(Constant.ERROR_MESSAGE , String.valueOf(this.getError_message()));
		obj.put(ConstantPayment.REDIRECT_URL , String.valueOf(this.getRedirect_url()));
		obj.put(ConstantPayment.SUCCESS_URL , this.getSuccess_url());				
		obj.put(ConstantPayment.ERROR_URL, this.getError_url());
		obj.put(ConstantPayment.TOKEN, this.getToken());
		
		return obj.toString();
	}

}
