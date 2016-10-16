/*
 * Created on 16-oct-2009
 */
package cl.cencosud.informes;

import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cl.cencosud.util.Db;
import cl.cencosud.util.Parametros;
import cl.cencosud.util.SendMail;
import org.apache.log4j.Logger;
/**
 * @author jdroguett
 */
public class Informes {
   private final static Logger log = Logger.getLogger(Informes.class.getName());
   private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
   final static String FLAG_MANUAL	= Parametros.getString("flagManual");

   public static void main(String[] args) throws Exception {
      String host = Parametros.getString("mail.smtp.host");
      String from = Parametros.getString("mail.from");
      String mail_cc_N = Parametros.getString("mail.cc_N");
      String mail_cc_L = Parametros.getString("mail.cc_L");
      String to = Parametros.getString("mail.to");
      String user = Parametros.getString("USER");
      String password = Parametros.getString("PASSWORD");
      String driver = Parametros.getString("DRIVER");
      String url = Parametros.getString("URL");
      String path = Parametros.getString("PATH");
      Connection con = Db.conexion(user, password, driver, url);
      
      boolean manual = "1".equals(FLAG_MANUAL)? true : false;
      
      if (!manual) {
        log.debug("Db.conexion("+driver+","+ url+") ");
        log.info("****** Inicio Proceso Crontab ******");
      
        /** Limpiar y generar datos en BODBA.IPRODUCTIVIDAD */
        Resumen resumen = new Resumen();
      resumen.generar(con);
      
      
        /** INI - ESTO ES PARA EL PROCESO AUTOMATICO 
         *  Hasta es el domingo de la última semana
         */ 
        //*
      GregorianCalendar hasta = new GregorianCalendar(); //hoy
      hasta.setFirstDayOfWeek(Calendar.MONDAY); //la semana comienza el lunes
      hasta.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //lunes de la semana actual
      hasta.add(Calendar.DAY_OF_YEAR, -1); //domingo
      GregorianCalendar hastaN1 = (GregorianCalendar) hasta.clone();
      GregorianCalendar hastaN2 = (GregorianCalendar) hasta.clone();
      GregorianCalendar hastaL1 = (GregorianCalendar) hasta.clone();
      GregorianCalendar hastaL2 = (GregorianCalendar) hasta.clone();
        /*/*/
      /** FIN - ESTO ES PARA EL PROCESO AUTOMATICO **/
      
        /** Fecha de hoy **/
        GregorianCalendar diaHoy = new GregorianCalendar(); //dia de hoy
        					diaHoy.set(Calendar.HOUR_OF_DAY, 0);
        					diaHoy.set(Calendar.MINUTE, 0);
        					diaHoy.set(Calendar.SECOND, 0);
        					diaHoy.set(Calendar.MILLISECOND, 0);
        log.info("fecha start: "  + sdf.format(diaHoy.getTime()));
        /** ************ **/
      
      Productividad produccion = new Productividad();
      Faltantes faltantes = new Faltantes();

      /** Obtiene Productividad **/
        
        //Productividad Normal
      File archivoN = produccion.generarN(con, path, hastaN1);
      
        //Faltantes Normal
      File archivoFaltantesN = faltantes.generarN(con, path, hastaN2);
      
        //Productividad Light
      File archivoL = produccion.generarL(con, path, hastaL1);
      
        //Faltante Light
      File archivoFaltantesL = faltantes.generarL(con, path, hastaL2);
        /** ********************* **/
      Db.close(con);
      
        log.info("Coneccion Cerrada ... ");

      List archivosN = new ArrayList();
      archivosN.add(archivoN);
      archivosN.add(archivoFaltantesN);
      
      List archivosL = new ArrayList();
      archivosL.add(archivoL);
      archivosL.add(archivoFaltantesL);

        
      String bodyHtml = "Estimados <br /> Junto con saludarlos, adjunto informe semanal de Productividad y Faltantes para ser publicado en <b> Mural de Indicadores Jumbo.cl.</b>"
            + "<br/><br/><br/>--<br/>Envío automático de informes.";
      
        Informes informes = new Informes();
        informes.enviarMail(host, from, bodyHtml, archivosN, to, mail_cc_N);
      informes.enviarMail(host, from, bodyHtml, archivosL, to, mail_cc_L);
      
        log.info("****** Fin Proceso ******");		
	} else {
        log.info("Proceso No ejectutado : Modalidad Manual");
	}

   }

   /**
    * @param host
    * @param from
    * @param bodyHtml
    * @throws Exception
    */
   private void enviarMail(String host, String from, String bodyHtml, List archivos, String to, String cc) throws Exception {
      SendMail email = new SendMail(host, from, bodyHtml, archivos);
      email.enviar(to, cc, "Informe de Productividad y Faltantes");
   }
}
