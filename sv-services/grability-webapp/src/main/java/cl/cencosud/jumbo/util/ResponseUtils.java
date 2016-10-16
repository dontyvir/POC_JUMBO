package cl.cencosud.jumbo.util;


import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

import org.apache.commons.lang.StringUtils;

import cl.bbr.jumbocl.shared.log.Logging;
import cl.cencosud.jumbo.Constant.Constant;

/**
 * General purpose utility methods related to generating a servlet response in
 * the View Servlet.
 */
public class ResponseUtils {
    // ------------------------------------------------------- Static Variables


    /**
     * Java 1.4 encode method to use instead of deprecated 1.3 version.
     */
    private static Method encode = null;
    
    /**
     * General purpose utility is View author.
     */
    private static final String author = "@alvgutierr";
    private static final Logging logger = new Logging(ResponseUtils.class.getName());
   

    /**
     * Initialize the encode variable with the
     * Java 1.4 method if available.
     */
    static {
        try {
            // get version of encode method with two String args
            Class[] args = new Class[] { String.class, String.class };

            encode = URLEncoder.class.getMethod("encode", args);
        } catch (NoSuchMethodException e) {
            //log.debug("Could not find Java 1.4 encode method.  Using deprecated version.", e);
        }
    }

    // --------------------------------------------------------- Public Methods

    /**
     * URLencodes a string assuming the character encoding is UTF-8.
     *
     * @param url
     * @return String The encoded url in UTF-8
     */
    public static String encodeURL(String url) {
        return encodeURL(url, "UTF-8");
    }

    /**
     * Use the new URLEncoder.encode() method from Java 1.4 if available, else
     * use the old deprecated version.  This method uses reflection to find
     * the appropriate method; if the reflection operations throw exceptions,
     * this will return the url encoded with the old URLEncoder.encode()
     * method.
     *
     * @param enc The character encoding the urlencode is performed on.
     * @return String The encoded url.
     */
    public static String encodeURL(String url, String enc) {
        try {
            if ((enc == null) || (enc.length() == 0)) {
                enc = "UTF-8";
            }

            // encode url with new 1.4 method and UTF-8 encoding
            if (encode != null) {
                return (String) encode.invoke(null, new Object[] { url, enc });
            }
        } catch (IllegalAccessException e) {
            //log.debug("Could not find Java 1.4 encode method.  Using deprecated version.", e);
        } catch (InvocationTargetException e) {
            //log.debug("Could not find Java 1.4 encode method. Using deprecated version.",e);
        }

        return URLEncoder.encode(url);
    }
    
    /**
     * print Json a string assuming the character encoding is ISO-8859-1.
     *
     * @param response, out
     * @return void
     */
    public static void printStringJson(HttpServletResponse response, HttpServletRequest request, String out, Exception e) {
    	printStringJson(response, request, out, Constant.ENCODING, e);
    	//printStringJson(response, request, out, "ISO-8859-1", e);
    }
    
    /**
     * Print string .
     *
     * @param response y out string.
     * @return void.
     */    
    public static void printStringJson(HttpServletResponse response, HttpServletRequest request, String out, String encoding, Exception e){
				
		PrintWriter output;
		try {
			
			//response.setCharacterEncoding(encoding); sobre escribe a setContentType
			if(isXmlOut(request)){	
				response.setContentType("application/xml; charset="+encoding);
			}else{
				response.setContentType("application/json; charset="+encoding);
			}
			output = response.getWriter();		
			
			response.addHeader("X-requestId", String.valueOf(request.getAttribute("requestId")));
			response.addHeader("X-authorized", String.valueOf(request.getHeader("X-authorized")));
			response.addHeader("X-author", ResponseUtils.author);
			
			logger.info("x-Response : "+String.valueOf(request.getAttribute("requestId")));		
						
			JSONObject jsOut= (JSONObject)JSONSerializer.toJSON(out);
			String status = jsOut.getString("status");
			String error_message = jsOut.getString("error_message");
			
			
			Date date = new Date();
			SimpleDateFormat CreationDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");	
			
			String CreationDateTime = CreationDateTimeFormat.format(date);						
			String Target = Constant.TARGET;
			JSONObject jsInputSender = new JSONObject();
			JSONObject jsEBMTracking= new JSONObject();	
						
			if(Util.isGetMethod(request.getMethod()) || (request.getAttribute("request.getReader") == null || !Util.isJSONValid(request.getAttribute("request.getReader").toString()))){

				//CreationDateTime = "";
				//Target = "";
				
				jsInputSender = new JSONObject();
				jsInputSender.put("Application", Constant.APPLICATION);
				jsInputSender.put("Country", Constant.COUNTRY);
				jsInputSender.put("BusinessUnit", Constant.BUSINESSUNIT);
								
				jsEBMTracking= new JSONObject();	
													
				try{
					jsEBMTracking.put("IntegrationCode", String.valueOf(request.getAttribute("IntegrationCode")));
				}catch(Exception ex){
					jsEBMTracking.put("IntegrationCode", "");
				}
					
				try{
					jsEBMTracking.put("ReferenceID",  String.valueOf(request.getAttribute("ReferenceID")));
				}catch(Exception ex){
					jsEBMTracking.put("ReferenceID", "");
				}
				
				
			}else{				
				
				if(Util.isJSONValid(request.getAttribute("request.getReader").toString())){
					JSONObject jsInput = (JSONObject)request.getAttribute("request.getReader");
				
					if(Util.isJSONValid(jsInput.getString("EBMHeader"))){
						JSONObject jsInputEBMHeader = (JSONObject)JSONSerializer.toJSON( jsInput.getString("EBMHeader"));
						CreationDateTime = jsInputEBMHeader.getString("CreationDateTime");		
						Target = jsInputEBMHeader.getString("Target");
						
						if(Util.isJSONValid(jsInputEBMHeader.getString("Sender"))){
							jsInputSender = (JSONObject)JSONSerializer.toJSON(jsInputEBMHeader.getString("Sender"));	
						}
						
						if(Util.isJSONValid(jsInputEBMHeader.getString("EBMTracking"))){
							jsEBMTracking= (JSONObject) JSONSerializer.toJSON(jsInputEBMHeader.getString("EBMTracking"));
						}
					}							
				}				
			}
			
			//__________________________________________________________
			//Salida
			JSONObject objOut	= new JSONObject();		
			
			JSONObject objEBMHeader	= new JSONObject();	
			
			objEBMHeader.put("CreationDateTime" , CreationDateTime);			
			objEBMHeader.put("Sender" , jsInputSender.toString());
			objEBMHeader.put("Target" , Target);
			objEBMHeader.put("EBMTracking",jsEBMTracking.toString());			
			
			JSONObject objResponse	= new JSONObject();	
			
			if("0000".equals(status)){
				
				objResponse.put("ReturnCode", "0");
				objResponse.put("ReturnMessage", "OK");
				objResponse.put("RequestID", Constant.APPLICATION);
				objResponse.put("ResponderID", Target);
				objResponse.put("ResponseDateTime", CreationDateTime);
				objResponse.put("ErrorCode", "");
				
				JSONObject objErrorDetail	= new JSONObject();	
				objErrorDetail.put("ErrorType", "");
				objErrorDetail.put("HandlerServiceName", "");
				objErrorDetail.put("SourceApplication", "");
				objErrorDetail.put("SourceServiceName", "");
				objErrorDetail.put("SourceErrorCode", "");
				objErrorDetail.put("SourceErrorMessage", "");
				objErrorDetail.put("SourceErrorTrace", "");			
				objResponse.put("ErrorDetail", objErrorDetail.toString());	
				
			}else{
							
				int intStatus = 0;
				try{intStatus = Integer.parseInt(status);}catch(Exception en){}
												
				if(intStatus > 0 && intStatus < 100){
					objResponse.put("ReturnCode", "1");
					objResponse.put("ReturnMessage", error_message);
					objResponse.put("RequestID", Constant.APPLICATION);
					objResponse.put("ResponderID", Target);					
					objResponse.put("ResponseDateTime", CreationDateTime);
					
					switch(intStatus){
					
						case Constant.SC_BAD_REQUEST_INT:
							objResponse.put("ErrorCode", Constant.SC_BAD_REQUEST_ERROR_CODE);
						break;
						
						case Constant.SC_BAD_REQUEST_PARAM_INT:
							objResponse.put("ErrorCode", Constant.SC_BAD_REQUEST_PARAM_ERROR_CODE);
						break;
						
						case Constant.SC_ERROR_BD_INT:
							objResponse.put("ErrorCode", Constant.SC_ERROR_BD_ERROR_CODE);
						break;
						
						case Constant.SC_ERROR_INESPERADO_SQL_INT:
							objResponse.put("ErrorCode", Constant.SC_ERROR_INESPERADO_SQL_ERROR_CODE);
						break;
						
						case Constant.SC_NO_SE_ENCONTRO_CONF_NOMBRE_SERVICIO_INT:
							objResponse.put("ErrorCode", Constant.SC_NO_SE_ENCONTRO_CONF_NOMBRE_SERVICIO_ERROR_CODE);
						break;
						
						case Constant.SC_ERROR_INESPERADO_INT:
							objResponse.put("ErrorCode", Constant.SC_ERROR_INESPERADO_ERROR_CODE);
						break;				
						
						default: objResponse.put("ErrorCode", Constant.SC_ERROR_INESPERADO_ERROR_CODE);
						
					}
					
					JSONObject objErrorDetail	= new JSONObject();	
					objErrorDetail.put("ErrorType", "Application");
					objErrorDetail.put("HandlerServiceName", String.valueOf(request.getAttribute("service")));
					objErrorDetail.put("SourceApplication", String.valueOf(request.getAttribute("contextPath")+"/"+request.getAttribute("service")));
					objErrorDetail.put("SourceServiceName",  String.valueOf(request.getAttribute("restMethod")+" "+request.getAttribute("service")));
					objErrorDetail.put("SourceErrorCode", status);
					objErrorDetail.put("SourceErrorMessage", error_message);
					if (e != null && e.fillInStackTrace()!=null){
						objErrorDetail.put("SourceErrorTrace", e.fillInStackTrace().toString()); 
					}else if(e != null ){
						objErrorDetail.put("SourceErrorTrace", e.getMessage());
					}else{
						objErrorDetail.put("SourceErrorTrace", Constant.MSG_EXCEPCION_SIN_TRAZA+ "App error:" +status+":"+error_message);
					}
					String jsErrorDetail = objErrorDetail.toString();
					objResponse.put("ErrorDetail", jsErrorDetail);	
					logger.error(jsErrorDetail);
				}else{
					objResponse.put("ReturnCode", "0");
					objResponse.put("ReturnMessage", error_message);
					objResponse.put("RequestID", Constant.APPLICATION);
					objResponse.put("ResponderID", Target);
					objResponse.put("ResponseDateTime", CreationDateTime);
					objResponse.put("ErrorCode", "");
					
					JSONObject objErrorDetail	= new JSONObject();	
					objErrorDetail.put("ErrorType", "Application");
					objErrorDetail.put("HandlerServiceName", String.valueOf(request.getAttribute("service")));
					objErrorDetail.put("SourceApplication", String.valueOf(request.getAttribute("contextPath")+"/"+request.getAttribute("service")));
					objErrorDetail.put("SourceServiceName",  String.valueOf(request.getAttribute("restMethod")+" "+request.getAttribute("service")));
					objErrorDetail.put("SourceErrorCode", status);
					objErrorDetail.put("SourceErrorMessage", error_message);
					if (e != null && e.fillInStackTrace()!=null){
						objErrorDetail.put("SourceErrorTrace", e.fillInStackTrace().toString()); 
					}else if(e != null ){
						objErrorDetail.put("SourceErrorTrace", e.getMessage());
					}else{
						objErrorDetail.put("SourceErrorTrace", Constant.MSG_EXCEPCION_SIN_TRAZA+ "App error:" +status+":"+error_message);
					}
					objResponse.put("ErrorDetail", objErrorDetail.toString());	
					
				}								
			}
			
			objEBMHeader.put("Response",objResponse.toString());
			
			objOut.put("EBMHeader" , objEBMHeader.toString());
			objOut.put("DataArea" , out);
		
					 		  
			logger.info("Locale : "+response.getLocale());
			logger.info("BufferSize : "+response.getBufferSize());
			logger.info("ContentType : "+response.getContentType());
			logger.info("CharacterEncoding : "+response.getCharacterEncoding());
						
			if(isXmlOut(request)){	
				XMLSerializer xmlSerializer = new XMLSerializer();  			
				xmlSerializer.setTypeHintsCompatibility( false );  
			    String xml = xmlSerializer.write(objOut);  
			    logger.info(xml+"\n");
			    output.println(xml);
			}else{
				String js = objOut.toString();
				logger.info(js+"\n");
				output.println(js);
			}			
			output.close(); 
			response.flushBuffer();
			
		} catch (IOException ex) {
			logger.error(ex);
		} 
		
    }
    
    /**
     * Print string .
     *
     * @param response y out string.
     * @return void.
     */ 
    public static void printError(HttpServletRequest request, HttpServletResponse response, Exception e) {
    			
    	JSONObject obj=new JSONObject();
    	if( e != null ){ //Se agrega al logger
    		
    	}

    	if(request.getAttribute("status") != null && !StringUtils.isEmpty(request.getAttribute("status").toString())){
    		obj.put("status",request.getAttribute("status").toString());
    	}else{
    		obj.put("status",Constant.SC_BAD_REQUEST);
    	}

    	if(request.getAttribute("error_message")!= null && !StringUtils.isEmpty(request.getAttribute("error_message").toString())){
    		obj.put("error_message",request.getAttribute("error_message").toString());
    	}else{
    		obj.put("error_message",Constant.MSG_BAD_REQUEST);
    	}

    	obj.put("method",request.getMethod());
    	printStringJson(response, request, obj.toString(), e);
	}
    
    /**
     * Print string .
     *
     * @param response y out string.
     * @return void.
     */ 
    public static boolean isXmlOut(HttpServletRequest request) {    	
    	return (request.getParameter("_view") != null && StringUtils.equals("xml", request.getParameter("_view").toString()));	
    }
        	
}

