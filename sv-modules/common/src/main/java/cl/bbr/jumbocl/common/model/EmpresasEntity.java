package cl.bbr.jumbocl.common.model;

import java.sql.Timestamp;

/**
 * Clase que captura desde la Base de Datos los datos de empresa
 * 
 * @author BBR
 *
 */
public class EmpresasEntity {

	private Long 	id;
	private Long 	rut;
	private Character dv;
	private String 	nombre;
	private String 	descripcion;
	private String 	rsocial;
	private String 	rubro;
	private String 	tamano;
	private Integer	qtyemp;
	private String 	nom_contacto;
	private String 	fono1_contacto;
	private String 	fono2_contacto;
	private String 	fono3_contacto;
	private String 	mail_contacto;
	private String 	cargo_contacto;
	private Double 	saldo;
	private Timestamp fact_saldo;
	private Timestamp fmod;
	private String	estado;
	private Double mrg_minimo;
	private Timestamp fec_crea;
	private Double  dscto_max;
	private Integer mod_rzsoc;
	private String cod_fon1;
	private String cod_fon2;
	private String cod_fon3;
	
	/**
	 * Constructor
	 */
	public EmpresasEntity() {
	}

	/**
	 * @return Returns the cod_fon1.
	 */
	public String getCod_fon1() {
		return cod_fon1;
	}

	/**
	 * @param cod_fon1 The cod_fon1 to set.
	 */
	public void setCod_fon1(String cod_fon1) {
		this.cod_fon1 = cod_fon1;
	}

	/**
	 * @return Returns the cod_fon2.
	 */
	public String getCod_fon2() {
		return cod_fon2;
	}

	/**
	 * @param cod_fon2 The cod_fon2 to set.
	 */
	public void setCod_fon2(String cod_fon2) {
		this.cod_fon2 = cod_fon2;
	}

	/**
	 * @return Returns the cod_fon3.
	 */
	public String getCod_fon3() {
		return cod_fon3;
	}

	/**
	 * @param cod_fon3 The cod_fon3 to set.
	 */
	public void setCod_fon3(String cod_fon3) {
		this.cod_fon3 = cod_fon3;
	}

	public Double getDscto_max() {
		return dscto_max;
	}

	public void setDscto_max(Double dscto_max) {
		this.dscto_max = dscto_max;
	}

	public Integer getMod_rzsoc() {
		return mod_rzsoc;
	}

	public void setMod_rzsoc(Integer mod_rzsoc) {
		this.mod_rzsoc = mod_rzsoc;
	}

	public Timestamp getFec_crea() {
		return fec_crea;
	}

	public void setFec_crea(Timestamp fec_crea) {
		this.fec_crea = fec_crea;
	}

	/**
	 * @return Returns the cargo_contacto.
	 */
	public String getCargo_contacto() {
		return cargo_contacto;
	}

	/**
	 * @param cargo_contacto The cargo_contacto to set.
	 */
	public void setCargo_contacto(String cargo_contacto) {
		this.cargo_contacto = cargo_contacto;
	}

	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	/**
	 * @return Returns the dv.
	 */
	public Character getDv() {
		return dv;
	}

	/**
	 * @param dv The dv to set.
	 */
	public void setDv(Character dv) {
		this.dv = dv;
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return Returns the fact_saldo.
	 */
	public Timestamp getFact_saldo() {
		return fact_saldo;
	}

	/**
	 * @param fact_saldo The fact_saldo to set.
	 */
	public void setFact_saldo(Timestamp fact_saldo) {
		this.fact_saldo = fact_saldo;
	}

	/**
	 * @return Returns the fmod.
	 */
	public Timestamp getFmod() {
		return fmod;
	}

	/**
	 * @param fmod The fmod to set.
	 */
	public void setFmod(Timestamp fmod) {
		this.fmod = fmod;
	}

	/**
	 * @return Returns the fono1_contacto.
	 */
	public String getFono1_contacto() {
		return fono1_contacto;
	}

	/**
	 * @param fono1_contacto The fono1_contacto to set.
	 */
	public void setFono1_contacto(String fono1_contacto) {
		this.fono1_contacto = fono1_contacto;
	}

	/**
	 * @return Returns the fono2_contacto.
	 */
	public String getFono2_contacto() {
		return fono2_contacto;
	}

	/**
	 * @param fono2_contacto The fono2_contacto to set.
	 */
	public void setFono2_contacto(String fono2_contacto) {
		this.fono2_contacto = fono2_contacto;
	}

	/**
	 * @return Returns the fono3_contacto.
	 */
	public String getFono3_contacto() {
		return fono3_contacto;
	}

	/**
	 * @param fono3_contacto The fono3_contacto to set.
	 */
	public void setFono3_contacto(String fono3_contacto) {
		this.fono3_contacto = fono3_contacto;
	}

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns the mail_contacto.
	 */
	public String getMail_contacto() {
		return mail_contacto;
	}

	/**
	 * @param mail_contacto The mail_contacto to set.
	 */
	public void setMail_contacto(String mail_contacto) {
		this.mail_contacto = mail_contacto;
	}

	/**
	 * @return Returns the nom_contacto.
	 */
	public String getNom_contacto() {
		return nom_contacto;
	}

	/**
	 * @param nom_contacto The nom_contacto to set.
	 */
	public void setNom_contacto(String nom_contacto) {
		this.nom_contacto = nom_contacto;
	}

	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return Returns the qtyemp.
	 */
	public Integer getQtyemp() {
		return qtyemp;
	}

	/**
	 * @param qtyemp The qtyemp to set.
	 */
	public void setQtyemp(Integer qtyemp) {
		this.qtyemp = qtyemp;
	}

	/**
	 * @return Returns the rsocial.
	 */
	public String getRsocial() {
		return rsocial;
	}

	/**
	 * @param rsocial The rsocial to set.
	 */
	public void setRsocial(String rsocial) {
		this.rsocial = rsocial;
	}

	/**
	 * @return Returns the rubro.
	 */
	public String getRubro() {
		return rubro;
	}

	/**
	 * @param rubro The rubro to set.
	 */
	public void setRubro(String rubro) {
		this.rubro = rubro;
	}

	/**
	 * @return Returns the rut.
	 */
	public Long getRut() {
		return rut;
	}

	/**
	 * @param rut The rut to set.
	 */
	public void setRut(Long rut) {
		this.rut = rut;
	}

	/**
	 * @return Returns the saldo.
	 */
	public Double getSaldo() {
		return saldo;
	}

	/**
	 * @param saldo The saldo to set.
	 */
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	/**
	 * @return Returns the tamano.
	 */
	public String getTamano() {
		return tamano;
	}

	/**
	 * @param tamano The tamano to set.
	 */
	public void setTamano(String tamano) {
		this.tamano = tamano;
	}

	/**
	 * @return Returns the mrg_minimo.
	 */
	public Double getMrg_minimo() {
		return mrg_minimo;
	}

	/**
	 * @param mrg_minimo The mrg_minimo to set.
	 */
	public void setMrg_minimo(Double mrg_minimo) {
		this.mrg_minimo = mrg_minimo;
	}


}