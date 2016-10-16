package cl.jumbo.ventamasiva.Constant;

public class Constant {
	
	public static final String CONTEXT_ROOT = "/VentaMasiva"; 
	public static final String REDIRECT_URL_PAYMENT_BASE_TBRAINS = "REDIRECT_URL_PAYMENT_BASE_TBRAINS";	
	public static final String REDIRECT_URL_PAYMENT_TBRAINS = "REDIRECT_URL_PAYMENT_TBRAINS";
	public static final String REDIRECT_URL_SUCCESS_TBRAINS = "REDIRECT_URL_SUCCESS_TBRAINS"; //2b
	public static final String REDIRECT_URL_ERROR_TBRAINS = "REDIRECT_URL_ERROR_TBRAINS"; //2b
	public static final String URL_PAGO_CAT_TBRAINS = "URL_PAGO_CAT_TBRAINS";
	public static final String ID_BP_VENTA_MASIVA = "ID_BP_VENTA_MASIVA"; 
		
	public static final String CAT_PURCHASE_END = "/PurchaseEnd";
	public static final String TBK_PURCHASE_END = "/PurchaseEnd";
	public static final String TBK_PURCHASE_END_ERROR = "/PurchaseEnd?TBK_ERROR=";
	
	public static final String MEDIO_PAGO_TBK = "TBK";
	public static final String MEDIO_PAGO_CAT = "CAT";
	public static final String NAME_IN = REDIRECT_URL_PAYMENT_TBRAINS+ "','" +REDIRECT_URL_SUCCESS_TBRAINS+ "','" +REDIRECT_URL_ERROR_TBRAINS+ "','" +REDIRECT_URL_PAYMENT_BASE_TBRAINS+ "','" + URL_PAGO_CAT_TBRAINS + "','" + ID_BP_VENTA_MASIVA;
	public static final String NAME_CARD_TBK_GENERIC = "Transbank";
	public static final long   ID_USER_PHONE_DEFAULT = 3587;
	public static final String TYPE_DOCUMENT = "B";
	public static final long   ID_POLIGONO = 1;
	public static final String POLIGONO_SUSTITUCION	= "Sí";
	public static final long   ID_LOCAL_VENTA_MASIVA = 4; //La REINA
	public static final long   ID_LOCAL_FACTURADOR = 4; //La Reina
	public static final String VALOR_MINIMO_DESPACHO_VENTA_MASIVA	= "VALOR_MINIMO_DESPACHO_VENTA_MASIVA";
	public static final long ID_LOCAL_FLORIDA=4;
	public static final long ID_SECTOR_VENTA_MASIVA=28;
	public static final long ID_ESTADO_RONDA_CREADA = 11;
	public static final long ID_ESTADO_RONDA_TERMINADA = 13;
	
	public static final int ID_ESTAD_PEDIDO_PRE_INGRESADO = 1;
	public static final int ID_ESTAD_PEDIDO_EN_VALIDACION = 4;
	public static final int ID_ESTAD_PEDIDO_VALIDADO      = 5;
	public static final int ID_ESTAD_PEDIDO_EN_BODEGA      = 7;
	
	
	public static final String TEXT_DESCTO_DESPACHO = "Servicio a $1";
	public static final String TEXT_DESCTO_APLICADO = "Cupón de descuento código ";
    public static final String TEXT_COSTO_DESPSCHO = " (Solo costo de despacho $1) ";
    public static final String TEXT_CUPON_DESCUENTO = "Cupón";
    public static final String TEXT_PROMOCION = "Promoción";
    public static final String TEXT_COLABORADOR = "Colaborador";
	public static final String NOMBRE_DESCTO = "{nombre_descuento}";
    public static final String DETALLE_DESCTO = "{detalle_descuento}";
    public static final String MONTO_DESCTO = "{monto_descuento}";
    public static final String POR = " por ";
    public static final String SYSTEM = "SYSTEM";
    
	public static final String ENCODING = "UTF-8";
	public static final String AUTHOR = "@example-bf";
	public static final String TARGET = "JUMSMCL";
	public static final String APPLICATION = "VMCENCO";	
	
	public static final String REG_FALTAN_PARAM		= "0301";
	public static final String MEDIOS_DE_PAGO_VALIDOS = "CAT-TBK";
	public static final String VENTANAS = "AM-PM";
	public static final String PATRON_MAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public static final Object PAYMENT_METHOD_TBK = "TBK";
}
