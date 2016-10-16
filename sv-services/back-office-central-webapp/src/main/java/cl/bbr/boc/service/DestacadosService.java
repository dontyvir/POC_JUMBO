/*
 * Created on 29-ene-2009
 *
 */
package cl.bbr.boc.service;

import java.util.List;

import cl.bbr.boc.dao.DestacadosDAO;
import cl.bbr.boc.dto.DestacadoDTO;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.jumbo.common.dao.DAOFactory;
import cl.jumbo.common.dao.DAOFactoryException;

/**
 * @author jdroguett
 *  
 */
public class DestacadosService {
   Logging logger = new Logging(this);

   /**
    * @throws ServiceException
    * @throws
    *  
    */
   public List getDestacados() throws ServiceException {
      try {
         DestacadosDAO destacadosDAO = (DestacadosDAO) DAOFactory.getInstanceDAO(DestacadosDAO.class.getName());
         return destacadosDAO.getDestacados();
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }

   /**
    *  
    */
   public void addDestacado(DestacadoDTO destacadoDTO) throws ServiceException {
      try {
         DestacadosDAO destacadosDAO = (DestacadosDAO) DAOFactory.getInstanceDAO(DestacadosDAO.class.getName());
         destacadosDAO.addDestacado(destacadoDTO);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e);
      } catch (DAOException e) {
         throw new ServiceException(e);
      }
   }

   /**
    * @param destacadoDTO
    */
   public void updDestacado(DestacadoDTO destacadoDTO) throws ServiceException {
      try {
         DestacadosDAO destacadosDAO = (DestacadosDAO) DAOFactory.getInstanceDAO(DestacadosDAO.class.getName());
         destacadosDAO.updDestacado(destacadoDTO);
      } catch (DAOFactoryException e) {
         throw new ServiceException(e);
      } catch (DAOException e) {
         throw new ServiceException(e);
      }
   }

   /**
    * @param id
    */
   public void delDestacado(int id) throws ServiceException {
      try {
         DestacadosDAO destacadosDAO = (DestacadosDAO) DAOFactory.getInstanceDAO(DestacadosDAO.class.getName());
         destacadosDAO.delDestacado(id);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }

   /**
    * @param id
    * @return
    */
   public DestacadoDTO getDestacado(int id) throws ServiceException {
      try {
         DestacadosDAO destacadosDAO = (DestacadosDAO) DAOFactory.getInstanceDAO(DestacadosDAO.class.getName());
         return destacadosDAO.getDestacado(id);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }

   /**
    * @param id
    * @return
    */
   public List getProductosDestacados(int id) throws ServiceException {
      try {
         DestacadosDAO destacadosDAO = (DestacadosDAO) DAOFactory.getInstanceDAO(DestacadosDAO.class.getName());
         return destacadosDAO.getProductosDestacados(id);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }

   /**
    * @param id
    * @return
    */
   public List getDestacadoLocales(int id) throws ServiceException {
      try {
         DestacadosDAO destacadosDAO = (DestacadosDAO) DAOFactory.getInstanceDAO(DestacadosDAO.class.getName());
         return destacadosDAO.getDestacadoLocales(id);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }

}
