package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la base de datos los datos de un precio Sap
 * @author bbr
 *
 */
public class PrecioSapEntity {
	private Long id_loc;
	private Long id_prod;
	private String cod_prod_1;
	private String cod_local;
	private Double prec_valor;
	private String uni_med;
	private String cod_barra;
	private String est_act;
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
	 * @return Returns the cod_local.
	 */
	public String getCod_local() {
		return cod_local;
	}
	/**
	 * @param cod_local The cod_local to set.
	 */
	public void setCod_local(String cod_local) {
		this.cod_local = cod_local;
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
	 * @return Returns the est_act.
	 */
	public String getEst_act() {
		return est_act;
	}
	/**
	 * @param est_act The est_act to set.
	 */
	public void setEst_act(String est_act) {
		this.est_act = est_act;
	}
	/**
	 * @return Returns the id_loc.
	 */
	public Long getId_loc() {
		return id_loc;
	}
	/**
	 * @param id_loc The id_loc to set.
	 */
	public void setId_loc(Long id_loc) {
		this.id_loc = id_loc;
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
	 * @return Returns the prec_valor.
	 */
	public Double getPrec_valor() {
		return prec_valor;
	}
	/**
	 * @param prec_valor The prec_valor to set.
	 */
	public void setPrec_valor(Double prec_valor) {
		this.prec_valor = prec_valor;
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
