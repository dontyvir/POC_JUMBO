/*
 * Created on 15-abr-2010
 *
 */
package cl.bbr.boc.service;

import java.util.List;

import cl.bbr.boc.dao.LocalesDAO;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.jumbo.common.dao.DAOFactory;
import cl.jumbo.common.dao.DAOFactoryException;

/**
 * @author jdroguett
 */
public class LocalesService {
   /**
    * @throws ServiceException
    * @throws
    *  
    */
   public List getLocales() throws ServiceException {
      try {
         LocalesDAO localesDAO  = (LocalesDAO) DAOFactory.getInstanceDAO(LocalesDAO.class.getName());
         return localesDAO .getLocales();
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }
}
