package cl.bbr.jumbocl.usuarios.dao;

import java.sql.Connection;
import java.sql.SQLException;

import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.usuarios.dao.JdbcPerfilesDAO;
import cl.bbr.jumbocl.usuarios.dao.JdbcUsuariosDAO;
import cl.bbr.jumbocl.usuarios.dao.PerfilesDAO;
import cl.bbr.jumbocl.usuarios.dao.UsuariosDAO;

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
	 * Obtiene la conexión JDBC con la base de datos.
	 * @return Connection
	 * @throws SQLException 
	 */
	public static Connection getConexion() throws SQLException 
		{
			return conexionutil.getConexion();
		}

		
		// *********** metodos del DAO ************* // 

		public UsuariosDAO getUsuariosDAO() throws SQLException {
			return new JdbcUsuariosDAO();
		}		
		
		public PerfilesDAO getPerfilesDAO() {
			return new JdbcPerfilesDAO();
		}

		
}
