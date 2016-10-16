package cl.cencosud.jumbo.bizdelegate;

import java.util.List;

import cl.bbr.jumbocl.clientes.dto.ClienteCriteriaDTO;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.service.ClientesService;

import cl.bbr.jumbocl.pedidos.exceptions.BolException;
import cl.bbr.jumbocl.pedidos.service.PedidosService;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantClient;

import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.util.Util;


/**
 * @author 
 *
 */
public class BizDelegateClient {

	
	private static ClientesService clientesService = null;
	private static PedidosService pedidosService = null;
	
	/**
	 * Constructor Business Delegate
	 * 
	 */
	public BizDelegateClient() {
		
		if(clientesService == null)
			clientesService = new ClientesService();
		
		if(pedidosService == null)
			pedidosService = new PedidosService();

	}

		
	/**
	 * Obtiene los datos de un Cliente segun Criterio.
	 * @param criterio
	 * @return
	 * @throws BolException
	 */
	public List getClienteByCriterio(ClienteCriteriaDTO criterio) throws GrabilityException{
		try{
			return clientesService.getClientesByCriteria(criterio);
		}catch (Exception e){
				if(Util.isSqlException(e)){				
					throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
				}else{
					throw new GrabilityException(ConstantClient.SC_ERROR_RECUPERAR_CLIENTE, ConstantClient.MSG_ERROR_RECUPERAR_CLIENTE,e);
				}					
		}
	}
		
	public ClientesDTO getClienteById(long idCliente) throws GrabilityException{
		try{
			return clientesService.getClienteById(idCliente);
		}catch (Exception e){
				if(Util.isSqlException(e)){				
					throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
				}else{
					throw new GrabilityException(ConstantClient.SC_ERROR_RECUPERAR_CLIENTE, ConstantClient.MSG_ERROR_RECUPERAR_CLIENTE,e);
				}					
		}
	}
	
	public ClientesDTO getClienteByRut(long rut) throws GrabilityException{
		try{
			return clientesService.getClienteByRut(rut);
		}catch (Exception e){
				if(Util.isSqlException(e)){				
					throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
				}else{
					throw new GrabilityException(ConstantClient.SC_ERROR_RECUPERAR_CLIENTE, ConstantClient.MSG_ERROR_RECUPERAR_CLIENTE,e);
				}							
		}
	}
		    
    
    /**
     * Crea un nuevo cliente
     * 
     * @param cliente
     *            DTO con datos del cliente
     * @param despachos
     *            DTO con datos de las direcciones de despacho
     * @throws SystemException
     */
    public String clienteNewRegistro(ClienteDTO cliente, DireccionesDTO direccion) throws GrabilityException {
        try {
            return clientesService.clienteNewRegistroFO(cliente, direccion);
        }catch (Exception e){
        		if(Util.isSqlException(e)){				
					throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
				}else{
					throw new GrabilityException(ConstantClient.SC_ERROR_AL_INSERTAR_CLENTE, ConstantClient.MSG_ERROR_AL_INSERTAR_CLENTE,e);
				}					
		}
    }
    
	 //Recupera clave
	 public void recuperaClaveFO(ClienteDTO cliente, String contextPath) throws GrabilityException{
		 try {
			 clientesService.recuperaClaveFO(cliente, contextPath);
		 } catch (Exception e) {
			 throw new GrabilityException(ConstantClient.SC_ERROR_AL_RESTAURAR_PASSWORD, ConstantClient.MSG_ERROR_AL_RESTAURAR_PASSWORD,e);
		 }
	 }
	 
	/**
	 * Modifica los datos de un cliente
	 * 
	 * @param cliente
	 *            ClienteDTO con datos del cliente
	 * @throws SystemException
	 */
	public void clienteUpdate(ClienteDTO cliente) throws GrabilityException {
	
	    try {	
	    	clientesService.clienteUpdateFO(cliente);	
	    } catch (Exception e) {
	    		if(Util.isSqlException(e)){				
					throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
				}else{
					throw new GrabilityException(ConstantClient.SC_ERROR_AL_ACTUALIZAR_CLENTE, ConstantClient.MSG_ERROR_AL_ACTUALIZAR_CLENTE,e);
				}					
	    }
	
	}

	public DireccionesDTO crearDireccionDummy(DireccionesDTO d) throws GrabilityException  {
		 try {
			 return clientesService.crearDireccionDummy(d);
		 } catch (Exception e) {
			 throw new GrabilityException(ConstantClient.SC_ERROR_AL_CRAR_DIR_DUMMY, ConstantClient.MSG_ERROR_AL_CRAR_DIR_DUMMY,e);
		 }
	}
    
    /**
     * @param idSesion
     * @return id
     * @throws SystemException
     */
    public int crearSesionInvitado(String idSesion) throws GrabilityException {
        try {
            return clientesService.crearSesionInvitadoFO(idSesion);
        } catch (Exception e) {
        		if(Util.isSqlException(e)){					
					throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
				}else{
					throw new GrabilityException(ConstantClient.SC_ERROR_AL_CRAR_SESS_INVITADO, ConstantClient.MSG_ERROR_AL_CRAR_SESS_INVITADO, e);
				}					
        }
    }
	
    	  
}

