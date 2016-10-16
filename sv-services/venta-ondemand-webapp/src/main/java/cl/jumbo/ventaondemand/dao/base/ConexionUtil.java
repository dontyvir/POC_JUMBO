package cl.jumbo.ventaondemand.dao.base;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import cl.jumbo.ventaondemand.utils.PropertyUtil;

/**
 * Inicia una conexion a la base de datos 
 * 
 */
public class ConexionUtil {

	private static Properties prop;

	public ConexionUtil() { }
			
	public static Connection getConexion() throws SQLException {
		Connection con = null;
		try {			
			prop = PropertyUtil.getInstance().getProperties();

			if ( prop.getProperty("conf.db.tipo").compareTo("drivemanager") == 0 ) {
				/*
				try {
					Class.forName("com.mysql.jdbc.Driver");
					con = DriverManager.getConnection(prop.getProperty("conf.db.drivemanager.URL"), prop.getProperty("conf.db.drivemanager.usr"),prop.getProperty("conf.db.drivemanager.pass"));
					con.createStatement();
				} catch (ClassNotFoundException e) {
					System.out.println("Conexión : No se encuentra la clase : " + e.getMessage());
					e.printStackTrace();
				}
				*/
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
						System.out.println("***** NO HAY MAS CONEXIONES - OnDEMAND *****");
					}
				}
			}
		} catch (NamingException e) {
			System.out.println("ERROR EN JNDI - OnDEMAND:" + e);			
			e.printStackTrace();
			con = null;
			//throw new Exception(e);			
		}
		return con;
	}
	
	public void isEnableConn() throws SQLException {
		Connection conn = null;
		Statement st = null;
		try {
			conn = ConexionUtil.getConexion();
			st = conn.createStatement();			
			st.execute("SELECT current date FROM SYSIBM.SYSDUMMY1 WITH UR");
		} finally {			
			try {
				closeStatement(st);
				closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeConnection(Connection conn) throws SQLException{		
		if (conn != null && !conn.isClosed()){
			try{
				conn.close();
			}catch (SQLException sqle) {
				sqle.printStackTrace();
				//logger.error("[Database][closeConnection] Error al cerrar conexion con la DB: " + e.getMessage());
				//Main.logger.error("Error al cerrar conexion con la DB: " + e.getMessage() );
			}		
		}
	}
		
	public static void closeStatement(Statement state)throws SQLException{
		if (state != null){
			try{
				state.close();
			}catch (SQLException sqle){
				sqle.printStackTrace();
				//Main.logger.error("[Database][closeConnection] Error al cerrar CallableStatement con la DB: " + ex.getMessage());
			}
		}
	}
	
	public static void closeCallableStatement(CallableStatement cs)throws SQLException{
		if (cs != null){
			try{
				cs.close();
			}catch (SQLException sqle){
				sqle.printStackTrace();
				//Main.logger.error("[Database][closeConnection] Error al cerrar CallableStatement con la DB: " + ex.getMessage());
			}
		}
	}

	public static void closePreparedStatement (PreparedStatement ps)throws SQLException{
		if (ps != null){
			try{
				ps.close();
			}catch (SQLException sqle){
				sqle.printStackTrace();
				//Main.logger.error("[Database][closeConnection] Error al cerrar PreparedStatement con la DB: " + ex.getMessage());
			}
		}		
	}
	   
	public static void closeResulset(ResultSet rs) throws SQLException{
		if (rs != null){
			try{
				rs.close();
			}catch(SQLException sqle){
				sqle.printStackTrace();
				//Main.logger.error("[Database][closeConnection] Error al cerrar PreparedStatement con la DB: " + ex.getMessage());
			}
		}
	}
}
