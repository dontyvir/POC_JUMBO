package cl.bbr.vte.cotizaciones.dto;

/**
 * Clase que contiene la información del local
 * 
 * @author BBR
 *
 */
public class LocalDTO {
	private long id_local;
	private String cod_local;
	private String nom_local;
	private int orden;
	private String fec_carga_prec;
	
	
	/**
	 * @param id_local
	 * @param cod_local
	 * @param nom_local
	 */
	public LocalDTO(long id_local, String cod_local, String nom_local) {
		super();
		this.id_local = id_local;
		this.cod_local = cod_local;
		this.nom_local = nom_local;
	}
	/**
	 * @param id_local
	 * @param cod_local
	 * @param nom_local
	 * @param orden
	 * @param fec_carga_prec
	 */
	public LocalDTO(long id_local, String cod_local, String nom_local, int orden, String fec_carga_prec) {
		super();
		this.id_local = id_local;
		this.cod_local = cod_local;
		this.nom_local = nom_local;
		this.orden = orden;
		this.fec_carga_prec = fec_carga_prec;
	}
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
	 * @return Returns the fec_carga_prec.
	 */
	public String getFec_carga_prec() {
		return fec_carga_prec;
	}
	/**
	 * @param fec_carga_prec The fec_carga_prec to set.
	 */
	public void setFec_carga_prec(String fec_carga_prec) {
		this.fec_carga_prec = fec_carga_prec;
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
	/**
	 * @return Returns the orden.
	 */
	public int getOrden() {
		return orden;
	}
	/**
	 * @param orden The orden to set.
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}
}
