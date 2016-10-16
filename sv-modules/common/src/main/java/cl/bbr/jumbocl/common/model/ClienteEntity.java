package cl.bbr.jumbocl.common.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Clase que captura desde la Base de Datos los datos de un cliente
 * @author BBRI
 * 
 */
public class ClienteEntity {

	private Long id;

	private Long rut;

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

	private Integer rec_info;

	private Timestamp fec_crea;
	
	private Timestamp fec_act;
	
	private String estado;

	private Date fec_nac;

	private String genero;
	
	private String bloqueo;
	
	private String mod_dato;
	
	private Long 	id_empresa;
	
	private String rzs_empresa;
	
	private boolean colaborador;
	
	private int cli_envio_mail;

	/**
	 * Constructor
	 */
	public ClienteEntity() {
	}

	public Long getId_empresa() {
		return id_empresa;
	}

	public void setId_empresa(Long id_empresa) {
		this.id_empresa = id_empresa;
	}

	/**
	 * @return apellido_mat
	 */
	public String getApellido_mat() {
		return apellido_mat;
	}

	/**
	 * @param apellido_mat
	 */
	public void setApellido_mat(String apellido_mat) {
		this.apellido_mat = apellido_mat;
	}

	/**
	 * @return apellido_pat
	 */
	public String getApellido_pat() {
		return apellido_pat;
	}

	/**
	 * @param apellido_pat
	 */
	public void setApellido_pat(String apellido_pat) {
		this.apellido_pat = apellido_pat;
	}

	/**
	 * @return clave
	 */
	public String getClave() {
		return clave;
	}

	/**
	 * @param clave
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}

	/**
	 * @return dv
	 */
	public String getDv() {
		return dv;
	}

	/**
	 * @param dv
	 */
	public void setDv(String dv) {
		this.dv = dv;
	}

	/**
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return fec_crea
	 */
	public Timestamp getFec_crea() {
		return fec_crea;
	}

	/**
	 * @param fec_crea
	 */
	public void setFec_crea(Timestamp fec_crea) {
		this.fec_crea = fec_crea;
	}

	/**
	 * @return fec_act
	 */
	public Timestamp getFec_act() {
		return fec_act;
	}

	/**
	 * @param fec_act
	 */
	public void setFec_act(Timestamp fec_act) {
		this.fec_act = fec_act;
	}

	/**
	 * @return fec_nac
	 */
	public Date getFec_nac() {
		return fec_nac;
	}

	/**
	 * @param fec_nac
	 */
	public void setFec_nac(Date fec_nac) {
		this.fec_nac = fec_nac;
	}

	/**
	 * @return fon_cod_1
	 */
	public String getFon_cod_1() {
		return fon_cod_1;
	}

	/**
	 * @param fon_cod_1
	 */
	public void setFon_cod_1(String fon_cod_1) {
		this.fon_cod_1 = fon_cod_1;
	}

	/**
	 * @return fon_cod_2
	 */
	public String getFon_cod_2() {
		return fon_cod_2;
	}

	/**
	 * @param fon_cod_2
	 */
	public void setFon_cod_2(String fon_cod_2) {
		this.fon_cod_2 = fon_cod_2;
	}

	/**
	 * @return fon_num_1
	 */
	public String getFon_num_1() {
		return fon_num_1;
	}

	/**
	 * @param fon_num_1
	 */
	public void setFon_num_1(String fon_num_1) {
		this.fon_num_1 = fon_num_1;
	}

	/**
	 * @return fon_num_2
	 */
	public String getFon_num_2() {
		return fon_num_2;
	}

	/**
	 * @param fon_num_2
	 */
	public void setFon_num_2(String fon_num_2) {
		this.fon_num_2 = fon_num_2;
	}

	/**
	 * @return genero
	 */
	public String getGenero() {
		return genero;
	}

	/**
	 * @param genero
	 */
	public void setGenero(String genero) {
		this.genero = genero;
	}

	/**
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return nombre
	 */ 
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return rec_info
	 */
	public Integer getRec_info() {
		return rec_info;
	}

	/**
	 * @param rec_info
	 */
	public void setRec_info(Integer rec_info) {
		this.rec_info = rec_info;
	}

	/**
	 * @return rut
	 */
	public Long getRut() {
		return rut;
	}

	/**
	 * @param rut
	 */
	public void setRut(Long rut) {
		this.rut = rut;
	}

	/**
	 * @return bloqueo
	 */
	public String getBloqueo() {
		return bloqueo;
	}

	/**
	 * @return mod_dato
	 */
	public String getMod_dato() {
		return mod_dato;
	}

	/**
	 * @param bloqueo
	 */
	public void setBloqueo(String bloqueo) {
		this.bloqueo = bloqueo;
	}

	/**
	 * @param mod_dato
	 */
	public void setMod_dato(String mod_dato) {
		this.mod_dato = mod_dato;
	}

	/**
	 * @return Returns the rzs_empresa.
	 */
	public String getRzs_empresa() {
		return rzs_empresa;
	}

	/**
	 * @param rzs_empresa The rzs_empresa to set.
	 */
	public void setRzs_empresa(String rzs_empresa) {
		this.rzs_empresa = rzs_empresa;
	}
	
	
    /**
     * @return Devuelve fon_cod_3.
     */
    public String getFon_cod_3() {
        return fon_cod_3;
    }
    /**
     * @return Devuelve fon_num_3.
     */
    public String getFon_num_3() {
        return fon_num_3;
    }
    /**
     * @param fon_cod_3 El fon_cod_3 a establecer.
     */
    public void setFon_cod_3(String fon_cod_3) {
        this.fon_cod_3 = fon_cod_3;
    }
    /**
     * @param fon_num_3 El fon_num_3 a establecer.
     */
    public void setFon_num_3(String fon_num_3) {
        this.fon_num_3 = fon_num_3;
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

	public int getCli_envio_mail() {
		return cli_envio_mail;
	}

	public void setCli_envio_mail(int cli_envio_mail) {
		this.cli_envio_mail = cli_envio_mail;
	}
    
    
}
