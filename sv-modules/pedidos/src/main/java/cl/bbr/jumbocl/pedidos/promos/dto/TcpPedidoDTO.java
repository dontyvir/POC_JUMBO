package cl.bbr.jumbocl.pedidos.promos.dto;

import java.io.Serializable;


public class TcpPedidoDTO implements Serializable{
	private long id_tcp;
	private long id_pedido;
	private int nro_tcp;
	private int cant_max;
	private int cant_util;

	public TcpPedidoDTO() {
	}

	/**
	 * @return Returns the cant_max.
	 */
	public int getCant_max() {
		return cant_max;
	}

	/**
	 * @param cant_max The cant_max to set.
	 */
	public void setCant_max(int cant_max) {
		this.cant_max = cant_max;
	}

	/**
	 * @return Returns the cant_util.
	 */
	public int getCant_util() {
		return cant_util;
	}

	/**
	 * @param cant_util The cant_util to set.
	 */
	public void setCant_util(int cant_util) {
		this.cant_util = cant_util;
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
	 * @return Returns the id_tcp.
	 */
	public long getId_tcp() {
		return id_tcp;
	}

	/**
	 * @param id_tcp The id_tcp to set.
	 */
	public void setId_tcp(long id_tcp) {
		this.id_tcp = id_tcp;
	}

	/**
	 * @return Returns the nro_tcp.
	 */
	public int getNro_tcp() {
		return nro_tcp;
	}

	/**
	 * @param nro_tcp The nro_tcp to set.
	 */
	public void setNro_tcp(int nro_tcp) {
		this.nro_tcp = nro_tcp;
	}
	
	
	
	
}
