package cl.cencosud.jumbo.bizdelegate;

import java.util.List;

import org.apache.commons.lang.StringUtils;


import cl.bbr.cupondscto.service.CuponDsctoService;
import cl.bbr.jumbocl.clientes.service.ClientesService;
import cl.bbr.jumbocl.pedidos.collaboration.ProcInsPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.JorDespachoCalDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.service.PedidosService;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.bbr.jumbocl.productos.service.ProductosService;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantPurchaseOrder;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.util.Util;


public class BizDelegatePurchaseOrder {
	private PedidosService pedidosService = null;
	private ProductosService productosService = null;
	private ClientesService clientesService=null;
	private static CuponDsctoService cuponDsctoService = null;
	public BizDelegatePurchaseOrder() {
		if (pedidosService== null)
			pedidosService = new PedidosService();
		if (clientesService==null)
			clientesService= new ClientesService();
		if (cuponDsctoService==null)
			cuponDsctoService=new CuponDsctoService();
	}
	
	
	public long doInsPedidoNew(ProcInsPedidoDTO pedido, CuponDsctoDTO cddto, List cuponProds) throws GrabilityException {

        try {
            return pedidosService.doInsPedido(pedido, cddto, cuponProds);
        } catch (Exception e) { // RemoteException ex
        	if(Util.isSqlException(e)){						
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else if (StringUtils.contains(e.getCause().getMessage().toLowerCase(), "err_capacidad")){
				throw new GrabilityException(ConstantPurchaseOrder.SC_ERROR_CAPACIDAD,ConstantPurchaseOrder.MSG_ERROR_CAPACIDAD);
			}else if (StringUtils.contains(e.getCause().getMessage().toLowerCase(), "tipo_despacho_error")){
				throw new GrabilityException(ConstantPurchaseOrder.SC_ERROR_CAPACIDAD_TIPO_DESPACHO,ConstantPurchaseOrder.MSG_ERROR_CAPACIDAD_TIPO_DESPACHO);
			}else{
				throw new GrabilityException(ConstantPurchaseOrder.SC_ORDEN_PEDIDO_INVALIDA, ConstantPurchaseOrder.MSG_ORDEN_DE_PEDIDO_INVALIDA,e);
			}					
		}
    }
	
	public ProductoDTO productosById(int idCliente, int idProducto, int idLocal) throws GrabilityException {
        try {
           return productosService.productoById(idCliente, idProducto, idLocal);
        } catch (Exception e) {
        	if(Util.isSqlException(e)){						
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else {
				throw new GrabilityException(ConstantPurchaseOrder.SC_PRODUCTO_NO_ENCONTRADO, ConstantPurchaseOrder.MSG_PRODUCTO_NO_ENCONTRADO,e);
			}
		}
     }
    public List carroComprasGetProductos(long cliente_id, String local, String idSession) throws GrabilityException {
        try {
            return clientesService.carroComprasGetFO(cliente_id, local, idSession);
        } catch (Exception e) {
        	if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
        	}else{
        		throw new GrabilityException(ConstantPurchaseOrder.SC_ERROR_CARRO, ConstantPurchaseOrder.MSG_ERROR_CARRO,e);

        	}
		}
    }
    
    public List getProdsCupon (long id_cupon, String tipo) throws GrabilityException {
	    try{
	    	return cuponDsctoService.getProdsCupon(id_cupon, tipo);
	    } catch (Exception e) {
    		if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
        	}else{
        		throw new GrabilityException(ConstantPurchaseOrder.SC_PRODUCTOS_CUPON, ConstantPurchaseOrder.MSG_PRODUCTOS_CUPON,e);
        	}
		}
    }
    public CuponDsctoDTO getCuponDscto(String codigo) throws GrabilityException{
    	try{
    		return cuponDsctoService.getCuponDscto(codigo);
    	} catch (Exception e) {
    		if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
        	}else{
        		throw new GrabilityException(ConstantPurchaseOrder.SC_CUPON_DESCUENTO, ConstantPurchaseOrder.MSG_CUPON_DESCUENTO,e);
        	}
		}
    }
    
    /**
     * Verifica que cupon es para el cliente
     * @param rut
     * @param id_cupon
     * @return
     * @throws SystemException
     */
    public boolean isCuponForRut(long rut, long id_cupon) throws GrabilityException{
	    try{
	    	return cuponDsctoService.isCuponForRut(rut, id_cupon);
	    } catch (Exception e) {
	    	if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
        	}else{
        		throw new GrabilityException(ConstantPurchaseOrder.SC_CUPON_DESCUENTO_X_RUT, ConstantPurchaseOrder.MSG_CUPON_DESCUENTO_X_RUT,e);
        	}
	    }
    	
    }
    public void insertaTrackingOP(long id_pedido, String user, String mensajeLog) throws GrabilityException {
		try {
			pedidosService.insertaTrackingOP(id_pedido, user, mensajeLog);
		} catch (Exception e) {
			if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
        	}else{
        		throw new GrabilityException(ConstantPurchaseOrder.SC_INSERTAR_LOG_BO, ConstantPurchaseOrder.MSG_INSERTAR_LOG_BO,e);
        	}
		}
	}
    public PedidoDTO getPedidoById(long pedido_id) throws GrabilityException {

        try {
            return pedidosService.getPedido(pedido_id);
        } catch (Exception e) {
			if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
        	}else{
        		throw new GrabilityException(ConstantPurchaseOrder.SC_ERROR_RECUPERAR_OP, ConstantPurchaseOrder.MSG_ERROR_RECUPERAR_OP, e);
        	}
		}
    }
    
    //Valida Jornada Despacho grability
  	public boolean isValidJornadasLocal(long idJdespacho, long idJpicking, long idLocal)throws GrabilityException {
  		try {
  			return pedidosService.isValidJornadasLocal(idJdespacho, idJpicking, idLocal);
  		} catch (Exception e) {
  			if(Util.isSqlException(e)){					
  				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
  			}else{
  				throw new GrabilityException(ConstantPurchaseOrder.SC_ERROR_JORNADAS_LOCAL, ConstantPurchaseOrder.MSG_ERROR_JORNADA_LOCAL,e);
  			}	
  		}
  	}
  	
	/**
	 * Obtiene la jornada de despacho segun su id
	 * 
	 * @param id_jornada
	 * @return
	 * @throws BocException
	 */
	public JorDespachoCalDTO getJornadaDespachoById(long idJdespacho) throws GrabilityException {
		try {
			return pedidosService.getJornadaDespachoById(idJdespacho);
		} catch (Exception e) {
			if(Util.isSqlException(e)){					
  				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
  			}else{
  				throw new GrabilityException(ConstantPurchaseOrder.SC_ERROR_JORNADAS_LOCAL, ConstantPurchaseOrder.MSG_ERROR_JORNADA_LOCAL,e);
  			}
		}
	}


}
