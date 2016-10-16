package cl.bbr.boc.ctrl;

import cl.bbr.boc.dao.CriterioSustitucionDAO;
import cl.bbr.boc.dao.DAOFactoryParametros;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;

public class CriterioSustitucionCtrl {
	
	Logging logger = new Logging(this);
	
	public int noSustituir(long id_pedido) throws DAOException{
		CriterioSustitucionDAO cs = (CriterioSustitucionDAO)DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getCriterioSustitucionDAO();
		try{
			return cs.noSustituir(id_pedido);
		} catch (Exception e) {
			logger.error("Error noSustituir: ", e);
			throw new DAOException(e);
		}
	}
	
	public void registrarTracking(long id_pedido, String usuario)throws DAOException{
		CriterioSustitucionDAO cs = (CriterioSustitucionDAO)DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getCriterioSustitucionDAO();
		try{
			cs.registrarTracking(id_pedido, usuario);
		} catch (Exception e) {
			logger.error("Error registrarTracking: ", e);
			throw new DAOException(e);
		}	
	}

}
