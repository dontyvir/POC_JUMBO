package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;


/**
 * Promociones del detalle pedido
 * @author BBR
 *
 */
public class PromoDetallePedidoDTO implements Serializable  {
	
	private long   id_detalle;
	private long   id_promocion;
	private long   id_producto;
	private long   id_pedido;
	private long   promo_codigo;
	private String promo_desc;
	private long   promo_tipo;
	private String promo_fini;
	private String promo_ffin;
	private double promo_dscto_porc;
	
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
	 * @return Returns the id_pedido.
	 */
	public long getId_pedido() {
		return id_pedido;
	}
	/**
	 * @param id_pedido The id_pedido to set.
	 */
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
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
	 * @return Returns the promo_desc.
	 */
	public String getPromo_desc() {
		return promo_desc;
	}
	/**
	 * @param promo_desc The promo_desc to set.
	 */
	public void setPromo_desc(String promo_desc) {
		this.promo_desc = promo_desc;
	}
	/**
	 * @return Returns the promo_ffin.
	 */
	public String getPromo_ffin() {
		return promo_ffin;
	}
	/**
	 * @param promo_ffin The promo_ffin to set.
	 */
	public void setPromo_ffin(String promo_ffin) {
		this.promo_ffin = promo_ffin;
	}
	/**
	 * @return Returns the promo_fini.
	 */
	public String getPromo_fini() {
		return promo_fini;
	}
	/**
	 * @param promo_fini The promo_fini to set.
	 */
	public void setPromo_fini(String promo_fini) {
		this.promo_fini = promo_fini;
	}
	/**
	 * @return Returns the promo_tipo.
	 */
	public long getPromo_tipo() {
		return promo_tipo;
	}
	/**
	 * @param promo_tipo The promo_tipo to set.
	 */
	public void setPromo_tipo(long promo_tipo) {
		this.promo_tipo = promo_tipo;
	}
	/**
	 * @return Returns the promo_dscto_porc.
	 */
	public double getPromo_dscto_porc() {
		return promo_dscto_porc;
	}
	/**
	 * @param promo_dscto_porc The promo_dscto_porc to set.
	 */
	public void setPromo_dscto_porc(double promo_dscto_porc) {
		this.promo_dscto_porc = promo_dscto_porc;
	}
	
	
	
	
}
