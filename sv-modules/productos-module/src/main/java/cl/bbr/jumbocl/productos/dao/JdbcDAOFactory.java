package cl.bbr.jumbocl.productos.dao;

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

	private static cl.bbr.jumbocl.shared.conexion.ConexionUtil conexionutil = new ConexionUtil();
	
	public static Connection getConexion() throws SQLException 
	{
		return conexionutil.getConexion();
	}

	/**
	 * metodos del DAO 
	 * 
	 */
	public ProductosDAO getProductosDAO() {
	  // retorna el dao para jdb
	  return new JdbcProductosDAO();
	}

}
