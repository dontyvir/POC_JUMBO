package cl.jumbo.ventaondemand.dao.factory;

import cl.jumbo.ventaondemand.dao.ClientesDAO;

/**
 * Permite definir la modalidad de conexion con la base de datos, Jdbc, Hibernate, etc.
 *
 */
public abstract class DAOFactory {

	public static final int JDBC = 2;
	  
	/**
	 * Obtiene la modalidad de conexion a la base de datos.
	 * 
	 * @param  whichFactory
	 * @return DAOFactory
	 */
	public static DAOFactory getDAOFactory(int whichFactory) {	  
		switch (whichFactory) {
			case JDBC: 
				return new DAOManager();
			default: 
				return null;
		}
	}

	/**
	 * Obtiene la clase que realiza consultas de Clientes.
	 * 
	 * @return ClientesDAO
	 */
	public abstract ClientesDAO getClientesDAO();	
}
