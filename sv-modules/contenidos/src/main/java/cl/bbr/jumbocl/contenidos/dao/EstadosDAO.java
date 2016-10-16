package cl.bbr.jumbocl.contenidos.dao;

import java.util.List;

import cl.bbr.jumbocl.contenidos.exceptions.EstadosDAOException;

/**
 * Permite las operaciones en base de datos sobre los Estados.
 * @author BBR
 *
 */
public interface EstadosDAO {

	/**
	 * Obtiene la lista de estados, segun el tipo de estado y si es visible en web o no
	 * 
	 * @param  tip_estado String
	 * @param  visible String
	 * @return List EstadoEntity
	 * @throws EstadosDAOException
	 */
	public List getEstados(String tip_estado,String visible) throws EstadosDAOException ;
}
