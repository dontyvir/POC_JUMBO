package cl.bbr.jumbocl.usuarios.ctrl;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.EstadoEntity;
import cl.bbr.jumbocl.common.model.LocalEntity;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.common.model.UsuariosEntity;
import cl.bbr.jumbocl.common.utils.Cifrador;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.usuarios.dao.JdbcUsuariosDAO;
import cl.bbr.jumbocl.usuarios.dto.PerfilDTO;
import cl.bbr.jumbocl.usuarios.dto.ProcModUserDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.jumbocl.usuarios.dto.UsuariosCriteriaDTO;
import cl.bbr.jumbocl.usuarios.dao.DAOFactory;
import cl.bbr.jumbocl.usuarios.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.LocalDTO;
import cl.bbr.jumbocl.usuarios.exceptions.UsuariosDAOException;
import cl.bbr.jumbocl.usuarios.exceptions.UsuariosException;

/**
 * Entrega metodos de navegacion por usuarios y busqueda en base a criterios. 
 * Los resultados son listados de usuarios y datos de locales.
 * 
 * @author BBR Ingenieria
 *
 */
public class UserCtrl {

	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	/**
	 * Código de error.
	 */
	String cod_error = "";
	
	
	/**
	 * Retorna información de un usuario
	 * 
	 * @param  id_user
	 * @return UserDTO
	 * @throws SystemException 
	 * @throws UsuariosException
	 */
	public UserDTO getUserById(long id_user)  throws UsuariosException, SystemException{
		UserDTO result=null;
		try{
			logger.debug("en getUserById");
			JdbcUsuariosDAO usuariosDAO = (JdbcUsuariosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getUsuariosDAO();
			
			UsuariosEntity user = usuariosDAO.getUserById(id_user);
			
			List lst_perf 	= usuariosDAO.getPerfilesByUserId(id_user);

			//agregar la lista de locales relacionados al usuario
			List lst_loc	= usuariosDAO.getLocalesByUserId(id_user);
			
			user.setPerfiles(lst_perf);
			user.setLocales(lst_loc);
			
			result = new UserDTO(user.getIdUsuario(), user.getLogin(), user.getPass(), 
					user.getNombre(), user.getApe_paterno(), user.getApe_materno(), user.getEmail(), user.getEstado(), 
					user.getPerfiles(), user.getId_pedido());
			
			result.setId_cotizacion(user.getId_cotizacion());
			result.setId_local(user.getId_local());
			result.setLocal(user.getLocal());
			
			result.setLocales(user.getLocales());
			
		}catch(UsuariosDAOException ex){
			logger.debug("Problema :"+ex);
			throw new SystemException(ex);
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
			logger.debug("Problema :"+e);
			throw new SystemException(e);
		}
		return result;
	}
	
	
	/**
	 * Obtiene información del cliente a partir de su login
	 * 
	 * @param  login
	 * @return UserDTO
	 * @throws UsuariosException
	 * @throws SystemException 
	 */
	public UserDTO getUserByLogin(String login)
		throws UsuariosException, SystemException {
		
		long id_usuario = -1;
		UserDTO user = null;
		
		try {
			
			JdbcUsuariosDAO dao = (JdbcUsuariosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getUsuariosDAO();
		
			id_usuario = dao.getUserIdByLogin(login);
			
			logger.debug("UserCtrl: id_usuario: " + id_usuario);
			
			if (id_usuario != -1)
				user = this.getUserById(id_usuario);
			else
				user = null;
			
			return user;
			
		}catch(UsuariosException ex){
			logger.debug("Problema :"+ex);
			throw new UsuariosException(ex);
		} catch (UsuariosDAOException e) {
			logger.debug("Problema :"+e);
			throw new SystemException(e);
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
			logger.debug("Problema :"+e);
			throw new SystemException(e);
		} 
		
		
	}
	
	
	/**
	 * Retorna lista de usuarios, segun criterios de busqueda
	 * 
	 * @param  criterio
	 * @return List UserDTO
	 * @throws UsuariosException 
	 */
	public List getUsersByCriteria(UsuariosCriteriaDTO criterio)throws UsuariosException {
		List result = new ArrayList();
		try{
			logger.debug("en getUsersByCriteria");
			JdbcUsuariosDAO usuariosDAO = (JdbcUsuariosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getUsuariosDAO();
			
			List lstUsr = new ArrayList();
			lstUsr = usuariosDAO.getUsersByCriteria(criterio);
			UsuariosEntity usr = null;
			
			for(int i=0;i<lstUsr.size();i++){
				usr = null;
				usr = (UsuariosEntity)lstUsr.get(i);
				UserDTO dto = new UserDTO (usr.getIdUsuario(), usr.getLogin(), 
						usr.getNombre(), usr.getApe_paterno(), usr.getApe_materno(), usr.getEstado());
				result.add(dto);
			}
			
		}catch(UsuariosDAOException ex){
			logger.debug("Problema getUsersByCriteria:"+ex);
			throw new UsuariosException(ex);
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
			logger.debug("Problema getUsersByCriteria:"+e);
			throw new UsuariosException(e);
		}
		return result;
	}	

	
	/**
	 * Retorna cantidad de usuarios, segun criterios de busqueda
	 * 
	 * @param  criterio
	 * @return int
	 * @throws UsuariosException 
	 */
	public int getUsersCountByCriteria(UsuariosCriteriaDTO criterio)throws UsuariosException {
		int cant = 0;
		try{
			logger.debug("en getUsersCountByCriteria");
			JdbcUsuariosDAO usuariosDAO = (JdbcUsuariosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getUsuariosDAO();
			
			cant = usuariosDAO.getUsersCountByCriteria(criterio);
			
		}catch(UsuariosDAOException ex){
			logger.debug("Problema:"+ex);
			throw new UsuariosException(ex);
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
			logger.debug("Problema:"+e);
			throw new UsuariosException(e);
		}
		return cant;
	}	

	
	/**
	 * Modifica información del usuario
	 * 
	 * @param  user UserDTO
	 * @return boolean, devuelve <i>true</i> si la modificación fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws SystemException 
	 * @throws UsuariosException 
	 */
	public boolean setModUser(UserDTO user)throws UsuariosException, SystemException {
		boolean res = false;
		
		UserDTO userOld = this.getUserById(user.getId_usuario());
		
		// si la clave es nueva, se debe encriptar MD5, caso contrario, pasa tal cual
		if ( ! userOld.getPassword().equals( user.getPassword() )  ){

			// encripta el password en MD5
			try {
				String passwd = Cifrador.toMD5(user.getPassword());
				user.setPassword(passwd);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				throw new SystemException("Error al convertir MD5");
			}
			
		}
		
		/* El login debe permanecer inalterado */
		user.setLogin(userOld.getLogin());
		
		try{
			logger.debug("en setModUser");
			JdbcUsuariosDAO usuariosDAO = (JdbcUsuariosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getUsuariosDAO();
			
			res = usuariosDAO.setModUser(user);
			
		}catch(UsuariosDAOException ex){
			logger.debug("Problema :"+ex);
			throw new SystemException(ex);
		} catch (SQLException e) {
			logger.debug("Problema :"+e);
			e.printStackTrace();
			throw new SystemException(e);
		
		}
		
		return res;
	}

	/**
	 * Agrega usuario al sistema
	 * 
	 * @param user
	 * @return id_usuario
	 * @throws UsuariosException _EX_USR_LOGIN_DUPLICADO login duplicado
	 * @throws SystemException
	 */
	public long addUser(UserDTO user)throws UsuariosException, SystemException {
		
		logger.debug("Ctrl addUser");
		
		try{		
			// encripta el password en MD5
			String passwd = Cifrador.toMD5(user.getPassword());
			user.setPassword(passwd);

			// dao
			JdbcUsuariosDAO usuariosDAO = (JdbcUsuariosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getUsuariosDAO();
			
			return usuariosDAO.addUser(user);
			
		}catch(UsuariosDAOException ex){
			logger.debug("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_DUP_KEY_CODE ) ){ // Controlamos error login duplicado
				throw new UsuariosException( Constantes._EX_USR_LOGIN_DUPLICADO, ex );
			}
			throw new SystemException("Error no controlado",ex);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new SystemException("Error al convertir MD5", e);
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
			throw new SystemException("Error al convertir MD5", e);
		}
		
	}

	
	/**
	 * Agrega la relación entre el usuario y el perfil a asignar.
	 * 
	 * @param id_user
	 * @param id_perf
	 * @return boolean, devuelve <i>true</i> si la modificación fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws UsuariosException
	 */
	public boolean agregaPerfUser(long id_user, long id_perf)throws UsuariosException {
		boolean res = false;
		try{
			logger.debug("en agregaPerfUser");
			JdbcUsuariosDAO usuariosDAO = (JdbcUsuariosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getUsuariosDAO();
			
			res = usuariosDAO.agregaPerfUser(id_user, id_perf);
			
		}catch(UsuariosDAOException ex){
			logger.debug("Problema :"+ex);
			throw new UsuariosException(ex);
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
			logger.debug("Problema :"+e);
			throw new UsuariosException(e);
		}
		
		return res;
	}
	
	
	/**
	 * Elimina la relación entre el usuario y el perfil.
	 * 
	 * @param id_user
	 * @param id_perf
	 * @return boolean, devuelve <i>true</i> si la eliminación fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws UsuariosException
	 */
	public boolean elimPerfUser(long id_user, long id_perf)throws UsuariosException {
		boolean res = false;
		try{
			logger.debug("en elimPerfUser");
			JdbcUsuariosDAO usuariosDAO = (JdbcUsuariosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getUsuariosDAO();
			
			res = usuariosDAO.elimPerfUser(id_user, id_perf);
			
		}catch(UsuariosDAOException ex){
			logger.debug("Problema :"+ex);
			throw new UsuariosException(ex);
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
			logger.debug("Problema :"+e);
			throw new UsuariosException(e);
		}
		
		return res;
	}

	
	/**
	 * Obtiene el listados de los estados del usuario
	 * 
	 * @param  tipo
	 * @return List EstadoDTO
	 * @throws UsuariosException
	 */
	public List getUsrEstados(String tipo)throws UsuariosException {
		List result = new ArrayList();
		try{
			logger.debug("en getUsrEstados");
			JdbcUsuariosDAO usuariosDAO = (JdbcUsuariosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getUsuariosDAO();
			
			List lstEst = new ArrayList();
			lstEst = usuariosDAO.getUsrEstados(tipo);
			EstadoEntity est = null;
			
			for(int i=0;i<lstEst.size();i++){
				est = null;
				est = (EstadoEntity)lstEst.get(i);
				logger.debug("est:"+est.getId().charValue());
				EstadoDTO dto = new EstadoDTO (est.getId().charValue(), est.getEstado());
				result.add(dto);
			}
			
		}catch(UsuariosDAOException ex){
			logger.debug("Problema getUserById:"+ex);
			throw new UsuariosException(ex);
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
			logger.debug("Problema getUserById:"+e);
			throw new UsuariosException(e);
		}
		return result;
	}	

	
	/**
	 * Obtiene el listado de locales
	 * 
	 * @return List LocalDTO
	 * @throws UsuariosException
	 */
	public List getUsrLocales()throws UsuariosException {
		List result = new ArrayList();
		try{
			logger.debug("en getUsrLocales");
			JdbcUsuariosDAO usuariosDAO = (JdbcUsuariosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getUsuariosDAO();
			
			List lstLoc = new ArrayList();
			lstLoc = usuariosDAO.getUsrLocales();
			LocalEntity loc = null;
			
			for(int i=0;i<lstLoc.size();i++){
				loc = null;
				loc = (LocalEntity)lstLoc.get(i);
				LocalDTO locDto = new LocalDTO(loc.getId_local().longValue(), loc.getCod_local(), loc.getNom_local());
				result.add(locDto);
			}
			
		}catch(UsuariosDAOException ex){
			logger.debug("Problema getUserById:"+ex);
			throw new UsuariosException(ex);
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
			logger.debug("Problema getUserById:"+e);
			throw new UsuariosException(e);
		}
		return result;
	}	

	
	/**
	 * Obtiene el listado de los perfiles.
	 * 
	 * @return List PerfilDTO
	 * @throws UsuariosException
	 */
	public List getPerfiles()throws UsuariosException {
		List result = new ArrayList();
		try{
			logger.debug("en getPerfiles");
			JdbcUsuariosDAO usuariosDAO = (JdbcUsuariosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getUsuariosDAO();
			
			List lstPerf = new ArrayList();
			lstPerf = usuariosDAO.getPerfiles();
			PerfilesEntity per = null;
			
			for(int i=0;i<lstPerf.size();i++){
				per = null;
				per = (PerfilesEntity)lstPerf.get(i);
				PerfilDTO locDto = new PerfilDTO(per.getIdPerfil().longValue(), per.getNombre(), per.getDescripcion());
				result.add(locDto);
			}
			
		}catch(UsuariosDAOException ex){
			logger.debug("Problema getUserById:"+ex);
			throw new UsuariosException(ex);
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
			logger.debug("Problema getUserById:"+e);
			throw new UsuariosException(e);
		}
		return result;
	}	
	
	
	
	/**
	 * Autentica usuario en el sistema
	 * 
	 * @param  login
	 * @param  password
	 * @throws SystemException 
	 * @throws UsuariosException 
	 */
	public boolean doAutenticaUser(String login, String password)
		throws SystemException, UsuariosException{
		
		UserDTO user = null;
		String passwd = null;
		
		// encripta el password en MD5
		try {
			passwd = Cifrador.toMD5(password);
			
			user = this.getUserByLogin(login);
			
			if ( user == null ){
				logger.debug("El login no existe");
				return false;
			}
			
			if ( !user.getPassword().equals(passwd) ){
				logger.debug("El password es incorrecto");
				return false;
			}

			if ( !user.getEstado().equals("A") ){
				logger.debug("El Usuario esta Bloqueado");
				return false;
			}

					
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new SystemException("Error al convertir MD5");	
		}
		
		
		return true;
		
	}
	
	
	
	
	/**
	 * Obtiene listado de usuarios de un perfil en un local
	 * 
	 * @param  id_perfil
	 * @param  id_local 
	 * @return List UserDTO
	 * @throws SystemException 
	 * @throws UsuariosException 
	 */
	public List getUsrByPerfilyLocal(long id_perfil, long id_local)
		throws SystemException, UsuariosException{
		
		try{
			logger.debug("en getPerfiles");
			JdbcUsuariosDAO usuariosDAO = (JdbcUsuariosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getUsuariosDAO();
			
			return usuariosDAO.getUsrByPerfilyLocal(id_perfil, id_local);
			
		}catch(UsuariosDAOException ex){
			logger.debug("Problema getUserById:"+ex);
			throw new SystemException(ex);
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
			logger.debug("Problema getUserById:"+e);
			throw new SystemException(e);
		}
	}

	/**
	 * Valida si el usuario tiene el perfil y pertenece al local indicado
	 * 
	 * @param  id_usuario
	 * @param  id_perfil
	 * @param  id_local 
	 * @return boolean
	 * @throws SystemException 
	 * @throws UsuariosException 
	 */
	public boolean ValidaUserByPerfilByLocal(long id_usuario, long id_perfil, long id_local)
		throws SystemException, UsuariosException{
		
		try{
			logger.debug("en ValidaUserByPerfilByLocal");
			JdbcUsuariosDAO usuariosDAO = (JdbcUsuariosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getUsuariosDAO();
			
			return usuariosDAO.ValidaUserByPerfilByLocal(id_usuario, id_perfil, id_local);
			
		}catch(UsuariosDAOException ex){
			logger.debug("Problema ValidaUserByPerfilByLocal:"+ex);
			throw new SystemException(ex);
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
			logger.debug("Problema ValidaUserByPerfilByLocal:"+e);
			throw new SystemException(e);
		}
	}


	/**
	 * Listado de locales 
	 * 
	 * @return List LocalDTO
	 * @throws SystemException
	 * @throws UsuariosException
	 */
	public List getLocales() throws SystemException, UsuariosException{
		
		try{
			logger.debug("en getPerfiles");
			JdbcUsuariosDAO usuariosDAO = (JdbcUsuariosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getUsuariosDAO();
			
			return usuariosDAO.getLocales();
			
		}catch(UsuariosDAOException ex){
			logger.debug("Problema getUserById:"+ex);
			throw new SystemException(ex);
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
			logger.debug("Problema getUserById:"+e);
			throw new SystemException(e);
		}
	}


	/**
	 * Modifica la relación entre el usuario y el local
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws SystemException
	 * @throws UsuariosException
	 */
	public boolean setModLocUser(ProcModUserDTO dto) throws SystemException, UsuariosException{
		
		boolean res = false;
		try{
			logger.debug("en setModLocUser");
			JdbcUsuariosDAO usuariosDAO = (JdbcUsuariosDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getUsuariosDAO();
			
			//verifica q exista el usuario
			UsuariosEntity user = usuariosDAO.getUserById(dto.getId_usuario());
			
			if(user ==null)
				throw new UsuariosException(Constantes._EX_USR_ID_NO_EXISTE);

			//obtiene el listado de locales del usuario
			logger.debug("obtiene locales");
			user.setLocales(usuariosDAO.getLocalesByUserId(dto.getId_usuario()));

			//si desea agregar la relacion
			if(dto.getAccion().equals("agregar")){
				List lst_loc = user.getLocales();
				boolean encontro = false;
				for(int i=0; i<lst_loc.size(); i++){
					LocalDTO loc = (LocalDTO)lst_loc.get(i);
					if(dto.getId_local()== loc.getId_local()){
						encontro = true;
						break;
					}
				}
				if(encontro){
					throw new UsuariosException(Constantes._EX_USR_LOC_EXISTE);
				}
				//agrega la relacion
				res = usuariosDAO.addUserLoc(dto);
			}else if(dto.getAccion().equals("eliminar")){
				List lst_loc = user.getLocales();
				boolean encontro = false;
				for(int i=0; i<lst_loc.size(); i++){
					LocalDTO loc = (LocalDTO)lst_loc.get(i);
					if(dto.getId_local()== loc.getId_local()){
						encontro = true;
						break;
					}
				}
				if(!encontro){
					throw new UsuariosException(Constantes._EX_USR_LOC_NO_EXISTE);
				}
				//elimina la relacion
				res = usuariosDAO.delUserLoc(dto);
			}
			
		}catch(UsuariosDAOException ex){
			logger.debug("Problema getUserById:"+ex);
			throw new SystemException(ex);
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
			logger.debug("Problema getUserById:"+e);
			throw new SystemException(e);
		}
		return res;
	}
	
	
	
	
	
}
