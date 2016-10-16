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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;

import cl.cencosud.jumbo.scheduler.Scheduler;
import cl.cencosud.jumbo.scheduler.SchedulerTask;
import cl.cencosud.jumbo.beans.BeanLocal;
import cl.cencosud.jumbo.beans.BeanParamConfig;
import cl.cencosud.jumbo.home.SchedulingHome;
import cl.cencosud.jumbo.iterator.DailyIterator;
import cl.cencosud.jumbo.log.Logging;

/**
 * @author rbelmar
 *
 * Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class Tracking {

	private final String CONFIG_BUNDLE_NAME = "Config";
	PropertyResourceBundle configBundle = (PropertyResourceBundle) PropertyResourceBundle.getBundle(CONFIG_BUNDLE_NAME);
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss.SSS");
	//private final SimpleDateFormat dateFormat2= new SimpleDateFormat("yyyyMMdd_HHmmss");
	private final SimpleDateFormat dateFormat2= new SimpleDateFormat("yyyyMMdd");
	private Scheduler scheduler = new Scheduler();

	Logging logger = new Logging(this);
	
	
	BeanParamConfig getParamProperties() {
		int i=1;
		int salir=0;
		String Nombre = "";
		String Clase = "";
		
		BeanParamConfig param = new BeanParamConfig();
		param.setNombreArchivo(configBundle.getString("NombreArchivo"));
		param.setTipo(configBundle.getString("Tipo"));
		param.setHorarios(parseaParam(configBundle.getString("Horarios")));//UltimaJornada
		param.setIntervalo(configBundle.getString("Intervalo"));
		
		param.setPuerto(configBundle.getString("mail.puerto"));
		param.setServer(configBundle.getString("mail.server"));
		param.setFrom(configBundle.getString("mail.from"));
		param.setTo(parseaParam(configBundle.getString("mail.to")));
		param.setCc(parseaParam(configBundle.getString("mail.cc")));
		param.setCco(parseaParam(configBundle.getString("mail.cco")));
		param.setSubject(configBundle.getString("mail.subject"));
		param.setMensaje(configBundle.getString("mail.mensaje"));
		param.setPathImagen1(configBundle.getString("PathImagen1"));
		param.setPathImagen2(configBundle.getString("PathImagen2"));
		param.setEliminarArchivosTemporales(configBundle.getString("EliminarArchivosTemporales"));
		
		return param;
	}

	
    private Map parseaParam(String listaHorarios){
        Map parametros = new LinkedHashMap();
		for (java.util.StringTokenizer stringtokenizer = new java.util.StringTokenizer(listaHorarios, ",", false); stringtokenizer.hasMoreTokens(); ) {
		    String valor = stringtokenizer.nextToken();
		    parametros.put(valor, valor);
		}
    	return parametros;
    }
    

    public void inicia() {
		int hourOfDay=0, minute=0, second=0;
		String Local = "";
		//Map Locales = new LinkedHashMap();
		try{
			final SchedulingHome home = SchedulingHome.getHome();
            final BeanParamConfig param = getParamProperties();
			Map lstHorarios = param.getHorarios();
			
			for (Iterator it=lstHorarios.keySet().iterator(); it.hasNext(); ){
			    String key = it.next().toString();
			    
			    final String HoraIni = key;
			    
				hourOfDay = Integer.parseInt(HoraIni.substring(0, 2));
				minute    = Integer.parseInt(HoraIni.substring(3, 5));
				second    = Integer.parseInt(HoraIni.substring(6, 8));
			    
				//PLANIFICA UNA TAREA PARA EJECUTARSE DIARIAMENTE Y A UNA MISMA HORA.
				SchedulerTask schedulTask = new SchedulerTask(){
					public void run() {
						logger.debug("\n\nTASKS - Fecha y Hora de planificación del Informe de S&F: " + dateFormat.format(new Date()));
			            String Path = "";
			            try{
				            File fd = new File("temp");
				     	    Path = fd.getCanonicalPath() + "/";
				     	    logger.debug("Path (temp):" + Path);
			    		}catch(IOException e){
			    		    e.printStackTrace();
			    		}
			    		if (param.getEliminarArchivosTemporales().equalsIgnoreCase("S")){
				    		home.EliminaArchivosXLS(Path);
			    		}
			            
			            Calendar cal = Calendar.getInstance();
			            cal.add(Calendar.DATE, -1);
			            
			            logger.debug("Fecha: " + dateFormat.format(cal.getTime()));
					
						List loc = home.listadoLocales();
						for (int i=0; i < loc.size(); i++){
						    BeanLocal local = (BeanLocal)loc.get(i);
						    logger.debug("******  LOCAL: " + local.getNom_local() + "  ******");
						    String Fecha = dateFormat2.format(cal.getTime());
				            String filename= param.getNombreArchivo() + local.getNom_local() + "_" + Fecha + ".xls";
				            String RutaArchivoXLS = Path + filename;
				            logger.debug("RutaArchivoXLS: " + RutaArchivoXLS);
				            
						    Map ListadoOP = home.getListadoOP(cal, local.getId_local(), "todos");
						    if (ListadoOP.size() > 0){
							    home.GeneraExcel(ListadoOP, local, RutaArchivoXLS, param, cal);
								logger.debug("EXCEL generado con Exito.");
								home.CreaZipXLS(Path, filename);
								logger.debug("COMPREASION A ZIP generado con Exito.");
						    }
						}
						
						HashMap archivos = null;
						archivos = home.RecuperaListaArchivos(Path);
						if (archivos != null && archivos.size() > 0 ){
							home.EnviaEMail(param, archivos);
							logger.debug("CORREO Enviado con Exito.");
						}
					}
				};
				scheduler.schedule(schedulTask, new DailyIterator(hourOfDay, minute, second));
			}
		}catch(Throwable theException){
			System.out.println("SchedulerTaskServlet.EjecutaTarea: ERROR:" + theException);
			theException.printStackTrace();
		}
    }
}
