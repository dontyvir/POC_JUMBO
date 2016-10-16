package cl.bbr.jumbocl.clientes.dao;

import cl.bbr.jumbocl.clientes.dao.ClientesDAO;

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
	 * 
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
	 * Obtiene la clase que realiza consultas de Clientes.
	 * 
	 * @return ClientesDAO
	 */
	public abstract ClientesDAO getClientesDAO();
	public abstract RegionesDAO getRegionesDAO();
	
}
