package cl.bbr.promo.lib.dto;

import java.io.Serializable;

/**
 * DTO para los grupos de TCP. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class FOTcpDTO implements Serializable {

	private int tcp_nro;
	private String tcp_nombre;
	private int tcp_max;
	private String cupon = "";
	
	/**
	 * toString
	 */
	public String toString() {

		String result = "";
		
		result += " tcp_nro: " + this.tcp_nro;
		result += " tcp_nombre: " + this.tcp_nombre;
		result += " tcp_max: " + this.tcp_max;
		result += " tcp_cupon: " + this.cupon;
		
		return result;
		
	}

	/**
	 * @return Devuelve tcp_id.
	 */
	public int getTcp_nro() {
		return tcp_nro;
	}
	/**
	 * @param tcp_id El tcp_id a establecer.
	 */
	public void setTcp_nro(int tcp_nro) {
		this.tcp_nro = tcp_nro;
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
	/**
	 * @return Devuelve tcp_max.
	 */
	public int getTcp_max() {
		return tcp_max;
	}
	/**
	 * @param tcp_max El tcp_max a establecer.
	 */
	public void setTcp_max(int tcp_max) {
		this.tcp_max = tcp_max;
	}
	/**
	 * @return Devuelve cupon.
	 */
	public String getCupon() {
		return cupon;
	}
	/**
	 * @param cupon El cupon a establecer.
	 */
	public void setCupon(String cupon) {
		this.cupon = cupon;
	}
}