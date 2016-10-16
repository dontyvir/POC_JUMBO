package cl.bbr.boc.ctrl;

import java.util.List;

import cl.bbr.boc.dao.CargarColaboradoresDAO;
import cl.bbr.boc.dao.DAOFactoryParametros;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;

public class CargarColaboradoresCtrl {
	
	Logging logger = new Logging(this);

	public boolean truncateTableColaboradores() throws DAOException{
		CargarColaboradoresDAO ccDAO = (CargarColaboradoresDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getCargarColaboradoresDAO();
		try {
			return ccDAO.truncateTableColaboradres();
		} catch (Exception e) {
			logger.error("Error truncateTableColaboradores: ", e);
			throw new DAOException(e);
		}
	}
	
	public boolean cargarColaboradores(List colaboradores) throws DAOException{
		CargarColaboradoresDAO ccDAO = (CargarColaboradoresDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getCargarColaboradoresDAO();
		try {
			return ccDAO.cargarColaboradores(colaboradores);
		} catch (Exception e) {
			logger.error("Error cargarColaboradores: ", e);
			throw new DAOException(e);
		}
	}
	
	public String cantidadColaboradores() throws DAOException{
		CargarColaboradoresDAO ccDAO = (CargarColaboradoresDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getCargarColaboradoresDAO();
		try {
			return ccDAO.cantidadColaboradores();
		} catch (Exception e) {
			logger.error("Error cargarColaboradores: ", e);
			throw new DAOException(e);
		}
	}
	
}
