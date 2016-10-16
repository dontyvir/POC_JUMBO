package cl.jumbo.interfaces;

import cl.jumbo.interfaces.Status;

/**
 * Contiene las constantes de tipo Status para el establecer el
 * estado de un Proceso de Carga de Ventas
 * 
 * @author Informática Paris - Javier Villalobos Arancibia
 * @version 1.0 - 14/07/2006
 */
public class StatusConstants {

	public static final Status PROCESO_INACTIVO = new Status(0, "Proceso inactivo");
	public static final Status PROCESO_EN_ESPERA = new Status(1, "Proceso en espera");
	public static final Status VALIDANDO_DIRECTORIOS = new Status(2, "Validando directorios");
	public static final Status BUSCANDO_ARCHIVOS = new Status(3, "Buscando archivos");
	public static final Status MOVIENDO_ARCHIVOS_ENCONTRADOS = new Status(4, "Moviendo archivos encontrados");
	public static final Status DESCOMPRIMIENDO_ARCHIVOS = new Status(5, "Descomprimiendo archivos");
	public static final Status VALIDANDO_ARCHIVOS = new Status(6, "Validando archivos");
	public static final Status RESPALDANDO_DIRECTORIOS = new Status(7, "Respaldando directorios");
	public static final Status MOVIENDO_ARCHIVOS_INVALIDOS = new Status(8, "Moviendo archivos invalidos");
	public static final Status ARCHIVOS_NO_ENCONTRADOS = new Status(9, "No se registran archivos para cargar");
	public static final Status ARCHIVO_ENCONTRADO = new Status(10, "Archivo encontrado");
	public static final Status CARGANDO = new Status(11, "Cargando");
	public static final Status LEYENDO_RUTS_CLIENTES = new Status(12, "Leyendo Ruts Clientes Registrados");
	public static final Status DIRECTORIOS_INVALIDOS = new Status(-1, "Directorios inválidos");
}
