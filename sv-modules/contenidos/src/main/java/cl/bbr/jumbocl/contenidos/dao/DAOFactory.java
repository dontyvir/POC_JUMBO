package cl.bbr.jumbocl.contenidos.dao;

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
	 * Obtiene la clase que realiza consultas de Categorias Web.
	 * 
	 * @return CategoriasDAO
	 */
	public abstract CategoriasDAO getCategoriasDAO();
	
	/**
	 * Obtiene la clase que realiza consultas de Productos Web.
	 * 
	 * @return ProductosDAO
	 */ 
	public abstract ProductosDAO getProductosDAO();
	
	/**
	 * Obtiene la clase que realiza consultas de Productos Sap.
	 * 
	 * @return ProductosSapDAO
	 */
	public abstract ProductosSapDAO getProductosSapDAO();
	
	/**
	 * Obtiene la clase que realiza consultas de Categorias Sap.
	 * 
	 * @return CategoriasSapDAO
	 */
	public abstract CategoriasSapDAO getCategoriasSapDAO();
	
	/**
	 * Obtiene la clase que realiza consultas de Estados.
	 * 
	 * @return EstadosDAO
	 */
	public abstract EstadosDAO getEstadosDAO();

	/**
	 * Obtiene la clase que realiza consultas de Campanas.
	 * 
	 * @return CampanaDAO
	 */
	public abstract CampanaDAO getCampanaDAO();
	
	/**
	 * Obtiene la clase que realiza consultas de Elementos.
	 * 
	 * @return
	 */
	public abstract ElementoDAO getElementoDAO();

}
