package cl.jumbo.ventaondemand.web.response;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cl.jumbo.ventaondemand.web.constant.Constant;
import cl.jumbo.ventaondemand.web.servlet.Client;
import net.sf.json.JSONObject;

public class ResponseJson {

	private static final Logger logger = Logger.getLogger(ResponseJson.class);
	private static JSONObject objOut = null; 
	private static JSONObject objHeader = null;
	private static JSONObject objResponse = null;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	public static HttpServletResponse getHeader(HttpServletResponse response, HttpServletRequest request){
		response.setContentType("application/json; charset="+ Constant.ENCODING);
		response.addHeader("X-requestId", String.valueOf(request.getAttribute("requestId")));
		response.addHeader("X-authorized", String.valueOf(request.getHeader("X-authorized")));
		response.addHeader("X-author", Constant.AUTHOR);
		return response;
	}
	
	public static JSONObject createObjResponseJSON(String mensaje, String status)throws Exception{		 	
		objResponse = new JSONObject();
		//objResponse.put("Code", "1");
		objResponse.put("AppId", Constant.APPLICATION);
		objResponse.put("DateTime", sdf.format(new Date()));
		objResponse.put("Message", mensaje);		
		objResponse.put("Status", status);
		return objResponse;		
	}
	
	public static JSONObject createObjOutResponseJSON(JSONObject objResponse)throws Exception{
		objHeader = new JSONObject();
		objOut = new JSONObject();
		
		objHeader.put("Response", objResponse.toString());
		objOut.put("Header", objHeader.toString());
		return objOut;
	}

	public static void outPutJson(JSONObject objOut, HttpServletResponse response) throws IOException, Exception{
		 PrintWriter output;
		 String js = objOut.toString();
		 logger.info(js+"");
		 output = response.getWriter();
		 output.println(js);							
		 output.close(); 
		 response.flushBuffer();
	 }
}
