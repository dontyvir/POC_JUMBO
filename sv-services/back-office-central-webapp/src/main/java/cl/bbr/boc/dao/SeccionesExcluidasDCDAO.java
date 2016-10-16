package cl.bbr.boc.dao;

import java.util.List;

import cl.bbr.jumbocl.shared.exceptions.DAOException;

/**
 * @author JoLazoGu
 * @version 1
 */
public interface SeccionesExcluidasDCDAO {

	/**
	 * @return Lista de Secciones Excluidas
	 * @throws DAOException
	 */
	public List getSeccionesExcluidas() throws DAOException;

	/**
	 * @return Lista de Secciones
	 * @throws DAOException
	 */
	public List getSecciones() throws DAOException;

	/**
	 * @param id_seccion
	 * @throws DAOException
	 */
	public void excluirSeccion(int id_seccion) throws DAOException;

	/**
	 * @param id_seccion
	 * @throws DAOException
	 */
	public void permitirSeccion(int id_seccion) throws DAOException;

}
