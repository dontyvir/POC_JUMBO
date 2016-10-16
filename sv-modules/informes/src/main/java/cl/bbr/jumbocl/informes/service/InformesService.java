package cl.bbr.jumbocl.informes.service;

import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.informes.ctrl.InformesCtrl;
import cl.bbr.jumbocl.informes.dto.MailSustitutosFaltantesDTO;
import cl.bbr.jumbocl.informes.exceptions.InformesException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * Permite manejar información y operaciones sobre Casos. 
 * 
 * @author imoyano
 *
 */
public class InformesService {

    
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

    /**
     * @return
     */
    public List getMailsSyF() throws ServiceException {		
		InformesCtrl informes = new InformesCtrl();
		try {
			return informes.getMailsSyF();
		} catch (InformesException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param m
     * @return
     */
    public long addMailSyF(MailSustitutosFaltantesDTO m) throws ServiceException {		
		InformesCtrl informes = new InformesCtrl();
		try {
			return informes.addMailSyF(m);
		} catch (InformesException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param m
     */
    public void modMailSyF(MailSustitutosFaltantesDTO m) throws ServiceException {		
		InformesCtrl informes = new InformesCtrl();
		try {
			informes.modMailSyF(m);
		} catch (InformesException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idMail
     * @return
     */
    public MailSustitutosFaltantesDTO getMailSyFById(long idMail) throws ServiceException {		
		InformesCtrl informes = new InformesCtrl();
		try {
			return informes.getMailSyFById(idMail);
		} catch (InformesException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param idMail
     */
    public void delMailSyFById(long idMail) throws ServiceException {		
		InformesCtrl informes = new InformesCtrl();
		try {
			informes.delMailSyFById(idMail);
		} catch (InformesException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @return
     */
    public List getOriginalesYSustitutos() throws ServiceException {		
		InformesCtrl informes = new InformesCtrl();
		try {
			return informes.getOriginalesYSustitutos();
		} catch (InformesException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }

    /**
     * @param sustitutos
     * @return
     */
    public String addCodigosSyF(ArrayList sustitutos) throws ServiceException {		
		InformesCtrl informes = new InformesCtrl();
		try {
			return informes.addCodigosSyF(sustitutos);
		} catch (InformesException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
    }		
	
	
	
	
	
}
