package cl.cencosud.jumbo.Constant;

public class ConstantList {
	
	public static final String LISTS ="list";
	public static final String LIST_ID ="list_id";//LIST UNIQUE IDENTIFIER",
	public static final String LIST_NAME ="list_name";//LIST NAME",
	public static final String CREATED_AT ="created_at";//": "DATE OF CREATION - USED TO ORDER THE LISTS",
	public static final String LIST_TYPE ="list_type";//": "M(MANUAL) OR W(GENERATED BY A PURCHASE)"
	public static final String OFFSET = "offset";
	public static final String LIMIT = "limit";
	public static final String ACTION = "action";
	public static final String QUANTITY = "quantity";
	public static final String PRODUCT_ID = "product_id";
	public static final String PRODUCTS = "products";
	public static final String TOTAL = "total";
	
		
	
	
	public static final String ACTION_LISTS = "lists";
	public static final String ACTION_DETAIL = "detail";
	public static final String LIST_TYPE_MANUAL = "M";
	public static final String LIST_TYPE_WEB = "W";
	public static final Object LIST_TYPE_PREFERED = "P";
	public static final String LIST_TYPE_ALL = "A";
	public static final String LIST_TYPE_ACTION_LISTS_PERMITED = "M-W-A"; //TIPO LISTAS PERMITIDAS PARA CABECERAS
	public static final String LIST_TYPE_ACTION_DETAIL_PERMITED = "M-W-P"; //TIPO LISTAS PERMITIDAS PARA CABECERAS
	public static final String LIST_TYPE_PUT_PERMITED = "M-W";
	
	
	
	
	//INTEGRATION CODES
		public static final String INTEGRATION_CODE_AUTH_PUT_LIST = "INT2374";
		public static final String INTEGRATION_CODE_AUTH_GET_LIST = "INT2371";
		public static final String INTEGRATION_CODE_AUTH_GET_LIST_DETAIL = "INT2372";
		public static final String INTEGRATION_CODE_AUTH_POST_LIST = "INT2373";
		
	//Errores Listas 200 - 299
	public static final String SC_ERROR_LISTAS_CLIENTE = "0200";//HttpServletResponse.SC_OK;
	public static final String MSG_ERROR_LISTAS_CLIENTE = "Cliente sin listas validas.";//HttpServletResponse.SC_OK;
	public static final String SC_ERROR_LIST_TYPE = "0201";
	public static final String MSG_ERROR_LIST_TYPE = "Tipo de lista invalida";
	public static final String SC_INVALID_SOURCE = "0202";
	public static final String MSG_INVALID_SOURCE = "Origen Invalido";
	public static final String SC_INVALID_ACTION = "0203";
	public static final String MSG_INVALID_ACTION = "Accion invalida";
	public static final String SC_TIPO_LISTA_INVALIDA = "0204";
	public static final String MSG_TIPO_LISTA_INVALIDA = "Tipo de lista Invalida";
	public static final String SC_INVALID_LIST_TYPE_MANUAL = "0205";
	public static final String MSG_INVALID_LIST_TYPE_MANUAL = "Tipo de lista Invalida";
	public static final String SC_ERROR_LIST_EMPTY = "0206";
	public static final String MSG_ERROR_LIST_EMPTY = "Lista vacia o no existe";
	public static final String SC_INVALID_LIST_NAME = "0207";
	public static final String MSG_INVALID_LIST_NAME = "Nombre de lista invalido";
	
	
	
	
}
