/*
 * Created on 05-Nov-2013
 */

package cl.cencosud.informes.manual;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import cl.cencosud.util.Db;
import cl.cencosud.util.ParametrosManual;
import cl.cencosud.util.SendMail;

public class InformesManualBOC {
	   private final static Logger log = Logger.getLogger(InformesManualBOC.class.getName());
	  // private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*
	   final static int DIA_INI			= 16;
	   final static int MES_INI			= 12;
	   final static int DIA_FIN			= 13;
	   final static int MES_FIN			=1;
	   final static int AGNO_INI 			= 2013; 
	   final static int AGNO_FIN 			= 2014; 
	   */
   public static void main(String[] args) throws Exception {
	   ParamDTO paramDTO= new ParamDTO();

	   paramDTO.setDia_ini(Integer.parseInt(args[0]));
	   paramDTO.setMes_ini(Integer.parseInt(args[1]));
	   paramDTO.setDia_fin(Integer.parseInt(args[2]));
	   paramDTO.setMes_fin(Integer.parseInt(args[3]));
	   paramDTO.setAgno_ini(Integer.parseInt(args[4]));
	   paramDTO.setAgno_fin(Integer.parseInt(args[5]));

	   /*
	   paramDTO.setDia_ini(DIA_INI);
	   paramDTO.setMes_ini(MES_INI);
	   paramDTO.setDia_fin(DIA_FIN);
	   paramDTO.setMes_fin(MES_FIN);
	   paramDTO.setAgno_ini(AGNO_INI);
	   paramDTO.setAgno_fin(AGNO_FIN);
*/
	   
	   log.info("***********************************************"); 
	 //  log.info("**** Arguments*****" + dia_ini+" "+mes_ini+" "+dia_fin+" "+mes_fin+" "+agno+" "+flag_manual);
	   log.info("**** Param DTO*****" + paramDTO.getDia_ini()+" "+paramDTO.getMes_ini()+" "+paramDTO.getDia_fin()+" "+paramDTO.getMes_fin()+" "+paramDTO.getAgno_ini()+" "+paramDTO.getAgno_fin());
	   log.info("***********************************************"); 
	   
	  String host = ParametrosManual.getString("mail.smtp.host");
      String from = ParametrosManual.getString("mail.from");
      String mail_cc_N = ParametrosManual.getString("mail.cc_N");
      String mail_cc_L = ParametrosManual.getString("mail.cc_L");
      String to = ParametrosManual.getString("mail.to");
      String user = ParametrosManual.getString("USER");
      String password = ParametrosManual.getString("PASSWORD");
      String driver = ParametrosManual.getString("DRIVER");
      String url = ParametrosManual.getString("URL");
      String path = ParametrosManual.getString("PATH");
      
	
      Connection con = Db.conexion(user, password, driver, url);
      
	      log.debug("Db.conexion reporte Manual("+driver+","+ url+") ");
	      log.info("*********************************** Inicio Proceso Manual ******************************");
	      
	      /** Limpiar y generar datos en BODBA.IPRODUCTIVIDAD */
      ResumenManual resumenManual = new ResumenManual();
      resumenManual.generar(con,paramDTO);
      
	      GregorianCalendar hastaN1 = null;
	      GregorianCalendar hastaN2 = null;
	      GregorianCalendar hastaL1 = null;
	      GregorianCalendar hastaL2 = null;
      
	      /** INI - ESTO ES PARA EL PROCESO MANUAL, PETICIONES DE OPERACION **/
		      /* ENERO = 0 */

		      GregorianCalendar hasta = new GregorianCalendar();
		      hasta.setFirstDayOfWeek(Calendar.MONDAY);
		     
		      hasta.set(paramDTO.getAgno_fin(), paramDTO.getMes_fin()-1, paramDTO.getDia_fin());
		      
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

      ProductividadManual produccionManual = new ProductividadManual();
      FaltantesManual faltantesManual = new FaltantesManual();

	      /** Obtiene Productividad **/
      
	      //Productividad Normal
	      File archivoN 			= produccionManual.generarN(con, path, hastaN1,paramDTO);
      
	      //Faltantes Normal
	      File archivoFaltantesN 	= faltantesManual.generarN(con, path, hastaN2, paramDTO);
      
	      //Productividad Light
	      File archivoL 			= produccionManual.generarL(con, path, hastaL1,paramDTO);

	      //Faltante Light
	      File archivoFaltantesL 	= faltantesManual.generarL(con, path, hastaL2,paramDTO);
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
      
	  InformesManualBOC informesManual = new InformesManualBOC();
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
