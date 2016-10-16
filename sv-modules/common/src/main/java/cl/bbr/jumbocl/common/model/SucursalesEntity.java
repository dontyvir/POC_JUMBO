package cl.bbr.jumbocl.common.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Clase que captura desde la Base de Datos los datos de sucursal
 * 
 * @author BBR
 *
 */
public class SucursalesEntity {
	
	private Long 	suc_id;
	private Long 	suc_emp_id;
	private Long 	suc_rut;
	private String 	suc_dv;
	private String 	suc_razon;
	private String 	suc_nombre;
	private String 	suc_descr;
	private String 	suc_clave;
	private String 	suc_email;
	private String 	suc_fono_cod1;
	private String 	suc_fono_num1;
	private String 	suc_fono_cod2;
	private String 	suc_fono_num2;
	private Integer 	suc_rec_info;
	private Timestamp 	suc_fec_crea;
	private Timestamp 	suc_fec_act;
	private String 	suc_estado; 
	private Date 	suc_fec_nac;
	private String 	suc_genero;
	private String 	suc_pregunta;
	private String 	suc_respuesta;
	private String 	suc_bloqueo;
	private String 	suc_mod_dato;
	private Timestamp 	suc_fec_login;
	private Integer		suc_intentos;
	private String 	suc_tipo;
	private String 	suc_desc_estado;
	private String 	emp_nom;
	
	/**
	 * @return Returns the suc_bloqueo.
	 */
	public String getSuc_bloqueo() {
		return suc_bloqueo;
	}
	/**
	 * @param suc_bloqueo The suc_bloqueo to set.
	 */
	public void setSuc_bloqueo(String suc_bloqueo) {
		this.suc_bloqueo = suc_bloqueo;
	}
	/**
	 * @return Returns the suc_clave.
	 */
	public String getSuc_clave() {
		return suc_clave;
	}
	/**
	 * @param suc_clave The suc_clave to set.
	 */
	public void setSuc_clave(String suc_clave) {
		this.suc_clave = suc_clave;
	}
	/**
	 * @return Returns the suc_descr.
	 */
	public String getSuc_descr() {
		return suc_descr;
	}
	/**
	 * @param suc_descr The suc_descr to set.
	 */
	public void setSuc_descr(String suc_descr) {
		this.suc_descr = suc_descr;
	}
	public String getSuc_nombre() {
		return suc_nombre;
	}
	public void setSuc_nombre(String suc_nombre) {
		this.suc_nombre = suc_nombre;
	}
	/**
	 * @return Returns the suc_dv.
	 */
	public String getSuc_dv() {
		return suc_dv;
	}
	/**
	 * @param suc_dv The suc_dv to set.
	 */
	public void setSuc_dv(String suc_dv) {
		this.suc_dv = suc_dv;
	}
	/**
	 * @return Returns the suc_email.
	 */
	public String getSuc_email() {
		return suc_email;
	}
	/**
	 * @param suc_email The suc_email to set.
	 */
	public void setSuc_email(String suc_email) {
		this.suc_email = suc_email;
	}
	/**
	 * @return Returns the suc_emp_id.
	 */
	public Long getSuc_emp_id() {
		return suc_emp_id;
	}
	/**
	 * @param suc_emp_id The suc_emp_id to set.
	 */
	public void setSuc_emp_id(Long suc_emp_id) {
		this.suc_emp_id = suc_emp_id;
	}
	/**
	 * @return Returns the suc_estado.
	 */
	public String getSuc_estado() {
		return suc_estado;
	}
	/**
	 * @param suc_estado The suc_estado to set.
	 */
	public void setSuc_estado(String suc_estado) {
		this.suc_estado = suc_estado;
	}
	/**
	 * @return Returns the suc_fec_act.
	 */
	public Timestamp getSuc_fec_act() {
		return suc_fec_act;
	}
	/**
	 * @param suc_fec_act The suc_fec_act to set.
	 */
	public void setSuc_fec_act(Timestamp suc_fec_act) {
		this.suc_fec_act = suc_fec_act;
	}
	/**
	 * @return Returns the suc_fec_crea.
	 */
	public Timestamp getSuc_fec_crea() {
		return suc_fec_crea;
	}
	/**
	 * @param suc_fec_crea The suc_fec_crea to set.
	 */
	public void setSuc_fec_crea(Timestamp suc_fec_crea) {
		this.suc_fec_crea = suc_fec_crea;
	}
	/**
	 * @return Returns the suc_fec_login.
	 */
	public Timestamp getSuc_fec_login() {
		return suc_fec_login;
	}
	/**
	 * @param suc_fec_login The suc_fec_login to set.
	 */
	public void setSuc_fec_login(Timestamp suc_fec_login) {
		this.suc_fec_login = suc_fec_login;
	}
	/**
	 * @return Returns the suc_fec_nac.
	 */
	public Date getSuc_fec_nac() {
		return suc_fec_nac;
	}
	/**
	 * @param suc_fec_nac The suc_fec_nac to set.
	 */
	public void setSuc_fec_nac(Date suc_fec_nac) {
		this.suc_fec_nac = suc_fec_nac;
	}
	/**
	 * @return Returns the suc_fono_cod1.
	 */
	public String getSuc_fono_cod1() {
		return suc_fono_cod1;
	}
	/**
	 * @param suc_fono_cod1 The suc_fono_cod1 to set.
	 */
	public void setSuc_fono_cod1(String suc_fono_cod1) {
		this.suc_fono_cod1 = suc_fono_cod1;
	}
	/**
	 * @return Returns the suc_fono_cod2.
	 */
	public String getSuc_fono_cod2() {
		return suc_fono_cod2;
	}
	/**
	 * @param suc_fono_cod2 The suc_fono_cod2 to set.
	 */
	public void setSuc_fono_cod2(String suc_fono_cod2) {
		this.suc_fono_cod2 = suc_fono_cod2;
	}
	/**
	 * @return Returns the suc_fono_num1.
	 */
	public String getSuc_fono_num1() {
		return suc_fono_num1;
	}
	/**
	 * @param suc_fono_num1 The suc_fono_num1 to set.
	 */
	public void setSuc_fono_num1(String suc_fono_num1) {
		this.suc_fono_num1 = suc_fono_num1;
	}
	/**
	 * @return Returns the suc_fono_num2.
	 */
	public String getSuc_fono_num2() {
		return suc_fono_num2;
	}
	/**
	 * @param suc_fono_num2 The suc_fono_num2 to set.
	 */
	public void setSuc_fono_num2(String suc_fono_num2) {
		this.suc_fono_num2 = suc_fono_num2;
	}
	/**
	 * @return Returns the suc_genero.
	 */
	public String getSuc_genero() {
		return suc_genero;
	}
	/**
	 * @param suc_genero The suc_genero to set.
	 */
	public void setSuc_genero(String suc_genero) {
		this.suc_genero = suc_genero;
	}
	/**
	 * @return Returns the suc_id.
	 */
	public Long getSuc_id() {
		return suc_id;
	}
	/**
	 * @param suc_id The suc_id to set.
	 */
	public void setSuc_id(Long suc_id) {
		this.suc_id = suc_id;
	}
	/**
	 * @return Returns the suc_intentos.
	 */
	public Integer getSuc_intentos() {
		return suc_intentos;
	}
	/**
	 * @param suc_intentos The suc_intentos to set.
	 */
	public void setSuc_intentos(Integer suc_intentos) {
		this.suc_intentos = suc_intentos;
	}
	/**
	 * @return Returns the suc_mod_dato.
	 */
	public String getSuc_mod_dato() {
		return suc_mod_dato;
	}
	/**
	 * @param suc_mod_dato The suc_mod_dato to set.
	 */
	public void setSuc_mod_dato(String suc_mod_dato) {
		this.suc_mod_dato = suc_mod_dato;
	}
	/**
	 * @return Returns the suc_pregunta.
	 */
	public String getSuc_pregunta() {
		return suc_pregunta;
	}
	/**
	 * @param suc_pregunta The suc_pregunta to set.
	 */
	public void setSuc_pregunta(String suc_pregunta) {
		this.suc_pregunta = suc_pregunta;
	}
	/**
	 * @return Returns the suc_razon.
	 */
	public String getSuc_razon() {
		return suc_razon;
	}
	/**
	 * @param suc_razon The suc_razon to set.
	 */
	public void setSuc_razon(String suc_razon) {
		this.suc_razon = suc_razon;
	}
	/**
	 * @return Returns the suc_rec_info.
	 */
	public Integer getSuc_rec_info() {
		return suc_rec_info;
	}
	/**
	 * @param suc_rec_info The suc_rec_info to set.
	 */
	public void setSuc_rec_info(Integer suc_rec_info) {
		this.suc_rec_info = suc_rec_info;
	}
	/**
	 * @return Returns the suc_respuesta.
	 */
	public String getSuc_respuesta() {
		return suc_respuesta;
	}
	/**
	 * @param suc_respuesta The suc_respuesta to set.
	 */
	public void setSuc_respuesta(String suc_respuesta) {
		this.suc_respuesta = suc_respuesta;
	}
	/**
	 * @return Returns the suc_rut.
	 */
	public Long getSuc_rut() {
		return suc_rut;
	}
	/**
	 * @param suc_rut The suc_rut to set.
	 */
	public void setSuc_rut(Long suc_rut) {
		this.suc_rut = suc_rut;
	}
	/**
	 * @return Returns the suc_tipo.
	 */
	public String getSuc_tipo() {
		return suc_tipo;
	}
	/**
	 * @param suc_tipo The suc_tipo to set.
	 */
	public void setSuc_tipo(String suc_tipo) {
		this.suc_tipo = suc_tipo;
	}
	/**
	 * @return Returns the suc_desc_estado.
	 */
	public String getSuc_desc_estado() {
		return suc_desc_estado;
	}
	/**
	 * @param suc_desc_estado The suc_desc_estado to set.
	 */
	public void setSuc_desc_estado(String suc_desc_estado) {
		this.suc_desc_estado = suc_desc_estado;
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

	
}
