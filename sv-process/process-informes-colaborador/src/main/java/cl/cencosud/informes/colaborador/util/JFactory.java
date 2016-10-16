package cl.cencosud.informes.colaborador.util;

import java.io.File;
import java.sql.Connection;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


/**
 * Clase que se encarga de ser la fabrica de las clases helpers 
 * del aplicativo. En ella se realizan las instancias de estos helpers
 * ya sea el de conexión, obtener las hojas excel o el envio de correo electronico.
 *
 */
public class JFactory {
	
	/**
	 * Connection a la base de datos
	 */
	public static Connection oConnection 	= null;
	/**
	 * QueryRunner de BD2 para las consultas
	 */
	public static QueryRunner qRunner 		= null;
	/**
	 * StringBuffer 
	 */
	public static StringBuffer sBuffer 		= null;
	/**
	 * Libro excel
	 */
	public static HSSFWorkbook hWorkbook	= null;
	/**
	 * Archivos
	 */
	public static File fFile				= null;
	/**
	 * Objeto para el envio de correo.
	 */
	public static SendMail sMail			= null;
	/**
	 * Texto para el excel
	 */
	public static HSSFRichTextString richTextString = null;
	/**
	 * mensaje de error.
	 */
	public static String MSG_ERROR  		= "";	
	/**
     * Logger de la clase.
     */
    protected static Logger logger = Logger.getLogger(JFactory.class.getName());
    
    
    /**
     * Método que se encarga de obtener la conexión a través de ConnectionDB2
     * @see cl.cencosud.informes.util.ConnectionDB2
     * @return <code>Connection</code> con la instancia a la conexión de base de datos.
     */
    public static Connection getConnectionDB2(){    	
    	
    	logger.debug("[JFactory][getConnectionDB2] Ingreso al método.");
    	
    	if (oConnection == null) {
    		
    		logger.debug("[JFactory][getConnectionDB2] Obtengo la conexión.");
    		oConnection = ConnectionDB2.getConnectionDB2();
    	
    	}
        
    	logger.debug("[JFactory][getConnectionDB2] retorno la conexión.");
    	return oConnection;
    }
    
    /**
     * Método que se encarga de recuperar una instancia de StringBuffer
     * @return <code>StringBuffer</code>
     */
    public static StringBuffer getStringBuffer() { 
    	logger.debug("[JFactory][getStringBuffer] Ingreso al método.");
    	
    	if (sBuffer  == null) {
    		logger.debug("[JFactory][getStringBuffer] Recupero un StringBuffer.");
    		sBuffer = new StringBuffer();
    	
    	}
    	
    	logger.debug("[JFactory][getStringBuffer] Borro su contenido.");
    	sBuffer.delete(0, sBuffer.length());
    	
    	logger.debug("[JFactory][getStringBuffer] Retorno el StringBuffer.");
    	return sBuffer;
    }
    
    /**
     * Método que se encarga de recuperar una instancia de un queryRunner
     * @return <code>QueryRunner</code>
     */
    public static QueryRunner getQueryRunner() { 
    	
    	logger.debug("[JFactory][getQueryRunner] Ingreso al método.");
    	
    	if (qRunner  == null) {
    		
    		logger.debug("[JFactory][getQueryRunner] Realizo la instancia.");
    		qRunner = new QueryRunner();
    	
    	}
    	
    	logger.debug("[JFactory][getQueryRunner] Retorno el objeto.");
    	return qRunner;
    }
    
    /**
     * Método que se encarga de recuperar un nuevo libro de excel
     * @return <code>HSSFWorkbook</code>
     */
    public static HSSFWorkbook getHSSFWorkbook() { 
    	
    	logger.debug("[JFactory][getHSSFWorkbook] Ingreso al método.");
    	
    	if (hWorkbook  == null) {
    		
    		logger.debug("[JFactory][getHSSFWorkbook] Realizo la instancia.");
    		hWorkbook = new HSSFWorkbook();
    	
    	}
    	
    	logger.debug("[JFactory][getHSSFWorkbook] retorno el objeto.");
    	return hWorkbook;
    }
    
    /**
     * Método que se encarga de recuperar un nuevo archivo a partir de su ruta
     * 
     * @param file String de ubicación del archivo.
     * @return <code>File</code>
     */
    public static File getFile(String file) { 
    	
    	logger.debug("[JFactory][getFile] Ingreso al método.");
    	
    	if (fFile  == null) {
    		
    		logger.debug("[JFactory][getFile] Realizo la instancia del file.");
    		fFile = new File(file);
    	
    	}
    	
    	logger.debug("[JFactory][getFile] Retorno el objeto.");
    	return fFile;
    }
    
    /**
     * Método que se encarga de recuperar el objeto para realizar envios de correo.
     * 
     * @param host 		ip del servidor de smtp 
     * @param from		remitente
     * @param bodyHtml	cuerpo html con el contenido
     * @param archivos	archivos adjuntos
     * @return <code>SendMail</code>
     */
    public static SendMail getSendMail(String host, String from, String bodyHtml, List archivos) { 
    	
    	logger.debug("[JFactory][getSendMail] Ingreso al método.");
    	
    	if (sMail  == null) {
    		
    		logger.debug("[JFactory][getSendMail] Realizo la instancia.");
    		sMail = new SendMail(host, from, bodyHtml, archivos);
    	
    	}
    	
    	logger.debug("[JFactory][getSendMail] Retorno el objeto.");
    	return sMail;
    }
    
    /**
     * Método que se encarga de recuperar el texto para el excel
     * @return <code>HSSFRichTextString</code>
     */
    public static HSSFRichTextString getHSSFRichTextString() { 
    	
    	logger.debug("[JFactory][getHSSFRichTextString] Ingreso al método.");
    	
    	if (richTextString  == null) {
    	
    		logger.debug("[JFactory][getHSSFRichTextString] Realizo la instancia.");
    		richTextString = new HSSFRichTextString();
    	
    	}
    	
    	logger.debug("[JFactory][getHSSFRichTextString] Retorno el objeto.");
    	return richTextString;
    }
    
}
