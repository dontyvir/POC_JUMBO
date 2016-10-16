package cl.bbr.jumbocl.usuarios.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.model.EstadoEntity;
import cl.bbr.jumbocl.common.model.LocalEntity;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.common.model.UsuariosEntity;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.usuarios.dao.JdbcDAOFactory;
import cl.bbr.jumbocl.usuarios.dto.LocalDTO;
import cl.bbr.jumbocl.usuarios.dto.ProcModUserDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.jumbocl.usuarios.dto.UsuariosCriteriaDTO;
import cl.bbr.jumbocl.usuarios.exceptions.UsuariosDAOException;

/**
 * Clase que permite consultar los Usuarios que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcUsuariosDAO implements UsuariosDAO{
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

	/**
	 * Permite la conexión con la base de datos.
	 */
	Connection 	conn 		= null;
	
	/**
	 * Indica si debe usar una conexión única
	 */
	boolean 	isUniqueCon = false; 
	
	
	// ************ Constructores de la Clase *************** //
	
	/**
	 * Constructor. Asigna una conexión del JdbcDAOFactory
	 * @throws SQLException 
	 */
	public JdbcUsuariosDAO() throws SQLException{
		conn = JdbcDAOFactory.getConexion();
	}
	
	/**
	 * Constructor que asigna una conexión para ser utilizada en forma única
	 *  (en una transacción)
	 * @param conexion
	 */
	public JdbcUsuariosDAO(Connection conexion){
		this.conn	= conexion;
		isUniqueCon = true;
	}
	
	// ************ Métodos Privados *************** //
	
	/**
	 * Obtiene la conexión
	 * @return
	 * @throws SQLException 
	 */
	private Connection getConnection() throws SQLException{
		
		if ( conn == null ){
			conn = JdbcDAOFactory.getConexion();
		}
		return this.conn;

	}

	
	/**
	 * Libera la conexión. Sólo si no es una conexión única, en cuyo caso
	 * no la cierra.
	 */
	private void releaseConnection(){
		if ( ! isUniqueCon ){
            try {
            	if (conn!=null){
            		conn.close();
            		conn = null;
            	}
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
		}
			
	}
	
	
	// ************ Métodos Publicos *************** //
	
	/**
	 * Obtiene usuario a partir de su Id de Usuario
	 * 
	 * @param id long : Id del Usuario
	 * @return UsuariosEntity
	 * @throws UsuariosDAOException
	 */
	public UsuariosEntity getUserById(long id) throws UsuariosDAOException {

		UsuariosEntity usu = new UsuariosEntity();
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String sql =
			" SELECT id_usuario, login, pass, nombre, apellido, apellido_mat, email, estado," +
			" coalesce(id_pedido,0) id_pedido, coalesce(id_cotizacion,0) id_cotizacion  " +
			" FROM bo_usuarios " +
			" WHERE id_usuario = ? ";
			/*"SELECT id_usuario, login, pass, nombre, apellido, apellido_mat, email, estado, " +
			" u.id_local AS id_local, coalesce(id_pedido,0) id_pedido, nom_local " +			
			" FROM bo_usuarios u " +
			" JOIN bo_locales l on l.id_local=u.id_local " +
			" WHERE id_usuario = ? " ;*/
		
		logger.debug("getUserById");
		logger.debug("SQL: " + sql + ", id:" + id);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			stm.setLong(1,id);
			rs = stm.executeQuery();
			while (rs.next()) {
				usu = new UsuariosEntity();
				usu.setIdUsuario(rs.getLong("id_usuario"));
				usu.setLogin(rs.getString("login"));
				usu.setPass(rs.getString("pass"));
				usu.setNombre(rs.getString("nombre"));
				usu.setApe_paterno(rs.getString("apellido"));
				usu.setApe_materno(rs.getString("apellido_mat"));
				usu.setEmail(rs.getString("email"));
				usu.setEstado(rs.getString("estado"));
				//usu.setId_local(rs.getLong("id_local"));
				usu.setId_local(-1);
				usu.setId_pedido(rs.getLong("id_pedido"));
				//usu.setLocal(rs.getString("nom_local"));
				usu.setLocal("");
				usu.setId_cotizacion(rs.getLong("id_cotizacion"));
			}

		} catch (SQLException e) {
			logger.debug("Problema getUserById :"+ e);
			throw new UsuariosDAOException(e);
			
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getUserById - Problema SQL (close)", e);
			}
		}
		return usu;
	}

	
	/**
	 * Obtiene id de usuario a partir del login
	 * 
	 * @param  login String 
	 * @return id_usuario; -1 si no encuentra al usuario
	 * @throws UsuariosDAOException
	 */
	public long getUserIdByLogin(String login) throws UsuariosDAOException {

		PreparedStatement stm = null;
		ResultSet rs = null;
		long id_usuario = -1;
		int i=0;
		
		String SQLStmt =
			"SELECT id_usuario " +
			" FROM bo_usuarios " +
			" WHERE UPPER(login) = '" + login.toUpperCase() + "' " +
			"   AND ESTADO = '" + Constantes.ESTADO_ACTIVADO + "' ";
		
		logger.debug("DAO getUserByLogin");
		logger.debug("SQL: " + SQLStmt);
		logger.debug("login: " + login);

		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt);

			//stm.setString(1,login);

			rs = stm.executeQuery();

			while (rs.next()) {
				id_usuario = rs.getLong("id_usuario");
				i++;
			}
		} catch (Exception e) {
			logger.debug("Problema getUserIdByLogin :"+ e);
			throw new UsuariosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getUserIdByLogin - Problema SQL (close)", e);
			}
		}
		
		if ( i > 1 ){
			logger.error("Se encontró más de un usuario con el mismo login!!!!");
			throw new UsuariosDAOException("Se encontró más de un usuario con el mismo login!!!!");
		}
		
		logger.debug("N° regs: " + i);
		logger.debug("id_usuario: " + id_usuario);
		
		return id_usuario;
		
	}	
	
	
	/**
	 * Obtiene listado de perfiles de un usuario
	 * 
	 * @param id
	 * @return List PerfilesEntity
	 * @throws UsuariosDAOException
	 */
	public List getPerfilesByUserId(long id) throws UsuariosDAOException {
		
		List lst_per = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		PerfilesEntity perf = null;

		String sql =
			"SELECT P.id_perfil as per_id, nombre, descripcion " +
			" FROM bo_usuxperf UP, bo_perfiles P " +
			" WHERE UP.id_perfil = P.id_perfil AND UP.id_usuario = ? ";

		logger.debug("getPerfilesByUserId");
		logger.debug(sql+", id:"+id);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			stm.setLong(1,id);
			rs = stm.executeQuery();
			while (rs.next()) {
				perf = new PerfilesEntity();
				perf.setIdPerfil(new Long(rs.getString("per_id")));
				perf.setNombre(rs.getString("nombre"));
				perf.setDescripcion(rs.getString("descripcion"));
				lst_per.add(perf);
			}

		} catch (Exception e) {
			logger.debug("Problema getPerfilesByUserId :"+ e);
			throw new UsuariosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getPerfilesByUserId - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lst_per.size());
		return lst_per;
	}
	
	
	/**
	 * Obtiele listado de usuarios de acuerdo a un criterio de búsqueda
	 * 
	 * @param  criterio UsuariosCriteriaDTO 
	 * @return List UsuariosEntity
	 * @throws UsuariosDAOException
	 */
	public List getUsersByCriteria(UsuariosCriteriaDTO criterio)throws UsuariosDAOException {
		
		List lstUsr = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		UsuariosEntity usu = new UsuariosEntity();

		//busqueda por login
		String sqlLog = "";
		if(criterio.getLogin() != null)
			sqlLog = " AND upper(login) like '"+criterio.getLogin().toUpperCase()+"%' ";
		
		//busqueda por apellido
		String sqlApe = "";
		if(criterio.getApellido() != null)
			sqlApe = " AND upper(apellido) like '"+criterio.getApellido().toUpperCase()+"%' ";
		
		// busqueda por id_local
		String sqlLocal = "";
		if ( criterio.getId_local() > 0 )
			sqlLocal = " AND id_local = " + criterio.getId_local();
		
		//variables de paginacion
		int pag = criterio.getPag();
		int regXpag = criterio.getRegsperpage();
		if(pag<0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;

		logger.debug("getUsersByCriteria");
		String sql =
			" SELECT * FROM ( " +
			" SELECT row_number() over(order by id_usuario) as row, id_usuario, login, pass, nombre, apellido, apellido_mat, email, estado " +
			" FROM bo_usuarios " +
			" WHERE 1=1 "+ sqlLog + sqlApe + sqlLocal +
			" ) AS TEMP " +
			" WHERE row BETWEEN "+ iniReg +" AND "+ finReg ;
		
		logger.debug("SQL: " + sql);
		
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				usu = new UsuariosEntity();
				usu.setIdUsuario(rs.getLong("id_usuario"));
				usu.setLogin(rs.getString("login"));
				usu.setPass(rs.getString("pass"));
				usu.setNombre(rs.getString("nombre"));
				usu.setApe_paterno(rs.getString("apellido"));
				usu.setApe_materno(rs.getString("apellido_mat"));
				usu.setEmail(rs.getString("email"));
				usu.setEstado(rs.getString("estado"));
				lstUsr.add(usu);
			}

		} catch (Exception e) {
			logger.debug("Problema getUsersByCriteria :"+ e);
			throw new UsuariosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getUsersByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("cantidad:"+lstUsr.size());
		return lstUsr;
	}

	/**
	 * Obtiene número de resultados que arroja la consulta por criterio
	 * 
	 * @param  criterio
	 * @return int
	 * @throws UsuariosDAOException
	 */
	public int getUsersCountByCriteria(UsuariosCriteriaDTO criterio) throws UsuariosDAOException {

		int cant = 0;
		PreparedStatement stm = null;
		ResultSet rs = null;

		//busqueda por login
		String sqlLog = "";
		if(criterio.getLogin() != null)
			sqlLog = " AND upper(login) like '"+criterio.getLogin().toUpperCase()+"%' ";
		
		//busqueda por apellido
		String sqlApe = "";
		if(criterio.getApellido() != null)
			sqlApe = " AND upper(apellido) like '"+criterio.getApellido().toUpperCase()+"%' ";

		// busqueda por id_local
		String sqlLocal = "";
		if ( criterio.getId_local() > 0 )
			sqlLocal = " AND id_local = " + criterio.getId_local();

		
		logger.debug("getUsersCountByCriteria");
		String sql =
			" SELECT count(id_usuario) as cantidad " +
			" FROM bo_usuarios " +
			" WHERE 1=1 "+ sqlLog + sqlApe + sqlLocal;
		logger.debug("SQL: " + sql);
		
		try {

			conn = this.getConnection();

			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			if (rs.next()) {
				cant = rs.getInt("cantidad");
			}
		} catch (Exception e) {
			logger.debug("Problema getUsersCountByCriteria :"+ e);
			throw new UsuariosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getUsersCountByCriteria - Problema SQL (close)", e);
			}
		}
		return cant;
	}

	
	/**
	 * Obtiene listado de estados
	 * 
	 * @param  tip_estado
	 * @return List EstadoEntity
	 * @throws UsuariosDAOException
	 */
	public List getUsrEstados(String tip_estado)throws UsuariosDAOException {
		List lstEst = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		EstadoEntity est = null;
		

		String sql =
			" SELECT id, estado, tipo " +
			" FROM fo_estados " +
			" WHERE tipo='"+tip_estado+"' AND visible='S' " ;

		logger.debug("SQL: " + sql);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				est = new EstadoEntity();
				est.setId(new Character(rs.getString("id").charAt(0)));
				est.setEstado(rs.getString("estado"));
				est.setTipo(rs.getString("tipo"));
				lstEst.add(est);
			}
		} catch (Exception e) {
			logger.debug("Problema getUsrEstados :"+ e);
			throw new UsuariosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getUsrEstados - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lstEst.size());
		
		return lstEst;
	}

	
	/**
	 * Modifica información del usuario
	 * 
	 * @param  usr
	 * @return boolean
	 * @throws UsuariosDAOException
	 */
	public boolean setModUser(UserDTO usr) throws UsuariosDAOException {
		
		boolean res = false;
		PreparedStatement stm = null;
		
		String SQLStmt =
			" UPDATE bo_usuarios "+
			" SET id_local = ?, login = ?, pass = ?, nombre = ?, apellido = ?, apellido_mat = ?, email = ?, estado = ? " +
			" WHERE id_usuario = ? ";
		
		logger.debug("DAO setModUser");
		logger.debug("SQL: " + SQLStmt);
		//logger.debug("id_loc:"+usr.getId_local());
		logger.debug("login:"+usr.getLogin());
		logger.debug("passw:"+usr.getPassword());
		logger.debug("nombre:"+usr.getNombre());
		logger.debug("ape_pat:"+usr.getApe_paterno());
		logger.debug("ape_mat:"+usr.getApe_materno());
		logger.debug("email:"+usr.getEmail());
		logger.debug("estado:"+usr.getEstado());
		logger.debug("id_usr:"+usr.getId_usuario());
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );

			//stm.setLong(1,usr.getId_local());
			stm.setNull(1,Types.INTEGER);
			stm.setString(2, usr.getLogin());
			stm.setString(3, usr.getPassword());
			stm.setString(4, usr.getNombre());
			stm.setString(5, usr.getApe_paterno());
			stm.setString(6, usr.getApe_materno());
			stm.setString(7, usr.getEmail());
			stm.setString(8, usr.getEstado());
			stm.setLong(9,usr.getId_usuario());
			
			int i = stm.executeUpdate();
			if(i>0)
				res = true;

		} catch (Exception e) {
			logger.debug("Problema setModUser :"+ e);
			throw new UsuariosDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("res?"+res);
		return res;
	}

	
	/**
	 * Agrega un usuario a la tabla BO_USUARIOS
	 * 
	 * @param  usr
	 * @return long
	 * @throws UsuariosDAOException
	 */
	public long addUser(UserDTO usr) throws UsuariosDAOException {

		PreparedStatement stm 	= null;
		ResultSet results 		= null;
		long id_usuario 	= 0;
		
		String SQLStmt =
			" INSERT INTO bo_usuarios (id_local, login, pass, nombre, apellido, apellido_mat, email, estado) " +
			" VALUES (?,?,?,?,?,?,?,?)";

		logger.debug("DAO addUser");
		logger.debug("SQL: " + SQLStmt );
		//logger.debug("getId_local: " + usr.getId_local());
		logger.debug("getLogin: " + usr.getLogin());
		logger.debug("getPassword: " + usr.getPassword());
		logger.debug("getNombre: " + usr.getNombre());
		logger.debug("getApe_paterno: " + usr.getApe_paterno());
		logger.debug("getApe_materno: " + usr.getApe_materno());
		logger.debug("getEmail: " + usr.getEmail());
		logger.debug("getEstado: " + usr.getEstado());
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt , Statement.RETURN_GENERATED_KEYS );
			
			//stm.setLong(1,usr.getId_local());
			stm.setNull(1,Types.INTEGER);
			stm.setString(2,usr.getLogin());
			stm.setString(3,usr.getPassword());
			stm.setString(4,usr.getNombre());
			stm.setString(5,usr.getApe_paterno());
			stm.setString(6,usr.getApe_materno());
			stm.setString(7,usr.getEmail());
			stm.setString(8,usr.getEstado());

			int i = stm.executeUpdate();
			logger.debug("rc: " + i);

			results = stm.getGeneratedKeys();
			
			if (results.next()) {				
				id_usuario = results.getLong(1);
				 logger.debug("id_usuario insertado:" + id_usuario);
			 }			
			
		} catch (SQLException e) {
			logger.error("SQLException!! ErrorCode: " + e.getErrorCode() + " , SQLState: " + e.getSQLState());
			if ( e.getErrorCode() == Integer.parseInt(DbSQLCode.SQL_DUP_KEY_CODE) ){ // -803: llave duplicada
				throw new UsuariosDAOException( String.valueOf(e.getErrorCode()) , e );
			}
			throw new UsuariosDAOException(e);		
		} finally {
			try {
				if (results != null)
					results.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addUser - Problema SQL (close)", e);
			}
		}
		return id_usuario;
		
	}
	

	/**
	 * Obtiene listado de locales
	 * 
	 * @return List LocalEntity
	 * @throws UsuariosDAOException
	 */
	public List getUsrLocales() throws UsuariosDAOException {

		List lstLoc = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		LocalEntity loc = null;
		
		String sql =
			" SELECT id_local, cod_local, nom_local " +
			" FROM bo_locales ";
		
		try {
			conn = this.getConnection();
			stm  = conn.prepareStatement(sql + " WITH UR");

			logger.debug(sql);
			rs = stm.executeQuery();
			while (rs.next()) {
				loc = new LocalEntity();
				loc.setId_local(new Long(rs.getString("id_local")));
				loc.setCod_local(rs.getString("cod_local"));
				loc.setNom_local(rs.getString("nom_local"));
				lstLoc.add(loc);
			}
		} catch (Exception e) {
			logger.debug("Problema getUsrLocales :"+ e);
			throw new UsuariosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getUsrLocales - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lstLoc.size());
		
		return lstLoc;
	}

	/**
	 * Obtiene el listado de perfiles
	 * 
	 * @return List PerfilesEntity
	 * @throws UsuariosDAOException
	 */
	public List getPerfiles() throws UsuariosDAOException {

		List lst_per = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		PerfilesEntity perf = null;

		String sql =
			"SELECT id_perfil, nombre, descripcion " +
			" FROM bo_perfiles ";
		
		logger.debug("getPerfiles");
		logger.debug("SQL: " + sql);
		
		try {
			conn = this.getConnection();

			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				perf = new PerfilesEntity();
				perf.setIdPerfil(new Long(rs.getString("id_perfil")));
				perf.setNombre(rs.getString("nombre"));
				perf.setDescripcion(rs.getString("descripcion"));
				lst_per.add(perf);
			}
		} catch (Exception e) {
			logger.debug("Problema getPerfiles :"+ e);
			throw new UsuariosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lst_per.size());
		return lst_per;
	}

	/**
	 * Agrega la relación entre el usuario y el perfil
	 * 
	 * @param  id_user
	 * @param  id_perf
	 * @return boolean
	 * @throws UsuariosDAOException
	 */
	public boolean agregaPerfUser(long id_user, long id_perf) throws UsuariosDAOException {
		
		boolean res = false;
		PreparedStatement stm = null;

		String sql = " INSERT INTO bo_usuxperf (id_usuario, id_perfil) VALUES (?,?)";
		logger.debug("agregaPerfUser");
		logger.debug(sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,id_user);
			stm.setLong(2,id_perf);

			int i = stm.executeUpdate();
			if(i>0)
				res = true;

		} catch (SQLException e) {
			logger.debug("Problema agregaPerfUser :"+ e);
			throw new UsuariosDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return res;
	}

	
	/**
	 * 
	 */
	public boolean elimPerfUser(long id_user, long id_perf) throws UsuariosDAOException {

		boolean res = false;
		PreparedStatement stm = null;

		String sql = " DELETE FROM bo_usuxperf WHERE id_usuario = ? AND id_perfil =? ";
		logger.debug("elimPerfUser");
		logger.debug("SQL: " + sql);

		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,id_user);
			stm.setLong(2,id_perf);

			int i = stm.executeUpdate();
			if(i>0)
				res = true;

		} catch (SQLException e) {
			logger.debug("Problema elimPerfUser :"+ e);
			throw new UsuariosDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return res;
	}


	/**
	 * Obtiene listado de usuarios de un perfil en un local
	 * 
	 * @param  id_perfil
	 * @param  id_local
	 * @return List UserDTO
	 * @throws UsuariosDAOException
	 */
	public List getUsrByPerfilyLocal(long id_perfil, long id_local) throws UsuariosDAOException {

		PreparedStatement 	stm = null;
		ResultSet 			rs 	= null;
		
		List lstUsr = new ArrayList();
		
		logger.debug("getUsrByPerfilyLocal");
		String SQLStmt =
			" SELECT u.id_usuario AS id_usuario, login, pass, nombre, apellido, apellido_mat, email, estado " +
			" FROM bo_usuarios u " +
			" JOIN bo_usuxperf p ON p.id_usuario = u.id_usuario AND id_perfil = ? " +
			" JOIN bo_usuarios_locales ul ON  ul.id_usuario= u.id_usuario " +
			" WHERE ul.id_local = ? ";
			/*//version anterior a bo_usuarios_locales
			 " SELECT u.id_usuario AS id_usuario, login, pass, nombre, apellido, apellido_mat, email, estado " +  
			" FROM bo_usuarios u " +
			" JOIN bo_usuxperf p ON p.id_usuario = u.id_usuario AND id_perfil = ? " + 
			" WHERE id_local = ? "
			;*/
		
		logger.debug("SQL: " + SQLStmt);
		logger.debug("id_perfil: " + id_perfil);
		logger.debug("id_local: " + id_local);
		
		try {
			
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			
			stm.setLong(1,id_perfil);
			stm.setLong(2,id_local);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				UserDTO usu = new UserDTO();
				usu.setId_usuario(rs.getLong("id_usuario"));
				usu.setLogin(rs.getString("login"));
				usu.setPassword(rs.getString("pass"));
				usu.setNombre(rs.getString("nombre"));
				usu.setApe_paterno(rs.getString("apellido"));
				usu.setApe_materno(rs.getString("apellido_mat"));
				usu.setEmail(rs.getString("email"));
				usu.setEstado(rs.getString("estado"));
				lstUsr.add(usu);
			}
		} catch (SQLException e) {
			logger.debug("Problema getUsrByPerfilyLocal :"+ e);
			throw new UsuariosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getUsrByPerfilyLocal - Problema SQL (close)", e);
			}
		}
		logger.debug("total registros: "+lstUsr.size());
		return lstUsr;
	}

	
	/**
	 * Valida si el usuario tiene el perfil y pertenece al local indicado
	 * 
	 * @param  id_usuario
	 * @param  id_perfil
	 * @param  id_local
	 * @return boolean 
	 * @throws UsuariosDAOException
	 */
	public boolean ValidaUserByPerfilByLocal(long id_usuario, long id_perfil, long id_local) throws UsuariosDAOException {

		PreparedStatement 	stm = null;
		ResultSet 			rs 	= null;
		boolean tienePerfil = false;
		
		logger.debug("ValidaUserByPerfilByLocal");
		String SQL = "SELECT U.ID_USUARIO "
                   + "FROM BODBA.BO_USUARIOS U "
                   + "     JOIN BODBA.BO_USUXPERF UP ON UP.ID_USUARIO = U.ID_USUARIO AND " 
                   + "                                  UP.ID_PERFIL  = ? "
                   + "     JOIN BODBA.BO_USUARIOS_LOCALES UL ON  UL.ID_USUARIO = U.ID_USUARIO " 
                   + "WHERE UL.ID_LOCAL  = ? "
                   + "  AND U.ID_USUARIO = ? ";
		logger.debug("SQL: " + SQL);
		logger.debug("id_perfil : " + id_perfil);
		logger.debug("id_local  : " + id_local);
		logger.debug("id_usuario: " + id_usuario);
		
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement( SQL + " WITH UR" );
			
			stm.setLong(1,id_perfil);
			stm.setLong(2,id_local);
			stm.setLong(3,id_usuario);
			
			rs = stm.executeQuery();
			if (rs.next()) {
			    tienePerfil = true;
			}

		} catch (SQLException e) {
			logger.debug("Problema ValidaUserByPerfilByLocal :"+ e);
			throw new UsuariosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("Estado Validación: " + tienePerfil);
		return tienePerfil;
	}

	
	/**
	 * Obtiene el listado de locales por usuario
	 * 
	 * @param  id_user
	 * @return List LocalDTO
	 * @throws UsuariosDAOException
	 */
	public List getLocalesByUserId(long id_user) throws UsuariosDAOException {

			PreparedStatement 	stm = null;
			ResultSet 			rs 	= null;
			
			List lst_loc = new ArrayList();
			
			logger.debug("getLocalesByUserId");
			String SQLStmt = "SELECT l.id_local id_local, cod_local, nom_local, orden, fecha_carga_precios, "
                           + "       cod_local_pos, tipo_flujo, tipo_picking "  
                           + "FROM bodba.bo_locales l "
                           + "     JOIN bodba.bo_usuarios_locales ul ON l.id_local = ul.id_local AND ul.id_usuario = ? ";
			logger.debug("SQL: " + SQLStmt);
			logger.debug("id_usuario: " + id_user);
			
			try {
				conn = this.getConnection();
				stm = conn.prepareStatement( SQLStmt + " WITH UR" );
				stm.setLong(1,id_user);
				
				rs = stm.executeQuery();
				while (rs.next()) {
					LocalDTO local = new LocalDTO();
					local.setId_local(rs.getLong("id_local"));
					local.setCod_local(rs.getString("cod_local"));
					local.setNom_local(rs.getString("nom_local"));
					local.setFec_carga_prec(rs.getString("fecha_carga_precios"));
					local.setOrden(rs.getInt("orden"));
					local.setCod_local_pos(rs.getInt("cod_local_pos"));
					local.setTipo_flujo(rs.getString("tipo_flujo"));
					local.setTipo_picking(rs.getString("tipo_picking"));
					lst_loc.add(local);
				}
			} catch (SQLException e) {
				logger.debug("Problema getLocalesByUserId :"+ e);
				throw new UsuariosDAOException(e);
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (stm != null)
						stm.close();
				releaseConnection();
				} catch (SQLException e) {
					logger.error("[Metodo] : xxx - Problema SQL (close)", e);
				}
			}
			logger.debug("total registros: "+lst_loc.size());
			return lst_loc;
	}

	/**
	 * Listado de locales
	 * 
	 * @return List LocalDTO
	 * @throws UsuariosDAOException
	 */
	public List getLocales() throws UsuariosDAOException {

		PreparedStatement 	stm = null;
		ResultSet 			rs 	= null;
		
		List lst_loc = new ArrayList();
		
		logger.debug("getLocales");
		String SQLStmt = 
			" SELECT id_local, cod_local, nom_local, orden, fecha_carga_precios, cod_local_pos " +  
			" FROM bo_locales  " ;
		
		logger.debug("SQL: " + SQLStmt);
		
		try {
			
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			
			
			rs = stm.executeQuery();
			while (rs.next()) {
				LocalDTO loc = new LocalDTO();
				loc.setId_local(rs.getLong("id_local"));
				loc.setCod_local(rs.getString("cod_local"));
				loc.setNom_local(rs.getString("nom_local"));
				loc.setFec_carga_prec(rs.getString("fecha_carga_precios"));
				loc.setOrden(rs.getInt("orden"));
				loc.setCod_local_pos(rs.getInt("cod_local_pos"));
				lst_loc.add(loc);
			}
		} catch (SQLException e) {
			logger.debug("Problema getLocales :"+ e);
			throw new UsuariosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("total registros: "+lst_loc.size());
		return lst_loc;
}

	/**
	 * Agrega la relacion entre usuario y local
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws UsuariosDAOException
	 */
	public boolean addUserLoc(ProcModUserDTO dto) throws UsuariosDAOException {

		boolean res = false;
		PreparedStatement stm = null;

		String sql = " INSERT INTO bo_usuarios_locales (id_usuario, id_local) VALUES (?,?) ";
		logger.debug("En addUserLoc");
		logger.debug("SQL: " + sql);

		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,dto.getId_usuario());
			stm.setLong(2,dto.getId_local());

			int i = stm.executeUpdate();
			if(i>0)
				res = true;

		} catch (SQLException e) {
			logger.debug("Problema addUserLoc :"+ e);
			throw new UsuariosDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addUserLoc - Problema SQL (close)", e);
			}
		}
		return res;
	}

	/**
	 * Elimina la relación entre usuario y local
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws UsuariosDAOException
	 */
	public boolean delUserLoc(ProcModUserDTO dto)  throws UsuariosDAOException {

		boolean res = false;
		PreparedStatement stm = null;

		String sql = " DELETE FROM bo_usuarios_locales WHERE id_usuario=? AND id_local=? ";
		logger.debug("En delUserLoc");
		logger.debug("SQL: " + sql);

		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,dto.getId_usuario());
			stm.setLong(2,dto.getId_local());

			int i = stm.executeUpdate();
			if(i>0)
				res = true;

		} catch (SQLException e) {
			logger.debug("Problema delUserLoc :"+ e);
			throw new UsuariosDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delUserLoc - Problema SQL (close)", e);
			}
		}
		return res;
	}
}
