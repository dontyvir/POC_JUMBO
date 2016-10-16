package cl.bbr.cupondscto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.cupondscto.dto.CarroAbandonadoDTO;
import cl.bbr.cupondscto.exception.CuponDsctoDAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;

public class JdbcCuponDsctoDAO implements CuponDsctoDAO {

	Logging logger = new Logging(this);
	
	public CuponDsctoDTO getCuponDscto(String codigo) throws CuponDsctoDAOException{
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		CuponDsctoDTO cupon = null;
		try {
			String query = "SELECT ID_CUP_DTO, CODIGO, TIPO, DESCUENTO, " +
					"CANTIDAD, DESPACHO, PUBLICO, MEDIO_PAGO, FECHA_INI, FECHA_FIN " +
					"FROM BODBA.BO_CUPON_DSCTO WHERE CODIGO = ? " +
					"AND (FECHA_FIN >= CURRENT DATE AND (FECHA_INI <= CURRENT DATE)) ";
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query + "WITH UR");
			stm.setString(1, codigo);
			rs = stm.executeQuery();
			while (rs.next()) {
				cupon = new CuponDsctoDTO();
				cupon.setId_cup_dto(rs.getLong("ID_CUP_DTO"));
				cupon.setCodigo(rs.getString("CODIGO"));
				cupon.setTipo(rs.getString("TIPO"));
				cupon.setDescuento(rs.getInt("DESCUENTO"));
				cupon.setCantidad(rs.getInt("CANTIDAD"));
				cupon.setDespacho(rs.getInt("DESPACHO"));
				cupon.setPublico(rs.getInt("PUBLICO"));
				cupon.setMedio_pago(rs.getInt("MEDIO_PAGO"));
				cupon.setFecha_ini(rs.getString("FECHA_INI"));
				cupon.setFecha_fin(rs.getString("FECHA_FIN"));
			}
			
		} catch (SQLException ex) {
			
			logger.error("Error (sql) getCuponDscto", ex);
			throw new CuponDsctoDAOException(ex);
		} catch (Exception e) {
			logger.error("Error getCuponDscto", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stm != null) {
					stm.close();
				}
				if ((conexion != null) && !conexion.isClosed()) {
					conexion.close();
				}
			} catch (SQLException e) {
				logger.error("Error (close) getCuponDscto", e);
			}
		}
		return cupon;
	}
	public CuponDsctoDTO getCuponDsctoById(int idCupon) throws CuponDsctoDAOException{
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		CuponDsctoDTO cupon = null;
		try {
			String query = "SELECT ID_CUP_DTO, CODIGO, TIPO, DESCUENTO, " +
					"CANTIDAD, DESPACHO, PUBLICO, MEDIO_PAGO, FECHA_INI, FECHA_FIN " +
					"FROM BODBA.BO_CUPON_DSCTO WHERE ID_CUP_DTO = ? " +
					"AND (FECHA_FIN >= CURRENT DATE AND (FECHA_INI <= CURRENT DATE)) ";
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query + "WITH UR");
			stm.setInt(1, idCupon);
			rs = stm.executeQuery();
			while (rs.next()) {
				cupon = new CuponDsctoDTO();
				cupon.setId_cup_dto(rs.getLong("ID_CUP_DTO"));
				cupon.setCodigo(rs.getString("CODIGO"));
				cupon.setTipo(rs.getString("TIPO"));
				cupon.setDescuento(rs.getInt("DESCUENTO"));
				cupon.setCantidad(rs.getInt("CANTIDAD"));
				cupon.setDespacho(rs.getInt("DESPACHO"));
				cupon.setPublico(rs.getInt("PUBLICO"));
				cupon.setMedio_pago(rs.getInt("MEDIO_PAGO"));
				cupon.setFecha_ini(rs.getString("FECHA_INI"));
				cupon.setFecha_fin(rs.getString("FECHA_FIN"));
			}
			
		} catch (SQLException ex) {
			
			logger.error("Error (sql) getCuponDsctoById", ex);
			throw new CuponDsctoDAOException(ex);
		} catch (Exception e) {
			logger.error("Error getCuponDscto", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stm != null) {
					stm.close();
				}
				if ((conexion != null) && !conexion.isClosed()) {
					conexion.close();
				}
			} catch (SQLException e) {
				logger.error("Error (close) getCuponDsctoById", e);
			}
		}
		return cupon;
	}

	public boolean isCuponForRut(long rut, long id_cupon) throws CuponDsctoDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			String query = "SELECT ID_CUP_DTO FROM BODBA.BO_CUPON_DSCTOXRUT WHERE RUT = ? AND ID_CUP_DTO = ? ";
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query + "WITH UR");
			stm.setLong(1, rut);
			stm.setLong(2, id_cupon);
			rs = stm.executeQuery();
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException ex) {
			logger.error("Error (sql) isCuponForRut", ex);
		} catch (Exception e) {
			logger.error("Error isCuponForRut", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stm != null) {
					stm.close();
				}
				if ((conexion != null) && !conexion.isClosed()) {
					conexion.close();
				}
			} catch (SQLException e) {
				logger.error("Error (close) isCuponForRut", e);
				throw new CuponDsctoDAOException(e);
			}
		}
		return result;
	}

	public List getProdsCupon(long id_cupon, String tipo) throws CuponDsctoDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		String query = null;
		List id_prods = new ArrayList();
		String dato = "";
		String dato2 = "";
		try {
			if (tipo.equals("P")) {
				query = "SELECT ID_PRODUCTO FROM BODBA.BO_CUPON_DSCTOXPRODS WHERE ID_CUP_DTO = ? ";
				dato = "ID_PRODUCTO";
			} else if (tipo.equals("R")) {
				query = "SELECT ID_SECCION, ID_RUBRO FROM BODBA.BO_CUPON_DSCTOXPRODS WHERE ID_CUP_DTO = ? "; 
				dato = "ID_SECCION";
				dato2 = "ID_RUBRO";
			} else if (tipo.equals("S")){
				query = "SELECT ID_SECCION FROM BODBA.BO_CUPON_DSCTOXPRODS WHERE ID_CUP_DTO = ? ";
				dato = "ID_SECCION";
			}
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query + "WITH UR");
			stm.setLong(1, id_cupon);
			rs = stm.executeQuery();
			while (rs.next()) {
				if (tipo.equals("R"))
					id_prods.add(rs.getString(dato)+ rs.getString(dato2));
				else
					id_prods.add(rs.getString(dato));
			}
		} catch (SQLException ex) {
			logger.error("Error (sql) getProdsCupon", ex);
			throw new CuponDsctoDAOException(ex);
		} catch (Exception e) {
			logger.error("Error getProdsCupon", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stm != null) {
					stm.close();
				}
				if ((conexion != null) && !conexion.isClosed()) {
					conexion.close();
				}
			} catch (SQLException e) {
				logger.error("Error (close) getProdsCupons", e);
				throw new CuponDsctoDAOException(e);
			}
		}
		return id_prods;
	}
	
	
	public boolean dsctaStockCupon(long idCupon) throws CuponDsctoDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;

		boolean result = false;
		try {
			String query = "UPDATE BODBA.BO_CUPON_DSCTO SET CANTIDAD = (SELECT (cantidad -1) FROM BODBA.BO_CUPON_DSCTO WHERE ID_CUP_DTO = ?), FECHA_UPD = CURRENT TIMESTAMP WHERE ID_CUP_DTO = ? AND CANTIDAD > 0 ";
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query + " WITH UR");
			stm.setLong(1, idCupon);
			stm.setLong(2, idCupon);
			int rs = stm.executeUpdate();
			if (rs > 0) {
				result = true;
			}
		} catch (SQLException ex) {
			logger.error("Error (sql) dsctaStockCupon", ex);
		} catch (Exception e) {
			logger.error("Error dsctaStockCupon", e);
		} finally {
			try {
				if (stm != null) {
					stm.close();
				}
				if ((conexion != null) && !conexion.isClosed()) {
					conexion.close();
				}
			} catch (SQLException e) {
				logger.error("Error (close) dsctaStockCupon", e);
			}
		}
		
		return result;
	}
	
	
	/**
	 * 
	 * @param idCupon
	 * @param idPedido
	 * @return
	 * @throws CuponDsctoDAOException
	 */
	public boolean setIdCuponIdPedido( long idCupon, long idPedido ) throws CuponDsctoDAOException {
		
		Connection conexion = null;
		PreparedStatement stm = null;
		boolean result = false;

		try {
		
			String query = "INSERT INTO BODBA.BO_CUPON_DSCTOXPEDIDO(ID_CUPON, ID_PEDIDO) VALUES(?,?) ";
			conexion = JdbcDAOFactory.getConexion();
			
			stm = conexion.prepareStatement( query + " WITH UR" );
			stm.setLong( 1, idCupon );
			stm.setLong( 2, idPedido );
			
			int rs = stm.executeUpdate();
			
			if ( rs > 0 ) {
				
				result = true;
			}
			
		} catch ( SQLException ex ) {
			
			logger.error( "Error (sql) setIdCuponIdPedido", ex );
		
		} catch ( Exception e ) {
			
			logger.error( "Error setIdCuponIdPedido", e );
			
		} finally {
			
			try {
				
				if ( stm != null ) {
					
					stm.close();
				
				}
				
				if ( ( conexion != null ) && !conexion.isClosed() ) {
				
					conexion.close();
				
				}
				
			} catch ( SQLException e ) {
				
				logger.error( "Error (close) setIdCuponIdPedido", e );
			
			}
			
		}
		
		return result;
	
	}
	
	/**
	 * Carro Abandonado
	 */

	public CarroAbandonadoDTO getCuponCarroAbandonado(int codigo) throws CuponDsctoDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		CarroAbandonadoDTO cupon = null;
		try {
			String query =	"SELECT CA.CAR_CLI_ID, CA.CAR_FEC_MAIL, CA.CAR_TIPO_DCTO, CA.CAR_CLI_NOMBRES, " +
						    "CA.CAR_FECHA_FIN, CA.CAR_MONTO_DCTO, CA.CAR_CUPON, CA.CAR_NOM_SECCION, CA.CAR_ESTADO_MAIL "+
							"FROM FODBA.FO_CARRO_ABANDONADO CA "+
							"WHERE CA.CAR_ID = ? ";
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query + "WITH UR");
			stm.setInt(1, codigo);
			rs = stm.executeQuery();
			while (rs.next()) {
				cupon = new CarroAbandonadoDTO();

				cupon.setCar_tipo_dcto(rs.getString("CAR_TIPO_DCTO"));
				cupon.setCar_cli_nombres(rs.getString("CAR_CLI_NOMBRES"));
				cupon.setCar_monto_dcto(rs.getString("CAR_MONTO_DCTO"));
				cupon.setCar_cupon(rs.getString("CAR_CUPON"));
				cupon.setCar_nom_seccion(rs.getString("CAR_NOM_SECCION"));
				cupon.setCar_fecha_fin(rs.getString("CAR_FECHA_FIN"));

			}
		} catch (SQLException ex) {
			logger.error("Error (sql) getCuponDscto", ex);
		} catch (Exception e) {
			logger.error("Error getCuponDscto", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stm != null) {
					stm.close();
				}
				if ((conexion != null) && !conexion.isClosed()) {
					conexion.close();
				}
			} catch (SQLException e) {
				logger.error("Error (close) getCuponCarroAbandonado", e);
			}
		}
		return cupon;
	}


}
