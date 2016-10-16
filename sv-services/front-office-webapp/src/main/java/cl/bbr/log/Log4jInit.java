package cl.bbr.log;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

/**
 * Clase que permite la generción de log en capas de front (BizDelegate, Comandos). 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class Log4jInit extends HttpServlet {

	/* (sin Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() {
		String prefix = getServletContext().getRealPath("/");
		String file = getInitParameter("log4j-init-file");
		// if the log4j-init-file is not set, then no point in trying
		if (file != null) {
			PropertyConfigurator.configure(prefix + file);
		}
	}

	/* (sin Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		
		if ("TRUE".equals(req.getParameter("RL4J"))) {
			
			Log4JUtils.initializeLog4J();
			log(res, "/FO/WEB-INF/classes/log4j_app.properties inicializado ");
			this.init(); // for log4j
			log(res, "/FO/WEB-INF/classes/log4j.properties inicializado ");
		
		}
		
	}
	
	private void log(HttpServletResponse res, String message) {
		
		try {
			
			res.getWriter().write(message+"\n");
			res.getWriter().flush();
		
		} catch (IOException ioe) {
			
		}
		
	}
}
