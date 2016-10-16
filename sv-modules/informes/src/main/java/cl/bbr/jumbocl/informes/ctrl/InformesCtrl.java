package cl.bbr.jumbocl.informes.ctrl;

import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.informes.dao.DAOFactory;
import cl.bbr.jumbocl.informes.dao.JdbcInformesDAO;
import cl.bbr.jumbocl.informes.dto.MailSustitutosFaltantesDTO;
import cl.bbr.jumbocl.informes.exceptions.InformesDAOException;
import cl.bbr.jumbocl.informes.exceptions.InformesException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * Entrega metodos de navegacion por gestion de casos
 * 
 * @author imoyano
 *  
 */
public class InformesCtrl {

    /**
     * Permite generar los eventos en un archivo log.
     */
    Logging logger = new Logging(this);

    /**
     * @return
     */
    public List getMailsSyF() throws InformesException {
        JdbcInformesDAO dao = (JdbcInformesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getInformesDAO();
        try {
            return dao.getMailsSyF();
        } catch (InformesDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new InformesException(ex);
        }
    }

    /**
     * @param m
     * @return
     */
    public long addMailSyF(MailSustitutosFaltantesDTO m) throws InformesException {
        JdbcInformesDAO dao = (JdbcInformesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getInformesDAO();
        try {
            return dao.addMailSyF(m);
        } catch (InformesDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new InformesException(ex);
        }
    }

    /**
     * @param m
     */
    public void modMailSyF(MailSustitutosFaltantesDTO m) throws InformesException {
        JdbcInformesDAO dao = (JdbcInformesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getInformesDAO();
        try {
            dao.modMailSyF(m);
        } catch (InformesDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new InformesException(ex);
        }
    }

    /**
     * @param idMail
     * @return
     */
    public MailSustitutosFaltantesDTO getMailSyFById(long idMail) throws InformesException {
        JdbcInformesDAO dao = (JdbcInformesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getInformesDAO();
        try {
            return dao.getMailSyFById(idMail);
        } catch (InformesDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new InformesException(ex);
        }
    }

    /**
     * @param idMail
     */
    public void delMailSyFById(long idMail) throws InformesException {
        JdbcInformesDAO dao = (JdbcInformesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getInformesDAO();
        try {
            dao.delMailSyFById(idMail);
        } catch (InformesDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new InformesException(ex);
        }
    }

    /**
     * @return
     */
    public List getOriginalesYSustitutos() throws InformesException {
        JdbcInformesDAO dao = (JdbcInformesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getInformesDAO();
        try {
            return dao.getOriginalesYSustitutos();
        } catch (InformesDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new InformesException(ex);
        }
    }

    /**
     * @param sustitutos
     * @return
     */
    public String addCodigosSyF(ArrayList sustitutos) throws InformesException {
        JdbcInformesDAO dao = (JdbcInformesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getInformesDAO();
        try {
            return dao.addCodigosSyF(sustitutos);
        } catch (InformesDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new InformesException(ex);
        }
    }
   

}
