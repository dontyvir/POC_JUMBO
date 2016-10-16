package cl.bbr.jumbocl.shared.conexion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.commons.dbcp.BasicDataSource;

public class IRSDataSource {

	static private IRSDataSource 	ds = null;
	static private BasicDataSource bds = null;
	
	private IRSDataSource(){
		
		java.io.InputStream props = this
		.getClass()
		.getClassLoader()
		.getResourceAsStream("conexion.properties");
		
		Properties prop = new Properties();
		try {
			prop.load(props);
		} catch (IOException e) {
			System.err.println(" problemas en IRSDataSource ");
		}
		
		bds = new BasicDataSource();
		bds.setDriverClassName(prop.getProperty("conf.db.drivemanager.ClassName"));
		bds.setUsername(prop.getProperty("conf.db.drivemanager.usr"));
		bds.setPassword(prop.getProperty("conf.db.drivemanager.pass"));
		bds.setUrl(prop.getProperty("conf.db.drivemanager.URL"));
		bds.setMaxActive(Integer.parseInt(prop.getProperty("conf.db.drivemanager.MaxActive")));
		bds.setMaxIdle(Integer.parseInt(prop.getProperty("conf.db.drivemanager.MaxIdle")));
		
	}
	
    static public IRSDataSource getSingleton() {

        if (ds == null) {
        	ds = new IRSDataSource();
        }
        return ds;
    }

    /*
     * Metodos del singleton.
     */

    public Connection getConnection() throws SQLException {
    	//printDataSourceStats();
        return bds.getConnection();
    }
	
    public static void printDataSourceStats() throws SQLException {
        System.out.println("NumActive: " + bds.getNumActive());
        System.out.println("NumIdle: " + bds.getNumIdle());
    }
    
    public static void shutdownDataSource() throws SQLException {
        bds.close();
    }
    
    
}
