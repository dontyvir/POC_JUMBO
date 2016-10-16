/*
 * Creado el Aug 8, 2006
 *
 */
package cl.jumbo.ldap.process;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ResourceBundle;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;

import cl.jumbo.ldap.LDAPJumbo;

/**
 * @author Cristian Arriagada
 *
 */
public class AutenticacionLDAP {
	
	private String username;
	private String password;
	private String url1;
	private String url2;
	private String usuario;
	private String pass;
	
	ResourceBundle rb = ResourceBundle.getBundle("ldap");
	
	/**
	 * @param username nombre de usuario a autenticar
	 * @param password clave del usuario a autenticar
	 */
	public AutenticacionLDAP(String username,String password){
		this.usuario = username;
		this.pass = password;
		try {
			this.url1 = rb.getString("ldap.url1");
			this.url2 = rb.getString("ldap.url2");
			this.username = rb.getString("ldap.username");
			this.password = rb.getString("ldap.password");
		} catch (Exception e) {
			this.url1 = "ldap://192.168.50.132:389";
			this.url2 = "ldap://192.168.50.133:389";
			this.username = "_BackOfficeJumboCL";
			this.password = "4leclaco01";
		}
	}
/*	public AutenticacionLDAP(String username,String password){
		this.usuario = username;
		this.pass = password;
		try {
			javax.naming.InitialContext ctx = new javax.naming.InitialContext();
			String urljndi1 = "ldap://192.168.50.132:389"; //(String) ctx.lookup("url/ldap1");
			this.url1 = urljndi1;
			String urljndi2 = "ldap://192.168.50.133:389"; //(String) ctx.lookup("url/ldap2");
			this.url2 = urljndi2;
			String usuario = "_BackOfficeJumboCL"; //(String)ctx.lookup("username/ldap");
			this.username = usuario;
			String clave = "4leclaco01"; //(String)ctx.lookup("password/ldap");
			this.password = clave;
		} catch (NamingException e) {
			this.url1 = "ldap://192.168.50.132:389";
			this.url2 = "ldap://192.168.50.133:389";
			this.username = "_BackOfficeJumboCL";
			this.password = "4leclaco01";
		}
	}
*/	
	/**
	 * @return verifica si el usuario a autenticar es un usuario válido para LDAP.
	 * @throws NamingException
	 * @throws UnsupportedEncodingException
	 */
	public boolean isValid() throws NamingException, UnsupportedEncodingException, AuthenticationException, CommunicationException, NamingException {
		LDAPJumbo ldap = new LDAPJumbo(url1, url2, username, password, "5000");
		return ldap.esUsuarioValido(this.usuario, this.pass); 
	}
	
	public static void main(String[] args) throws NamingException, MalformedURLException, IOException {
		AutenticacionLDAP a = new AutenticacionLDAP("jsime", "octubrenov");
		//AutenticacionLDAP a = new AutenticacionLDAP("CArriagadaF", "juniojulio");
		//AutenticacionLDAP a = new AutenticacionLDAP("pcrowley", "10288374");
		//System.out.println(a.isValid());
		//System.out.println("fin");
	}
}
