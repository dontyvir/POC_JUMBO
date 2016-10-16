package cl.bbr.vte.cotizaciones.dto;

/**
 * Clase que contiene la información de las marcas de los productos
 * 
 * @author BBR
 *
 */
import java.io.Serializable;

/**
 * DTO para datos de las Marcas. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class MarcaDTO implements Serializable {

	private long mar_id;
	private String mar_nombre;
	
	/**
	 * Constructor
	 */
	public MarcaDTO() {
	}

	public long getMar_id() {
		return mar_id;
	}

	public void setMar_id(long mar_id) {
		this.mar_id = mar_id;
	}

	public String getMar_nombre() {
		return mar_nombre;
	}

	public void setMar_nombre(String mar_nombre) {
		this.mar_nombre = mar_nombre;
	}
	
}