/*
 * Creado el Aug 1, 2006
 *
 */
package cl.jumbo.ldap;

//import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
//import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;

import cl.jumbo.ldap.beans.UsuarioLDAP;

/**
 * @author Cristian Arriagada
 *
 */
public class LDAPJumbo {

	private static final String MEMBERS_GROUP = "cn=GG_BackOfficeJumbo_CL,ou=Appl Groups,ou=Central,ou=CL,dc=cencosud,dc=corp";
	private LdapContext ctx;
	private Hashtable env;
	private String url1;
	private String url2;
	private String username;
	private String password;
	static Logger logger = Logger.getLogger(LDAPJumbo.class);
	
	/**
	 * @param url1 URL principal del LDAP
	 * @param url2 URL alternativa del LDAP
	 * @param timeout tiempo de espera por la conección al LDAP
	 * @throws NamingException
	 */
	public LDAPJumbo(String url1, String url2, String username, String password, String timeout) throws NamingException {
		this.url1 = url1;
		this.url2 = url2;
		this.username = username;
		this.password = password;
		env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put("com.sun.jndi.ldap.connect.timeout", timeout);
	}
	
	/**
	 * @param username nombre de usuario
	 * @param password clave del usuario
	 * @return indica si el usuario es valido en LDAP y además si es miembro del grupo Jumbo.cl
	 */
	public boolean esUsuarioValido(String username, String password) throws AuthenticationException, CommunicationException, NamingException   {
		boolean valido = validaUsuario(username, password, 1);
		if (valido) {
			boolean miembro = existeMember(username,1);
			if (miembro){
				logger.info("El usuario '" + username + "' es un usuario válido y pertenece al grupo");
				return true;
			} else {
				logger.error("El usuario '" + username + "' es un usuario válido pero no pertenece al grupo");
				return false;
			}
		} else {
			logger.info("El usuario '" + username + "' no es un usuario válido");
			return false;
		}
	}
	
	/**
	 * @param username nombre de usuario para verificar
	 * @param password clave del usuario a verificar
	 * @param numURL indica si se usa URL principal o alternativa
	 * <li>1 - Principal</li>
	 * <li>2 - Alternativa</li> 
	 * @return indica si el usuario es un usuario válido o no.
	 */
	public boolean validaUsuario(String username, String password, int numURL) {
		logger.info("username : " + username);
		try {
			if ((username.equals("")) || (password.equals(""))) {
				logger.warn("Username o Password vacíos");
				return false;
			}
			if (numURL == 1)
				env.put(Context.PROVIDER_URL, url1);
			else
				env.put(Context.PROVIDER_URL, url2);
			env.put(Context.SECURITY_PRINCIPAL,"cencosud\\" + username);
			env.put(Context.SECURITY_CREDENTIALS, password);
			
			//Abre la conexión con el LDAP
			ctx = new InitialLdapContext(env,null);
			ctx.close();
			logger.info("Usuario " + username + " OK");
 	 		return true;
		} catch (AuthenticationException ae) {
			logger.error("Ocurrió un error al intentar autenticar al usuario '" + username + "'");
			String tempString;
			StringTokenizer tokenizerTemp = new StringTokenizer(ae.toString());
			while (tokenizerTemp.hasMoreElements()) {
				tempString = tokenizerTemp.nextToken();
				if (tempString.equalsIgnoreCase("AcceptSecurityContext")) {
					while (tokenizerTemp.hasMoreElements()) {
						tempString = tokenizerTemp.nextToken();
						if (tempString.startsWith("773")) { 
							logger.error("'" + username + "' : debe reiniciar su password");
						} else if (tempString.startsWith("52e")) {
							logger.error("'" + username + "' : password incorrecto");
						} else 	if (tempString.startsWith("533")) {
							logger.error("'" + username + "' : cuenta desabilitada");
						} else 	if (tempString.startsWith("525")) {
							logger.error("'" + username + "' : usuario no existe");
						} else 	if (tempString.startsWith("532")) {
							logger.error("'" + username + "' : la password ha expirado");
						} else 	if (tempString.startsWith("701")) {
							logger.error("'" + username + "' : la cuenta ha expirado");
						} else 	if (tempString.startsWith("530")) {
							logger.error("'" + username + "' : No está permitido loguearse en este momento");
						}	
					}
				}
			}
			return false;
		} catch (CommunicationException ce) {
			if (numURL == 1) {
				logger.error("Ocurrió un error de comunicación con el LDAP al intentar autenticar al usuario '" + username + "'"); 
				return validaUsuario(username, password, 2);
			} else {
				logger.error("No se pudo autentificar al usuario '" + username + "' por problemas de comunicación con el LDAP");
				return false;
			}
					
		} catch (NamingException ne){
			logger.error("Error al crear la conexión con LDAP al intentar autenticar al usuario '" + username + "'");
			return false;
		}
	}
	
	/**
	 * @param numurl indica si se usa URL principal o alternativa
	 * <li>1 - Principal</li>
	 * <li>2 - Alternativa</li>
	 * @return arreglo de UsuarioLDAP que contiene los usuarios que podrían usar la aplicación (no estan ordenados)
	 */
	public ArrayList getMembers(int numurl){
		ArrayList usuarios = new ArrayList();
		try {
			if (numurl == 1)
				env.put(Context.PROVIDER_URL, url1);
			else
				env.put(Context.PROVIDER_URL, url2);
			
			env.put(Context.SECURITY_PRINCIPAL, this.username);
			env.put(Context.SECURITY_CREDENTIALS, this.password);
			ctx = new InitialLdapContext(env,null);
			
			//Abre la conexión con el LDAP
			DirContext groupContext = (DirContext)this.ctx.lookup(MEMBERS_GROUP);
			
			Attributes atts = groupContext.getAttributes("");
			Attribute members = atts.get("member");
			if (members == null) {
                logger.warn("No hay miembros en el grupo");
                return null;
			}    
			NamingEnumeration result = members.getAll();
			while (result.hasMore()) {
				String userGridID = (String) result.next();
				String userGridIDaux=userGridID.replaceAll("\\\\,", "");
				String nombre = new String();
				String path = new String();
				if(userGridID.equals(userGridIDaux)){
					nombre = getUserUID(userGridID);
					path = getUserPath(userGridID);
				}else {
					String nombres[] = userGridID.split(",");
					nombre = (nombres[0].replaceAll("\\\\", "")+","+ nombres[1]);
					nombre = nombre.replaceAll("CN=", "");
					path = getUserPath(userGridIDaux);					
				}
				UsuarioLDAP usuario = getInfoUsuario(nombre,path);
				if (usuario != null){
					usuarios.add(usuario);
				}
				
			}
			ctx.close();
			logger.info("Se Consultaron " + usuarios.size() + " usuarios en el grupo");
			return usuarios;
		} catch (AuthenticationException ae) {
			logger.error("Ocurrió un error al intentar autenticar al usuario");
			return null;
		} catch (CommunicationException ce) {
			if (numurl == 1) {
				logger.error("Ocurrió un error de comunicación");
				return getMembers(2);
			} else {
				logger.error("No se pudieron rescatar los miebros por un error de comunicación");
				return null;
			}	
		} catch (NamingException e) {
			logger.error("Error al crear la conexión con LDAP");
			return null;
		}
	}
	
	/**
	 * @param username nombre de usuario del LDAP
	 * @param numurl indica si se usa URL principal o alternativa
	 * <li>1 - Principal</li>
	 * <li>2 - Alternativa</li>
	 * @return indica si es miembro del grupo o no
	 */
	public boolean existeMember(String username,int numurl){
		ArrayList usuarios = new ArrayList();
		try {
			if (numurl == 1)
				env.put(Context.PROVIDER_URL, url1);
			else
				env.put(Context.PROVIDER_URL, url2);
			env.put(Context.SECURITY_PRINCIPAL, this.username);
			env.put(Context.SECURITY_CREDENTIALS, this.password);
			ctx = new InitialLdapContext(env,null);
			
			//Abre la conexión con el LDAP
			DirContext groupContext = (DirContext)this.ctx.lookup(MEMBERS_GROUP);
			
			Attributes atts = groupContext.getAttributes("");
			Attribute members = atts.get("member");
			if (members == null) {
                logger.warn("No hay miembros en el grupo");
                return false;
			}    
			NamingEnumeration result = members.getAll();
			while (result.hasMore()) {
				String userGridID = (String) result.next();
				String userGridIDaux=userGridID.replaceAll("\\\\,", "");
				String nombre = new String();
				String path = new String();
				if(userGridID.equals(userGridIDaux)){
					nombre = getUserUID(userGridID);
					path = getUserPath(userGridID);
				}else {
					String nombres[] = userGridID.split(",");
					nombre = (nombres[0].replaceAll("\\\\", "")+","+ nombres[1]);
					nombre = nombre.replaceAll("CN=", "");
					path = getUserPath(userGridIDaux);
				}
				UsuarioLDAP usuario = getInfoUsuario(nombre,path);
				if ((usuario != null) && (usuario.getUsername().trim().toLowerCase().equals(username.trim().toLowerCase()))){
					logger.info("'" + username + "' es miembro del grupo");
					return true;
				}
			}
			ctx.close();
			logger.info("'" + username + "' NO es miebro del grupo");
			return false;
		} catch (AuthenticationException ae) {
			logger.error("Ocurrió un error al intentar autenticar al usuario del servicio");
			return false;
		} catch (CommunicationException ce) {
			if (numurl == 1) {
				logger.error("Ocurrió un error de comunicación");
				return existeMember(username,2);
			} else {
				logger.error("No se pudieron rescatar los miebros por un error de comunicación");
				return false;
			}	
		} catch (NamingException e) {
			logger.error("Error al crear la conexión con LDAP");
			return false;
		}
	}

	
	/**
	 * @param nombre nombre del usuario LDAP (cn)
	 * @param path path del usuario dentro del arbol LDAP
	 * @return objeto UsuarioLDAP con la información del usuario buscado
	 */
	private UsuarioLDAP getInfoUsuario(String nombre, String path) {
		UsuarioLDAP usuario = null;
		try {
			String filter = "(cn=" + nombre + ")";
			String[] attrIDs = {"sn", "givenName", "description", "telephonenumber", "userprincipalname", "department", "title", "company","sAMAccountName"};
			
		    SearchControls constraints = new SearchControls();
			constraints.setReturningAttributes(attrIDs);
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration results = ctx.search(path,filter,constraints);
			SearchResult sr = null;
			if (results.hasMore()) {
				sr = (SearchResult) results.next();
				Attributes attrs = sr.getAttributes();
				usuario = new UsuarioLDAP();
				if (attrs.get("givenName") != null) 
					usuario.setNombre(getValor(attrs.get("givenName").toString().trim()));
				if (attrs.get("sn") != null)
					usuario.setApellido(getValor(attrs.get("sn").toString().trim()));
				if (attrs.get("description") != null)
					usuario.setRut(getValor(attrs.get("description").toString().trim()));
				usuario.setUsername(getValor(attrs.get("sAMAccountName").toString().trim()));
				if (attrs.get("telephonenumber") != null)
					usuario.setTelefono(getValor(attrs.get("telephonenumber").toString().trim()));
				if (attrs.get("mail") != null)
					usuario.setEmail(getValor(attrs.get("mail").toString().trim()));
				if (attrs.get("department") != null)
					usuario.setDepartamento(getValor(attrs.get("department").toString().trim()));
				if (attrs.get("title") != null)
					usuario.setCargo(getValor(attrs.get("title").toString().trim()));
				if (attrs.get("company") != null)
					usuario.setCentroCosto(getValor(attrs.get("company").toString().trim()));
			}
			return usuario;
		} catch (AuthenticationException ae) {
			logger.error("Ocurrió un error al traer la información del usuario " + nombre);
			return null;
		} catch (CommunicationException ce) {
			logger.error("Ocurrió un error de comunicación al traer la información del usuario " + nombre);
			return null;
		} catch (NamingException e) {
			logger.error("Error al crear la conexión con LDAP para traer la información del usuario " + nombre);
			return null;
		}	
	}

	/**
	 * @param username nombre de usuario del LDAP
	 * @param numurl indica si se usa URL principal o alternativa
	 * <li>1 - Principal</li>
	 * <li>2 - Alternativa</li>
	 * @return Datos del usuario LDAP
	 */
	public UsuarioLDAP getInfoUsuarioByLogin(String username,int numurl){
		ArrayList usuarios = new ArrayList();
		try {
			if (numurl == 1)
				env.put(Context.PROVIDER_URL, url1);
			else
				env.put(Context.PROVIDER_URL, url2);
			env.put(Context.SECURITY_PRINCIPAL, this.username);
			env.put(Context.SECURITY_CREDENTIALS, this.password);
			ctx = new InitialLdapContext(env,null);
			
			//Abre la conexión con el LDAP
			DirContext groupContext = (DirContext)this.ctx.lookup(MEMBERS_GROUP);
			
			Attributes atts = groupContext.getAttributes("");
			Attribute members = atts.get("member");
			if (members == null) {
                logger.warn("No hay miembros en el grupo");
                return null;
			}    
			NamingEnumeration result = members.getAll();
			while (result.hasMore()) {
				String userGridID = (String) result.next();
				String userGridIDaux=userGridID.replaceAll("\\\\,", "");
				String nombre = new String();
				String path = new String();
				if(userGridID.equals(userGridIDaux)){
					nombre = getUserUID(userGridID);
					path = getUserPath(userGridID);
				}else {
					String nombres[] = userGridID.split(",");
					nombre = (nombres[0].replaceAll("\\\\", "")+","+ nombres[1]);
					nombre = nombre.replaceAll("CN=", "");
					path = getUserPath(userGridIDaux);
				}
				UsuarioLDAP usuario = getInfoUsuario(nombre,path);
				if ((usuario != null) && (usuario.getUsername().trim().toLowerCase().equals(username.trim().toLowerCase()))){
					logger.info("'" + username + "' es miembro del grupo");
					return usuario;
				}
			}
			ctx.close();
			logger.info("'" + username + "' NO es miebro del grupo");
			return null;
		} catch (AuthenticationException ae) {
			logger.error("Ocurrió un error al intentar autenticar al usuario del servicio");
			return null;
		} catch (CommunicationException ce) {
			if (numurl == 1) {
				logger.error("Ocurrió un error de comunicación");
				return getInfoUsuarioByLogin(username,2);
			} else {
				logger.error("No se pudieron rescatar los miebros por un error de comunicación");
				return null;
			}	
		} catch (NamingException e) {
			logger.error("Error al crear la conexión con LDAP");
			return null;
		}
	}
	
	
	/**
	 * @param atributo par atributo-valor
	 * @return valor del atributo
	 */
	private String getValor(String atributo){
		int start = atributo.indexOf(":");
		return atributo.substring(start+1, atributo.length()).trim();
	}
	
	/**
	 * @param userDN DN del usuario
	 * @return nombre del usuario (CN)
	 */ 
	private String getUserUID(String userDN) {
        int start = userDN.indexOf("=");
        int end = userDN.indexOf(",");

        if (end == -1) {
            end = userDN.length();
        }

        return userDN.substring(start+1, end).trim();
    }
	
	/**
	 * @param userDN DN del usuario
	 * @return path del usuario dentro del arbol LDAP
	 */
	private String getUserPath(String userDN) {
        int start = userDN.indexOf(",");
        return userDN.substring(start+1, userDN.length()).trim();
    }
}
