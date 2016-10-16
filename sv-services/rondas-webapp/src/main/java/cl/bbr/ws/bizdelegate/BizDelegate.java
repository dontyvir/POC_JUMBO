package cl.bbr.ws.bizdelegate;

import java.util.Hashtable;
import java.util.List;

import cl.bbr.ws.exceptions.WsException;
import cl.bbr.jumbocl.common.exceptions.*;
import cl.bbr.jumbocl.pedidos.dto.ActDetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.RondaDTO;
import cl.bbr.jumbocl.pedidos.service.PedidosService;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.jumbocl.usuarios.service.UsuariosService;

/**
 * Encapsula los procesos de negocio utilizado por la carga y descarga de rondas.
 *
 */
public class BizDelegate {

	private static PedidosService 	pedidoService;
	private static UsuariosService 	usuariosService;
	
	/**
	 * Constructor Business Delegate
	 * 
	 */
	public BizDelegate() {
		if(pedidoService == null) 
			pedidoService = new PedidosService();
		if(usuariosService == null) 
			usuariosService = new UsuariosService();
	}


	/*
	 * ------------ Pedidos ------------------
	 */	
	

	
	/*
	 * ------------- Rondas ------------------
	 */

	
	/**
	 * Obtiene el detalle de una ronda
	 * @param id_ronda
	 * @return RondaDTO
	 */
	public RondaDTO getRondaById(long id_ronda)
	throws WsException, SystemException {
		try {
			return pedidoService.getRondaById(id_ronda);
		} catch (ServiceException e) {
			throw new WsException(e);
		}
	}
	
	/**
	 * Obtiene listado de productos de la ronda
	 * @param id_ronda
	 * @return List ProductosPedidoDTO
	 */
	public List getProductosRonda(long id_ronda)
		throws WsException, SystemException {
			try {
				return pedidoService.getProductosRonda(id_ronda);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new WsException(ex);
		}
	}	


	/**
	 * Obtiene listado de codigos de barra de los productos del pedido de la ronda
	 * @param id_ronda
	 * @return Listado de codigos de barra de los productos del pedido de la ronda
	 * @throws WsException
	 * @throws SystemException
	 */
	public List getBarrasRondaDetallePedido(long id_ronda)
		throws WsException, SystemException {
			try {
				return pedidoService.getBarrasRondaDetallePedido(id_ronda);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new WsException(ex);
		}
	}
	
	/**
	 * Asigna pickeador a la ronda y cambia su estado a En Picking
	 * @param id_ronda
	 * @param id_usuario
	 * @throws SystemException 
	 * @throws WsException
	 */
	public void doAsignaRonda(long id_ronda, long id_usuario) 
		throws WsException, SystemException {
			try {
				pedidoService.doAsignaRonda(id_ronda, id_usuario);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new WsException(ex);
		}
	}
	

	/**
	 * Recepciona Ronda
	 * @param ActDetallePickingDTO dto
	 * @throws SystemException 
	 * @throws WsException
	 */
	public void recepcionRondaByPOS(ActDetallePickingDTO dto) 
		throws WsException, SystemException {
			try {
				pedidoService.recepcionRondaByPOS(dto);
		} catch (ServiceException e) {
			throw new WsException(e);
		}
	}	
	
	
	public List getPromocionRonda(long id_ronda) throws WsException, SystemException {
		try {
			return pedidoService.getPromocionesRonda(id_ronda);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new WsException(ex);
		}
	}		
	
	/*
	 * ------------- Local ------------------
	 */
	
	//******************* Usuarios ***********************//
	
	/**
	 * Obtiene usuario a partir del login
	 * @param login
	 * @return usuario
	 * @throws WsException
	 * @throws SystemException
	 */
	public UserDTO getUserByLogin(String login)
		throws WsException, SystemException{
		
		try {
			return usuariosService.getUserByLogin(login);
		  } catch (ServiceException ex) { //RemoteException ex
			// Translate the service exception into
			// application exception
			  throw new WsException(ex);
		  }	
		
	}	
	
	
	/**
	 * Autentica usuario en el sistema
	 * @param login
	 * @param password
	 * @throws BocException 
	 * @throws SystemException
	 */
	public boolean doAutenticaUser(String login, String password)
		throws WsException, SystemException {
		
		try {
			return usuariosService.doAutenticaUser(login, password);
		  } catch (ServiceException ex) { //RemoteException ex
			// Translate the service exception into
			// application exception
			  throw new WsException(ex);
		  }	
		
	}	
	
	public List getUsrByPerfilyLocal(long id_perfil, long id_local)
		throws WsException, SystemException{
		
		try {
			return usuariosService.getUsrByPerfilyLocal(id_perfil, id_local);
		  } catch (ServiceException ex) { //RemoteException ex
			// Translate the service exception into
			// application exception
			  throw new WsException(ex);
		  }
	}

	
	public boolean ValidaUserByPerfilByLocal(long id_usuario, long id_perfil, long id_local)
		throws WsException, SystemException{
		
		try {
			return usuariosService.ValidaUserByPerfilByLocal(id_usuario, id_perfil, id_local);
		  } catch (ServiceException ex) { //RemoteException ex
			// Translate the service exception into
			// application exception
			  throw new WsException(ex);
		  }
	}
	

	/**
	 * Obtiene listado de sustitutos criterios
	 * @param id_ronda
	 * @return List ProductosPedidoDTO
	 */
	public List getSustitutosCriterios()
		throws WsException, SystemException {
			try {
				return pedidoService.getSustitutosCriterios();
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new WsException(ex);
		}
	}
	
	/**
	 * Obtiene Id_Sector por Nombre
	 * @param nombre
	 * @return long Id_Sector
	 */
	public long getIdSectorByNombre(String nombre)
		throws WsException, SystemException {
		try {
			return pedidoService.getIdSectorByNombre(nombre);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new WsException(ex);
		}
	}
	
    public Hashtable setOrdenProductosPDA(long id_ronda)
        throws WsException, SystemException {
        try {
            return pedidoService.setOrdenProductosPDA(id_ronda);
        }catch(ServiceException ex) { // RemoteException ex
            // Translate the service exception into
            // application exception
            throw new WsException(ex);
        }
    }
}
