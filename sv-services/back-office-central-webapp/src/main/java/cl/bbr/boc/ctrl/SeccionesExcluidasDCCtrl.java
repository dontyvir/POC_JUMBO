package cl.bbr.boc.ctrl;

import java.util.List;

import cl.bbr.boc.dao.DAOFactoryParametros;
import cl.bbr.boc.dao.jdbc.JdbcSeccionesExcluidasDCDAO;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * 
 * @author JoLazoGu
 * @version 1
 */
public class SeccionesExcluidasDCCtrl {

	Logging logger = new Logging(this);

	/**
	 * @return Lista de Secciones
	 * @throws DAOException
	 */
	public List getSecciones() throws DAOException {
		JdbcSeccionesExcluidasDCDAO seDAO = (JdbcSeccionesExcluidasDCDAO) DAOFactoryParametros
				.getDAOFactory(DAOFactoryParametros.JDBC).getSeccionesExluidasDCDAO();
		try {
			return seDAO.getSecciones();
		} catch (Exception e) {
			logger.error("Error getSecciones: ", e);
			throw new DAOException(e);
		}
	}

	/**
	 * @return Lista de Secciones Excluidas
	 * @throws DAOException
	 */
	public List getSeccionesExcluidas() throws DAOException {
		JdbcSeccionesExcluidasDCDAO seDAO = (JdbcSeccionesExcluidasDCDAO) DAOFactoryParametros
				.getDAOFactory(DAOFactoryParametros.JDBC).getSeccionesExluidasDCDAO();
		try {
			return seDAO.getSeccionesExcluidas();
		} catch (Exception e) {
			logger.error("Error getSecciones: ", e);
			throw new DAOException(e);
		}
	}

	/**
	 * @param id_seccion
	 * @throws DAOException
	 */
	public void excluirSeccion(int id_seccion) throws DAOException {
		JdbcSeccionesExcluidasDCDAO seDAO = (JdbcSeccionesExcluidasDCDAO) DAOFactoryParametros
				.getDAOFactory(DAOFactoryParametros.JDBC).getSeccionesExluidasDCDAO();
		try {
			seDAO.excluirSeccion(id_seccion);
		} catch (Exception e) {
			logger.error("Error getSecciones: ", e);
			throw new DAOException(e);
		}
	}

	/**
	 * @param id_seccion
	 * @throws DAOException
	 */
	public void permitirSeccion(int id_seccion) throws DAOException {
		JdbcSeccionesExcluidasDCDAO seDAO = (JdbcSeccionesExcluidasDCDAO) DAOFactoryParametros
				.getDAOFactory(DAOFactoryParametros.JDBC).getSeccionesExluidasDCDAO();
		try {
			seDAO.permitirSeccion(id_seccion);
		} catch (Exception e) {
			logger.error("Error getSecciones: ", e);
			throw new DAOException(e);
		}
	}

}
