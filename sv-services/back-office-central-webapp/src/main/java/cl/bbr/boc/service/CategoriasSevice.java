/*
 * Created on 23-abr-2009
 */
package cl.bbr.boc.service;

import java.util.List;

import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.jumbo.common.dao.CategoriasDAO;
import cl.jumbo.common.dao.DAOFactory;
import cl.jumbo.common.dao.DAOFactoryException;
import cl.jumbo.common.dto.Categoria;
import cl.jumbo.common.dto.CategoriaMasvDTO;
import cl.jumbo.common.dto.CategoriaBannerDTO;

/**
 * @author jdroguett
 */
public class CategoriasSevice {
   Logging logger = new Logging(this);

   public Categoria root() throws ServiceException {
      try {
         CategoriasDAO categoriasDAO = (CategoriasDAO) DAOFactory.getInstanceDAO(CategoriasDAO.class.getName());
         return categoriasDAO.root();
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }

   /**
    * @return
    * @throws ServiceException
    */
   public List getMasvCategorias() throws ServiceException {
      try {
         CategoriasDAO categoriasDAO = (CategoriasDAO) DAOFactory.getInstanceDAO(CategoriasDAO.class.getName());
         return categoriasDAO.getMasvCategorias();
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }
   
   /**
    * @return
    * @throws ServiceException
    */
   public List getCategoriasParaMiniHome() throws ServiceException {
      try {
         CategoriasDAO categoriasDAO = (CategoriasDAO) DAOFactory.getInstanceDAO(CategoriasDAO.class.getName());
         return categoriasDAO.getCategoriasParaMiniHome();
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }   

   /**
    * @return
    * @throws ServiceException
    */
   public List getBannerCategorias() throws ServiceException {
      try {
         CategoriasDAO categoriasDAO = (CategoriasDAO) DAOFactory.getInstanceDAO(CategoriasDAO.class.getName());
         return categoriasDAO.getBannerCategorias();
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }
   
   
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
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }
   
   /**
    * @param categoriaId
    * @return
    * @throws ServiceException
    */
   public CategoriaBannerDTO getBannersCategoria(int categoriaId) throws ServiceException {
      try {
         CategoriasDAO categoriasDAO = (CategoriasDAO) DAOFactory.getInstanceDAO(CategoriasDAO.class.getName());
         return categoriasDAO.getBannersCategoria(categoriaId);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }
   
   /**
    * @param categoriaId
    * @return
    * @throws ServiceException
    */
   public void addBannerCatWeb(int categoriaId) throws ServiceException {
      try {
         CategoriasDAO categoriasDAO = (CategoriasDAO) DAOFactory.getInstanceDAO(CategoriasDAO.class.getName());
         categoriasDAO.addBannerCatWeb(categoriaId);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }


   /**
    * @param categoriaId
    * @param activoMasv
    * @throws ServiceException
    */
   public void updActivoMasv(int categoriaId, int activoMasv) throws ServiceException {
      try {
         CategoriasDAO categoriasDAO = (CategoriasDAO) DAOFactory.getInstanceDAO(CategoriasDAO.class.getName());
         categoriasDAO.updActivoMasv(categoriaId, activoMasv);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }

   /**
    * @param categoriaId
    * @param banner
    * @param nombre
    * @throws ServiceException
    */
   public void updBannerMasv(int categoriaId, String banner, String nombre) throws ServiceException {
      try {
         CategoriasDAO categoriasDAO = (CategoriasDAO) DAOFactory.getInstanceDAO(CategoriasDAO.class.getName());
         categoriasDAO.updBannerMasv(categoriaId, banner, nombre);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }

   /**
    * @param categoriaId
    * @param activoBanner
    * @throws ServiceException
    */
   public void updActivoBanner(int categoriaId, int activoBanner) throws ServiceException {
      try {
         CategoriasDAO categoriasDAO = (CategoriasDAO) DAOFactory.getInstanceDAO(CategoriasDAO.class.getName());
         categoriasDAO.updActivoBanner(categoriaId, activoBanner);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
      
   }

   /**
    * @param cat
    * @throws ServiceException
    */
   public void updCategoriaMasv(CategoriaMasvDTO cat) throws ServiceException {
      try {
         CategoriasDAO categoriasDAO = (CategoriasDAO) DAOFactory.getInstanceDAO(CategoriasDAO.class.getName());
         categoriasDAO.updCategoriaMasv(cat);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }
   
   /**
    * @param cat
    * @throws ServiceException
    */
   public void updBannersCategoria(CategoriaBannerDTO cat) throws ServiceException {
      try {
         CategoriasDAO categoriasDAO = (CategoriasDAO) DAOFactory.getInstanceDAO(CategoriasDAO.class.getName());
         categoriasDAO.updBannersCategoria(cat);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }   
}
