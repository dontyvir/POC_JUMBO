package cl.bbr.boc.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cl.bbr.boc.dao.CambiaEstadoValidadoDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcDAOFactory;
import cl.bbr.jumbocl.pedidos.exceptions.JornadasDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.jumbo.common.dao.jdbc.JdbcDAO;

public class JdbcCambiaEstadoValidadoDAO extends JdbcDAO implements CambiaEstadoValidadoDAO {
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
            	logger.error(e.getMessage(),e);
            }
		}
			
	}

	
	
	// ************ Métodos Publicos *************** //
	
	/**
	 * Setea una transacción al dao y le asigna su conexión
	 * 
	 * @param  trx JdbcTransaccion 
	 * 
	 * @throws JornadasDAOException 
	 */
	public void setTrx(JdbcTransaccion trx)
			throws DAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new DAOException(e);
		}
	}	

	public int getCountDetallePicking(long id_pedido) throws DAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		int cantidad = 0;

		String sql = " SELECT COUNT(*) AS CANTIDAD FROM BO_DETALLE_PICKING WHERE ID_PEDIDO = ? ";

		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR ");
			stm.setLong(1, id_pedido);
			rs = stm.executeQuery();

			if (rs.next()) {
				cantidad = rs.getInt("CANTIDAD");
			}
		} catch (Exception e) {
			logger.debug("Problema getCountDetallePicking :"+ e);
			throw new DAOException(e);
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
		return cantidad;
	}

	public void registrarTracking(long id_pedido, String usuario, String descripcion) throws DAOException {
		PreparedStatement stm = null;
		try {
			
			conn = this.getConnection();
			String sql = "INSERT INTO BO_TRACKING_OD(ID_PEDIDO, USUARIO, DESCRIPCION) VALUES(?, ?, ?)";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, id_pedido);
			stm.setString(2, usuario);
			stm.setString(3, descripcion);
			stm.executeUpdate();

		} catch (SQLException e) {
			logger.debug("Problema en registrarTracking:"+ e);
			throw new DAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
	}

	public void borrarDetallePicking(long idPedido) throws DAOException {
		PreparedStatement stm = null;
		try {
			conn = this.getConnection();
			String sql = "DELETE FROM BO_DETALLE_PICKING  WHERE ID_PEDIDO= ? ";

			stm = conn.prepareStatement(sql);
			stm.setLong(1, idPedido);
			stm.executeUpdate();

		} catch (SQLException e) {
			logger.debug("Problema en borrarDetallePicking:"+ e);
			throw new DAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}

	}

	public boolean modificarCantidadesDP(long idPedido) throws DAOException {
		boolean result = false;
		PreparedStatement stm = null;
		try {
			
			conn = this.getConnection();
			String sql = " UPDATE BO_DETALLE_PEDIDO SET CANT_FALTAN = 0, CANT_PICK=0, CANT_SPICK = CANT_SOLIC WHERE ID_PEDIDO= ? ";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, idPedido);

			int i = stm.executeUpdate();
			if(i>0){
				result=true;
			}
		} catch (Exception e) {
			logger.debug("Problema modificarCantidadesDP :"+ e);
			throw new DAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	public boolean cambioEstadoOP(long idPedido) throws DAOException {
		boolean result = false;
		PreparedStatement stm = null;
		try {
			
			conn = this.getConnection();
			String sql = " UPDATE BO_PEDIDOS SET ID_ESTADO=5 WHERE ID_PEDIDO= ? ";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, idPedido);

			int i = stm.executeUpdate();
			if(i>0){
				
				result=true;
			}
		} catch (Exception e) {
			logger.debug("Problema cambioEstadoOP :"+ e);
			throw new DAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return result;
	}

	public int getCountTrxMpByIdPedido(long idPedido) throws DAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		int cantidad = 0;

		String sql = " SELECT COUNT(*) AS CANTIDAD FROM BO_TRX_MP WHERE ID_PEDIDO = ? ";

		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR ");
			stm.setLong(1, idPedido);
			rs = stm.executeQuery();

			if (rs.next()) {
				cantidad = rs.getInt("CANTIDAD");
			}
		} catch (Exception e) {
			logger.debug("Problema getCountTrxMpByIdPedido :"+ e);
			throw new DAOException(e);
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
		return cantidad;
	}

	public void DelTrxMP(long idPedido) throws DAOException {
		PreparedStatement stm = null;
		try {
			conn = this.getConnection();
			String sql = " DELETE FROM BO_TRX_MP TMP WHERE TMP.ID_PEDIDO = ? ";

			stm = conn.prepareStatement(sql);
			stm.setLong(1, idPedido);
			stm.executeUpdate();

		} catch (SQLException e) {
			logger.debug("Problema en borrar trx:"+ e);
			throw new DAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}

	}
}
