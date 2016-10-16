package cl.bbr.vte.cotizaciones.dto;

import java.io.Serializable;

/**
 * Clase que contiene información de la cotización
 * 
 * @author BBR
 *
 */
public class MonitorCotizacionesDTO implements Serializable {
	private long id_cot;
	private String rut_empresa;
	private String nom_empresa;
	private String sucursal;
	private String fec_despacho;
	private String fec_vencimiento;
	private double monto_total;
	private String estado;
	private String fec_ing;
	private String alias_dir;
	private String calle;
	private String numero;
	private long   id_usuario;
	private long   id_usuario_fono = 0;
	private long cot_estado;
	private String nombre_comprador;
	private long id_comprador;
	private double cant_prod;
	private String nom_sucursal;
	
	
    /**
     * @return Devuelve id_usuario_fono.
     */
    public long getId_usuario_fono() {
        return id_usuario_fono;
    }
    /**
     * @param id_usuario_fono El id_usuario_fono a establecer.
     */
    public void setId_usuario_fono(long id_usuario_fono) {
        this.id_usuario_fono = id_usuario_fono;
    }
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @return Returns the fec_despacho.
	 */
	public String getFec_despacho() {
		return fec_despacho;
	}
	/**
	 * @return Returns the fec_vencimiento.
	 */
	public String getFec_vencimiento() {
		return fec_vencimiento;
	}
	/**
	 * @return Returns the id_cot.
	 */
	public long getId_cot() {
		return id_cot;
	}
	/**
	 * @return Returns the monto_total.
	 */
	public double getMonto_total() {
		return monto_total;
	}
	/**
	 * @return Returns the nom_empresa.
	 */
	public String getNom_empresa() {
		return nom_empresa;
	}
	/**
	 * @return Returns the rut_empresa.
	 */
	public String getRut_empresa() {
		return rut_empresa;
	}
	/**
	 * @return Returns the sucursal.
	 */
	public String getSucursal() {
		return sucursal;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @param fec_despacho The fec_despacho to set.
	 */
	public void setFec_despacho(String fec_despacho) {
		this.fec_despacho = fec_despacho;
	}
	/**
	 * @param fec_vencimiento The fec_vencimiento to set.
	 */
	public void setFec_vencimiento(String fec_vencimiento) {
		this.fec_vencimiento = fec_vencimiento;
	}
	/**
	 * @param id_cot The id_cot to set.
	 */
	public void setId_cot(long id_cot) {
		this.id_cot = id_cot;
	}
	/**
	 * @param monto_total The monto_total to set.
	 */
	public void setMonto_total(double monto_total) {
		this.monto_total = monto_total;
	}
	/**
	 * @param nom_empresa The nom_empresa to set.
	 */
	public void setNom_empresa(String nom_empresa) {
		this.nom_empresa = nom_empresa;
	}
	/**
	 * @param rut_empresa The rut_empresa to set.
	 */
	public void setRut_empresa(String rut_empresa) {
		this.rut_empresa = rut_empresa;
	}
	/**
	 * @param sucursal The sucursal to set.
	 */
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}
	/**
	 * @return Returns the alias_dir.
	 */
	public String getAlias_dir() {
		return alias_dir;
	}
	/**
	 * @return Returns the calle.
	 */
	public String getCalle() {
		return calle;
	}
	/**
	 * @return Returns the fec_ing.
	 */
	public String getFec_ing() {
		return fec_ing;
	}
	/**
	 * @return Returns the numero.
	 */
	public String getNumero() {
		return numero;
	}
	/**
	 * @param alias_dir The alias_dir to set.
	 */
	public void setAlias_dir(String alias_dir) {
		this.alias_dir = alias_dir;
	}
	/**
	 * @param calle The calle to set.
	 */
	public void setCalle(String calle) {
		this.calle = calle;
	}
	/**
	 * @param fec_ing The fec_ing to set.
	 */
	public void setFec_ing(String fec_ing) {
		this.fec_ing = fec_ing;
	}
	/**
	 * @param numero The numero to set.
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}
	/**
	 * @return Returns the id_usuario.
	 */
	public long getId_usuario() {
		return id_usuario;
	}
	/**
	 * @param id_usuario The id_usuario to set.
	 */
	public void setId_usuario(long id_usuario) {
		this.id_usuario = id_usuario;
	}
	/**
	 * @return Returns the cot_estado.
	 */
	public long getCot_estado() {
		return cot_estado;
	}
	/**
	 * @param cot_estado The cot_estado to set.
	 */
	public void setCot_estado(long cot_estado) {
		this.cot_estado = cot_estado;
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
	 * @return Returns the nombre_comprador.
	 */
	public String getNombre_comprador() {
		return nombre_comprador;
	}
	/**
	 * @param nombre_comprador The nombre_comprador to set.
	 */
	public void setNombre_comprador(String nombre_comprador) {
		this.nombre_comprador = nombre_comprador;
	}
	/**
	 * @return Returns the cant_prod.
	 */
	public double getCant_prod() {
		return cant_prod;
	}
	/**
	 * @param cant_prod The cant_prod to set.
	 */
	public void setCant_prod(double cant_prod) {
		this.cant_prod = cant_prod;
	}
	/**
	 * @return Returns the nom_sucursal.
	 */
	public String getNom_sucursal() {
		return nom_sucursal;
	}
	/**
	 * @param nom_sucursal The nom_sucursal to set.
	 */
	public void setNom_sucursal(String nom_sucursal) {
		this.nom_sucursal = nom_sucursal;
	}

	
}
