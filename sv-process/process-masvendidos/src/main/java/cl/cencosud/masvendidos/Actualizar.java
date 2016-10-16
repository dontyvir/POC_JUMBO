/*
 * Created on 16-abr-2010
 */
package cl.cencosud.masvendidos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import cl.cencosud.util.Db;
import cl.cencosud.util.Parametros;

/**
 * @author jdroguett
 */
public class Actualizar {

   public static void main(String[] args) throws Exception {
      String user = Parametros.getString("USER");
      String password = Parametros.getString("PASSWORD");
      String driver = Parametros.getString("DRIVER");
      String url = Parametros.getString("URL");
      Connection con = Db.conexion(user, password, driver, url);
      System.out.println("CON : " + con);
      Actualizar actualizar = new Actualizar();
      actualizar.up(con);
   }

   /**
    * @param con
    * @throws Exception
    */
   private void up(Connection con) throws Exception {
      //esto es por si hay nueva categorias publicadas
      insertarMasvCategorias(con);
      insertarMasvProductos(con);

   }

   /**
    * @param con
    * @throws Exception
    */
   private void insertarMasvProductos(Connection con) throws Exception {
      GregorianCalendar hastaDomingo = new GregorianCalendar(); //hoy
      hastaDomingo.setFirstDayOfWeek(Calendar.MONDAY); //la semana comienza el lunes
      hastaDomingo.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //lunes de la semana actual
      hastaDomingo.add(Calendar.DAY_OF_YEAR, -1); //domingo
      GregorianCalendar desdeLunes = (GregorianCalendar) hastaDomingo.clone();
      desdeLunes.setFirstDayOfWeek(Calendar.MONDAY);
      desdeLunes.add(Calendar.WEEK_OF_YEAR, -1); //domingo hace 1 semana
      desdeLunes.add(Calendar.DAY_OF_YEAR, 1); //lunes
      int semana = desdeLunes.get(Calendar.WEEK_OF_YEAR);

      //lista de categorias no actualizada para la semana
      List categoriaIds = categoriasMasv(con, semana);
      List localesIds = locales(con);
      for (int i = 0; i < categoriaIds.size(); i++) {
         Integer id = (Integer) categoriaIds.get(i);
         try {
            con.setAutoCommit(false);
            for (int j = 0; j < localesIds.size(); j++) {
               Integer localId = (Integer) localesIds.get(j);
               insertarMasvProductos(con, localId.intValue(), id.intValue(), desdeLunes.getTime(), hastaDomingo
                     .getTime());
               updateSemanaMasvCategorias(con, id.intValue(), semana);
            }
            con.commit();
            con.setAutoCommit(true);
         } catch (Exception e) {
            Db.rollback(con);
            throw e;
         } finally {
            Db.autoCommitTrue(con);
         }
      }
   }

   /**
    * 
    * @param categoriaId
    * @param semana
    * @throws SQLException
    */
   private void updateSemanaMasvCategorias(Connection con, int categoriaId, int semana) throws SQLException {
      PreparedStatement ps = null;
      String sql = "update fodba.masv_categorias set semana = ? where categoria_id = ? ";
      try {
         ps = con.prepareStatement(sql);
         ps.setInt(1, semana);
         ps.setInt(2, categoriaId);
         ps.executeUpdate();
      } finally {
         Db.close(ps);
      }
   }

   /**
    * 
    * @param localId
    * @param categoriaId
    * @param desde
    * @param hasta
    * @throws SQLException
    */
   private void insertarMasvProductos(Connection con, int localId, int categoriaId, Date desde, Date hasta)
         throws SQLException {
      String sqlDel = "delete from fodba.masv_productos where masv_categoria_id = ? and local_id = ?";

      String sql = "insert into fodba.masv_productos(masv_categoria_id, local_id, producto_id, cantidad)   "
            + " select " + categoriaId + "," + localId + ", xx.pro_id, xx.cantidad  from ( "
            + " select  subcat.prca_cat_id, fp.pro_id, count(distinct pe.id_pedido) as cantidad "
            + " from bodba.bo_pedidos pe inner join bodba.bo_detalle_pedido dp on pe.id_pedido = dp.id_pedido "
            + " inner join fodba.fo_productos fp on fp.pro_id_bo = dp.id_producto "
            + " inner join fodba.fo_precios_locales pl on pl.pre_loc_id  = pe.id_local and pl.pre_pro_id = fp.pro_id and pre_estado = 'A' and fp.pro_estado = 'A' "
            + " inner join fodba.fo_productos_categorias subcat on subcat.prca_pro_id = fp.pro_id "
            + " inner join fodba.fo_catsubcat cat on cat.subcat_id = subcat.prca_cat_id "
            + " where pe.id_local = ? and cat.cat_id = ?  and pe.created_at >= ? and pe.created_at <= ? "
            + " group by subcat.prca_cat_id, fp.pro_id " + " ) as xx " + " inner join ( "
            + " select x.prca_cat_id, max(x.cantidad) as maxc from( "
            + " select  subcat.prca_cat_id, fp.pro_id, count(distinct pe.id_pedido) as cantidad "
            + " from bodba.bo_pedidos pe inner join bodba.bo_detalle_pedido dp on pe.id_pedido = dp.id_pedido "
            + " inner join fodba.fo_productos fp on fp.pro_id_bo = dp.id_producto  "
            + " inner join fodba.fo_precios_locales pl on pl.pre_loc_id  = pe.id_local and pl.pre_pro_id = fp.pro_id and pre_estado = 'A' and fp.pro_estado = 'A' "
            + " inner join fodba.fo_productos_categorias subcat on subcat.prca_pro_id = fp.pro_id "
            + " inner join fodba.fo_catsubcat cat on cat.subcat_id = subcat.prca_cat_id "
            + " where pe.id_local = ? and cat.cat_id = ? and pe.created_at >= ? and pe.created_at <= ? "
            + " group by subcat.prca_cat_id, fp.pro_id " + " order by subcat.prca_cat_id, cantidad desc " + " )as x "
            + " group by x.prca_cat_id " + " ) as y on xx.prca_cat_id = y.prca_cat_id and xx.cantidad =  y.maxc ";

      /*
       * + "select " + categoriaId + "," + localId + ", pro_id, cantidad from " + " (select fp.pro_id, count(distinct
       * pe.id_pedido) as cantidad, " + " ROW_NUMBER() OVER (order by count(distinct pe.id_pedido) desc) AS row_number " +
       * "from bodba.bo_pedidos pe inner join bodba.bo_detalle_pedido dp on pe.id_pedido = dp.id_pedido " + "inner join
       * fodba.fo_productos fp on fp.pro_id_bo = dp.id_producto " + "inner join fodba.fo_productos_categorias subcat on
       * subcat.prca_pro_id = fp.pro_id " + "inner join fodba.fo_catsubcat cat on cat.subcat_id = subcat.prca_cat_id " +
       * "where pe.id_local = ? and cat.cat_id = ? and pe.created_at >= ? and pe.created_at <= ? " + "group by
       * fp.pro_id) as x where row_number <= 10";
       */

      PreparedStatement ps = null;
      try {
         ps = con.prepareStatement(sqlDel);
         ps.setInt(1, categoriaId);
         ps.setInt(2, localId);
         ps.executeUpdate();
         Db.close(ps);
         ps = con.prepareStatement(sql);
         ps.setInt(1, localId);
         ps.setInt(2, categoriaId);
         ps.setDate(3, new java.sql.Date(desde.getTime()));
         ps.setDate(4, new java.sql.Date(hasta.getTime()));
         ps.setInt(5, localId);
         ps.setInt(6, categoriaId);
         ps.setDate(7, new java.sql.Date(desde.getTime()));
         ps.setDate(8, new java.sql.Date(hasta.getTime()));
         ps.executeUpdate();

      } finally {
         Db.close(ps);
      }
   }

   /**
    * @param con
    * @return
    * @throws SQLException
    */
   private List locales(Connection con) throws SQLException {
      PreparedStatement ps = null;
      ResultSet rs = null;
      String sql = "select id_local from bodba.bo_locales";
      List lista = new ArrayList();
      try {
         ps = con.prepareStatement(sql);
         rs = ps.executeQuery();
         while (rs.next()) {
            Integer id = new Integer(rs.getInt("id_local"));
            lista.add(id);
         }
      } finally {
         Db.close(rs, ps);
      }
      return lista;
   }

   private void insertarMasvCategorias(Connection con) throws SQLException {
      Statement st = null;
      String sql = "insert into fodba.masv_categorias(categoria_id) "
            + "select distinct cat.cat_id from fodba.fo_productos pro "
            + "inner join fodba.fo_productos_categorias subcat on subcat.prca_pro_id = pro.pro_id "
            + "inner join fodba.fo_catsubcat cat on cat.subcat_id = subcat.prca_cat_id "
            + "inner join fodba.fo_categorias fcat on fcat.cat_id = cat.cat_id "
            + "where  fcat.cat_estado = 'A' and pro.pro_estado = 'A' and pro_inter_valor > 0 "
            + "and cat.cat_id not in (select categoria_id from fodba.masv_categorias) ";
      try {
         st = con.createStatement();
         st.executeUpdate(sql);
      } finally {
         Db.close(st);
      }
   }

   private List categoriasMasv(Connection con, int semana) throws SQLException {
      PreparedStatement ps = null;
      ResultSet rs = null;
      //categorias que no se han actualizado sus productos mas vendidos
      String sql = "select categoria_id from fodba.masv_categorias where semana != ? ";
      List lista = new ArrayList();
      try {
         ps = con.prepareStatement(sql);
         ps.setInt(1, semana);
         rs = ps.executeQuery();
         while (rs.next()) {
            Integer id = new Integer(rs.getInt("categoria_id"));
            lista.add(id);
         }
      } finally {
         Db.close(rs, ps);
      }
      return lista;
   }
}
