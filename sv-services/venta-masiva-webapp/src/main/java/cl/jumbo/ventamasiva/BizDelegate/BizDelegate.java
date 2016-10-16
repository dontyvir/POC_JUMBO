package cl.jumbo.ventamasiva.BizDelegate;

import java.util.List;
import java.util.Map;
import java.text.ParseException;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.clientes.service.ClientesService;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.parametros.service.ParametrosService;
import cl.bbr.jumbocl.pedidos.ctrl.PedidosCtrl;
import cl.bbr.jumbocl.pedidos.dto.BinDTO;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.CreaRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PagoVentaMasivaDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosException;
import cl.bbr.log.Logging;
import cl.jumbo.ventamasiva.exceptions.FuncionalException;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.productos.service.ProductosService;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcInsPedidoDTO;

public class BizDelegate {

	private Logging logger = new Logging(this);

	/**
	 * Instancia para el service de pedidos BO.
	 */
	private static cl.bbr.jumbocl.pedidos.service.PedidosService bolped_service = null;
	private static ParametrosService parametroService = null;
	private static ClientesService cli_service = null;
	private static cl.bbr.jumbocl.clientes.service.ClientesService clientesService = null;
	private static ClientesService clienteService = null;
	private static ProductosService productoService = null;
	
	public BizDelegate() {
		if (parametroService == null) 
			parametroService = new ParametrosService();
		if (bolped_service == null) 
			bolped_service = new cl.bbr.jumbocl.pedidos.service.PedidosService();
		if (cli_service == null) 
			cli_service = new ClientesService();
		if (clientesService == null)
			clientesService = new cl.bbr.jumbocl.clientes.service.ClientesService();
		if (clienteService == null)
			clienteService = new ClientesService();		
		if (productoService == null)
			productoService = new ProductosService();
		
	}

	public Map getParametroByNameIn(String name) throws Exception {
		try {
			return parametroService.getParametroByNameIn(name);
		} catch (Exception ex) {
			logger.error("Problema BizDelegate (doInsPedido)", ex);
			throw new Exception(ex);
		}
	}

	/**
	 * 
	 * Retorna datos del pedido indicado
	 * 
	 * @param pedido_id Identificador único del pedido
	 * @return DTO con datos del pedido
	 * @throws FuncionalException
	 */
	public PedidoDTO getPedidoById(long pedido_id) throws FuncionalException {
		try {
			return bolped_service.getPedido(pedido_id);
		} catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
			logger.error("Problema BizDelegate (getPedidoById)", e);
			throw new FuncionalException(e);
		} catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
			logger.error("Problema BizDelegate (getPedidoById)", e);
			throw new FuncionalException(e);
		}
	}

	/**
     * Recupera los productos del pedido
     * 
     * @param pedido_id Identificador único del pedido
     * @return DTO con datos del pedido
     * @throws FuncionalException
     */
    public List getProductosPedido(long pedido_id) throws FuncionalException {
        try {
            return bolped_service.getProductosPedido(pedido_id);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (doInsPedido)", e);
            throw new FuncionalException(e);
        }
    }

	public int webpayGetEstado(int idPedido) throws FuncionalException {
		try {
			return bolped_service.webpayGetEstado(idPedido);
		} catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
			logger.error("Problema BizDelegate (webpayGetEstado)", e);
			throw new FuncionalException(e);
		} catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
			logger.error("Problema BizDelegate (webpayGetEstado)", e);
			throw new FuncionalException(e);
		}
	}

	public void purgaPedidoPreIngresado(PedidoDTO pedido, long idCliente) throws FuncionalException {
		try {
			bolped_service.purgaPedidoPreIngresado(pedido, idCliente);
		} catch (Exception e) {
			logger.error("Problema BizDelegate (purgaPedidoPreIngresado)", e);
			throw new FuncionalException(e);
		}
	}

	public WebpayDTO webpayGetPedido(long idPedido) throws FuncionalException {
		try {
			return bolped_service.webpayGetPedido(idPedido);
		} catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
			logger.error("Problema BizDelegate (webpayGetPedido)", e);
			throw new FuncionalException(e);
		} catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
			logger.error("Problema BizDelegate (webpayGetPedido)", e);
			throw new FuncionalException(e);
		}
	}
	
	public BotonPagoDTO botonPagoGetByPedido(long idPedido)
			throws FuncionalException {
		try {
			return bolped_service.botonPagoGetByPedido(idPedido);
		} catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
			logger.error("Problema BizDelegate (botonPagoGetByPedido)", e);
			throw new FuncionalException(e);
		} catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
			logger.error("Problema BizDelegate (botonPagoGetByPedido)", e);
			throw new FuncionalException(e);
		}
	}

	/**
	 * Actualiza registro de Boton de pago CAT
	 */
	public boolean updateNotificacionBotonPago(BotonPagoDTO bp) throws FuncionalException {
		try {
			return bolped_service.updateNotificacionBotonPago(bp);
		} catch (Exception e) {
			logger.error("Problema BizDelegate (updateNotificacionBotonPago)", e);
			throw new FuncionalException(e);
		}
	}

	public ClienteDTO clienteGetById(long cliente_id) throws FuncionalException {
		try {
			return cli_service.clienteGetByIdFO(cliente_id);
		} catch (Exception e) {
			logger.error("Problemas con controles de clientes", e);
			throw new FuncionalException(e);
		}
	}

	public List getDescuentosAplicados(long pedido_id)throws FuncionalException {
		try {
			return bolped_service.getDescuentosAplicados(pedido_id);
		} catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
			logger.error("Problema BizDelegate (doInsPedido)", e);
			throw new FuncionalException(e);
		}
	}

	/**
	 * 
	 * Agrega registro de mail para ser enviado
	 * 
	 * @param mail DTO con información del mail
	 * @throws SystemException
	 */
	public void addMail(MailDTO mail) throws FuncionalException {
		try {
			clientesService.addMail(mail);
		} catch (Exception ex) {
			logger.error("Problema BizDelegate (addMail)", ex);
			throw new FuncionalException(ex);
		}
	}

	public LocalDTO getLocalRetiro(long idLocal) throws FuncionalException {
		try {
			return bolped_service.getLocalRetiro(idLocal);
		} catch (Exception ex) {
			logger.error("Problema BizDelegate (getLocalRetiro)", ex);
			throw new FuncionalException(ex);
		}
	}

	public PedidoDTO getValidaCuponYPromocionPorIdPedido(long idPedido)	throws FuncionalException {
		PedidoDTO pedido = null;
		try {
			pedido = bolped_service.getValidaCuponYPromocionPorIdPedido(idPedido);
		} catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
			logger.error("Problema BizDelegate (getValidaCuponYPromocionPorIdPedido)", e);
			throw new FuncionalException(e);
		}
		return pedido;
	}

	public void addLogPedido(LogPedidoDTO log) throws ServiceException, SystemException {
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			try {
				pedidos.addLogPedido(log);
			} catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
				logger.debug("Problemas con controles de pedidos");
			}
		} catch (cl.bbr.jumbocl.pedidos.exceptions.PedidosException ex) {
			logger.debug("Problemas con controles de pedidos");
			throw new cl.bbr.jumbocl.common.exceptions.ServiceException(ex.getMessage());
		}
	}

	public List getProductosSolicitadosById(long id_pedido) throws FuncionalException {
		try {
			return bolped_service.getProductosSolicitadosById(id_pedido);
		} catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
			logger.error("Problema BizDelegate (getProductosSolicitadosById)", e);
			throw new FuncionalException(e);
		} catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
			logger.error("Problema BizDelegate (getProductosSolicitadosById)", e);
			throw new FuncionalException(e);
		}
	}
	
	public boolean setModEstadoPedido(long id_pedido, long id_estado) throws FuncionalException, SystemException {		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.setModEstadoPedido(id_pedido, id_estado);
		} catch (PedidosException e) {
			logger.error("Problema BizDelegate (setModEstadoPedido), ", e);			
			throw new FuncionalException(e.getMessage());
		}
	}
	
	public PagoVentaMasivaDTO getPagoVentaMasivaByToken(String token) throws ServiceException, SystemException, FuncionalException{
		try {
			return bolped_service.getPagoVentaMasivaByToken(token);
		} catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
			logger.error("Problema BizDelegate (getProductosSolicitadosById), ", e);
			throw new FuncionalException(e);
		} catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
			logger.error("Problema BizDelegate (PagoVentaMasivaDTO), ", e);
			throw new FuncionalException(e);
		}
	}	
	public PagoVentaMasivaDTO getPagoVentaMasivaByIdPedido(long idPedido) throws ServiceException, SystemException, FuncionalException{
		try {
			return bolped_service.getPagoVentaMasivaByIdPedido(idPedido);
		} catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
			logger.error("Problema BizDelegate (getPagoVentaMasivaByIdPedido), ", e);
			throw new FuncionalException(e);
		} catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
			logger.error("Problema BizDelegate (getPagoVentaMasivaByIdPedido), ", e);
			throw new FuncionalException(e);
		}
	}	
	public void addPagoVentaMasiva(PagoVentaMasivaDTO dto) throws Exception {		
		try {
			bolped_service.addPagoVentaMasiva(dto);
		} catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
			logger.error("Problema BizDelegate (addPagoVentaMasiva), ", e);
			throw new FuncionalException(e);
		}
	}
	public void actualizaPagoVentaMasivaByOP(PagoVentaMasivaDTO pago)throws FuncionalException {
		try {
			bolped_service.actualizaPagoVentaMasivaByOP(pago);
		} catch (Exception e) {
			logger.error("Problema (actualizaPagoVentaMasivaByOP), ", e);
			throw new FuncionalException(e);
		}
	}
	
	public String creaClienteDesdeInvitado(ClienteDTO cliente, DireccionesDTO direccion, String forma_despacho) throws ServiceException {
        try {
            return clienteService.creaClienteDesdeInvitadoFO(cliente, direccion, forma_despacho);
        } catch (Exception ex) {
            logger.error("Problemas creaClienteDesdeInvitado, ", ex);
            throw new ServiceException(ex);
        }
    }
	public ProductoDTO getProductoPrecioFO(String idProd, long idLocal) throws ServiceException, SystemException {
		ProductoDTO result = new ProductoDTO();	
		try{
			result = productoService.getProductoPrecioFO(idProd,idLocal);		
		} catch (Exception ex) {
	        logger.error("Problema getProductoPrecioFO, ", ex);
	        throw new SystemException(ex);
	    }
		return result;
	}
	public JornadaDTO getDatosJornada(String hora, String fecha, long idLocal) throws ServiceException, SystemException, ParseException {
		try {
			return bolped_service.getDatosJornada( hora,  fecha, idLocal);
		} catch (PedidosException ex) {
			 logger.error("Problemas getDatosJornada, ", ex);
	            throw new ServiceException(ex);
		}
	}
	public long doInsPedidoNew(ProcInsPedidoDTO pedido) throws ServiceException   {
        try {
            return bolped_service.doInsPedido(pedido, null, null);
        } catch (Exception ex) { // RemoteException ex
        	 logger.error("Problemas doInsPedidoNew, ", ex);
	            throw new ServiceException(ex);			
		}
    }
	public void carroComprasInsertProducto(List listcarro, long cliente, String idSession) throws ServiceException {    	
        try {
        	clientesService.carroComprasInsertProductoFO(listcarro, cliente, idSession);
        } catch (Exception ex) { // RemoteException ex
       	 logger.error("Problemas carroComprasInsertProducto, ", ex);
	            throw new ServiceException(ex);			
		}
    }
	public void guardaCriteriosMiCarro(Long idCliente, List criteriosProductos, String idSession) throws ServiceException {
        try {
        	clientesService.guardaCriteriosMiCarroFO(idCliente, criteriosProductos, idSession);
        } catch (Exception ex) { // RemoteException ex
       	 logger.error("Problemas guardaCriteriosMiCarro, ", ex);
	            throw new ServiceException(ex);			
		}
    }
	public List carroComprasGetProductosVentaMasiva(long cliente_id, String local, String idSession) throws SystemException {
		try {
			return cli_service.carroComprasGetVentaMasiva(cliente_id, local, idSession);
		} catch (Exception ex) {
			logger.error("Problemas carroComprasGetProductosVentaMasiva, ", ex);
			throw new SystemException(ex);
		}
	}
	public void addDetallePickingVentaMasiva(List lista, CreaRondaDTO dtoRonda, BinDTO dtoBin, long idPedido, long pedidoValidado) throws SystemException {
		try {
			bolped_service.addDetallePickingVentaMasiva(lista, dtoRonda, dtoBin, idPedido, pedidoValidado);
		} catch (Exception ex) {
			logger.error("Problemas addDetallePickingVentaMasiva, ", ex);
			throw new SystemException(ex);
		}
	}
	public List getProductosSolicitadosVMById(long id_pedido) throws FuncionalException {
		try {
			return bolped_service.getProductosSolicitadosVMById(id_pedido);
		} catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
			logger.error("Problema BizDelegate (getProductosSolicitadosVMById), ", e);
			throw new FuncionalException(e);
		} catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
			logger.error("Problema BizDelegate (getProductosSolicitadosVMById), ", e);
			throw new FuncionalException(e);
		}
	}

	public JornadaDTO getDatosJornadaDespachoSegunComuna(String hora, String fecha, long comuna_id, long idLocal) throws ServiceException, SystemException, ParseException {
		try {
			return bolped_service.getDatosJornadaDespachoSegunComuna(hora,  fecha, comuna_id, idLocal);
		} catch (PedidosException ex) {
			 logger.error("Problemas getDatosJornadaDespachoSegunComuna", ex);
	            throw new ServiceException(ex);
		}
	}

	public int getPoligonoVentaMasivaPorComuna(long comuna_id) throws ServiceException, SystemException, ParseException {
		try {
			return bolped_service.getPoligonoVentaMasivaPorComuna(comuna_id);
		} catch (PedidosException ex) {
			 logger.error("Problemas getPoligonoVentaMasivaPorComuna", ex);
	            throw new ServiceException(ex);
		}
	}
}
