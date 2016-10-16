package cl.bbr.jumbocl.usuarios.dto;

import java.io.Serializable;
import java.util.List;

public class UserDTO implements Serializable{

	private long	id_usuario;
	private String	login;
	private String	password;
	private String	nombre;
	private String	ape_paterno;
	private String	ape_materno;
	private String	perfil;
	private long	id_perfil;
	private String	local;
	private long	id_local;
	private String  local_tipo_picking; //N: Normal, L: Light
	private String 	email;
	private String 	estado;
	private long	id_pedido;
	private List 	perfiles;
	private List 	locales;
	private long    id_cotizacion;

	/**
	 * Constructor
	 *
	 */
	public UserDTO(){
		
	}
	
	/**
	 * @param id_usuario
	 * @param login
	 * @param password
	 * @param nombre
	 * @param ape_paterno
	 * @param ape_materno
	 * @param email
	 * @param estado
	 * @param perfiles
	 * @param id_pedido
	 */
	public UserDTO(long id_usuario, String login, String password, String nombre, String ape_paterno, String ape_materno, 
			String email, String estado, List perfiles, long id_pedido) {
		super();
		this.id_usuario = id_usuario;
		this.login = login;
		this.password = password;
		this.nombre = nombre;
		this.ape_paterno = ape_paterno;
		this.ape_materno = ape_materno;
		this.email = email;
		this.estado = estado;
		this.perfiles = perfiles;
		this.id_pedido = id_pedido;
	}

	public UserDTO(long id_usuario, String login, String nombre, String ape_paterno, String ape_materno, String estado) {
		super();
		this.id_usuario = id_usuario;
		this.login = login;
		this.nombre = nombre;
		this.ape_paterno = ape_paterno;
		this.ape_materno = ape_materno;
		this.estado = estado;
	}
	
	public UserDTO(long id_usuario, String login, String nombre, String ape_paterno, String ape_materno,
			String password, String email, String estado, long id_local) {
		super();
		this.id_usuario = id_usuario;
		this.login = login;
		this.password = password;
		this.nombre = nombre;
		this.ape_paterno = ape_paterno;
		this.ape_materno = ape_materno;
		this.email = email;
		this.estado = estado;
		this.id_local = id_local;
	}
	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return Returns the id_pedido.
	 */
	public long getId_pedido() {
		return id_pedido;
	}

	/**
	 * @param id_pedido The id_pedido to set.
	 */
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}

	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public long getId_local() {
		return id_local;
	}

	public void setId_local(long id_local) {
		this.id_local = id_local;
	}

	public long getId_perfil() {
		return id_perfil;
	}

	public void setId_perfil(long id_perfil) {
		this.id_perfil = id_perfil;
	}

	public long getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(long id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

    /**
     * @return Devuelve local_tipo_picking.
     */
    public String getLocal_tipo_picking() {
        return local_tipo_picking;
    }
    /**
     * @param local_tipo_picking El local_tipo_picking a establecer.
     */
    
    public void setLocal_tipo_picking(String local_tipo_picking) {
        this.local_tipo_picking = local_tipo_picking;
    }
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
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
	 * @return Returns the perfiles.
	 */
	public List getPerfiles() {
		return perfiles;
	}

	/**
	 * @param perfiles The perfiles to set.
	 */
	public void setPerfiles(List perfiles) {
		this.perfiles = perfiles;
	}

	/**
	 * @return Returns the ape_materno.
	 */
	public String getApe_materno() {
		return ape_materno;
	}

	/**
	 * @param ape_materno The ape_materno to set.
	 */
	public void setApe_materno(String ape_materno) {
		this.ape_materno = ape_materno;
	}

	/**
	 * @return Returns the ape_paterno.
	 */
	public String getApe_paterno() {
		return ape_paterno;
	}

	/**
	 * @param ape_paterno The ape_paterno to set.
	 */
	public void setApe_paterno(String ape_paterno) {
		this.ape_paterno = ape_paterno;
	}

	/**
	 * @return Returns the locales.
	 */
	public List getLocales() {
		return locales;
	}

	/**
	 * @param locales The locales to set.
	 */
	public void setLocales(List locales) {
		this.locales = locales;
	}

	/**
	 * @return Returns the id_cotizacion.
	 */
	public long getId_cotizacion() {
		return id_cotizacion;
	}

	/**
	 * @param id_cotizacion The id_cotizacion to set.
	 */
	public void setId_cotizacion(long id_cotizacion) {
		this.id_cotizacion = id_cotizacion;
	}
		
	
	
	
}
