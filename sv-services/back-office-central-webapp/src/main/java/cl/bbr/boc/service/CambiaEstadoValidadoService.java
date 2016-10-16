package cl.bbr.boc.service;

import cl.bbr.boc.ctrl.CambiaEstadoValidadoCtrl;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.log.Logging;

public class CambiaEstadoValidadoService {
	Logging logger = new Logging(this);

	public boolean retrocederOPEstadoValidado(long idPedido, String user) throws ServiceException {
		CambiaEstadoValidadoCtrl cevCtrl = new CambiaEstadoValidadoCtrl();
		try {
			return cevCtrl.retrocederOPEstadoValidado(idPedido,user);
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}
	}

}
