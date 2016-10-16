package cl.cencosud.jumbo.Constant;

public class ConstantPayment {
	
	public static final String SERVICE_NAME = "Payment";
	
	//CONSTANTES
	public static final String REDIRECT_URL = "redirect_url";
	public static final String SUCCESS_URL = "success_url";
	public static final String ERROR_URL = "error_url";
	public static final String TOKEN = "token";
	
	public static String INTEGRATION_CODE_CREATE_PAYMENT ="INT2370";
	public static long ESTADO_PEDIDO_PRE_INGRESADO = 1;
			
	public static final String REDIRECT_URL_PAYMENT_GRABILITY = "REDIRECT_URL_PAYMENT_GRABILITY";
	
	public static final String REDIRECT_URL_SUCCESS_TBK = "REDIRECT_URL_SUCCESS";
	public static final String REDIRECT_URL_ERROR_TBK = "REDIRECT_URL_ERROR";
	
	public static final String REDIRECT_URL_SUCCESS_CAT = "REDIRECT_URL_SUCCESS";
	public static final String REDIRECT_URL_ERROR_CAT = "REDIRECT_URL_ERROR";
		
		
	//ERRORES Servicio pago 400 - 499		
	public static final String SC_ESTADO_PEDIDO_NO_VALIDO_PAGAR = "0400";
	public static final String MSG_ESTADO_PEDIDO_NO_VALIDO_PAGAR = "Pedido no valido para ser pagado.";
	
	public static final String SC_ERROR_CREATE_TOKEN_PAYMENT  = "0401";
	public static final String MSG_ERROR_CREATE_TOKEN_PAYMENT = "Error al crear token de validacion de pago.";
	
	public static final String SC_MEDIO_PAGO_INVALIDO = "0402";
	public static final String MSG_MEDIO_PAGO_INVALIDO = "Medio de pago invalido.";
	
	public static final String SC_ERROR_RECUPERAR_OP = "0403";
	public static final String MSG_ERROR_RECUPERAR_OP = "Error al recuperar pedido.";
	
	public static final String  SC_ERROR_RECUPERAR_PARAMETRO_PAGO = "0404";
	public static final String  MSG_ERROR_RECUPERAR_PARAMETRO_PAGO = "Error al recuperar parametros de pago.";
	

}
