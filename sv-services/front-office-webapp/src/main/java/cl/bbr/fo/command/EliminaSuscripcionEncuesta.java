package cl.bbr.fo.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.fo.bizdelegate.BizDelegate;

/**
 * Elimina la Suscripcion a Encuesta de satisfaccion de un cliente
 *  
 * @author imoyano
 *  
 */
public class EliminaSuscripcionEncuesta extends Command {

    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
        String param = arg0.getParameter("param");
        if ( param != null ) {        
            BizDelegate biz = new BizDelegate();
            biz.eliminaSuscripcionEncuestaByRut( Long.parseLong( strToRut(param) ) );            
            arg1.sendRedirect( "/supermercado/encuestas/eliminaSuscripcion.html" );
        }
    }

    private static String strToRut(String param) {
        String rut = "";
        char[] numeros = param.toCharArray();
        for (int i=numeros.length-1; i >= 0; i--) {
            rut += numeros[i];
            i--;
        }
        return rut;
    }
}