/*
 * Created on 23-abr-2010
 */
package cl.bbr.fo.service;

import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.jumbo.common.dao.CategoriasDAO;
import cl.jumbo.common.dao.DAOFactory;
import cl.jumbo.common.dao.DAOFactoryException;
import cl.jumbo.common.dto.CategoriaBannerDTO;
import cl.jumbo.common.dto.CategoriaMasvDTO;

/**
 * @author jdroguett
 */
public class CategoriasService {
   /**
    * @param categoriaId
    * @return
    * @throws ServiceException
    */
   public CategoriaMasvDTO getMasvCategoria(int categoriaId) throws ServiceException {
      try {
         CategoriasDAO categoriasDAO = (CategoriasDAO) DAOFactory.getInstanceDAO(CategoriasDAO.class.getName());
         return categoriasDAO.getMasvCategoria(categoriaId);
      } catch (DAOFactoryException e) {
         
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }
   
   public CategoriaBannerDTO getBannersCategoria(int categoriaId) throws ServiceException {
       try {
          CategoriasDAO categoriasDAO = (CategoriasDAO) DAOFactory.getInstanceDAO(CategoriasDAO.class.getName());
          return categoriasDAO.getBannersCategoria(categoriaId);
       } catch (DAOFactoryException e) {
          
          throw new ServiceException(e.getMessage());
       } catch (DAOException e) {
          throw new ServiceException(e.getMessage());
       }
    }
}
