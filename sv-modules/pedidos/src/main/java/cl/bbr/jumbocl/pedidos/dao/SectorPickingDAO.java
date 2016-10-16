package cl.bbr.jumbocl.pedidos.dao;


import cl.bbr.jumbocl.pedidos.dto.SectorLocalDTO;
import cl.bbr.jumbocl.pedidos.exceptions.SectorPickingDAOException;

/**
 * Permite las operaciones en base de datos sobre los Sectores de picking.
 * @author BBR
 *
 */
public interface SectorPickingDAO {

	
	/**
	 * Agrega un sector de picking
	 * 
	 * @param  sector
	 * @throws SectorPickingDAOException
	 */
	public void doAddSectorPicking(SectorLocalDTO sector)
		throws SectorPickingDAOException;	
	
	/**
	 * Actualiza un sector de picking
	 * 
	 * @param  sector
	 * @throws SectorPickingDAOException
	 */
	public void doActualizaSectorPicking(SectorLocalDTO sector)
		throws SectorPickingDAOException;
	
	/**
	 * Elimina un sector de picking
	 * 
	 * @param  id_sector
	 * @throws SectorPickingDAOException
	 */
	public void doEliminaSectorPicking(long id_sector)
	throws SectorPickingDAOException;
	
	
}
