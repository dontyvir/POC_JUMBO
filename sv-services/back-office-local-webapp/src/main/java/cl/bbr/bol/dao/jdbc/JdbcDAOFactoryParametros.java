package cl.bbr.bol.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import cl.bbr.bol.dao.FaltantesDAO;
import cl.bbr.jumbocl.contenidos.dao.CampanaDAO;
import cl.bbr.jumbocl.contenidos.dao.CategoriasDAO;
import cl.bbr.jumbocl.contenidos.dao.CategoriasSapDAO;
import cl.bbr.jumbocl.contenidos.dao.DAOFactory;
import cl.bbr.jumbocl.contenidos.dao.ElementoDAO;
import cl.bbr.jumbocl.contenidos.dao.EstadosDAO;
import cl.bbr.jumbocl.contenidos.dao.ProductosDAO;
import cl.bbr.jumbocl.contenidos.dao.ProductosSapDAO;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.cencosud.jumbocl.umbrales.dao.UmbralDAO;
import cl.cencosud.jumbocl.umbrales.dao.jdbc.JdbcUmbral;

/**
 * Clase que se conecta a la base de datos mediante JDBC.
 * @author RMI - DNT
 *
 */
public class JdbcDAOFactoryParametros extends DAOFactory {

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

		public UmbralDAO getUmbralDAO() {
			return new JdbcUmbral();
		}
		
		public CategoriasDAO getCategoriasDAO() {
			// TODO Apéndice de método generado automáticamente
			return null;
		}

		public ProductosDAO getProductosDAO() {
			// TODO Apéndice de método generado automáticamente
			return null;
		}
		public ProductosSapDAO getProductosSapDAO() {
			// TODO Apéndice de método generado automáticamente
			return null;
		}
		public CategoriasSapDAO getCategoriasSapDAO() {
			// TODO Apéndice de método generado automáticamente
			return null;
		}
		public EstadosDAO getEstadosDAO() {
			// TODO Apéndice de método generado automáticamente
			return null;
		}

		public CampanaDAO getCampanaDAO() {
			// TODO Apéndice de método generado automáticamente
			return null;
		}


		public ElementoDAO getElementoDAO() {
			// TODO Apéndice de método generado automáticamente
			return null;
		}

		public FaltantesDAO getFaltantesDAO(){
			return new FaltantesJdbc();
		}
		

		
		
}
