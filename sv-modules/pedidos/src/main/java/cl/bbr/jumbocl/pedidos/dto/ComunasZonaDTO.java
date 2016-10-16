package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class ComunasZonaDTO implements Serializable{

	private long	id_zona;
	private long[]  comunas;

	
	public long[] getComunas() {
		return comunas;
	}

	
	public void setComunas(long[] comunas) {
		this.comunas = comunas;
	}


	public long getId_zona() {
		return id_zona;
	}


	public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
	}

	
}
