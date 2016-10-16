package cl.bbr.vte.log;

import org.apache.log4j.PropertyConfigurator;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Clase que permite la generción de log en capas de front (BizDelegate, Comandos). 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class Log4j_Funcional_Init extends HttpServlet {

  /* (sin Javadoc)
 * @see javax.servlet.GenericServlet#init()
 */
public void init() {
    String prefix =  getServletContext().getRealPath("/");
    String file = getInitParameter("log4j-init-file");
    // if the log4j-init-file is not set, then no point in trying
    if(file != null) {
      PropertyConfigurator.configure(prefix+file);
    }
  }

 /* (sin Javadoc)
 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
 */
  public void doGet(HttpServletRequest req, HttpServletResponse res) {
  }
}

 
