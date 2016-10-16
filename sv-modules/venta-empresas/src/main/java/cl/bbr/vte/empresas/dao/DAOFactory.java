package cl.bbr.vte.empresas.dao;


/**
 * Factory para crear la forma de recuperación de datos desde el repositorio. 
 *  
 * @author BBR e-commerce & retail
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
	 * Obtiene la clase que realiza consultas de Empresas.
	 * 
	 * @return EmpresasDAO 
	 */
	public abstract EmpresasDAO getEmpresasDAO();
	
	public abstract SucursalesDAO getSucursalesDAO();
	
	public abstract CompradoresDAO getCompradoresDAO();
	
	public abstract DireccionesDAO getDireccionesDAO();
	
	public abstract DirFacturacionDAO getDirFacturacionDAO();

}
