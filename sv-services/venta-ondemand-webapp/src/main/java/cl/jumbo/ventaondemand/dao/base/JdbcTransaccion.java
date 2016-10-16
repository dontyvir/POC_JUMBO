package cl.jumbo.ventaondemand.dao.base;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import cl.jumbo.ventaondemand.exceptions.DAOException;
import cl.jumbo.ventaondemand.web.servlet.Logon;

/**
 * Clase que setea la transaccionalidad de la base de datos.
 * 
 */
public class JdbcTransaccion implements JdbcTransaccionIF {

	private static final Logger logger = Logger.getLogger(Logon.class);		
	private Connection conn;	

	//private static ConexionUtil conexionutil = ConexionUtil.getInstance();
	
	/**
	 * @throws SQLException 
	 * Inicia Transacción
	 * @throws  
	 */
	public void begin() throws DAOException, SQLException {
		logger.debug("Begin transaction");
		createConnection();
		conn.setAutoCommit(false);
	}

	/**
	 * Hace Rollback a transaccion
	 */
	public void rollback() throws DAOException {
		try {
			logger.debug("Rollback transaction");
			conn.rollback();
	      } catch (SQLException e) {
	        try {
	        	closeConnection();
	        } catch (DAOException de) {
	          logger.error("Close failed after rollback failed", de);
	        }
	        throw new DAOException("Could not rollback", e);
	      }

	      try {
	    	  closeConnection();
	      } catch (DAOException e) {
	        throw new DAOException("Could not close", e);
	      }
	}

	/**
	 * Finaliza Transaccion
	 */
	public void end() throws DAOException {
	    try {
	    	logger.debug("End transaction");
	        conn.commit();
	      } catch (SQLException e) {
	        try {
	        	closeConnection();
	        } catch (DAOException de) {
	          logger.error("Close failed after commit failed", de);
	        }
	        throw new DAOException("Could not commit", e);
	      }

	      try {
	    	 closeConnection();
	      } catch (DAOException e) {
	        throw new DAOException("Could not close", e);
	      }
	}

	/**
	 * Obtiene la conexion creada en la transaccion. Si no existe, la crea.
	 * @return Connection
	 * @throws DAOException
	 */
	public Connection getConnection() throws DAOException {
		logger.debug("Get a connection");
		try {
			if (conn == null || conn.isClosed())
				createConnection();
		} catch (SQLException e) {
			throw new DAOException("Could not check if connection was closed",e);
		}
		logger.debug("Connection " + conn + " returned");
		return conn;
	}

	/**
	 * Crea una conexion
	 * @throws SQLException 
	 */
	private void createConnection() throws DAOException, SQLException {
		conn = ConexionUtil.getConexion();
		if ( conn == null ) {
			logger.error("No se pudo obtener conexión");
			throw new DAOException("Could not create connection");
		}
		logger.debug("New Connection created");
	}

	/**
	 * Cierra una conexion
	 * @throws DAOException
	 */
	private void closeConnection() throws DAOException {
		try {
			logger.debug("Close connection");
			conn.close();
			conn = null;
		} catch (SQLException e) {
			throw new DAOException("Could not close the connection", e);
		}
	}
}
