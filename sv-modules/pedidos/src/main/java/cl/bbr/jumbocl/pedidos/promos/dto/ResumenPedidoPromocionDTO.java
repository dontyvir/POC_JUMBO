package cl.bbr.jumbocl.pedidos.promos.dto;

import java.util.List;

public class ResumenPedidoPromocionDTO {
	private long id_promocion;
	private long promo_codigo;
	private long tipo_promo;
	private String desc_promo;
	private String fec_ini;	
	private String fec_fin;
	private String desc_prod;
	private long monto_descuento;
	private List prod_relacionados;
	
	private int num_promos;
	
	public ResumenPedidoPromocionDTO() {		
	}
	
	/**
	 * @return Returns the prod_relacionados.
	 */
	public List getProd_relacionados() {
		return prod_relacionados;
	}

	/**
	 * @param prod_relacionados The prod_relacionados to set.
	 */
	public void setProd_relacionados(List prod_relacionados) {
		this.prod_relacionados = prod_relacionados;
	}

	/**
	 * @return Returns the desc_prod.
	 */
	public String getDesc_prod() {
		return desc_prod;
	}
	/**
	 * @param desc_prod The desc_prod to set.
	 */
	public void setDesc_prod(String desc_prod) {
		this.desc_prod = desc_prod;
	}
	/**
	 * @return Returns the desc_promo.
	 */
	public String getDesc_promo() {
		return desc_promo;
	}
	/**
	 * @param desc_promo The desc_promo to set.
	 */
	public void setDesc_promo(String desc_promo) {
		this.desc_promo = desc_promo;
	}
	/**
	 * @return Returns the fec_fin.
	 */
	public String getFec_fin() {
		return fec_fin;
	}
	/**
	 * @param fec_fin The fec_fin to set.
	 */
	public void setFec_fin(String fec_fin) {
		this.fec_fin = fec_fin;
	}
	/**
	 * @return Returns the fec_ini.
	 */
	public String getFec_ini() {
		return fec_ini;
	}
	/**
	 * @param fec_ini The fec_ini to set.
	 */
	public void setFec_ini(String fec_ini) {
		this.fec_ini = fec_ini;
	}	
	/**
	 * @return Returns the id_promocion.
	 */
	public long getId_promocion() {
		return id_promocion;
	}
	/**
	 * @param id_promocion The id_promocion to set.
	 */
	public void setId_promocion(long id_promocion) {
		this.id_promocion = id_promocion;
	}
	/**
	 * @return Returns the monto_descuento.
	 */
	public long getMonto_descuento() {
		return monto_descuento;
	}
	/**
	 * @param monto_descuento The monto_descuento to set.
	 */
	public void setMonto_descuento(long monto_descuento) {
		this.monto_descuento = monto_descuento;
	}
	/**
	 * @return Returns the promo_codigo.
	 */
	public long getPromo_codigo() {
		return promo_codigo;
	}
	/**
	 * @param promo_codigo The promo_codigo to set.
	 */
	public void setPromo_codigo(long promo_codigo) {
		this.promo_codigo = promo_codigo;
	}
	/**
	 * @return Returns the tipo_promo.
	 */
	public long getTipo_promo() {
		return tipo_promo;
	}
	/**
	 * @param tipo_promo The tipo_promo to set.
	 */
	public void setTipo_promo(long tipo_promo) {
		this.tipo_promo = tipo_promo;
	}
	
	public int getNum_promos() {
		return num_promos;
	}

	public void setNum_promos(int num_promos) {
		this.num_promos = num_promos;
	}
	
	
	
}
