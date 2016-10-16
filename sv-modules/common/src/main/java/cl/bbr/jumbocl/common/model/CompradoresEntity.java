package cl.bbr.jumbocl.common.model;

import java.sql.Timestamp;

/**
 * @author BBRI
 * 
 */
public class CompradoresEntity {
	
	private long   		cpr_id;
	private long   		cpr_rut;
	private String 		cpr_dv;
	private String 		cpr_nombres;
	private String 		cpr_ape_pat;
	private String 		cpr_ape_mat;
	private String 		cpr_genero;
	private String 		cpr_fono1;
	private String 		cpr_fono2;
	private String 		cpr_fono3;
	private String 		cpr_email;
	private Timestamp   cpr_fmod;
	private String 		cpr_estado;
	private String 		cpr_pass;
	private Timestamp   cpr_fec_crea;
	private String 		cpr_pregunta;
	private String 		cpr_respuesta;
	private String 		cpr_bloqueo;
	private String 		cpr_mod_dato;
	private Timestamp   cpr_fec_login;
	private long   		cpr_intentos;
	
	public CompradoresEntity() {
		super();
	}

	/**
	 * @return Returns the cpr_ape_mat.
	 */
	public String getCpr_ape_mat() {
		return cpr_ape_mat;
	}


	/**
	 * @param cpr_ape_mat The cpr_ape_mat to set.
	 */
	public void setCpr_ape_mat(String cpr_ape_mat) {
		this.cpr_ape_mat = cpr_ape_mat;
	}


	/**
	 * @return Returns the cpr_ape_pat.
	 */
	public String getCpr_ape_pat() {
		return cpr_ape_pat;
	}


	/**
	 * @param cpr_ape_pat The cpr_ape_pat to set.
	 */
	public void setCpr_ape_pat(String cpr_ape_pat) {
		this.cpr_ape_pat = cpr_ape_pat;
	}


	/**
	 * @return Returns the cpr_bloqueo.
	 */
	public String getCpr_bloqueo() {
		return cpr_bloqueo;
	}


	/**
	 * @param cpr_bloqueo The cpr_bloqueo to set.
	 */
	public void setCpr_bloqueo(String cpr_bloqueo) {
		this.cpr_bloqueo = cpr_bloqueo;
	}


	/**
	 * @return Returns the cpr_dv.
	 */
	public String getCpr_dv() {
		return cpr_dv;
	}


	/**
	 * @param cpr_dv The cpr_dv to set.
	 */
	public void setCpr_dv(String cpr_dv) {
		this.cpr_dv = cpr_dv;
	}


	/**
	 * @return Returns the cpr_email.
	 */
	public String getCpr_email() {
		return cpr_email;
	}


	/**
	 * @param cpr_email The cpr_email to set.
	 */
	public void setCpr_email(String cpr_email) {
		this.cpr_email = cpr_email;
	}


	/**
	 * @return Returns the cpr_estado.
	 */
	public String getCpr_estado() {
		return cpr_estado;
	}


	/**
	 * @param cpr_estado The cpr_estado to set.
	 */
	public void setCpr_estado(String cpr_estado) {
		this.cpr_estado = cpr_estado;
	}


	/**
	 * @return Returns the cpr_fec_crea.
	 */
	public Timestamp getCpr_fec_crea() {
		return cpr_fec_crea;
	}


	/**
	 * @param cpr_fec_crea The cpr_fec_crea to set.
	 */
	public void setCpr_fec_crea(Timestamp cpr_fec_crea) {
		this.cpr_fec_crea = cpr_fec_crea;
	}


	/**
	 * @return Returns the cpr_fec_login.
	 */
	public Timestamp getCpr_fec_login() {
		return cpr_fec_login;
	}


	/**
	 * @param cpr_fec_login The cpr_fec_login to set.
	 */
	public void setCpr_fec_login(Timestamp cpr_fec_login) {
		this.cpr_fec_login = cpr_fec_login;
	}


	/**
	 * @return Returns the cpr_fmod.
	 */
	public Timestamp getCpr_fmod() {
		return cpr_fmod;
	}


	/**
	 * @param cpr_fmod The cpr_fmod to set.
	 */
	public void setCpr_fmod(Timestamp cpr_fmod) {
		this.cpr_fmod = cpr_fmod;
	}


	/**
	 * @return Returns the cpr_fono1.
	 */
	public String getCpr_fono1() {
		return cpr_fono1;
	}


	/**
	 * @param cpr_fono1 The cpr_fono1 to set.
	 */
	public void setCpr_fono1(String cpr_fono1) {
		this.cpr_fono1 = cpr_fono1;
	}


	/**
	 * @return Returns the cpr_fono2.
	 */
	public String getCpr_fono2() {
		return cpr_fono2;
	}


	/**
	 * @param cpr_fono2 The cpr_fono2 to set.
	 */
	public void setCpr_fono2(String cpr_fono2) {
		this.cpr_fono2 = cpr_fono2;
	}


	/**
	 * @return Returns the cpr_fono3.
	 */
	public String getCpr_fono3() {
		return cpr_fono3;
	}


	/**
	 * @param cpr_fono3 The cpr_fono3 to set.
	 */
	public void setCpr_fono3(String cpr_fono3) {
		this.cpr_fono3 = cpr_fono3;
	}


	/**
	 * @return Returns the cpr_genero.
	 */
	public String getCpr_genero() {
		return cpr_genero;
	}


	/**
	 * @param cpr_genero The cpr_genero to set.
	 */
	public void setCpr_genero(String cpr_genero) {
		this.cpr_genero = cpr_genero;
	}


	/**
	 * @return Returns the cpr_id.
	 */
	public long getCpr_id() {
		return cpr_id;
	}


	/**
	 * @param cpr_id The cpr_id to set.
	 */
	public void setCpr_id(long cpr_id) {
		this.cpr_id = cpr_id;
	}


	/**
	 * @return Returns the cpr_intentos.
	 */
	public long getCpr_intentos() {
		return cpr_intentos;
	}


	/**
	 * @param cpr_intentos The cpr_intentos to set.
	 */
	public void setCpr_intentos(long cpr_intentos) {
		this.cpr_intentos = cpr_intentos;
	}


	/**
	 * @return Returns the cpr_mod_dato.
	 */
	public String getCpr_mod_dato() {
		return cpr_mod_dato;
	}


	/**
	 * @param cpr_mod_dato The cpr_mod_dato to set.
	 */
	public void setCpr_mod_dato(String cpr_mod_dato) {
		this.cpr_mod_dato = cpr_mod_dato;
	}


	/**
	 * @return Returns the cpr_pass.
	 */
	public String getCpr_pass() {
		return cpr_pass;
	}


	/**
	 * @param cpr_pass The cpr_pass to set.
	 */
	public void setCpr_pass(String cpr_pass) {
		this.cpr_pass = cpr_pass;
	}


	/**
	 * @return Returns the cpr_pregunta.
	 */
	public String getCpr_pregunta() {
		return cpr_pregunta;
	}


	/**
	 * @param cpr_pregunta The cpr_pregunta to set.
	 */
	public void setCpr_pregunta(String cpr_pregunta) {
		this.cpr_pregunta = cpr_pregunta;
	}


	/**
	 * @return Returns the cpr_respuesta.
	 */
	public String getCpr_respuesta() {
		return cpr_respuesta;
	}


	/**
	 * @param cpr_respuesta The cpr_respuesta to set.
	 */
	public void setCpr_respuesta(String cpr_respuesta) {
		this.cpr_respuesta = cpr_respuesta;
	}


	/**
	 * @return Returns the cpr_rut.
	 */
	public long getCpr_rut() {
		return cpr_rut;
	}


	/**
	 * @param cpr_rut The cpr_rut to set.
	 */
	public void setCpr_rut(long cpr_rut) {
		this.cpr_rut = cpr_rut;
	}


	/**
	 * @return Returns the crp_nombres.
	 */
	public String getCpr_nombres() {
		return cpr_nombres;
	}


	/**
	 * @param crp_nombres The crp_nombres to set.
	 */
	public void setCpr_nombres(String cpr_nombres) {
		this.cpr_nombres = cpr_nombres;
	}


	public String toString() {
		
		String res = "";

		res += this.getCpr_rut() + " | ";
		res += this.getCpr_nombres() + " | ";
		res += this.getCpr_ape_mat() + " | ";
		res += this.getCpr_ape_pat() + " | ";
		res += this.getCpr_email() + " | ";
		
		return res;
		
	}

	
}