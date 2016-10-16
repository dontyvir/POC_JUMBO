package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;


/**
 * Promociones del detalle pedido para una ronda
 * @author BBR
 *
 */
public class PromoDetPedRondaDTO implements Serializable  {
	
	private long   id_ronda;
	private long   id_detalle;
	private long   id_producto;
	private long   id_pedido;
	private long   id_promocion;
	private String sustituible;
	private String faltante;
	/**
	 * @return Returns the faltante.
	 */
	public String getFaltante() {
		return faltante;
	}
	/**
	 * @param faltante The faltante to set.
	 */
	public void setFaltante(String faltante) {
		this.faltante = faltante;
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
	 * @return Returns the id_ronda.
	 */
	public long getId_ronda() {
		return id_ronda;
	}
	/**
	 * @param id_ronda The id_ronda to set.
	 */
	public void setId_ronda(long id_ronda) {
		this.id_ronda = id_ronda;
	}
	/**
	 * @return Returns the sustituible.
	 */
	public String getSustituible() {
		return sustituible;
	}
	/**
	 * @param sustituible The sustituible to set.
	 */
	public void setSustituible(String sustituible) {
		this.sustituible = sustituible;
	}
	
	
	
	
	
}
