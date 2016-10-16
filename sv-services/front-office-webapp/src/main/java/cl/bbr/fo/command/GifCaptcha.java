package cl.bbr.fo.command;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.util.GIFEncoder;
import cl.bbr.log_app.Logging;


/**
 * Imagen Gif para Captcha
 * 
 * @author imoyano
 *  
 */

public class GifCaptcha extends HttpServlet {
	
	static Logging logger = new Logging(GifCaptcha.class);

    /**
     * Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }

    /**
     * Destroys the servlet.
     */
    public void destroy() {
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws AWTException
     */
    protected void execute(HttpServletRequest req,
            HttpServletResponse res) throws ServletException,
            java.io.IOException, AWTException {

     
        ServletOutputStream bufferSalida = res.getOutputStream();
        Graphics contextoGrafico = null;      
        
        try {
            int w = 145;
            int h = 25;

            
            Image offscreen = new BufferedImage(w,h, BufferedImage.TYPE_INT_BGR);
            contextoGrafico = offscreen.getGraphics();
            
            contextoGrafico.setColor( Color.WHITE );
            contextoGrafico.fillRect(0,0,w,h);
            
            contextoGrafico.setColor( Color.BLACK );
            
            contextoGrafico.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 20));
            
            contextoGrafico.draw3DRect(0, 0, w - 1, h - 1, true);
            contextoGrafico.drawArc(4, 10, w - 10, h - 10, 0, 190);
            contextoGrafico.drawString(randomText(req), 5, 20);

            // Encode the off screen graphics into a GIF and send it to the client
            res.setContentType("image/gif");
            GIFEncoder encoder = new GIFEncoder(offscreen);
            encoder.Write(bufferSalida);
        } catch (Exception e) {
            logger.error("ERROR:"+e.getMessage());
            e.printStackTrace();
        } finally {
            if (contextoGrafico != null)
                contextoGrafico.dispose();
        }
    }

    /**
     * @return
     */
    private String randomText(HttpServletRequest req) {
        int number = (int) (Math.random() * 2000);
        String[] quotes = {"B","C","D","F","G","H","J","K","L","M","N","P","Q","R","S","T","V","W","X","Y","Z"};
        String texto = "";
        for ( int i = 0; i < 4; i++ ) {
            texto += quotes[ (int) Math.floor( Math.random() * quotes.length ) ];
        }
        
        String var = texto + number;
        HttpSession session = req.getSession();
        
        if ( session.getAttribute("captcha_mail") != null ) {
            session.removeAttribute("captcha_mail");
        }
        session.setAttribute("captcha_mail",var);
        
        return var;
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            java.io.IOException {
        try {
            execute(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            java.io.IOException {
        try {
            execute(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }

}