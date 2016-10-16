package cl.bbr.jumbocl.contenidos.dto;

/**
 * Clase que muestra informacion de Locales.
 * 
 * @author BBR
 *
 */
public class LocalDTO {
	
	/**
	 * Id del local 
	 */
	private long id_local;
	
	/**
	 * Codigo del local
	 */
	private String cod_local;
	
	/**
	 * Nombre del local 
	 */
	private String nom_local;
	
	/**
	 * Orden en el listado 
	 */
	private int orden;
	
	/**
	 * Fecha de carga de precios
	 */
	private String fec_carga_prec;
	
	
	/**
	 * @param id_local
	 * @param cod_local
	 * @param nom_local
	 */
	public LocalDTO(long id_local , String cod_local , String nom_local) {
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
	public LocalDTO(long id_local , String cod_local , String nom_local , int orden , String fec_carga_prec) {
		super();
		this.id_local = id_local;
		this.cod_local = cod_local;
		this.nom_local = nom_local;
		this.orden = orden;
		this.fec_carga_prec = fec_carga_prec;
	}
	
	/**
	 * @return Retorna cod_local.
	 */
	public String getCod_local() {
		return cod_local;
	}
	/**
	 * @param cod_local , cod_local a modificar.
	 */
	public void setCod_local(String cod_local) {
		this.cod_local = cod_local;
	}
	/**
	 * @return Retorna fec_carga_prec.
	 */
	public String getFec_carga_prec() {
		return fec_carga_prec;
	}
	/**
	 * @param fec_carga_prec , fec_carga_prec a modificar.
	 */
	public void setFec_carga_prec(String fec_carga_prec) {
		this.fec_carga_prec = fec_carga_prec;
	}
	/**
	 * @return Retorna id_local.
	 */
	public long getId_local() {
		return id_local;
	}
	/**
	 * @param id_local , id_local a modificar.
	 */
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}
	/**
	 * @return Retorna nom_local.
	 */
	public String getNom_local() {
		return nom_local;
	}
	/**
	 * @param nom_local , nom_local a modificar.
	 */
	public void setNom_local(String nom_local) {
		this.nom_local = nom_local;
	}
	/**
	 * @return Retorna orden.
	 */
	public int getOrden() {
		return orden;
	}
	/**
	 * @param orden , orden a modificar.
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}
}
