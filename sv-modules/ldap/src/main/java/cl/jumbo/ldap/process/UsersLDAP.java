/*
 * Creado el Aug 8, 2006
 *
 */
package cl.jumbo.ldap.process;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.naming.NamingException;

import cl.jumbo.ldap.LDAPJumbo;
import cl.jumbo.ldap.beans.UsuarioLDAP;

/**
 * @author Cristian Arriagada
 *
 */
public class UsersLDAP {
	
	private String username;
	private String password;
	private String url1;
	private String url2;
	
	ResourceBundle rb = ResourceBundle.getBundle("ldap");
	
	public UsersLDAP(){
		try {
			this.url1 = rb.getString("ldap.url1");
			this.url2 = rb.getString("ldap.url2");
			this.username = rb.getString("ldap.username");
			this.password = rb.getString("ldap.password");
		} catch (Exception e) {
			this.username = "_BackOfficeJumboCL";
			this.password = "4leclaco01";
			this.url1 = "ldap://192.168.50.132:389";
			this.url2 = "ldap://192.168.50.133:389";
		}
	}

/*	public UsersLDAP(){
		try {
			javax.naming.InitialContext ctx = new javax.naming.InitialContext();
			String urljndi1 = (String) ctx.lookup("url/ldap1");
			this.url1 = urljndi1;
			String urljndi2 = (String) ctx.lookup("url/ldap2");
			this.url2 = urljndi2;
			String usuario = (String)ctx.lookup("username/ldap");
			this.username = usuario;
			String clave = (String)ctx.lookup("password/ldap");
			this.password = clave;
		} catch (NamingException e) {
			this.username = "_BackOfficeJumboCL";
			this.password = "4leclaco01";
			this.url1 = "ldap://192.168.50.132:389";
			this.url2 = "ldap://192.168.50.133:389";
		}
	}
*/	
	/**
	 * @return obtiene la lista de usuarios existentes en LDAP
	 * @throws NamingException
	 * @throws UnsupportedEncodingException
	 */
	public ArrayList getUsers() throws NamingException, UnsupportedEncodingException{
		LDAPJumbo ldap = new LDAPJumbo(url1, url2, this.username, this.password, "5000");
		ArrayList usuarios = ldap.getMembers(1);
		return usuarios; 
	}
	
	public UsuarioLDAP getInfoUsuarioByLogin(String username) throws NamingException, UnsupportedEncodingException {
		LDAPJumbo ldap = new LDAPJumbo(url1, url2, this.username, this.password, "5000");
		UsuarioLDAP usuario = ldap.getInfoUsuarioByLogin(username,1);
		return usuario;
	}
	
	public static void main(String[] args) throws NamingException, MalformedURLException, IOException {
		UsersLDAP a = new UsersLDAP();
		//a.getUsers();
		UsuarioLDAP u = a.getInfoUsuarioByLogin("jsime");
		//System.out.println(u);
	}
	
}
