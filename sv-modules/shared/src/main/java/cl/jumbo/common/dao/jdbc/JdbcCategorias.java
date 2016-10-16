/*
 * Created on 23-abr-2009
 */
package cl.jumbo.common.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.jumbo.common.dao.CategoriasDAO;
import cl.jumbo.common.dto.Categoria;
import cl.jumbo.common.dto.CategoriaBannerDTO;
import cl.jumbo.common.dto.CategoriaMasvDTO;

/**
 * @author jdroguett
 */
public class JdbcCategorias extends JdbcDAO implements CategoriasDAO {
   private static ConexionUtil conexionUtil = new ConexionUtil();

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.CategoriasDAO#root()
    */
   public Categoria root() throws DAOException {
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      Categoria root = new Categoria();
      root.putId(0);
      root.putData("title", "Categorias");
      root.putData("icon", "");
      String sql = "select cat.cat_id as cat_id , cat.cat_nombre as cat_nombre, cat.cat_estado as cat_estado, "
            + " sub.cat_id as sub_id, sub.cat_nombre as sub_nombre, sub.cat_estado as sub_estado "
            + " from fo_categorias cat " + " inner join fo_catsubcat u on u.cat_id = cat.cat_id "
            + " inner join fo_categorias sub on sub.cat_id = u.subcat_id "
            + " order by cat.cat_nombre, cat.cat_id, sub.cat_nombre, sub.cat_id ";
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql + " WITH UR ");
         rs = ps.executeQuery();
         Hashtable ht = new Hashtable();

         while (rs.next()) {
            Integer catId = new Integer(rs.getInt("cat_id"));
            Categoria cat = (Categoria) ht.get(catId);
            if (cat == null) {
               cat = new Categoria();
               cat.putId(catId.intValue());
               cat.putAttributes("rel", "cat");
               cat.putAttributes("pub", "D".equals(rs.getString("cat_estado")) ? "no" : "si");
               cat.putData("title", rs.getString("cat_nombre"));
               cat.putData("class", "D".equals(rs.getString("cat_estado")) ? "unchecked" : "checked");
               //cat.putData("icon", "D".equals(rs.getString("cat_estado")) ? "./js/tree/themes/default/despub.png" :
               // "");
            }
            Categoria sub = new Categoria();
            sub.putId(rs.getInt("sub_id"));
            sub.putAttributes("rel", "sub");
            sub.putAttributes("pub", "D".equals(rs.getString("sub_estado")) ? "no" : "si");
            sub.putData("title", rs.getString("sub_nombre"));
            sub.putData("class", "D".equals(rs.getString("sub_estado")) ? "unchecked" : "checked");
            //sub.putData("icon", "D".equals(rs.getString("sub_estado")) ? "./js/tree/themes/default/despub.png" : "");
            cat.addChildren(sub);
            ht.put(catId, cat);
         }

         Enumeration enum = ht.elements();
         while (enum.hasMoreElements()) {
            Categoria cat = (Categoria) enum.nextElement();
            root.addChildren(cat);
         }
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(rs, ps, con);
      }
      return root;
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.CategoriasDAO#getMasvCategorias()
    */
   public List getMasvCategorias() throws DAOException {
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      String sql = "select CATEGORIA_ID, cat_nombre, ACTIVO_MASV, ACTIVO_BANNER, "
            + "FECHA_INICIO, FECHA_TERMINO, BANNER_PRINCIPAL, BANNER_SECUNDARIO1, BANNER_SECUNDARIO2 "
            + "from fodba.masv_categorias m inner join fodba.fo_categorias c on m.categoria_id = c.cat_id";

      List lista = new ArrayList();
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql + " WITH UR ");
         rs = ps.executeQuery();

         while (rs.next()) {
            CategoriaMasvDTO dto = new CategoriaMasvDTO();
            dto.setId(rs.getInt("categoria_id"));
            dto.setNombre(rs.getString("cat_nombre"));
            dto.setActivoMasv(rs.getInt("activo_masv") != 0);
            dto.setActivoBanner(rs.getInt("activo_banner") != 0);
            Date fechaInicio = rs.getDate("fecha_inicio");
            Date fechaTermino = rs.getDate("fecha_termino");
            dto.setFechaInicio(fechaInicio == null ? null : new Date(fechaInicio.getTime()));
            dto.setFechaTermino(fechaTermino == null ? null : new Date(fechaTermino.getTime()));
            dto.setBannerPrincipal(rs.getString("banner_principal"));
            dto.setBannerSecundario1(rs.getString("banner_secundario1"));
            dto.setBannerSecundario2(rs.getString("banner_secundario2"));
            lista.add(dto);
         }
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(rs, ps, con);
      }
      return lista;
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.CategoriasDAO#getBannerCategorias()
    */
   public List getBannerCategorias() throws DAOException {
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      String sql = "select bca_cat_id, cat_nombre, bca_estado, "
            + "bca_fch_inicio, bca_fch_termino, bca_banner_principal "
            + "from fodba.fo_banner_cat b inner join fodba.fo_categorias c on b.bca_cat_id = c.cat_id "
            + "order by cat_tipo asc, cat_nombre asc";

      List lista = new ArrayList();
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql + " WITH UR ");
         rs = ps.executeQuery();

         while (rs.next()) {
            CategoriaBannerDTO dto = new CategoriaBannerDTO();
            dto.setId(rs.getInt("bca_cat_id"));
            dto.setNombre(rs.getString("cat_nombre"));
            if (rs.getInt("bca_estado") == 1)
                dto.setEstado(true);
            else
                dto.setEstado(false);
            Date fechaInicio = rs.getDate("bca_fch_inicio");
            Date fechaTermino = rs.getDate("bca_fch_termino");
            dto.setFechaInicio(fechaInicio == null ? null : new Date(fechaInicio.getTime()));
            dto.setFechaTermino(fechaTermino == null ? null : new Date(fechaTermino.getTime()));
            dto.setBannerPrincipal(rs.getString("bca_banner_principal"));
            lista.add(dto);
         }
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(rs, ps, con);
      }
      return lista;
   }
   
   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.CategoriasDAO#getCategoriasParaMiniHome()
    */
   public List getCategoriasParaMiniHome() throws DAOException {
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      String sql = "SELECT cat_id, cat_nombre, cat_tipo "
            + "FROM fodba.fo_categorias "
            + "WHERE cat_tipo in ('C') and cat_id not in (select bca_cat_id from fodba.fo_banner_cat) and cat_estado = 'A'"
            + "ORDER BY cat_tipo asc, cat_nombre asc";

      List lista = new ArrayList();
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql + " WITH UR ");
         rs = ps.executeQuery();

         while (rs.next()) {
            CategoriaBannerDTO dto = new CategoriaBannerDTO();
            dto.setId(rs.getInt("cat_id"));
            dto.setNombre(rs.getString("cat_nombre"));
            lista.add(dto);
         }
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(rs, ps, con);
      }
      return lista;
   }   
   
   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.CategoriasDAO#updActivoMasv(int, int)
    */
   public void updActivoMasv(int categoriaId, int activoMasv) throws DAOException {
      Connection con = null;
      PreparedStatement ps = null;
      String sql = "update fodba.masv_categorias set activo_masv = ? where categoria_id = ?";
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql);
         ps.setInt(1, activoMasv);
         ps.setInt(2, categoriaId);
         ps.executeUpdate();
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(ps, con);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.CategoriasDAO#getMasvCategoria(int)
    */
   public CategoriaMasvDTO getMasvCategoria(int categoriaId) throws DAOException {
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      String sql = "select CATEGORIA_ID, cat_nombre, ACTIVO_MASV, ACTIVO_BANNER, "
            + "FECHA_INICIO, FECHA_TERMINO, BANNER_PRINCIPAL, BANNER_SECUNDARIO1, BANNER_SECUNDARIO2 "
            + "from fodba.masv_categorias m inner join fodba.fo_categorias c on m.categoria_id = c.cat_id "
            + "where categoria_id = ?";
      CategoriaMasvDTO dto = null;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql + " WITH UR");
         ps.setInt(1, categoriaId);
         rs = ps.executeQuery();

         if (rs.next()) {
            dto = new CategoriaMasvDTO();
            dto.setId(rs.getInt("categoria_id"));
            dto.setNombre(rs.getString("cat_nombre"));
            dto.setActivoMasv(rs.getInt("activo_masv") != 0);
            dto.setActivoBanner(rs.getInt("activo_banner") != 0);
            Date fechaInicio = rs.getDate("fecha_inicio");
            Date fechaTermino = rs.getDate("fecha_termino");
            dto.setFechaInicio(fechaInicio == null ? null : new Date(fechaInicio.getTime()));
            dto.setFechaTermino(fechaTermino == null ? null : new Date(fechaTermino.getTime()));
            dto.setBannerPrincipal(rs.getString("banner_principal"));
            dto.setBannerSecundario1(rs.getString("banner_secundario1"));
            dto.setBannerSecundario2(rs.getString("banner_secundario2"));
         }
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(rs, ps, con);
      }
      return dto;
   }
   
   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.CategoriasDAO#getBannersCategoria(int)
    */
   public CategoriaBannerDTO getBannersCategoria(int categoriaId) throws DAOException {
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      String sql = "select bca_cat_id, cat_nombre, bca_estado, "
            + "bca_fch_inicio, bca_fch_termino, bca_banner_principal "
            + "from fodba.fo_banner_cat b inner join fodba.fo_categorias c on b.bca_cat_id = c.cat_id "
            + "where bca_cat_id = ?";
      CategoriaBannerDTO dto = null;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql + " WITH UR");
         ps.setInt(1, categoriaId);
         rs = ps.executeQuery();

         if (rs.next()) {
            dto = new CategoriaBannerDTO();
            dto.setId(rs.getInt("bca_cat_id"));
            dto.setNombre(rs.getString("cat_nombre"));
            if (rs.getInt("bca_estado") == 1)
                dto.setEstado(true);
            else
                dto.setEstado(false);
            Date fechaInicio = rs.getDate("bca_fch_inicio");
            Date fechaTermino = rs.getDate("bca_fch_termino");
            dto.setFechaInicio(fechaInicio == null ? null : new Date(fechaInicio.getTime()));
            dto.setFechaTermino(fechaTermino == null ? null : new Date(fechaTermino.getTime()));
            dto.setBannerPrincipal(rs.getString("bca_banner_principal"));
         }
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(rs, ps, con);
      }
      return dto;
   }   

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.CategoriasDAO#updBannerMasv(int, java.lang.String, java.lang.String)
    */
   public void updBannerMasv(int categoriaId, String banner, String nombre) throws DAOException {
      Connection con = null;
      PreparedStatement ps = null;
      String sql = null;
      if (banner.equals("bb1")) {
         sql = "update fodba.masv_categorias set banner_principal = ? where categoria_id = ?";
      } else if (banner.equals("bb2")) {
         sql = "update fodba.masv_categorias set banner_secundario1 = ? where categoria_id = ?";
      } else if (banner.equals("bb3")) {
         sql = "update fodba.masv_categorias set banner_secundario2 = ? where categoria_id = ?";
      }

      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql);
         ps.setString(1, nombre);
         ps.setInt(2, categoriaId);
         ps.executeUpdate();
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(ps, con);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.CategoriasDAO#updActivoBanner(int, int)
    */
   public void updActivoBanner(int categoriaId, int activoBanner) throws DAOException {
      Connection con = null;
      PreparedStatement ps = null;
      String sql = "update fodba.masv_categorias set activo_banner = ? where categoria_id = ?";
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql);
         ps.setInt(1, activoBanner);
         ps.setInt(2, categoriaId);
         ps.executeUpdate();
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(ps, con);
      }

   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.jumbo.common.dao.CategoriasDAO#updCategoriaMasv(cl.jumbo.common.dto.CategoriaMasvDTO)
    */
   public void updCategoriaMasv(CategoriaMasvDTO cat) throws DAOException {
      Connection con = null;
      PreparedStatement ps = null;
      Hashtable datos = new Hashtable();

      String sql = "update fodba.masv_categorias set ";
      int i = 0;

      if (cat.getBannerPrincipal() != null) {
         sql += " banner_principal = ?,";
         datos.put(new Integer(++i), cat.getBannerPrincipal());
      }
      if (cat.getBannerSecundario1() != null) {
         sql += " banner_secundario1 = ?,";
         datos.put(new Integer(++i), cat.getBannerSecundario1());
      }
      if (cat.getBannerSecundario2() != null) {
         sql += " banner_secundario2 = ?,";
         datos.put(new Integer(++i), cat.getBannerSecundario2());
      }
      if (cat.getFechaInicio() != null) {
         sql += " fecha_inicio = ?,";
         datos.put(new Integer(++i), new java.sql.Date(cat.getFechaInicio().getTime()));
      }
      if (cat.getFechaTermino() != null) {
         sql += " fecha_termino = ?,";
         datos.put(new Integer(++i), new java.sql.Date(cat.getFechaTermino().getTime()));
      }
      if (cat.getActivoBanner() != null) {
         sql += " activo_banner = ?,";
         datos.put(new Integer(++i), cat.getActivoBanner());
      }
      if (cat.getActivoMasv() != null) {
         sql += " activo_masv = ?,";
         datos.put(new Integer(++i), cat.getActivoMasv());
      }

      sql = sql.substring(0, sql.length() - 1);
      sql += " where categoria_id = ?";
      System.out.println("**************************************************");
      System.out.println(sql);
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql);
         int n = 0;
         Object obj = null;
         while ((obj = datos.get(new Integer(++n))) != null) {
            ps.setObject(n, obj);
         }
         ps.setInt(n, cat.getId());
         ps.executeUpdate();
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(ps, con);
      }

   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.jumbo.common.dao.CategoriasDAO#updCategoriaMasv(cl.jumbo.common.dto.CategoriaMasvDTO)
    */
   public void updBannersCategoria(CategoriaBannerDTO cat) throws DAOException {
      Connection con = null;
      PreparedStatement ps = null;
      Hashtable datos = new Hashtable();

      String sql = "update fodba.fo_banner_cat set ";
      int i = 0;

      if (cat.getBannerPrincipal() != null) {
         sql += " bca_banner_principal = ?,";
         datos.put(new Integer(++i), cat.getBannerPrincipal());
      }
      if (cat.getFechaInicio() != null) {
         sql += " bca_fch_inicio = ?,";
         datos.put(new Integer(++i), new java.sql.Date(cat.getFechaInicio().getTime()));
      }
      if (cat.getFechaTermino() != null) {
         sql += " bca_fch_termino = ?,";
         datos.put(new Integer(++i), new java.sql.Date(cat.getFechaTermino().getTime()));
      }
      
      sql += " bca_estado = ?,";
      if (cat.getEstado())
          datos.put(new Integer(++i), "1");
      else
          datos.put(new Integer(++i), "0");
            
      sql = sql.substring(0, sql.length() - 1);
      sql += " where bca_cat_id = ?";
      System.out.println("**************************************************");
      System.out.println(sql);
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql);
         int n = 0;
         Object obj = null;
         while ((obj = datos.get(new Integer(++n))) != null) {
            ps.setObject(n, obj);
         }
         ps.setInt(n, cat.getId());
         ps.executeUpdate();
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(ps, con);
      }

   }
   
   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.CategoriasDAO#addBannerCatWeb(int)
    */
   public void addBannerCatWeb(int categoriaId) throws DAOException {
      Connection con = null;
      PreparedStatement ps = null;
      String sql = "insert into fodba.fo_banner_cat (bca_cat_id, bca_estado) values(?,0)";
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql);
         ps.setInt(1, categoriaId);
         ps.executeUpdate();
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(ps, con);
      }
   }
   
}
