package cl.bbr.fo.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;

public class pruebaGet extends Command{

	private static final long serialVersionUID = 7731289427383082257L;

	protected void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		try{
	        HttpSession session = request.getSession(true);  
	        session.setAttribute("ses_oJsonFacebook", null);
	        PrintWriter out = response.getWriter(); 
            /*      	
	    	String proxy = "localhost";
			String port  = "3128";		 		
			Properties systemProperties = System.getProperties();
			systemProperties.setProperty("https.proxyHost",proxy);
			systemProperties.setProperty("https.proxyPort",port);
			*/
			String tokenJunta = "", apikeyJunta = "", amountJunta = "";
			String parametrosJunta = "?token="+tokenJunta+"&apikey="+apikeyJunta+"&amount="+amountJunta;

			//URL url = new URL("http://junta.cl/cashback"+parametrosJunta);
			URL url = new URL("https://junta.cl:443/cashback");


			  URLConnection con = url.openConnection();
			  con.connect();
			  BufferedReader in = new BufferedReader(
				 new InputStreamReader(con.getInputStream()));
		 
			  PrintWriter pw = response.getWriter();
			  String linea;
			  while ((linea = in.readLine()) != null) {
				 System.out.println(linea);
				 out.println(linea.toString());
				 pw.write(linea);				 

			  }
	        
			//response.setContentType("application/json");
			out.close();
			pw.close();
			
		} catch (Exception e) {
			logger.error("RouterFacebookLogin",e);
		}
		
	}
	
}
