package cl.cencosud.jumbo.conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

public class IRSDataSource {
    
    static Logger logger = Logger.getLogger(IRSDataSource.class);

    private static IRSDataSource ds = null;
    private static BasicDataSource bds = null;

    private IRSDataSource() {

        InputStream props = getClass().getClassLoader().getResourceAsStream("datasource.properties");

        Properties prop = new Properties();
        try {
            prop.load(props);
        } catch (IOException e) {
            logger.error("Error inicializando datasource.properties", e);
        }

        bds = new BasicDataSource();
        bds.setDriverClassName(prop.getProperty("conf.db.drivemanager.ClassName"));
        bds.setUsername(prop.getProperty("conf.db.drivemanager.usr"));
        bds.setPassword(prop.getProperty("conf.db.drivemanager.pass"));
        bds.setUrl(prop.getProperty("conf.db.drivemanager.URL"));
        bds.setMaxActive(Integer.parseInt(prop.getProperty("conf.db.drivemanager.MaxActive")));
        bds.setMaxIdle(Integer.parseInt(prop.getProperty("conf.db.drivemanager.MaxIdle")));
    }

    public static IRSDataSource getSingleton() {

        if ( ds == null ) {
            ds = new IRSDataSource();
        }
        return ds;
    }

    public Connection getConnection() throws SQLException {

        return bds.getConnection();
    }

    public static void printDataSourceStats() throws SQLException {

        logger.info("NumActive: " + bds.getNumActive());
        logger.info("NumIdle: " + bds.getNumIdle());
    }

    public static void shutdownDataSource() throws SQLException {

        bds.close();
    }
}

/*
 * Location:D:\EAvendanoA\JUMBO.CL\EXPORT_INTEGRATION_2012\JUMBO_PROCESS\
 * InformePreFacturacion\lib\InformePreFacturacion.jar Qualified Name:
 * cl.cencosud.jumbo.conexion.IRSDataSource JD-Core Version: 0.6.0
 */