package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura  desde la Base de Datos los datos de un estado
 * @author bbr
 *
 */
public class EstadoEntity {
	private Character id;
	private String estado;
	private String tipo;
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
	 * @return Returns the id.
	 */
	public Character getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Character id) {
		this.id = id;
	}
	/**
	 * @return Returns the tipo.
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
