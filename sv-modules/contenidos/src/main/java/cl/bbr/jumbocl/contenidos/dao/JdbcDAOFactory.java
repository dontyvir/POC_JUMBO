package cl.bbr.jumbocl.contenidos.dao;

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
		 * Obtiene la conexión JDBC con la base de datos.
		 * @return Connection
		 * @throws SQLException 
		 */
		public static Connection getConexion() throws SQLException 
		{
			return conexionutil.getConexion();
		}

		/**
		 * Métodos del DAO 
		 * 
		 */

		public CategoriasDAO getCategoriasDAO() {
			return new JdbcCategoriasDAO();
		}

		public ProductosDAO getProductosDAO() {
			return new JdbcProductosDAO();
		}

		public ProductosSapDAO getProductosSapDAO() {
			return new JdbcProductosSapDAO();
		}

		public CategoriasSapDAO getCategoriasSapDAO() {
			return new JdbcCategoriasSapDAO();
		}

		public EstadosDAO getEstadosDAO() {
			return new JdbcEstadosDAO();
		}
		
		public CampanaDAO getCampanaDAO() {
			return new JdbcCampanaDAO();
		}
		
		public ElementoDAO getElementoDAO() {
			return new JdbcElementoDAO();
		}
		
}
