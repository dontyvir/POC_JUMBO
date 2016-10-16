package cl.bbr.fo.faq.dao;

import java.sql.Connection;

import cl.bbr.conexion.ConexionUtil;

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
	public FaqDAO getFaqDAO() {
	  // retorna el dao para jdb
	  return new JdbcFaqDAO();
	}

}
