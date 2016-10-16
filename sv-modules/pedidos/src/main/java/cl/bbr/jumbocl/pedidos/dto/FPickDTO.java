package cl.bbr.jumbocl.pedidos.dto;

public class FPickDTO {
	
	private long id_fpick;
	private long id_ronda;
	private long id_form_bin;
	private String cbarra;
	private double cantidad;
	private double cant_pend;
	private String cod_bin;
	private int posicion;
	private long id_op;
	
	
	/**
	 * @return Returns the cant_pend.
	 */
	public double getCant_pend() {
		return cant_pend;
	}
	/**
	 * @param cant_pend The cant_pend to set.
	 */
	public void setCant_pend(double cant_pend) {
		this.cant_pend = cant_pend;
	}
	/**
	 * @return Returns the cantidad.
	 */
	public double getCantidad() {
		return cantidad;
	}
	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	/**
	 * @return Returns the cbarra.
	 */
	public String getCbarra() {
		return cbarra;
	}
	/**
	 * @param cbarra The cbarra to set.
	 */
	public void setCbarra(String cbarra) {
		this.cbarra = cbarra;
	}
	/**
	 * @return Returns the id_form_bin.
	 */
	public long getId_form_bin() {
		return id_form_bin;
	}
	/**
	 * @param id_form_bin The id_form_bin to set.
	 */
	public void setId_form_bin(long id_form_bin) {
		this.id_form_bin = id_form_bin;
	}
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
	/**
	 * @return Returns the cod_bin.
	 */
	public String getCod_bin() {
		return cod_bin;
	}
	/**
	 * @param cod_bin The cod_bin to set.
	 */
	public void setCod_bin(String cod_bin) {
		this.cod_bin = cod_bin;
	}
	/**
	 * @return Returns the id_op.
	 */
	public long getId_op() {
		return id_op;
	}
	/**
	 * @param id_op The id_op to set.
	 */
	public void setId_op(long id_op) {
		this.id_op = id_op;
	}
	/**
	 * @return Returns the posicion.
	 */
	public int getPosicion() {
		return posicion;
	}
	/**
	 * @param posicion The posicion to set.
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}



}
