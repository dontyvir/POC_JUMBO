package cl.cencosud.facade;

import java.util.HashMap;
import java.util.List;

import cl.cencosud.parser.ParserInterface;

public class CargarPreciosFacade {

	
	/**
	 * Devuelve un Map con 2 lista: listaPreciosOk y listaPreciosNoValidas   
	 * 
	 * @param archivo, nombre del archivo
	 * @return HashMap
	 * */
	public static HashMap getListaPreciosMapList(String archivo) {
		return ParserInterface.cargarPreciosInterfaceJCP(archivo);
	}

	public static List getListaPrecios(String archivo) {
		HashMap mapListaUnits = ParserInterface.cargarPreciosInterfaceJCP(archivo);
		List listaPrecios = (List) mapListaUnits.get("listaPreciosOk");
		return listaPrecios;
	}

	public static List getListaPreciosNoValidos(String archivo, HashMap unitsMedida) {
		HashMap mapListaUnits = ParserInterface.cargarPreciosInterfaceJCP(archivo);
		List listaPrecios = (List) mapListaUnits.get("listaPreciosNoValidas");
		return listaPrecios;
	}

}
