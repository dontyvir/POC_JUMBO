package cl.bbr.jumbocl.pedidos.dto;

public class FormPickOpDTO {

	private long id_pedido;
	private long id_ronda;
	private String estado;
	
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
	 * @return Returns the id_ronda.
	 */
	public long getId_ronda() {
		return id_ronda;
	}
	/**
	 * @param id_ronda The id_ronda to set.
	 */
	public void setId_ronda(long id_ronda) {
		this.id_ronda = id_ronda;
	}
	
}
