package cl.bbr.boc.bizdelegate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import cl.bbr.boc.dto.BOPreciosLocalesDTO;
import cl.bbr.boc.dto.DestacadoDTO;
import cl.bbr.boc.dto.UmbralDTO;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.boc.service.CambiaEstadoValidadoService;
import cl.bbr.boc.service.CargaSapMasivaService;
import cl.bbr.boc.service.CargarColaboradoresService;
import cl.bbr.boc.service.CatalogacionMasivaService;
import cl.bbr.boc.service.CriterioSustitucionService;
import cl.bbr.boc.service.CuponesDCService;
import cl.bbr.boc.service.DestacadosService;
import cl.bbr.boc.service.ParametrosEditablesDCService;
import cl.bbr.boc.service.ProductosService;
import cl.bbr.boc.service.SeccionesExcluidasDCService;
import cl.bbr.boc.service.StockOnLineService;
import cl.bbr.boc.service.UmbralesService;
import cl.bbr.jumbocl.bolsas.dto.BolsaDTO;
import cl.bbr.jumbocl.bolsas.service.BolsasService;
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.CasosCriterioDTO;
import cl.bbr.jumbocl.casos.dto.LogCasosDTO;
import cl.bbr.jumbocl.casos.dto.PedidoCasoDTO;
import cl.bbr.jumbocl.casos.dto.ProductoCasoDTO;
import cl.bbr.jumbocl.casos.dto.QuiebreCasoDTO;
import cl.bbr.jumbocl.casos.service.CasosService;
import cl.bbr.jumbocl.clientes.dto.ClienteCriteriaDTO;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.clientes.dto.ProcModAddrBookDTO;
import cl.bbr.jumbocl.clientes.dto.ZonaDTO;
import cl.bbr.jumbocl.clientes.service.ClientesService;
import cl.bbr.jumbocl.common.dto.MailDTO;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.CuponEntity;
import cl.bbr.jumbocl.common.model.CuponPorProducto;
import cl.bbr.jumbocl.common.model.CuponPorRut;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCampanaDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCatWebDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCategoryProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddElementoDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelAllProductCategory;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelCampanaDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelCatWebDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelCategoryProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelElementoDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelGenericProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModCampElementoDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModCampanaDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModCatWebDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModElementoDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModFichaTecnicaDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModGenericItemDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModMPVProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModSubCatWebDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModSugProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcPubDespProductDTO;
import cl.bbr.jumbocl.contenidos.dto.CampanaDTO;
import cl.bbr.jumbocl.contenidos.dto.CampanasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.CategoriaSapDTO;
import cl.bbr.jumbocl.contenidos.dto.CategoriasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.CategoriasDTO;
import cl.bbr.jumbocl.contenidos.dto.ElementoDTO;
import cl.bbr.jumbocl.contenidos.dto.ElementosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.ItemFichaProductoDTO;
import cl.bbr.jumbocl.contenidos.dto.MarcasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.MarcasDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoCategDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoLogDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoSugerDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosSapCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosSapDTO;
import cl.bbr.jumbocl.contenidos.service.ContenidosService;
import cl.bbr.jumbocl.eventos.dto.EventoDTO;
import cl.bbr.jumbocl.eventos.dto.EventosCriterioDTO;
import cl.bbr.jumbocl.eventos.service.EventosService;
import cl.bbr.jumbocl.informes.dto.MailSustitutosFaltantesDTO;
import cl.bbr.jumbocl.informes.service.InformesService;
import cl.bbr.jumbocl.pedidos.collaboration.ProcInsPedidoDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModFacturaDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModPedidoIndicDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModPedidoPolSustDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModPedidoProdDTO;
import cl.bbr.jumbocl.pedidos.dto.AsignaOPDTO;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.CalendarioDespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.CambEnPagoOPDTO;
import cl.bbr.jumbocl.pedidos.dto.CriterioCarruselDTO;
import cl.bbr.jumbocl.pedidos.dto.CuponPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.DatosMedioPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.DespachoCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.EmpresaTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.pedidos.dto.JorDespachoCalDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.ListaGrupoDTO;
import cl.bbr.jumbocl.pedidos.dto.ListaTipoGrupoDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.LogRutaDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoExtDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidosCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.PilaProductoDTO;
import cl.bbr.jumbocl.pedidos.dto.PoligonoDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductoCarruselDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaCriterioDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.pedidos.dto.SustitutoDTO;
import cl.bbr.jumbocl.pedidos.dto.UltimasComprasDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.jumbocl.pedidos.exceptions.BolException;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosException;
import cl.bbr.jumbocl.pedidos.promos.dto.PromocionDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PromocionesCriteriaDTO;
import cl.bbr.jumbocl.pedidos.service.PedidosService;
import cl.bbr.jumbocl.usuarios.dto.ComandoDTO;
import cl.bbr.jumbocl.usuarios.dto.PerfilDTO;
import cl.bbr.jumbocl.usuarios.dto.PerfilesCriteriaDTO;
import cl.bbr.jumbocl.usuarios.dto.ProcModPerfilDTO;
import cl.bbr.jumbocl.usuarios.dto.ProcModUserDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.jumbocl.usuarios.dto.UsuariosCriteriaDTO;
import cl.bbr.jumbocl.usuarios.service.UsuariosService;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;
import cl.bbr.vte.cotizaciones.dto.AsignaCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesCriteriaDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.LogsCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ModCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.ModProdCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCotizacionesDTO;
import cl.bbr.vte.cotizaciones.service.CotizacionesService;
import cl.bbr.vte.empresas.dto.ComprXEmpDTO;
import cl.bbr.vte.empresas.dto.ComprXSucDTO;
import cl.bbr.vte.empresas.dto.CompradorCriteriaDTO;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;
import cl.bbr.vte.empresas.dto.EmpresaCriteriaDTO;
import cl.bbr.vte.empresas.dto.EmpresaLogDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.SucursalCriteriaDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;
import cl.bbr.vte.empresas.service.EmpresasService;

/**
 * Clase que contiene todos los métodos de: - Usuarios - Pedidos - Clientes -
 * Direccion - Categorias - Productos - Productos Sap - Jornadas y Calendarios
 * de Despacho - Comunas y locales. - Campañas y Elementos. - Umbrales Indra
 * 
 */
public class BizDelegate {

	/**
	 * ContenidosService atributo que permite trabajar con con los datos de: -
	 * Estados - Categorias - Productos - productos Sap
	 */
	private static ContenidosService contFacade;

	/**
	 * PedidosService atributo que permite trabajar con los datos de: - Pedido -
	 * Jornada - calendarios de Picking - calendarios de Despacho - Rondas -
	 * Despachos - Local - Comunas
	 */
	private static PedidosService pedidosBolService;

	/**
	 * UsuariosService atributo que permite trabajar con los datos de: -
	 * Usuarios - Perfiles
	 */
	private static UsuariosService usuariosService;

	/**
	 * ClientesService atributo que permite trabajar con los datos de: -
	 * Clientes - Direccion
	 */
	private static ClientesService clientesService;

	private static EmpresasService empresasService;

	private static CotizacionesService cotizacionesService;

	private static CasosService casosService;

	private static EventosService eventosService;

	private static InformesService informesService;

	private static DestacadosService destacadosService;

	private static BolsasService bolsasService;

	private static ProductosService productosService;

	private static CriterioSustitucionService criterioSustitucionService;

	private static CambiaEstadoValidadoService cambiaEstadoValidadoService;

	/** umbrales Indra **/
	private static UmbralesService umbralService;
	private static CargarColaboradoresService cargarColaboradoresService;
	private static ParametrosEditablesDCService parametrosEditablesService;
	private static SeccionesExcluidasDCService seccionesExcluidasDCService;
	private static CuponesDCService cuponesDCService;
	private static StockOnLineService stockOnLineService;
	private static CatalogacionMasivaService catalogacionMasivaService;
	private static CargaSapMasivaService cargaSapMasivaService;
	

	public BizDelegate() {

		if (contFacade == null)
			contFacade = new ContenidosService();
		// INDRA
		if (umbralService == null)
			umbralService = new UmbralesService();
		// INDRA
		if (pedidosBolService == null)
			pedidosBolService = new PedidosService();
		if (usuariosService == null)
			usuariosService = new UsuariosService();
		if (clientesService == null)
			clientesService = new ClientesService();
		if (empresasService == null)
			empresasService = new EmpresasService();
		if (cotizacionesService == null)
			cotizacionesService = new CotizacionesService();
		if (casosService == null)
			casosService = new CasosService();
		if (eventosService == null)
			eventosService = new EventosService();
		if (informesService == null)
			informesService = new InformesService();
		if (destacadosService == null)
			destacadosService = new DestacadosService();
		if (bolsasService == null)
			bolsasService = new BolsasService();
		if (productosService == null)
			productosService = new ProductosService();
		/** UmbralesService indra **/
		if (umbralService == null)
			umbralService = new UmbralesService();
		// indra
		if (cargarColaboradoresService == null)
			cargarColaboradoresService = new CargarColaboradoresService();

		if (parametrosEditablesService == null)
			parametrosEditablesService = new ParametrosEditablesDCService();

		if (criterioSustitucionService == null)
			criterioSustitucionService = new CriterioSustitucionService();

		if (cambiaEstadoValidadoService == null)
			cambiaEstadoValidadoService = new CambiaEstadoValidadoService();

		if (seccionesExcluidasDCService == null)
			seccionesExcluidasDCService = new SeccionesExcluidasDCService();

		if (cuponesDCService == null)
			cuponesDCService = new CuponesDCService();
		
		//hs agrego servicio
		if (stockOnLineService == null)
			stockOnLineService = new StockOnLineService();		
		
		if (catalogacionMasivaService == null)
			catalogacionMasivaService = new CatalogacionMasivaService();	
		
		if (cargaSapMasivaService == null)
			cargaSapMasivaService = new CargaSapMasivaService();	
		//hs agrego servicio

	}

	
	
	// -----------------------StockOnLine--------------------------------


	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws BocException
	 */
	public List getListaProductosPorLocal( long idLocal ) throws BocException {
			
		try {
				
			return stockOnLineService.getListaProductosPorLocal( idLocal );
			
		} catch ( Exception ex ) { 
				
			throw new BocException( ex );
			
		}

	}
		
	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws BocException
	 */
	public long[] cantidadDeProductosTendranCambios( long idLocal ) throws BocException {
	
		try {
			
			return stockOnLineService.cantidadDeProductosTendranCambios( idLocal );
			
		} catch ( Exception ex ) { 
		
			throw new BocException( ex );
		
		}
	
	}
	
	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws BocException
	 */
	public long[] cantidadDeProductosActualmente( long idLocal ) throws BocException {
	
		try {
			
			return stockOnLineService.cantidadDeProductosActualmente( idLocal );
			
		} catch ( Exception ex ) { 
		
			throw new BocException( ex );
		
		}
	
	}
	
	
	public int getTotalMaestra( long idLocal ) throws BocException {
		
		try {
			
			return stockOnLineService.getTotalMaestra( idLocal );
			
		} catch ( Exception ex ) { 
		
			throw new BocException( ex );
		
		}
	
	}
		

		/**
		 * 
		 * @param idLocal
		 * @return
		 * @throws BocException
		 */
		public List getDetalleSemiautomatico( long idLocal ) throws BocException {
			
			List listaDetalleSemiautomatico = new ArrayList();
			
			try {
				
				listaDetalleSemiautomatico = stockOnLineService.getDetalleSemiautomatico( idLocal );
				
			} catch ( Exception ex ) {
				
				throw new BocException( ex );
			
			}
			
			return listaDetalleSemiautomatico;
		
		}


		/**
		 * 
		 * @param idLocal
		 * @return
		 * @throws BocException
		 */
		public List getProductosDetalleSemiautomatico( long idLocal ) throws BocException {
			
			List listaDetalleSemiautomatico = new ArrayList();
			
			try {
				
				listaDetalleSemiautomatico = stockOnLineService.getProductosDetalleSemiautomatico( idLocal );
				
			} catch ( Exception ex ) {
				
				throw new BocException( ex );
			
			}
			
			return listaDetalleSemiautomatico;
		
		}
		


	// -----------------------Usuarios--------------------------------
	/**
	 * Obtiene el listado de estados, segun el tipo de estado.
	 * 
	 * @param tipo
	 *            String
	 * @return EstadoDTO List
	 * @throws BocException
	 */
	public List getEstadosAll(String tipo) throws BocException {
		try {
			return contFacade.getEstadosAll(tipo);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	/**
	 * Obtiene el listado de estados, segun el tipo de estado y si es visible en
	 * Web.
	 * 
	 * @param tipo
	 *            String
	 * @param visible
	 *            String
	 * @return EstadoDTO List
	 * @throws BocException
	 */
	public List getEstadosByVis(String tipo, String visible)
			throws BocException {
		try {
			return contFacade.getEstadosByVis(tipo, visible);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	// -----------------------Usuarios--------------------------------

	/**
	 * Obtiene los datos de un usuario en particular según su id de usuario
	 * 
	 * @param id_user
	 *            long
	 * @return UserDTO
	 * @throws BocException
	 * @throws SystemException
	 */
	public UserDTO getUserById(long id_user) throws BocException,
			SystemException {
		try {
			return usuariosService.getUserById(id_user);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new BocException(e);
		}

	}

	/**
	 * Obtiene los datos de un usuario en particular según un login determinado
	 * 
	 * @param login
	 *            String
	 * @return UserDTO
	 * @throws BocException
	 * @throws SystemException
	 */
	public UserDTO getUserByLogin(String login) throws BocException,
			SystemException {

		try {
			return usuariosService.getUserByLogin(login);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	/**
	 * Obtiene un listado de usuarios dependiendo de criterios de búsqueda
	 * 
	 * @param criterio
	 *            UsuariosCriteriaDTO
	 * @return UserDTO List
	 * @throws BocException
	 */
	public List getUsersByCriteria(UsuariosCriteriaDTO criterio)
			throws BocException {
		try {
			return usuariosService.getUsersByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	/**
	 * Obtiene el count(*) de usuarios según criterios de búsqueda
	 * 
	 * @param criterio
	 * @return count int
	 * @throws BocException
	 */
	public int getUsersCountByCriteria(UsuariosCriteriaDTO criterio)
			throws BocException {
		try {
			return usuariosService.getUsersCountByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	/**
	 * Obtiene un listado de locales
	 * 
	 * @return LocalDTO List
	 * @throws BocException
	 */
	public List getUsrLocales() throws BocException {
		try {
			return usuariosService.getUsrLocales();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	/**
	 * Permite modificar la clave del usuario
	 * 
	 * @param usr
	 * @return boolean
	 * @throws BocException
	 * @throws SystemException
	 */
	public boolean setModUser(UserDTO usr) throws BocException, SystemException {

		try {
			return usuariosService.setModUser(usr);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	/**
	 * Permite agregar un usuario
	 * 
	 * @param usr
	 *            UserDTO
	 * @return addUser long
	 * @throws BocException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public long addUser(UserDTO usr) throws BocException, ServiceException,
			SystemException {
		try {
			return usuariosService.addUser(usr);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	public long addPoligono(PoligonoDTO pol) throws BocException,
			ServiceException, SystemException {
		try {
			return pedidosBolService.AddPoligono(pol);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}
	public long addPoligonoWithZona(PoligonoDTO pol) throws BocException,
	ServiceException, SystemException {
		try {
			return pedidosBolService.AddPoligonoWithZona(pol);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}
	public boolean ModPoligono(PoligonoDTO pol) throws BocException,
			ServiceException, SystemException {
		try {
			return pedidosBolService.ModPoligono(pol);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	public int VerificaPoligonoEnDirecciones(long id_poligono)
			throws BocException, ServiceException, SystemException {
		try {
			return pedidosBolService.VerificaPoligonoEnDirecciones(id_poligono);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	public boolean DelPoligono(long id_poligono) throws BocException,
			ServiceException, SystemException {
		try {
			return pedidosBolService.DelPoligono(id_poligono);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	public boolean DelTrxMP(long id_pedido) throws BocException,
			ServiceException, SystemException {
		try {
			return pedidosBolService.DelTrxMP(id_pedido);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	/**
	 * Permite agregar perfiles de usuario
	 * 
	 * @param id_user
	 *            long
	 * @param id_perf
	 *            long
	 * @return boolean
	 * @throws BocException
	 */
	public boolean agregaPerfUser(long id_user, long id_perf)
			throws BocException {
		try {
			return usuariosService.agregaPerfUser(id_user, id_perf);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	/**
	 * Permite eliminar un perfil de usuario a un usuario en particular
	 * 
	 * @param id_user
	 *            long
	 * @param id_perf
	 *            long
	 * @return boolean
	 * @throws BocException
	 */
	public boolean elimPerfUser(long id_user, long id_perf) throws BocException {
		try {
			return usuariosService.elimPerfUser(id_user, id_perf);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	/**
	 * Obtiene un listado de perfiles
	 * 
	 * @return PerfilDTO List
	 * @throws BocException
	 */
	public List getPerfiles() throws BocException {
		try {
			return usuariosService.getPerfiles();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	/**
	 * Autentica usuario en el sistema
	 * 
	 * @param login
	 * @param password
	 * @throws BocException
	 * @throws SystemException
	 */
	public boolean doAutenticaUser(String login, String password)
			throws BocException, SystemException {

		try {
			return usuariosService.doAutenticaUser(login, password);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	// -----------------------Perfiles--------------------------------

	/**
	 * permite saber si un usuario tiene algún perfil en especifico
	 * 
	 * @param usr
	 * @param id_comando
	 * @return boolean
	 * @throws BocException
	 */
	public boolean doCheckPermisoPerfilComando(UserDTO usr, long id_comando)
			throws BocException {

		try {
			return usuariosService.doCheckPermisoPerfilComando(usr, id_comando);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	/**
	 * Obtiene los datos de un perfil en particular
	 * 
	 * @param id_perf
	 *            long
	 * @return PerfilDTO
	 * @throws BocException
	 */
	public PerfilDTO getPerfilById(long id_perf) throws BocException {
		try {
			return usuariosService.getPerfilById(id_perf);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	/**
	 * Obtiene un listado de todos los perfiles según los criterios que se le
	 * envie
	 * 
	 * @param criterio
	 *            PerfilesCriteriaDTO
	 * @return PerfilDTO List
	 * @throws BocException
	 */
	public List getPerfilesAll(PerfilesCriteriaDTO criterio)
			throws BocException {
		try {
			return usuariosService.getPerfilesAll(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	/**
	 * Obtiene un Count(*) de todos los perfiles según un criterio determinado
	 * 
	 * @param criterio
	 *            PerfilesCriteriaDTO
	 * @return getPerfilesAllCount int
	 * @throws BocException
	 */
	public int getPerfilesAllCount(PerfilesCriteriaDTO criterio)
			throws BocException {
		try {
			return usuariosService.getPerfilesAllCount(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	/**
	 * permite modificar el nombre y la descripción de un perfil
	 * 
	 * @param perf
	 *            PerfilDTO
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setModPerfil(PerfilDTO perf) throws BocException {
		try {
			return usuariosService.setModPerfil(perf);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	/**
	 * Permite agregar un perfil
	 * 
	 * @param perf
	 *            PerfilDTO
	 * @return boolean
	 * @throws BocException
	 */
	public boolean addPerfil(PerfilDTO perf) throws BocException {
		try {
			return usuariosService.addPerfil(perf);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	/**
	 * Obtiene un listado de todos los comandos
	 * 
	 * @param id
	 *            long
	 * @return ComandosEntity List
	 * @throws BocException
	 */
	public List getComandosAll(long id) throws BocException {
		try {
			return usuariosService.getComandosAll(id);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	/**
	 * obtiene los datos de un comando
	 * 
	 * @param comando
	 * @return ComandoDTO
	 * @throws BocException
	 */
	public ComandoDTO getComandoByName(String comando) throws BocException {

		try {
			return usuariosService.getComandoByName(comando);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	public boolean agregaCmdPerf(long id_cmd, long id_perf) throws BocException {
		try {
			return usuariosService.agregaCmdPerf(id_cmd, id_perf);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	public boolean elimCmdPerf(long id_cmd, long id_perf) throws BocException {
		try {
			return usuariosService.elimCmdPerf(id_cmd, id_perf);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	public boolean elimCmdsByIdPerf(long id_perf) throws BocException {
		try {
			return usuariosService.elimCmdsByIdPerf(id_perf);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	/**
	 * Actualiza listado de comandos asociados a un perfil
	 * 
	 * @param dto
	 *            ProcModPerfilDTO
	 * @throws BocException
	 * @throws SystemException
	 */
	public void doModComandosAlPerfil(ProcModPerfilDTO dto)
			throws BocException, SystemException {

		try {
			usuariosService.doModComandosAlPerfil(dto);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}

	}

	// -----------------------Clientes--------------------------------

	/**
	 * Obtiene los datos del cliente según su id
	 * 
	 * @param idcliente
	 *            long
	 * @return ClientesDTO
	 * @throws BocException
	 */
	public ClientesDTO getClienteById(long idcliente) throws BocException {
		try {
			return clientesService.getClienteById(idcliente);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado de Clientes según un criterio
	 * 
	 * @param criterio
	 *            ClienteCriteriaDTO
	 * @return ClientesDTO List
	 * @throws BocException
	 */
	public List getClientesByCriteria(ClienteCriteriaDTO criterio)
			throws BocException {
		try {
			return clientesService.getClientesByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado con todos los clientes
	 * 
	 * @return ClientesDTO List
	 * @throws BocException
	 */
	public List getClientesAll() throws BocException {
		try {
			return clientesService.getClientesAll();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param criterio
	 *            ClienteCriteriaDTO
	 * @return count int
	 * @throws BocException
	 */
	public int getClientesCountByCriteria(ClienteCriteriaDTO criterio)
			throws BocException {
		try {
			return clientesService.getClientesCountByCriteria(criterio);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Realiza proceso de bloqueo de cliente
	 * 
	 * @param id_cliente
	 *            long
	 * @throws BocException
	 * @throws SystemException
	 */
	public void doBloqueaCliente(long id_cliente) throws BocException,
			SystemException {

		try {
			clientesService.doBloqueaCliente(id_cliente);
		} catch (ServiceException ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Realiza proceso de desbloqueo de cliente
	 * 
	 * @param id_cliente
	 *            long
	 * @throws BocException
	 * @throws SystemException
	 */
	public void doDesbloqueaCliente(long id_cliente) throws BocException,
			SystemException {

		try {
			clientesService.doDesbloqueaCliente(id_cliente);
		} catch (ServiceException ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene el id del cliente
	 * 
	 * @param rut
	 *            String
	 * @param apellido
	 *            String
	 * @return clientesService.getClienteByTips(rut, apellido) long
	 * @throws BocException
	 */
	public long getClienteByTips(String rut, String apellido)
			throws BocException {
		try {
			return clientesService.getClienteByTips(rut, apellido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	// -----------------------Direccion-----------------------
	/**
	 * Obtiene las direcciones por el id de direccion
	 * 
	 * @param id_direccion
	 *            long
	 * @return DireccionesDTO
	 * @throws BocException
	 */
	public DireccionesDTO getDireccionByIdDir(long id_direccion)
			throws BocException {
		try {
			return clientesService.getDireccionByIdDir(id_direccion);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado de direcciones por el id del cliente
	 * 
	 * @param id_cliente
	 * @return List TipoCalleDTO
	 * @throws BocException
	 */
	public List getDireccionesByIdCliente(long id_cliente) throws BocException {
		try {
			return clientesService.getDireccionesByIdCliente(id_cliente);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * ModAddrBook: controller :Direcciones
	 */

	/**
	 * permite modificar el libro de direcciones
	 * 
	 * @param procparam
	 *            ProcModAddrBookDTO
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setModAddrBook(ProcModAddrBookDTO procparam)
			throws BocException {
		try {
			return clientesService.setModAddrBook(procparam);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado con los datos de las zonas
	 * 
	 * @return List ZonaDTO
	 * @throws BocException
	 */
	public List getZonas() throws BocException {
		try {
			return clientesService.getZonas();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado de los locales
	 * 
	 * @return List LocalDTO
	 * @throws BocException
	 */

	public List getLocales() throws BocException {
		try {
			return clientesService.getLocales();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	public List getLocales(String cod_sucursal) throws BocException {
		try {
			return clientesService.getLocales(cod_sucursal);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	public boolean doModLocal(LocalDTO dto) throws BocException {
		try {
			return clientesService.doModLocal(dto);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	public boolean doModZonaLocal(LocalDTO dto) throws BocException {
		try {
			return clientesService.doModZonaLocal(dto);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}
	
	public boolean doAddLocal(LocalDTO dto) throws BocException {
		try {
			return clientesService.doAddLocal(dto);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}
	public int doAddLocalWithZone(LocalDTO dto) throws BocException {
		try {
			return clientesService.doAddLocalWithZone(dto);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado de las comunas
	 * 
	 * @return List ComunaDTO
	 * @throws BocException
	 */
	public List getComunas() throws BocException {
		try {
			return clientesService.getComunas();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado de las comunas con poligonos
	 * 
	 * @return List ComunaDTO
	 * @throws BocException
	 */
	public List getComunasConPoligonos() throws BocException {
		try {
			return clientesService.getComunasConPoligonos();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado de las comunas
	 * 
	 * @return List ComunaDTO
	 * @throws BocException
	 */
	public String getComunaById(long id_comuna) throws BocException {
		try {
			return clientesService.getComunaById(id_comuna);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado de las comunas con Zona Asignada
	 * 
	 * @return List ComunaDTO
	 * @throws BocException
	 */
	/*
	 * public List getComunasConZona() throws BocException{ try{ return
	 * clientesService.getComunasConZona(); }catch (Exception ex) {
	 * //RemoteException ex // Translate the service exception into //
	 * application exception throw new BocException(ex); } }
	 */

	/**
	 * Obtiene un listado de las comunas
	 * 
	 * @return List ComunaDTO
	 * @throws BocException
	 */
	public List getComunasFact() throws BocException {
		try {
			return clientesService.getComunasFact();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado con los tipos de calle
	 * 
	 * @return List TipoCalleDTO
	 * @throws BocException
	 */
	public List getTiposCalle() throws BocException {
		try {
			return clientesService.getTiposCalle();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado de comunas por su zona
	 * 
	 * @param id_zona
	 *            long
	 * @return List ComunaDTO
	 * @throws BocException
	 */
	/*
	 * public List getComunasByZona(long id_zona) throws BocException{ try{
	 * return clientesService.getComunasByZona(id_zona); }catch (Exception ex) {
	 * //RemoteException ex // Translate the service exception into //
	 * application exception throw new BocException(ex); } }
	 */

	/**
	 * Obtiene un listado de zonas por comunas
	 * 
	 * @param id_comuna
	 *            long
	 * @return List ZonaDTO
	 * @throws BocException
	 */
	public List getZonasByComuna(long id_comuna) throws BocException {
		try {
			return clientesService.getZonasByComuna(id_comuna);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado de locales por zona
	 * 
	 * @param id_zona
	 *            long
	 * @return List LocalDTO
	 * @throws BocException
	 */
	public List getLocalesByZona(long id_zona) throws BocException {
		try {
			return clientesService.getLocalesByZona(id_zona);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene los datos de una zona por su id
	 * 
	 * @param id_zona
	 *            long
	 * @return ZonaDTO
	 * @throws BocException
	 */
	public ZonaDTO getZonaById(long id_zona) throws BocException {
		try {
			return clientesService.getZonaById(id_zona);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	// -----------------------Categorias-----------------------
	/**
	 * Obtiene un listado de Categoria según algún criterio
	 * 
	 * @param criterio
	 * @return List CategoriasDTO
	 * @throws BocException
	 */
	public List getCategoriasByCriteria(CategoriasCriteriaDTO criterio)
			throws BocException {
		try {
			return contFacade.getCategoriasByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/*
	 * //este método devuelve null public List
	 * getCategoriasByNumeroByCriteria(CategoriasCriteriaDTO criterio, long
	 * num_cat) throws BocException{ try{ return
	 * contFacade.getCategoriasByNumeroByCriteria(criterio, num_cat); }catch
	 * (Exception ex) { //RemoteException ex // Translate the service exception
	 * into // application exception throw new BocException(ex); } }
	 */
	/*
	 * //este método devuelve null public List
	 * getCategoriasByNombreByCriteria(CategoriasCriteriaDTO criterio, String
	 * nombre_cat) throws BocException{ try{ return
	 * contFacade.getCategoriasByNombreByCriteria(criterio, nombre_cat); }catch
	 * (Exception ex) { //RemoteException ex // Translate the service exception
	 * into // application exception throw new BocException(ex); } }
	 */
	/**
	 * Obtiene un listado con los datos para la navegacion de las categorias
	 * 
	 * @param criterio
	 *            CategoriasCriteriaDTO
	 * @param cat_padre
	 *            long
	 * @return List
	 * @throws BocException
	 */
	public List getCategoriasNavegacion(CategoriasCriteriaDTO criterio,
			long cat_padre) throws BocException {
		try {
			return contFacade.getCategoriasNavegacion(criterio, cat_padre);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Entrega una categoria identificada por el id_categoria
	 * 
	 * @param id_categoria
	 *            long
	 * @return CategoriasDTO
	 */
	public CategoriasDTO getCategoriasById(long id_categoria)
			throws BocException {
		try {
			return contFacade.getCategoriasById(id_categoria);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Entrega el listado de productos de una categoria.
	 * 
	 * @param codCat
	 *            long
	 * @return List
	 */
	public List getProductosByCategId(long codCat) throws BocException {
		try {
			return contFacade.getProductosByCategId(codCat);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Listado de Tipos de las categorias presentadas en base a un criterio de
	 * busqueda.
	 * 
	 * @return List
	 */
	public List getTiposCategorias() throws BocException {
		try {
			return contFacade.getTiposCategorias();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Entrega el total de registros de categorias existentes para un criterio
	 * de busqueda.
	 * 
	 * @param criterio
	 * @return List
	 */
	public int getCategoriasCountByCriteria(CategoriasCriteriaDTO criterio)
			throws BocException {
		try {
			return contFacade.getCategoriasCountByCriteria(criterio);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * AddCatWeb: controller :Categorias
	 */
	/**
	 * Permite insertar una categoria web
	 * 
	 * @param procparam
	 * @return long
	 * @throws BocException
	 */
	public long setAddCatWeb(ProcAddCatWebDTO procparam) throws BocException {
		try {
			return contFacade.setAddCatWeb(procparam);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * DelCatWeb: controller :Categorias
	 */
	/**
	 * Permite Eliminar una Categoria Web
	 * 
	 * @param procparam
	 *            ProcDelCatWebDTO
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setDelCatWeb(ProcDelCatWebDTO procparam) throws BocException {
		try {
			return contFacade.setDelCatWeb(procparam);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * ModCatWeb: controller :Categorias
	 */
	/**
	 * Permite modificar los datos de una categoria web
	 * 
	 * @param procparam
	 *            ProcModCatWebDTO
	 * @throws BocException
	 */
	public void setModCatWeb(ProcModCatWebDTO procparam) throws BocException {
		try {
			contFacade.setModCatWeb(procparam);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado de Categorias según el codigo de la categoria
	 * 
	 * @param codCat
	 *            long
	 * @return List
	 * @throws BocException
	 */
	public List getCategoriasByCategId(long codCat) throws BocException {
		try {
			return contFacade.getCategoriasByCategId(codCat);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * ModSubCatWeb: controller :Categorias
	 */
	/**
	 * Permite modificar una SubCategoria Web
	 * 
	 * @param procparam
	 *            ProcModSubCatWebDTO
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setModSubCatWeb(ProcModSubCatWebDTO procparam)
			throws BocException {
		try {
			return contFacade.setModSubCatWeb(procparam);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Permite saber si el id corresponde a una categoria
	 * 
	 * @param id
	 * @return boolean
	 * @throws BocException
	 */
	public boolean isCategoriaById(long id) throws BocException {
		try {
			return contFacade.isCategoriaById(id);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	// -----------------------Productos-----------------------

	/**
	 * Devuelve el código sap de un producto generico
	 * 
	 * @return int
	 * @throws BocException
	 */
	public long getCodSapGenerico() throws BocException {
		try {
			return contFacade.getCodSapGenerico();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Devuelve el producto con todos sus atributos incluyendo sus imagenes, en
	 * base a un codigo sap entregado
	 * 
	 * @param cod_prodsap
	 * @return
	 */
	/*
	 * // este método devuelve nulo public ProductosDTO getProducto(String
	 * cod_prodsap) throws BocException{ try{ return
	 * contFacade.getProducto(cod_prodsap); }catch (Exception ex) {
	 * //RemoteException ex // Translate the service exception into //
	 * application exception throw new BocException(ex); } }
	 */
	/**
	 * Retorna un listado de productos en base al criterio
	 * 
	 * @param criterio
	 *            ProductosCriteriaDTO
	 * @return List ProductosDTO
	 * @throws BocException
	 */
	public List getProductosByCriteria(ProductosCriteriaDTO criterio)
			throws BocException {
		try {
			return contFacade.getProductosByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna la cantidad de productos en base al criterio
	 * 
	 * @param criterio
	 *            ProductosCriteriaDTO
	 * @return int
	 * @throws BocException
	 */
	public int getProductosCountByCriteria(ProductosCriteriaDTO criterio)
			throws BocException {
		try {
			return contFacade.getProductosCountByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene los datos de un producto según su cod de producto
	 * 
	 * @param codProd
	 *            long
	 * @return ProductosDTO
	 * @throws BocException
	 */
	public ProductosDTO getProductosById(long codProd) throws BocException {
		try {
			return contFacade.getProductosById(codProd);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Permite agrar un Item a un producto
	 * 
	 * @param proc
	 *            ProcModGenericItemDTO
	 * @return boolean
	 * @throws BocException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean agregaItemProducto(ProcModGenericItemDTO proc)
			throws BocException, SystemException, ServiceException {
		try {
			return contFacade.agregaItemProducto(proc);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Permite eliminar items de un producto
	 * 
	 * @param item
	 *            ProductoEntity
	 * @return boolean
	 * @throws BocException
	 */
	public boolean eliminaItemProducto(ProductoEntity item) throws BocException {
		try {
			return contFacade.eliminaItemProducto(item);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Permite agregar Sugeridos al producto
	 * 
	 * @param suger
	 *            ProductoSugerDTO
	 * @return boolean
	 * @throws BocException
	 */
	public boolean agregaSugeridoProducto(ProductoSugerDTO suger)
			throws BocException {
		try {
			return contFacade.agregaSugeridoProducto(suger);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Permite eliminar sugeridos del Producto
	 * 
	 * @param id_suger
	 * @return boolean
	 * @throws BocException
	 */
	public boolean eliminaSugeridoProducto(long id_suger) throws BocException {
		try {
			return contFacade.eliminaSugeridoProducto(id_suger);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado de productos en base a un criterio y un patron de
	 * codigo de CATEGORIA SAP
	 * 
	 * @param criterio
	 * @param cod_catsap
	 * @return
	 * @throws BocException
	 */
	/*
	 * public List getProductosByCriteriaCatSap(ProductosCriteriaDTO criterio,
	 * String cod_catsap) throws BocException{ try{ return
	 * contFacade.getProductosByCriteriaCatSap(criterio, cod_catsap); }catch
	 * (Exception ex) { //RemoteException ex // Translate the service exception
	 * into // application exception throw new BocException(ex); } }
	 */

	/**
	 * Obtiene un listado de productos en base al patron de codigo de PRODUCTO
	 * SAP
	 * 
	 * @param criterio
	 * @param cod_prodsap
	 * @return
	 * @throws BocException
	 */
	/*
	 * public List getProductosByCriteriaProdSap(ProductosCriteriaDTO criterio,
	 * String cod_prodsap) throws BocException{ try{ return
	 * contFacade.getProductosByCriteriaProdSap(criterio, cod_prodsap); }catch
	 * (Exception ex) { //RemoteException ex // Translate the service exception
	 * into // application exception throw new BocException(ex); } }
	 */

	/**
	 * Obtiene un listado de productos navegando por la categoria web
	 * 
	 * @param id_categoria
	 * @return
	 * @throws BocException
	 */
	/*
	 * public List getProductosByCategoriaWeb(long id_categoria) throws
	 * BocException{ try{ return
	 * contFacade.getProductosByCategoriaWeb(id_categoria); }catch (Exception
	 * ex) { //RemoteException ex // Translate the service exception into //
	 * application exception throw new BocException(ex); } }
	 */

	/*
	 * Obtiene las categorias relacionadas al producto
	 */
	/**
	 * Obtiene un listado con los datos de las Categorias según el cod de
	 * producto
	 * 
	 * @param codProd
	 *            long
	 * @return List CategoriasDTO
	 * @throws BocException
	 */
	public List getCategoriasByProductId(long codProd) throws BocException {
		try {
			return contFacade.getCategoriasByProductId(codProd);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Listado tipos de productos
	 * 
	 * @return List EstadoDTO
	 */
	public List getTiposProductos() throws BocException {
		try {
			return contFacade.getTiposProductos();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Listado de unidades de medida
	 * 
	 * @return List UnidadMedidaDTO
	 * @throws BocException
	 */
	public List getUnidMedida() throws BocException {
		try {
			return contFacade.getUnidMedida();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * permite saber si una categoria está asociada a un producto
	 * 
	 * @param id_cat
	 *            long
	 * @param id_prod
	 *            long
	 * @return boolean
	 * @throws BocException
	 */
	public boolean isCatAsocProd(long id_cat, long id_prod) throws BocException {
		boolean result = false;
		try {
			result = contFacade.isCatAsocProd(id_cat, id_prod);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
		return result;
	}

	/**
	 * AddCategoryProduct: controller :Productos
	 */
	/**
	 * Permite agregar categorias a un producto
	 * 
	 * @param procparam
	 *            ProcAddCategoryProductDTO
	 * @return boolean
	 * @throws BocException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean setAddCategoryProduct(ProcAddCategoryProductDTO procparam)
			throws BocException, SystemException, ServiceException {
		boolean result = false;
		try {
			result = contFacade.setAddCategoryProduct(procparam);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
		return result;
	}

	/**
	 * Permite actualizar productos de una categoria
	 * 
	 * @param param
	 *            ProductoCategDTO
	 * @return boolean
	 * @throws BocException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean setModCategoryProduct(ProductoCategDTO param)
			throws BocException, SystemException, ServiceException {
		boolean result = false;
		try {
			result = contFacade.setModCategoryProduct(param);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
		return result;
	}

	/**
	 * DelCategoryProduct: controller :Productos
	 */
	/**
	 * Permite eliminar categorias a un producto
	 * 
	 * @param procparam
	 *            ProcDelCategoryProductDTO
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setDelCategoryProduct(ProcDelCategoryProductDTO procparam)
			throws BocException {
		boolean result = false;
		try {
			result = contFacade.setDelCategoryProduct(procparam);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
		return result;
	}

	// 20121106avc
	public boolean setDelAllProductCategory(ProcDelAllProductCategory proc)
			throws BocException {
		boolean result = false;
		try {
			result = contFacade.setDelAllProductCategory(proc);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
		return result;

	}

	// 20121106avc

	/**
	 * DelGenericProduct: controller :Productos
	 */
	/**
	 * Permite eliminar productos genericos
	 * 
	 * @param procparam
	 *            ProcDelGenericProductDTO
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setDelGenericProduct(ProcDelGenericProductDTO procparam)
			throws BocException {
		boolean result = false;
		try {
			result = contFacade.setDelGenericProduct(procparam);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
		return result;
	}

	/**
	 * ModGenericItem: controller :Productos
	 */
	/*
	 * public void setModGenericItem(ProcModGenericItemDTO procparam) throws
	 * BocException{ try { contFacade.setModGenericItem(procparam); } catch
	 * (Exception ex) { //RemoteException ex throw new BocException(ex); } }
	 */

	/**
	 * ModProduct: controller :Productos
	 */
	/**
	 * Permite modificar los datos de un producto
	 * 
	 * @param procparam
	 *            ProcModProductDTO
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setModProduct(ProcModProductDTO procparam)
			throws BocException {
		boolean result = false;
		try {
			result = contFacade.setModProduct(procparam);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
		return result;
	}

	/**
	 * ModSugProduct: controller :Productos
	 */
	/**
	 * Permite Modificar los productos sugeridos
	 * 
	 * @param procparam
	 *            ProcModSugProductDTO
	 * @return boolean
	 * @throws BocException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setModSugProduct(ProcModSugProductDTO procparam)
			throws BocException, ServiceException, SystemException {
		try {
			return contFacade.setModSugProduct(procparam);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * ModSupCatProduct: controller :Productos
	 */
	/*
	 * public void setModSupCatProduct(ProcModSupCatProductDTO procparam) throws
	 * BocException{ try { contFacade.setModSupCatProduct(procparam); } catch
	 * (Exception ex) { //RemoteException ex throw new BocException(ex); } }
	 */

	/**
	 * NewGenericProduct: controller :Productos
	 */
	/*
	 * public long setNewGenericProduct(ProcNewGenericProductDTO procparam)
	 * throws BocException{ try { return
	 * contFacade.setNewGenericProduct(procparam); } catch (Exception ex) {
	 * //RemoteException ex throw new BocException(ex); } }
	 */

	/**
	 * AddProduct: controller :Productos
	 */
	/**
	 * Permite agregar un producto
	 * 
	 * @param procparam
	 *            ProcAddProductDTO
	 * @return int
	 * @throws BocException
	 */
	public int setAddProduct(ProcAddProductDTO procparam) throws BocException {
		try {
			return contFacade.setAddProduct(procparam);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	public int setAddProductBolsa(ProcAddProductDTO procparam)
			throws BocException {
		try {
			return contFacade.setAddProductBolsa(procparam);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Permite agregar un log al monitor de productos
	 * 
	 * @param log
	 *            ProductoLogDTO
	 * @return int
	 * @throws BocException
	 */
	public int setLogProduct(ProductoLogDTO log) throws BocException {
		try {
			return contFacade.setLogProduct(log);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * PubDespProduct: controller :Productos
	 */

	/**
	 * Permite modificar la Publicacion o Despublicacion de un producto
	 * 
	 * @param procparam
	 * @throws BocException
	 */
	public boolean setPubDespProduct(ProcPubDespProductDTO procparam)
			throws BocException {
		try {
			return contFacade.setPubDespProduct(procparam);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado con los datos de los productos genericos
	 * 
	 * @param cod_prod
	 *            long
	 * @return List prodDto
	 * @throws BocException
	 */
	public List getProductGenericos(long cod_prod) throws BocException {
		try {
			return contFacade.getProductGenericos(cod_prod);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * RegLogProduct: controller :Productos
	 */
	/*
	 * public void setRegLogProduct(ProcRegLogProductDTO procparam) throws
	 * BocException{ try { contFacade.setRegLogProduct(procparam); } catch
	 * (Exception ex) { //RemoteException ex throw new BocException(ex); } }
	 */

	/**
	 * Obtiene un listado con los log's de un producto en particular
	 * 
	 * @param cod_prod
	 *            long
	 * @return List ProductoLogDTO
	 * @throws BocException
	 */
	public List getLogByProductId(long cod_prod) throws BocException {
		try {
			return contFacade.getLogByProductId(cod_prod);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado con los datos de las marcas
	 * 
	 * @return List MarcasDTO
	 * @throws BocException
	 */
	public List getMarcas() throws BocException {
		try {
			return contFacade.getMarcas();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado con los datos de las marcas, segun criterio
	 * 
	 * @return List MarcasDTO
	 * @throws BocException
	 */
	public List getMarcas(MarcasCriteriaDTO criterio) throws BocException {
		try {
			return contFacade.getMarcas(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene la cantidad total de marcas
	 * 
	 * @return List MarcasDTO
	 * @throws BocException
	 */
	public int getMarcasAllCount() throws BocException {
		try {
			return contFacade.getMarcasAllCount();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene los datos de una marca en particular
	 * 
	 * @param codMrc
	 *            int
	 * @return MarcasDTO
	 * @throws BocException
	 */
	public MarcasDTO getMarcasById(int codMrc) throws BocException {
		try {
			return contFacade.getMarcasById(codMrc);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Agrega una marca
	 * 
	 * @param prm
	 *            MarcasDTO
	 * @return boolean
	 */
	public boolean addMarca(MarcasDTO prm) throws BocException {
		try {
			return contFacade.addMarca(prm);
		} catch (Exception ex) {
			throw new BocException(ex.getMessage());
		}
	}

	/**
	 * Modificar una marca
	 * 
	 * @param prm
	 *            MarcasDTO
	 * @return boolean
	 * @throws BocException
	 */
	public boolean modMarca(MarcasDTO prm) throws BocException {
		try {
			return contFacade.modMarca(prm);
		} catch (Exception ex) {
			throw new BocException(ex.getMessage());
		}
	}

	/**
	 * Eliminar una marca
	 * 
	 * @param prm
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setDelMarca(MarcasDTO prm) throws BocException {
		try {
			return contFacade.setDelMarca(prm);
		} catch (Exception ex) {
			throw new BocException(ex.getMessage());
		}
	}

	/**
	 * Obtiene un listado con los Motivos de despublicación
	 * 
	 * @return List mtvdto
	 * @throws BocException
	 */
	public List getMotivosDesp() throws BocException {
		try {
			return contFacade.getMotivosDesp();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Permite saber su el codigo que se envía pertenece a un producto
	 * 
	 * @param cod_prod
	 *            long
	 * @return boolean
	 * @throws BocException
	 */
	public boolean isProductById(long cod_prod) throws BocException {
		try {
			return contFacade.isProductById(cod_prod);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/*
	 * *************************************************************************************************************
	 * ------------------------------ProductosSap para el Monitor de
	 * MPV------------------------------
	 * ****************************************
	 * *********************************************************************
	 */

	/**
	 * Entrega un listado de Productos SAP en base al criterio
	 * 
	 * @param criterio
	 *            ProductosSapCriteriaDTO
	 * @return List ProductosSapDTO
	 */
	public List getProductosSapByCriteria(ProductosSapCriteriaDTO criterio)
			throws BocException {
		try {
			return contFacade.getProductosSapByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene el count(*) de productos Sap según algún criterio
	 * 
	 * @param criterio
	 * @return int
	 * @throws BocException
	 */
	public int getCountProdSapByCriteria(ProductosSapCriteriaDTO criterio)
			throws BocException {
		try {
			return contFacade.getCountProdSapByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Entrega un listado de Productos SAP en base al criterio y a un patron de
	 * codigo de producto SAP.
	 * 
	 * @param criterio
	 * @param cod_catsap
	 * @return
	 */
	/*
	 * // este método devuelve nulo public List
	 * getProductosSapByCriteriaByCategoriaSap(ProductosSapCriteriaDTO criterio,
	 * String cod_catsap) throws BocException{ try{ return
	 * contFacade.getProductosSapByCriteriaByCategoriaSap(criterio,cod_catsap);
	 * }catch (Exception ex) { //RemoteException ex // Translate the service
	 * exception into // application exception throw new BocException(ex); } }
	 */

	/**
	 * Entrega un listado de Productos SAP en base al criterio y a un patron de
	 * codigo de categoria SAP.
	 * 
	 * @param criterio
	 *            ProductosSapCriteriaDTO
	 * @param cod_prodsap
	 * @return
	 */
	/*
	 * // este método devuelve nulo public List
	 * getProductosSapByCriteriaByProductoSap(ProductosSapCriteriaDTO criterio,
	 * String cod_prodsap)throws BocException{ try{ return
	 * contFacade.getProductosSapByCriteriaByProductoSap(criterio,cod_prodsap);
	 * }catch (Exception ex) { //RemoteException ex // Translate the service
	 * exception into // application exception throw new BocException(ex); } }
	 */
	/**
	 * Entrega un listado de Productos SAP en base al criterio y a un RANGO de
	 * fechas en que el producto SAP fue cargado.
	 * 
	 * @param criterio
	 *            ProductosSapCriteriaDTO
	 * @param fecha_ini
	 * @param fecha_fin
	 * @return 
	 *         contFacade.getProductosSapByCriteriaByDateRange(criterio,fecha_ini
	 *         ,fecha_fin) List ProductosSapDTO
	 */
	public List getProductosSapByCriteriaByDateRange(
			ProductosSapCriteriaDTO criterio, String fecha_ini, String fecha_fin)
			throws BocException {
		try {
			return contFacade.getProductosSapByCriteriaByDateRange(criterio,
					fecha_ini, fecha_fin);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene el Count (*) de Productos SAP en base al criterio y a un RANGO de
	 * fechas en que el producto SAP fue cargado.
	 * 
	 * @param criterio
	 *            ProductosSapCriteriaDTO
	 * @param fecha_ini
	 *            String
	 * @param fecha_fin
	 *            String
	 * @return int
	 * @throws BocException
	 */
	public int getCountProdSapByCriteriaByDateRange(
			ProductosSapCriteriaDTO criterio, String fecha_ini, String fecha_fin)
			throws BocException {
		try {
			return contFacade.getCountProdSapByCriteriaByDateRange(criterio,
					fecha_ini, fecha_fin);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Entrega un listado de Productos SAP en base a la categoria SAP dada. Esto
	 * permite mostrar listados navegando por las categorias SAP.
	 * 
	 * @param criterio
	 * @param cod_prodsap
	 * @return
	 */
	/*
	 * // este método devuelve nulo public List
	 * getProductosSapNavegacion(ProductosSapCriteriaDTO criterio, String
	 * cod_catsap)throws BocException{ try{ return
	 * contFacade.getProductosSapNavegacion(criterio,cod_catsap); }catch
	 * (Exception ex) { //RemoteException ex // Translate the service exception
	 * into // application exception throw new BocException(ex); } }
	 */

	/**
	 * ModMPVProduct: controller :ProductosSap
	 */
	/**
	 * Permite modificar el mix de productos vendibles
	 * 
	 * @param procparam
	 *            ProcModMPVProductDTO
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setModMPVProduct(ProcModMPVProductDTO procparam)
			throws BocException {
		try {
			return contFacade.setModMPVProduct(procparam);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}

	}

	/**
	 * Obtiene un listado de Categorias sap según el cod de la categoria y le
	 * cod de la categoria padre
	 * 
	 * @param cod_cat
	 *            String
	 * @param cod_cat_padre
	 *            String
	 * @return List CategoriaSapDTO
	 * @throws BocException
	 */
	public List getCategoriasSapById(String cod_cat, String cod_cat_padre)
			throws BocException {
		try {
			return contFacade.getCategoriasSapById(cod_cat, cod_cat_padre);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene el cod de la categoria padre
	 * 
	 * @param cod_cat
	 *            String
	 * @return String
	 * @throws BocException
	 */
	public String getCodCatPadre(String cod_cat) throws BocException {
		try {
			return contFacade.getCodCatPadre(cod_cat);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene los datos de un producto sap según el codigo del producto
	 * 
	 * @param codProd
	 *            long
	 * @return ProductosSapDTO
	 * @throws BocException
	 */
	public ProductosSapDTO getProductosSapById(long codProd)
			throws BocException {
		try {
			return contFacade.getProductosSapById(codProd);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado de Codigos de Barras según el codigo de un producto
	 * 
	 * @param codProd
	 * @return List CodigosBarraSapDTO
	 * @throws BocException
	 */
	public List getCodBarrasByProdId(long codProd) throws BocException {
		try {
			return contFacade.getCodBarrasByProdId(codProd);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado de precios segun el id del producto
	 * 
	 * @param codProd
	 *            long
	 * @return List PreciosSapDTO
	 * @throws BocException
	 */
	public List getPreciosByProdId(long codProd) throws BocException {
		try {
			return contFacade.getPreciosByProdId(codProd);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	// -----------------------Pedidos-----------------------
	/**
	 * Retorna listado de pedidos segun el criterio
	 * 
	 * @param criterio
	 *            PedidosCriteriaDTO
	 * @return List MonitorPedidoDTO
	 * @throws BocException
	 */
	public List getPedidosByCriteria(PedidosCriteriaDTO criterio)
			throws BocException {
		try {
			return pedidosBolService.getPedidosByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna la cantidad de pedidos, segun el criterio
	 * 
	 * @param criterio
	 *            PedidosCriteriaDTO
	 * @return long
	 * @throws BocException
	 */
	public long getCountPedidosByCriteria(PedidosCriteriaDTO criterio)
			throws BocException {
		try {
			return pedidosBolService.getCountPedidosByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna listado de pedidos, segun el criterio de fecha
	 * 
	 * @param criterio
	 *            PedidosCriteriaDTO
	 * @return List MonitorPedidoDTO
	 * @throws BocException
	 */
	public List getPedidosByFecha(PedidosCriteriaDTO criterio)
			throws BocException {
		try {
			return pedidosBolService.getPedidosByFecha(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna la cantidad de pedidos, segun el criterio de fecha
	 * 
	 * @param criterio
	 *            PedidosCriteriaDTO
	 * @return long
	 * @throws BocException
	 */
	public long getCountPedidosByFecha(PedidosCriteriaDTO criterio)
			throws BocException {
		try {
			return pedidosBolService.getCountPedidosByFecha(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/*
	 * Obtiene el Pedido
	 */
	/**
	 * Retorna los datos del pedido
	 * 
	 * @param id_pedido
	 *            long
	 * @return PedidoDTO
	 * @throws BocException
	 * @throws SystemException
	 */
	public PedidoDTO getPedidosById(long id_pedido) throws BocException,
			SystemException {

		try {
			return pedidosBolService.getPedido(id_pedido);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * 
	 * 
	 * @param id_pedido
	 * @return
	 * @throws BocException
	 */

	/*
	 * Obtiene la lista de las alertas del pedido
	 */
	/**
	 * Retorna listado de alertas del pedido
	 * 
	 * @param id_pedido
	 *            long
	 * @return List AlertaDTO
	 * @throws BocException
	 */
	public List getAlertasPedidosById(long id_pedido) throws BocException {
		try {
			return pedidosBolService.getAlertasPedido(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/*
	 * Obtiene la lista de los productos del pedido
	 */
	/**
	 * Retorna listado de productos del pedido
	 * 
	 * @param id_pedido
	 *            long
	 * @return List ProductosPedidoDTO
	 * @throws BocException
	 */
	public List getProductosPedidosById(long id_pedido) throws BocException {
		try {
			return pedidosBolService.getProductosPedido(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/*
	 * Obtiene id_sector del producto
	 */
	/**
	 * Retorna el id del sector, segun el <b>id_prod</b> código del producto y
	 * el <b>id_local</b> del local.
	 * 
	 * @param id_prod
	 *            long
	 * @return long, id del sector
	 * @throws BocException
	 */
	public long getSectorByProdId(long id_prod) throws BocException {
		try {
			return pedidosBolService.getSectorByProdId(id_prod);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/*
	 * Agrega producto al detalle del pedido
	 */
	/**
	 * Permite agregar el producto en el pedido, segun la información del
	 * detalle a agregar en el pedido.<br>
	 * Se utiliza desde Jumbo BOC, en la vista del detalle del pedido.
	 * 
	 * @param prod
	 *            ProductosPedidoDTO
	 * @return boolean, devuelve <i>true</i> si no hay error, caso contrario,
	 *         devuelve <i>false</i>.
	 * @throws BocException
	 */
	public boolean agregaProductoPedido(ProductosPedidoDTO prod)
			throws BocException {
		try {
			return pedidosBolService.agregaProductoPedido(prod);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/*
	 * Elimina producto al detalle del pedido
	 */
	/**
	 * Permite eliminar el detalle de un pedido. Se utiliza en el Jumbo BOC, en
	 * la vista del detalle del pedido.
	 * 
	 * @param prod
	 *            ProductosPedidoDTO
	 * @return boolean
	 * @throws BocException
	 */
	public boolean elimProductoPedido(ProductosPedidoDTO prod)
			throws BocException {
		try {
			return pedidosBolService.elimProductoPedido(prod);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Agrega registro al log del pedido
	 * 
	 * @param log
	 *            LogPedidoDTO
	 * @throws BocException
	 */
	public void addLogPedido(LogPedidoDTO log) throws BocException {
		try {
			pedidosBolService.addLogPedido(log);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/*
	 * Obtiene la lista de bins del pedido
	 */
	/**
	 * Obtiene lista de bins para un pedido
	 * 
	 * @param id_pedido
	 *            long
	 * @return List BinDTO
	 * @throws BocException
	 */
	public List getBinsPedidoById(long id_pedido) throws BocException {
		try {
			return pedidosBolService.getBinsPedido(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/*
	 * Obtiene la lista de sustitutos del pedido getBinsPedido
	 */
	/**
	 * Obtiene el listado de sustitutos de un pedido, segun el <b>id</b> del
	 * pedido.
	 * 
	 * @param id_pedido
	 *            long
	 * @return List SustitutoDTO
	 * @throws BocException
	 */
	public List getSustitutosByPedidoId(long id_pedido) throws BocException {
		try {
			return pedidosBolService.getSustitutosByPedidoId(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/*
	 * Obtiene la lista de faltantes del pedido getBinsPedido
	 */
	/**
	 * Obtiene el listado de faltantes de un pedido, segun el <b>id</b> del
	 * pedido.
	 * 
	 * @param id_pedido
	 *            long
	 * @return List FaltanteDTO
	 * @throws BocException
	 */
	public List getFaltantesByPedidoId(long id_pedido) throws BocException {
		try {
			return pedidosBolService.getFaltantesByPedidoId(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna si el pedido fue asignado o no.
	 * 
	 * @param col
	 *            AsignaOPDTO
	 * @return boolean, devuelve <i>true</i> en el caso que la asignación fue
	 *         satisfactoria, caso contrario devuelve <i>false</i>.
	 * @throws BocException
	 * 
	 */
	public boolean setAsignaOP(AsignaOPDTO col) throws BocException {
		try {
			return pedidosBolService.setAsignaOP(col);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna si el pedido fue liberado o no.
	 * 
	 * @param col
	 *            AsignaOPDTO
	 * @return boolean, devuelve true en el caso que la liberación fue
	 *         satisfactoria, caso contrario devuelve false.
	 * @throws BocException
	 * 
	 */
	public boolean setLiberaOP(AsignaOPDTO col) throws BocException {
		try {
			return pedidosBolService.setLiberaOP(col);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/*
	 * Obtiene la lista de despachos del pedido getBinsPedido
	 */
	/**
	 * Obtiene listado de pedidos en su flujo de despacho
	 * 
	 * @param criterio
	 *            DespachoCriteriaDTO
	 * @return List MonitorDespachosDTO
	 * @throws BocException
	 */
	public List getDespachosPedidoById(DespachoCriteriaDTO criterio)
			throws BocException {
		try {
			return pedidosBolService.getDespachosByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/*
	 * Obtiene la lista del log de pedidos
	 */
	/**
	 * Obtiene listado del log de un despacho
	 * 
	 * @param id_pedido
	 *            long
	 * @return List LogSimpleDTO
	 * @throws BocException
	 */
	public List getLogsPedidoById(long id_pedido) throws BocException {
		try {
			return pedidosBolService.getLogDespacho(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene los estados del pedido
	 * 
	 * @return List EstadosDTO
	 * @throws BocException
	 */
	public List getEstadosPedidoBOC() throws BocException {
		try {
			return pedidosBolService.getEstadosPedidoBOC();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene los estados del pedido
	 * 
	 * @return List EstadosDTO
	 * @throws BocException
	 */
	public EstadoDTO getEstadoPedido(long IdPedido) throws BocException {
		try {
			return pedidosBolService.getEstadoPedido(IdPedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene los estados de la TrxMp
	 * 
	 * @return EstadosDTO
	 * @throws BocException
	 */
	public EstadoDTO getEstadoTrxMp(long IdTrxMp) throws BocException {
		try {
			return pedidosBolService.getEstadoTrxMp(IdTrxMp);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene los motivos del pedido
	 * 
	 * @return List MotivoDTO
	 * @throws BocException
	 */
	public List getMotivosPedidoBOC() throws BocException {
		try {
			return pedidosBolService.getMotivosPedidoBOC();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Realiza el proceso de validación de la OP
	 * 
	 * @param id_pedido
	 *            long
	 * @return boolean, devuelve true en el caso que la validación fue
	 *         satisfactoria, caso contrario devuelve false.
	 * @throws BocException
	 * 
	 */
	public boolean setValidaOP(long id_pedido) throws BocException {
		try {
			return pedidosBolService.setValidaOP(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Elimina las alertas relacionadas al pedido.
	 * 
	 * @param id_pedido
	 *            long
	 * @return boolean, devuelve true en el caso que la eliminación fue
	 *         satisfactoria, caso contrario devuelve false.
	 * @throws BocException
	 * 
	 */
	public boolean elimAlertaByPedido(long id_pedido) throws BocException {
		try {
			return pedidosBolService.elimAlertaByPedido(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Agrega una alerta a un pedido.
	 * 
	 * @param id_pedido
	 *            long
	 * @param id_alerta
	 *            long
	 * @return boolean, devuelve true en el caso que la eliminación fue
	 *         satisfactoria, caso contrario devuelve false.
	 * @throws BocException
	 * 
	 */
	public boolean addAlertToPedido(long id_pedido, long id_alerta)
			throws BocException {
		try {
			return pedidosBolService.addAlertToPedido(id_pedido, id_alerta);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Modifica el estado de un pedido.
	 * 
	 * @param id_pedido
	 *            long
	 * @param id_estado
	 *            long
	 * @return boolean, devuelve true en el caso que la actualización fue
	 *         satisfactoria, caso contrario devuelve false.
	 * @throws BocException
	 * 
	 */
	public boolean setModEstadoPedido(long id_pedido, long id_estado)
			throws BocException {
		try {
			return pedidosBolService.setModEstadoPedido(id_pedido, id_estado);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Modifica el estado de una TrxMp.
	 * 
	 * @param id_trx_mp
	 *            long
	 * @param id_estado
	 *            long
	 * @return boolean, devuelve true en el caso que la actualización fue
	 *         satisfactoria, caso contrario devuelve false.
	 * @throws BocException
	 * 
	 */
	public boolean setModEstadoTrxMp(long id_trx_mp, long id_estado)
			throws BocException {
		try {
			return pedidosBolService.setModEstadoTrxMp(id_trx_mp, id_estado);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna si tiene alerta activa.
	 * 
	 * @param id_pedido
	 *            long
	 * @return boolean, devuelve true en el caso que la actualización fue
	 *         satisfactoria, caso contrario devuelve false.
	 * @throws BocException
	 * 
	 */
	public boolean getExisteAlertaActiva(long id_pedido) throws BocException {
		try {
			return pedidosBolService.getExisteAlertaActiva(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna si tiene alerta activa.
	 * 
	 * @param id_pedido
	 *            long
	 * @return boolean, devuelve true en el caso que la actualización fue
	 *         satisfactoria, caso contrario devuelve false.
	 * @throws BocException
	 * 
	 */
	public boolean setAnularOP(long id_pedido) throws BocException {
		try {
			return pedidosBolService.setAnularOP(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna si el pedido existe o no.
	 * 
	 * @param id_pedido
	 *            long
	 * @return boolean, devuelve true en el caso que el pedido existe, caso
	 *         contrario devuelve false.
	 * @throws BocException
	 */
	public boolean isPedidoById(long id_pedido) throws BocException {
		try {
			return pedidosBolService.isPedidoById(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Entrega el listado de trx de pago de un pedido
	 * 
	 * @param id_pedido
	 *            long
	 * @return List MonitorTrxMpDTO
	 * @throws BocException
	 */
	public List getTrxMpByIdPedido(long id_pedido) throws BocException {
		try {
			return pedidosBolService.getTrxMpByIdPedido(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Genera los cupones de pago en base a las trx de pago de un pedido.
	 * 
	 * @param id_pedido
	 *            long
	 * @param id_trxmp
	 *            long
	 * @return CuponPagoDTO
	 * @throws BocException
	 */
	public CuponPagoDTO getCuponPago(long id_pedido, long id_trxmp)
			throws BocException, SystemException {
		try {
			return pedidosBolService.getCuponPago(id_pedido, id_trxmp);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/*------------------------ Jornadas y Calendarios Despacho --------------------*/

	/**
	 * Obtiene el calendario de despacho semanal para una zona de despacho
	 * 
	 * @param n_semana
	 *            int número de semana del año
	 * @param ano
	 *            int año
	 * @param id_zona
	 *            long
	 * @return CalendarioDespachoDTO
	 */
	public CalendarioDespachoDTO getCalendarioDespacho(int n_semana, int ano,
			long id_zona) throws BocException {
		try {
			return pedidosBolService.getCalendarioDespacho(n_semana, ano,
					id_zona);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Realiza proceso de Re-agendamiento de un despacho
	 * 
	 * @param id_pedido
	 *            long
	 * @param id_jdespacho
	 *            long
	 * @param sobrescribeprecio
	 *            boolean
	 * @param precio
	 *            int
	 * @throws BocException
	 */
	public void doReagendaDespacho(long id_pedido, long id_jdespacho,
			boolean sobrescribeprecio, int precio, String usr_login,
			boolean modificarJPicking, boolean modificarPrecio)
			throws BocException {

		try {
			pedidosBolService.doReagendaDespacho(id_pedido, id_jdespacho,
					sobrescribeprecio, precio, usr_login, modificarJPicking,
					modificarPrecio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Realiza proceso de Cambiar el costo del despacho
	 * 
	 * @param id_pedido
	 *            long
	 * @param precio
	 *            int
	 * @param usr_login
	 *            String
	 * @throws BocException
	 */
	public void doCambiaCostoDespacho(long id_pedido, int precio,
			String usr_login) throws BocException {

		try {
			pedidosBolService.doCambiaCostoDespacho(id_pedido, precio,
					usr_login);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene lista de logs de un pedido
	 * 
	 * @param id_pedido
	 *            long
	 * @return List LogPedidoDTO
	 * @throws BocException
	 */
	public List getLogPedido(long id_pedido) throws BocException {
		try {
			return pedidosBolService.getLogPedido(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Cambia la dirección de despacho a un pedido a partir de las direcciones
	 * de la libreta
	 * 
	 * @param dto
	 *            ProcModPedidoDirDTO
	 * @return boolean
	 * @throws BocException
	 * @throws SystemException
	 */
	public boolean setModPedidoDir(String user, DireccionesDTO nuevaDir,
			PedidoDTO ped) throws BocException, SystemException {

		try {
			return pedidosBolService.setModPedidoDir(user, nuevaDir, ped);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Modifica las indicaciones de un pedido, segun la información contenida en
	 * <b>ProcModPedidoIndicDTO</b>.
	 * 
	 * @param prm
	 *            ProcModPedidoIndicDTO
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setModPedidoIndic(ProcModPedidoIndicDTO prm)
			throws BocException {
		try {
			return pedidosBolService.setModPedidoIndic(prm);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Permite agregar o eliminar un producto de un pedido, segun la información
	 * contenida en <b>ProcModPedidoProdDTO</b>.<br>
	 * Nota: Este método tiene Transaccionalidad.
	 * 
	 * @param prm
	 *            ProcModPedidoProdDTO
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setModPedidoProd(ProcModPedidoProdDTO prm)
			throws BocException {
		try {
			return pedidosBolService.setModPedidoProd(prm);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna listado de productos de un bin
	 * 
	 * @param id_bp
	 *            long
	 * @throws BocException
	 */
	public List getProductosBin(long id_bp) throws BocException {

		try {
			return pedidosBolService.getProductosBin(id_bp);
		} catch (ServiceException e) {
			throw new BocException(e);
		}
	}

	/**
	 * Actualiza el medio de pago del pedido.
	 * 
	 * @param mp
	 *            DatosMedioPagoDTO
	 * @throws BocException
	 * 
	 */
	public void setCambiarMedio_pago(DatosMedioPagoDTO mp) throws BocException,
			SystemException {

		try {
			pedidosBolService.setCambiarMedio_pago(mp);
		} catch (ServiceException e) {
			throw new BocException(e);
		}
	}

	/**
	 * Permite actualizar el monto total y la cantidad de productos del pedido,
	 * segun el <b>id</b> del pedido.
	 * 
	 * @param id_pedido
	 *            long
	 * @return boolean
	 * @throws BocException
	 */
	/*
	 * public boolean recalcPedido(long id_pedido) throws BocException,
	 * SystemException, PedidosException {
	 * 
	 * try { return pedidosBolService.recalcPedido(id_pedido); } catch
	 * (ServiceException e) { throw new BocException(e); }
	 * 
	 * }
	 */

	/**
	 * Obtiene el precio de un producto, segun el <b>id</b> del producto y el
	 * <b>id</b> del local.
	 * 
	 * @param id_local
	 *            long
	 * @param id_producto
	 *            long
	 * @return double, devuelve el precio del producto.
	 * @throws BocException
	 */
	public double getPrecioByLocalProd(long id_local, long id_producto)
			throws SystemException, PedidosException, BocException {
		try {
			return pedidosBolService
					.getPrecioByLocalProd(id_local, id_producto);
		} catch (ServiceException e) {
			throw new BocException(e);
		}
	}

	/**
	 * Permite confirmar el pedido, segun el <b>id</b> del pedido.<br>
	 * Luego de la confirmación del pedido, actualiza información en el cliente.
	 * 
	 * @param id_pedido
	 *            long
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setConfirmarOP(long id_pedido, UserDTO usr)
			throws SystemException, PedidosException, BocException {
		try {
			return pedidosBolService.setConfirmarOP(id_pedido, usr);
		} catch (ServiceException e) {
			throw new BocException(e);
		}
	}

	/**
	 * Obtiene las facturas de un pedido, segun el <b>id</b> del pedido.<br>
	 * 
	 * @param id_pedido
	 *            long
	 * @return List FacturaDTO
	 * @throws BocException
	 */
	public List getFacturasByIdPedido(long id_pedido) throws SystemException,
			PedidosException, BocException {
		try {
			return pedidosBolService.getFacturasByIdPedido(id_pedido);
		} catch (ServiceException e) {
			throw new BocException(e);
		}
	}

	/**
	 * Actualiza los datos de una factura, segun la información contenida en
	 * <b>ProcModFacturaDTO</b>.<br>
	 * 
	 * @param prm
	 *            ProcModFacturaDTO
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setModFactura(ProcModFacturaDTO prm) throws SystemException,
			PedidosException, BocException {
		try {
			return pedidosBolService.setModFactura(prm);
		} catch (ServiceException e) {
			throw new BocException(e);
		}
	}

	/*------------------------ Comunas y Locales --------------------*/

	/**
	 * Listado de Comunas
	 * 
	 * @return Lits of ComunaDTO's
	 * @throws SystemException
	 * @throws BocException
	 */
	public List getComunasAll() throws SystemException, BocException {
		try {
			return pedidosBolService.getComunasAll();
		} catch (ServiceException e) {
			throw new BocException(e);
		}
	}

	/**
	 * Obtiene listado de Zonas de despacho para una comuna
	 * 
	 * @param id_comuna
	 *            long
	 * @return List of ZonaxComunaDTO's
	 * @throws SystemException
	 * @throws BocException
	 */
	public List getZonasxComuna(long id_comuna) throws SystemException,
			BocException {
		try {
			return pedidosBolService.getZonasxComuna(id_comuna);
		} catch (ServiceException e) {
			throw new BocException(e);
		}
	}

	public List getPoligonosXComuna(long id_comuna) throws SystemException,
			BocException {
		try {
			return pedidosBolService.getPoligonosXComuna(id_comuna);
		} catch (ServiceException e) {
			throw new BocException(e);
		}
	}

	public List getPoligonosXComunaAll(long id_comuna) throws SystemException,
			BocException {
		try {
			return pedidosBolService.getPoligonosXComunaAll(id_comuna);
		} catch (ServiceException e) {
			throw new BocException(e);
		}
	}

	public PoligonoDTO getPoligonoById(long id_poligono)
			throws SystemException, BocException {
		try {
			return pedidosBolService.getPoligonoById(id_poligono);
		} catch (ServiceException e) {
			throw new BocException(e);
		}
	}

	/**
	 * Actualiza el orden de una relación comuna-zona
	 * 
	 * @param procModZonaxComuna
	 *            List
	 * @throws SystemException
	 * @throws BocException
	 */
	/*
	 * public void doActualizaOrdenZonaxComuna(List procModZonaxComuna) throws
	 * SystemException, BocException { try {
	 * System.out.println("dentro del BizDelegate");
	 * pedidosBolService.doActualizaOrdenZonaxComuna(procModZonaxComuna); }
	 * catch (ServiceException e) { throw new BocException(e); } }
	 */

	/**
	 * Obtiene información de la categoría Sap.
	 * 
	 * @param cod_cat
	 *            String
	 * @return String
	 * @throws BocException
	 */
	public String getCatSapById(String cod_cat) throws BocException {
		try {
			return contFacade.getCatSapById(cod_cat);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna listado de Sectores
	 * 
	 * @param cod_prod
	 *            long
	 * @return List LocalDTO
	 * @throws BocException
	 */
	public List getLocalesByProducto(long cod_prod) throws BocException {
		try {
			return pedidosBolService.getLocalesByProducto(cod_prod);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna listado de Sectores
	 * 
	 * @return List SectorLocalDTO
	 * @throws BocException
	 */
	public List getSectores() throws BocException {
		try {
			return pedidosBolService.getSectores();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	public boolean setDelProductoXSector(long id_producto) throws BocException {
		try {
			return pedidosBolService.setDelProductoXSector(id_producto);
		} catch (Exception ex) { // RemoteException ex
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	public boolean setAddProductoXSector(long id_producto, long id_sector)
			throws BocException {
		try {
			return pedidosBolService.setAddProductoXSector(id_producto,
					id_sector);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Obtener el sector, segun producto y local
	 * 
	 * @param id_producto
	 * @return long
	 * @throws BocException
	 */
	public long getSectorByProd(long id_producto) throws BocException {
		try {
			return pedidosBolService.getSectorByProd(id_producto);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Obtener el listado de las politicas de sustitucion
	 * 
	 * @return List
	 * @throws BocException
	 */
	public List getPolitSustitucionAll() throws BocException {
		try {
			return pedidosBolService.getPolitSustitucionAll();
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Modifica la politica de sustitucion de un pedido
	 * 
	 * @param prm
	 *            ProcModPedidoPolSustDTO
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setModPedidoPolSust(ProcModPedidoPolSustDTO prm)
			throws BocException {
		try {
			return pedidosBolService.setModPedidoPolSust(prm);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Agregar la relación entre el usuario y el local
	 * 
	 * @param id_user
	 * @param id_loc
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setModLocUser(ProcModUserDTO dto) throws BocException {
		try {
			return usuariosService.setModLocUser(dto);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene la categoria Sap, segun el codigo
	 * 
	 * @param id_cat
	 * @return CategoriaSapDTO
	 * @throws BocException
	 */
	public CategoriaSapDTO getCategoriaSapById(String id_cat)
			throws BocException {
		try {
			return contFacade.getCategoriaSapById(id_cat);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/*
	 * *************************************************************************************************************
	 * ------------------------------Monitor de
	 * Campanias------------------------------
	 * **********************************
	 * ****************************************
	 * ***********************************
	 */

	/**
	 * Obtiene el listado de campanias, segun los criterios ingresados
	 * 
	 * @param criterio
	 * @return List CampanaDTO
	 * @throws BocException
	 */
	public List getCampanasByCriteria(CampanasCriteriaDTO criterio)
			throws BocException {
		try {
			return contFacade.getCampanasByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene la cantidad de campanias, segun los criterios ingresados
	 * 
	 * @param criterio
	 * @return int
	 * @throws BocException
	 */
	public int getCountCampanasByCriteria(CampanasCriteriaDTO criterio)
			throws BocException {
		try {
			return contFacade.getCountCampanasByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene informacion de la campania, segun codigo
	 * 
	 * @param id_campana
	 * @return CampanaDTO
	 * @throws BocException
	 */
	public CampanaDTO getCampanaById(long id_campana) throws BocException {
		try {
			return contFacade.getCampanaById(id_campana);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Actualiza la informacion de campania
	 * 
	 * @param dto
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setModCampana(ProcModCampanaDTO dto) throws BocException {
		try {
			return contFacade.setModCampana(dto);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Modifica la relacion entre Campaña y elemento, agrega o elimina
	 * 
	 * @param dto
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setModCampanaElemento(ProcModCampElementoDTO dto)
			throws BocException {
		try {
			return contFacade.setModCampanaElemento(dto);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Agrega una nueva campaña
	 * 
	 * @param dto
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setAddCampana(ProcAddCampanaDTO dto) throws BocException {
		try {
			return contFacade.setAddCampana(dto);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Elimina una campaña
	 * 
	 * @param dto
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setDelCampana(ProcDelCampanaDTO dto) throws BocException {
		try {
			return contFacade.setDelCampana(dto);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/*
	 * *************************************************************************************************************
	 * ------------------------------Monitor de
	 * Elementos------------------------------
	 * **********************************
	 * ****************************************
	 * ***********************************
	 */

	/**
	 * Obtiene informacion del elemento, segun codigo
	 * 
	 * @param id_elemento
	 * @return ElementoDTO
	 * @throws BocException
	 */
	public ElementoDTO getElementoById(long id_elemento) throws BocException {
		try {
			return contFacade.getElementoById(id_elemento);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene el listado de elementos, segun los criterios ingresados
	 * 
	 * @param criterio
	 * @return List ElementoDTO
	 * @throws BocException
	 */
	public List getElementosByCriteria(ElementosCriteriaDTO criterio)
			throws BocException {
		try {
			return contFacade.getElementosByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene el listado de los umbrales segun criterio de busqueda indra
	 * 
	 * @param criterio
	 * @return List ElementosCriteriaDTO
	 * @throws BocException
	 */
	public List getParametrosByCriteria(ElementosCriteriaDTO criterio)
			throws BocException {
		try {
			return umbralService.getParametrosByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	// indra

	/**
	 * Obtiene la cantidad de elementos , segun los criterios ingresados
	 * 
	 * @param criterio
	 * @return int
	 * @throws BocException
	 */
	public int getCountElementosByCriteria(ElementosCriteriaDTO criterio)
			throws BocException {
		try {
			return contFacade.getCountElementosByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Actualiza la informacion de elemento
	 * 
	 * @param dto
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setModElemento(ProcModElementoDTO dto) throws BocException {
		try {
			return contFacade.setModElemento(dto);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene el listado de los tipos de elementos
	 * 
	 * @return List TipoElementoDTO
	 * @throws BocException
	 */
	public List getLstTipoElementos() throws BocException {
		try {
			return contFacade.getLstTipoElementos();
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Agregar un nuevo elemento
	 * 
	 * @param dto
	 * @return long
	 * @throws BocException
	 */
	public long setAddElemento(ProcAddElementoDTO dto) throws BocException {
		try {
			return contFacade.setAddElemento(dto);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Elimina un elemento de la BD.
	 * 
	 * @param dto
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setDelElemento(ProcDelElementoDTO dto) throws BocException {
		try {
			return contFacade.setDelElemento(dto);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Cambia estado del pedido a 'En Pago'
	 * 
	 * @param coll
	 * @return boolean
	 * @throws BolException
	 */
	public boolean setCambiarEnPagoOP(CambEnPagoOPDTO coll) throws BocException {
		try {
			return pedidosBolService.setCambiarEnPagoOP(coll);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/*
	 * *************************************************************************************************************
	 * ------------------------------Monitor de Empresas
	 * ------------------------------
	 * *******************************************
	 * ******************************************************************
	 */

	/**
	 * Obtiene el listado de empresas, segun el criterio de busqueda
	 * seleccionado
	 * 
	 * @param criterio
	 * @return List EmpresasDTO
	 * @throws BocException
	 */
	public List getEmpresasByCriteria(EmpresaCriteriaDTO criterio)
			throws BocException {
		try {
			return empresasService.getEmpresasByCriteria(criterio);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Recupera el Listado de Horas de Inicio e Jornadas de Despacho
	 * (Modificación según req. 567)
	 * 
	 * @return List
	 * @throws BolException
	 */
	public List getHorasInicioJDespacho() throws BocException {
		try {
			return pedidosBolService.getHorasInicioJDespacho();
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene la cantidad de empresas que cumple los criterios de busqueda
	 * seleccionados
	 * 
	 * @param criterio
	 * @return int
	 * @throws BocException
	 */
	public int getEmpresasCountByCriteria(EmpresaCriteriaDTO criterio)
			throws BocException {
		try {
			return empresasService.getEmpresasCountByCriteria(criterio);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Agrega una empresa, segun los datos ingresados en el formulario
	 * 
	 * @param prm
	 * @return boolean
	 * @throws BocException
	 */
	public long setCreaEmpresa(EmpresasDTO prm) throws BocException {
		try {
			return empresasService.setCreaEmpresa(prm);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene información de la empresa , segun el id
	 * 
	 * @param id
	 * @return EmpresasDTO
	 * @throws BocException
	 */
	public EmpresasDTO getEmpresaById(long id) throws BocException {
		try {
			return empresasService.getEmpresaById(id);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Actualiza la información de la empresa, segun los datos ingresados en el
	 * formulario
	 * 
	 * @param prm
	 * @return
	 * @throws BocException
	 */
	public boolean setModEmpresa(EmpresasDTO prm) throws BocException {
		try {
			return empresasService.setModEmpresa(prm);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/*
	 * *************************************************************************************************************
	 * ------------------------------Monitor de Sucursales
	 * ------------------------------
	 * *******************************************
	 * ******************************************************************
	 */

	public List getSucursalesByCriteria(SucursalCriteriaDTO criterio)
			throws BocException {
		try {
			return empresasService.getSucursalesByCriteria(criterio);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	public double getSucursalesCountByCriteria(SucursalCriteriaDTO criterio)
			throws BocException {
		try {
			return empresasService.getSucursalesCountByCriteria(criterio);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	public List getSucursalesByEmpresaId(long id_empresa) throws BocException {
		try {
			return empresasService.getSucursalesByEmpresaId(id_empresa);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	public long setCreaSucursal(SucursalesDTO prm) throws BocException {
		try {
			return empresasService.setCreaSucursal(prm);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	public SucursalesDTO getSucursalById(long id_sucursal) throws BocException {
		try {
			return empresasService.getSucursalById(id_sucursal);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	public boolean setModSucursal(SucursalesDTO prm) throws BocException {
		try {
			return empresasService.setModSucursal(prm);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Marca como eliminada una sucursal
	 * 
	 * @param prm
	 * @return
	 * @throws BocException
	 */
	public boolean setDelSucursal(SucursalesDTO prm) throws BocException {
		try {
			return empresasService.delSucursal(prm);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/*
	 * ****************************************************************************
	 * Monitor de Cotizaciones
	 * **************************************************
	 * *************************
	 */

	/**
	 * Obtiene un listado de cotizaciones de acuerdo a un criterio determinado
	 * 
	 * @param criterio
	 * @return lista
	 * @throws BocException
	 */
	public List getCotizacionesByCriteria(CotizacionesCriteriaDTO criterio)
			throws BocException {
		try {
			return cotizacionesService.getCotizacionesByCriteria(criterio);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado de los locales
	 * 
	 * @return List LocalDTO
	 * @throws BocException
	 */
	public List getLocalesCotizacion() throws BocException {
		try {
			return cotizacionesService.getLocales();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un listado de los estados de las cotizaciones
	 * 
	 * @return List EstadoDTO
	 * @throws BocException
	 */
	public List getEstadosCotizacion() throws BocException {
		try {
			return cotizacionesService.getEstadosCotizaciones();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna la cantidad de pedidos, segun el criterio
	 * 
	 * @param criterio
	 *            PedidosCriteriaDTO
	 * @return long
	 * @throws BocException
	 */
	public long getCountCotizacionesByCriteria(CotizacionesCriteriaDTO criterio)
			throws BocException {
		try {
			return cotizacionesService.getCountCotizacionesByCriteria(criterio);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna el detalle de la cotización segun su id
	 * 
	 * @param cot_id
	 * @return CotizacionesDTO
	 * @throws BocException
	 */
	public CotizacionesDTO getCotizacionesById(long cot_id) throws BocException {
		try {
			return cotizacionesService.getCotizacionesById(cot_id);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene la cantidad de productos asociados a una cotización a partir de
	 * su Id.
	 * 
	 * @param cot_id
	 * @return long
	 * @throws BocException
	 */
	public long getCountProductosEnCotizacionById(long cot_id)
			throws BocException {
		try {
			return cotizacionesService
					.getCountProductosEnCotizacionById(cot_id);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna un listado con las alertas de las cotizaciones de acuerdo a su
	 * Id.
	 * 
	 * @param id_cot
	 * @return List AlertasDTO
	 * @throws BocException
	 */
	public List getAlertasCotizacion(long id_cot) throws BocException {
		try {
			return cotizacionesService.getAlertasCotizacion(id_cot);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna una lista con los productos asociados a una cotización
	 * 
	 * @param id_cot
	 * @return
	 * @throws BocException
	 */
	public List getProductosCotiz(long id_cot) throws BocException {
		try {
			return cotizacionesService.getProductosCotiz(id_cot);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna una lista con los pedidos asociados a una cotización
	 * 
	 * @param id_cot
	 * @return
	 * @throws BocException
	 */
	public List getPedidosCotiz(long id_cot) throws BocException {
		try {
			return cotizacionesService.getPedidosCotiz(id_cot);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna una lista con los logs asociados a una cotización a partir de su
	 * ID.
	 * 
	 * @param id_cot
	 * @return List LogsCotizacionesDTO
	 * @throws BocException
	 */
	public List getLogsCotiz(long id_cot) throws BocException {
		try {
			return cotizacionesService.getLogsCotiz(id_cot);
		} catch (Exception ex) {
			throw new BocException(ex);
		}

	}

	/**
	 * Obtiene el listado de productos de cotizacion, incluyendo pedido y
	 * cantidades en el pedido
	 * 
	 * @param id_cotizacion
	 * @return List ProductosCotizacionesDTO
	 * @throws BocException
	 */
	public List getLstProductosByCotizacion(long id_cotizacion)
			throws BocException {
		try {
			return cotizacionesService
					.getLstProductosByCotizacion(id_cotizacion);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Insertar el pedido
	 * 
	 * @param pedido
	 * @return long id del nuevo pedido
	 * @throws BocException
	 */
	/*
	 * public long doInsPedido(ProcInsPedidoDTO pedido) throws BocException {
	 * try { //Si se llega a usar este metodo, el segundo parametro indica si el
	 * clientes esta en la lista de fo_cli_confiables return
	 * pedidosBolService.doInsPedido(pedido, false); } catch (Exception ex) {
	 * //RemoteException ex throw new BocException(ex); } }
	 */

	/**
	 * Obtiene un listado con las Zonas de Despacho de un Local
	 * 
	 * @param id_local
	 * @return List ZonaDTO
	 * @throws BocException
	 */
	public List getZonasLocal(long id_local) throws BocException {
		try {
			return pedidosBolService.getZonasLocal(id_local);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	public long doGeneraPedido(ProcInsPedidoDTO pedido) throws BocException {
		try {
			return pedidosBolService.doGeneraPedido(pedido);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Permite actualizar observaciones, productos fuera de mix y costo de
	 * despacho de una cotización
	 * 
	 * @param id_cot
	 * @param obs
	 * @param fueraMix
	 * @param costo_despacho
	 * @return
	 * @throws BocException
	 */
	public boolean setModCotizacion(ModCotizacionDTO cot) throws BocException {
		try {
			return cotizacionesService.setModCotizacion(cot);
		} catch (Exception ex) {
			throw new BocException(ex);
		}

	}

	/**
	 * Permite anular una cotización
	 * 
	 * @param id_cot
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setAnularCotizacion(long id_cot) throws BocException {
		try {
			return cotizacionesService.setAnularCotizacion(id_cot);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	public boolean addLogCotizacion(LogsCotizacionesDTO log)
			throws BocException {
		try {
			return cotizacionesService.addLogCotizacion(log);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Permite agregar un producto a una cotización
	 * 
	 * @param prod
	 * @return
	 * @throws BocException
	 */
	public boolean addProductoCotizacion(ProductosCotizacionesDTO prod)
			throws BocException {
		try {
			return cotizacionesService.addProductoCotizacion(prod);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Permite eliminar un producto de una cotización
	 * 
	 * @param detcot_id
	 * @return
	 * @throws BocException
	 */
	public boolean delProductoCotizacion(long detcot_id) throws BocException {
		try {
			return cotizacionesService.delProductoCotizacion(detcot_id);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene el precio de un producto segun su id y id de local
	 * 
	 * @param id_prod
	 * @param id_loc
	 * @return
	 * @throws BocException
	 */
	public double getProductosPrecios(long id_prod, long id_loc)
			throws BocException {
		try {
			return contFacade.getProductosPrecios(id_prod, id_loc);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Permite caducar cotizaciones con fecha superior a la fecha de expiración
	 * 
	 * @return
	 * @throws BocException
	 */
	public boolean doCaducaCotizaciones(long id_comprador) throws BocException {
		try {
			return cotizacionesService.CaducarCotizaciones(id_comprador);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Permite modificar los datos de un producto asociado a una cotización
	 * 
	 * @param prod
	 * @return
	 * @throws BocException
	 */
	public boolean updProductoCotizacion(ModProdCotizacionesDTO prod)
			throws BocException {
		try {
			return cotizacionesService.updProductoCotizacion(prod);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Permite validar una cotizacion y mostrar las alertas que cumple.
	 * 
	 * @param id_cotizacion
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setValidarCotizacion(long id_cotizacion) throws BocException {
		try {
			return cotizacionesService.setValidarCotizacion(id_cotizacion);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}

	/**
	 * Permite confirmar la cotizacion y cambiar estado a Cotizada.
	 * 
	 * @param id_cotizacion
	 * @param usr
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setConfirmarCotizacion(long id_cotizacion, UserDTO usr)
			throws BocException {
		try {
			return cotizacionesService.setConfirmarCotizacion(id_cotizacion,
					usr);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Asigna una cotización
	 * 
	 * @param col
	 * @return
	 * @throws BocException
	 */
	public boolean setAsignaCotizacion(AsignaCotizacionDTO col)
			throws BocException {
		try {
			return cotizacionesService.setAsignaCotizacion(col);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna si la cotización fue liberada o no.
	 * 
	 * @param col
	 *            AsignaCotizacionDTO
	 * @return boolean, devuelve true en el caso que la liberación fue
	 *         satisfactoria, caso contrario devuelve false.
	 * @throws BocException
	 * 
	 */
	public boolean setLiberaCotizacion(AsignaCotizacionDTO col)
			throws BocException {
		try {
			return cotizacionesService.setLiberaCotizacion(col);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna la suma de las cantidades de productos asociados a una
	 * cotización.
	 * 
	 * @param id_cot
	 * @return double
	 * @throws BocException
	 */
	public double getTotalProductosCot(long id_cot) throws BocException {
		try {
			return cotizacionesService.getTotalProductosCot(id_cot);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/*
	 * *************************************************************************************************************
	 * ------------------------------Monitor de Compradores
	 * ------------------------------
	 * *******************************************
	 * ******************************************************************
	 */
	/**
	 * Obtiene el listado de compradores de una sucursal
	 * 
	 * @param id_sucursal
	 * @return List CompradoresDTO
	 * @throws BocException
	 */
	public List getListCompradoresBySucursalId(long id_sucursal)
			throws BocException {
		try {
			return empresasService.getListCompradoresBySucursalId(id_sucursal);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene la información del comprador, segun id del comprador
	 * 
	 * @param comprador_id
	 * @return CompradoresDTO
	 * @throws BocException
	 */
	public CompradoresDTO getCompradoresById(long comprador_id)
			throws BocException {
		try {
			return empresasService.getCompradoresById(comprador_id);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Agrega un nuevo comprador y su relacion con una sucursal
	 * 
	 * @param comprador
	 * @return boolean
	 * @throws BocException
	 */
	public long setCreaComprador(CompradoresDTO comprador) throws BocException {
		try {
			return empresasService.setCreaComprador(comprador);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Actualiza la informacion del comprador, segun los datos contenidos en el
	 * DTO
	 * 
	 * @param comprador
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setModComprador(CompradoresDTO comprador)
			throws BocException {
		try {
			return empresasService.setModComprador(comprador);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene el listado de compradores que coinciden con los criterios de
	 * busqueda
	 * 
	 * @param dto
	 * @return List CompradoresDTO
	 * @throws BocException
	 */
	public List getCompradoresByCriteria(CompradorCriteriaDTO dto)
			throws BocException {
		try {
			return empresasService.getCompradoresByCriteria(dto);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene la cantidad total de compradores que coinciden con los criterios
	 * de busqueda
	 * 
	 * @param dto
	 * @return int
	 * @throws BocException
	 */
	public int getCompradoresCountByCriteria(CompradorCriteriaDTO dto)
			throws BocException {
		try {
			return empresasService.getCompradoresCountByCriteria(dto);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene el listado de las sucursales relacionadas a un comprador, segun
	 * id
	 * 
	 * @param id_comprador
	 * @return List ComprXSucDTO
	 * @throws BocException
	 */
	public List getListSucursalesByCompradorId(long id_comprador)
			throws BocException {
		try {
			return empresasService.getListSucursalesByCompradorId(id_comprador);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Agrega la relacion entre sucursal y comprador
	 * 
	 * @param prm
	 * @return boolean
	 * @throws BocException
	 */
	public boolean addRelSucursalComprador(ComprXSucDTO prm)
			throws BocException {
		try {
			return empresasService.addRelSucursalComprador(prm);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Elimina la relacion entre sucursal y comprador
	 * 
	 * @param prm
	 * @return
	 * @throws BocException
	 */
	public boolean delRelSucursalComprador(ComprXSucDTO prm)
			throws BocException {
		try {
			return empresasService.delRelSucursalComprador(prm);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene el listado de empresas donde el comprador es el administrador
	 * 
	 * @param id_comprador
	 * @return List EmpresasDTO
	 * @throws BocException
	 */
	public List getListAdmEmpresasByCompradorId(long id_comprador)
			throws BocException {
		try {
			return empresasService
					.getListAdmEmpresasByCompradorId(id_comprador);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Agrega la relacion entre la empresa y el comprador
	 * 
	 * @param dto
	 * @return boolean
	 * @throws BocException
	 */
	public boolean addRelEmpresaComprador(ComprXEmpDTO dto) throws BocException {
		try {
			return empresasService.addRelEmpresaComprador(dto);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Elimina la relacion entre la empresa y el comprador
	 * 
	 * @param dto
	 * @return boolean
	 * @throws BocException
	 */
	public boolean delRelEmpresaComprador(ComprXEmpDTO dto) throws BocException {
		try {
			return empresasService.delRelEmpresaComprador(dto);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Permite verificar si un comprador es administrador
	 * 
	 * @param id_comprador
	 * @param id_sucursal
	 * @return
	 * @throws BocException
	 */
	public boolean esAdministrador(long id_comprador, long id_sucursal)
			throws BocException {
		try {
			return empresasService.esAdministrador(id_comprador, id_sucursal);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/*
	 * *************************************************************************************************************
	 * ------------------------------Metodos de Direcciones
	 * ------------------------------
	 * *******************************************
	 * ******************************************************************
	 */

	/**
	 * Obtiene el listado de direcciones de despacho de una sucursal
	 * 
	 * @param id_sucursal
	 * @return List DireccionesDTO
	 * @throws BocException
	 */
	public List getListDireccionDespBySucursal(long id_sucursal)
			throws BocException {
		try {
			return empresasService.getListDireccionDespBySucursal(id_sucursal);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Inserta una nueva direccion de despacho de una sucursal, segun datos
	 * contenidos en el DTO
	 * 
	 * @param prm
	 * @return long
	 * @throws BocException
	 */
	public long insDirDespacho(cl.bbr.vte.empresas.dto.DireccionesDTO prm)
			throws BocException {
		try {
			return empresasService.insDirDespacho(prm);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Modifica la direccion de despacho de una sucursal, segun datos contenidos
	 * en el DTO
	 * 
	 * @param prm
	 * @return boolean
	 * @throws BocException
	 */
	public boolean modDirDespacho(cl.bbr.vte.empresas.dto.DireccionesDTO prm)
			throws BocException {
		try {
			return empresasService.modDirDespacho(prm);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Elimina la direccion de despacho, cambiando el estado, segun el id de
	 * direccion
	 * 
	 * @param id_dir
	 * @return boolean
	 * @throws BocException
	 */
	public boolean delDirDespacho(long id_dir) throws BocException {
		try {
			return empresasService.delDirDespacho(id_dir);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene el listado de direcciones de facturacion de una sucursal
	 * 
	 * @param id_sucursal
	 * @return List DirFacturacionDTO
	 * @throws BocException
	 */
	public List getListDireccionFactBySucursal(long id_sucursal)
			throws BocException {
		try {
			return empresasService.getListDireccionFactBySucursal(id_sucursal);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene informacion de direccion de despacho de una sucursal, segun el id
	 * de direccion
	 * 
	 * @param id_dir
	 * @return DireccionesDTO
	 * @throws BocException
	 */
	public DireccionEntity getDireccionesDespByIdDesp(long id_dir)
			throws BocException {
		try {
			return empresasService.getDireccionesDespByIdDesp(id_dir);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene informacion de direccion de facturación de una sucursal, segun el
	 * id de direccion
	 * 
	 * @param id_dir
	 * @return DirFacturacionDTO
	 * @throws BocException
	 */
	public DirFacturacionDTO getDireccionesFactByIdFact(long id_dir)
			throws BocException {
		try {
			return empresasService.getDireccionesFactByIdFact(id_dir);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Agrega una nueva direccion de facturacion de una sucursal
	 * 
	 * @param dto
	 * @return long
	 * @throws BocException
	 */
	public long insDirFacturacion(DirFacturacionDTO dto) throws BocException {
		try {
			return empresasService.insDirFacturacion(dto);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Elimina la direccion de facturacion de una sucursal, cambiando de estado
	 * 
	 * @param id_dir
	 * @return boolean
	 * @throws BocException
	 */
	public boolean delDirFacturacion(long id_dir) throws BocException {
		try {
			return empresasService.delDirFacturacion(id_dir);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Actualiza los datos de direccion de facturacion de una sucursal, segun
	 * datos contenidos en el DTO
	 * 
	 * @param dto
	 * @return boolean
	 * @throws BocException
	 */
	public boolean modDirFacturacion(DirFacturacionDTO dto) throws BocException {
		try {
			return empresasService.modDirFacturacion(dto);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene el saldo pendiente actualizado de la Empresa pedidos pendientes
	 * (sin tomar en cuenta los anulados)
	 * 
	 * @param id_empresa
	 * @return double saldo pendientes
	 * @throws BocException
	 */
	public double getSaldoActualPendiente(long id_empresa) throws BocException {
		try {
			return empresasService.getSaldoActualPendiente(id_empresa);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Permite modificar el estado de una cotización
	 * 
	 * @param id_cot
	 * @param id_estado
	 * @return
	 * @throws BocException
	 */
	public boolean setModEstadoCotizacion(long id_cot, long id_estado)
			throws BocException {
		try {
			return cotizacionesService
					.setModEstadoCotizacion(id_cot, id_estado);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene la jornada de despacho segun su id
	 * 
	 * @param id_jornada
	 * @return
	 * @throws BocException
	 */
	public JorDespachoCalDTO getJornadaDespachoById(long id_jornada)
			throws BocException {
		try {
			return pedidosBolService.getJornadaDespachoById(id_jornada);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene un local de acuerdo a su id
	 * 
	 * @param id_local
	 * @return
	 * @throws BocException
	 */
	public LocalDTO getLocalById(long id_local) throws BocException {
		try {
			return clientesService.getLocalById(id_local);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene el detalle de una jornada de picking
	 * 
	 * @param id_jornada
	 * @return
	 * @throws BocException
	 */
	public JornadaDTO getJornadaById(long id_jornada) throws BocException {
		try {
			return pedidosBolService.getJornadaById(id_jornada);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Entrega una lista de compradores segun Id de empresa
	 * 
	 * @param id_empresa
	 * @return List
	 * @throws BocException
	 */
	public List getListCompradoresByEmpresalId(long id_empresa)
			throws BocException {
		try {
			return empresasService.getListCompradoresByEmpresalId(id_empresa);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	/**
	 * Retorna listado de casos segun el criterio
	 * 
	 * @param criterio
	 *            CasosCriterioDTO
	 * @return List MonitorCasosDTO
	 * @throws BocException
	 */
	public List getCasosByCriterio(CasosCriterioDTO criterio)
			throws BocException {
		try {
			return casosService.getCasosByCriterio(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna listado de estados de casos
	 * 
	 * @return List EstadoCasoDTO
	 * @throws BocException
	 */
	public List getEstadosDeCasos() throws BocException {
		try {
			return casosService.getEstadosDeCasos();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna cantidad de casos segun el criterio de busqueda
	 * 
	 * @param criterio
	 *            Busqueda
	 * @return Cantidad de casos
	 */
	public double getCountCasosByCriterio(CasosCriterioDTO criterio)
			throws BocException {
		try {
			return casosService.getCountCasosByCriterio(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Nos devuelve el ID del caso que esta editando un usuario
	 * 
	 * @param usr
	 *            Usuario
	 * @return ID del caso en edicion. Retorna '0' si no está editando ningun
	 *         caso
	 */
	public long getCasoEnEdicionByUsuario(UserDTO usr) throws BocException {
		try {
			return casosService.getCasoEnEdicionByUsuario(usr);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Libera el caso que esta en edición
	 * 
	 * @param idCaso
	 *            ID del caso que será liberado
	 * @return True o False según el resultado de la operación
	 */
	public boolean setLiberaCaso(long idCaso) throws BocException {
		try {
			return casosService.setLiberaCaso(idCaso);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene los datos de un caso con su ID
	 * 
	 * @param idCaso
	 *            ID Caso
	 * @return MonitorCasosDTO
	 */
	public CasoDTO getCasoByIdCaso(long idCaso) throws BocException {
		try {
			return casosService.getCasoByIdCaso(idCaso);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna un listado con los productos de un caso
	 * 
	 * @param idCaso
	 *            ID de Caso
	 * @param tipo
	 *            Tipo: Productos a Enviar = 'S', Productos a Retirar = 'N'
	 * @return Listado de Productos
	 */
	public List getProductosByCasoAndTipo(long idCaso, String tipo)
			throws BocException {
		try {
			return casosService.getProductosByCasoAndTipo(idCaso, tipo);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna los datos de un pedido
	 * 
	 * @param idPedido
	 * @return PedidoCasoDTO
	 */
	public PedidoCasoDTO getPedidoById(long idPedido) throws BocException {
		try {
			return casosService.getPedidoById(idPedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Deja en estado de edicion al caso
	 * 
	 * @param idCaso
	 * @param id_usuario
	 * @return
	 */
	public boolean setModCaso(long idCaso, long id_usuario) throws BocException {
		try {
			return casosService.setModCaso(idCaso, id_usuario);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna un listado con las jornadas
	 * 
	 * @return Listado de Jornadas
	 */
	public List getJornadas() throws BocException {
		try {
			return casosService.getJornadas();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna un listado con las jornadas
	 * 
	 * @return Listado de Jornadas
	 */
	public List getJornadasDespachoByFecha(String fecha, int local)
			throws BocException {
		try {
			return pedidosBolService.getJornadasDespachoByFecha(fecha, local);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Ingresar un nuevo caso
	 * 
	 * @param caso
	 *            MonitorCasosDTO con los datos de un caso
	 * @return ID del nuevo caso registrado
	 */
	public long addCaso(CasoDTO caso) throws BocException, ServiceException,
			SystemException {
		try {
			return casosService.addCaso(caso);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna un listado con los quiebres registrados en el sistema
	 * 
	 * @return
	 */
	public List getQuiebres() throws BocException {
		try {
			return casosService.getQuiebres();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna un listado con las responsables registradas en el sistema
	 * 
	 * @return
	 */
	public List getResponsables() throws BocException {
		try {
			return casosService.getResponsables();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param producto
	 * @return
	 */
	public long addProductoCaso(ProductoCasoDTO producto) throws BocException,
			ServiceException, SystemException {
		try {
			return casosService.addProductoCaso(producto);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	/**
	 * @param producto
	 * @return
	 */
	public boolean modProductoCaso(ProductoCasoDTO producto)
			throws BocException {
		try {
			return casosService.modProductoCaso(producto);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param idProducto
	 * @return
	 */
	public ProductoCasoDTO getProductoById(long idProducto) throws BocException {
		try {
			return casosService.getProductoById(idProducto);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param idProducto
	 * @return
	 */
	public boolean delProductoCaso(long idProducto) throws BocException {
		try {
			return casosService.delProductoCaso(idProducto);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param caso
	 * @return
	 */
	public boolean modCaso(CasoDTO caso) throws BocException {
		try {
			return casosService.modCaso(caso);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param log
	 * @return
	 */
	public void addLogCaso(LogCasosDTO log) throws BocException {
		try {
			casosService.addLogCaso(log);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param idCaso
	 * @return
	 */
	public List getLogCaso(long idCaso) throws BocException {
		try {
			return casosService.getLogCaso(idCaso);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param idQuiebre
	 * @return
	 */
	public QuiebreCasoDTO getQuiebreById(long idQuiebre) throws BocException {
		try {
			return casosService.getQuiebreById(idQuiebre);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param idObjeto
	 * @return
	 */
	public ObjetoDTO getResponsableById(long idResponsable) throws BocException {
		try {
			return casosService.getResponsableById(idResponsable);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param quiebre
	 * @return
	 */
	public long addQuiebre(QuiebreCasoDTO quiebre) throws BocException,
			ServiceException, SystemException {
		try {
			return casosService.addQuiebre(quiebre);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	/**
	 * @param quiebre
	 */
	public void modQuiebre(QuiebreCasoDTO quiebre) throws BocException,
			ServiceException, SystemException {
		try {
			casosService.modQuiebre(quiebre);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	/**
	 * @param idObjeto
	 */
	public void delQuiebre(long idQuiebre) throws BocException,
			ServiceException, SystemException {
		try {
			casosService.delQuiebre(idQuiebre);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	/**
	 * @param responsable
	 * @return
	 */
	public long addResponsable(ObjetoDTO responsable) throws BocException,
			ServiceException, SystemException {
		try {
			return casosService.addResponsable(responsable);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	/**
	 * @param responsable
	 */
	public void modResponsable(ObjetoDTO responsable) throws BocException,
			ServiceException, SystemException {
		try {
			casosService.modResponsable(responsable);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	/**
	 * @param idObjeto
	 */
	public void delResponsable(long idResponsable) throws BocException,
			ServiceException, SystemException {
		try {
			casosService.delResponsable(idResponsable);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	/**
	 * @param jornada
	 * @return
	 */
	public long addJornada(cl.bbr.jumbocl.casos.dto.JornadaDTO jornada)
			throws BocException, ServiceException, SystemException {
		try {
			return casosService.addJornada(jornada);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	/**
	 * @param jornada
	 */
	public void modJornada(cl.bbr.jumbocl.casos.dto.JornadaDTO jornada)
			throws BocException, ServiceException, SystemException {
		try {
			casosService.modJornada(jornada);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	/**
	 * @param idObjeto
	 * @return
	 */
	public cl.bbr.jumbocl.casos.dto.JornadaDTO getJornadaCasoById(long idJornada)
			throws BocException {
		try {
			return casosService.getJornadaCasoById(idJornada);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param idObjeto
	 */
	public void delJornadaDeCaso(long idJornada) throws BocException,
			ServiceException, SystemException {
		try {
			casosService.delJornadaDeCaso(idJornada);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	/**
	 * Retorna un listado con las jornadas, dependiendo del estado
	 * 
	 * @param estado
	 * @return Jornadas
	 */
	public List getJornadasByEstado(String estado) throws BocException {
		try {
			return casosService.getJornadasByEstado(estado);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param string
	 * @return
	 */
	public List getQuiebresByEstado(String estado) throws BocException {
		try {
			return casosService.getQuiebresByEstado(estado);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param string
	 * @return
	 */
	public List getResponsablesByEstado(String estado) throws BocException {
		try {
			return casosService.getResponsablesByEstado(estado);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idCaso
	 * @return
	 */
	public long getIdUsuarioEditorDeCaso(long idCaso) throws BocException {
		try {
			return casosService.getIdUsuarioEditorDeCaso(idCaso);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idPedido
	 * @return
	 */
	public List getCasosByOp(long idPedido) throws BocException {
		try {
			return casosService.getCasosByOp(idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param fechaIni
	 * @param fechaFin
	 * @return
	 */
	public List getTablaReclamosClientes(String fechaIni, String fechaFin,
			Hashtable llaves) throws BocException {
		try {
			return casosService.getTablaReclamosClientes(fechaIni, fechaFin,
					llaves);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param mail
	 */
	public void enviarMailSupervisor(MailDTO mail) throws BocException {
		try {
			casosService.enviarMailSupervisor(mail);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param cliRut
	 * @return
	 */
	public long getNroComprasByCliente(long rutCliente) throws BocException {
		try {
			return casosService.getNroComprasByCliente(rutCliente);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param cliRut
	 * @return
	 */
	public long getNroCasosByCliente(long rutCliente) throws BocException {
		try {
			return casosService.getNroCasosByCliente(rutCliente);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param criterio
	 * @return
	 */
	public List getEventosByCriterio(EventosCriterioDTO criterio)
			throws BocException {
		try {
			return eventosService.getEventosByCriterio(criterio);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param criterio
	 * @return
	 */
	public double getCountEventosByCriterio(EventosCriterioDTO criterio)
			throws BocException {
		try {
			return eventosService.getCountEventosByCriterio(criterio);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 */
	public List getTiposEventos() throws BocException {
		try {
			return eventosService.getTiposEventos();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param usr
	 * @return
	 */
	public long getEventoEnEdicionByUsuario(UserDTO usr) throws BocException {
		try {
			return eventosService.getEventoEnEdicionByUsuario(usr);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 */
	public List getPasos() throws BocException {
		try {
			return eventosService.getPasos();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param evento
	 * @return
	 */
	public long addEvento(EventoDTO evento) throws BocException,
			ServiceException, SystemException {
		try {
			return eventosService.addEvento(evento);
		} catch (ServiceException ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idEvento
	 * @return
	 */
	public EventoDTO getEvento(long idEvento) throws BocException {
		try {
			return eventosService.getEvento(idEvento);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idEvento
	 * @param id_usuario
	 * @return
	 */
	public boolean setModEvento(long idEvento, long idUsuario)
			throws BocException {
		try {
			return eventosService.setModEvento(idEvento, idUsuario);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param evento
	 */
	public void modEvento(EventoDTO evento) throws BocException {
		try {
			eventosService.modEvento(evento);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idEvento
	 * @return
	 */
	public boolean setLiberaEvento(long idEvento) throws BocException {
		try {
			return eventosService.setLiberaEvento(idEvento);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param evento
	 * @return
	 */
	public boolean existeEvento(EventoDTO evento) throws BocException {
		try {
			return eventosService.existeEvento(evento);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param evento
	 */
	public void delEvento(EventoDTO evento) throws BocException {
		try {
			eventosService.delEvento(evento);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idEvento
	 * @return
	 */
	public long getIdUsuarioEditorDeEvento(long idEvento) throws BocException {
		try {
			return eventosService.getIdUsuarioEditorDeEvento(idEvento);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idEvento
	 * @return
	 */
	public boolean eventoUtilizado(long idEvento) throws BocException {
		try {
			return eventosService.eventoUtilizado(idEvento);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param ruts
	 * @param idEvento
	 * @return
	 */
	public String addRutsEvento(Vector ruts, EventoDTO evento)
			throws BocException, ServiceException, SystemException {
		try {
			return eventosService.addRutsEvento(ruts, evento);
		} catch (ServiceException ex) {
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	/**
	 * @param idEvento
	 * @return
	 */
	public List getRutsByEvento(long idEvento) throws BocException {
		try {
			return eventosService.getRutsByEvento(idEvento);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param rut_cliente
	 * @return
	 */
	public List getEventosByRutCliente(long rutCliente) throws BocException {
		try {
			return eventosService.getEventosByRutCliente(rutCliente);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param rut_cliente
	 * @return
	 */
	public boolean tieneEventoByRutCliente(long rutCliente) throws BocException {
		try {
			return eventosService.tieneEventoByRutCliente(rutCliente);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Para obtener el evento que debemos mostrar para el Rut indicado
	 * 
	 * @param clienteRut
	 *            Rut del cliente
	 * @return Evento a mostrar, si no tenemos que mostrar ningún evento
	 *         devuleve un "new EventoDTO" (con idEvento = 0)
	 */
	public EventoDTO getEventoMostrarByRut(long clienteRut)
			throws SystemException {
		try {
			return eventosService.getEventoMostrarByRut(clienteRut);
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
	}

	/**
	 * Para obtener el evento que debemos mostrar de acuerdo a una fecha y al
	 * Rut de un cliente.
	 * 
	 * @param rutCliente
	 *            Rut ciente
	 * @param fingreso
	 *            Fecha del evento
	 * @return Evento a mostrar, si no tenemos que mostrar ningún evento
	 *         devuleve un "new EventoDTO" (con idEvento = 0)
	 */
	public EventoDTO getEventoMostrarByFecha(long rutCliente, String fingreso)
			throws SystemException {
		try {
			return eventosService.getEventoMostrarByRutYFecha(rutCliente,
					fingreso);
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
	}

	/**
	 * Modificamos la ocurrencia del evento.
	 * 
	 * @param idEvento
	 * @param rut_cliente
	 * @param ocurrencia_rebajar
	 */
	public boolean bajarOcurrenciaEvento(long idPedido, long rutCliente)
			throws SystemException {
		try {
			return eventosService.bajarOcurrenciaEvento(idPedido, rutCliente);
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
	}

	/**
	 * @param rut_cliente
	 * @param id_pedido
	 * @return
	 */
	public List getEventosByRutClienteAndPedido(long rutCliente, long idPedido)
			throws BocException {
		try {
			return eventosService.getEventosByRutClienteAndPedido(rutCliente,
					idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param rut_cliente
	 * @param id_pedido
	 * @return
	 */
	public EventoDTO getEventoByPedido(long rutCliente, long idPedido)
			throws BocException {
		try {
			return eventosService.getEventoByPedido(rutCliente, idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idEvento
	 * @return
	 */
	public long getCantidadRutsUsaronEvento(long idEvento) throws BocException {
		try {
			return eventosService.getCantidadRutsUsaronEvento(idEvento);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Entrega el o los nombre de categorias padre de una categoria en
	 * particular
	 * 
	 * @param idCategoria
	 * @return
	 */
	public String getNombresCategoriasPadreByIdCat(long idCategoria)
			throws BocException {
		try {
			return contFacade.getNombresCategoriasPadreByIdCat(idCategoria);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Nos entrega un listado con los mails para enviar los sustitutos y
	 * faltantes
	 * 
	 * @return
	 */
	public List getMailsSyF() throws BocException {
		try {
			return informesService.getMailsSyF();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Agregar un nuevo mail para sustitutos y faltantes
	 * 
	 * @param m
	 *            Datos de Mail SyF
	 * @return
	 */
	public long addMailSyF(MailSustitutosFaltantesDTO m) throws BocException {
		try {
			return informesService.addMailSyF(m);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Modificar los datos de mail SyF
	 * 
	 * @param m
	 */
	public void modMailSyF(MailSustitutosFaltantesDTO m) throws BocException {
		try {
			informesService.modMailSyF(m);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idMail
	 * @return
	 */
	public MailSustitutosFaltantesDTO getMailSyFById(long idMail)
			throws BocException {
		try {
			return informesService.getMailSyFById(idMail);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idMail
	 */
	public void delMailSyFById(long idMail) throws BocException {
		try {
			informesService.delMailSyFById(idMail);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 */
	public List getOriginalesYSustitutos() throws BocException {
		try {
			return informesService.getOriginalesYSustitutos();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param sustitutos
	 * @return
	 */
	public String addCodigosSyF(ArrayList sustitutos) throws BocException {
		try {
			return informesService.addCodigosSyF(sustitutos);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	// ------------- PROMOCIONES --------------------------

	/**
	 * Entrega un listado de promociones utilizando un criterio de busqueda
	 * opcional dado por un id_promocion o un id_local
	 * 
	 * @param criteria
	 * @return
	 * @throws BocException
	 */

	public List getPromocionesByCriteria(PromocionesCriteriaDTO criteria)
			throws BocException {
		try {
			return pedidosBolService.getPromocionesByCriteria(criteria);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public List getPromociones() throws BocException {
		try {
			return pedidosBolService.getPromociones();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public PromocionDTO getPromocion(int codigo) throws BocException {
		try {
			return pedidosBolService.getPromocion(codigo);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Entrega el numero de registros del listado de promociones utilizando un
	 * criterio de busqueda opcional dado por un id_promocion o un id_local
	 * 
	 * @param criteria
	 * @return
	 * @throws BocException
	 */
	public long getPromocionesByCriteriaCount(PromocionesCriteriaDTO criteria)
			throws BocException {
		try {
			return pedidosBolService.getPromocionesByCriteriaCount(criteria);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene los datos de una promoción específica
	 * 
	 * @param id_promocion
	 * @return
	 * @throws BocException
	 */
	public PromocionDTO getPromocionById(long id_promocion) throws BocException {
		try {
			return pedidosBolService.getPromocionById(id_promocion);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public List getCategoriasSapByPromocionSeccion(long id_local, int tipo)
			throws BocException {
		try {
			return pedidosBolService.getCategoriasSapByPromocionSeccion(
					id_local, tipo);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene los productos de una promoción.
	 * 
	 * @param codigo
	 * @return List ProductoPromocionDTO
	 * @throws BocException
	 */
	public List getPromocionProductos(int codigo) throws BocException {
		try {
			return pedidosBolService.getPromocionProductos(codigo);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Permite modificar una promoción
	 * 
	 * @param dto
	 * @return
	 * @throws BocException
	 */
	public boolean updPromocion(PromocionDTO dto) throws BocException {
		try {
			return pedidosBolService.updPromocion(dto);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene las promociones calculadas de un pedido web en base al numero de
	 * pedido. Utiliza la librería de calculo de promociones.
	 * 
	 * @param id_pedido
	 * @param colaborador
	 * @return
	 * @throws BocException
	 */
	// [20121115avc
	public List doRecalculoPromocion(long id_pedido, long id_local,
			Long colaborador) throws BocException {
		return doRecalculoPromocionNew(id_pedido, id_local, colaborador, null,
				null);
	}

	public List doRecalculoPromocionNew(long id_pedido, long id_local,
			Long colaborador, CuponDsctoDTO cddto, List cuponProds)
			throws BocException {
		try {
			return pedidosBolService.doRecalculoPromocion(id_pedido, id_local,
					colaborador, cddto, cuponProds);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	// ]20121115avc

	/**
	 * Obtiene los productos CON promoción de un pedido especifico
	 * 
	 * @param id_pedido
	 * @return List PedidoPromocionDTO
	 * @throws BocException
	 */
	public List getPromocionPedidos(long id_pedido) throws BocException {
		try {
			return pedidosBolService.getPromocionPedidos(id_pedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Permite aplicar recalculo de promociones
	 * 
	 * @param lst_promo
	 *            listado de promociones
	 * @param lst_dp
	 *            listado de detalle pedido
	 * @param id_pedido
	 * @throws BocException
	 */
	public void setAplicaRecalculo(List lst_promo, List lst_dp, long id_pedido)
			throws BocException {
		try {
			pedidosBolService.setAplicaRecalculo(lst_promo, lst_dp, id_pedido);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BocException(e);
		}
	}

	/**
	 * @param idCliente
	 * @param idProducto
	 * @return
	 */
	public SustitutoDTO getCriterioClientePorProducto(long idCliente,
			long idProducto) throws BocException {
		try {
			return pedidosBolService.getCriterioClientePorProducto(idCliente,
					idProducto);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param paramId_prod
	 * @param idCliente
	 * @param idCriterio
	 * @param descCriterio
	 */
	public void addModCriterioCliente(long idProducto, long idCliente,
			long idCriterio, String descCriterio) throws BocException {
		try {
			pedidosBolService.addModCriterioCliente(idProducto, idCliente,
					idCriterio, descCriterio);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idPedido
	 * @param idRonda
	 * @return
	 */
	public List getProductosPedidoRonda(long idPedido, long idRonda)
			throws BocException {
		try {
			return pedidosBolService.getProductosPedidoRonda(idPedido, idRonda);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 */
	public List getMotivos() throws BocException {
		try {
			return casosService.getMotivos();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param motivo
	 * @return
	 */
	public long addMotivo(ObjetoDTO motivo) throws BocException,
			ServiceException, SystemException {
		try {
			return casosService.addMotivo(motivo);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	/**
	 * @param motivo
	 */
	public void modMotivo(ObjetoDTO motivo) throws BocException,
			ServiceException, SystemException {
		try {
			casosService.modMotivo(motivo);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	/**
	 * @param idObjeto
	 * @return
	 */
	public ObjetoDTO getMotivoById(long idMotivo) throws BocException {
		try {
			return casosService.getMotivoById(idMotivo);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param idObjeto
	 */
	public void delMotivo(long idMotivo) throws BocException, ServiceException,
			SystemException {
		try {
			casosService.delMotivo(idMotivo);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			ex.printStackTrace();
			throw new BocException(ex);
		}
	}

	/**
	 * @param string
	 * @return
	 */
	public List getMotivosByEstado(String estado) throws BocException {
		try {
			return casosService.getMotivosByEstado(estado);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public List getTrxMpDetalles(long id_trx) throws BocException {
		try {
			return pedidosBolService.getTrxPagoDetalleByIdTrxMp(id_trx);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 */
	public List getGruposListado() throws BocException {
		try {
			return pedidosBolService.getGruposListado();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 */
	public List getTiposGruposListado() throws BocException {
		try {
			return pedidosBolService.getTiposGruposListado();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param lg
	 * @return
	 */
	public long addGrupoLista(ListaGrupoDTO lg) throws BocException {
		try {
			return pedidosBolService.addGrupoLista(lg);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param lg
	 */
	public void modGrupoLista(ListaGrupoDTO lg) throws BocException {
		try {
			pedidosBolService.modGrupoLista(lg);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param idObjeto
	 * @return
	 */
	public ListaGrupoDTO getGrupoListadoById(long idGrupoListado)
			throws BocException {
		try {
			return pedidosBolService.getGrupoListadoById(idGrupoListado);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param idObjeto
	 */
	public void delGrupoLista(long idGrupoLista) throws BocException {
		try {
			pedidosBolService.delGrupoLista(idGrupoLista);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param idGrupoLista
	 * @return
	 */
	public List clienteListasEspeciales(long idGrupoLista) throws BocException {
		try {
			return pedidosBolService.clienteListasEspeciales(idGrupoLista);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param hash
	 * @return
	 */
	public String addListasEspeciales(Hashtable hash) throws BocException {
		try {
			return pedidosBolService.addListasEspeciales(hash);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param idLista
	 * @return
	 */
	public List getGruposAsociadosLista(long idLista) throws BocException {
		try {
			return pedidosBolService.getGruposAsociadosLista(idLista);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param idLista
	 * @return
	 */
	public List getGruposNoAsociadosLista(long idLista) throws BocException {
		try {
			return pedidosBolService.getGruposNoAsociadosLista(idLista);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param idLista
	 * @return
	 */
	public UltimasComprasDTO getListaById(long idLista) throws BocException {
		try {
			return pedidosBolService.getListaById(idLista);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param lista
	 * @param grupos
	 */
	public void modLista(UltimasComprasDTO lista, String[] grupos)
			throws BocException {
		try {
			pedidosBolService.modLista(lista, grupos);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param idLista
	 * @param idGrupo
	 */
	public void delListaEspecial(long idLista, long idGrupo)
			throws BocException {
		try {
			pedidosBolService.delListaEspecial(idLista, idGrupo);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param idLista
	 * @return
	 */
	public List listaDeProductosByLista(long idLista) throws BocException {
		try {
			return pedidosBolService.listaDeProductosByLista(idLista);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 * @throws BocException
	 */
	public List getDestacados() throws BocException {
		try {
			return destacadosService.getDestacados();
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new BocException(e);
		}
	}

	/**
	 * @param destacadoDTO
	 * @throws BocException
	 */
	public void addDestacado(DestacadoDTO destacadoDTO) throws BocException {
		try {
			destacadosService.addDestacado(destacadoDTO);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new BocException(e);
		}
	}

	/**
	 * Si la lista de locales o de productos vienen vacias no se modifican en la
	 * base de datos.
	 * 
	 * @param destacadoDTO
	 */
	public void updDestacado(DestacadoDTO destacadoDTO) throws BocException {
		try {
			destacadosService.updDestacado(destacadoDTO);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new BocException(e);
		}
	}

	/**
	 * @param id
	 */
	public void delDestacado(int id) throws BocException {
		try {
			destacadosService.delDestacado(id);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new BocException(e);
		}
	}

	/**
	 * @param id
	 * @return
	 */
	public DestacadoDTO getDestacado(int id) throws BocException {
		try {
			return destacadosService.getDestacado(id);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new BocException(e);
		}
	}

	/**
	 * @param id
	 * @return
	 */
	public List getProductosDestacados(int id) throws BocException {
		try {
			return destacadosService.getProductosDestacados(id);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new BocException(e);
		}
	}

	/**
	 * @param id
	 * @return
	 */
	public List getDestacadoLocales(int id) throws BocException {
		try {
			return destacadosService.getDestacadoLocales(id);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new BocException(e);
		}
	}

	/**
	 * @param idPedido
	 * @param tipoDespacho
	 */
	public void modTipoDespachoDePedido(long idPedido, String tipoDespacho)
			throws BocException {
		try {
			pedidosBolService.modTipoDespachoDePedido(idPedido, tipoDespacho);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idPedido
	 * @param tipoPicking
	 */
	public void modTipoPickingPedido(long idPedido, String tipoPicking)
			throws BocException {
		try {
			pedidosBolService.modTipoPickingPedido(idPedido, tipoPicking);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 */
	public List getRutsConfiables() throws BocException {
		try {
			return clientesService.getRutsConfiables();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param ruts
	 * @return
	 */
	public void addRutsConfiables(Vector ruts) throws BocException {
		try {
			clientesService.addRutsConfiables(ruts);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 */
	public List getLogRutsConfiables() throws BocException {
		try {
			return clientesService.getLogRutsConfiables();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param usr
	 */
	public void addLogRutConfiables(String user, String nombre, String msjLog)
			throws BocException {
		try {
			clientesService.addLogRutConfiables(user, nombre, msjLog);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public HashMap getListadoOP(Calendar cal, int id_local, String hora_desp)
			throws BocException {
		try {
			return pedidosBolService.getListadoOP(cal, id_local, hora_desp);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public boolean verificaNumPoligono(int id_comuna, int num_pol)
			throws BocException {
		try {
			return pedidosBolService.verificaNumPoligono(id_comuna, num_pol);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public int getLocalByPoligono(int id_poligono) throws BocException {
		try {
			return pedidosBolService.getLocalByPoligono(id_poligono);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Devuelve un marco
	 * 
	 * @param idMarco
	 * @return
	 */
	public ListaTipoGrupoDTO getTipoGrupoListadoById(long idMarco)
			throws BocException {
		try {
			return pedidosBolService.getTipoGrupoListadoById(idMarco);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param marco
	 */
	public void addMarco(ListaTipoGrupoDTO marco) throws BocException {
		try {
			pedidosBolService.addMarco(marco);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param marco
	 */
	public void modMarco(ListaTipoGrupoDTO marco) throws BocException {
		try {
			pedidosBolService.modMarco(marco);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param marco
	 */
	public void delMarco(ListaTipoGrupoDTO marco) throws BocException {
		try {
			pedidosBolService.delMarco(marco);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param i
	 * @return
	 */
	public List getGruposByMarco(int idMarco) throws BocException {
		try {
			return pedidosBolService.getGruposByMarco(idMarco);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 */
	public List getTiposGruposListadoActivos() throws BocException {
		try {
			return pedidosBolService.getTiposGruposListadoActivos();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param criterio
	 * @return
	 */
	public List getProductosCarruselPorCriterio(CriterioCarruselDTO criterio)
			throws BocException {
		try {
			return pedidosBolService.getProductosCarruselPorCriterio(criterio);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param prod
	 */
	public long addEditProductoCarrusel(ProductoCarruselDTO prod)
			throws BocException {
		try {
			return pedidosBolService.addEditProductoCarrusel(prod);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idProductoCarrusel
	 * @return
	 */
	public ProductoCarruselDTO getProductoCarruselById(long idProductoCarrusel)
			throws BocException {
		try {
			return pedidosBolService
					.getProductoCarruselById(idProductoCarrusel);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idProductoCarrusel
	 */
	public void deleteProductoCarruselById(long idProductoCarrusel)
			throws BocException {
		try {
			pedidosBolService.deleteProductoCarruselById(idProductoCarrusel);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param usr
	 * @param strLog
	 */
	public void addLogCarrusel(UserDTO usr, String strLog) throws BocException {
		try {
			pedidosBolService.addLogCarrusel(usr, strLog);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param criterio
	 * @return
	 */
	public double getCountProductosCarruselPorCriterio(
			CriterioCarruselDTO criterio) throws BocException {
		try {
			return pedidosBolService
					.getCountProductosCarruselPorCriterio(criterio);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param string
	 * @return
	 */
	public List getLogCarruselByFecha(String fecha) throws BocException {
		try {
			return pedidosBolService.getLogCarruselByFecha(fecha);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param criterio
	 * @return
	 */
	public List getRutasByCriterio(RutaCriterioDTO criterio)
			throws BocException {
		try {
			return pedidosBolService.getRutasByCriterio(criterio);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idLocal
	 * @param fechaDespacho
	 * @return
	 */
	public List getJornadasDespachoParaFiltro(long idLocal,
			String fechaDespacho, long idZona) throws BocException {
		try {
			return pedidosBolService.getJornadasDespachoParaFiltro(idLocal,
					fechaDespacho, idZona);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public List getJornadasDespacho(int localId, Date fechaIni, Date fechaFin)
			throws BocException {
		try {
			return pedidosBolService.getJornadasDespacho(localId, fechaIni,
					fechaFin);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idEmpresa
	 * @param id_local
	 * @return
	 */
	public List getChoferesDeTransporte(int idEmpresa, long idLocal)
			throws BocException {
		try {
			return pedidosBolService
					.getChoferesDeTransporte(idEmpresa, idLocal);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idEmpresa
	 * @param idLocal
	 * @return
	 */
	public List getPatentesDeTransporte(int idEmpresa, long idLocal)
			throws BocException {
		try {
			return pedidosBolService
					.getPatentesDeTransporte(idEmpresa, idLocal);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 */
	public List getEstadosRuta() throws BocException {
		try {
			return pedidosBolService.getEstadosRuta();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param criterio
	 * @return
	 */
	public double getCountRutasByCriterio(RutaCriterioDTO criterio)
			throws BocException {
		try {
			return pedidosBolService.getCountRutasByCriterio(criterio);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idRuta
	 * @return
	 */
	public RutaDTO getRutaById(long idRuta) throws BocException {
		try {
			return pedidosBolService.getRutaById(idRuta);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idRuta
	 * @return
	 */
	public List getLogRuta(long idRuta) throws BocException {
		try {
			return pedidosBolService.getLogRuta(idRuta);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idRuta
	 * @return
	 */
	public List getPedidosByRuta(long idRuta) throws BocException {
		try {
			return pedidosBolService.getPedidosByRuta(idRuta);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param id_pedido
	 * @param string
	 */
	public void modificaVecesEnRutaDePedido(long id_pedido, String operacion)
			throws BocException {
		try {
			pedidosBolService.modificaVecesEnRutaDePedido(id_pedido, operacion);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param log1
	 */
	public void addLogRuta(LogRutaDTO log) throws BocException {
		try {
			pedidosBolService.addLogRuta(log);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param id_pedido
	 * @return
	 */
	public int delPedidoRuta(long id_pedido) throws BocException {
		try {
			return pedidosBolService.delPedidoRuta(id_pedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param estado_ruta_anulada
	 * @param idRuta
	 */
	public void setEstadoRuta(int estadoRutaAnulada, long idRuta)
			throws BocException {
		try {
			pedidosBolService.setEstadoRuta(estadoRutaAnulada, idRuta);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idPedido
	 * @param id_estad_pedido_en_despacho
	 * @param login
	 * @param string
	 */
	public void setCambiaEstadoDespacho(long idPedido,
			int idEstadoPedidoEnDespacho, String login, String string)
			throws BocException {
		try {
			pedidosBolService.setCambiaEstadoDespacho(idPedido,
					idEstadoPedidoEnDespacho, login, string);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idPedido
	 * @return
	 */
	public List getDocumentosByPedido(long idPedido) throws BocException {
		try {
			return pedidosBolService.getDocumentosByPedido(idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Lista de responsables de no cumplimiento de entrega del modulo de
	 * despacho
	 * 
	 * @return
	 */
	public List getResponsablesDespachoNC() throws BocException {
		try {
			return pedidosBolService.getResponsablesDespachoNC();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Lista de motivos de no cumplimiento de entrega del modulo de despacho
	 * 
	 * @return
	 */
	public List getMotivosDespachoNC() throws BocException {
		try {
			return pedidosBolService.getMotivosDespachoNC();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param pedExt
	 */
	public void updPedidoFinalizado(PedidoExtDTO pedExt) throws BocException {
		try {
			pedidosBolService.updPedidoFinalizado(pedExt);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idRuta
	 * @return
	 */
	public int getCountPedidoNoFinalizadosByRuta(long idRuta)
			throws BocException {
		try {
			return pedidosBolService.getCountPedidoNoFinalizadosByRuta(idRuta);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param id_pedido
	 */
	public void updPedidoReagendado(long idPedido) throws BocException {
		try {
			pedidosBolService.updPedidoReagendado(idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param criterio
	 * @return
	 */
	public List getPedidosPendientesByCriterio(DespachoCriteriaDTO criterio)
			throws BocException {
		try {
			return pedidosBolService.getPedidosPendientesByCriterio(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param criterio
	 * @return
	 */
	public double getCountPedidosPendientesByCriterio(
			DespachoCriteriaDTO criterio) throws BocException {
		try {
			return pedidosBolService
					.getCountPedidosPendientesByCriterio(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param id_pedido
	 * @param idMotivo
	 * @param idResponsable
	 */
	public void addMotivoResponsableReprogramacion(long idPedido,
			long idMotivo, long idResponsable, long idJornadaDespachoAnterior,
			long idUsuario) throws BocException {
		try {
			pedidosBolService.addMotivoResponsableReprogramacion(idPedido,
					idMotivo, idResponsable, idJornadaDespachoAnterior,
					idUsuario);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * @param id_pedido
	 * @return
	 */
	public RutaDTO getRutaByPedido(long idPedido) throws BocException {
		try {
			return pedidosBolService.getRutaByPedido(idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 */
	public List getMotivosDespachoNCAll() throws BocException {
		try {
			return pedidosBolService.getMotivosDespachoNCAll();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 */
	public List getResponsablesDespachoNCAll() throws BocException {
		try {
			return pedidosBolService.getResponsablesDespachoNCAll();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param i
	 */
	public void delMotivoNCById(long idMotivo) throws BocException {
		try {
			pedidosBolService.delMotivoNCById(idMotivo);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param motivo
	 */
	public void addMotivoNC(ObjetoDTO motivo) throws BocException {
		try {
			pedidosBolService.addMotivoNC(motivo);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param motivo
	 */
	public void modMotivoNC(ObjetoDTO motivo) throws BocException {
		try {
			pedidosBolService.modMotivoNC(motivo);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idRuta
	 */
	public void actualizaCantBinsRuta(long idRuta) throws BocException {
		try {
			pedidosBolService.actualizaCantBinsRuta(idRuta);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param i
	 */
	public void delResponsableNCById(long idResponsable) throws BocException {
		try {
			pedidosBolService.delResponsableNCById(idResponsable);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param motivo
	 */
	public void addResponsableNC(ObjetoDTO responsable) throws BocException {
		try {
			pedidosBolService.addResponsableNC(responsable);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param motivo
	 */
	public void modResponsableNC(ObjetoDTO responsable) throws BocException {
		try {
			pedidosBolService.modResponsableNC(responsable);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param i
	 */
	public void delEmpresaTransporteById(long idEmpresa) throws BocException {
		try {
			pedidosBolService.delEmpresaTransporteById(idEmpresa);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param emp
	 */
	public void addEmpresaTransporte(EmpresaTransporteDTO empresa)
			throws BocException {
		try {
			pedidosBolService.addEmpresaTransporte(empresa);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param emp
	 */
	public void modEmpresaTransporte(EmpresaTransporteDTO empresa)
			throws BocException {
		try {
			pedidosBolService.modEmpresaTransporte(empresa);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 */
	public List getEmpresasTransporteAll() throws BocException {
		try {
			return pedidosBolService.getEmpresasTransporteAll();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idPedido
	 * @return
	 */
	public JornadaDTO getJornadaDespachoOriginalDePedidoReprogramado(
			long idPedido) throws BocException {
		try {
			return pedidosBolService
					.getJornadaDespachoOriginalDePedidoReprogramado(idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param id_pedido
	 * @return
	 */
	public List getReprogramacionesByPedido(long idPedido) throws BocException {
		try {
			return pedidosBolService.getReprogramacionesByPedido(idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idCliente
	 */
	public void actualizaContadorEncuestaCliente(long idCliente, long idPedido,
			int nroCompras) throws BocException {
		try {
			clientesService.actualizaContadorEncuestaCliente(idCliente,
					idPedido, nroCompras);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idPedido
	 * @return
	 */
	public List getProductosTodasTrxByPedido(long idPedido) throws BocException {
		try {
			return pedidosBolService.getProductosTodasTrxByPedido(idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idProducto
	 * @param porcion
	 * @param unidadPorcion
	 */
	public void modPorcionProducto(long idProducto, double porcion,
			long unidadPorcion) throws BocException {
		try {
			pedidosBolService.modPorcionProducto(idProducto, porcion,
					unidadPorcion);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idProducto
	 * @param idPila
	 */
	public void delPilaProducto(long idProducto, long idPila)
			throws BocException {
		try {
			pedidosBolService.delPilaProducto(idProducto, idPila);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param pilaProd
	 */
	public void addPilaProducto(PilaProductoDTO pilaProd) throws BocException {
		try {
			pedidosBolService.addPilaProducto(pilaProd);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 */
	public List getUnidadesNutricionales() throws BocException {
		try {
			return pedidosBolService.getUnidadesNutricionales();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param id
	 * @return
	 */
	public List getPilasNutricionalesByProductoFO(long idProducto)
			throws BocException {
		try {
			return pedidosBolService
					.getPilasNutricionalesByProductoFO(idProducto);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 */
	public List getPilasNutricionales() throws BocException {
		try {
			return pedidosBolService.getPilasNutricionales();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param id_pedido
	 * @return
	 */
	public WebpayDTO webpayGetPedido(long id_pedido) throws BocException {
		try {
			return pedidosBolService.webpayGetPedido(id_pedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param id_pedido
	 * @return
	 */
	public BotonPagoDTO botonPagoGetByPedido(long id_pedido)
			throws BocException {
		try {
			return pedidosBolService.botonPagoGetByPedido(id_pedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param mail
	 */
	public void addMail(cl.bbr.jumbocl.clientes.dto.MailDTO mail)
			throws BocException {
		try {
			clientesService.addMail(mail);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idPedido
	 * @return
	 */
	public List getProductosPickeadosByIdPedido(long idPedido)
			throws BocException {
		try {
			return pedidosBolService.getProductosPickeadosByIdPedido(idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idPedido
	 * @return
	 */
	public List getSustitutosYPesablesByPedidoId(long idPedido)
			throws BocException {
		try {
			return pedidosBolService.getSustitutosYPesablesByPedidoId(idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idPedido
	 * @param datosCambiados
	 */
	public void cambiarPreciosPickeados(long idPedido, String datosCambiados)
			throws BocException {
		try {
			pedidosBolService.cambiarPreciosPickeados(idPedido, datosCambiados);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idPedido
	 * @param b
	 */
	public void modPedidoExcedido(long idPedido, boolean excedido)
			throws BocException {
		try {
			pedidosBolService.modPedidoExcedido(idPedido, excedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 */
	public List getPedidosEnTransicionTEMP(long idLocal) throws BocException {
		try {
			return pedidosBolService.getPedidosEnTransicionTEMP(idLocal);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param idProductoFO
	 * @param idPedido
	 * @param idDetallePedido
	 * @param cantidad
	 */
	public void modProductoDePedido(long idPedido, long idDetallePedido,
			double cantidad) throws BocException {
		try {
			pedidosBolService.modProductoDePedido(idPedido, idDetallePedido,
					cantidad);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param fcIni
	 * @param fcFin
	 * @param local
	 * @param usuario
	 * @return
	 */
	public List getInformeModificacionDePrecios(String fcIni, String fcFin,
			long local, String usuario) throws BocException {
		try {
			return pedidosBolService.getInformeModificacionDePrecios(fcIni,
					fcFin, local, usuario);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @return
	 */
	public List getUsuariosInformeModPrecios() throws BocException {
		try {
			return pedidosBolService.getUsuariosInformeModPrecios();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param usuario
	 * @return
	 */
	public List getInformeProductosSinStock() throws BocException {
		try {
			return pedidosBolService.getInformeProductosSinStock();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param pedido
	 */
	public boolean anulacionAceleradaCAT(PedidoDTO pedido) throws BocException {
		try {
			return pedidosBolService.anulacionAceleradaCAT(pedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param id_pedido
	 * @param b
	 */
	public void marcaAnulacionBoletaEnLocal(long idPedido, boolean marcar)
			throws BocException {
		try {
			pedidosBolService.marcaAnulacionBoletaEnLocal(idPedido, marcar);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	// BOLSAS DE REGALO
	public List getStockBolsasRegalo(String cod_sucursal) throws BocException {

		try {
			return bolsasService.getStockBolsasRegalo(cod_sucursal);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	public List getBitacoraBolsasRegalo(String cod_sucursal)
			throws BocException {

		try {
			return bolsasService.getBitacoraBolsasRegalo(cod_sucursal);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	public void actualizarStockBolsa(String cod_bolsa, String cod_sucursal,
			int stock) throws BocException {

		try {
			bolsasService.actualizarStockBolsa(cod_bolsa, cod_sucursal, stock);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exceptio
			throw new BocException(ex);
		}
	}

	/**
	 * (+) INDRA (+)
	 * 
	 * @param int id_local
	 * @param String
	 *            fecha_modi
	 * @param String
	 *            nom_usuario
	 * @param double u_unidad
	 * @param double u_monto
	 * @param String
	 *            u_activacion
	 * 
	 * **/
	public void insertarUmbral(int id_local, String fecha_modi,
			String nom_usuario, double u_unidad, double u_monto,
			String u_activacion) throws BocException {

		try { // intenta Insertar umbral con parametros establecidos
			umbralService.insertarUmbrales(id_local, fecha_modi, nom_usuario,
					u_unidad, u_monto, u_activacion);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	public void updateUmbral(int id_local, String fecha_modi,
			String nom_usuario, double u_unidad, double u_monto,
			String u_activacion) throws BocException {

		try { // intenta Insertar umbral con parametros establecidos
			umbralService.updateUmbral(id_local, fecha_modi, nom_usuario,
					u_unidad, u_monto, u_activacion);
		} catch (Exception ex) {

			throw new BocException(ex);
		}
	}

	public UmbralDTO getPorcenUmbralById(long id_pedido) throws BocException {
		try {
			return umbralService.getPorcenUmbralById(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	public long consultaIdLocal(long id_local) throws BocException {
		long id_local_return = 0;
		try {

			id_local_return = umbralService.consultaIdLocal(id_local); // id_local_return;
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
		return id_local_return;
	}

	// //

	// (-) INDRA (-)

	public void insertarRegistroBitacoraBolsas(String desc_operacion,
			String usuario, String cod_sucursal) throws BocException {

		try {
			bolsasService.insertarRegistroBitacoraBolsas(desc_operacion,
					usuario, cod_sucursal);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	public void asignacionBolsaCliente(String rut_cliente, String cod_bolsa)
			throws BocException {

		try {
			bolsasService.asignacionBolsaCliente(rut_cliente, cod_bolsa);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	public void crearBolsaRegalo(BolsaDTO bolsa) throws BocException {

		try {
			bolsasService.crearBolsaRegalo(bolsa);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	public void activaBolsa() throws BocException {

		try {
			bolsasService.activaBolsa();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	public void eliminarBolsaRegalo(BolsaDTO bolsa) throws BocException {

		try {
			bolsasService.eliminarBolsaRegalo(bolsa);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	public void actualizarBolsa(BolsaDTO bolsa) throws BocException {

		try {
			bolsasService.actualizarBolsa(bolsa);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	public BolsaDTO getBolsaRegalo(String cod_bolsa) throws BocException {

		try {
			return bolsasService.getBolsaRegalo(cod_bolsa);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	public List getAsignacionesBolsas() throws BocException {
		try {
			return bolsasService.getAsignacionesBolsas();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public boolean validaCodSap(String cod_sap) throws BocException {
		try {
			return bolsasService.validaCodSap(cod_sap);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public boolean validaCodBolsa(String cod_bolsa) throws BocException {
		try {
			return bolsasService.validaCodBolsa(cod_bolsa);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public boolean validaCodBolsaSap(String cod_bolsa, String cod_sap)
			throws BocException {
		try {
			return bolsasService.validaCodBolsaSap(cod_bolsa, cod_sap);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public long getIdProdBO(String cod_sap) throws BocException {
		try {
			return bolsasService.getIdProdBO(cod_sap);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public long getIdProdFO(String cod_sap) throws BocException {
		try {
			return bolsasService.getIdProdFO(cod_sap);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public ProductoEntity getProductoByIdProd(long idProd) throws BocException {
		try {
			return bolsasService.getProductoByIdProd(idProd);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public boolean setModEmpresaLinea(EmpresasDTO prm) throws BocException {
		try {
			return empresasService.setModEmpresaLinea(prm);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param log
	 */
	public void setEmpresaLineaLog(EmpresaLogDTO log) throws BocException {
		try {
			empresasService.setEmpresaLineaLog(log);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Metodo que consulta si una OP tiene Exceso ([total
	 * pickiado(BO_DETALLE_PICKING) + costo despacho(BO_PEDIDOS)] vs [monto
	 * reservado(BO_PEDIDOS)])
	 * 
	 * @param id_pedido
	 *            :id del pedido a consultar
	 * @return boolean true en caso de exceso o false en caso que OP no tenga
	 *         exceso
	 * @throws BocException
	 */
	public boolean isOpConExceso(long idPedido) throws BocException {
		try {
			return pedidosBolService.isOpConExceso(idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public boolean truncateTableColaborador() throws BocException {
		try {
			return cargarColaboradoresService.truncateTableColaborador();
		} catch (Exception e) {
			throw new BocException(e);
		}
	}

	public boolean cargarColaboradores(List colaboradores) throws BocException {
		try {
			return cargarColaboradoresService
					.cargarColaboradores(colaboradores);
		} catch (Exception e) {
			throw new BocException(e);
		}
	}

	public String cantidadColaboradores() throws BocException {
		try {
			return cargarColaboradoresService.cantidadColaboradores();
		} catch (Exception e) {
			throw new BocException(e);
		}
	}

	public String getMontoLimite() throws BocException {
		try {
			return parametrosEditablesService.getMontoLimite();
		} catch (Exception e) {
			throw new BocException(e);
		}
	}

	public String getDescuentoMayor() throws BocException {
		try {
			return parametrosEditablesService.getDescuentoMayor();
		} catch (Exception e) {
			throw new BocException(e);
		}
	}

	public String getDescuentoMenor() throws BocException {
		try {
			return parametrosEditablesService.getDescuentoMenor();
		} catch (Exception e) {
			throw new BocException(e);
		}
	}

	public void setMontoLimite(String monto) throws BocException {
		try {
			parametrosEditablesService.setMontoLimite(monto);
		} catch (Exception e) {
			throw new BocException(e);
		}
	}

	public void setDescuentoMayor(String valor) throws BocException {
		try {
			parametrosEditablesService.setDescuentoMayor(valor);
		} catch (Exception e) {
			throw new BocException(e);
		}
	}

	public void setDescuentoMenor(String valor) throws BocException {
		try {
			parametrosEditablesService.setDescuentoMenor(valor);
		} catch (Exception e) {
			throw new BocException(e);
		}
	}

	public int noSustituir(long id_pedido) throws BocException {
		try {
			return criterioSustitucionService.noSustituir(id_pedido);
		} catch (Exception e) {
			throw new BocException(e);
		}
	}

	public void registrarTracking(long id_pedido, String usuario)
			throws BocException {
		try {
			criterioSustitucionService.registrarTracking(id_pedido, usuario);
		} catch (Exception e) {
			throw new BocException(e);
		}
	}

	public boolean retrocederOPEstadoValidado(long idPedido, String user)
			throws BocException {
		try {
			return cambiaEstadoValidadoService.retrocederOPEstadoValidado(
					idPedido, user);
		} catch (Exception e) {
			throw new BocException(e);
		}
	}

	/**
	 * @return Lista de Secciones
	 * @throws BocException
	 */
	public List getSecciones() throws BocException {
		try {
			return seccionesExcluidasDCService.getSecciones();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @return Lista de Secciones Excluidas
	 * @throws BocException
	 */
	public List getSeccionesExcluidas() throws BocException {
		try {
			return seccionesExcluidasDCService.getSeccionesExcluidas();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param id_seccion
	 * @throws BocException
	 */
	public void excluirSeccion(int id_seccion) throws BocException {
		try {
			seccionesExcluidasDCService.excluirSeccion(id_seccion);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * @param id_seccion
	 * @throws BocException
	 */
	public void permitirSeccion(int id_seccion) throws BocException {
		try {
			seccionesExcluidasDCService.permitirSeccion(id_seccion);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public boolean modFechaOP(long id_pedido, String paramFecha,
			String fechaOld, String userLogin) throws BocException {
		try {
			return pedidosBolService.modFechaOP(id_pedido, paramFecha,
					fechaOld, userLogin);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public boolean doBlanqueoDireccion(long id_cliente) throws BocException,
			SystemException {
		try {
			return clientesService.doBlanqueoDireccion(id_cliente);
		} catch (ServiceException ex) { // RemoteException ex
			throw new BocException(ex);
		}

	}

	public boolean tieneDireccionesConCobertura(long idCliente)
			throws BocException, SystemException {
		try {
			return clientesService.tieneDireccionesConCobertura(idCliente);
		} catch (ServiceException ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * Obtiene lista de mails de Project Managers
	 * 
	 * 
	 * @return List mailsPM
	 * @throws BocException
	 */

	public List getMailPM() throws BocException {
		try {
			return pedidosBolService.getMailPM();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * 
	 * @param cupon
	 * @return
	 * @throws BocException
	 */
	public boolean setGuardarCupon(CuponEntity cupon) throws BocException {
		try {
			return cuponesDCService.setGuardarCupon(cupon);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * 
	 * @return
	 * @throws BocException
	 */
	public List getListaTiposCupones() throws BocException {
		try {
			return cuponesDCService.getListaTiposCupones();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * 
	 * @param cuponId
	 * @return
	 * @throws BocException
	 */
	public CuponEntity getDatoCuponPorId(CuponEntity cuponId)
			throws BocException {
		try {
			return cuponesDCService.getDatoCuponPorId(cuponId);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * 
	 * @param cupon
	 * @return
	 * @throws BocException
	 */

	public CuponEntity getDatoCuponPorCodigo(CuponEntity cupon)
			throws BocException {
		try {
			return cuponesDCService.getDatoCuponPorCodigo(cupon);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * 
	 * @param codDesc
	 * @return
	 * @throws BocException
	 */
	public List getTiposProductosPorCod(String codDesc) throws BocException {
		try {
			return contFacade.getTiposProductosPorCod(codDesc);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * 
	 * @param codDesc
	 * @return
	 * @throws BocException
	 */
	public List getTiposProductosPorDesc(String codDesc) throws BocException {
		try {
			return contFacade.getTiposProductosPorDesc(codDesc);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	/**
	 * 
	 * @return
	 * @throws BocException
	 */
	public List getListaRubros() throws BocException {
		try {
			return cuponesDCService.getListaRubros();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * 
	 * @return
	 * @throws BocException
	 */
	public List getListaSecciones() throws BocException {
		try {
			return cuponesDCService.getListaSecciones();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * 
	 * @param cpr
	 * @param id_usuario
	 * @return
	 * @throws BocException
	 */
	public boolean setCargaRutMasiva(CuponPorRut cpr, long id_usuario)
			throws BocException {
		try {
			return cuponesDCService.setCargaRutMasiva(cpr, id_usuario);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * 
	 * @param id_cup_dto
	 * @return
	 * @throws BocException
	 */
	public int getCantidadRutAsociado(int id_cup_dto) throws BocException {
		try {
			return cuponesDCService.getCantidadRutAsociado(id_cup_dto);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * 
	 * @param cpp
	 * @param rad_tipo
	 * @param id_usuario
	 * @throws BocException
	 */
	public void setCuponAsociarTipo(CuponPorProducto cpp, String rad_tipo,
			long id_usuario, String rad_despacho) throws BocException {
		try {
			cuponesDCService.setCuponAsociarTipo(cpp, rad_tipo, id_usuario, rad_despacho);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * 
	 * @param ce
	 * @return
	 * @throws BocException
	 */
	public void setTodasLasSeccionesAsociado(CuponEntity ce)
			throws BocException {
		try {
			cuponesDCService.setTodasLasSeccionesAsociado(ce);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	/**
	 * 
	 * @param id_cup_dto
	 * @return
	 * @throws BocException
	 */
	public List getListaCuponAsociado(int id_cup_dto) throws BocException {
		try {
			return cuponesDCService.getListaCuponAsociado(id_cup_dto);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}
	
	//tachar percios
	public Map updBannerProducto(List productos, String locales, String obs, UserDTO usr) throws BocException {
		try {
			return productosService.updBannerProducto(productos, locales, obs, usr);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new BocException(e);
		}
	}
	
	//tachar percios
	public Map revertBannerProducto(List productos, String locales, String obs, UserDTO usr) throws BocException {
		try {
			return productosService.revertBannerProducto(productos, locales, obs, usr);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new BocException(e);
		}
	}
	
	/**
	 * 
	 * @param idLocal
	 * @throws BocException
	 */
	public void getLimpiarTablaStockOnlinePorLocal( long idLocal ) throws BocException {
		
		try {
			
			stockOnLineService.getLimpiarTablaStockOnlinePorLocal( idLocal );
		
		} catch ( Exception ex ) {
			
			throw new BocException( ex );
		
		}
	
	}
	
	/**
	 * 
	 * @param idLocal
	 * @throws BocException
	 */
	public void getConfirmarSemiautomaticoStockOnlinePorLocal( long idLocal ) throws BocException {
		
		try {
			
			stockOnLineService.getConfirmarSemiautomaticoStockOnlinePorLocal( idLocal );
		
		} catch ( Exception ex ) {
			
			throw new BocException( ex );
		
		}
	
	}
	
	
	/**
	 * 
	 * @param archivoFiltrado
	 * @param idLocal
	 * @return
	 * @throws BocException
	 */
	public boolean setInsertRegistroExcelToBD( List archivoFiltrado, long idLocal ) throws BocException {
		
		try {
		
			return stockOnLineService.setInsertRegistroExcelToBD( archivoFiltrado, idLocal );
		
		} catch (Exception ex) {
			
			throw new BocException(ex);
		
		}
	
	}
	
	/**
	 * 
	 * @param cod_prod1
	 * @param uniMed
	 * @return
	 * @throws BocException
	 */
	public boolean getProductoBySkuUniMed( String cod_prod1, String uniMed ) throws BocException {
		
		try {
			
			return stockOnLineService.getProductoBySkuUniMed( cod_prod1, uniMed );
		
		} catch ( Exception ex ) {
			
			throw new BocException( ex );
		
		}
	
	}
	
	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws BocException
	 */
	public boolean setEjecutaProcesoSemiAutomatico( long idLocal ) throws BocException {
		
		try {
			
			return stockOnLineService.setEjecutaProcesoSemiAutomatico( idLocal );
		
		} catch ( Exception ex ) {
		
			throw new BocException( ex );
		
		}
	
	}
	
	
	public boolean isActivaCorreccionAutomatica() throws BocException {
		// TODO Apéndice de método generado automáticamente
		try {
			return pedidosBolService.isActivaCorreccionAutomatica();
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}
	
	/**
	 * Obtiene los datos de la ficha del producto según codigo producto
	 * @param codProd
	 * @return
	 * @throws BocException
	 */
	public List getFichaProductoPorId(long codProd) throws BocException {
		try {
			return contFacade.getFichaProductoPorId(codProd);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}
	
	/**
	 * Obtiene todos los item de la ficha tecnica
	 * @return
	 * @throws BocException
	 */
	public List getItemFichaProductoAll() throws BocException {
		try {
			return contFacade.getListItemFichaProductoAll();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}
		
	/**
	 * Obtiene el item de la ficha tecnica según id
	 * @param idItem
	 * @return
	 * @throws BocException
	 */
	public ItemFichaProductoDTO getItemFichaProductoById(long idItem) throws BocException {
		try {
			return contFacade.getItemFichaProductoById(idItem);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}		
	
	/**
	 * ModFichaTecnica: controller :Ficha tecnica
	 */
	/**
	 * Permite modificar los datos de una ficha tecnica
	 * 
	 * @param procparam
	 *            ProcModProductDTO
	 * @return boolean
	 * @throws BocException
	 */
	public boolean setModFichaTecnica(ProcModFichaTecnicaDTO procparam, boolean cambioEstadoFicha, String valorItem, boolean actualizaLog)
			throws BocException {
		boolean result = false;
		try {
			result = contFacade.setModFichaTecnica(procparam, cambioEstadoFicha, valorItem, actualizaLog);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
		return result;
	}
	
	/**
	 * Permite saber si el producto tiene ficha.
	 * 
	 * @param cod_prod
	 *            long
	 * @return boolean
	 * @throws BocException
	 */
	public boolean tieneFichaProductoById(long cod_prod) throws BocException {
		try {
			return contFacade.tieneFichaProductoById(cod_prod);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
	}
	
	/**
	 * Actualiza estado ficha tecnica del producto
	 * @param prodId
	 * @param estadoFichaTecnica
	 * @return
	 * @throws BocException
	 */
	public boolean actualizaEstadoFichaTecnica(long prodId, long estadoFichaTecnica, String usrLogin, String mensaje) throws BocException {
		boolean result = false;
		try {
			result = contFacade.actualizaEstadoFichaTecnica(prodId, estadoFichaTecnica, usrLogin, mensaje);
		} catch (Exception ex) { // RemoteException ex
			throw new BocException(ex);
		}
		return result;
	}
	
	public boolean eliminaFichaProductoById(long prodId) throws BocException {
		try {
			return contFacade.eliminaFichaProductoById(prodId);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}
	

	public List getIdDetalleSolicitadoConExceso(long idPedido) throws BocException {
		// TODO Apéndice de método generado automáticamente
		try {
			return pedidosBolService.getIdDetalleSolicitadoConExceso(idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}



	public ProductosPedidoDTO getDetalleProductoSolicitado(long idPedido, long idDetalle) throws BocException {
		// TODO Apéndice de método generado automáticamente
		try {
			return pedidosBolService.getDetalleProductoSolicitado(idPedido,idDetalle);
		} catch (Exception ex) {
			throw new BocException(ex);
		}		
	}

	public List getDetallePickingByIdDetalle(long idDetalle, long idPedido) throws BocException {
		// TODO Apéndice de método generado automáticamente
		try {
			return pedidosBolService.getDetallePickingByIdDetalle(idDetalle, idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public boolean updatePrecioPickXPrecioSegunCantidad(long idDetalle,	long idPedido) throws BocException {
		// TODO Apéndice de método generado automáticamente
		try {
			return pedidosBolService.updatePrecioPickXPrecioSegunCantidad(idDetalle, idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public boolean deletePickingByIdDpicking(long idPedido, long idDpicking) throws BocException {
		// TODO Apéndice de método generado automáticamente
		try {
			return pedidosBolService.deletePickingByIdDpicking(idPedido, idDpicking);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}


	public boolean isExcesoCorreccionAutomatico(long idPedido) throws BocException {
		try {
			return pedidosBolService.isExcesoCorreccionAutomatico(idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}


	public List getTrackingExcesoByOP(long idPedido) throws BocException{
		try {
			return pedidosBolService.getTrackingExcesoByOP(idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}


	public boolean CorregirPrecioSegúnPrecioSolicitado(long idDetalle, long idPedido) throws BocException{
		try {
			return pedidosBolService.CorregirPrecioSegúnPrecioSolicitado(idDetalle, idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}

	public boolean ajustarCantidadSolicitada(long idPedido, long idDetalle) throws BocException {
		try {
			return pedidosBolService.ajustarCantidadSolicitada(idDetalle, idPedido);
		} catch (Exception ex) {
			throw new BocException(ex);
		}
	}
	

	/**
	 * 
	 * @param marca
	 * @return
	 * @throws BocException
	 */
	public boolean getExisteMarcaProducto( String marca ) throws BocException {
		
		try {
			
			return catalogacionMasivaService.getExisteMarcaProducto( marca );
		
		} catch ( Exception ex ) {
			
			throw new BocException( ex );
		
		}
	
	}
	
	/**
	 * 
	 * @param unidadMedidaPPM
	 * @return
	 * @throws BocException
	 */
	public boolean getExisteUnidadMedidaPPM( String unidadMedidaPPM ) throws BocException {
		
		try {
			
			return catalogacionMasivaService.getExisteUnidadMedidaPPM( unidadMedidaPPM );
		
		} catch ( Exception ex ) {
			
			throw new BocException( ex );
		
		}
	
	}
	
	/**
	 * 
	 * @param sectorPicking
	 * @return
	 * @throws BocException
	 */
	public boolean getExisteSectorPicking( String sectorPicking ) throws BocException {
		
		try {
			
			return catalogacionMasivaService.getExisteSectorPicking( sectorPicking );
		
		} catch ( Exception ex ) {
			
			throw new BocException( ex );
		
		}
	
	}
	
	/**
	 * 
	 * @param codLocal
	 * @return
	 * @throws BocException
	 */
	public String getLocalNoExiste( String codLocal ) throws BocException {
		
		try {
			
			return catalogacionMasivaService.getLocalNoExiste( codLocal );
		
		} catch ( Exception ex ) {
			
			throw new BocException( ex );
		
		}
	
	}
	
	/**
	 * 
	 * @param cod_prod1
	 * @param unidadMedida
	 * @return
	 * @throws BocException
	 */
	public String getValidandoDatosBO( String cod_prod1, String unidadMedida ) throws BocException {
		
		try {
			
			return catalogacionMasivaService.getValidandoDatosBO( cod_prod1, unidadMedida );
		
		} catch ( Exception ex ) {
			
			throw new BocException( ex );
		
		}
	
	}
	
	/**
	 * 
	 * @param listProductosXls
	 * @param usr
	 * @return
	 * @throws BocException
	 */
	public boolean getAgregandoProductosAlMix( List listProductosXls, UserDTO usr ) throws BocException {
		
		try {
			
			return catalogacionMasivaService.getAgregandoProductosAlMix( listProductosXls, usr );
		
		} catch ( Exception ex ) {
			
			throw new BocException( ex );
		
		}
	
	}
	
	/**
	 * 
	 * @param listProductosXls
	 * @param usr
	 * @return
	 * @throws BocException
	 */
	public boolean getCatalogarProducto( List listProductosXls, UserDTO usr ) throws BocException {
		
		try {
			
			return catalogacionMasivaService.getCatalogarProducto( listProductosXls, usr );
		
		} catch ( Exception ex ) {
			
			throw new BocException( ex );
		
		}
	
	}
	
	/**
	 * 
	 * @param listaCategorias
	 * @return
	 * @throws BocException
	 */
	public String getExisteCategoria( List listaCategorias ) throws BocException {
		
		try {
			
			return catalogacionMasivaService.getExisteCategoria( listaCategorias );
		
		} catch ( Exception ex ) {
			
			throw new BocException( ex );
		
		}
	
	}
	
	public List getLogByProductId(List productosCatalogacionMasiva) throws BocException {
		try {
			return catalogacionMasivaService.getLogByProductId(productosCatalogacionMasiva);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}
	
	public List getLogByProductIdSap(List productosCargaSapMasiva) throws BocException {
		try {
			return cargaSapMasivaService.getLogByProductId(productosCargaSapMasiva);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}
	
	/**
	 * 
	 * @param listProductosXls
	 * @param usr
	 * @return
	 * @throws BocException
	 */
	public boolean getInsertandoBOProducto( List listProductosXls, UserDTO usr ) throws BocException {
		
		try {
			
			return cargaSapMasivaService.getInsertandoBOProducto( listProductosXls, usr );
		
		} catch ( Exception ex ) {
			
			throw new BocException( ex );
		
		}
	
	}
	

	/**
	 * 
	 * @param idGrupo
	 * @return
	 * @throws BocException
	 */
	public boolean getExisteIdGrupo( String idGrupo ) throws BocException {
		
		try {
			
			return cargaSapMasivaService.getExisteIdGrupo( idGrupo );
		
		} catch ( Exception ex ) {
			
			throw new BocException( ex );
		
		}
	
	}
	
	//Reagenda jornada pedido con cambio local
	public boolean doReagendaDespachoLocal(PedidoDTO oPedido, JorDespachoCalDTO oJorDespachoNuevaCalDTO, cl.bbr.jumbocl.pedidos.dto.LocalDTO localDtoNuevo, double precioNuevo, boolean isRetiroLocal, long direccionId, boolean modificarJPicking) throws BocException{
		// TODO Apéndice de método generado automáticamente		
		try {
			
			return pedidosBolService.doReagendaDespachoLocal(oPedido, oJorDespachoNuevaCalDTO, localDtoNuevo, precioNuevo, isRetiroLocal, direccionId, modificarJPicking);
		
		} catch ( Exception ex ) {
			
			throw new BocException( ex );
		
		}
	}
	
	/**
	 * Inserta productos de la categoría para Grability 
	 * 
	 * @param id_cat
	 * @return boolean
	 */
	public boolean updateMarcaGrabilityProducto(String id_cat, String flag)
			throws BocException {
		try {
			return contFacade.updateMarcaGrabilityProducto(id_cat, flag);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}
	
	/**
	 * Elimina productos de la categoría para que no aparezcan en Grability
	 * 
	 * @param id_cat
	 * @return boolean
	 */
	public boolean deleteMarcaGrabilityProducto(String id_cat)
			throws BocException {
		try {
			return contFacade.deleteMarcaGrabilityProducto(id_cat);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}
	

	/**
	 * Obtiene el listado de categorias sap en mobile
	 * 
	 * @param inicio int
	 * @param fin int
	 * @return List CategoriaSapDTO
	 * @throws BocException
	 */
	public LinkedHashMap getCategoriasInGRB(int inicio, int fin, String idCat)throws BocException {
		try {
			return contFacade.getCategoriasInGRB(inicio, fin, idCat);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}
	
	
	/**
	 * Obtiene el listado de categorias sap en mobile
	 * 
	 * @param inicio int
	 * @param fin int
	 * @return List CategoriaSapDTO
	 * @throws BocException
	 */
	public LinkedHashMap getCategoriasNoGRB(String idCat)throws BocException {
		try {
			return contFacade.getCategoriasNoGRB(idCat);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	public int deleteCategoriaById(String seccion, int segmento) throws BocException{
		try {
			return contFacade.deleteCategoriaById(seccion, segmento);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}

	public int addCategoriaById(String seccion, int segmento) throws BocException{
		try {
			return contFacade.addCategoriaById(seccion, segmento);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BocException(ex);
		}
	}
}