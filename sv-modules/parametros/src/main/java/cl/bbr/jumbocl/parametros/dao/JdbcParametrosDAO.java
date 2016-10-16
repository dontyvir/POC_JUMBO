package cl.bbr.jumbocl.parametros.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.bbr.jumbocl.parametros.dao.JdbcDAOFactory;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.parametros.dto.ParametroFoDTO;
import cl.bbr.jumbocl.parametros.exceptions.ParametrosDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Clase que permite consultar los Pedidos que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcParametrosDAO implements ParametrosDAO {
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

	/**
	 * Permite la conexión con la base de datos.
	 */
	Connection 	conn 		= null;

	/**
	 * Permite el manejo de la transaccionalidad, para procesos de multiples operaciones en la base de datos
	 */
	JdbcTransaccion trx		= null;
	

	// ************ Métodos Privados *************** //
	
	/**
	 * Obtiene la conexión
	 * 
	 * @return Connection
	 * @throws SQLException 
	 */
	private Connection getConnection() throws SQLException{
		
		if ( conn == null ){
			conn = JdbcDAOFactory.getConexion();
		}
		
		logger.debug("Conexion usada por el dao: " + conn);
		
		return this.conn;

	}
	
	
	// ************ Métodos Publicos *************** //
	/**
	 * Libera la conexión. Sólo si no es una conexión única, en cuyo caso
	 * no la cierra.
	 * 
	 * 
	 */
	private void releaseConnection(){
		if ( trx == null ){
            try {
            	if (conn != null){
            		conn.close();
            		conn = null;
            	}
            } catch (SQLException e) {
            	logger.error(e.getMessage(), e);

            }
		}
			
	}
	// ******************************************** //	
	
	/**
	 * Setea una transacción al dao y le asigna su conexión
	 * 
	 * @param  trx JdbcTransaccion 
	 * 
	 * @throws PedidosDAOException
	 */
	public void setTrx(JdbcTransaccion trx)
			throws ParametrosDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new ParametrosDAOException(e);
		}
	}
	
	/**
	 * Constructor
	 *
	 */
	public JdbcParametrosDAO(){
		
	}
	//	 ********************************************************** //
	
	
	/**
	 * Obtiene Listado de parametros
	 * 
	 * @return List ParametroDTO
	 * @throws ParametrosDAOException 
	 * 
	 */
	public List getParametros() throws ParametrosDAOException {
		List result = new ArrayList();
		PreparedStatement stm =null;
		ResultSet rs = null;
		
		try {
			conn = this.getConnection();
			String Query = "SELECT ID_PARAMETRO, NOMBRE, VALOR "
                         + "FROM BO_PARAMETROS "
                         + "ORDER BY ID_PARAMETRO ASC";
			stm = conn.prepareStatement(Query + " WITH UR");

			logger.debug("SQL query: " + stm.toString());

			rs = stm.executeQuery();
			while (rs.next()) {
				ParametroDTO param = new ParametroDTO();
				param.setIdParametro(rs.getInt("ID_PARAMETRO"));
				param.setNombre(rs.getString("NOMBRE"));
				param.setValor(rs.getString("VALOR"));
				result.add(param);
			}

		} catch (SQLException e) {
			throw new ParametrosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getParametros - Problema SQL (close)", e);
			}
		}
		logger.debug("Se listaron:"+result.size());
		return result;
	}

	
	/**
	 * Obtiene un Parametro por su Nombre
	 * 
	 * @return ParametroDTO
	 * @throws ParametrosDAOException 
	 * 
	 */
	public ParametroDTO getParametroByName(String Name) throws ParametrosDAOException {
	    ParametroDTO param = null;
		PreparedStatement stm =null;
		ResultSet rs = null;
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			String Query = "SELECT ID_PARAMETRO, NOMBRE, VALOR FROM BODBA.BO_PARAMETROS WHERE NOMBRE = '" + Name + "' ";
			stm = conn.prepareStatement(Query + " WITH UR");

			//logger.debug("SQL query: " + stm.toString());

			rs = stm.executeQuery();
			if(rs.next()){			
				param = new ParametroDTO();
				param.setIdParametro(rs.getInt("ID_PARAMETRO"));
				param.setNombre(rs.getString("NOMBRE"));
				param.setValor(rs.getString("VALOR"));
			}

		} catch (SQLException e) {
			logger.error(e);
			throw new ParametrosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getParametroByName - Problema SQL (close)", e);
			}
		}
		return param;
	}
	
	/**
	 * Obtiene un Parametro por su Nombre
	 * 
	 * @return ParametroDTO
	 * @throws ParametrosDAOException 
	 * 
	 */
	public Map getParametroByNameIn(String NameIn) throws ParametrosDAOException {
	   
		PreparedStatement stm =null;
		ResultSet rs = null;
		Map parametros = new HashMap();
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			String Query = "SELECT ID_PARAMETRO, NOMBRE, VALOR FROM BODBA.BO_PARAMETROS WHERE NOMBRE in ('" + NameIn + "') ";
			stm = conn.prepareStatement(Query + " WITH UR");

			//logger.debug("SQL query: " + stm.toString());

			rs = stm.executeQuery();
			while(rs.next()){			
				ParametroDTO param = new ParametroDTO();
				param.setIdParametro(rs.getInt("ID_PARAMETRO"));
				param.setNombre(rs.getString("NOMBRE"));
				param.setValor(rs.getString("VALOR"));
				parametros.put(rs.getString("NOMBRE"),param);
			}

		} catch (SQLException e) {
			logger.error(e);
			throw new ParametrosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getParametroByName - Problema SQL (close)", e);
			}
		}
		return parametros;
	}

	
	/**
	 * Obtiene un Parametro por su ID
	 * 
	 * @return ParametroDTO
	 * @throws ParametrosDAOException 
	 * 
	 */
	public ParametroDTO getParametroById(int Id) throws ParametrosDAOException {
	    ParametroDTO param = null;
		PreparedStatement stm =null;
		ResultSet rs = null;
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			String Query = "SELECT ID_PARAMETRO, NOMBRE, VALOR "
                         + "FROM BO_PARAMETROS "
                         + "WHERE ID_PARAMETRO = " + Id;
			stm = conn.prepareStatement(Query + " WITH UR");

			logger.debug("SQL query: " + stm.toString());

			rs = stm.executeQuery(Query);
			rs.next();
			
			param = new ParametroDTO();
			param.setIdParametro(rs.getInt("ID_PARAMETRO"));
			param.setNombre(rs.getString("NOMBRE"));
			param.setValor(rs.getString("VALOR"));
						
		} catch (SQLException e) {
			throw new ParametrosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		return param;
	}
	
	public ParametroFoDTO getParametroFoByKey(String key) throws ParametrosDAOException {
		ParametroFoDTO param = null;
		PreparedStatement stm =null;
		ResultSet rs = null;
		Connection conn = null;
		
		try {
			conn = JdbcDAOFactory.getConexion();
			String Query = "SELECT PAR_ID, PAR_NOMBRE, PAR_DESCRIPCION, PAR_VALOR, PAR_LLAVE FROM FO_PARAMETROS WHERE PAR_LLAVE = '" + key + "' ";
			stm = conn.prepareStatement(Query + " WITH UR");
			logger.debug("SQL query: " + stm.toString());
			rs = stm.executeQuery();
			
			if(rs.next()){
				param = new ParametroFoDTO();
				param.setId(rs.getInt("PAR_ID"));
				param.setNombre(rs.getString("PAR_NOMBRE"));
				param.setDescripcion(rs.getString("PAR_DESCRIPCION"));
				param.setValor(rs.getString("PAR_VALOR"));
				param.setLlave(rs.getString("PAR_LLAVE"));
			}
			
		} catch (SQLException e) {
			throw new ParametrosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
		    try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				if (conn != null && !conn.isClosed())
				    conn.close();
				
			} catch (SQLException e) {
				logger.error("getParametroFoByKey - Problema SQL (close)", e);
			}
		}
		return param;
	}
	
	public ParametroFoDTO getParametroByKey(String key) throws ParametrosDAOException {
	    ParametroFoDTO param = null;
		PreparedStatement stm =null;
		ResultSet rs = null;
		Connection conn = null;
		
		try {
			conn = JdbcDAOFactory.getConexion();
			String Query = "SELECT PAR_ID, PAR_NOMBRE, PAR_DESCRIPCION,PAR_VALOR,PAR_LLAVE "
                         + "FROM FO_PARAMETROS "
                         + "WHERE PAR_LLAVE = '" + key + "' ";
			stm = conn.prepareStatement(Query + " WITH UR");
			logger.debug("SQL query: " + stm.toString());
			rs = stm.executeQuery();
			
			if(rs.next()){
				param = new ParametroFoDTO();
				param.setId(rs.getInt("PAR_ID"));
				param.setNombre(rs.getString("PAR_NOMBRE"));
				param.setDescripcion(rs.getString("PAR_DESCRIPCION"));
				param.setValor(rs.getString("PAR_VALOR"));
				param.setLlave(rs.getString("PAR_LLAVE"));
			}
			
		} catch (SQLException e) {
			throw new ParametrosDAOException(e);
		} finally {
		    try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				if (conn != null && !conn.isClosed())
				    conn.close();
				
			} catch (SQLException e) {
				logger.error("getParametroByKey - Problema SQL (close)", e);
			}
		}
		return param;
	}
	
	public ParametroFoDTO getParametroByID(int id) throws ParametrosDAOException {
	    ParametroFoDTO param = null;
		PreparedStatement stm =null;
		ResultSet rs = null;
		Connection conn = null;
		
		try {
			conn = JdbcDAOFactory.getConexion();
			String Query = "SELECT PAR_ID, PAR_NOMBRE, PAR_DESCRIPCION,PAR_VALOR,PAR_LLAVE "
                         + "FROM FO_PARAMETROS "
                         + "WHERE PAR_ID = ?";
			stm = conn.prepareStatement(Query + " WITH UR");
			stm.setInt(1,id);
			
			logger.debug("SQL query: " + stm.toString());
			rs = stm.executeQuery();
			
			if(rs.next()){
				param = new ParametroFoDTO();
				param.setId(rs.getInt("PAR_ID"));
				param.setNombre(rs.getString("PAR_NOMBRE"));
				param.setDescripcion(rs.getString("PAR_DESCRIPCION"));
				param.setValor(rs.getString("PAR_VALOR"));
				param.setLlave(rs.getString("PAR_LLAVE"));
}
		} catch (SQLException e) {
			throw new ParametrosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
		    try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				if (conn != null && !conn.isClosed())
				    conn.close();
				
			} catch (SQLException e) {
				logger.error("getParametroByID - Problema SQL (close)", e);
			}
		}
		return param;
	}
	public boolean actualizaParametroByName(String name, String valor) throws ParametrosDAOException {	    
		PreparedStatement stm =null;
		boolean result = false;
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			String sql = "UPDATE BO_PARAMETROS SET VALOR = ? "                          
                         	+ "WHERE NOMBRE = ? ";
			stm = conn.prepareStatement(sql + " WITH UR");
			
			logger.debug("SQL query: " + stm.toString());

			stm = conn.prepareStatement(sql);
			stm.setString(1, valor);
			stm.setString(2, name);
			int i = stm.executeUpdate();
			if(i>0)
				result = true;
				
		} catch (SQLException e) {
			throw new ParametrosDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {			
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getParametroByName - Problema SQL (close)", e);
			}
		}
		return result;
	}

	
}
