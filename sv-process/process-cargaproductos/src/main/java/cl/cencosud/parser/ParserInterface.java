package cl.cencosud.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import cl.cencosud.beans.Precio;
import cl.cencosud.constantes.Constantes;
import cl.cencosud.util.CargaPreciosUtil;
import cl.cencosud.util.DateUtil;
import cl.cencosud.util.LogUtil;

public class ParserInterface {

	static {
		LogUtil.initLog4J();
	}
	
	private static Logger logger = Logger.getLogger(ParserInterface.class);
	//private static String path = Parametros.getString("PATH_ARCHIVOS");
	
	private static HashMap unitsMedida = null;
	 
	/**
	  * Parsea el archivo JCP y se crea 2 lista de precio con los registros de la interfaz
	  * 
	  * @param archivo, nombre del archivo
	  * 
	  * @return HashMap
     */
    public static HashMap cargarPreciosInterfaceJCP( String archivo) {
    	BufferedReader br = null;
    	List listaPrecioOk = null;
    	List listaPrecioNoValida = null;
    	String linea = null;
    	String unidad = null;
    	HashMap mapListaPrecios = new HashMap();
    	
    	try{    		
    		listaPrecioOk = new ArrayList();
    		listaPrecioNoValida = new ArrayList();

    		GregorianCalendar hoy = DateUtil.getCalendar(Integer.parseInt(archivo.substring(7, 9)), Integer.parseInt(archivo.substring(9, 11)));
    		Date fechaPrecioNuevo = hoy.getTime();
            // System.out.println("Fecha precio nuevo: " + fechaPrecioNuevo);
            
    		br = new BufferedReader(new FileReader(Constantes.path + archivo));

    		if(unitsMedida==null){
    			unitsMedida = CargaPreciosUtil.getUnitsFromJson();	
    		}
    		if (unitsMedida==null){
    			throw new Exception("Map Unidad de Medida es null");
    		}
    		
    		long x = 0;        
    		while ( ( linea = br.readLine() ) != null ) {
    			x++;
    			try {
    				unidad = linea.substring(44, 47).trim();
    				String unidadValor = "";
    				/*
    				 * Verifica que la unidad de medida que viene en el archivo,se encuentre en el objeto unitsMedida
    				 * */
    				if ( unitsMedida.get(unidad) != null ) {
    					/*
        				 * Contiene una lista de precios con unidades de medidas validas
        				 * */
    					//listaPrecioOk.add( setPrecio(linea, unidad, fechaPrecioNuevo, archivo) );
    					unidadValor = (String) unitsMedida.get(unidad);
    					listaPrecioOk.add( setPrecio(linea, unidadValor, fechaPrecioNuevo, archivo) );
    					
	                }else{
	                	/*
        				 * Contiene una lista de precios con unidades de medida NO validas
        				 * */
	                	listaPrecioNoValida.add( setPrecio(linea, unidad, fechaPrecioNuevo, archivo) );
	                }
	            } catch (Exception e) {
	                logger.error("ERROR, al obtener precio de la interfaz "+archivo+", en la linea "+x+". Error: " + e.getMessage(), e);
	            }
	        }
    		
        }catch(Exception e){
        	 logger.error("ERROR, al parsear interfaz "+archivo+", Error:"+  e);
        }finally{
        	try {
        		if (br != null){
        			br.close();
        		}
			}catch (IOException ex) {
				ex.printStackTrace();
			}
        }
    	mapListaPrecios.put("listaPreciosOk", listaPrecioOk);
    	mapListaPrecios.put("listaPreciosNoValidas", listaPrecioNoValida);
    	
    	logger.debug("Precios cargados con unidades validas " + listaPrecioOk.size()+
    			" y Precios cargados con unidades NO validas " + listaPrecioNoValida.size() + ", en interfaz ["+archivo+"]");
    	
        return mapListaPrecios;
    }
    
    
    /**
	  * Setea el objeto Precio con los datos de la interfaz.
	  * 	  
	  * @param linea,	linea del archivo
	  * @param unidad, unidade de medida
	  * @param fechaPrecioNuevo, fecha precio nuevo
	  * @param archivo, nombre del archivo
	  * 
	  * @return Precio
	  * @throws Exception	  
    */
    private static Precio setPrecio(String linea, String unidad, Date fechaPrecioNuevo, String archivo)throws Exception{
    	Precio precio = new Precio();
        precio.setCodigoLocal(linea.substring(0, 4));
        /*
         * estoy suponiendo que los codigos de producto siempre son
         * enteros aunque se manejen como string, si fueran string
         * vendrián con espacios a la izquiersa y no con ceros como
         * vienen en el archivo de precios.
         */
        precio.setCodigoProducto(String.valueOf(Integer.valueOf(linea.substring(4, 22))));
        precio.setPrecio(Integer.valueOf(linea.substring(22, 32)).intValue());
        precio.setCodigoBarra(linea.substring(32, 44));
        precio.setUnidadMedida(unidad);
        precio.setFechaPrecioNuevo(fechaPrecioNuevo);
        precio.setNombreArchivo(archivo);
        return precio;
    }
        
}
