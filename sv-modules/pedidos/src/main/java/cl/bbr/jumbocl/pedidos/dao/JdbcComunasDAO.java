package cl.bbr.jumbocl.pedidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cl.bbr.jumbocl.pedidos.dto.ComunaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaxComunaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.ComunasDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Clase que permite consultar las comunas que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcComunasDAO implements ComunasDAO{

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
	 * @throws ComunasDAOException 
	 */
	public void setTrx(JdbcTransaccion trx)
			throws ComunasDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new ComunasDAOException(e);
		}
	}

	
	/**
	 * Obtiene listado de comunas
	 * 
	 * @return List of ComunaDTO's
	 * @throws ComunasDAOException
	 */
	public List getComunasAll() throws ComunasDAOException {

		List result = new ArrayList();
		
		PreparedStatement stm=null;
		ResultSet rs =null;
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(
							" SELECT id_comuna, nombre " +
							" FROM bo_comunas "
							);
			
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
			throw new ComunasDAOException(String.valueOf(e.getErrorCode()),e);
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
	 * Obtiene listado de Zonas de despacho para una comuna
	 * 
	 * @param  id_comuna long 
	 * @return List of ZonaxComunaDTO's
	 * @throws ComunasDAOException
	 */
	public List getZonasxComuna(long id_comuna) throws ComunasDAOException {

		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		/*String SQLStm = 
			"SELECT c.id_comuna, c.id_zona, c.orden, " +
			"       z.nombre, z.descripcion " +
			"FROM bodba.bo_comunaxzona c " +
			"     JOIN bodba.bo_zonas z ON z.id_zona = c.id_zona " +
			"WHERE id_comuna = ? "
			;*/
		String SQLStm = "SELECT L.ID_LOCAL, L.NOM_LOCAL, C.ID_COMUNA, Z.ID_ZONA, Z.ORDEN_ZONA AS ORDEN, Z.NOMBRE AS NOM_ZONA, Z.DESCRIPCION "
                      + "FROM BODBA.BO_COMUNAS C "
                      + "     JOIN BODBA.BO_POLIGONO P ON P.ID_COMUNA = C.ID_COMUNA "
                      + "     JOIN BODBA.BO_ZONAS    Z ON Z.ID_ZONA   = P.ID_ZONA "
                      + "     JOIN BODBA.BO_LOCALES  L ON L.ID_LOCAL  = Z.ID_LOCAL "
                      + "WHERE C.ID_COMUNA = ? "
                      + "GROUP BY L.ID_LOCAL, L.NOM_LOCAL, C.ID_COMUNA, Z.ID_ZONA, Z.ORDEN_ZONA, Z.NOMBRE, Z.DESCRIPCION";
		
		logger.debug("SQL: " + SQLStm);
		logger.debug("id_comuna: " + id_comuna);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStm );
			
			stm.setLong(1, id_comuna);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				ZonaxComunaDTO zxc = new ZonaxComunaDTO();
				zxc.setId_local(rs.getLong("ID_LOCAL"));
				zxc.setId_comuna(rs.getLong("ID_COMUNA"));
				zxc.setId_zona(rs.getLong("ID_ZONA"));
				zxc.setOrden(rs.getInt("ORDEN"));
				zxc.setNom_local(rs.getString("NOM_LOCAL"));
				zxc.setNom_zona(rs.getString("NOM_ZONA"));
				zxc.setDesc_zona(rs.getString("DESCRIPCION"));
				result.add(zxc);
			}
			rs.close();
			stm.close();
			releaseConnection();
		}catch (SQLException e) {
			throw new ComunasDAOException(String.valueOf(e.getErrorCode()),e);
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
	 * (J)
	 * @return
	 * @throws ComunasDAOException
	 */
	public HashSet getComunasSusceptibleFraude() throws ComunasDAOException {
	   HashSet set = new HashSet();
	   String sql = "select id_comuna from bodba.bo_comunas where susceptible_fraude = 1 ";
	   
	   ResultSet rs = null;
	   PreparedStatement ps = null;
	   try {
		   conn = this.getConnection();
         ps = conn.prepareStatement(sql + " WITH UR ");
         rs = ps.executeQuery();
         while(rs.next()){
            Integer id = new Integer(rs.getInt("id_comuna"));
            set.add(id);
         }
	   } catch (SQLException e) {
	      throw new ComunasDAOException(e);
	   }finally{
            try {
               if(rs != null)
                  rs.close();
               if(ps != null)
                  ps.close();
               releaseConnection();
            } catch (SQLException e1) {
               e1.printStackTrace();
            }
	   }
	   return set;
	}
	
	

	
	/**
	 * Actualiza el orden de una relación comuna-zona
	 * 
	 * @param  id_zona long 
	 * @param  id_comuna long 
	 * @param  orden int 
	 * 
	 * @throws ComunasDAOException
	 */
	/*public void doActualizaOrdenZonaxComuna(long id_zona, long id_comuna, int orden) throws ComunasDAOException {
		
		
		PreparedStatement stm = null;
		int	rc;

		String SQLStmt = "UPDATE bo_comunaxzona " +
				         "   SET orden = ? " +
				         "WHERE id_zona = ? " + 
				         "  AND id_comuna = ? ";
		
		logger.debug("Ejecutando DAO doActualizaOrdenZonaxComuna");
		logger.debug("SQL: " + SQLStmt);
		logger.debug("orden: " + orden);
		logger.debug("id_zona: " + id_zona);
		logger.debug("id_comuna: " + id_comuna);
		
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );

			stm.setLong(1,orden);
			stm.setLong(2,id_zona);
			stm.setLong(3,id_comuna);
			
			rc = stm.executeUpdate();
			
			logger.debug("rc: " + rc);
			stm.close();
			releaseConnection();
		} catch (SQLException e) {
			logger.debug("Problema en doActualizaOrdenZonaxComuna:"+ e);
			throw new ComunasDAOException(e);
		}finally {
            try {
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		
	}*/

	
	
	
}
