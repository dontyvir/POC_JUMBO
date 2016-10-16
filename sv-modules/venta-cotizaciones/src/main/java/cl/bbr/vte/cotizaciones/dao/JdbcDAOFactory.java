package cl.bbr.vte.cotizaciones.dao;

import java.sql.Connection;
import java.sql.SQLException;

import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.vte.empresas.dao.EmpresasDAO;
import cl.bbr.vte.empresas.dao.JdbcEmpresasDAO;

public class JdbcDAOFactory extends DAOFactory {

	private static ConexionUtil conexionutil = new ConexionUtil();
	
	public static Connection getConexion() throws SQLException 
	{
		return conexionutil.getConexion();
	}

	/**
	 * metodos del DAO 
	 * 
	 */

	public CotizacionesDAO getCotizacionesDAO(){
		return new JdbcCotizacionesDAO();
	}

	public EmpresasDAO getEmpresasDAO(){
		return new JdbcEmpresasDAO();
	}
}
