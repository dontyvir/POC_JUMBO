package cl.bbr.jumbocl.pedidos.dto;

public class ProrrateoPromocionDTO {
	//Cantidad de productos pickeados por registro.
	//No corresponde al total por producto pedido.
	private double cant_pick;
	//Cantidad de productos en promocion (cantidad solicitada).
	private double cant_promocion; 
	//Descuento para el producto final.
	private double dscto_final; 
	//Descuento para el producto original de la promoción.
	private double dscto_promo;
	//Monto total del descuento del producto.
	private long   monto_tot_dscto_prod;
	//Monto total del producto.
	private long   monto_tot_prod;
	
	private long id_detalle;
	
	
	/**
	 * @return Returns the cant_pick.
	 */
	public double getCant_pick() {
		return cant_pick;
	}
	/**
	 * @param cant_pick The cant_pick to set.
	 */
	public void setCant_pick(double cant_pick) {
		this.cant_pick = cant_pick;
	}
	/**
	 * @return Returns the cant_promocion.
	 */
	public double getCant_promocion() {
		return cant_promocion;
	}
	/**
	 * @param cant_promocion The cant_promocion to set.
	 */
	public void setCant_promocion(double cant_promocion) {
		this.cant_promocion = cant_promocion;
	}
	/**
	 * @return Returns the dscto_final.
	 */
	public double getDscto_final() {
		return dscto_final;
	}
	/**
	 * @param dscto_final The dscto_final to set.
	 */
	public void setDscto_final(double dscto_final) {
		this.dscto_final = dscto_final;
	}
	/**
	 * @return Returns the dscto_promo.
	 */
	public double getDscto_promo() {
		return dscto_promo;
	}
	/**
	 * @param dscto_prod The dscto_promo to set.
	 */
	public void setDscto_prod(double dscto_promo) {
		this.dscto_promo = dscto_promo;
	}
	/**
	 * @return Returns the monto_tot_dscto_prod.
	 */
	public long getMonto_tot_dscto_prod() {
		return monto_tot_dscto_prod;
	}
	/**
	 * @param monto_tot_dscto_prod The monto_tot_dscto_prod to set.
	 */
	public void setMonto_tot_dscto_prod(long monto_tot_dscto_prod) {
		this.monto_tot_dscto_prod = monto_tot_dscto_prod;
	}
	/**
	 * @return Returns the monto_tot_prod.
	 */
	public long getMonto_tot_prod() {
		return monto_tot_prod;
	}
	/**
	 * @param monto_tot_prod The monto_tot_prod to set.
	 */
	public void setMonto_tot_prod(long monto_tot_prod) {
		this.monto_tot_prod = monto_tot_prod;
	}
	/**
	 * @return Returns the id_detalle.
	 */
	public long getId_detalle() {
		return id_detalle;
	}
	/**
	 * @param id_detalle The id_detalle to set.
	 */
	public void setId_detalle(long id_detalle) {
		this.id_detalle = id_detalle;
	}
	/**
	 * @param dscto_promo The dscto_promo to set.
	 */
	public void setDscto_promo(double dscto_promo) {
		this.dscto_promo = dscto_promo;
	}
	
	
		
}
