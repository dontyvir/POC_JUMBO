package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
/**
 * 
 * @author BBR
 *
 */
public class DetalleFormPickingDTO implements Serializable{

	private long id_row;
	private long id_detalle;
	private long id_ronda;
	private long id_det_ronda;
	private long id_form_bin;
	private String cBarra;
	private String tipo;
	private double cantidad;
	private String pesable;
	private int mot_sust;
	private String cod_bin;
	private long id_prod;
	private String cod_sap;
	private String Umed;
	private String desc_prod;
	private int posicion;
	private long id_pedido;
	private long id_fpick;
	private double cant_rel_ped;
	
	
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
	/**
	 * @return Returns the cantidad.
	 */
	public double getCantidad() {
		return cantidad;
	}
	/**
	 * @return Returns the cBarra.
	 */
	public String getCBarra() {
		return cBarra;
	}
	/**
	 * @return Returns the id_det_ronda.
	 */
	public long getId_det_ronda() {
		return id_det_ronda;
	}
	/**
	 * @return Returns the id_detalle.
	 */
	public long getId_detalle() {
		return id_detalle;
	}
	/**
	 * @return Returns the id_form_bin.
	 */
	public long getId_form_bin() {
		return id_form_bin;
	}
	/**
	 * @return Returns the id_ronda.
	 */
	public long getId_ronda() {
		return id_ronda;
	}
	/**
	 * @return Returns the id_row.
	 */
	public long getId_row() {
		return id_row;
	}
	/**
	 * @return Returns the mot_sust.
	 */
	public int getMot_sust() {
		return mot_sust;
	}
	/**
	 * @return Returns the pesable.
	 */
	public String getPesable() {
		return pesable;
	}
	/**
	 * @return Returns the tipo.
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	/**
	 * @param barra The cBarra to set.
	 */
	public void setCBarra(String barra) {
		cBarra = barra;
	}
	/**
	 * @param id_det_ronda The id_det_ronda to set.
	 */
	public void setId_det_ronda(long id_det_ronda) {
		this.id_det_ronda = id_det_ronda;
	}
	/**
	 * @param id_detalle The id_detalle to set.
	 */
	public void setId_detalle(long id_detalle) {
		this.id_detalle = id_detalle;
	}
	/**
	 * @param id_form_bin The id_form_bin to set.
	 */
	public void setId_form_bin(long id_form_bin) {
		this.id_form_bin = id_form_bin;
	}
	/**
	 * @param id_ronda The id_ronda to set.
	 */
	public void setId_ronda(long id_ronda) {
		this.id_ronda = id_ronda;
	}
	/**
	 * @param id_row The id_row to set.
	 */
	public void setId_row(long id_row) {
		this.id_row = id_row;
	}
	/**
	 * @param mot_sust The mot_sust to set.
	 */
	public void setMot_sust(int mot_sust) {
		this.mot_sust = mot_sust;
	}
	/**
	 * @param pesable The pesable to set.
	 */
	public void setPesable(String pesable) {
		this.pesable = pesable;
	}
	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
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
	 * @return Returns the cod_sap.
	 */
	public String getCod_sap() {
		return cod_sap;
	}
	/**
	 * @return Returns the desc_prod.
	 */
	public String getDesc_prod() {
		return desc_prod;
	}
	/**
	 * @return Returns the id_prod.
	 */
	public long getId_prod() {
		return id_prod;
	}
	/**
	 * @return Returns the umed.
	 */
	public String getUmed() {
		return Umed;
	}
	/**
	 * @param cod_sap The cod_sap to set.
	 */
	public void setCod_sap(String cod_sap) {
		this.cod_sap = cod_sap;
	}
	/**
	 * @param desc_prod The desc_prod to set.
	 */
	public void setDesc_prod(String desc_prod) {
		this.desc_prod = desc_prod;
	}
	/**
	 * @param id_prod The id_prod to set.
	 */
	public void setId_prod(long id_prod) {
		this.id_prod = id_prod;
	}
	/**
	 * @param umed The umed to set.
	 */
	public void setUmed(String umed) {
		Umed = umed;
	}
	/**
	 * @return Returns the id_pedido.
	 */
	public long getId_pedido() {
		return id_pedido;
	}
	/**
	 * @param id_pedido The id_pedido to set.
	 */
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
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
	 * @return Returns the cant_rel_ped.
	 */
	public double getCant_rel_ped() {
		return cant_rel_ped;
	}
	/**
	 * @param cant_rel_ped The cant_rel_ped to set.
	 */
	public void setCant_rel_ped(double cant_rel_ped) {
		this.cant_rel_ped = cant_rel_ped;
	}
	
	
	

}
