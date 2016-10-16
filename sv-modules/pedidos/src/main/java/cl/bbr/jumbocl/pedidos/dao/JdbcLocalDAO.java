package cl.bbr.jumbocl.pedidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.pedidos.dao.JdbcDAOFactory;
import cl.bbr.jumbocl.pedidos.dto.ComunaDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.SectorLocalDTO;
import cl.bbr.jumbocl.pedidos.exceptions.LocalDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Clase que permite consultar los locales que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcLocalDAO implements LocalDAO {
	
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
		return this.conn;

	}
	
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
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
		}
			
	}
	
	
	
	// ************ Métodos Publicos *************** //
	
	/**
	 * Setea una transacción al dao y le asigna su conexión
	 * 
	 * @param  trx JdbcTransaccion 
	 * 
	 * @throws LocalDAOException 
	 */
	public void setTrx(JdbcTransaccion trx)
			throws LocalDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new LocalDAOException(e);
		}
	}


	/**
	 * Retorna un listado de Sectores de Picking de un Local
	 * 
	 * @return List SectorLocalDTO
	 * @throws LocalDAOException
	 */	
	public List getSectores() throws LocalDAOException {
		
		List result = new ArrayList();
		
		PreparedStatement stm=null;
		ResultSet rs =null;
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(
							"SELECT id_sector, nombre, max_prod, max_op, " +
			                "       min_op_fill, cant_min_prods " +
							"FROM bo_sector");
			
			logger.debug("SQL: " + stm.toString());

			rs = stm.executeQuery();
			while (rs.next()) {
				SectorLocalDTO sec = new SectorLocalDTO();
				sec.setId_sector(rs.getLong("id_sector"));
				sec.setNombre(rs.getString("nombre"));
				sec.setMax_prod(rs.getLong("max_prod"));
				sec.setMax_op(rs.getLong("max_op"));
				sec.setMin_op_fill(rs.getLong("min_op_fill"));
				sec.setCant_min_prods(rs.getLong("cant_min_prods"));
				result.add(sec);
			}
			rs.close();
			stm.close();
			releaseConnection();
		}catch (SQLException e) {
			throw new LocalDAOException(String.valueOf(e.getErrorCode()),e);
		}finally {
            try {
            	if (rs != null) rs.close();
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		logger.debug("Se listaron:"+result.size());
		return result;
		
	
	}
	


	
	/**
	 * Obtiene listado de comunas de un local
	 * 
	 * @param  id_local long 
	 * @return List ComunaDTO
	 * @throws LocalDAOException
	 */
	public List getComunasLocal(long id_local) throws LocalDAOException {
		
		List result = new ArrayList();
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		/*String SQLStmt = 
				" SELECT distinct c.id_comuna, c.nombre " +
				" FROM bo_comunas c, bo_comunaxzona x, bo_zonas z " +
				" WHERE c.id_comuna = x.id_comuna " +
				" AND x.id_zona = z.id_zona " +
				" AND z.id_local = ? "
				;*/
        String SQL = "SELECT DISTINCT C.ID_COMUNA, C.NOMBRE "
                   + "FROM BODBA.BO_LOCALES L "
                   + "     JOIN BODBA.BO_ZONAS    Z ON Z.ID_LOCAL  = L.ID_LOCAL "
                   + "     JOIN BODBA.BO_POLIGONO P ON P.ID_ZONA   = Z.ID_ZONA "
                   + "     JOIN BODBA.BO_COMUNAS  C ON C.ID_COMUNA = P.ID_COMUNA "
                   + "WHERE L.ID_LOCAL = ?";
		logger.debug("Ejecución DAO getComunasLocal()");
		logger.debug("SQL: " + SQL);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQL );
			
			stm.setLong(1,id_local);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				ComunaDTO com = new ComunaDTO();
				com.setId_comuna(rs.getLong("id_comuna"));
				com.setNombre(rs.getString("nombre"));
				result.add(com);
			}
			rs.close();
			stm.close();
			releaseConnection();
		}catch (SQLException e) {
			throw new LocalDAOException(String.valueOf(e.getErrorCode()),e);
		}finally {
            try {
            	if (rs != null) rs.close();
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		logger.debug("Se listaron:"+result.size());
		return result;
		
	}



	/**
	 * Retorna listado de locales
	 * 
	 * @return List of LocalDTO's
	 * @throws LocalDAOException
	 */
	public List getLocalesAll() throws LocalDAOException {
		
		List result = new ArrayList();
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		String SQLStmt = 
				" SELECT id_local, cod_local, nom_local, orden, fecha_carga_precios " +
				" FROM bo_locales "
				;

		logger.debug("Ejecución DAO getLocalesAll()");
		logger.debug("SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			
			rs = stm.executeQuery();
			while (rs.next()) {
				LocalDTO local = new LocalDTO();
				local.setId_local(rs.getLong("id_local"));
				local.setCod_local(rs.getString("cod_local"));
				local.setNom_local(rs.getString("mon_local"));
				local.setFcarga_precios(rs.getString("fecha_carga_precios"));
				result.add(local);
			}
			rs.close();
			stm.close();
			releaseConnection();
		}catch (SQLException e) {
			throw new LocalDAOException(String.valueOf(e.getErrorCode()),e);
		}finally {
            try {
            	if (rs != null) rs.close();
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		logger.debug("Se listaron:"+result.size());
		return result;
	}

	
	/**
	 * Retorna listado de locales
	 * 
	 * @return List of LocalDTO's
	 * @throws LocalDAOException
	 */
	public LocalDTO getLocalSapByCodLocalPos(int cod_local_pos) throws LocalDAOException {
		

		LocalDTO local = new LocalDTO();
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		String SQLStmt = 
				" SELECT id_local, cod_local, nom_local, orden, fecha_carga_precios " +
				" FROM bo_locales " +
				" WHERE cod_local_pos="+cod_local_pos;
				;

		logger.debug("Ejecución DAO getLocalSapByCodLocalPos()");
		logger.debug("SQL: " + SQLStmt);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			rs = stm.executeQuery();
			if (rs.next()) {
				local.setId_local(rs.getLong("id_local"));
				local.setCod_local(rs.getString("cod_local"));
				local.setNom_local(rs.getString("nom_local"));
				local.setFcarga_precios(rs.getString("fecha_carga_precios"));
			}
			rs.close();
			stm.close();
			releaseConnection();
		}catch (SQLException e) {
			e.printStackTrace();
			throw new LocalDAOException(String.valueOf(e.getErrorCode()),e);
		}finally {
            try {
            	if (rs != null) rs.close();
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		return local;
	}
	
	public LocalDTO getLocalPromoByCodLocalSap(String cod_local_sap) throws LocalDAOException {
		

		LocalDTO local = new LocalDTO();
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		String SQLStmt = 
				" SELECT id_local, cod_local, nom_local, orden, fecha_carga_precios, cod_local_promo " +
				" FROM bo_locales " +
				" WHERE cod_local='"+cod_local_sap+"'";
				;

		logger.debug("Ejecución DAO getLocalPromoByCodLocalSap()");
		logger.debug("SQL: " + SQLStmt);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			rs = stm.executeQuery();
			if (rs.next()) {
				local.setId_local(rs.getLong("id_local"));
				local.setCod_local(rs.getString("cod_local"));
				local.setNom_local(rs.getString("nom_local"));
				local.setFcarga_precios(rs.getString("fecha_carga_precios"));
				local.setCod_local_promo(rs.getString("cod_local_promo"));
			}
			rs.close();
			stm.close();
			releaseConnection();
		}catch (SQLException e) {
			e.printStackTrace();
			throw new LocalDAOException(String.valueOf(e.getErrorCode()),e);
		}finally {
            try {
            	if (rs != null) rs.close();
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		return local;
	}
	
	
}
