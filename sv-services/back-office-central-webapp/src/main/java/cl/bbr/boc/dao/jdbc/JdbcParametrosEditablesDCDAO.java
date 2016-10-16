package cl.bbr.boc.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import cl.bbr.boc.dao.ParametrosEditablesDCDAO;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.jumbo.common.dao.jdbc.JdbcDAO;

public class JdbcParametrosEditablesDCDAO extends JdbcDAO implements ParametrosEditablesDCDAO {
	private static ConexionUtil conexionUtil = new ConexionUtil();
	Logging logger = new Logging(this);
	
	public String getMontoLimite() throws DAOException {
		String sql = "SELECT VALOR FROM BODBA.BO_PARAMETROS WHERE ID_PARAMETRO = 12 WITH UR";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next())
				return rs.getString("VALOR");
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return null;
	}
	
	public String getDescuentoMayor() throws DAOException {
		String sql = "SELECT VALOR FROM BODBA.BO_PARAMETROS WHERE ID_PARAMETRO = 10 WITH UR";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next())
				return rs.getString("VALOR");
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return null;
	}
	
	public String getDescuentoMenor() throws DAOException {
		String sql = "SELECT VALOR FROM BODBA.BO_PARAMETROS WHERE ID_PARAMETRO = 11 WITH UR";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next())
				return rs.getString("VALOR");
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return null;
	}
	
	public void setMontoLimite(String monto) throws DAOException{
		String sql = "UPDATE BODBA.BO_PARAMETROS SET VALOR = ? WHERE ID_PARAMETRO = 12";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conexionUtil.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, monto);
            ps.executeUpdate();
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
	         close(ps, con);
	    }
	}
	
	public void setDescuentoMayor(String valor) throws DAOException{
		String sql = "UPDATE BODBA.BO_PARAMETROS SET VALOR = ? WHERE ID_PARAMETRO = 10";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conexionUtil.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, valor);
            ps.executeUpdate();
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
	         close(ps, con);
	    }
	}
	
	public void setDescuentoMenor(String valor) throws DAOException{
		String sql = "UPDATE BODBA.BO_PARAMETROS SET VALOR = ? WHERE ID_PARAMETRO = 11";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conexionUtil.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, valor);
            ps.executeUpdate();
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
	         close(ps, con);
	    }
	}
	
}
