package cl.bbr.boc.service;

import cl.bbr.boc.ctrl.ParametrosEditablesDCCtrl;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.log.Logging;

public class ParametrosEditablesDCService {
	
	Logging logger = new Logging(this);

	public String getMontoLimite() throws ServiceException {
		ParametrosEditablesDCCtrl peCtrl = new ParametrosEditablesDCCtrl();
		try {
			return peCtrl.getMontoLimite();
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}
	}
	
	public String getDescuentoMayor() throws ServiceException {
		ParametrosEditablesDCCtrl peCtrl = new ParametrosEditablesDCCtrl();
		try {
			return peCtrl.getDescuentoMayor();
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}
	}
	
	public String getDescuentoMenor() throws ServiceException {
		ParametrosEditablesDCCtrl peCtrl = new ParametrosEditablesDCCtrl();
		try {
			return peCtrl.getDescuentoMenor();
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}
	}
	
	public void setMontoLimite(String monto) throws ServiceException {
		ParametrosEditablesDCCtrl peCtrl = new ParametrosEditablesDCCtrl();
		try {
			peCtrl.setMontoLimite(monto);
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}
	}
	
	public void setDescuentoMayor(String valor) throws ServiceException {
		ParametrosEditablesDCCtrl peCtrl = new ParametrosEditablesDCCtrl();
		try {
			peCtrl.setDescuentoMayor(valor);
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}
	}
	
	public void setDescuentoMenor(String valor) throws ServiceException {
		ParametrosEditablesDCCtrl peCtrl = new ParametrosEditablesDCCtrl();
		try {
			peCtrl.setDescuentoMenor(valor);
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}
	}	
}
