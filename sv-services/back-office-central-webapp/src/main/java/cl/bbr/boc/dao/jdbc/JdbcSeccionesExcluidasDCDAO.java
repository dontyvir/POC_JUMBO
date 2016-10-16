package cl.bbr.boc.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import cl.bbr.boc.dao.SeccionesExcluidasDCDAO;
import cl.bbr.boc.dto.BOSeccionDTO;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.jumbo.common.dao.jdbc.JdbcDAO;

/**
 * @author JoLazoGu
 * @version 1
 */
public class JdbcSeccionesExcluidasDCDAO extends JdbcDAO implements
		SeccionesExcluidasDCDAO {
	private static ConexionUtil conexionUtil = new ConexionUtil();
	Logging logger = new Logging(this);

	public List getSeccionesExcluidas() throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List seccionesExcluidas = new ArrayList();
		BOSeccionDTO seccionDTO = null;
		String sql = "SELECT SED_ID,NOMBRE FROM FODBA.FO_SECCION_EXCLUIDA_DESCUENTO " +
				     "INNER JOIN BODBA.BO_SECCION ON BODBA.BO_SECCION.ID_SECCION=FODBA.FO_SECCION_EXCLUIDA_DESCUENTO.SED_ID WITH UR";
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				seccionDTO = new BOSeccionDTO();
				seccionDTO.setId_seccion(rs.getInt("SED_ID"));
				seccionDTO.setNombre(rs.getString("NOMBRE"));
				seccionesExcluidas.add(seccionDTO);
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return seccionesExcluidas;
	}

	public List getSecciones() throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List secciones = new ArrayList();
		BOSeccionDTO seccionDTO = null;
		String sql = "SELECT ID_SECCION, NOMBRE FROM BODBA.BO_SECCION " +
				     "WHERE ID_SECCION  NOT IN(SELECT SED_ID FROM FODBA.FO_SECCION_EXCLUIDA_DESCUENTO) " +
				     "ORDER BY NOMBRE WITH UR";
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				seccionDTO = new BOSeccionDTO();
				seccionDTO.setId_seccion(rs.getInt("ID_SECCION"));
				seccionDTO.setNombre(rs.getString("NOMBRE"));
				secciones.add(seccionDTO);
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return secciones;
	}

	public void excluirSeccion(int id_seccion) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		String sql = "INSERT INTO FODBA.FO_SECCION_EXCLUIDA_DESCUENTO VALUES(?)";
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id_seccion);
			ps.executeUpdate();
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(ps, con);
		}
	}

	public void permitirSeccion(int id_seccion) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		String sql = "DELETE FROM FODBA.FO_SECCION_EXCLUIDA_DESCUENTO WHERE SED_ID = ?";
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id_seccion);
			ps.executeUpdate();
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(ps, con);
		}
	}

}
