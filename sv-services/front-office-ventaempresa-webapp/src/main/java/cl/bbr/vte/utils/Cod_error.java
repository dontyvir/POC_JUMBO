package cl.bbr.vte.utils;

/**
 * Códigos de error de la aplicación
 * 
 * @author BBR e-commerce & retail
 *
 */
public class Cod_error {

	/**
	 * Cliente no tiene permisos para el comando
	 */
	public static final String GEN_ERR_PERMISOS 	= "0001";
	/**
	 * No graba el registro
	 */
	public static final String GEN_SQL_NO_SAVE 		= "0011";
	/**
	 * Faltan parámetros mínimos
	 */
	public static final String GEN_FALTAN_PARA 		= "0021";
	/**
	 * Indica que hay cambio de clave para ir a su respectivo comando
	 */
	public static final String GEN_CAM_CLAVE 		= "0031";
	/**
	 * Cliente no existe
	 */
	public static final String CLI_NO_EXISTE 		= "0102";
	/**
	 * Cliente existe y su clave no es válida
	 */
	public static final String CLI_CLAVE_INVALIDA	= "0103";
	/**
	 * @deprecated
	 */
	public static final String CLI_CAMBIO_CLAVE		= "0104";
	/**
	 * @deprecated
	 */
	public static final String TRA_NO_GRABA 		= "0201";
	/**
	 * Faltan parámetros mínimos
	 */
	public static final String REG_FALTAN_PARA		= "0301";
	/**
	 * Error en comando OrderProcess, No graba pedido
	 */
	public static final String CHECKOUT_ERROR		= "0401";
	/**
	 * Error en comando OrderProcess, No graba pedido (No utilizado)
	 */	
	public static final String CHECKOUT_PICK		= "0402";
	/**
	 * Error en comando OrderDisplay, No existe pedido
	 */
	public static final String CHECKOUT_NO_PEDIDO	= "0403";
	/**
	 * Error en comando Order Process y OrderDisplay, No existen productos para el pedido
	 */	
	public static final String CHECKOUT_SIN_PROD	= "0404";

	/**
	 * Constructor
	 */
	public Cod_error() {
	}
	
}
