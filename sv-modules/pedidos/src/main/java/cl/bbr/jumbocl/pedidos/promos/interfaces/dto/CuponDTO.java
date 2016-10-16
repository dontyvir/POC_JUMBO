package cl.bbr.jumbocl.pedidos.promos.interfaces.dto;

import cl.bbr.jumbocl.common.utils.Formatos;

public class CuponDTO {
	public static int CUPON_LEN =30;
	public static int ESTADO_LEN =1;
	public static int CANTIDAD_LEN =4;
	
	private String cupon;
	private String estado;
	private int cantidad_usada;
	
	public CuponDTO() {		
	}

	
	public String toMsg(){
		String out = "";
		out += Formatos.formatField(cupon, 	CUPON_LEN, 				Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(estado, 	ESTADO_LEN, 		Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(""+cantidad_usada, 	CANTIDAD_LEN, 	Formatos.ALIGN_RIGHT,"0");				
		return out;
	}
	
	/**
	 * @return Returns the cantidad_usada.
	 */
	public int getCantidad_usada() {
		return cantidad_usada;
	}

	/**
	 * @param cantidad_usada The cantidad_usada to set.
	 */
	public void setCantidad_usada(int cantidad_usada) {
		this.cantidad_usada = cantidad_usada;
	}

	/**
	 * @return Returns the cupon.
	 */
	public String getCupon() {
		return cupon;
	}

	/**
	 * @param cupon The cupon to set.
	 */
	public void setCupon(String cupon) {
		this.cupon = cupon;
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

}
