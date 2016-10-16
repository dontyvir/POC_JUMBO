package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class CalJorPickDTO implements Serializable {
	
	long	id_jornada;
	String	fecha;
	String	h_inicio;
	String	h_fin;
	long	capac_picking;
	long	capac_ocupada;
	
	public long getCapac_ocupada() {
		return capac_ocupada;
	}

	public void setCapac_ocupada(long capac_ocupada) {
		this.capac_ocupada = capac_ocupada;
	}

	public CalJorPickDTO(){
		
	}

	public long getCapac_picking() {
		return capac_picking;
	}

	public void setCapac_picking(long capac_picking) {
		this.capac_picking = capac_picking;
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

	public String getH_inicio() {
		return h_inicio;
	}

	public void setH_inicio(String h_inicio) {
		this.h_inicio = h_inicio;
	}

	public long getId_jornada() {
		return id_jornada;
	}

	public void setId_jornada(long id_jornada) {
		this.id_jornada = id_jornada;
	}
	
}
