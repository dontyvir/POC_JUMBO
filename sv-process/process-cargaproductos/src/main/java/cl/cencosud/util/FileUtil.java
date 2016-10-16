package cl.cencosud.util;

import java.io.EOFException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import cl.cencosud.filtro.FiltroER;

public class FileUtil {

	static {
		LogUtil.initLog4J();
	}
	private static Logger logger = Logger.getLogger(FileUtil.class);
	
	public static final String expresionGZ = "JCPJ\\d{13}+(\\.(?i)(gz))";

	/**
	 * Lista archivos con extension .gz
	 **/		
	public static String[] listaArchivosExtGZ(String path) {
		File directorio = new File(path);	      
		String[] lista = directorio.list(new FiltroER(expresionGZ));
		if ( lista == null ) {
			return new String[] {};
		}
		if ( lista.length > 1 ) {
			Arrays.sort(lista);
		}
		return lista;
	}
	
	/**
	 * Remueve la extension de un archivo
	 * 	
	 * @param file nombre de archivo
	 * @return String
	 */
	public static String stripExtension(final String file){
	    return file != null && file.lastIndexOf(".") > 0 ? file.substring(0, file.lastIndexOf(".")) : file;
	}
	
	
	public static List validaArchivosGZ(String[] archivos, String path) {
		FileGzip fgz = new FileGzip();
		List lista = new ArrayList();
		for (int i = 0; i < archivos.length; i++) {
			try {
				String decompressedFile = FileUtil.stripExtension(archivos[i]);
				fgz.unGunzipFile(path + archivos[i], path + decompressedFile);
			}catch(EOFException eofe){
				lista.add(archivos[i]);
				logger.error("Error al comprobar archivo " + archivos[i]+ ", Exception:" + eofe.getMessage());
			} catch (Exception e) {
				lista.add(archivos[i]);
				logger.error("Error archivo " + archivos[i]+ ", Exception:" + e.getMessage());
			}
		}
		return lista;
	}
	
	public static String local(String nombre) {
		String expresion = "JCPJ\\d{3}";
		Pattern p = Pattern.compile(expresion);
		Matcher m = p.matcher(nombre);
		if (m.find()) {
			return m.group().substring(3);
		}
		return null;
	}
	
	/**
	 * Entrega la lista de archivos JCP encontrados del día actual.
	 * 
	 * @param path
	 * @return Lista de nombres de archivos ordenados del más antiguo al más
	 *         nuevo (orden según nombre)
	 */
	public static String[] nombresArchivos(String path) {
		File directorio = new File(path);
		// String expresion = "JCPJ5\\d{2}";
		String expresion = "JCPJ\\d{13}";
		// GregorianCalendar hoy = new GregorianCalendar();
		// int mes = hoy.get(Calendar.MONTH) + 1;
		// int dia = hoy.get(Calendar.DAY_OF_MONTH);
		// expresion += (mes > 9 ? mes + "" : "0" + mes);
		// expresion += (dia > 9 ? dia + "" : "0c" + dia);
		// expresion += "\\d{6}";

		String[] lista = directorio.list(new FiltroER(expresion));
		if (lista == null) {
			return new String[] {};
		}

		if (lista.length > 1) {
			Arrays.sort(lista);
		}
		return lista;
	}
	
	/**
	 * Devuelve una lista con los nombres de las interfaces, 
	 * cuando su tamaño es igual Cero
	 * 
	 *  @param archivos, Array que contiene la lista de archivos
	 *  @param path, Directorio de interfaces
	 *  
	 *   @return List
	 * */
	public static List getSizeFile(String[] archivos, String path){		
		List lista = new ArrayList();
		File  file = null;
		for (int i = 0; i < archivos.length; i++) {
			try {
				file = new File(path + archivos[i]);			   			   
				if (file.length()==0){
					lista.add(archivos[i]);
				}					
			} catch (Exception e) {
				logger.error("Error al obtener el tamaño de la interfaz " + archivos[i]+ ", Exception:" + e);
			}
		}
		return lista;
				
	}

}
