/*
 * Created on 16-oct-2009
 */
package cl.cencosud.informes.diario;

import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.MissingResourceException;

import org.apache.log4j.Logger;

import cl.cencosud.util.Db;
import cl.cencosud.util.Parametros;
import cl.cencosud.util.SendMail;

/**
 * @author Sebastián Bernal 
 * @version
 */
public class InformesDiarios {

private final static Logger log = Logger.getLogger(InformesDiarios.class.getName());
private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

   public static void main(String[] args) throws Exception {
	  
	  log.info("****** Inicio Proceso ******");
	      
	  String host = Parametros.getString("mail.smtp.host");
      String from = Parametros.getString("mail.from");
      String mail_cc_N = Parametros.getString("days.mail.cc_N");
      String mail_cc_L = Parametros.getString("days.mail.cc_L");
      String to = Parametros.getString("days.mail.to");

      String user = Parametros.getString("USER");
      String password = Parametros.getString("PASSWORD");
      String driver = Parametros.getString("DRIVER");
      String url = Parametros.getString("URL");
      String path = Parametros.getString("PATH");
      Connection con = Db.conexion(user, password, driver, url);
      
      Resumen resumen = new Resumen();
      
      /** Limpia y carga los datos BODBA.IPRODUCTIVIDAD */
      resumen.generar(con, -ProductividadDiaria.DIAS_ATRAS);
 
      String fechaStart = null;
      try{
      	fechaStart = Parametros.getBundle().getString("days.date"); //fecha cargada para el proceso
      	log.info(" fecha start cargada desde properties "+ fechaStart);
      } catch (MissingResourceException e) {
    	  log.debug("la fecha no ha sido cargada desde properties");
      }
      	
      GregorianCalendar dia = new GregorianCalendar(); //dia de hoy
      dia.set(Calendar.HOUR_OF_DAY, 0);
      dia.set(Calendar.MINUTE, 0);
      dia.set(Calendar.SECOND, 0);
      dia.set(Calendar.MILLISECOND, 0);
      
      inicializacionDAYSYEAR(dia);
      
      if(fechaStart!=null){
    	  try{
	     	  int year =Integer.valueOf(fechaStart.split("-")[0]).intValue(); 
	    	  int month =Integer.valueOf(fechaStart.split("-")[1]).intValue()-1; 
	    	  int date =Integer.valueOf(fechaStart.split("-")[2]).intValue(); 
	    	  dia.set(year, month, date);
    	  }catch (Exception e) {
    		  log.fatal("error en definir el parametro days.date en properties",e);
    		  throw e;
		}
      }
      
      
      log.info("fecha start: "  + sdf.format(dia.getTime()));
      
      GregorianCalendar diaN1 = (GregorianCalendar) dia.clone();
      GregorianCalendar diaN2 = (GregorianCalendar) dia.clone();
      GregorianCalendar diaL1 = (GregorianCalendar) dia.clone();
      GregorianCalendar diaL2 = (GregorianCalendar) dia.clone();
  
      /** Obtiene Productividad **/
      ProductividadDiariaNormal produccionN = new ProductividadDiariaNormal();
      File archivoProductividadN = produccionN.generarN(con, path, diaN1);
      
      ProductividadDiariaLight produccionL = new ProductividadDiariaLight();
      File archivoProductividadL = produccionL.generarL(con, path, diaL1);
      
      /** Obtiene Faltantes **/
      FaltantesDiariosNormal faltantesN = new FaltantesDiariosNormal();
      File archivoFaltantesN = faltantesN.generarNDiario(con, path, diaN2);
      
      FaltantesDiariosLight faltantesL = new FaltantesDiariosLight();
      File archivoFaltantesL = faltantesL.generarLDiario(con, path, diaL2);
      
      /** Cierra coneccion */
      Db.close(con);
      log.info("Coneccion Cerrada ... ");
      
      List archivosN = new ArrayList();
      List archivosL = new ArrayList();
      
      archivosN.add(archivoFaltantesN);
      archivosL.add(archivoFaltantesL);

      archivosN.add(archivoProductividadN);
      archivosL.add(archivoProductividadL);
      
      log.info("archivo generado faltante normal:"+archivoFaltantesN.getName());
      log.info("archivo generado faltante light:"+archivoFaltantesL.getName());
      log.info("archivo generado productividad normal:"+archivoProductividadN.getName());
      log.info("archivo generado productividad light:"+archivoProductividadL.getName());
      
      String bodyHtml = "Estimados <br /> Junto con saludarlos, adjunto informe diario de Productividad y Faltantes para ser publicado en <b> Mural de Indicadores Jumbo.cl.</b>"
            + "<br/><br/><br/>--<br/>Envío automático de informes.";   
      
      InformesDiarios informes = new InformesDiarios();
      
      informes.enviarMail(host, from, bodyHtml, archivosN, to, mail_cc_N);    
      informes.enviarMail(host, from, bodyHtml, archivosL, to, mail_cc_L);
      
      log.info("****** Fin Proceso ******");
      
   }

   /**
    * @param host
    * @param from
    * @param bodyHtml
    * @throws Exception
    */
   private void enviarMail(String host, String from, String bodyHtml, List archivos, String to, String cc) throws Exception {
      SendMail email = new SendMail(host, from, bodyHtml, archivos);
      email.enviar(to, cc, "Informe Diario de Productividad y Faltantes");
   }
   
   private static void inicializacionDAYSYEAR(GregorianCalendar fecha1){
	   	
	   	GregorianCalendar fecha = (GregorianCalendar) fecha1.clone();
		
	    fecha.add(Calendar.DAY_OF_YEAR,-ProductividadDiaria.DIAS_ATRAS);
		fecha.set(Calendar.MONTH,11 );
		fecha.set(Calendar.DAY_OF_MONTH,31);
		fecha.set(Calendar.HOUR_OF_DAY,23 );
		fecha.set(Calendar.MINUTE, 59);
		fecha.set(Calendar.SECOND, 59);
		fecha.set(Calendar.MILLISECOND,999);
		
		ProductividadDiaria.DAYS_YEAR=fecha.get(Calendar.DAY_OF_YEAR);
		log.info("cantidad de dias del año en donde parte el proceso (fecha desde):" + fecha.get(Calendar.DAY_OF_YEAR));
			
	}
   
}
