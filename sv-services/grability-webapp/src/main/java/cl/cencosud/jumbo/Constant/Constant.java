package cl.cencosud.jumbo.Constant;

public class Constant {
	
	public static final String APPLICATION = "GRBSMCL";
	public static final String COUNTRY = "CL";
	public static final String BUSINESSUNIT = "SM";
	public static final String TARGET = "JUMSMCL";
	
	public static final String ENCODING = "UTF-8";

	//"this Is A Secret Key";
	//static KEY_PASS_AES is 128 - bit
	public static final String KEY_PASS_AES = "E8f.c7e5#3$16w9G";
	public static final String[] enableUriDecodePass = {"localhost", "172.18.150.35", "172.18.145.62"};
	
	public static final String TYPE = "type"; //$NON-NLS-1$
	public static final String SOURCE = "g";
	public static final String STATUS = "status";
	public static final String ERROR_MESSAGE = "error_message";
	public static final String LOCAL_ID = "local_id";
	public static final String ACTION = "action";
		
	public static final int CUOTAS_CAT = 0;
	public static final String CAT = "CAT";
	public static final int CUOTAS_TBK = 0;
	public static final String TBK = "TBK";
	
	public static final String BOOLEAN_FALSE = "0";
	public static final String BOOLEAN_TRUE = "1";	
	
	public static final String CLIENTE_NO_EXISTE = "Cliente no existe.";
	public static final String RUT_INVALIDO = "Rut invalido.";
	public static final String RUT_NO_EXISTE = "Rut no existe.";
	public static final String INVALID_PARAM = "Parametros invalidos.";
	public static final String INVALID_SOURCE = "Origen invalido";
	public static final String ID_DIRECCION_INVALIDO = "Id direccion invalido.";
	public static final String ID_REGION_INVALIDO = "Id region invalido.";
	public static final String ID_COMUNA_INVALIDO = "Id comuna invalido.";	
	public static final String SERVICIO_NO_VALIDO = "Servicio no valido.";
	public static final String ERROR_HTTP = "Error HTTP, respuesta codigo";
	public static final String ID_LOCAL_INVALIDO = "Id local no existe";
	
	public static final String MSG_EXCEPCION_SIN_TRAZA="Excepcion sin traza.";
	public static final String SQL_EXCEPTION = "sqlexception";
	
	public static final String IP_REFERER_REQUEST = "IP_REQUEST_GRABILITY";
	
	//Estatus de las respuestas.
	//STATUS OK
	public static final String SC_OK = "0000";//HttpServletResponse.SC_OK;
	public static final String MSG_OK = "";
	
	//ERRORES GENERALES
	
	//OSB BAM ERROR	
	//2002	Error de validación de Datos	SOA-'000-2002	Error datos de entrada
	public static final int SC_BAD_REQUEST_INT  = 1;
	public static final String SC_BAD_REQUEST  = "0001";
	public static final String SC_BAD_REQUEST_ERROR_CODE = "SOA-GRBSMCL-2002";
	public static final String SC_BAD_REQUEST_ERROR_CODE_OSB = "2002";
	public static final String MSG_BAD_REQUEST = "Error de validación de Datos.";
		
	//OSB BAM ERROR	
	//2003 -- Error de Falta de Parametrización -- SOA-'000-2003 -- Request falta de parametros
	public static final int SC_BAD_REQUEST_PARAM_INT = 2;
	public static final String SC_BAD_REQUEST_PARAM = "0002";
	public static final String SC_BAD_REQUEST_PARAM_ERROR_CODE = "SOA-GRBSMCL-2003";
	public static final String SC_BAD_REQUEST_PARAM_ERROR_CODE_OSB = "2003";
	public static final String MSG_BAD_REQUEST_PARAM  = "Error de Falta de Parametrizacion.";
		
	//OSB BAM ERROR	
	//1004	Error BD	SOA-'000-1004	BD Abajo o no disponible
	public static final int SC_ERROR_BD_INT = 3;
	public static final String SC_ERROR_BD = "0003";
	public static final String SC_ERROR_BD_ERROR_CODE = "SOA-GRBSMCL-1004";
	public static final String SC_ERROR_BD_ERROR_CODE_OSB = "1004";
	public static final String MSG_ERROR_BD = "Error BD.";
	
	//OSB BAM ERROR
	//1014	Error inesperado SQL	SOA-'000-1014	 Falla al ejecutar SQL
	public static final int SC_ERROR_INESPERADO_SQL_INT = 4;
	public static final String SC_ERROR_INESPERADO_SQL = "0004";
	public static final String SC_ERROR_INESPERADO_SQL_ERROR_CODE = "SOA-GRBSMCL-1014";
	public static final String SC_ERROR_INESPERADO_SQL_ERROR_CODE_OSB = "1014";
	public static final String MSG_ERROR_INESPERADO_SQL = "Error inesperado SQL.";
	
	//OSB BAM ERROR
	//1017	No se encontro configuracion para el nombre de servicio	SOA-'000-1017	 Request mal hecho
	public static final int SC_NO_SE_ENCONTRO_CONF_NOMBRE_SERVICIO_INT = 5;
	public static final String SC_NO_SE_ENCONTRO_CONF_NOMBRE_SERVICIO = "0005";
	public static final String SC_NO_SE_ENCONTRO_CONF_NOMBRE_SERVICIO_ERROR_CODE = "SOA-GRBSMCL-1017";
	public static final String SC_NO_SE_ENCONTRO_CONF_NOMBRE_SERVICIO_ERROR_CODE_OSB = "1017";
	public static final String MSG_NO_SE_ENCONTRO_CONF_NOMBRE_SERVICIO = "No se encontro configuracion para el nombre de servicio.";
	
	//OSB BAM ERROR
	//1010	Error inesperado (En el caso del IID obteniendo parametros de ruteo de SAP para el IDOC)	SOA-'000-1010	 
	public static final int SC_ERROR_INESPERADO_INT = 6;
	public static final String SC_ERROR_INESPERADO = "0006";
	public static final String SC_ERROR_INESPERADO_ERROR_CODE = "SOA-GRBSMCL-1010";
	public static final String SC_ERROR_INESPERADOERROR_CODE_OSB = "1010";
	public static final String MSG_ERROR_INESPERADO = "Error inesperado de servicio.";
	
	
	public static final String  SC_ERROR_EJECUCION = "0099";
	public static final String  MSG_ERROR_EJECUCION = "Error de ejecucion.";
		
			
}
