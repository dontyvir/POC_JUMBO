package cl.bbr.promo.lib.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Criterio para búsquedas. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class PromocionCriterio implements Serializable {

	private long id_producto_fo;
	private long id_producto_bo;
	private long id_local;
	private List lista_tcp;
	
	/**
	 * toString
	 */
	public String toString() {
		
		String result = "";
		
		result += " id_local: " + this.id_local;
		result += " id_producto_fo: " + this.id_producto_fo;
		result += " id_producto_bo: " + this.id_producto_bo;
		
		return result;
		
	}
	
	/**
	 * @return Devuelve id_local.
	 */
	public long getId_local() {
		return id_local;
	}
	/**
	 * @param id_local El id_local a establecer.
	 */
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}
	/**
	 * @return Devuelve id_producto_bo.
	 */
	public long getId_producto_bo() {
		return id_producto_bo;
	}
	/**
	 * @param id_producto_bo El id_producto_bo a establecer.
	 */
	public void setId_producto_bo(long id_producto_bo) {
		this.id_producto_bo = id_producto_bo;
	}
	/**
	 * @return Devuelve id_producto_fo.
	 */
	public long getId_producto_fo() {
		return id_producto_fo;
	}
	/**
	 * @param id_producto_fo El id_producto_fo a establecer.
	 */
	public void setId_producto_fo(long id_producto_fo) {
		this.id_producto_fo = id_producto_fo;
	}

	public List getLista_tcp() {
		return lista_tcp;
	}

	public void setLista_tcp(List lista_tcp) {
		this.lista_tcp = lista_tcp;
	}
	
}