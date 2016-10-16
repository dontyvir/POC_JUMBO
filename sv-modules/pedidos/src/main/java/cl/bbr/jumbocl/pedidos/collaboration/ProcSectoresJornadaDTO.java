package cl.bbr.jumbocl.pedidos.collaboration;

public class ProcSectoresJornadaDTO {
	private long id_jornada;
	private int flag_especiales=0; //0 normales 1 :especiales 
	private long id_zona=0;
	
	/**
	 * @return Returns the id_zona.
	 */
	public long getId_zona() {
		return id_zona;
	}
	/**
	 * @param id_zona The id_zona to set.
	 */
	public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
	}
	/**
	 * @return Returns the flag_especiales.
	 */
	public int getFlag_especiales() {
		return flag_especiales;
	}
	/**
	 * @param flag_especiales :0 para pedidos normales y 1 para pedidos especiales.
	 */
	public void setFlag_especiales(int flag_especiales) {
		this.flag_especiales = flag_especiales;
	}
	/**
	 * @return Returns the id_jornada.
	 */
	public long getId_jornada() {
		return id_jornada;
	}
	/**
	 * @param id_jornada The id_jornada to set.
	 */
	public void setId_jornada(long id_jornada) {
		this.id_jornada = id_jornada;
	}
	
	

}
