package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class DetalleRondaDTO implements Serializable{
	private long id_dronda;
	private long id_detalle;
	private long id_ronda;
	private long id_pedido;
	private double cantidad;
	private double cant_pick;
	private double cant_faltan;
	private double cant_spick;
	private String sustituto;
	private int mot_sustitucion;
	//adicionales
	private String cod_prod1;
	private String uni_med;
	private String Descripcion;
	private double precio;
	private String pesable;
	private String Observacion;
	private String Nom_Sector;
	private long id_producto;
	
	
	
	/**
	 * @return Returns the id_producto.
	 */
	public long getId_producto() {
		return id_producto;
	}
	/**
	 * @param id_producto The id_producto to set.
	 */
	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return Descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}
	/**
	 * @return Returns the cod_prod1.
	 */
	public String getCod_prod1() {
		return cod_prod1;
	}
	/**
	 * @param cod_prod1 The cod_prod1 to set.
	 */
	public void setCod_prod1(String cod_prod1) {
		this.cod_prod1 = cod_prod1;
	}
	/**
	 * @return Returns the nom_Sector.
	 */
	public String getNom_Sector() {
		return Nom_Sector;
	}
	/**
	 * @param nom_Sector The nom_Sector to set.
	 */
	public void setNom_Sector(String nom_Sector) {
		Nom_Sector = nom_Sector;
	}
	/**
	 * @return Returns the observacion.
	 */
	public String getObservacion() {
		return Observacion;
	}
	/**
	 * @param observacion The observacion to set.
	 */
	public void setObservacion(String observacion) {
		Observacion = observacion;
	}
	/**
	 * @return Returns the pesable.
	 */
	public String getPesable() {
		return pesable;
	}
	/**
	 * @param pesable The pesable to set.
	 */
	public void setPesable(String pesable) {
		this.pesable = pesable;
	}
	/**
	 * @return Returns the precio.
	 */
	public double getPrecio() {
		return precio;
	}
	/**
	 * @param precio The precio to set.
	 */
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	/**
	 * @return Returns the uni_med.
	 */
	public String getUni_med() {
		return uni_med;
	}
	/**
	 * @param uni_med The uni_med to set.
	 */
	public void setUni_med(String uni_med) {
		this.uni_med = uni_med;
	}
	public double getCant_faltan() {
		return cant_faltan;
	}
	public void setCant_faltan(double cant_faltan) {
		this.cant_faltan = cant_faltan;
	}
	public double getCant_pick() {
		return cant_pick;
	}
	public void setCant_pick(double cant_pick) {
		this.cant_pick = cant_pick;
	}
	public double getCant_spick() {
		return cant_spick;
	}
	public void setCant_spick(double cant_spick) {
		this.cant_spick = cant_spick;
	}
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public long getId_detalle() {
		return id_detalle;
	}
	public void setId_detalle(long id_detalle) {
		this.id_detalle = id_detalle;
	}
	public long getId_dronda() {
		return id_dronda;
	}
	public void setId_dronda(long id_dronda) {
		this.id_dronda = id_dronda;
	}
	public long getId_pedido() {
		return id_pedido;
	}
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	public long getId_ronda() {
		return id_ronda;
	}
	public void setId_ronda(long id_ronda) {
		this.id_ronda = id_ronda;
	}
	/**
	 * @return Returns the sustituto.
	 */
	public String getSustituto() {
		return sustituto;
	}
	/**
	 * @param sustituto The sustituto to set.
	 */
	public void setSustituto(String sustituto) {
		this.sustituto = sustituto;
	}
	/**
	 * @return Returns the mot_sustitucion.
	 */
	public int getMot_sustitucion() {
		return mot_sustitucion;
	}
	/**
	 * @param mot_sustitucion The mot_sustitucion to set.
	 */
	public void setMot_sustitucion(int mot_sustitucion) {
		this.mot_sustitucion = mot_sustitucion;
	}
	
	
}
