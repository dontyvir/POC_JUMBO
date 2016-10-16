package cl.bbr.jumbocl.common.model;

import java.sql.Date;

/**
 * Clase que captura desde la base de datos los datos de un pedido
 * @author bbr
 *
 */
public class PedidosEntity {
	private int id_pedido;
	private int id_estado;
	private int id_jdespacho;
	private int id_jpicking;
	private int id_mot;
	private int id_mot_ant;
	private int id_local;
	private int id_comuna;
	private int id_zona;
	private int id_usuario;
	private String genero;
	private Date fnac;
	private String nom_cliente;
	private String telefono2;	
	private String telefono;
	private Date fdespacho;
	private double costo_despacho;
	private Date fcreacion;
	private double monto_pedido;
	private String indicacion;
	private String medio_pago;
	private String num_mp;
	private String titular;
	private String rut_tit;
	private String nom_tit;
	private int cant_productos;
	private int cant_bins;
	private int sin_gente_op;
	private String sin_gente_txt;
	private String dir_tipo_calle;
	private String dir_calle;
	private String dir_numero;
	private String dir_depto;
	private String pol_sustitucion;

	/**
	 * @return Returns the dir_calle.
	 */
	public String getDir_calle() {
		return dir_calle;
	}

	/**
	 * @param dir_calle The dir_calle to set.
	 */
	public void setDir_calle(String dir_calle) {
		this.dir_calle = dir_calle;
	}

	/**
	 * @return Returns the dir_depto.
	 */
	public String getDir_depto() {
		return dir_depto;
	}

	/**
	 * @param dir_depto The dir_depto to set.
	 */
	public void setDir_depto(String dir_depto) {
		this.dir_depto = dir_depto;
	}

	/**
	 * @return Returns the dir_numero.
	 */
	public String getDir_numero() {
		return dir_numero;
	}

	/**
	 * @param dir_numero The dir_numero to set.
	 */
	public void setDir_numero(String dir_numero) {
		this.dir_numero = dir_numero;
	}

	/**
	 * @return Returns the dir_tipo_calle.
	 */
	public String getDir_tipo_calle() {
		return dir_tipo_calle;
	}

	/**
	 * @param dir_tipo_calle The dir_tipo_calle to set.
	 */
	public void setDir_tipo_calle(String dir_tipo_calle) {
		this.dir_tipo_calle = dir_tipo_calle;
	}

	/**
	 * @return Returns the pol_sustitucion.
	 */
	public String getPol_sustitucion() {
		return pol_sustitucion;
	}

	/**
	 * @param pol_sustitucion The pol_sustitucion to set.
	 */
	public void setPol_sustitucion(String pol_sustitucion) {
		this.pol_sustitucion = pol_sustitucion;
	}

	/**
	 * @return Returns the sin_gente_op.
	 */
	public int getSin_gente_op() {
		return sin_gente_op;
	}

	/**
	 * @param sin_gente_op The sin_gente_op to set.
	 */
	public void setSin_gente_op(int sin_gente_op) {
		this.sin_gente_op = sin_gente_op;
	}

	/**
	 * @return Returns the sin_gente_txt.
	 */
	public String getSin_gente_txt() {
		return sin_gente_txt;
	}

	/**
	 * @param sin_gente_txt The sin_gente_txt to set.
	 */
	public void setSin_gente_txt(String sin_gente_txt) {
		this.sin_gente_txt = sin_gente_txt;
	}

	public PedidosEntity() {
	}

	/**
	 * @param id_pedido
	 * @param id_estado
	 * @param id_jdespacho
	 * @param id_jpicking
	 * @param id_mot
	 * @param id_mot_ant
	 * @param id_local
	 * @param id_comuna
	 * @param id_zona
	 * @param id_usuario
	 * @param genero
	 * @param fnac
	 * @param nom_cliente
	 * @param telefono2
	 * @param telefono
	 * @param fdespacho
	 * @param costo_despacho
	 * @param fcreacion
	 * @param monto_pedido
	 * @param indicacion
	 * @param medio_pago
	 * @param num_mp
	 * @param titular
	 * @param rut_tit
	 * @param nom_tit
	 * @param cant_productos
	 * @param cant_bins
	 */
	public PedidosEntity(int id_pedido, int id_estado, int id_jdespacho, int id_jpicking, int id_mot, int id_mot_ant, int id_local, int id_comuna, int id_zona, int id_usuario, String genero, Date fnac, String nom_cliente, String telefono2, String telefono, Date fdespacho, double costo_despacho, Date fcreacion, double monto_pedido, String indicacion, String medio_pago, String num_mp, String titular, String rut_tit, String nom_tit, int cant_productos, int cant_bins) {
		this.id_pedido = id_pedido;
		this.id_estado = id_estado;
		this.id_jdespacho = id_jdespacho;
		this.id_jpicking = id_jpicking;
		this.id_mot = id_mot;
		this.id_mot_ant = id_mot_ant;
		this.id_local = id_local;
		this.id_comuna = id_comuna;
		this.id_zona = id_zona;
		this.id_usuario = id_usuario;
		this.genero = genero;
		this.fnac = fnac;
		this.nom_cliente = nom_cliente;
		this.telefono2 = telefono2;
		this.telefono = telefono;
		this.fdespacho = fdespacho;
		this.costo_despacho = costo_despacho;
		this.fcreacion = fcreacion;
		this.monto_pedido = monto_pedido;
		this.indicacion = indicacion;
		this.medio_pago = medio_pago;
		this.num_mp = num_mp;
		this.titular = titular;
		this.rut_tit = rut_tit;
		this.nom_tit = nom_tit;
		this.cant_productos = cant_productos;
		this.cant_bins = cant_bins;
	}

	/**
	 * @return Returns the cant_bins.
	 */
	public int getCant_bins() {
		return cant_bins;
	}

	/**
	 * @return Returns the cant_productos.
	 */
	public int getCant_productos() {
		return cant_productos;
	}

	/**
	 * @return Returns the costo_despacho.
	 */
	public double getCosto_despacho() {
		return costo_despacho;
	}

	/**
	 * @return Returns the fcreacion.
	 */
	public Date getFcreacion() {
		return fcreacion;
	}

	/**
	 * @return Returns the fdespacho.
	 */
	public Date getFdespacho() {
		return fdespacho;
	}

	/**
	 * @return Returns the fnac.
	 */
	public Date getFnac() {
		return fnac;
	}

	/**
	 * @return Returns the genero.
	 */
	public String getGenero() {
		return genero;
	}

	/**
	 * @return Returns the id_comuna.
	 */
	public int getId_comuna() {
		return id_comuna;
	}

	/**
	 * @return Returns the id_estado.
	 */
	public int getId_estado() {
		return id_estado;
	}

	/**
	 * @return Returns the id_jdespacho.
	 */
	public int getId_jdespacho() {
		return id_jdespacho;
	}

	/**
	 * @return Returns the id_jpicking.
	 */
	public int getId_jpicking() {
		return id_jpicking;
	}

	/**
	 * @return Returns the id_local.
	 */
	public int getId_local() {
		return id_local;
	}

	/**
	 * @return Returns the id_mot.
	 */
	public int getId_mot() {
		return id_mot;
	}

	/**
	 * @return Returns the id_mot_ant.
	 */
	public int getId_mot_ant() {
		return id_mot_ant;
	}

	/**
	 * @return Returns the id_pedido.
	 */
	public int getId_pedido() {
		return id_pedido;
	}

	/**
	 * @return Returns the id_usuario.
	 */
	public int getId_usuario() {
		return id_usuario;
	}

	/**
	 * @return Returns the id_zona.
	 */
	public int getId_zona() {
		return id_zona;
	}

	/**
	 * @return Returns the indicacion.
	 */
	public String getIndicacion() {
		return indicacion;
	}

	/**
	 * @return Returns the medio_pago.
	 */
	public String getMedio_pago() {
		return medio_pago;
	}

	/**
	 * @return Returns the monto_pedido.
	 */
	public double getMonto_pedido() {
		return monto_pedido;
	}

	/**
	 * @return Returns the nom_cliente.
	 */
	public String getNom_cliente() {
		return nom_cliente;
	}

	/**
	 * @return Returns the nom_tit.
	 */
	public String getNom_tit() {
		return nom_tit;
	}

	/**
	 * @return Returns the num_mp.
	 */
	public String getNum_mp() {
		return num_mp;
	}

	/**
	 * @return Returns the rut_tit.
	 */
	public String getRut_tit() {
		return rut_tit;
	}

	/**
	 * @return Returns the telefono.
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @return Returns the telefono2.
	 */
	public String getTelefono2() {
		return telefono2;
	}

	/**
	 * @return Returns the titular.
	 */
	public String getTitular() {
		return titular;
	}

	/**
	 * @param cant_bins The cant_bins to set.
	 */
	public void setCant_bins(int cant_bins) {
		this.cant_bins = cant_bins;
	}

	/**
	 * @param cant_productos The cant_productos to set.
	 */
	public void setCant_productos(int cant_productos) {
		this.cant_productos = cant_productos;
	}

	/**
	 * @param costo_despacho The costo_despacho to set.
	 */
	public void setCosto_despacho(double costo_despacho) {
		this.costo_despacho = costo_despacho;
	}

	/**
	 * @param fcreacion The fcreacion to set.
	 */
	public void setFcreacion(Date fcreacion) {
		this.fcreacion = fcreacion;
	}

	/**
	 * @param fdespacho The fdespacho to set.
	 */
	public void setFdespacho(Date fdespacho) {
		this.fdespacho = fdespacho;
	}

	/**
	 * @param fnac The fnac to set.
	 */
	public void setFnac(Date fnac) {
		this.fnac = fnac;
	}

	/**
	 * @param genero The genero to set.
	 */
	public void setGenero(String genero) {
		this.genero = genero;
	}

	/**
	 * @param id_comuna The id_comuna to set.
	 */
	public void setId_comuna(int id_comuna) {
		this.id_comuna = id_comuna;
	}

	/**
	 * @param id_estado The id_estado to set.
	 */
	public void setId_estado(int id_estado) {
		this.id_estado = id_estado;
	}

	/**
	 * @param id_jdespacho The id_jdespacho to set.
	 */
	public void setId_jdespacho(int id_jdespacho) {
		this.id_jdespacho = id_jdespacho;
	}

	/**
	 * @param id_jpicking The id_jpicking to set.
	 */
	public void setId_jpicking(int id_jpicking) {
		this.id_jpicking = id_jpicking;
	}

	/**
	 * @param id_local The id_local to set.
	 */
	public void setId_local(int id_local) {
		this.id_local = id_local;
	}

	/**
	 * @param id_mot The id_mot to set.
	 */
	public void setId_mot(int id_mot) {
		this.id_mot = id_mot;
	}

	/**
	 * @param id_mot_ant The id_mot_ant to set.
	 */
	public void setId_mot_ant(int id_mot_ant) {
		this.id_mot_ant = id_mot_ant;
	}

	/**
	 * @param id_pedido The id_pedido to set.
	 */
	public void setId_pedido(int id_pedido) {
		this.id_pedido = id_pedido;
	}

	/**
	 * @param id_usuario The id_usuario to set.
	 */
	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	/**
	 * @param id_zona The id_zona to set.
	 */
	public void setId_zona(int id_zona) {
		this.id_zona = id_zona;
	}

	/**
	 * @param indicacion The indicacion to set.
	 */
	public void setIndicacion(String indicacion) {
		this.indicacion = indicacion;
	}

	/**
	 * @param medio_pago The medio_pago to set.
	 */
	public void setMedio_pago(String medio_pago) {
		this.medio_pago = medio_pago;
	}

	/**
	 * @param monto_pedido The monto_pedido to set.
	 */
	public void setMonto_pedido(double monto_pedido) {
		this.monto_pedido = monto_pedido;
	}

	/**
	 * @param nom_cliente The nom_cliente to set.
	 */
	public void setNom_cliente(String nom_cliente) {
		this.nom_cliente = nom_cliente;
	}

	/**
	 * @param nom_tit The nom_tit to set.
	 */
	public void setNom_tit(String nom_tit) {
		this.nom_tit = nom_tit;
	}

	/**
	 * @param num_mp The num_mp to set.
	 */
	public void setNum_mp(String num_mp) {
		this.num_mp = num_mp;
	}

	/**
	 * @param rut_tit The rut_tit to set.
	 */
	public void setRut_tit(String rut_tit) {
		this.rut_tit = rut_tit;
	}

	/**
	 * @param telefono The telefono to set.
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @param telefono2 The telefono2 to set.
	 */
	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	/**
	 * @param titular The titular to set.
	 */
	public void setTitular(String titular) {
		this.titular = titular;
	}		
	
	
	
}
