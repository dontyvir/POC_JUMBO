package cl.bbr.jumbocl.usuarios.dao;

import java.util.List;

import cl.bbr.jumbocl.common.model.UsuariosEntity;
import cl.bbr.jumbocl.usuarios.dto.ProcModUserDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.jumbocl.usuarios.dto.UsuariosCriteriaDTO;
import cl.bbr.jumbocl.usuarios.exceptions.UsuariosDAOException;

/**
 * Permite las operaciones en base de datos sobre los Usuarios.
 * 
 * @author BBR
 *
 */
public interface UsuariosDAO {
	
	/**
	 * Obtiene usuario a partir de su Id de Usiario
	 * 
	 * @param  id
	 * @return UsuariosEntity
	 * @throws UsuariosDAOException
	 */
	public UsuariosEntity getUserById(long id) throws UsuariosDAOException;
	
	/**
	 * Obtiene id de usuario a partir del login
	 * 
	 * @param  login
	 * @return long
	 * @throws UsuariosDAOException
	 */
	public long getUserIdByLogin(String login) throws UsuariosDAOException;
	
	/**
	 * Obtiene listado de perfiles de un usuario
	 * 
	 * @param  id
	 * @return List PerfilesEntity
	 * @throws UsuariosDAOException
	 */
	public List getPerfilesByUserId(long id) throws UsuariosDAOException;
	
	/**
	 * Obtiele listado de usuarios de acuerdo a un criterio de búsqueda
	 * 
	 * @param  criterio
	 * @return List UsuariosEntity
	 * @throws UsuariosDAOException
	 */
	public List getUsersByCriteria(UsuariosCriteriaDTO criterio)throws UsuariosDAOException;
	
	/**
	 * Obtiene número de resultados que arroja la consulta por criterio
	 * 
	 * @param  criterio
	 * @return int
	 * @throws UsuariosDAOException
	 */
	public int getUsersCountByCriteria(UsuariosCriteriaDTO criterio)throws UsuariosDAOException;
	
	/**
	 * Modifica información del usuario
	 * 
	 * @param  usr
	 * @return boolean
	 * @throws UsuariosDAOException
	 */
	public boolean setModUser(UserDTO usr)throws UsuariosDAOException;
	
	/**
	 * Agrega un usuario a la tabla BO_USUARIOS
	 * 
	 * @param  usr
	 * @return long
	 * @throws UsuariosDAOException
	 */
	public long addUser(UserDTO usr)throws UsuariosDAOException;
	
	/**
	 * Obtiene listado de estados
	 * 
	 * @param  tip_estado
	 * @return List EstadoEntity
	 * @throws UsuariosDAOException
	 */
	public List getUsrEstados(String tip_estado) throws UsuariosDAOException;
	
	/**
	 * Obtiene listado de locales
	 * 
	 * @return List LocalEntity
	 * @throws UsuariosDAOException
	 */
	public List getUsrLocales() throws UsuariosDAOException;
	
	/**
	 * Obtiene el listado de perfiles
	 * 
	 * @return List PerfilesEntity
	 * @throws UsuariosDAOException
	 */
	public List getPerfiles() throws UsuariosDAOException;
	
	/**
	 * Obtiene listado de usuarios de un perfil en un local
	 * 
	 * @param  id_perfil
	 * @param  id_local
	 * @return List UserDTO
	 * @throws UsuariosDAOException
	 */
	public List getUsrByPerfilyLocal(long id_perfil, long id_local) throws UsuariosDAOException;
	
	/**
	 * Agrega la relación entre el usuario y el perfil
	 * 
	 * @param  id_user
	 * @param  id_perf
	 * @return boolean
	 * @throws UsuariosDAOException
	 */
	public boolean agregaPerfUser(long id_user, long id_perf) throws UsuariosDAOException;
	
	/**
	 * Elimina  la relación entre el usuario y el perfil
	 * 
	 * @param  id_user
	 * @param  id_perf
	 * @return boolean
	 * @throws UsuariosDAOException
	 */
	public boolean elimPerfUser(long id_user, long id_perf) throws UsuariosDAOException;
		
	/**
	 * Obtiene el listado de locales por usuario
	 * 
	 * @param  id_user
	 * @return List LocalDTO
	 * @throws UsuariosDAOException
	 */
	public List getLocalesByUserId(long id_user) throws UsuariosDAOException;
	
	/**
	 * Listado de locales
	 * 
	 * @return List LocalDTO
	 * @throws UsuariosDAOException
	 */
	public List getLocales() throws UsuariosDAOException;
	
	/**
	 * Agrega la relacion entre usuario y local
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws UsuariosDAOException
	 */
	public boolean addUserLoc(ProcModUserDTO dto) throws UsuariosDAOException;
	
	/**
	 * Elimina la relación entre usuario y local
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws UsuariosDAOException
	 */
	public boolean delUserLoc(ProcModUserDTO dto)  throws UsuariosDAOException;
	
}
