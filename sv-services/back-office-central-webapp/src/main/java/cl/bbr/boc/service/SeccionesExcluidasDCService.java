package cl.bbr.boc.service;

import java.util.List;

import cl.bbr.boc.ctrl.SeccionesExcluidasDCCtrl;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * @author JoLazoGu
 * @version 1
 */
public class SeccionesExcluidasDCService {

	Logging logger = new Logging(this);

	/**
	 * @return Lista de Secciones
	 * @throws ServiceException
	 */
	public List getSecciones() throws ServiceException {

		SeccionesExcluidasDCCtrl seCtrl = new SeccionesExcluidasDCCtrl();
		List result = null;
		try {
			result = seCtrl.getSecciones();
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}
		return result;
	}

	/**
	 * @return Lista de Secciones Excluidas
	 * @throws ServiceException
	 */
	public List getSeccionesExcluidas() throws ServiceException {

		SeccionesExcluidasDCCtrl seCtrl = new SeccionesExcluidasDCCtrl();
		List result = null;
		try {
			result = seCtrl.getSeccionesExcluidas();
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}
		return result;
	}

	/**
	 * @param id_seccion
	 * @throws ServiceException
	 */
	public void excluirSeccion(int id_seccion) throws ServiceException {

		SeccionesExcluidasDCCtrl seCtrl = new SeccionesExcluidasDCCtrl();

		try {
			seCtrl.excluirSeccion(id_seccion);
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}

	}

	/**
	 * @param id_seccion
	 * @throws ServiceException
	 */
	public void permitirSeccion(int id_seccion) throws ServiceException {

		SeccionesExcluidasDCCtrl seCtrl = new SeccionesExcluidasDCCtrl();

		try {
			seCtrl.permitirSeccion(id_seccion);
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}

	}
}
