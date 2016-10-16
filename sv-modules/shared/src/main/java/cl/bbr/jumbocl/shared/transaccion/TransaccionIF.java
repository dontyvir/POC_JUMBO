package cl.bbr.jumbocl.shared.transaccion;

import java.sql.SQLException;

import cl.bbr.jumbocl.shared.exceptions.DAOException;

/**
 * Interfaz de transacci�n.
 * 
 * @author BBR
 *
 */
public interface TransaccionIF {

    /**
     * Inicia la conexi�n
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
	 * Finaliza la conexi�n. 
	 * 
	 * @throws DAOException
	 */
	public void end() throws DAOException;

	  
}
