package cl.bbr.vte.empresas.dto;

import java.io.Serializable;

/**
 * DTO para datos de las empresas 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class EmpresasDTO implements Serializable {

	private long   	emp_id;
	private long   	emp_rut;
	private String 	emp_dv;
	private String 	emp_nom;
	private String 	emp_descr;
	private String 	emp_rzsocial;
	private String 	emp_rubro;
	private String 	emp_tamano;
	private long   	emp_qtyemp;
	private String 	emp_nom_contacto;
	private String 	emp_fono1_contacto;
	private String 	emp_fono2_contacto;
	private String 	emp_fono3_contacto;
	private String 	emp_mail_contacto;
	private String 	emp_cargo_contacto;
	private double 	emp_saldo;
	private String 	emp_fact_saldo;
	private String 	emp_fmod;
	private String 	emp_estado;
	private double	emp_mrg_minimo;
	private String	emp_fec_crea;
	//Informacion de linea de credito
	private CargosDTO	cargo;
	//indicador de modificacion de razon social
	private int 	emp_mod_rzsoc;
	//descuento maximo
	private double  emp_dscto_max;
	private String emp_cod_fon1;
	private String emp_cod_fon2;
	private String emp_cod_fon3;
	
	/**
	 * Constructor
	 */
	public EmpresasDTO() {
	}
	
	/**
	 * @return Returns the emp_cod_fon1.
	 */
	public String getEmp_cod_fon1() {
		return emp_cod_fon1;
	}

	/**
	 * @param emp_cod_fon1 The emp_cod_fon1 to set.
	 */
	public void setEmp_cod_fon1(String emp_cod_fon1) {
		this.emp_cod_fon1 = emp_cod_fon1;
	}

	/**
	 * @return Returns the emp_cod_fon2.
	 */
	public String getEmp_cod_fon2() {
		return emp_cod_fon2;
	}

	/**
	 * @param emp_cod_fon2 The emp_cod_fon2 to set.
	 */
	public void setEmp_cod_fon2(String emp_cod_fon2) {
		this.emp_cod_fon2 = emp_cod_fon2;
	}

	/**
	 * @return Returns the emp_cod_fon3.
	 */
	public String getEmp_cod_fon3() {
		return emp_cod_fon3;
	}

	/**
	 * @param emp_cod_fon3 The emp_cod_fon3 to set.
	 */
	public void setEmp_cod_fon3(String emp_cod_fon3) {
		this.emp_cod_fon3 = emp_cod_fon3;
	}

	public double getEmp_dscto_max() {
		return emp_dscto_max;
	}

	public void setEmp_dscto_max(double emp_dscto_max) {
		this.emp_dscto_max = emp_dscto_max;
	}

	public int getEmp_mod_rzsoc() {
		return emp_mod_rzsoc;
	}

	public void setEmp_mod_rzsoc(int emp_mod_rzsoc) {
		this.emp_mod_rzsoc = emp_mod_rzsoc;
	}

	public String getEmp_fec_crea() {
		return emp_fec_crea;
	}

	public void setEmp_fec_crea(String emp_fec_crea) {
		this.emp_fec_crea = emp_fec_crea;
	}

	/**
	 * @return Returns the emp_cargo_contacto.
	 */
	public String getEmp_cargo_contacto() {
		return emp_cargo_contacto;
	}
	/**
	 * @param emp_cargo_contacto The emp_cargo_contacto to set.
	 */
	public void setEmp_cargo_contacto(String emp_cargo_contacto) {
		this.emp_cargo_contacto = emp_cargo_contacto;
	}
	/**
	 * @return Returns the emp_descr.
	 */
	public String getEmp_descr() {
		return emp_descr;
	}
	/**
	 * @param emp_descr The emp_descr to set.
	 */
	public void setEmp_descr(String emp_descr) {
		this.emp_descr = emp_descr;
	}
	/**
	 * @return Returns the emp_disp.
	 */
	
	/**
	 * @return Returns the emp_dv.
	 */
	public String getEmp_dv() {
		return emp_dv;
	}
	/**
	 * @param emp_dv The emp_dv to set.
	 */
	public void setEmp_dv(String emp_dv) {
		this.emp_dv = emp_dv;
	}
	/**
	 * @return Returns the emp_estado.
	 */
	public String getEmp_estado() {
		return emp_estado;
	}
	/**
	 * @param emp_estado The emp_estado to set.
	 */
	public void setEmp_estado(String emp_estado) {
		this.emp_estado = emp_estado;
	}
	/**
	 * @return Returns the emp_fact_saldo.
	 */
	public String getEmp_fact_saldo() {
		return emp_fact_saldo;
	}
	/**
	 * @param emp_fact_saldo The emp_fact_saldo to set.
	 */
	public void setEmp_fact_saldo(String emp_fact_saldo) {
		this.emp_fact_saldo = emp_fact_saldo;
	}
	/**
	 * @return Returns the emp_fmod.
	 */
	public String getEmp_fmod() {
		return emp_fmod;
	}
	/**
	 * @param emp_fmod The emp_fmod to set.
	 */
	public void setEmp_fmod(String emp_fmod) {
		this.emp_fmod = emp_fmod;
	}
	/**
	 * @return Returns the emp_fono1_contacto.
	 */
	public String getEmp_fono1_contacto() {
		return emp_fono1_contacto;
	}
	/**
	 * @param emp_fono1_contacto The emp_fono1_contacto to set.
	 */
	public void setEmp_fono1_contacto(String emp_fono1_contacto) {
		this.emp_fono1_contacto = emp_fono1_contacto;
	}
	/**
	 * @return Returns the emp_fono2_contacto.
	 */
	public String getEmp_fono2_contacto() {
		return emp_fono2_contacto;
	}
	/**
	 * @param emp_fono2_contacto The emp_fono2_contacto to set.
	 */
	public void setEmp_fono2_contacto(String emp_fono2_contacto) {
		this.emp_fono2_contacto = emp_fono2_contacto;
	}
	/**
	 * @return Returns the emp_fono3_contacto.
	 */
	public String getEmp_fono3_contacto() {
		return emp_fono3_contacto;
	}
	/**
	 * @param emp_fono3_contacto The emp_fono3_contacto to set.
	 */
	public void setEmp_fono3_contacto(String emp_fono3_contacto) {
		this.emp_fono3_contacto = emp_fono3_contacto;
	}
	/**
	 * @return Returns the emp_id.
	 */
	public long getEmp_id() {
		return emp_id;
	}
	/**
	 * @param emp_id The emp_id to set.
	 */
	public void setEmp_id(long emp_id) {
		this.emp_id = emp_id;
	}
	/**
	 * @return Returns the emp_mail_contacto.
	 */
	public String getEmp_mail_contacto() {
		return emp_mail_contacto;
	}
	/**
	 * @param emp_mail_contacto The emp_mail_contacto to set.
	 */
	public void setEmp_mail_contacto(String emp_mail_contacto) {
		this.emp_mail_contacto = emp_mail_contacto;
	}
	/**
	 * @return Returns the emp_nom.
	 */
	public String getEmp_nom() {
		return emp_nom;
	}
	/**
	 * @param emp_nom The emp_nom to set.
	 */
	public void setEmp_nom(String emp_nom) {
		this.emp_nom = emp_nom;
	}
	/**
	 * @return Returns the emp_nom_contacto.
	 */
	public String getEmp_nom_contacto() {
		return emp_nom_contacto;
	}
	/**
	 * @param emp_nom_contacto The emp_nom_contacto to set.
	 */
	public void setEmp_nom_contacto(String emp_nom_contacto) {
		this.emp_nom_contacto = emp_nom_contacto;
	}
	/**
	 * @return Returns the emp_qtyemp.
	 */
	public long getEmp_qtyemp() {
		return emp_qtyemp;
	}
	/**
	 * @param emp_qtyemp The emp_qtyemp to set.
	 */
	public void setEmp_qtyemp(long emp_qtyemp) {
		this.emp_qtyemp = emp_qtyemp;
	}
	/**
	 * @return Returns the emp_rubro.
	 */
	public String getEmp_rubro() {
		return emp_rubro;
	}
	/**
	 * @param emp_rubro The emp_rubro to set.
	 */
	public void setEmp_rubro(String emp_rubro) {
		this.emp_rubro = emp_rubro;
	}
	/**
	 * @return Returns the emp_rut.
	 */
	public long getEmp_rut() {
		return emp_rut;
	}
	/**
	 * @param emp_rut The emp_rut to set.
	 */
	public void setEmp_rut(long emp_rut) {
		this.emp_rut = emp_rut;
	}
	/**
	 * @return Returns the emp_rzsocial.
	 */
	public String getEmp_rzsocial() {
		return emp_rzsocial;
	}
	/**
	 * @param emp_rzsocial The emp_rzsocial to set.
	 */
	public void setEmp_rzsocial(String emp_rzsocial) {
		this.emp_rzsocial = emp_rzsocial;
	}
	/**
	 * @return Returns the emp_saldo.
	 */
	public double getEmp_saldo() {
		return emp_saldo;
	}
	/**
	 * @param emp_saldo The emp_saldo to set.
	 */
	public void setEmp_saldo(double emp_saldo) {
		this.emp_saldo = emp_saldo;
	}
	/**
	 * @return Returns the emp_tamano.
	 */
	public String getEmp_tamano() {
		return emp_tamano;
	}
	/**
	 * @param emp_tamano The emp_tamano to set.
	 */
	public void setEmp_tamano(String emp_tamano) {
		this.emp_tamano = emp_tamano;
	}

	/**
	 * @return Returns the emp_mrg_minimo.
	 */
	public double getEmp_mrg_minimo() {
		return emp_mrg_minimo;
	}

	/**
	 * @param emp_mrg_minimo The emp_mrg_minimo to set.
	 */
	public void setEmp_mrg_minimo(double emp_mrg_minimo) {
		this.emp_mrg_minimo = emp_mrg_minimo;
	}

	/**
	 * @return Returns the cargo.
	 */
	public CargosDTO getCargo() {
		return cargo;
	}

	/**
	 * @param cargo The cargo to set.
	 */
	public void setCargo(CargosDTO cargo) {
		this.cargo = cargo;
	}


	
}