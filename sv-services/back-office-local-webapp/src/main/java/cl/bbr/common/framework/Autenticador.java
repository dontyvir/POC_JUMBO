package cl.bbr.common.framework;

import java.io.UnsupportedEncodingException;

import javax.naming.NamingException;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.jumbo.ldap.process.AutenticacionLDAP;


public class Autenticador {

	private String	login;
	private String	pass;
	private String	errMsg;
	private String	errCode;
	
	/**
	 * Constructor
	 *
	 */
	public Autenticador(){
		
	}
	
	/*
	 * M�todos privados
	 */
	private void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	
	private void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
		
	
	/*
	 * M�todos p�blicos
	 */
	public String getErrCode() {
		return errCode;
	}
	
	public String getErrMsg() {
		return errMsg;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPass() {
		return pass;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	/*
	 * M�todos con acciones
	 */
	
	/**
	 * Realiza proceso de autenticaci�n con Base de Datos Local
	 * @param paramLogin String login del usuario
	 * @param paramPass String password del usuario
	 * @throws SystemException 
	 */
	public boolean doAuth(String paramLogin, String paramPass) throws SystemException{
		
		this.login 	= paramLogin;
		this.pass	= paramPass;
		boolean result = false; 
		
		
		BizDelegate biz = new BizDelegate();
		
		try {
			result = biz.doAutenticaUser(paramLogin, paramPass);
			
			if ( result ){
				return true;
			}
			else
			{
				errCode = "1";
				errMsg	= "Usuario y/o Password incorrecto";
				return false;	
			}			
			
		} catch (BolException e) {
			e.printStackTrace();
			
			errCode = "2";
			errMsg	= "Error inesperado";
			return false;
			
		}
		

		
	}
	
	
	/**
	 * Realiza proceso de autenticaci�n contra LDAP
	 * @param paramLogin String login del usuario
	 * @param paramPass String password del usuario
	 * @throws SystemException 
	 */
	public boolean doLDAPAuth(String paramLogin, String paramPass) {
		
		this.login 	= paramLogin;
		this.pass	= paramPass;
		boolean result = false; 
		
		AutenticacionLDAP aut = new AutenticacionLDAP(paramLogin, paramPass);
		
		try {
			result = aut.isValid();
			
			if ( result ){
				return true;
			}
			else
			{
				errCode = "1";
				errMsg	= "Usuario y/o Password incorrecto";
				return false;	
			}			
						
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

			errCode = "2";
			errMsg	= "Error inesperado";
			return false;
			
		} catch (NamingException e) {
			e.printStackTrace();

			errCode = "2";
			errMsg	= "Error inesperado";
			return false;
			
		}
		

		
	}
	
	
		
}
