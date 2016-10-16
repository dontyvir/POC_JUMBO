package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class PoligonosZonaDTO implements Serializable{

	private long	id_zona;
	private long[]  poligonos;

	
	public long[] getPoligonos() {
		return poligonos;
	}

	
	public void setPoligonos(long[] poligonos) {
		this.poligonos = poligonos;
	}


	public long getId_zona() {
		return id_zona;
	}


	public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
	}

	
}
