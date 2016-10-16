package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

/**
 * Clase que permite eliminar un elemento. 
 * @author BBR
 *
 */
public class ProcDelElementoDTO implements Serializable{
	private final static long serialVersionUID = 1;
	
      private long id_elemento; // obligatorio

	/**
	 * Constructor inicial.
	 */
	public ProcDelElementoDTO() {
	}

	/**
	 * @return Retorna el id_elemento.
	 */
	public long getId_elemento() {
		return id_elemento;
	}

	/**
	 * @param id_elemento , id_elemento a modificar.
	 */
	public void setId_elemento(long id_elemento) {
		this.id_elemento = id_elemento;
	}

	
      
}
