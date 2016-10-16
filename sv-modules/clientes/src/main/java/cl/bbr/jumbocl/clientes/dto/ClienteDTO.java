package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;

/**
 * DTO para datos de los clientes. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class ClienteDTO implements Serializable {

	private static final long serialVersionUID = -1100740376040212611L;
	
	private long id;
	private long rut;
	private String dv;
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
	private long rec_info;
	private long fec_crea;
	private String estado;
	private long fec_nac;
	private String genero;
	private long fec_act;
	private String pregunta;
	private String respuesta;
	private String cli_mod_dato;
	private long fec_login;
	private long intento;
	private long recibeSms;
    private long recibeEMail; //CLI_ENVIO_MAIL
    private String key_CambioClave;
    
    private boolean colaborador;

	private String id_facebook=null;

	public ClienteDTO() {
	}
	
		public ClienteDTO(long id, long rut, String dv, String nombre,
			String apellido_pat, String apellido_mat, String clave,
			String email, String fon_cod_1, String fon_num_1, String fon_cod_2,
			String fon_num_2, String fon_cod_3, String fon_num_3, long rec_info,
			long fec_crea, String estado,long fec_nac, String genero,
			String pregunta, String respuesta ) {
		this.id = id;
		this.rut = rut;
		this.dv = dv;
		this.nombre = nombre;
		this.apellido_pat = apellido_pat;
		this.apellido_mat = apellido_mat;
		this.clave = clave;
		this.email = email;
		this.fon_cod_1 = fon_cod_1;
		this.fon_num_1 = fon_num_1;
		this.fon_cod_2 = fon_cod_2;
		this.fon_num_2 = fon_num_2;
		this.fon_cod_3 = fon_cod_3;
		this.fon_num_3 = fon_num_3;
		this.rec_info = rec_info;
		this.fec_crea = fec_crea;
		this.estado = estado;
		this.fec_nac = fec_nac;
		this.genero = genero;
		this.pregunta = pregunta;
		this.respuesta = respuesta;
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

	public String getDv() {
		return dv;
	}

	public void setDv(String dv) {
		this.dv = dv;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public long getFec_crea() {
		return fec_crea;
	}

	public void setFec_crea(long fec_crea) {
		this.fec_crea = fec_crea;
	}

	public long getFec_nac() {
		return fec_nac;
	}

	public void setFec_nac(long fec_nac) {
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
	
	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public long getRec_info() {
		return rec_info;
	}

	public void setRec_info(long rec_info) {
		this.rec_info = rec_info;
	}

	public long getRut() {
		return rut;
	}

	public void setRut(long rut) {
		this.rut = rut;
	}

	public long getFec_act() {
		return fec_act;
	}

	public void setFec_act(long fec_act) {
		this.fec_act = fec_act;
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
	
	public String getCli_mod_dato() {
		return cli_mod_dato;
	}
	public void setCli_mod_dato(String cli_mod_dato) {
		this.cli_mod_dato = cli_mod_dato;
	}
	public long getFec_login() {
		return fec_login;
	}
	public void setFec_login(long fec_login) {
		this.fec_login = fec_login;
	}
	public long getIntento() {
		return intento;
	}
	public void setIntento(long intento) {
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

	public String getId_facecebook() {
		return id_facebook;
	}

	public void setId_facebook(String id_facebook) {
		this.id_facebook = id_facebook;
	}
}