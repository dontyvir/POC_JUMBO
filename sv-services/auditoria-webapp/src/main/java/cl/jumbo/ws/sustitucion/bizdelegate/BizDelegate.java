package cl.jumbo.ws.sustitucion.bizdelegate;

import java.util.List;

import cl.jumbo.ws.sustitucion.exceptions.WsException;
import cl.bbr.jumbocl.common.exceptions.*;
import cl.bbr.jumbocl.pedidos.service.PedidosService;

/**
 * Encapsula los procesos de negocio utilizado por la carga y descarga de rondas.
 *
 */
public class BizDelegate {

	private static PedidosService 	pedidoService;
	
	/**
	 * Constructor Business Delegate
	 * 
	 */
	public BizDelegate() {
		if(pedidoService == null) 
			pedidoService = new PedidosService();
	}


	/*
	 * ------------- Rondas ------------------
	 */


    /**
     * Obtiene listado de codigos de barra de los productos del pedido de la ronda
     * @param id_ronda
     * @return Listado de codigos de barra de los productos del pedido de la ronda
     * @throws WsException
     * @throws SystemException
     */
    public List getBarrasAuditoriaSustitucion(List listBarras, long id_ronda)
        throws WsException, SystemException {
        try {
            return pedidoService.getBarrasAuditoriaSustitucion(listBarras, id_ronda);
        } catch (ServiceException ex) { // RemoteException ex
            // Translate the service exception into
            // application exception
            throw new WsException(ex);
        }
    }
    

    public void recepcionAuditoriaSustitucion(List lst_AudSust)
        throws WsException, SystemException {
        try {
            pedidoService.recepcionAuditoriaSustitucion(lst_AudSust);
        } catch (ServiceException e) {
            throw new WsException(e);
        }
    }
    
	
    public void marcaRondaAuditoriaSustitucion(int id_ronda)
        throws WsException, SystemException {
        try {
            pedidoService.marcaRondaAuditoriaSustitucion(id_ronda);
        } catch (ServiceException e) {
            throw new WsException(e);
        }
    }
}
