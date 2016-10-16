package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class TotProdZonJorDTO implements Serializable {
	
	long id_zona;
	String	zona;
	double	cant_prods;
	double	cant_pick;
	double	cant_falt;
	double	cant_prods_no_asig;
	/**
	 * @return Returns the cant_falt.
	 */
	public double getCant_falt() {
		return cant_falt;
	}
	/**
	 * @param cant_falt The cant_falt to set.
	 */
	public void setCant_falt(double cant_falt) {
		this.cant_falt = cant_falt;
	}
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
	 * @return Returns the cant_prods.
	 */
	public double getCant_prods() {
		return cant_prods;
	}
	/**
	 * @param cant_prods The cant_prods to set.
	 */
	public void setCant_prods(double cant_prods) {
		this.cant_prods = cant_prods;
	}
	/**
	 * @return Returns the cant_prods_no_asig.
	 */
	public double getCant_prods_no_asig() {
		return cant_prods_no_asig;
	}
	/**
	 * @param cant_prods_no_asig The cant_prods_no_asig to set.
	 */
	public void setCant_prods_no_asig(double cant_prods_no_asig) {
		this.cant_prods_no_asig = cant_prods_no_asig;
	}
	/**
	 * @return Returns the id_zona.
	 */
	public long getId_zona() {
		return id_zona;
	}
	/**
	 * @param id_zona The id_zona to set.
	 */
	public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
	}
	/**
	 * @return Returns the zona.
	 */
	public String getZona() {
		return zona;
	}
	/**
	 * @param zona The zona to set.
	 */
	public void setZona(String zona) {
		this.zona = zona;
	}
	
	
}
