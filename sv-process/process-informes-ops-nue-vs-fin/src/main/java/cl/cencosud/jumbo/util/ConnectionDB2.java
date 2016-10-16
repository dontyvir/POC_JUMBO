package cl.cencosud.jumbo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;


public class ConnectionDB2 {
	
   	public Connection conn;
    public static ConnectionDB2 instance = null;
    
    protected static Logger logger = Logger.getLogger(ConnectionDB2.class.getName());
    
    private ConnectionDB2() {
    	
        String url		= Util.getPropertiesString("db.url");
        String dbName 	= Util.getPropertiesString("db.dbname");
        String driver 	= Util.getPropertiesString("db.driver");
        String userName = Util.getPropertiesString("db.username");
        String password = Util.getPropertiesString("db.password");

        try {
            DbUtils.loadDriver(driver);
            conn = DriverManager.getConnection(url+"/"+dbName,userName,password);
        }
       catch (Exception e) {
    	   logger.error(e);
        }
    }
    
    public static synchronized Connection getConnectionDB2() {
        if ( instance == null ) {
        	instance = new ConnectionDB2();
        }
        return instance.conn;	 
    }
    
    public static String closeConnectionDB2() {
    	String msg=null;
        if (instance.conn != null) {
           try {
        	   instance.conn.close();
           } catch (SQLException e) {
        	   msg = e.getMessage();
        	   logger.error(e);
           }
        }
        return msg;
     }
	  
}
