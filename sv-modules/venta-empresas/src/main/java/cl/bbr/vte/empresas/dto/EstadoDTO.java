package cl.bbr.vte.empresas.dto;

/**
 * DTO para datos de los estados 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class EstadoDTO {

	private String id;
	private String estado;
	private String tipo;
	private String visible;
	
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
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
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
	/**
	 * @return Returns the visible.
	 */
	public String getVisible() {
		return visible;
	}
	/**
	 * @param visible The visible to set.
	 */
	public void setVisible(String visible) {
		this.visible = visible;
	}

	
}
