package cl.bbr.fo.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Valida Imagen Gif para Captcha
 * 
 * @author imoyano
 *  
 */

public class ValidaGifCaptcha extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception  {
        String respuestaAjax = "OK";

        String captcha = arg0.getParameter("gif_captcha").toString();
        
        HttpSession session = arg0.getSession();
        
        if ( session.getAttribute("captcha_mail") != null ) {
            
            if ( !session.getAttribute("captcha_mail").toString().equals(captcha) ) {
                respuestaAjax = "Ingrese nuevamente el código de seguridad.";
            }
            
        } else {
            respuestaAjax = "Por favor, intente nuevamente.";
        }
        
        arg1.setContentType("text/xml");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("UTF-8");

        arg1.getWriter().write("<datos_captcha>");
        arg1.getWriter().write("<mensaje>" + respuestaAjax + "</mensaje>");
        arg1.getWriter().write("</datos_captcha>");
    }
}