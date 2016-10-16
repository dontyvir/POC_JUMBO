package cl.bbr.boc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UtilStockOnline {
	 static ResourceBundle rb = ResourceBundle.getBundle("bo");
	 private final static String path = rb.getString("conf.path.archivos.sap");
	      
	
	public static int getStockReal( String valores[][] ,String idProductoBusqueda) {
		  
		   int codigo_error = Constantes.CONSTANTE_INT_ERROR;
		   int codigo_no_encontrado = Constantes.CONSTANTE_NO_ENCONTRADO;
			   try{
				   
				   for ( int i = 0; i < valores.length; i++ ) {
			    	 //  int idProductoArchivo = Integer.parseInt(valores[i][0]);
			    	   if(idProductoBusqueda.equals(valores[i][0])){    		   
			    			if(valores[i][1].indexOf('.')!=-1)
							{
								return Integer.parseInt(valores[i][1].substring(0,valores[i][1].indexOf(".")));

							}
							if(valores[i][1].indexOf('.')!=-1)
							{
								return Integer.parseInt(valores[i][1].substring(0,valores[i][1].indexOf(".")));

							}
							return  Integer.parseInt(valores[i][1]);
	
			    	   }
			       }
				   
			   }catch (NumberFormatException e) {			   
				   return codigo_error;
			   }
		   return codigo_no_encontrado;
	   }
	
	public static String getNombreArchivoPorCodigoLocal(String codigoLocal) {
		
		   String prefijo = "SST";
		   String prefijoNombreArchivo = prefijo + codigoLocal;
		   
		   String archivo = nombreArchivoCSV(prefijoNombreArchivo, "CSV");
		   
		   return archivo;
		   
		   
	}

	public static String[][] cargarCSV( String archivo) throws IOException {
		//obtiene archivos sst de Sap Fpenaloza
	    BufferedReader br = new BufferedReader(new FileReader(path + archivo));
	    //logger.debug("Ruta Archivo: "+ pathInterfaces + archivo);
	    String fila = null;       
	    List lista = new ArrayList();
	    
	    boolean primeraLinea = false; 
	    while ( ( fila = br.readLine() ) != null) {
	 	   if(!primeraLinea){
	 	       String columna[] = fila.split(",");
	 		   String resumenColumna[][] = new String[1][2];
	 		   if(!"".equals(columna[0].trim()) && !"".equals(columna[2].trim())){
		    		   resumenColumna[0][0] = columna[2].replaceAll("\"", "");; //idProducto
		    		   resumenColumna[0][1] = columna[3].replaceAll("\"", "");; //stockReal
		    		   lista.add(resumenColumna);   
		    		}
	 	   }
	 	   primeraLinea = false;
	    }
	    
	    String[][] datos = new String[lista.size()][2];
	    for ( int i = 0; i < lista.size(); i++ ) {
	 	   
	 	   String resumenComlumna[][] = new String[1][2];
	 	   resumenComlumna = (String[][]) lista.get(i);
	 	   datos[i][0] = resumenComlumna[0][0];//idProducto
	 	   datos[i][1] = resumenComlumna[0][1];//StockReal
	    }
	    
	   
	 
	    
	    br.close();
	    return datos;
	}  
	public static String nombreArchivoCSV( String prefijo, String extension ) {

//	    if ( ruta != null ) {
//	        path = ruta;
//	    }

	    File directorio = new File(path);

	    String[] lista = directorio.list(new Filtro(prefijo, extension));

	    if ( lista.length == 1 ) {
	        return lista[0];
	    }

	    int fechaMax = 0;
	    String archivo = "";
	    for ( int i = 0; i < lista.length; i++ ) {
	        
	        String sFecha = lista[i].substring(prefijo.length(), lista[i].length() - 4);
	        
	        int fecha = Integer.parseInt(sFecha.substring(4) + sFecha.substring(0, 2) + sFecha.substring(2, 4));

	        if ( fecha > fechaMax ) {
	            archivo = lista[i];
	            fechaMax = fecha;
	        }
	    }

	    return archivo;
	}



}
