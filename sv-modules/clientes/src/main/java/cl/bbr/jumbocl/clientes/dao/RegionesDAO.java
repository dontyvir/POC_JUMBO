package cl.bbr.jumbocl.clientes.dao;


import java.util.List;

import cl.bbr.jumbocl.clientes.exceptions.RegionesDAOException;


/**
 * Interfaz para implementaci�n de m�todos en DAO para diferentes tipos de conexi�n a repositorios. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public interface RegionesDAO {

	/**
	 * Recupera la lista de regiones y retorna como una lista de DTO (RegionesDTO).
	 * 
	 * @return				Lista de DTO (RegionesDTO)
	 * @throws RegionesDAOException
	 */
	public List getRegiones() throws RegionesDAOException;
	
	/**
	 * Recupera la lista de comunas por region y retorna como una lista de DTO (ComunasDTO).
	 * 
	 * @param reg_id	identificador �nico de la region 
	 * @return Lista de DTO (ComunasDTO)
	 * @throws RegionesDAOException
	 */	
	public List getComunas( long reg_id ) throws RegionesDAOException;

}
