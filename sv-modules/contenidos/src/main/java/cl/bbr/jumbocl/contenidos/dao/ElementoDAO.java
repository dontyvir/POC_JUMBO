package cl.bbr.jumbocl.contenidos.dao;


import java.util.List;

import cl.bbr.jumbocl.common.model.ElementoEntity;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddElementoDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelElementoDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModElementoDTO;
import cl.bbr.jumbocl.contenidos.dto.ElementosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.ElementoDAOException;

/**
 * Permite las operaciones en base de datos sobre los elementos Web.
 * @author BBR
 *
 */
public interface ElementoDAO {

	/**
	 * Obtiene el elemento, segun el código
	 * 
	 * @param  id_elemento
	 * @return ElementoEntity
	 * @throws ElementoDAOException
	 */
	public ElementoEntity getElementoById(long id_elemento) throws ElementoDAOException;
	
	/**
	 * Obtener el listado de elementos, segun los criterios de busqueda
	 * 
	 * @param  criterio
	 * @return List ElementoEntity
	 * @throws ElementoDAOException
	 */
	public List getElementosByCriteria(ElementosCriteriaDTO criterio) throws ElementoDAOException;
	
	/**
	 * Obtiene la cantidad de elementos, segun el criterio
	 * 
	 * @param  criterio
	 * @return int
	 * @throws ElementoDAOException
	 */
	public int getCountElementosByCriteria(ElementosCriteriaDTO criterio) throws ElementoDAOException;
	
	/**
	 * Obtiene el listado de campanias de un elemento
	 * 
	 * @param  id_elemento
	 * @return List CampanaEntity
	 * @throws ElementoDAOException
	 */
	public List getCampanasByElementoId(long id_elemento) throws ElementoDAOException;

	/**
	 * Actualiza la información del elemento
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws ElementoDAOException
	 */
	public boolean setModElemento(ProcModElementoDTO dto) throws ElementoDAOException;
	
	/**
	 * Obtiene el listado de todos los tipos de elementos
	 * 
	 * @return List TipoElementoEntity
	 * @throws ElementoDAOException 
	 */
	public List getLstTipoElementos() throws ElementoDAOException;
	
	/**
	 * Agrega un nuevo elemento
	 * 
	 * @param  dto
	 * @return long
	 * @throws ElementoDAOException
	 */
	public long setAddElemento(ProcAddElementoDTO dto) throws ElementoDAOException;
	
	/**
	 * Elimina un elemento
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws ElementoDAOException
	 */
	public boolean setDelElemento(ProcDelElementoDTO dto) throws ElementoDAOException;
	
}
