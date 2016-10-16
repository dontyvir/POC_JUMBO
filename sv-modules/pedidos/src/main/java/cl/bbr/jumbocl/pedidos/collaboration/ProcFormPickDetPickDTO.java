package cl.bbr.jumbocl.pedidos.collaboration;

public class ProcFormPickDetPickDTO {

	private long id_ronda;
	private boolean pendientes;
	private long id_pick;
	private boolean por_idpick;

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
	/**
	 * @return Returns the pendientes.
	 */
	public boolean isPendientes() {
		return pendientes;
	}
	/**
	 * @param pendientes The pendientes to set.
	 */
	public void setPendientes(boolean pendientes) {
		this.pendientes = pendientes;
	}
	/**
	 * @return Returns the id_pick.
	 */
	public long getId_pick() {
		return id_pick;
	}
	/**
	 * @param id_pick The id_pick to set.
	 */
	public void setId_pick(long id_pick) {
		this.id_pick = id_pick;
	}
	/**
	 * @return Returns the por_idpick.
	 */
	public boolean isPor_idpick() {
		return por_idpick;
	}
	/**
	 * @param por_idpick The por_idpick to set.
	 */
	public void setPor_idpick(boolean por_idpick) {
		this.por_idpick = por_idpick;
	}
	
	

}
