package cl.bbr.fo.fonocompras.dto;

import java.io.Serializable;

/**
 * DTO para datos de los datos del ejecutivo. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class UsuarioDTO implements Serializable {

	private long id_usuario;
	private long id_local;
	private String login;
	private String pass;
	private String nombre;
	private String apellido_p;
	private String apellido_m;
	private String email;
	
	public String getApellido_m() {
		return apellido_m;
	}
	public void setApellido_m(String apellido_m) {
		this.apellido_m = apellido_m;
	}
	public String getApellido_p() {
		return apellido_p;
	}
	public void setApellido_p(String apellido_p) {
		this.apellido_p = apellido_p;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getId_local() {
		return id_local;
	}
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}
	public long getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(long id_usuario) {
		this.id_usuario = id_usuario;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
}