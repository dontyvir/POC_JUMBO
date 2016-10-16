package cl.cencosud.procesos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import cl.cencosud.util.Parametros;

public class Archivo {
    
    private static Logger logger = Logger.getLogger(Archivo.class);

    private static String path = Parametros.getString("PATH_ARCHIVOS");
    private static String pathSST = Parametros.getString("PATH_ARCHIVOS_SST");
    
      
    
    public static String nombreArchivoCSV( String ruta, String prefijo, String extension, String rutaSST ) {

       
        File directorio = new File(pathSST);

        logger.info("Directorio : " +directorio);
        
        String[] lista = directorio.list(new Filtro(prefijo, extension));

        if ( lista.length == 1 ) {
            return lista[0];
        }

        int fechaMax = 0;
        String archivo = "";
        for ( int i = 0; i < lista.length; i++ ) {
            System.out.println(lista[i]);
            String sFecha = lista[i].substring(prefijo.length(), lista[i].length() - 4);
            System.out.println(sFecha);
            int fecha = Integer.parseInt(sFecha.substring(4) + sFecha.substring(0, 2) + sFecha.substring(2, 4));

            if ( fecha > fechaMax ) {
                archivo = lista[i];
                fechaMax = fecha;
            }
        }

        return archivo;
    }

   
}