package cl.bbr.jumbocl.pedidos.promos.dto;

public class CuponPedidoDTO {
	private long id_cupon;
	private long id_pedido;
	private String nro_cupon;
	private long id_tcp;
	private int nro_tcp;
	private int cant_max;
	
	public CuponPedidoDTO() {		
	}

	/**
	 * @return Returns the id_cupon.
	 */
	public long getId_cupon() {
		return id_cupon;
	}

	/**
	 * @param id_cupon The id_cupon to set.
	 */
	public void setId_cupon(long id_cupon) {
		this.id_cupon = id_cupon;
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
	 * @return Returns the nro_cupon.
	 */
	public String getNro_cupon() {
		return nro_cupon;
	}

	/**
	 * @param nro_cupon The nro_cupon to set.
	 */
	public void setNro_cupon(String nro_cupon) {
		this.nro_cupon = nro_cupon;
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
