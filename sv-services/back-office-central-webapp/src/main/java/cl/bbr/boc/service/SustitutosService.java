/*
 * Created on 13-may-2009
 */
package cl.bbr.boc.service;

import java.util.List;

import cl.bbr.boc.dao.SustitutosDAO;
import cl.bbr.boc.dto.SustitutoDTO;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.jumbo.common.dao.DAOFactory;
import cl.jumbo.common.dao.DAOFactoryException;

/**
 * @author jdroguett
 */
public class SustitutosService {
   Logging logger = new Logging(this);

   public List detallePedido(int pedidoId) throws ServiceException {
      try {
         SustitutosDAO sustitutosDAO = (SustitutosDAO) DAOFactory.getInstanceDAO(SustitutosDAO.class.getName());
         return sustitutosDAO.detallePedido(pedidoId);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }

   /**
    * @param localId
    * @param barra
    * @return
    * @throws ServiceException
    */
   public SustitutoDTO sustituto(String localId, String barra) throws ServiceException {
      try {
         SustitutosDAO sustitutosDAO = (SustitutosDAO) DAOFactory.getInstanceDAO(SustitutosDAO.class.getName());
         return sustitutosDAO.sustituto(localId, barra);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }

   /**
    * @param sustituto
    * @throws ServiceException
    */
   public int updateSustituto(SustitutoDTO sustituto) throws ServiceException {
      try {
         SustitutosDAO sustitutosDAO = (SustitutosDAO) DAOFactory.getInstanceDAO(SustitutosDAO.class.getName());
         return sustitutosDAO.updateSustituto(sustituto);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e);
      } catch (DAOException e) {
         throw new ServiceException(e);
      }
   }

}
