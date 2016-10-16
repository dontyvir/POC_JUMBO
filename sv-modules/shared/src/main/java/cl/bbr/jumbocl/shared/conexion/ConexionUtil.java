package cl.bbr.jumbocl.shared.conexion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Inicia una conexión a la base de datos 
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ConexionUtil {

	private static Properties prop;

	/**
	 * Constructor de la clase, permite conectarse con el repositorio de datos
	 */
	public ConexionUtil() {
		
		
	}

	/**
	 * Retorna la conexión a la base de datos
	 * 
	 * @return conexión
	 * @throws Exception 
	 */
	public Connection getConexion() throws SQLException {
	    
        Connection con = null;
		try {

			if(prop == null){
				java.io.InputStream log4jProps = this.getClass().getClassLoader().getResourceAsStream("/conexion.properties");
				prop = new Properties();
				try {
					prop.load(log4jProps);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
	                try {
	                    log4jProps.close();
	                    log4jProps = null;
	                } catch (Exception e) {
	                	e.printStackTrace();
	                }
	            }
			}	
			
			String tipo = prop.getProperty("conf.db.tipo");

			if ( tipo.compareTo("drivemanager") == 0 ) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					con = DriverManager.getConnection(prop.getProperty("conf.db.drivemanager.URL"), prop.getProperty("conf.db.drivemanager.usr"),prop.getProperty("conf.db.drivemanager.pass"));
					con.createStatement();
				} catch (ClassNotFoundException e) {
					System.out.println("Conexión : No se encuentra la clase : " + e.getMessage());
					e.printStackTrace();
				}
			} else {
				Context ctx = new InitialContext();
				if (ctx == null)
					con = null;

                String Source = "java:comp/env/" + prop.getProperty("conf.db.datasource.lookup");
				DataSource ds = (DataSource) ctx.lookup(Source);
				if (ds != null) {
					con = ds.getConnection();
					//System.out.println("Se obtuvo la conexion JNDI a la base de datos " + prop.getProperty("conf.db.datasource.lookup"));
				} else {
	                Source = prop.getProperty("conf.db.datasource.lookup");
					ds = (DataSource) ctx.lookup(Source);
					if (ds != null) {
						con = ds.getConnection();
						//System.out.println("Se obtuvo la conexion JNDI a la base de datos " + prop.getProperty("conf.db.datasource.lookup"));
					} else {
						con = null;
						System.out.println("***** NO HAY MAS CONEXIONES *****");
					}
				}
			}

		/*} catch (SQLException e) {
			System.out.println("Conexión : No conecta : " + e.getMessage() + " "+ e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e);*/
		} catch (NamingException e) {
			System.out.println("Ocurrio un Error JNDI");
			System.out.println("***** " + e.getMessage() + " *****");
			e.printStackTrace();
			con = null;
			//throw new Exception(e);			
		}
		return con;
	}
	
	/**
	 * valida la conexion a la base de datos
	 * 
	 * @throws SQLException 
	 */
	public void isEnableConn() throws SQLException {
		
		Connection conn = null;
		Statement st = null;
		try {
			conn = this.getConexion();
			st = conn.createStatement();
			String sql = "SELECT current date FROM SYSIBM.SYSDUMMY1 WITH UR";
			st.execute(sql);
		} finally {			
			try {
				if(st != null)
					st.close();
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


}
