package cl.bbr.jumbocl.usuarios.dto;

import java.io.Serializable;


public class LocalDTO implements Serializable {
	private long id_local;
	private String cod_local;
	private String nom_local;
	private int orden;
	private String fec_carga_prec;
	private int cod_local_pos;
	private String tipo_flujo;
	private String tipo_picking;
	
	public LocalDTO() {
		super();
	}
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
	 * @return Returns the tipo_flujo.
	 */
	public String getTipo_flujo() {
		return tipo_flujo;
	}
	/**
	 * @param tipo_flujo The tipo_flujo to set.
	 */
	public void setTipo_flujo(String tipo_flujo) {
		this.tipo_flujo = tipo_flujo;
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
	/**
	 * @return Returns the cod_local_pos.
	 */
	public int getCod_local_pos() {
		return cod_local_pos;
	}
	/**
	 * @param cod_local_pos The cod_local_pos to set.
	 */
	public void setCod_local_pos(int cod_local_pos) {
		this.cod_local_pos = cod_local_pos;
	}
	
    /**
     * @return Devuelve tipo_picking.
     */
    public String getTipo_picking() {
        return tipo_picking;
    }
    /**
     * @param tipo_picking El tipo_picking a establecer.
     */
    public void setTipo_picking(String tipo_picking) {
        this.tipo_picking = tipo_picking;
    }
}
