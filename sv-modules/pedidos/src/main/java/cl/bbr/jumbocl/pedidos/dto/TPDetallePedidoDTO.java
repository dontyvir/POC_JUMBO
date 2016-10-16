package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class TPDetallePedidoDTO implements Serializable{
	
	private long id_detalle;
	private long id_op;
	private String cod_sap;
	private String u_med;
	private String descripcion;
	private double cant_pedida;
	private double cant_pickeada;
	private double cant_faltante;
	private double cant_sinpickear;
	private double precio;
	private String observacion;
	private int es_pesable;
	private String sector;
	private int sust_camb_form;
	private long id_dronda;
	private int mot_sustitucion;
	
	public double getCant_faltante() {
		return cant_faltante;
	}
	public void setCant_faltante(double cant_faltante) {
		this.cant_faltante = cant_faltante;
	}
	public double getCant_pedida() {
		return cant_pedida;
	}
	public void setCant_pedida(double cant_pedida) {
		this.cant_pedida = cant_pedida;
	}
	public double getCant_pickeada() {
		return cant_pickeada;
	}
	public void setCant_pickeada(double cant_pickeada) {
		this.cant_pickeada = cant_pickeada;
	}
	public double getCant_sinpickear() {
		return cant_sinpickear;
	}
	public void setCant_sinpickear(double cant_sinpickear) {
		this.cant_sinpickear = cant_sinpickear;
	}
	public String getCod_sap() {
		return cod_sap;
	}
	public void setCod_sap(String cod_sap) {
		this.cod_sap = cod_sap;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getEs_pesable() {
		return es_pesable;
	}
	public void setEs_pesable(int es_pesable) {
		this.es_pesable = es_pesable;
	}
	public long getId_detalle() {
		return id_detalle;
	}
	public void setId_detalle(long id_detalle) {
		this.id_detalle = id_detalle;
	}
	public long getId_op() {
		return id_op;
	}
	public void setId_op(long id_op) {
		this.id_op = id_op;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public int getSust_camb_form() {
		return sust_camb_form;
	}
	public void setSust_camb_form(int sust_camb_form) {
		this.sust_camb_form = sust_camb_form;
	}
	public String getU_med() {
		return u_med;
	}
	public void setU_med(String u_med) {
		this.u_med = u_med;
	}
	public long getId_dronda() {
		return id_dronda;
	}
	public void setId_dronda(long id_dronda) {
		this.id_dronda = id_dronda;
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
