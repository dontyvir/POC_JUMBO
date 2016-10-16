package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class BinFormPickDTO  implements Serializable{
	
	private long	id_fpbin;
	private long	id_ronda;
	private String	cod_bin;
	private String	cod_ubicacion;
	private String	tipo;
	private long 	id_pedido;
	private int 	posicion;
	
	
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
	 * @return Returns the cod_bin.
	 */
	public String getCod_bin() {
		return cod_bin;
	}
	/**
	 * @return Returns the cod_ubicacion.
	 */
	public String getCod_ubicacion() {
		return cod_ubicacion;
	}
	/**
	 * @return Returns the id_fpbin.
	 */
	public long getId_fpbin() {
		return id_fpbin;
	}
	/**
	 * @return Returns the id_ronda.
	 */
	public long getId_ronda() {
		return id_ronda;
	}
	/**
	 * @return Returns the tipo.
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param cod_bin The cod_bin to set.
	 */
	public void setCod_bin(String cod_bin) {
		this.cod_bin = cod_bin;
	}
	/**
	 * @param cod_ubicacion The cod_ubicacion to set.
	 */
	public void setCod_ubicacion(String cod_ubicacion) {
		this.cod_ubicacion = cod_ubicacion;
	}
	/**
	 * @param id_fpbin The id_fpbin to set.
	 */
	public void setId_fpbin(long id_fpbin) {
		this.id_fpbin = id_fpbin;
	}
	/**
	 * @param id_ronda The id_ronda to set.
	 */
	public void setId_ronda(long id_ronda) {
		this.id_ronda = id_ronda;
	}
	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	



}
