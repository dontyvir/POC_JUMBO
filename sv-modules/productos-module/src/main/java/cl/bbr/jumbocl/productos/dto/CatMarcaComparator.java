package cl.bbr.jumbocl.productos.dto;

import java.util.Comparator;

/**
 * Clase para poder comparar Marcas. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class CatMarcaComparator implements Comparator  {

	public int compare(Object arg0, Object arg1) {
		CategoriaDTO cat1 = (CategoriaDTO) arg0;
		CategoriaDTO cat2 = (CategoriaDTO) arg1;
		return cat1.getNombre_marca().compareTo(cat2.getNombre_marca());
	}
	
}
