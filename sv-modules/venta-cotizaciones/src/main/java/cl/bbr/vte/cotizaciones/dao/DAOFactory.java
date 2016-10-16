package cl.bbr.vte.cotizaciones.dao;

import cl.bbr.vte.empresas.dao.EmpresasDAO;


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
	  case HIBERNATE: 
		  return new HibernateDAOFactory();
	  case JDBC   : 
	   	  return new JdbcDAOFactory();
	  default           : 
		  return null;
	}
  }

	/**
	 * Obtiene la clase que realiza consultas de las Cotizaciones
	 * 
	 * @return CotizacionesDAO 
	 */  
  public abstract CotizacionesDAO getCotizacionesDAO();

	/**
	 * Obtiene la clase que realiza consultas de las Empresas
	 * 
	 * @return EmpresasDAO 
	 */  
  public abstract EmpresasDAO getEmpresasDAO();
  
}
