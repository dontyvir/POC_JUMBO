package cl.bbr.jumbocl.shared.conexion;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * Inicia una conexión a la base de datos 
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ConexionUtil {


	private Connection con;
	

	public ConexionUtil() {
		
	}

	public Connection getConexion() {
		try {
			con = IRSDataSource.getSingleton().getConnection();
		} catch (SQLException e) {
			con = null;
			System.out.println("No se pudo obtener una conexión: " + e.getMessage());
			e.printStackTrace();
		}
		return con;
	}


	public void finalize() {
		try {
			if( !con.isClosed() )
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
