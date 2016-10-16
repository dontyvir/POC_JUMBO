package cl.cencosud.jumbo.bizdelegate;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.clientes.service.ClientesService;
import cl.bbr.jumbocl.parametros.dto.ParametroFoDTO;
import cl.bbr.jumbocl.parametros.service.ParametrosService;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.pedidos.service.PedidosService;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;
import cl.bbr.promo.lib.dto.doRecalculoCriterio;
import cl.bbr.promo.lib.dto.doRecalculoResultado;
import cl.bbr.promo.lib.service.PromocionesService;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.Constant.ConstantDeliveryWindow;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.util.Util;

/**
 * @author frramosg
 * 
 */
public class BizDelegateDeliveryWindow {
	private PedidosService pedidosService = null;
	private ClientesService clientesService = null;
	private PromocionesService promocionesService = null;
	private ParametrosService parametrosService = null;

	public BizDelegateDeliveryWindow() {
		if (pedidosService == null)
			pedidosService = new PedidosService();
		if (clientesService == null)
			clientesService = new ClientesService();
		if (promocionesService == null)
			promocionesService = new PromocionesService();
		if (parametrosService == null)
			parametrosService = new ParametrosService();
	}

	public Date fechaActualBD() throws GrabilityException {
		try {
			return pedidosService.fechaActualBD();
		} catch (Exception e) {
			if(Util.isSqlException(e)){			
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantDeliveryWindow.SC_ERROR_OBTENER_FECHA_HORA_SERVER, ConstantDeliveryWindow.MSG_ERROR_OBTENER_FECHA_HORA_SERVER,e);
			}		
		}
	}

	public DireccionesDTO clienteGetDireccion(long idDireccion)	throws GrabilityException {
		try {
			return clientesService.clienteGetDireccionFO(idDireccion);
		} catch (Exception e) {
			if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantDeliveryWindow.SC_ERROR_OBTENER_DIRECCION_CLIENTE, ConstantDeliveryWindow.MSG_ERROR_OBTENER_DIRECCION_CLIENTE,e);
			}		
		}
	}

	public ZonaDTO getZonaDespachoById(long id_zona) throws GrabilityException {
		try {
			return pedidosService.getZonaDespacho(id_zona);
		} catch (Exception e) {
			if(Util.isSqlException(e)){				
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantDeliveryWindow.SC_ERROR_OBTENER_ZONA_DESPACHO, ConstantDeliveryWindow.MSG_ERROR_OBTENER_ZONA_DESPACHO,e);
			}		
		}
	}

	public long carroComprasGetCantidadProductos(long cliente_id,String idSession) throws GrabilityException {
		try {
			return clientesService.carroComprasGetCantidadProductosFO(cliente_id, idSession);
		} catch (Exception e) {
			if(Util.isSqlException(e)){		
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantDeliveryWindow.SC_ERROR_OBTENER_CANTIDAD_PRODUCTOS, ConstantDeliveryWindow.MSG_ERROR_OBTENER_CANTIDAD_PRODUCTOS,e);
			}		
		
		}
	}

	public List carroComprasPorCategorias(long cliente_id, String local,String idSession) throws GrabilityException {
		try {
			return clientesService.carroComprasPorCategoriasFO(cliente_id, local, idSession);
		} catch (Exception e) {
			if(Util.isSqlException(e)){			
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantDeliveryWindow.SC_ERROR_OBTENER_CARRO_COMPRAS, ConstantDeliveryWindow.MSG_ERROR_OBTENER_CARRO_COMPRAS,e);
			}		
		}
	}

	public doRecalculoResultado doRecalculoPromocionNew(doRecalculoCriterio recalculodto, CuponDsctoDTO cddto, List cuponProds) throws GrabilityException {
		try {
			return promocionesService.doRecalculoPromocion(recalculodto, cddto, cuponProds);
		} catch (Exception e) {
			if(Util.isSqlException(e)){				
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantDeliveryWindow.SC_ERROR_AL_CALCULAR_PROMOCIONES, ConstantDeliveryWindow.MSG_ERROR_AL_CALCULAR_PROMOCIONES,e);
			}	
		}
	}

	public ClientesDTO getClienteById(long idCliente) throws GrabilityException {
		try {
			return clientesService.getClienteById(idCliente);
		} catch (Exception e) {
			if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantClient.SC_CLIENTE_NO_EXISTE, ConstantClient.MSG_CLIENTE_NO_EXISTE,e);
			}	
		}
	}

	public boolean esPrimeraCompra(long idCliente) throws GrabilityException {
		try {
			return pedidosService.esPrimeraCompra(idCliente);
		} catch (Exception e) {
			if(Util.isSqlException(e)){			
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantDeliveryWindow.SC_ERROR_SI_ES_PRIMERA_COMPRA, ConstantDeliveryWindow.MSG_ERROR_SI_ES_PRIMERA_COMPRA,e);
			}	
		}
	}

	public boolean zonaEsRetiroLocal(long idZona) throws GrabilityException {
		try {
			return pedidosService.zonaEsRetiroLocal(idZona);
		} catch (Exception e) {
			if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantDeliveryWindow.SC_ERROR_SI_ES_RETIRO, ConstantDeliveryWindow.MSG_ERROR_SI_ES_RETIRO,e);
			}	
		}
	}

	public boolean clienteEsConfiable(long rut) throws GrabilityException {
		try {
			return clientesService.clienteEsConfiable(rut);
		} catch (Exception e) {
			if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantDeliveryWindow.SC_ERROR_OBTENER_CLIENTE_CONFIABLE, ConstantDeliveryWindow.MSG_ERROR_OBTENER_CLIENTE_CONFIABLE,e);
			}	
		}
	}

	public ParametroFoDTO getParametroByKey(String key)	throws GrabilityException {
		try {
			return parametrosService.getParametroByKey(key);
		} catch (Exception e) {
			if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantDeliveryWindow.SC_ERROR_OBTENER_PARAMETRO, ConstantDeliveryWindow.MSG_ERROR_OBTENER_PARAMETRO,e);
			}	
		}
	}

	/**
	 * @param idZona
	 * @param string
	 * @param cantProductos
	 * @return
	 */
	public long getJornadaDespachoMayorCapacidad(long idZona,String fechaDespacho, long cantProductos) throws GrabilityException {
		try {
			return pedidosService.getJornadaDespachoMayorCapacidad(idZona,	fechaDespacho, cantProductos);
		} catch (Exception e) {
			if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantDeliveryWindow.SC_ERROR_OBTENER_MAYORES_CAPACIDADES, ConstantDeliveryWindow.MSG_ERROR_OBTENER_MAYORES_CAPACIDADES,e);
			}	
		}
	}

	public int actualizarCapacidadOcupadaPicking(long id_jpicking)throws GrabilityException {
		try {
			return pedidosService.actualizarCapacidadOcupadaPicking(id_jpicking);
		} catch (Exception e) {
			if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantDeliveryWindow.SC_ERROR_ACTUALIZAR_CAPACIDADES, ConstantDeliveryWindow.MSG_ERROR_ACTUALIZAR_CAPACIDADES,e);
			}	
		}
	}

	/**
	 * @param diasCalendario
	 * @param id_zona
	 * @return
	 */
	public List getCalendarioDespachoByDias(int diasCalendario, long idZona, long id_jpicking, int cantProductos) throws GrabilityException {
		try {
			return pedidosService.getCalendarioDespachoByDias(diasCalendario, idZona, id_jpicking, cantProductos);
		} catch (Exception e) {
			if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantDeliveryWindow.SC_ERROR_OBTENER_CALENDARIO, ConstantDeliveryWindow.MSG_ERROR_OBTENER_CALENDARIO,e);
			}	
		}
	}

	public List getCalendarioDespachoByDias(int diasCalendario, long idZona)throws GrabilityException {
		try {
			return pedidosService.getCalendarioDespachoByDias(diasCalendario, idZona);
		} catch (Exception e) {
			if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantDeliveryWindow.SC_ERROR_OBTENER_CALENDARIO, ConstantDeliveryWindow.MSG_ERROR_OBTENER_CALENDARIO,e);
			}	
		}
	}
    public List localesRetiro() throws GrabilityException {
        try {
            return pedidosService.localesRetiro();
        } catch (Exception e) {
        	if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantDeliveryWindow.SC_ERROR_OBTENER_LOCALES_RETIRO, ConstantDeliveryWindow.MSG_ERROR_OBTENER_LOCALES_RETIRO,e);
			}	
		}
    }
    public LocalDTO getLocalByID(long id_local) throws GrabilityException{
		try{ 
			return clientesService.getLocalById(id_local);
		} catch (Exception e) {
			if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantDeliveryWindow.SC_ERROR_OBTENER_LOCAL, ConstantDeliveryWindow.MSG_ERROR_OBTENER_LOCAL,e);
			}	
		}
	}

}
