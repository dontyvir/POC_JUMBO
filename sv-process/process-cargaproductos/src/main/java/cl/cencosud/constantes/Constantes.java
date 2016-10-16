package cl.cencosud.constantes;

import cl.cencosud.util.Parametros;

/**
 * 
 * @author jolazogu
 *
 */
public class Constantes {
		
	/* 
	* Se comentan constantes, NO pertenecen al modulo /process-cargaproductos,
	* estas estan vinculadas al modulo /process-stock-online
	* */
	
	/*
    public static final String CONSTANTE_ESTADO_AUTOMATICO = "AUTOMATICO";
    public static final String CONSTANTE_ESTADO_SEMIAUTOMATICO = "SEMIAUTOMATICO";
    public static final int CONSTANTE_ESTADO_INT_AUTOMATICO = 1;
    public static final int CONSTANTE_ESTADO_INT_SEMIAUTOMATICO = 0;
    public static final int CONSTANTE_INT_ERROR = -1000;
    public static final int CONSTANTE_MODO_INT_ON = 1;
    public static final int CONSTANTE_MODO_INT_OFF = 0;
    public static final int CONSTANTE_NO_ENCONTRADO = 999999999;
    public static final String TITULO_HOJA_EXCEL = "Automático";
    public static final String PRIMERA_CELDA = "LOCAL";
    public static final String SEGUNDA_CELDA = "SKU";
    public static final String TERCERA_CELDA = "STOCK REAL";
    public static final String CUARTA_CELDA = "STOCK MINIMO";
    public static final String QUINTA_CELDA = "ID PRODUCTO";
    public static final String SEXTA_CELDA = "ESTADO INICIAL";
    public static final String SEPTIMA_CELDA = "ESTADO FINAL";
    public static final String OCTAVA_CELDA = "RESULTADO TRANSACCIÓN";
    public static final String NOVENA_CELDA = "ESTADO";
    public static final String AUTOMATICO = "Automatico";
	public static final String XLS = "xls";
	public static final String EXITO = "EXITO";
	public static final String SIN_STOCK = "SIN STOCK";
	public static final String CON_STOCK = "CON STOCK";
	public static final String SUBJECT = "Resumen Stock Online Proceso Automático";
	public static final String FROM = "Stock Online <no-reply@cencosud.cl>";
	public static final String PUBLICADO = "A";
	public static final String DESPUBLICADO = "D";	
	
	public static String path = Parametros.getString("PATH_ARCHIVOS_SST");
	public static String pathRespaldos = Parametros.getString("PATH_ARCHIVOS_STOCK_ONLINE");
	public static String user = Parametros.getString( "USER" );
	public static String password = Parametros.getString( "PASSWORD" );
	public static String driver = Parametros.getString( "DRIVER" );
	public static String url = Parametros.getString( "URL" );
	public static String conf_path_html = Parametros.getString( "PATH_MAIL_STOCK_AUTOMATICO" );
	public static String DESTINATARIO = Parametros.getString("STOCK_ONLINE_DESTINATARIO");
	public static String CC = Parametros.getString("STOCK_ONLINE_CC");
	public static String HOST = Parametros.getString("STOCK_ONLINE_MAIL_SMTP_HOST");
	*/
	
	public static String path = Parametros.getString("PATH_ARCHIVOS");
	
	public final static String CLP_FORMAT = "$###,###.##;$ -###,###.##";
	public static final String FILE_UNIDADES_JSON = "unidades.json";
	//public static String alert_mail_jcp_html = Parametros.getString( "PATH_MAIL_ALERT_IFACE_AUTOMATICO" );
	//public static String alert_mail_file_gz_html = Parametros.getString( "PATH_MAIL_ALERT_IFACE_AUTOMATICO_GZ" );
	//public static String alert_mail_file_precios_no_actualizados = Parametros.getString("PATH_MAIL_ALERT_IFACE_AUTOMATICO_PRECIOS_NO_ACTUALIZADOS");
	public static String PATH_TEMPLATE = Parametros.getString("PATH_TEMPLATE");
	public static String TEMPLATE_PRECIOS_PARCIALES = Parametros.getString("TEMPLATE_PRECIOS_PARCIALES");
	public static String NAME_FILE_ALERTA = Parametros.getString("NAME_FILE_ALERTA");

	public static String ENVIO_MAIL  = Parametros.getString("ENVIO_MAIL");
	public static String HOST        = Parametros.getString("HOST");
	public static String CONNECTION_TIMEOUT        = Parametros.getString("CONNECTION_TIMEOUT");
	public static String TIMEOUT     = Parametros.getString("TIMEOUT");
	public static String FROM        = Parametros.getString("FROM");
	public static String SUBJECT     = Parametros.getString("SUBJECT");
	public static String INFO_HTML   = Parametros.getString("INFO_HTML");
	public static String NAME_FOLDER_GENERACION   = Parametros.getString("NAME_FOLDER_GENERACION");
	
}
