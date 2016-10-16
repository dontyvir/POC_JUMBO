package cl.cencosud.jumbo.Constant;

public class ConstantDeliveryWindow {
	
	public static final String DELIVERY_TYPE = "delivery_type";
	public static final String SHIPPING_TYPE = "shipping_type";
	public static final String CART_VALUE_CAT = "cart_value_cat";
	public static final int CUOTAS_TARJETA = 0;
	public static final String FORMA_PAGO_PROMO = "TBK";
	public static final int DIAS_CALENDARIO = 7;
	public static final int DIAS_PRESENTACION = 8;
	public static final int WEB_CLIENTE_CONFIABLES = 0;
	public static final String DAY_OF_WEEK_FOR_DELIVERY = "day_of_week_for_delivery";
	public static final String DAY_OF_WEEK = "day_of_week";
	public static final String DELIVERY_RANGES = "delivery_ranges";
	public static final String ENABLED = "enabled";
	public static final String START_TIME = "start_time";
	public static final String END_TIME = "end_time";
	public static final String JOURNEY_PRICE = "journey_price";
	public static final String DELIVERY_JOURNEY_ID = "delivery_journey_id";
	public static final String PICKING_JOURNEY_ID = "picking_journey_id";
	public static final long CANTIDAD_PRODUCTOS_DUMMY = 5;
	
	
	public static final String TIPO_DESPACHO_UMBRAL = "U";
	public static final String TIPO_DESPACHO_ECONOMICO = "C";
	public static final String TIPO_DESPACHO_EXPRESS = "E";
	public static final String TIPO_DESPACHO_NORMAL = "N";
	public static final String TIPO_DESPACHO_RETIRO = "R";
	public static final int VALOR_DESPACHO_GRATUITO = 1;
	public static final String TIPO_SERVICIO_VALIDOS = "D-C"; //despacho, retiro
	public static final String TIPO_SERVICIO_RETIRO = "C";
	public static final String TIPO_SERVICIO_DESPACHO = "D";
	
	public static final String INTEGRATION_CODE_VENTANA_DESPACHO = "INT2365";
	
	//ERRORES
	
	public static final String SC_ERROR_OBTENER_VENTANA = "0600";
	public static final String MSG_ERROR_OBTENER_VENTANA = "No se pudo obtener ventana de despacho";
	public static final String SC_ERROR_LOCAL_SIN_RETIRO = "0601";
	public static final String MSG_ERROR_LOCAL_SIN_RETIRO = "Local no posee opcion de Click & al Auto";
	public static final String SC_ID_DIRECCION_INVALIDA = "0602";
	public static final String MSG_ID_DIRECCION_INVALIDA = "Id direccion invalida";
	public static final String SC_ID_CLIENTE_INVALIDO = "0603";
	public static final String MSG_ID_CLIENTE_INVALIDO = "Id Cliente invalido";
	public static final String SC_LOCAL_INVALIDO = "0604";
	public static final String MSG_LOCAL_INVALIDO = "Id de local es 0";
	public static final String SC_MONTO_CARRO_INVALIDO = "0605";
	public static final String MSG_MONTO_CARRO_INVALIDO = "El monto del carro es 0";
	public static final String SC_ERROR_ID_GRABILITY = "0606";
	public static final String MSG_ERROR_ID_GRABILITY = "Id de Grability es Erroneo";
	public static final String SC_TIPO_SERVICIO_INVALIDO = "0607";
	public static final String MSG_TIPO_SERVICIO_INVALIDO = "Tipo de servicio invalido";
	public static final String SC_ERROR_OBTENER_FECHA_HORA_SERVER = "0608";
	public static final String MSG_ERROR_OBTENER_FECHA_HORA_SERVER = "Error al obtener fecha/hora de servidor";
	public static final String SC_ERROR_OBTENER_DIRECCION_CLIENTE = "0609";
	public static final String MSG_ERROR_OBTENER_DIRECCION_CLIENTE = "Error al obtener direccion del cliente";
	public static final String SC_ERROR_OBTENER_ZONA_DESPACHO = "0610";
	public static final String MSG_ERROR_OBTENER_ZONA_DESPACHO = "Error al obtener zona de despacho de cliente";
	public static final String SC_ERROR_OBTENER_CANTIDAD_PRODUCTOS = "0611";
	public static final String MSG_ERROR_OBTENER_CANTIDAD_PRODUCTOS = "Error al obtener cantidad de productos del carro de compras";
	public static final String SC_ERROR_OBTENER_CARRO_COMPRAS = "0612";
	public static final String MSG_ERROR_OBTENER_CARRO_COMPRAS = "Error al obtener carro de compras";
	public static final String SC_ERROR_AL_CALCULAR_PROMOCIONES = "0613";
	public static final String MSG_ERROR_AL_CALCULAR_PROMOCIONES = "Error al recalcular promociones";
	public static final String SC_ERROR_SI_ES_PRIMERA_COMPRA = "0614";
	public static final String MSG_ERROR_SI_ES_PRIMERA_COMPRA = "Error al obtener validacion de primera compra";
	public static final String SC_ERROR_SI_ES_RETIRO = "0615";
	public static final String MSG_ERROR_SI_ES_RETIRO = "Error al obtener validacion si es retiro en local";
	public static final String SC_ERROR_OBTENER_CLIENTE_CONFIABLE = "0616";
	public static final String MSG_ERROR_OBTENER_CLIENTE_CONFIABLE = "Error al obtener cliente confiable";
	public static final String SC_ERROR_OBTENER_PARAMETRO = "0617";
	public static final String MSG_ERROR_OBTENER_PARAMETRO = "Error al obtener valor de parametro solicitado";
	public static final String SC_ERROR_OBTENER_MAYORES_CAPACIDADES = "0618";
	public static final String MSG_ERROR_OBTENER_MAYORES_CAPACIDADES = "Error al obtener mayores capacidades por jornada";
	public static final String SC_ERROR_ACTUALIZAR_CAPACIDADES = "0619";
	public static final String MSG_ERROR_ACTUALIZAR_CAPACIDADES = "Error al actualizar capacidades ocupadas";
	public static final String SC_ERROR_OBTENER_CALENDARIO = "0620";
	public static final String MSG_ERROR_OBTENER_CALENDARIO = "Error al obtener calendario";
	public static final String SC_ERROR_OBTENER_LOCALES_RETIRO = "0621";
	public static final String MSG_ERROR_OBTENER_LOCALES_RETIRO = "Error al obtener lista de locales con Click & al Auto";
	public static final String SC_ERROR_OBTENER_LOCAL = "0622";
	public static final String MSG_ERROR_OBTENER_LOCAL = "Error al obtener local por Id";
	public static final String SC_TIPO_ORIGEN_INVALIDO = "0623";
	public static final String MSG_TIPO_ORIGEN_INVALIDO = "Origen Invalido";
	
}
