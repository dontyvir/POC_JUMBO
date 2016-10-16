package cl.bbr.jumbocl.pedidos.dto;

public class DatosMedioPagoDTO {
	private String medio_pago; //JUMBOMAS TPARIS etc
	private long id_pedido;
	private String num_mp;
	private String rut_tit;
	private String dv_tit;
	private String nombre_tit;
	private String appaterno_tit;
	private String apmaterno_tit;
	private String direccion_tit;
	private String numero_tit;
	private String tipo; //boleta = B o factura = F;
	private long num_dcto;
	private String cuotas;
	private String banco;
	private String fecha_expiracion;
	private String Meses_LibrePago;
	private String razon_social;
	private String direccion_empresa;
	private String usr_login;
	private String nom_tban;
	
	
	
	
	/**
	 * @return Devuelve apmaterno_tit.
	 */
	public String getApmaterno_tit() {
		return apmaterno_tit;
	}
	/**
	 * @return Devuelve appaterno_tit.
	 */
	public String getAppaterno_tit() {
		return appaterno_tit;
	}
	/**
	 * @return Devuelve banco.
	 */
	public String getBanco() {
		return banco;
	}
	/**
	 * @return Devuelve cuotas.
	 */
	public String getCuotas() {
		return cuotas;
	}
	/**
	 * @return Devuelve direccion_empresa.
	 */
	public String getDireccion_empresa() {
		return direccion_empresa;
	}
	/**
	 * @return Devuelve direccion_tit.
	 */
	public String getDireccion_tit() {
		return direccion_tit;
	}
	/**
	 * @return Devuelve dv_tit.
	 */
	public String getDv_tit() {
		return dv_tit;
	}
	/**
	 * @return Devuelve fecha_expiracion.
	 */
	public String getFecha_expiracion() {
		return fecha_expiracion;
	}
	/**
	 * @return Devuelve id_pedido.
	 */
	public long getId_pedido() {
		return id_pedido;
	}
	/**
	 * @return Devuelve medio_pago.
	 */
	public String getMedio_pago() {
		return medio_pago;
	}
	/**
	 * @return Devuelve meses_LibrePago.
	 */
	public String getMeses_LibrePago() {
		return Meses_LibrePago;
	}
	/**
	 * @return Devuelve nom_tban.
	 */
	public String getNom_tban() {
		return nom_tban;
	}
	/**
	 * @return Devuelve nombre_tit.
	 */
	public String getNombre_tit() {
		return nombre_tit;
	}
	/**
	 * @return Devuelve num_dcto.
	 */
	public long getNum_dcto() {
		return num_dcto;
	}
	/**
	 * @return Devuelve num_mp.
	 */
	public String getNum_mp() {
		return num_mp;
	}
	/**
	 * @return Devuelve numero_tit.
	 */
	public String getNumero_tit() {
		return numero_tit;
	}
	/**
	 * @return Devuelve razon_social.
	 */
	public String getRazon_social() {
		return razon_social;
	}
	/**
	 * @return Devuelve rut_tit.
	 */
	public String getRut_tit() {
		return rut_tit;
	}
	/**
	 * @return Devuelve tipo.
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @return Devuelve usr_login.
	 */
	public String getUsr_login() {
		return usr_login;
	}
	/**
	 * @param apmaterno_tit El apmaterno_tit a establecer.
	 */
	public void setApmaterno_tit(String apmaterno_tit) {
		this.apmaterno_tit = apmaterno_tit;
	}
	/**
	 * @param appaterno_tit El appaterno_tit a establecer.
	 */
	public void setAppaterno_tit(String appaterno_tit) {
		this.appaterno_tit = appaterno_tit;
	}
	/**
	 * @param banco El banco a establecer.
	 */
	public void setBanco(String banco) {
		this.banco = banco;
	}
	/**
	 * @param cuotas El cuotas a establecer.
	 */
	public void setCuotas(String cuotas) {
		this.cuotas = cuotas;
	}
	/**
	 * @param direccion_empresa El direccion_empresa a establecer.
	 */
	public void setDireccion_empresa(String direccion_empresa) {
		this.direccion_empresa = direccion_empresa;
	}
	/**
	 * @param direccion_tit El direccion_tit a establecer.
	 */
	public void setDireccion_tit(String direccion_tit) {
		this.direccion_tit = direccion_tit;
	}
	/**
	 * @param dv_tit El dv_tit a establecer.
	 */
	public void setDv_tit(String dv_tit) {
		this.dv_tit = dv_tit;
	}
	/**
	 * @param fecha_expiracion El fecha_expiracion a establecer.
	 */
	public void setFecha_expiracion(String fecha_expiracion) {
		this.fecha_expiracion = fecha_expiracion;
	}
	/**
	 * @param id_pedido El id_pedido a establecer.
	 */
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	/**
	 * @param medio_pago El medio_pago a establecer.
	 */
	public void setMedio_pago(String medio_pago) {
		this.medio_pago = medio_pago;
	}
	/**
	 * @param meses_LibrePago El meses_LibrePago a establecer.
	 */
	public void setMeses_LibrePago(String meses_LibrePago) {
		Meses_LibrePago = meses_LibrePago;
	}
	/**
	 * @param nom_tban El nom_tban a establecer.
	 */
	public void setNom_tban(String nom_tban) {
		this.nom_tban = nom_tban;
	}
	/**
	 * @param nombre_tit El nombre_tit a establecer.
	 */
	public void setNombre_tit(String nombre_tit) {
		this.nombre_tit = nombre_tit;
	}
	/**
	 * @param num_dcto El num_dcto a establecer.
	 */
	public void setNum_dcto(long num_dcto) {
		this.num_dcto = num_dcto;
	}
	/**
	 * @param num_mp El num_mp a establecer.
	 */
	public void setNum_mp(String num_mp) {
		this.num_mp = num_mp;
	}
	/**
	 * @param numero_tit El numero_tit a establecer.
	 */
	public void setNumero_tit(String numero_tit) {
		this.numero_tit = numero_tit;
	}
	/**
	 * @param razon_social El razon_social a establecer.
	 */
	public void setRazon_social(String razon_social) {
		this.razon_social = razon_social;
	}
	/**
	 * @param rut_tit El rut_tit a establecer.
	 */
	public void setRut_tit(String rut_tit) {
		this.rut_tit = rut_tit;
	}
	/**
	 * @param tipo El tipo a establecer.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	/**
	 * @param usr_login El usr_login a establecer.
	 */
	public void setUsr_login(String usr_login) {
		this.usr_login = usr_login;
	}
}
