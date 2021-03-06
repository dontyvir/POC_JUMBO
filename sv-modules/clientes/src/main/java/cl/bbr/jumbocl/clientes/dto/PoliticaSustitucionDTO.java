package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;

/**
 * DTO para datos de las políticas de sustitución. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class PoliticaSustitucionDTO implements Serializable {

	private long id;
	private String nombre;
	private String descripcion;
	private String estado;
	private String seleccion;
	/**
	 * Constructor
	 */
	public PoliticaSustitucionDTO() {
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getSeleccion() {
		return seleccion;
	}
	public void setSeleccion(String seleccion) {
		this.seleccion = seleccion;
	}
	

	
}