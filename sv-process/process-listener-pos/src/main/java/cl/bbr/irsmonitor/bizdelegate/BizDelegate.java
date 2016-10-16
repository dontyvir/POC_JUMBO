package cl.bbr.irsmonitor.bizdelegate;

import java.util.List;

import cl.bbr.irsmonitor.exceptions.IrsMonException;
import cl.bbr.jumbocl.common.exceptions.*;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.POSFeedbackProcPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.TrxMpDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.jumbocl.pedidos.service.PedidosService;


public class BizDelegate {
	
	private static PedidosService 		pedidosBolService;
	
	public BizDelegate() {
			
		if(pedidosBolService == null) 
			pedidosBolService = new PedidosService();
		
	}
	

	// -----------------------Pedidos-----------------------

	/**
	 * Obtiene el Pedido a partir del número de OP 
	 * @params id_pedido
	 * @returns PedidoDTO
	 * */
	public PedidoDTO getPedidosById(long id_pedido)
		throws IrsMonException, SystemException {
		
		try{
			return pedidosBolService.getPedido(id_pedido);
		}catch (ServiceException ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new IrsMonException(ex);
		}
	}
	
	
	/**
	 * Obtiene la lista de los productos del pedido
	 * @param id_pedido número de pedido
	 * @return List
	 * @throws IrsMonException
	 * */
	public List getProductosPedidosById(long id_pedido) throws IrsMonException {
		try{
			return pedidosBolService.getProductosPedido(id_pedido);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new IrsMonException(ex);
		}
	}
	
	/**
	 * Agrega registro al log del pedido
	 * @param log
	 * @throws IrsMonException
	 */
	public void addLogPedido(LogPedidoDTO log)throws IrsMonException {
		try{
			pedidosBolService.addLogPedido(log);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new IrsMonException(ex);
		}
	}
	
	
	/*
	 * Obtiene la lista del log de pedidos
	 * */
	public List getLogsPedidoById(long id_pedido) throws IrsMonException {
		try{
			return pedidosBolService.getLogDespacho(id_pedido);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new IrsMonException(ex);
		}
	}
	
	/**
	 * Modifica el estado de un pedido
	 * @param id_pedido
	 * @param id_estado
	 * @return boolean true|false
	 * @throws IrsMonException
	 */
	public boolean setModEstadoPedido(long id_pedido, long id_estado) throws IrsMonException {
		try{
			return pedidosBolService.setModEstadoPedido(id_pedido, id_estado);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new IrsMonException(ex);
		}
	}
	
	/**
	 * Obtiene los datos de facturación de un pedido a partir de su número de OP
	 * @param id_pedido
	 * @return List
	 * @throws IrsMonException
	 */
	public List getFacturasByIdPedido(long id_pedido) throws IrsMonException {
		try{
			return pedidosBolService.getFacturasByIdPedido(id_pedido);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new IrsMonException(ex);
		}
	}	
	
	
	/**
	 * Obtiene el listado de productos que han sido pickeados
	 * @param id_pedido
	 * @return
	 * @throws IrsMonException
	 */
	public List getDetPickingByIdPedido(long id_pedido) throws IrsMonException {
		try{
			return pedidosBolService.getDetPickingByIdPedido(id_pedido);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new IrsMonException(ex);
		}
	}	
	
	
	
	/**
	 * Procesa el feedback de la transacción POS
	 * @param POSFeedbackProcPagoDTO fback
	 * @throws IrsMonException
	 * @throws SystemException 
	 */
	public void doProcesaFeedbackPOS(POSFeedbackProcPagoDTO fback)
		throws IrsMonException, SystemException {
		
		try{
			pedidosBolService.doProcesaFeedbackPOS(fback);
		}catch (ServiceException ex) { //RemoteException ex
			// Translate the service exception into
			// application exception
			throw new IrsMonException(ex);
		}
		
	}
	
	
	/**
	 * Entrega los datos de un encabezado de trx de pago
	 * @param id_trxmp
	 * @throws ServiceException
	 */
	public TrxMpDTO getTrxPagoById( long id_trxmp )
		throws IrsMonException, SystemException {
		
		try {
			 return pedidosBolService.getTrxPagoById(id_trxmp);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new IrsMonException(e);
		}
	}
	
	
	/**
	 * Entrega el listado de detalle de una trx de pago
	 * @param id_trxmp
	 * @throws ServiceException
	 */
	public List getTrxPagoDetalleByIdTrxMp( long id_trxmp )
		throws IrsMonException, SystemException {
		
		try {
			 return pedidosBolService.getTrxPagoDetalleByIdTrxMp(id_trxmp);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new IrsMonException(e);
		}
	}
	
	
	/**
	 * Entrega el detalle del Pago con WebPay
	 * @param id_pedido
	 * @throws ServiceException
	 */
	public WebpayDTO webpayGetPedido( long id_pedido )
		throws IrsMonException, SystemException {
		
		try {
			 return pedidosBolService.webpayGetPedido(id_pedido);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new IrsMonException(e);
		}
	}
	
	/**
	 * Entrega el detalle del Pago con Boton de Pago CAT
	 * @param id_pedido
	 * @throws ServiceException
	 */
	public BotonPagoDTO botonPagoGetPedido( long id_pedido )
		throws IrsMonException, SystemException {
		
		try {
			 return pedidosBolService.botonPagoGetPedido(id_pedido);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new IrsMonException(e);
		}
	}
	
	/**
	 * Entrega el detalle del Pago con Boton de Pago CAT
	 * @param id_pedido
	 * @throws ServiceException
	 */
	public BotonPagoDTO botonPagoGetByPedido( long id_pedido )
		throws IrsMonException, SystemException {
		
		try {
			 return pedidosBolService.botonPagoGetByPedido(id_pedido);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new IrsMonException(e);
		}
	}
	
	
}
