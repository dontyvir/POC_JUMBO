package cl.bbr.jumbocl.clientes.dao;

import java.sql.Connection;
import java.sql.SQLException;

import cl.bbr.jumbocl.clientes.dao.ClientesDAO;
import cl.bbr.jumbocl.clientes.dao.JdbcClientesDAO;
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
	 * Obtiene la conexión JDBC con la base de datos.
	 * @return Connection
	 * @throws Exception 
	 */
	public static Connection getConexion() throws SQLException 
		{
			return conexionutil.getConexion();
		}

		/**
		 * metodos del DAO 
		 * 
		 */
		public ClientesDAO getClientesDAO() {
		  // retorna el dao para jdb
		  return new JdbcClientesDAO();
		}
		
		public RegionesDAO getRegionesDAO() {
			  // retorna el dao para jdb
			  return new JdbcRegionesDAO();
			}

		
}
