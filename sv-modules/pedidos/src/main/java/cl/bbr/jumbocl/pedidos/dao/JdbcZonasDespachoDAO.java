package cl.bbr.jumbocl.pedidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.pedidos.dto.ComunaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.LocalDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.ZonasDespachoDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Clase que permite consultar las zonas de despacho que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcZonasDespachoDAO implements ZonasDespachoDAO {

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
	 * @throws ZonasDespachoDAOException 
	 */
	public void setTrx(JdbcTransaccion trx)
			throws ZonasDespachoDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new ZonasDespachoDAOException(e);
		}
	}
	
	/**
	 * Retorna un listado con las Zonas de Despacho de un Local
	 * 
	 * @param  id_local long 
	 * @return List ZonaDTO
	 * @throws ZonasDespachoDAOException
	 */
	public List getZonasDespachoLocal(long id_local)
		throws ZonasDespachoDAOException {
		
		List result = new ArrayList();
		
		PreparedStatement stm =null;
		ResultSet rs = null;
		logger.debug("en getZonasDespachoLocal");
		String SQLStmt =
			" SELECT id_zona, id_local, nombre, descripcion, " +
            "        tarifa_normal_baja, tarifa_normal_media, tarifa_normal_alta, " +
            "        tarifa_economica, tarifa_express, estado_tarifa_economica, " +
            "        estado_tarifa_express, mensaje_cal_despacho, orden_zona " +
			" FROM bo_zonas " +
			" WHERE id_local = ?" +
			" ORDER BY orden_zona ASC";
		
		logger.debug("SQL: " + SQLStmt);
		logger.debug("id_local: " + id_local);
		
		try {
	
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			stm.setLong(1,id_local);
	
	
			rs = stm.executeQuery();
			while (rs.next()) {
				ZonaDTO zona = new ZonaDTO();
				zona.setId_zona(rs.getLong("id_zona"));
				zona.setNombre(rs.getString("nombre"));
				zona.setDescripcion(rs.getString("descripcion"));
				zona.setTarifa_normal_baja(rs.getInt("tarifa_normal_baja"));
				zona.setTarifa_normal_media(rs.getInt("tarifa_normal_media"));
				zona.setTarifa_normal_alta(rs.getInt("tarifa_normal_alta"));
				zona.setTarifa_economica(rs.getInt("tarifa_economica"));
				zona.setTarifa_express(rs.getInt("tarifa_express"));
				zona.setEstado_tarifa_economica(rs.getInt("estado_tarifa_economica"));
				zona.setEstado_tarifa_express(rs.getInt("estado_tarifa_express"));
				zona.setMensaje_cal_despacho(rs.getString("mensaje_cal_despacho"));
				zona.setOrden(rs.getInt("orden_zona"));
				result.add(zona);
			}

		}catch (SQLException e) {
			throw new ZonasDespachoDAOException(String.valueOf(e.getErrorCode()),e);
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
	 * Obtiene el detalle de una zona de despacho
	 * 
	 * @param  id_zona long 
	 * @return ZonaDTO
	 * @throws ZonasDespachoDAOException
	 */	
	public ZonaDTO getZonaDespachoById(long id_zona) throws ZonasDespachoDAOException {
		
		ZonaDTO zona = new ZonaDTO();
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		try {
			conn = this.getConnection();
			String Sql = "SELECT Z.ID_ZONA, Z.ID_LOCAL, Z.NOMBRE, Z.DESCRIPCION, " +
                         "Z.TARIFA_NORMAL_BAJA, Z.TARIFA_NORMAL_MEDIA, Z.TARIFA_NORMAL_ALTA, " +
                         "Z.TARIFA_ECONOMICA, Z.TARIFA_EXPRESS, Z.ESTADO_TARIFA_ECONOMICA, " +
                         "Z.ESTADO_TARIFA_EXPRESS, Z.MENSAJE_CAL_DESPACHO, Z.ORDEN_ZONA, Z.REGALO_CLIENTES, " +
                         "Z.ESTADO_DESCUENTO_CAT, Z.ESTADO_DESCUENTO_TBK, Z.ESTADO_DESCUENTO_PAR, " +
                         "Z.MONTO_DESCUENTO_CAT, Z.MONTO_DESCUENTO_TBK, Z.MONTO_DESCUENTO_PAR, " +
                         "Z.MONTO_DESCUENTO_PC_CAT, Z.MONTO_DESCUENTO_PC_TBK, Z.MONTO_DESCUENTO_PC_PAR, " +
                         "CASE WHEN L.ID_ZONA_RETIRO IS NULL THEN 'N' ELSE 'S' END ES_RETIRO " +
                         "FROM BODBA.BO_ZONAS Z " +
                         "LEFT OUTER JOIN BODBA.BO_LOCALES L ON L.ID_ZONA_RETIRO = Z.ID_ZONA " +
                         "WHERE Z.ID_ZONA = ?";

			stm = conn.prepareStatement(Sql + " WITH UR");
			
			stm.setLong(1,id_zona);
			logger.debug("SQL: " + stm.toString());
			rs = stm.executeQuery();
			if (rs.next()) {
				zona.setId_zona(rs.getLong("id_zona"));
				zona.setId_local(rs.getLong("id_local"));
				zona.setNombre(rs.getString("nombre"));
				zona.setDescripcion(rs.getString("descripcion"));
				zona.setTarifa_normal_baja(rs.getInt("tarifa_normal_baja"));
				zona.setTarifa_normal_media(rs.getInt("tarifa_normal_media"));
				zona.setTarifa_normal_alta(rs.getInt("tarifa_normal_alta"));
				zona.setTarifa_economica(rs.getInt("tarifa_economica"));
				zona.setTarifa_express(rs.getInt("tarifa_express"));
				zona.setEstado_tarifa_economica(rs.getInt("estado_tarifa_economica"));
				zona.setEstado_tarifa_express(rs.getInt("estado_tarifa_express"));
				zona.setMensaje_cal_despacho(rs.getString("mensaje_cal_despacho"));
				zona.setRegalo_clientes(rs.getInt("regalo_clientes"));
				zona.setOrden(rs.getInt("orden_zona"));
				zona.setEstado_descuento_cat(rs.getInt("ESTADO_DESCUENTO_CAT"));
				zona.setEstado_descuento_tbk(rs.getInt("ESTADO_DESCUENTO_TBK"));
				zona.setEstado_descuento_par(rs.getInt("ESTADO_DESCUENTO_PAR"));
				zona.setMonto_descuento_cat(rs.getInt("MONTO_DESCUENTO_CAT"));
				zona.setMonto_descuento_tbk(rs.getInt("MONTO_DESCUENTO_TBK"));
				zona.setMonto_descuento_par(rs.getInt("MONTO_DESCUENTO_PAR"));
				zona.setMonto_descuento_pc_cat(rs.getInt("MONTO_DESCUENTO_PC_CAT"));
				zona.setMonto_descuento_pc_tbk(rs.getInt("MONTO_DESCUENTO_PC_TBK"));
				zona.setMonto_descuento_pc_par(rs.getInt("MONTO_DESCUENTO_PC_PAR"));
                if ( (rs.getString("ES_RETIRO") != null) && (rs.getString("ES_RETIRO").equalsIgnoreCase("S") ) ) {
                    zona.setRetiroLocal(true);
                } else {
                    zona.setRetiroLocal(false);    
                }
                
			}

		}catch (SQLException e) {
			throw new ZonasDespachoDAOException(e);
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
		return zona;
	}


	/**
	 * Inserta registro en tabla BO_ZONAS
	 * 
	 * @param  zona ZonaDTO 
	 * @return long, nuevo id
	 * @throws ZonasDespachoDAOException
	 */
	public long doAgregaZonaDespacho(ZonaDTO zona) throws ZonasDespachoDAOException {
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		long	id_zona = -1;
		String SQLStmt =
			" INSERT INTO bo_zonas (id_local, nombre, descripcion, tarifa_normal_baja, " +
			" tarifa_normal_media, tarifa_normal_alta, tarifa_economica, tarifa_express, " +
			" estado_tarifa_economica, estado_tarifa_express, mensaje_cal_despacho, orden_zona," +
			" regalo_clientes, estado_descuento_cat, estado_descuento_tbk, estado_descuento_par, " +
			" monto_descuento_cat, monto_descuento_tbk, monto_descuento_par, " +
			" monto_descuento_pc_cat, monto_descuento_pc_tbk, monto_descuento_pc_par) " +
			" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
		
		logger.debug("Ejecucion DAO doAgregaZonaDespacho");
		logger.debug("SQL: " + SQLStmt);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt, Statement.RETURN_GENERATED_KEYS );
			
			stm.setLong(1, zona.getId_local());
			stm.setString(2, zona.getNombre());
			stm.setString(3, zona.getDescripcion());
			stm.setInt(4, zona.getTarifa_normal_baja());
			stm.setInt(5, zona.getTarifa_normal_media());
			stm.setInt(6, zona.getTarifa_normal_alta());
			stm.setInt(7, zona.getTarifa_economica());
			stm.setInt(8, zona.getTarifa_express());
			stm.setInt(9, zona.getEstado_tarifa_economica());
			stm.setInt(10, zona.getEstado_tarifa_express());
			stm.setString(11, zona.getMensaje_cal_despacho());
			stm.setInt(12, zona.getOrden());
			stm.setInt(13, zona.getRegalo_clientes());
			stm.setInt(14, zona.getEstado_descuento_cat());
			stm.setInt(15, zona.getEstado_descuento_tbk());
			stm.setInt(16, zona.getEstado_descuento_par());
			stm.setInt(17, zona.getMonto_descuento_cat());
			stm.setInt(18, zona.getMonto_descuento_tbk());
			stm.setInt(19, zona.getMonto_descuento_par());
			stm.setInt(20, zona.getMonto_descuento_pc_cat());
			stm.setInt(21, zona.getMonto_descuento_pc_tbk());
			stm.setInt(22, zona.getMonto_descuento_pc_par());
			
			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);
			
			rs = stm.getGeneratedKeys();
			if (rs.next()) {				
				 id_zona = rs.getLong(1);
				 logger.debug("id_zona insertado: " + id_zona);
			 }			

		}catch (SQLException e) {
			throw new ZonasDespachoDAOException(String.valueOf(e.getErrorCode()),e);
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
		return id_zona;
	}
	
	//modificar el orden de la zona de despacho para agregar la zona como retiro
		public void doModZonaDespachoOrden (int idZona, int orden) throws ZonasDespachoDAOException{
			PreparedStatement stm =null;
			String SQLStmt = "UPDATE bo_zonas " 
					+ "      set orden_zona = ? "
					+ "WHERE id_zona = ? ";
			try {
				conn = this.getConnection();
				stm = conn.prepareStatement( SQLStmt );
				stm.setInt(1, orden);
				stm.setInt(2, idZona);
				int i = stm.executeUpdate();
				logger.debug("Resultado Ejecución: " + i);
			}catch (SQLException e) {
				throw new ZonasDespachoDAOException(String.valueOf(e.getErrorCode()),e);
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
	 * Actualiza registro en tabla BO_ZONAS
	 * 
	 * @param  zona ZonaDTO 
	 * 
	 * @throws ZonasDespachoDAOException
	 */
	public void doModZonaDespacho(ZonaDTO zona) throws ZonasDespachoDAOException {
		
		PreparedStatement stm =null;
		String Descuentos = "";
		
	    Descuentos += "      ,estado_descuento_cat = " + zona.getEstado_descuento_cat();
	    Descuentos += "      ,estado_descuento_tbk = " + zona.getEstado_descuento_tbk();
	    Descuentos += "      ,estado_descuento_par = " + zona.getEstado_descuento_par();
		
		if (zona.getEstado_descuento_cat() > 0){
		    Descuentos += "      ,monto_descuento_cat = " + zona.getMonto_descuento_cat();
		    Descuentos += "      ,monto_descuento_pc_cat = " + zona.getMonto_descuento_pc_cat();
		}
		if (zona.getEstado_descuento_tbk() > 0){
		    Descuentos += "      ,monto_descuento_tbk = " + zona.getMonto_descuento_tbk();
		    Descuentos += "      ,monto_descuento_pc_tbk = " + zona.getMonto_descuento_pc_tbk();
		}
		if (zona.getEstado_descuento_par() > 0){
		    Descuentos += "      ,monto_descuento_par = " + zona.getMonto_descuento_par();
		    Descuentos += "      ,monto_descuento_pc_par = " + zona.getMonto_descuento_pc_par();
		}
		
		String SQLStmt = "UPDATE bo_zonas " 
                       + "   SET nombre = ? "
                       + "      ,descripcion = ? "
                       + "      ,tarifa_normal_baja = ? "
                       + "      ,tarifa_normal_media = ? "
                       + "      ,tarifa_normal_alta = ? "
                       + "      ,tarifa_economica = ? "
                       + "      ,tarifa_express = ? "
                       + "      ,estado_tarifa_economica = ? "
                       + "      ,estado_tarifa_express = ? "
                       + "      ,mensaje_cal_despacho = ? "
                       + "      ,orden_zona = ? "
                       + "      ,regalo_clientes = ? "
                       + Descuentos + " "
                       + "WHERE id_zona = ? ";
		
		logger.debug("Ejecucion DAO doModZonaDespacho");
		logger.debug("SQL: " + SQLStmt);
				
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			
			stm.setString(1, zona.getNombre());
			stm.setString(2, zona.getDescripcion());
			stm.setDouble(3, zona.getTarifa_normal_baja());
			stm.setDouble(4, zona.getTarifa_normal_media());
			stm.setDouble(5, zona.getTarifa_normal_alta());
			stm.setDouble(6, zona.getTarifa_economica());
			stm.setDouble(7, zona.getTarifa_express());
			stm.setDouble(8, zona.getEstado_tarifa_economica());
			stm.setDouble(9, zona.getEstado_tarifa_express());
			stm.setString(10, zona.getMensaje_cal_despacho());
			stm.setLong(11, zona.getOrden());
			stm.setDouble(12, zona.getRegalo_clientes());
			stm.setLong(13, zona.getId_zona());
			
			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);
			
		}catch (SQLException e) {
			throw new ZonasDespachoDAOException(String.valueOf(e.getErrorCode()),e);
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
	 * Obtiene listado de comunas de una zona de despacho
	 * 
	 * @param  id_zona long 
	 * @return List ComunaDTO
	 * @throws LocalDAOException
	 */
	public List getComunasByIdZonaDespacho(long id_zona) throws LocalDAOException {

		List result = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		
        String SQL = "SELECT DISTINCT C.ID_COMUNA, C.NOMBRE "
                   + "FROM BODBA.BO_POLIGONO P "
                   + "     JOIN BODBA.BO_COMUNAS C ON C.ID_COMUNA = P.ID_COMUNA "
                   + "WHERE P.ID_ZONA = ? "
                   + "ORDER BY C.NOMBRE";
		logger.debug("Ejecución DAO getComunasByIdZonaDespacho()");
		logger.debug("SQL: " + SQL);
		logger.debug("id_zona: " + id_zona);
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQL  + " WITH UR");
			stm.setLong(1,id_zona);
			rs = stm.executeQuery();
			while (rs.next()) {
				ComunaDTO com = new ComunaDTO();
				com.setId_comuna(rs.getLong("ID_COMUNA"));
				com.setNombre(rs.getString("NOMBRE"));
				result.add(com);
			}

		}catch (SQLException e) {
			throw new LocalDAOException(String.valueOf(e.getErrorCode()),e);
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
	 * Obtiene listado de todas las comunas
	 * 
	 * @return List ComunaDTO
	 * @throws LocalDAOException
	 */
	public List getComunasAll() throws LocalDAOException {
		
		List result = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String SQLStmt = " SELECT c.id_comuna, c.nombre FROM bo_comunas c "	;
		logger.debug("Ejecución DAO getComunasAll()");
		logger.debug("SQL: " + SQLStmt);
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			rs = stm.executeQuery();
			while (rs.next()) {
				ComunaDTO com = new ComunaDTO();
				com.setId_comuna(rs.getLong("id_comuna"));
				com.setNombre(rs.getString("nombre"));
				result.add(com);
			}

		}catch (SQLException e) {
			throw new LocalDAOException(String.valueOf(e.getErrorCode()),e);
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
	

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.jumbocl.pedidos.dao.ZonasDespachoDAO#doBuscaZonaByRonda(long)
	 */
	public List doBuscaZonaByRonda(long id_ronda) throws ZonasDespachoDAOException {
		
		List result = new ArrayList();
		
		PreparedStatement stm =null;
		ResultSet rs = null;
		try {
			logger.debug("en doBuscaZonaByRonda");
			String SQLStmt = " SELECT z.id_zona,  z.nombre, z.orden_zona " +
							 " FROM bo_zonas z " +
							 " JOIN bo_pedidos p ON p.id_zona = z.id_zona " +
							 " JOIN bo_detalle_rondas dr ON dr.id_pedido = p.id_pedido " +
							 " WHERE dr.id_ronda = ? and p.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") " +
							 " GROUP BY z.id_zona, z.nombre, z.orden_zona " +
							 " ORDER BY z.orden_zona ";
			
			logger.debug("SQL: " + SQLStmt);
			logger.debug("id_ronda: " + id_ronda);
		
		
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt + " WITH UR" );
			stm.setLong(1,id_ronda);

			rs = stm.executeQuery();
			while (rs.next()) {
				ZonaDTO zona = new ZonaDTO();
				zona.setId_zona(rs.getLong("id_zona"));
				zona.setNombre(rs.getString("nombre"));
				zona.setOrden(rs.getInt("orden_zona"));
				result.add(zona);
			}

		}catch (SQLException e) {
			e.printStackTrace();
			throw new ZonasDespachoDAOException(String.valueOf(e.getErrorCode()),e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new ZonasDespachoDAOException(String.valueOf(e.getMessage()),e);
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
}
