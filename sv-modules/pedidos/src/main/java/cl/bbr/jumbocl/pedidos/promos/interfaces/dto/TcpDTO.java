package cl.bbr.jumbocl.pedidos.promos.interfaces.dto;

import cl.bbr.jumbocl.common.utils.Formatos;

public class TcpDTO {
	public static int TCP_LEN =4;
	public static int CANTIDAD_LEN =4;
	
	private String nro_tcp;
	private int cantidad; //a utilizar o utilizada depende de la consulta

	public TcpDTO() {
		
	}

	
	public String toMsg(){
		String out = "";
		out += Formatos.formatField(nro_tcp, 	TCP_LEN, 		Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(""+cantidad, 	CANTIDAD_LEN, 	Formatos.ALIGN_RIGHT,"0");				
		return out;
	}
	
	/**
	 * @return Returns the nro_tcp.
	 */
	public String getNro_tcp() {
		return nro_tcp;
	}

	/**
	 * @param nro_tcp The nro_tcp to set.
	 */
	public void setNro_tcp(String nro_tcp) {
		this.nro_tcp = nro_tcp;
	}

	/**
	 * @return Returns the cantidad.
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}


}
