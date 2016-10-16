
package cl.bbr.boc.dao;

import java.sql.Connection;
import java.sql.SQLException;

import cl.bbr.boc.dao.jdbc.JdbcDAOFactoryParametros;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;

/**
 * Permite definir la modalidad de conexion con la base de datos, Jdbc, Hibernate, etc.
 *  
 * @author RMI-DNT
 *
 */
public abstract class DAOFactoryParametros {

		
	/**
	 * Identifica que la conexión a usar es Jdbc.
	 */
	public static final int JDBC = 2;
	  
	/**
	 * Obtiene la modalidad de conexion a la base de datos.
	 * 
	 * @param  whichFactory
	 * @return DAOFactory
	 */
	public static JdbcDAOFactoryParametros getDAOFactory(
		  int whichFactory) {
	  
		switch (whichFactory) {
		
		  case JDBC   : 
		   	  return new JdbcDAOFactoryParametros();
		  default           : 
			  return null;
		}
	  }
	/**
	 * Permite la conexion con la base de datos. 
	 */
	private static ConexionUtil conexionutil = new ConexionUtil();

	/**
	 * Obtiene la conexión JDBC con la base de datos.
	 * @return Connection
	 * @throws SQLException 
	 */
	public static Connection getConexion() throws SQLException 
	{
		//	Recommend connection pool implementation/usage
		return conexionutil.getConexion();
	}
	/**
	 * Obtiene la clase que realiza consultas a los Umbrales.
	 * 
	 * @return UmbralDAO
	 */
	public abstract UmbralDAO getUmbralDAO();
	
}
