/*
 * Created on 23-abr-2009
 *
 */
package cl.jumbo.common.dao;

import java.util.List;

import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.jumbo.common.dto.Categoria;
import cl.jumbo.common.dto.CategoriaMasvDTO;
import cl.jumbo.common.dto.CategoriaBannerDTO;

/**
 * @author jdroguett
 *
 */
public interface CategoriasDAO {

   public Categoria root()  throws DAOException;
   /**
    * @return
    * @throws DAOException
    */
   public List getMasvCategorias() throws DAOException;
   
   /**
    * @return
    * @throws DAOException
    */
   public List getCategoriasParaMiniHome() throws DAOException;

   /**
    * @return
    * @throws DAOException
    */
   public List getBannerCategorias() throws DAOException;   
   
   /**
    * @param categoriaId
    * @param activoMasv
    * @throws DAOException
    */
   public void updActivoMasv(int categoriaId, int activoMasv) throws DAOException;

   /**
    * @param categoriaId
    * @return
    * @throws DAOException
    */
   public CategoriaMasvDTO getMasvCategoria(int categoriaId) throws DAOException;

   /**
    * @param categoriaId
    * @return
    * @throws DAOException
    */
   public CategoriaBannerDTO getBannersCategoria(int categoriaId) throws DAOException;
   
   /**
    * @param categoriaId
    * @throws DAOException
    */
   public void addBannerCatWeb(int categoriaId) throws DAOException;
   
   /**
    * @param categoriaId
    * @param banner
    * @param nombre
    * @throws DAOException
    */
   public void updBannerMasv(int categoriaId, String banner, String nombre) throws DAOException;

   /**
    * @param categoriaId
    * @param activoBanner
    * @throws DAOException
    */
   public void updActivoBanner(int categoriaId, int activoBanner) throws DAOException;
   /**
    * @param cat
    * @throws DAOException
    */
   public void updCategoriaMasv(CategoriaMasvDTO cat) throws DAOException;

   /**
    * @param cat
    * @throws DAOException
    */
   public void updBannersCategoria(CategoriaBannerDTO cat) throws DAOException;
   
}
