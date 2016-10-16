package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;

/**
 * DTO para datos de las formas de pago. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class FormaPagoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6710843785768439648L;
	private long id;
	private String nombre;
	private String estado;
	/**
	 * Constructor
	 */
	public FormaPagoDTO() {
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
	
}