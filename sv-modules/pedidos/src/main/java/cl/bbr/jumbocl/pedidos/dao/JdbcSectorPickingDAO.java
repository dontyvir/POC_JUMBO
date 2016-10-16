package cl.bbr.jumbocl.pedidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cl.bbr.jumbocl.pedidos.dto.SectorLocalDTO;
import cl.bbr.jumbocl.pedidos.exceptions.ComunasDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.SectorPickingDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Clase que permite consultar las comunas que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcSectorPickingDAO implements SectorPickingDAO{

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
	 */
	private Connection getConnection()throws SQLException{
		
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
			throws SectorPickingDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new SectorPickingDAOException(e);
		}
	}

	/**
	 * Agrega un sector de picking
	 * @param sector SectorLocalDTO 
	 */		
	public void doAddSectorPicking(SectorLocalDTO sector) throws SectorPickingDAOException {		
		PreparedStatement stm = null;
		int	rc;
			
		try {
			String SQLStmt = "INSERT INTO bo_sector " +
					" (nombre, max_prod, max_op, min_op_fill, cant_min_prods ) " +
					" VALUES(?,?,?,?,?) ";
			logger.debug(SQLStmt);
			
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );

			//stm.setLong(1,sector.getId_local());
			stm.setString(1,sector.getNombre());
			stm.setLong(2,sector.getMax_prod());
			stm.setLong(3,sector.getMax_op());
			stm.setLong(4,sector.getMin_op_fill());
			stm.setLong(5,sector.getCant_min_prods());
			
			logger.debug("nombre:"+sector.getNombre());
			logger.debug("id_sector:"+sector.getId_sector());
			logger.debug("max_prod:"+sector.getMax_prod());
			logger.debug("max_op:"+sector.getMax_op());
			logger.debug("min_op_fill:"+sector.getMin_op_fill());
			logger.debug("cant_min_prods:"+sector.getCant_min_prods());
			
			rc = stm.executeUpdate();
			logger.debug("rc: " + rc);
			stm.close();
			releaseConnection();
		} catch (SQLException e) {
			logger.debug("Problema en doAddSectorPicking:"+ e.getMessage());
			throw new SectorPickingDAOException(e);
		}finally {
            try {
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
	}
	/**
	 * Actualiza un sector de picking
	 * 
	 * @param  sector SectorLocalDTO 
	 */	
	public void doActualizaSectorPicking(SectorLocalDTO sector) throws SectorPickingDAOException {
		PreparedStatement stm = null;
		int	rc;

		String SQLStmt = "UPDATE bo_sector " +
				" set nombre 		= ? , " +
				" max_prod 			= ? , " +
				" max_op 			= ? , " +
				" min_op_fill 		= ? , " +
				" cant_min_prods    = ?  " +
				" where id_sector 	= ? ";
		logger.debug(SQLStmt);
		try {			

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );

			stm.setString(1,sector.getNombre());						
			stm.setLong(2,sector.getMax_prod());
			stm.setLong(3,sector.getMax_op());
			stm.setLong(4,sector.getMin_op_fill());
			stm.setLong(5,sector.getCant_min_prods());
			stm.setLong(6,sector.getId_sector());
			logger.debug("nombre:"+sector.getNombre());			
			logger.debug("max_prod:"+sector.getMax_prod());
			logger.debug("max_op:"+sector.getMax_op());
			logger.debug("min_op_fill:"+sector.getMin_op_fill());
			logger.debug("id_sector:"+sector.getId_sector());
			logger.debug("cant_min_prods:"+sector.getCant_min_prods());
			
			rc = stm.executeUpdate();
			logger.debug("rc: " + rc);
			stm.close();
			releaseConnection();
		} catch (SQLException e) {
			logger.debug("Problema en doActualizaSectorPicking:"+ e);
			throw new SectorPickingDAOException(e);
		}finally {
            try {
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		
	}
	/**
	 * Elimina un sector de picking
	 * 
	 * @param  id_sector
	 */
	public void doEliminaSectorPicking(long id_sector) throws SectorPickingDAOException {
		PreparedStatement stm = null;
		int	rc;

		String SQLStmt = "DELETE FROM bo_sector WHERE id_sector = ?";
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );

			stm.setLong(1,id_sector);
			rc = stm.executeUpdate();
			logger.debug("rc: " + rc);
			stm.close();
			releaseConnection();
		} catch (SQLException e) {
			logger.debug("Problema en doEliminaSectorPicking:"+ e);
			throw new SectorPickingDAOException(e);
		}finally {
            try {
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		
	}

	
	
	
}
