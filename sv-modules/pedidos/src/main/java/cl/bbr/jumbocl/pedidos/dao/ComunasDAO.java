package cl.bbr.jumbocl.pedidos.dao;

import java.util.List;

import cl.bbr.jumbocl.pedidos.exceptions.ComunasDAOException;

/**
 * Permite las operaciones en base de datos sobre los Calendarios.
 * @author BBR
 *
 */
public interface ComunasDAO {

	/**
	 * Obtiene listado de comunas
	 * 
	 * @return List ComunaDTO
	 * @throws ComunasDAOException
	 */
	public List getComunasAll()
		throws ComunasDAOException;
	
	/**
	 * Obtiene listado de Zonas de despacho para una comuna
	 * 
	 * @param  id_comuna
	 * @return List ZonaxComunaDTO
	 * @throws ComunasDAOException
	 */
	public List getZonasxComuna(long id_comuna)
		throws ComunasDAOException;	
	
	/**
	 * Actualiza el orden de una relación comuna-zona
	 * 
	 * @param  id_zona
	 * @param  id_comuna
	 * @param  orden
	 * @throws ComunasDAOException
	 */
	/*public void doActualizaOrdenZonaxComuna(long id_zona, long id_comuna, int orden)
		throws ComunasDAOException;
	*/
}
