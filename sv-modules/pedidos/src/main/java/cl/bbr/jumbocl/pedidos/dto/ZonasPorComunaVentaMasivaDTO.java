package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class ZonasPorComunaVentaMasivaDTO implements Serializable {
	private long zvmId;
	private long zvmIdComuna;
	private long zvmIdZona;
	public long getZvmId() {
		return zvmId;
	}
	public void setZvmId(long zvmId) {
		this.zvmId = zvmId;
	}
	public long getZvmIdComuna() {
		return zvmIdComuna;
	}
	public void setZvmIdComuna(long zvmIdComuna) {
		this.zvmIdComuna = zvmIdComuna;
	}
	public long getZvmIdZona() {
		return zvmIdZona;
	}
	public void setZvmIdZona(long zvmIdZona) {
		this.zvmIdZona = zvmIdZona;
	}
	
	
}
