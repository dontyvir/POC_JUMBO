package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;

/**
 * Clase que muestra informacion de motivos de despublicacion.
 * 
 * @author BBR
 *
 */

public class MotivosDespDTO implements Serializable{
	
	/**
	 * Id del motivo 
	 */
	private long id;
	
	/**
	 * Motivo de despublicacion
	 */
	private String motivo;
	
	/**
	 * Estado
	 */
	private String estado;
	
	/**
	 * 
	 */
	public MotivosDespDTO() {
		super();

	}
	
	/**
	 * @param id
	 * @param motivo
	 * @param estado
	 */
	public MotivosDespDTO(long id , String motivo , String estado) {
		super();
		this.id = id;
		this.motivo = motivo;
		this.estado = estado;
	}
	
	/**
	 * @return estado
	 */
	public String getEstado() {
		return estado;
	}
	
	/**
	 * @return id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @return motivo
	 */
	public String getMotivo() {
		return motivo;
	}
	
	/**
	 * @param estado , estado a modificar.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * @param id , id a modificar.
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * @param motivo , motivo a modificar.
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	

}
