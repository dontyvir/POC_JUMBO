package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

/**
 * Clase que permite eliminar una categoría Web. 
 * @author BBR
 *
 */
public class ProcDelCatWebDTO implements Serializable{
	private final static long serialVersionUID = 1;
	
      private long id_categoria; // obligatorio

	/**
	 * Constructor inicial.
	 */
	public ProcDelCatWebDTO() {
	}

	/**
	 * @param id_categoria
	 */
	public ProcDelCatWebDTO(long id_categoria) {
		super();
		this.id_categoria = id_categoria;
	}

	/**
	 * @return Retorna el id_categoria.
	 */
	public long getId_categoria() {
		return id_categoria;
	}

	/**
	 * @param id_categoria , id_categoria a modificar.
	 */
	public void setId_categoria(long id_categoria) {
		this.id_categoria = id_categoria;
	}

	
      
}
