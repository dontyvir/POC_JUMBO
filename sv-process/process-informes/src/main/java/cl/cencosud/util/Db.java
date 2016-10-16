/*
 * Created on 14-mar-2008
 *
 */
package cl.cencosud.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import cl.cencosud.informes.datos.Configuracion;
import cl.cencosud.informes.datos.Local;

/**
 * @author jdroguett
 *  
 */
public class Db {
   private final static Logger log = Logger.getLogger(Db.class.getName());	
   
   public static Connection conexion(String user, String password, String driver, String url) throws Exception {
      Connection con = null;

      log.info(" ... driver : "+driver);
      
      try {
         try {
            Class.forName(driver).newInstance();
         } catch (ClassNotFoundException e) {
         	log.info(" Error: El driver no se encuentra : "+driver);
            throw new Exception("Error: El driver no se encuentra");
         }
         Properties p = new Properties();
         p.setProperty("user", user);
         p.setProperty("password", password);

         if ((con = DriverManager.getConnection(url, p)) == null) {
         	log.info(" Error: la conexion es nula");
            throw new Exception("Error: la conexion es nula");
         }
         con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
      } catch (Exception e) {
      	 log.info("Error al obtener nueva conexión: " + e.getMessage());
         e.printStackTrace();
         throw new Exception("Error al obtener nueva conexión: " + e.getMessage());
      }
      return con;
   }

   public static PreparedStatement preparedStatement(Connection con, String sql) throws SQLException {
      return con.prepareStatement(sql);
   }

   public static void close(Connection con) {
      if (con != null) {
         try {
            con.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
   }

   public static void close(Statement st) {
      if (st != null) {
         try {
            st.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
   }

   public static void close(ResultSet rs) {
      if (rs != null) {
         try {
            rs.close();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   public static void close(ResultSet rs, Statement st) {
      close(rs);
      close(st);
   }
/**
 * getConfiguracionSemanal
 * @param con
 * @return
 * @throws SQLException
 */
   public static Configuracion getConfiguracionSemanal(Connection con) throws SQLException {
    String sql = " select ultima_fecha_semanal, tiempo_minimo_ronda, productividad_maxima_ronda " +
    		     " from bodba.iconfiguracion";
    Configuracion conf = new Configuracion();
    PreparedStatement ps = con.prepareStatement(sql + " WITH UR");
    ResultSet rs = ps.executeQuery();
    if (rs.next()) {
       conf.setUltimaFechaSemanal(new Date(rs.getDate("ultima_fecha_semanal").getTime()));
       conf.setTiempoMinimoRonda(rs.getInt("tiempo_minimo_ronda"));
       conf.setProductividadMaximaRonda(rs.getInt("productividad_maxima_ronda"));
    }
    close(rs, ps);
    return conf;
 }
/**
 * setConfiguracionSemanal
 * @param con
 * @param configuracion
 * @throws SQLException
 */
 public static void setConfiguracionSemanal(Connection con, Configuracion configuracion) throws SQLException {
    String sql = "update bodba.iconfiguracion set ultima_fecha_semanal = ?";
    PreparedStatement ps = con.prepareStatement(sql);
    ps.setDate(1, new java.sql.Date(configuracion.getUltimaFechaSemanal().getTime()));
    ps.executeUpdate();
    close(ps);
 }
/**
 * addConfiguracionSemanal
 * @param con
 * @param configuracion
 * @throws SQLException
 */
 public static void addConfiguracionSemanal(Connection con, Configuracion configuracion) throws SQLException {
    String sql = "insert into bodba.iconfiguracion(ultima_fecha_semanal, tiempo_minimo_ronda, productividad_maxima_ronda) "
          		+ " values (?, 120, 700)";
    PreparedStatement ps = con.prepareStatement(sql);
    ps.setDate(1, new java.sql.Date(configuracion.getUltimaFechaSemanal().getTime()));
    ps.executeUpdate();
    close(ps);
 }  
   
 /**
  * getConfiguracion DIARIO
  * @param con
  * @return
  * @throws SQLException
  */

   public static Configuracion getConfiguracion(Connection con) throws SQLException {
      String sql = " select ultima_fecha, tiempo_minimo_ronda, productividad_maxima_ronda " +
      		       " from bodba.iconfiguracion";
      Configuracion conf = new Configuracion();
      PreparedStatement ps = con.prepareStatement(sql + " WITH UR");
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
         conf.setUltimaFecha(new Date(rs.getDate("ultima_fecha").getTime()));
         conf.setTiempoMinimoRonda(rs.getInt("tiempo_minimo_ronda"));
         conf.setProductividadMaximaRonda(rs.getInt("productividad_maxima_ronda"));
      }
      close(rs, ps);
      return conf;
   }
/**
 * setConfiguracion DIARIO
 * @param con
 * @param configuracion
 * @throws SQLException
 */
   public static void setConfiguracion(Connection con, Configuracion configuracion) throws SQLException {
      String sql = "update bodba.iconfiguracion set ultima_fecha = ?";
      PreparedStatement ps = con.prepareStatement(sql);
      ps.setDate(1, new java.sql.Date(configuracion.getUltimaFecha().getTime()));
      ps.executeUpdate();
      close(ps);
   }
/**
 * addConfiguracion DIARIO
 * @param con
 * @param configuracion
 * @throws SQLException
 */
   public static void addConfiguracion(Connection con, Configuracion configuracion) throws SQLException {
      String sql = "insert into bodba.iconfiguracion(ultima_fecha, tiempo_minimo_ronda, productividad_maxima_ronda) "
            + " values (?, 120, 700)";
      PreparedStatement ps = con.prepareStatement(sql);
      ps.setDate(1, new java.sql.Date(configuracion.getUltimaFecha().getTime()));
      ps.executeUpdate();
      close(ps);
   }

   public static List getLocales(Connection con, String tipoPicking) throws SQLException {
      List lista = new ArrayList();
      // FIXME : id_local_sap = 1 parche para que el local aparezca en informes de los dos tipos de picking
      String sql = 	" select id_local, nom_local " +
      				" from bodba.bo_locales " +
      				" where id_local_sap = 1 " +
      				" or tipo_picking = '"
					+ tipoPicking + "' " +
				    " order by id_local";
      
      PreparedStatement ps = con.prepareStatement(sql + " WITH UR");
      ResultSet rs = ps.executeQuery();
      int i = 0;
      while (rs.next()) {
         Local local = new Local();
         local.setId(rs.getInt("id_local"));
         local.setNombre(rs.getString("nom_local"));
         local.setOrden(++i);
         lista.add(local);
      }
      close(rs, ps);
      return lista;
   }
   
   public static List getLocales(Connection con) throws SQLException {
      List lista = new ArrayList();
      String sql =  " select id_local, nom_local " +
      				" from bodba.bo_locales " +
      				" order by id_local";
      PreparedStatement ps = con.prepareStatement(sql + " WITH UR");
      ResultSet rs = ps.executeQuery();
      int i = 0;
      while (rs.next()) {
         Local local = new Local();
         local.setId(rs.getInt("id_local"));
         local.setNombre(rs.getString("nom_local"));
         local.setOrden(++i);
         lista.add(local);
      }
      close(rs, ps);
      return lista;
   }

   public static Hashtable getLocalesMap(Connection con, String tipoPicking) throws SQLException {
      Hashtable map = new Hashtable();
      // FIXME : id_local_sap = 1 parche para que el local aparezca en informes de los dos tipos de picking
 
      String sql =  " select id_local, nom_local " +
      				" from bodba.bo_locales " +
      				" where tipo_picking = '"
                    + tipoPicking + "' " +
                    " order by id_local";
      PreparedStatement ps = con.prepareStatement(sql + " WITH UR");
      ResultSet rs = ps.executeQuery();
      int i = 0;
      while (rs.next()) {
         Local local = new Local();
         local.setId(rs.getInt("id_local"));
         local.setNombre(rs.getString("nom_local"));
         local.setOrden(++i);
         map.put(new Integer(local.getId()), local);
      }
      close(rs, ps);
      return map;
   }

   public static String getSqlCount(String sql) {
      return "select count(*) from (" + sql + ") as x  WITH UR";
   }

   public static int getSemana(Connection con, GregorianCalendar fecha) throws SQLException {
      
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String sql = " select week_iso('" + sdf.format(fecha.getTime()) + "') " +
      		       " from bo_locales fetch first 1 rows only";
      Statement st = con.createStatement();
      ResultSet rs = st.executeQuery(sql + " WITH UR");
      if (rs.next()){
         return rs.getInt(1);
      }
      return -1;
   }
   
   
 //20121212VMatheu
   public static int getDia(Connection con, GregorianCalendar fecha) throws SQLException {
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String sql = " select DAYOFYEAR('" + sdf.format(fecha.getTime()) + "')" +
    		     " from bo_locales fetch first 1 rows only";
    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery(sql + " WITH UR");
    if (rs.next()){
       return rs.getInt(1);
    }
    st.close();
    return -1; 
   }
 //-20121212VMatheu
   

   
}