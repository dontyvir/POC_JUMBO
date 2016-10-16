/*
 * Created on 18-oct-2012
 *
 */
package cl.cencosud.informes.diario;

import java.io.File;
import java.sql.Connection;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import cl.cencosud.util.Db;

/**
 * @author Victor Matheu
 * @version 18/10/2012
 */
public class FaltantesDiariosLight extends FaltantesDiarios {
	
	private final static Logger log = Logger.getLogger(FaltantesDiariosLight.class.getName());
	
	/**
	 * Calcula los faltantes del dia que viene en el parametro dia 
	 * @param con
	 * @param path carpeta donde registrara el archivo.
	 * @param dia a este parametro se le reste un dia del año para realizar el calculo
	 * @return
	 * @throws Exception
	 */
	public File generarLDiario(Connection con, String path, GregorianCalendar dia) throws Exception {
		   
		   log.info("inicio proceso faltantes light");
		
		   dia.add(Calendar.DAY_OF_YEAR,-1);   
		   int diaAno = dia.get(Calendar.DAY_OF_YEAR);
	       
	       log.info("dia: " + new Date(dia.getTimeInMillis()));
	       log.info("dia año: " + diaAno);
	       
	       Hashtable datos = getPorLocal(con, dia);
	       
	       List locales = Db.getLocales(con, "L");
	       File archivo = new File(path + "faltantes-L-dia-" + sdf.format(new Date(dia.getTimeInMillis())) + ".xls");
	       
	       imprimir(archivo, locales, datos);
	       
	       log.info("fin proceso faltantes light");
	       
	       return archivo; 
	   }
	
}
