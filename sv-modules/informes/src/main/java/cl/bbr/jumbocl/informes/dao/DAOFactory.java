package cl.bbr.jumbocl.informes.dao;


/**
 * Permite definir la modalidad de conexion con la base de datos, Jdbc, Hibernate, etc.
 *  
 * @author imoyano
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
	 * Obtiene la clase que realiza consultas de Informes.
	 * 
	 * @return InformesDAO
	 */
	public abstract InformesDAO getInformesDAO();
	
	


    
}
