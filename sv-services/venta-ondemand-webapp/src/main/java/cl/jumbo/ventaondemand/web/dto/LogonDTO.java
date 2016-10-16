package cl.jumbo.ventaondemand.web.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import cl.jumbo.ventaondemand.exceptions.JsonException;
import cl.jumbo.ventaondemand.utils.PropertyUtil;

public class LogonDTO implements Serializable {

private static final long serialVersionUID = 1L;
	
	private final String rut;
	private final String dv;
	private final String pass;
	
	public static class Builder{
		private String rut;
		private String dv;	
		private String pass;
			
		/*
		public Builder(String rut, String pass){
			this.rut = rut;
			this.pass = pass;
		}
		public Builder(String rut, String dv, String pass){
			this.rut = rut;
			//this.dv = dv;
			this.pass = pass;
		}
		*/
		public Builder rut(String rut){
			String dv = rut.substring(rut.indexOf('-')+1, rut.length());
			rut = rut.replaceAll("\\.","").replaceAll("\\-","");
			rut = rut.substring(0, rut.length()-1);            
			this.rut = rut;
			this.dv = dv;
			return this;
		}
		
		public Builder pass(String pass){
			this.pass = pass;
			return this;
		}
		
		public LogonDTO build() throws JsonException, Exception{
			LogonDTO logon = new LogonDTO(this);
			validateUserObject (logon);
			return logon;
		}
		
		private void validateUserObject(LogonDTO logon) throws JsonException, Exception{
			if(logon==null){
				 throw new JsonException("Logon object is null.");
			}
			Properties prop = PropertyUtil.getInstance().getProperties();

			String[] rutProhidos = prop.getProperty("rut_prohibidos").split(",");
			java.util.Arrays.sort(rutProhidos);
			if(rutProhidos.length>0){
				if(Arrays.binarySearch(rutProhidos, logon.getRut()) >= 0){
					throw new JsonException("Rut no esta permitido.");
				}
			}
			if(StringUtils.isEmpty(logon.getRut())){
				throw new JsonException("Rut es blanco.");
			}			
			if(!( (logon.getRut().length()>6) && (logon.getRut().length()<9))){
				throw new JsonException("Largo del rut invalido.");
			}
			if (StringUtils.isEmpty(logon.getPass())){
				throw new JsonException("Password es blanco.");
			}
			if(logon.getPass().length()<=6){
				throw new JsonException("Largo del password debe ser minimo a 6 caracteres.");
			}
			if(!ValidarRut(Integer.parseInt(logon.getRut()), logon.getDv().charAt(0))){
				throw new JsonException("Rut no es valido. [" + logon.getRut()+"-"+logon.getDv()+"]");
			}
		}
		
		/*
	     * Método Estático que valida si un rut es válido 
	     */
		public boolean ValidarRut(int rut, char dv){
			int m = 0, s = 1;
			for (; rut != 0; rut /= 10){
				s = (s + rut % 10 * (9 - m++ % 6)) % 11;
			}
			return dv == (char) (s != 0 ? s + 47 : 75);
		}
	}
	
	private LogonDTO(Builder builder){
		this.rut = builder.rut;
		this.pass = builder.pass;
		this.dv = builder.dv;
	}
		
	public String getRut() {
		return rut;
	}

	public String getPass() {
		return pass;
	}
	
	public String getDv() {
		return dv;
	}
}
