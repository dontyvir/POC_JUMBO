package cl.cencosud.procesos;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import cl.cencosud.beans.Costo;
import cl.cencosud.beans.Local;
import cl.cencosud.util.LogUtil;
import cl.cencosud.util.Parametros;

/**
 * @author jdroguett
 * 
 *         Carga los costos que vienen en los archivos CVC
 * 
 */
public class CargarCostos {
    
    static {
        
        LogUtil.initLog4J();
        
    }
    
    private static Logger logger = Logger.getLogger(CargarCostos.class);
   private static int RANGO = 20000;

   public static void main(String[] args){
   	logger.debug("Inicio del proceso CargarCostos ");
      long iniTotal = System.currentTimeMillis();

      String path = Parametros.getString("PATH_COSTOS");
      String user = Parametros.getString("USER");
      String password = Parametros.getString("PASSWORD");
      String driver = Parametros.getString("DRIVER");
      String url = Parametros.getString("URL");
      try{
      Connection con = DbCarga.conexion(user, password, driver, url);
        logger.debug("CON : " + con);

      try {
         CargarCostos cargar = new CargarCostos();
         cargar.cargar(con, path);
      } catch (SQLException e) {
         if (e.getNextException() != null)
                logger.error(e.getNextException());
      }
        logger.info("tiempo total: " + ( ( System.currentTimeMillis() - iniTotal ) / 1000.0 ));
      }catch(Exception e){
      	logger.info("Se detubo el proceso debido a "+e);
      }
   }

   /**
    * @param con
    * @throws SQLException
    * @throws IOException
    */
   private void cargar(Connection con, String path) throws Exception {
        List locales = DbCarga.locales(con);
      for (int i = 0; i < locales.size(); i++) {
         Local local = (Local) locales.get(i);
            logger.debug(path + "CVC" + local.getCodigo());
         String archivo = Archivo.nombreArchivoCSV(path, "CVC" + local.getCodigo(), "");
            logger.debug(archivo);
         actualizarCosto(con, local, archivo);
         System.gc();
      }

   }

   /**
    * @param con
    * @param id
    * @param archivo
    * @throws IOException
    * @throws SQLException
    */
   private void actualizarCosto(Connection con, Local local, String archivo) throws Exception, SQLException {
      try {
            Hashtable costos = Archivo.cargarCostos(archivo);
            List costosDb = DbCarga.costos(con, local.getId());
         String sql = "update fodba.fo_precios_locales set pre_costo = ? where pre_pro_id = ? and pre_loc_id = ?";

         PreparedStatement ps = con.prepareStatement(sql);

         for (int i = 0; i < costosDb.size(); i++) {
            Costo costoDb = (Costo) costosDb.get(i);
            Costo costo = (Costo) costos.get(costoDb.getCodigoProd());

            if (costo != null && costo.getCosto() != null && !costo.getCosto().equals(costoDb.getCosto())) {
               ps.setBigDecimal(1, costo.getCosto());
               ps.setInt(2, costo.getId());
               ps.setInt(3, local.getId());
               ps.addBatch();
            }
            if (i % RANGO == 0) {
               ps.executeBatch();
                    System.out.println(i);
               ps.clearBatch();
            }
         }
         ps.executeBatch();
         
      } catch (FileNotFoundException e) {
            logger.error("Archivo no existe para local " + local.getNombre() + "(" + local.getCodigo() + ")", e);
      }
   }
}
