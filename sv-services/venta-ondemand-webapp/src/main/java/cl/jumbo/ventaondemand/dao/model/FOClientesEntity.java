package cl.jumbo.ventaondemand.dao.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class FOClientesEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long cli_id;
	private Long cli_rut;
	private Character cli_dv;
	private String cli_nombre;
	private String cli_apellido_pat;
	private String cli_apellido_mat;
	private String cli_clave;
	private String cli_email;
	private String cli_fon_cod_1;
	private String cli_fon_num_1;
	private String cli_fon_cod_2;
	private String cli_fon_num_2;
	private String cli_fon_cod_3;
	private String cli_fon_num_3;
	private Long cli_rec_info;
	private Timestamp cli_fec_crea;
	private Timestamp cli_fec_act;
	private Character cli_estado;
	private Date cli_fec_nac;
	private Character cli_genero;	
	private String cli_pregunta;
	private String cli_respuesta;
	private String cli_bloqueo;//B:bloqueado D:Desactivado
	private String cli_mod_dato;//00 : nada 01:Apellidos
	private Timestamp cli_fec_login;//fecha de login
	private Long cli_intento;//nro de intentos de login
	private Long cli_emp_id;//identificador de empresa
	private String cli_tipo;//E:Empresa o P:Persona
	private Long cli_envio_email;//campo que indica si el cliente tiene habilitado el envio de mail a su casilla
	private Date cli_create_at;
	private Date cli_update_at;
	private long cli_envio_sms;//Indica si el cliente tiene habilitado el envio de Servicio de mensajeria corta
	private long cli_recibe_encuesta;//1: si cliente recibe encuesta satisfaccion y 0: No
	private long cli_contador_encuesta;//Contador que registra las compras desde el envio de la encuesta de satisfaccion
	private String cli_key_recupera_clave;
	private String cli_id_facebook;//Identificador de la cuenta de FACEBOOK
	
	private boolean colaborador;

	public Long getCli_id() {
		return cli_id;
	}

	public void setCli_id(Long cli_id) {
		this.cli_id = cli_id;
	}

	public Long getCli_rut() {
		return cli_rut;
	}

	public void setCli_rut(Long cli_rut) {
		this.cli_rut = cli_rut;
	}

	public Character getCli_dv() {
		return cli_dv;
	}

	public void setCli_dv(Character cli_dv) {
		this.cli_dv = cli_dv;
	}

	public String getCli_nombre() {
		return cli_nombre;
	}

	public void setCli_nombre(String cli_nombre) {
		this.cli_nombre = cli_nombre;
	}

	public String getCli_apellido_pat() {
		return cli_apellido_pat;
	}

	public void setCli_apellido_pat(String cli_apellido_pat) {
		this.cli_apellido_pat = cli_apellido_pat;
	}

	public String getCli_apellido_mat() {
		return cli_apellido_mat;
	}

	public void setCli_apellido_mat(String cli_apellido_mat) {
		this.cli_apellido_mat = cli_apellido_mat;
	}

	public String getCli_clave() {
		return cli_clave;
	}

	public void setCli_clave(String cli_clave) {
		this.cli_clave = cli_clave;
	}

	public String getCli_email() {
		return cli_email;
	}

	public void setCli_email(String cli_email) {
		this.cli_email = cli_email;
	}

	public String getCli_fon_cod_1() {
		return cli_fon_cod_1;
	}

	public void setCli_fon_cod_1(String cli_fon_cod_1) {
		this.cli_fon_cod_1 = cli_fon_cod_1;
	}

	public String getCli_fon_num_1() {
		return cli_fon_num_1;
	}

	public void setCli_fon_num_1(String cli_fon_num_1) {
		this.cli_fon_num_1 = cli_fon_num_1;
	}

	public String getCli_fon_cod_2() {
		return cli_fon_cod_2;
	}

	public void setCli_fon_cod_2(String cli_fon_cod_2) {
		this.cli_fon_cod_2 = cli_fon_cod_2;
	}

	public String getCli_fon_num_2() {
		return cli_fon_num_2;
	}

	public void setCli_fon_num_2(String cli_fon_num_2) {
		this.cli_fon_num_2 = cli_fon_num_2;
	}

	public String getCli_fon_cod_3() {
		return cli_fon_cod_3;
	}

	public void setCli_fon_cod_3(String cli_fon_cod_3) {
		this.cli_fon_cod_3 = cli_fon_cod_3;
	}

	public String getCli_fon_num_3() {
		return cli_fon_num_3;
	}

	public void setCli_fon_num_3(String cli_fon_num_3) {
		this.cli_fon_num_3 = cli_fon_num_3;
	}

	public Long getCli_rec_info() {
		return cli_rec_info;
	}

	public void setCli_rec_info(Long cli_rec_info) {
		this.cli_rec_info = cli_rec_info;
	}

	public Timestamp getCli_fec_crea() {
		return cli_fec_crea;
	}

	public void setCli_fec_crea(Timestamp cli_fec_crea) {
		this.cli_fec_crea = cli_fec_crea;
	}

	public Timestamp getCli_fec_act() {
		return cli_fec_act;
	}

	public void setCli_fec_act(Timestamp cli_fec_act) {
		this.cli_fec_act = cli_fec_act;
	}

	public Character getCli_estado() {
		return cli_estado;
	}

	public void setCli_estado(Character cli_estado) {
		this.cli_estado = cli_estado;
	}

	public Date getCli_fec_nac() {
		return cli_fec_nac;
	}

	public void setCli_fec_nac(Date cli_fec_nac) {
		this.cli_fec_nac = cli_fec_nac;
	}

	public Character getCli_genero() {
		return cli_genero;
	}

	public void setCli_genero(Character cli_genero) {
		this.cli_genero = cli_genero;
	}

	public String getCli_pregunta() {
		return cli_pregunta;
	}

	public void setCli_pregunta(String cli_pregunta) {
		this.cli_pregunta = cli_pregunta;
	}

	public String getCli_respuesta() {
		return cli_respuesta;
	}

	public void setCli_respuesta(String cli_respuesta) {
		this.cli_respuesta = cli_respuesta;
	}

	public String getCli_bloqueo() {
		return cli_bloqueo;
	}

	public void setCli_bloqueo(String cli_bloqueo) {
		this.cli_bloqueo = cli_bloqueo;
	}

	public String getCli_mod_dato() {
		return cli_mod_dato;
	}

	public void setCli_mod_dato(String cli_cli_mod_dato) {
		this.cli_mod_dato = cli_cli_mod_dato;
	}

	public Timestamp getCli_fec_login() {
		return cli_fec_login;
	}

	public void setCli_fec_login(Timestamp cli_fec_login) {
		this.cli_fec_login = cli_fec_login;
	}

	public Long getCli_intento() {
		return cli_intento;
	}

	public void setCli_intento(Long cli_intento) {
		this.cli_intento = cli_intento;
	}

	public Long getCli_emp_id() {
		return cli_emp_id;
	}

	public void setCli_emp_id(Long cli_emp_id) {
		this.cli_emp_id = cli_emp_id;
	}

	public String getCli_tipo() {
		return cli_tipo;
	}

	public void setCli_tipo(String cli_tipo) {
		this.cli_tipo = cli_tipo;
	}

	public Long getCli_envio_email() {
		return cli_envio_email;
	}

	public void setCli_envio_email(Long cli_envio_email) {
		this.cli_envio_email = cli_envio_email;
	}

	public Date getCli_create_at() {
		return cli_create_at;
	}

	public void setCli_create_at(Date cli_create_at) {
		this.cli_create_at = cli_create_at;
	}

	public Date getCli_update_at() {
		return cli_update_at;
	}

	public void setCli_update_at(Date cli_update_at) {
		this.cli_update_at = cli_update_at;
	}

	public long getCli_envio_sms() {
		return cli_envio_sms;
	}

	public void setCli_envio_sms(long cli_envio_sms) {
		this.cli_envio_sms = cli_envio_sms;
	}

	public long getCli_recibe_encuesta() {
		return cli_recibe_encuesta;
	}

	public void setCli_recibe_encuesta(long cli_recibe_encuesta) {
		this.cli_recibe_encuesta = cli_recibe_encuesta;
	}

	public long getCli_contador_encuesta() {
		return cli_contador_encuesta;
	}

	public void setCli_contador_encuesta(long cli_contador_encuesta) {
		this.cli_contador_encuesta = cli_contador_encuesta;
	}

	public String getCli_key_recupera_clave() {
		return cli_key_recupera_clave;
	}

	public void setCli_key_recupera_clave(String cli_key_recupera_clave) {
		this.cli_key_recupera_clave = cli_key_recupera_clave;
	}

	public String getCli_id_facebook() {
		return cli_id_facebook;
	}

	public void setCli_id_facebook(String cli_id_facebook) {
		this.cli_id_facebook = cli_id_facebook;
	}

	public boolean isColaborador() {
		return colaborador;
	}

	public void setColaborador(boolean colaborador) {
		this.colaborador = colaborador;
	}
}