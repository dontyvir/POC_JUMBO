/*
 * Created on 21-ago-2009
 */
package cl.cencosud.informes.diario;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import cl.cencosud.informes.datos.Configuracion;
import cl.cencosud.util.Db;
import cl.cencosud.util.Parametros;

/**
 * @author desarrollos menores
 */
public class Resumen implements IProductividadDB{
	

   private final static Logger log = Logger.getLogger(Resumen.class.getName());

    public static void main(String[] args) throws Exception {
        String user = Parametros.getString("USER");
        String password = Parametros.getString("PASSWORD");
        String driver = Parametros.getString("DRIVER");
        String url = Parametros.getString("URL");
        Connection con = Db.conexion(user, password, driver, url);
        
        Resumen resumen = new Resumen();
        resumen.generar(con,-ProductividadDiaria.DIAS_ATRAS);
        Db.close(con);
     }
      
    /**
    * Genera el resumen principal
    * 
    * @param con
    * @throws SQLException
    */
   public void generar(Connection con,int diasAtras) throws SQLException {
      Configuracion conf = Db.getConfiguracion(con);
      
      if (conf.getUltimaFecha() == null) {
         GregorianCalendar fecha = new GregorianCalendar(2008, 0, 7); //primer lunes del año
         conf.setUltimaFecha(fecha.getTime());
         Db.addConfiguracion(con, conf);
      }
      
//define fecha 00:00:00 de ejecucion del proceso.
      GregorianCalendar hoy = new GregorianCalendar();
      hoy.set(Calendar.HOUR_OF_DAY, 0);
      hoy.set(Calendar.MINUTE, 0);
      hoy.set(Calendar.SECOND, 0);
      hoy.set(Calendar.MILLISECOND, 0);
      
      hoy.setFirstDayOfWeek(Calendar.MONDAY);
      Date hasta = new Date(hoy.getTimeInMillis());

//resta numeros de dias del mes.
      hoy.add(Calendar.DAY_OF_YEAR,diasAtras);
      Date desde = new Date(hoy.getTimeInMillis());

      log.info("desde: " + desde);
      log.info("hasta: " + hasta);
      log.info("*************************************************");
        
//por si acaso
      String sql = "DELETE FROM "+NAME_TB_PRODUCTIVIDAD+" WHERE FECHA >= ? ";
      PreparedStatement ps = con.prepareStatement(sql);
      ps.setDate(1, desde);
      log.info("Delete desde: " + desde);
      ps.executeUpdate();
      Db.close(ps);
 
//Primera parte de la carga: ronda_id, local_id, fecha, segundos (picking normal)
      sql = "INSERT INTO "+NAME_TB_PRODUCTIVIDAD+" (" +
            "   RONDA_ID, " +
            "   LOCAL_ID, " +
            "   USUARIO_ID, " +
            "   FECHA, " +
            "   SEGUNDOS, " +
            "   SEGUNDOS_CAJA, " +
            "   TIPO_PICK, " +
            "   CANT_PICK, " +
            "   CANT_PICK_SUST, " +
            "   CANT_FALT, " +
            "   CANT_FALT_NO_SUST   ) " +
            "       SELECT " +
            "           R.ID_RONDA, " +
            "           R.ID_LOCAL, " +
            "           U.ID_USUARIO, " +
            "           DATE(FINI_PICKING) AS FECHA, " +
            "           (HOUR(FFIN_PICKING - FINI_PICKING)*3600 + MINUTE(FFIN_PICKING - FINI_PICKING)*60  + SECOND(FFIN_PICKING - FINI_PICKING)) AS SEGUNDOS, " +
            "           0, " +
            "           R.TIPO_PICKING, " +
            "           0," +
            "           0," +
            "           0," +
            "           0 " +
            "       FROM BODBA.BO_USUARIOS U INNER JOIN BODBA.BO_RONDAS R ON R.ID_USUARIO = U.ID_USUARIO " +
            "       WHERE ID_ESTADO = 13 " +
            "           AND FINI_PICKING IS NOT NULL " +
            "           AND FFIN_PICKING IS NOT NULL " +
            "           AND  FFIN_PICKING > FINI_PICKING " +
            "           AND R.TIPO_PICKING = 'N' " +
            "           AND FINI_PICKING >= ? " +
            "           AND FINI_PICKING < ? ";
      
      ps = con.prepareStatement(sql);
      ps.setDate(1, desde);
      ps.setDate(2, hasta);
      
      log.info("picking normal desde: " + desde + " - hasta: " + hasta);

      ps.executeUpdate();
      Db.close(ps);

      sql = "INSERT INTO "+NAME_TB_PRODUCTIVIDAD+" (" +
            "   RONDA_ID, " +
            "   LOCAL_ID, " +
            "   USUARIO_ID, " +
            "   FECHA, " +
            "   SEGUNDOS, " +
            "   SEGUNDOS_CAJA, " +
            "   TIPO_PICK, " +
            "   CANT_PICK, " +
            "   CANT_PICK_SUST, " +
            "   CANT_FALT, " +
            "   CANT_FALT_NO_SUST) " +
            "   SELECT R.ID_RONDA, R.ID_LOCAL, U.ID_USUARIO, DATE(FINI_PICKING) AS FECHA, " +
            "       CASE WHEN FECHA_INI_RONDA_PKL IS NULL " +
            "       THEN (HOUR(FFIN_PICKING - FINI_PICKING)*3600 + MINUTE(FFIN_PICKING - FINI_PICKING)*60  + SECOND(FFIN_PICKING - FINI_PICKING)) " +
            "       ELSE (HOUR(FINI_PICKING - FECHA_INI_RONDA_PKL)*3600 + MINUTE(FINI_PICKING - FECHA_INI_RONDA_PKL)*60  + SECOND(FINI_PICKING - FECHA_INI_RONDA_PKL)) END AS SEGUNDOS, " +
            "       (HOUR(FFIN_PICKING - FINI_PICKING)*3600 + MINUTE(FFIN_PICKING - FINI_PICKING)*60  + SECOND(FFIN_PICKING - FINI_PICKING)) AS SEGUNDOS_CAJA, " +
            "       R.TIPO_PICKING, 0,0,0,0 " +
            "   FROM BODBA.BO_USUARIOS U INNER JOIN BODBA.BO_RONDAS R ON R.ID_USUARIO = U.ID_USUARIO " +
            "   WHERE ID_ESTADO = 13 " +
            "   AND FINI_PICKING IS NOT NULL " +
            "   AND FFIN_PICKING IS NOT NULL " +
            "   AND  FFIN_PICKING > FINI_PICKING " +
            "   AND R.TIPO_PICKING = 'L' " +
            "   AND FINI_PICKING >= ? " +
            "   AND FINI_PICKING < ?";     
      
      
      ps = con.prepareStatement(sql);
      ps.setDate(1, desde);
      ps.setDate(2, hasta);
      log.info("picking light desde: " + desde + " - hasta: " + hasta);

      ps.executeUpdate();
      Db.close(ps);

      //NO sustituto
      sql = "MERGE INTO "+NAME_TB_PRODUCTIVIDAD+" AS IP USING ( " +
            "   SELECT " +
            "       DR.ID_RONDA, " +
            "       SUM(DR.CANT_PICK) AS CANT_PICK, " +
            "       SUM(DR.CANT_FALTAN) AS CANT_FALT " +
            "   FROM BODBA.BO_DETALLE_RONDAS DR INNER JOIN BODBA.BO_RONDAS R ON R.ID_RONDA = DR.ID_RONDA " +
            "   WHERE   DR.SUSTITUTO = 'N' " +
            "           AND R.FINI_PICKING >= ? " +
            "           AND R.FINI_PICKING < ? " +
            "           GROUP BY DR.ID_RONDA" +
            "   ) AS CANT " +
            "   ON IP.RONDA_ID = CANT.ID_RONDA " +
            "   WHEN MATCHED THEN " +
            "       UPDATE SET IP.CANT_PICK = CANT.CANT_PICK, IP.CANT_FALT = IP.CANT_FALT + CANT.CANT_FALT";
      ps = con.prepareStatement(sql);
      ps.setDate(1, desde);
      ps.setDate(2, hasta);
      log.info("NO sustituto desde: " + desde + " - hasta: " + hasta);

      ps.executeUpdate();
      Db.close(ps);
      //Sustituto
      sql = "MERGE INTO "+NAME_TB_PRODUCTIVIDAD+" AS IP USING ( " +
            "       SELECT  DR.ID_RONDA, " +
            "           SUM(DR.CANT_PICK) AS CANT_PICK, " +
            "           SUM(DR.CANT_FALTAN) AS CANT_FALT " +
            "       FROM BODBA.BO_DETALLE_RONDAS DR INNER JOIN BODBA.BO_RONDAS R ON R.ID_RONDA = DR.ID_RONDA " +
            "       WHERE   DR.SUSTITUTO = 'S' AND " +
            "           R.FINI_PICKING >= ? AND " +
            "           R.FINI_PICKING < ? " +
            "       GROUP BY DR.ID_RONDA" +
            "       ) AS CANT " +
            "   ON IP.RONDA_ID = CANT.ID_RONDA " +
            "   WHEN MATCHED THEN " +
            "           UPDATE SET IP.CANT_PICK_SUST = CANT.CANT_PICK, IP.CANT_FALT = IP.CANT_FALT + CANT.CANT_FALT";
      ps = con.prepareStatement(sql);
      ps.setDate(1, desde);
      ps.setDate(2, hasta);
      log.info("Sustituto desde: " + desde + " - hasta: " + hasta);

      ps.executeUpdate();

      //Faltante porque no se permite sustitucion
      sql = "MERGE  INTO "+NAME_TB_PRODUCTIVIDAD+" AS IP USING (" +
            "               SELECT " +
            "                   R.ID_RONDA,  " +
            "                   SUM(DR.CANT_FALTAN) AS FALT " +
            "               FROM BODBA.BO_RONDAS R INNER JOIN BODBA.BO_DETALLE_RONDAS DR ON R.ID_RONDA = DR.ID_RONDA INNER JOIN BODBA.BO_DETALLE_PEDIDO P ON P.ID_DETALLE = DR.ID_DETALLE  " +
            "               WHERE   P.ID_CRITERIO = 5 AND " +
            "                       R.FINI_PICKING >= ? AND " +
            "                       R.FINI_PICKING < ? " +
            "               GROUP BY R.ID_RONDA " +
            "               ) AS CANT " +
            "       ON IP.RONDA_ID = CANT.ID_RONDA  " +
            "       WHEN    MATCHED THEN " +
            "               UPDATE SET IP.CANT_FALT_NO_SUST = COALESCE(CANT.FALT, 0)";
      
      ps = con.prepareStatement(sql);
      ps.setDate(1, desde);
      ps.setDate(2, hasta);
      
      log.info("Faltante porque no se permite sustitucion desde: " + desde + " - hasta: " + hasta);
      
      ps.executeUpdate();
      Db.close(ps);
      conf.setUltimaFecha(hasta);
      Db.setConfiguracion(con, conf);
      
   }
}
