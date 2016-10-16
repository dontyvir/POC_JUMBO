package cl.bbr.boc.dao;

import java.util.List;

import cl.bbr.jumbocl.shared.exceptions.DAOException;

/**
 * @author JoLazoGu
 * @version 1
 */
public interface CuponesDCDAO {

	/**
	 * @return Lista de Secciones Excluidas
	 * @throws DAOException
	 */
	public List getListaTiposCupones() throws DAOException;

	

}
