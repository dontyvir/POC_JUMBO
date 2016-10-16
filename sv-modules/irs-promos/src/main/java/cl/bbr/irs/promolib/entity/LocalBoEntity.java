package cl.bbr.irs.promolib.entity;

import java.io.Serializable;

/**
 *Clase que captura desde la base de datos los datos de un local
 * @author bbr
 *
 */
public class LocalBoEntity implements Serializable {
	private long id_local;
	private String cod_local;
	private String nom_local;
	private long id_local_bop;
	private long id_local_sap;
	/**
	 * @return Returns the cod_local.
	 */
	public String getCod_local() {
		return cod_local;
	}
	/**
	 * @param cod_local The cod_local to set.
	 */
	public void setCod_local(String cod_local) {
		this.cod_local = cod_local;
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
	 * @return Returns the id_local_bop.
	 */
	public long getId_local_bop() {
		return id_local_bop;
	}
	/**
	 * @param id_local_bop The id_local_bop to set.
	 */
	public void setId_local_bop(long id_local_bop) {
		this.id_local_bop = id_local_bop;
	}
	/**
	 * @return Returns the id_local_sap.
	 */
	public long getId_local_sap() {
		return id_local_sap;
	}
	/**
	 * @param id_local_sap The id_local_sap to set.
	 */
	public void setId_local_sap(long id_local_sap) {
		this.id_local_sap = id_local_sap;
	}
	/**
	 * @return Returns the nom_local.
	 */
	public String getNom_local() {
		return nom_local;
	}
	/**
	 * @param nom_local The nom_local to set.
	 */
	public void setNom_local(String nom_local) {
		this.nom_local = nom_local;
	}
	
	
}
