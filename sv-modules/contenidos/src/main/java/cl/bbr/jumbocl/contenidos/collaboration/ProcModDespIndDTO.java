package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

/**
 * Clase que contiene la informaci�n de indicaci�n del pedido a modificar.
 * @author BBR
 *
 */
public class ProcModDespIndDTO implements Serializable{
      
	/**
	 * Id del pedido. 
	 */
	private long id_pedido; // obligatorio
      
	/**
	 * Indicaci�n. 
	 */
	private String indicacion; // obligatorio
	
	/**
	 * Constructor inicial. 
	 */
	public ProcModDespIndDTO() {
	}
	
	/**
	 * @param id_pedido
	 * @param indicacion
	 */
	public ProcModDespIndDTO(long id_pedido , String indicacion) {
		
		this.id_pedido = id_pedido;
		this.indicacion = indicacion;
	}
	/**
	 * @return Retorna el id_pedido.
	 */
	public long getId_pedido() {
		return id_pedido;
	}
	/**
	 * @return Retorna indicacion.
	 */
	public String getIndicacion() {
		return indicacion;
	}
	/**
	 * @param id_pedido , id_pedido a modificar.
	 */
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	/**
	 * @param indicacion , indicacion a modificar.
	 */
	public void setIndicacion(String indicacion) {
		this.indicacion = indicacion;
	}
    
}
