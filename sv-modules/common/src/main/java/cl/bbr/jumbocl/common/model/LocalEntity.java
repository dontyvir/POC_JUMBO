package cl.bbr.jumbocl.common.model;

import java.sql.Timestamp;

/**
 *Clase que captura desde la base de datos los datos de un local
 * @author bbr
 *
 */
public class LocalEntity {
	private Long id_local;
	private String cod_local;
	private String nom_local;
	private Integer orden;
	private Timestamp fec_carga_prec;
	private Integer cod_local_pos;
	private String tipo_flujo;
	private String cod_local_promocion;
	private String tipo_picking;
	
	private String retirolocal;
	private int id_zona_retiro;
	private String direccion;
	
	
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getRetirolocal() {
		return retirolocal;
	}
	public void setRetirolocal(String retirolocal) {
		this.retirolocal = retirolocal;
	}
	public int getId_zona_retiro() {
		return id_zona_retiro;
	}
	public void setId_zona_retiro(int id_zona_retiro) {
		this.id_zona_retiro = id_zona_retiro;
	}
	/**
	 * @return Returns the cod_local_promocion.
	 */
	public String getCod_local_promocion() {
		return cod_local_promocion;
	}
	/**
	 * @param cod_local_promocion The cod_local_promocion to set.
	 */
	public void setCod_local_promocion(String cod_local_promocion) {
		this.cod_local_promocion = cod_local_promocion;
	}
	/**
	 * @return Returns the cod_local_pos.
	 */
	public Integer getCod_local_pos() {
		return cod_local_pos;
	}
	/**
	 * @param cod_local_pos The cod_local_pos to set.
	 */
	public void setCod_local_pos(Integer cod_local_pos) {
		this.cod_local_pos = cod_local_pos;
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
	public Timestamp getFec_carga_prec() {
		return fec_carga_prec;
	}
	/**
	 * @param fec_carga_prec The fec_carga_prec to set.
	 */
	public void setFec_carga_prec(Timestamp fec_carga_prec) {
		this.fec_carga_prec = fec_carga_prec;
	}
	/**
	 * @return Returns the id_local.
	 */
	public Long getId_local() {
		return id_local;
	}
	/**
	 * @param id_local The id_local to set.
	 */
	public void setId_local(Long id_local) {
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
	public Integer getOrden() {
		return orden;
	}
	/**
	 * @param orden The orden to set.
	 */
	public void setOrden(Integer orden) {
		this.orden = orden;
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