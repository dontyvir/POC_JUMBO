package cl.bbr.cupondscto.dao;

import java.sql.Connection;
import java.sql.SQLException;

import cl.bbr.jumbocl.shared.conexion.ConexionUtil;

public class JdbcDAOFactory extends DAOFactory {

	private static ConexionUtil conexionutil = new ConexionUtil();
	
	public static Connection getConexion() throws SQLException 
	{
		return conexionutil.getConexion();
	}

	public CuponDsctoDAO getCuponDsctoDAO() {
		return new JdbcCuponDsctoDAO();
	}
	
	public CuponDsctoDAO setIdCuponIdPedido() {
		// TODO Apéndice de método generado automáticamente
		return new JdbcCuponDsctoDAO();
	}

}
