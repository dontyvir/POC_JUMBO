package cl.bbr.jumbocl.pedidos.dao;

import java.util.List;

import cl.bbr.jumbocl.common.model.TrxMpDetalleEntity;
import cl.bbr.jumbocl.common.model.TrxMpEncabezadoEntity;
import cl.bbr.jumbocl.pedidos.dto.POSFeedbackProcPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.TrxMpCriteriaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.TrxMedioPagoDAOException;

/**
 * Permite las operaciones en base de datos sobre las Transacciones de medio de pago.
 * 
 * @author BBR
 *
 */
public interface TrxMedioPagoDAO {
	
	/**
	 * Crea Encabezado de transaccion de medio de pagos
	 * 
	 * @param  trxmpenc
	 * @return boolean
	 * @throws TrxMedioPagoDAOException
	 */
	public boolean setCreaTrxMpEnc(TrxMpEncabezadoEntity trxmpenc)throws TrxMedioPagoDAOException;
	
	/**
	 * Crea Detalle de transaccion de medio de pagos
	 * 
	 * @param  prod_traspaso
	 * @return boolean
	 * @throws TrxMedioPagoDAOException
	 */
	public boolean setCreaTrxMpDet(TrxMpDetalleEntity prod_traspaso)throws TrxMedioPagoDAOException;
	
	
	/**
	 * Actualiza Detalle de transaccion de medio de pagos
	 * 
	 * @param  trxdet
	 * @return , devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>.
	 * @throws TrxMedioPagoDAOException
	 */
	public boolean updTrxMpDet(TrxMpDetalleEntity trxdet) throws TrxMedioPagoDAOException;
	
	/**
	 * Obtiene las transacciones relacionadas a un pedido.
	 * 
	 * @param  id_pedido
	 * @return List MonitorTrxMpDTO
	 * @throws TrxMedioPagoDAOException
	 */
	public List getTrxMpByIdPedido(long id_pedido)  throws TrxMedioPagoDAOException;
	
	/**
	 * Actualiza la transaccion de pago cuando ha sido pagada OK
	 * 
	 * @param  fback
	 * @throws TrxMedioPagoDAOException
	 */
	public void doProcesaPagoOK(POSFeedbackProcPagoDTO fback) throws TrxMedioPagoDAOException;
	
	/**
	 * Actualiza transaccion de pago cuando ha sido rechazada
	 * 
	 * @param  fback
	 * @throws TrxMedioPagoDAOException
	 */
	public void doRechazaPago(POSFeedbackProcPagoDTO fback) throws TrxMedioPagoDAOException;
	
	/**
	 * Actualiza transaccion de pago cuando ha sido rechazada
	 * 
	 * @param  fback
	 * @throws TrxMedioPagoDAOException
	 */
	public boolean isAllTrxMPbyPedidoAndState(long id_pedido, int id_estado) throws TrxMedioPagoDAOException;
	
	/**
	 * Lista las trx de medio de pago segun criterios
	 * 
	 * @param  TrxMpCriteriaDTO criterios
	 * @return List
	 * @throws TrxMedioPagoDAOException
	 */
	public List getTrxMpByCriteria(TrxMpCriteriaDTO criterios)   throws TrxMedioPagoDAOException;
	
	/**
	 * Entrega el total de registros de Trx. de medio de pago
	 * 
	 * @param  TrxMpCriteriaDTO criterios
	 * @return List
	 * @throws TrxMedioPagoDAOException
	 */	
	public long getCountTrxMpByCriteria(TrxMpCriteriaDTO criterios)  throws TrxMedioPagoDAOException;
	
	/**
	 * Entrega el listado de estados de las trx. de medio de pago
	 * 
	 * @param  TrxMpCriteriaDTO criterios
	 * @return List
	 * @throws TrxMedioPagoDAOException
	 */
	public List getEstadosTrxMp() throws TrxMedioPagoDAOException;
	
	/**
	 * Actualizar estado de trx de pago segun op
	 * @param id_pedido
	 * @param id_estado
	 * @throws TrxMedioPagoDAOException
	 */
	public void setModEstadoTrxOP(long id_pedido, int id_estado) throws TrxMedioPagoDAOException;
	
}
