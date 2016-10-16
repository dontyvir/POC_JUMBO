package cl.bbr.common.framework;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class TaskError {

	public static final String ERR_INVALID_PARAM	= "001";
	public static final String ERR_INVALID_HTML		= "002";
	public static final String ERR_EXCEPTION		= "003";
		
	public static void ErrorTask(String task, HttpServletRequest req, HttpServletResponse res, Logger logger) throws IOException{

			// Se recupera la salida para el servlet
			PrintWriter out = res.getWriter();
			res.setContentType("text/html");
			
			//PrintWriter out = res.getWriter();
			out.println("<html><head><title>Hola!!!</title></head><body>Tarea de Error: <p>Mensage: <b>" + task + "</b></body></html>");
			out.close();	
	
			logger.error(task);
	
	}	
	
	
}
