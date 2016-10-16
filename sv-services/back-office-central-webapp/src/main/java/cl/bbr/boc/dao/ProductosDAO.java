/*
 * Created on 22-abr-2009
 */
package cl.bbr.boc.dao;

import java.math.BigDecimal;
import java.util.List;

import cl.bbr.boc.dto.FOProductoBannerDTO;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 */
public interface ProductosDAO {

   /**
    * @param productos
    * @return
    */
   public int publicar(String locales, List productos, String usuario) throws DAOException;

   /**
    * @param locales
    * @param productos
    * @return
    * @throws DAOException
    */
   public int despublicar(String locales, List productos, String usuario, int motivoId, String obs) throws DAOException;

   /**
    * @param locales
    * @param productos
    * @return
    * @throws DAOException
    */
   public int conStock(String locales, List productos, String usuario) throws DAOException;
   
   /**
    * @param locales
    * @param productos
    * @return
    * @throws DAOException
    */
   public int sinStock(String locales, List productos, String usuario) throws DAOException;

   
   /**
    * @param localId
    * @param productos
    * @return
    * @throws DAOException
    */
   public List idsQueNoExisten(String localId, List productos) throws DAOException;

   /**
    * @param productoId
    * @return
    * @throws DAOException
    */
   public List getPublicacion(int productoId) throws DAOException;
   
   /**
    * @param productoId
    * @param locales
    * @param motivoDes
    * @param usuario
    * @throws DAOException
    */
   public void publicar(long productoId, String[] locales, String motivoDes, String obs, String usuario) throws DAOException;

   /**
    * @param productoId
    * @param localestienestock
    * @param usuario
    * @throws DAOException
    */
   public void setTieneStock(long productoId, String[] localestienestock, String usuario) throws DAOException;

   
   /**
    * @return
    * @throws DAOException
    */
   public List getMotivosDespublicacion() throws DAOException;

   /**
    * @param productos
    * @param evitarPubDes
    * @return
    * @throws DAOException
    */
   public void evitarPubDes(List productos, boolean evitarPubDes) throws DAOException;

   /**
    * @param productoId
    * @return
    * @throws DAOException
    */
   public boolean tieneSectorPicking(int productoId) throws DAOException;

   /**
    * @param productos
    * @return
    * @throws DAOException
    */
   public List conSectorPicking(List productos) throws DAOException;

   /**
    * @return
    */
   public List listaNegraProductos() throws DAOException;

   /**
    * @param lista
    * @throws DAOException
    */
   public void updSubrubro(List lista) throws DAOException;

   /**
    * @param categoriaId
    * @return
    */
   public List getMasvProductos(int categoriaId) throws DAOException;

   /**
    * @param codsap
    * @return
    * @throws DAOException
    */
   public List getProductosBO(String codsap) throws DAOException;

   /**
    * @param usr
    * @param id
    * @param localId
    * @param precio
    * @param b
    * @throws DAOException
    */
   public void updatePrecio(UserDTO usr, int id, int localId, BigDecimal precio, boolean b) throws DAOException;

   /**
    * @param catprod
    * @return
    * @throws DAOException
    */
   public String getCategCompletaProducto(int codprod) throws DAOException;
   
   //Tachar precios
   
   public int updateBannerProductoOnLy(FOProductoBannerDTO pro, String obs, UserDTO usr) throws DAOException;
   
   public int revertBannerProductoOnLy(FOProductoBannerDTO pro, String obs, UserDTO usr) throws DAOException;
   
}