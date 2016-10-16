package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

public class ProcValidBloqClientDTO implements Serializable{
      private long id_cliente; // obligatorio

	/**
	 * 
	 */
	public ProcValidBloqClientDTO() {
	}

	/**
	 * @param id_cliente
	 */
	public ProcValidBloqClientDTO(long id_cliente) {
		this.id_cliente = id_cliente;
	}

	/**
	 * @return Returns the id_cliente.
	 */
	public long getId_cliente() {
		return id_cliente;
	}

	/**
	 * @param id_cliente The id_cliente to set.
	 */
	public void setId_cliente(long id_cliente) {
		this.id_cliente = id_cliente;
	}
    
      
}
