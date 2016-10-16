package cl.bbr.promo.lib.dto;

import java.io.Serializable;

/**
 * Criterio para búsquedas. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class MedioPagoNormalizadoDTO implements Serializable {

	private String mp_promo;
	private String mp_jmcl;
	private String mp_jmcl_ncuotas;
	
	/**
	 * toString
	 */
	public String toString() {

		String result = "";
		
		result += " mp_jmcl: " + this.mp_jmcl;
		result += " mp_promo: " + this.mp_promo;
		result += " mp_jmcl_ncuotas: " + this.mp_jmcl_ncuotas;
		
		return result;
		
	}

	/**
	 * @return Devuelve mp_jmcl.
	 */
	public String getMp_jmcl() {
		return mp_jmcl;
	}
	/**
	 * @param mp_jmcl El mp_jmcl a establecer.
	 */
	public void setMp_jmcl(String mp_jmcl) {
		this.mp_jmcl = mp_jmcl;
	}
	/**
	 * @return Devuelve mp_promo.
	 */
	public String getMp_promo() {
		return mp_promo;
	}
	/**
	 * @param mp_promo El mp_promo a establecer.
	 */
	public void setMp_promo(String mp_promo) {
		this.mp_promo = mp_promo;
	}

	/**
	 * @return Devulve mp_jmcl_ncuotas
	 */
	public String getMp_jmcl_ncuotas() {
		return mp_jmcl_ncuotas;
	}

	/**
	 * 
	 * @param mp_jmcl_ncuotas El mp_jmcl_ncuotas a establecer 
	 */
	public void setMp_jmcl_ncuotas(String mp_jmcl_ncuotas) {
		this.mp_jmcl_ncuotas = mp_jmcl_ncuotas;
	}
	
}