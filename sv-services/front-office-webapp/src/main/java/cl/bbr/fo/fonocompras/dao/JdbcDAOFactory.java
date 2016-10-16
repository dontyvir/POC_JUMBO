package cl.bbr.fo.fonocompras.dao;

import java.sql.Connection;

import cl.bbr.conexion.ConexionUtil;

/**
 * Factory para jdbc. 
 *  
 * @author BBR e-commerce & retail
 *
 */
public class JdbcDAOFactory extends DAOFactory {

	private static ConexionUtil conexionutil = new ConexionUtil();
	
	public static Connection getConexion() 
	{
		return conexionutil.getConexion();
	}

	/**
	 * metodos del DAO 
	 * 
	 */
	public FonoComprasDAO getFonoComprasDAO() {
	  // retorna el dao para jdb
	  return new JdbcFonoComprasDAO();
	}

}
