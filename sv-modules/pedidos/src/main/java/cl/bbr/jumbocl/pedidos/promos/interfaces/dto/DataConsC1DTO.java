package cl.bbr.jumbocl.pedidos.promos.interfaces.dto;

import cl.bbr.jumbocl.common.utils.Formatos;

public class DataConsC1DTO {
	
	public static final int N_CUPON_LEN = 30;
	
	private String n_cupon;

	public DataConsC1DTO() {	
	}
	
	public String toMsg(){
		String out = "";
		out += Formatos.formatField(n_cupon, 	N_CUPON_LEN, 	Formatos.ALIGN_RIGHT,"0");			
		return out;
	}
	

	/**
	 * @return Returns the n_cupon.
	 */
	public String getN_cupon() {
		return n_cupon;
	}

	/**
	 * @param n_cupon The n_cupon to set.
	 */
	public void setN_cupon(String n_cupon) {
		this.n_cupon = n_cupon;
	}

}
