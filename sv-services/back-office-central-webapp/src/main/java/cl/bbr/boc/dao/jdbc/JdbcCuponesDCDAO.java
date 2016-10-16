package cl.bbr.boc.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.boc.dao.CuponesDCDAO;
import cl.bbr.jumbocl.common.model.CuponEntity;
import cl.bbr.jumbocl.common.model.CuponPorProducto;
import cl.bbr.jumbocl.common.model.CuponPorRut;
import cl.bbr.jumbocl.common.model.RubroEntity;
import cl.bbr.jumbocl.common.model.SeccionEntity;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.jumbo.common.dao.jdbc.JdbcDAO;

/**
 * @author JoLazoGu
 * @version 1
 */
public class JdbcCuponesDCDAO extends JdbcDAO implements
	 CuponesDCDAO {
	private static ConexionUtil conexionUtil = new ConexionUtil();
	Logging logger = new Logging(this);

	/*
	 * (sin Javadoc)
	 * @see cl.bbr.boc.dao.CuponesDCDAO#getListaTiposCupones()
	 */
	
	public List getListaTiposCupones() throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List cupones = new ArrayList();
		CuponEntity cupon = null;
		String sql = "SELECT ID_CUP_DTO, CODIGO, TIPO, DESCUENTO, CANTIDAD, DESPACHO, PUBLICO, MEDIO_PAGO, FECHA_INI, FECHA_FIN "+
					 "FROM BODBA.BO_CUPON_DSCTO ORDER BY CODIGO WITH UR ";
		
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				cupon = new CuponEntity();
				cupon.setId_cup_dto(rs.getInt("ID_CUP_DTO"));
				cupon.setCodigo(rs.getString("CODIGO"));
				cupon.setTipo(rs.getString("TIPO"));
				cupon.setDescuento(rs.getInt("DESCUENTO"));
				cupon.setCantidad(rs.getInt("CANTIDAD"));
				cupon.setDespacho(rs.getInt("DESPACHO"));
				cupon.setPublico(rs.getInt("PUBLICO"));
				cupon.setMedio_pago(rs.getInt("MEDIO_PAGO"));
				cupon.setFecha_ini(rs.getString("FECHA_INI"));
				cupon.setFecha_fin(rs.getString("FECHA_FIN"));
				cupon.setStock(getTotalProductoPorIdCupon(rs.getInt("ID_CUP_DTO")));
	
				cupones.add(cupon);
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return cupones;
	}
	
	/**
	 * 
	 * @param id_cup_dto
	 * @return
	 * @throws DAOException
	 */
	public int getTotalProductoPorIdCupon(long id_cup_dto ) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int TotalProducto = 0;
		
		String sql = "SELECT COUNT(*) as TOTALPRODUCTO FROM BODBA.BO_CUPON_DSCTOXPRODS WHERE ID_CUP_DTO = ? WITH UR "; 
		
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			ps.setLong(1, id_cup_dto);
			rs = ps.executeQuery();
			
			if (rs.next())
				TotalProducto = rs.getInt("TOTALPRODUCTO");		
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return TotalProducto;
	}

	/**
	 * 
	 * @param cuponId
	 * @return
	 * @throws DAOException
	 */
	public CuponEntity getDatoCuponPorId(CuponEntity cuponId) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CuponEntity cupon = null;
		String sql = "SELECT ID_CUP_DTO, BODBA.BO_CUPON_DSCTO.ID_USUARIO, CODIGO, TIPO, DESCUENTO, CANTIDAD, DESPACHO, PUBLICO, MEDIO_PAGO, FECHA_INI, FECHA_FIN " +
				", FECHA_INS, FECHA_UPD, NOMBRE, APELLIDO, APELLIDO_MAT FROM BODBA.BO_CUPON_DSCTO " +
				"INNER JOIN BODBA.BO_USUARIOS ON BODBA.BO_USUARIOS.ID_USUARIO = BODBA.BO_CUPON_DSCTO.ID_USUARIO " +
				"WHERE ID_CUP_DTO = ? WITH UR ";
		
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			ps.setLong(1, cuponId.getId_cup_dto());
			rs = ps.executeQuery();
			
			if (rs.next()) {	
				cupon = new CuponEntity();
				cupon.setId_cup_dto(rs.getLong("ID_CUP_DTO"));
				cupon.setId_usuario(rs.getLong("ID_USUARIO"));
				cupon.setCodigo(rs.getString("CODIGO"));
				cupon.setTipo(rs.getString("TIPO"));
				cupon.setDescuento(rs.getInt("DESCUENTO"));
				cupon.setCantidad(rs.getInt("CANTIDAD"));
				cupon.setDespacho(rs.getInt("DESPACHO"));
				cupon.setPublico(rs.getInt("PUBLICO"));
				cupon.setMedio_pago(rs.getInt("MEDIO_PAGO"));
				cupon.setFecha_ini(rs.getString("FECHA_INI"));
				cupon.setFecha_fin(rs.getString("FECHA_FIN"));
				cupon.setFecha_ins(rs.getString("FECHA_INS"));
				cupon.setFecha_upd(rs.getString("FECHA_UPD"));
				cupon.setNombre_usuario(rs.getString("NOMBRE")+ " " +rs.getString("APELLIDO")+ " " +rs.getString("APELLIDO_MAT"));
			}
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return cupon;
	}
	
	/**
	 * 
	 * @param cuponCodigo
	 * @return
	 * @throws DAOException
	 */
	public CuponEntity getDatoCuponPorCodigo(CuponEntity cuponCodigo) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CuponEntity cupon = null;
		String sql = "SELECT ID_CUP_DTO, BODBA.BO_CUPON_DSCTO.ID_USUARIO, CODIGO, TIPO, DESCUENTO, CANTIDAD, DESPACHO, PUBLICO, MEDIO_PAGO, FECHA_INI, FECHA_FIN" +
					 ", FECHA_INS, FECHA_UPD, NOMBRE, APELLIDO, APELLIDO_MAT FROM BODBA.BO_CUPON_DSCTO " +
					 "INNER JOIN BODBA.BO_USUARIOS ON BODBA.BO_USUARIOS.ID_USUARIO = BODBA.BO_CUPON_DSCTO.ID_USUARIO " +
					 "WHERE CODIGO = ? WITH UR ";
		
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			ps.setString(1, cuponCodigo.getCodigo());
			rs = ps.executeQuery();
			
			if (rs.next()) {
				cupon = new CuponEntity();
				cupon.setId_cup_dto(rs.getLong("ID_CUP_DTO"));
				cupon.setId_usuario(rs.getLong("ID_USUARIO"));
				cupon.setCodigo(rs.getString("CODIGO"));
				cupon.setTipo(rs.getString("TIPO"));
				cupon.setDescuento(rs.getInt("DESCUENTO"));
				cupon.setCantidad(rs.getInt("CANTIDAD"));
				cupon.setDespacho(rs.getInt("DESPACHO"));
				cupon.setPublico(rs.getInt("PUBLICO"));
				cupon.setMedio_pago(rs.getInt("MEDIO_PAGO"));
				cupon.setFecha_ini(rs.getString("FECHA_INI"));
				cupon.setFecha_fin(rs.getString("FECHA_FIN"));
				cupon.setFecha_ins(rs.getString("FECHA_INS"));
				cupon.setFecha_upd(rs.getString("FECHA_UPD"));
				cupon.setNombre_usuario(rs.getString("NOMBRE")+ " " +rs.getString("APELLIDO")+ " " +rs.getString("APELLIDO_MAT"));
			}
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return cupon;
	}
	
	/**
	 * 
	 * @param cupon
	 * @return
	 * @throws DAOException
	 */
	public boolean getExisteCupon(CuponEntity cupon) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result = false;
		
		String sql = "select CODIGO from BODBA.BO_CUPON_DSCTO WHERE CODIGO = ?";
		
		try {
			 con = conexionUtil.getConexion();
	         ps = con.prepareStatement(sql + " WITH UR");
	         ps.setString(1, cupon.getCodigo());
	         rs = ps.executeQuery();
	         
	         if (rs.next())
	               result = true;
		
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return result;
	}
	
	/**
	 * 
	 * @param cupon
	 * @return
	 * @throws DAOException
	 */
	public boolean setGuardarCupon(CuponEntity cupon) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		boolean result = false;   
		String sql = "INSERT INTO BODBA.BO_CUPON_DSCTO(ID_USUARIO, CODIGO, TIPO, DESCUENTO, CANTIDAD, PUBLICO, MEDIO_PAGO, FECHA_INI, FECHA_FIN) " +
				     "VALUES(?,?,?,?,?,?,?,?,?)";
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			
			ps.setLong(1, cupon.getId_usuario());
			ps.setString(2, cupon.getCodigo());
			ps.setString(3, cupon.getTipo());
			ps.setInt(4, cupon.getDescuento());
			ps.setInt(5, cupon.getCantidad());
			ps.setInt(6, cupon.getPublico());
			ps.setInt(7, cupon.getMedio_pago());
			ps.setString(8, cupon.getFecha_ini());
			ps.setString(9, cupon.getFecha_fin());
			
			ps.executeUpdate();
			
			result = true;
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(ps, con);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param cupon
	 * @throws DAOException
	 */
	public void setTodasLasSeccionesAsociado(CuponEntity cupon) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		 
		
		String sql = "INSERT INTO BODBA.BO_CUPON_DSCTOXPRODS (ID_CUP_DTO, ID_SECCION) "+  
				     "SELECT "+cupon.getId_cup_dto()+", ID_SECCION FROM BODBA.BO_SECCION A "+
				     "WHERE ID_SECCION  NOT IN (SELECT SED_ID FROM FODBA.FO_SECCION_EXCLUIDA_DESCUENTO)";
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			
			ps.executeUpdate();
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(ps, con);
		}
	}
	
	/**
	 * 
	 * @param cupon
	 * @return
	 * @throws DAOException
	 */
	public boolean setActualizarCupon(CuponEntity cupon) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		boolean result = false;  
		
		String sql = "UPDATE BODBA.BO_CUPON_DSCTO SET ID_USUARIO = ?, CODIGO = ?, TIPO = ?, DESCUENTO = ?, CANTIDAD = ?, " +
					 "DESPACHO = ?, PUBLICO = ?, MEDIO_PAGO = ?, FECHA_INI = ?, FECHA_FIN = ?, FECHA_UPD = CURRENT TIMESTAMP WHERE ID_CUP_DTO = ? ";
		
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			
			ps.setLong(1, cupon.getId_usuario());
			ps.setString(2, cupon.getCodigo());
			ps.setString(3, cupon.getTipo());
			ps.setInt(4, cupon.getDescuento());
			ps.setInt(5, cupon.getCantidad());
			
			if(cupon.getTipo().equals("D"))
				ps.setInt(6, 1);
			else
				ps.setInt(6, cupon.getDespacho());
			
			ps.setInt(7, cupon.getPublico());
			
			if(cupon.getPublico() == 1)
				setBorrarRutMasivaPorIdCupon(cupon.getId_cup_dto());
			
			ps.setInt(8, cupon.getMedio_pago());
			ps.setString(9, cupon.getFecha_ini());
			ps.setString(10, cupon.getFecha_fin());
			ps.setLong(11, cupon.getId_cup_dto());
			
			ps.executeUpdate();
			
			result = true;
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(ps, con);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param Id_cup_dto
	 * @throws DAOException
	 */
	public void setBorrarRutMasivaPorIdCupon(long Id_cup_dto) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
	
		String sql = "DELETE FROM BODBA.BO_CUPON_DSCTOXRUT WHERE ID_CUP_DTO = ? ";
		
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			ps.setLong(1, Id_cup_dto);
			
			ps.executeUpdate();
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(ps, con);
		}
		
	}
	
	/**
	 * 
	 * @return
	 * @throws DAOException
	 */
	public List getListaRubros() throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List rubros = new ArrayList();
		RubroEntity rubro  = null;
		
		String sql = "select ID_SECCION, ID_RUBRO, NOMBRE_RUBRO from BO_RUBROXSECCION order by NOMBRE_RUBRO WITH UR ";
		
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
		
			while (rs.next()) {
				rubro = new RubroEntity();	
				rubro.setIdRubro(rs.getInt("ID_SECCION"));
				rubro.setIdSeccion(rs.getInt("ID_RUBRO"));
				rubro.setNombreRubro(rs.getString("NOMBRE_RUBRO"));
				
				rubros.add(rubro);
			}
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return rubros;
	}
	
	/**
	 * 
	 * @return
	 * @throws DAOException
	 */
	public List getListaSecciones() throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List secciones = new ArrayList();
		SeccionEntity seccion  = null;
		
		String sql = "select ID_SECCION, NOMBRE from BO_SECCION order by NOMBRE WITH UR ";
		
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				seccion = new SeccionEntity();
				seccion.setIdSeccion(rs.getInt("ID_SECCION"));
				seccion.setNombreSeccion(rs.getString("NOMBRE"));
				
				secciones.add(seccion);
			}
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return secciones;
	}
	
	
	/**
	 * 
	 * @param cpr
	 * @param id_usuario
	 * @return
	 * @throws DAOException
	 */
	public boolean setCargaRutMasiva(CuponPorRut cpr, long id_usuario) throws DAOException {
		Connection con = null;
		Connection conUp = null;
		PreparedStatement ps = null;
		PreparedStatement psUp = null;
		boolean result = false;   
		
		String sql = "INSERT INTO BODBA.BO_CUPON_DSCTOXRUT(ID_CUP_DTO, RUT) VALUES(?,?)";
		
		String sqlUp = "UPDATE BODBA.BO_CUPON_DSCTO SET ID_USUARIO = ?, PUBLICO = ?, FECHA_UPD = CURRENT TIMESTAMP WHERE ID_CUP_DTO = ? ";
		
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			ps.setLong(1, cpr.getId_cupon());
			ps.setLong(2, cpr.getRut());
			ps.executeUpdate();
			
			conUp = conexionUtil.getConexion();
			psUp = conUp.prepareStatement(sqlUp);
			psUp.setLong(1, id_usuario);
			psUp.setInt(2, 0);
			psUp.setLong(3, cpr.getId_cupon());
			psUp.executeUpdate();
			
			result = true;
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(ps, con);
			close(ps, conUp);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param id_cup_dto
	 * @return
	 * @throws DAOException
	 */
	public int getCantidadRutAsociado(int id_cup_dto) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int TotalRut = 0;
		
		String sql = "SELECT COUNT(*) as TOTALRUT FROM BODBA.BO_CUPON_DSCTOXRUT WHERE ID_CUP_DTO = ? WITH UR "; 
		
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			ps.setLong(1, id_cup_dto);
			rs = ps.executeQuery();
			
			if (rs.next())
				TotalRut = rs.getInt("TOTALRUT");
				
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return TotalRut;
	} 
	
	/**
	 * 
	 * @param cpp
	 * @param rad_tipo
	 * @param id_usuario
	 * @throws DAOException
	 */
	public void setCuponAsociarTipo(CuponPorProducto cpp, String rad_tipo, long id_usuario, String rad_despacho ) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		
		String sql = null;
		
		String tipo = getBuscarTipoPorIdCupon(cpp.getId_cup_dto());
		
		
		if(rad_tipo.equals("S") && (rad_tipo.equals(tipo) || tipo.equals(""))) {
			
			sql = "INSERT INTO BO_CUPON_DSCTOXPRODS (ID_CUP_DTO, ID_SECCION) VALUES (?, ?)";
			
			try {
				con = conexionUtil.getConexion();
				ps = con.prepareStatement(sql);
				ps.setLong(1, cpp.getId_cup_dto());
				ps.setLong(2, cpp.getId_seccion());
				ps.executeUpdate();
			
			} catch (Exception e) {
				logger.error("Error: ", e);
				throw new DAOException(e);
			} finally {
				close(ps, con);
			}
		}
		else if(rad_tipo.equals("S") && !rad_tipo.equals(tipo)){
			
			setBorrarAsociacionesPorIdCupon(cpp.getId_cup_dto());
			
			sql = "INSERT INTO BO_CUPON_DSCTOXPRODS (ID_CUP_DTO, ID_SECCION) VALUES (?, ?)";
			
			try {
				con = conexionUtil.getConexion();
				ps = con.prepareStatement(sql);
				ps.setLong(1, cpp.getId_cup_dto());
				ps.setLong(2, cpp.getId_seccion());
				ps.executeUpdate();
			
			} catch (Exception e) {
				logger.error("Error: ", e);
				throw new DAOException(e);
			} finally {
				close(ps, con);
			}
		}
		
		if(rad_tipo.equals("R") && (rad_tipo.equals(tipo) || tipo.equals(""))){
			
			sql = "INSERT INTO BO_CUPON_DSCTOXPRODS (ID_CUP_DTO, ID_RUBRO, ID_SECCION) VALUES (?, ?, ?)";
			
			try {
				con = conexionUtil.getConexion();
				ps = con.prepareStatement(sql);
				ps.setLong(1, cpp.getId_cup_dto());
				ps.setLong(2, cpp.getId_rubro());
				ps.setLong(3, cpp.getId_seccion());
				ps.executeUpdate();
			
			} catch (Exception e) {
				logger.error("Error: ", e);
				throw new DAOException(e);
			} finally {
				close(ps, con);
			}
		}
		else if(rad_tipo.equals("R") && !rad_tipo.equals(tipo)){
			
			setBorrarAsociacionesPorIdCupon(cpp.getId_cup_dto());
			
			sql = "INSERT INTO BO_CUPON_DSCTOXPRODS (ID_CUP_DTO, ID_RUBRO, ID_SECCION) VALUES (?, ?, ?)";
			
			try {
				con = conexionUtil.getConexion();
				ps = con.prepareStatement(sql);
				ps.setLong(1, cpp.getId_cup_dto());
				ps.setLong(2, cpp.getId_rubro());
				ps.setLong(3, cpp.getId_seccion());
				ps.executeUpdate();
			
			} catch (Exception e) {
				logger.error("Error: ", e);
				throw new DAOException(e);
			} finally {
				close(ps, con);
			}
		}
		
		if(rad_tipo.equals("P") && (rad_tipo.equals(tipo) || tipo.equals(""))){
			
			sql = "INSERT INTO BO_CUPON_DSCTOXPRODS (ID_CUP_DTO, ID_PRODUCTO) VALUES (?, ?)";
			
			try {
				con = conexionUtil.getConexion();
				ps = con.prepareStatement(sql);
				ps.setLong(1, cpp.getId_cup_dto());
				ps.setLong(2, cpp.getId_producto());
				ps.executeUpdate();
			
			} catch (Exception e) {
				logger.error("Error: ", e);
				throw new DAOException(e);
			} finally {
				close(ps, con);
			}
		}
		else if(rad_tipo.equals("P") && !rad_tipo.equals(tipo)){
			
			setBorrarAsociacionesPorIdCupon(cpp.getId_cup_dto());
			
			sql = "INSERT INTO BO_CUPON_DSCTOXPRODS (ID_CUP_DTO, ID_PRODUCTO) VALUES (?, ?)";
			
			try {
				con = conexionUtil.getConexion();
				ps = con.prepareStatement(sql);
				ps.setLong(1, cpp.getId_cup_dto());
				ps.setLong(2, cpp.getId_producto());
				ps.executeUpdate();
			
			} catch (Exception e) {
				logger.error("Error: ", e);
				throw new DAOException(e);
			} finally {
				close(ps, con);
			}
		}
		
		if(rad_tipo.equals("TS"))
			setBorrarAsociacionesPorIdCupon(cpp.getId_cup_dto());
		
		if(rad_tipo.equals("D"))
			setBorrarAsociacionesPorIdCupon(cpp.getId_cup_dto());

		
		sql = "UPDATE BODBA.BO_CUPON_DSCTO SET ID_USUARIO = ?, TIPO = ?, DESPACHO = ?, FECHA_UPD = CURRENT TIMESTAMP WHERE ID_CUP_DTO = ? ";
	
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			ps.setLong(1, id_usuario);
			ps.setString(2, rad_tipo);
			ps.setInt(3, Integer.parseInt(rad_despacho));
			ps.setLong(4, cpp.getId_cup_dto());
			ps.executeUpdate();
		
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(ps, con);
		}
	}
	
	/**
	 * 
	 * @param id_cup_dto
	 * @return
	 * @throws DAOException
	 */
	public List getListaCuponAsociado(int id_cup_dto) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List asociaciones = new ArrayList();
		CuponPorProducto cpp = null;
		
		String sql = "SELECT ID_CUP_DTO, ID_PRODUCTO, ID_RUBRO, ID_SECCION FROM BO_CUPON_DSCTOXPRODS WHERE ID_CUP_DTO = ? WITH UR ";
		
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			ps.setLong(1, id_cup_dto);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				cpp = new CuponPorProducto();
				cpp.setId_cup_dto(rs.getInt("ID_CUP_DTO"));
					
				if(rs.getInt("ID_PRODUCTO") != 0) {
				
					cpp.setId_prodRubSecc(rs.getInt("ID_PRODUCTO"));
					cpp.setNombreProdRubSecc(getProductoAsociado(rs.getInt("ID_PRODUCTO")));
				
				}else if(rs.getInt("ID_SECCION") != 0 && rs.getInt("ID_RUBRO") ==0) {
					
					cpp.setId_prodRubSecc(rs.getInt("ID_SECCION"));
					cpp.setNombreProdRubSecc(getSeccionAsociado(rs.getInt("ID_SECCION")));
				
				}else if(rs.getInt("ID_RUBRO") !=0) {
				
					String codRubro = rs.getString("ID_SECCION")+rs.getString("ID_RUBRO");
					cpp.setId_prodRubSecc(Integer.parseInt(codRubro));
					cpp.setNombreProdRubSecc(getRubroAsociado(rs.getInt("ID_RUBRO") , rs.getInt("ID_SECCION")));
				}
					
				asociaciones.add(cpp);
			}
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return asociaciones;
	}
	
	
	/**
	 * 
	 * @param id_prod
	 * @return
	 * @throws DAOException
	 */
	public String getProductoAsociado(int id_prod) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String producto = null;
		
		String sql = "select PRO_TIPO_PRODUCTO, MAR_NOMBRE, PRO_DES_CORTA from FODBA.FO_PRODUCTOS " +
				"INNER JOIN FODBA.FO_MARCAS ON FODBA.FO_MARCAS.MAR_ID = FODBA.FO_PRODUCTOS.PRO_MAR_ID " +
				"where PRO_ID_BO= ? WITH UR ";
		
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			ps.setString(1, String.valueOf(id_prod));
			rs = ps.executeQuery();
			
			if(rs.next())				
				producto = (rs.getString("PRO_TIPO_PRODUCTO") + " " + rs.getString("MAR_NOMBRE") + " " + rs.getString("PRO_DES_CORTA")); 
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return producto;
	}
	
	/**
	 * 
	 * @param id_rubr
	 * @param id_secc
	 * @return
	 * @throws DAOException
	 */
	public String getRubroAsociado(int id_rubr, int id_secc) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String rubro = "";

		String sql = "select NOMBRE_RUBRO from BO_RUBROXSECCION where ID_SECCION = ? and ID_RUBRO = ? WITH UR ";
		
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			ps.setLong(1, id_secc);
			ps.setLong(2, id_rubr);
			rs = ps.executeQuery();
			
			if(rs.next())
				rubro = rs.getString("NOMBRE_RUBRO"); 
				
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return rubro;
	}
	
	/**
	 * 
	 * @param id_secc
	 * @return
	 * @throws DAOException
	 */
	public String getSeccionAsociado(int id_secc) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String seccion = "";
		
		String sql = "select NOMBRE from BO_SECCION where ID_SECCION = ? WITH UR  ";
		
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			ps.setLong(1, id_secc);
			rs = ps.executeQuery();
			
			if(rs.next())	
				seccion = rs.getString("NOMBRE"); 
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return seccion;
	}
	
	/**
	 * 
	 * @param Id_cup_dto
	 * @return
	 * @throws DAOException
	 */
	public boolean setBorrarAsociacionesPorIdCupon(long Id_cup_dto) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		
		boolean result = false;   
		
		String sql = "DELETE FROM BODBA.BO_CUPON_DSCTOXPRODS WHERE ID_CUP_DTO = ? ";
		
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			ps.setLong(1, Id_cup_dto);
			ps.executeUpdate();
			
			result = true;
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(ps, con);
		}
		
		return result;
		
	}
	
	/**
	 * 
	 * @param Id_cup_dto
	 * @return
	 * @throws DAOException
	 */
	public String getBuscarTipoPorIdCupon( long Id_cup_dto ) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String tipo = null;
		
		String sql = "SELECT TIPO FROM BODBA.BO_CUPON_DSCTO WHERE ID_CUP_DTO = ? WITH UR "; 
		
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			ps.setLong(1, Id_cup_dto);
			rs = ps.executeQuery();
			
			if (rs.next())	
				tipo = rs.getString("TIPO");
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return tipo;
	}
	
}
