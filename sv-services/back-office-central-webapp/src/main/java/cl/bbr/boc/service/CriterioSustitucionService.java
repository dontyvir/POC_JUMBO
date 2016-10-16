package cl.bbr.boc.service;

import cl.bbr.boc.ctrl.CriterioSustitucionCtrl;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.log.Logging;

public class CriterioSustitucionService {
	Logging logger = new Logging(this);
	
	public int noSustituir(long id_pedido) throws ServiceException {
		CriterioSustitucionCtrl csCtrl = new CriterioSustitucionCtrl();
		try {
			return csCtrl.noSustituir(id_pedido);
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}
	}
	
	public void registrarTracking(long id_pedido, String usuario) throws ServiceException {
		CriterioSustitucionCtrl csCtrl = new CriterioSustitucionCtrl();
		try {
			csCtrl.registrarTracking(id_pedido, usuario);
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}
	}
}
