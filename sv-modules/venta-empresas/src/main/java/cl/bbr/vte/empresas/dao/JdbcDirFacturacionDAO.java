package cl.bbr.vte.empresas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.DirFacturacionEntity;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;
import cl.bbr.vte.empresas.exceptions.DirFacturacionDAOException;
import cl.bbr.vte.empresas.exceptions.DireccionesDAOException;

/**
 * Clase para la interacción con el repositorio de datos.
 * 
 * @author BBR
 *
 */
public class JdbcDirFacturacionDAO implements DirFacturacionDAO{

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
	

	// ************ Métodos Privados *************** //
	
	/**
	 * Obtiene la conexión
	 * 
	 * @return Connection
	 * @throws SQLException 
	 */
	private Connection getConnection() throws SQLException{
		
		if ( conn == null ){
			conn = JdbcDAOFactory.getConexion();
		}
		
		logger.debug("Conexion usada por el dao: " + conn);
		
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
	 * 
	 * @throws DirFacturacionDAOException
	 */
	public void setTrx(JdbcTransaccion trx)	throws DirFacturacionDAOException {
		
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new DirFacturacionDAOException(e);
		}
	}

	public JdbcDirFacturacionDAO() {
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DirFacturacionDAO#getListDireccionFactBySucursal(long)
	 */
	public List getListDireccionFactBySucursal(long id_sucursal) throws DirFacturacionDAOException {
		List result = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try{
			conn = this.getConnection();
			String sql = "SELECT dfac_id, tip_nombre, dfac_calle, dfac_numero, dfac_depto, "
                       + "       nombre, R.reg_nombre nom_region, dfac_alias " 
                       + "FROM ve_dirfact " 
                       + "     JOIN fo_tipo_calle ON tip_id = dfac_tip_id " 
                       + "     JOIN BO_COMUNAS_GENERAL C ON C.comuna_id = dfac_com_id " 
                       + "     JOIN bo_regiones R ON R.reg_id = C.reg_id " 
                       + "WHERE	dfac_cli_id = ?  "
                       + "  AND dfac_estado = '"+Constantes.ESTADO_ACTIVADO+"' ";

			logger.debug("En  getListDireccionFactBySucursal()...");
			logger.debug("SQL: " + sql);
			logger.debug("id_sucursal: " + id_sucursal);

			stm = conn.prepareStatement( sql + " WITH UR");
			stm.setLong(1,id_sucursal);
			
			rs = stm.executeQuery();
			DirFacturacionEntity ent = null;
			while (rs.next()) {
				ent = new DirFacturacionEntity();
				ent.setDfac_id(new Long(rs.getLong("dfac_id")));
				ent.setNom_tip_calle(rs.getString("tip_nombre"));
				ent.setDfac_calle(rs.getString("dfac_calle"));
				ent.setDfac_numero(rs.getString("dfac_numero"));
				ent.setDfac_depto(rs.getString("dfac_depto"));
				ent.setNom_comuna(rs.getString("nombre"));
				ent.setNom_region(rs.getString("nom_region"));
				ent.setDfac_alias(rs.getString("dfac_alias"));
				//ent.setNom_local("");
				result.add(ent);
			}


		}catch (SQLException e) {
			throw new DirFacturacionDAOException(String.valueOf(e.getErrorCode()),e);
		}catch (Exception e) {
			logger.error( "Problema getListDireccionFactBySucursal :", e);
			throw new DirFacturacionDAOException();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListDireccionFactBySucursal - Problema SQL (close)", e);
			}
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DirFacturacionDAO#insDirFacturacion(cl.bbr.vte.empresas.dto.DirFacturacionDTO)
	 */
	public long insDirFacturacion(DirFacturacionDTO dto) throws DirFacturacionDAOException {
		long id = -1;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try{
			conn = this.getConnection();
			String sql = 
					" INSERT INTO ve_dirfact (dfac_tip_id, dfac_cli_id, dfac_com_id, dfac_alias, " +
					"  dfac_calle, dfac_numero, dfac_depto, dfac_comentarios, dfac_estado, dfac_ciudad, dfac_fax, " +
					"  dfac_nom_contacto, dfac_cargo, dfac_email, dfac_fono1, dfac_fono2, dfac_fono3, dfac_fec_crea) " +
					" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, CURRENT_TIMESTAMP) ";
			
			logger.debug("En  insDirFacturacion()...");
			logger.debug("SQL: " + sql);
			
			stm = conn.prepareStatement( sql,Statement.RETURN_GENERATED_KEYS);
			stm.setLong(1, dto.getDfac_tip_id());
			stm.setLong(2, dto.getDfac_cli_id());
			stm.setLong(3, dto.getDfac_com_id());
			stm.setString(4, dto.getDfac_alias());
			stm.setString(5, dto.getDfac_calle());
			stm.setString(6, dto.getDfac_numero());
			stm.setString(7, dto.getDfac_depto());
			stm.setString(8, dto.getDfac_comentarios());
			stm.setString(9, dto.getDfac_estado());
			stm.setString(10, dto.getDfac_ciudad());
			stm.setString(11, dto.getDfac_fax());
			stm.setString(12, dto.getDfac_nom_contacto());
			stm.setString(13, dto.getDfac_cargo());
			stm.setString(14, dto.getDfac_email());
			stm.setString(15, dto.getDfac_fono1());
			stm.setString(16, dto.getDfac_fono2());
			stm.setString(17, dto.getDfac_fono3());

			stm.executeUpdate();
			rs = stm.getGeneratedKeys();
			if (rs.next()) {
	            id = rs.getInt(1);
	            logger.debug("id:"+id);
	        }

		}catch (SQLException e) {
			throw new DirFacturacionDAOException(String.valueOf(e.getErrorCode()),e);
		}catch (Exception e) {
			logger.error( "Problema insDirFacturacion :", e);
			throw new DirFacturacionDAOException();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : insDirFacturacion - Problema SQL (close)", e);
			}
		}
		return id;
		
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DirFacturacionDAO#modDirFacturacion(cl.bbr.vte.empresas.dto.DirFacturacionDTO)
	 */
	public boolean modDirFacturacion(DirFacturacionDTO dto) throws DirFacturacionDAOException {
		boolean result = false;
		
		PreparedStatement stm = null;
		
		try{
			conn = this.getConnection();
			String sql = 
					" UPDATE ve_dirfact SET dfac_tip_id = ?, dfac_cli_id = ?, dfac_com_id = ?, dfac_alias = ?, " +
					" dfac_calle = ?, dfac_numero = ?, dfac_depto = ?, dfac_comentarios = ?, dfac_estado = ?, dfac_ciudad = ?, dfac_fax = ?, " +
					" dfac_nom_contacto = ?, dfac_cargo = ?, dfac_email = ?, dfac_fono1 = ?, dfac_fono2 = ?, dfac_fono3 = ? " +
					" WHERE dfac_id = ? ";
			
			logger.debug("En  modDirFacturacion()...");
			logger.debug("SQL: " + sql);
			
			stm = conn.prepareStatement( sql);
			stm.setLong(1, dto.getDfac_tip_id());
			stm.setLong(2, dto.getDfac_cli_id());
			stm.setLong(3, dto.getDfac_com_id());
			stm.setString(4, dto.getDfac_alias());
			stm.setString(5, dto.getDfac_calle());
			stm.setString(6, dto.getDfac_numero());
			stm.setString(7, dto.getDfac_depto());
			stm.setString(8, dto.getDfac_comentarios());
			stm.setString(9, dto.getDfac_estado());
			stm.setString(10, dto.getDfac_ciudad());
			stm.setString(11, dto.getDfac_fax());
			stm.setString(12, dto.getDfac_nom_contacto());
			stm.setString(13, dto.getDfac_cargo());
			stm.setString(14, dto.getDfac_email());
			stm.setString(15, dto.getDfac_fono1());
			stm.setString(16, dto.getDfac_fono2());
			stm.setString(17, dto.getDfac_fono3());
			stm.setLong(18, dto.getDfac_id());

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new DirFacturacionDAOException(String.valueOf(e.getErrorCode()),e);
		}catch (Exception e) {
			logger.error( "Problema modDirFacturacion :", e);
			throw new DirFacturacionDAOException();
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : modDirFacturacion - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DirFacturacionDAO#delDirFacturacion(long)
	 */
	public boolean delDirFacturacion(long id_dir_fact) throws DirFacturacionDAOException {
		boolean result = false;
		
		PreparedStatement stm = null;
		
		try{
			conn = this.getConnection();
			String sql = 
					" UPDATE ve_dirfact SET dfac_estado = ? " +
					" WHERE dfac_id = ? ";
			
			logger.debug("En  modDirFacturacion()...");
			logger.debug("SQL: " + sql);
			
			stm = conn.prepareStatement( sql);
			stm.setString(1, Constantes.ESTADO_ELIMINADO);
			stm.setLong(2, id_dir_fact);

			int i = stm.executeUpdate();
			if(i>0)
				result = true;
			
		}catch (SQLException e) {
			throw new DirFacturacionDAOException(String.valueOf(e.getErrorCode()),e);
		}catch (Exception e) {
			logger.error( "Problema delDirFacturacion :", e);
			throw new DirFacturacionDAOException();
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delDirFacturacion - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.empresas.dao.DirFacturacionDAO#getDireccionesFactByIdFact(long)
	 */
	public DirFacturacionEntity getDireccionesFactByIdFact(long id_dir_fact) throws DirFacturacionDAOException {
		DirFacturacionEntity result = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try{
			conn = this.getConnection();
			String sql = 
					" SELECT	dfac_id, dfac_tip_id, dfac_cli_id, dfac_com_id, dfac_alias, dfac_calle, dfac_numero, dfac_depto, " +
					" dfac_comentarios, dfac_estado, dfac_ciudad, dfac_fax, dfac_nom_contacto, dfac_cargo, dfac_email, " +
					" dfac_fono1, dfac_fono2, dfac_fono3, dfac_fec_crea " +
					" FROM    ve_dirfact " +
					" WHERE	dfac_id = ? ";

			logger.debug("En  getDireccionesFactByIdFact()...");
			logger.debug("SQL: " + sql);
			logger.debug("id_dir_fact: " + id_dir_fact);

			stm = conn.prepareStatement( sql + " WITH UR");
			stm.setLong(1,id_dir_fact);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				result = new DirFacturacionEntity();
				result.setDfac_id(new Long(rs.getLong("dfac_id")));
				result.setDfac_tip_id(new Long(rs.getLong("dfac_tip_id")));
				result.setDfac_cli_id(new Long(rs.getLong("dfac_cli_id")));
				result.setDfac_com_id(new Long(rs.getLong("dfac_com_id")));
				result.setDfac_alias(rs.getString("dfac_alias"));
				result.setDfac_calle(rs.getString("dfac_calle"));
				result.setDfac_numero(rs.getString("dfac_numero"));
				result.setDfac_depto(rs.getString("dfac_depto"));
				result.setDfac_comentarios(rs.getString("dfac_comentarios"));
				result.setDfac_estado(rs.getString("dfac_estado"));
				result.setDfac_ciudad(rs.getString("dfac_ciudad"));
				result.setDfac_fax(rs.getString("dfac_fax"));
				result.setDfac_nom_contacto(rs.getString("dfac_nom_contacto"));
				result.setDfac_cargo(rs.getString("dfac_cargo"));
				result.setDfac_email(rs.getString("dfac_email"));
				result.setDfac_fono1(rs.getString("dfac_fono1"));
				result.setDfac_fono2(rs.getString("dfac_fono2"));
				result.setDfac_fono3(rs.getString("dfac_fono3"));
				result.setDfac_fec_crea(rs.getTimestamp("dfac_fec_crea"));
			}

		}catch (SQLException e) {
			throw new DirFacturacionDAOException(String.valueOf(e.getErrorCode()),e);
		}catch (Exception e) {
			logger.error( "Problema getDireccionesFactByIdFact :", e);
			throw new DirFacturacionDAOException();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getDireccionesFactByIdFact - Problema SQL (close)", e);
			}
		}
		
		return result;
	}
	
	
	public List getListDireccionFactByComprador(long comprador_id) throws DireccionesDAOException {
		List lista = new ArrayList();
		DirFacturacionDTO dto = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			conn = this.getConnection();
			String sql = "SELECT distinct(dfac_id), dfac_cli_id, dfac_alias, dfac_calle, "
                       + "       dfac_numero, dfac_depto, com.nombre, tip.tip_nombre "
                       + "FROM ve_comprxsuc comx "
                       + "     join ve_dirfact         dir on dfac_cli_id = comx.cli_id "
                       + "     join BO_COMUNAS_GENERAL com on dfac_com_id = com.comuna_id " 
                       + "     join fo_tipo_calle      tip on tip.tip_id  = dfac_tip_id "
                       + "WHERE cpr_id = ? "
                       + "  and dfac_estado <> 'E' "
                       + "ORDER BY dfac_cli_id";
			stm = conn.prepareStatement( sql + " WITH UR");
			stm.setLong(1,comprador_id);
			logger.debug("SQL: " + sql);
			logger.debug("comprador_id: " + comprador_id);

			rs = stm.executeQuery();
			
			while (rs.next()) {
				dto = new DirFacturacionDTO();
				dto.setDfac_cli_id(rs.getLong("dfac_cli_id"));
				dto.setDfac_id(rs.getLong("dfac_id"));
				dto.setDfac_alias( rs.getString("dfac_alias") );
				dto.setDfac_calle(rs.getString("dfac_calle"));
				dto.setDfac_numero(rs.getString("dfac_numero"));
				dto.setDfac_depto(rs.getString("dfac_depto"));
				dto.setNom_comuna(rs.getString("nombre"));
				dto.setNom_tip_calle(rs.getString("tip_nombre"));
				lista.add(dto);
			}

		} catch (Exception e) {
			logger.error( "(getListDireccionFactByComprador) Problema", e);
			throw new DireccionesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListDireccionFactByComprador - Problema SQL (close)", e);
			}
		}
		return lista;	
	}	
	
	
}
