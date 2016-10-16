package cl.bbr.jumbocl.usuarios.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.model.ComandosEntity;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.usuarios.dao.JdbcDAOFactory;
import cl.bbr.jumbocl.usuarios.dto.PerfilDTO;
import cl.bbr.jumbocl.usuarios.dto.PerfilesCriteriaDTO;
import cl.bbr.jumbocl.usuarios.exceptions.PerfilesDAOException;

/**
 * Clase que permite consultar los Perfiles que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcPerfilesDAO implements PerfilesDAO{
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

	/**
	 * Permite la conexión con la base de datos.
	 */
	Connection 	conn 		= null;

	/**
	 * Permite el manejo de la transaccionalidad, para procesos de multiples operaciones en la base de datos
	 */
	JdbcTransaccion trx		= null;
	
	
	// ************ Constructores de la Clase *************** //
	
	/**
	 * Constructor
	 */
	public JdbcPerfilesDAO(){

	}
	
	// ************ Métodos Privados *************** //
	
	/**
	 * Obtiene la conexión
	 * 
	 * @return
	 * @throws SQLException 
	 */
	private Connection getConnection() throws SQLException{
		
		if ( conn == null ){
			conn = JdbcDAOFactory.getConexion();
		}
		return this.conn;

	}
	
	// ************ Métodos Publicos *************** //
	/**
	 * Libera la conexión. Sólo si no es una conexión única, en cuyo caso
	 * no la cierra.
	 * 
	 * 
	 */
	private void releaseConnection(){
		if ( trx == null ){
            try {
            	if (conn != null){
            		conn.close();
            		conn = null;
            	}
            } catch (SQLException e) {
            	logger.error(e.getMessage(), e);

            }
		}
			
	}
	// ******************************************** //	
	
	/**
	 * Setea una transacción al dao y le asigna su conexión
	 * 
	 * @param  trx JdbcTransaccion 
	 * @throws PerfilesDAOException 
	 */
	public void setTrx(JdbcTransaccion trx) throws PerfilesDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new PerfilesDAOException(e);
		}
	}
	
	/**
	 * Retorna la transacción en que participa el dao
	 * 
	 * @return JdbcTransaccion
	 */
	public JdbcTransaccion getTrx() {
		return trx;
	}

	
	// ************ Métodos de Negocio ************** //
	/**
	 * Obtiene información del perfil
	 * 
	 * @param  id
	 * @return PerfilesEntity
	 * @throws PerfilesDAOException
	 */
	public PerfilesEntity getPerfilById(long id) throws PerfilesDAOException {

		PerfilesEntity per = new PerfilesEntity();
		
		PreparedStatement stm = null;
		ResultSet rs = null;

		String sql =
			"SELECT id_perfil, nombre, descripcion " +
			" FROM bo_perfiles " +
			" WHERE id_perfil = ? " ;

		logger.debug("getPerfilById");
		logger.debug(sql+", id:"+id);
		
		try {

			conn = this.getConnection();

			stm = conn.prepareStatement(sql + " WITH UR");

			stm.setLong(1,id);
			rs = stm.executeQuery();
			while (rs.next()) {
				per = new PerfilesEntity();
				per.setIdPerfil(new Long(rs.getString("id_perfil")));
				per.setNombre(rs.getString("nombre"));
				per.setDescripcion(rs.getString("descripcion"));
			}

		} catch (Exception e) {
			logger.debug("Problema getPerfilById :"+ e);
			throw new PerfilesDAOException(e);
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
		return per;
	}

	
	/**
	 * Obtiene listado de perfiles
	 * 
	 * @param criterio
	 * @return List PerfilesEntity
	 * @throws PerfilesDAOException
	 */
	public List getPerfilesAll(PerfilesCriteriaDTO criterio) throws PerfilesDAOException {

		List lstPer = new ArrayList();
		PerfilesEntity per = new PerfilesEntity();
		PreparedStatement stm = null;
		ResultSet rs = null;

		//variables de paginacion
		int pag = criterio.getPag();
		int regXpag = criterio.getRegsperpage();
		if(pag<0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;

		String sql =
			" SELECT * FROM ( " +
			" SELECT row_number() over(order by id_perfil) as row, id_perfil, nombre, descripcion " +
			" FROM bo_perfiles "+
			" ) AS TEMP " +
			" WHERE row BETWEEN "+ iniReg +" AND "+ finReg ;

		logger.debug("getPerfilesAll");
		logger.debug(sql);
		
		try {
			
			conn = this.getConnection();

			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				per = new PerfilesEntity();
				per.setIdPerfil(new Long(rs.getString("id_perfil")));
				per.setNombre(rs.getString("nombre"));
				per.setDescripcion(rs.getString("descripcion"));
				lstPer.add(per);
			}

		} catch (Exception e) {
			logger.debug("Problema getPerfilesAll :"+ e);
			throw new PerfilesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getPerfilesAll - Problema SQL (close)", e);
			}
		}
			logger.debug("cant en lst:"+lstPer.size());
		return lstPer;
	}

	
	/**
	 * Obtiene número total de perfiles
	 * 
	 * @param  criterio
	 * @return int
	 * @throws PerfilesDAOException
	 */
	public int getPerfilesAllCount(PerfilesCriteriaDTO criterio) throws PerfilesDAOException {

		int cant = 0;
		PreparedStatement stm = null;
		ResultSet rs = null;

		logger.debug("getPerfilesAllCount");
		String sql = 
			" SELECT count(id_perfil) as cantidad " +
			" FROM bo_perfiles " ;
		logger.debug(sql);
		
		try {

			conn = this.getConnection();

			stm = conn.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			if (rs.next()) {
				cant = rs.getInt("cantidad");
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new PerfilesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getPerfilesAllCount - Problema SQL (close)", e);
			}
		}
		logger.debug("cant:"+cant);
		return cant;

	}

	/**
	 * Obtiene listado de comandos
	 * 
	 * @param  id
	 * @return List ComandosEntity
	 * @throws PerfilesDAOException
	 */
	public List getComandos(long id) throws PerfilesDAOException {

		List lstCmd = new ArrayList();
		ComandosEntity cmd = new ComandosEntity();
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String sql = 
			"SELECT C.id_comando as id_cmd, nombre_clase, descripcion " +
			" FROM bo_comandos C, bo_comxperf CP" +
			" WHERE C.id_comando = CP.id_comando AND CP.id_perfil = ? " ;

		logger.debug("getComandos");
		logger.debug(sql);
		logger.debug("id:"+id);
		
		try {
			conn = this.getConnection();

			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id);

			rs = stm.executeQuery();
			while (rs.next()) {
				cmd = new ComandosEntity();
				cmd.setId_cmd(rs.getLong("id_cmd"));
				cmd.setNombre(rs.getString("nombre_clase"));
				cmd.setDescripcion(rs.getString("descripcion"));
				lstCmd.add(cmd);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new PerfilesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getComandos - Problema SQL (close)", e);
			}
		}
			logger.debug("cant en lst:"+lstCmd.size());
		return lstCmd;
	}
	
	
	/**
	 * Obtiene registro de la tabla BO_COMANDOS a partir del nombre del comando
	 * 
	 * @param  comando
	 * @return ComandosEntity
	 * @throws PerfilesDAOException
	 */
	public ComandosEntity getComandoByName(String comando) throws PerfilesDAOException {
		PreparedStatement 		stm = null;
		ResultSet 				rs 	= null;
		ComandosEntity 			cmd	= null;

		String SQLStmt = 
			"SELECT id_comando as id_cmd, nombre_clase, descripcion, seguridad, activo " +
			" FROM bo_comandos " +
			" WHERE nombre_clase = '"+comando+"' " ;
		
		logger.debug("Dao getComandoByName");
		logger.debug( SQLStmt );
		logger.debug("comando: " + comando);
		
		try {

			conn = this.getConnection();
		
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			rs = stm.executeQuery();
			if (rs.next()) {
				cmd = new ComandosEntity();
				cmd.setId_cmd(rs.getLong("id_cmd"));
				cmd.setNombre(rs.getString("nombre_clase"));
				cmd.setDescripcion(rs.getString("descripcion"));
				cmd.setSeguridad(rs.getString("seguridad"));
				cmd.setActivo(rs.getString("activo"));	
			}
			else
			{
				logger.debug("La consulta no ha arrojado resultados");
				cmd = null;
			}

		} catch (Exception e) {
			logger.debug("Problema getComandoByName :"+ e);
			e.printStackTrace();
			throw new PerfilesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getComandoByName - Problema SQL (close)", e);
			}
		}


		return cmd;
	}
	
	
	/**
	 * Obtiene listado de todos los comandos
	 * 
	 * @param  id
	 * @return List ComandosEntity
	 * @throws PerfilesDAOException
	 */
	public List getComandosAll(long id) throws PerfilesDAOException {

		List lstCmd = new ArrayList();
		ComandosEntity cmd = new ComandosEntity();
		PreparedStatement stm = null;
		ResultSet rs = null;

		String sql = "SELECT id_comando, nombre_clase, descripcion " +
		" FROM bo_comandos order by nombre_clase ASC ";
		//" WHERE C.id_comando = CP.id_comando AND CP.id_perfil = ? " ;
		logger.debug("getComandos");
		logger.debug(sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			//stm.setLong(1,id);

			rs = stm.executeQuery();
			while (rs.next()) {
				cmd = new ComandosEntity();
				cmd.setId_cmd(rs.getLong("id_comando"));
				cmd.setNombre(rs.getString("nombre_clase"));
				cmd.setDescripcion(rs.getString("descripcion"));
				lstCmd.add(cmd);
			}

		} catch (Exception e) {
			logger.debug("Problema getComandosAll :"+ e);
			throw new PerfilesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getComandosAll - Problema SQL (close)", e);
			}
		}
			logger.debug("cant en lst:"+lstCmd.size());
		return lstCmd;
	}

	/**
	 * Actualiza perfil
	 * 
	 * @param  perf
	 * @return boolean
	 * @throws PerfilesDAOException
	 */
	public boolean setModPerfil(PerfilDTO perf) throws PerfilesDAOException {

		boolean res = false;
		PreparedStatement stm = null;

		String sql = 
			" UPDATE bo_perfiles SET nombre = ?, descripcion = ? " +
			" WHERE id_perfil = ?";

		logger.debug("setModPerfil");
		logger.debug(sql);
		logger.debug("nom:"+perf.getNombre());
		logger.debug("desc:"+perf.getDescripcion());
		logger.debug("id:"+perf.getIdPerfil());

		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setString(1,perf.getNombre());
			stm.setString(2,perf.getDescripcion());
			stm.setLong(3,perf.getIdPerfil());

			int i = stm.executeUpdate();
			if(i>0)
				res = true;

		} catch (Exception e) {
			logger.debug("Problema setModPerfil :"+ e);
			throw new PerfilesDAOException(e);
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
	 * Agrega un nuevo perfil
	 * 
	 * @param  perf
	 * @return boolean
	 * @throws PerfilesDAOException
	 */
	public boolean addPerfil(PerfilDTO perf) throws PerfilesDAOException {

		boolean res = false;
		PreparedStatement stm = null;

		String sql = " INSERT INTO bo_perfiles (nombre, descripcion) VALUES (?,?)";
		logger.debug("addPerfil");
		logger.debug(sql);
		logger.debug("nom:"+perf.getNombre());
		logger.debug("desc:"+perf.getDescripcion());

		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setString(1,perf.getNombre());
			stm.setString(2,perf.getDescripcion());

			int i = stm.executeUpdate();
			if(i>0)
				res = true;

		} catch (Exception e) {
			logger.debug("Problema addPerfil:"+ e);
			throw new PerfilesDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addPerfil - Problema SQL (close)", e);
			}
		}
		return res;
	}

	/**
	 * Elimina un perfil
	 * 
	 * @param  perf
	 * @return boolean
	 * @throws PerfilesDAOException
	 */
	public boolean delPerfil(PerfilDTO perf) throws PerfilesDAOException {
		boolean res = false;
		PreparedStatement stm = null;

		String sql = " DELETE FROM bo_perfiles WHERE id_perfil =? ";
		logger.debug("delPerfil");
		logger.debug(sql);
		logger.debug("id:"+perf.getIdPerfil());

		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,perf.getIdPerfil());

			int i = stm.executeUpdate();
			if(i>0)
				res = true;

		} catch (Exception e) {
			logger.debug("Problema delPerfil :"+ e);
			throw new PerfilesDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delPerfil - Problema SQL (close)", e);
			}
		}
		return res;
	}
	
	
	/**
	 * Asocia comando a un perfil
	 * 
	 * @param  id_cmd
	 * @param  id_perf
	 * @return boolean
	 * @throws PerfilesDAOException
	 */
	public boolean agregaCmdPerf(long id_cmd, long id_perf) throws PerfilesDAOException {
		boolean res = false;
		PreparedStatement stm = null;

		String sql = " INSERT INTO bo_comxperf (id_comando, id_perfil) VALUES (?,?)";
		logger.debug("agregaCmdPerf");
		logger.debug(sql);
		logger.debug("id_cmd:"+id_cmd);
		logger.debug("id_perf:"+id_perf);

		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,id_cmd);
			stm.setLong(2,id_perf);

			int i = stm.executeUpdate();
			if(i>0)
				res = true;

		} catch (Exception e) {
			logger.debug("Problema agregaCmdPerf : "+ e);
			throw new PerfilesDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : agregaCmdPerf - Problema SQL (close)", e);
			}
		}
		return res;
	}

	
	/**
	 * Desasocia comando a un perfil
	 * 
	 * @param  id_cmd
	 * @param  id_perf
	 * @return boolean
	 * @throws PerfilesDAOException
	 */
	public boolean elimCmdPerf(long id_cmd, long id_perf) throws PerfilesDAOException {
		boolean res = false;
		PreparedStatement stm = null;

		String sql = " DELETE FROM bo_comxperf WHERE id_comando = ? AND id_perfil =? ";
		logger.debug("elimCmdPerf");
		logger.debug(sql);
		logger.debug("id_cmd:"+id_cmd);
		logger.debug("id_perf:"+id_perf);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,id_cmd);
			stm.setLong(2,id_perf);

			int i = stm.executeUpdate();
			if(i>0)
				res = true;

		} catch (Exception e) {
			logger.debug("Problema elimCmdPerf : "+ e);
			throw new PerfilesDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : elimCmdPerf - Problema SQL (close)", e);
			}
		}
		return res;
	}

	
	/**
	 * Elimina comandos a un perfil
	 * 
	 * @param  id_perf
	 * @return boolean
	 * @throws PerfilesDAOException
	 */
	public boolean elimCmdsByIdPerf(long id_perf) throws PerfilesDAOException {
		boolean res = false;
		PreparedStatement stm = null;
		
		String sql = " DELETE FROM bo_comxperf WHERE id_perfil =? ";
		logger.debug("elimCmdsByIdPerf");
		logger.debug(sql);
		logger.debug("id_perf:"+id_perf);
			
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,id_perf);

			int i = stm.executeUpdate();
			if(i>0)
				res = true;

		} catch (Exception e) {
			logger.debug("Problema elimCmdsByIdPerf : "+ e);
			throw new PerfilesDAOException(e);
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
	 * Consulta si un perfil tiene permiso para ejecutar un comando
	 * 
	 * @param in_id_perfiles 
	 * @param id_comando
	 * @return boolean true si tiene permiso, false si no tiene
	 */
	public boolean doCheckPermisoPerfilComando(String in_id_perfiles, long id_comando)
		throws PerfilesDAOException {
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		int 	cuenta 		= 0;
		
		logger.debug("DAO doCheckPermisoPerfilComando");
		
		String SQLStmt =
			"SELECT count(*) AS cuenta " +
			" FROM bo_comxperf" +
			" WHERE id_comando = ? " +
			" AND id_perfil in (" + in_id_perfiles + ")"
			;

		logger.debug("DAO doCheckPermiso");
		logger.debug("SQL: " + SQLStmt);
		logger.debug("id_comando: " + id_comando);
		logger.debug( SQLStmt );
		
		try {
		
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			
			stm.setLong(1,id_comando);
			//stm.setLong(2,id_perfil);

			rs = stm.executeQuery();
			int i=0;
			while (rs.next()) {
				cuenta = rs.getInt("cuenta");
				i++;
			}
			logger.debug("N regs: " + i);

		} catch (Exception e) {
			logger.debug("Problema doCheckPermisoPerfilComando : "+ e);
			e.printStackTrace();
			throw new PerfilesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : doCheckPermisoPerfilComando - Problema SQL (close)", e);
			}
		}
		logger.debug( "Tot regs: " + cuenta );
		
		if ( cuenta > 0 )
			return true;
		else
			return false;
		
	}


	
	

}
