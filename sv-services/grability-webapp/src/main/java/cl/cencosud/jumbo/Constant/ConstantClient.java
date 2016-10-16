package cl.cencosud.jumbo.Constant;

public class ConstantClient {
	
	public static final String SERVICE_NAME = "Client";
	//INTEGRATION_CODE OSB
	public static final String INTEGRATION_CODE_OBTENER_USUARIO = "INT2356";
	public static final String INTEGRATION_CODE_AUTH_USU_INSERT_GUEST_LOGIN = "INT2357";
	public static final String INTEGRATION_CODE_UPDATE_USER = "INT2358";
	public static final String INTEGRATION_CODE_RECORVER_PASS = "INT2359";
				
	//Identificadores
	public static final String CLIENT_ID = "client_id";// "user identifier",
	public static final String RUT = "rut";
	public static final String DV = "dv";
	public static final String PASSWORD = "password"; //$NON-NLS-1$
	public static final String NEW_PASSWORD = "new_password";
		
	public static final String NAME =  "name";	
	public static final String LAST_NAME = "last_name"; 
	public static final String EMAIL = "email"; 
	public static final String COD_PHONE_NUMBER = "cod_phone_number";
	public static final String PHONE_NUMBER = "phone_number"; 
	public static final String COD_PHONE_NUMBER2 = "cod_phone_number2";
	public static final String PHONE_NUMBER2 = "phone_number2"; 
	public static final String NEWSLETTER = "newsletter";
	public static final String LIST = "list";
	public static final String ADDRESSES = "addresses";
	
	public static final String RUT_INVITADO="123123";
	
	//Acciones para el post
	public static final String INSERT	= "insert";
	public static final String RECOVER	= "recover";
	public static final String GUEST 	= "guest";
	public static final String LOGIN 	= "login";
	
	
	
	//ERRORES para el cliente del 100-199
	//___________________________________
	public static final String SC_CLAVE_INVALIDA = "0100";//HttpServletResponse.SC_OK;
	public static final String MSG_CLAVE_INVALIDA = "Contraseña incorrecta.";//HttpServletResponse.SC_OK;
	
	public static final String SC_CLIENTE_NO_EXISTE = "0101";//HttpServletResponse.SC_OK;
	public static final String MSG_CLIENTE_NO_EXISTE = "Cliente no existe.";//HttpServletResponse.SC_OK;
	
	public static final String SC_ERROR_DIRECCION_CLIENTE = "0102";//HttpServletResponse.SC_OK;
	public static final String MSG_ERROR_DIRECCION_CLIENTE = "Cliente sin direccion valida.";//HttpServletResponse.SC_OK;
		
	//Cliente existe y no se puede insertar
	public static final String SC_CLIENTE_EXISTE_AL_INSERTAR = "0104";
	public static final String MSG_CLIENTE_EXISTE_AL_INSERTAR = "Cliente existe no se puede volver a crear.";
	
	public static final String SC_NOMBRE_CLIENTE_INVALIDO = "0105";
	public static final String MSG_NOMBRE_CLIENTE_INVALIDO = "Nombre del cliente invalido.";
	
	public static final String SC_APELLIDO_CLIENTE_INVALIDO = "0106";
	public static final String MSG_APELLIDO_CLIENTE_INVALIDO = "Apellido del cliente invalido.";	
	
	public static final String SC_TELEFONO1_CLIENTE_INVALIDO = "0107";
	public static final String MSG_TELEFONO1_CLIENTE_INVALIDO = "Telefono invalido.";
	
	public static final String SC_TELEFONO2_CLIENTE_INVALIDO = "0108";
	public static final String MSG_TELEFONO2_CLIENTE_INVALIDO = "Telefono 2 cliente invalido.";
	
	public static final String SC_NEWSLETTER_CLIENTE_INVALIDO = "0109";
	public static final String MSG_NEWSLETTER_CLIENTE_INVALIDO = "Campo newsletter invalido.";
	
	public static final String SC_ID_COMUNA_NO_EXISTE = "0110";
	public static final String MSG_ID_COMUNA_NO_EXISTE = "ID comuna invalido.";
	
	public static final String SC_ID_REGION_NO_EXISTE = "0111";
	public static final String MSG_ID_REGION_NO_EXISTE = "ID region invalido.";
	
	public static final String SC_ERROR_AL_INSERTAR_CLENTE = "0112";
	public static final String MSG_ERROR_AL_INSERTAR_CLENTE = "Error al insertar cliente.";
	
	public static final String SC_ERROR_AUTH_CLIENTE = "0113";
	public static final String MSG_ERROR_AUTH_CLIENTE = "No es posible autenticar al cliente.";	
	
	public static final String SC_ERROR_AL_RESTAURAR_PASSWORD = "0114";
	public static final String MSG_ERROR_AL_RESTAURAR_PASSWORD = "No es posible restaurar contraseña.";	
	
	public static final String SC_ERROR_AL_ACTUALIZAR_CLENTE = "0115";
	public static final String MSG_ERROR_AL_ACTUALIZAR_CLENTE = "Error al actualizar cliente.";
	
	public static final String SC_ERROR_AL_INSERTAR_CLENTE_INVITADO = "0116";
	public static final String MSG_ERROR_AL_INSERTAR_CLENTE_INVITADO = "Error al crear cliente invitado.";
	
	public static final String SC_RUT_INVALIDO = "0117";//HttpServletResponse.SC_OK;
	public static final String MSG_RUT_INVALIDO = "Rut invalido.";//HttpServletResponse.SC_OK;

	public static final String SC_ERROR_RECUPERAR_CLIENTE ="0118";
	public static final String MSG_ERROR_RECUPERAR_CLIENTE ="Error al recuperar cliente.";

	public static final String SC_ID_CLIENTE_INVALIDO = "0119";
	public static final String MSG_ID_CLIENTE_INVALIDO ="Id cliente invalido.";

	public static final String SC_PASSWORD_INVALIDO = "0120";
	public static final String MSG_PASSWORD_INVALIDO = "Password invalido.";
	
	public static final String SC_ERROR_AL_CRAR_DIR_DUMMY = "0121";
	public static final String MSG_ERROR_AL_CRAR_DIR_DUMMY = "Error al crear direccion dummy.";
	
	public static final String SC_ERROR_AL_CRAR_SESS_INVITADO = "0122";
	public static final String MSG_ERROR_AL_CRAR_SESS_INVITADO = "Error al crear session invitado.";
	
	
	public static final String SC_ERROR_AL_CREAR_PASS_CLIENTE = "0123";
	public static final String MSG_ERROR_AL_CREAR_PASS_CLIENTE = "Error al crear password cliente.";
	public static final String INTEGRATION_CODE_VENTANA_DESPACHO = "INT2365";
	
	public static final String SC_ENC_PASS = "0124";
	public static final String MSG_ENC_PASS= "Error al codificar/decodificar password cliente.";
	
	public static final String SC_MAIL_CLIENTE_INVALIDO = "0125";
	public static final String MSG_MAIL_CLIENTE_INVALIDO = "Email invalido.";
	
	public static final String SC_ERROR_PARAM_CONSULTAR_CLIENTE = "0126";
	public static final String MSG_ERROR_PARAM_CONSULTAR_CLIENTE = "No es posible consultar cliente.";
	
}
