package cl.bbr.vte.bizdelegate;

import java.util.List;

import cl.bbr.jumbocl.clientes.service.ClientesService;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.jumbocl.pedidos.service.PedidosService;
import cl.bbr.vte.cotizaciones.dto.CategoriaDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesCriteriaDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ModProdCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProcInsCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCriteriaDTO;
import cl.bbr.vte.cotizaciones.exception.CotizacionesDAOException;
import cl.bbr.vte.cotizaciones.exception.CotizacionesException;
import cl.bbr.vte.cotizaciones.service.CotizacionesService;
import cl.bbr.vte.empresas.dto.ComprXEmpDTO;
import cl.bbr.vte.empresas.dto.ComprXSucDTO;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;
import cl.bbr.vte.empresas.dto.DireccionesDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.MailDTO;
import cl.bbr.vte.empresas.dto.SolRegDTO;
import cl.bbr.vte.empresas.dto.SucursalCriteriaDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;
import cl.bbr.vte.empresas.dto.UserDTO;
import cl.bbr.vte.empresas.exceptions.CompradoresDAOException;
import cl.bbr.vte.empresas.exceptions.CompradoresException;
import cl.bbr.vte.empresas.exceptions.SucursalesException;
import cl.bbr.vte.empresas.service.EmpresasService;
import cl.bbr.vte.exception.VteException;
import cl.bbr.vte.log.Logging;

/**
 * <p>BizDelegate para Front Office Venta Empresas.</p>
 * <p>Es la capa responsable de disponibilizar los servicios de la capa de negocios y datos del sistema.</p>
 * 
 * @author BBR e-commerce & retail
 *
 */
public class BizDelegate {

	/**
	 * Instancia para log
	 */
	private Logging logger = new Logging(this);
	/**
	 * Instancia para el service de COTIZACIONES.
	 */	
	private static CotizacionesService cotizacionesService = null;
	/**
	 * Instancia para el service de EMPRESAS.
	 */
	private static  EmpresasService empresasService = null;
    
    /**
     * Instancia para el service de PEDIDOS.
     */
    private static  PedidosService pedidosService = null;
    
    /**
     * Instancia para el service de CLIENTES.
     */
    private static  ClientesService clientesService = null;
	
	/**
	 * Constructor, se instancian los servicios.
	 */
	public BizDelegate() {
		this.logger.info("New BizDelegate");
		if( cotizacionesService == null )
			cotizacionesService = new CotizacionesService();
		if( empresasService == null )
			empresasService = new EmpresasService();
        if( pedidosService == null )
            pedidosService = new PedidosService();
        if( clientesService == null )
            clientesService = new ClientesService();
	}	
	
	/* 
	 * METODOS DE EMPRESAS
	 * 
	 */
	
	/**
	 * Recupera los datos de la empresa
	 * 
	 * @param  rut RUT de la empresa a consultar
	 * @return Empresas DTO
	 * @throws VteException
	 * @throws SystemException 
	 * @see EmpresasDTO
	 */
	public EmpresasDTO getEmpresaByRut(long rut) throws VteException, SystemException {

		try {
			return empresasService.getEmpresaByRut(rut);
		} catch (ServiceException ex) {
			logger.error("Problemas con controles de Empresas", ex);
			throw new VteException(ex);
		}		

	}

	/**
	 * Datos de la empresa
	 * 
	 * @param  empresa_id	Identificador único de la empresa a consultar
	 * @return List				Lista de DTO
	 * @throws VteException
	 * 
	 */
	public EmpresasDTO getEmpresaById(long empresa_id) throws VteException {

		try {
			return empresasService.getEmpresaById(empresa_id);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}	
	}
	
	/**
	 * Ingresa informacion de nueva empresa
	 * 
	 * @param  dto EmpresasDTO que contiene la información de la empresa
	 * @return boolean
	 * @throws VteException
	 */
	public long insEmpresa(EmpresasDTO dto) throws VteException {

		try {
			return empresasService.setCreaEmpresa(dto);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}	
	}
	
	/**
	 * Modifica informacion de la empresa
	 * 
	 * @param dto EmpresasDTO que contiene la información de la empresa
	 * @return True: éxito False: error
	 * @throws VteException
	 */
	public boolean modEmpresa(EmpresasDTO dto) throws VteException {

		try {
			return empresasService.setModEmpresa(dto);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}	
	}
	
	/**
	 * Elimina informacion de una empresa
	 * 
	 * @param  dto EmpresasDTO que contiene la información de la empresa
	 * @return boolean
	 * @throws VteException
	 */
	public boolean delEmpresa(EmpresasDTO dto)throws VteException {

		try {
			return empresasService.delEmpresa(dto);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}	
	} 

	/**
	 * Agrega una dirección de despacho a la sucursal
	 * 
	 * @param  direccion		DTO con datos de la dirección de despacho
	 * @return True: éxito False: error 
	 * @throws VteException
	 * @see DireccionesDTO
	 */
	public long insDirDespacho( DireccionesDTO direccion ) throws VteException {
		try {
			return empresasService.insDirDespacho( direccion );
			
		} catch (Exception ex) {
			logger.error("Problema BizDelegate", ex);
			throw new VteException(ex);
		}

	}
		
	/**
	 * Modifica dirección de despacho de una empresa
	 * 
	 * @param  direccion		DTO con datos de la dirección a modificar
	 * @throws SystemException
	 * @see DireccionesDTO
	 */
	public boolean modDirDespacho(DireccionesDTO direccion ) throws VteException {
		try {
			return empresasService.modDirDespacho( direccion );
			
		} catch (Exception ex) {
			logger.error("Problema BizDelegate", ex);
			throw new VteException(ex);
		}

	}
	
	/* 
	 * METODOS DE COMPRADORES
	 * 
	 */
	
	/**
	 * Datos del comprador
	 * 
	 * @param comprador_id	Identificador único del comprador a consultar
	 * @return				Lista de DTO
	 * @throws SystemException 
	 * @see CompradoresDTO
	 */
	public CompradoresDTO getCompradoresById(long comprador_id) throws VteException {

		try {
			return empresasService.getCompradoresById(comprador_id);
		} catch (Exception ex) {
			logger.error("Problemas con controles del comprador", ex);
			throw new VteException(ex);
		}	
	}

	/**
	 * Obtiene informacion de compradores segun el Rut
	 * 
	 * @param  rut Rut del comprador
	 * @return CompradoresDTO 
	 * @throws VteException
	 * @throws SystemException
	 */
	public CompradoresDTO getCompradoresByRut(long rut) throws VteException, SystemException{
		try{
			return empresasService.getCompradoresByRut(rut);
		}catch(ServiceException ex){
			throw new VteException(ex);
		}
	}
	
	/**
	 * Inserta un comprador en el repositorio
	 * 
	 * @param  comprador CompradorDTO con datos del comprador
	 * @throws VteException
	 */
	public long setCreaComprador(CompradoresDTO comprador) throws VteException {
		try {
			return empresasService.setCreaComprador( comprador );
			
		} catch (Exception ex) {
			logger.error("Problema BizDelegate", ex);
			throw new VteException(ex);
		}

	}

	/**
	 * Modifica los datos de un comprador
	 * 
	 * @param  comprador CompradoresDTO con datos del comprador
	 * @throws VteException
	 */
	public boolean setModComprador(CompradoresDTO comprador) throws VteException {
		try {
			return empresasService.setModComprador( comprador );
			
		} catch (Exception ex) {
			logger.error("Problema BizDelegate", ex);
			throw new VteException(ex);
		}

	}	
	
	/**
	 * Elimina un comprador
	 * 
	 * @param  comprador_id	Identificador único del comprador a eliminar
	 * @throws VteException
	 */
	public boolean delCompradores( long comprador_id ) throws VteException {
		try {
			return empresasService.delCompradores( comprador_id );
			
		} catch (Exception ex) {
			logger.error("Problema BizDelegate", ex);
			throw new VteException(ex);
		}

	}
	
	/**
	 * Listado de compradores por sucursal
	 * 
	 * @param  id_sucursal	Identificador único de la sucursal
	 * @return List				Lista de DTO
	 * @throws VteException 
	 * @see CompradoresDTO
	 */
	public List getListCompradoresBySucursalId(long id_sucursal) throws VteException {

		try {
			return empresasService.getListCompradoresBySucursalId(id_sucursal);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}	
	}	
	

	/**
	 * Listado de compradores por sucursal
	 * 
	 * @param  id_sucursal	Identificador único de la sucursal
	 * @return List				Lista de DTO
	 * @throws VteException 
	 * @see CompradoresDTO
	 */
	public List getListCompradoresBySucursalId(long id_sucursal, String TipoAcceso, long comprador_id) throws VteException {

		try {
			return empresasService.getListCompradoresBySucursalId(id_sucursal, TipoAcceso, comprador_id);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}	
	}	
	

	/*
	 * METODOS DE COTIZACIONES
	 * 
	 */
	/**
	 * Recupera las categorías que tiene productos para el cliente
	 * 
	 * @param cliente_id	Identificador único del cliente
	 * @return				Lista de DTO
	 * @throws SystemException
	 */
	public List getListCategoria(long cliente_id) throws VteException {

		try {
			return cotizacionesService.getListCategoria(cliente_id);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}	
	}	

	
	/*
	 * METODOS DE DIRECCIONES
	 * 
	 */
	/**
	 * Listado de tipos de calle activos
	 * 
	 * @return Listado con tipos de calles DTO
	 * @throws VteException
	 * @throws SystemException
	 */
	public List getTiposCalleAll() throws VteException, SystemException{
		try {
			return empresasService.getTiposCalleAll();
		}
		catch (ServiceException ex) {
			logger.error("Problemas con servicio de empresas", ex);
			throw new VteException(ex);
		}		
	}


	/**
	 * Lista de Comunas
	 * 
	 * @return List ComunaDTO
	 * @throws SystemException 
	 * 
	 */
	public List getComunas() throws VteException {
		try {
			return empresasService.getComunas();
		} catch (Exception ex) {
			logger.error("Problemas con controles del comprador", ex);
			throw new VteException(ex);
		}
	}

	/**
	 * 
	 * Agrega registro de mail para ser enviado al ejecutivo
	 * 
	 * @param mail DTO con información del mail
	 * @throws SystemException
	 */
	public boolean insMail(MailDTO mail) throws VteException {
		try {
			return empresasService.insMail(mail);			
		} catch (Exception ex) {
			logger.error("Problema BizDelegate (insMail)", ex);
			throw new VteException(ex);
		}
	}	

	
	/**
	 * Lista de tipos de calle
	 * 
	 * @return List TiposCallesDTO
	 * @throws SystemException
	 *
	public List getTiposCalle() throws SystemException {
		try {
			return Compradores_service.tiposCallesGetList();
		} catch (ServiceException ex) {
			logger.error("Problemas con controles del comprador", ex);
			throw new SystemException(ex);
		}
	}*/


	/**
	 * Elimina una direccion
	 * 
	 * @param  direccion_id	Identificador único de la direccion a eliminar
	 * @throws VteException
	 */
	public boolean delDirDespacho( long direccion_id ) throws VteException {
		try {
			return empresasService.delDirDespacho( direccion_id );
			
		} catch (Exception ex) {
			logger.error("Problema BizDelegate", ex);
			throw new VteException(ex);
		}
	}

	/**
	 * Datos de la direccion
	 * 
	 * @param direccion_id	Identificador único de la direccion a consultar
	 * @return				Lista de DTO
	 * @throws SystemException 
	 * @see CompradoresDTO
	 */
	public DireccionEntity getDireccionesDespByIdDesp(long direccion_id) throws VteException {

		try {
			return empresasService.getDireccionesDespByIdDesp(direccion_id);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la direccion", ex);
			throw new VteException(ex);
		}	
	}	
	

	/**
	 * Local al que pertenece la direccion
	 * 
	 * @param direccion_id	Identificador único de la direccion a consultar
	 * @return				Lista de DTO
	 * @throws SystemException 
	 * @see CompradoresDTO
	 */
	public long getLocalDireccion(long direccion_id) throws VteException {

		try {
			return empresasService.getLocalDireccion(direccion_id);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la direccion", ex);
			throw new VteException(ex);
		}	
	}	
	

	/**
	 * Listado de regiones
	 * 
	 * @return Lista con RegionesDTO
	 * @throws SystemException
	 */
	public List regionesGetAll() throws SystemException {
		List result = null;
		
		try {
			
			result = empresasService.getAllRegiones();
						
		} catch ( Exception ex ) {
			logger.error("Problemas con servicios de regiones", ex);
			throw new SystemException(ex);
		}
		
		return result;
		
	}	
	
	/**
	 * Recupera el listado de comunas para la región.
	 * 
	 * @param reg_id	Identificador único de la región
	 * @return Lista de ComunasDTO
	 * @throws SystemException
	 */
	public List regionesGetAllComunas( long reg_id ) throws SystemException {
		List result = null;
		
		try {
			
			result = empresasService.getAllComunas( reg_id );
						
		} catch ( Exception ex ) {
			logger.error("Problemas con servicios de regiones", ex);
			throw new SystemException(ex);
		}
		
		return result;
		
	}		

	/**
	 * Listado de direcciones de despacho por sucursal
	 * 
	 * @param  sucursal_id	Identificador único de la sucursal
	 * @return List			Lista de DTO
	 * @throws SystemException 
	 */
	public List getListDireccionDespBySucursal(long sucursal_id) throws VteException, SystemException {

		try {
			return  empresasService.getListDireccionDespBySucursal(sucursal_id);
		} catch (Exception ex) {
			logger.error("Problemas Bizdelegate (getListDireccionDespBySucursal)", ex);
			throw new VteException(ex);
		}	
	}	
	
	
	/*
	 * METODOS DE DIRECCIONES DE FACTURACION
	 * 
	 */
	/**
	
	/**
	 * Listado de direcciones de facturacion por sucursal
	 * 
	 * @param  sucursal_id	Identificador único de la sucursal
	 * @return List			Lista de DTO
	 * @throws SystemException 
	 */
	public List getListDireccionFactBySucursal(long sucursal_id) throws VteException, SystemException {

		try {
			return  empresasService.getListDireccionFactBySucursal(sucursal_id);
		} catch (Exception ex) {
			logger.error("Problemas Bizdelegate (getListDireccionFacBySucursal)", ex);
			throw new VteException(ex);
		}	
	}
	
	/**
	 * Elimina una direccion de faturacion
	 * 
	 * @param  direccion_id	Identificador único de la direccion de facturacion a eliminar
	 * @throws VteException
	 */
	public boolean delDirFacturacion( long direccion_id ) throws VteException {
		try {
			return empresasService.delDirFacturacion( direccion_id );
			
		} catch (Exception ex) {
			logger.error("Problema BizDelegate", ex);
			throw new VteException(ex);
		}
	}

	/**
	 * Agrega una dirección de facturación a la sucursal
	 * 
	 * @param  direccion		DTO con datos de la dirección de facturacion
	 * @return True: éxito False: error 
	 * @throws VteException
	 * @see DirFacturacionDTO
	 */
	public long insDirFacturacion( DirFacturacionDTO direccion ) throws VteException {
		try {
			return empresasService.insDirFacturacion( direccion );
			
		} catch (Exception ex) {
			logger.error("Problema BizDelegate", ex);
			throw new VteException(ex);
		}

	}
	
	/**
	 * Modifica dirección de facturación de una sucursal
	 * 
	 * @param  direccion		DTO con datos de la dirección a modificar
	 * @throws SystemException
	 * @see DirFacturacionDTO
	 */
	public boolean modDirFacturacion(DirFacturacionDTO direccion ) throws VteException {
		try {
			return empresasService.modDirFacturacion( direccion );
			
		} catch (Exception ex) {
			logger.error("Problema BizDelegate", ex);
			throw new VteException(ex);
		}

	}	

	
	/**
	 * Datos de la direccion de facturación por id de facturacion
	 * 
	 * @param direccion_id	Identificador único de la direccion de facturación a consultar
	 * @return				Lista de DTO
	 * @throws SystemException 
	 * @see DirFacturacionDTO
	 */
	
	public DirFacturacionDTO getDireccionesFactByIdFact(long direccion_id) throws VteException {

		try {
			return empresasService.getDireccionesFactByIdFact(direccion_id);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la direccion", ex);
			throw new VteException(ex);
		}	
	}
	
	
	/**
	 * Obtiene el listado de sucursales y empresas relacionadas a un comprador
	 * 
	 * @param  id_comprador	Identificador único del comprador
	 * @return List				Lista de DTO
	 * @throws VteException 
	 * @see CompradoresDTO
	 */
	public List getListSucursalesByCompradorId(long id_comprador) throws VteException {

		try {
			return empresasService.getListSucursalesByCompradorId(id_comprador);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}	
	}		
	
	/**
	 * Obtiene el listado de sucursales y empresas relacionadas a un usuario
	 * 
	 * @param  id_comprador	Identificador único del comprador
	 * @return List			Lista de DTO
	 * @throws VteException 
	 * @see CompradoresDTO
	 */
	public List getListSucursalesByUser(long id_comprador) throws VteException {

		try {
			return empresasService.getListSucursalesByUser(id_comprador);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}	
	}		

	/**
	 * Obtiene el listado de sucursales y empresas relacionadas a un usuario
	 * 
	 * @param  id_comprador	Identificador único del comprador
	 * @return String
	 * @throws VteException 
	 * @see CompradoresDTO
	 */
	public String getListSucursalesByUser2(long id_comprador) throws VteException {

		try {
			return empresasService.getListSucursalesByUser2(id_comprador);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}	
	}		
	
	
	/**
	 * Obtiene el listado de sucursales relacionadas a un comprador para las empresas que el administrador tiene acceso
	 * 
	 * @param  id_comprador	Identificador único del comprador
	 * @param  id_administrador Identificador único del administrador
	 * @return List				Lista de DTO
	 * @throws VteException 
	 * @see CompradoresDTO
	 */
	public List getListSucursalesByCompradorId(long id_comprador, long id_administrador) throws VteException {

		try {
			return empresasService.getListSucursalesByCompradorId(id_comprador, id_administrador);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}	
	}	

	/**
	 * Elimina a relacion sucursal/comprador
	 * 
	 * @param  dto ComprXSucDTO
	 * @return boolean
	 * @throws VteException
	 */
	public boolean delRelSucursalComprador(ComprXSucDTO dto) throws VteException {

		try {
			return empresasService.delRelSucursalComprador(dto);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}	
	} 	

	
	/**
	 * Obtiene el listado de sucursales que no se encuentran asociadas al comprador
	 * 
	 * @param  id_comprador	Identificador único del comprador
	 * @return List				Lista de DTO
	 * @throws VteException 
	 * @see SucursalesDTO
	 */
	public List getListSucursalesNoAsocComprador(long id_administrador, long id_comprador) throws VteException {

		try {
			return empresasService.getListSucursalesNoAsocComprador(id_administrador, id_comprador);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}	
	}
	
	/**
	 * Agrega la relacion entre sucursal y comprador
	 * 
	 * @param  dto ComprXSucDTO
	 * @return boolean
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public boolean addRelSucursalComprador(ComprXSucDTO dto)	throws VteException {
		
		try {
			return empresasService.addRelSucursalComprador(dto);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}			
	}

	/**
	 * Obtiene informacion del comprador, y el tipo de comprador segun id de sucursal
	 *
	 * @param  id_comprador: Identificador único del comprador
	 * @param  id_sucursal : Identificador único de la sucursal
	 * @return CompradoresDTO
	 * @throws VteException
	 */
	
	public CompradoresDTO getCompradorXSucursal(long id_comprador, long id_sucursal) throws VteException {
		
		try {
			return empresasService.getCompradorXSucursal(id_comprador, id_sucursal);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}			
	}	
	
	/**
	 * Obtiene el listado de Empresas en la cual el comprador es administrador
	 * 
	 * @param  id_comprador	Identificador único del comprador
	 * @return List  Lista con datos (EmpresasDTO)
	 * @throws CompradoresException, SystemException
	 */
	public List getListAdmEmpresasByCompradorId(long id_comprador) throws VteException {
		
		try {
			return empresasService.getListAdmEmpresasByCompradorId(id_comprador);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}			
	}	 	
	
	/**
	 * Obtiene el listado de sucursales para las empresas a las cuales el comprador es administrador.
	 * 
	 * @param id_comprador	Identificador único del comprador
	 * @return Lista con datos (SucursalesDTO)
	 * @throws CompradoresException, SystemException
	 */	
	public List getListAdmSucursalesByCompradorId(long id_comprador) throws VteException {
		
		try {
			return empresasService.getListAdmSucursalesByCompradorId(id_comprador);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}			
	}
	
	/**
	 * Obtiene el listado de compradores relacionados a una empresa, segun el id de la empresa.
	 * 
	 * @param  id_empresa Identificador de la empresa
	 * @return List CompradoresEntity
	 * @throws VteException
	 */	
	public List getListCompradoresByEmpresalId(long id_empresa) throws VteException {
		
		try {
			return empresasService.getListCompradoresByEmpresalId(id_empresa);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}			
	}	
	
	/**
	 * Retorna las cotizaciones de acuerdo a un criterio determinado
	 * @param criterio
	 * @author BBRI
	 * @return lista MonitorCotizacionesDTO
	 * @throws CotizacionesDAOException
	 */
	public List getCotizacionesByCriteria(CotizacionesCriteriaDTO criterio)	throws VteException {
		
		try {
			return cotizacionesService.getCotizacionesByCriteria(criterio);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}	
	}
	
	/**
	 * Obtiene un listado con los estados de las cotizaciones
	  * @return Listado con estados de la cotizacion DTO
	 * @throws VteException
	 */
	public List getEstadosCotizacion() throws VteException {
		
		try {
			return cotizacionesService.getEstadosCotizaciones();
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}		
	}
	
	/**
	 * Retorna el detalle de la cotización segun Id
	 * @param cot_id
	 * @return CotizacionesDTO
	 * @throws VteException
	 */
	
	public CotizacionesDTO getCotizacionesById(long cot_id)  throws VteException 	{
		try {
			return cotizacionesService.getCotizacionesById(cot_id);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}			
	}
	
	
	public String getEstadoSaldoEmpresaByCotizacion(long cot_id)  throws VteException 	{
		try {
			return cotizacionesService.getEstadoSaldoEmpresaByCotizacion(cot_id);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}			
	}
	
	
	public long getDireccionByCotizacion(long cot_id)  throws VteException 	{
		try {
			return cotizacionesService.getDireccionByCotizacion(cot_id);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}			
	}
	

	/**
	 * Retorna una lista con los productos asociados a una cotización
	 * @param id_cot Identificador único de la cotización
	 * @return List ProductosCotizacionesDTO
	 * @throws VteException
	 */
	public List getProductosCotiz(long id_cot)	throws ServiceException 	{
		
		try {
			return cotizacionesService.getProductosCotiz(id_cot);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new ServiceException(ex);
		}			
	}
	
	/**
	 * 
	 * Obtiene el listado de compradores para las sucursales que el administrador tiene acceso.
	 * 
	 * @param id_administrador Identificador único del administrador
	 * @return Listado con datos de los compradores
	 * @throws VteException
	 */
	public List getListAdmCompradoresByAdministradorId(long id_administrador) throws VteException {
		
		try {
			return empresasService.getListAdmCompradoresByAdministradorId(id_administrador);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}			
	}	
	
	/**
	 * Caducar las cotizaciones vencidas
	 * 
	 * @param id_comprador Identificador único del comprador
	 * @return True: éxito False: error
	 * @throws VteException
	 */
	public boolean CaducarCotizacion( long id_comprador ) throws VteException {
		try {
			return cotizacionesService.CaducarCotizaciones( id_comprador );
		} catch (Exception e) {
			logger.error("Problemas con controles de la Empresa", e);
			throw new VteException(e);
		}
	}
	
	/**
	 * Inserta datos de solicitud de registro
	 * 
	 * @param  dto SolRegDTO Contiene la información necesaria para la solicitud de registro
	 * @return True: éxito False: error
	 * @throws VteException
	 */
	public long insSolReg(SolRegDTO dto) throws VteException {
		try {
			return empresasService.insSolReg(dto);
		} catch (Exception e) {
			logger.error("Problemas con controles de la Empresa", e);
			throw new VteException(e);
		}
	}
	
	/**
	 * Inserta  mail a ejecutivo y datos para solicitud de registro
	 * @param solreg : DTO con los datos del formulario  solicitud  de registro 
	 * @return Id: Numero de solicitud de registro
	 * @throws VteException
	 */
	
	public long setMailSolRegistro(SolRegDTO solreg)throws VteException {
		try {
			return empresasService.setMailSolRegistro(solreg);
		} catch (Exception e) {
			logger.error("Problemas Bizdelegate (setMailSolRegistro)", e);
			throw new VteException(e);
		}
		
	}

	
	/**
	 * Listado de direcciones de despacho por comprador
	 * 
	 * @param  comprador_id	Identificador único del comprador
	 * @return List			Lista de DTO
	 * @throws VteException, SystemException
	 */
	public List getListDireccionDespByComprador(long comprador_id) throws VteException, SystemException {

		try {
			return  empresasService.getListDireccionDespByComprador(comprador_id);
		} catch (Exception ex) {
			logger.error("Problemas Bizdelegate (getListDireccionDespByCmprador)", ex);
			throw new VteException(ex);
		}	
	}	
	
	/**
	 * Listado de direcciones de facturación por comprador
	 * 
	 * @param  comprador_id	Identificador único del comprador
	 * @return List			Lista de DTO
	 * @throws VteException, SystemException
	 */
	public List getListDireccionFactByComprador(long comprador_id) throws VteException, SystemException {

		try {
			return  empresasService.getListDireccionFactByComprador(comprador_id);
		} catch (Exception ex) {
			logger.error("Problemas Bizdelegate (getListDireccionFactByComprador)", ex);
			throw new VteException(ex);
		}	
	}	
	
	/**
	 * Obtiene las categorías y subcategorias
	 * 
	 * @return Listado de categorías
	 * @throws VteException
	 */
	public List getCategoriasList() throws VteException {
		try {
			return cotizacionesService.getCategoriasList();
		} catch (Exception e) {
			logger.error("Problemas con controles de la Cotización", e);
			throw new VteException(e);
		}
		
	}	
	
	/**
	 * Recupera información de la categoría.
	 * 
	 * @param id_categoria Identificador único de la categoría
	 * @return DTO con datos de la categoría
	 * @throws VteException
	 */
	public CategoriaDTO getCategoriaById(long id_categoria) throws VteException {
		try {
			return cotizacionesService.getCategoriaById( id_categoria );
		} catch (Exception e) {
			logger.error("Problemas con controles de la Cotización", e);
			throw new VteException(e);
		}
		
	}	 
	
	/**
	 * Recupera todas las marcas diferentes de los productos para una categoría.  
	 * 
	 * @param id_categoria Identificador único de la categoría
	 * @return Listado de marcas
	 * @throws VteException
	 */
	public List getListMarcasByCategoria(long id_categoria) throws VteException {
		try {
			return cotizacionesService.getListMarcasByCategoria( id_categoria );
		} catch (Exception e) {
			logger.error("Problemas con controles de la Cotización", e);
			throw new VteException(e);
		}
		
	}	 	

	/**
	 * Recupera los productos para una categoría.
	 * 
	 * Debe considerar los criterios de búsquedas.
	 * 
	 * @param criterio DTO con información de los diferetentes datos para filtrar y ordenar el listado de productos
	 * @return Listado de productos
	 * @throws VteException
	 */
	public List getListProductosByCriteria(ProductosCriteriaDTO criterio) throws VteException {
		try {
			return cotizacionesService.getListProductosByCriteria( criterio );
		} catch (Exception e) {
			logger.error("Problemas con controles de la Cotización", e);
			throw new VteException(e);
		}
		
	}	 

	/**
	 * Inserta un nuevo encabezado de cotización sin detalle de productos
	 * 
	 * @param dto ProcInsCotizacionDTO Datos de la cotización
	 * @return Identificador único de la cotización insertada
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public long doInsCotizacionHeader( ProcInsCotizacionDTO dto )  throws VteException {
		try {
			return cotizacionesService.doInsCotizacionHeader( dto );
		} catch (Exception e) {
			logger.error("Problemas con controles de la Cotización", e);
			throw new VteException(e);
		}
		
	}

	/**
	 * Inserta una lista de detalles (productos) a una cotización. 
	 * @param lst_det Lista con DTO de los detalles
	 * @throws VteException
	 */
	public void doInsCotizacionDetail( List lst_det ) throws VteException {
		try {
			cotizacionesService.doInsCotizacionDetail( lst_det );
		} catch (Exception e) {
			logger.error("Problemas con controles de la Cotización", e);
			throw new VteException(e);
		}
		
	}	 

	/**
	 * Elimina un producto del carro de compra 
	 * 
	 * @param  id	Identificador único del producto
	 * @throws VteException
	 */
	public boolean delProductoCotizacion( long id ) throws VteException {
		try {
			return cotizacionesService.delProductoCotizacion( id );
			
		} catch (Exception ex) {
			logger.error("Problema BizDelegate", ex);
			throw new VteException(ex);
		}

	}
	
	/**
	 * Permite modificar datos(cantidad) de un producto asociado a una cotizacion 
	 * 
	 * @param  prod	ModProdCotizacionesDTO Datos del producto 
	 * @throws VteException
	 */
	public boolean updCantProductoCotizacion( ModProdCotizacionesDTO prod ) throws VteException {
		try {
			return cotizacionesService.updCantProductoCotizacion( prod );
			
		} catch (Exception ex) {
			logger.error("Problema BizDelegate", ex);
			throw new VteException(ex);
		}

	}	
	
	
	/**
	 * Retorna una lista con los productos asociados a una cotización
	 * @param id_cot Identificador único de la cotización
	 * @return List ProductosCotizacionesDTO
	 * @throws VteException
	 */
	public List getProductosDetCotiz(long id_cot)	throws ServiceException 	{
		
		try {
			return cotizacionesService.getProductosDetCotiz(id_cot);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new ServiceException(ex);
		}			
	}	
	
	/**
	 * Permite agregar al encabezado (estado, observacion) y al detalle de la cotizacion (descripcion, precio unitario, cantidad)
	 * @param id_cot, comentario
	 * @return List ProductosCotizacionesDTO
	 * @throws VteException
	 */
	public boolean SendCotizacion(CotizacionesDTO cotizacion)	throws ServiceException 	{
		
		try {
			return cotizacionesService.SendCotizacion( cotizacion );
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new ServiceException(ex);
		}			
	}
	
	/**
	 * Obtiene el listado de Empresas en la cual el comprador puede comprar
	 * 
	 * @param  id_comprador	Identificador único del comprador
	 * @return List  Lista con datos (EmpresasDTO)
	 * @throws CompradoresException, SystemException
	 */
	public List getListEmpresasByCompradorId(long id_comprador) throws ServiceException 	{
		
		try {
			return empresasService.getListEmpresasByCompradorId( id_comprador );
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new ServiceException(ex);
		}			
	}	

	
	/**
	 * Obtiene el listado de Empresas en la cual el comprador puede comprar
	 * 
	 * @param  id_comprador	Identificador único del comprador
	 * @return List  Lista con datos (EmpresasDTO)
	 * @throws CompradoresException, SystemException
	 */
	public List getListEmpresasByUser(long id_comprador) throws ServiceException 	{
		
		try {
			return empresasService.getListEmpresasByUser( id_comprador );
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new ServiceException(ex);
		}			
	}	
	
	
	/**
	 * Permite anular una cotizacion
	 * @param id_cot Identificador único de la cotización
	 * @return True: éxito False: error
	 * @throws VteException
	 */
	public boolean setAnularCotizacion(long id_cot)	throws ServiceException 	{
		
		try {
			return cotizacionesService.setAnularCotizacion(id_cot);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new ServiceException(ex);
		}			
	}
	
	/**
	 * Recupera categorías y marcas para la búsqueda
	 *  
	 * @param patron		Patrón de búsqueda
	 * @param local_id		Identificador único del local
	 * @param criterio		criterio de busqueda
	 * @return				Lista de DTO con datos de las categorías
	 * @throws SystemException
	 */
	public List getSearch( List patron, long local_id, long criterio ) throws ServiceException 	{
		List result = null;
		try {

			result = cotizacionesService.getSearch( patron, local_id, criterio );
			
		} catch (Exception ex) {
			logger.error("Problemas Bizdelegate (getSearch)", ex);
			throw new ServiceException(ex);
		}
		return result;
	}	
		
	/**
	 * Obtiene los datos del ejecutivo fono empresa
	 * @param  login Login del usuario
	 * @return UserDTO
	 * @throws CompradoresDAOException
	 */	
	public UserDTO getEjecutivoGetByRut(String login )  throws VteException, SystemException	{
		
		try {
			return empresasService.getEjecutivoGetByRut(login);
		} catch (ServiceException ex) {
			logger.error("Problemas con controles de Empresas", ex);
			throw new VteException(ex);
		}			
	}
	
	/**
	 * Cambia a estado ACEPTADA la cotizacion y graba la informacion del resumen
	 * @param dto ProcInsCotizacionDTO
	 * @return True: éxito False: error
	 * @throws SystemException
	 */
	public boolean setAceptarCotizacion(ProcInsCotizacionDTO dto)	throws ServiceException 	{
		
		try {
			return cotizacionesService.setAceptarCotizacion(dto);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new ServiceException(ex);
		}			
	}
	
	
	/**
	 * Obtiene el listado de sucursales que pertenecen a una empresa, segun el id de la empresa
	 * 
	 * @param id_empresa Identificafot único de la empresa
	 * @return List SucursalesDTO
	 * @throws SucursalesException
	 * @throws SystemException
	 */
	public List getSucursalesByEmpresaId(long id_empresa) throws VteException, SystemException	{
		
		try {
			return empresasService.getSucursalesByEmpresaId(id_empresa);
		} catch (ServiceException ex) {
			logger.error("Problemas con controles de Empresas", ex);
			throw new VteException(ex);
		}			
	}	
	
	
	
	/** Realiza un cambio de estado al campo cli_estado a 'E' 
	 * @param prm : SucursalesDTO Contiene la información necesaria de la sucursal
	 * @return : True o False
	 * @throws SucursalesException
	 * @throws SystemException
	 */
	public boolean delSucursal(SucursalesDTO prm) throws VteException, SystemException	{
		
		try {
			return empresasService.delSucursal(prm);
		} catch (ServiceException ex) {
			logger.error("Problemas con controles de Empresas", ex);
			throw new VteException(ex);
		}			
	}	
	
	
	/** Recupera la informacion de la sucursal  
	 * @param id : Identificador unico de la sucursal
	 * @return
	 * @throws VteException
	 * @throws SystemException
	 */
	public SucursalesDTO getSucursalById(long id)	throws VteException, SystemException	{
		
		try {
			return empresasService.getSucursalById(id);
		} catch (ServiceException ex) {
			logger.error("Problemas con controles de Empresas", ex);
			throw new VteException(ex);
		}			
	}	

	/**
	 * Agrega una sucursal
	 * 
	 * @param  dto SucursalesDTO Contiene la información necesaria de la sucursal
	 * @return boolean
	 * @throws VteException
	 */
	public long setCreaSucursal(SucursalesDTO dto)	throws VteException {
		
		try {
			return empresasService.setCreaSucursal(dto);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}			
	}	
	
	
	/**
	 * Permite actualizar los datos de la sucursal, segun las modificaciones ingresadas en el formulario de detalle
	 *  
	 * @param  prm SucursalesDTO, contiene la información del formulario
	 * @return boolean,
	 * @throws VteException
	 */
	public boolean setModSucursal(SucursalesDTO dto)	throws VteException {
		
		try {
			return empresasService.setModSucursal(dto);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}			
	}
	
	/**
	 * Obtiene un listado con los estados de las sucursales
	  * @return Listado con estados de la cotizacion DTO
	 * @throws VteException
	 */
	public List getEstadosSucursales(String tipo) throws VteException {
		
		try {
			return empresasService.getEstadosByTipo(tipo);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}		
	}
	
	/**
	 * Retorna las sucursales de acuerdo a un criterio determinado
	 * @param criterio SucursalCriteriaDTO
	 * @author BBRI
	 * @return lista VteException
	 */
	public List getSucursalesByCriteria(SucursalCriteriaDTO criterio)	throws VteException {
		
		try {
			return empresasService.getSucursalesByCriteria(criterio);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}	
	}	
	
	/**
	 * Recupera el monto de los pedidos por cobrar, para rectificar el saldo de la línea de crédito.
	 * 
	 * @param id_empresa Identificador único de la empresa
	 * @return			 Monto por cobrar
	 * @throws VteException
	 */
	public double getSaldoActualPendiente(long id_empresa) throws VteException {
		
		try {
			return empresasService.getSaldoActualPendiente(id_empresa);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}	
	}	
	
	/**
	 * Elimina la relacion entre empresa y comprador, donde el comprador era administrador
	 * 
	 * @param dto
	 * @return true, si agrego con exito, false en caso contrario
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public boolean delRelEmpresaComprador(ComprXEmpDTO dto) throws VteException {
		
		try {
			return empresasService.delRelEmpresaComprador(dto);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}		
	}

	/**
	 * Agrega la relacion entre empresa y comprador, donde el comprador sera administrador
	 *  
	 * @param dto
	 * @return true, si agrego con exito, false en caso contrario
	 * @throws CompradoresException
	 * @throws SystemException
	 */
	public boolean addRelEmpresaComprador(ComprXEmpDTO dto) throws VteException {
		
		try {
			return empresasService.addRelEmpresaComprador(dto);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}		
	}	
	


	/**
	 * Modifica los datos password - estado de un comprador
	 * 
	 * @param  comprador CompradoresDTO con datos del comprador
	 * @throws VteException
	 */
	public boolean compradorChangePass(CompradoresDTO comprador) throws VteException {
		try {
			return empresasService.compradorChangePass( comprador );
			
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}

	}		
	
	/**
	 * Modifica la cantidad de intentos de logeo de un comprador
	 * 
	 * @param comprador_id 	Identificador único del comprador
	 * @param accion		Acción a gatillar 1: Aumentar, 0: Reset
	 * @throws VteException 
	 */
	public boolean updateIntentos(long comprador_id, long accion) throws VteException {

		try {
			return empresasService.updateIntentos(comprador_id, accion);
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}		

	}
	
	/**
	 * Ejecucion del Wizard ingresa un comprador, sucursales y empresas
	 * 
	 * @param  comprador CompradoresDTO con datos del comprador
	 * @param  registros_suc Lista con datos de la sucursal
	 * @param  registros_emp Lista DTO con datos de la empresa
	 * @throws VteException
	 */
	public boolean setExecWizard(CompradoresDTO comprador, List registros_suc, List registros_emp) throws VteException {
		try {
			return empresasService.setExecWizard( comprador, registros_suc, registros_emp );
			
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}

	}		
	
	/**
	 * Recupera la lista de Regiones y la retorna como una lista de DTO (RegionesDTO).
	 * 
	 * @return	Lista de DTO (RegionesDTO)
	 * @throws RegionesException
	 */
	public List getRegiones() throws  VteException {
		try {
			return empresasService.getAllRegiones();
			
		} catch (Exception ex) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}
	}
	
	/**
	 * Listado general de comunas
	 * 
	 * @return Lista de ComunasDTO
	 * @throws SystemException
	 */
	public List getComunasGeneral( ) throws  VteException {
		List result = null;
		
		try {
			
			result = empresasService.getComunasGeneral(  );
						
		} catch ( Exception ex ) {
			logger.error("Problemas con controles de la Empresa", ex);
			throw new VteException(ex);
		}
		
		return result;
		
	}

    /**
     * @param idComprador
     * @return
     */
    public List getPedidosPorPagar(long idComprador) throws SystemException {
        try {
            return pedidosService.getPedidosPorPagar(idComprador);
        } catch (Exception ex) {
            logger.error("Problema en pedidosService", ex);
            throw new SystemException(ex);
        }          
    }	
    
    public PedidoDTO getPedidoById(long idPedido) throws SystemException {
        try {
            return pedidosService.getPedido(idPedido);
        } catch (Exception ex) {
            logger.error("Problema en pedidosService (getPedidoById)", ex);
            throw new SystemException(ex);
        } 
    }
    
    public LocalDTO getLocalRetiro(long idLocal) throws SystemException {
        try {
            return pedidosService.getLocalRetiro(idLocal);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (getLocalRetiro)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public List resumenCompraGetCategoriasProductos(long idPedido) throws SystemException {
        try {
            return clientesService.resumenCompraGetCategoriasProductos(idPedido);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (resumenCompraGetCategoriasProductos)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idPedido
     */
    public void actualizaSecuenciaPago(long idPedido) throws SystemException {
        try {
            pedidosService.actualizaSecuenciaPago(idPedido);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (getLocalRetiro)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param webpayDTO
     */
    public void webpaySave(WebpayDTO webpayDTO) throws SystemException {
        try {
            pedidosService.webpaySave(webpayDTO);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (webpaySave)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public int webpayMonto(int idPedido) throws SystemException {
        try {
            return pedidosService.webpayMonto(idPedido);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (webpayMonto)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public int webpayGetEstado(int idPedido) throws SystemException {
        try {
            return pedidosService.webpayGetEstado(idPedido);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (webpayGetEstado)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public WebpayDTO webpayGetPedido(long idPedido) throws SystemException {
        try {
            return pedidosService.webpayGetPedido(idPedido);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (webpayGetPedido)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idPedido
     */
    public void ingresarPedidoVteASistema(long idPedido) throws SystemException {
        try {
            pedidosService.ingresarPedidoVteASistema(idPedido);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (ingresarPedidoVteASistema)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param mail
     */
    public void addMail(cl.bbr.jumbocl.clientes.dto.MailDTO mail) throws SystemException {
        try {
            clientesService.addMail(mail);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (resumenCompraGetCategoriasProductos)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public BotonPagoDTO botonPagoGetByPedido(long idPedido) throws SystemException {
        try {
            return pedidosService.botonPagoGetByPedido(idPedido);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (botonPagoGetByPedido)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param bp
     */
    public void updateNotificacionBotonPago(BotonPagoDTO bp) throws SystemException {
        try {
            pedidosService.updateNotificacionBotonPago(bp);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (updateNotificacionBotonPago)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public List getProductosSolicitadosById(long idPedido) throws SystemException {
        try {
            return pedidosService.getProductosSolicitadosById(idPedido);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (getProductosSolicitadosById)", ex);
            throw new SystemException(ex);
        }
    }
    
}

