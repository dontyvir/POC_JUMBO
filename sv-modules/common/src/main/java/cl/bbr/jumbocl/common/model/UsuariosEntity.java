package cl.bbr.jumbocl.common.model;

import java.io.Serializable;
import java.util.List;


/**
 * Clase que captura desde la base de datos los datos del usuario
 * @author bbr
 *
 */
public class UsuariosEntity implements Serializable
{ 
   private long idUsuario;
   private String login;
   private String pass;
   private String nombre;
   private String ape_paterno;
   private String ape_materno;
   private String email;
   private long id_local;
   private String estado;
   private long id_pedido;
   private String local;
   private long id_cotizacion;
   
   private PerfilesEntity[] RefUxpPerfs;
   private List perfiles;
   private List locales;
   
	/**
	 * @return Returns the ape_materno.
	 */
	public String getApe_materno() {
		return ape_materno;
	}
	/**
	 * @param ape_materno The ape_materno to set.
	 */
	public void setApe_materno(String ape_materno) {
		this.ape_materno = ape_materno;
	}
	/**
	 * @return Returns the ape_paterno.
	 */
	public String getApe_paterno() {
		return ape_paterno;
	}
	/**
	 * @param ape_paterno The ape_paterno to set.
	 */
	public void setApe_paterno(String ape_paterno) {
		this.ape_paterno = ape_paterno;
	}
	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return Returns the id_local.
	 */
	public long getId_local() {
		return id_local;
	}
	/**
	 * @param id_local The id_local to set.
	 */
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}
	/**
	 * @return Returns the idUsuario.
	 */
	public long getIdUsuario() {
		return idUsuario;
	}
	/**
	 * @param idUsuario The idUsuario to set.
	 */
	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}
	/**
	 * @return Returns the login.
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login The login to set.
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @return Returns the pass.
	 */
	public String getPass() {
		return pass;
	}
	/**
	 * @param pass The pass to set.
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return Returns the perfiles.
	 */
	public List getPerfiles() {
		return perfiles;
	}
	/**
	 * @param perfiles The perfiles to set.
	 */
	public void setPerfiles(List perfiles) {
		this.perfiles = perfiles;
	}
	
	/**
	 * @return id_pedido
	 */
	public long getId_pedido() {
		return id_pedido;
	}
	/**
	 * @param id_pedido
	 */
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	/**
	 * @return local
	 */
	public String getLocal() {
		return local;
	}
	/**
	 * @param local
	 */
	public void setLocal(String local) {
		this.local = local;
	}
	/**
	 * @return List
	 */
	public List getLocales() {
		return locales;
	}
	/**
	 * @param locales
	 */
	public void setLocales(List locales) {
		this.locales = locales;
	}
	/**
	 * @return Returns the id_cotizacion.
	 */
	public long getId_cotizacion() {
		return id_cotizacion;
	}
	/**
	 * @param id_cotizacion The id_cotizacion to set.
	 */
	public void setId_cotizacion(long id_cotizacion) {
		this.id_cotizacion = id_cotizacion;
	}
	

}