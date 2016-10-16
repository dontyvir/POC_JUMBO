package cl.bbr.boc.ctrl;

import java.util.List;

import cl.bbr.boc.dao.DAOFactoryParametros;
import cl.bbr.boc.dao.jdbc.JdbcCuponesDCDAO;
import cl.bbr.jumbocl.common.model.CuponEntity;
import cl.bbr.jumbocl.common.model.CuponPorProducto;
import cl.bbr.jumbocl.common.model.CuponPorRut;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * 
 * @author JoLazoGu
 * @version 1
 */
public class CuponesDCCtrl {

	Logging logger = new Logging(this);

	/**
	 * @return Lista de Secciones
	 * @throws DAOException
	 */
	public List getListaTiposCupones() throws DAOException {
		JdbcCuponesDCDAO seDAO = (JdbcCuponesDCDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getListaTiposCupones();
		
		try {
			return seDAO.getListaTiposCupones();
		} catch (Exception e) {
			logger.error("Error getSecciones: ", e);
			throw new DAOException(e);
		}
	}

	/**
	 * @return Lista de Secciones
	 * @throws DAOException
	 */
	public CuponEntity getDatoCuponPorId(CuponEntity cuponId) throws DAOException {
		JdbcCuponesDCDAO seDAO = (JdbcCuponesDCDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getDatoCuponPorId();
		
		try {
			return seDAO.getDatoCuponPorId(cuponId);
		} catch (Exception e) {
			logger.error("Error getSecciones: ", e);
			throw new DAOException(e);
		}
	}

	/**
	 * @return Lista de Secciones
	 * @throws DAOException
	 */
	public CuponEntity getDatoCuponPorCodigo(CuponEntity cupon) throws DAOException {
		JdbcCuponesDCDAO seDAO = (JdbcCuponesDCDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getDatoCuponPorCodigo();
		
		try {
			return seDAO.getDatoCuponPorCodigo(cupon);
		} catch (Exception e) {
			logger.error("Error getSecciones: ", e);
			throw new DAOException(e);
		}
	}

	/**
	 * @param id_seccion
	 * @throws DAOException
	 */
	public boolean setGuardarCupon(CuponEntity cupon) throws DAOException {
		JdbcCuponesDCDAO seDAO = (JdbcCuponesDCDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).setGuardarCupon();
		
		try {

			if (!seDAO.getExisteCupon(cupon) && seDAO.setGuardarCupon(cupon)) {
				return true;
			} else if (cupon.getId_cup_dto() != 0 && seDAO.setActualizarCupon(cupon)) {
				return true;
			}else {
				return false;
			}

		} catch (Exception e) {
			logger.error("Error getSecciones: ", e);
			throw new DAOException(e);
		}
	}

	/**
	 * @return Lista de Secciones
	 * @throws DAOException
	 */
	public List getListaRubros() throws DAOException {
		JdbcCuponesDCDAO seDAO = (JdbcCuponesDCDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getListaRubros();
		
		try {
			return seDAO.getListaRubros();
		} catch (Exception e) {
			logger.error("Error getSecciones: ", e);
			throw new DAOException(e);
		}
	}

	/**
	 * @return Lista de Secciones
	 * @throws DAOException
	 */
	public List getListaSecciones() throws DAOException {
		JdbcCuponesDCDAO seDAO = (JdbcCuponesDCDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getListaSecciones();
		
		try {
			return seDAO.getListaSecciones();
		} catch (Exception e) {
			logger.error("Error getSecciones: ", e);
			throw new DAOException(e);
		}
	}

	/**
	 * @param id_seccion
	 * @throws DAOException
	 */
	public boolean setCargaRutMasiva(CuponPorRut cpr, long id_usuario) throws DAOException {
		JdbcCuponesDCDAO seDAO = (JdbcCuponesDCDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).setCargaRutMasiva();
		
		try {

			if (seDAO.setCargaRutMasiva(cpr, id_usuario))
				return true;
			else
				return false;

		} catch (Exception e) {
			logger.error("Error getSecciones: ", e);
			throw new DAOException(e);
		}
	}

	/**
	 * @param id_seccion
	 * @throws DAOException
	 */
	public void setTodasLasSeccionesAsociado(CuponEntity ce) throws DAOException {
		JdbcCuponesDCDAO seDAO = (JdbcCuponesDCDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).setTodasLasSeccionesAsociado();
		
		try {
			seDAO.setTodasLasSeccionesAsociado(ce);
		} catch (Exception e) {
			logger.error("Error getSecciones: ", e);
			throw new DAOException(e);
		}
	}

	/**
	 * @param id_seccion
	 * @throws DAOException
	 */
	public int getCantidadRutAsociado(int id_cup_dto) throws DAOException {
		JdbcCuponesDCDAO seDAO = (JdbcCuponesDCDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getCantidadRutAsociado();
		
		try {
			return seDAO.getCantidadRutAsociado(id_cup_dto);
		} catch (Exception e) {
			logger.error("Error getSecciones: ", e);
			throw new DAOException(e);
		}
	}

	/**
	 * @param id_seccion
	 * @throws DAOException
	 */
	public void setCuponAsociarTipo(CuponPorProducto cpp, String rad_tipo, long id_usuario, String rad_despacho) throws DAOException {
		JdbcCuponesDCDAO seDAO = (JdbcCuponesDCDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).setCuponAsociarTipo();
		
		try {
			seDAO.setCuponAsociarTipo(cpp, rad_tipo, id_usuario, rad_despacho);
		} catch (Exception e) {
			logger.error("Error getSecciones: ", e);
			throw new DAOException(e);
		}
	}

	/**
	 * @return Lista de Secciones
	 * @throws DAOException
	 */
	public List getListaCuponAsociado(int id_cup_dto) throws DAOException {
		JdbcCuponesDCDAO seDAO = (JdbcCuponesDCDAO) DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getListaCuponAsociado();
		
		try {
			return seDAO.getListaCuponAsociado(id_cup_dto);
		} catch (Exception e) {
			logger.error("Error getSecciones: ", e);
			throw new DAOException(e);
		}
	}

}
