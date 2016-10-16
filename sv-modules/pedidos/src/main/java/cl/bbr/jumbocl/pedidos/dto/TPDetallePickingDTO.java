package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class TPDetallePickingDTO implements Serializable{
	
	private String 	cBarra;
	private String 	tipo;
	private long 	id_detalle;
	private int 	posicion;
	private long 	id_dronda;
	private double 	cantidad;
	//	---------- mod_ene09 - ini------------------------
	private String auditado; //S si N no
	
	/**
	 * @return Returns the auditado.
	 */
	public String getAuditado() {
		return auditado;
	}
	/**
	 * @param auditado The auditado to set.
	 */
	public void setAuditado(String auditado) {
		this.auditado = auditado;
	}
	
	//	---------- mod_ene09 - ini------------------------
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public long getId_dronda() {
		return id_dronda;
	}
	public void setId_dronda(long id_dronda) {
		this.id_dronda = id_dronda;
	}
	public String getCBarra() {
		return cBarra;
	}
	public void setCBarra(String barra) {
		cBarra = barra;
	}
	public long getId_detalle() {
		return id_detalle;
	}
	public void setId_detalle(long id_detalle) {
		this.id_detalle = id_detalle;
	}
	public int getPosicion() {
		return posicion;
	}
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
}
