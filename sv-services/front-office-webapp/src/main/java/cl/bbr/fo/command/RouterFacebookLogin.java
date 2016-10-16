package cl.bbr.fo.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;

public class RouterFacebookLogin extends Command{

	private static final long serialVersionUID = 7731289427383082257L;

	protected void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		try{
	        HttpSession session = request.getSession(true);  
	        session.setAttribute("ses_oJsonFacebook", null);
	        PrintWriter out = response.getWriter(); 
                  	
	    	if (!(request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").toString().equals("XMLHttpRequest"))){
	    		response.sendRedirect("/FO/LogonForm"); //Say good bye	
	    	}     

	        String access_token =(request.getParameter("access_token") != null)? request.getParameter("access_token").toString().trim():null;
	        String id =(request.getParameter("id") != null)? request.getParameter("id").toString().trim():null;
				            
	        if(access_token == null  || id == null){
	        	response.sendRedirect("/FO/LogonForm"); //Say good bye
	        }            
	    	
	        BizDelegate biz = new BizDelegate();  
	        	        
	        JSONObject oJsonResponse = new JSONObject(); 
	        JSONObject oJsonFromFacebook = requestGraphFacebook(access_token);	        	              
	        
	        String idFacebook = oJsonFromFacebook.getString("id");
	        
	        if(oJsonFromFacebook != null && id.equals(idFacebook)){
	        	String rut = biz.clienteGetRutByIdFacebook(idFacebook);
	        	if(rut != null ){//Si ya se logeo via facebook por primera vez
	        		String redirect ="/FO/CategoryDisplay";
		        	long rut_parser = Long.parseLong(rut);			
		        	ClienteDTO cliente = biz.clienteGetByRut(rut_parser);		        	
		        	sessionStart(request, biz, cliente, FACEBOOK_SESSION);
		        	
					if(session.getAttribute("ses_destination") != null)
						redirect = session.getAttribute("ses_destination").toString();
                					
					oJsonResponse.put("status", "202");//202 Accepted
					oJsonResponse.put("location", redirect);
					
	        	}else{
	        		session.setAttribute("ses_oJsonFacebook", oJsonFromFacebook);

	        		oJsonResponse.put("status", "406");//Not Acceptable, debe logearse en jumbo para asociar su cuanta facbook a jumbo.
	        		oJsonResponse.put("location", "#");  
	        	}	
	        	
	        }else{
	        	oJsonResponse.put("status", "500"); //Say good bye	        	
	        }
	        
			response.setContentType("application/json");
			out.println(oJsonResponse.toString());
			out.close();
			
		} catch (Exception e) {
			logger.error("RouterFacebookLogin",e);
		}
		
	}
	
	private JSONObject requestGraphFacebook(String token){
				
		String url="https://graph.facebook.com:443/me?access_token="+token;
		
		
		String inputLine;
		String inputOut="";
		
		URL server = null;
		HttpURLConnection connection = null;
		InputStreamReader oInputStream = null;
		BufferedReader oBufferedReader =null;
		
		try {
			server = new URL(url);
			
				//solo localhost con proxy Cntlm
				/*String proxy = "localhost";
				String port  = "3128";		 		
				Properties systemProperties = System.getProperties();
				systemProperties.setProperty("https.proxyHost",proxy);
				systemProperties.setProperty("https.proxyPort",port);*/
		
			
			connection = (HttpURLConnection)server.openConnection();
			connection.connect();
			
			oInputStream = new InputStreamReader(connection.getInputStream());
			oBufferedReader = new BufferedReader(oInputStream);
								
			while ((inputLine = oBufferedReader.readLine()) != null) {
				inputOut += inputLine;
			}
			
			JSONObject oJsonFromFacebook = (JSONObject) JSONSerializer.toJSON(inputOut);	
			return (!oJsonFromFacebook.isNullObject())? oJsonFromFacebook:null;
			
		}catch (Exception e) {
			logger.error("RouterFacebookLogin",e);
			return null;
		}finally{
			server = null;
			try {oInputStream.close();} catch (IOException e) {}
			try {oBufferedReader.close();} catch (IOException e) {}
			try {connection.disconnect();} catch (Exception e) {}			
		}
		
	}		
}
