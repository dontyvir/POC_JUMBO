package cl.cencosud.procesos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.apache.log4j.Logger;

import cl.cencosud.beans.Empresa;
import cl.cencosud.util.LogUtil;
import cl.cencosud.util.Parametros;

/**
 * @author jdroguett
 */
public class CargarSaldoEmpresa {
    
    static {
        
        LogUtil.initLog4J();
        
    }
    
    private static Logger logger = Logger.getLogger(CargarSaldoEmpresa.class);

   public static void main(String[] args) {
   	  logger.debug("Inicio del proceso CargarSaldoEmpresa ");
      String user = Parametros.getString("USER");
      String password = Parametros.getString("PASSWORD");
      String driver = Parametros.getString("DRIVER");
      String url = Parametros.getString("URL");
      Connection con = null;
      try{
  		con = DbCarga.conexion(user, password, driver, url);
        logger.debug("CON : " + con);
      CargarSaldoEmpresa cargar = new CargarSaldoEmpresa();
      cargar.saldo(con);
      }catch (Exception e){
      	logger.error("Error al procesar: "+e);
      } finally {
        try {
            con.close();
            con = null;
  		}catch(Exception e){
  			logger.info("connection ya estaba cerrada");
  		}
        }
    }

   /**
    * @param con
    * @throws Exception
    */
   private void saldo(Connection con) throws Exception {
      //CVDCE0120090917093757
      String archivo = Archivo.nombreArchivoCSV("CVDCE01", "");
        logger.debug(archivo);
        if ( archivo.equals("") ) {
        	logger.info("Archivo CVD no encontrado");
        	//throw new Exception("Archivo CVD no encontrado");
        }else{

        List empresas = Archivo.cargarEmpresas(archivo);

      String sql = "UPDATE VE_EMPRESA SET EMP_SALDO = ?, EMP_FACT_SALDO = CURRENT_TIMESTAMP "
            + " WHERE EMP_RUT = ? AND EMP_DV = ? ";
      PreparedStatement ps = con.prepareStatement(sql);

      int n = 0;
      for (int i = 0; i < empresas.size(); i++) {
         Empresa empresa = (Empresa) empresas.get(i);
         ps.setBigDecimal(1, empresa.getSaldo());
         ps.setInt(2, empresa.getRut());
         ps.setString(3, "" + empresa.getDv());
         n += ps.executeUpdate();
      }
        logger.debug("Empresas actualizadas: " + n);
        }
    }
}
