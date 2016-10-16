package cl.cencosud.jumbo.Constant;


public class ConstantPurchaseOrder {
	
	public static final String ORDER_ID = "order_id";//  "unique identifier of the purchase order"  
	
	public static final String RESERVED_AMOUNT = "reserved_amount";
	public static final String CART_AMOUNT = "cart_amount";
	
	
	public static final String DELIVERY_JOURNEY_ID = "delivery_journey_id"; // "delivery journey unique identifier, ie 123",
	public static final String PICKING_JOURNEY_ID = "picking_journey_id"; //"picking journey unique identifier, ie 111",
	public static final String LOCAL_ID = "local_id"; //"unique identifier of the local related to this address",
	public static final String SHIPPING_TYPE ="shipping_type"; //"alphanumeric value representing delivery or click and collect"	
	public static final String JOURNEY_PRICE = "journey_price"; //"price of delivery for this window, ie 4990",
	
	public static final String ORDER_OBSERVATION ="order_observation"; //"observations regarding the purchase order",
	public static final String PAYMENT_METHOD ="payment_method"; //"payment method selected by the user, ie CAT or TBK",
	public static final String OTHER_RECEIVER  ="other_receiver"; //"indicates if the buyer will receive the order or if other person will receive it, ie: 0 if buyer receives 1 otherwise",
	public static final String RECEIVER_OBSERVATION ="receiver_observation"; //"this field is optional, the user can enter observations regarding the person receiving the order",
	public static final String RECEIVER_RUT ="receiver_rut"; //"this field is optional, the receiver's rut",
	public static final String RECEIVER_DV  ="receiver_dv"; //"this field is optional, the receiver's dv (digito verificador)",
	public static final String AMOUNT ="amount"; //"amount to be reserved on the client's credit card",
	public static final String PRODUCTS = "products";
	public static final String QUANTITY = "quantity"; //"how many products were added",
	public static final String PRODUCT_ID = "product_id"; //"product id (FO id)",
	public static final String TOTAL_PRICE = "total_price"; //"unit price times quantity",
	public static final String UNIT_PRICE = "unit_price"; //"prorated price per unit, this price includes promotions and discounts. according to the payment method chosen this parameter should be tmp_price or cat_price",
	public static final String SUBSTITUTION_CRITERIA_ID = "substitution_criteria_id"; //"unique identifier for a substitution criteria, ie (1..6)",
	public static final String SUSBTITUTION_DESCRIPTION = "susbtitution_description"; //"one criteria is other and has a textbox so the user can enter text. This field represents that text"
	public static final String COUPON_CODE = "coupon_code";	
	
	public static final String SIN_GENTE_OP = "0";
	public static final long ID_USUARIO_FONO_COMPRAS_GENERICO = 3587;
	public static final long ID_POLIGONO = 1;//Cliente (no VE) siempre tiene id Poligono 1
	public static final String POLIGONO_SUSTITUCION = "Sí"; //Cliente (No VE) siempre tiene poligono de sustitucion
	public static final String NOMBRE_TARJETA_BANCARIA = "Transbank";
	public static final String TIPO_DOCUMENTO = "B";
	public static final double PORC_DIFERENCIA_VALORCARRO_MONTO = 20;
	public static final String MEDIOS_DE_PAGO_VALIDOS = "TBK-CAT";
	
	public static final String INTEGRATION_CODE_GENERATE_PURCHASE_ORDER = "INT2369";
	
	//Mensajes errores	
	public static final String MSG_ORDEN_DE_PEDIDO_INVALIDA = "No se pudo generar Orden de pedido";
	public static final String SC_ORDEN_PEDIDO_INVALIDA = "0300";
	public static final String MSG_TIPO_DESPACHO_INVALIDO = "Tipo de despacho invalido";
	public static final String SC_TIPO_DESPACHO_INVALIDO = "0301";
	public static final String MSG_RECIBIDOR_INVALIDO = "Nombre de la persona que recibe/retira es invalido";
	public static final String SC_RECIBIDOR_INVALIDO = "0302";
	public static final String MSG_RUT_RECIBIDOR_INVALIDO = "Rut de la persona que recibe/retira es invalido";
	public static final String SC_RUT_RECIBIDOR_INVALIDO = "0303";
	public static final String MSG_DV_RECIBIDOR_INVALIDO = "Digito verificador de la persona que recibe/retira es invalido";
	public static final String SC_DV_RECIBIDOR_INVALIDO = "0304";
	public static final String MSG_VERIFICACION_RUT_RECIBIDOR_INVALIDA = "Verificacion rut de la persona que recibe/retira es invalida";
	public static final String SC_VERIFICACION_RUT_RECIBIDOR_INVALIDA = "0305";
	public static final String MSG_ID_CLIENTE_INVALIDO = "ID Cliente Invalido";
	public static final String SC_ID_CLIENTE_INVALIDO = "0306";
	public static final String MSG_ID_DIRECCION_INVALIDA = "ID Direccion invalida";
	public static final String SC_ID_DIRECCION_INVALIDA = "0307";
	public static final String MSG_ERROR_CAPACIDAD = "Excede capacidad, no se puede generar pedido";
	public static final String SC_ERROR_CAPACIDAD = "0308";
	public static final String MSG_ERROR_MONTO_TOTAL_ES_CERO = "El monto total no puede ser menor o igual a 0";
	public static final String SC_ERROR_MONTO_TOTAL_ES_CERO = "0309";
	public static final String MSG_MONTO_TOTAL_INVALIDO = "El monto total es invalido";
	public static final String SC_MONTO_TOTAL_INVALIDO = "0310";
	public static final String MSG_ERROR_MONTO_SERVICIO_ES_CERO = "El monto del servicio no puede ser menor o igual a 0";
	public static final String SC_ERROR_MONTO_SERVICIO_ES_CERO = "0311";
	public static final String MSG_LOCAL_INVALIDO = "El local es invalido";
	public static final String SC_LOCAL_INVALIDO = "0312";
	public static final String MSG_MEDIO_PAGO_INVALIDO = "Medio de pago invalido";
	public static final String SC_MEDIO_PAGO_INVALIDO = "0313";
	public static final String MSG_PRODUCTO_NO_ENCONTRADO = "Producto no encontrado";
	public static final String SC_PRODUCTO_NO_ENCONTRADO = "0314";
	public static final String MSG_ERROR_CARRO = "Error al obtener carro de compras cliente";
	public static final String SC_ERROR_CARRO = "0315";
	public static final String MSG_PRODUCTOS_CUPON = "Error al obtener cupon por producto";
	public static final String SC_PRODUCTOS_CUPON = "0316";
	public static final String MSG_CUPON_DESCUENTO = "Error al obtener cupon de descuento";
	public static final String SC_CUPON_DESCUENTO = "0317";
	public static final String MSG_CUPON_DESCUENTO_X_RUT = "Error al obtener cupon por rut de cliente";
	public static final String SC_CUPON_DESCUENTO_X_RUT = "0318";
	public static final String MSG_MONTO_SERVICIO_INVALIDO = "El monto del servicio es invalido";
	public static final String SC_MONTO_SERVICIO_INVALIDO = "0319";
	public static final String MSG_MONTO_FUERA_DE_RANGO = "Monto llegado por parametro excede rango de valor calculado segun el carro";
	public static final String SC_MONTO_FUERA_DE_RANGO = "0320";
	public static final String MSG_INSERTAR_LOG_BO = "Error al insertar LOG de pedido para BOL y BOC";
	public static final String SC_INSERTAR_LOG_BO = "0321";
	public static final String MSG_ERROR_RECUPERAR_OP = "Error al recuperar OP";
	public static final String SC_ERROR_RECUPERAR_OP = "0322";
	
	public static final String MSG_ERROR_JORNADA_LOCAL = "Jornada de picking o despacho no corresponden al local.";
	public static final String SC_ERROR_JORNADAS_LOCAL = "0323";
	
	public static final String MSG_ERROR_CAPACIDAD_TIPO_DESPACHO = "Jornada sin capacidad para el tipo despacho.";
	public static final String SC_ERROR_CAPACIDAD_TIPO_DESPACHO = "0324";
	
	// 4.1 Obtiene jornada de despacho con Mayor Capacidad de Picking,
    // sólo en caso de que el despacho sea Económico.

}
