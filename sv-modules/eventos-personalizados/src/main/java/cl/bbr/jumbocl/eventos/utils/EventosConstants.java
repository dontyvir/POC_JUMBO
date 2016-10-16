/*
 * Creado el 21-08-2007
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c�digo - Plantillas de c�digo
 */
package cl.bbr.jumbocl.eventos.utils;

/**
 * @author imoyano
 *
 * Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c�digo - Plantillas de c�digo
 */
public class EventosConstants {
    
    public static final int PASO_BIENVENIDA	= 1;
    public static final int PASO_1 			= 2;
    public static final int PASO_2 			= 3;
    public static final int PASO_3 			= 4;
    public static final int PASO_RESUMEN 	= 5;
    
    public static final String MSJ_ADD_EVENTO_ERROR = "El evento no pudo ser creado.";    
    public static final String MSJ_ADD_EVENTO_EN_EDICION = "Usted no puede crear un nuevo evento, ya que est� editando uno.";
    public static final String MSJ_ADD_EVENTO_EXISTE = "Ya existe un evento con el mismo nombre.";
    public static final String MSJ_ADD_EVENTO_EXITO = "El evento fue creado exit�samente.";
    
    public static final String MSJ_MOD_EVENTO_EN_EDICION = "El evento est� siendo editado por el usuario: ";
    public static final String MSJ_MOD_EVENTO_ERROR = "El evento no pudo ser modificado.";
    public static final String MSJ_MOD_EVENTO_EXITO = "El evento fue modificado exit�samente.";
    
    public static final String MSJ_LIB_EVENTO_EXITO = "El evento fue liberado con exito.";
    public static final String MSJ_LIB_EVENTO_ERROR = "El evento no puede ser liberado.";
    
    public static final String MSJ_DEL_EVENTO_ERROR = "El evento no pudo ser eliminado.";
    public static final String MSJ_DEL_EVENTO_EXITO = "El evento fue eliminado exit�samente.";
    public static final String MSJ_DEL_EVENTO_ERROR_TOMADO = "El evento no puede ser eliminado, ya que est� siendo editado por otro usuario.";
    public static final String MSJ_DEL_EVENTO_ERROR_UTILIZADO = "El evento no puede ser eliminado, ya que este fue utilizado por clientes.";
    
    public static final String MSJ_RUT_EVENTO_ERROR_TOMADO = "No se pueden asignar Rut's al evento, ya q est� tomado.";
    public static final String MSJ_RUT_EVENTO_ERROR = "No se pueden asignar Rut's al evento.";
    public static final String MSJ_RUT_EVENTO_SIN_ARCHIVO = "No se pueden asignar Rut's al evento, ya que no se especific� el archivo.";
    public static final String MSJ_RUT_EVENTO_ARCHIVO_EXTENSION = "No se pueden asignar Rut's al evento, ya que el archivo seleccionado no es v�lido.";
    
}
