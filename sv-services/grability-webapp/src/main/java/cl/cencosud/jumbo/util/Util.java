package cl.cencosud.jumbo.util;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import cl.cencosud.jumbo.Constant.Constant;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class Util {
	
    public static boolean isJSONValid(String testJson) {
        try {
            JSONObject js = (JSONObject)JSONSerializer.toJSON(testJson);
        } catch (Exception ex) {
            return false;            
        }
        return true;
    }
    
    public static boolean isSqlException(Exception e) {
            	
    	if (e != null && e.getCause() != null && e.getCause() instanceof SQLException) {
    		return true;
    	}
    	
    	String except = "";
		if(e != null && e.getCause() != null){
			except = e.getCause().getMessage();
		}
		
		if(!StringUtils.isBlank(except)){
			
			if(StringUtils.contains(except.toLowerCase(), Constant.SQL_EXCEPTION)){					
				return true;
			}
			
			return false;
		}else{
			return false;
		}
    }

    
    public static String formatoFechaGrability(String fecha) {
        try {
        	//Date date = new Date();						
        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
        	
        	String f = dateFormat.format(dateFormat.parse(fecha));
        	String searchStr = "00:00:00";
        	
        	if(StringUtils.contains(f, searchStr)){
        		return StringUtils.trim(StringUtils.remove(f, searchStr));
        	}else{
        		return f;
        	}
        	
        } catch (Exception ex) {
            return "";            
        }        
    }
    
    public static boolean isContentTypeJson(String contentType){
		return StringUtils.contains(StringUtils.lowerCase(contentType), "json");
	}
	
    public static boolean isPostMethod(String method){
		return StringUtils.equals(StringUtils.upperCase(method), "POST");
	}
	
    public static boolean isGetMethod(String method){
		return StringUtils.equals(StringUtils.upperCase(method), "GET");
	}
	
    public static boolean isPutMethod(String method){
		return StringUtils.equals(StringUtils.upperCase(method), "PUT");
	}

    public static boolean isDeleteMethod(String method){
		return StringUtils.equals(StringUtils.upperCase(method), "DELETE");
	}
}
