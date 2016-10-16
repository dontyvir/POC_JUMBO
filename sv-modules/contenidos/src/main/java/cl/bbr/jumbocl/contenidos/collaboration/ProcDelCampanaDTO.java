package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

/**
 * Clase que permite eliminar una campaña. 
 * @author BBR
 *
 */
public class ProcDelCampanaDTO implements Serializable{
	private final static long serialVersionUID = 1;
	
      private long id_campana; // obligatorio

	/**
	 * Constructor inicial.
	 */
	public ProcDelCampanaDTO() {
	}

	/**
	 * @return Retorna el id_campana.
	 */
	public long getId_campana() {
		return id_campana;
	}

	/**
	 * @param id_campana , id_campana a modificar.
	 */
	public void setId_campana(long id_campana) {
		this.id_campana = id_campana;
	}

	
      
}
