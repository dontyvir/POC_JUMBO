/**
 * ProductoPromosEntity.java
 * Creado   : 26-feb-2007
 * Historia : 26-feb-2007 Version 1.0
 * Historia : 17-jun-2007 version 1.1
 * Version  : 1.1
 * BBR
 */
package cl.bbr.irs.promolib.entity;

import java.io.Serializable;

/**
 * @author JORGE SILVA
 *
 */
public class ProductoPromosEntity implements Serializable {

	private int id_prodpromos;
	private int id_producto;
	private int id_local;
	private int promo1;
	private int promo2;
	private int promo3;
	private String ean13;
	
	/**
	 * <b>Description: Se setean las promociones en -1 para identificar los datos que
	 * no han sido modificados y lograr un update inteligente</b>
	 */
	public ProductoPromosEntity() {
		promo1=-1;
		promo2=-1;
		promo3=-1;
	}

	/**
	 * @return Returns the ean13.
	 */
	public String getEan13() {
		return ean13;
	}

	/**
	 * @param ean13 The ean13 to set.
	 */
	public void setEan13(String ean13) {
		this.ean13 = ean13;
	}
	
	/**
	 * @return Returns the id_local.
	 */
	public int getId_local() {
		return id_local;
	}

	/**
	 * @param id_local The id_local to set.
	 */
	public void setId_local(int id_local) {
		this.id_local = id_local;
	}

	/**
	 * @return Returns the id_producto.
	 */
	public int getId_producto() {
		return id_producto;
	}

	/**
	 * @param id_producto The id_producto to set.
	 */
	public void setId_producto(int id_producto) {
		this.id_producto = id_producto;
	}

	/**
	 * @return Returns the promo1.
	 */
	public int getPromo1() {
		return promo1;
	}

	/**
	 * @param promo1 The promo1 to set.
	 */
	public void setPromo1(int promo1) {
		this.promo1 = promo1;
	}

	/**
	 * @return Returns the promo2.
	 */
	public int getPromo2() {
		return promo2;
	}

	/**
	 * @param promo2 The promo2 to set.
	 */
	public void setPromo2(int promo2) {
		this.promo2 = promo2;
	}

	/**
	 * @return Returns the promo3.
	 */
	public int getPromo3() {
		return promo3;
	}

	/**
	 * @param promo3 The promo3 to set.
	 */
	public void setPromo3(int promo3) {
		this.promo3 = promo3;
	}

	/**
	 * @return Returns the id_prodpromos.
	 */
	public int getId_prodpromos() {
		return id_prodpromos;
	}

	/**
	 * @param id_prodpromos The id_prodpromos to set.
	 */
	public void setId_prodpromos(int id_prodpromos) {
		this.id_prodpromos = id_prodpromos;
	}
	
	
}
