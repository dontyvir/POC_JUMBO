package cl.jumbo.ventaondemand.dao.base;

import java.sql.SQLException;

import cl.jumbo.ventaondemand.exceptions.DAOException;

/**
 * Interfaz de transaccion.
 *
 */
public interface TransaccionIF {

    /**
     * Inicia la conexion
     * 
	 * @throws DAOException
	 */
	public void begin() throws DAOException, SQLException;

	/**
	 * Anula y revierte las operaciones en base de datos.
	 * 
	 * @throws DAOException
	 */
	public void rollback() throws DAOException;

	/**
	 * Finaliza la conexion. 
	 * 
	 * @throws DAOException
	 */
	public void end() throws DAOException;
}
