package cl.bbr.boc.ctrl;

import cl.bbr.boc.dao.DAOFactoryParametros;
import cl.bbr.boc.dao.ParametrosEditablesDCDAO;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;

public class ParametrosEditablesDCCtrl {

	Logging logger = new Logging(this);
	
	public String getMontoLimite() throws DAOException{
		ParametrosEditablesDCDAO peDAO = (ParametrosEditablesDCDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getParametrosEditablesDCDAO();
		try {
			return peDAO.getMontoLimite();
		} catch (Exception e) {
			logger.error("Error getMontoLimite: ", e);
			throw new DAOException(e);
		}
	}
	
	public String getDescuentoMayor() throws DAOException{
		ParametrosEditablesDCDAO peDAO = (ParametrosEditablesDCDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getParametrosEditablesDCDAO();
		try {
			return peDAO.getDescuentoMayor();
		} catch (Exception e) {
			logger.error("Error getDescuentoMayor: ", e);
			throw new DAOException(e);
		}
	}
	
	public String getDescuentoMenor() throws DAOException{
		ParametrosEditablesDCDAO peDAO = (ParametrosEditablesDCDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getParametrosEditablesDCDAO();
		try {
			return peDAO.getDescuentoMenor();
		} catch (Exception e) {
			logger.error("Error getDescuentoMenor: ", e);
			throw new DAOException(e);
		}
	}
	
	public void setMontoLimite(String monto) throws DAOException{
		ParametrosEditablesDCDAO peDAO = (ParametrosEditablesDCDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getParametrosEditablesDCDAO();
		try {
			peDAO.setMontoLimite(monto);
		} catch (Exception e) {
			logger.error("Error setMontoLimite: ", e);
			throw new DAOException(e);
		}
	}
	
	public void setDescuentoMayor(String valor) throws DAOException{
		ParametrosEditablesDCDAO peDAO = (ParametrosEditablesDCDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getParametrosEditablesDCDAO();
		try {
			peDAO.setDescuentoMayor(valor);
		} catch (Exception e) {
			logger.error("Error setDescuentoMayor: ", e);
			throw new DAOException(e);
		}
	}
	
	public void setDescuentoMenor(String valor) throws DAOException{
		ParametrosEditablesDCDAO peDAO = (ParametrosEditablesDCDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getParametrosEditablesDCDAO();
		try {
			peDAO.setDescuentoMenor(valor);
		} catch (Exception e) {
			logger.error("Error setDescuentoMenor: ", e);
			throw new DAOException(e);
		}
	}
}
