package cl.cencosud.jumbo.bizdelegate;

import java.util.Iterator;
import java.util.List;

import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.RegionesDTO;
import cl.bbr.jumbocl.clientes.dto.TipoCalleDTO;
import cl.bbr.jumbocl.clientes.service.ClientesService;
import cl.bbr.jumbocl.clientes.service.RegionesService;
import cl.bbr.jumbocl.pedidos.dto.DireccionMixDTO;
import cl.bbr.jumbocl.pedidos.service.PedidosService;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantAddress;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.util.Util;

public class BizDelegateAddress {
	
	private static ClientesService clientesService = null;
	private static RegionesService regionService = null;
	private static PedidosService pedidosService = null;

	
	/**
	 * Constructor Business Delegate
	 * 
	 */
	public BizDelegateAddress() {
		
		if(clientesService == null)
			clientesService = new ClientesService();
		
		if(regionService == null)
			regionService = new RegionesService();
		
		if(pedidosService == null)
			pedidosService = new PedidosService();

	}
	
	/**
	 * Obtiene un local de acuerdo a su id
	 * 
	 * @param id_local
	 * @return
	 * @throws GrabilityException
	 */
	public LocalDTO getLocalById(long id_local) throws GrabilityException {
		try {
			return clientesService.getLocalById(id_local);
		} catch (Exception ex) {
			throw new GrabilityException(ex);
		}
	}
	
	public DireccionesDTO getDireccionByIdDir(long id_direccion) throws GrabilityException {
      try {
            return clientesService.getDireccionByIdDir(id_direccion);
      } catch (Exception e) { // RemoteException ex
	  		if(Util.isSqlException(e)){				
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantAddress.SC_ERROR_RECUPERAR_DIR_CLIENTE_X_ID_DIR, ConstantAddress.MSG_ERROR_RECUPERAR_DIR_CLIENTE_X_ID_DIR,e);
			}	
      }
	}
      
  	public List getComunas() throws GrabilityException {
		try {
			return clientesService.getComunas();
		} catch (Exception e) { 
			if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantAddress.SC_ERROR_RECUPERAR_COMUNA_X_ID, ConstantAddress.MSG_ERROR_RECUPERAR_COMUNA_X_ID,e);
			}	
		}
	}
  	
    public RegionesDTO getRegionById(int idRegion) throws GrabilityException {
        try {
            return regionService.getRegionById(idRegion);
        } catch (Exception e) {
    		if(Util.isSqlException(e)){				
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantAddress.SC_ERROR_RECUPERAR_REGION_X_ID, ConstantAddress.MSG_ERROR_RECUPERAR_REGION_X_ID,e);
			}	
        }
    }
    
	/**
	 * Obtiene un listado de direcciones por el id del cliente
	 * 
	 * @param id_cliente
	 * @return List TipoCalleDTO
	 * @throws BocException
	 */
	public List getDireccionesByIdCliente(long id_cliente) throws GrabilityException {
		try {
			 //clientesService.getDireccionesByIdCliente(id_cliente);
			 return clientesService.clienteAllDireccionesConCoberturaFO(id_cliente);//FO al iniciar session
			 //return clientesService.clienteGetAllDireccionesFO(id_cliente);//FO
		} catch (Exception e) { // RemoteException ex
			if(Util.isSqlException(e)){				
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantAddress.SC_ERROR_RECUPERAR_DIR_CLIENTE_X_ID_CLI, ConstantAddress.MSG_ERROR_RECUPERAR_DIR_CLIENTE_X_ID_DIR,e);
			}	
		}
	}
	
	public DireccionMixDTO getDireccionIniciaSessionCliente(long idCliente) throws GrabilityException{
		try{
			return pedidosService.getDireccionIniciaSessionCliente(idCliente);
		}catch (Exception e){
			if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantAddress.SC_ERROR_RECUPERAR_DIR_CLIENTE_INICIA_SESSION, ConstantAddress.MSG_ERROR_RECUPERAR_DIR_CLIENTE_INICIA_SESSION,e);
			}
		}
	}
	
	
	
    public boolean isDireccionDummy(DireccionesDTO d) {
    	 try {
			 return clientesService.isDireccionDummy(d);
		 } catch (Exception ex) {
			 return false;
			// throw new GrabilityException(ex);
		 }
   }
    
    /**
     * Agrega una dirección de despacho al cliente
     * 
     * @param direccion
     *            DTO con datos de la dirección de despacho
     * @throws SystemException
     * @see DireccionesDTO
     */
    public long clienteInsertDireccion(DireccionesDTO direccion) throws GrabilityException {
        try {
            return clientesService.clienteInsertDireccionFO(direccion);
        } catch (Exception e) {
        	if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantAddress.SC_ERROR_INSERT_DIR_NUEVA, ConstantAddress.MSG_ERROR_INSERT_DIR_NUEVA, e);
			}
        }
    }
    
    /**
     * Modifica dirección de despacho del cliente
     * 
     * @param direccion_id
     *            DTO con datos de la dirección a modificar
     * @throws SystemException
     * @see DireccionesDTO
     */
    public void clienteUpdateDireccion(DireccionesDTO direccion) throws GrabilityException {

        try {
        	clientesService.clienteUpdateDireccionFO(direccion);
        } catch (Exception e) {
        	if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantAddress.SC_ERROR_UPDATE_DIR_NUEVA, ConstantAddress.MSG_ERROR_UPDATE_DIR_NUEVA, e);
			}
        }

    }
    
    /**
     * Elimina dirección de despacho del cliente
     * 
     * @param direccion_id
     *            Identificador único de la dirección a eliminar
     * @throws SystemException
     */
    public void clienteDeleteDireccion(long direccion_id) throws GrabilityException {
        try {
        	clientesService.clienteDeleteDireccionFO(direccion_id);
        } catch (Exception e) {
        	if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantAddress.SC_ERROR_DELETE_DIR, ConstantAddress.MSG_ERROR_DELETE_DIR, e);
			}
        }
    }
    

	/**
	 * Obtiene un listado con los tipos de calle
	 * 
	 * @return List TipoCalleDTO
	 * @throws GrabilityException
	 */
	public List getTiposCalle() throws GrabilityException {
		try {
			return clientesService.getTiposCalle();
		} catch (Exception e) {
			if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantAddress.SC_ERROR_RECUPERAR_TIPO_CALLE, ConstantAddress.MSG_ERROR_RECUPERAR_TIPO_CALLE, e);
			}
		}
	}

	/**
	 * Valida tipo calle
	 * 
	 * @return boolean isValid
	 * @throws GrabilityException
	 */
	public boolean isValidTiposCalle(long tipo) throws GrabilityException {
		boolean isValid= false;
		List tipos = this.getTiposCalle();
		Iterator it = tipos.iterator();
		while(it.hasNext()){
			TipoCalleDTO tipDto = (TipoCalleDTO) it.next();
			if(tipDto.getId() == tipo){
				isValid = true;
				break;
			}
		}				
		return isValid;
	}

}
