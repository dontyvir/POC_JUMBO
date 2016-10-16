/*
 * Creado el 16-08-2007
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.jumbocl.eventos.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import net.sf.fastm.IValueSet;
import net.sf.fastm.ValueSet;

/**
 * @author imoyano
 * 
 * Para cambiar la plantilla de este comentario generado, vaya a Ventana -
 * Preferencias - Java - Estilo de código - Plantillas de código
 */
public class EventosUtil {
    
    /**
     * Este metodo nos devuelve el nombre del archivo almacenado en el directorio. Se ocupa ya que el codigo para flash es case-sensitive
     * y puede ser que al evento le asignen un nombre con minuscula, siendo que el nombre real es con mayuscula (por ejemplo)
     * 
     * @param nombreArchivo nombre del flash guardado en la base de datos
     * @param dirFlash directorio donde se guardan los flash
     * @return nombre real
     */
//    public static String obtieneElNombreDelArchivo(String nombreArchivo, File dirFlash) {
//        if (dirFlash.exists()) {
//            for (int i = 0; i < dirFlash.listFiles().length; i++) {
//                File file = (File) dirFlash.listFiles()[i];
//                if (file.getName().equalsIgnoreCase(nombreArchivo)) {
//                    return file.getName();
//                }                
//                
//            }		
//		}
//        return nombreArchivo;
//    }
    
    /**
     * Este metodo nos permite eliminar todos los archivos antiguos de un directorio en particular
     * 
     * @param directorioDeArchivos Directorio donde estan losa rchivos a eliminar
     */
    public static void eliminarArchivosAntiguos(File directorioDeArchivos) {
        Calendar cAyer = Calendar.getInstance();
        cAyer.setFirstDayOfWeek(Calendar.MONDAY);
        cAyer.add(Calendar.DAY_OF_YEAR, -1);
        java.util.Date dAyer = new java.util.Date();
        dAyer = cAyer.getTime();
        
        if (directorioDeArchivos.exists()) {
            for (int i = 0; i < directorioDeArchivos.listFiles().length; i++) {
                File file = (File) directorioDeArchivos.listFiles()[i];
                if (dAyer.getTime() >= file.lastModified()) {
                    if (file.exists()) {
	                    if (!file.delete()) {
	                        //logger.debug("No se puede eliminar el archivo:" + file.getName());
	                    }
                    }
                }                
                
            }		
		}        
    }
    
    /**
     * Nos permite copiar el contenido de un archivo, hacia otro archivo
     * 
     * @param src Archivo original
     * @param dst Archivo destino
     * @throws IOException
     */
    public static void copy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}
    
    
    public static boolean validaRut(String sRut, char sDv) {
        boolean rutValido = false;
        
        int M = 0, S = 1,T = Integer.parseInt(sRut);
        for(;T!=0;T/=10)S=(S+T%10*(9-M++%6))%11;
        
        char realDv = (char)(S!=0?S+47:75);
        
        if (realDv == sDv) {
            rutValido = true;    
        }

        return rutValido;
    }
    

    public static Date frmFecha(String fecha, String patron) {
        SimpleDateFormat formatter = new SimpleDateFormat(patron);
        try {
            if (fecha != null && !fecha.equalsIgnoreCase("")) {
                return new Date(formatter.parse(fecha).getTime());
            }
        } catch (ParseException e) {            
        }
        return null;
    }

    /**
     * Cambia el formato de una fecha que viende desde la pagina dd/MM/yyyy para ser ingresada en la BD yyyy-MM-dd
     * @param fechaResolucion
     * @return
     */
    public static String cambioFrmFc(String fechaResolucion) {
        if (fechaResolucion.length() == 10) {
            String dia = "";
            String mes = "";
            String anio = "";
            
            dia = fechaResolucion.substring(0,2);
            mes = fechaResolucion.substring(3,5);
            anio = fechaResolucion.substring(6,10);
            
            return anio+"-"+mes+"-"+dia;
            
        }
        return null;
    }
    
    /**
     * Nos entrega un listado para generar el combo de las alarmas
     * @param filtroAlarma Codigo de la alarma por la cual filtró para dejar seleccionada
     * @return Listado de alarmas
     */
    public static ArrayList generaComboEstado(String filtroEstado) {
        ArrayList lista = new ArrayList();
        IValueSet fila = new ValueSet();
        fila.setVariable("{id_estado}", "S");
		fila.setVariable("{estado}", "Activado");
		if (filtroEstado.equalsIgnoreCase("S")) {
			fila.setVariable("{sel_estado}", "selected");
		} else {
			fila.setVariable("{sel_estado}", "");
		}				
		lista.add(fila);
		
		fila = new ValueSet();
        fila.setVariable("{id_estado}", "N");
		fila.setVariable("{estado}", "Desactivado");
		if (filtroEstado.equalsIgnoreCase("N")) {
			fila.setVariable("{sel_estado}", "selected");
		} else {
			fila.setVariable("{sel_estado}", "");
		}				
		lista.add(fila);
		
        return lista;
    }
    
    /**
     * @param i
     * @return
     */
    private static String mesAnio(int i) {
        if (i < 10) {
            return "0"+i;
        }
        return ""+i;
    }

    /**
     * Nos entrega la fecha actual
     * @return Fecha actual
     */
    public static String fechaActual() {    
        Calendar cHoy = Calendar.getInstance();
        java.util.Date dHoy = cHoy.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    return sdf.format(dHoy);
    }

	 public static boolean validarRut(String rut){
		 try{		
			rut = rut.toUpperCase();
		     //validar si rut tiene el digito verificador correcto         
			int largo=rut.length(); 
		    
			char digito=rut.charAt(largo-1);
			
		    char val; 
		    
		    rut=rut.substring(0, largo-2); 
		    
		    //***
		    String rutAux=null;
		    int sdv = rut.length();
		    char[] cRut = rut.toCharArray();
		    switch (sdv) {
			case 9:
				if (cRut[1]=='.'&& cRut[5]=='.'){
					cRut[1]=cRut[2];
					cRut[2]=cRut[3];
					cRut[3]=cRut[4];
					cRut[4]=cRut[6];
					cRut[5]=cRut[7];
					cRut[6]=cRut[8];
					cRut[7]='\0';
					cRut[8]='\0';
				}
				break;
			case 10:
				if (cRut[2]=='.'&& cRut[6]=='.'){
					cRut[2]=cRut[3];
					cRut[3]=cRut[4];
					cRut[4]=cRut[5];
					cRut[5]=cRut[7];
					cRut[6]=cRut[8];
					cRut[7]=cRut[9];
					cRut[8]='\0';
					cRut[9]='\0';
				}	 
				break;			
			}
		    
		    rutAux = new String(cRut);
		    rut = rutAux.trim(); 
		    //***
		    
		    int m=0,s=1,t=Integer.parseInt(rut); 
		    for(;t!=0;t/=10){ 
		    	s=(s+t%10*(9-m++%6))%11; 
		    } 
		    val=(char)(s!=0? s+47:75); 
		    if(digito==val){ 
		    	return true; 
		    }else{ 
		    	return false; 
		    }
		}catch (Exception e) {
			//e.printStackTrace();
			return false;
		} 
	 }
    
    
}
