package cl.jumbo.ventaondemand.web.command;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public abstract class Command extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected JSONObject objOut = null; 
	protected JSONObject objHeader = null;
	protected JSONObject objResponse = null;
	protected DateFormat dateFormatYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
	
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

}
