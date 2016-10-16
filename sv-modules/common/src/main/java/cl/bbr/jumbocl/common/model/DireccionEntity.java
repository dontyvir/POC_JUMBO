package cl.bbr.jumbocl.common.model;

import java.sql.Timestamp;

/**
 * Clase que captura desde la Base de Datos los datos de una Dirección
 * @author bbr
 *
 */
public class DireccionEntity {

	private Long   id;
	private Long   loc_cod;
	private Long   com_id;
	private TipoCalleEntity tipo_calle;
	private Long   cli_id;
	private Long   zon_id;
	private String alias;
	private String calle;
	private String numero;
	private String depto;
	private String comentarios;
	private Timestamp fec_crea;
	private String fnueva;
	private String cuadrante;
	private String estado;
	private String nom_local;
	private String nom_comuna;
	private String nom_zona;
	private String nom_tip_calle;
	private String nom_region;
	//agregado por VTE
	private Long   tip_id;
	private Long   zona_id;
	private String ciudad;
	private String fax;
	private Long   reg_id;
	private String otra_comuna;
	private String dir_conflictiva;
	private String dir_conflictiva_comentario;
	private String tipoPicking;
	private int    id_poligono;


	/**
	 * Constructor
	 */
	public DireccionEntity() {

	}

	/**
	 * @param id
	 * @param loc_cod
	 * @param com_id
	 * @param tipo_calle
	 * @param cli_id
	 * @param alias
	 * @param calle
	 * @param numero
	 * @param depto
	 * @param comentarios
	 * @param fec_crea
	 * @param estado
	 * @param nom_local
	 * @param nom_comuna
	 * @param tip_calle
	 * @param nom_region
	 */
	public DireccionEntity(Long id, Long loc_cod, Long com_id, TipoCalleEntity tipo_calle, Long cli_id, 
			String alias, String calle, String numero, String depto, String comentarios, Timestamp fec_crea, 
			String estado, String nom_local, String nom_comuna, String tip_calle, String nom_region) {
		this.id = id;
		this.loc_cod = loc_cod;
		this.com_id = com_id;
		this.tipo_calle = tipo_calle;
		this.cli_id = cli_id;
		this.alias = alias;
		this.calle = calle;
		this.numero = numero;
		this.depto = depto;
		this.comentarios = comentarios;
		this.fec_crea = fec_crea;
		this.estado = estado;
		this.nom_local = nom_local;
		this.nom_comuna = nom_comuna;
		this.nom_region = nom_region;
	}


	/**
	 * @return alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return calle
	 */
	public String getCalle() {
		return calle;
	}

	/**
	 * @param calle
	 */
	public void setCalle(String calle) {
		this.calle = calle;
	}

	/**
	 * @return cli_id
	 */
	public Long getCli_id() {
		return cli_id;
	}

	/**
	 * @param cli_id
	 */
	public void setCli_id(Long cli_id) {
		this.cli_id = cli_id;
	}

	/**
	 * @return com_id
	 */
	public Long getCom_id() {
		return com_id;
	}

	/**
	 * @param com_id
	 */
	public void setCom_id(Long com_id) {
		this.com_id = com_id;
	}

	/**
	 * @return comentarios
	 */
	public String getComentarios() {
		return comentarios;
	}

	/**
	 * @param comentarios
	 */
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	/**
	 * @return depto
	 */
	public String getDepto() {
		return depto;
	}

	/**
	 * @param depto
	 */
	public void setDepto(String depto) {
		this.depto = depto;
	}

	/**
	 * @return estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return fec_crea
	 */
	public Timestamp getFec_crea() {
		return fec_crea;
	}

	/**
	 * @param fec_crea
	 */
	public void setFec_crea(Timestamp fec_crea) {
		this.fec_crea = fec_crea;
	}

	/**
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return loc_cod
	 */
	public Long getLoc_cod() {
		return loc_cod;
	}

	/**
	 * @param loc_cod
	 */
	public void setLoc_cod(Long loc_cod) {
		this.loc_cod = loc_cod;
	}

	/**
	 * @return numero
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @param numero
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * @return tipo_calle
	 */
	public TipoCalleEntity getTipo_calle() {
		return tipo_calle;
	}

	/**
	 * @param tipo_calle
	 */
	public void setTipo_calle(TipoCalleEntity tipo_calle) {
		this.tipo_calle = tipo_calle;
	}

	/**
	 * @return Returns the nom_comuna.
	 */
	public String getNom_comuna() {
		return nom_comuna;
	}

	/**
	 * @param nom_comuna The nom_comuna to set.
	 */
	public void setNom_comuna(String nom_comuna) {
		this.nom_comuna = nom_comuna;
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
	 * @return Returns the nom_tip_calle.
	 */
	public String getNom_tip_calle() {
		return nom_tip_calle;
	}

	/**
	 * @param nom_tip_calle The nom_tip_calle to set.
	 */
	public void setNom_tip_calle(String nom_tip_calle) {
		this.nom_tip_calle = nom_tip_calle;
	}

	/**
	 * @return Returns the cuadrante.
	 */
	public String getCuadrante() {
		return cuadrante;
	}

	/**
	 * @param cuadrante The cuadrante to set.
	 */
	public void setCuadrante(String cuadrante) {
		this.cuadrante = cuadrante;
	}

	/**
	 * @return Returns the fnueva.
	 */
	public String getFnueva() {
		return fnueva;
	}

	/**
	 * @param fnueva The fnueva to set.
	 */
	public void setFnueva(String fnueva) {
		this.fnueva = fnueva;
	}

	/**
	 * @return Returns the zon_id.
	 */
	public Long getZon_id() {
		return zon_id;
	}

	/**
	 * @param zon_id The zon_id to set.
	 */
	public void setZon_id(Long zon_id) {
		this.zon_id = zon_id;
	}

	/**
	 * @return Returns the nom_zona.
	 */
	public String getNom_zona() {
		return nom_zona;
	}

	/**
	 * @param nom_zona The nom_zona to set.
	 */
	public void setNom_zona(String nom_zona) {
		this.nom_zona = nom_zona;
	}

	/**
	 * @return nom_region
	 */
	public String getNom_region() {
		return nom_region;
	}

	/**
	 * @param nom_region
	 */
	public void setNom_region(String nom_region) {
		this.nom_region = nom_region;
	}

	/**
	 * @return Returns the fax.
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax The fax to set.
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return Returns the otra_comuna.
	 */
	public String getOtra_comuna() {
		return otra_comuna;
	}

	/**
	 * @param otra_comuna The otra_comuna to set.
	 */
	public void setOtra_comuna(String otra_comuna) {
		this.otra_comuna = otra_comuna;
	}

	/**
	 * @return Returns the reg_id.
	 */
	public Long getReg_id() {
		return reg_id;
	}

	/**
	 * @param reg_id The reg_id to set.
	 */
	public void setReg_id(Long reg_id) {
		this.reg_id = reg_id;
	}

	/**
	 * @return Returns the tip_id.
	 */
	public Long getTip_id() {
		return tip_id;
	}

	/**
	 * @param tip_id The tip_id to set.
	 */
	public void setTip_id(Long tip_id) {
		this.tip_id = tip_id;
	}

	/**
	 * @return Returns the zona_id.
	 */
	public Long getZona_id() {
		return zona_id;
	}

	/**
	 * @param zona_id The zona_id to set.
	 */
	public void setZona_id(Long zona_id) {
		this.zona_id = zona_id;
	}

	/**
	 * @return Returns the ciudad.
	 */
	public String getCiudad() {
		return ciudad;
	}

	/**
	 * @param ciudad The ciudad to set.
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

    /**
     * @return Devuelve dir_conflictiva.
     */
    public String getDir_conflictiva() {
        return dir_conflictiva;
    }
    /**
     * @return Devuelve dir_conflictiva_comentario.
     */
    public String getDir_conflictiva_comentario() {
        return dir_conflictiva_comentario;
    }
    /**
     * @param dir_conflictiva El dir_conflictiva a establecer.
     */
    public void setDir_conflictiva(String dir_conflictiva) {
        this.dir_conflictiva = dir_conflictiva;
    }
    /**
     * @param dir_conflictiva_comentario El dir_conflictiva_comentario a establecer.
     */
    public void setDir_conflictiva_comentario(String dir_conflictiva_comentario) {
        this.dir_conflictiva_comentario = dir_conflictiva_comentario;
    }
    /**
     * @return Devuelve tipoPicking.
     */
    public String getTipoPicking() {
        return tipoPicking;
    }
    /**
     * @param tipoPicking El tipoPicking a establecer.
     */
    public void setTipoPicking(String tipoPicking) {
        this.tipoPicking = tipoPicking;
    }
    /**
     * @return Devuelve id_poligono.
     */
    public int getId_poligono() {
        return id_poligono;
    }
    /**
     * @param id_poligono El id_poligono a establecer.
     */
    public void setId_poligono(int id_poligono) {
        this.id_poligono = id_poligono;
    }
}
