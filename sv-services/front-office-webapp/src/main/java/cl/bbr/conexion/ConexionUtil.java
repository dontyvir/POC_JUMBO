package cl.bbr.conexion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import cl.bbr.log.Logging;

/**
 * Inicia una conexión a la base de datos 
 * Modificado 17-04-2012 
 * @author BBR e-commerce & retail
 * 
 */
public class ConexionUtil {
    private static Logging logger = new Logging(ConexionUtil.class);
	private Connection con;
	private String tipo;
	private String url;
	private String driver;
	private String user;
	private String pass;
	private String lookup;

	/**
	 * Constructor de la clase, permite conectarse con el repositorio de datos
	 */
	public ConexionUtil() {
	   java.io.InputStream log4jProps = this
			.getClass()
			.getClassLoader()
			.getResourceAsStream("/conexion.properties");
	   Properties prop = new Properties();
	   try {
	      prop.load(log4jProps);
	   } catch (IOException e) {
	      logger.error("error al leer conexion.properties: ", e);
	   }
	   tipo = prop.getProperty("conf.db.tipo");
	   url = prop.getProperty("conf.db.drivemanager.URL");
	   driver = prop.getProperty("conf.db.drivemanager.classforname");
	   user = prop.getProperty("conf.db.drivemanager.usr");
	   pass = prop.getProperty("conf.db.drivemanager.pass");
	   lookup = prop.getProperty("conf.db.datasource.lookup");
	}

	/**
	 * Retorna la conexión a la base de datos
	 * 
	 * @return conexión		Conexión al repositorio de datos
	 */
	public Connection getConexion() {
		try {
			if ( tipo.compareTo("drivemanager") == 0 ) {
				try {
					Class.forName(driver);
					con = DriverManager.getConnection(url, user, pass);
					con.createStatement();
				} catch (ClassNotFoundException e) {
				   logger.error("Conexión : No se encuentra la clase : " , e);
				}
			} else {
				Context ctx = new InitialContext();
				if (ctx == null)
					this.con = null;

                String Source = "java:comp/env/" + lookup;
				DataSource ds = (DataSource) ctx.lookup(Source);
				if (ds != null) {
					this.con = ds.getConnection();
					//logger.debug("Se obtuvo la conexion JNDI a la base de datos " + prop.getProperty("conf.db.datasource.lookup"));
				} else {
	                Source = lookup;
					ds = (DataSource) ctx.lookup(Source);
					if (ds != null) {
						this.con = ds.getConnection();
					//	logger.debug("Se obtuvo la conexion JNDI a la base de datos " + prop.getProperty("conf.db.datasource.lookup"));
					} else {
						this.con = null;
						logger.error("***** NO HAY MAS CONEXIONES *****");
					}
				}

			}
		} catch (SQLException e) {
		   logger.error("Conexión : No conecta : " + e.getMessage() + " "+ e.getErrorCode());
		} catch (NamingException e) {
		    logger.error("Ocurrio un Error JNDI:", e);
			this.con = null;
		}
		return this.con;
	}

	/**
	 * Termina una conexión sólo si quedó abierta
	 */
	public void finalize() {
		try {
			if( con != null && !con.isClosed() )
				con.close();
		} catch (Exception e) {
		   logger.error("Error al cerrar conexión: " , e);
		}
	}

}
