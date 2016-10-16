package cl.bbr.boc.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;

import cl.bbr.boc.dao.CriterioSustitucionDAO;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.jumbo.common.dao.jdbc.JdbcDAO;

public class JdbcCriterioSustitucionDAO extends JdbcDAO implements CriterioSustitucionDAO {
	private static ConexionUtil conexionUtil = new ConexionUtil();
	Logging logger = new Logging(this);
	
	public int noSustituir(long id_pedido) throws DAOException {
		String sql = "UPDATE BODBA.BO_DETALLE_PEDIDO SET ID_CRITERIO = 5, DESC_CRITERIO = 'No sustituir' WHERE id_pedido = ?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conexionUtil.getConexion();
            ps = con.prepareStatement(sql);
            ps.setLong(1, id_pedido);
            return ps.executeUpdate();
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
	         close(ps, con);
	    }
	}

	public void registrarTracking(long id_pedido, String usuario) throws DAOException {
		String sql = "INSERT INTO BO_TRACKING_OD(ID_PEDIDO, USUARIO, DESCRIPCION) VALUES(?, ?,'Se cambia criterio a no sustituir')";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conexionUtil.getConexion();
            ps = con.prepareStatement(sql);
            ps.setLong(1, id_pedido);
            ps.setString(2, usuario);
            ps.executeUpdate();
		} catch (Exception e) {
			logger.error("Error: ", e);
			throw new DAOException(e);
		} finally {
	        close(ps, con);
	    }
	}

}
