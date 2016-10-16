package cl.bbr.jumbocl.pedidos.promos.cargas;


import java.io.File;
import java.io.FilenameFilter;

public class FiltroArchPromo implements FilenameFilter {
	String prefijo;
	String extension;
	
	/**
	 * Necesita el prefijo y la extension del archivo a buscar
	 * Ejemplo prefijo="FD" , extension=".dat"
	 * @param prefijo
	 * @param extension
	 */
	public FiltroArchPromo(String prefijo, String extension) {
		this.prefijo = prefijo;
		this.extension = extension;
	}
	
    public boolean accept(File dir, String name){
    		
	    return ((name.toUpperCase().startsWith(prefijo) )
	    		&& (name.toUpperCase().endsWith(extension))) ;
	}
	
	
}
