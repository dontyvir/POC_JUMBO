package cl.bbr.fo.marketing.dao;

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
	public MarketingDAO getMarketingDAO() {
	  // retorna el dao para jdb
	  return new JdbcMarketingDAO();
	}

}
