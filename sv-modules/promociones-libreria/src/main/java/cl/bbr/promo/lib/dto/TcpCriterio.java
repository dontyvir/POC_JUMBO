package cl.bbr.promo.lib.dto;

import java.io.Serializable;

/**
 * Criterio para búsquedas. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class TcpCriterio implements Serializable {

	private long tcp_id;
	private String tcp_nombre;
	
	/**
	 * toString
	 */
	public String toString() {

		String result = "";
		
		result += " tcp_id: " + this.tcp_id;
		result += " tcp_nombre: " + this.tcp_nombre;
		
		return result;
		
	}

	/**
	 * @return Devuelve tcp_id.
	 */
	public long getTcp_id() {
		return tcp_id;
	}
	/**
	 * @param tcp_id El tcp_id a establecer.
	 */
	public void setTcp_id(long tcp_id) {
		this.tcp_id = tcp_id;
	}
	/**
	 * @return Devuelve tcp_nombre.
	 */
	public String getTcp_nombre() {
		return tcp_nombre;
	}
	/**
	 * @param tcp_nombre El tcp_nombre a establecer.
	 */
	public void setTcp_nombre(String tcp_nombre) {
		this.tcp_nombre = tcp_nombre;
	}
}