package cl.bbr.fo.faq.dao;

/**
 * Factory para crear la forma de recuperación de datos desde el repositorio. 
 *  
 * @author BBR e-commerce & retail
 *
 */
// Abstract class DAO Factory
public abstract class DAOFactory {

  // List of DAO types supported by the factory
  public static final int HIBERNATE = 1;
  public static final int JDBC = 2;
  
  public static DAOFactory getDAOFactory(
	  int whichFactory) {
  
	switch (whichFactory) {	  
	  case JDBC   : 
	   	  return new JdbcDAOFactory();
	  default           : 
		  return null;
	}
  }

  public abstract FaqDAO getFaqDAO();
  
}
