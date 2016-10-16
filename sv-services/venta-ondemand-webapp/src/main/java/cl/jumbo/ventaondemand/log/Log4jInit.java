package cl.jumbo.ventaondemand.log;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.PropertyConfigurator;

public class Log4jInit extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void init() {
		try {
			String prefix = getServletContext().getRealPath("/");
			String file = getInitParameter("log4j-init-file");
			// 	if the log4j-init-file is not set, then no point in trying
			if(file != null) {
				PropertyConfigurator.configure(prefix+file);
			}	
		}catch(Exception e){
			System.out.println("Error load 'Log4jInit',  " + e);
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
	}
}
