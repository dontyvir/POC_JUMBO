package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;

public class DireccionesDTO implements Serializable{	

	private long id;
	private long loc_cod;
	private long com_id;
	private long tipo_calle;
	private long zona_id;
	private long reg_id;
	private String reg_nombre;
	private String com_nombre;
	private String nueva;
	private double latitud;
	private double longitud;
	private boolean confirmada;
	private long fec_crea_long;
	private int id_dir;
	private String alias;
	private String calle;
	private String numero;
	private String depto;
	private String comentarios;
	private String estado;
	private String fec_crea;
	private String fnueva;
	private String cuadrante;
	private String comuna;
	private long id_cliente;
	private String nom_local;
	private String nom_zona;
	private String nom_tipocalle;
	private String nom_region;
	private String dir_conflictiva;
	private String dir_conflictiva_comentario;
	private String tipoPicking;
	private int    id_poligono;

	private long id_loc;
	private long id_com;
	private long id_zon;
	private TipoCalleDTO tipoCalle;
	
	private boolean direDummy;
	private boolean direUltimoPedido;

	public DireccionesDTO() {}


	/*
	 * Constructor
	 */
	public DireccionesDTO(long id, long loc_cod, long com_id,
			long tipo_calle, long cli_id, String alias, String calle,
			String numero, String depto, String comentarios,
			long fec_crea, String estado) {
		this.id = id;
		this.loc_cod = loc_cod;
		this.com_id = com_id;
		this.tipo_calle = tipo_calle;
		this.id_cliente = cli_id;
		this.alias = alias;
		this.calle = calle;
		this.numero = numero;
		this.depto = depto;
		this.comentarios = comentarios;
		this.fec_crea_long = fec_crea;
		this.estado = estado;
	}

	/*
	 * Constructor
	 */
	public DireccionesDTO(long id, long com_id,  long tipo_calle, long cli_id, String alias, String calle,
			              String numero, String depto, String comentarios, long fec_crea, String estado) {
		this.id = id;
		this.com_id = com_id;
		this.tipo_calle = tipo_calle;
		this.id_cliente = cli_id;
		this.alias = alias;
		this.calle = calle;
		this.numero = numero;
		this.depto = depto;
		this.comentarios = comentarios;
		this.fec_crea_long = fec_crea;
		this.estado = estado;
	}

	public DireccionesDTO(int id, long id_cliente, String alias, String calle, String comuna, String depto, 
			String comentarios, String est, String fec_crea, String numero, String nom_local, String nom_tipocalle,
			String fnueva, String cuadrante, long id_loc, long id_com, long id_zon, TipoCalleDTO tipoCalleDto, 
			String nom_region) {
		this.id_dir = id;
		this.id_cliente = id_cliente;
		this.alias = alias;
		this.calle = calle;
		this.comuna = comuna;
		this.depto = depto;
		this.estado = est;
		this.comentarios = comentarios;
		this.fec_crea = fec_crea;
		this.numero = numero;
		this.nom_local = nom_local;
		this.nom_tipocalle = nom_tipocalle;
		this.fnueva = fnueva;
		this.cuadrante = cuadrante;
		this.id_loc = id_loc;
		this.id_com = id_com;
		this.id_zon = id_zon;
		this.tipoCalle = tipoCalleDto;
		this.nom_region = nom_region;
	}


	public DireccionesDTO(int id, long id_cliente, String alias, String calle, String comuna, String depto, 
			String comentarios, String est, String fec_crea, String numero, String nom_local, String nom_tipocalle,
			String fnueva, String cuadrante, long id_loc, long id_com, TipoCalleDTO tipoCalleDto, 
			String nom_region) {
		this.id_dir = id;
		this.id_cliente = id_cliente;
		this.alias = alias;
		this.calle = calle;
		this.comuna = comuna;
		this.depto = depto;
		this.estado = est;
		this.comentarios = comentarios;
		this.fec_crea = fec_crea;
		this.numero = numero;
		this.nom_local = nom_local;
		this.nom_tipocalle = nom_tipocalle;
		this.fnueva = fnueva;
		this.cuadrante = cuadrante;
		this.id_loc = id_loc;
		this.id_com = id_com;
		this.tipoCalle = tipoCalleDto;
		this.nom_region = nom_region;
	}


	public DireccionesDTO(int id, long id_cliente, String alias, String calle, String comuna, String depto, 
			String comentarios, String est, String fec_crea, String numero, String nom_local, String nom_zona, String nom_tipocalle,
			String fnueva, String cuadrante, long id_loc, long id_com, long id_zon, TipoCalleDTO tipoCalleDto, 
			String nom_region, String dir_conflictiva, String dir_conflictiva_comentario, String tipoPicking,
			int id_poligono) {
		this.id_dir = id;
		this.id_cliente = id_cliente;
		this.alias = alias;
		this.calle = calle;
		this.comuna = comuna;
		this.depto = depto;
		this.estado = est;
		this.comentarios = comentarios;
		this.fec_crea = fec_crea;
		this.numero = numero;
		this.nom_local = nom_local;
		this.nom_zona = nom_zona;
		this.nom_tipocalle = nom_tipocalle;
		this.fnueva = fnueva;
		this.cuadrante = cuadrante;
		this.id_loc = id_loc;
		this.id_com = id_com;
		this.id_zon = id_zon;
		this.tipoCalle = tipoCalleDto;
		this.nom_region = nom_region;
		this.dir_conflictiva = dir_conflictiva;
		this.dir_conflictiva_comentario = dir_conflictiva_comentario;
		this.tipoPicking = tipoPicking;
		this.id_poligono = id_poligono;
	}

	public String getAlias() {
		return alias;
	}
	public String getCalle() {
		return calle;
	}
	public String getComentarios() {
		return comentarios;
	}
	public String getComuna() {
		return comuna;
	}
	public String getDepto() {
		return depto;
	}
	public String getEstado() {
		return estado;
	}
	public String getFec_crea() {
		return fec_crea;
	}
	public long getId_cliente() {
		return id_cliente;
	}
	public int getId_dir() {
		return id_dir;
	}
	public String getNom_local() {
		return nom_local;
	}
	public String getNom_tipocalle() {
		return nom_tipocalle;
	}
	public String getNumero() {
		return numero;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	public void setComuna(String comuna) {
		this.comuna = comuna;
	}
	public void setDepto(String depto) {
		this.depto = depto;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public void setFec_crea(String fec_crea) {
		this.fec_crea = fec_crea;
	}
	public void setId_cliente(long id_cliente) {
		this.id_cliente = id_cliente;
	}
	public void setId_dir(int id_dir) {
		this.id_dir = id_dir;
	}
	public void setNom_local(String nom_local) {
		this.nom_local = nom_local;
	}
	public void setNom_tipocalle(String nom_tipocalle) {
		this.nom_tipocalle = nom_tipocalle;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * @return Returns the id_com.
	 */
	public long getId_com() {
		return id_com;
	}

	/**
	 * @param id_com The id_com to set.
	 */
	public void setId_com(long id_com) {
		this.id_com = id_com;
	}

	/**
	 * @return Returns the id_loc.
	 */
	public long getId_loc() {
		return id_loc;
	}

	/**
	 * @param id_loc The id_loc to set.
	 */
	public void setId_loc(long id_loc) {
		this.id_loc = id_loc;
	}

	/**
	 * @return Returns the tipoCalle.
	 */
	public TipoCalleDTO getTipoCalle() {
		return tipoCalle;
	}

	/**
	 * @param tipoCalle The tipoCalle to set.
	 */
	public void setTipoCalle(TipoCalleDTO tipoCalle) {
		this.tipoCalle = tipoCalle;
	}

	/**
	 * @return Returns the cuadrante.
	 */
	public String getCuadrante() {
		return cuadrante;
	}

	/**
	 * @param cuadrante The cuadrante to set.
	 */
	public void setCuadrante(String cuadrante) {
		this.cuadrante = cuadrante;
	}

	/**
	 * @return Returns the fnueva.
	 */
	public String getFnueva() {
		return fnueva;
	}

	/**
	 * @param fnueva The fnueva to set.
	 */
	public void setFnueva(String fnueva) {
		this.fnueva = fnueva;
	}

	/**
	 * @return Returns the id_zon.
	 */
	public long getId_zon() {
		return id_zon;
	}

	/**
	 * @param id_zon The id_zon to set.
	 */
	public void setId_zon(long id_zon) {
		this.id_zon = id_zon;
	}

	public String getNom_region() {
		return nom_region;
	}

	public void setNom_region(String nom_region) {
		this.nom_region = nom_region;
	}

	/**
	 * @return Devuelve dir_conflictiva.
	 */
	public String getDir_conflictiva() {
		return dir_conflictiva;
	}
	/**
	 * @return Devuelve dir_conflictiva_comentario.
	 */
	public String getDir_conflictiva_comentario() {
		return dir_conflictiva_comentario;
	}
	/**
	 * @param dir_conflictiva El dir_conflictiva a establecer.
	 */
	public void setDir_conflictiva(String dir_conflictiva) {
		this.dir_conflictiva = dir_conflictiva;
	}
	/**
	 * @param dir_conflictiva_comentario El dir_conflictiva_comentario a establecer.
	 */
	public void setDir_conflictiva_comentario(String dir_conflictiva_comentario) {
		this.dir_conflictiva_comentario = dir_conflictiva_comentario;
	}
	/**
	 * @return Devuelve tipoPicking.
	 */
	public String getTipoPicking() {
		return tipoPicking;
	}
	/**
	 * @param tipoPicking El tipoPicking a establecer.
	 */
	public void setTipoPicking(String tipoPicking) {
		this.tipoPicking = tipoPicking;
	}
	/**
	 * @return Devuelve id_poligono.
	 */
	public int getId_poligono() {
		return id_poligono;
	}
	/**
	 * @param id_poligono El id_poligono a establecer.
	 */
	public void setId_poligono(int id_poligono) {
		this.id_poligono = id_poligono;
	}
	/**
	 * @return Devuelve nom_zona.
	 */
	public String getNom_zona() {
		return nom_zona;
	}
	/**
	 * @param nom_zona El nom_zona a establecer.
	 */
	public void setNom_zona(String nom_zona) {
		this.nom_zona = nom_zona;
	}

	public long getCom_id() {
		return com_id;
	}

	public void setCom_id(long com_id) {
		this.com_id = com_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLoc_cod() {
		return loc_cod;
	}

	public void setLoc_cod(long loc_cod) {
		this.loc_cod = loc_cod;
	}

	public long getTipo_calle() {
		return tipo_calle;
	}

	public void setTipo_calle(long tipo_calle) {
		this.tipo_calle = tipo_calle;
	}

	public long getReg_id() {
		return reg_id;
	}

	public void setReg_id(long reg_id) {
		this.reg_id = reg_id;
	}

	public String getNueva() {
		return nueva;
	}

	public void setNueva(String nueva) {
		this.nueva = nueva;
	}

	public String getCom_nombre() {
		return com_nombre;
	}

	public void setCom_nombre(String com_nombre) {
		this.com_nombre = com_nombre;
	}

	public String getReg_nombre() {
		return reg_nombre;
	}

	public void setReg_nombre(String reg_nombre) {
		this.reg_nombre = reg_nombre;
	}


	public long getZona_id() {
		return zona_id;
	}

	public void setZona_id(long zona_id) {
		this.zona_id = zona_id;
	}


	/**
	 * @return Devuelve confirmada.
	 */
	public boolean isConfirmada() {
		return confirmada;
	}
	/**
	 * @return Devuelve latitud.
	 */
	public double getLatitud() {
		return latitud;
	}
	/**
	 * @return Devuelve longitud.
	 */
	public double getLongitud() {
		return longitud;
	}
	/**
	 * @param confirmada El confirmada a establecer.
	 */
	public void setConfirmada(boolean confirmada) {
		this.confirmada = confirmada;
	}
	/**
	 * @param latitud El latitud a establecer.
	 */
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	/**
	 * @param longitud El longitud a establecer.
	 */
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}


	public long getFec_crea_long() {
		return fec_crea_long;
	}


	public void setFec_crea_long(long fec_crea_long) {
		this.fec_crea_long = fec_crea_long;
	}
	
	
}
