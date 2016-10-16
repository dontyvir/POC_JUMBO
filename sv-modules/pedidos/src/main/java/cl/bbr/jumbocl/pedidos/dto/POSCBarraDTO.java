package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class POSCBarraDTO implements Serializable {
	
	private int cBarra;
	private String tipo_cbarra;
	private int id_detalle;
	
	
	public int getCBarra() {
		return cBarra;
	}
	public void setCBarra(int barra) {
		cBarra = barra;
	}
	public int getId_detalle() {
		return id_detalle;
	}
	public void setId_detalle(int id_detalle) {
		this.id_detalle = id_detalle;
	}
	public String getTipo_cbarra() {
		return tipo_cbarra;
	}
	public void setTipo_cbarra(String tipo_cbarra) {
		this.tipo_cbarra = tipo_cbarra;
	}
	
	

}
