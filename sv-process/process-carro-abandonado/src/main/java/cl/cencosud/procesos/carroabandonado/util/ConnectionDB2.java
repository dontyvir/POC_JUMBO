package cl.cencosud.procesos.carroabandonado.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;


/**
 * Clase que se encarga de ayudar a la conexión a la base de datos de DB2
 *
 */
public class ConnectionDB2 {
	
   	/**
   	 * Connection del aplicativo.
   	 */
   	public Connection conn;
    /**
     * Instancia unica del la clase.
     */
    public static ConnectionDB2 instance = null;
    
    /**
     * Logger de la clase.
     */
    protected static Logger logger = Logger.getLogger(ConnectionDB2.class.getName());
    
    /**
     * Constructor de la clase.
     * Se encarga de realizar la inicializacion de la conexión a la base de datos.
     * 
     */
    private ConnectionDB2() {
    	
    	logger.debug("[ConnectionDB2][ConnectionDB2] Ingreso al metodo constructor de la clase.");
    	logger.debug("[ConnectionDB2][ConnectionDB2] Realizo la busqueda de los parámetros en el properties.");
    	
        String url		= Util.getPropertiesString("db.url");
        String dbName 	= Util.getPropertiesString("db.dbname");
        String driver 	= Util.getPropertiesString("db.driver");
        String userName = Util.getPropertiesString("db.username");
        String password = Util.getPropertiesString("db.password");

        try {
        	
        	logger.info("[ConnectionDB2][ConnectionDB2] url 		: " + url);
        	logger.info("[ConnectionDB2][ConnectionDB2] dbName 		: " + dbName);
        	logger.info("[ConnectionDB2][ConnectionDB2] driver 		: " + driver);
        	logger.info("[ConnectionDB2][ConnectionDB2] userName 	: " + userName);
        	logger.info("[ConnectionDB2][ConnectionDB2] password 	: " + password);
        	
            DbUtils.loadDriver(driver);
            conn = DriverManager.getConnection(url+"/"+dbName,userName,password);
        
        }
       catch (Exception e) {
    	   
    	   logger.error("[ConnectionDB2][ConnectionDB2] A ocurrido un error al realizar la inicialización de la conexión.",e);
        
       }
        
    }
    
    /**
     * Método que se encarga de recuperar al instancia de la conexión a la base de datos.
     * @return <code>Connection</code>
     */
    public static synchronized Connection getConnectionDB2() {
    	logger.debug("[ConnectionDB2][getConnectionDB2] Ingreso al método.");
    	if ( instance == null ) {
    		
    		logger.debug("[ConnectionDB2][getConnectionDB2] Realizo la instancia de la clase.");
        	instance = new ConnectionDB2();
        
        }
        
    	logger.debug("[ConnectionDB2][getConnectionDB2] Retorno conexión.");
        return instance.conn;	 
    }
    
    /**
     * Método que se encarga de realizar el cierre de la conexión a la base de datos.
     * 
     * @return <code>String</code> Mensaje de resultado
     */
    public static String closeConnectionDB2() {
    	logger.debug("[ConnectionDB2][getConnectionDB2] Ingreso al método.");
    	String msg	=	null;
    	
        if (instance.conn != null) {
           
        	try {
        		
        		logger.debug("[ConnectionDB2][getConnectionDB2] Realizo el cierre de la conexión.");
        		instance.conn.close();
           
        	} catch (SQLException e) {
        	  
        		msg = e.getMessage();
        		logger.error("[ConnectionDB2][closeConnectionDB2] Error al realizar el cierre de la conexión.",e);
           
        	}
        }
        
        logger.debug("[ConnectionDB2][getConnectionDB2] Fin del metodo. Mensaje " + msg);
        return msg;
     }
	  
}
