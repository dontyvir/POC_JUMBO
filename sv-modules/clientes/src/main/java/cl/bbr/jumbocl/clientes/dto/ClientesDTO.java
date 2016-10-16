package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;
import java.util.List;

/**ClientesDTO
 * @author GME
 * @param private int id_cliente  Id del cliente
 * @param private String rut
 * @param private String nombre
 *	@param private String paterno
 *	@param private String materno
 *	@param private String fnac
 *	@param private String estado
 */
public class ClientesDTO implements Serializable{
	private long id_cliente;
	private long rut;
	private String dv;
	private String nombre;
	private String paterno;
	private String materno;
	private String email;
	private String codfono1;
	private String fono1;
	private String codfono2;
	private String fono2;
	private String codfono3;
	private String fono3;
	private int rec_info;
	private String fecCrea;
	private String fecAct;
	private String estado;	
	private String fnac;
	private String genero="";
	private String clave;
	private String est_bloqueo;
	private String rzs_empresa;
	private long id_empresa;
	private String pregunta;
	private String respuesta;
	private long recibeSms;
	private String cli_mod_dato;	
	private List refDirecciones[];
	private List refPedidos[];
	
	private boolean colaborador;
	private int cli_envio_mail;
	
	public ClientesDTO() {
	}

	public ClientesDTO(long id, long rut, String dv, String nombre, String apellido_pat, String apellido_mat, 
			String clave, String email, String fon_cod_1, String fon_num_1, String fon_cod_2, String fon_num_2, 
			String fon_cod_3, String fon_num_3, int rec_info, String fec_crea, String fec_act, String estado, 
			String fec_nac, String genero) {
		this.id_cliente = id;
		this.rut = rut;
		this.dv = dv;
		this.nombre = nombre;
		this.paterno = apellido_pat;
		this.materno = apellido_mat;
		this.clave = clave;
		this.email = email;
		this.codfono1 = fon_cod_1;
		this.fono1 = fon_num_1;
		this.codfono2 = fon_cod_2;
		this.fono2 = fon_num_2;
		this.codfono3 = fon_cod_3;
		this.fono3 = fon_num_3;
		this.rec_info = rec_info;
		this.fecCrea = fec_crea;
		this.fecAct = fec_act;
		this.estado = estado;
		this.fnac = fec_nac;	//.toString();
		this.genero = genero;
	}

	public ClientesDTO(long id, long rut, String dv, String nombre, 
			String apellido_pat, String apellido_mat, String fec_nac, 
			String estado, String est_bloqueo) {
		this.id_cliente = id;
		this.rut = rut;
		this.dv = dv;
		this.nombre = nombre;
		this.paterno = apellido_pat;
		this.materno = apellido_mat;
		this.estado = estado;
		this.fnac = fec_nac;
		this.est_bloqueo = est_bloqueo;
	}
	
	/**
	 * @return Returns the id_empresa.
	 */
	public long getId_empresa() {
		return id_empresa;
	}

	/**
	 * @param id_empresa The id_empresa to set.
	 */
	public void setId_empresa(long id_empresa) {
		this.id_empresa = id_empresa;
	}

	/**
	 * @param id_cliente The id_cliente to set.
	 */
	public void setId_cliente(long id_cliente) {
		this.id_cliente = id_cliente;
	}

	public List[] getRefDirecciones() {
		return refDirecciones;
	}
	public List[] getRefPedidos() {
		return refPedidos;
	}
	public void setRefDirecciones(List[] refDirecciones) {
		this.refDirecciones = refDirecciones;
	}
	public void setRefPedidos(List[] refPedidos) {
		this.refPedidos = refPedidos;
	}
	public String getCodfono1() {
		return codfono1;
	}
	public String getCodfono2() {
		return codfono2;
	}
    /**
     * @return Devuelve fono3.
     */
    public String getFono3() {
        return fono3;
    }
    /**
     * @return Devuelve codfono3.
     */
    public String getCodfono3() {
        return codfono3;
    }
    /**
     * @param codfono3 El codfono3 a establecer.
     */
    public void setCodfono3(String codfono3) {
        this.codfono3 = codfono3;
    }
    /**
     * @param fono3 El fono3 a establecer.
     */
    public void setFono3(String fono3) {
        this.fono3 = fono3;
    }
	public String getDv() {
		return dv;
	}
	public String getEmail() {
		return email;
	}
	public String getEstado() {
		return estado;
	}
	public String getFecCrea() {
		return fecCrea;
	}
	public String getFecAct() {
		return fecAct;
	}
	public String getFnac() {
		return fnac;
	}
	public String getFono1() {
		return fono1;
	}
	public String getFono2() {
		return fono2;
	}
	public String getGenero() {
		return genero;
	}
	public long getId_cliente() {
		return id_cliente;
	}
	public String getMaterno() {
		return materno;
	}
	public String getNombre() {
		return nombre;
	}
	public String getPaterno() {
		return paterno;
	}
	public int getRec_info() {
		return rec_info;
	}
	public long getRut() {
		return rut;
	}
	public String getClave() {
		return clave;
	}
	public void setCodfono1(String codfono1) {
		this.codfono1 = codfono1;
	}
	public void setCodfono2(String codfono2) {
		this.codfono2 = codfono2;
	}
	public void setDv(String dv) {
		this.dv = dv;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public void setFecCrea(String fecCrea) {
		this.fecCrea = fecCrea;
	}
	public void setFecAct(String fecAct) {
		this.fecAct = fecAct;
	}
	public void setFnac(String fnac) {
		this.fnac = fnac;
	}
	public void setFono1(String fono1) {
		this.fono1 = fono1;
	}
	public void setFono2(String fono2) {
		this.fono2 = fono2;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public void setId_cliente(int id_cliente) {
		this.id_cliente = id_cliente;
	}
	public void setMaterno(String materno) {
		this.materno = materno;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setPaterno(String paterno) {
		this.paterno = paterno;
	}
	public void setRec_info(int rec_info) {
		this.rec_info = rec_info;
	}
	public void setRut(long rut) {
		this.rut = rut;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getEst_bloqueo() {
		return est_bloqueo;
	}

	public void setEst_bloqueo(String est_bloqueo) {
		this.est_bloqueo = est_bloqueo;
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

	public long getRecibeSms() {
		return recibeSms;
	}

	public void setRecibeSms(long recibeSms) {
		this.recibeSms = recibeSms;
	}
    
    
}
