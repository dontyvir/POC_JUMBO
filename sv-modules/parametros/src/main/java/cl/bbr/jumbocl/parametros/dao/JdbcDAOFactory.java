package cl.bbr.jumbocl.parametros.dao;

import java.sql.Connection;
import java.sql.SQLException;

import cl.bbr.jumbocl.shared.conexion.ConexionUtil;

/**
 * Clase que se conecta a la base de datos mediante JDBC.
 * @author BBR
 *
 */
public class JdbcDAOFactory extends DAOFactory {

	/**
	 * Permite la conexion con la base de datos. 
	 */
	private static ConexionUtil conexionutil = new ConexionUtil();
	
	/**
	 * Obtiene la conexi�n JDBC con la base de datos.
	 * @return Connection
	 * @throws SQLException 
	 */
	public static Connection getConexion() throws SQLException 
	{
		//	Recommend connection pool implementation/usage
		return conexionutil.getConexion();
	}

	//****************** metodos del DAO *******************//
	public ParametrosDAO getParametrosDAO() {
		return new JdbcParametrosDAO();
	}
	

}
