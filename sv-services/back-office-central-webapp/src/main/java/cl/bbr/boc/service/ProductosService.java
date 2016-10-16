/*
 * Created on 22-abr-2009
 *
 */
package cl.bbr.boc.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cl.bbr.boc.dao.ProductosDAO;
import cl.bbr.boc.dto.FOProductoBannerDTO;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.jumbo.common.dao.DAOFactory;
import cl.jumbo.common.dao.DAOFactoryException;

/**
 * @author jdroguett
 */
public class ProductosService {

   public int publicar(String locales, List productos, String usuario) throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         return productosDAO.publicar(locales, productos, usuario);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }

   /**
    * @param locales
    * @param productos
    * @return
    * @throws ServiceException
    */
   public int despublicar(String locales, List productos, String usuario, int motivoId, String obs)
         throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         return productosDAO.despublicar(locales, productos, usuario, motivoId, obs);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }

   /**
    * @param locales
    * @param productos
    * @return
    * @throws ServiceException
    */
   public int conStock(String locales, List productos, String usuario)
         throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         return productosDAO.conStock(locales, productos, usuario);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }
   
   /**
    * @param locales
    * @param productos
    * @return
    * @throws ServiceException
    */
   public int sinStock(String locales, List productos, String usuario)
         throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         return productosDAO.sinStock(locales, productos, usuario);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }
   
   /**
    * Entrega los ids de productos que no existe en el local
    * 
    * @param string
    * @param productos
    * @return
    * @throws ServiceException
    */
   public List idsQueNoExisten(String localId, List productos) throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         return productosDAO.idsQueNoExisten(localId, productos);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }

   /**
    * @param cod_prod
    * @return
    * @throws ServiceException
    */
   public List getPublicacion(int productoId) throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         return productosDAO.getPublicacion(productoId);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }
   
   /**
    * @param id_producto
    * @param locales
    * @param motivoDes
    * @param usuario
    * @throws ServiceException
    */
   public void publicar(long productoId, String[] locales, String motivoDes, String obs, String usuario)
         throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         productosDAO.publicar(productoId, locales, motivoDes, obs, usuario);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e);
      } catch (DAOException e) {
         throw new ServiceException(e);
      }
   }

   /**
    * @param id_producto
    * @param localestienestock
    * @param usuario
    * @throws ServiceException
    */
   public void setTieneStock(long productoId, String[] localestienestock, String usuario)
         throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         productosDAO.setTieneStock(productoId, localestienestock, usuario);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e);
      } catch (DAOException e) {
         throw new ServiceException(e);
      }
   }
   
   /**
    * @return
    * @throws ServiceException
    */
   public List getMotivosDespublicacion() throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         return productosDAO.getMotivosDespublicacion();
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }

   /**
    * @param productos
    * @param b
    * @throws ServiceException
    */
   public void evitarPubDes(List productos, boolean evitarPubDes) throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         productosDAO.evitarPubDes(productos, evitarPubDes);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }

   /**
    * @param productoId
    * @return
    * @throws ServiceException
    */
   public boolean tieneSectorPicking(int productoId) throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         return productosDAO.tieneSectorPicking(productoId);
      } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }

   /**
    * @param productos
    * @return
    * @throws ServiceException
    */
   public List conSectorPicking(List productos) throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         return productosDAO.conSectorPicking(productos);
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
   public List listaNegraProductos() throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         return productosDAO.listaNegraProductos();
      } catch (DAOFactoryException e) {
         throw new ServiceException(e.getMessage());
      } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
      }
   }

   /**
    * @param lista
    * @throws ServiceException
    */
   public void updRubrubro(List lista) throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         productosDAO.updSubrubro(lista);
      } catch (DAOFactoryException e) {
         throw new ServiceException(e);
      } catch (DAOException e) {
         throw new ServiceException(e);
      }
   }

   /**
    * @param categoriaId
    * @return
    * @throws ServiceException
    */
   public List getMasvProductos(int categoriaId) throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         return productosDAO.getMasvProductos(categoriaId);
      } catch (DAOFactoryException e) {
         throw new ServiceException(e);
      } catch (DAOException e) {
         throw new ServiceException(e);
      }
   }

   /**
    * @param codsap
    * @return
    * @throws ServiceException
    */
   public List getProductosBO(String codsap) throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         return productosDAO.getProductosBO(codsap);
      } catch (DAOFactoryException e) {
         throw new ServiceException(e);
      } catch (DAOException e) {
         throw new ServiceException(e);
      }
   }

   /**
    * @param usr
    * @param id
    * @param localId
    * @param precio
    * @param b
    * @throws ServiceException
    */
   public void updatePrecio(UserDTO usr, int id, int localId, BigDecimal precio, boolean b) throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         productosDAO.updatePrecio(usr, id, localId, precio, b);
      } catch (DAOFactoryException e) {
         throw new ServiceException(e);
      } catch (DAOException e) {
         throw new ServiceException(e);
      }
   }
   
   /**
    * @param catprod
    * @return
    * @throws ServiceException
    */
   public String getCategCompletaProducto(int codprod) throws ServiceException {
      try {
         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
         return productosDAO.getCategCompletaProducto(codprod);
      } catch (DAOFactoryException e) {
         throw new ServiceException(e);
      } catch (DAOException e) {
         throw new ServiceException(e);
      }
   }
   
   //Tachar precios
   public Map updBannerProducto(List productos, String locales, String obs, UserDTO usr) throws ServiceException {
	   Map processRow = new HashMap();
	   int rptaOK =0;
	   int rptaNOK =0;
	   try {
	         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
	         //productosDAO.updateBannerProducto(productos, locales, obs, usr);
	         
	         for (Iterator iter = productos.iterator(); iter.hasNext();) {
	      	   	 FOProductoBannerDTO pro = (FOProductoBannerDTO) iter.next();
	      	     int rptaSQL = 0;
		      	 try {
		      		 rptaSQL = productosDAO.updateBannerProductoOnLy(pro, obs, usr);
		      		 if(rptaSQL==1){
		      			 rptaOK += 1;
		      		 }else{
		      			rptaNOK += 1;
		      		 }
		      	 } catch (Exception e) {
		      		 rptaNOK += 1;
		      		 continue;
			     }  
	         }
	         processRow.put("OK", new Integer(rptaOK));
	         processRow.put("NOK", new Integer(rptaNOK));
	      } catch (DAOFactoryException e) {
	         throw new ServiceException(e);
	      } catch (Exception e) {
	         throw new ServiceException(e);
	      }
	   return processRow;
	   }
   
   /* 
    * revrtBannerProducto
    * metodo encargado de recorrer a lista con los registros cargados en el archivo xls
    * realiza llamada al DAO
    * 
    */
   public Map revertBannerProducto(List productos, String locales, String obs, UserDTO usr) throws ServiceException {
	   Map processRow = new HashMap();
	   int rptaOK =0;
	   int rptaNOK =0;
	   try {
	         ProductosDAO productosDAO = (ProductosDAO) DAOFactory.getInstanceDAO(ProductosDAO.class.getName());
	         //productosDAO.updateBannerProducto(productos, locales, obs, usr);
	         
	         for (Iterator iter = productos.iterator(); iter.hasNext();) {
	      	   	 FOProductoBannerDTO pro = (FOProductoBannerDTO) iter.next();
	      	     int rptaSQL = 0;
		      	 try {
		      		 rptaSQL = productosDAO.revertBannerProductoOnLy(pro, obs, usr);
		      		 if(rptaSQL==1){
		      			 rptaOK += 1;
		      		 }else{
		      			rptaNOK += 1;
		      		 }
		      	 } catch (Exception e) {
		      		 rptaNOK += 1;
		      		 continue;
			     }  
	         }
	         processRow.put("OK", new Integer(rptaOK));
	         processRow.put("NOK", new Integer(rptaNOK));
	      } catch (DAOFactoryException e) {
	         throw new ServiceException(e);
	      } catch (Exception e) {
	         throw new ServiceException(e);
	      }
	   return processRow;
	   }
   
   
}