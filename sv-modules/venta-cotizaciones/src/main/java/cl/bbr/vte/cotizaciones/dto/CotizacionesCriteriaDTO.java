package cl.bbr.vte.cotizaciones.dto;

import java.io.Serializable;

/**
 * DTO para datos de la cotización. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class CotizacionesCriteriaDTO implements Serializable {
	private long id_cot;
	private String rut_emp;
	private String razon_social;
	private String nom_emp;
	private String rut_comprador;
	private long id_empresa;
	private long id_sucursal;
	private long id_local;
	private long id_estado;
	private String nom_estado;
	private String tipo_fec;
	private String fec_ini;
	private String fec_fin;
	private int pag;
	private int regsperpag;
	private String ordena_por;
	private String nom_sucursal;
	private String alias_direccion;
	private long id_comprador;
	private String procedencia; // W: fove
	private int tipo_comprador; // 0: comprador 1: administrador
	private long id_Usuario;    // ID del usuario que esta logeado
	private String listSucursales;
	private String listCompradores;
	
    /**
     * @return Devuelve listCompradores.
     */
    public String getListCompradores() {
        return listCompradores;
    }
    /**
     * @param listCompradores El listCompradores a establecer.
     */
    public void setListCompradores(String listCompradores) {
        this.listCompradores = listCompradores;
    }
    /**
     * @return Devuelve listSucursales.
     */
    public String getListSucursales() {
        return listSucursales;
    }
    /**
     * @param listSucursales El listSucursales a establecer.
     */
    public void setListSucursales(String listSucursales) {
        this.listSucursales = listSucursales;
    }
    /**
     * @return Devuelve id_Usuario.
     */
    public long getId_Usuario() {
        return id_Usuario;
    }
    /**
     * @param id_Usuario El id_Usuario a establecer.
     */
    public void setId_Usuario(long id_Usuario) {
        this.id_Usuario = id_Usuario;
    }
	/**
	 * @return Devuelve procedencia.
	 */
	public String getProcedencia() {
		return procedencia;
	}
	/**
	 * @param procedencia El procedencia a establecer.
	 */
	public void setProcedencia(String procedencia) {
		this.procedencia = procedencia;
	}
	/**
	 * @return Returns the fec_fin.
	 */
	public String getFec_fin() {
		return fec_fin;
	}
	/**
	 * @return Returns the fec_ini.
	 */
	public String getFec_ini() {
		return fec_ini;
	}
	/**
	 * @return Returns the id_cot.
	 */
	public long getId_cot() {
		return id_cot;
	}
	/**
	 * @return Returns the id_empresa.
	 */
	public long getId_empresa() {
		return id_empresa;
	}
	/**
	 * @return Returns the id_estado.
	 */
	public long getId_estado() {
		return id_estado;
	}
	/**
	 * @return Returns the id_local.
	 */
	public long getId_local() {
		return id_local;
	}
	/**
	 * @return Returns the id_sucursal.
	 */
	public long getId_sucursal() {
		return id_sucursal;
	}
	/**
	 * @return Returns the nom_emp.
	 */
	public String getNom_emp() {
		return nom_emp;
	}
	/**
	 * @return Returns the razon_social.
	 */
	public String getRazon_social() {
		return razon_social;
	}
	/**
	 * @return Returns the rut_comprador.
	 */
	public String getRut_comprador() {
		return rut_comprador;
	}
	/**
	 * @return Returns the rut_emp.
	 */
	public String getRut_emp() {
		return rut_emp;
	}
	/**
	 * @return Returns the tipo_fec.
	 */
	public String getTipo_fec() {
		return tipo_fec;
	}
	/**
	 * @param fec_fin The fec_fin to set.
	 */
	public void setFec_fin(String fec_fin) {
		this.fec_fin = fec_fin;
	}
	/**
	 * @param fec_ini The fec_ini to set.
	 */
	public void setFec_ini(String fec_ini) {
		this.fec_ini = fec_ini;
	}
	/**
	 * @param id_cot The id_cot to set.
	 */
	public void setId_cot(long id_cot) {
		this.id_cot = id_cot;
	}
	/**
	 * @param id_empresa The id_empresa to set.
	 */
	public void setId_empresa(long id_empresa) {
		this.id_empresa = id_empresa;
	}
	/**
	 * @param id_estado The id_estado to set.
	 */
	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}
	/**
	 * @param id_local The id_local to set.
	 */
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}
	/**
	 * @param id_sucursal The id_sucursal to set.
	 */
	public void setId_sucursal(long id_sucursal) {
		this.id_sucursal = id_sucursal;
	}
	/**
	 * @param nom_emp The nom_emp to set.
	 */
	public void setNom_emp(String nom_emp) {
		this.nom_emp = nom_emp;
	}
	/**
	 * @param razon_social The razon_social to set.
	 */
	public void setRazon_social(String razon_social) {
		this.razon_social = razon_social;
	}
	/**
	 * @param rut_comprador The rut_comprador to set.
	 */
	public void setRut_comprador(String rut_comprador) {
		this.rut_comprador = rut_comprador;
	}
	/**
	 * @param rut_emp The rut_emp to set.
	 */
	public void setRut_emp(String rut_emp) {
		this.rut_emp = rut_emp;
	}
	/**
	 * @param tipo_fec The tipo_fec to set.
	 */
	public void setTipo_fec(String tipo_fec) {
		this.tipo_fec = tipo_fec;
	}
	/**
	 * @return Returns the nom_estado.
	 */
	public String getNom_estado() {
		return nom_estado;
	}
	/**
	 * @param nom_estado The nom_estado to set.
	 */
	public void setNom_estado(String nom_estado) {
		this.nom_estado = nom_estado;
	}
	/**
	 * @return Returns the pag.
	 */
	public int getPag() {
		return pag;
	}
	/**
	 * @return Returns the regsperpag.
	 */
	public int getRegsperpag() {
		return regsperpag;
	}
	/**
	 * @param pag The pag to set.
	 */
	public void setPag(int pag) {
		this.pag = pag;
	}
	/**
	 * @param regsperpag The regsperpag to set.
	 */
	public void setRegsperpag(int regsperpag) {
		this.regsperpag = regsperpag;
	}
	/**
	 * @return Returns the alias_direccion.
	 */
	public String getAlias_direccion() {
		return alias_direccion;
	}
	/**
	 * @return Returns the nom_sucursal.
	 */
	public String getNom_sucursal() {
		return nom_sucursal;
	}
	/**
	 * @return Returns the ordena_por.
	 */
	public String getOrdena_por() {
		return ordena_por;
	}

	/**
	 * @param alias_direccion The alias_direccion to set.
	 */
	public void setAlias_direccion(String alias_direccion) {
		this.alias_direccion = alias_direccion;
	}
	/**
	 * @param nom_sucursal The nom_sucursal to set.
	 */
	public void setNom_sucursal(String nom_sucursal) {
		this.nom_sucursal = nom_sucursal;
	}
	/**
	 * @param ordena_por The ordena_por to set.
	 */
	public void setOrdena_por(String ordena_por) {
		this.ordena_por = ordena_por;
	}
	/**
	 * @return Returns the id_comprador.
	 */
	public long getId_comprador() {
		return id_comprador;
	}
	/**
	 * @param id_comprador The id_comprador to set.
	 */
	public void setId_comprador(long id_comprador) {
		this.id_comprador = id_comprador;
	}



	
	
	
    /**
     * @return Devuelve tipo_comprador.
     */
    public int getTipo_comprador() {
        return tipo_comprador;
    }
    /**
     * @param tipo_comprador El tipo_comprador a establecer.
     */
    public void setTipo_comprador(int tipo_comprador) {
        this.tipo_comprador = tipo_comprador;
    }
}
