package cl.cencosud.jumbo.bizdelegate;

import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.clientes.service.ClientesService;
import cl.bbr.jumbocl.pedidos.service.PedidosService;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantList;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.util.Util;

public class BizDelegateList {
	
	private static ClientesService clientesService = null;
	private static PedidosService pedidosService = null;
	
	
	/**
	 * Constructor Business Delegate
	 * 
	 */
	public BizDelegateList() {
		
		if(clientesService == null)
			clientesService = new ClientesService();
		if (pedidosService == null)
			pedidosService = new PedidosService();
		
	}
	
    public List clienteGetUltComprasInternet(long idCliente) throws GrabilityException {
        List result = new ArrayList();
        try {
            result = clientesService.ultComprasGetListInternetFO(idCliente);

        } catch (Exception e){
        	if(Util.isSqlException(e)){			
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantList.SC_ERROR_LISTAS_CLIENTE, ConstantList.MSG_ERROR_LISTAS_CLIENTE,e);
			}					
		}
        return result;
    }
	
    /**
     * @param l
     * @return
     */
    public List clienteMisListas(long idCliente) throws GrabilityException {
        List result = new ArrayList();
        try {
            result = clientesService.clienteMisListasFO(idCliente);
        } catch (Exception e){
    		if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantList.SC_ERROR_LISTAS_CLIENTE, ConstantList.MSG_ERROR_LISTAS_CLIENTE,e);
			}					
		}
        return result;
    }
    public List listaDeProductosByLista(long idLista) throws GrabilityException {
		try {
			return pedidosService.listaDeProductosByLista(idLista);
		} catch (Exception e) { // RemoteException ex
			if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantList.SC_ERROR_LISTAS_CLIENTE, ConstantList.MSG_ERROR_LISTAS_CLIENTE,e);
			}					
		}
	}
    public long setCompraHistoricaForMobile(String nombre, long cliente_id, ArrayList productos) throws GrabilityException {
        try {

        	return pedidosService.setCompraHistoricaForMobile(nombre, cliente_id, productos);

        } catch (Exception e) {
        	if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantList.SC_ERROR_LISTAS_CLIENTE, ConstantList.MSG_ERROR_LISTAS_CLIENTE,e);
			}	
        }

    }

	public List listaDeProductosPreferidos(int client_id) throws GrabilityException {
		// TODO Apéndice de método generado automáticamente
		try {

        	return pedidosService.listaDeProductosPreferidos(client_id);

        } catch (Exception e) {
        	if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantList.SC_ERROR_LISTAS_CLIENTE, ConstantList.MSG_ERROR_LISTAS_CLIENTE,e);
			}	
        }
	}

	public long updateList(long idClient, ArrayList productsArray,String listType, long listId, String listName) throws GrabilityException {
		try {

        	return pedidosService.updateList(idClient, productsArray, listType,listId,listName);

        } catch (Exception e) {
        	if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantList.SC_ERROR_LISTAS_CLIENTE, ConstantList.MSG_ERROR_LISTAS_CLIENTE,e);
			}	
        }
	}

}
