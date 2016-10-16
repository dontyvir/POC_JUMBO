package cl.bbr.jumbocl.clientes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import cl.bbr.jumbocl.clientes.ctrl.ClientesCtrl;
import cl.bbr.jumbocl.clientes.ctrl.DireccionesCtrl;
import cl.bbr.jumbocl.clientes.dto.ClienteCriteriaDTO;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.clientes.dto.CriterioClienteSustitutoDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.clientes.dto.ProcModAddrBookDTO;
import cl.bbr.jumbocl.clientes.dto.ZonaDTO;
import cl.bbr.jumbocl.clientes.exceptions.ClientesException;
import cl.bbr.jumbocl.clientes.exceptions.DireccionesException;
import cl.bbr.jumbocl.common.exceptions.DuplicateKeyException;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * Permite manejar información y operaciones sobre Clientes, direcciones, locales, zonas y comunas. 
 * 
 * @author BBR
 *
 */
public class ClientesService {
	Logging logger = new Logging(this); 
	
	
	/*
	 * ----------------------- Clientes -----------------------
	 */
	
	/**
	 * Obtiene una lista de clientes de acuerdo a un criterio
	 * 
	 * @param  criterio ClienteCriteriaDTO
	 * @throws ServiceException
	 * @return List ClientesDTO
	 * @see    cl.bbr.jumbocl.clientes.dto.ClientesDTO
	 */
	public List getClientesByCriteria(ClienteCriteriaDTO criterio) throws ServiceException {
		ClientesCtrl clientes = new ClientesCtrl();
		List list = null;
		try{
			list = clientes.getClientesByCriteria(criterio);
		}catch(ClientesException ex){
			logger.debug("Problemas con controles de clientes");
			throw new ServiceException(ex);
		}
		return list; 
	}
	
	/**
	 * Obtiene una lista de clientes
	 * 
	 * @throws ServiceException
	 * @return List ClientesDTO
	 * @see    cl.bbr.jumbocl.clientes.dto.ClientesDTO
	 */	
	public List getClientesAll() throws ServiceException {
		ClientesCtrl clientes = new ClientesCtrl();
		List list = null;
		try{
			list = clientes.getClientesAll();
		}catch(ClientesException ex){
			logger.debug("Problemas con controles de clientes");
			throw new ServiceException(ex);
		}
		return list; 
	}
	
	/**
	 * Obtiene la cantidad de clientes de acuerdo a un criterio
	 * 
	 * @param  criterio ClienteCriteriaDTO
	 * @throws ServiceException
	 * @return int 
	 */
	public int getClientesCountByCriteria(ClienteCriteriaDTO criterio) throws ServiceException {
		ClientesCtrl clientes = new ClientesCtrl();
		int numCli = 0;
		try{
			numCli = clientes.getClientesCountByCriteria(criterio);
		}catch(ClientesException ex){
			logger.debug( "Problemas con cliente count");
			throw new ServiceException(ex);
		}
		
		return numCli;
	}	
	
	
	/**
	 * Obtiene una lista con los estados de Clientes
	 * 
	 * @throws ServiceException
	 * @return List EstadoDTO
	 * @see    cl.bbr.jumbocl.clientes.dto.EstadoDTO
	 */
	public List getEstadosCliente() throws ServiceException {
		ClientesCtrl clientes = new ClientesCtrl();
		try {
			return clientes.getEstadosClientes();
		} catch (ClientesException ex) {
			logger.debug( "Problemas con cliente by id");
			throw new ServiceException(ex);
		}
	}
	
	/**
	 * Obtiene cliente a partir de su id
	 * 
	 * @param  idcliente long
	 * @throws ServiceException
	 * @return ClientesDTO
	 */	
	public ClientesDTO getClienteById(long idcliente) throws ServiceException {
		ClientesCtrl clientes = new ClientesCtrl();
		try {
			return clientes.getClienteById(idcliente);
		} catch(ClientesException ex) {
			logger.debug( "Problemas con cliente by id");
			throw new ServiceException(ex);
		} 
	}
	
	
	/**
	 * Obtiene cliente a partir de su RUT
	 * 
	 * @param  idcliente long
	 * @throws ServiceException
	 * @return ClientesDTO
	 */	
	public ClientesDTO getClienteByRut(long rut) throws ServiceException {
		ClientesCtrl clientes = new ClientesCtrl();
		ClientesDTO cli = null;
		try{
			cli = clientes.getClienteByRut(rut);
		}catch(ClientesException ex){
			logger.debug( "Problemas con cliente by RUT");
			throw new ServiceException(ex);
		}
		return cli; 
	}

	
	/**
	 * Realiza proceso de bloqueo de cliente
	 * 
	 * @param  id_cliente long
	 * @throws ServiceException
	 * @throws SystemException 
	 */
	public void doBloqueaCliente(long id_cliente) throws ServiceException, SystemException {
	    
		ClientesCtrl ctrl = new ClientesCtrl();
	    try{
	    	ctrl.doBloqueaCliente(id_cliente);
	    }catch(ClientesException ex){
	        throw new ServiceException(ex);
	    }
	}

	/**
	 * Realiza proceso de desbloqueo de cliente
	 * 
	 * @param  id_cliente long
	 * @throws ServiceException
	 * @throws SystemException 
	 */
	public void doDesbloqueaCliente(long id_cliente)
		throws ServiceException, SystemException {
	    
		ClientesCtrl ctrl = new ClientesCtrl();
	    try{
	    	ctrl.doDesbloqueaCliente(id_cliente);
	    }catch(ClientesException ex){
	        throw new ServiceException(ex);
	    }
	}	
	
	/**
	 * Obtiene el id de cliente de acuerdo a una busqueda por rut, apellido o ambas
	 * 
	 * @param  rut String
	 * @param  apellido String
	 * @return long
	 * @throws ServiceException
	 */
	public long getClienteByTips(String rut, String apellido) throws ServiceException {
		ClientesCtrl clientes = new ClientesCtrl();
		long id_cli = 0;
		try{
			id_cli = clientes.getClienteByTips(rut, apellido);
		}catch(ClientesException ex){
			logger.debug( "Problemas con cliente by id");
			throw new ServiceException(ex);
		}
		return id_cli; 
	}

	/*
	 *  ------------------------------ Direccion-----------------------------
	 */
	
	/**
	 * Entrega una direccion dado su id de direccion
	 * 
	 * @param  id_direccion long
	 * @return DireccionesDTO
	 * @throws ServiceException	 
	 */	
	public DireccionesDTO getDireccionByIdDir(long id_direccion) throws ServiceException {
		DireccionesCtrl dirs = new DireccionesCtrl();
		DireccionesDTO dir = null;
		try{
			dir = dirs.getDireccionByIdDir(id_direccion);	
		}catch (DireccionesException ex) {
			logger.debug( "Problemas con direccion by id"+ ex);
			throw new ServiceException(ex);
		}
		return 	dir;
	}
	
	/**
	 * Entrega el listado de direcciones en base a un id de cliente
	 * 
	 * @param  id_cliente long
	 * @return List DireccionesDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.clientes.dto.DireccionesDTO
	 */	
	public List getDireccionesByIdCliente(long id_cliente) throws ServiceException{
		DireccionesCtrl dirs = new DireccionesCtrl();
		List list = null;

		try {
			list = dirs.getDireccionesByIdCliente(id_cliente);
		} catch (DireccionesException ex) {
			logger.debug( "Problemas con controles de direcciones"+ ex);
			throw new ServiceException(ex);
		}

		return list;
		
	}

	/**
	 * Actualiza la información de la direccion, ingresada en procparam
	 * 
	 * @param  procparam ProcModAddrBookDTO
	 * @return boolean
	 * @throws ServiceException
	 */
	public boolean setModAddrBook(ProcModAddrBookDTO procparam) throws ServiceException {
	    DireccionesCtrl direcciones = new DireccionesCtrl();
	    try{
	       return  direcciones.setModAddrBook(procparam);
	    }catch(DireccionesException ex){
	        logger.debug(" Problemas con setModAddrBook ");
	        throw new ServiceException(ex);
	    }
	}
	
	/**
	 * Obtiene una lista de estados correspondientes a las Direcciones
	 * 
	 * @return List EstadoDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.clientes.dto.EstadoDTO
	 */	
	public List getEstadosDirecciones() throws ServiceException {
		DireccionesCtrl dirs= new DireccionesCtrl();
		try {
			return dirs.getEstadosDirecciones();
		} catch (DireccionesException ex) {
			logger.debug( "Problemas con getEstadosDirecciones");
			throw new ServiceException(ex);
		}
	}
	
	/**
	 * Obtiene una listado de zonas
	 * 
	 * @return List ZonaDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.clientes.dto.ZonaDTO
	 */	
	public List getZonas() throws ServiceException {
		DireccionesCtrl dirs= new DireccionesCtrl();
		try {
			return dirs.getZonas();
		} catch (DireccionesException ex) {
			logger.debug( "Problemas con getLocales");
			throw new ServiceException(ex);
		}
	}
	
	
	/**
	 * Obtiene un listado de Locales
	 * 
	 * @return List ZonaDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.clientes.dto.ZonaDTO
	 */	
	
	public List getLocales() throws ServiceException {
		DireccionesCtrl dirs= new DireccionesCtrl();
		try {
			return dirs.getLocales();
		} catch (DireccionesException ex) {
			logger.debug( "Problemas con getLocales");
			throw new ServiceException(ex);
		}
	}
	
	public List getLocales(String cod_sucursal) throws ServiceException {
		DireccionesCtrl dirs= new DireccionesCtrl();
		try {
			return dirs.getLocal(cod_sucursal);
		} catch (DireccionesException ex) {
			logger.debug( "Problemas con getLocales");
			throw new ServiceException(ex);
		}
	}
	
	/**
	 * Obtiene un listado de comunas
	 * 
	 * @return List ComunaDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.clientes.dto.ComunasDTO
	 */	
	public List getComunas() throws ServiceException {
		DireccionesCtrl dirs= new DireccionesCtrl();
		try {
			return dirs.getComunas();
		} catch (DireccionesException ex) {
			logger.debug( "Problemas con getComunas");
			throw new ServiceException(ex);
		}
	}
	
	
	/**
	 * Obtiene un listado de comunas con Poligonos
	 * 
	 * @return List ComunaDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.clientes.dto.ComunasDTO
	 */	
	public List getComunasConPoligonos() throws ServiceException {
		DireccionesCtrl dirs= new DireccionesCtrl();
		try {
			return dirs.getComunasConPoligonos();
		} catch (DireccionesException ex) {
			logger.debug( "Problemas con getComunas");
			throw new ServiceException(ex);
		}
	}
	
	
	/**
	 * Obtiene la Comuna por su ID
	 * 
	 * @return String 
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.clientes.dto.ComunasDTO
	 */	
	public String getComunaById(long id_comuna) throws ServiceException {
	    DireccionesCtrl dirs= new DireccionesCtrl();
		try {
			return dirs.getComunaById(id_comuna);
		} catch (DireccionesException ex) {
			logger.debug( "Problemas con getComunas");
			throw new ServiceException(ex);
		}
	}
	
	
	/**
	 * Obtiene un listado de comunas con Zona asignada
	 * 
	 * @return List ComunaDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.clientes.dto.ComunaDTO
	 */	
	/*public List getComunasConZona() throws ServiceException {
		DireccionesCtrl dirs= new DireccionesCtrl();
		try {
			return dirs.getComunasConZona();
		} catch (DireccionesException ex) {
			logger.debug( "Problemas con getComunas");
			throw new ServiceException(ex);
		}
	}*/

	
	/**
	 * Obtiene un listado de comunas de Facturación
	 * 
	 * @return List ComunaDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.clientes.dto.ComunasDTO
	 */	
	public List getComunasFact() throws ServiceException {
		DireccionesCtrl dirs= new DireccionesCtrl();
		try {
			return dirs.getComunasFact();
		} catch (DireccionesException ex) {
			logger.debug( "Problemas con getComunas");
			throw new ServiceException(ex);
		}
	}
	
	/**
	 * Obtiene un listado de los tipos de calles
	 * 
	 * @return List de TipoCalleDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.clientes.dto.TipoCalleDTO
	 */
	public List getTiposCalle() throws ServiceException {
		DireccionesCtrl dirs= new DireccionesCtrl();
		try {
			return dirs.getTiposCalle();
		} catch (DireccionesException ex) {
			logger.debug( "Problemas con getTiposCalle");
			throw new ServiceException(ex);
		}
	}
	
	/**
	 * Obtiene un listado de comunas de acuerdo a un id de zona
	 * 
	 * @param  id_zona long
	 * @return List ComunaDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.clientes.dto.ComunaDTO
	 */	
	/*public List getComunasByZona(long id_zona) throws ServiceException {
		DireccionesCtrl dirs= new DireccionesCtrl();
		try {
			return dirs.getComunasByZona(id_zona);
		} catch (DireccionesException ex) {
			logger.debug( "Problemas con getTiposCalle");
			throw new ServiceException(ex);
		}
	}*/
	
	/**
	 * Obtiene un listado de zonas de acuerdo a un id de comuna
	 * 
	 * @param  id_comuna long
	 * @return List ZonaDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.clientes.dto.ZonaDTO
	 */	
	public List getZonasByComuna(long id_comuna) throws ServiceException {
		DireccionesCtrl dirs= new DireccionesCtrl();
		try {
			return dirs.getZonasByComuna(id_comuna);
		} catch (DireccionesException ex) {
			logger.debug( "Problemas con getTiposCalle");
			throw new ServiceException(ex);
		}
	}
	
	/**
	 * Obtiene un listado de locales de acuerdo a un id de zona
	 * 
	 * @param  id_zona long
	 * @return List LocalDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.clientes.dto.LocalDTO
	 */	
	public List getLocalesByZona(long id_zona) throws ServiceException {
		DireccionesCtrl dirs= new DireccionesCtrl();
		try {
			return dirs.getLocalesByZona(id_zona);
		} catch (DireccionesException ex) {
			logger.debug( "Problemas con getTiposCalle");
			throw new ServiceException(ex);
		}
	}

	
	/**
	 * Obtiene la información de una zona dado su id_zona
	 * 
	 * @param  id_zona long
	 * @return ZonaDTO
	 * @throws ServiceException
	 */	
	public ZonaDTO getZonaById(long id_zona) throws ServiceException {
		DireccionesCtrl dirs= new DireccionesCtrl();
		try {
			return dirs.getZonaById(id_zona);
		} catch (DireccionesException ex) {
			logger.debug( "Problemas con getTiposCalle");
			throw new ServiceException(ex);
		}
	}
	
	/**
	 * Obtiene local segun su id
	 * @param id_local
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public LocalDTO getLocalById(long id_local) throws ServiceException, SystemException{
		ClientesCtrl ctrl = new ClientesCtrl();
		try {
			return ctrl.getLocalById(id_local);
		} catch (ClientesException e) {
			throw new ServiceException(e);
		}
	}

	public boolean doModLocal(LocalDTO dto) throws ServiceException, SystemException{
		ClientesCtrl ctrl = new ClientesCtrl();
		try {
			return ctrl.doModLocal(dto);
		} catch (ClientesException e) {
			throw new ServiceException(e);
		}
	}
	public boolean doModZonaLocal(LocalDTO dto) throws ServiceException, SystemException{
		ClientesCtrl ctrl = new ClientesCtrl();
		try {
			return ctrl.doModZonaLocal(dto);
		} catch (ClientesException e) {
			throw new ServiceException(e);
		}
	}
	public boolean doAddLocal(LocalDTO dto) throws ServiceException, SystemException{
		ClientesCtrl ctrl = new ClientesCtrl();
		try {
			return ctrl.doAddLocal(dto);
		} catch (ClientesException e) {
			throw new ServiceException(e);
		}
	}
	public int doAddLocalWithZone(LocalDTO dto) throws ServiceException, SystemException{
		ClientesCtrl ctrl = new ClientesCtrl();
		try {
			return ctrl.doAddLocalWithZone(dto);
		} catch (ClientesException e) {
			throw new ServiceException(e);
		}
	}
    public boolean clienteEsConfiable(long rut) throws ServiceException, SystemException{
        ClientesCtrl ctrl = new ClientesCtrl();
        try {
            return ctrl.clienteEsConfiable(rut);
        } catch (ClientesException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @return
     */
    public List getRutsConfiables() throws ServiceException, SystemException{
        ClientesCtrl ctrl = new ClientesCtrl();
        try {
            return ctrl.getRutsConfiables();
        } catch (ClientesException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @param ruts
     * @return
     */
    public void addRutsConfiables(Vector ruts) throws ServiceException, SystemException{
        ClientesCtrl ctrl = new ClientesCtrl();
        try {
            ctrl.addRutsConfiables(ruts);
        } catch (ClientesException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @return
     */
    public List getLogRutsConfiables() throws ServiceException, SystemException{
        ClientesCtrl ctrl = new ClientesCtrl();
        try {
            return ctrl.getLogRutsConfiables();
        } catch (ClientesException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @param usr
     */
    public void addLogRutConfiables(String user, String nombre, String msjLog) throws ServiceException, SystemException{
        ClientesCtrl ctrl = new ClientesCtrl();
        try {
            ctrl.addLogRutConfiables(user, nombre, msjLog);
        } catch (ClientesException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @param idCliente
     */
    public void actualizaContadorEncuestaCliente(long idCliente, long idPedido, int nroCompras) throws ServiceException, SystemException{
        ClientesCtrl ctrl = new ClientesCtrl();
        try {
            ctrl.actualizaContadorEncuestaCliente(idCliente, idPedido, nroCompras);
        } catch (ClientesException e) {
            throw new ServiceException(e);
        }
    }
    
    /**
     * 
     * Retorna la lista de productos del pedido por categoría.
     * 
     * @param id_pedido     Identificador único del cliente
     * @return              Lista de DTO con datos delos productos
     * 
     * @throws ServiceException
     * 
     */ 
    public List resumenCompraGetCategoriasProductos(long id_pedido) throws ServiceException {        
        ClientesCtrl dirctr = new ClientesCtrl();
        try {            
            return dirctr.resumenCompraGetCatPro(id_pedido);            
        } catch (ClientesException ex) {
            logger.error( "Problemas (resumenCompraGetCategoriasProductos)", ex);
            throw new ServiceException(ex);
        }
    }
    
    /**
     * Ingresa un registro para el envío de mail.
     * 
     * @param mail  DTO con datos del mail a enviar.
     * @throws ServiceException
     */ 
    public void addMail(MailDTO mail) throws ServiceException {
        ClientesCtrl dirctr = new ClientesCtrl();
        try {
            dirctr.addMail(mail);
        } catch (ClientesException ex) {
            logger.error( "Problemas con mail", ex);
            throw new ServiceException(ex);
        }       
        
    }

	public boolean doBlanqueoDireccion(long id_cliente)  throws ServiceException, SystemException {
	    
		ClientesCtrl ctrl = new ClientesCtrl();
	    try{
	    	return ctrl.doBlanqueoDireccion(id_cliente);
	    }catch(ClientesException ex){
	        throw new ServiceException(ex);
	    }
	}

    public boolean tieneDireccionesConCobertura(long idCliente) throws ServiceException, SystemException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {            
            return dirctr.tieneDireccionesConCobertura(idCliente);
        } catch (ClientesException ex) {
            throw new ServiceException(ex);
        }
    }
    
    /*
     * DESDE FO
     * */
    
    /**
	 * Direcciones de despacho para un cliente.
	 * 
	 * @param cliente_id	Identificador único del cliente
	 * @throws SystemException 
	 * 
	 */
	public List clienteGetAllDireccionesFO(long cliente_id) throws ServiceException {
		ClientesCtrl dirctr = new ClientesCtrl();
		try {
			return dirctr.listadoDireccionesFO( cliente_id );
		} catch (ClientesException ex) {
			logger.error( "Problemas con controles de clientes", ex);
			throw new ServiceException(ex);
		}
    }

	/**
	 * Datos del cliente por RUT
	 * 
	 * @param rut	RUT del cliente a consultar
	 * @return		DTO con datos del cliente
	 * @throws ServiceException 
	 */
	public ClienteDTO clienteGetByRutFO(long rut) throws ServiceException {
		ClientesCtrl dirctr = new ClientesCtrl();
		try {
			return dirctr.ClienteGetByRutFO( rut );
		} catch (ClientesException ex) {
			logger.error( "Problemas con controles de clientes", ex);
			throw new ServiceException(ex);
		}
	}
	
	/**
	 * Datos del cliente por ID
	 * 
	 * @param cliente_id	Identificador único del cliente
	 * @return				DTO con datos del cliente
	 * @throws ServiceException 
	 */
	public ClienteDTO clienteGetByIdFO(long cliente_id) throws ServiceException {

		ClientesCtrl dirctr = new ClientesCtrl();

		try {
			return dirctr.ClienteGetByIdFO( cliente_id );
		} catch (ClientesException ex) {
			logger.error( "Problemas con controles de clientes", ex);
			throw new ServiceException(ex);
		}
		
	}	

	/**
	 * Desinscribe mail cliente
	 * 
	 * @param cliente	ClinteDTO
	 * @return boolean se realizo el update o no
	 * @throws ServiceException 
	 */
	public boolean clienteDesinscribeMailFO(ClienteDTO cliente) throws ServiceException {

		ClientesCtrl dirctr = new ClientesCtrl();

		try {
			return dirctr.ClienteDesinscribeMailFO( cliente );
		} catch (ClientesException ex) {
			logger.error( "Problemas con controles de clientes", ex);
			throw new ServiceException(ex);
		}
		
	}	
	
	/**
	 * Datos de las últimas compras para el cliente indicado
	 * 
	 * @param cliente_id	Identificador único del cliente
	 * @return 				Lista de DTO con datos de compras
	 * @throws ServiceException 
	 */
	public List ultComprasGetListFO(long cliente_id) throws ServiceException {

		ClientesCtrl dirctr = new ClientesCtrl();

		try {
			
			return dirctr.ultComprasGetListFO( cliente_id );		
			
		} catch (ClientesException ex) {
			logger.error( "Problemas (ultComprasGetList)", ex);
			throw new ServiceException(ex);
		}		
	}

	/**
	 * Recupera los productos de las listas de las últimas compras seleccionadas.
	 * 
	 * @param listas		Identificador de las listas
	 * @param local         Identificador único del local
	 * @param orden			Forma de ordenamiento de los productos
	 * @param cliente_id	Identificador único del cliente
	 * @return list			Lista de DTO
	 * @throws ServiceException
	 */
	public List ultComprasProductosGetListFO(String listas, String local, long cliente_id, long rut) throws ServiceException {
		ClientesCtrl dirctr = new ClientesCtrl();
		try {
			return dirctr.ultComprasGetCatListFO( listas, local, cliente_id, rut );
		} catch (ClientesException ex) {
			logger.error( "Problemas (ultComprasProductosGetList)", ex);
			throw new ServiceException(ex);
		}			
	}

	/**
	 * Ingresa un nuevo cliente.
	 * 
	 * @param cliente	DTO con datos del cliente
	 * @throws ServiceException
	 */	
	public long clienteInsertFO(ClienteDTO cliente) throws ServiceException {

		try {

			ClientesCtrl ctr = new ClientesCtrl();
			return ctr.clienteInsertFO( cliente );
			
		} catch (ClientesException ex) {
			logger.error("Problema DAO", ex);
			throw new ServiceException(ex);
		} catch (Exception ex) {
			logger.error("Problema general", ex);
			throw new ServiceException(ex);
		}

	}	

	/**
	 * Modifica los datos de un cliente existente.
	 * 
	 * @param cliente	DTO con datos del cliente
	 * @throws ServiceException
	 */
	public void clienteUpdateFO(ClienteDTO cliente) throws ServiceException {

		try {

			ClientesCtrl ctr = new ClientesCtrl();
			ctr.clienteUpdateFO( cliente );
			
		} catch (ClientesException ex) {
			logger.error("Problema DAO", ex);
			throw new ServiceException(ex);
		} catch (Exception ex) {
			logger.error("Problema general", ex);
			throw new ServiceException(ex);
		}

	}	

    /**
     * Actualiza los datos de contacto de un cliente existente.
     * 
     * @param cliente   DTO con datos del cliente
     * @throws ServiceException
     */
    public void updateDatosContactoClienteFO(ClienteDTO cliente) throws ServiceException {

        try {

        	ClientesCtrl ctr = new ClientesCtrl();
            ctr.updateDatosContactoClienteFO( cliente );
            
        } catch (ClientesException ex) {
            logger.error("Problema DAO", ex);
            throw new ServiceException(ex);
        } catch (Exception ex) {
            logger.error("Problema general", ex);
            throw new ServiceException(ex);
        }

    }   
    
    
	/**
	 * Modifica el estado de un cliente existente. Esta acción no cambia la clave, ya que en el 
	 * comando anterior se le permite cambiar la clave en el formulario dispuesto para ello.
	 * El estado que se ingresa como parámetro determina el estado que queda el cliente 
	 * luego de modificar su clave.
	 * 
	 * @param rut		RUT del cliente
	 * @param estado	Estado del cliente 
	 * @throws ServiceException
	 */
	public void clienteChangePassFO(long rut, String estado ) throws ServiceException {

		try {

			ClientesCtrl ctr = new ClientesCtrl();
			ctr.clienteChangePassFO( rut, estado );
			
		} catch (ClientesException ex) {
			logger.error("Problema DAO", ex);
			throw new ServiceException(ex);
		} catch (Exception ex) {
			logger.error("Problema general", ex);
			throw new ServiceException(ex);
		}

	}		

	/**
	 * 
	 * 
	 * @param cli_id	Id del cliente
	 * @param email		Email del cliente
	 * @param codigo	codigo del teléfono del cliente
	 * @param telefono	telefono del cliente 
	 * @throws ServiceException
	 */
	public void clienteChangeDatosPaso3FO(long cli_id, String email, String codigo, String telefono ) throws ServiceException {

		try {

			ClientesCtrl ctr = new ClientesCtrl();
			ctr.clienteChangeDatosPaso3FO( cli_id, email, codigo, telefono );
			
		} catch (ClientesException ex) {
			logger.error("Problema DAO", ex);
			throw new ServiceException(ex);
		} catch (Exception ex) {
			logger.error("Problema general", ex);
			throw new ServiceException(ex);
		}

	}		
	
	
	/**
	 * Modifica la clave de un cliente existente.
	 * El estado que se ingresa como parámetro determina el estado que queda el cliente 
	 * luego de modificar su clave.
	 * 
	 * @param rut		RUT del cliente
	 * @param estado	Estado del cliente
	 * @param clave		Clave nueva para el cliente 
	 * @throws ServiceException
	 */	
	public void clienteChangePassFO(long rut, String clave, String estado ) throws ServiceException {

		try {

			ClientesCtrl ctr = new ClientesCtrl();
			ctr.clienteChangePassFO( rut, clave, estado );
			
		} catch (ClientesException ex) {
			logger.error("Problema DAO", ex);
			throw new ServiceException(ex);
		} catch (Exception ex) {
			logger.error("Problema general", ex);
			throw new ServiceException(ex);
		}

	}		
	
	/**
	 * 
	 * Ingresa una nueva dirección de despacho para el cliente.
	 * 
	 * @param direccion	DTO con datos de la dirección a ingresar
	 * 
	 * @throws ServiceException
	 * 
	 */
	public long clienteInsertDireccionFO( DireccionesDTO direccion ) throws ServiceException {
		try {
			ClientesCtrl ctr = new ClientesCtrl();
			return ctr.insertDireccionFO(direccion);
		} catch (ClientesException ex) {
			logger.error("Problema DAO", ex);
			throw new ServiceException(ex);
		} catch (Exception ex) {
			logger.error("Problema general", ex);
			throw new ServiceException(ex);
		}
	}		
	
	/**
	 * 
	 * Elimina una dirección de despacho para el cliente.
	 * 
	 * @param direccion_id	Identificador único de la dirección a eliminar
	 * 
	 * @throws ServiceException
	 * 
	 */
	public void clienteDeleteDireccionFO( long direccion_id ) throws ServiceException {

		try {

			ClientesCtrl ctr = new ClientesCtrl();
			ctr.deleteDireccionFO(direccion_id);
			
		} catch (ClientesException ex) {
			logger.error("Problema DAO", ex);
			throw new ServiceException(ex);
		} catch (Exception ex) {
			logger.error("Problema general", ex);
			throw new ServiceException(ex);
		}

	}
	
	/**
	 * 
	 * Modifica una dirección de despacho para el cliente.
	 * 
	 * @param direccion_id		DTO con datos de la dirección a ingresar
	 * 
	 * @throws ServiceException
	 * 
	 */	
	public void clienteUpdateDireccionFO( DireccionesDTO direccion_id ) throws ServiceException {

		try {

			ClientesCtrl ctr = new ClientesCtrl();
			ctr.updateDireccionFO(direccion_id);
			
		} catch (ClientesException ex) {
			logger.error("Problema DAO", ex);
			throw new ServiceException(ex);
		} catch (Exception ex) {
			logger.error("Problema general", ex);
			throw new ServiceException(ex);
		}

	}		


	/**
	 * Retorna la lista de tipos de calle del sistema. 
	 * 
	 * @return Lista de tipos de calle
	 *  
	 * @throws ServiceException
	 */	
	public List tiposCallesGetListFO() throws ServiceException {

		ClientesCtrl dirctr = new ClientesCtrl();
		List list = null;

		try {
			list = dirctr.getTiposCalleFO();
		} catch (ClientesException ex) {
			logger.error( "Problemas con controles de clientes", ex);
			throw new ServiceException(ex);
		}

		return list;

	}

	/**
	 * 
	 * Retorna la lista de productos del carro de compras del cliente. 
	 * Si el local es -1 sólo recupera los productos para consultar por datos de productos sin precios. 
	 * 
	 * @param local			Identificador del local asociado a la dirección de despachos
	 * @param cliente_id	Identificador único del cliente
	 * @return 				Lista de DTO con datos del los productos
	 * 
	 * @throws ServiceException
	 * 
	 */
	public List carroComprasGetFO(long cliente_id, String local, String idSession) throws ServiceException {
		ClientesCtrl dirctr = new ClientesCtrl();
		try {
			return dirctr.getCarroComprasFO( cliente_id, local, idSession );
		} catch (ClientesException ex) {
			logger.error( "Problemas getCarroCompras", ex);
			throw new ServiceException(ex);
		}
	}
    
    //riffo
    
    public List carroComprasGetCheckOutFO(long cliente_id, String local, String idSession) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.getCarroComprasCheckOutFO( cliente_id, local, idSession );
        } catch (ClientesException ex) {
            logger.error( "Problemas getCarroComprasCheckOut", ex);
            throw new ServiceException(ex);
        }
    }
    
    
    /*
     * Crea el cliente desde la sesión de invitado para poder asociar el pedido y guardar la información
     * Se asocia el carro de compras y se ingresan los criterios de sustitución
     */
    public String creaClienteDesdeInvitadoFO(ClienteDTO cliente, DireccionesDTO desp, String forma_despacho) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.creaClienteDesdeInvitadoFO( cliente, desp, forma_despacho);
        } catch (ClientesException ex) {
            logger.error( "Problemas creaClienteDesdeInvitado", ex);
            throw new ServiceException(ex);
        }
    }
    
    
    /*
     * Se asocia el carro de compras al Nuevo ID del Cliente
     */
    public boolean reasignaCarroDelInvitadoFO(long idCliente, long idInvitado) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.reasignaCarroDelInvitadoFO(idCliente, idInvitado);
        } catch (ClientesException ex) {
            logger.error( "Problemas creaClienteDesdeInvitado", ex);
            throw new ServiceException(ex);
        }
    }
   

    /*
     * Se asocian los criterios de sustitución al Nuevo ID de Cliente
     */
    public boolean reasignaSustitutosDelInvitadoFO(long idCliente, long idInvitado) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.reasignaSustitutosDelInvitadoFO(idCliente, idInvitado);
        } catch (ClientesException ex) {
            logger.error( "Problemas creaClienteDesdeInvitado", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * Retorna la true si el carro esta vacio, de lo contrario retorna false
     * 
     * @param cliente_id
     *            Identificador único del cliente
     * @param idSession
     *            Identificador de la sessión
     * @return boolean true si el carro esta vacio sino false
     * 
     * @throws SystemException
     */
    public boolean isCarroComprasEmptyFO(long cliente_id, String idSession) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.isCarroComprasEmptyFO( cliente_id, idSession );
        } catch (ClientesException ex) {
            logger.error( "Problemas isCarroComprasEmpty", ex);
            throw new ServiceException(ex);
        }
    }
    
    /**
     * Retorna la cantidad de productos en el carro
     * 
     * @param cliente_id
     *            Identificador único del cliente
     * @param idSession
     *            Identificador de la sessión
     * @return cantidad de productos
     * 
     * @throws SystemException
     */
    public long carroComprasGetCantidadProductosFO(long cliente_id, String idSession) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.carroComprasGetCantidadProductosFO( cliente_id, idSession );
        } catch (ClientesException ex) {
            logger.error( "Problemas carroComprasGetCantidadProductos", ex);
            throw new ServiceException(ex);
        }
    }    
    
    /**
     * 
     * Retorna la lista de productos del carro de compras del cliente ordenado por categorias. 
     * Si el local es -1 sólo recupera los productos para consultar por datos de productos sin precios. 
     * 
     * @param local         Identificador del local asociado a la dirección de despachos
     * @param cliente_id    Identificador único del cliente
     * @return              Lista de DTO con datos del los productos
     * 
     * @throws ServiceException
     * 
     */
    public List carroComprasPorCategoriasFO(long cliente_id, String local, String idSession) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.getCarroComprasPorCategoriasFO( cliente_id, local, idSession );
        } catch (ClientesException ex) {
            logger.error( "Problemas carroComprasPorCategorias", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * 
     * elimina los productos no disponibles del carro de un cliente
     * 
     * @param local         Identificador del local asociado a la dirección de despachos
     * @param cliente_id    Identificador único del cliente
     * 
     * @throws ServiceException
     * 
     */
    public void eliminaProdCarroNoDispFO(long cliente_id, String local, String idSession) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            dirctr.eliminaProdCarroNoDispFO( cliente_id, local, idSession );
        } catch (ClientesException ex) {
            logger.error( "Problemas eliminaProdCarroNoDisp", ex);
            throw new ServiceException(ex);
        }
    }    
    
    /**
     * 
     * Retorna la lista de productos del carro de compras del cliente ordenado por categorias. 
     * Si el local es -1 sólo recupera los productos para consultar por datos de productos sin precios. 
     * 
     * @param local         Identificador del local asociado a la dirección de despachos
     * @param cliente_id    Identificador único del cliente
     * @return              Lista de DTO con datos del los productos
     * 
     * @throws ServiceException
     * 
     */
    public List carroComprasPorCategoriasFO(long cliente_id, String local, String idSession, String filtro) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.getCarroComprasPorCategoriasFO( cliente_id, local, idSession, filtro);
        } catch (ClientesException ex) {
            logger.error( "Problemas carroComprasPorCategorias", ex);
            throw new ServiceException(ex);
        }
    }    

	/**
	 * Ingresa un nuevo cliente.
	 * 
	 * @param cliente 		Identificador único del cliente
	 * @param despachos		Lista con direcciones de despacho del cliente
	 * 
	 * @throws ServiceException
	 */
	public String clienteNewRegistroFO(ClienteDTO cliente, DireccionesDTO direccion) throws ServiceException {
		ClientesCtrl dirctr = new ClientesCtrl();
		try {
			return dirctr.newClienteFO( cliente, direccion);
		} catch (ClientesException ex) {
			logger.error( "Problemas con controles de clientes", ex);
			throw new ServiceException(ex);
		}		
	}

	/**
	 * Retorna si tiene o no productos en el carro de compras.
	 * 
	 * @param cliente	Identificador único del cliente
	 * @return			True: existe, False: no existe
	 * 
	 * @throws ServiceException
	 */
	public boolean clienteExisteCarroComprasFO(long cliente) throws ServiceException {

		ClientesCtrl dirctr = new ClientesCtrl();

		try {
			return dirctr.clienteExisteCarroComprasFO( cliente );
		} catch (ClientesException ex) {
			logger.error( "Problemas con controles de clientes", ex);
			throw new ServiceException(ex);
		}			
		
	}

	/**
	 * Eliminar un producto desde el carro de compras del cliente.
	 * 
	 * @param cliente		Identificador único del cliento
	 * @param producto		Identificador único del producto del carro de compras
	 * @throws ServiceException
	 */
	public void carroComprasDeleteProductoFO(long cliente, long producto, String idSession) throws ServiceException {

		ClientesCtrl dirctr = new ClientesCtrl();

		try {
			dirctr.carroComprasDeleteProductoFO( cliente, producto, idSession );
		} catch (ClientesException ex) {
			logger.error( "Problemas con controles de clientes", ex);
			throw new ServiceException(ex);
		}
		
	}

    /**
     * Modifica Cantidad Producto Mi Carro
     * 
     * @param cliente       Identificador único del cliento
     * @param producto      Identificador único del producto del carro de compras
     * @param cantidad cantidad del producto
     * @param idSession id de session del cliente
     * @throws ServiceException
     */
    public void modificarCantidadProductoMiCarroFO(long cliente, long producto, double cantidad, String idSession, String tipoSel) throws ServiceException {

    	ClientesCtrl dirctr = new ClientesCtrl();

        try {
            dirctr.modificarCantidadProductoMiCarroFO( cliente, producto, cantidad, idSession, tipoSel);
        } catch (ClientesException ex) {
            logger.error( "Problemas con controles de clientes", ex);
            throw new ServiceException(ex);
        }
        
    }
    
	/**
	 * Eliminar un producto desde el carro de compras del cliente (por id de producto).
	 * 
	 * @param cliente		Identificador único del cliento
	 * @param producto		Identificador único del producto del carro de compras
	 * @throws ServiceException
	 */
	public void carroComprasDeleteProductoxIdFO(long cliente, long producto, String idSession) throws ServiceException {
		ClientesCtrl dirctr = new ClientesCtrl();
		try {
			dirctr.carroComprasDeleteProductoxIdFO( cliente, producto, idSession );
		} catch (ClientesException ex) {
			logger.error( "Problemas con controles de clientes", ex);
			throw new ServiceException(ex);
		}		
	}
	
	
	/**
	 * Actualiza la cantidad del producto del carro de compras del cliente.
	 * 
	 * @param cliente		Identificador único del cliente
	 * @param producto		Identificador único del producto del carro de compras
	 * @param cantidad		Valor para actualizar
	 * @param nota			Valor para actualizar
	 * @throws ServiceException
	 */
	public void carroComprasUpdateProductoFO(long cliente, long producto, double cantidad, String nota, String idSession, String tipoSel) throws ServiceException {
		ClientesCtrl dirctr = new ClientesCtrl();
		try {
			dirctr.carroComprasUpdateProductoFO( cliente, producto, cantidad, nota, idSession, tipoSel);
		} catch (ClientesException ex) {
			logger.error( "Problemas con controles de clientes", ex);
			throw new ServiceException(ex);
		}		
		
	}
	
	/**
	 * Inserta un producto al carro de compras.
	 * 
	 * @param listcarro		Lista de DTO
	 * @param cliente		Identificador único del cliente
	 * @throws ServiceException
	 */
	public void carroComprasInsertProductoFO(List listcarro, long cliente, String idSession) throws ServiceException {

		ClientesCtrl dirctr = new ClientesCtrl();

		try {
			dirctr.carroComprasInsertProductoFO( listcarro, cliente, idSession);
		} catch (ClientesException ex) {
			logger.error( "Problemas con insercion de productos carro", ex);
			throw new ServiceException(ex);
		}		
		
	}
	
	/**
	 * Guarda una lista de compra a partir del carro.
	 * 
	 * @param nombre		nombre de la lista
	 * @param cliente		Identificador único del cliente
	 * @throws ServiceException
	 */
	public int carroComprasSaveListFO(String nombre, long cliente) throws ServiceException {

		ClientesCtrl dirctr = new ClientesCtrl();

		try {
			return dirctr.carroComprasSaveListFO( nombre, cliente);
		} catch (ClientesException ex) {
			logger.error( "Problemas con insercion de productos carro", ex);
			throw new ServiceException(ex);
		}		
		
	}	
    
    /**
     * Crea una sesion invitado.
     * 
     * @param idSesion
     * @throws ServiceException
     */
    public int crearSesionInvitadoFO(String idSesion) throws ServiceException {

    	ClientesCtrl cliente = new ClientesCtrl();

        try {
            return cliente.crearSesionInvitadoFO(idSesion);
        } catch (ClientesException ex) {
            logger.error( "Problemas con insercion de sesion invitado", ex);
            throw new ServiceException(ex);
        }       
        
    }       

	/**
	 * Elimina una lista de compra.
	 * 
	 * @param id_lista		id de la lista
	 * @throws ServiceException
	 */
	public int carroComprasDeleteListFO(int id_lista) throws ServiceException {

		ClientesCtrl dirctr = new ClientesCtrl();

		try {
			return dirctr.carroComprasDeleteListFO( id_lista);
		} catch (ClientesException ex) {
			logger.error( "Problemas con eliminación de la lista de compra", ex);
			throw new ServiceException(ex);
		}		
		
	}	
	
	
	/**
	 * Retorna las políticas de sustitución del sistema.
	 * 
	 * @return Lista de DTO
	 * @throws ServiceException
	 */	
	public List PoliticaSustitucionFO() throws ServiceException {

		ClientesCtrl dirctr = new ClientesCtrl();
		List list = null;

		try {
			list = dirctr.PoliticaSustitucionFO();			
		} catch (ClientesException ex) {
			logger.error( "Problemas con controles de clientes", ex);
			throw new ServiceException(ex);
		}			
		return list;
	}
	
	/**
	 * 
	 * Retorna la lista de productos por categoría del carro de compras del cliente.
	 * 
	 * @param local			Identificador del local asociado a la dirección de despachos
	 * @param cliente_id	Identificador único del cliente
	 * @param modo_orden		Forma de ordenamiento de los productos
	 * 
	 * @return				Lista de DTO con datos delos productos
	 * 
	 * @throws ServiceException
	 * 
	 */	
	public List carroComprasGetCategoriasProductosFO(String local, long cliente_id) throws ServiceException {
		ClientesCtrl dirctr = new ClientesCtrl();
		try {
			return dirctr.carroComprasGetCatProFO( local, cliente_id );
		} catch (ClientesException ex) {
			logger.error( "Problemas (carroComprasGetCategoriasProductos)", ex);
			throw new ServiceException(ex);
		}			
		
	}	
	
	/**
	 * Retorna las formas de pago del sistema.
	 * 
	 * @return Lista de DTO
	 * @throws ServiceException
	 */	
	public List FormaPagoFO() throws ServiceException {

		ClientesCtrl dirctr = new ClientesCtrl();
		List list = null;

		try {
			list = dirctr.FormaPagoFO();			
		} catch (ClientesException ex) {
			logger.error( "Problemas con controles de clientes", ex);
			throw new ServiceException(ex);
		}			
		return list;
	}	

	/**
	 * Recupera últimas compras del cliente. 
	 * 
	 * @param cliente_id	Identificador único del cliente
	 * @return				Lista de DTO
	 * @throws ServiceException
	 */
	public List compraHistoryGetListFO(long cliente_id) throws ServiceException {

		ClientesCtrl dirctr = new ClientesCtrl();
		List list = null;

		try {
			list = dirctr.ultComprasGetListFO(cliente_id);			
		} catch (ClientesException ex) {
			logger.error( "Problemas con controles de clientes", ex);
			throw new ServiceException(ex);
		}			
		return list;
	}	

	/**
	 * Elimina todos los productos del carro de compras.
	 * 
	 * @param cliente_id	Identificador único del cliente
	 * @throws ServiceException
	 */	
	public void deleteCarroCompraAllFO(long cliente_id, String ses_invitado_id) throws ServiceException {

		ClientesCtrl dirctr = new ClientesCtrl();

		try {
			dirctr.deleteCarroCompraAllFO( cliente_id, ses_invitado_id );
		} catch (ClientesException ex) {
			logger.error( "Problemas con borrar productos del carro", ex);
			throw new ServiceException(ex);
		}		
		
	}
    
    //riffo
    
    public void deleteCarroCompraFO(long cliente_id, long carro_id, String idSession) throws ServiceException {

    	ClientesCtrl dirctr = new ClientesCtrl();

        try {
            dirctr.deleteCarroCompraFO(  cliente_id,  carro_id,  idSession );
        } catch (ClientesException ex) {
            logger.error( "Problemas con borrar productos del carro", ex);
            throw new ServiceException(ex);
        }       
        
    }
	

    /**
     * Elimina todos los productos del carro de compras.
     * 
     * @param cliente_id Identificador único del cliente
     * @param id_session
     * @throws ServiceException
     */ 
    public void limpiarMiCarroFO(long cliente_id, String id_session) throws ServiceException {

    	ClientesCtrl dirctr = new ClientesCtrl();

        try {
            dirctr.limpiarMiCarroFO(cliente_id,id_session );
        } catch (ClientesException ex) {
            logger.error( "Problemas con borrar productos del carro", ex);
            throw new ServiceException(ex);
        }       
        
    }
    
    
	/**
	 * Ingresa un registro en el tracking para el web.
	 * 
	 * @param seccion	Sección de la página
	 * @param rut		RUT del cliente que deja el registro
	 * @param arg0		Información del navegador y url
	 * @throws ServiceException
	 */
	public void addTrakingFO(String seccion, Long rut, HashMap arg0) throws ServiceException {

		ClientesCtrl dirctr = new ClientesCtrl();

		try {
			dirctr.addTrakingFO( seccion, rut, arg0 );
		} catch (ClientesException ex) {
			logger.error( "Problemas con addTraking", ex);
			throw new ServiceException(ex);
		}		
		
	}	

	/**
	 * Modifica la cantidad de intentos de login para un cliente.
	 * 
	 * @param cliente_id	Identificador del cliente.
	 * @param accion		1: Aumenta 0: Reset
	 * @throws ServiceException
	 */	
	public void updateIntentosFO(long cliente_id, long accion) throws ServiceException {

		ClientesCtrl dirctr = new ClientesCtrl();

		try {
			dirctr.updateIntentosFO(cliente_id, accion);
		} catch (ClientesException ex) {
			logger.error( "Problemas con el numero de intentos del logeo", ex);
			throw new ServiceException(ex);
		}		
		
	}
    
    /**
     * Modifica los datos de un cliente invitado
     * 
     * @param cliente
     * @throws ServiceException
     */ 
    public void updateDatosInvitadoFO(ClienteDTO cliente, String opcion) throws ServiceException {

    	ClientesCtrl dirctr = new ClientesCtrl();

        try {
            dirctr.updateDatosInvitadoFO(cliente, opcion);
        } catch (ClientesException ex) {
            logger.error( "Problemas con updateDatosInvitado", ex);
            throw new ServiceException(ex);
        }       
        
    }    
    

    /**
     * @param idCliente
     * @param local
     * @param paginado
     * @param pagina
     * @return
     */
    public List carroComprasMobiFO(long idCliente, String local, int paginado, int pagina) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        List list = null;
        try {
            list = dirctr.getCarroComprasMobiFO( idCliente, local, paginado, pagina );
        } catch (ClientesException ex) {
            logger.error( "Problemas getCarroCompras", ex);
            throw new ServiceException(ex);
        }
        return list;
    }

    /**
     * @param idCliente
     * @param local
     * @return
     */
    public double getCountCarroComprasFO(long idCliente, String local) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.getCountCarroComprasFO( idCliente, local );
        } catch (ClientesException ex) {
            logger.error( "Problemas getCarroCompras", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @param idCarro
     * @param idProducto
     * @param nota
     * @return
     */
    public void updateNotaCarroCompraFO(long idCliente, long idCarro, long idProducto, String nota) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            dirctr.updateNotaCarroCompraFO( idCliente, idCarro, idProducto, nota );
        } catch (ClientesException ex) {
            logger.error( "Problemas getCarroCompras", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public List ultComprasGetListInternetFO(long idCliente) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {            
            return dirctr.ultComprasGetListInternetFO( idCliente );
        } catch (ClientesException ex) {
            logger.error( "Problemas (ultComprasGetListInternet)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public List clienteMisListasFO(long idCliente) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {            
            return dirctr.clienteMisListasFO( idCliente );
        } catch (ClientesException ex) {
            logger.error( "Problemas (clienteMisListas)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public List clienteMisListasPredefinidasFO() throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {            
            return dirctr.clienteMisListasPredefinidasFO();
        } catch (ClientesException ex) {
            logger.error( "Problemas (clienteMisListasPredefinidas)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public boolean clienteTieneSustitutosFO(long idCliente) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {            
            return dirctr.clienteTieneSustitutosFO( idCliente );
        } catch (ClientesException ex) {
            logger.error( "Problemas (clienteTieneSustitutos)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @param sustitutosPorCategorias
     */
    public void updateSustitutosClienteFO(Long idCliente, List sustitutosPorCategorias) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {            
            dirctr.updateSustitutosClienteFO( idCliente, sustitutosPorCategorias );
        } catch (ClientesException ex) {
            logger.error( "Problemas (updateSustitutosCliente)", ex);
            throw new ServiceException(ex);
        }        
    }

    /**
     * @param idCliente
     * @param criteriosProductos
     * @param idSession
     */
    public void guardaCriteriosMiCarroFO(Long idCliente, List criteriosProductos, String idSession) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {            
            dirctr.guardaCriteriosMiCarroFO( idCliente, criteriosProductos, idSession );
        } catch (ClientesException ex) {
            logger.error( "Problemas (guardaCriteriosMiCarro)", ex);
            throw new ServiceException(ex);
        }        
    }
    

    /**
     * @param idCliente
     * @param prodsNuevos
     */
    public void addSustitutosClienteFO(long idCliente, List prodsNuevos) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {            
            dirctr.addSustitutosClienteFO( idCliente, prodsNuevos );
        } catch (ClientesException ex) {
            logger.error( "Problemas (addSustitutosCliente)", ex);
            throw new ServiceException(ex);
        }        
    }

    /**
     * @param idCliente
     * @return
     */
    public boolean clienteHaCompradoFO(long idCliente) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {            
            return dirctr.clienteHaCompradoFO( idCliente );
        } catch (ClientesException ex) {
            logger.error( "Problemas (clienteHaComprado)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idGrupo
     * @return
     */
    public List listasEspecialesByGrupoFO(long idGrupo, int idLocal) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {            
            return dirctr.listasEspecialesByGrupoFO(idGrupo, idLocal);
        } catch (ClientesException ex) {
            logger.error( "Problemas (listasEspecialesByGrupo)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param listas
     * @param local
     * @return
     */
    public List clientesGetProductosListasEspecialesFO(String listas, String local, long idCliente) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {            
            return dirctr.clientesGetProductosListasEspecialesFO( listas, local, idCliente );            
        } catch (ClientesException ex) {
            logger.error( "Problemas (clientesGetProductosListasEspeciales)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @param idDireccion
     * @return
     */
    public DireccionesDTO clienteGetDireccionFO(long idDireccion) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.clienteGetDireccionFO( idDireccion );
        } catch (ClientesException ex) {
            logger.error( "Problemas con controles de clientes", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public boolean clienteTieneDisponibleRetirarEnLocalFO(long idCliente) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.clienteTieneDisponibleRetirarEnLocalFO( idCliente );
        } catch (ClientesException ex) {
            logger.error( "Problemas con controles de clientes", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @param idProducto
     * @return
     */
    public CriterioClienteSustitutoDTO criterioSustitucionByClienteProductoFO(long idCliente, long idProducto) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {            
            return dirctr.criterioSustitucionByClienteProductoFO( idCliente, idProducto );
        } catch (ClientesException ex) {
            logger.error( "Problemas (criterioSustitucionByClienteProducto)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @param criterio
     */
    public void setCriterioClienteFO(long idCliente, CriterioClienteSustitutoDTO criterio) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {            
            dirctr.setCriterioClienteFO( idCliente, criterio );
        } catch (ClientesException ex) {
            logger.error( "Problemas (setCriterioCliente)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @param criterio
     */
    public void addSustitutoClienteFO(long idCliente, CriterioClienteSustitutoDTO criterio) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {            
            dirctr.addSustitutoClienteFO( idCliente, criterio );
        } catch (ClientesException ex) {
            logger.error( "Problemas (addSustitutoCliente)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public boolean tieneDireccionesConCoberturaFO(long idCliente) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {            
            return dirctr.tieneDireccionesConCoberturaFO( idCliente );
        } catch (ClientesException ex) {
            logger.error( "Problemas (tieneDireccionesConCobertura)", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public List clienteAllDireccionesConCoberturaFO(long idCliente) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.clienteAllDireccionesConCoberturaFO( idCliente );
        } catch (ClientesException ex) {
            logger.error( "Problemas con controles de clientes", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public boolean clienteConOPsTrackingFO(long idCliente) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.clienteConOPsTrackingFO( idCliente );
        } catch (ClientesException ex) {
            logger.error( "Problemas con controles de clientes", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public List clienteListaOPsTrackingFO(long idCliente) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.clienteListaOPsTrackingFO( idCliente );
        } catch (ClientesException ex) {
            logger.error( "Problemas con controles de clientes", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param rut
     * @return
     */
    public List clienteComprasEnLocalFO(long rut) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.clienteComprasEnLocalFO( rut );
        } catch (ClientesException ex) {
            logger.error( "Problemas con controles de clientes", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param email
     */
    public void addMailHomeFO(String email) throws ServiceException, DuplicateKeyException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            dirctr.addMailHomeFO(email);
        } catch (DuplicateKeyException ex) {
            logger.error("Problema BizDelegate (addMailHome)", ex);
            throw new DuplicateKeyException(ex);            
        } catch (ClientesException ex) {
            logger.error( "Problemas con mail", ex);
            throw new ServiceException(ex);
        }
    }
    
    /**
     * @param idCliente
     * @return
     */
    public boolean AlmacenaConfirmacionMapaFO(double lat, double lng, long dir_id) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.AlmacenaConfirmacionMapaFO(lat, lng, dir_id);
        } catch (ClientesException ex) {
            logger.error( "Problemas con controles de clientes", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param rut
     * @return
     */
    public void eliminaSuscripcionEncuestaByRutFO(long rut) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            dirctr.eliminaSuscripcionEncuestaByRutFO(rut);
        } catch (ClientesException ex) {
            logger.error( "Problemas con controles de clientes", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @param idComuna
     * @return
     */
    public DireccionesDTO getDireccionClienteByComunaFO(long idCliente, long idComuna) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.getDireccionClienteByComunaFO(idCliente, idComuna);
        } catch (ClientesException ex) {
            logger.error( "Problemas con controles de clientes", ex);
            throw new ServiceException(ex);
        }
    }
    
    public void convierteCarroDonaldAfterPagoFO(long idCliente, String idSession, long idInvitado) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            dirctr.convierteCarroDonaldAfterPagoFO(idCliente, idSession, idInvitado);
        } catch (ClientesException ex) {
            logger.error( "Problemas con controles de clientes", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idCliente
     * @param idSession
     */
    public void convierteCarroDonaldFO(long idCliente, String idSession) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            dirctr.convierteCarroDonaldFO(idCliente, idSession);
        } catch (ClientesException ex) {
            logger.error( "Problemas con controles de clientes", ex);
            throw new ServiceException(ex);
        }
    }


    public void RecuperaClave_GuardaKeyClienteFO(long idCliente, String key) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            dirctr.RecuperaClave_GuardaKeyClienteFO(idCliente, key);
        } catch (ClientesException ex) {
            logger.error( "Problemas con controles de clientes - RecuperaClave_GuardaKeyCliente", ex);
            throw new ServiceException(ex);
        }
    }


    public String RecuperaClave_getKeyClienteFO(long idCliente) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.RecuperaClave_getKeyClienteFO(idCliente);
        } catch (ClientesException ex) {
            logger.error( "Problemas con controles de clientes - RecuperaClave_getKeyCliente", ex);
            throw new ServiceException(ex);
        }
    }
    
    
    public void setClienteFacebookFO(long id_cliente, String id_facebook) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            dirctr.setClienteFacebookFO(id_cliente, id_facebook);
        } catch (ClientesException ex) {
            logger.error( "Problemas con controles de clientes - setClienteFace", ex);
            throw new ServiceException(ex);
        }
    }
    	
	public String clienteGetRutByIdFacebookFO(String idFacebook) throws ServiceException {
		ClientesCtrl dirctr = new ClientesCtrl();
		try {
			return dirctr.getRutByIdFacebookFO(idFacebook);
		} catch (ClientesException ex) {
			logger.error( "Problemas con controles de clientes", ex);
			throw new ServiceException(ex);
		}		
	}
	/**
     * se filtro idProducto
     * @param cliente_id
     * @param local
     * @param idSession
     * @param filtro
     * @return
     * @throws ServiceException
     */
    public List getItemCarroComprasPorCategoriasFO(long cliente_id, String local, String idSession, String filtro) throws ServiceException {
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
            return dirctr.getItemCarroComprasPorCategoriasFO( cliente_id, local, idSession, filtro);
        } catch (ClientesException ex) {
            logger.error( "Problemas getItemCarroComprasPorCategorias", ex);
            throw new ServiceException(ex);
        }
    }
    
    public void recuperaClaveFO(ClienteDTO cliente, String contextPath) throws ServiceException{
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
             dirctr.recuperaClaveFO(cliente, contextPath);
        } catch (ClientesException ex) {
            logger.error( "Problemas recuperaClaveFO", ex);
            throw new ServiceException(ex);
        }
    }
    
    
    
    public DireccionesDTO crearDireccionDummy(DireccionesDTO d)  throws ServiceException{
    	
    	ClientesCtrl dirctr = new ClientesCtrl();
        try {
             return dirctr.crearDireccionDummy(d);
        } catch (ClientesException ex) {
            logger.error( "Problemas recuperaClaveFO", ex);
            throw new ServiceException(ex);
        }
    }
    
    public boolean isDireccionDummy(DireccionesDTO d) throws ServiceException{
    	ClientesCtrl dirctr = new ClientesCtrl();
	   	try{	   		
	   		return dirctr.isDireccionDummy(d);  
	   	}catch (Exception ex) {
	   		logger.error("Problema (isDireccionDummy)", ex);
	   		throw new ServiceException(ex);
	   	}
   }
    public List carroComprasGetVentaMasiva(long cliente_id, String local, String idSession) throws ServiceException {
		ClientesCtrl dirctr = new ClientesCtrl();
		try {
			return dirctr.getCarroComprasVentaMasiva( cliente_id, local, idSession );
		} catch (ClientesException ex) {
			logger.error( "Problemas getCarroCompras", ex);
			throw new ServiceException(ex);
		}
	}
		
}
