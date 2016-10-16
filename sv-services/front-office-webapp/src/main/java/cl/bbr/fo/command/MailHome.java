package cl.bbr.fo.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.fo.bizdelegate.BizDelegate;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.DuplicateKeyException;

/**
 * Comando que permite agregar correos desde el home del sitio.
 * 
 * @author imoyano
 *  
 */
public class MailHome extends Command {

    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {

        //variable guarda error
        //String mensajeSistema = "Gracias por suscribirte!.\rTu E-Mail ha sido ingresado correctamente para recibir ofertas.\rPronto tendrá noticias de nuestros ejecutivos.";
    	 String mensajeSistema =Constantes.MSG_SUSCRIPCION_EXITO;
        try {
            String email = arg0.getParameter("email").toString();
            // Instacia del bizdelegate
            BizDelegate biz = new BizDelegate();
            biz.addMailHome(email);
        
        } catch (DuplicateKeyException e) {
            //mensajeSistema = "El E-Mail ingresado ya existe en nuestros registros.\rGracias.";
        	mensajeSistema = Constantes.MSG_SUSCRIPCION_DUPLICADO;
            e.printStackTrace();
        } catch (Exception e) {
            //mensajeSistema = "Disculpe las molestias!\rPor favor intente más tarde.\rGracias por su comprensión.";
        	mensajeSistema = Constantes.MSG_ERROR_GENERAL;
            e.printStackTrace();
        }

        arg1.setContentType("text/xml");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("UTF-8");

        //vemos cual es el mensaje a desplegar
        arg1.getWriter().write("<mail>");
        arg1.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
        arg1.getWriter().write("</mail>");
    }
}