package cl.bbr.jumbocl.contenidos.dto;

/**
 * Clase que muestra informacion de Codigo de barras Sap.
 * 
 * @author BBR
 *
 */
public class CodigosBarraSapDTO {
	
	/**
	 * Codigo de producto SAP 
	 */
	private String cod_prod_1;
	
	/**
	 * Codigo de barra 
	 */
	private String cod_barra;
	
	/**
	 * Tipo de codigo de barra
	 */
	private String tip_cod_barra;
	
	/**
	 * Codigo Pal 
	 */
	private String cod_ppal;
	
	/**
	 * Unidad de medida
	 */
	private String uni_med;
	
	/**
	 * Id del producto 
	 */
	private Long id_prod;
	
	/**
	 * Estado
	 */
	private String estado;
	
	/**
	 * @return cod_barra
	 */
	public String getCod_barra() {
		return cod_barra;
	}
	
	/**
	 * @return cod_ppal
	 */
	public String getCod_ppal() {
		return cod_ppal;
	}
	
	/**
	 * @return cod_prod_1
	 */
	public String getCod_prod_1() {
		return cod_prod_1;
	}
	
	/**
	 * @return estado
	 */
	public String getEstado() {
		return estado;
	}
	
	/**
	 * @return id_prod
	 */
	public Long getId_prod() {
		return id_prod;
	}
	
	/**
	 * @return tip_cod_barra
	 */
	public String getTip_cod_barra() {
		return tip_cod_barra;
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
	 * @param cod_ppal , cod_ppal a modificar.
	 */
	public void setCod_ppal(String cod_ppal) {
		this.cod_ppal = cod_ppal;
	}
	
	/**
	 * @param cod_prod_1 , cod_prod_1 a modificar.
	 */
	public void setCod_prod_1(String cod_prod_1) {
		this.cod_prod_1 = cod_prod_1;
	}
	
	/**
	 * @param estado , estado a modificar.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * @param id_prod , id_prod a modificar.
	 */
	public void setId_prod(Long id_prod) {
		this.id_prod = id_prod;
	}
	
	/**
	 * @param tip_cod_barra , tip_cod_barra a modificar.
	 */
	public void setTip_cod_barra(String tip_cod_barra) {
		this.tip_cod_barra = tip_cod_barra;
	}
	
	/**
	 * @param uni_med , uni_med a modificar.
	 */
	public void setUni_med(String uni_med) {
		this.uni_med = uni_med;
	}
	
		
}
