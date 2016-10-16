package cl.bbr.jumbocl.pedidos.collaboration;

public class ProcFPickDTO {
	private long id_ronda;
	private long id_fpick;
	
	/**
	 * @return Returns the id_fpick.
	 */
	public long getId_fpick() {
		return id_fpick;
	}
	/**
	 * @param id_fpick The id_fpick to set.
	 */
	public void setId_fpick(long id_fpick) {
		this.id_fpick = id_fpick;
	}
	/**
	 * @return Returns the id_ronda.
	 */
	public long getId_ronda() {
		return id_ronda;
	}
	/**
	 * @param id_ronda The id_ronda to set.
	 */
	public void setId_ronda(long id_ronda) {
		this.id_ronda = id_ronda;
	}

}
