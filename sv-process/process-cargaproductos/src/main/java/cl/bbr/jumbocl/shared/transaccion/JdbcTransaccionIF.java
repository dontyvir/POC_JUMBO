package cl.bbr.jumbocl.shared.transaccion;

import java.sql.Connection;

import cl.bbr.jumbocl.shared.exceptions.DAOException;

/**
 * Interfaz que permite obtener la conexion a la base de datos.
 * 
 * @author BBR
 *
 */
public interface JdbcTransaccionIF extends TransaccionIF {

	/**
	 * Obtiene una conexión JDBC
	 * @return Connection
	 * @throws DAOException
	 */
	public Connection getConnection() throws DAOException; 
	
}
