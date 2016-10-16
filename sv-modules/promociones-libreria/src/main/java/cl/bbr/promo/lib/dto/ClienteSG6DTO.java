package cl.bbr.promo.lib.dto;

import java.io.Serializable;

public class ClienteSG6DTO implements Serializable {
	
	
	private static final long serialVersionUID = 8124188254292708793L;
	
	private int id;
	private String rut;
	private String dv;
	private String email;
	private String nombre;
	private String apellido;
	private String codFono;
	private String telefono;
	private String modeloSamsung;
	private int id_modelo;
	private String direccion;
	private String comuna;
	private boolean registrado;
	
	/**
	 * @return el direccion
	 */
	public String getDireccion() {
		return direccion;
	}
	/**
	 * @param direccion el direccion a establecer
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	/**
	 * @return el comuna
	 */
	public String getComuna() {
		return comuna;
	}
	/**
	 * @param comuna el comuna a establecer
	 */
	public void setComuna(String comuna) {
		this.comuna = comuna;
	}
	/**
	 * @return el id_modelo
	 */
	public int getId_modelo() {
		return id_modelo;
	}
	/**
	 * @param id_modelo el id_modelo a establecer
	 */
	public void setId_modelo(int id_modelo) {
		this.id_modelo = id_modelo;
	}
	/**
	 * @return el modeloSamsung
	 */
	public String getModeloSamsung() {
		return modeloSamsung;
	}
	/**
	 * @param modeloSamsung el modeloSamsung a establecer
	 */
	public void setModeloSamsung(String modeloSamsung) {
		this.modeloSamsung = modeloSamsung;
	}
	/**
	 * @return el codFono
	 */
	public String getCodFono() {
		return codFono;
	}
	/**
	 * @param codFono el codFono a establecer
	 */
	public void setCodFono(String codFono) {
		this.codFono = codFono;
	}
	/**
	 * @return el id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id el id a establecer
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return el rut
	 */
	public String getRut() {
		return rut;
	}
	/**
	 * @return el registrado
	 */
	public boolean isRegistrado() {
		return registrado;
	}
	/**
	 * @param registrado el registrado a establecer
	 */
	public void setRegistrado(boolean registrado) {
		this.registrado = registrado;
	}
	/**
	 * @param rut el rut a establecer
	 */
	public void setRut(String rut) {
		this.rut = rut;
	}
	/**
	 * @return el dv
	 */
	public String getDv() {
		return dv;
	}
	/**
	 * @param dv el dv a establecer
	 */
	public void setDv(String dv) {
		this.dv = dv;
	}
	/**
	 * @return el email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email el email a establecer
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return el nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre el nombre a establecer
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return el apellido
	 */
	public String getApellido() {
		return apellido;
	}
	/**
	 * @param apellido el apellido a establecer
	 */
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	/**
	 * @return el telefono
	 */
	public String getTelefono() {
		return telefono;
	}
	/**
	 * @param telefono el telefono a establecer
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
}
