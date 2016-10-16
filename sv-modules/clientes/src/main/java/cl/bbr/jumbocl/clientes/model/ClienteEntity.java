package cl.bbr.jumbocl.clientes.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author BBRI
 * 
 */
public class ClienteEntity {

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
	private Character estado;
	private Date fec_nac;
	private Character genero;
	private Timestamp fec_act;
	private String pregunta;
	private String respuesta;
	private String cli_mod_dato;
	private Timestamp fec_login;
	private Long intento;
	private long recibeSms;
    private long recibeEMail;
    private String key_CambioClave;
    private boolean colaborador;

	/**
	 * Constructor
	 */
	public ClienteEntity() {
	}

	public String getApellido_mat() {
		return apellido_mat;
	}

	public void setApellido_mat(String apellido_mat) {
		this.apellido_mat = apellido_mat;
	}

	public String getApellido_pat() {
		return apellido_pat;
	}

	public void setApellido_pat(String apellido_pat) {
		this.apellido_pat = apellido_pat;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Character getDv() {
		return dv;
	}

	public void setDv(Character dv) {
		this.dv = dv;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Character getEstado() {
		return estado;
	}

	public void setEstado(Character estado) {
		this.estado = estado;
	}

	public Timestamp getFec_crea() {
		return fec_crea;
	}

	public void setFec_crea(Timestamp fec_crea) {
		this.fec_crea = fec_crea;
	}

	public Date getFec_nac() {
		return fec_nac;
	}

	public void setFec_nac(Date fec_nac) {
		this.fec_nac = fec_nac;
	}

	public String getFon_cod_1() {
		return fon_cod_1;
	}

	public void setFon_cod_1(String fon_cod_1) {
		this.fon_cod_1 = fon_cod_1;
	}

	public String getFon_cod_2() {
		return fon_cod_2;
	}

	public void setFon_cod_2(String fon_cod_2) {
		this.fon_cod_2 = fon_cod_2;
	}

	public String getFon_cod_3() {
		return fon_cod_3;
	}

	public void setFon_cod_3(String fon_cod_3) {
		this.fon_cod_3 = fon_cod_3;
	}

	public String getFon_num_1() {
		return fon_num_1;
	}

	public void setFon_num_1(String fon_num_1) {
		this.fon_num_1 = fon_num_1;
	}

	public String getFon_num_2() {
		return fon_num_2;
	}

	public void setFon_num_2(String fon_num_2) {
		this.fon_num_2 = fon_num_2;
	}

	public String getFon_num_3() {
		return fon_num_3;
	}

	public void setFon_num_3(String fon_num_3) {
		this.fon_num_3 = fon_num_3;
	}
	
	public Character getGenero() {
		return genero;
	}

	public void setGenero(Character genero) {
		this.genero = genero;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getRec_info() {
		return rec_info;
	}

	public void setRec_info(Long rec_info) {
		this.rec_info = rec_info;
	}

	public Long getRut() {
		return rut;
	}

	public void setRut(Long rut) {
		this.rut = rut;
	}

	public Timestamp getFec_act() {
		return fec_act;
	}

	public void setFec_act(Timestamp fec_act) {
		this.fec_act = fec_act;
	}

	public String toString() {
		
		String res = "";

		res += this.getId() + " | ";
		res += this.getRut() + " | ";
		res += this.getDv() + " | ";		
		res += this.getNombre() + " | ";
		res += this.getApellido_pat() + " | ";
		res += this.getApellido_mat() + " | ";
		res += this.getEmail() + " | ";
		res += this.getEstado() + " | ";
		
		return res;
		
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

    /**
     * @return Devuelve recibeSms.
     */
    public long getRecibeSms() {
        return recibeSms;
    }
    /**
     * @param recibeSms El recibeSms a establecer.
     */
    public void setRecibeSms(long recibeSms) {
        this.recibeSms = recibeSms;
    }
    /**
     * @return Devuelve recibeEMail.
     */
    public long getRecibeEMail() {
        return recibeEMail;
    }
    /**
     * @param recibeEMail El recibeEMail a establecer.
     */
    public void setRecibeEMail(long recibeEMail) {
        this.recibeEMail = recibeEMail;
    }
    /**
     * @return Devuelve key_CambioClave.
     */
    public String getKey_CambioClave() {
        return key_CambioClave;
    }
    /**
     * @param key_CambioClave El key_CambioClave a establecer.
     */
    public void setKey_CambioClave(String key_CambioClave) {
        this.key_CambioClave = key_CambioClave;
    }
    /**
     * @return Returns the colaborador.
     */
    public boolean isColaborador() {
        return colaborador;
    }
    /**
     * @param colaborador The colaborador to set.
     */
    public void setColaborador(boolean colaborador) {
        this.colaborador = colaborador;
    }
}