package cl.bbr.jumbocl.parametros.dao;

import java.util.List;

import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.parametros.dto.ParametroFoDTO;
import cl.bbr.jumbocl.parametros.exceptions.ParametrosDAOException;


/**
 * Permite las operaciones en base de datos sobre los Pedidos.
 * 
 * @author BBR
 *
 */
public interface ParametrosDAO { 

	
	/**
	 * Obtiene Listado de parametros
	 * 
	 * @return List ParametroDTO
	 * @throws ParametrosDAOException 
	 * 
	 */
	public List getParametros() throws ParametrosDAOException;
	
	
	/**
	 * Obtiene un Parametro por su Nombre
	 * 
	 * @return ParametroDTO
	 * @throws ParametrosDAOException 
	 * 
	 */
	public ParametroDTO getParametroByName(String Name) throws ParametrosDAOException;
	
	
	/**
	 * Obtiene un Parametro por su ID
	 * 
	 * @return ParametroDTO
	 * @throws ParametrosDAOException 
	 * 
	 */
	public ParametroDTO getParametroById(int Id) throws ParametrosDAOException;
	
	
	/**
	 * Obtiene un Parametro por su llave
	 * 
	 * @return ParametroFoDTO
	 * @throws ParametrosDAOException 
	 * 
	 */
	public ParametroFoDTO getParametroFoByKey(String key) throws ParametrosDAOException;

	
	 public ParametroFoDTO getParametroByKey(String key) throws ParametrosDAOException;
}
