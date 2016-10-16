package cl.cencosud.jumbo.view;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.util.ResponseUtils;

import net.sf.json.JSONObject;


/**
 * Servlet implementation class Error
 */
public class Error extends HttpServlet {

	private static final long serialVersionUID = 7533072304178708285L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub		
		doError(request, response, "doGet");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doError(request, response, "doPost");
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doError(request, response, "doPut");
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doError(request, response, "doDelete");
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	private void doError(HttpServletRequest request, HttpServletResponse response, String m){
		// TODO Auto-generated method stub	
		//to-do::Agregar un filter para interceptar el status http y redirigir el flujo a este servlet.
		if(!StringUtils.isEmpty(request.getParameter("http_error"))){
			int codigo = Integer.parseInt(request.getParameter("http_error"));
			GrabilityException e = new GrabilityException(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
			switch(codigo){
				case 400:
					request.setAttribute("status", request.getParameter("http_error"));
					request.setAttribute("error_message", Constant.ERROR_HTTP+" : "+request.getParameter("http_error"));
					e = new GrabilityException(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
				break;
				case 401:
					request.setAttribute("status", request.getParameter("http_error"));
					request.setAttribute("error_message", Constant.ERROR_HTTP+" : "+request.getParameter("http_error"));
					e = new GrabilityException(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
				break;
				case 403:
					request.setAttribute("status", request.getParameter("http_error"));
					request.setAttribute("error_message", Constant.ERROR_HTTP+" : "+request.getParameter("http_error"));
					e = new GrabilityException(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
				break;
				case 404:
					request.setAttribute("status", request.getParameter("http_error"));
					request.setAttribute("error_message", Constant.ERROR_HTTP+" : "+request.getParameter("http_error"));
					e = new GrabilityException(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
				break;
				case 500:
					request.setAttribute("status", request.getParameter("http_error"));
					request.setAttribute("error_message", Constant.ERROR_HTTP+" : "+request.getParameter("http_error"));
					e = new GrabilityException(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
				break;
				case 503:
					request.setAttribute("status", request.getParameter("http_error"));
					request.setAttribute("error_message", Constant.ERROR_HTTP+" : "+request.getParameter("http_error"));
					e = new GrabilityException(Constant.SC_BAD_REQUEST, Constant.MSG_BAD_REQUEST);
				break;				
				 default:
					 System.out.println("The students grade is unknown.");
				 break;				
			}
			ResponseUtils.printError(request, response, e);
			
	     }else{
	    	request.setAttribute("status", Constant.SC_BAD_REQUEST);
			request.setAttribute("error_message", Constant.SERVICIO_NO_VALIDO);
			ResponseUtils.printError(request, response, null);
	     }
		
	}

			
}
