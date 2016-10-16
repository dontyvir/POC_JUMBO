package cl.bbr.botonpago.bizdelegate;

import cl.bbr.botonpago.exception.FuncionalException;
import cl.bbr.botonpago.log.Logging;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.service.PedidosService;

/**
 * <p>
 * BizDelegate para Front Office.
 * </p>
 * <p>
 * Es la capa responsable de disponibilizar los servicios de la capa de negocios
 * y datos del sistema.
 * </p>
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class BizDelegate {

	/**
	 * Instancia para log
	 */
	Logging logger = new Logging(this);

	/**
	 * Instancia para el service de pedidos BO.
	 */
	private static PedidosService bolped_service = null;

	/**
	 * Constructor, se instancian los servicios.
	 */
	public BizDelegate() {
		if (bolped_service == null)
			bolped_service = new PedidosService();
	}

	/* ************************************************************* */

	/**
	 * @param idPedido
	 * @return
	 */
	public BotonPagoDTO botonPagoGetPedido(long idPedido)
			throws FuncionalException {
		try {
			return bolped_service.botonPagoGetPedido(idPedido);
		} catch (Exception e) {
			logger.error("Problema BizDelegate (botonPagoGetPedido)", e);
			throw new FuncionalException(e);
		}
	}

    /**
     * Inserta un registro del resultado del pago con Boton de Pago CAT
     * 
     * @param BotonPagoDTO
     */
    public void botonPagoSave(BotonPagoDTO bp) throws FuncionalException {
		try {
			bolped_service.botonPagoSave(bp);
		} catch (Exception e) {
			logger.error("Problema BizDelegate (botonPagoSave)", e);
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
    
}
