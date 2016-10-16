package cl.bbr.jumbocl.pedidos.dao;

import java.util.List;

import cl.bbr.jumbocl.pedidos.exceptions.LocalDAOException;

/**
 * Permite las operaciones en base de datos sobre los Locales.
 * 
 * @author BBR
 *
 */
public interface LocalDAO {

	/**
	 * Retorna listado de locales
	 * 
	 * @return List LocalDTO
	 * @throws LocalDAOException
	 */
	public List getLocalesAll()
		throws LocalDAOException;
	
	/**
	 * Retorna un listado de Sectores de Picking de un Local
	 * 
	 * @return List SectorLocalDTO
	 * @throws LocalDAOException
	 */
	public List getSectores()
		throws LocalDAOException;

	/**
	 * Obtiene listado de comunas de un local
	 * 
	 * @param  id_local
	 * @return List ComunaDTO
	 * @throws LocalDAOException
	 */
	public List getComunasLocal(long id_local)
		throws LocalDAOException;
	
	
	
	
}
