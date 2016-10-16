package cl.jumbo.ventaondemand.web.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class ClienteDTO implements Serializable {

	private static final long serialVersionUID = -1100740376040212611L;
	
	private Long id;
	private Long rut;
	private Character dv;
	private String nombre;
	private String apellido_pat;
	private String apellido_mat;
	private String clave;
	private String email;
	private String fon_cod_1;
	private String fon_num_1;
	private String fon_cod_2;
	private String fon_num_2;
	private String fon_cod_3;
	private String fon_num_3;
	private Long rec_info;
	private Timestamp fec_crea;
	private Timestamp fec_act;
	private Character estado;
	private Date fec_nac;
	private Character genero;	
	private String pregunta;
	private String respuesta;
	private String bloqueo;
	private String cli_mod_dato;
	private Timestamp fec_login;
	private Long intento;
	private Long emp_id;
	private String tipo;
	private Long envio_email;
	private Date create_at;
	private Date update_at;
	private long envio_sms;
	private long recibe_encuesta;
	private long contador_encuesta;
	private String key_recupera_clave;
	private String id_facebook;	
	private boolean colaborador;
	
	private String status = "NOK";
	private String statusMensaje;
			
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getRut() {
		return rut;
	}
	public void setRut(Long rut) {
		this.rut = rut;
	}
	public Character getDv() {
		return dv;
	}
	public void setDv(Character dv) {
		this.dv = dv;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido_pat() {
		return apellido_pat;
	}
	public void setApellido_pat(String apellido_pat) {
		this.apellido_pat = apellido_pat;
	}
	public String getApellido_mat() {
		return apellido_mat;
	}
	public void setApellido_mat(String apellido_mat) {
		this.apellido_mat = apellido_mat;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFon_cod_1() {
		return fon_cod_1;
	}
	public void setFon_cod_1(String fon_cod_1) {
		this.fon_cod_1 = fon_cod_1;
	}
	public String getFon_num_1() {
		return fon_num_1;
	}
	public void setFon_num_1(String fon_num_1) {
		this.fon_num_1 = fon_num_1;
	}
	public String getFon_cod_2() {
		return fon_cod_2;
	}
	public void setFon_cod_2(String fon_cod_2) {
		this.fon_cod_2 = fon_cod_2;
	}
	public String getFon_num_2() {
		return fon_num_2;
	}
	public void setFon_num_2(String fon_num_2) {
		this.fon_num_2 = fon_num_2;
	}
	public String getFon_cod_3() {
		return fon_cod_3;
	}
	public void setFon_cod_3(String fon_cod_3) {
		this.fon_cod_3 = fon_cod_3;
	}
	public String getFon_num_3() {
		return fon_num_3;
	}
	public void setFon_num_3(String fon_num_3) {
		this.fon_num_3 = fon_num_3;
	}
	public Long getRec_info() {
		return rec_info;
	}
	public void setRec_info(Long rec_info) {
		this.rec_info = rec_info;
	}
	public Timestamp getFec_crea() {
		return fec_crea;
	}
	public void setFec_crea(Timestamp fec_crea) {
		this.fec_crea = fec_crea;
	}
	public Timestamp getFec_act() {
		return fec_act;
	}
	public void setFec_act(Timestamp fec_act) {
		this.fec_act = fec_act;
	}
	public Character getEstado() {
		return estado;
	}
	public void setEstado(Character estado) {
		this.estado = estado;
	}
	public Date getFec_nac() {
		return fec_nac;
	}
	public void setFec_nac(Date fec_nac) {
		this.fec_nac = fec_nac;
	}
	public Character getGenero() {
		return genero;
	}
	public void setGenero(Character genero) {
		this.genero = genero;
	}
	public String getPregunta() {
		return pregunta;
	}
	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	public String getBloqueo() {
		return bloqueo;
	}
	public void setBloqueo(String bloqueo) {
		this.bloqueo = bloqueo;
	}
	public String getCli_mod_dato() {
		return cli_mod_dato;
	}
	public void setCli_mod_dato(String cli_mod_dato) {
		this.cli_mod_dato = cli_mod_dato;
	}
	public Timestamp getFec_login() {
		return fec_login;
	}
	public void setFec_login(Timestamp fec_login) {
		this.fec_login = fec_login;
	}
	public Long getIntento() {
		return intento;
	}
	public void setIntento(Long intento) {
		this.intento = intento;
	}
	public Long getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(Long emp_id) {
		this.emp_id = emp_id;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Long getEnvio_email() {
		return envio_email;
	}
	public void setEnvio_email(Long envio_email) {
		this.envio_email = envio_email;
	}
	public Date getCreate_at() {
		return create_at;
	}
	public void setCreate_at(Date create_at) {
		this.create_at = create_at;
	}
	public Date getUpdate_at() {
		return update_at;
	}
	public void setUpdate_at(Date update_at) {
		this.update_at = update_at;
	}
	public long getEnvio_sms() {
		return envio_sms;
	}
	public void setEnvio_sms(long envio_sms) {
		this.envio_sms = envio_sms;
	}
	public long getRecibe_encuesta() {
		return recibe_encuesta;
	}
	public void setRecibe_encuesta(long recibe_encuesta) {
		this.recibe_encuesta = recibe_encuesta;
	}
	public long getContador_encuesta() {
		return contador_encuesta;
	}
	public void setContador_encuesta(long contador_encuesta) {
		this.contador_encuesta = contador_encuesta;
	}
	public String getKey_recupera_clave() {
		return key_recupera_clave;
	}
	public void setKey_recupera_clave(String key_recupera_clave) {
		this.key_recupera_clave = key_recupera_clave;
	}
	public String getId_facebook() {
		return id_facebook;
	}
	public void setId_facebook(String id_facebook) {
		this.id_facebook = id_facebook;
	}
	public boolean isColaborador() {
		return colaborador;
	}
	public void setColaborador(boolean colaborador) {
		this.colaborador = colaborador;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusMensaje() {
		return statusMensaje;
	}
	public void setStatusMensaje(String statusMensaje) {
		this.statusMensaje = statusMensaje;
	}
}