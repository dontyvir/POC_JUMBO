package cl.cencosud.jumbo.util;

import java.io.File;
import java.sql.Connection;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class JFactory {
	
	public static Connection oConnection 	= null;
	public static QueryRunner qRunner 		= null;
	public static StringBuffer sBuffer 		= null;
	public static HSSFWorkbook hWorkbook	= null;
	public static File fFile				= null;
	public static SendMail sMail			= null;
	public static HSSFRichTextString richTextString = null;
	public static String MSG_ERROR  		= "";	
    
    public static Connection getConnectionDB2(){    	
    	if (oConnection == null) {
    		oConnection = ConnectionDB2.getConnectionDB2();
    	}
        return oConnection;
    }
    
    public static StringBuffer getStringBuffer() { 
    	if (sBuffer  == null) {
    		sBuffer = new StringBuffer();
    	}
    	sBuffer.delete(0, sBuffer.length());
    	return sBuffer;
    }
    
    public static QueryRunner getQueryRunner() { 
    	if (qRunner  == null) {
    		qRunner = new QueryRunner();
    	}
    	return qRunner;
    }
    
    public static HSSFWorkbook getHSSFWorkbook() { 
    	if (hWorkbook  == null) {
    		hWorkbook = new HSSFWorkbook();
    	}
    	return hWorkbook;
    }
    
    public static File getFile(String file) { 
    	if (fFile  == null) {
    		fFile = new File(file);
    	}
    	return fFile;
    }
    
    public static SendMail getSendMail(String host, String from, String bodyHtml, List archivos) { 
    	if (sMail  == null) {
    		sMail = new SendMail(host, from, bodyHtml, archivos);
    	}
    	return sMail;
    }
    
    public static HSSFRichTextString getHSSFRichTextString() { 
    	if (richTextString  == null) {
    		richTextString = new HSSFRichTextString();
    	}
    	return richTextString;
    }
    
}
