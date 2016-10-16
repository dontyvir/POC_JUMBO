package cl.jumbo.ventamasiva.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Command extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		try {
			execute(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);		
	}
	
	/**
	 * Proceso que ejecuta el proceso, se implementa en las clases que heredan
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected abstract void execute(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public boolean isNumeric( String s ){
		try{
			double y = Double.parseDouble( s );
			return true;
		}catch( NumberFormatException err ){
			return false;
		}
	}
}
