package cl.bbr.jumbocl.pedidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.TrxMpDetalleEntity;
import cl.bbr.jumbocl.common.model.TrxMpEncabezadoEntity;
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorTrxMpDTO;
import cl.bbr.jumbocl.pedidos.dto.POSFeedbackProcPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.TrxMpCriteriaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.TrxMedioPagoDAOException;

/**
 * Clase que permite consultar las Transacciones de Medio de pago que se encuentran en la base de datos.
 * 
 * @author BBR
 *
 */
public class JdbcTrxMedioPagoDAO implements TrxMedioPagoDAO {
	
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
	 * Constructor de la clase
	 */
	public JdbcTrxMedioPagoDAO() {
	}
	
	/**
	 * Setea una transacción al dao y le asigna su conexión
	 * 
	 * @param  trx JdbcTransaccion 
	 * 
	 * @throws TrxMedioPagoDAOException 
	 */
	public void setTrx(JdbcTransaccion trx)
			throws TrxMedioPagoDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new TrxMedioPagoDAOException(e);
		}
	}
	/**
	 * Crea Encabezado de transaccion de medio de pagos
	 * 
	 * @param  trxmpenc TrxMpEncabezadoEntity 
	 * @return boolean, devuelve <i>true</i> si la inserción fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws TrxMedioPagoDAOException 
	 *  
	 */
	public boolean setCreaTrxMpEnc(TrxMpEncabezadoEntity trxmpenc) throws TrxMedioPagoDAOException {
		boolean result = false;
		PreparedStatement stm = null;
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"setCreaTrxMpEnc");
		logger.debug("-----------------------------------------------------------------");
		try{
			logger.debug("setCreaTrxMpEnc");
			String SQLStmt = "INSERT INTO BO_TRX_MP " +
					" (id_trxmp, id_pedido, id_estado, " +
					"  id_local, id_cliente, costo_despacho," +
					"  monto_trxmp, cant_productos) " +
					" VALUES(?,?,?,?,?,?,?,?)";
			logger.debug(SQLStmt);
			logger.debug("id_trxmp:"+trxmpenc.getId_trxmp());
			logger.debug("id_pedido:"+trxmpenc.getId_pedido());
			logger.debug("id_estado:"+trxmpenc.getId_estado());
			logger.debug("id_local:"+trxmpenc.getId_local());
			logger.debug("id_cliente:"+trxmpenc.getId_cliente());
			logger.debug("costo_despacho:"+trxmpenc.getCosto_despacho());
			logger.debug("monto_trxmp:"+trxmpenc.getMonto_trxmp());
			logger.debug("cant_productos:"+trxmpenc.getCant_productos());
						
			//con = JdbcDAOFactory.getConexion();
			conn = this.trx == null ? this.getConnection() : this.trx.getConnection();
            
			stm = conn.prepareStatement( SQLStmt );
			
			stm.setLong(1, trxmpenc.getId_trxmp());
			stm.setLong(2, trxmpenc.getId_pedido());
			stm.setLong(3, trxmpenc.getId_estado());
			stm.setLong(4, trxmpenc.getId_local());
			stm.setLong(5, trxmpenc.getId_cliente());
			stm.setDouble(6, trxmpenc.getCosto_despacho());
			stm.setDouble(7, trxmpenc.getMonto_trxmp());
			stm.setInt(8, trxmpenc.getCant_productos());
			
			int i = stm.executeUpdate();
			if (i > 0) {
				logger.debug("Resultado OK:"+i);
				result = true;
                conn.commit();
			}

		}catch (SQLException e) {
			throw new TrxMedioPagoDAOException(String.valueOf(e.getErrorCode()),e);
        }catch (DAOException e) {
            throw new TrxMedioPagoDAOException(String.valueOf(e.getMessage()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
                    releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				
				stm = null;
			}
		}	
		return result;
	}

	/**
	 * Crea Detalle de transaccion de medio de pagos
	 * 
	 * @param  trxdet TrxMpDetalleEntity 
	 * @return boolean, devuelve <i>true</i> si la inserción fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws TrxMedioPagoDAOException 
	 *  
	 */
	public boolean setCreaTrxMpDet(TrxMpDetalleEntity trxdet) throws TrxMedioPagoDAOException  {
		boolean result = false;
		PreparedStatement stm = null;
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"setCreaTrxMpDet");
		logger.debug("-----------------------------------------------------------------");
		try{
			logger.debug("setCreaTrxMpDet");
			String SQLStmt = "INSERT INTO BO_TRX_MP_DETALLE " +
					" (id_detalle, id_trxmp, id_pedido, id_producto, cod_barra, precio, " +
					"  descripcion, cantidad)" +
					" VALUES (?,?,?,?,?,?,?,?) " ;
			logger.debug(SQLStmt);
			logger.debug("id_detalle:"+trxdet.getId_detalle());
			logger.debug("id_trxmp:"+trxdet.getId_trxmp());
			logger.debug("id_pedido:"+trxdet.getId_pedido());
			logger.debug("id_producto:"+trxdet.getId_producto());
			logger.debug("cod_barra:"+trxdet.getCod_barra());
			logger.debug("precio:"+trxdet.getPrecio());
			logger.debug("descripcion:"+trxdet.getDescripcion());
			logger.debug("cantidad:"+trxdet.getCantidad());
			
			//con = JdbcDAOFactory.getConexion();
			conn = this.trx == null ? this.getConnection() : this.trx.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			
			stm.setLong(1, trxdet.getId_detalle());
			stm.setLong(2, trxdet.getId_trxmp());
			stm.setLong(3, trxdet.getId_pedido());
			stm.setLong(4, trxdet.getId_producto());
			stm.setString(5, trxdet.getCod_barra());
			stm.setDouble(6, trxdet.getPrecio());
			stm.setString(7, trxdet.getDescripcion());
			stm.setDouble(8, trxdet.getCantidad());

			int i = stm.executeUpdate();
			if(i>0){
				logger.debug("Resultado OK:"+i);
				result = true;
                conn.commit();
			}

		}catch (SQLException e) {
			throw new TrxMedioPagoDAOException(String.valueOf(e.getErrorCode()),e);
        }catch (DAOException e) {
            throw new TrxMedioPagoDAOException(String.valueOf(e.getMessage()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
					releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{

				stm=null;				
			}
		}
		return result;
	}

	/**
	 * Actualiza Detalle de transaccion de medio de pagos
	 * 
	 * @param  trxdet TrxMpDetalleEntity 
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws TrxMedioPagoDAOException 
	 *  
	 */
	public boolean updTrxMpDet(TrxMpDetalleEntity trxdet) throws TrxMedioPagoDAOException  {
		boolean result = false;
		PreparedStatement stm = null;
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"updTrxMpDet");
		logger.debug("-----------------------------------------------------------------");
		try{
			logger.debug("updTrxMpDet");
						
			String SQLStmt = " UPDATE BO_TRX_MP_DETALLE " +
				" SET cod_barra      = ? " +
				" WHERE id_trxdet    = ? ";
			
			logger.debug(SQLStmt);
			logger.debug("cod_barra:"+trxdet.getCod_barra());
			logger.debug("id_trxdet:"+trxdet.getId_trxdet());
			
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			
			stm.setString(1, trxdet.getCod_barra());
			stm.setLong(2, trxdet.getId_trxdet());
			

			int i = stm.executeUpdate();
			if(i>0){
				logger.debug("Resultado OK:"+i);
				result = true;
			}

		}catch (SQLException e) {
			throw new TrxMedioPagoDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
					releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
				
			}
		}
		return result;
	}
	
	
	/**
	 * Obtiene las transacciones relacionadas a un pedido.
	 * 
	 * @param  id_pedido long 
	 * @return List MonitorTrxMpDTO
	 * @throws TrxMedioPagoDAOException 
	 *  
	 */
	public List getTrxMpByIdPedido(long id_pedido)  throws TrxMedioPagoDAOException  {
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_trx = new ArrayList();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getTrxMpByIdPedido");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getTrxMpByIdPedido:");
		logger.debug("numero_pedido:"+id_pedido);
		
		String Sql =" SELECT  t.id_trxmp id_trxmp, " +
				"  		t.monto_trxmp monto_trxmp, " +
				"  		t.id_estado id_estado, " +
				"		DP.NUM_DOC, " +
				"       e.nombre estado_nom, " +
				"  		count(*) cant_prods, " +
				"		t.pos_fecha fecha," +
				"		t.pos_num_caja num_caja, " +
				" 		t.pos_monto_fp pos_monto_fp, t.pos_fecha pos_fecha, t.pos_hora pos_hora, t.pos_fp pos_fp, t.pos_boleta pos_boleta "+
				" FROM BO_TRX_MP T " +
				" JOIN BO_TRX_MP_DETALLE d ON d.id_trxmp = t.id_trxmp" + 
				" JOIN BO_ESTADOS e ON e.id_estado = t.id_estado " +
				" LEFT JOIN BODBA.BO_DOC_PAGO DP ON DP.ID_TRXMP     = T.ID_TRXMP " +
				" WHERE t.id_pedido = ? " +
				" GROUP BY t.id_trxmp, t.monto_trxmp, t.id_estado, DP.NUM_DOC, e.nombre, t.pos_fecha, t.pos_num_caja, " +
				" 		t.pos_monto_fp, t.pos_fecha, t.pos_hora, t.pos_fp, t.pos_boleta "+
				" ORDER BY t.id_trxmp ";
		logger.debug("sql:"+Sql);
		
		try {

			conn = this.getConnection();
			
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_pedido);
			rs = stm.executeQuery();
			while (rs.next()) {
				MonitorTrxMpDTO trx =new MonitorTrxMpDTO();
				trx.setId_trxmp(rs.getLong("id_trxmp"));
				trx.setMonto_trxmp(rs.getDouble("monto_trxmp"));
				trx.setId_estado(rs.getLong("id_estado"));
				trx.setCant_prods(rs.getLong("cant_prods"));
				trx.setEstado_nom(rs.getString("estado_nom"));
				trx.setNum_doc(rs.getLong("num_doc"));
				
				if(trx.getNum_doc() == 0)
					trx.setNum_doc(rs.getLong("pos_boleta"));
				
				trx.setNum_caja(rs.getLong("num_caja"));
				trx.setFecha(rs.getString("fecha"));
				trx.setPos_monto_fp(rs.getDouble("pos_monto_fp"));
				trx.setPos_fecha(rs.getString("pos_fecha"));
				trx.setPos_hora(rs.getString("pos_hora"));
				trx.setPos_fp(rs.getString("pos_fp"));
				list_trx.add(trx);
				}

		}catch (SQLException e) {
			throw new TrxMedioPagoDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
					releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;

			}
		}
		return list_trx;
	}

	/**
	 * Obtiene la transacción de pago, segun el id de transacción.
	 * 
	 * @param  id_trxmp long 
	 * @return TrxMpEncabezadoEntity
	 * @throws TrxMedioPagoDAOException 
	 *  
	 */
	public TrxMpEncabezadoEntity getTrxMpEnc(long id_trxmp)  throws TrxMedioPagoDAOException  {

		PreparedStatement 		stm = null;
		ResultSet 				rs 	= null;
		TrxMpEncabezadoEntity 	trx = null;
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getTrxMpEnc");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getTrxMpEnc:");
		logger.debug("numero_trxmp:"+id_trxmp);
		
		String Sql =" SELECT " +
				" T.id_trxmp, " +
				" T.id_pedido, " +
				" T.id_estado, " +
				" T.id_local, " +
				" T.id_cliente, " +
				" T.costo_despacho, " +
				" T.fcreacion, " +
				" T.monto_trxmp, " +
				" T.pos_monto_fp, " +
				" T.pos_num_caja, " +
				" DP.NUM_DOC, " +
				" T.pos_trx_sma, " +
				" COALESCE (T.pos_fecha, '') pos_fecha, " +
				" COALESCE (T.pos_hora, '') pos_hora, " +
				" COALESCE (T.pos_fp,'') pos_fp, " +
				" T.cant_productos " +
			" FROM BODBA.BO_TRX_MP T " +
			"      LEFT JOIN BODBA.BO_DOC_PAGO DP ON DP.ID_TRXMP = T.ID_TRXMP " +
			" WHERE T.ID_TRXMP = ? ";
		
		
		logger.debug("sql:"+Sql);
		
		try {
			conn = this.getConnection();
			
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_trxmp);
			rs = stm.executeQuery();
			
			logger.debug("antes cons");
			if (rs.next()) {
				trx = new TrxMpEncabezadoEntity();
				trx.setId_trxmp(rs.getLong("id_trxmp"));
				trx.setId_pedido(rs.getLong("id_pedido"));
				trx.setId_estado(rs.getLong("id_estado"));
				trx.setId_local(rs.getLong("id_local"));
				trx.setId_cliente(rs.getLong("id_cliente"));
				trx.setCosto_despacho(rs.getDouble("costo_despacho"));
				trx.setFcreacion(rs.getDate("fcreacion"));
				trx.setMonto_trxmp(rs.getDouble("monto_trxmp"));
				trx.setPos_monto_fp(rs.getDouble("pos_monto_fp"));
				trx.setPos_num_caja(rs.getInt("pos_num_caja"));
				trx.setPos_boleta(rs.getInt("NUM_DOC"));
				trx.setPos_trx_sma(rs.getInt("pos_trx_sma"));
				trx.setPos_fecha(rs.getString("pos_fecha"));
				trx.setPos_hora(rs.getString("pos_hora"));
				trx.setPos_fp(rs.getString("pos_fp"));
				trx.setCant_productos(rs.getInt("cant_productos"));
				}

		}catch (SQLException e) {
			//logger.debug("Sql error:"+e.getErrorCode()+ e.getMessage());
			throw new TrxMedioPagoDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
					releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;

			}
		}
		return trx;
	}
	
	
	/**
	 * Obtiene los detalles de la transacción de pago, segun el id de transacción.
	 * 
	 * @param  id_trxmp long 
	 * @return List TrxMpDetalleEntity
	 * @throws TrxMedioPagoDAOException 
	 *  
	 */
	public List getTrxMpDetalles(long id_trxmp)  throws TrxMedioPagoDAOException  {
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_det = new ArrayList();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getTrxMpByIdPedido");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getTrxMpByIdPedido:");
		logger.debug("numero_pedido:"+id_trxmp);
		
		String Sql =" SELECT  id_trxdet, " +
				    " id_detalle	, " +
					" id_trxmp		, " +
					" id_pedido		, " +
					" id_producto	, " +
					" cod_barra		, " +
					" precio		, " +
					" descripcion	, " +
					" cantidad	 	" +		
				" FROM BO_TRX_MP_DETALLE " + 
				" WHERE id_trxmp = ? " +
				" ORDER BY descripcion ";
		logger.debug("sql:"+Sql);
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_trxmp);
			rs = stm.executeQuery();
			while (rs.next()) {
				TrxMpDetalleEntity trx =new TrxMpDetalleEntity();
				trx.setId_trxdet(rs.getLong("id_trxdet"));
				trx.setId_trxmp(rs.getLong("id_trxmp"));
				trx.setId_pedido(rs.getLong("id_pedido"));
				trx.setId_producto(rs.getLong("id_producto"));
				trx.setId_detalle(rs.getLong("id_detalle"));
				trx.setCod_barra(rs.getString("cod_barra"));				
				trx.setPrecio(rs.getDouble("precio"));
				trx.setDescripcion(rs.getString("descripcion"));
				trx.setCantidad(rs.getDouble("cantidad"));				
				list_det.add(trx);
				}

		}catch (SQLException e) {
			throw new TrxMedioPagoDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
					releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;

			}
		}
		return list_det;
	}

	/**
	 * Actualiza la transaccion de pago cuando ha sido pagada OK
	 * 
	 * @param  fback POSFeedbackProcPagoDTO 
	 * 
	 * @throws TrxMedioPagoDAOException
	 */
	public void doProcesaPagoOK(POSFeedbackProcPagoDTO fback) throws TrxMedioPagoDAOException {
		
		PreparedStatement stm=null;
		
		long id_trxmp 	= fback.getId_trxmp();
		int id_estado 	= Constantes.ID_ESTAD_TRXMP_PAGADA;
		int pos_monto	= fback.getMonto_pagado();
		int num_caja 	= fback.getNum_pos();
		int pos_boleta  = fback.getNum_boleta();
		int pos_sma		= fback.getNum_sma();
		String fecha	= fback.getFecha();
		String hora		= fback.getHora();
		
		logger.debug("DAO doProcesaPagoOK:" );
		
		String sql =
			" UPDATE bo_trx_mp " +
			" SET id_estado=?, pos_monto_fp=?, pos_num_caja=?, pos_boleta=?, " +
			" pos_trx_sma=?, pos_fecha=?, pos_hora=? " +
			" WHERE id_trxmp = ? ";
		
		logger.debug("SQL: " + sql);
		logger.debug("id_trxmp: " + id_trxmp);
		logger.debug("id_estado: " + id_estado);
		logger.debug("pos_monto: " + pos_monto);
		logger.debug("pos_boleta: " + pos_boleta);
		logger.debug("num_caja: " + num_caja);
		logger.debug("pos_sma: " + pos_sma);
		logger.debug("fecha: " + fecha);
		logger.debug("hora: " + hora);

		
		
		try {
	
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement( sql );
	
			stm.setLong(1, id_estado);
			stm.setInt(2, pos_monto);
			stm.setInt(3, num_caja);
			stm.setInt(4, pos_boleta);
			stm.setInt(5, pos_sma);
			stm.setString(6, fecha);
			stm.setString(7, hora);
			stm.setLong(8,id_trxmp);
			
			int rc = stm.executeUpdate();
			logger.debug("rc:" + rc);

		} catch (SQLException e) {
			logger.debug("error: " + e.getMessage() );
			throw new TrxMedioPagoDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
					releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;

			}
		}
		
		
	}

	
	/**
	 * Actualiza transaccion de pago cuando ha sido rechazada
	 * 
	 * @param fback POSFeedbackProcPagoDTO 
	 * 
	 * @throws TrxMedioPagoDAOException
	 */
	public void doRechazaPago(POSFeedbackProcPagoDTO fback) throws TrxMedioPagoDAOException {

		PreparedStatement stm=null;
		
		long id_trxmp 	= fback.getId_trxmp();
		int id_estado 	= Constantes.ID_ESTAD_TRXMP_RECHAZADA;
		
		logger.debug("DAO doRechazaPago:" );
		
		String sql =
			" UPDATE bo_trx_mp " +
			" SET id_estado=? " +
			" WHERE id_trxmp = ? ";
		
		logger.debug("SQL: " + sql);
		logger.debug("id_trxmp: " + id_trxmp);
		logger.debug("id_estado: " + id_estado);
		
		try {
	
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement( sql );
	
			stm.setLong(1, id_estado);
			stm.setLong(2,id_trxmp);
			
			int rc = stm.executeUpdate();
			logger.debug("rc:" + rc);

		} catch (SQLException e) {
			logger.debug("error: " + e.getMessage() );
			throw new TrxMedioPagoDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
					releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{

				stm=null;
			
			}
		}
		
		
	}

	/* (sin Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.TrxMedioPagoDAO#checkTrxMPbyState(long, int)
	 */
	public boolean isAllTrxMPbyPedidoAndState(long id_pedido, int id_estado) throws TrxMedioPagoDAOException {

		PreparedStatement stm=null;
		ResultSet rs = null;
		logger.debug("DAO checkTrxMPbyState:" );
		
		// Esta query entrega la cantidad de transacciones por estado para un pedido
		// Si todas las transacciones tienen un sólo estado, el resultado de la consulta será de un registro
		
		String sql = "SELECT TP.ID_ESTADO, COUNT(TP.ID_TRXMP) "
                   + "FROM BODBA.BO_TRX_MP TP "
                   + "     JOIN BODBA.BO_PEDIDOS P ON P.ID_PEDIDO = TP.ID_PEDIDO "
                   + "WHERE TP.ID_PEDIDO = ? AND P.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") "
                   + "  AND P.ESTADO_ENVIO_EMAIL = 'N' "
                   + "GROUP BY TP.ID_ESTADO";
		
		logger.debug("SQL: " + sql);
		logger.debug("id_pedido: " + id_pedido);
		logger.debug("id_estado: " + id_estado);
		
		try {
	
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement( sql + " WITH UR" );
	
			stm.setLong(1, id_pedido);
			rs = stm.executeQuery();

			// 'countEstados' contará los registros por tipo de estado, si countEstados > 1, significa que
			// hay transacciones con estados diferentes, por lo tanto, no todas en un mismo estado
			int countEstados = 0;
			int estado = 0;
			while (rs.next()) {
				countEstados++;
				estado = rs.getInt(1);
				logger.debug("countEstados = " + countEstados + ", estado = " + estado);
			}
			
			if (countEstados != 1) 
				return false;
			else if (estado == id_estado) {
				// Si hay un sólo registro, y el estado corresponde al consultado, entonces, está todo OK
				// Si no, por defecto devuelve 'false'
				return true;
			}
			
		} catch (SQLException e) {
			logger.debug("error: " + e.getMessage() );
			throw new TrxMedioPagoDAOException(String.valueOf(e.getErrorCode()),e);
		} catch (Exception e1) {
			logger.debug("error: " + e1.getMessage() );
			throw new TrxMedioPagoDAOException(e1.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
					releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;

			}
		}
		
		return false;
	}	

	/* (sin Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.TrxMedioPagoDAO#getTrxMpByCriteria(TrxMpCriteriaDTO)
	 */
	public List getTrxMpByCriteria(TrxMpCriteriaDTO criterios)   throws TrxMedioPagoDAOException  {
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_trx = new ArrayList();
		GregorianCalendar hoy = new GregorianCalendar();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getTrxMpByCriteria");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getTrxMpByCriteria:");
		logger.debug("local facturador: " + criterios.getLocal_fact());
		logger.debug("id_pedido : " + criterios.getId_pedido());
		logger.debug("id_estado : " + criterios.getId_estado());
		logger.debug("tipo_fecha: " + criterios.getTipo_fecha());
		logger.debug("fecha_ini : " + criterios.getFecha_ini());
		logger.debug("fecha_fin : " + criterios.getFecha_fin());
		logger.debug("origen    : " + criterios.getOrigen());
		logger.debug("tipo_doc  : " + criterios.getTipo_doc());
		
		//param
		String tipo_fecha= "";
		String fecha_ini = "";
		String fecha_fin = "";
		//criterios
		String sqlDespacho= "";
		String sqlPicking = "";
		String sqlEstados = "";
		String sqlOrigen  = "";
		String sqlTipoDoc = "";
		String sqlPedido  = "";
		String sqlLimitaFecha = "1=1 ";
		
		
		if (criterios.getId_estado()>0){
		sqlEstados = " AND t.id_estado ="+criterios.getId_estado();
		}
		
		if ((criterios.getTipo_fecha()!="")&& (criterios.getTipo_fecha()!=null)){
		tipo_fecha = criterios.getTipo_fecha();
		}
		
		if ((criterios.getFecha_ini()!="")&& (criterios.getFecha_ini()!=null)){
		fecha_ini = criterios.getFecha_ini();
		}
		
		if ((criterios.getFecha_fin()!="")&& (criterios.getFecha_fin()!=null)){
		fecha_fin = criterios.getFecha_fin();
		}
		
		if(tipo_fecha.equals("P")){ //formato debe ir con '2007-06-21'
			sqlPicking = " AND jp.fecha BETWEEN '"+fecha_ini+"' AND '"+fecha_fin+"' ";
		}
		else if(tipo_fecha.equals("D")){
			sqlDespacho = " AND jd.fecha BETWEEN '"+fecha_ini+"' AND '"+fecha_fin+"' ";
		}
		
		if (!criterios.getOrigen().equals("")){
		    sqlOrigen = "  AND P.ORIGEN = '" + criterios.getOrigen() + "' ";
		}

		if (!criterios.getTipo_doc().equals("")){
		    sqlTipoDoc = "  AND P.TIPO_DOC = '" + criterios.getTipo_doc() + "' ";
		}

		if (criterios.getId_pedido()>0){
			sqlPedido ="  AND p.id_pedido = " + criterios.getId_pedido();	
		}else{
			if (criterios.isLimitarFecha()){
				hoy.add(Calendar.MONTH, -6);
				sqlLimitaFecha = " P.FCREACION >= ? ";
				//String fecha = Formatos.frmFechaByDate(new java.sql.Date(hoy.getTimeInMillis()));
				//sql_LimiteFecha = "P.FCREACION >= DATE('" + fecha + "') ";
			}
		}


		//paginacion
		int pag = criterios.getPag();
		int regXpag = criterios.getRegsperpag();
		logger.debug("pagina:"+pag + " regxpag:"+ regXpag);
		if(pag<=0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;			
		
		logger.debug("pag:"+pag);
		logger.debug("regXpag:"+regXpag);
		logger.debug("iniReg:"+iniReg);
		logger.debug("finReg:"+finReg);
		
		
		
		String Sql ="SELECT * FROM ( " +
		 		" 	SELECT row_number() over(ORDER BY t.id_trxmp desc) as row, " +
		 		" 		t.id_trxmp, " +
				"  		t.monto_trxmp, " +
				"  		t.id_estado, " +
				"		DP.NUM_DOC, " +
				"       e.nombre estado_nom, " +
				"  		count(*) cant_prods, " +
				"		t.pos_fecha fecha," +
				"		t.pos_num_caja num_caja, " +
				" 		t.pos_monto_fp, " +
				"		t.pos_fecha, t.pos_hora, " +
				"		t.pos_fp, " +
				"		p.id_local_fact, " +
				"		l.nom_local nom_local_fact, " +
				"		p.id_pedido," +
				"		p.tipo_doc," +
				"       P.ORIGEN, " +
				"       jp.fecha fecha_picking, " +
				"       jd.fecha fecha_despacho " +
				" FROM BODBA.BO_TRX_MP t " +
				" JOIN BODBA.BO_PEDIDOS p ON p.id_pedido = t.id_pedido " +
				"		                 AND p.id_local_fact = ? AND p.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") " + 
				                         sqlPedido;
		
		
		Sql +=  " JOIN BODBA.BO_JORNADAS_PICK jp ON p.id_jpicking = jp.id_jpicking " + sqlPicking +
				" JOIN BODBA.BO_JORNADA_DESP jd ON p.id_jdespacho = jd.id_jdespacho " + sqlDespacho +
				" JOIN BODBA.BO_LOCALES l ON l.id_local = p.id_local_fact " +
				" JOIN BODBA.BO_TRX_MP_DETALLE d ON d.id_trxmp = t.id_trxmp" + 
				" JOIN BODBA.BO_ESTADOS e ON e.id_estado = t.id_estado" + 
				" LEFT JOIN BODBA.BO_DOC_PAGO DP ON DP.ID_TRXMP     = T.ID_TRXMP " +
				" WHERE " + sqlLimitaFecha + sqlEstados + sqlOrigen + sqlTipoDoc + 
				" GROUP BY t.id_trxmp, t.monto_trxmp, t.id_estado, DP.NUM_DOC, e.nombre, t.pos_fecha, t.pos_num_caja, " +
				" 		t.pos_monto_fp, t.pos_fecha, t.pos_hora, t.pos_fp , " +
				"	p.id_local_fact, l.nom_local,  p.id_pedido , p.tipo_doc, P.ORIGEN, jp.fecha, jd.fecha "+
				//" ORDER BY t.id_trxmp " +
				") AS TEMP WHERE row BETWEEN "+ iniReg +" AND "+ finReg;
		logger.debug("SQL (getTrxMpByCriteria):"+Sql);
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, criterios.getLocal_fact());
			
			if (criterios.getId_pedido()<=0){
			    stm.setDate(2, new java.sql.Date(hoy.getTimeInMillis()));
			}
			/*if (criterios.getId_pedido()>0){
				Sql +="		AND p.id_pedido = ? ";	
			}*/
			
			rs = stm.executeQuery();
			while (rs.next()) {
				MonitorTrxMpDTO trx =new MonitorTrxMpDTO();
				trx.setId_trxmp(rs.getLong("id_trxmp"));
				trx.setMonto_trxmp(rs.getDouble("monto_trxmp"));
				trx.setId_estado(rs.getLong("id_estado"));
				trx.setCant_prods(rs.getLong("cant_prods"));
				trx.setEstado_nom(rs.getString("estado_nom"));
				trx.setNum_doc(rs.getLong("num_doc"));
				trx.setNum_caja(rs.getLong("num_caja"));
				trx.setFecha(rs.getString("fecha"));
				trx.setPos_monto_fp(rs.getDouble("pos_monto_fp"));
				trx.setPos_fecha(rs.getString("pos_fecha"));
				trx.setPos_hora(rs.getString("pos_hora"));
				trx.setPos_fp(rs.getString("pos_fp"));
				trx.setId_local_fact(rs.getLong("ID_LOCAL_FACT"));
				trx.setNom_local_fact(rs.getString("nom_local_fact"));
				trx.setId_pedido(rs.getLong("id_pedido"));
				trx.setTipo_doc(rs.getString("tipo_doc"));
				trx.setOrigen(rs.getString("ORIGEN"));
				trx.setFecha_picking(rs.getString("fecha_picking"));
				trx.setFecha_despacho(rs.getString("fecha_despacho"));
				list_trx.add(trx);
			}

		}catch (SQLException e) {
			throw new TrxMedioPagoDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
					releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;

			}
		}
		return list_trx;
	}
	
	/* (sin Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.TrxMedioPagoDAO#getCountTrxMpByCriteria(TrxMpCriteriaDTO)
	 */
	public long getCountTrxMpByCriteria(TrxMpCriteriaDTO criterios)  throws TrxMedioPagoDAOException  {
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		int numTrx=0;
		GregorianCalendar hoy = new GregorianCalendar();
		
		logger.debug("-----------------------------------------------------------------");
		logger.debug("Ejecución DAO: " + getClass().getName()+"getCountTrxMpByCriteria");
		logger.debug("-----------------------------------------------------------------");
		
		logger.debug("Parametros getCountTrxMpByCriteria:");
		logger.debug("local facturador:"+criterios.getLocal_fact());
		logger.debug("id_pedido : " + criterios.getId_pedido());
		logger.debug("id_estado : " + criterios.getId_estado());
		logger.debug("tipo_fecha: " + criterios.getTipo_fecha());
		logger.debug("fecha_ini : " + criterios.getFecha_ini());
		logger.debug("fecha_fin : " + criterios.getFecha_fin());
		logger.debug("origen    : " + criterios.getOrigen());
		logger.debug("tipo_doc  : " + criterios.getTipo_doc());
		
		//param
		String tipo_fecha= "";
		String fecha_ini = "";
		String fecha_fin = "";
		//criterios
		String sqlDespacho = "";
		String sqlPicking  = "";
		String sqlEstados  = "";
		String sqlOrigen   = "";
		String sqlTipoDoc  = "";
		String sqlPedido   = "";
		String sqlLimitaFecha = "1=1 ";
		
		
		if (criterios.getId_estado()>0){
		    sqlEstados = " AND t.id_estado =" + criterios.getId_estado();
		}
		
		if ((criterios.getTipo_fecha()!="") && (criterios.getTipo_fecha()!=null)){
		    tipo_fecha = criterios.getTipo_fecha();
		}
		
		if ((criterios.getFecha_ini()!="") && (criterios.getFecha_ini()!=null)){
		    fecha_ini = criterios.getFecha_ini();
		}
		
		if ((criterios.getFecha_fin()!="") && (criterios.getFecha_fin()!=null)){
		    fecha_fin = criterios.getFecha_fin();
		}
		
		if(tipo_fecha.equals("P")){ //formato debe ir con '2007-06-21'
			sqlPicking = "  AND jp.fecha BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' ";
		}
		else if(tipo_fecha.equals("D")){
			sqlDespacho = "  AND jd.fecha BETWEEN '" + fecha_ini + "' AND '" + fecha_fin + "' ";
		} 

		if (!criterios.getOrigen().equals("")){
		    sqlOrigen = "  AND P.ORIGEN = '" + criterios.getOrigen() + "' ";
		}

		if (!criterios.getTipo_doc().equals("")){
		    sqlTipoDoc = "  AND P.TIPO_DOC = '" + criterios.getTipo_doc() + "' ";
		}

		if (criterios.getId_pedido()>0){
			sqlPedido ="  AND p.id_pedido = " + criterios.getId_pedido();	
		}else{
			if (criterios.isLimitarFecha()){
				hoy.add(Calendar.MONTH, -6);
				sqlLimitaFecha = " P.FCREACION >= ? ";
				//String fecha = Formatos.frmFechaByDate(new java.sql.Date(hoy.getTimeInMillis()));
				//sql_LimiteFecha = "P.FCREACION >= DATE('" + fecha + "') ";
			}
		}

		//paginacion
		int pag = criterios.getPag();
		int regXpag = criterios.getRegsperpag();
		logger.debug("pagina:"+pag + " regxpag:"+ regXpag);
		if(pag<=0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;			
		
		logger.debug("pag:"+pag);
		logger.debug("regXpag:"+regXpag);
		logger.debug("iniReg:"+iniReg);
		logger.debug("finReg:"+finReg);
		
		String Sql ="SELECT count(*) cantidad FROM ( " +
	 		" 	SELECT row_number() over(ORDER BY t.id_trxmp desc) as row, "+ 
	 		" 		t.id_trxmp id_trxmp, " +
			"  		t.monto_trxmp monto_trxmp, " +
			"  		t.id_estado id_estado, " +
			"		DP.NUM_DOC, " +
			"       e.nombre estado_nom, " +
			"  		count(*) cant_prods, " +
			"		t.pos_fecha fecha," +
			"		t.pos_num_caja num_caja, " +
			" 		t.pos_monto_fp pos_monto_fp, " +
			"		t.pos_fecha pos_fecha, t.pos_hora pos_hora, " +
			"		t.pos_fp pos_fp, " +
			"		p.id_local_fact id_long_fact, " +
			"		l.nom_local nom_local_fact, " +
			"		p.id_pedido id_pedido," +
			"		p.tipo_doc tipo_doc, "+
			"       jp.fecha fecha_picking, " +
			"       jd.fecha fecha_despacho "+
			" FROM BO_TRX_MP T " +
			" JOIN BO_PEDIDOS p ON p.id_pedido = t.id_pedido " +
			"		AND p.id_local_fact = ? AND p.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") " + 
			        sqlPedido;
	Sql +=  " JOIN BO_JORNADAS_PICK jp ON p.id_jpicking = jp.id_jpicking " + sqlPicking +
			" JOIN BO_JORNADA_DESP jd ON p.id_jdespacho = jd.id_jdespacho " + sqlDespacho +	
			" JOIN BO_LOCALES l ON l.id_local = p.id_local_fact " +
			" JOIN BO_TRX_MP_DETALLE d ON d.id_trxmp = t.id_trxmp" + 
			" JOIN BO_ESTADOS e ON e.id_estado = t.id_estado" +
			" LEFT JOIN BODBA.BO_DOC_PAGO DP ON DP.ID_TRXMP     = T.ID_TRXMP " +
			" WHERE " + sqlLimitaFecha + sqlEstados + sqlOrigen + sqlTipoDoc +
			" GROUP BY t.id_trxmp, t.monto_trxmp, t.id_estado, DP.NUM_DOC, e.nombre, t.pos_fecha, t.pos_num_caja, " +
			" 		t.pos_monto_fp, t.pos_fecha, t.pos_hora, t.pos_fp , " +
			"	p.id_local_fact, l.nom_local,  p.id_pedido , p.tipo_doc, jp.fecha, jd.fecha "+
			//" ORDER BY t.id_trxmp " +
			") AS TEMP";
		logger.debug("SQL (getCountTrxMpByCriteria): " + Sql);
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			
			//los que no estan en picking
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, criterios.getLocal_fact());	
			
			if (criterios.getId_pedido()<=0){
			    stm.setDate(2, new java.sql.Date(hoy.getTimeInMillis()));
			}
			rs = stm.executeQuery();
			if (rs.next()) {
				numTrx = rs.getInt("cantidad");
			}

		}catch (SQLException e) {
			throw new TrxMedioPagoDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
					releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;

			}
		}
		return numTrx;
	}	
	
	/* (sin Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.TrxMedioPagoDAO#getEstadosTrxMp()
	 */
	public List getEstadosTrxMp() throws TrxMedioPagoDAOException {
		
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		String SQLStmt = 
				"SELECT id_estado,nombre " +
				"FROM bo_estados " +
				"WHERE tipo_estado= ? ";

		logger.debug("Ejecución getEstadosTrxMp()");
		logger.debug("SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			
			stm.setString(1,"MP");
			logger.debug("SQL query: " + stm.toString());

			rs = stm.executeQuery();
			while (rs.next()) {
				EstadoDTO est = new EstadoDTO();
				est.setId_estado(rs.getLong("id_estado"));
				est.setNombre(rs.getString("nombre"));
				result.add(est);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e.getMessage());
			throw new TrxMedioPagoDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
					releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;

			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;
	}
	

    /**
     * @param idPedido
     * @param tipoDespacho
     */
    public boolean DelTrxMP(long id_pedido) throws TrxMedioPagoDAOException {
        PreparedStatement stm = null;
        boolean eliminado = false;
        int numreg = 0;
        
        String SQL = "DELETE FROM BODBA.BO_TRX_MP TMP WHERE TMP.ID_PEDIDO = ?";
        
        logger.debug("SQL (DelTrxMP): " + SQL);
        
        try {
            //con = JdbcDAOFactory.getConexion();
            conn = this.getConnection();
            
            //stm = conn.prepareStatement(sql);
            stm = conn.prepareStatement(SQL);
            stm.setLong(1, id_pedido);
            
            numreg = stm.executeUpdate();
            
			if (numreg > 0) {
				logger.debug("Las Transacciones del Pedido ID: " + id_pedido + " fueron Eliminadas.");
				eliminado = true;
			}		

        }catch (SQLException e) {
            throw new TrxMedioPagoDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
					releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;

			}
		}
        return eliminado;
    }

	/**
	 * @param id_pedido
	 * @param id_estado
	 */
	public void setModEstadoTrxOP(long id_pedido, int id_estado) throws TrxMedioPagoDAOException {
        PreparedStatement stm = null;
        boolean actualizado = false;
        int numreg = 0;
        
        String SQL = " UPDATE BODBA.BO_TRX_MP SET ID_ESTADO = ? WHERE ID_PEDIDO = ? ";
        
        logger.debug("SQL (DelTrxMP): " + SQL);
        
        try {
            //con = JdbcDAOFactory.getConexion();
            conn = this.getConnection();
            
            //stm = conn.prepareStatement(sql);
            stm = conn.prepareStatement(SQL);
            stm.setInt(1, id_estado);
            stm.setLong(2, id_pedido);
            
            numreg = stm.executeUpdate();
            
			if (numreg > 0) {
				logger.debug("Las Transacciones del Pedido ID: " + id_pedido + " fueron actualizados.");
				actualizado = true;
			}		

        }catch (SQLException e) {
            throw new TrxMedioPagoDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
					releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{

				stm=null;

			}
		}
        
	}


}

	
