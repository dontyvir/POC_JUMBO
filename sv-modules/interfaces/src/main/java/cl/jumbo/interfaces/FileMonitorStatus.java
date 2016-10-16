package cl.jumbo.interfaces;

/**
 * Contiene las constantes de tipo Status para el establecer el
 * estado de un Proceso de Carga de Ventas
 * 
 * @author Informática Paris - Javier Villalobos Arancibia
 * @version 1.0 - 14/07/2006
 */
public class FileMonitorStatus {

	public static final Status VALIDANDO_DIRECTORIOS = new Status(1, "Validando directorios");
	public static final Status BUSCANDO_ARCHIVOS = new Status(2, "Buscando archivos");
	public static final Status MOVIENDO_ARCHIVOS_ENCONTRADOS = new Status(3, "Moviendo archivos encontrados");
	public static final Status DESCOMPRIMIENDO_ARCHIVOS = new Status(4, "Descomprimiendo archivos");
	public static final Status ARCHIVOS_NO_ENCONTRADOS = new Status(5, "No se encontraron archivos");
	public static final Status DIRECTORIOS_INVALIDOS = new Status(-1, "Directorios inválidos");
}
