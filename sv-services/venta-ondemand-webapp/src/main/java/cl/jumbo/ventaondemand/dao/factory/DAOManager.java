package cl.jumbo.ventaondemand.dao.factory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cl.jumbo.ventaondemand.dao.ClientesDAO;
import cl.jumbo.ventaondemand.dao.ClientesDAOImpl;
import cl.jumbo.ventaondemand.dao.base.ConexionUtil;

/**
 * Clase que se conecta a la base de datos mediante JDBC.
 *
 */
public class DAOManager extends DAOFactory {
	
	public static Connection getConexion() throws SQLException {
		return ConexionUtil.getConexion();
	}
	public static void closeConnection(Connection conn) throws SQLException {
		ConexionUtil.closeConnection(conn);
	}
	public static void closeStatement(Statement state) throws SQLException {
		ConexionUtil.closeStatement(state);
	}
	public static void closePreparedStatement(PreparedStatement ps) throws SQLException {
		ConexionUtil.closePreparedStatement(ps);
	}
	public static void closeCallableStatement(CallableStatement cs) throws SQLException {
		ConexionUtil.closeCallableStatement(cs);
	}
	public static void closeResulset(ResultSet rs) throws SQLException {
		ConexionUtil.closeResulset(rs);
	}

	
	public ClientesDAO getClientesDAO() {
		return new ClientesDAOImpl();
	}
	
}