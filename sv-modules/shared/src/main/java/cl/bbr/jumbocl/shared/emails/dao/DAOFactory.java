package cl.bbr.jumbocl.shared.emails.dao;

import cl.bbr.jumbocl.shared.emails.dao.JdbcDAOFactory;

/**
 * Factory para crear la forma de recuperación de datos desde el repositorio. 
 *  
 * @author BBR e-commerce & retail
 *
 */
public abstract class DAOFactory {

  // List of DAO types supported by the factory
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

  public abstract EmailDAO getEmailDAO();
  
}
