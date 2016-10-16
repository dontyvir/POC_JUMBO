package cl.bbr.jumbocl.usuarios.dao;

import java.util.List;

import cl.bbr.jumbocl.common.model.ComandosEntity;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.usuarios.dto.PerfilDTO;
import cl.bbr.jumbocl.usuarios.dto.PerfilesCriteriaDTO;
import cl.bbr.jumbocl.usuarios.exceptions.PerfilesDAOException;

/**
 * Permite las operaciones en base de datos sobre los Perfiles.
 * 
 * @author BBR
 *
 */
public interface PerfilesDAO {
	
	/**
	 * Obtiene información del perfil
	 * 
	 * @param  id
	 * @return PerfilesEntity
	 * @throws PerfilesDAOException
	 */
	public PerfilesEntity getPerfilById(long id) throws PerfilesDAOException;
	
	/**
	 * Obtiene listado de perfiles
	 * 
	 * @param  criterio
	 * @return List PerfilesEntity
	 * @throws PerfilesDAOException
	 */
	public List getPerfilesAll(PerfilesCriteriaDTO criterio) throws PerfilesDAOException;
	
	/**
	 * Obtiene número total de perfiles
	 * 
	 * @param  criterio
	 * @return int
	 * @throws PerfilesDAOException
	 */
	public int getPerfilesAllCount(PerfilesCriteriaDTO criterio)throws PerfilesDAOException;
	
	/**
	 * Obtiene listado de comandos para un perfil
	 * 
	 * @param  id
	 * @return List ComandosEntity
	 * @throws PerfilesDAOException
	 */
	public List getComandos(long id) throws PerfilesDAOException;
	
	/**
	 * Obtiene listado de todos los comandos
	 * 
	 * @param  id
	 * @return List ComandosEntity
	 * @throws PerfilesDAOException
	 */
	public List getComandosAll(long id) throws PerfilesDAOException;
	
	/**
	 * Obtiene registro de la tabla BO_COMANDOS a partir del nombre del comando
	 * 
	 * @param  comando
	 * @return ComandosEntity
	 * @throws PerfilesDAOException
	 */
	public ComandosEntity getComandoByName(String comando)  throws PerfilesDAOException;
	
	
	/**
	 * Actualiza información del perfil
	 * 
	 * @param  perf
	 * @return boolean
	 * @throws PerfilesDAOException
	 */
	public boolean setModPerfil(PerfilDTO perf)throws PerfilesDAOException;
	
	/**
	 * Agrega un nuevo perfil
	 * 
	 * @param  perf
	 * @return boolean
	 * @throws PerfilesDAOException
	 */
	public boolean addPerfil(PerfilDTO perf)throws PerfilesDAOException;
	
	/**
	 * Elimina un perfil
	 * 
	 * @param  perf
	 * @return boolean
	 * @throws PerfilesDAOException
	 */
	public boolean delPerfil(PerfilDTO perf)throws PerfilesDAOException;
	
	/**
	 * Asocia comando a un perfil
	 * 
	 * @param  id_cmd
	 * @param  id_perf
	 * @return boolean
	 * @throws PerfilesDAOException
	 */
	public boolean agregaCmdPerf(long id_cmd, long id_perf) throws PerfilesDAOException;
	
	/**
	 * Desasocia comando a un perfil
	 * 
	 * @param  id_cmd
	 * @param  id_perf
	 * @return boolean
	 * @throws PerfilesDAOException
	 */
	public boolean elimCmdPerf(long id_cmd, long id_perf) throws PerfilesDAOException;
	
	/**
	 * Elimina comandos a un perfil
	 * 
	 * @param  id_perf
	 * @return boolean
	 * @throws PerfilesDAOException
	 */
	public boolean elimCmdsByIdPerf(long id_perf) throws PerfilesDAOException;
	
	/**
	 * Consulta si un perfil tiene permiso para ejecutar un comando
	 * 
	 * @param in_id_perfiles
	 * @param id_comando
	 * @return boolean
	 * @throws PerfilesDAOException
	 */
	public boolean doCheckPermisoPerfilComando(String in_id_perfiles, long id_comando) throws PerfilesDAOException;
	
	
}
