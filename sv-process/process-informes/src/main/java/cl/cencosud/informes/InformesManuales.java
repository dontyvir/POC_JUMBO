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
 * @author rbelmar
 */
public class InformesManuales {
	   private final static Logger log = Logger.getLogger(InformesManuales.class.getName());
	   private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	   final static String FLAG_MANUAL	= Parametros.getString("flagManual");
	   final static int DIA_FIN			= Integer.parseInt(Parametros.getString("dia.fin"));
	   final static int MES_FIN			= Integer.parseInt(Parametros.getString("mes.fin"));
	   final static int AGNO 			= Integer.parseInt(Parametros.getString("agno"));  

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
      
	  boolean manual = "1".equals(FLAG_MANUAL)? true : false;
	      
      Connection con = Db.conexion(user, password, driver, url);
      
	      log.debug("Db.conexion reporte Manual("+driver+","+ url+") ");
	      log.info("****** Inicio Proceso Manual ******");
	      
	      /** Limpiar y generar datos en BODBA.IPRODUCTIVIDAD */
      Resumen resumen = new Resumen();
      resumen.generar(con);
      
	      GregorianCalendar hastaN1 = null;
	      GregorianCalendar hastaN2 = null;
	      GregorianCalendar hastaL1 = null;
	      GregorianCalendar hastaL2 = null;
      
	      /** INI - ESTO ES PARA EL PROCESO MANUAL, PETICIONES DE OPERACION **/
		      /* ENERO = 0 */

		      GregorianCalendar hasta = new GregorianCalendar();
		      hasta.setFirstDayOfWeek(Calendar.MONDAY);
		      //TODO HASTA 
		      hasta.set(AGNO, MES_FIN-1, DIA_FIN);
		      hastaN1 = (GregorianCalendar) hasta.clone();
		      hastaN2 = (GregorianCalendar) hasta.clone();
		      hastaL1 = (GregorianCalendar) hasta.clone();
		      hastaL2 = (GregorianCalendar) hasta.clone();

		      /** FIN - ESTO ES PARA EL PROCESO MANUAL, PETICIONES DE OPERACION **/

	      /** Fecha de hoy **/
	      GregorianCalendar diaHoy = new GregorianCalendar(); //dia de hoy
	      					diaHoy.set(Calendar.HOUR_OF_DAY, 0);
	      					diaHoy.set(Calendar.MINUTE, 0);
	      					diaHoy.set(Calendar.SECOND, 0);
	      					diaHoy.set(Calendar.MILLISECOND, 0);
	      /** ************ **/

      Productividad produccion = new Productividad();
      Faltantes faltantes = new Faltantes();

	      /** Obtiene Productividad **/
      
	      //Productividad Normal
	      File archivoN 			= produccion.generarN(con, path, hastaN1);
      
	      //Faltantes Normal
	      File archivoFaltantesN 	= faltantes.generarN(con, path, hastaN2);
      
	      //Productividad Light
	      File archivoL 			= produccion.generarL(con, path, hastaL1);

	      //Faltante Light
	      File archivoFaltantesL 	= faltantes.generarL(con, path, hastaL2);
	      /** ********************* **/
      Db.close(con);
      
	      log.info("Coneccion para Report Manual Cerrada ... ");

      List archivosN = new ArrayList();
      archivosN.add(archivoN);
      archivosN.add(archivoFaltantesN);
      
	      List archivosL = new ArrayList();
	      archivosL.add(archivoL);
	      archivosL.add(archivoFaltantesL);


      String bodyHtml = "Estimados <br /> Junto con saludarlos, adjunto informe semanal de Productividad y Faltantes para ser publicado en <b> Mural de Indicadores Jumbo.cl.</b>"
            + "<br/><br/><br/>--<br/>Envío automático de informes.";
      
	  InformesManuales informesManual = new InformesManuales();
	  informesManual.enviarMailManual(host, from, bodyHtml, archivosN, to, mail_cc_N);
	  informesManual.enviarMailManual(host, from, bodyHtml, archivosL, to, mail_cc_L);
      
      log.info("****** Fin Proceso para Report Manual ******");
   }

   /**
    * @param host
    * @param from
    * @param bodyHtml
    * @throws Exception
    */
   private void enviarMailManual(String host, String from, String bodyHtml, List archivos, String to, String cc) throws Exception {
      SendMail email = new SendMail(host, from, bodyHtml, archivos);
      email.enviar(to, cc, "Informe de Productividad y Faltantes");
   }
}
