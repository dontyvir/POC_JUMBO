package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la base de datos los datos para el motivo de despublicación
 * @author bbr
 *
 */
public class MotivosDespEntity {
	private long id_desp;
	private String motivo;
	private String estado;
	
	/**
	 * @return estado
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @return id_desp
	 */
	public long getId_desp() {
		return id_desp;
	}
	/**
	 * @return motivo
	 */
	public String getMotivo() {
		return motivo;
	}
	/**
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @param id_desp
	 */
	public void setId_desp(long id_desp) {
		this.id_desp = id_desp;
	}
	/**
	 * @param motivo
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

}
