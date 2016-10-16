package cl.bbr.boc.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

import cl.bbr.boc.dao.CargarColaboradoresDAO;
import cl.bbr.boc.dto.ColaboradorDTO;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.jumbo.common.dao.jdbc.JdbcDAO;

public class JdbcCargarColaboradoresDAO extends JdbcDAO implements CargarColaboradoresDAO {
	private static ConexionUtil conexionUtil = new ConexionUtil();
	Logging logger = new Logging(this);

	public boolean truncateTableColaboradres() throws DAOException {
		logger.info("Inicio truncateTableColaboradres");
		String sql = "DELETE FROM FODBA.FO_COLABORADOR";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			ps.executeUpdate();
			logger.info("Fin truncateTableColaboradres");
			return true;
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(ps, con);
		}
	}

	public boolean cargarColaboradores(List colaboradores) throws DAOException {
		logger.info("Inicio de cargarColaboradores");
		String sql = "INSERT INTO FODBA.FO_COLABORADOR(COL_RUT, COD_EMPRESA, EMPRESA) VALUES(?, ?, ?)";
		Connection con = null;
		PreparedStatement ps = null;
		final int batchSize = 1000;
		int count = 0;
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			Iterator itCC = colaboradores.iterator();
			while (itCC.hasNext()) {
				ColaboradorDTO cDTO = (ColaboradorDTO) itCC.next();
				ps.setInt(1, cDTO.getColRut());
				ps.setInt(2, cDTO.getCodEmpresa());
				ps.setString(3, cDTO.getEmpresa());
				ps.addBatch();
				if(++count % batchSize == 0) {
			        ps.executeBatch();
			    }
			}
			ps.executeBatch();
			logger.info("Cantidad de registros insertados: " + count);
			logger.info("Fin de cargarColaboradores");
			return true;
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(ps, con);
		}
	}
	
	public String cantidadColaboradores() throws DAOException{
		String sql = "SELECT COUNT(0) AS CANTIDAD FROM FODBA.FO_COLABORADOR WITH UR";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next())
				return rs.getString("CANTIDAD");
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return "0";
	}

}
