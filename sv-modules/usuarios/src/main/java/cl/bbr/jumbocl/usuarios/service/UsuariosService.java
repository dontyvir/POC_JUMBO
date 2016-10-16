package cl.bbr.jumbocl.usuarios.service;

import java.util.List;

import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.usuarios.ctrl.PerfilCtrl;
import cl.bbr.jumbocl.usuarios.ctrl.UserCtrl;
import cl.bbr.jumbocl.usuarios.dto.ComandoDTO;
import cl.bbr.jumbocl.usuarios.dto.PerfilDTO;
import cl.bbr.jumbocl.usuarios.dto.PerfilesCriteriaDTO;
import cl.bbr.jumbocl.usuarios.dto.ProcModPerfilDTO;
import cl.bbr.jumbocl.usuarios.dto.ProcModUserDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.jumbocl.usuarios.dto.UsuariosCriteriaDTO;
import cl.bbr.jumbocl.usuarios.exceptions.PerfilesException;
import cl.bbr.jumbocl.usuarios.exceptions.UsuariosException;

/**
 * Permite manejar información y operaciones sobre Usuarios, Perfiles, Comandos y sus relaciones.
 *  
 * @author BBR
 *
 */
public class UsuariosService {

	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

	/**
	 * Agrega un nuevo perfil.
	 * 
	 * @param  dto
	 * @return boolean, devuelve <i>true</i> si se agregó con exito, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 */
	public boolean addPerfil(PerfilDTO dto)throws ServiceException {
		PerfilCtrl perf = new PerfilCtrl();
		boolean result = false;
		try{
			result = perf.addPerfil(dto);
		}catch(PerfilesException ex){
			logger.debug("Problemas con controles de Perfiles");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/**
	 * Agrega nuevo usuario
	 * 
	 * @param  usr
	 * @return id_usuario
	 * @throws ServiceException
	 * @throws SystemException 
	 */
	public long addUser(UserDTO usr) throws ServiceException, SystemException {
		UserCtrl users = new UserCtrl();
			try {
				return users.addUser(usr);
			} catch (UsuariosException e) {
				throw new ServiceException(e.getMessage());
			}
	}

	/**
	 * Agrega la relación entre un comando y un perfil.
	 * 
	 * @param  id_cmd
	 * @param  id_perf
	 * @return boolean, devuelve <i>true</i> si se agregó con exito, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 */
	public boolean agregaCmdPerf(long id_cmd, long id_perf)throws ServiceException {
		PerfilCtrl perf = new PerfilCtrl();
		boolean result = false;
		try{
			result = perf.agregaCmdPerf(id_cmd, id_perf);
		}catch(PerfilesException ex){
			logger.debug("Problemas con controles de Perfiles");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/**
	 * Agrega la relación entre el usuario y el perfil a asignar.
	 * 
	 * @param  id_user
	 * @param  id_perf
	 * @return boolean, devuelve <i>true</i> si se agregó con exito, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 */
	public boolean agregaPerfUser(long id_user, long id_perf) throws ServiceException {
		UserCtrl users = new UserCtrl();
		boolean result = false;
		try{
			result = users.agregaPerfUser(id_user, id_perf);
		}catch(UsuariosException ex){
			logger.debug("Problemas con controles de usuarios");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/**
	 * Modifica información de un perfil
	 * 
	 * @param  dto
	 * @return boolean, devuelve <i>true</i> si se modificó con exito, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 */
	public boolean setModPerfil(PerfilDTO dto)throws ServiceException {
		PerfilCtrl perf = new PerfilCtrl();
		boolean result = false;
		try{
			result = perf.setModPerfil(dto);
		}catch(PerfilesException ex){
			logger.debug("Problemas con controles de Perfiles");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/**
	 * Modifica información del usuario
	 * 
	 * @param  usr
	 * @return boolean, devuelve <i>true</i> si se modificó con exito, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setModUser(UserDTO usr)
		throws ServiceException, SystemException {
		
		UserCtrl users = new UserCtrl();

		try{
			return users.setModUser(usr);
		}catch(UsuariosException ex){
			logger.debug("Problemas con controles de usuarios");
			throw new ServiceException(ex);
		}

	}

	/**
	 * Elimina la relación entre el usuario y el perfil.
	 * 
	 * @param  id_user
	 * @param  id_perf
	 * @return boolean, devuelve <i>true</i> si se eliminó con exito, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 */
	public boolean elimPerfUser(long id_user, long id_perf) throws ServiceException {
		UserCtrl users = new UserCtrl();
		boolean result = false;
		try{
			result = users.elimPerfUser(id_user, id_perf);
		}catch(UsuariosException ex){
			logger.debug("Problemas con controles de usuarios");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/**
	 * Autentica usuario en el sistema
	 * 
	 * @param login
	 * @param password
	 * @throws SystemException 
	 * @throws ServiceException 
	 *  
	 */
	public boolean doAutenticaUser(String login, String password)
		throws SystemException, ServiceException{	
		
		UserCtrl users = new UserCtrl();
	
		try{
			return users.doAutenticaUser(login, password);
		}catch(UsuariosException ex){
			logger.debug("Problemas con controles de usuarios");
			throw new ServiceException(ex);
		}
	
	}

	/*
	 * ----------------------- Perfiles -----------------------
	 */
	
	/**
	 * Consulta si un perfil tiene permiso para ejecutar un comando
	 * 
	 * @param  usr
	 * @param  id_comando
	 * @return boolean, devuelve <i>true</i> si tiene permiso, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 */
	public boolean doCheckPermisoPerfilComando(UserDTO usr, long id_comando)
		throws ServiceException {
		
		PerfilCtrl ctrl = new PerfilCtrl();
		try{
			return ctrl.doCheckPermisoPerfilComando(usr, id_comando);
		}catch(PerfilesException e){
			logger.debug("Problemas con controles de Perfiles");
			e.printStackTrace();
			throw new ServiceException(e);
		}
	
	}

	/**
	 * Retorna información de un perfil
	 * 
	 * @param  id_perf
	 * @return PerfilDTO
	 * @throws ServiceException
	 */
	public PerfilDTO getPerfilById(long id_perf) throws ServiceException {
		PerfilCtrl perf = new PerfilCtrl();
		PerfilDTO result = null;
		try{
			result = perf.getPerfilById(id_perf);
		}catch(PerfilesException ex){
			logger.debug("Problemas con controles de Perfiles");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/**
	 * Obtiene el listado de los perfiles.
	 * 
	 * @return List PerfilDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.usuarios.dto.PerfilDTO
	 */
	public List getPerfiles() throws ServiceException {
		UserCtrl users = new UserCtrl();
		List result = null;
		try{
			result = users.getPerfiles();
		}catch(UsuariosException ex){
			logger.debug("Problemas con controles de usuarios");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/**
	 * Obtiene el listados de los perfiles, segun el criterio de busqueda.
	 * 
	 * @param  criterio
	 * @return List PerfilDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.usuarios.dto.PerfilDTO
	 */
	public List getPerfilesAll(PerfilesCriteriaDTO criterio)throws ServiceException {
		PerfilCtrl perf = new PerfilCtrl();
		List result = null;
		try{
			result = perf.getPerfilesAll(criterio);
		}catch(PerfilesException ex){
			logger.debug("Problemas con controles de Perfiles");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/**
	 * Obtiene la cantidad de perfiles, segun el criterio de busqueda.
	 * 
	 * @param  criterio
	 * @return int
	 * @throws ServiceException
	 */
	public int getPerfilesAllCount(PerfilesCriteriaDTO criterio)throws ServiceException {
		PerfilCtrl perf = new PerfilCtrl();
		int result = 0;
		try{
			result = perf.getPerfilesAllCount(criterio);
		}catch(PerfilesException ex){
			logger.debug("Problemas con controles de Perfiles");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/*
	 * ----------------------- Usuarios -----------------------
	 */
	/**
	 * Retorna información de un usuario
	 * 
	 * @param  id_user
	 * @return UserDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public UserDTO getUserById(long id_user) throws ServiceException, SystemException {
		UserCtrl users = new UserCtrl();
		UserDTO result = null;
		try{
			result = users.getUserById(id_user);
		}catch(UsuariosException ex){
			logger.debug("Problemas con controles de usuarios");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/**
	 * Obtiene información del cliente a partir de su login
	 * 
	 * @param  login 
	 * @return UserDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public UserDTO getUserByLogin(String login)
	 	throws ServiceException, SystemException {
		
		UserCtrl users = new UserCtrl();
		UserDTO result = null;
		try{
			result = users.getUserByLogin(login);
		}catch(UsuariosException ex){
			logger.debug("Problemas con controles de usuarios");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/**
	 * Retorna lista de usuarios, segun criterios de busqueda
	 * 
	 * @param  criterio
	 * @return List UserDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.usuarios.dto.UserDTO
	 */
	public List getUsersByCriteria(UsuariosCriteriaDTO criterio) throws ServiceException {
		UserCtrl users = new UserCtrl();
		List result = null;
		try{
			result = users.getUsersByCriteria(criterio);
		}catch(UsuariosException ex){
			logger.debug("Problemas con controles de usuarios");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/**
	 * Retorna cantidad de usuarios, segun criterios de busqueda
	 * 
	 * @param  criterio
	 * @return int
	 * @throws ServiceException
	 */
	public int getUsersCountByCriteria(UsuariosCriteriaDTO criterio) throws ServiceException {
		UserCtrl users = new UserCtrl();
		int result = 0;
		try{
			result = users.getUsersCountByCriteria(criterio);
		}catch(UsuariosException ex){
			logger.debug("Problemas con controles de usuarios");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/**
	 * Obtiene el listados de los estados del usuario
	 * 
	 * @param  tipo
	 * @return List EstadoDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.usuarios.dto.EstadoDTO
	 */
	public List getUsrEstados(String tipo) throws ServiceException {
		UserCtrl users = new UserCtrl();
		List result = null;
		try{
			result = users.getUsrEstados(tipo);
		}catch(UsuariosException ex){
			logger.debug("Problemas con controles de usuarios");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/**
	 * Obtiene el listado de locales
	 * 
	 * @return List LocalDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.usuarios.dto.LocalDTO
	 */
	public List getUsrLocales() throws ServiceException {
		UserCtrl users = new UserCtrl();
		List result = null;
		try{
			result = users.getUsrLocales();
		}catch(UsuariosException ex){
			logger.debug("Problemas con controles de usuarios");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/**
	 * Elimina la relación entre un comando y un perfil.
	 * 
	 * @param id_cmd
	 * @param id_perf
	 * @return boolean, devuelve <i>true</i> si se eliminó con exito, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 */
	public boolean elimCmdPerf(long id_cmd, long id_perf)throws ServiceException {
		PerfilCtrl perf = new PerfilCtrl();
		boolean result = false;
		try{
			result = perf.elimCmdPerf(id_cmd, id_perf);
		}catch(PerfilesException ex){
			logger.debug("Problemas con controles de Perfiles");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/**
	 * Elimina todos los comandos relacionados a un perfil.
	 * 
	 * @param  id_perf
	 * @return boolean, devuelve <i>true</i> si se eliminó con exito, caso contrario devuelve <i>false</i>
	 * @throws ServiceException
	 */
	public boolean elimCmdsByIdPerf(long id_perf)throws ServiceException {
		PerfilCtrl perf = new PerfilCtrl();
		boolean result = false;
		try{
			result = perf.elimCmdsByIdPerf(id_perf);
		}catch(PerfilesException ex){
			logger.debug("Problemas con controles de Perfiles");
			throw new ServiceException(ex);
		}
		return result; 
	}

	/**
	 * Obtiene información de un Comando
	 * 
	 * @param  comando
	 * @return ComandoDTO
	 * @throws ServiceException
	 */
	public ComandoDTO getComandoByName(String comando)
		throws ServiceException {
		
		PerfilCtrl ctrl = new PerfilCtrl();
		try{
			return ctrl.getComandoByName(comando);
		}catch(PerfilesException ex){
			logger.debug("Problemas con controles de Perfiles");
			throw new ServiceException(ex);
		}
	
	}

	/**
	 * Obtiene el listado de los comandos
	 * 
	 * @param  id
	 * @return List ComandosEntity
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.common.model.ComandosEntity
	 */
	public List getComandosAll(long id)throws ServiceException {
		PerfilCtrl perf = new PerfilCtrl();
		List result = null;
		try{
			result = perf.getComandosAll(id);
		}catch(PerfilesException ex){
			logger.debug("Problemas con controles de Perfiles");
			throw new ServiceException(ex);
		}
		return result; 
	}	
	
	
	/**
	 * Obtiene listado de usuarios de un perfil en un local
	 * 
	 * @param id_perfil
	 * @param id_local 
	 * @return List UserDTO
	 * @throws ServiceException, SystemException 
	 * @see    cl.bbr.jumbocl.usuarios.dto.UserDTO
	 */
	public List getUsrByPerfilyLocal(long id_perfil, long id_local)
		throws ServiceException, SystemException {
		
		UserCtrl users = new UserCtrl();

		try{
			return users.getUsrByPerfilyLocal(id_perfil,id_local);
		}catch(UsuariosException ex){
			logger.debug("Problemas con controles de usuarios");
			throw new ServiceException(ex);
		}
	}	
	
	/**
	 * Valida si el usuario tiene el perfil y pertenece al local indicado
	 * 
	 * @param id_usuario
	 * @param id_perfil
	 * @param id_local
	 * @return boolean
	 * @throws ServiceException, SystemException 
	 * @see    cl.bbr.jumbocl.usuarios.dto.UserDTO
	 */
	public boolean ValidaUserByPerfilByLocal(long id_usuario, long id_perfil, long id_local)
		throws ServiceException, SystemException {
		
		UserCtrl users = new UserCtrl();

		try{
			return users.ValidaUserByPerfilByLocal(id_usuario, id_perfil, id_local);
		}catch(UsuariosException ex){
			logger.debug("Problemas con controles de usuarios");
			throw new ServiceException(ex);
		}
	}	
	

	
	/**
	 * Transacción que actualiza datos del perfil y 
	 * el listado de comandos relacionados con un perfil.
	 * 
	 * @param  dto ProcModPerfilDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void doModComandosAlPerfil(ProcModPerfilDTO dto)
		throws ServiceException, SystemException {
		
		PerfilCtrl ctrl = new PerfilCtrl();

		try{
			ctrl.doModComandosPerfil(dto);
		}catch(PerfilesException ex){
			throw new ServiceException(ex);
		}

	}
	
	/**
	 * Listado de locales
	 * 
	 * @return List LocalDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getLocales() throws ServiceException, SystemException {
		
		UserCtrl users = new UserCtrl();

		try{
			return users.getLocales();
		}catch(UsuariosException ex){
			logger.debug("Problemas con controles de usuarios");
			throw new ServiceException(ex);
		}
	}
	
	/**
	 * Modifica la relación entre usuario y local
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setModLocUser(ProcModUserDTO dto) throws ServiceException, SystemException {
		
		UserCtrl users = new UserCtrl();
		try{
			return users.setModLocUser(dto);
		}catch(UsuariosException ex){
			logger.debug("Problemas con controles de usuarios");
			throw new ServiceException(ex.getMessage());
		}
	}
	
}
