package cl.bbr.vte.empresas.dto;

import java.io.Serializable;

/**
 * DTO para datos de la solicitud de registro 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class SolRegDTO implements Serializable {

	private long   id_reg;
	private String nom_contacto;
	private String cargo;
	private String tel_cod1;
	private String tel_num1;
	private String tel_cod2;
	private String tel_num2;
	private String tel_cod3;
	private String tel_num3;
	private String email;
	private String rut_emp;
	private String nom_emp;
	private String raz_social;
	private String giro;
	private long   tam_empresa;
	private String dir_comercial;
	private String estado;
	private String fec_ing;	
	private String ciudad;	
	private String comuna;	

	
	/**
	 * Constructor
	 */
	public SolRegDTO() {
	}

	/**
	 * @return Returns the cargo.
	 */
	public String getCargo() {
		return cargo;
	}

	/**
	 * @param cargo The cargo to set.
	 */
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	/**
	 * @return Returns the dir_comercial.
	 */
	public String getDir_comercial() {
		return dir_comercial;
	}

	/**
	 * @param dir_comercial The dir_comercial to set.
	 */
	public void setDir_comercial(String dir_comercial) {
		this.dir_comercial = dir_comercial;
	}

	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * @return Returns the fec_ing.
	 */
	public String getFec_ing() {
		return fec_ing;
	}

	/**
	 * @param fec_ing The fec_ing to set.
	 */
	public void setFec_ing(String fec_ing) {
		this.fec_ing = fec_ing;
	}

	/**
	 * @return Returns the giro.
	 */
	public String getGiro() {
		return giro;
	}

	/**
	 * @param giro The giro to set.
	 */
	public void setGiro(String giro) {
		this.giro = giro;
	}

	/**
	 * @return Returns the id_reg.
	 */
	public long getId_reg() {
		return id_reg;
	}

	/**
	 * @param id_reg The id_reg to set.
	 */
	public void setId_reg(long id_reg) {
		this.id_reg = id_reg;
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
	 * @return Returns the nom_emp.
	 */
	public String getNom_emp() {
		return nom_emp;
	}

	/**
	 * @param nom_emp The nom_emp to set.
	 */
	public void setNom_emp(String nom_emp) {
		this.nom_emp = nom_emp;
	}

	/**
	 * @return Returns the raz_social.
	 */
	public String getRaz_social() {
		return raz_social;
	}

	/**
	 * @param raz_social The raz_social to set.
	 */
	public void setRaz_social(String raz_social) {
		this.raz_social = raz_social;
	}

	/**
	 * @return Returns the rut_emp.
	 */
	public String getRut_emp() {
		return rut_emp;
	}

	/**
	 * @param rut_emp The rut_emp to set.
	 */
	public void setRut_emp(String rut_emp) {
		this.rut_emp = rut_emp;
	}

	/**
	 * @return Returns the tam_empresa.
	 */
	public long getTam_empresa() {
		return tam_empresa;
	}

	/**
	 * @param tam_empresa The tam_empresa to set.
	 */
	public void setTam_empresa(long tam_empresa) {
		this.tam_empresa = tam_empresa;
	}

	/**
	 * @return Returns the tel_cod1.
	 */
	public String getTel_cod1() {
		return tel_cod1;
	}

	/**
	 * @param tel_cod1 The tel_cod1 to set.
	 */
	public void setTel_cod1(String tel_cod1) {
		this.tel_cod1 = tel_cod1;
	}

	/**
	 * @return Returns the tel_cod2.
	 */
	public String getTel_cod2() {
		return tel_cod2;
	}

	/**
	 * @param tel_cod2 The tel_cod2 to set.
	 */
	public void setTel_cod2(String tel_cod2) {
		this.tel_cod2 = tel_cod2;
	}

	/**
	 * @return Returns the tel_cod3.
	 */
	public String getTel_cod3() {
		return tel_cod3;
	}

	/**
	 * @param tel_cod3 The tel_cod3 to set.
	 */
	public void setTel_cod3(String tel_cod3) {
		this.tel_cod3 = tel_cod3;
	}

	/**
	 * @return Returns the tel_num1.
	 */
	public String getTel_num1() {
		return tel_num1;
	}

	/**
	 * @param tel_num1 The tel_num1 to set.
	 */
	public void setTel_num1(String tel_num1) {
		this.tel_num1 = tel_num1;
	}

	/**
	 * @return Returns the tel_num2.
	 */
	public String getTel_num2() {
		return tel_num2;
	}

	/**
	 * @param tel_num2 The tel_num2 to set.
	 */
	public void setTel_num2(String tel_num2) {
		this.tel_num2 = tel_num2;
	}

	/**
	 * @return Returns the tel_num3.
	 */
	public String getTel_num3() {
		return tel_num3;
	}

	/**
	 * @param tel_num3 The tel_num3 to set.
	 */
	public void setTel_num3(String tel_num3) {
		this.tel_num3 = tel_num3;
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
	 * @return Returns the comuna.
	 */
	public String getComuna() {
		return comuna;
	}

	/**
	 * @param comuna The comuna to set.
	 */
	public void setComuna(String comuna) {
		this.comuna = comuna;
	}


}