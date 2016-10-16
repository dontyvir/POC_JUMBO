package cl.bbr.jumbocl.contenidos.dto;

/**
 * Clase que muestra informacion de Precios SAP.
 * 
 * @author BBR
 *
 */
public class PreciosSapDTO {
	
	/**
	 * Id del local
	 */
	private Long id_loc;
	
	/**
	 * Id del producto
	 */
	private Long id_prod;
	
	/**
	 * Codigo del producto 1
	 */
	private String cod_prod_1;
	
	/**
	 * Codigo del local
	 */
	private String cod_local;
	
	/**
	 * Valor del precio
	 */
	private Double prec_valor;
	
	/**
	 * Unidad de medida
	 */
	private String uni_med;
	
	/**
	 * Codigo de barra
	 */
	private String cod_barra;
	
	/**
	 * Estado activo
	 */
	private String est_act;
	
	/**
	 * Constructor inicial.
	 */
	public PreciosSapDTO() {
	}
	
	/**
	 * @return cod_barra
	 */
	public String getCod_barra() {
		return cod_barra;
	}
	
	/**
	 * @return cod_local
	 */
	public String getCod_local() {
		return cod_local;
	}
	
	/**
	 * @return cod_prod_1
	 */
	public String getCod_prod_1() {
		return cod_prod_1;
	}
	
	/**
	 * @return est_act
	 */
	public String getEst_act() {
		return est_act;
	}
	
	/**
	 * @return id_loc
	 */
	public Long getId_loc() {
		return id_loc;
	}
	
	/**
	 * @return id_prod
	 */
	public Long getId_prod() {
		return id_prod;
	}
	
	/**
	 * @return prec_valor
	 */
	public Double getPrec_valor() {
		return prec_valor;
	}
	
	/**
	 * @return uni_med
	 */
	public String getUni_med() {
		return uni_med;
	}
	
	/**
	 * @param cod_barra , cod_barra a modificar.
	 */
	public void setCod_barra(String cod_barra) {
		this.cod_barra = cod_barra;
	}
	
	/**
	 * @param cod_local , cod_local a modificar.
	 */
	public void setCod_local(String cod_local) {
		this.cod_local = cod_local;
	}
	
	/**
	 * @param cod_prod_1 , cod_prod_1 a modificar.
	 */
	public void setCod_prod_1(String cod_prod_1) {
		this.cod_prod_1 = cod_prod_1;
	}
	
	/**
	 * @param est_act , est_act a modificar.
	 */
	public void setEst_act(String est_act) {
		this.est_act = est_act;
	}
	
	/**
	 * @param id_loc , id_loc a modificar.
	 */
	public void setId_loc(Long id_loc) {
		this.id_loc = id_loc;
	}
	
	/**
	 * @param id_prod , id_prod a modificar.
	 */
	public void setId_prod(Long id_prod) {
		this.id_prod = id_prod;
	}
	
	/**
	 * @param prec_valor , prec_valor a modificar.
	 */
	public void setPrec_valor(Double prec_valor) {
		this.prec_valor = prec_valor;
	}
	
	/**
	 * @param uni_med , uni_med a modificar.
	 */
	public void setUni_med(String uni_med) {
		this.uni_med = uni_med;
	}
	
	
	
}
