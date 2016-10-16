package cl.cencosud.jumbo.conexion;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class ConexionUtil {
    
    Logger logger = Logger.getLogger(ConexionUtil.class);

    private Connection con;

    public Connection getConexion() {

        try {
            this.con = IRSDataSource.getSingleton().getConnection();
        } catch (SQLException e) {
            this.con = null;
            logger.error("No se pudo obtener una conexión: " + e.getMessage(), e);            
        }
        return this.con;
    }   
}

/*
 * Location:D:\EAvendanoA\JUMBO.CL\EXPORT_INTEGRATION_2012\JUMBO_PROCESS\
 * InformePreFacturacion\lib\InformePreFacturacion.jar Qualified Name:
 * cl.cencosud.jumbo.conexion.ConexionUtil JD-Core Version: 0.6.0
 */