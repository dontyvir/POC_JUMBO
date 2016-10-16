package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class TPBinOpDTO implements Serializable{
	
	private int posicion;
	private String cod_bin;
	private long id_op;
	private String tipo;
	private String cod_sello1;
	private String cod_sello2;
	private String cod_ubicacion;
	private long id_bin;
	//	---------- mod_feb09 - ini------------------------
	private String visualizado;
	
	/**
	 * @return Returns the visualizado.
	 */
	public String getVisualizado() {
		return visualizado;
	}
	/**
	 * @param visualizado The visualizado to set.
	 */
	public void setVisualizado(String visualizado) {
		this.visualizado = visualizado;
	}
	//	---------- mod_feb09 - fin------------------------	
	public long getId_bin() {
		return id_bin;
	}
	public void setId_bin(long id_bin) {
		this.id_bin = id_bin;
	}
	public String getCod_ubicacion() {
		return cod_ubicacion;
	}
	public void setCod_ubicacion(String cod_ubicacion) {
		this.cod_ubicacion = cod_ubicacion;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getCod_bin() {
		return cod_bin;
	}
	public void setCod_bin(String cod_bin) {
		this.cod_bin = cod_bin;
	}
	public String getCod_sello1() {
		return cod_sello1;
	}
	public void setCod_sello1(String cod_sello1) {
		this.cod_sello1 = cod_sello1;
	}
	public String getCod_sello2() {
		return cod_sello2;
	}
	public void setCod_sello2(String cod_sello2) {
		this.cod_sello2 = cod_sello2;
	}
	public long getId_op() {
		return id_op;
	}
	public void setId_op(long id_op) {
		this.id_op = id_op;
	}
	public int getPosicion() {
		return posicion;
	}
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	
	
}
