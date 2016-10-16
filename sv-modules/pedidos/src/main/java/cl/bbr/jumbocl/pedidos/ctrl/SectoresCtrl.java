package cl.bbr.jumbocl.pedidos.ctrl;


import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.pedidos.dao.DAOFactory;
import cl.bbr.jumbocl.pedidos.dao.JdbcSectorPickingDAO;
import cl.bbr.jumbocl.pedidos.dto.SectorLocalDTO;
import cl.bbr.jumbocl.pedidos.exceptions.SectorPickingDAOException;
import cl.bbr.jumbocl.shared.log.Logging;

public class SectoresCtrl {
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

	/**
	 * Agrega un sector de picking
	 * 
	 * @param sector
	 * @throws SectorPickingDAOException
	 * @throws SystemException
	 */
	public void doAddSectorPicking(SectorLocalDTO sector)
		throws SectorPickingDAOException, SystemException{
		logger.debug("en ctrl: doAddSectorPicking");
		JdbcSectorPickingDAO dao = (JdbcSectorPickingDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getSectorPickingDAO();
		try{

			dao.doAddSectorPicking(sector);

		}catch(SectorPickingDAOException ex){
			logger.debug("en el catch");
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new SectorPickingDAOException(Constantes._EX_LOCAL_ID_INVALIDO);
			}
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new SectorPickingDAOException(Constantes._EX_ID_SECTOR_INVALIDO);
			}
			else
				throw new SystemException("Error no controlado al insertar un sector de picking",ex);		
		}catch(Exception ex){
			logger.debug("error no controlado al insertar un sector de picking: "+ex.getMessage());
		}			
	}	
	/**
	 * Actualiza un sector de picking
	 *	 
	 * @param sector
	 * @throws SectorPickingDAOException
	 * @throws SystemException
	 */
	public void doActualizaSectorPicking(SectorLocalDTO sector)
	throws SectorPickingDAOException, SystemException{
			
			JdbcSectorPickingDAO dao = (JdbcSectorPickingDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getSectorPickingDAO();
			try{
				dao.doActualizaSectorPicking(sector);
			}catch(SectorPickingDAOException ex){
				if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
					throw new SectorPickingDAOException(Constantes._EX_ID_SECTOR_INVALIDO);
				}
				else
					throw new SystemException("Error no controlado al insertar un sector de picking",ex);
			}		
	}		
	/**
	 * Elimina un sector de picking
	 * 
	 * @param  id_sector
	 * @throws SectorPickingDAOException
	 */
	public void doEliminaSectorPicking(long id_sector)
	throws SectorPickingDAOException, SystemException{
		
		JdbcSectorPickingDAO dao = (JdbcSectorPickingDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getSectorPickingDAO();
		try{
			dao.doEliminaSectorPicking(id_sector);
		}catch(SectorPickingDAOException ex){
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new SectorPickingDAOException(Constantes._EX_ID_SECTOR_INVALIDO);
			}
			else
				throw new SystemException("Error no controlado al insertar un sector de picking",ex);
		}		
}	
}
