package cl.bbr.vte.cotizaciones.dto;

import java.io.Serializable;

/**
 * DTO para datos de las cotizaciones. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class ModCotizacionDTO implements Serializable {

	private long id_cot;
	private String obs;
	private String fueraMix;
	private double costo_despacho;
	private String usuario;
	private long id_dirfact;
	private String tipo_doc;
	private long id_dirdesp;
	private String fecha_venc;
	private String aut_margen;
	private String aut_dscto;
	private long id_jorn_ref;
	private String fec_desp;
	
	
	
	/**
	 * @return Returns the aut_dscto.
	 */
	public String getAut_dscto() {
		return aut_dscto;
	}
	/**
	 * @param aut_dscto The aut_dscto to set.
	 */
	public void setAut_dscto(String aut_dscto) {
		this.aut_dscto = aut_dscto;
	}
	/**
	 * @return Returns the aut_margen.
	 */
	public String getAut_margen() {
		return aut_margen;
	}
	/**
	 * @param aut_margen The aut_margen to set.
	 */
	public void setAut_margen(String aut_margen) {
		this.aut_margen = aut_margen;
	}
	/**
	 * @return Returns the fecha_venc.
	 */
	public String getFecha_venc() {
		return fecha_venc;
	}
	/**
	 * @param fecha_venc The fecha_venc to set.
	 */
	public void setFecha_venc(String fecha_venc) {
		this.fecha_venc = fecha_venc;
	}
	/**
	 * @return Returns the costo_despacho.
	 */
	public double getCosto_despacho() {
		return costo_despacho;
	}
	/**
	 * @return Returns the fueraMix.
	 */
	public String getFueraMix() {
		return fueraMix;
	}
	/**
	 * @return Returns the id_cot.
	 */
	public long getId_cot() {
		return id_cot;
	}
	/**
	 * @return Returns the obs.
	 */
	public String getObs() {
		return obs;
	}
	/**
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param costo_despacho The costo_despacho to set.
	 */
	public void setCosto_despacho(double costo_despacho) {
		this.costo_despacho = costo_despacho;
	}
	/**
	 * @param fueraMix The fueraMix to set.
	 */
	public void setFueraMix(String fueraMix) {
		this.fueraMix = fueraMix;
	}
	/**
	 * @param id_cot The id_cot to set.
	 */
	public void setId_cot(long id_cot) {
		this.id_cot = id_cot;
	}
	/**
	 * @param obs The obs to set.
	 */
	public void setObs(String obs) {
		this.obs = obs;
	}
	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return Returns the id_dirfact.
	 */
	public long getId_dirfact() {
		return id_dirfact;
	}
	/**
	 * @return Returns the tipo_doc.
	 */
	public String getTipo_doc() {
		return tipo_doc;
	}
	/**
	 * @param id_dirfact The id_dirfact to set.
	 */
	public void setId_dirfact(long id_dirfact) {
		this.id_dirfact = id_dirfact;
	}
	/**
	 * @param tipo_doc The tipo_doc to set.
	 */
	public void setTipo_doc(String tipo_doc) {
		this.tipo_doc = tipo_doc;
	}
	/**
	 * @return Returns the id_dirdesp.
	 */
	public long getId_dirdesp() {
		return id_dirdesp;
	}
	/**
	 * @param id_dirdesp The id_dirdesp to set.
	 */
	public void setId_dirdesp(long id_dirdesp) {
		this.id_dirdesp = id_dirdesp;
	}
	/**
	 * @return Returns the fec_desp.
	 */
	public String getFec_desp() {
		return fec_desp;
	}
	/**
	 * @param fec_desp The fec_desp to set.
	 */
	public void setFec_desp(String fec_desp) {
		this.fec_desp = fec_desp;
	}
	/**
	 * @return Returns the id_jorn_ref.
	 */
	public long getId_jorn_ref() {
		return id_jorn_ref;
	}
	/**
	 * @param id_jorn_ref The id_jorn_ref to set.
	 */
	public void setId_jorn_ref(long id_jorn_ref) {
		this.id_jorn_ref = id_jorn_ref;
	}
	
	
	
	
}


