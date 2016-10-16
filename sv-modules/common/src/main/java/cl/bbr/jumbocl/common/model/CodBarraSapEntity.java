package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la Base de Datos los códigos de barra SAP
 * @author bbr
 *
 */
public class CodBarraSapEntity {
	private String cod_prod_1;
	private String cod_barra;
	private String tip_cod_barra;
	private String cod_ppal;
	private String uni_med;
	private Long id_prod;
	private String estado;
	/**
	 * @return Returns the cod_barra.
	 */
	public String getCod_barra() {
		return cod_barra;
	}
	/**
	 * @param cod_barra The cod_barra to set.
	 */
	public void setCod_barra(String cod_barra) {
		this.cod_barra = cod_barra;
	}
	/**
	 * @return Returns the cod_ppal.
	 */
	public String getCod_ppal() {
		return cod_ppal;
	}
	/**
	 * @param cod_ppal The cod_ppal to set.
	 */
	public void setCod_ppal(String cod_ppal) {
		this.cod_ppal = cod_ppal;
	}
	/**
	 * @return Returns the cod_prod_1.
	 */
	public String getCod_prod_1() {
		return cod_prod_1;
	}
	/**
	 * @param cod_prod_1 The cod_prod_1 to set.
	 */
	public void setCod_prod_1(String cod_prod_1) {
		this.cod_prod_1 = cod_prod_1;
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
	 * @return Returns the tip_cod_barra.
	 */
	public String getTip_cod_barra() {
		return tip_cod_barra;
	}
	/**
	 * @param tip_cod_barra The tip_cod_barra to set.
	 */
	public void setTip_cod_barra(String tip_cod_barra) {
		this.tip_cod_barra = tip_cod_barra;
	}
	/**
	 * @return Returns the uni_med.
	 */
	public String getUni_med() {
		return uni_med;
	}
	/**
	 * @param uni_med The uni_med to set.
	 */
	public void setUni_med(String uni_med) {
		this.uni_med = uni_med;
	}
}
