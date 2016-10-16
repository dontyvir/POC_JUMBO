package cl.bbr.jumbocl.pedidos.promos.interfaces.dto;

import java.io.Serializable;

import cl.bbr.jumbocl.common.utils.Formatos;

public class DataConsR1DTO implements Serializable{

	public static final int TIPO_CLIENTE_LEN = 2;
	public static final int RUT_LEN          = 8;
	public static final int FILLER_LEN       = 60;

	private String tipo_cliente;
	private String rut;
	private String filler; 
	
	public DataConsR1DTO() {
	
	}
	
	public String toMsg(){
		String out = "";
		out += Formatos.formatField(tipo_cliente, 	TIPO_CLIENTE_LEN, 	Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(rut, 			RUT_LEN, 			Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(filler, 		FILLER_LEN, 		Formatos.ALIGN_RIGHT,"0");		
		return out;
	}

	/**
	 * @return Returns the filler.
	 */
	public String getFiller() {
		return filler;
	}

	/**
	 * @param filler The filler to set.
	 */
	public void setFiller(String filler) {
		this.filler = filler;
	}

	/**
	 * @return Returns the rut.
	 */
	public String getRut() {
		return rut;
	}

	/**
	 * @param rut The rut to set.
	 */
	public void setRut(String rut) {
		this.rut = rut;
	}

	/**
	 * @return Returns the tipo_cliente.
	 */
	public String getTipo_cliente() {
		return tipo_cliente;
	}

	/**
	 * @param tipo_cliente The tipo_cliente to set.
	 */
	public void setTipo_cliente(String tipo_cliente) {
		this.tipo_cliente = tipo_cliente;
	}

	
	
}
