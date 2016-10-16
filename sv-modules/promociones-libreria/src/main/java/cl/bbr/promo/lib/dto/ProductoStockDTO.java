package cl.bbr.promo.lib.dto;

import java.io.Serializable;

public class ProductoStockDTO implements Serializable {
	private long pro_id;
	private boolean tieneStock;
	private long cantidadMaxima;
	
	public long getPro_id() {
		return pro_id;
	}
	public void setPro_id(long pro_id) {
		this.pro_id = pro_id;
	}
	public boolean isTieneStock() {
		return tieneStock;
	}
	public void setTieneStock(boolean tieneStock) {
		this.tieneStock = tieneStock;
	}
	public long getCantidadMaxima() {
		return cantidadMaxima;
	}
	public void setCantidadMaxima(long cantidadMaxima) {
		this.cantidadMaxima = cantidadMaxima;
	}
	
	
}