package cl.bbr.jumbocl.pedidos.collaboration;

import java.io.Serializable;

public class ProcModZonaxComuna implements Serializable {

	private long id_zona;
	private long id_comuna;
	private int orden;
	
	public ProcModZonaxComuna(){
		
	}

	public long getId_comuna() {
		return id_comuna;
	}

	public void setId_comuna(long id_comuna) {
		this.id_comuna = id_comuna;
	}

	public long getId_zona() {
		return id_zona;
	}

	public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}
	
	
	
}
