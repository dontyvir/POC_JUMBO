/*
 * Creado el 07-12-2007
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.cencosud.jumbo.home;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;

import cl.cencosud.jumbo.beans.BeanParamConfig;
import cl.cencosud.jumbo.scheduler.Scheduler;

/**
 * @author rbelmar
 *
 * Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class SYF {

	private static final String CONFIG_BUNDLE_NAME = "Config";
	static PropertyResourceBundle configBundle = (PropertyResourceBundle) PropertyResourceBundle.getBundle(CONFIG_BUNDLE_NAME);
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss.SSS");
	private static final SimpleDateFormat dateFormat2= new SimpleDateFormat("yyyyMMdd_HHmmss");
	private static Scheduler scheduler = new Scheduler();

	
	
	static BeanParamConfig getParamProperties() {
		int i=1;
		int salir=0;
		String Nombre = "";				
		String Clase = "";
		
		BeanParamConfig param = new BeanParamConfig();
		param.setNombreArchivo(configBundle.getString("NombreArchivo"));
		param.setTipo(configBundle.getString("Tipo"));
		param.setHorarios(parseaParam(configBundle.getString("Horarios")));
		param.setNumReintentos(configBundle.getString("NumReintentos"));
		param.setIntervalo(configBundle.getString("Intervalo"));
		
		param.setPuerto(configBundle.getString("mail.puerto"));
		param.setServer(configBundle.getString("mail.server"));
		param.setFrom(configBundle.getString("mail.from"));
		param.setTo(parseaParam(configBundle.getString("mail.to")));
		param.setCc(parseaParam(configBundle.getString("mail.cc")));
		param.setCco(parseaParam(configBundle.getString("mail.cco")));
		param.setSubject(configBundle.getString("mail.subject"));
		param.setMensaje(configBundle.getString("mail.mensaje"));
		param.setPathImagen(configBundle.getString("PathImagen"));
		
		param.setPorcentaje(Integer.parseInt(configBundle.getString("PorcentajeFaltante")));
		
		return param;
	}

	
    private static Map parseaParam(String listaHorarios){
        Map parametros = new LinkedHashMap();
		for (java.util.StringTokenizer stringtokenizer = new java.util.StringTokenizer(listaHorarios, ",", false); stringtokenizer.hasMoreTokens(); ) {
		    String valor = stringtokenizer.nextToken();
		    parametros.put(valor, valor);
		} 
    	return parametros;
    }
    

    public static void main(String[] args) {
		int hourOfDay, minute, second;
		//Map Locales = new LinkedHashMap();
		try{
			final SchedulingHome home = SchedulingHome.getHome();
            final BeanParamConfig param = getParamProperties();
			Map lstHorarios = param.getHorarios();
			
			for (Iterator it=lstHorarios.keySet().iterator(); it.hasNext(); ){
			    String key = it.next().toString();
			    int pos = key.indexOf("-");
			    
			    final String HoraIni = key.substring(0,pos);
			    final String HoraFin = key.substring(pos+1);
			    
				hourOfDay = Integer.parseInt(HoraFin.substring(0, 2));
				minute    = Integer.parseInt(HoraFin.substring(3, 5));
				second    = Integer.parseInt(HoraFin.substring(6, 8));
			    
				//PLANIFICA UNA TAREA PARA EJECUTARSE DIARIAMENTE Y A UNA MISMA HORA.
				/*SchedulerTask schedulTask = new SchedulerTask(){
					public void run() {*/
					    //Logging logger = new Logging(this);
						//System.out.println("TASKS - Fecha y Hora de planificación de la Tarea: " + dateFormat.format(new Date()));
						//logger.debug("\n\nTASKS - Fecha y Hora de planificación del Informe de S&F: " + dateFormat.format(new Date()));
						String Fecha = dateFormat2.format(new Date());
			            String filename= param.getNombreArchivo() + Fecha;
			            String Path = "";
			            try{
				            File fd = new File("temp");
				     	    Path = fd.getCanonicalPath();
				     	    //logger.debug("Path (temp):" + Path);
			    		}catch(IOException e){
			    		    e.printStackTrace();
			    		}
			    		home.EliminaArchivosXLS(Path + "/");
			            String RutaArchivoXLS = Path + "/"+ filename + ".xls";
			            //logger.debug("RutaArchivoXLS: " + RutaArchivoXLS);
			            //logger.debug("Jornada: " + HoraIni + " - " + HoraFin);
			            Map SustFaltLocal = home.getSustitutosFaltantes(HoraIni, HoraFin);
			            //Locales = home.getSustitutos(HoraIni, HoraFin, Locales);
						//logger.debug("getSustitutos.");
						//Locales  = home.getFaltantes(param.getPorcentaje(), HoraIni, HoraFin, Locales);
						//logger.debug("getFaltantes.");
						//home.GeneraExcel(sustitutos, faltantes, RutaArchivoXLS, param.getPathImagen(), HoraIni.subSequence(0,2).toString(), HoraFin.subSequence(0,2).toString());
						home.GeneraExcel(SustFaltLocal, RutaArchivoXLS, param.getPathImagen(), HoraIni.subSequence(0,2).toString(), HoraFin.subSequence(0,2).toString());
						//logger.debug("EXCEL generado con Exito.");
						home.CreaZipXLS(Path + "/", filename + ".xls");
						//logger.debug("COMPREASION A ZIP generado con Exito.");
						//home.EnviaEMail(param, Path + "/", filename + ".zip");
						//logger.debug("CORREO Enviado con Exito.");
				/*	}
				};
				scheduler.schedule(schedulTask, new DailyIterator(hourOfDay, minute, second));*/
			}
		}catch(Throwable theException){
			System.out.println("SchedulerTaskServlet.EjecutaTarea: ERROR:" + theException);
			theException.printStackTrace();
		}
    }
}
