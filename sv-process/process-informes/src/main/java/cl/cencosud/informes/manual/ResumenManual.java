package cl.cencosud.informes.manual;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import cl.cencosud.informes.datos.Configuracion;
import cl.cencosud.util.Db;
import cl.cencosud.util.Parametros;

public class ResumenManual {
	
   private final static Logger log 	= Logger.getLogger(ResumenManual.class.getName());	

 /* 
   public static void main(String[] args) throws Exception {
      String user = Parametros.getString("USER");
      String password = Parametros.getString("PASSWORD");
      String driver = Parametros.getString("DRIVER");
      String url = Parametros.getString("URL");
      Connection con = Db.conexion(user, password, driver, url);

      ResumenManual resumen = new ResumenManual();
      resumen.generar(con);
      Db.close(con);
   }
*/
   /**
    * Genera el resumen principal
    * 
    * @param con
 * @param paramDTO 
    * @throws SQLException
    */
   public void generar(Connection con, ParamDTO paramDTO) throws SQLException {
 
      java.sql.Date desde =null;
      
      //Manual

	  // ENERO = 0
      GregorianCalendar gregDesde = new GregorianCalendar();
      
      gregDesde.setFirstDayOfWeek(Calendar.MONDAY);
      
	  gregDesde.set(paramDTO.getAgno_ini(), paramDTO.getMes_ini()-1, paramDTO.getDia_ini());
	  
	  desde = new java.sql.Date(gregDesde.getTimeInMillis());
       
      GregorianCalendar hoy = new GregorianCalendar();
      hoy.set(Calendar.HOUR_OF_DAY, 0);
      hoy.set(Calendar.MINUTE, 0);
      hoy.set(Calendar.SECOND, 0);
      hoy.set(Calendar.MILLISECOND, 0);
          hoy.add(Calendar.DATE, -3);
      
      java.sql.Date hasta = new java.sql.Date(hoy.getTimeInMillis());

      log.info("***************** TRAMO de REPORTE *************************");
      log.info("desde: " + desde);
      log.info("hasta: " + hasta);
      log.info("************************************************************");

      /**
       * por si acaso
       */
      log.info("... Delete desde: " + desde);
      String sql = "DELETE FROM BODBA.IPRODUCTIVIDAD WHERE FECHA >= ? ";
      PreparedStatement ps = con.prepareStatement(sql);
      ps.setDate(1, desde);
      ps.executeUpdate();
      Db.close(ps);
      log.info("... Delete ok");
 
      /**
       * Primera parte de la carga: ronda_id, local_id, fecha, segundos (picking normal)
       */
      log.info("Primera parte de la carga - picking normal desde: " + desde + " - hasta: " + hasta);
      sql = "INSERT INTO BODBA.IPRODUCTIVIDAD (" +
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
      ps.executeUpdate();
      Db.close(ps);
      log.info("... Primer Insert ok");

      /**
       * Segunda parte de la carga
       */
      log.info("Segunda parte de la carga - picking light desde: " + desde + " - hasta: " + hasta);
      sql = "INSERT INTO BODBA.IPRODUCTIVIDAD (" +
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
      ps.executeUpdate();
      Db.close(ps);
      log.info("... Segundo Insert ok");
      
      /**
       * NO sustituto
       */
      log.info("MERGE NO sustituto desde: " + desde + " - hasta: " + hasta);

      sql = "MERGE INTO BODBA.IPRODUCTIVIDAD AS IP USING ( " +
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
      ps.executeUpdate();
      Db.close(ps);
      log.info("... MERGE NO sustituto OK");
      
      /**
       * Sustituto
       */
      log.info(" MERGE Sustituto desde: " + desde + " - hasta: " + hasta);

      sql = "MERGE INTO BODBA.IPRODUCTIVIDAD AS IP USING ( " +
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
      ps.executeUpdate();
      log.info("... MERGE NO sustituto OK");
      
      /**
       * Faltante porque no se permite sustitucion
       */
      log.info("MERGE Faltante porque no se permite sustitucion desde: " + desde + " - hasta: " + hasta);

      sql = "MERGE  INTO BODBA.IPRODUCTIVIDAD AS IP USING (" +
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
      ps.executeUpdate();
      Db.close(ps);
      log.info("... MERGE Faltante OK");
      log.info("... Se guarda ULTIMA FECHA SEMANAL : " + hasta);
   /*   
      if (!manual) {
        conf.setUltimaFechaSemanal(hasta);
        Db.setConfiguracionSemanal(con, conf);
      }
*/
      
   }
}
