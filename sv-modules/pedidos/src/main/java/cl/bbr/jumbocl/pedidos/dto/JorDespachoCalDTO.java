package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class JorDespachoCalDTO implements Serializable{

	long	id_jdespacho;
	long	id_jpicking;
	long	id_zona;
	String	h_ini;
	String	h_fin;
	String	fecha;
	long	capac_despacho;
	long	capac_ocupada;
	String	jp_h_ini;
	String	jp_h_fin;
	String	jp_fecha;
	long	precio;
	
	long capacPicking;
	long capacOcupadaPicking;
		
	public JorDespachoCalDTO(){
		
	}
	
	public long getCapac_ocupada() {
		return capac_ocupada;
	}



	public void setCapac_ocupada(long capac_ocupada) {
		this.capac_ocupada = capac_ocupada;
	}



	public long getCapac_despacho() {
		return capac_despacho;
	}



	public void setCapac_despacho(long capac_despacho) {
		this.capac_despacho = capac_despacho;
	}



	public String getFecha() {
		return fecha;
	}



	public void setFecha(String fecha) {
		this.fecha = fecha;
	}



	public String getH_fin() {
		return h_fin;
	}



	public void setH_fin(String h_fin) {
		this.h_fin = h_fin;
	}



	public String getH_ini() {
		return h_ini;
	}



	public void setH_ini(String h_ini) {
		this.h_ini = h_ini;
	}



	public long getId_jdespacho() {
		return id_jdespacho;
	}



	public void setId_jdespacho(long id_jdespacho) {
		this.id_jdespacho = id_jdespacho;
	}



	public long getId_jpicking() {
		return id_jpicking;
	}



	public void setId_jpicking(long id_jpicking) {
		this.id_jpicking = id_jpicking;
	}



	public long getId_zona() {
		return id_zona;
	}



	public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
	}



	public String getJp_fecha() {
		return jp_fecha;
	}



	public void setJp_fecha(String jp_fecha) {
		this.jp_fecha = jp_fecha;
	}



	public String getJp_h_fin() {
		return jp_h_fin;
	}



	public void setJp_h_fin(String jp_h_fin) {
		this.jp_h_fin = jp_h_fin;
	}



	public String getJp_h_ini() {
		return jp_h_ini;
	}



	public void setJp_h_ini(String jp_h_ini) {
		this.jp_h_ini = jp_h_ini;
	}



	public long getPrecio() {
		return precio;
	}



	public void setPrecio(long precio) {
		this.precio = precio;
	}

	public long getCapacPicking() {
		return capacPicking;
	}

	public void setCapacPicking(long capacPicking) {
		this.capacPicking = capacPicking;
	}

	public long getCapacOcupadaPicking() {
		return capacOcupadaPicking;
	}

	public void setCapacOcupadaPicking(long capacOcupadaPicking) {
		this.capacOcupadaPicking = capacOcupadaPicking;
	}	
	
}