package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;


public class ProductoPromoDTO implements Serializable{
	
	private long id_prodpromo;
	private long id_producto;
	private long id_local;
	private long cod_promo1;
	private long cod_promo2;
	private long cod_promo3;
	/**
	 * @return Returns the cod_promo1.
	 */
	public long getCod_promo1() {
		return cod_promo1;
	}
	/**
	 * @param cod_promo1 The cod_promo1 to set.
	 */
	public void setCod_promo1(long cod_promo1) {
		this.cod_promo1 = cod_promo1;
	}
	/**
	 * @return Returns the cod_promo2.
	 */
	public long getCod_promo2() {
		return cod_promo2;
	}
	/**
	 * @param cod_promo2 The cod_promo2 to set.
	 */
	public void setCod_promo2(long cod_promo2) {
		this.cod_promo2 = cod_promo2;
	}
	/**
	 * @return Returns the cod_promo3.
	 */
	public long getCod_promo3() {
		return cod_promo3;
	}
	/**
	 * @param cod_promo3 The cod_promo3 to set.
	 */
	public void setCod_promo3(long cod_promo3) {
		this.cod_promo3 = cod_promo3;
	}
	/**
	 * @return Returns the id_local.
	 */
	public long getId_local() {
		return id_local;
	}
	/**
	 * @param id_local The id_local to set.
	 */
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}
	/**
	 * @return Returns the id_prodpromo.
	 */
	public long getId_prodpromo() {
		return id_prodpromo;
	}
	/**
	 * @param id_prodpromo The id_prodpromo to set.
	 */
	public void setId_prodpromo(long id_prodpromo) {
		this.id_prodpromo = id_prodpromo;
	}
	/**
	 * @return Returns the id_producto.
	 */
	public long getId_producto() {
		return id_producto;
	}
	/**
	 * @param id_producto The id_producto to set.
	 */
	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}
	
	
	
	
}
