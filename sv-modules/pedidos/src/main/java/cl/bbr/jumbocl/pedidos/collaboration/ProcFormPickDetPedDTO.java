package cl.bbr.jumbocl.pedidos.collaboration;

public class ProcFormPickDetPedDTO {

	private long id_ronda;
	private boolean sin_pickear;
	private long id_detalle;
	private boolean por_idDet;
	
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
	 * @return Returns the sin_pickear.
	 */
	public boolean isSin_pickear() {
		return sin_pickear;
	}
	/**
	 * @param sin_pickear The sin_pickear to set.
	 */
	public void setSin_pickear(boolean sin_pickear) {
		this.sin_pickear = sin_pickear;
	}
	/**
	 * @return Returns the id_detalle.
	 */
	public long getId_detalle() {
		return id_detalle;
	}
	/**
	 * @param id_detalle The id_detalle to set.
	 */
	public void setId_detalle(long id_detalle) {
		this.id_detalle = id_detalle;
	}
	/**
	 * @return Returns the por_idDet.
	 */
	public boolean isPor_idDet() {
		return por_idDet;
	}
	/**
	 * @param por_idDet The por_idDet to set.
	 */
	public void setPor_idDet(boolean por_idDet) {
		this.por_idDet = por_idDet;
	}
	
	

}
