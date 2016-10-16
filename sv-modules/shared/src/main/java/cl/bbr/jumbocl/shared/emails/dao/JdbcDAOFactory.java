package cl.bbr.jumbocl.shared.emails.dao;

import java.sql.Connection;
import java.sql.SQLException;

import cl.bbr.jumbocl.shared.conexion.ConexionUtil;

/**
 * Factory para jdbc. 
 *  
 * @author BBR e-commerce & retail
 *
 */
public class JdbcDAOFactory extends DAOFactory {

	private static ConexionUtil conexionutil = new ConexionUtil();
	
	public static Connection getConexion() throws SQLException 
	{
		return conexionutil.getConexion();
	}

	/**
	 * metodos del DAO 
	 * 
	 */
	public EmailDAO getEmailDAO() {
	  // retorna el dao para jdb
	  return new JdbcEmailDAO();
	}

}
