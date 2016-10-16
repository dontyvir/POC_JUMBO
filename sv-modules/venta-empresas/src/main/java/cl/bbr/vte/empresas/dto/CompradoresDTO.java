package cl.bbr.vte.empresas.dto;

import java.io.Serializable;

/**
 * DTO para datos del comprador 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class CompradoresDTO implements Serializable {

	private long   cpr_id;
	private long   cpr_rut;
	private String cpr_dv;
	private String cpr_nombres;
	private String cpr_ape_pat;
	private String cpr_ape_mat;
	private String cpr_genero;
	private String cpr_fono1;
	private String cpr_fono2;
	private String cpr_fono3;
	private String cpr_email;
	private String cpr_fmod;
	private String cpr_estado;
	private String cpr_pass;
	private String cpr_fec_crea;
	private String cpr_pregunta;
	private String cpr_respuesta;
	private String cpr_bloqueo;
	private String cpr_mod_dato;
	private long cpr_fec_login;
	private long   cpr_intentos;
	//asociacion con la sucursal
	private long   id_sucursal;
	//asociacion con la empresa
	private long   id_empresa;
	private String cpr_tipo;
	
	
	/**
	 * Constructor
	 */
	public CompradoresDTO() {
	}


	public long getId_sucursal() {
		return id_sucursal;
	}


	public void setId_sucursal(long id_sucursal) {
		this.id_sucursal = id_sucursal;
	}


	public String getCpr_ape_mat() {
		return cpr_ape_mat;
	}


	public void setCpr_ape_mat(String cpr_ape_mat) {
		this.cpr_ape_mat = cpr_ape_mat;
	}


	public String getCpr_ape_pat() {
		return cpr_ape_pat;
	}


	public void setCpr_ape_pat(String cpr_ape_pat) {
		this.cpr_ape_pat = cpr_ape_pat;
	}


	public String getCpr_bloqueo() {
		return cpr_bloqueo;
	}


	public void setCpr_bloqueo(String cpr_bloqueo) {
		this.cpr_bloqueo = cpr_bloqueo;
	}


	public String getCpr_dv() {
		return cpr_dv;
	}


	public void setCpr_dv(String cpr_dv) {
		this.cpr_dv = cpr_dv;
	}


	public String getCpr_email() {
		return cpr_email;
	}


	public void setCpr_email(String cpr_email) {
		this.cpr_email = cpr_email;
	}


	public String getCpr_estado() {
		return cpr_estado;
	}


	public void setCpr_estado(String cpr_estado) {
		this.cpr_estado = cpr_estado;
	}


	public String getCpr_fec_crea() {
		return cpr_fec_crea;
	}


	public void setCpr_fec_crea(String cpr_fec_crea) {
		this.cpr_fec_crea = cpr_fec_crea;
	}


	public long getCpr_fec_login() {
		return cpr_fec_login;
	}


	public void setCpr_fec_login(long cpr_fec_login) {
		this.cpr_fec_login = cpr_fec_login;
	}


	public String getCpr_fmod() {
		return cpr_fmod;
	}


	public void setCpr_fmod(String cpr_fmod) {
		this.cpr_fmod = cpr_fmod;
	}


	public String getCpr_fono1() {
		return cpr_fono1;
	}


	public void setCpr_fono1(String cpr_fono1) {
		this.cpr_fono1 = cpr_fono1;
	}


	public String getCpr_fono2() {
		return cpr_fono2;
	}


	public void setCpr_fono2(String cpr_fono2) {
		this.cpr_fono2 = cpr_fono2;
	}


	public String getCpr_fono3() {
		return cpr_fono3;
	}


	public void setCpr_fono3(String cpr_fono3) {
		this.cpr_fono3 = cpr_fono3;
	}


	public String getCpr_genero() {
		return cpr_genero;
	}


	public void setCpr_genero(String cpr_genero) {
		this.cpr_genero = cpr_genero;
	}


	public long getCpr_id() {
		return cpr_id;
	}


	public void setCpr_id(long cpr_id) {
		this.cpr_id = cpr_id;
	}


	public long getCpr_intentos() {
		return cpr_intentos;
	}


	public void setCpr_intentos(long cpr_intentos) {
		this.cpr_intentos = cpr_intentos;
	}


	public String getCpr_mod_dato() {
		return cpr_mod_dato;
	}


	public void setCpr_mod_dato(String cpr_mod_dato) {
		this.cpr_mod_dato = cpr_mod_dato;
	}


	public String getCpr_pass() {
		return cpr_pass;
	}


	public void setCpr_pass(String cpr_pass) {
		this.cpr_pass = cpr_pass;
	}


	public String getCpr_pregunta() {
		return cpr_pregunta;
	}


	public void setCpr_pregunta(String cpr_pregunta) {
		this.cpr_pregunta = cpr_pregunta;
	}


	public String getCpr_respuesta() {
		return cpr_respuesta;
	}


	public void setCpr_respuesta(String cpr_respuesta) {
		this.cpr_respuesta = cpr_respuesta;
	}


	public long getCpr_rut() {
		return cpr_rut;
	}


	public void setCpr_rut(long cpr_rut) {
		this.cpr_rut = cpr_rut;
	}


	public String getCpr_tipo() {
		return cpr_tipo;
	}


	public void setCpr_tipo(String cpr_tipo) {
		this.cpr_tipo = cpr_tipo;
	}


	public String getCpr_nombres() {
		return cpr_nombres;
	}


	public void setCpr_nombres(String cpr_nombres) {
		this.cpr_nombres = cpr_nombres;
	}
		
	public long getId_empresa() {
		return id_empresa;
	}

	public void setId_empresa(long id_empresa) {
		this.id_empresa = id_empresa;
	}

}