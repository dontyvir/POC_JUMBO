package cl.bbr.jumbocl.contenidos.dao;


import java.util.List;

import cl.bbr.jumbocl.common.model.CampanaEntity;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCampanaDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelCampanaDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModCampElementoDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModCampanaDTO;
import cl.bbr.jumbocl.contenidos.dto.CampanasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.CampanaDAOException;

/**
 * Permite las operaciones en base de datos sobre las campañas.
 * @author BBR
 *
 */
public interface CampanaDAO {

	/**
	 * Obtiene la campania, segun el código
	 * 
	 * @param  id_campana
	 * @return CampanaEntity
	 * @throws CampanaDAOException
	 */
	public CampanaEntity getCampanaById(long id_campana) throws CampanaDAOException;
	
	/**
	 * Obtener el listado de campanias, segun los criterios de busqueda
	 * 
	 * @param  criterio
	 * @return List CampanaEntity
	 * @throws CampanaDAOException
	 */
	public List getCampanasByCriteria(CampanasCriteriaDTO criterio) throws CampanaDAOException;
	
	/**
	 * Obtiene la cantidad de campanias, segun el criterio
	 * 
	 * @param  criterio
	 * @return int
	 * @throws CampanaDAOException
	 */
	public int getCountCampanasByCriteria(CampanasCriteriaDTO criterio) throws CampanaDAOException;
	
	/**
	 * Obtiene el listado de elementos de una campania
	 * 
	 * @param  id_campana
	 * @return List ElementoEntity
	 * @throws CampanaDAOException
	 */
	public List getElementosByCampanaId(long id_campana) throws CampanaDAOException;
	
	/**
	 * Actualiza informacion de campania
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CampanaDAOException
	 */
	public boolean setModCampana(ProcModCampanaDTO dto) throws CampanaDAOException;
	
	/**
	 * Permite agregar la relacion entre campania y elemento
	 *  
	 * @param  dto
	 * @return boolean
	 * @throws CampanaDAOException
	 */
	public boolean addCampanaElemento(ProcModCampElementoDTO dto) throws CampanaDAOException;
	
	/**
	 * Permite eliminar la relacion entre campania y elemento
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CampanaDAOException
	 */
	public boolean delCampanaElemento(ProcModCampElementoDTO dto) throws CampanaDAOException;
	
	/**
	 * Agrega una nueva campaña
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CampanaDAOException
	 */
	public boolean setAddCampana(ProcAddCampanaDTO dto) throws CampanaDAOException;
	
	/**
	 * Elimina la campaña
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CampanaDAOException
	 */
	public boolean setDelCampana(ProcDelCampanaDTO dto) throws CampanaDAOException;
	
	/**
	 * Modificar relacion entre campaña y elemento
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CampanaDAOException
	 */
	public boolean modCampanaElemento(ProcModCampElementoDTO dto) throws CampanaDAOException;
}
