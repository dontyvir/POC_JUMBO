package cl.bbr.jumbocl.parametros.dao;

import cl.bbr.jumbocl.parametros.dao.HibernateDAOFactory;
import cl.bbr.jumbocl.parametros.dao.JdbcDAOFactory;


/**
 * Permite definir la modalidad de conexion con la base de datos, Jdbc, Hibernate, etc.
 *  
 * @author BBR
 *
 */
public abstract class DAOFactory {
	
	/**
	 * Identifica que la conexión a usar es Hibernate.  
	 */
	public static final int HIBERNATE = 1;
	  
	/**
	 * Identifica que la conexión a usar es Jdbc.
	 */
	public static final int JDBC = 2;
	  
	/**
	 * Obtiene la modalidad de conexion a la base de datos.
	 * @param  whichFactory
	 * @return DAOFactory
	 */
	public static DAOFactory getDAOFactory(
		  int whichFactory) {
	  
		switch (whichFactory) {
		  case HIBERNATE: 
			  return new HibernateDAOFactory();
		  case JDBC   : 
		   	  return new JdbcDAOFactory();
		  default           : 
			  return null;
		}
	  }
    
	
	/**
	 * Obtiene la clase que realiza consultas de Parametros.
	 * 
	 * @return ParametrosDAO
	 */
	public abstract ParametrosDAO getParametrosDAO();

}
