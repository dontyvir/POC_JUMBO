package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class TrxMpDetalleDTO implements Serializable {
	private long id_trxdet;
	private long id_detalle;
	private long id_trxmp;
	private long id_pedido;
	private long id_producto;
	private String cod_barra;
	private double precio;
	private String descripcion;
	private double cantidad;
	
	public TrxMpDetalleDTO() {
	}

	public long getId_trxdet() {
		return id_trxdet;
	}

	public void setId_trxdet(long id_trxdet) {
		this.id_trxdet = id_trxdet;
	}

	public double getCantidad() {
		return cantidad;
	}

	public String getCod_barra() {
		return cod_barra;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public long getId_detalle() {
		return id_detalle;
	}

	public long getId_pedido() {
		return id_pedido;
	}

	public long getId_producto() {
		return id_producto;
	}

	public long getId_trxmp() {
		return id_trxmp;
	}

	public double getPrecio() {
		return precio;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public void setCod_barra(String cod_barra) {
		this.cod_barra = cod_barra;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setId_detalle(long id_detalle) {
		this.id_detalle = id_detalle;
	}

	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}

	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}

	public void setId_trxmp(long id_trxmp) {
		this.id_trxmp = id_trxmp;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	

	
	
	
	
}
