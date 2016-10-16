package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

public class ProcValidAutoDTO implements Serializable{
      private long id_pedido; // obligatorio

	/**
	 * 
	 */
	public ProcValidAutoDTO() {
	}

	/**
	 * @param id_pedido
	 */
	public ProcValidAutoDTO(long id_pedido) {
		this.id_pedido = id_pedido;
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
    
}
