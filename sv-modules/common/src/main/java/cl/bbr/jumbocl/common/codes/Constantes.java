package cl.bbr.jumbocl.common.codes;

/**
 
 * Constantes relacionadas con: 
 * - Estados de Ronda, Jornada, Pedidos
 * - Códigos de Perfiles
 * - Códigos de Excepción
 * - semanas
 * - zonas
 * - trxmp
 * - Alertas de OP
 * - Estados de Bloqueo de Clientes
 * - tarjetas
 * - sustitutos
 * - otras constantes.
 * @author bbr
 *
 */
/**
 * Clase que contiene las constantes que interactúan con el sistema.
 * @author bbr
 *
 */

public class Constantes {

	// Estados Ronda
	/**
	 * constante que indica el id del estado de una ronda cuando es creada  
	 */
	public static final int ID_ESTADO_RONDA_CREADA 		= 11;
	/**
	 * constante que indica el id del estado de una ronda cuando pasa a estado preparada
	 */
	public static final int ID_ESTADO_RONDA_PREPARADA	= 41;
	/**
	 * constante que indica el id del estado de una ronda cuando pasa a estado en picking
	 */
	public static final int ID_ESTADO_RONDA_EN_PICKING 	= 12;
	/**
	 * constante que indica el id del estado de una ronda cuando pasa a estado finalizada
	 */
	public static final int ID_ESTADO_RONDA_FINALIZADA 	= 13;
	
	// Estados Jornada
	/**
	 * constante que indica el id del estado de una jornada cuando no ha sido iniciada 
	 */
	public static final int ID_ESTADO_JORNADA_NO_INICIADA 	= 14;
	
	/**
	 * constante que indica el id del estado de una jornada cuando está en proceso
	 */
	public static final int ID_ESTADO_JORNADA_EN_PROCESO 	= 15;
	
	// Grupos Estados Pedido
	
	/**
	 * grupo de estados que pertenecen a pedidos desde el BOC 
	 */
	public static final String[] ESTADOS_PEDIDO_BOC			= {"3","4","21","54"};
	//Inicio Indra 
	/**
	 * grupo de estados del pedido en picking 
	 */
	public static final String[] ESTADOS_PEDIDO_PICKING		= {"3","4","5","6","7","8","9","10","21","54","70"};
	//Fin indra
	//Inicio AJARA 
	/**
	 * grupo de estados del pedido en picking, incluye anulado
	 */
	public static final String[] ESTADOS_PEDIDO_PICKING_CON_ANULADO	= {"3","4","5","6","7","8","9","10","21","54","70","20"};
	public static final String[] ESTADOS_PEDIDO_PICKING_CON_ANULADOS = {"3","4","5","6","7","8","9","10","21","54","70","69","20"};
	//Fin AJARA
	/**
	 * grupo de estados del pedido en despacho 
	 */
	public static final String[] ESTADOS_PEDIDO_DESPACHO	= {"5","6","7","8","9","10","21","54"};
    /**
     * grupo de estados del pedido para crear rutas
     */
    public static final String[] ESTADOS_PEDIDO_PARA_RUTAS  = {"7","54","21","8","6"};
	/**
	 *grupo de estados del pedido para compras web 
	 */
	public static final String[] ESTADOS_PEDIDO_COMPRA_WEB	= {"5","6","7","8","9","10","21","54"};
	/**
	 * grupo de estados del pedido ingresados y en validación 
	 */
	public static final String[] ESTADOS_PEDIDO_INGR_EN_VAL	= {"3","4"};
	/**
	 * grupo de estados del pedido ingresados y en validación para pantalla BOL(excluye anuladas)
	 */
	//public static final String[] ESTADOS_PEDIDO_INGR_EN_VAL_NULOS	= {"3","4","20","53","63","69"};
//	public static final String[] ESTADOS_PEDIDO_PARA_PICK	= {"5","6","21","70"};
	public static final String[] ESTADOS_PEDIDO_PARA_PICK	= {"5","6","7","8","9","10","21","54","70"};
//	public static final String[] ESTADOS_PEDIDO_PARA_PICK	= {"5","6"};
	public static final String ESTADOS_PEDIDO_PARA_PICKING	= "5,6";
	
	public static final String ESTADOS_PEDIDO_PICKEADO	= "8,10";
	public static final String ESTADOS_PEDIDO_NULOS	= "20,69";
	
	public static final String ESTADOS_PICKEADOS_FINALIZADOS	= "5,6,7,8,9,10,21,54,70";
	//public static final String ESTADOS_PICKEADOS_FINALIZADOS	= "5,6,7,8,9,10,54,70";

    
	public static final String ESTADOS_PEDIDO_PENDIENTES	= "(3,4,5,6,7,54,8,9,10,21)";
    
    /**
     * grupo de estados del pedido en que puede ser reprogramado 
     */
    public static final String[] ESTADOS_PEDIDO_PARA_REPROGRAMAR_DESPACHO    = {"3","4","5","6","7","8","9","54","21"};
    
    /**
     * grupo de estados del pedido en los cuales se modifica la jornada de picking al momento de reprogramar. 
     */
    public static final String[] ESTADOS_PEDIDO_PARA_REPROGRAMAR_CON_CAMBIO_DE_JPICKING    = {"3","4","5"};
    
    /**
     * grupo de estados del pedido a los cuales se les muestra el precio en la grilla de reprogramacion
     * y ademas se visualiza la opcion de sobreescribir precio o modificar este. 
     */
    public static final String[] ESTADOS_PEDIDO_PARA_REPROGRAMAR_CON_PRECIOS    = {"3","4","5","6"};
    
    /**
     * grupo de estados del pedido a los cuales se les muestra el checkbox si el pedido estuvo en transito. 
     */
    public static final String[] ESTADOS_PEDIDO_PARA_MOSTRAR_EN_TRANSITO    = {"9","8"};
    
    /**
     * grupo de estados del pedido en que se envia una alerta antes de reprogramar
     */
    public static final String[] ESTADOS_PEDIDO_PARA_ALERTAR_PREREPROGRAMAR_DESPACHO    = {"6","7","8","54"};
    
    /**
     * grupo de estados del pedido que se puede imprimir hojas despacho 
     */
    public static final String[] ESTADOS_PEDIDO_PARA_IMPRIMIR_HOJAS_DESPACHO    = {"7","54","8","9"};
    
    
	
		
	
	
	// Estados Pedido
    /**
     * id de estado del pedido pre-ingresado 
     */
    public static final int ID_ESTAD_PEDIDO_PRE_INGRESADO       = 1;
    /**
	 * id de estado del pedido ingresado 
	 */
	public static final int ID_ESTAD_PEDIDO_INGRESADO		= 3;
	/**
	 * id de estado del pedido en validación 
	 */
	public static final int ID_ESTAD_PEDIDO_EN_VALIDACION	= 4;
	/**
	 * id de estado del pedido validado 
	 */
	public static final int ID_ESTAD_PEDIDO_VALIDADO		= 5;
	/**
	 * id de estado del pedido en picking 
	 */
	public static final int ID_ESTAD_PEDIDO_EN_PICKING		= 6;
	/**
	 * id de estado del pedido en bodega 
	 */
	public static final int ID_ESTAD_PEDIDO_EN_BODEGA		= 7;
	/**
	 * id de estado del pedido en pago 
	 */
	public static final int ID_ESTAD_PEDIDO_EN_PAGO			= 54;
	//Inicio Indra
	/**
	 * id de estado del pedido pendiente de validar 
	 */
	public static final int ID_ESTAD_PEDIDO_REVISION_FALTAN	= 70;
	//Fin Indra
	/**
	 * id de estado del pedido pagado
	 */
	public static final int ID_ESTAD_PEDIDO_PAGADO			= 8;
	/**
	 * id de estado del pedido en despacho 
	 */
	public static final int ID_ESTAD_PEDIDO_EN_DESPACHO		= 9;
	/**
	 * id de estado del pedido finalizado 
	 */
	public static final int ID_ESTAD_PEDIDO_FINALIZADO		= 10;
	/**
	 * id de estado del pedido anulado 
	 */
	public static final int ID_ESTAD_PEDIDO_ANULADO			= 20;	
	/**
	 * id de estado del pedido con pago rechazado
	 */
	public static final int ID_ESTAD_PEDIDO_PAGO_RECHAZADO	= 21;

	// Estados trx medio de pago	
	/**
	 * id de estado de la transacción de pago creada  
	 */
	public static final int ID_ESTAD_TRXMP_CREADA 			= 50;
	/**
	 * id de estado de la transacción de pago pagada 
	 */
	public static final int ID_ESTAD_TRXMP_PAGADA 			= 51;
	/**
	 * id de estado de la transacción de pago rechazada
	 */
	public static final int ID_ESTAD_TRXMP_RECHAZADA 		= 52;
	/**
	 * id de estado de la transacción de pago anulada
	 */
	public static final int ID_ESTAD_TRXMP_ANULADA 			= 53;
	
	
	// Códigos de Perfiles
	/**
	 * id de perfil del pickeador 
	 */
	public static final int ID_PERFIL_PICKEADOR 		= 12;
	/**
	 * id de perfil del fiscalizador 
	 */
	public static final int ID_PERFIL_FISCALIZADOR 		= 13;
	/**
	 *  id de perfil del bodeguero
	 */
	public static final int ID_PERFIL_BODEGUERO	 		= 14;
    /**
     *  id de perfil del Auditor Sustitucion
     */
    public static final int ID_PERFIL_AUDITOR_SUSTITUCION = 15;
	
	/** 
	 * id de perfil del ejecutivo BOC
	 */
	public static final int ID_PERFIL_EJECUTIVO_BOC	= 4;

	/**
	 * id de perfil del ejecutivo venta a empresas
	 */
	public static final int ID_PERFIL_EJECUTIVO_VE		= 82;
	/**
	 * id de perfil del supervisor de venta a empresas 
	 */
	public static final int ID_PERFIL_SUPERVISOR_VE		= 83;
	/**
	 * id de perfil del Administrador de Estados de la OP 
	 */
	public static final int ID_PERFIL_ADMINISTRADOR_ESTADOS_OP = 208;
	/**
	 * id de prefil del ejecutivo BOC para modificacion de montos con exceso.
	 */
	public static final int ID_PERFIL_MODIFICADOR_EXCESOS_OP = 212;
	/**
	 * id de prefil del ejecutivo BOC para retrocer op a estado validado.
	 */
	public static final int ID_PERFIL_RETROCEDER_OP_A_VALIDADO = 212;	
	
	public static final int ID_PERFIL_MODIFICAR_FECHA_OP = 212;	
	
	public static final int ID_PERFIL_BLANQUEA_DIRECCION = 212;	
	
	// Códigos de Excepción
	/**
	 * excepción de usuario, login duplicado 
	 */
	public static final String 	_EX_USR_LOGIN_DUPLICADO		= "USR001";	
	/**
	 * excepción de usuario, id no existe 
	 */
	public static final String 	_EX_USR_ID_NO_EXISTE		= "USR002";
	
	/**
	 * excepción de usuario, existe relacion entre usuario y local
	 */
	public static final String 	_EX_USR_LOC_EXISTE			= "USR003";
	
	/**
	 * excepción de usuario, no existe relacion entre usuario y local
	 */
	public static final String 	_EX_USR_LOC_NO_EXISTE		= "USR004";
	
	/**
	 * excepción de cliente, id no existe 
	 */
	public static final String  _EX_CLI_ID_NO_EXISTE		= "CLI001";
	
	/**
	 * excepción de categoría, id no existe 
	 */
	public static final String 	_EX_CAT_ID_NO_EXISTE		= "CAT001";
	/**
	 * excepción de categoría, producto relacionado existe 
	 */
	public static final String 	_EX_CAT_PROD_REL_EXISTE		= "CAT002";
	/**
	 * excepción de categoría, subcategoria relacionada existe
	 */
	public static final String 	_EX_CAT_SUBCAT_REL_EXISTE	= "CAT003";
	/**
	 * excepción de categoría, id de subcategoria no existe 
	 */
	public static final String 	_EX_CAT_SUBCAT_ID_NO_EXISTE	= "CAT004";
	/**
	 * excepción de categoría, producto relacionado no existe 
	 */
	public static final String 	_EX_CAT_PROD_REL_NO_EXISTE	= "CAT005";
	/**
	 *  excepción de categoría, categoria no es terminal 
	 */
	public static final String 	_EX_CAT_NO_ES_TERMINAL		= "CAT006";
	/**
	 * excepción de categoría, las subcategorías son iguales 
	 */
	public static final String 	_EX_CAT_SUBCAT_IGUALES		= "CAT007";
	/**
     * excepción de categoría, asignación incorrecta de categorías según su tipo 
     */
    public static final String  _EX_CAT_SUBCAT_ASIG_INCORRECTA      = "CAT008";
    
	/**
	 * excepción de producto, el id no existe 
	 */
	public static final String 	_EX_PROD_ID_NO_EXISTE		= "PRO001";
	
	/**
	 * excepción de producto, no posee sector de picking 
	 */
	public static final String 	_EX_PROD_ID_SECTOR_PICKING		= "PRO000";
	
	/**
	 * excepción de producto, item genérico
	 */
	public static final String 	_EX_PROD_ITEM_GENERICO		= "PRO002";
	/**
	 * excepción de producto, sugerido genérico 
	 */
	public static final String 	_EX_PROD_SUG_GENERICO		= "PRO003";
	/**
	 * excepción de producto, sugerido no existe 
	 */
	public static final String 	_EX_PROD_SUG_NO_EXISTE		= "PRO004";
	/**
	 * excepción de producto, sugerido es igual a producto normal 
	 */
	public static final String 	_EX_PROD_SUG_IGUAL_PROD		= "PRO005";
	/**
	 * excepción de producto, item es igual a producto
	 */
	public static final String 	_EX_PROD_ITEM_IGUAL_PROD	= "PRO006";	
	/**
	 * excepción de producto, id de marca no existe
	 */
	public static final String 	_EX_PROD_ID_MAR_NO_EXISTE	= "PRO007";
	/**
	 * excepción de producto, id de unidad de medida no existe 
	 */
	public static final String 	_EX_PROD_ID_UME_NO_EXISTE	= "PRO008";
	/**
	 * excepción de producto, categoria no existe 
	 */
	public static final String 	_EX_PROD_CAT_NO_TIENE		= "PRO009";
	/**
	 * excepción de producto, sugerido no existe 
	 */
	public static final String 	_EX_PROD_SUG_EXISTE			= "PRO010";
	/**
	 * excepción de producto, preparable no existe 
	 */
	public static final String 	_EX_PROD_PREP_NO_EXISTE		= "PRO011";
	/**
	 * excepción de producto, producto sin categoria
	 */
	public static final String 	_EX_PROD_SIN_CAT			= "PRO012";
	/**
	 * excepción de producto, producto despublicado 
	 */
	public static final String 	_EX_PROD_DESPUBLICADO		= "PRO013";
	
	/**
	 * excepción de producto Sap, id no existe 
	 */
	public static final String 	_EX_PSAP_ID_NO_EXISTE		= "PSA001";
	/**
	 *  excepción de producto Sap, producto eliminado
	 */
	public static final String 	_EX_PSAP_PROD_ELIMINADO		= "PSA002";
	/**
	 * excepción de producto Sap, estado activo - inactivo 
	 */
	public static final String 	_EX_PSAP_ESTACT_INACTIVO	= "PSA003";
	/**
	 * excepción de producto Sap, código de producto no existe 
	 */
	public static final String 	_EX_PSAP_COD_PROD_NO_EXISTE	= "PSA004";
	/**
	 *  excepción de producto Sap, desactivado
	 */
	public static final String 	_EX_PSAP_DESACTIVADO		= "PSA005";
	/**
	 * excepción de producto Sap, sector no existe 
	 */
	public static final String 	_EX_PSAP_SECTOR_NO_EXISTE	= "PSA006";
	/**
	 * excepción de producto Sap, categoria decoinvalida 
	 */
	public static final String 	_EX_PSAP_CAT_DECOINVALIDA	= "PSA007";
	
	/**
	 * excepción de pedido, id no existe  
	 */
	public static final String 	_EX_OPE_ID_NO_EXISTE		= "OPE001";
	/**
	 * excepción de pedido, id motivo no existe 
	 */
	public static final String 	_EX_OPE_ID_MOT_NO_EXISTE	= "OPE002";
	/**
	 * excepción de pedido, usuario tiene otro pedido asignado 
	 */
	public static final String 	_EX_OPE_USR_TIENE_OTRO_PED	= "OPE003";
	/**
	 * excepción de pedido, asignado a otro usuario 
	 */
	public static final String 	_EX_OPE_ASIG_OTRO_USR		= "OPE004";
	/**
	 * excepción de pedido, la relación con el usuario no existe 
	 */
	public static final String 	_EX_OPE_REL_USR_NO_EXISTE	= "OPE005";
	/**
	 * excepción de pedido, el id del producto no existe
	 */
	public static final String 	_EX_OPE_ID_PROD_NO_EXISTE	= "OPE006";
	/**
	 * excepción de pedido, el id de detalle no existe 
	 */
	public static final String 	_EX_OPE_ID_DET_NO_EXISTE	= "OPE007";
	/**
	 * excepción de pedido, el id de la alerta no existe 
	 */
	public static final String 	_EX_OPE_ID_ALERTA_NO_EXISTE	= "OPE008";
	/**
	 * excepción de pedido, el id de la dirección no existe  
	 */
	public static final String 	_EX_OPE_ID_DIR_NO_EXISTE	= "OPE009";
	/**
	 * excepción de pedido, la relación de alerta existe 
	 */
	public static final String 	_EX_OPE_REL_ALERTA_EXISTE	= "OPE010";
	/**
	 * excepción de pedido, código de barra duplicado 
	 */
	public static final String 	_EX_OPE_DUP_COD_BARRA		= "OPE011";
	/**
	 * excepción de pedido, código de precio duplicado 
	 */
	public static final String 	_EX_OPE_DUP_COD_PRECIO		= "OPE012";
	/**
	 * excepción de pedido, el precio no existe 
	 */
	public static final String 	_EX_OPE_PRECIO_NO_EXISTE	= "OPE013";
	/**
	 * excepción de pedido, codigo de barra no existe
	 */
	public static final String 	_EX_OPE_CODBARRA_NO_EXISTE	= "OPE014";
	/**
	 * excepción de pedido, la relación con el detalle no existe 
	 */
	public static final String 	_EX_OPE_REL_DET_NO_EXISTE	= "OPE015";
	/**
	 * excepción de pedido, la relación con la ronda no existe  
	 */
	public static final String 	_EX_OPE_REL_RONDA_NO_EXISTE	= "OPE016";
	/**
	 *  excepción de pedido, el estado es inadecuado
	 */
	public static final String 	_EX_OPE_ESTADO_INADECUADO	= "OPE017"; // cuando el estado del pedido es inadecuado para la operación que se quiere realizar
	/**
	 * excepción de pedido, el id de la factura no existe 
	 */
	public static final String 	_EX_OPE_ID_FACT_NO_EXISTE	= "OPE018";
	/**
	 * excepción de pedido, tiene otro usuario  
	 */
	public static final String 	_EX_OPE_TIENE_OTRO_USR		= "OPE019";
	/**
	 * excepción de pedido, el pedido tiene alertas activas 
	 */
	public static final String 	_EX_OPE_TIENE_ALERTA_ACT	= "OPE020";
	/**
	 * excepción de pedido, el id del detalle de picking no existe 
	 */
	public static final String 	_EX_OPE_ID_DETPCK_NO_EXISTE	= "OPE021";
	/**
	 * excepción de pedido, monto de picking es mayor a monto reservado 
	 */
	public static final String 	_EX_OPE_MONTO_PICK_MAYOR	= "OPE022";
	
	/**
	 * excepción de pedido, monto precios igual 1 MXN 
	 */
	public static final String 	_EX_OPE_MONTO_MXN	= "OPE023";
	
	/**
	 * excepción de la jornada de despacho, verifica si la jornada existe 
	 */
	public static final String _EX_JDESP_JORNADA_EXISTE		= "JD001";
	/**
	 * excepción de la jornada de despacho, faltan datos
	 */
	public static final String _EX_JDESP_FALTAN_DATOS		= "JD002";
	/**
	 * excepción de la jornada de despacho, no se puede borrar 
	 */
	public static final String _EX_JDESP_NO_DELETE			= "JD003";
	/**
	 * excepción de la jornada de despacho, la jornada está asociada 
	 */
	public static final String _EX_JDESP_ASOCIADA			= "JD004";
	
	/**
	 * excepcion de la jornada de despacho, la jornada no tiene capacidad 
	 */
	public static final String _EX_JDESP_SIN_CAPACIDAD			= "JD005";
	/**
	 * excepción de la jornada de picking, el id es inválido  
	 */
	public static final String _EX_JDESP_ID_INVALIDO		= "JD006";
	
	
	/**
	 * excepción de la jornada de picking, verifica si la jornada existe
	 */
	public static final String _EX_JPICK_JORNADA_EXISTE		= "JP001";
	/**
	 * excepción de la jornada de picking, faltan datos 
	 */
	public static final String _EX_JPICK_FALTAN_DATOS		= "JP002";
	/**
	 * excepción de la jornada de picking, el id es inválido  
	 */
	public static final String _EX_JPICK_ID_INVALIDO		= "JP003";
	/**
	 * excepción de la jornada de picking,no se puede eliminar  
	 */
	public static final String _EX_JPICK_NO_DELETE			= "JP004";
	/**
	 * excepción de la jornada de picking,verifica si la jornada está asociada 
	 */
	public static final String _EX_JPICK_ASOCIADA			= "JP005";
		
	/**
	 * excepción de local, el id de local es inválido 
	 */
	public static final String _EX_LOCAL_ID_INVALIDO		= "LOC001";
	
	/**
	 * excepción de poligono, el numero del poligono exite para la comuna 
	 */
	public static final String _EX_POLIGONO_NUM_EXISTE		= "POL001";

	/**
	 *excepción de Horario de Despacho, el id del horario no existe 
	 */
	public static final String _EX_ID_H_DESP_NO_EXISTE		= "HOR001";
	/**
	 * excepción de Horario de picking, el id del horario no existe
	 */
	public static final String _EX_ID_H_PICK_NO_EXISTE		= "HOR002";	
	/**
	 *excepción de Horario de Picking, el id del horario no se puede borrar 
	 */
	public static final String _EX_ID_H_PICK_NO_DELETE		= "HOR003";
	
	/**
	 * excepción de jornada, la capacidad de la jornada para el día lunes es inválida
	 */
	public static final String _EX_JOR_CAPAC_LU				="JORCA001";
	/**
	 * excepción de jornada, la capacidad de la jornada para el día martes es inválida
	 */
	public static final String _EX_JOR_CAPAC_MA				="JORCA002";
	/**
	 * excepción de jornada, la capacidad de la jornada para el día miércoles es inválida
	 */
	public static final String _EX_JOR_CAPAC_MI				="JORCA003";
	/**
	 *excepción de jornada, la capacidad de la jornada para el día jueves es inválida 
	 */
	public static final String _EX_JOR_CAPAC_JU				="JORCA004";
	/**
	 *excepción de jornada, la capacidad de la jornada para el día viernes es inválida 
	 */
	public static final String _EX_JOR_CAPAC_VI				="JORCA005";
	/**
	 *excepción de jornada, la capacidad de la jornada para el día sábado es inválida 
	 */
	public static final String _EX_JOR_CAPAC_SA				="JORCA006";
	/**
	 *excepción de jornada, la capacidad de la jornada para el día domingo es inválida 
	 */
	public static final String _EX_JOR_CAPAC_DO				="JORCA007";
	/**
	 * excepción de jornada, las horas válidas de la jornada para el día lunes son inválidas 
	 */
	public static final String _EX_JOR_HRAS_VAL_LU			="JORVA001";
	/**
	 *excepción de jornada, las horas válidas de la jornada para el día martes son inválidas 
	 */
	public static final String _EX_JOR_HRAS_VAL_MA			="JORVA002";
	/**
	 *excepción de jornada, las horas válidas de la jornada para el día miércoles son inválidas 
	 */
	public static final String _EX_JOR_HRAS_VAL_MI			="JORVA003";
	/**
	 *excepción de jornada, las horas válidas de la jornada para el día jueves son inválidas 
	 */
	public static final String _EX_JOR_HRAS_VAL_JU			="JORVA004";
	/**
	 *excepción de jornada, las horas válidas de la jornada para el día viernes son inválidas 
	 */
	public static final String _EX_JOR_HRAS_VAL_VI			="JORVA005";
	/**
	 *excepción de jornada, las horas válidas de la jornada para el día sabado son inválidas 
	 */
	public static final String _EX_JOR_HRAS_VAL_SA			="JORVA006";
	/**
	 *excepción de jornada, las horas válidas de la jornada para el día domingo son inválidas 
	 */
	public static final String _EX_JOR_HRAS_VAL_DO			="JORVA007";
	/**
	 *excepción de jornada, las horas web de la jornada para el día lunes son inválidas 
	 */
	public static final String _EX_JOR_HRAS_WEB_LU			="JORWE001";
	/**
	 * excepción de jornada, las horas web de la jornada para el día martes son inválidas
	 */
	public static final String _EX_JOR_HRAS_WEB_MA			="JORWE002";
	/**
	 *excepción de jornada, las horas web de la jornada para el día miércoles son inválidas 
	 */
	public static final String _EX_JOR_HRAS_WEB_MI			="JORWE003";
	/**
	 *excepción de jornada, las horas web de la jornada para el día jueves son inválidas 
	 */
	public static final String _EX_JOR_HRAS_WEB_JU			="JORWE004";
	/**
	 *excepción de jornada, las horas web de la jornada para el día viernes son inválidas 
	 */
	public static final String _EX_JOR_HRAS_WEB_VI			="JORWE005";
	/**
	 *excepción de jornada, las horas web de la jornada para el día sábado son inválidas 
	 */
	public static final String _EX_JOR_HRAS_WEB_SA			="JORWE006";
	/**
	 *excepción de jornada, las horas web de la jornada para el día domingo son inválidas 
	 */
	public static final String _EX_JOR_HRAS_WEB_DO			="JORWE007";
	
	
	/**
	 * excepción de pedido, id de pedido es inválido 
	 */
	public static final String _EX_PED_ID_INVALIDO			="PED001";
	
	/**
	 * excepción de ronda, id de ronda es inválido 
	 */
	public static final String _EX_RON_ID_INVALIDO			="RON001";
	/**
	 *excepción de ronda, id de ronda es nula 
	 */
	public static final String _EX_RON_ID_NULA				="RON002";
	/**
	 * excepción de ronda, el estado de la ronda es inadecuado 
	 */
	public static final String _EX_RON_ESTADO_INADECUADO	="RON003";
	/**
	 * excepción de ronda, el máximo de pedidos por ronda es erróneo 
	 */
	public static final String _EX_RON_MAX_PEDIDOS_X_RONDA	="RON004";
	/**
	 * excepción de ronda, la ronda no tiene pedidos seleccionados 
	 */
	public static final String _EX_RON_SIN_PEDIDOS_SELEC	="RON005";
	/**
	 * excepción de ronda, el pedido no coincide 
	 */
	public static final String _EX_RON_PEDIDO_NO_COINCIDE	="RON006";
	
	/**
	 * excepción el código de barra no existe 
	 */
	public static final String _EX_CODBARRA_NO_EXISTE		="CBA001";
	
	
	//estados en general
	/**
	 * excepción estado inválido 
	 */
	public static final String _EX_ESTADO_INVALIDO			="EST001";
	
	// semanas
	/**
	 * excepción id de semana inválido 
	 */
	public static final String _EX_SEMANA_ID				= "SEM001";
	
	// zonas
	/**
	 * excepción de zona, el id es inválido 
	 */
	public static final String _EX_ZONA_ID_INVALIDA			= "ZON001";	
	
	// trxmp
	/**
	 * excepción de transacción, la transacción está duplicada 
	 */
	public static final String _EX_TRX_DUPLICADA			= "TRX001";	
	/**
	 * excepción de transacción, el producto viene sin Unidad de Medida 
	 */
	public static final String _EX_TRX_PROD_SIN_UM			= "TRX002";
	/**
	 * excepción de transacción, no se insertó el encabezado 
	 */
	public static final String _EX_TRX_NO_INSERTA_ENC		= "TRX003";
	/**
	 * excepción de transacción, no se insertó el detalle 
	 */
	public static final String _EX_TRX_NO_INSERTA_DET		= "TRX004";
	/**
	 * excepción de transacción, la transacción no existe
	 */
	public static final String _EX_TRX_NO_EXISTE			= "TRX005";
	/**
	 * excepción de transacción, los productos son inválidos 
	 */
	public static final String _EX_TRX_PRODS_INVALIDOS		= "TRX006";
	/**
	 * excepción de transacción, el estado es inadecuado 
	 */
	public static final String _EX_TRX_ESTADO_INADECUADO	= "TRX007";
	
	/**
	 * excepción de transacción, el estado es inadecuado 
	 */
	public static final String _EX_TRX_CODBAR_INVALIDO	= "TRX008";
	
	/**
	 * id de sector es inválido 
	 */
	public static final String _EX_ID_SECTOR_INVALIDO 		="SEC001";
	
	/**
	 * excepción de marca, el id no existe 
	 */
	public static final String _EX_MAR_ID_NO_EXISTE			= "MAR001";

	/**
	 * excepción de campana, id no existe 
	 */
	public static final String 	_EX_CAM_ID_NO_EXISTE		= "CAM001";
	/**
	 * excepción de campana, elemento relacionado existe 
	 */
	public static final String 	_EX_CAM_ELEM_REL_EXISTE		= "CAM002";
	/**
	 * excepción de campana, elemento relacionado no existe 
	 */
	public static final String 	_EX_CAM_ELEM_REL_NO_EXISTE	= "CAM003";
	
	/**
	 * excepción de campana, el estado es inadecuado 
	 */
	public static final String 	_EX_CAM_ESTADO_INADECUADO	= "CAM004";
	
	/**
	 * excepción de elemento, id no existe 
	 */
	public static final String 	_EX_ELM_ID_NO_EXISTE		= "ELM001";
	
	// Venta Empresas - inicio -
	/**
	 * excepción de no existe registro
	 */
	public static final String	_EX_REG_ID_NO_EXISTE		= "VTE001";
	
	/**
	 * excepción llave duplicada
	 */
	public static final String	_EX_KEY_DUPLICADA			= "VTE002";
	
	// excepciones del formulario de picking 
	/**
	 * excepción al finalizar el formulario manual de picking
	 */
	public static final String	_EX_VE_FPIK_FINALIZA_MAL	= "VTEFP007";
	
	/**
	 *Excepción al recepcionar Ronda a través del formulario manual de picking.
	 */
	public static final String  _EX_VE_FPIK_RECEPCIONA_MAL	= "VTEFP008";
	
	/**
	 * Excepción al Reversar picking a través del formulario manual de picking
	 */
	public static final String  _EX_VE_FPIK_NO_REVERSA_PICK	= "VTEFP009";
	
	/**
	 * Excepción al Reversar sustitución a través del formulario manual de picking
	 */
	public static final String  _EX_VE_FPIK_NO_REVERSA_SUST	= "VTEFP010";
	
	/**
	 *  Excepcion al reversar sustitución,no existe el registro en la tabla detalle 
	 *  de picking, formulario manual de picking
	 */
	public static final String  _EX_VE_FPIK_NO_EXISTE_RELACION	= "VTEFP011";
	
	/**
	 * Excepción al realizar la relacion entre producto pedido y productos pickeados 
	 * en el formulario manual de picking
	 */
	public static final String  _EX_VE_FPIK_NO_REALIZO_RELACION	= "VTEFP012";
	
	/**
	 * Excepción al ingresar un detalle inválido
	 */
	public static final String  _EX_VE_FPIK_ID_DETALLE_INVALIDO	= "VTEFP013";
	
	/**
	 * Excepción al finalizar formulario manual de picking, el perfil no es pickeador.
	 */
	public static final String  _EX_VE_FPIK_NO_ES_PICKEADOR	= "VTEFP014";
	
	/**
	 * Excepción cuando no existe la op para una ronda determinada
	 */
	public static final String  _EX_VE_FPIK_OP_RONDA_NO_EXISTE	= "VTEFP015";
	
	
	/**
	 * Excepción al realizar picking.
	 */
	public static final String  _EX_VE_FPIK_NO_REALIZO_PICKING	= "VTEFP016";
	
	//Generacion de Pedidos
	
	/**
	 * Generacion de pedidos campo id cliente incorrecto
	 */
	public static final String  _EX_VE_GP_CAMPOS_INC		= "VEGP001";
	/**
	 * Generacion de pedidos excede capacidad despacho y/o picking
	 */	
	public static final String  _EX_VE_GP_EXCEDE_CAPAC			= "VEGP002";
	
	/**
	 * No se pudo guardar la ficha tecnica en la bade datos
	 */
	public static final String  _EX_PFT_NO_REALIZO_UPDATE	    = "PTF001";
		
	// Venta Empresas - fin -
	
	//Alertas de OP
	/**
	 * Alerta por si es la primera compra web realizada 
	 */
	public static int ALE_PRIMERA_COMPRA_WEB	= 1;
	/**
	 * Alerta validar a cliente invitado por compra realizada
	 */
	public static int ALE_CLIENTE_INVITADO		= 39;
	/**
	 * Alerta por si se cambia de apellidos 
	 */
	public static int ALE_CAMBIA_APELLIDOS		= 2;
	/**
	 * Alerta por si se ingresa una dirección nueva 
	 */
	public static int ALE_DIRECC_NUEVA			= 3;
	/**
	 * Alerta por el cupo del medio de pago 
	 */
	public static int ALE_MED_PAGO_SIN_CUPO		= 4;
	/**
	 * Alerta por bloqueo del medio de pago 
	 */
	public static int ALE_MEDIO_PAGO_BLOQU		= 5;	
	/**
	 * Alerta por si se pasa del monto limite 
	 */
	public static int ALE_SOBRE_MONTO_LIMITE	= 6;
	/**
	 * Alerta por bloqueo realizado por jumbocl 
	 */
	public static int ALE_BLOQUEADO_X_JUMBOCL	= 7;
	/**
	 * Alerta por si se pasa de la fecha y hora del despacho 
	 */
	public static int ALE_FEC_HORA_DESPACHO		= 8;
	/**
	 * Alerta por cambio de zona de despacho 
	 */
	public static int ALE_CAMBIO_ZONA_DESPACHO	= 9;
	/**
	 * Alerta por compras con factura 
	 */
	public static int ALE_COMPRA_CON_FACTURA	= 10;
	/**
	 * Alerta por pedido sin sector  
	 */
	public static int ALE_PEDIDO_SIN_SECTOR		= 11;
	/**
	 * Alerta por atrasos con la fecha y hora de picking 
	 */
	public static int ALE_FEC_HOR_PICK_ATRAS	= 12;
	/**
	 * Alerta por alguna dirección nueva en varias zonas 
	 */
	public static int ALE_DIR_NVA_VARIAS_ZONAS	= 13;
	/**
	 * Alerta por tarjeta Cat para el medio de pago 
	 */
	public static int ALE_MED_PAGO_TARJETA_CAT	= 14;
	/**
	 * Alerta sobre algún medio de pago alternativo 
	 */
	public static int ALE_MED_PAGO_ALTERNATIVO	= 15;

	
	//****************************************************
	/**
	 * Alerta 
	 */
	public static int ALE_VALIDA_CUENTA_PROBLEMA_MQ     = 16;
	/**
	 * Alerta 
	 */
	public static int ALE_VALIDA_CUENTA_CLIENTE_NO_PARIS   = 17;
	/**
	 * Alerta 
	 */
	public static int ALE_VALIDA_CUENTA_ERROR_DATOS_INGRESADOS = 18;
	/**
	 * Alerta   
	 */
	public static int ALE_VALIDA_CUENTA_ERROR_EJECUCION   = 19;
	/**
	 * Alerta 
	 */
	public static int ALE_VALIDADOR_MPAGO_NO_EJECUTADO = 20;
	/**
	 * Alerta 
	 */
	public static int ALE_VALIDADOR_MPAGO_ERROR_DATOS_CLIENTE = 21;
	/**
	 * Alerta 
	 */
	public static int ALE_VALIDADOR_MPAGO_CON_BLOQUEOS = 22;
	/**
	 * Alerta 
	 */
	public static int ALE_VALIDADOR_MPAGO_SIN_CUPO = 23;
	/**
	 * Alerta 
	 */
	public static int ALE_VALIDADOR_MPAGO_NO_PERTENECE_AL_CLIENTE = 24;
	/**
	 * Alerta 
	 */
	public static int ALE_MEDIO_PAGO_PARIS = 25;

	
	/**
	 * Alerta 
	 */
	public static int ALE_OP_TODOS_FALTANTES = 26;

	
	
	//**************************************************** 

	//Alertas para VE
	/**
	 * Alerta de cantidad de productos excedidas
	 */
	public static int ALE_CANT_PROD_EXCEDIDAS	= 27;
	/**
	 * Alerta de medio de pago sea linea de credito
	 */
	public static int ALE_MED_PAGO_LINEA_CRED	= 28;
	/**
	 * Alerta de pedido asociado a zona SPOT
	 */
	public static int ALE_ZONA_SPOT				= 29;
	
	//************* Eventos Personalizados 
	/**
	 * Alerta de pedido asociado a EVENTO PERSONALIZADO
	 */
	public static int ALE_EVENTO_PERSONALIZADO	= 30;
	
	//************** Alertas de Promociones
	
	/**
	 * Alerta de recalculo de promociones por eliminacion de productos
	 */
	public static int ALE_RECALC_PROMO_ELIM_PROD	= 31;
	/**
	 * Alerta de recalculo de promociones por modificacion
	 * del medio de pago
	 */
	public static int ALE_RECALC_PROMO_CAMBIO_MPAGO	= 32;
	
	/**
	 * Primera compra con producto Susceptible a fraude
	 */
	public static int ALE_PROD_SUSCEPTIBLE_A_FRAUDE = 33;
	
	/**
	 * Comuna Susceptible a fraude
	 */
	public static int ALE_COMUNA_SUSCEPTIBLE_A_FRAUDE = 34;
	
	/**
	 * Alerta Poligono Cero
	 */
	public static int ALE_POLIGONO_CERO = 35;
	
	
	public static int ALE_ARTICULO_ESCOLAR = 36;
    
    
    public static int ALE_PAGO_NO_AUTENTICADO = 37;

    /**
     * Alerta con productos con categoria validacion etapa I.
     */
    public static int ALE_PRODUCTO_CATEGORIA_VALIDACION = 38;  
    
    /**
	 * Alerta primera compra con retiro en local
	 */
	public static int ALE_PRIMERA_COMPRA_RETIRO_EN_LOCAL = 40;
    
	// Estados Bloqueo del Cliente
	/**
	 * Estado de Cliente Bloqueado 
	 */
	public static final String 	CLI_BLOQUEADO		= "B";
	/**
	 * Estado de Cliente Desbloqueado
	 */
	public static final String 	CLI_DESBLOQUEADO	= "D";
	
	/**
	 * Estado del Cliente sin datos modificados 
	 */
	public static final String 	CLI_NO_MOD_DATOS	= "00";
	/**
	 * Estado del Cliente con apellidos Modificados  
	 */
	public static final String 	CLI_MOD_APELLIDOS	= "01";
	
	//otras constantes
	
	
	/**
	 * define la categoria de validacion manual 
	 */
	public static final String 	KEY_VALIDACION_MANUAL_OP 	=  "validacion";	
	/**
	 * define la categoria de alerta de compra para validacion automatica 
	 */
	public static final String 	KEY_ALERTA_COMPRA_OP 		=  "alerta compra";
	/**
	 * define el monto límite 
	 */
	public static final String 	MONTO_LIMITE_OP 			=  "MONTO_LIMITE_OP";
	/**
	 * define la diferencia de horas  
	 */
	public static final String 	DIFERENCIA_HORAS			= "02:00:00";
	/**
	 * define la mínima diferencia de horas
	 */
	public static final long 	MIN_DIFERENCIA_HORAS		= 2*60*60*1000L;
	/**
	 * define el máximo en la diferencia de horas normal
	 */
	public static final long 	MAX_DIFERENCIA_HORAS_NORMAL	= 4*60*60*1000L;
	/**
	 * define el máximo en la diferencia de horas express
	 */
	public static final long 	MAX_DIFERENCIA_HORAS_EXPRESS= 1*60*60*1000L;
	/**
	 * define las horas en milisegundos 
	 */
	public static final long 	HORA_EN_MILI_SEG			= 60*60*1000L;
	/**
	 * define las horas para validar un pedido con despacho express 
	 */
	public static final long 	HORAS_VALIDACION_EXPRESS	= 1;
	/**
	 * define las horas para comprar un pedido con despacho express 
	 */
	public static final long 	HORAS_COMPRA_EXPRESS	    = 2;
	/**
	 * define las horas de inicio para validar un pedido con despacho express 
	 */
	public static final long 	HORAS_INICIO_VALIDACION_EXPRESS	= 8;
	/**
	 * define a un parámetro como obligatorio 
	 */
	public static final String 	PARAMETRO_OBLIGATORIO		= "OBLIGATORIO";
	/**
	 * define el estado del MIX (nuevo)
	 */
	public static final String 	MIX_ESTADO					= "N";
	/**
	 * define un genérico en el mix (P = Producto) (valor por defecto)
	 */
	public static final String 	MIX_GENERICO				= "P";
	/**
	 * define el tipo del Mix (valor por defecto)
	 */
	public static final String 	MIX_TIPO					= "";
	/**
	 * define la marca de un producto del mix (valor por defecto)
	 */
	public static final int		MIX_MARCA					= 0;
	/**
	 * define la unidad de medida de un producto del mix (valor por defecto)
	 */
	public static final int		MIX_UNIDAD_MEDIDA			= 0;
	/**
	 * define el contenido de un producto mix (valor por defecto)
	 */
	public static final int		MIX_CONTENIDO				= 0;
	/**
	 * define algún comentario (valor por defecto)
	 */
	public static final String 	MIX_ADM_COMENTARIOS			= "";
	/**
	 * define si un producto del mix es preparable (valor por defecto) 
	 */
	public static final String 	MIX_ES_PREPARABLE			= "";
	/**
	 *  define la medida (valor por defecto)
	 */
	public static final int		MIX_INT_MEDIDA				= 0;	
	/**
	 * define el máximo (valor por defecto) 
	 */
	public static final int		MIX_INT_MAXIMO				= 0;
	/**
	 *log BO producto ID 
	 */
	public static final int		LOG_BO_PRO_ID				= 0;
	/**
	 *  estado despublicado 
	 */
	public static final String 	ESTADO_DESPUBLICADO			= "D";
	/**
	 *  estado publicado
	 */
	public static final String 	ESTADO_PUBLICADO			= "A";
	/**
	 * define a un estado como nuevo 
	 */
	public static final String 	ESTADO_NUEVO				= "N";
	/**
	 * define a un estado como activado 
	 */
	public static final String 	ESTADO_ACTIVADO				= "A";
	/**
	 * tipo de producto genérico 
	 */
	public static final String 	TIPO_GENERICO				= "G";
	/**
	 * tipo de documento es factura
	 */
	public static final String 	TIPO_DOC_FACTURA			= "F";
    
    /**
     * Descripcion tipo de documento es factura
     */
    public static final String  TIPO_DOC_FACTURA_DESC            = "Factura";
    
	/**
	 *  tipo de documento es boleta
	 */
	public static final String 	TIPO_DOC_BOLETA				= "B";
    
    /**
     *  tipo de documento es boleta
     */
    public static final String  TIPO_DOC_BOLETA_DESC             = "Boleta";
    
	/**
	 * cantidad max de productos para un pedido 
	 */
	public static final int 	MAX_NUM_PRODUCTOS			= 180;
	/**
	 * define una variable sin datos 
	 */
	public static final String 	SIN_DATO					= "---";
	/**
	 * define un producto normal en el detalle de picking  
	 */
	public static final String 	DET_PICK_NORMAL				= "N";	
	/**
	 * define un producto sustituto en el detalle de picking   
	 */
	public static final String 	DET_PICK_SUST				= "S";
	/**
	 * define la cantidad máxima de pedidos que puede tener una ronda
	 */
	public static final int 	CANT_MAX_PED_X_RONDA		= 6;
	/**
	 * define la unidad de medida de un Sap 
	 */
	public static final String 	UMEDSAP_ST					= "ST";
	/**
	 * define que un producto es preparable  
	 */
	public static final String 	PRODUCTO_ES_PREPARABLE		= "S";
	
	/**
	 * máximo de transacciones 
	 */
	public static final int     TRXMP_MAX_POND_DETALLE      = 180;
	//public static final int     TRXMP_MAX_POND_DETALLE      = 1;
	/**
	 * despacho del producto sap 
	 */
	public static final long    DESPACHO_ID_PRODUCTO_SAP    = 0L;
	/**
	 * codigo de barra de las zanahorias dole bolsa 300 grs mientras encuentran el cod. de despacho
	 */
	//public static final String  DESPACHO_COD_BARRA          = "7809530600062"; codigo de barra de las zanahoras dole bolsa 300 grs mientras encuentran el cod. de despacho	
	public static final String  DESPACHO_COD_BARRA          = "2082001274763"; //codigo de barra del despacho
	
	/**
	 * Descripción de la glosa de despacho 
	 */
	public static final String  DESPACHO_GLOSA_DESCR        = "Costo Despacho";
	/**
	 *identifica al medio de pago con tarjeta CAT 
	 */
	public static final String  MEDIO_PAGO_CAT		= "CAT";
    
    /**
     *Descripcion al medio de pago con tarjeta CAT 
     */
    public static final String  MEDIO_PAGO_CAT_DESC      = "Tarjeta Más";
    
	/**
	 * identifica al medio de pago con tarjeta paris 
	 */
	public static final String  MEDIO_PAGO_PARIS	= "PAR";
	/**
	 * Descripcion al medio de pago con tarjeta bancaria 
	 */
	public static final String  MEDIO_PAGO_TBK		= "TBK";
    
    /**
     * identifica al medio de pago con tarjeta bancaria 
     */
    public static final String  MEDIO_PAGO_TBK_DESC      = "Tarjeta Bancaria";
    
	/**
	 * identifica al supervisor BO
	 */
	public static final long  	SUPERVISOR_BO		= 4;
	
	//Valores de productos sustitutos con cambio de formato
	/**
	 * identifica si existe un sustituto con cambio de formato 
	 */
	public static final int		SUST_CAMB_FORM_EXISTE		= 1;
	/**
	 *  identifica si no existe un sustituto con cambio de formato
	 */
	public static final int		SUST_CAMB_FORM_NO_EXISTE	= 0;
	
	//Tipos de bin
	/**
	 * identifica sin cuando un bins es virtual 
	 */
	public static final String  TIPO_BIN_VIRTUAL			= "V";
	/**
	 * identifica sin cuando un bins es fijo 
	 */
	public static final String  TIPO_BIN_FIJO				= "F";
	
	//Tipos de tarjeta 
	/**
	 * tipo de tarjeta CAT 
	 */
	public static final String  TIPO_TARJETA_CAT			= "CAT";	
	/**
	 * tarjeta Cat DUMMY 
	 */
	public static final String  TARJETA_CAT_DUMMY			= "1111111111111111";
	
	
	//Inicio del codigo de barra de productos pesables
	/**
	 * prefijo del codigo de barra para productos pesables 
	 */
	public static final String  COD_BARRA_PESABLE_PREFIJO	= "24";
	/**
	 * largo del codigo de barra para productos pesables 
	 */
	public static final int		COD_BARRA_PESABLE_LONG		= 7;
	
    // Constantes para LOG
	/**
	 * Mensaje de exito para reagendar jornada de despacho 
	 */
	public static final String  REAGENDA_JORNADA_DESP       = "Jornada Reagendada Satisfactoriamente";
	/**
	 * Mensaje de exito para actualizar el costo del despacho 
	 */
	public static final String  ACTUALIZA_COSTO_DESP       = "Costo de Despacho modificado Satisfactoriamente";

	/**
	 * Mensaje de Cambio de Dirección de Despacho 
	 */
	public static final String  CAMBIO_DIR_DESP             = "Cambio Dirección de Despacho";	
	/**
	 * Mensaje de Cambio de Medio de Pago 
	 */
	public static final String  CAMBIO_MEDIO_PAGO           = "Medio de Pago cambiado a: ";
	/**
	 * Mensaje de Cambio de Local del pedido 
	 */
	public static final String  CAMBIO_LOCAL_PED_SECTOR     = "El pedido debe ser validado nuevamente por cambio de local y sectores de picking ";
	
	
	//Sustitutos
	/**
	 * define un producto sustituto 
	 */
	public static final String PROD_SUSTITUIDO				= "S";
	/**
	 * define un sustituto no reconocido 
	 */
	public static final String SUSTITUTO_SIN_DESCRIP		= "SUSTITUIDO POR EL MISMO PRODUCTO";
	
	//nivel de sectores
	/**
	 * define el nivel de sección 
	 */
	public static final int NIVEL_SECCION					= 1;

	//Valores que se muestran en 'con_precio' del producto Sap
	/**
	 * Valor positivo que se muestra en 'con_precio' del producto Sap
	 */
	public static final String CON_PRE_S = "Si";
	/**
	 * Valor negativo que se muestra en 'con_precio' del producto Sap 
	 */
	public static final String CON_PRE_N = "No";
	
	//Mensaje que indica que la descarga fue finalizada
	/**
	 * Mensaje que indica que la descarga de una ronda fue finalizada de forma correcta 
	 */
	public static final String RONDA_MNS_FIN_DESCARGA 		= "La ronda ha sido descargada satisfactoriamente";
	
	/**
	 * Mensaje que indica que el pago fue rechazado 
	 */
	public static final String MNS_PAGO_RECHAZADO			= "PAGO RECHAZADO";
	
	/**
	 * Valor del porcentaje a utilizar para la aproximacion de cantidades faltantes
	 * 
	 */
	public static final int PORCENTAJE_APROX_FALTANTES		= 10;
	
	/**
	 * Valor del porcentaje maximo a utilizar para la aproximacion de cantidades faltantes
	 * 
	 */
	public static final int PORCENTAJE_APROX_MAX_FALTANTES		= 20;
	
	/**
	 * Indica el filtro de dia 
	 */
	public static final String PARAMETRO_DIA				= "AM";
	
	/**
	 * Indica el filtro de noche 
	 */
	public static final String PARAMETRO_NOCHE				= "PM";
	
	/**
	 * Indica el tope de diferencia entre mañana y tarde
	 */
	public static final String PARAMETRO_TOPE_MNA			= "12:00:00";
	
	/**
	 * Mensaje que indica que se modifico la politica de sustitucion
	 */
	public static final String MNS_POL_SUSTITUCION			= "Modificación de la Politica de Sustitución";
	
	/**
	 * Indica el estado activo de la categoria SAP
	 */
	public static final String ESTADO_COD_SAP_ACTIVO		= "1";
	
	/**
	 * Indica el estado inactivo de la categoria SAP
	 */
	public static final String ESTADO_COD_SAP_INACTIVO		= "0";
	
	/**
	 * Mensaje que indica que se modifico la politica de sustitucion
	 */
	public static final String MNS_CAMBIAR_EN_PAGO			= "Modificación de estado a 'En Pago'";
	
	/**
	 * Indica la extension de las imagenes de la ficha de productos
	 */
	public static final String PRODUCTO_FO_IMG_EXT		= ".jpg";
	
	/**
	 * Indica el maximo numero de semanas en un anio
	 */
	public static final int MAX_SEMANAS_X_ANIO			= 53;
	
	/**
	 * Indica que no fue posible crear el calendario
	 */
	public static final String MNS_ERROR_CREA_CALEND		= " No es posible crear el calendario.";

	/**
	 * Indica que existe jornadas 
	 */
	public static final String MNS_EXS_JORNADA				= "Existe(n) semana(s) que tienen jornada.";

	/**
	 * Indica que existe pedidos en la jornada
	 */
	public static final String MNS_EXS_PEDIDO_EN_JORNADA	= "Existe(n) semana(s) que tienen jornadas con pedidos asociados.";
	
	/**
	 * Indica que existe jornadas de despacho, en otra zona, que esta haciendo uso de la misma jornada de picking
	 */
	public static final String MNS_EXS_JORNADA_PICKING_EN_USO= "Existe(n) Jornada(s) de Despacho, en una Zona Distinta, que esta(n) haciendo uso de la(s) misma(s) Jornada(s) de Picking.";

	/**
	 * La Cantidad de Jornadas de Picking de la Semana de Origen Difieren de la Semana de Destino
	 */
	public static final String MNS_EXS_JORNADA_PICKING_DIFIERE_CANTIDAD= "La Cantidad de Jornadas de Picking de la Semana de Origen Difiere de las Jornadas de Picking de la Semana de Destino.";

	/**
	 * Los Horarios de Picking de la Semana de Origen Difiere en los Horarios de Picking de la Semana de Destino.
	 */
	public static final String MNS_EXS_HORARIO_PICKING_DIFIERES= "Los Horarios de Picking de la Semana de Origen Difiere en los Horarios de Picking de la Semana de Destino.";

	/**
	 * Indica que existe error al eliminar las jornadas entre semanas de inicio y fin
	 */
	public static final String MNS_ERROR_ELIM_JORNADAS		= "Error al eliminar las jornadas que existen entre semanas de inicio y fin";
	
	/**
	 * Indica que no se actualizo la jpicking en las jornadas de despacho
	 */
	public static final String MNS_ERROR_ACT_JORNADAS_DESP	= "Error al actualizar la jornada de picking en las jornadas de despacho";
	
	/**
	 * Indica la maxima diferencia en dias entre la nueva jornada de despacho a crear y la ultima jornada de picking correspondiente
	 */
	public static final int MAS_DIFER_DIAS_CREAR_JORNADAS	= 14;
	
	/**
	 * Indica q la diferencias en dias de creacion de jornadas es mayor al parametro 
	 */
	public static final String MNS_ERROR_DIFER_DIAS_CREAR_JORNADAS	= "La diferencia entre fecha de ultima jornada de picking y " +
					"fecha de nueva jornada de despacho es mayor a "+MAS_DIFER_DIAS_CREAR_JORNADAS + "dias.";
	
	/**
	 * Indica que el código de empresa no existe 
	 */
	public static String _EX_EMP_ID_NO_EXISTE 			= "EMP001";
	
	/**
	 * Indica que el código de la nueva empresa no fue creado 
	 */
	public static String _EX_EMP_ID_NO_CREADO 			= "EMP002";
	
	/**
	 * Indica que el código de sucursal no existe 
	 */
	public static String _EX_SUC_ID_NO_EXISTE 			= "SUC001";
	
	/**
	 * Indica que el código de la nueva sucursal no fue creado 
	 */
	public static String _EX_SUC_ID_NO_CREADO 			= "SUC002";
	
	//Excepciones para Cotizaciones
	/**
	 * Indica que el código de cotizacion no existe 
	 */
	public static String _EX_COT_ID_NO_EXISTE 			= "COT001";

	/**
	 * Indica que la cotizacion tiene alerta activa
	 */
	public static String _EX_COT_TIENE_ALERTA_ACT		= "COT002";
	
	/**
	 * excepción de cotización, usuario tiene otra cotización asignada 
	 */
	public static final String 	_EX_COT_USR_TIENE_OTRA_COT	= "COT003";
	
	
	/**
	 * excepción de cotización, tiene otro usuario  
	 */
	public static final String 	_EX_COT_TIENE_OTRO_USR		= "COT004";
	
	/**
	 * Excepción de cotización, producto asociado tiene precio = 0
	 */
	public static final String _EX_COT_PROD_PRECIO_CERO = "COT005";

	/**
	 * Excepción de cotización, producto despublicado
	 */
	public static final String _EX_COT_PROD_DESPUBLICADO = "COT006";

	/**
	 * Excepción de cotización, producto código de barras no existe
	 */
	public static final String _EX_COT_CODBARRA_NO_EXISTE = "COT007";
	
	/**
	 * Excepción de cotización, producto id no existe
	 */
	public static final String _EX_COT_PROD_ID_NO_EXISTE = "COT008";
	
	
	
	
	/**
	 * Tipo de cliente, indica que es una sucursal  
	 */
	public static String TIPO_CLIENTE_SUCURSAL			= "E";
	
	/**
	 * Tipo de cliente, indica que es una persona  
	 */
	public static String TIPO_CLIENTE_PERSONA			= "P";
	
	/**
	 * Indica que el estado es eliminado 
	 */
	public static String ESTADO_ELIMINADO			= "E";
	
	/**
	 * Tipo de estado para cotizacion
	 */
	public static String TIPO_ESTADO_COTIZACION		= "CT";
	
	/**
	 * Constante para el origen de pedidos web
	 */
	public static final String ORIGEN_WEB_CTE = "W";
	/**
	 * Constante para el origen de pedidos venta empresa
	 */
	public static final String ORIGEN_VE_CTE = "V";
    /**
     * Constante para el origen de pedidos Jumbo VA
     */
    public static final String ORIGEN_JV_CTE = "A";
    /**
     * Constante para el origen de pedidos de Casos
     */
    public static final String ORIGEN_CASOS_CTE = "C";
	
	/**
	 * Texto para el origen de pedidos web
	 */
	public static final String ORIGEN_WEB_TXT = "Jumbo.cl";
	
	/**
	 * Texto para el origen de pedidos venta empresa
	 */
	public static final String ORIGEN_VE_TXT = "V.Empresas";
    
    /**
     * Texto para el origen de pedidos Jumbo VA
     */
    public static final String ORIGEN_JV_TXT = "JumboVA";
    
    /**
     * Texto para el origen de pedidos de Casos
     */
    public static final String ORIGEN_CASOS_TXT = "Casos";
	
	/**
	 * Constante para el tipo de pedidos Spot
	 */
	public static final String TIPO_VE_SPECIAL_CTE = "S";
	
	/**
	 * Constante para el tipo de pedidos normal
	 */
	public static final String TIPO_VE_NORMAL_CTE = "N";
	
	/**
	 * Texto para el tipo de pedido SPOT de venta empresa
	 */
	public static final String TIPO_VE_SPECIAL_TXT = "Especial";
	
	/**
	 * Texto para el tipo de pedido normal de venta empresa
	 */
	public static final String TIPO_VE_NORMAL_TXT = "Normal";
		
	/**
	 * Constante para el tipo de despacho normal
	 */
	public static final String TIPO_DESPACHO_NORMAL_CTE = "N";

	/**
	 * Constante para el tipo de despacho express
	 */
	public static final String TIPO_DESPACHO_EXPRESS_CTE = "E";

	/**
	 * Constante para el tipo de despacho económico
	 */
	public static final String TIPO_DESPACHO_ECONOMICO_CTE = "C";
    
    /**
     * Constante para el tipo de despacho Retiro
     */
    public static final String TIPO_DESPACHO_RETIRO_CTE = "R";
    
    public static final String TIPO_DESPACHO_UMBRAL_CTE = "U";

	/**
	 * Texto para el tipo de despacho normal
	 */
	public static final String TIPO_DESPACHO_NORMAL_TXT = "Normal";

	/**
	 * Texto para el tipo de despacho express
	 */
	public static final String TIPO_DESPACHO_EXPRESS_TXT = "Express";

	/**
	 * Texto para el tipo de despacho económico
	 */
	public static final String TIPO_DESPACHO_ECONOMICO_TXT = "Económico";
    
    /**
     * Texto para el tipo de despacho Retiro
     */
    public static final String TIPO_DESPACHO_RETIRO_TXT = "Retiro en Local";
    
    public static final String TIPO_DESPACHO_UMBRAL_TXT = "Bajo Umbral";
	
	/**
	 * Tipo de comprador es Administrador
	 */
	public static final String TIPO_COMPR_ADM_COD 	= "A";
	
	/**
	 * Tipo de comprador es comprador 
	 */
	public static final String TIPO_COMPR_COD 		= "C";

	/**
	 * Texto del tipo de comprador administrados 
	 */
	public static final String TIPO_COMPR_ADM_TXT 	= "Administrador";
	
	/**
	 * Texto del tipo de comprador
	 */
	public static final String TIPO_COMPR_TXT 		= "Comprador";
	
	/**
	 * Cadena vacia
	 */
	public static final String CADENA_VACIA 		= "";
	/**
	 * Rut cliente no registrado 
	 */
	public static final String CLIENTE_INVITADO		= "123123";
	/**
	 * Mensaje de empresa, indica que Rut ingresado existe y no puede ser creado 
	 */
	public static final String MSJE_EMPR_RUT_EXISTE 	= " El RUT ingresado ya existe. No se puede crear la empresa con este RUT.";
	
	/**
	 * Mensaje de sucursal, indica que Rut ingresado existe y no puede ser creado 
	 */
	public static final String MSJE_SUCR_RUT_EXISTE 	= " El RUT ingresado ya existe. No se puede crear la sucursal con este RUT.";
	
	/**
	 * Mensaje de comprador, indica que Rut ingresado existe y no puede ser creado 
	 */
	public static final String MSJE_COMP_RUT_EXISTE 	= " El RUT ingresado ya existe. No se puede crear el comprador con este RUT.";
	
	/**
	 * Mensaje de empresa, indica que Rut ingresado existe y no puede ser creado 
	 */
	public static final String MSJE_EMPR_RUT_MOD_EXISTE 	= " El RUT modificado ya existe. No se puede actualizar la empresa con este RUT.";
	
	/**
	 * Mensaje de sucursal, indica que Rut ingresado existe y no puede ser creado 
	 */
	public static final String MSJE_SUCR_RUT_MOD_EXISTE 	= " El RUT modificado ya existe. No se puede actualizar la sucursal con este RUT.";
	
	/**
	 * Mensaje de comprador, indica que Rut ingresado existe y no puede ser creado 
	 */
	public static final String MSJE_COMP_RUT_MOD_EXISTE 	= " El RUT modificado ya existe. No se puede actualizar el comprador con este RUT.";
	
	//estados de la Cotizacion
	/**
	 * constante que indica el id del estado de una cotizacion cuando es ingresada por el comprador  
	 */
	public static final int ID_EST_COTIZACION_INGRESADA			= 55;
	
	/**
	 * constante que indica el id del estado de una cotizacion cuando esta en revision por ejecutivo venta empresa
	 */
	public static final int ID_EST_COTIZACION_EN_REVISION		= 56;
	
	/**
	 * constante que indica el id del estado de una cotizacion cuando requiere validación
	 */
	public static final int ID_EST_COTIZACION_EN_VALIDACION		= 57;
	
	/**
	 * constante que indica el id del estado de una cotizacion cuando es cotizada , para revisión por comprador
	 */
	public static final int ID_EST_COTIZACION_COTIZADA			= 58;
	
	/**
	 * constante que indica el id del estado de una cotizacion cuando es aceptada por el comprador
	 */
	public static final int ID_EST_COTIZACION_ACEPTADA			= 59;
	
	/**
	 * constante que indica el id del estado de una cotizacion cuando es anulada por el comprador
	 */
	public static final int ID_EST_COTIZACION_ANULADA			= 63;
	
	/**
	 * constante que indica el id del estado de una cotizacion cuando es caducada, expiro 
	 */
	public static final int ID_EST_COTIZACION_CADUCADA			= 62;
	
	/**
	 * constante que indica el id del estado de una cotizacion cuando esta en realizacion, comenzó a ser procesada en pedidos
	 */
	public static final int ID_EST_COTIZACION_EN_REALIZACION	= 60;
	
	/**
	 * constante que indica el id del estado de una cotizacion cuando es terminada, todos los pedidos de la cotización llegaron a su fin 
	 */
	public static final int ID_EST_COTIZACION_TERMINADA			= 61;
	
	/**
	 * constante que indica el id del estado de una cotizacion cuando se pasa a Gestion de Productos Fuera de Mix
	 */
	public static final int ID_EST_COTIZACION_GEST_FUERA_MIX			= 64;
	
	
	/**
	 * Indicador de afirmacion
	 */
	public static final String INDICADOR_SI						= "S";
	
	/**
	 * Indicador de negación
	 */
	public static final String INDICADOR_NO						= "N";
	
	/**
	 * Indica que acepta la politica de sustitucion
	 */
	public static final String POLITICA_SUSTITUCION_SI			= "SI";
	
	/**
	 * Indica que no acepta la politica de sustitucion
	 */
	public static final String POLITICA_SUSTITUCION_NO			= "NO";
	
	//Alertas de cotizacion
	/**
	 * Alerta : margen de cotizacion es menor al margen minimo de empresa
	 */
	public static final int COT_ALE_MARGEN_MINIMO				= 1;
	
	/**
	 * Alerta : descuento de cotizacion es mayor al descuento maximo de empresa
	 */
	public static final int COT_ALE_DSCTO_MAXIMO				= 2;
	
	/**
	 * Alerta : identifica si se modifico razon social de empresa
	 */
	public static final int COT_ALE_MOD_RAZ_SOCIAL				= 3;
	
	/**
	 * Alerta : identifica si la direccion de despacho es nueva
	 */
	public static final int COT_ALE_DIRECC_DESP_NUEVA			= 4;
	
	/**
	 * Alerta : si empresa esta bloqueada o no
	 */
	public static final int COT_ALE_EMPRESA_BLOQUEADA			= 5;

	/**
	 * Alerta : cotizacion sin fecha de vencimiento
	 */
	public static final int COT_ALE_SIN_FECHA_VENC				= 6;	

	/**
	 * Alerta : Existen productos en la cotizacion sin costo 
	 */
	public static final int COT_ALE_PROD_SIN_COSTO				= 7;	
	
	/**
	 * Alerta : Existen productos en la cotizacion sin costo 
	 */
	public static final int COT_ALE_AUT_MARGEN					= 8;	
	
	/**
	 * Alerta : Existen productos en la cotizacion sin costo 
	 */
	public static final int COT_ALE_AUT_DESCTO					= 9;	
	
	/**
	 * Alerta : Linea de credito insuficiente 
	 */
	public static final int COT_ALE_SIN_CUPO					= 10;	
	
	
	/**
	 * Numero de dias para actualizar los pedidos pendientes
	 * Los pedidos que esten en los ultimos X dias descontaran monto en el saldo 
	 * de la linea de credito.
	 */
	
	public static final int IF_LINEA_CREDITO_DIAS					= 2;
	
	/**
	 * Flag que identifica si la razon social de la empresa fue modificada
	 */
	public static final int EMP_MOD_RAZON_SOCIAL				= 1;
	
	/**
	 * Flag que identifica que la razon social de la empresa no fue modificada
	 */
	public static final int EMP_NO_MOD_RAZON_SOCIAL				= 0;
	
	/**
	 * Flag que identifica si la empresa esta bloqueada
	 */
	public static final String EMP_BLOQUEADO					= "B";
	
	/**
	 * Indica la cantidad maxima de productos en un  pedido de origen VE
	 */
	public static final double MAX_CANTIDAD_PRODUCTOS			= 100;
	
	/**
	 * Indica que el medio de pago es linea de credito 
	 */
	public static final String  MEDIO_PAGO_LINEA_CREDITO		= "CRE";
    
    /**
     * Descripcion del medio de pago es linea de credito 
     */
    public static final String  MEDIO_PAGO_LINEA_CREDITO_DESC        = "Línea de Crédito";
	
	/**
	 * Constante para el estado abierto del formulario de picking
	 */
	public static final String  FPICK_ESTADO_ABIERTO		= "A";
	/**
	 * Constante para el estado cerrado del formulario de picking
	 */
	public static final String  FPICK_ESTADO_CERRADO		= "C";
	/**
	 * Mensaje al log de rondas al iniciar formulario de pick
	 */
	public static final String  FPICK_INICIO_MSGLOG		= "Inicia ingreso manual de picking";
	
	/**
	 * Mensaje que indica que no se reverso picking
	 */
	public static final String  FPICK_REVERSA_PICK_MAL	= "No se pudo reversar picking.";
	
	/**
	 * Mensaje que indica que no se reverso sustitución
	 */
	public static final String  FPICK_REVERSA_SUST_MAL	= "No se pudo reversar sustitutos.";
	
	/**
	 * Mensaje que indica que no se relaciono el producto pedido con el producto pickeado
	 */
	public static final String  FPICK_RELACION_MAL	= "No se pudo realizar la relacion entre productos.";
	
	/**
	 * Rango inferior de margen minimo, venta empresas.
	 */
	public static final String VE_EMPRESAS_MARGEN_MIN_RANGO_INF = "15";
	
	/**
	 * Rango superior de margen minimo, venta empresas.
	 */
	public static final String VE_EMPRESAS_MARGEN_MIN_RANGO_SUP = "100";
	
	/**
	 * Rango inferior de descuento maximo a comprador, venta empresas.
	 */
	public static final String VE_EMPRESAS_DESCUENTO_MAX_RANGO_INF = "0";
	
	/**
	 * Rango superior de descuento maximo a comprador, venta empresas.
	 */
	public static final String VE_EMPRESAS_DESCUENTO_MAX_RANGO_SUP = "5";
	
	/**
	 * Producto no pesable
	 */
	public static final String PRODUCTO_NO_PESABLE = "N";
	
	/**
	 * Estado comprador cambio de clave
	 */
	public static final char ESTADO_COMPRADOR_CAMBIO_CLAVE = 'C';
	
	
	/**
	 * Formulario de Picking, Margen superior para pesables
	 */
	public static final double FORM_PICK_MARGEN_SUPERIOR_PESABLES = 0.2;
	
	/**
	 * Formulario de Picking, Margen inferior para pesables
	 */
	public static final double FORM_PICK_MARGEN_INFERIOR_PESABLES = 0.1;
	
	
	
	/**
	 * Locales tipo flujo
	 */
	public static final String LOCAL_TIPO_FLUJO_NORMAL_CTE  = "N";
	public static final String LOCAL_TIPO_FLUJO_NORMAL_TXT  = "Normal";
	public static final String LOCAL_TIPO_FLUJO_PARCIAL_CTE = "P";
	public static final String LOCAL_TIPO_FLUJO_PARCIAL_TXT = "Parcial";

	/**
	 * Locales Tipo Picking
	 */
	/*public static final String LOCAL_TIPO_PICKING_NORMAL_CTE = "N";
	public static final String LOCAL_TIPO_PICKING_NORMAL_TXT = "Normal";
	public static final String LOCAL_TIPO_PICKING_LIGHT_CTE  = "L";
	public static final String LOCAL_TIPO_PICKING_LIGHT_TXT  = "Light";*/

	public static final String TIPO_PICKING_NORMAL_CTE = "N";
	public static final String TIPO_PICKING_NORMAL_TXT = "Normal";
	public static final String TIPO_PICKING_LIGHT_CTE  = "L";
	public static final String TIPO_PICKING_LIGHT_TXT  = "Light";

	/* -------------------- PROMOCIONES -------------------*/

	public static final int CANAL_PROMOCIONES					= 1;
	public static final char PROMO_RECALCULO_FLAG_PROD_PESABLE  = 'P';
	public static final char PROMO_RECALCULO_FLAG_PROD_CANTIDAD = 'C';
	public static final char PROMO_RECALCULO_FLAG_VENTA         = 'V';
	
	public static final String  PROMO_PERMITE_SUSTITUTO    = "S";
	public static final String  PROMO_NO_PERMITE_SUSTITUTO = "N";
	public static final String  PROMO_PERMITE_FALTANTES    = "S";
	public static final String  PROMO_NO_PERMITE_FALTANTES = "N";
	
	
	/* ------------------- Consultas SOCKET Promociones ------------*/
	
	/**
	 * Dirección del servidor de promociones (cupones)
	 */
	//public static final String PROMO_SERVER_HOST = "localhost";
	//public static final String PROMO_SERVER_HOST = "128.1.85.131";
	public static final String PROMO_SERVER_HOST = "192.168.50.23"; // Producción
	//public static final String PROMO_SERVER_HOST = "128.1.85.131"; // Desarrollo
	/**
	 * Dirección del servidor de promociones (cupones)
	 */
	//public static final String PROMO_SERVER_HOST = "localhost";
	//public static final String PROMO_SERVER_HOST = "128.1.85.131";
	public static final String PROMO_SERVER_HOST_FB = "192.168.50.21"; //Producción	
	//public static final String PROMO_SERVER_HOST_FB = "128.1.85.131"; //Desarrollo
	
	/**
	 * Puerto para las consultas de tcp y cupones
	 */
	//public static final int PROMO_SERVER_TCP_PORT = 80;
	//public static final int PROMO_SERVER_TCP_PORT = 5500;
	public static final int PROMO_SERVER_TCP_PORT = 4600; //Producción
	//public static final int PROMO_SERVER_TCP_PORT = 5500; //Desarrollo
	
	/**
	 * puerto para el envio de feedback de cargas
	 */
	//public static final int PROMO_SERVER_FB_PORT = 100;
	//public static final int PROMO_SERVER_FB_PORT = 1137;
	public static final int PROMO_SERVER_FB_PORT = 1127; //Producción
	//public static final int PROMO_SERVER_FB_PORT = 1137; //Desarrollo
	
	/**
	 * Datos para el encabezado de consultas TCP y Cupones
	 */
	public static final String TCP_TIPO_HEADER_CANAL = "3";
	public static final String TCP_TIPO_HEADER_CADENA = "4";
	public static final String TCP_TIPO_CONS_R1 = "R1";
	public static final String TCP_TIPO_CONS_C1 = "C1";
	public static final String TCP_TIPO_CONS_R2 = "R2";
	public static final int TCP_TIPO_CONS_R1_CTE_N = 13;
	public static final String TCP_TIPO_CONS_R1_CTE_TIPO_CLIENTE = "00";
	public static final String TCP_R2_CUPON_GENERADO = "0";
	public static final String TCP_R2_CUPON_CANJEADO = "1";
	/**
	 * Estados de la tabla de envio de quemas de cupones (SAF)
	 */
	public static final String TCP_SAF_ESTADO_NO_ENVIADO = "N";
	public static final String TCP_SAF_ESTADO_PENDIENTE  = "P";
	public static final String TCP_SAF_ESTADO_ENVIADO    = "E";
	
	public static final int LARGO_TOTAL_CONS_CUPONES = 2098;

	/**
	 * Variables para la carga de promociones
	 */
	//public static final String CARGAS_PROMO_PATH= "D:/Proyectos/Promociones/Clientes TCPIP/feedback/PD/";
	//public static final String CARGAS_PROMO_PATH= "/u02/cargaftp/";
	public static final String CARGAS_PROMO_PREFIJO_ARCH  = "PD";
	public static final String CARGAS_PROMO_EXT_ARCH      = ".DAT";
	public static final String CARGAS_PROMO_EXT_ARCH_PROC = ".PROC";
	
	//public static final String  CARGAS_PROMO_PATH_FB= "D:/Proyectos/Promociones/Clientes TCPIP/feedback/FB/";
	//public static final String  CARGAS_PROMO_PATH_FB= "/u02/cargaftp/feedback/";
	public static final String CARGAS_PROMO_PREFIJO_ARCH_FB = "FB";
	
	/**
	 * Temout para envio de feedback en milisegundos (1000=1seg)
	 */
	public static final int CARGAS_PROMO_ARCH_FB_TIMEOUT_MS = 5000;
	
	public static final String  PROMO_TIPO_NORMAL    = "NORMAL";
	public static final String  PROMO_TIPO_EVENTO    = "EVENTO";
	public static final String  PROMO_TIPO_PERIODICA = "PERIODICA";
	
	/**
	 * Permite traspasar la observacion de sustitución del cliente y no traspasa
	 * los criterios fijos para no recargar la PDA.
	 * Indica el ID de la tabla sustitutos criterios
	 */
	public static final int POLITICA_SUSTITUCION_OTRO_ID = 4;
	
	/**
	 * Permite discriminar el modo de autenticacion de la carga de rondas
	 * utilizada en el WS_BOL
	 */
	public static final int PDA_MODO_AUTENTICACION_BD = 1;
	public static final int PDA_MODO_AUTENTICACION_LDAP = 2;
	
	
	public static final String AUDITADO_SI = "S";
	public static final String AUDITADO_NO = "N";
	
	/* SECTOR DE PICKING DE PRODUCTOS ASOCIADOS AL PICKING LIGHT*/
	//public static final int SECTOR_TIPO_PICKING_LIGHT_CTE = 101;
	public static final String SECTOR_TIPO_PICKING_LIGHT_TXT = "Pedido Completo";

	public static final int NUM_POLIGONO_RETIRO_LOCAL = 999;
        
    public static final String CUMPLIMIENTO_CTE_EN_TIEMPO  = "T";
    public static final String CUMPLIMIENTO_CTE_RETRASADO  = "R";
    public static final String CUMPLIMIENTO_CTE_ADELANTADO  = "A";
    
    public static final String CUMPLIMIENTO_DESC_EN_TIEMPO  = "A tiempo";
    public static final String CUMPLIMIENTO_DESC_RETRASADO  = "Retrasado";
    public static final String CUMPLIMIENTO_DESC_ADELANTADO  = "Adelantado";
    
    public static final int ESTADO_RUTA_EN_PREPARACION = 65;
    public static final int ESTADO_RUTA_EN_RUTA = 66;
    public static final int ESTADO_RUTA_FINALIZADA = 67;
    public static final int ESTADO_RUTA_CON_REPROGRAMACION = 68;
    public static final int ESTADO_RUTA_ANULADA = 69;
    
    public static final int LIBERA_OP_ORIGEN_FORM_PEDIDO = 1;
    public static final int LIBERA_OP_ORIGEN_MONITOR_PEDIDO = 2;
    public static final int LIBERA_OP_ORIGEN_DET_RUTA = 3;
    public static final int LIBERA_OP_ORIGEN_OP_PENDIENTES = 4;
    
    public static final int REPROGRAMACION_ORIGEN_MONITOR_DESPACHO = 1;

	public static final String COD_COMERCIO 		= "1234";

	/**
	 * Constantes para boton de pago
	 * */
	public static final String BTN_PAGO_COD_TRANSACCION	= "FO02";
    public static final String BTN_PAGO_COD_DEVOLUCION_RESERVA = "FF12";
	public static final String BTN_PAGO_APROBADO		= "A";
	public static final String BTN_PAGO_RECHAZADO		= "R";
    
    public static final int DIAS_RESERVADOS_TBK = 10;
    public static final int DIAS_RESERVADOS_CAT = 7;
    
    public static final String ID_PEDIDOS_PREINGRESADOS = "1,68,69";
  //Inicio AJARA 
  	/**
  	 * grupo de estados del pedido en picking pre ingresado, excluye anulado
  	 */
  	public static final String ID_PEDIDOS_PREINGRESADOS_SIN_ANULADO= "1,68";
  	//Fin AJARA
    
    
    /**
     * Comentario para <code>UMBRAL_PRECIO_SUSTITUTO</code>
     * Constante utilizada para calcular el umbral de precio sustituto
     * solicitado para mantener el precio de los productos originales en caso de ser sustituidos en el proceso de picking
     */
    public static final double UMBRAL_PRECIO_SUSTITUTO = 1.0;
    
    /**
     * Constante utilizada para MSG de respuestas al suscribirse en el footer de jumbo.cl
     */
    public static final String MSG_SUSCRIPCION_EXITO = "Gracias por suscribirte!.\rTu E-Mail ha sido ingresado correctamente para recibir ofertas.";//\rPronto tendrá noticias de nuestros ejecutivos.";
    public static final String MSG_ERROR_GENERAL = "Disculpe las molestias!\rPor favor intente más tarde.\rGracias por su comprensión.";
    public static final String MSG_SUSCRIPCION_DUPLICADO = "El E-Mail ingresado ya existe en nuestros registros.\rGracias.";
	
    public static final String TEXT_DESCTO_DESPACHO = "Servicio a $1";
	public static final String NOMBRE_DESCTO = "{nombre_descuento}";
    public static final String TEXT_CUPON_DESCUENTO = "Cupón";
    public static final String TEXT_PROMOCION = "Promoción";
    public static final String TEXT_COLABORADOR = "Colaborador";
    public static final String DETALLE_DESCTO = "{detalle_descuento}";
    public static final String MONTO_DESCTO = "{monto_descuento}";
    public static final String SYSTEM = "SYSTEM";
    public static final String TEXT_DESCTO_APLICADO = "Cupón de descuento código ";
    public static final String TEXT_COSTO_DESPSCHO = " (Solo costo de despacho $1) ";
    public static final String DESCTO = "DESCUENTOS";
    
    /**
     * Prefijo separador para la ficha tecnica     
     */
    public static final String MJS_TIENE_FICHA_TECNICA = "La ficha técnica ha sido habilitada.";
    public static final String MJS_NO_FICHA_TECNICA = "La ficha técnica ha sido deshabilitada.";
    public static final String MJS_UPDATE_FICHA_TECNICA = "La información de la ficha técnica ha sido actualizada.";    
    public static final String MJS_ERROR_FICHA_TECNICA = "Se ha producido un error al actualizar la información de la ficha técnica.";
    public static final String MJS_ERROR_ESTADO_FICHA_TECNICA = "No ha sido posible actualizar el estado de la ficha técnica.";
}