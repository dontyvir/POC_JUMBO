package cl.bbr.jumbocl.shared.transaccion;

import java.sql.Connection;
import java.sql.SQLException;

import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * Clase que setea la transaccionalidad de la base de datos.
 * 
 * @author BBR
 * 
 */
public class JdbcTransaccion implements JdbcTransaccionIF {

	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

	/**
	 * Permite la conexi�n con la base de datos.
	 */
	private Connection conn;

	/**
	 * 
	 */
	private static ConexionUtil conexionutil = new ConexionUtil();

	/**
	 * Inicia Transacci�n
	 */
	public void begin() throws DAOException {
		logger.debug("Begin transaction");
		createConnection();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			throw new DAOException("Could not set autocommit", e);
		}
	}

	/**
	 * Hace Rollback a transacci�n
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
	 * Finaliza Transacci�n
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
	 * Obtiene la conexi�n creada en la transacci�n. Si no existe, la crea.
	 * 
	 * @return Connection
	 * @throws DAOException
	 */
	public Connection getConnection() throws DAOException {
		logger.debug("Get a connection");
		try {
			if (conn == null || conn.isClosed())
				createConnection();
		} catch (SQLException e) {
			throw new DAOException("Could not check if connection was closed",
					e);
		}
		logger.debug("Connection " + conn + " returned");
		return conn;
	}

	// ******** M�todos Pirvados *************** //

	/**
	 * Crea una conexi�n
	 */
	private void createConnection() throws DAOException {
		conn = conexionutil.getConexion();
		if (conn == null) {
			logger.error("No se pudo obtener conexi�n");
			throw new DAOException("Could not create connection");
		} else
			logger.debug("New Connection created");
	}

	/**
	 * Cierra una conexi�n
	 * 
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
