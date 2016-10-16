package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la base de datos los datos de un precio por local
 * @author bbr
 *
 */
public class PrecioLocalEntity {
	private String cod_local;
	private Long id_prod;
	private Double costo;
	private Double valor;
	private Integer stock;
	private String estado;
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
	 * @return Returns the costo.
	 */
	public Double getCosto() {
		return costo;
	}
	/**
	 * @param costo The costo to set.
	 */
	public void setCosto(Double costo) {
		this.costo = costo;
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
	 * @return Returns the stock.
	 */
	public Integer getStock() {
		return stock;
	}
	/**
	 * @param stock The stock to set.
	 */
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	/**
	 * @return Returns the valor.
	 */
	public Double getValor() {
		return valor;
	}
	/**
	 * @param valor The valor to set.
	 */
	public void setValor(Double valor) {
		this.valor = valor;
	}
}
