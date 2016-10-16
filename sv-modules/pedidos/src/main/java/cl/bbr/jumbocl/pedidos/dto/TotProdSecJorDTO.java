package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class TotProdSecJorDTO implements Serializable {
	
	long id_sector;
	String	sector;
	double	cant_prods;
	double	cant_pick;
	double	cant_falt;
	double	cant_prods_no_asig;
	
	
	public double getCant_falt() {
		return cant_falt;
	}
	public double getCant_pick() {
		return cant_pick;
	}
	public void setCant_falt(double cant_falt) {
		this.cant_falt = cant_falt;
	}
	public void setCant_pick(double cant_pick) {
		this.cant_pick = cant_pick;
	}
	/**
	 * @return Returns the id_sector.
	 */
	public long getId_sector() {
		return id_sector;
	}
	/**
	 * @param id_sector The id_sector to set.
	 */
	public void setId_sector(long id_sector) {
		this.id_sector = id_sector;
	}
	public double getCant_prods() {
		return cant_prods;
	}
	public void setCant_prods(double cant_prods) {
		this.cant_prods = cant_prods;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public double getCant_prods_no_asig() {
		return cant_prods_no_asig;
	}
	public void setCant_prods_no_asig(double cant_prods_no_asig) {
		this.cant_prods_no_asig = cant_prods_no_asig;
	}
	
}
