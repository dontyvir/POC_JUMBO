/*
 * Creado el 21-08-2007
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.jumbocl.casos.utils;


/**
 * En esta clase se definen:
 *  - Los estados en que puede estar un caso.
 *  - Los estados que debe mostrar la lista de selección (combobox) de estados, dependiendo del estado actual del caso.
 *  - Los permisos que tienen los perfiles para cambiar estados.
 * 
 * @author imoyano
 *
 * Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class CasosEstadosUtil {

    public static final int INGRESADO 		= 1;
    public static final int EN_GESTION 		= 2;
    public static final int PROP_SOLUCION 	= 3;
    public static final int DESPACHO 		= 4;
    public static final int CUADRATURA 		= 5;
    public static final int CERRADO 		= 6;
    public static final int VERIFICADO 		= 7;
    public static final int ANULADO 		= 8;
    public static final int PRE_INGRESADO   = 9;
    
    private static String[] comboBoc = {"9-1","1","2","3-2","5","6-7"};
    
    private static String[] comboBol = {"1-2","2-3-4","4-5","5-2-6"};
    
    public CasosEstadosUtil() {
    }
    
    public static boolean mostrarEstado(long estadoCaso, long estadoMostrar, String app) {
        String[] params = {};
        if (app.equalsIgnoreCase("BOC")) {
            params = comboBoc;
        } else {
            params = comboBol;
        }        
        for (int i=0; params.length > i; i++) {
            long estado = Long.parseLong(params[i].split("-")[0]);            
            if (estado == estadoCaso) {
                String[] lista = params[i].split("-");
                for (int j=0; lista.length > j; j++) {
                    if (estadoMostrar == Long.parseLong(lista[j])) {
                        return true;
                    }
                }
            }            
        }        
        return false;
    }
    
}
