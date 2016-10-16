package cl.bbr.jumbocl.pedidos.promos.dto;

public class PedidoDatosSocketDTO {
	private long id_pedido;
	private int cod_local_pos;
	private long rut;

	public PedidoDatosSocketDTO() {
	}

	/**
	 * @return Returns the cod_local_pos.
	 */
	public int getCod_local_pos() {
		return cod_local_pos;
	}

	/**
	 * @param cod_local_pos The cod_local_pos to set.
	 */
	public void setCod_local_pos(int cod_local_pos) {
		this.cod_local_pos = cod_local_pos;
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
	 * @return Returns the rut.
	 */
	public long getRut() {
		return rut;
	}

	/**
	 * @param rut The rut to set.
	 */
	public void setRut(long rut) {
		this.rut = rut;
	}
	
	

}
