/*
 * Creado el Aug 8, 2006
 *
 */
package cl.jumbo.ldap.beans;

/**
 * @author Cristian Arriagada
 *
 */
public class UsuarioLDAP {

	private String nombre;
	private String apellido;
	private String rut;
	private String telefono;
	private String email;
	private String cargo;
	private String departamento;
	private String centroCosto;
	private String username;
	
	
	/**
	 * @return Devuelve apellido.
	 */
	public String getApellido() {
		return apellido;
	}
	/**
	 * @return Devuelve cargo.
	 */
	public String getCargo() {
		return cargo;
	}
	/**
	 * @return Devuelve centroCosto.
	 */
	public String getCentroCosto() {
		return centroCosto;
	}
	/**
	 * @return Devuelve departamento.
	 */
	public String getDepartamento() {
		return departamento;
	}
	/**
	 * @return Devuelve email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @return Devuelve nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @return Devuelve rut.
	 */
	public String getRut() {
		return rut;
	}
	/**
	 * @return Devuelve telefono.
	 */
	public String getTelefono() {
		return telefono;
	}
	/**
	 * @param apellido El apellido a establecer.
	 */
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	/**
	 * @param cargo El cargo a establecer.
	 */
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	/**
	 * @param centroCosto El centroCosto a establecer.
	 */
	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}
	/**
	 * @param departamento El departamento a establecer.
	 */
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	/**
	 * @param email El email a establecer.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @param nombre El nombre a establecer.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @param rut El rut a establecer.
	 */
	public void setRut(String rut) {
		this.rut = rut;
	}
	/**
	 * @param telefono El telefono a establecer.
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	/**
	 * @return Devuelve username.
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username El username a establecer.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	public String toString(){
		return "nombre : " + this.nombre + "\n" +
			   "apellido : " + this.apellido + "\n" +
			   "RUT : " + this.rut + "\n" +
			   "Username : " + this.username + "\n" +
			   "Telefono : " + this.telefono + "\n" +
			   "Email : " + this.email + "\n" +
			   "Cargo : " + this.cargo + "\n" +
			   "Departamento : " + this.departamento + "\n" +
			   "Centro Costo : " + this.centroCosto + "\n"
			   ;
	}
}
