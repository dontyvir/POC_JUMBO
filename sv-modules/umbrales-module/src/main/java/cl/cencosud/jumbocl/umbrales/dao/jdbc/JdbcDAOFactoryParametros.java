package cl.cencosud.jumbocl.umbrales.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import cl.cencosud.jumbocl.umbrales.dao.UmbralDAO;
import cl.bbr.jumbocl.contenidos.dao.CampanaDAO;
import cl.bbr.jumbocl.contenidos.dao.CategoriasDAO;
import cl.bbr.jumbocl.contenidos.dao.CategoriasSapDAO;
import cl.bbr.jumbocl.contenidos.dao.DAOFactory;
import cl.bbr.jumbocl.contenidos.dao.ElementoDAO;
import cl.bbr.jumbocl.contenidos.dao.EstadosDAO;
import cl.bbr.jumbocl.contenidos.dao.ProductosDAO;
import cl.bbr.jumbocl.contenidos.dao.ProductosSapDAO;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;

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
		 * Obtiene la conexi�n JDBC con la base de datos.
		 * @return Connection
		 * @throws SQLException 
		 */
		public static Connection getConexion() throws SQLException 
		{
			return conexionutil.getConexion();
		}

		/**
		 * M�todos del DAO 
		 * 
		 */

		public UmbralDAO getUmbralDAO() {
			return new JdbcUmbral();
		}
		
		public CategoriasDAO getCategoriasDAO() {
			// TODO Ap�ndice de m�todo generado autom�ticamente
			return null;
		}

		public ProductosDAO getProductosDAO() {
			// TODO Ap�ndice de m�todo generado autom�ticamente
			return null;
		}
		public ProductosSapDAO getProductosSapDAO() {
			// TODO Ap�ndice de m�todo generado autom�ticamente
			return null;
		}
		public CategoriasSapDAO getCategoriasSapDAO() {
			// TODO Ap�ndice de m�todo generado autom�ticamente
			return null;
		}
		public EstadosDAO getEstadosDAO() {
			// TODO Ap�ndice de m�todo generado autom�ticamente
			return null;
		}

		public CampanaDAO getCampanaDAO() {
			// TODO Ap�ndice de m�todo generado autom�ticamente
			return null;
		}


		public ElementoDAO getElementoDAO() {
			// TODO Ap�ndice de m�todo generado autom�ticamente
			return null;
		}

		

		
		
}
