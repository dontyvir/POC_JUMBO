package cl.bbr.jumbocl.pedidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

public class JdbcExcesoDAO implements ExcesoDAO {

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
		
	/**
	 * Setea una transacción al dao y le asigna su conexión
	 * 
	 * @param  trx JdbcTransaccion 
	 * @throws DAOException 
	 * 
	 * @throws PedidosDAOException
	 */
	public void setTrx(JdbcTransaccion trx) throws DAOException	 {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			logger.error(e);
			throw new DAOException(e);
		}
	}
	
	/**
	 * Constructor
	 *
	 */
	public JdbcExcesoDAO(){}	
	
	/**
	 * Obtiene los ID_DETALLE pickeados
	 * 
	 * @param  id_pedido long 
	 * @return List
	 */
	public List getIdsDetallePedidoPickeado(long idPedido) {
		
		List detallePedidoPickeado = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {

			String sqlQuery ="SELECT DISTINCT(ID_DETALLE) FROM BODBA.BO_DETALLE_PICKING WHERE ID_PEDIDO = ? ";
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sqlQuery + " WITH UR");
			
			stm.setLong(1,idPedido);
			
			//logger.debug("SQL: " + stm.toString());

			rs = stm.executeQuery();
			while (rs.next()) {
				detallePedidoPickeado.add(rs.getString("ID_DETALLE"));
			}

		} catch (SQLException e) {
			logger.error(e);
			detallePedidoPickeado=null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getIdsDetallePedidoPickeado - Problema SQL (close)", e);
			}
		}
		return detallePedidoPickeado;
	}
	
	/**
	 *  Obtiene el precio solicitado del producto comprado. 
	 * 
	 * @param  idDetalle long,  idPedido long
	 * @return long
	 * 
	 */
	public long getPrecioSolicitadoByIdDetalle(long idDetalle, long idPedido) {
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		long precio = 0;

		try {
			String sql = " SELECT PRECIO AS PRECIO_SOLICITADO FROM BODBA.BO_DETALLE_PEDIDO WHERE ID_DETALLE = ? AND ID_PEDIDO = ?";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
							
			stm.setLong(1,idDetalle);
			stm.setLong(2,idPedido);
			
			rs = stm.executeQuery();
			if (rs.next()) {				
				precio = Math.round(rs.getDouble("PRECIO_SOLICITADO"));
			}

		} catch (SQLException e) {
			logger.error(e);
			precio=0;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getPrecioSolicitadoByIdDetalle - Problema SQL (close)", e);
			}
		}
		return precio;
	}
	
	/**
	 * Obtiene el precio pickeado del producto comprado. 
	 * 
	 * @param  idDetalle long,  idPedido long
	 * @return long
	 * 
	 */
	public long getPrecioPickingByIdDetalle(long idDetalle, long idPedido) {
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		long precio = 0;

		try {
			String sql = " SELECT PRECIO AS PRECIO_PICKEADO FROM BODBA.BO_DETALLE_PICKING WHERE ID_DETALLE = ? AND ID_PEDIDO = ? ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
							
			stm.setLong(1,idDetalle);
			stm.setLong(2,idPedido);
			
			rs = stm.executeQuery();
			if (rs.next()) {				
				precio = Math.round(rs.getDouble("PRECIO_PICKEADO"));
			}

		} catch (SQLException e) {
			logger.error(e);
			precio=0;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getPrecioPickingByIdDetalle - Problema SQL (close)", e);
			}
		}
		return precio;
	}
	
	/**
	 *  Obtiene el precio total solicitado del producto comprado. 
	 * 
	 * @param  idDetalle long,  idPedido long
	 * @return long
	 * 
	 */
	public long getPrecioTotalSolicitadoByIdDetalle(long idDetalle, long idPedido) {
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		long precio = 0;

		try {
			String sql = " SELECT (CANT_SOLIC * PRECIO) AS PRECIO_SOLICITADO FROM BODBA.BO_DETALLE_PEDIDO WHERE ID_DETALLE = ? AND ID_PEDIDO = ?";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
							
			stm.setLong(1,idDetalle);
			stm.setLong(2,idPedido);
			
			rs = stm.executeQuery();
			if (rs.next()) {				
				precio = Math.round(rs.getDouble("PRECIO_SOLICITADO"));
			}

		} catch (SQLException e) {
			logger.error(e);
			precio=0;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getPrecioTotalSolicitadoByIdDetalle - Problema SQL (close)", e);
			}
		}
		return precio;
	}
	
	/**
	 * Obtiene el precio total pickeado del producto comprado. 
	 * 
	 * @param  idDetalle long,  idPedido long
	 * @return long
	 * 
	 */
	public long getPrecioTotalPickingByIdDetalle(long idDetalle, long idPedido) {
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		long precio = 0;

		try {
			String sql = " SELECT SUM(CANT_PICK * PRECIO) AS PRECIO_PICKEADO FROM BODBA.BO_DETALLE_PICKING WHERE ID_DETALLE = ? AND ID_PEDIDO = ? ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
							
			stm.setLong(1,idDetalle);
			stm.setLong(2,idPedido);
			
			rs = stm.executeQuery();
			if (rs.next()) {				
				precio = Math.round(rs.getDouble("PRECIO_PICKEADO"));
			}

		} catch (SQLException e) {
			logger.error(e);
			precio=0;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getPrecioTotalPickingByIdDetalle - Problema SQL (close)", e);
			}
		}
		return precio;
	}	
	
	/**
	 * Obtiene la cantidasd total pickeada. 
	 * 
	 * @param  idDetalle long,  idPedido long
	 * @return double
	 * 
	 */
	public double getCantPickingByIdDetalle(long idDetalle, long idPedido) {
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		double cantPick = 0;

		try {
			String sql = " SELECT SUM(CANT_PICK) AS CANT_PICKEADA FROM BODBA.BO_DETALLE_PICKING WHERE ID_DETALLE = ? AND ID_PEDIDO = ? ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
							
			stm.setLong(1,idDetalle);
			stm.setLong(2,idPedido);
			
			rs = stm.executeQuery();
			if (rs.next()) {				
				cantPick = rs.getDouble("CANT_PICKEADA");
			}

		} catch (SQLException e) {
			logger.error(e);
			cantPick=0;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCantPickingByIdDetalle - Problema SQL (close)", e);
			}
		}
		return cantPick;
	}
	
	/**
	 * Obtiene datos del detalle de picking. 
	 * 
	 * @param  idDetalle long,  idPedido long
	 * @return List
	 */	
	public DetallePickingDTO getDetallePickingByIdDpicking(long idDpicking) {
	
		DetallePickingDTO oDpick = null;
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = " SELECT ID_DPICKING, ID_DETALLE, ID_BP, ID_PRODUCTO, ID_PEDIDO, CBARRA, DESCRIPCION, " +
					" PRECIO, CANT_PICK, SUSTITUTO, AUDITADO, PRECIO_PICK, (PRECIO * CANT_PICK) AS TOTAL_PICKEADO " +
					" FROM BODBA.BO_DETALLE_PICKING WHERE ID_DPICKING = ? ";

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setLong(1,idDpicking);

			rs = stm.executeQuery();
			if(rs.next()) {
				oDpick = new DetallePickingDTO();	
				oDpick.setId_dpicking(rs.getLong("ID_DPICKING"));
				oDpick.setId_detalle(rs.getLong("ID_DETALLE"));
				oDpick.setId_bp(rs.getLong("ID_BP"));
				oDpick.setId_producto(rs.getLong("ID_PRODUCTO"));
				oDpick.setId_pedido(rs.getLong("ID_PEDIDO"));
				oDpick.setCBarra(rs.getString("CBARRA"));
				oDpick.setDescripcion(rs.getString("DESCRIPCION"));
				oDpick.setPrecio(rs.getDouble("PRECIO"));
				oDpick.setCant_pick(rs.getDouble("CANT_PICK"));
				oDpick.setSustituto(rs.getString("SUSTITUTO"));
				oDpick.setAuditado(rs.getString("AUDITADO"));
				oDpick.setPrecio_picking_ori(rs.getDouble("PRECIO_PICK"));
				oDpick.setTotal_pickeado(rs.getDouble("TOTAL_PICKEADO"));
			}

		} catch (SQLException e) {
			logger.error(e);
			oDpick = null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getDetallePickingByIdDetalle - Problema SQL (close)", e);
			}
		}
		
		return oDpick;
	}
	
	/**
	 * Obtiene datos del detalle de picking. 
	 * 
	 * @param  idDetalle long,  idPedido long
	 * @return List
	 */	
	public List getDetallePickingByIdDetalle(long idDetalle, long idPedido) {
	
		List ListPickingByIdDetalle = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = " SELECT ID_DPICKING, ID_DETALLE, ID_BP, ID_PRODUCTO, ID_PEDIDO, CBARRA, DESCRIPCION, " +
					" PRECIO, CANT_PICK, SUSTITUTO, AUDITADO, PRECIO_PICK, (PRECIO * CANT_PICK) AS TOTAL_PICKEADO " +
					" FROM BODBA.BO_DETALLE_PICKING WHERE ID_DETALLE = ? AND ID_PEDIDO = ? ";

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setLong(1,idDetalle);
			stm.setLong(2,idPedido);

			rs = stm.executeQuery();
			while(rs.next()) {
				DetallePickingDTO oDpick = new DetallePickingDTO();	
				oDpick.setId_dpicking(rs.getLong("ID_DPICKING"));
				oDpick.setId_detalle(rs.getLong("ID_DETALLE"));
				oDpick.setId_bp(rs.getLong("ID_BP"));
				oDpick.setId_producto(rs.getLong("ID_PRODUCTO"));
				oDpick.setId_pedido(rs.getLong("ID_PEDIDO"));
				oDpick.setCBarra(rs.getString("CBARRA"));
				oDpick.setDescripcion(rs.getString("DESCRIPCION"));
				oDpick.setPrecio(rs.getDouble("PRECIO"));
				oDpick.setCant_pick(rs.getDouble("CANT_PICK"));
				oDpick.setSustituto(rs.getString("SUSTITUTO"));
				oDpick.setAuditado(rs.getString("AUDITADO"));
				oDpick.setAuditado(rs.getString("AUDITADO"));
				oDpick.setPrecio_picking_ori(rs.getDouble("PRECIO_PICK"));
				oDpick.setTotal_pickeado(rs.getDouble("TOTAL_PICKEADO"));
				ListPickingByIdDetalle.add(oDpick);
			}

		} catch (SQLException e) {
			logger.error(e);
			ListPickingByIdDetalle=null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getDetallePickingByIdDetalle - Problema SQL (close)", e);
			}
		}
		
		return ListPickingByIdDetalle;
	}
	
	/**
	 * Obtiene el detalle de pedido, segun id del detalle y numero de OP. 
	 * 
	 * @param  id_detalle long 
	 * @return prodSolicitadouctosPedidoDTO
	 * 
	 */
	public ProductosPedidoDTO getDetallePedidoByIdDetalle(long idDetalle, long idPedido) {
		
		ProductosPedidoDTO prodSolicitado = null;
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
		 
			String sql = " SELECT ID_DETALLE, ID_PEDIDO, ID_SECTOR, ID_PRODUCTO, COD_PROD1, UNI_MED, PRECIO, DESCRIPCION, CANT_SOLIC," +
					" CANT_PICK, CANT_FALTAN, CANT_SPICK, OBSERVACION, PREPARABLE, PESABLE, CON_NOTA," +
					" PRECIO_LISTA, DSCTO_ITEM, TIPO_SEL, ID_CRITERIO, DESC_CRITERIO " +
					" FROM BODBA.BO_DETALLE_PEDIDO WHERE ID_DETALLE = ? AND ID_PEDIDO = ? ";

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
							
			stm.setLong(1,idDetalle);
			stm.setLong(2,idPedido);

			rs = stm.executeQuery();
			if (rs.next()) {
				
				prodSolicitado = new ProductosPedidoDTO();
				prodSolicitado.setId_detalle(rs.getInt("ID_DETALLE"));				
				prodSolicitado.setId_pedido(rs.getLong("ID_PEDIDO"));
				prodSolicitado.setId_sector(rs.getInt("ID_SECTOR"));
				prodSolicitado.setId_producto(rs.getInt("ID_PRODUCTO"));
				prodSolicitado.setCod_sap(rs.getString("COD_PROD1"));
				prodSolicitado.setUnid_medida(rs.getString("UNI_MED"));
				prodSolicitado.setPrecio(rs.getDouble("PRECIO"));
				prodSolicitado.setDescripcion(rs.getString("DESCRIPCION"));
				prodSolicitado.setCant_solic(rs.getDouble("CANT_SOLIC"));
				prodSolicitado.setCant_pick(rs.getDouble("CANT_PICK"));
				prodSolicitado.setCant_faltan(rs.getDouble("CANT_FALTAN"));
				prodSolicitado.setCant_spick(rs.getDouble("CANT_SPICK"));				
				prodSolicitado.setObservacion(rs.getString("OBSERVACION"));
				prodSolicitado.setPreparable(rs.getString("PREPARABLE"));				
				prodSolicitado.setPesable(rs.getString("PESABLE"));
				prodSolicitado.setCon_nota(rs.getString("CON_NOTA"));		
				prodSolicitado.setPrecio_lista(rs.getDouble("PRECIO_LISTA"));
				prodSolicitado.setDscto_item(rs.getDouble("DSCTO_ITEM"));
				prodSolicitado.setTipo_sel(rs.getString("TIPO_SEL"));
				prodSolicitado.setIdCriterio(rs.getInt("ID_CRITERIO"));
				prodSolicitado.setDescCriterio(rs.getString("DESC_CRITERIO"));
			}

		} catch (SQLException e) {
			logger.error(e);
			prodSolicitado=null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getDetallePedidoByIdDetalle - Problema SQL (close)", e);
			}
		}
		return prodSolicitado;
	}

	public boolean updatePrecioPickingOriginal(long idDetalle, long idPedido) {
		// TODO Apéndice de método generado automáticamente
		boolean result = false;
		PreparedStatement stm=null;
		
		try {
			String sql = " UPDATE BODBA.BO_DETALLE_PICKING SET PRECIO_PICK = PRECIO WHERE ID_DETALLE = ? AND ID_PEDIDO = ? ";

			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
	
			stm.setInt(1, (int)idDetalle);
			stm.setInt(2, (int)idPedido);


			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			logger.error(e);
			result = false;
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : updatePrecioPickingOriginal - Problema SQL (close)", e);
			}
		}
		return result;
		
	}

	public boolean updatePrecioPickXPrecioSolicitado(long idDetalle, long idPedido) {
		// TODO Apéndice de método generado automáticamente
		boolean result = false;
		PreparedStatement stm=null;
		
		try {
			String sql = " UPDATE BODBA.BO_DETALLE_PICKING SET PRECIO = (SELECT PRECIO FROM BODBA.BO_DETALLE_PEDIDO WHERE ID_DETALLE = ? AND ID_PEDIDO = ? ) " +
					"WHERE ID_DETALLE = ? AND ID_PEDIDO = ? ";

			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
	
			stm.setInt(1, (int)idDetalle);
			stm.setInt(2, (int)idPedido);
			stm.setInt(3, (int)idDetalle);
			stm.setInt(4, (int)idPedido);

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			logger.error(e);
			result = false;
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : updatePrecioPickXPrecioSolicitado - Problema SQL (close)", e);
			}
		}
		
		return result;		
	}
	
	public boolean updatePrecioPickXPrecioSolicitado(long idDetalle, long idPedido, long idDpicking) {
		// TODO Apéndice de método generado automáticamente
		boolean result = false;
		PreparedStatement stm=null;
		
		try {
			String sql = " UPDATE BODBA.BO_DETALLE_PICKING SET PRECIO = (SELECT PRECIO FROM BODBA.BO_DETALLE_PEDIDO WHERE ID_DETALLE = ? AND ID_PEDIDO = ? ) " +
					"WHERE ID_DPICKING = ?  ";

			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
	
			stm.setInt(1, (int)idDetalle);
			stm.setInt(2, (int)idPedido);
			stm.setInt(3, (int)idDpicking);

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			logger.error(e);
			result = false;
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : updatePrecioPickXPrecioSolicitado - Problema SQL (close)", e);
			}
		}
		
		return result;		
	}
	
	
	public boolean updatePrecioPickXPrecioSegunCantidad(long idDetalle, long idPedido) {
		// TODO Apéndice de método generado automáticamente
		boolean result = false;
		PreparedStatement stm=null;
		
		try {
			
			String sql = " UPDATE BO_DETALLE_PICKING SET " +
					" PRECIO = " +
					" (SELECT CAST (ROUND(" +
					" (SELECT (PRECIO * CANT_SOLIC) FROM BO_DETALLE_PEDIDO WHERE ID_DETALLE = ? AND ID_PEDIDO = ?) /" +
					" (SELECT SUM(CANT_PICK) FROM BO_DETALLE_PICKING WHERE ID_DETALLE = ? AND ID_PEDIDO = ?),2) AS DECIMAL(10,0))" +
					" FROM SYSIBM.SYSDUMMY1)" +
					" WHERE ID_DETALLE = ? AND ID_PEDIDO = ? ";
			

			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
	
			stm.setInt(1, (int)idDetalle);
			stm.setInt(2, (int)idPedido);
			stm.setInt(3, (int)idDetalle);
			stm.setInt(4, (int)idPedido);
			stm.setInt(5, (int)idDetalle);
			stm.setInt(6, (int)idPedido);

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			logger.error(e);
			result = false;
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : updatePrecioPickXPrecioSegunCantidad - Problema SQL (close)", e);
			}
		}
		return result;		
	}	

	public List getIdsDpickingByIdDetalle(long idDetalle, long idPedido) {
	
		List ListIdsDpicking = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = " SELECT ID_DPICKING FROM BODBA.BO_DETALLE_PICKING WHERE ID_DETALLE = ? AND ID_PEDIDO = ? ";

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setLong(1,idDetalle);
			stm.setLong(2,idPedido);

			rs = stm.executeQuery();
			while(rs.next()) {
				ListIdsDpicking.add(String.valueOf(rs.getString("ID_DPICKING")));
			}

		} catch (SQLException e) {
			logger.error(e);
			ListIdsDpicking=null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getIdsDpickingByIdDetalle - Problema SQL (close)", e);
			}
		}
		
		return ListIdsDpicking;
	}
	
	/**
	 * @param id_pedido
	 * @return
	 */
	public long getTotalDetPickingByOP(long id_pedido) {
		
        PreparedStatement stm = null;
        ResultSet rs = null;
        long total = 0;
        
        String  sql = "SELECT SUM(CANT_PICK * PRECIO) TOTAL_PICKING FROM BODBA.BO_DETALLE_PICKING WHERE ID_PEDIDO = ? ";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql  + " WITH UR");
            
            stm.setLong(1,id_pedido);
            rs = stm.executeQuery();
            
            if (rs.next()) {
                total = Math.round(rs.getDouble("TOTAL_PICKING"));
            }

        } catch (Exception e) {
            logger.error("getTotalDetPickingByOP - Problema SQL", e);
            total = 0;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("getTotalDetPickingByOP - Problema SQL (close)", e);
			}
		}
        return total ;
	}

	public List getPedidosByEstado(long idEstado) {
		List ListIdsDpicking = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;

		try {
			String sql = " SELECT ID_PEDIDO FROM BODBA.BO_PEDIDOS WHERE ID_ESTADO = ? ";

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setLong(1,idEstado);

			rs = stm.executeQuery();
			while(rs.next()) {
				ListIdsDpicking.add(String.valueOf(rs.getString("ID_PEDIDO")));
			}

		} catch (SQLException e) {
			logger.error(e);
			ListIdsDpicking=null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getIdsDpickingByIdDetalle - Problema SQL (close)", e);
			}
		}
		
		return ListIdsDpicking;
	}

	public boolean deletePickingByIdDpicking(long idDpicking) {
		// TODO Apéndice de método generado automáticamente
		boolean result = false;
		PreparedStatement stm=null;
		
		try {
			
			String sql = " DELETE BODBA.BO_DETALLE_PICKING WHERE ID_DPICKING = ? ";
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);	
			stm.setLong(1, idDpicking);
	
			int i = stm.executeUpdate();
			if(i>0)
				result = true;
	
		} catch (SQLException e) {
			logger.error(e);
			result = false;
		} finally {
			try {
	
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : deletePickingByIdDpicking - Problema SQL (close)", e);
			}
		}
		return result;		
	}

	public List getTrackingExcesoByOP(long idPedido) {
		List listTrackingExceso = new ArrayList();
		PreparedStatement stm=null;
		ResultSet rs=null;
	
		try {
			
			String sql  =" SELECT ID_TRACKING, ID_PEDIDO, USUARIO, DESCRIPCION, to_char(FECHA, 'DD-MM-YYYY HH24:MI:SS') as FECHA" +
					" FROM BO_TRACKING_OD" +
					" WHERE (USUARIO='EXCESO' OR DESCRIPCION like '[BOC-EXCESO]%')" +
					" AND ID_PEDIDO= ? ORDER BY FECHA";
	
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setLong(1,idPedido);
	
			rs = stm.executeQuery();
			while(rs.next()) {
				LogPedidoDTO oLog = new LogPedidoDTO();
				oLog.setId_log(rs.getLong("ID_TRACKING"));
				oLog.setId_pedido(rs.getLong("ID_PEDIDO"));
				oLog.setUsuario(rs.getString("USUARIO"));
				oLog.setLog(rs.getString("DESCRIPCION"));
				oLog.setFecha(rs.getString("FECHA"));
				listTrackingExceso.add(oLog);
			}
	
		} catch (SQLException e) {
			logger.error(e);
			listTrackingExceso=null;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getTrackingExcesoByOP - Problema SQL (close)", e);
			}
		}
		
		return listTrackingExceso;
	}
	
	public boolean ajustarCantidadSolicitada(long idDetalle, long idPedido) {
		// TODO Apéndice de método generado automáticamente
		boolean result = false;
		PreparedStatement stm=null;
		
		try {
			
			String sql = " UPDATE BO_DETALLE_PICKING SET " +
					" CANT_PICK = (SELECT CANT_SOLIC FROM BO_DETALLE_PEDIDO WHERE ID_DETALLE = ? AND ID_PEDIDO = ?)" +
					" WHERE ID_DETALLE = ? AND ID_PEDIDO = ? ";
			
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
	
			stm.setLong(1, idDetalle);
			stm.setLong(2, idPedido);
			stm.setLong(3, idDetalle);
			stm.setLong(4, idPedido);

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		} catch (SQLException e) {
			logger.error(e);
			result = false;
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : ajustarCantidadSolicitada - Problema SQL (close)", e);
			}
		}
		return result;
	}
	

	public void recalculoCantidad(long idDetalle, long idPedido, long idDpicking) {
		// TODO Apéndice de método generado automáticamente
		boolean result = false;
		PreparedStatement stm=null;
		String sql = "";
		try {
			
			//Se ajusto cantidad
			if(idDetalle != 0){
				sql = " UPDATE BO_DETALLE_PEDIDO SET " +
						" CANT_PICK   = (SELECT SUM(CANT_PICK) FROM BO_DETALLE_PICKING WHERE ID_DETALLE = ? AND ID_PEDIDO = ?)" +
						" WHERE ID_DETALLE = ? AND ID_PEDIDO = ? ";
				
				conn = this.getConnection();
				stm = conn.prepareStatement(sql);
		
				stm.setLong(1, idDetalle);
				stm.setLong(2, idPedido);
				stm.setLong(3, idDetalle);
				stm.setLong(4, idPedido);
			}
			else if(idDpicking != 0){//Se eliminino un registro de picking
				sql = " UPDATE BO_DETALLE_PEDIDO SET " +
						" CANT_PICK  = (SELECT SUM(CANT_PICK) FROM BO_DETALLE_PICKING WHERE ID_DETALLE = (SELECT ID_DETALLE FROM BO_DETALLE_PICKING WHERE ID_DPICKING=? ))" +
						" WHERE ID_DETALLE = ? AND ID_PEDIDO = ? ";
				
				conn = this.getConnection();
				stm = conn.prepareStatement(sql);
		
				stm.setLong(1, idDpicking);
				stm.setLong(2, idDetalle);
				stm.setLong(3, idPedido);
			}
			
			if(stm != null){
				int i = stm.executeUpdate();
				result = (i>0) ? true:false;
			}

		} catch (SQLException e) {
			logger.error(e);
			result = false;
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : recalculoCantidad - Problema SQL (close)", e);
			}
		}		
	}
}

