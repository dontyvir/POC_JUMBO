package cl.bbr.jumbocl.pedidos.dao;

import java.util.List;

import cl.bbr.jumbocl.pedidos.dto.DespachoCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.DespachoDTO;
import cl.bbr.jumbocl.pedidos.exceptions.DespachosDAOException;

/**
 * Permite las operaciones en base de datos sobre los Despachos.
 * @author BBR
 *
 */
public interface DespachosDAO {
	
	/**
	 * Agrega registro al log del despacho.
	 * 
	 * @param  id_pedido
	 * @param  login
	 * @param  log
	 * @throws DespachosDAOException
	 */
	public void addLogDespacho(long id_pedido, String login, String log)
	 throws DespachosDAOException;
	
	/**
	 * Obtiene listado del log de un despacho.
	 * 
	 * @param  id_pedido
	 * @return List LogSimpleDTO
	 * @throws DespachosDAOException
	 */
	public List getLogDespacho(long id_pedido)
	 throws DespachosDAOException;
	
	/**
	 * Obtiene listado de despachos por criterio
	 * 
	 * @param  criterio
	 * @return List MonitorDespachosDTO
	 * @throws DespachosDAOException
	 */
	public List getDespachosByCriteria(DespachoCriteriaDTO criterio)
	 throws DespachosDAOException;
	
	/**
	 * Retorna el número de registros de una query con criterio
	 * 
	 * @param  criterio
	 * @return long
	 * @throws DespachosDAOException
	 */
	public long getCountDespachosByCriteria( DespachoCriteriaDTO criterio )
	 throws DespachosDAOException;
		
	/**
	 * Obtiene información del despacho
	 * 
	 * @param  id_pedido
	 * @return DespachoDTO
	 * @throws DespachosDAOException
	 */
	public DespachoDTO getDespachoById(long id_pedido)
	 throws DespachosDAOException;
	
	/**
	 * Cambia el estado a un pedido en su fase de despacho
	 * 
	 * @param  id_pedido
	 * @param  id_estado
	 * @throws DespachosDAOException
	 */
	public void doCambiaEstadoDespacho( long id_pedido, long id_estado )
	 throws DespachosDAOException;
	
	
}
