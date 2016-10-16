/*
 * Creado el 21-08-2007
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.jumbocl.casos.utils;

/**
 * @author imoyano
 *
 * Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class CasosConstants {

    public static final String ESTADO_VENCIDO_COLOR 	= "#FF0000";
    public static final String ESTADO_POR_VENCER_COLOR 	= "#FF9933";
    public static final String ESTADO_NORMAL_COLOR 		= "#339900";
    public static final String ESTADO_FINALIZADO_COLOR 	= "#000000";
    
    public static final int DIAS_RESOLUCION_CASO = 0;    
    
    public static final String MSJ_ADD_CASO_EXITO = "Los datos del caso ingresado han sido guardados satisfactoriamente.";
    public static final String MSJ_ADD_CASO_ERROR = "Existió un error al ingresar el nuevo caso.";
    
    public static final String MSJ_ADD_PRODUCTO_EXITO = "OK"; //No cambiar, ya que el string 'OK' de respuesta es parseado con Ajax en la pagina
    public static final String MSJ_ADD_PRODUCTO_ERROR = "El producto no pudo ser guardado.";
    
    public static final String MSJ_DEL_PRODUCTO_EXITO = "OK"; //No cambiar, ya que el string 'OK' de respuesta es parseado con Ajax en la pagina
    public static final String MSJ_DEL_PRODUCTO_ERROR = "El producto no pudo ser eliminado.";
    
    public static final String MSJ_ANULAR_CASO_EXITO = "El caso fue Anulado exitósamente";
    public static final String MSJ_ANULAR_CASO_ERROR = "El caso no pudo ser anulado.";
    
    public static final String MSJ_MOD_CASO_EXITO = "El caso fue guardado exitósamente";
    public static final String MSJ_MOD_CASO_ERROR = "El caso no pudo ser guardado.";
    public static final String MSJ_MOD_CASO_EN_EDICION = "El caso está siendo editado por el usuario: ";
    public static final String MSJ_MOD_CASO_USUARIO = "No puede realizar esta acción, ya que usted está editando el caso: ";
    public static final String MSJ_MOD_CASO_FINALIZADO = "Acción no permitida, debido al estado del caso.";
    public static final String MSJ_MOD_CALL_CENTER = "Acción no permitida.";
    
    public static final String MSJ_LIB_CASO_EXITO = "El caso fue liberado con exito.";
    public static final String MSJ_LIB_CASO_ERROR = "El caso no puede ser liberado.";
    
    public static final String LOG_ADD_CASO = "Creación del caso";
    public static final String LOG_ANULA_CASO = "Caso anulado: ";
    public static final String LOG_MOD_INI_CASO = "Inicio de edición del caso. (Caso tomado)";
    public static final String LOG_MOD_FIN_CASO = "Término de edición del caso. ";
    public static final String LOG_MOD_IND_CASO = "Se modificó la indicación: ";
    public static final String LOG_ADD_IND_CASO = "Se agregó la indicación: ";
    public static final String LOG_LIB_CASO = "Liberación del caso";
    
    public static final String LOG_ADD_PRD_CASO = "Se agrega un producto. ";
    public static final String LOG_MOD_PRD_CASO = "Se modifica un producto. ";
    public static final String LOG_DEL_PRD_CASO = "Se elimina un producto. ";
    
    public static final String LOG_ESCALAMIENTO_CASO = "Se deja el caso en escalamiento. ";
    public static final String LOG_DESESCALAMIENTO_CASO = "Se quita el estado de escalamiento del caso. ";
    
    public static final String MSJ_PERMISO_PERFIL_ESTADO = "Usted no cuenta con los privilegios para modificar el estado del caso";
    
    public static final String MSJ_ADD_QUIEBRE_EXITO = "OK"; //No cambiar, ya que el string 'OK' de respuesta es parseado con Ajax en la pagina
    public static final String MSJ_ADD_QUIEBRE_ERROR = "El tipo de quiebre no pudo ser guardado.";
    
    public static final String MSJ_ADD_RESPONSABLE_EXITO = "OK"; //No cambiar, ya que el string 'OK' de respuesta es parseado con Ajax en la pagina
    public static final String MSJ_ADD_RESPONSABLE_ERROR = "El responsable no pudo ser guardado.";
    
    public static final String MSJ_ADD_MOTIVO_EXITO = "OK"; //No cambiar, ya que el string 'OK' de respuesta es parseado con Ajax en la pagina
    public static final String MSJ_ADD_MOTIVO_ERROR = "El motivo no pudo ser guardado.";
    
    public static final String MSJ_ADD_JORNADA_EXITO = "OK"; //No cambiar, ya que el string 'OK' de respuesta es parseado con Ajax en la pagina
    public static final String MSJ_ADD_JORNADA_ERROR = "La jornada no pudo ser guardada.";
    
    public static final String MSJ_DEL_QUIEBRE_EXITO = "OK"; //No cambiar, ya que el string 'OK' de respuesta es parseado con Ajax en la pagina
    public static final String MSJ_DEL_QUIEBRE_ERROR = "El tipo de quiebre no pudo ser eliminado.";
    
    public static final String MSJ_DEL_RESPONSABLE_EXITO = "OK"; //No cambiar, ya que el string 'OK' de respuesta es parseado con Ajax en la pagina
    public static final String MSJ_DEL_RESPONSABLE_ERROR = "El responsable no pudo ser eliminado.";
    
    public static final String MSJ_DEL_MOTIVO_EXITO = "OK"; //No cambiar, ya que el string 'OK' de respuesta es parseado con Ajax en la pagina
    public static final String MSJ_DEL_MOTIVO_ERROR = "El motivo no pudo ser eliminado.";
    
    public static final String MSJ_DEL_JORNADA_EXITO = "OK"; //No cambiar, ya que el string 'OK' de respuesta es parseado con Ajax en la pagina
    public static final String MSJ_DEL_JORNADA_ERROR = "La Jornada no pudo ser eliminada.";
    
    public static final String MSJ_CASOS_OP = "Existe un caso asociado a esta OP. El Id del caso es: @casos. \n\nPara evitar duplicidad de casos, por favor revise el caso indicado. ";
    public static final String MSJ_CASOS_OPS = "Existen casos asociados a esta OP. Los Id's de los casos son: @casos. \n\nPara evitar duplicidad de casos, por favor revise los casos indicados.";
    
    public static final String MSJ_MOD_ENVIO = "Se modifico el producto a tipo 'Envío de Dinero'.";
    
}
