package cl.cencosud.jumbo.Constant;

public class ConstantAddress {
	
	public static final String SERVICE_NAME = "Address";
	
	//INTEGRATION_CODE OSB
	public static final String INTEGRATION_CODE_OBTENER_DIR_CLIENTE = "INT2360";
	public static final String INTEGRATION_CODE_AGREGAR_DIR_NUEVA   = "INT2361";
	public static final String INTEGRATION_CODE_EDITAR_DIR			= "INT2362";
	public static final String INTEGRATION_CODE_BORRAR_DIR			= "INT2364";
	
	public static final String ADDRESS_ID = "address_id";// UNIQUE IDENTIFIER FOR THE ADDRESS",
	public static final String NAME = "name";
	public static final String ADDRESSES = "addresses";
	public static final String NAME_ADDRESS ="name";//NAME OF THE ADDRESS",
	public static final String STREET = "street";//STREET NAME",
	public static final String NUMBER = "number";//ADDRESS NUMBER",
	public static final String HOUSE_APT = "house_apt";//HOUSE/APT NUMBER",
	public static final String MUNICIPALITY_ID = "municipality_id";//UNIQUE IDENTIFIER FOR MUNICIPALITY (COMUNA)",
	public static final String REGION_ID = "region_id";//UNIQUE IDENTIFIER FOR THE REGION",
	public static final String OBSERVATION = "observation";//ANY REMARK REGARDING THE ADDRESS (IE: RIGHT NEXT TO THE MINIMARKET)",
	public static final String DEFAULT = "default";//INDICATES WHETHER THIS IS THE DEFAULT ADDRESS (LAST USED ADDRESS)",
	public static final String LOCAL_ID = "local_id";//UNIQUE IDENTIFIER OF THE LOCAL RELATED TO THIS ADDRESS",
	public static final String DUMMY = "dummy";//INDICATES WHETHER THIS ADDRESS IS A DUMMY ONE, AUTOCREATED BY THE BACKEND"
	public static final String STREET_TYPE = "street_type";//"Street represents the type associated with the address";
			
	//Erroree 700 - 799
	public static final String SC_ERROR_RECUPERAR_DIR_CLIENTE_X_ID_CLI = "0700";
	public static final String MSG_ERROR_RECUPERAR_DIR_CLIENTE_X_ID_CLI  = "Error al recuperar direcciones del cliente.";
	
	public static final String SC_ERROR_RECUPERAR_DIR_CLIENTE_X_ID_DIR = "0701";
	public static final String MSG_ERROR_RECUPERAR_DIR_CLIENTE_X_ID_DIR  = "Error al recuperar direcciones del cliente.";
	
	public static final String SC_ERROR_RECUPERAR_DIR_CLIENTE_INICIA_SESSION = "0702";
	public static final String MSG_ERROR_RECUPERAR_DIR_CLIENTE_INICIA_SESSION = "Error al recuperar direccion ultima compra.";
	
	public static final String SC_ERROR_RECUPERAR_REGION_X_ID = "0703";
	public static final String MSG_ERROR_RECUPERAR_REGION_X_ID = "Error al recuperar region por Id.";
	
	public static final String SC_ERROR_RECUPERAR_COMUNA_X_ID = "0704";
	public static final String MSG_ERROR_RECUPERAR_COMUNA_X_ID = "Error al recuperar comuna por Id.";
	
	public static final String SC_ID_COMUNA_INVALIDO = "0705";
	public static final String MSG_ID_COMUNA_INVALIDO = "Id comuna invalido.";
	
	public static final String SC_ID_LOCAL_INVALIDO = "0706";
	public static final String MSG_ID_LOCAL_INVALIDO = "Id local invalido.";
	
	public static final String SC_ID_REGION_INVALIDO = "0707";
	public static final String MSG_ID_REGION_INVALIDO = "Id region invalido.";
	
	public static final String SC_ID_DIR_SIN_DIRECCION = "0708";
	public static final String MSG_ID_DIR_SIN_DIRECCION = "Id direccion sin direcciones validas.";

	public static final String SC_CLIENTE_SIN_DIR = "0709";
	public static final String MSG_CLIENTE_SIN_DIR = "Id cliente sin direcciones.";
	
	public static final String SC_NOMBRE_DIR_INVALIDO = "0710";
	public static final String MSG_NOMBRE_DIR_INVALIDO  = "Nombre direccion invalido.";

	public static final String SC_CALLE_DIR_INVALIDA = "0711";;
	public static final String MSG_CALLE_DIR_INVALIDA = "Calle direccion invalida.";
	
	public static final String SC_NUM_DIR_INVALIDA = "0712";;
	public static final String MSG_NUM_DIR_INVALIDA = "Numero direccion invalido.";

	public static final String SC_ERROR_INSERT_DIR_NUEVA = "0713";
	public static final String MSG_ERROR_INSERT_DIR_NUEVA = "Error al crear direccion.";
	
	public static final String SC_ERROR_UPDATE_DIR_NUEVA = "0714";
	public static final String MSG_ERROR_UPDATE_DIR_NUEVA = "Error al actualizar direccion.";

	public static final String SC_ERROR_DELETE_DIR =  "0715";
	public static final String MSG_ERROR_DELETE_DIR =  "Error al eliminar direccion.";

	public static final String SC_DIR_ID_NO_ASOCIADO_CLIENTE = "0716";
	public static final String MSG_DIR_ID_NO_ASOCIADO_CLIENTE = "Id direccion no corresponde a cliente.";

	public static final String SC_ERROR_RECUPERAR_TIPO_CALLE =  "0717";;
	public static final String MSG_ERROR_RECUPERAR_TIPO_CALLE  = "Error al recuperar tipo de calle.";
	
	public static final String SC_ERROR_TIPO_CALLE_INVALIDO =  "0718";;
	public static final String MSG_ERROR_TIPO_CALLE_INVALIDO = "Tipo de calle invalido.";
		
}
