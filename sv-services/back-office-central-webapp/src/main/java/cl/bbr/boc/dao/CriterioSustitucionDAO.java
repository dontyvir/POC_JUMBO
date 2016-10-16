package cl.bbr.boc.dao;

import cl.bbr.jumbocl.shared.exceptions.DAOException;

public interface CriterioSustitucionDAO {
	
	/**
    * @param id_pedido
    * @throws DAOException
    * @return
    */
	public int noSustituir(long id_pedido) throws DAOException;
	
	/**
    * @param id_pedido
    * @param usuario
    * @throws DAOException
    */
	public void registrarTracking(long id_pedido, String usuario) throws DAOException;
	
}
