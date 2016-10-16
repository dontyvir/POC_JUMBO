package cl.cencosud.jumbo.bizdelegate;

import java.util.List;

import cl.bbr.jumbocl.clientes.service.ClientesService;
import cl.bbr.jumbocl.productos.service.ProductosService;
import cl.bbr.promo.lib.dto.doRecalculoCriterio;
import cl.bbr.promo.lib.dto.doRecalculoResultado;
import cl.bbr.promo.lib.service.PromocionesService;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantPurchaseOrder;
import cl.cencosud.jumbo.Constant.ConstantShoppingCart;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.util.Util;


public class BizDelegateShoppingCart {
	
	private static ClientesService clientesService = null;
	private static ProductosService proService = null;
	//private static PromocionesService promocionesService = null;
	
	
	/**
	 * Constructor Business Delegate
	 * 
	 */
	public BizDelegateShoppingCart() {
		
		if(clientesService == null)
			clientesService = new ClientesService();	
		
		if (proService == null)
			proService = new ProductosService();
		
        /*if (promocionesService == null)
            promocionesService = new PromocionesService();*/

	}
	
    /**
     * Retorna la lista de productos del carro de compras del cliente ordenado por categorias.
     * 
     * @param local: Identificador del local asociado a la dirección de despachos
     * @param cliente_id: Identificador único del cliente
     * @return Lista de DTO con datos del los productos
     * @throws GrabilityException
     */
    public List carroComprasPorCategorias(long cliente_id, String local, String idSession) throws GrabilityException {
        try {
            return clientesService.carroComprasPorCategoriasFO(cliente_id, local, idSession);
        } catch (Exception e) {
        	if(Util.isSqlException(e)){				
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantShoppingCart.SC_ERROR_AL_CONSULTAR_CARRO, ConstantShoppingCart.MSG_ERROR_AL_CONSULTAR_CARRO, e);
			}		
        }
    }
    	
	/**
	 * Obtiene las promociones de un carro
	 * 
	 * @param List productos, List lista_tcp, int idLocal
	 * @return List
	 * @throws GrabilityException
	 */	
    public List cargarPromocionesMiCarro(List productos, List lista_tcp, int idLocal) throws GrabilityException{
    	 try {
             return proService.cargarPromocionesMiCarro(productos, lista_tcp, idLocal);
         } catch (Exception e) {
         	if(Util.isSqlException(e)){				
 				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
 			}else{
 				throw new GrabilityException(ConstantShoppingCart.SC_ERROR_AL_CONSULTAR_PROMO_CARRO, ConstantShoppingCart.MSG_ERROR_AL_CONSULTAR_PROMO_CARRO, e);
 			}	
         }
    }
    
    /**
     * Se agregan parametros para aplicar cupon de descuento (cddto, cuponProds) cdd
     * 
     * @param recalculodto
     * @param cddto
     * @param cuponProds
     * @throws GrabilityException
     */
    public doRecalculoResultado doRecalculoPromocionNew(doRecalculoCriterio recalculodto) throws GrabilityException {
        try {
        	//if (promocionesService == null)
        	PromocionesService promocionesService = new PromocionesService();
            return promocionesService.doRecalculoPromocion(recalculodto, null, null);
           
        } catch (Exception e) {
        	if(Util.isSqlException(e)){				
 				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
 			}else{
 				throw new GrabilityException(ConstantShoppingCart.SC_ERROR_AL_CONSULTAR_DESCUENTOS_CARRO, ConstantShoppingCart.MSG_ERROR_AL_CONSULTAR_DESCUENTOS_CARRO, e);
 			}
        }
    }
    
    /**
     * Agrega una lista de producto al carro de compras
     * 
     * @param listcarro: Lista de DTO con datos de los productos
     * @param cliente: Identificador único del cliente
     * @throws GrabilityException
     */
    public void carroComprasInsertProducto(List listcarro, long cliente, String idSession) throws GrabilityException {    	
        try {
        	clientesService.carroComprasInsertProductoFO(listcarro, cliente, idSession);
        } catch (Exception e) {
        	if(Util.isSqlException(e)){				
 				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
 			}else{
 				throw new GrabilityException(ConstantShoppingCart.SC_ERROR_AL_ACTUALIZAR_CARRO, ConstantShoppingCart.MSG_ERROR_AL_ACTUALIZAR_CARRO, e);
 			}
        }
    }
    
    /**
     * Elimina los productos del carro de compras
     * 
     * @param cliente_id
     *            Identificador único del cliente
     * @throws SystemException
     */
    public void deleteCarroCompraAll(long cliente_id, String ses_invitado_id) throws GrabilityException {
        try {
        	clientesService.deleteCarroCompraAllFO(cliente_id, ses_invitado_id);
        } catch (Exception e) {
        	if(Util.isSqlException(e)){				
 				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
 			}else{
 				throw new GrabilityException(ConstantShoppingCart.SC_ERROR_AL_ELIMINAR_CARRO, ConstantShoppingCart.MSG_ERROR_AL_ELIMINAR_CARRO, e);
 			}
        }
    }
    
    /**
     * @param idCliente
     * @param criteriosProductos
     * @param idSession
     */
    public void guardaCriteriosMiCarro(Long idCliente, List criteriosProductos, String idSession) throws GrabilityException {
        try {
        	clientesService.guardaCriteriosMiCarroFO(idCliente, criteriosProductos, idSession);
        } catch (Exception e) {
        	if(Util.isSqlException(e)){				
 				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
 			}else{
 				throw new GrabilityException(ConstantShoppingCart.SC_ERROR_AL_CONSULTAR_CRITERIOS_CARRO, ConstantShoppingCart.MSG_ERROR_AL_CONSULTAR_CRITERIOS_CARRO, e);
 			}
        }        
    }
    
}
