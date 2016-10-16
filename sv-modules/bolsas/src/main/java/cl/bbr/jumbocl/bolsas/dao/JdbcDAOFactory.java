package cl.bbr.jumbocl.bolsas.dao;

import java.sql.Connection;
import java.sql.SQLException;

import cl.bbr.jumbocl.contenidos.dao.JdbcProductosDAO;
import cl.bbr.jumbocl.contenidos.dao.ProductosDAO;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;

/**
 * Clase que se conecta a la base de datos mediante JDBC.
 * @author imoyano
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
		//	Recommend connection pool implementation/usage
		return conexionutil.getConexion();
	}

	//****************** metodos del DAO *******************//


	/* (sin Javadoc)
	 * @see cl.bbr.jumbocl.bolsas.dao.DAOFactory#getBolsasDAO()
	 */
	public BolsasDAO getBolsasDAO() {
		return new JdbcBolsasDAO();
	}

	/* (sin Javadoc)
	 * @see cl.bbr.jumbocl.bolsas.dao.DAOFactory#getProductosDAO()
	 */
	public ProductosDAO getProductosDAO() {
		return new JdbcProductosDAO();
	}

	
}
