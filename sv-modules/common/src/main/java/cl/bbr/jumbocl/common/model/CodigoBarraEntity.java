package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la Base de Datos los datos del Código de Barra
 * @author bbr
 *
 */
public class CodigoBarraEntity {
	private String codigo;
	private String tip_cod;
	private Long id_prod;
	private String estado;
	/**
	 * @return Returns the codigo.
	 */
	public String getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
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
	 * @return Returns the id_prod.
	 */
	public Long getId_prod() {
		return id_prod;
	}
	/**
	 * @param id_prod The id_prod to set.
	 */
	public void setId_prod(Long id_prod) {
		this.id_prod = id_prod;
	}
	/**
	 * @return Returns the tip_cod.
	 */
	public String getTip_cod() {
		return tip_cod;
	}
	/**
	 * @param tip_cod The tip_cod to set.
	 */
	public void setTip_cod(String tip_cod) {
		this.tip_cod = tip_cod;
	}
}
