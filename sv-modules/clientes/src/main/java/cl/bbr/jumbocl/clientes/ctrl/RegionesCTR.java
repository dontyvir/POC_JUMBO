package cl.bbr.jumbocl.clientes.ctrl;

import java.util.List;

import cl.bbr.jumbocl.clientes.dao.DAOFactory;
import cl.bbr.jumbocl.clientes.dao.JdbcRegionesDAO;
import cl.bbr.jumbocl.clientes.dto.ComunaDTO;
import cl.bbr.jumbocl.clientes.dto.ComunasDTO;
import cl.bbr.jumbocl.clientes.dto.RegionesDTO;
import cl.bbr.jumbocl.clientes.exceptions.RegionesDAOException;
import cl.bbr.jumbocl.clientes.exceptions.RegionesException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * <p>Clase para que controla el flujo de datos desde el reposotorio de datos DAO a la aplicación. 
 *  
 * @author BBR ecommerce & retail
 *
 */
public class RegionesCTR {

	/**
	 * Instancia para log4j
	 */
	Logging logger = new Logging(this);
	
	/**
	 * Constructor
	 *
	 */
	public RegionesCTR() {
		this.logger.debug("New RegionesCTR");
	}

	/**
	 * Recupera la lista de Regiones y la retorna como una lista de DTO (RegionesDTO).
	 * 
	 * @return	Lista de DTO (RegionesDTO)
	 * @throws RegionesException
	 */
	public List getRegiones() throws RegionesException {
		try {
			JdbcRegionesDAO datosDAO = (JdbcRegionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRegionesDAO();
			return datosDAO.getRegiones();

		} catch (RegionesDAOException ex) {
			logger.error("Problema", ex);
			throw new RegionesException(ex);
		}

	}

	/**
	 * Recupera la Region a partir de una comuna  y la retorna como un objeto RegionesDTO.
	 * 
	 * @param id_comuna	identificador único de la comuna
	 * @return RegionesDTO
	 * @throws RegionesException
	 */
	public RegionesDTO getRegion(long id_comuna) throws RegionesException {
		try {
			JdbcRegionesDAO datosDAO = (JdbcRegionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRegionesDAO();
			return datosDAO.getRegion(id_comuna);			
		} catch (RegionesDAOException ex) {
			logger.error("Problema", ex);
			throw new RegionesException(ex);
		}
	}
	
	
	
	/**
	 * Retorna como una lista de ComunaDTO.
	 * 
	 * @param reg_id	identificador único de la region
	 * @return	Lista de DTO (ComunaDTO)
	 * @throws RegionesException
	 */
	public List getComunas( long reg_id ) throws RegionesException {
		try {
			JdbcRegionesDAO datosDAO = (JdbcRegionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRegionesDAO();
			return datosDAO.getComunas( reg_id );
		} catch (RegionesDAOException ex) {
			logger.error("Problema", ex);
			throw new RegionesException(ex);
		}
	}

    /**
     * @param idRegion
     * @return
     */
    public RegionesDTO getRegionById(int idRegion) throws RegionesException {
        try {
            JdbcRegionesDAO datosDAO = (JdbcRegionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRegionesDAO();
            return datosDAO.getRegionById(idRegion);           
        } catch (RegionesDAOException ex) {
            logger.error("Problema", ex);
            throw new RegionesException(ex);
        }
    }

    /**
     * @return
     */
    public List regionesConCobertura() throws RegionesException {
        try {
            JdbcRegionesDAO datosDAO = (JdbcRegionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRegionesDAO();
            return datosDAO.regionesConCobertura();           
        } catch (RegionesDAOException ex) {
            logger.error("Problema", ex);
            throw new RegionesException(ex);
        }
    }

    /**
     * @param idRegion
     * @return
     */
    public List comunasConCoberturaByRegion(long idRegion) throws RegionesException {
        try {
            JdbcRegionesDAO datosDAO = (JdbcRegionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRegionesDAO();
            return datosDAO.comunasConCoberturaByRegion(idRegion);           
        } catch (RegionesDAOException ex) {
            logger.error("Problema", ex);
            throw new RegionesException(ex);
        }
    }

    /**
     * @param idComuna
     * @return
     */
    public ComunaDTO getComunaConLocal(long idComuna) throws RegionesException {
        try {
            JdbcRegionesDAO datosDAO = (JdbcRegionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRegionesDAO();
            return datosDAO.getComunaConLocal(idComuna);           
        } catch (RegionesDAOException ex) {
            logger.error("Problema", ex);
            throw new RegionesException(ex);
        }
    }
    
    /**
     * @param idComuna
     * @return
     */
    public ComunaDTO getZonaxComuna(long idComuna) throws RegionesException {
        try {
            JdbcRegionesDAO datosDAO = (JdbcRegionesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRegionesDAO();
            return datosDAO.getZonaxComuna(idComuna);           
        } catch (RegionesDAOException ex) {
            logger.error("Problema", ex);
            throw new RegionesException(ex);
        }
    }
	
	
}
