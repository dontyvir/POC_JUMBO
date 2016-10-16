package cl.jumbo.ventaondemand.dao.base;

import java.sql.Connection;

import cl.jumbo.ventaondemand.exceptions.DAOException;

/**
 * Interfaz que permite obtener la conexion a la base de datos.
 *
 */
public interface JdbcTransaccionIF extends TransaccionIF {

	public Connection getConnection() throws DAOException;
}
