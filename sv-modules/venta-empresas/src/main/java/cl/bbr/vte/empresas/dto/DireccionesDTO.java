package cl.bbr.vte.empresas.dto;

import java.io.Serializable;

/**
 * DTO para datos de las direcciones de despacho. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class DireccionesDTO implements Serializable {

	private long 	id;
	private long 	loc_cod;
	private long 	tip_id;
	private long 	cli_id;
	private long 	com_id;
	private long    zona_id;
	private long 	id_poligono;
	private String 	alias;
	private String 	calle;
	private String 	numero;
	private String 	depto;
	private String 	comentarios;
	private String 	estado;
	private String	fec_crea;
	private String 	fnueva;
	private String 	cuadrante;
	private String 	ciudad;
	private String 	fax;
	private long 	reg_id;
	private String 	otra_comuna;
	private String 	reg_nombre;
	private long 	tipo_calle;
	private	String 	nom_region;
	private String 	nom_local;
	private String 	nom_comuna;
	private String 	nom_zona;
	private String	nom_tip_calle;

	/**
	 * Constructor
	 */
	public DireccionesDTO() {

	}


	public String getNom_comuna() {
		return nom_comuna;
	}


	public void setNom_comuna(String nom_comuna) {
		this.nom_comuna = nom_comuna;
	}


	public String getNom_local() {
		return nom_local;
	}


	public void setNom_local(String nom_local) {
		this.nom_local = nom_local;
	}


	public String getNom_tip_calle() {
		return nom_tip_calle;
	}


	public void setNom_tip_calle(String nom_tip_calle) {
		this.nom_tip_calle = nom_tip_calle;
	}


	public String getNom_zona() {
		return nom_zona;
	}


	public void setNom_zona(String nom_zona) {
		this.nom_zona = nom_zona;
	}


	public String getNom_region() {
		return nom_region;
	}

	public void setNom_region(String nom_region) {
		this.nom_region = nom_region;
	}


	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public long getCli_id() {
		return cli_id;
	}

	public void setCli_id(long cli_id) {
		this.cli_id = cli_id;
	}

	public long getCom_id() {
		return com_id;
	}

	public void setCom_id(long com_id) {
		this.com_id = com_id;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public String getDepto() {
		return depto;
	}

	public void setDepto(String depto) {
		this.depto = depto;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFec_crea() {
		return fec_crea;
	}

	public void setFec_crea(String fec_crea) {
		this.fec_crea = fec_crea;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLoc_cod() {
		return loc_cod;
	}

	public void setLoc_cod(long loc_cod) {
		this.loc_cod = loc_cod;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public long getTipo_calle() {
		return tipo_calle;
	}

	public void setTipo_calle(long tipo_calle) {
		this.tipo_calle = tipo_calle;
	}

	public long getReg_id() {
		return reg_id;
	}

	public void setReg_id(long reg_id) {
		this.reg_id = reg_id;
	}

	public String getNueva() {
		return fnueva;
	}

	public void setNueva(String nueva) {
		this.fnueva = nueva;
	}

	public String getReg_nombre() {
		return reg_nombre;
	}

	public void setReg_nombre(String reg_nombre) {
		this.reg_nombre = reg_nombre;
	}

	public String getCuadrante() {
		return cuadrante;
	}

	public void setCuadrante(String cuadrante) {
		this.cuadrante = cuadrante;
	}

	public long getId_poligono() {
		return id_poligono;
	}

	public void setId_poligono(long id_poligono) {
		this.id_poligono = id_poligono;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFnueva() {
		return fnueva;
	}

	public void setFnueva(String fnueva) {
		this.fnueva = fnueva;
	}

	public String getOtra_comuna() {
		return otra_comuna;
	}

	public void setOtra_comuna(String otra_comuna) {
		this.otra_comuna = otra_comuna;
	}

	public long getTip_id() {
		return tip_id;
	}

	public void setTip_id(long tip_id) {
		this.tip_id = tip_id;
	}
	
    /**
     * @return Devuelve zona_id.
     */
    public long getZona_id() {
        return zona_id;
    }
    /**
     * @param zona_id El zona_id a establecer.
     */
    public void setZona_id(long zona_id) {
        this.zona_id = zona_id;
    }
}