package cl.bbr.vte.empresas.dao;

import java.sql.Connection;
import java.sql.SQLException;

import cl.bbr.jumbocl.shared.conexion.ConexionUtil;

/**
 * Factory para jdbc. 
 *  
 * @author BBR e-commerce & retail
 *
 */
public class JdbcDAOFactory extends DAOFactory{

	/**
	 * Permite la conexion con la base de datos. 
	 */
	private static ConexionUtil conexionutil = new ConexionUtil();
	
	/**
	 * Obtiene la conexión JDBC con la base de datos.
	 * @return Connection
	 * @throws SQLException 
	 */
	public static Connection getConexion() throws SQLException 
	{
		//	Recommend connection pool implementation/usage
		return conexionutil.getConexion();
	}

	//****************** metodos del DAO *******************//
	public EmpresasDAO getEmpresasDAO(){
		return new JdbcEmpresasDAO();
	}
	
	public SucursalesDAO getSucursalesDAO(){
		return new JdbcSucursalesDAO();
	}
	
	public CompradoresDAO getCompradoresDAO(){
		return new JdbcCompradoresDAO();
	}
	
	public DireccionesDAO getDireccionesDAO(){
		return new JdbcDireccionesDAO();
	}
	
	public DirFacturacionDAO getDirFacturacionDAO(){
		return new JdbcDirFacturacionDAO();
	}
	
}
