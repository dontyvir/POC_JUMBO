package cl.bbr.boc.dao;

import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

public interface CambiaEstadoValidadoDAO {

	/**
	 * @param id_pedido
	 * @throws DAOException
	 * @return
	 */
	public int getCountDetallePicking(long id_pedido) throws DAOException;

	/**
	 * 
	 * @param id_pedido
	 * @param usuario
	 * @param descripcion
	 * @throws DAOException
	 */
	public void registrarTracking(long id_pedido, String usuario,String descripcion) throws DAOException;

	/**
	 * 
	 * @param idPedido
	 * @throws DAOException
	 */
	public void borrarDetallePicking(long idPedido) throws DAOException;
	/**
	 * 
	 * @param idPedido
	 * @return
	 * @throws DAOException
	 */
	public boolean modificarCantidadesDP(long idPedido) throws DAOException;
	/**
	 * 
	 * @param idPedido
	 * @return
	 * @throws DAOException
	 */
	public boolean cambioEstadoOP(long idPedido)throws DAOException;
	/**
	 * 
	 * @param trx1
	 */
	public void setTrx(JdbcTransaccion trx1)throws DAOException;
	/**
	 * 
	 * @param idPedido
	 * @return
	 * @throws DAOException
	 */
	public int getCountTrxMpByIdPedido(long idPedido)throws DAOException;
	/**
	 * 
	 * @param idPedido
	 * @throws DAOException
	 */
	public void DelTrxMP(long idPedido)throws DAOException;;

}
