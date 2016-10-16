package cl.cencosud.jumbo.Constant;

public class ConstantShoppingCart {
	
	public static final String SERVICE_NAME = "ShoppingCart";

	public static final String SOURCE_CART = "g";
	public static final String DELIVERY_TYPE = "delivery_type";
	public static final String COUPON_CODE = "coupon_code";
	public static final String GUEST_USER = "guest_user";
	public static final String PRODUCTS = "products";
	public static final String TOTAL = "total";
	public static final String DISCOUNTS = "discounts";
	public static final String COUPON = "coupon";
	
	public static final String CUPON_DSCTO_STATUS_NOK = "invalid";
	public static final String CUPON_DSCTO_STATUS_OK = "valid";
	
	//INTEGRATION_CODE
	//GET
	public static final String INTEGRATION_CODE_OBTENER_CARRO_COMPRAS = "INT2366";
	public static final String INTEGRATION_CODE_MODIFICAR_CARRO_COMPRAS = "INT2367";
	public static final String INTEGRATION_CODE_ELIMINAR_CARRO_COMPRAS = "INT2368";
	
	//Errores 500-599
	
	public static final String SC_CLIENT_TYPE = "0500";
	public static final String MSG_CLIENT_TYPE = "Tipo cliente invalido.";
	
	public static final String SC_ID_CLIENT_GUEST = "0501";
	public static final String MSG_ID_CLIENT_GUEST = "Id cliente invitado no es valido.";
	
	public static final String SC_ERROR_AL_CONSULTAR_PRODUCTOS_CARRO = "0502";
	public static final String MSG_ERROR_AL_CONSULTAR_PRODUCTOS_CARRO = "Error al consultar productos del Carro de Compras.";
	
	public static final String SC_CARRO_VACIO = "0503";
	public static final String MSG_CARRO_VACIO = "Carro de Compras vacio.";
	
	public static final String SC_ERROR_AL_CONSULTAR_CARRO = "0505";
	public static final String MSG_ERROR_AL_CONSULTAR_CARRO = "Error al consultar productos del Carro de Compras.";
	
	public static final String SC_LOCAL_INVALIDO = "0506";
	public static final String MSG_LOCAL_INVALIDO = "Id local no existe";
	
	public static final String SC_ERROR_AL_CONSULTAR_CUPON_DESCUENTO = "0507";
	public static final String MSG_ERROR_AL_CONSULTAR_CUPON_DESCUENTO = "Error al consultar cupon de descuento.";
	
	public static final String SC_ERROR_AL_ACTUALIZAR_CARRO = "0508";
	public static final String MSG_ERROR_AL_ACTUALIZAR_CARRO = "Error al actualiza Carro de Compras.";
	
	public static final String SC_ERROR_AL_CONSULTAR_TOTAL_CARRO = "0509";
	public static final String MSG_ERROR_AL_CONSULTAR_TOTAL_CARRO = "Error al obtener el total del Carro de Compras.";
	
	public static final String SC_ERROR_AL_CONSULTAR_DESCUENTOS_CARRO = "0510";
	public static final String MSG_ERROR_AL_CONSULTAR_DESCUENTOS_CARRO = "Error al obtener descuentos del Carro de Compras.";
	
	public static final String SC_LISTA_PRODUCTOS_INVALIDA = "0511";
	public static final String MSG_LISTA_PRODUCTOS_INVALIDA = "Lista de productos no valida para Carro de Compras.";
	
	public static final String SC_ERROR_AL_ELIMINAR_CARRO = "0512";
	public static final String MSG_ERROR_AL_ELIMINAR_CARRO = "Error al eliminar Carro de Compras.";
	
	public static final String SC_NO_ES_POSIBLE_ELIMINAR_CARRO = "0513";
	public static final String MSG_NO_ES_POSIBLE_ELIMINAR_CARRO = "No es posible eliminar Carro de Compras.";
	
	public static final String SC_ERROR_AL_CONSULTAR_PROMO_CARRO = "0514";
	public static final String MSG_ERROR_AL_CONSULTAR_PROMO_CARRO = "Error al consultar promociones del carro.";
	
	public static final String SC_ERROR_AL_CONSULTAR_CRITERIOS_CARRO = "0515";
	public static final String MSG_ERROR_AL_CONSULTAR_CRITERIOS_CARRO = "Error al consultar criterios de sustitucion del Carro de Compras.";

	public static final String SC_ERROR_AL_CONSULTAR_TOTAL_PRODUCTO_PROMO = "0516";
	public static final String MSG_ERROR_AL_CONSULTAR_TOTAL_PRODUCTO_PROMO = "Error al consultar promociones del producto.";
	
}
