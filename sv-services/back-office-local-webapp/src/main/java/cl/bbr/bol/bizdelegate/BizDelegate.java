package cl.bbr.bol.bizdelegate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.bbr.bol.dto.ParametroConsultaFaltantesDTO;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.bol.service.FaltantesService;
import cl.bbr.jumbocl.bolsas.service.BolsasService;
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.CasosCriterioDTO;
import cl.bbr.jumbocl.casos.dto.CasosDocBolDTO;
import cl.bbr.jumbocl.casos.dto.LogCasosDTO;
import cl.bbr.jumbocl.casos.dto.ProductoCasoDTO;
import cl.bbr.jumbocl.casos.dto.QuiebreCasoDTO;
import cl.bbr.jumbocl.casos.service.CasosService;
import cl.bbr.jumbocl.clientes.dto.ClienteCriteriaDTO;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.clientes.service.ClientesService;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.common.dto.RegionDTO;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.HorarioPickingEntity;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.common.model.SemanasEntity;
import cl.bbr.jumbocl.contenidos.dto.ProductosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosSapDTO;
import cl.bbr.jumbocl.contenidos.service.ContenidosService;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.parametros.dto.ParametroFoDTO;
import cl.bbr.jumbocl.parametros.service.ParametrosService;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPedDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPickDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcGeneraDespMasivoDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcIniciaFormPickingManualDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModTrxMPDetalleDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcRondasPropuestasDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcSectoresJornadaDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcZonasJornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.AdmHorarioDespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.AdmHorarioPickingDTO;
import cl.bbr.jumbocl.pedidos.dto.AvanceDTO;
import cl.bbr.jumbocl.pedidos.dto.BinFormPickDTO;
import cl.bbr.jumbocl.pedidos.dto.CalendarioDespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.CalendarioPickingDTO;
import cl.bbr.jumbocl.pedidos.dto.CambEnPagoOPDTO;
import cl.bbr.jumbocl.pedidos.dto.ChoferTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.CreaRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.CuponPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.DatosMedioPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.DespachoCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.DespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.DetalleFormPickingDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.FPickDTO;
import cl.bbr.jumbocl.pedidos.dto.FacturasDTO;
import cl.bbr.jumbocl.pedidos.dto.FonoTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaCriteria;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.LogRutaDTO;
import cl.bbr.jumbocl.pedidos.dto.PatenteTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidosCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.PoligonosZonaDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductoCbarraDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RechPagoOPDTO;
import cl.bbr.jumbocl.pedidos.dto.RondaDTO;
import cl.bbr.jumbocl.pedidos.dto.RondasCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaCriterioDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.pedidos.dto.SectorLocalDTO;
import cl.bbr.jumbocl.pedidos.dto.TrxMpCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.CalendarioException;
import cl.bbr.jumbocl.pedidos.exceptions.DespachosException;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosException;
import cl.bbr.jumbocl.pedidos.exceptions.RondasException;
import cl.bbr.jumbocl.pedidos.service.PedidosService;
import cl.bbr.jumbocl.usuarios.dto.ComandoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.jumbocl.usuarios.service.UsuariosService;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;
import cl.bbr.vte.empresas.service.EmpresasService;
import cl.cencosud.jumbocl.umbrales.dto.UmbralDTO;
import cl.cencosud.jumbocl.umbrales.service.UmbralesService;
//(+) INDRA (+)
//indra
// INDRA 05-12-2012
// INDRA 05-12-2012
//(-) INDRA (-)
//import cl.cencosud.jumbo.exceptions.SwitchException;

/**
 * Clase que contiene todos los metodos de:
 * - Pedidos
 * - Jornadas
 * - Rondas
 * - Calendario de Despacho
 * - Calendario de Picking
 * - Despacho
 * - Local
 * - Usuarios 
 * @author bbr
 *
 */
public class BizDelegate {

	/**
	 * PedidosService atributo que permite trabajar con los datos de:
	 * - Pedido
	 * - Jornada
	 * - calendarios de Picking
	 * - calendarios de Despacho
	 * - Rondas
	 * - Despachos
	 * - Local
	 * - Comunas 
	 */
	private static PedidosService pedidoService;
    
	/**
	 * UsuariosService atributo que permite trabajar con los datos de:
	 * - Usuarios
	 * - Perfiles
	 */
	private static UsuariosService 	usuariosService;
	/**
	 * ContenidosService atributo que permite trabajar con con los datos de:
	 * - Estados
	 * - Categorias
	 * - Productos
	 * - productos Sap
	 */
	private static ContenidosService  contFacade;
	
	private static EmpresasService    empresasService;
	
	private static CasosService       casosService;
	
	private static ClientesService    clientesService;
	
	private static ParametrosService  parametroService;
	//	INDRA 23-11-2012	
	private static FaltantesService faltantesService;
	//	INDRA 23-11-2012
	private static BolsasService  bolsasService;
	//(+) INDRA 2012-12-12 (+)
	private static UmbralesService umbralService;
	//(-) INDRA 2012-12-12 (-)

	
	

	
	/**
	 * Constructor Business Delegate
	 * 
	 */
	public BizDelegate() {
		if(pedidoService == null) 
			pedidoService = new PedidosService();
		if(parametroService == null) 
		    parametroService = new ParametrosService();
		if(usuariosService == null) 
			usuariosService = new UsuariosService();
		if(contFacade == null) 
			contFacade = new ContenidosService();
		if(empresasService == null)
			empresasService = new EmpresasService();
		if(casosService == null)
		    casosService = new CasosService();
		if(clientesService == null)
			clientesService = new ClientesService();
		if(bolsasService == null)
			bolsasService = new BolsasService();
			//indra
//		if (pedidosBolService == null)
//            pedidosBolService = new PedidosService();
		if (umbralService == null)
            umbralService= new UmbralesService(); 
            //indra
//		INDRA 23-11-2012
		if (faltantesService == null)
			faltantesService = new FaltantesService();
//		INDRA 23-11-2012
	}

	/**
	 * Obtiene el listado de estados de un pedido
	 * @throws BolException
	 * @return List EstadosDTO 
	 * 
	 */
	public List getEstadosPedido() throws BolException {
		try {
			return pedidoService.getEstadosPedido();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	/**
	 * Obtiene el listado de estados de una jornada
	 * @throws BolException
	 * @return List EstadosDTO 
	 * 
	 */
	public List getEstadosJornada() throws BolException {
		try {
			return pedidoService.getEstadosJornada();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
/**
 * obtiene el listado de estados de una ronda
 * @throws BolException
 * @return List EstadosDTO 
 * 
 */
	public List getEstadosRonda() throws BolException {
		try {
			return pedidoService.getEstadosRonda();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}

	public boolean valoresNegativos() {
		return pedidoService.valoresNegativos();
	}

	/*
	 * ------------ Pedidos ------------------
	 */	
	
	/**
	 * Retorna listados de pedido de acuerdo a una busqueda con los parámetros
	 * que se le pasan
	 * @param criterio PedidosCriteriaDTO
	 * @throws BolException
	 * @return List MonitorPedidosDTO
	 */
	public List getPedidosByCriteria(PedidosCriteriaDTO criterio)
			throws BolException {
		try {
			return pedidoService.getPedidosByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	/**
	 * Retorna listados de pedido de acuerdo a una busqueda con los parámetros
	 * que se le pasan
	 * @param criterio PedidosCriteriaDTO
	 * @throws BolException
	 * @return List MonitorPedidosDTO
	 */
	public List getPedidosByCriteriaHomologacion(PedidosCriteriaDTO criterio)
			throws BolException {
		try {
			return pedidoService.getPedidosByCriteriaHomologacion(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}

	public int calcularDiferenciaJornada(long id_jpicking) {
		int cantidad = 0;
		try {
			cantidad = pedidoService.calcularDiferenciaJornada(id_jpicking);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cantidad;
	}


	/**
	 * Retorna la cantidad de productos que resultan de la búsqueda con los
	 * parámetros que se le pasan
	 * @param criterio PedidosCriteriaDTO
	 * @throws BolException 
	 * @return long
	 */
	public long getCountPedidosByCriteria(PedidosCriteriaDTO criterio)
			throws BolException {
		try {

			return pedidoService.getCountPedidosByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}

	public int actualizarCapacidadOcupadaPicking(long id_jpicking) throws BolException {
    	try {
			return pedidoService.actualizarCapacidadOcupadaPicking(id_jpicking);
		} catch (ServiceException e) {
			throw new BolException(e);
		}
    }

/**
 * retorna los datos de un pedido en particular
 * @param id_pedido 
 * @throws BolException
 * @return PedidoDTO
 */

	public PedidoDTO getPedido(long id_pedido)
			throws BolException {
		try {
			return pedidoService.getPedido(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	
	
	/**
	 * retorna los productos de un pedido en particular pasandole el id_pedido
	 * @param id_pedido 
	 * @throws BolException
	 * @return List ProductosPedidoDTO
	 */
	public List getProductosPedido(long id_pedido)
		throws BolException {
		try {
			return pedidoService.getProductosPedido(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	

	}
	/**
	 * retorna productos sap pasandole el codigo del producto
	 * @param codProd
	 * @throws BolException
	 * @return ProductosSapDTO
	 */
	public ProductosSapDTO getProductosSapById(long codProd) throws BolException{	
		try{
			return contFacade.getProductosSapById(codProd);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
	}
	/**
	 * retorna codigos de barras de acuerdo a un codigo de producto en particular
	 * @param codProd
	 * @throws BolException
	 * @return List CodigosBarraSapDTO
	 */
	public List getCodBarrasByProdId(long codProd) throws BolException{	
		try{
			return contFacade.getCodBarrasByProdId(codProd);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
	}
	/**
	 * retorna los precios de un producto en particular
	 * @param codProd
	 * @throws BolException
	 * @return List PreciosSapDTO
	 */
	public List getPreciosByProdId(long codProd) throws BolException{	
		try{
			return contFacade.getPreciosByProdId(codProd);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
	}	
	/**
	 * retorna los bins asociados a un pedido en particular	 
	 * @param id_pedido
	 * @throws BolException
	 * @return List BinDTO
	 */
	public List getBinsPedido(long id_pedido)
		throws BolException {
		try {
			return pedidoService.getBinsPedido(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	
	
	}	

	/**
	 * retorna los bins asociados a un pedido en particular	 
	 * @param id_pedido
	 * @throws BolException
	 * @return List BinDTO
	 */
	public List getBinsPedidoPKL(long id_pedido)
		throws BolException {
		try {
			return pedidoService.getBinsPedidoPKL(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	
	
	}	


	/**
	 * Obtiene listado con log de eventos del pedido
	 * @param id_pedido
	 * @return List LogPedidoDTO
	 * @throws BolException	 
	 */	
	public List getLogPedido(long id_pedido)
		throws BolException {
		try {
			return pedidoService.getLogPedido(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	
	
	}

	/**
	 * retorna los sustitutos de un pedido en particular
	 * @param id_pedido
	 * @throws BolException
	 * @return List SustitutoDTO
	 */
	public List getSustitutosByPedidoId(long id_pedido)
		throws BolException {
			try {
				return pedidoService.getSustitutosByPedidoId(id_pedido);
			} catch (Exception ex) { // RemoteException ex
				// Translate the service exception into
				// application exception
				throw new BolException(ex);
			}	
	}	
	
	/**
	 * Modifica los datos de los productos sustitutos 
	 * @param mp DetallePickingDTO
	 * @throws BolException
	 */

	public void setDetallePickingSustituto(DetallePickingDTO mp)
	throws BolException, SystemException {
	try {
		pedidoService.setDetallePickingSustituto(mp);

	} catch (ServiceException ex) { // RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
	}	
}

	/**
	 * Obtiene los datos del detalle de picking 
	 * @param id_dpicking
	 * @throws BolException
	 * @return DetallePickingDTO
	 * 
	 */

	public DetallePickingDTO getDetallePickingById(long id_dpicking) throws BolException, SystemException {
		try {
			return pedidoService.getDetallePickingById(id_dpicking);
	
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	
	}
	
	/**
	 * Obtiene listado de faltantes de un pedido
	 * @param id_pedido
	 * @return List FaltanteDTO
	 * @throws BolException
	 */	
	public List getFaltantesByPedidoId(long id_pedido)
		throws BolException {
		try {
			return pedidoService.getFaltantesByPedidoId(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	
	
	}	
	
	/**
	 * Agrega al log del pedido
	 * @param log LogPedidoDTO
	 * @throws BolException
	 * @throws SystemException 
	 */
	public void addLogPedido(LogPedidoDTO log)
		throws BolException, SystemException {
		try {
			pedidoService.addLogPedido(log);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
    public boolean getRondaConFaltantes(long id_ronda)
        throws BolException, SystemException {
        try {
            return pedidoService.getRondaConFaltantes(id_ronda);
        }catch (ServiceException ex) { // RemoteException ex
            // Translate the service exception into
            // application exception
            throw new BolException(ex);
        }
    }

	
    public boolean setEstadoVerDetalle(long id_ronda)
        throws BolException, SystemException {
        try {
            return pedidoService.setEstadoVerDetalle(id_ronda);
        }catch (ServiceException ex) { // RemoteException ex
            // Translate the service exception into
            // application exception
            throw new BolException(ex);
        }
    }

	
    public boolean setEstadoImpEtiqueta(long id_ronda)
        throws BolException, SystemException {
        try {
            return pedidoService.setEstadoImpEtiqueta(id_ronda);
        }catch (ServiceException ex) { // RemoteException ex
            // Translate the service exception into
            // application exception
            throw new BolException(ex);
        }
    }

	
    public boolean setFechaImpListadoPKL(long id_ronda)
        throws BolException, SystemException {
        try {
            return pedidoService.setFechaImpListadoPKL(id_ronda);
        }catch (ServiceException ex) { // RemoteException ex
            // Translate the service exception into
            // application exception
            throw new BolException(ex);
        }
    }
	
    public boolean setFechaIniciaJornadaPKL(long id_ronda)
        throws BolException, SystemException {
        try {
            return pedidoService.setFechaIniciaJornadaPKL(id_ronda);
        }catch (ServiceException ex) { // RemoteException ex
            // Translate the service exception into
            // application exception
            throw new BolException(ex);
        }
    }

	
    public boolean ExisteFechaIniciaJornadaPKL(long id_ronda)
        throws BolException, SystemException {
        try {
            return pedidoService.ExisteFechaIniciaJornadaPKL(id_ronda);
        }catch (ServiceException ex) { // RemoteException ex
            // Translate the service exception into
            // application exception
            throw new BolException(ex);
        }
    }

    
	/**
	 * Retorna listado de productos de un bin
	 * @param id_bp
	 * @throws BolException
	 * @return List ProductosBinDTO
	 */
	public List getProductosBin(long id_bp)
		throws BolException {
		try {
			return pedidoService.getProductosBin(id_bp);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	
	
	}	
	/**
	 * permite cambiar el medio de pago de un pedido
	 * @param mp
	 * @throws BolException
	 * @throws SystemException
	 */
	public void setCambiarMedio_pago(DatosMedioPagoDTO mp)
		throws BolException, SystemException {
		try {
			pedidoService.setCambiarMedio_pago(mp);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	
	}
	/**
	 * retorna las transacciones de un medio de pago en particular 
	 * @param id_pedido
	 * @throws BolException
	 * @return List MonitorTrxMpDTO
	 */
	public List getTrxMpByIdPedido(long id_pedido) 
		throws BolException {
			try {
				return pedidoService.getTrxMpByIdPedido(id_pedido);
			} catch (Exception ex) { // RemoteException ex
				// Translate the service exception into
				// application exception
				throw new BolException(ex);
			}	
	}
	/**
	 * retorna el detalle transacciones de un medio de pago en particular 
	 * @param id_trxmp
	 * @throws BolException
	 * @return List TrxMpDetalleDTO
	 */
	public List getTrxPagoDetalleByIdTrxMp( long id_trxmp )
	throws BolException {
		try {
			 return pedidoService.getTrxPagoDetalleByIdTrxMp(id_trxmp);
		} catch (Exception ex) {
			//e.printStackTrace();
			throw new BolException(ex);
		}
	}	
	
	/**
	 * genera las transacciones del medio de pago de un pedido en particular
	 * @param id_pedido
	 * @throws BolException
	 * @throws SystemException
	 */
	public void doGeneraTrxMp(long id_pedido) 
		throws BolException, SystemException {
		try {
			pedidoService.doGeneraTrxMp(id_pedido);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	public double getMontoTotalDetPedidoByIdPedido(long id_pedido) 
        throws BolException, SystemException {
        try {
            return pedidoService.getMontoTotalDetPedidoByIdPedido(id_pedido);
        } catch (ServiceException ex) { // RemoteException ex
            // Translate the service exception into
            // application exception
            throw new BolException(ex);
        }
    }

	public double getMontoTotalDetPickingByIdPedido(long id_pedido) 
        throws BolException, SystemException {
        try {
            return pedidoService.getMontoTotalDetPickingByIdPedido(id_pedido);
        } catch (ServiceException ex) { // RemoteException ex
            // Translate the service exception into
            // application exception
            throw new BolException(ex);
        }
    }


	public ParametroDTO getParametroByName(String name) 
        throws BolException, SystemException {
        try {
            return parametroService.getParametroByName(name);
        } catch (ServiceException ex) { // RemoteException ex
            // Translate the service exception into
            // application exception
            throw new BolException(ex);
        }
    }
	
	public ParametroFoDTO getParametroFoByKey(String key) throws BolException, SystemException {
	        try {
	            return parametroService.getParametroFoByKey(key);
	        } catch (ServiceException ex) { // RemoteException ex
	            // Translate the service exception into
	            // application exception
	            throw new BolException(ex);
	        }
	    }

	/**
	 * retorna el cupon de pago de un pedido en particular
	 * @param id_pedido
	 * @param id_trxmp
	 * @throws BolException
	 * @throws SystemException
	 * @return CuponPagoDTO
	 */
	public CuponPagoDTO getCuponPago(long id_pedido, long id_trxmp) 
		throws BolException, SystemException {
		try {
			return pedidoService.getCuponPago(id_pedido, id_trxmp);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	
	}
	

	public HashMap getFacturasIngresadas(long id_pedido) 
		throws BolException, SystemException {
		try {
			return pedidoService.getFacturasIngresadas(id_pedido);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	
	}
	

	public int getCantFacturasIngresadas(long id_pedido) 
		throws BolException, SystemException {
		try {
			return pedidoService.getCantFacturasIngresadas(id_pedido);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	
	}
	

	
	public boolean ActListFacturas(FacturasDTO fact)
		throws BolException ,SystemException, ServiceException{
		try {
			return pedidoService.ActListFacturas(fact);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	


	public boolean setModTrxPagoDet(ProcModTrxMPDetalleDTO trx) 
	throws BolException, SystemException {
        try {
            return pedidoService.setModTrxPagoDet(trx);
	    } catch (ServiceException ex) { // RemoteException ex
		    // Translate the service exception into
		    // application exception
		    throw new BolException(ex);
        }
    }
	
	/**
	 * retorna una lista con los productos sueltos de un pedido en particular
	 * @param id_pedido 
	 * @throws BolException
	 * @return List ProductosPedidoDTO
	 */
	public List getProductosSueltosByPedidoId(long id_pedido) 
	throws BolException {
		try {
			return pedidoService.getProductosSueltosByPedidoId(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	
    }
	
	/**
	 * retorna un listado con los detalles de picking de un pedido en particular
	 * @param id_pedido
	 * @throws BolException
	 * @return List DetallePickingDTO
	 */
	public List getDetPickingByIdPedido(long id_pedido) 
	throws BolException {
		try {
			return pedidoService.getDetPickingByIdPedido(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	
	}
	
	
	/**
	 * retorna un listado con los detalles de picking de un pedido en particular
	 * @param id_pedido
	 * @throws BolException
	 * @return List DetallePickingDTO
	 */
	public List getDetPickingHojaDesp2(long id_pedido) 
	throws BolException {
		try {
			return pedidoService.getDetPickingHojaDesp2(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	
	}
	

	/**
	 * Obtiene una lista de secciones Sap
	 * @return list of CategoriaSapDTO's
	 * @throws BolException
	 */
	public List getSeccionesSap()throws BolException{
		try{
			return pedidoService.getSeccionesSap();
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
	}

	/**
	 * Obtiene el monto total de la hoja de despacho, segun el id_pedido 
	 * 
	 * @param id_pedido
	 * @return monto total.
	 */
	public double getMontoTotalHojaDespachoByIdPedido(long id_pedido) throws BolException{
		try{
			return pedidoService.getMontoTotalHojaDespachoByIdPedido(id_pedido);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
	}

	/*
	 * ------------ Jornadas ------------------
	 */
	
	/**
	 * Obtiene listado de jornadas por fecha
	 * @param fecha
	 * @param id_local
	 * @return List MonitorJornadasDTO
	 */
	public List getJornadasPickingByFecha(Date fecha, long id_local)
		throws BolException {
		try {
			return pedidoService.getJornadasPickingByFecha(fecha, id_local);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	
	
	/**
	 * Obtiene jornadas de acuerdo a criterios de consulta
	 * @param criterio
	 * @param id_local
	 * @return List MonitorJornadasDTO
	 */	
	public List getJornadasPickingByCriteria(JornadaCriteria criterio, long id_local)
		throws BolException {
		try {
			return pedidoService.getJornadasPickingByCriteria(criterio, id_local);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	//Maxbell arreglo homologacion
	public List getJornadasPickingByCriteriaEspeciales(JornadaCriteria criterio, long id_local)
			throws BolException {
			try {
				return pedidoService.getJornadasPickingByCriteriaEspeciales(criterio, id_local);
			} catch (Exception ex) { // RemoteException ex
				// Translate the service exception into
				// application exception
				throw new BolException(ex);
			}
		}
	
	
	/** (+) INDRA 2012-12-12 (+)
	 * Obtiene jornadas de acuerdo a si tienen pedidos en revision faltantes
	 * @param criterio
	 * @param id_local
	 * @return List MonitorJornadasDTO
	 */	
	public boolean existeJornadaRevFaltante( long id_jornada)
		throws BolException {
		try {
			return pedidoService.existeJornadaRevFaltante( id_jornada);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	//(-) INDRA 2012-12-12 (-)

	
	
	/**
	 * Obtiene datos de una jornada en particular
	 * @param id_jornada
	 * @return JornadaDTO
	 * @throws BolException
	 */
	public JornadaDTO getJornadaById(long id_jornada)
	throws BolException {
		try {
			return pedidoService.getJornadaById(id_jornada);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	

	
	
	/**
	 * Obtiene los pedidos por zona de una jornada dada
	 * @param datos
	 * @return List  TotProdZonJorDTO
	 * @throws BolException
	 */
	public List getTotalProductosZonasJornada(ProcZonasJornadaDTO datos)
	throws BolException {
		try {
			return pedidoService.getTotalProductosZonasJornada(datos);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	/**
	 * Obtiene la cantidad de productos por sección que deben ser
	 * pickeados en una jornada de picking
	 * @param id_jornada
	 * @return List TotProdSecJorDTO
	 */
	public List getTotalProductosSectorJornada(ProcSectoresJornadaDTO datos)
	throws BolException {
		try {
			return pedidoService.getTotalProductosSectorJornada(datos);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	
	/**
	 * le asigna una jornada al pedido  
	 * @param id_jornada
	 * @throws BolException
	 * @return boolean
	 */
	public boolean setPedidosByIdJornada(long id_jornada)
	throws BolException {
		try {
			return pedidoService.setPedidosByIdJornada(id_jornada);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}

	/**
	 * retorna una lista de comanda de preparados
	 * @param id_jornada
	 * @param id_sector 
	 * @throws BolException
	 * @return List ComandaPreparadosDTO
	 */
	public List getComandaPreparados(long id_jornada, String id_sector) 
	throws BolException {
		try {
			return pedidoService.getComandaPreparados(id_jornada, id_sector);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	/**
	 * Obtiene el log de eventos de una jornada
	 * @param id_jornada identificador de jornada
	 * @return List de LogSimpleDTO
	 */
	public List getLogJornada(long id_jornada)
	throws BolException {
		try {
			return pedidoService.getLogJornada(id_jornada);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}		
	/**
	 * agrega un log a la jornada de picking
	 * @param id_jornada
	 * @param user
	 * @param descr
	 * @throws BolException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void addLogJornadaPick(long id_jornada, String user, String descr)
	throws BolException ,SystemException, ServiceException {
	try {
		pedidoService.addLogJornadaPick(id_jornada, user, descr);
		} catch (Exception ex) { // RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}	

	}		
	
	
	public void addLogJornadaDesp(long id_jornada, String user, String descr)
			throws BolException ,SystemException, ServiceException {
			try {
				pedidoService.addLogJornadaDesp(id_jornada, user, descr);
				} catch (Exception ex) { // RemoteException ex
				// Translate the service exception into
				// application exception
				throw new BolException(ex);
				}	

			}	
	
	/**
	 * inicia una jornada de picking
	 * @param id_jpicking
	 * @throws BolException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void setIniciaJornada(long id_jpicking)
	throws BolException ,SystemException, ServiceException {
		try {
			pedidoService.setIniciaJornada(id_jpicking);
		} catch (Exception ex) { // RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}		
	}

	/*
	 * ------------- Calendario Picking ------------------
	 */
	
	/**
	 * Retorna una semana del calendario a partir de su id
	 * @param id_semana
	 * @return SemanasEntity
	 * @throws CalendarioException
	 */
	public SemanasEntity getSemanaById(long id_semana)
	throws BolException {
		try {
			return pedidoService.getSemanaById(id_semana);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}

	
	/**
	 * Obtiene el Calendario semanal de Jornadas de Picking
	 * @param n_semana numero de la semana en el año 
	 * @param ano año
	 * @param id_local
	 * @return CalendarioPickingDTO
	 * @throws SystemException 
	 * @throws ServiceException 
	 */	
	public CalendarioPickingDTO getCalendarioJornadasPicking(int n_semana, int ano, long id_local)
	throws BolException, SystemException {
		try {
			return pedidoService.getCalendarioJornadasPicking(n_semana, ano, id_local);
		} catch (ServiceException e) {
			throw new BolException(e);
		}
	}
	
	
	/**
	 * Obtiene horario de picking a partir de su id
	 * @param id_hor_pick
	 * @throws BolException
	 * @return HorarioPickingEntity
	 */
	public HorarioPickingEntity getHorarioPicking(long id_hor_pick)
	throws BolException {
		try {
			return pedidoService.getHorarioPicking(id_hor_pick);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}

	
	/**
	 * Obtiene listado de jornadas de picking para un horario
	 * @param id_hor_pick
	 * @return List JornadaPickingEntity
	 */
	public List getJornadasByIdHorario(long id_hor_pick)	
		throws BolException {
		try {
			return pedidoService.getJornadasByIdHorario(id_hor_pick);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	
	
	/**
	 * Inserta un horario con sus respectivas jornadas en el calendario de picking
	 *  para una semana determinada y un local determinado
	 * @param 	id_semana
	 * @param 	id_local
	 * @param 	hpicking
	 * @return	id_horario insertado
	 * @throws	CalendarioException 
	 */
	public long InsJornadasPickingyHorario(long id_semana, long id_local, AdmHorarioPickingDTO hpicking)
	throws BolException ,SystemException, ServiceException{
		try {
			return pedidoService.InsJornadasPickingyHorario(id_semana, id_local, hpicking);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	
	/**
	 * Modifica jornada de picking
	 * @param id_horpicking
	 * @param hpicking
	 * @throws BolException
	 */
	public void ModJornadaPicking(long id_horpicking, AdmHorarioPickingDTO hpicking)
		throws BolException ,SystemException, ServiceException{
		try {
			pedidoService.ModJornadaPicking(id_horpicking,hpicking);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	/**
	 * Elimina horario de picking de una semana junto con sus jornadas relacionadas 
	 * @param id_hor_pick
	 * @throws CalendarioException
	 */
	public void DelJornadasPickingyHorario(long id_hor_pick)
		throws BolException ,SystemException, ServiceException{
		try {
			pedidoService.DelJornadasPickingyHorario(id_hor_pick);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	/*
	 * ------------- Calendario Despacho ------------------
	 */

	
	/**
	 * Obtiene calendario de despacho
	 * @param n_semana número de semana del año
	 * @param ano año
	 * @param id_zona
	 * @return CalendarioDespachoDTO
	 * @throws BolException
	 */
	public CalendarioDespachoDTO getCalendarioDespacho(int n_semana, int ano, long id_zona)
		throws BolException {
		try {
			return pedidoService.getCalendarioDespacho(n_semana, ano, id_zona);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	
	
	
	
	/**
	 * Obtiene información de un horario de despacho
	 * @param id_hor_desp
	 * @return HorarioDespachoEntity
	 * @throws CalendarioException
	 */
	public HorarioDespachoEntity getHorarioDespacho(long id_hor_desp)
		throws BolException {
		try {
			return pedidoService.getHorarioDespacho(id_hor_desp);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	
	/**
	 * Obtiene listado de jornadas de despacho
	 * @param id_hor_desp
	 * @return List JornadaDespachoEntity
	 * @throws CalendarioException
	 */
	public List getJornadasDespachoByIdHorario(long id_hor_desp)
		throws BolException {
		try {
			return pedidoService.getJornadasDespachoByIdHorario(id_hor_desp);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	
	/**
	 * Inserta horario con jornadas de despacho relacionadas
	 * @param id_semana
	 * @param id_zona
	 * @param hdespacho
	 * @return long id_horario
	 * @throws CalendarioException
	 */
	public long InsJornadasDespachoyHorario(long id_semana, long id_zona, AdmHorarioDespachoDTO hdespacho)
		throws BolException,SystemException, ServiceException {
		try {
			return pedidoService.InsJornadasDespachoyHorario(id_semana, id_zona, hdespacho);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	
	/**
	 * Elimina jornadas de despacho de un horario
	 * @param id_hor_desp
	 * @throws ServiceException 
	 * @throws BolException, SystemException, ServiceException
	 */
	public void DelJornadasDespachoyHorario(long id_hor_desp)
		throws BolException, SystemException, ServiceException {
		try {
			pedidoService.DelJornadasDespachoyHorario(id_hor_desp);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	

	/**
	 * Modifica las jornadas de despacho de un horario
	 * @param id_hor_desp
	 * @param hdespacho
	 * @throws BolException, SystemException, ServiceException
	 */
	public void ModJornadaDespacho(long id_hor_desp, AdmHorarioDespachoDTO hdespacho, int id_zona )
		throws BolException, SystemException, ServiceException {
		try {
			pedidoService.ModJornadaDespacho(id_hor_desp, hdespacho, id_zona);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	
	
	/*
	 * ------------- Rondas ------------------
	 */

	/**
	 * Agrega registro al log de la ronda
	 * @param id_ronda
	 * @param usuario
	 * @param log
	 * @throws BolException, SystemException, ServiceException
	 */
	public void addLogRonda(long id_ronda, String usuario, String log)
		throws BolException , SystemException, ServiceException{
		
		try {
			pedidoService.addLogRonda(id_ronda, usuario, log);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
		
	}
	
	
	/**
	 * Obtiene resultado consulta de rondas de acuerdo a criterio
	 * @param criterio RondasCriterioDTO
	 * @return List MonitorRondasDTO
	 * @throws BolException
	 */
	public List getRondasByCriteria(RondasCriteriaDTO criterio)
		throws BolException {
		try {
			return pedidoService.getRondasByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	

	
	/**
	 * Obtiene resultado consulta de rondas de acuerdo a criterio, para monitor de rondas
	 * @param criterio
	 * @return
	 * @throws BolException
	 */
	/*public List getMonRondasByCriteria(RondasCriteriaDTO criterio)
	throws BolException {
		try {
			return pedidoService.getMonRondasByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}*/

	/**
	 * Obtiene resultado consulta de rondas de acuerdo a criterio, para monitor de rondas
	 * @param criterio
	 * @return
	 * @throws BolException
	 */
	public List getMonRondasByCriteriaCMO(RondasCriteriaDTO criterio)
	throws BolException {
		try {
			return pedidoService.getMonRondasByCriteriaCMO(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	

	/**
	 * Obtiene cantidad de productos pickeados y sin pickear y sus porcentajes respecto al total
	 * @param id_jornada_picking
	 * @return
	 * @throws BolException
	 */
	public AvanceDTO getAvanceJornada(long id_jornada_picking)
	throws BolException {
		try {
			return pedidoService.getAvanceJornada(id_jornada_picking);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	

	//Maxbell Arreglo homologacion
    public int getCantidadOpPasadosPorBodega(long id_jornada) {    	
		try {
			return pedidoService.getCantidadOpPasadosPorBodega(id_jornada);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
        return 0;
    }

	/**
	 * Obtiene cantidad de productos pickeados y sin pickear y sus porcentajes respecto al total
	 * @param id_jornada_picking
	 * @return
	 * @throws BolException
	 */
	public AvanceDTO getAvancePedido(long id_pedido)
	throws BolException {
		try {
			return pedidoService.getAvancePedido(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	

	/** (+) INDRA 2012-12-12 (+)
	 * Obtiene cantidad de productos pickeados y sin pickear y sus porcentajes respecto al total para ver si se pasan o no  a bodega
	 * @param id_jornada_picking
	 * @return
	 * @throws BolException
	 */
	public AvanceDTO getAvanceUmbralPedido(long id_pedido)
	throws BolException {
		try {
			return pedidoService.getAvanceUmbralPedido(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	

	/**
	 * Obtiene cantidad de productos pickeados y sin pickear y sus porcentajes respecto al total para ver si se pasan o no  a bodega
	 * @param id_jornada_picking
	 * @return
	 * @throws BolException
	 */
	public AvanceDTO getAvanceUmbralMonto(long id_pedido)
	throws BolException {
		try {
			return pedidoService.getAvanceUmbralMonto(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	
	
	/**
	 * Obtiene cantidad de umbrales de unidad y monto respecto al total para ver si se pasan o no  a bodega
	 * @param id_jornada_picking
	 * @return
	 * @throws BolException
	 */
	public AvanceDTO getAvanceUmbralParametros(long id_pedido)
	throws BolException {
		try {
			return pedidoService.getAvanceUmbralParametros(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	
	//(-) INDRA 2012-12-12 (-)

	/**
	 * Obtiene número de registros de una búsuqe da de rondas por criterio
	 * @param criterio RondasCriteriaDTO
	 * @return long
	 * @throws BolException
	 */
	public long getCountRondasByCriteria(RondasCriteriaDTO criterio)
		throws BolException {
		try {
			return pedidoService.getCountRondasByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	
	
	
	/**
	 * Obtiene lista de rondas para un pedido
	 * @param id_pedido
	 * @return List MonitorRondasDTO
	 * @throws BolException
	 */
	public List getRondasByIdPedido(long id_pedido)	
		throws BolException {
		try {
			return pedidoService.getRondasByIdPedido(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	
	
	
	/**
	 * Obtiene el detalle de una ronda
	 * @param id_ronda
	 * @return RondaDTO
	 * @throws BolException
	 */
	public RondaDTO getRondaById(long id_ronda)
	throws BolException {
		try {
			return pedidoService.getRondaById(id_ronda);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}

	
	/**
	 * Obtiene el log de la ronda
	 * @param id_ronda
	 * @return List LogSimpleDTO
	 * @throws BolException
	 */
	public List getLogRonda(long id_ronda)
	throws BolException {
		try {
			return pedidoService.getLogRonda(id_ronda);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	
	
	
	/**
	 * Obtiene listado de productos de la ronda
	 * @param id_ronda
	 * @return List ProductosPedidoDTO
	 * @throws BolException
	 */
	public List getProductosRonda(long id_ronda)
		throws BolException {
			try {
				return pedidoService.getProductosRonda(id_ronda);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	

	
	/**
	 * Obtiene listado de productos de la ronda
	 * @param id_ronda
	 * @return List ProductosPedidoDTO
	 * @throws BolException
	 */
	public List getProductosRondaPKL(long id_ronda)
		throws BolException {
			try {
				return pedidoService.getProductosRondaPKL(id_ronda);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	


	/**
	 * Obtiene id_pedido asociado a Ronda Picking Light 
	 * @param id_ronda
	 * @return long id_pedido
	 * @throws BolException
	 */
	public long getIdPedidoByRondaPKL(long id_ronda)
		throws BolException {
			try {
				return pedidoService.getIdPedidoByRondaPKL(id_ronda);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	

	/**
	 * Obtiene Listado productos que no han sido pickeados
	 * agrupados por OP y por sector, para aquellas jornadas
	 * que han sido iniciadas
	 * @param id_local
	 * @param id_sector
	 * @param id_jornada 
	 * @return List RondaPropuestaDTO
	 * @throws BolException
	 */
	public List getRondasPropuestasDet(ProcRondasPropuestasDTO criterio)
		throws BolException {
			try {
				return pedidoService.getRondasPropuestasDet(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	public List getRondasPropuestas(ProcRondasPropuestasDTO criterio)
	throws BolException {
		try {
			return pedidoService.getRondasPropuestas(criterio);
	} catch (Exception ex) { // RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
	}
}

	
	/**
	 * Obtiene Listado pedidos que no han sido pickeados (Picking light)
	 * agrupados por OP y por sector, para aquellas jornadas
	 * que han sido iniciadas
	 * @param id_local
	 * @param id_jornada 
	 * @return List RondaPropuestaDTO
	 * @throws BolException
	 */
	public List getPedidosXJornada(PedidosCriteriaDTO criterio)
		throws BolException {
			try {
				return pedidoService.getPedidosXJornada(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}

	/**
	 * Obtiene Listado de bins de la ronda
	 * @param id_ronda
	 * @return List BinCriteriaDTO
	 * @throws BolException
	 */
	public List getBinsRonda(long id_ronda)
		throws BolException {
			try {
				return pedidoService.getBinsRonda(id_ronda);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	

	

	/**
	 * Obtiene Listado de bins de la ronda
	 * @param id_ronda
	 * @return List BinCriteriaDTO
	 * @throws BolException
	 */
	public List getBinsRondaPKL(long id_ronda)
		throws BolException {
			try {
				return pedidoService.getBinsRondaPKL(id_ronda);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	

	
	/**
	 * Crea una ronda para un pedido por sector, incluyendo la cantidad de productos
	 * indicada
	 * @param ronda CreaRondaDTO
	 * @throws BolException, SystemException 
	 * @return long
	 */
	public long CreaRonda(CreaRondaDTO ronda)
		throws BolException, SystemException {
			try {
				return pedidoService.CreaRonda(ronda);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	
	
	/**
	 * Crea una ronda para un pedido por pedido, incluyendo la cantidad de productos
	 * indicada
	 * @param ronda CreaRondaDTO
	 * @throws BolException, SystemException 
	 * @return long
	 */
	public long CreaRondaPKL(CreaRondaDTO ronda)
		throws BolException, SystemException {
			try {
				return pedidoService.CreaRondaPKL(ronda);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	

	
	/**
	 * Realiza proceso de preparación de ronda
	 * @param id_ronda
	 * @throws BolException, SystemException, ServiceException
	 */
	public void PreparaRonda(long id_ronda)
		throws BolException, SystemException, ServiceException {
			try {
				pedidoService.PreparaRonda(id_ronda);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	/**
	 * retorna una lista con el resumen de una ronda en particular
	 * @param id_ronda
	 * @throws BolException
	 * @return List RondaDetalleResumenDTO
	 */
	public List getResumenRondaById(long id_ronda)
		throws BolException {
			try {
				return pedidoService.getResumenRondaById(id_ronda);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}

	
	/**
	 * Asigna pickeador a la ronda y cambia su estado a En Picking
	 * @param id_ronda
	 * @param id_usuario
	 * @throws SystemException 
	 * @throws RondasException
	 */
	public void doAsignaRonda(long id_ronda, long id_usuario) 
		throws BolException, SystemException {
			try {
				pedidoService.doAsignaRonda(id_ronda, id_usuario);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	
	
	/**
	 * retorna una lista de sustitutos para una ronda en particular
	 * @param id_ronda	
	 * @throws BolException
	 * @return List SustitutoDTO
	 */
	public List getSustitutosByRondaId(long id_ronda) 	
		throws BolException {
		try {
			return pedidoService.getSustitutosByRondaId(id_ronda);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	/**
	 * retorna una lista de faltantes para una ronda en particular
	 * @param id_ronda	
	 * @throws BolException
	 * @return List FaltanteDTO
	 */
	public List getFaltantesByRondaId(long id_ronda)  	
		throws BolException {
		try {
			return pedidoService.getFaltantesByRondaId(id_ronda);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	/*
	 * ------------- Despachos ------------------
	 */
	
	/**
	 * Obtiene listado de estados de despacho
	 * @return List EstadoDTO
	 * @throws DespachosException
	 */
	public List getEstadosDespacho()
		throws BolException {
			try {
				return pedidoService.getEstadosDespacho();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
		
	/**
	 * Obtiene listado de pedidos en su flujo de despacho
	 * @param criterio DespachoCriteriaDTO
	 * @return List MonitorDespachosDTO
	 */	
	public List getDespachosByCriteria(DespachoCriteriaDTO criterio)
		throws BolException {
			try {
				return pedidoService.getDespachosByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	
	
	
	/**
	 * Obtiene Count(*) consulta de pedidos
	 * @param criterio DespachoCriteriaDTO
	 * @return long
	 */
	public long getCountDespachosByCriteria( DespachoCriteriaDTO criterio )
		throws BolException {
			try {
				return pedidoService.getCountDespachosByCriteria(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	
	
	/**
	 * Obtiene información del despacho de un pedido
	 * @param id_pedido
	 * @return DespachoDTO
	 */
	public DespachoDTO getDespachoById(long id_pedido)
		throws BolException {
			try {
				return pedidoService.getDespachoById(id_pedido);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}		
	

	/**
	 * Cambia el estado a un pedido en su fase de despacho
	 * @param id_pedido
	 * @param id_estado
	 * @param login usuario que realiza la operación
	 * @param log
	 * @throws BolException, SystemException, ServiceException
	 */
	public void setCambiaEstadoDespacho(long id_pedido, long id_estado, String login, String log)
		throws BolException, SystemException, ServiceException{
		try {
				System.out.println("en el BizDelegate, antes del set");
				pedidoService.setCambiaEstadoDespacho(id_pedido, id_estado, login, log);
				System.out.println("en el BizDelegate, despues del set");
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			System.out.println("en el catch del bizdelegate");
			throw new BolException(ex);
		}
	}	
	
	
	/**
	 * Agrega registro al log del despacho
	 * @param id_pedido
	 * @param login String 
	 * @param log String 
	 */
	public void addLogDespacho(long id_pedido, String login, String log)
		throws BolException, SystemException, ServiceException {
		try {
				pedidoService.addLogDespacho(id_pedido, login, log);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	
	
	
	/**
	 * obtiene listado del log de un despacho
	 * @param id_pedido
	 * @return List LogSimpleDTO
	 * @throws BolException 
	 */
	public List getLogDespacho(long id_pedido)
		throws BolException {
			try {
				return pedidoService.getLogDespacho(id_pedido);
			} catch (Exception ex) { // RemoteException ex
				// Translate the service exception into
				// application exception
				throw new BolException(ex);
			}
		}
	

	/*
	 * ------------- Local ------------------
	 */
	
	/**
	 * Retorna un listado de Sectores de Picking
	 * @param id_local long 
	 * @return List SectorLocalDTO
	 * @throws BolException
	 */	
	public List getSectores()
		throws BolException {
		try {
				return pedidoService.getSectores();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}

	
	
	/**
	 * Retorna un listado con las Zonas de Despacho de un Local
	 * @param id_local
	 * @return List ZonaDTO
	 * @throws BolException
	 */	
	public List getZonasLocal(long id_local)
		throws BolException {
			try {
				return pedidoService.getZonasLocal(id_local);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}		
	
	/**
	 * Obtiene el detalle de una zona de despacho
	 * @param id_zona
	 * @return ZonaDTO
	 * @throws BolException
	 */
	public ZonaDTO getZonaDespacho(long id_zona)
		throws BolException {
			try {
				return pedidoService.getZonaDespacho(id_zona);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}		

	
	/**
	 * Obtiene el listado de comunas pertenecientes a una zona de despacho
	 * @param id_zona
	 * @return List ComunaDTO
	 * @throws BolException
	 */
	public List getComunasByIdZonaDespacho(long id_zona) throws BolException {
		try {
			return pedidoService.getComunasByIdZonaDespacho(id_zona);
		} catch (Exception ex) {
			throw new BolException(ex);
		}
	}	
	
    
    public List getPoligonosXComunaSinZona(long id_comuna) throws SystemException, BolException {
        try {
            return pedidoService.getPoligonosXComunaSinZona(id_comuna);
        } catch (ServiceException e) {
            throw new BolException(e);
        }
    }
    
    public List getComunasConPoligonosSinZona() throws SystemException, BolException {
        try {
            return pedidoService.getComunasConPoligonosSinZona();
        } catch (ServiceException e) {
            throw new BolException(e);
        }
    }
    
    public List getPoligonosXZona(long id_zona) throws SystemException, BolException {
        try {
            return pedidoService.getPoligonosXZona(id_zona);
        } catch (ServiceException e) {
            throw new BolException(e);
        }
    }
    

	/**
	 * Obtiene el listado total de comunas
	 * @return List ComunaDTO
	 * @throws BolException
	 */
	public List getComunasAll()
		throws BolException {
			try {
				return pedidoService.getComunasAll();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}	
	
	
	
	/**
	 * Obtiene listado de comunas de un local
	 * @param id_local
	 * @return List ComunaDTO
	 * @throws PedidosException 
	 */
	public List getComunasLocal(long id_local)
	throws BolException{
		try {
			return pedidoService.getComunasLocal(id_local);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}		
	}
	
	
	/**
	 * Actualiza listado de comunas de una Zona
	 * @param dto	
	 * @throws SystemException 
	 * @throws BolException 
	 */
	/*public void doActualizaComunasZonaDespacho(ComunasZonaDTO dto)
	throws BolException, SystemException{
		try {
			pedidoService.doActualizaComunasZonaDespacho(dto);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}		
	}*/
	
	
	/**
	 * Actualiza listado de Poligonos de una Zona
	 * @param dto	
	 * @throws SystemException 
	 * @throws BolException 
	 */
	public void doActualizaPoligonosZonaDespacho(PoligonosZonaDTO dto)
	throws BolException, SystemException{
		try {
			pedidoService.doActualizaPoligonosZonaDespacho(dto);
		} catch (ServiceException ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}		
	}
	

	/**
	 * Agrega una zona de despacho
	 * @param zona ZonaDTO 
	 * @return long id_zona insertada
	 * @throws PedidosException
	 */
	public long doAgregaZonaDespacho(ZonaDTO zona)
		throws BolException , SystemException, ServiceException{
		
		try {
			return pedidoService.doAgregaZonaDespacho(zona);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}		
		
	}
	
	
	/**
	 * modifica una zona de despacho para un local
	 * @param zona ZonaDTO 	 
	 * @throws BolException , SystemException, ServiceException
	 */
	public void doModZonaDespacho(ZonaDTO zona)
		throws BolException , SystemException, ServiceException{
		
		try {
			pedidoService.doModZonaDespacho(zona);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}		
		
	}

	
	//******************* Usuarios ***********************//
	/**
	 * retorna los datos de un usuario en particular
	 * @param id_user
	 * @throws BolException, SystemException 
	 * @return UserDTO
	 */
	public UserDTO getUserById(long id_user) throws BolException, SystemException {
		try {
			return usuariosService.getUserById(id_user);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new BolException(e);
		}	
		
	}
	
	/**
	 * Obtiene usuario, retorna null si no existe
	 * @param login
	 * @return UserDTO
	 * @throws BolException
	 * @throws SystemException
	 */
	public UserDTO getUserByLogin(String login)
		throws BolException, SystemException{
		
		try {
			return usuariosService.getUserByLogin(login);
		  } catch (ServiceException ex) { //RemoteException ex
			// Translate the service exception into
			// application exception
			  throw new BolException(ex);
		  }	
		
	}	
	/**
	 * retorna los comandos por nombre
	 * @param comando
	 * @throws BolException
	 * @return ComandoDTO
	 */
	public ComandoDTO getComandoByName(String comando)
		throws BolException{
		
		try {
			return usuariosService.getComandoByName(comando);
		  } catch (Exception ex) { //RemoteException ex
			// Translate the service exception into
			// application exception
			  throw new BolException(ex);
		  }	
		
	}
	/**
 	 * permite checkear los permiso que se asigna segun el perfil del comando	 
	 * @param usr
	 * @param id_comando
	 * @throws BolException
	 * @return boolean
	 */
	public boolean doCheckPermisoPerfilComando(UserDTO usr, long id_comando)
		throws BolException{
		
		try {
			return usuariosService.doCheckPermisoPerfilComando(usr, id_comando);
		  } catch (Exception ex) { //RemoteException ex
			// Translate the service exception into
			// application exception
			  throw new BolException(ex);
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
		throws BolException, SystemException {
		
		try {
			return usuariosService.doAutenticaUser(login, password);
		  } catch (ServiceException ex) { //RemoteException ex
			// Translate the service exception into
			// application exception
			  throw new BolException(ex);
		  }	
		
	}
	

	/**
	 * Obtiene el listado de secciones SAP
	 * @return List SeccionSapDTO
	 * @throws BolException
	 */
	public List getSeccionesSAPPreparadosByIdJornada(long id_jornada) throws BolException {
			try {
				return pedidoService.getSeccionesSAPPreparadosByIdJornada(id_jornada);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	/**
	 * retorna el count(*) de los productos en ronda segun el id de la jornada
	 * @param id_jornada
	 * @throws BolException
	 * @return double
	 */
	public double getCountProdEnRondaByIdJornada(long id_jornada) throws BolException {
		try {
			return pedidoService.getCountProdEnRondaByIdJornada(id_jornada);
	} catch (Exception ex) { // RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
	}
}


	/**
	 * retorna los productos del pedido según su sector
	 * @param id_pedido
	 * @param id_sector
	 * @param id_local
	 * @throws BolException
	 * @return List ProductosPedidoDTO
	 */
	public List getProdPedidoXSector(long id_pedido, long id_sector, long id_local) throws BolException{
		try {
			return pedidoService.getProdPedidoXSector(id_pedido, id_sector, id_local);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}


	/**
	 * retorna los productos del pedido según su sector
	 * @param id_pedido
	 * @param id_sector
	 * @param id_local
	 * @throws BolException
	 * @return List ProductosPedidoDTO
	 */
	public List getProdSinPickearXPedidoXSector(long id_pedido, long id_sector, long id_local) throws BolException{
		try {
			return pedidoService.getProdSinPickearXPedidoXSector(id_pedido, id_sector, id_local);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}

	/**
	 * retorna la categoria sap segun el codigo de la categoria
	 * @param cod_cat
	 * @return String
	 * @throws BolException
	 */
	public ClientesDTO getClienteByRut(long rut) throws BolException {
		try{
			return clientesService.getClienteByRut(rut);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
	}
	
	
	/**
	 * retorna la categoria sap segun el codigo de la categoria
	 * @param cod_cat
	 * @return String
	 * @throws BolException
	 */
	public String getCatSapById(String cod_cat) throws BolException {
		try{
			return contFacade.getCatSapById(cod_cat);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
	}

	
	public void doAddSectorPicking(SectorLocalDTO sector) throws BolException , SystemException, ServiceException{
		
		try {
			pedidoService.doAddSectorPicking(sector);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	
	}
	public void doActualizaSectorPicking(SectorLocalDTO sector) throws BolException , SystemException, ServiceException{
		
		try {
			pedidoService.doActualizaSectorPicking(sector);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	
	}

	public void doEliminaSectorPicking(long id_sector) throws BolException , SystemException, ServiceException{
		
		try {
			pedidoService.doEliminaSectorPicking(id_sector);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	
	}

	/**
	 * Obtiene los datos del producto Web segun el id del producto SAP
	 * 
	 * @param id_producto
	 * @return ProductoEntity
	 */
	public ProductoEntity getProductoFOByIdProdSap(long id_producto) throws BolException {
		try {
			return contFacade.getProductoFOByIdProdSap(id_producto);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}	
	}

	/**
	 * Rechaza el pago del pedido
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws BolException
	 */
	public boolean setRechazaPagoOP(RechPagoOPDTO dto) throws BolException {
		try {
			return pedidoService.setRechazaPagoOP(dto);
		} catch (Exception ex) { 

			throw new BolException(ex);
		}	
	}

	/**
	 * Resetea el estado de la ronda
	 * 
	 * @param id_ronda
	 * @return boolean
	 * @throws BolException
	 */
	public boolean setReseteaRonda(long id_ronda) throws BolException {
		try {
			return pedidoService.setReseteaRonda(id_ronda);
		} catch (Exception ex) { 

			throw new BolException(ex);
		}	
	}

	/**
	 * Listado de locales
	 * @return List LocalDTO
	 * @throws BolException
	 */
	public List getLocales() throws BolException {
		try {
			return usuariosService.getLocales();
		} catch (Exception ex) { 

			throw new BolException(ex);
		}	
	}

	/**
	 * Obtiene listado de estados
	 * 
	 * @param  tipo
	 * @param  visible
	 * @return List EstadoDTO
	 * @throws BolException
	 */
	public List getEstadosByVis(String tipo, String visible) throws BolException{
		try {
			return contFacade.getEstadosByVis(tipo, visible);
		  } catch (Exception ex) { //RemoteException ex
			// Translate the service exception into
			// application exception
			  throw new BolException(ex);
		  }	
		
	}

	/**
	 * Cambia estado del pedido a 'En Pago'
	 * 
	 * @param coll
	 * @return boolean
	 * @throws BolException 
	 */
	public boolean setCambiarEnPagoOP(CambEnPagoOPDTO coll) throws BolException {
		try {
			return pedidoService.setCambiarEnPagoOP(coll);
		} catch (Exception ex) { 

			throw new BolException(ex);
		}
	}
	
	/**
	 * Permite generar la jornada de despacho en forma pasiva
	 * 
	 * @param  dto  
	 * @return boolean
	 * @throws BolException
	 */
	public boolean doGeneraDespMasivo(ProcGeneraDespMasivoDTO dto) throws BolException {
		try {
			return pedidoService.doGeneraDespMasivo(dto);
		} catch (Exception ex) { 

			throw new BolException(ex);
		}
	}

	/**
	 * Obtiene el listado de transacciones de pago 
	 * segun local facturador. En base a criterios 
	 * de busqueda y paginacion
	 */
	
	public List getTrxMpByCriteria(TrxMpCriteriaDTO criterio) throws BolException {
		try {
			return pedidoService.getTrxMpByCriteria(criterio);
		} catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	/**
	 * Obtiene el total de registros de transacciones de pago 
	 * segun local facturador. En base a criterios 
	 * de busqueda y paginacion
	 */
	public long getCountTrxMpByCriteria(TrxMpCriteriaDTO criterio) throws BolException {
		try {
			return pedidoService.getCountTrxMpByCriteria(criterio);
		} catch (Exception ex) { 
			throw new BolException(ex);
		}
	}	
	
	/**
	 * retorna los sustitutos de un pedido en particular, de la forma como se envían en el email
	 * (Modificación según req. 500)
	 * @param id_pedido
	 * @throws BolException
	 * @return List SustitutoDTO
	 */
	public List productosEnviadosPedidoForEmail(long id_pedido)
		throws BolException {
			try {
				return pedidoService.productosEnviadosPedidoForEmail(id_pedido);
			} catch (Exception ex) { // RemoteException ex
				// Translate the service exception into
				// application exception
				throw new BolException(ex);
			}	
	}	
	
	// Formulario de Picking

	/**
	 * Obtiene un listado de bins asociados a una ronda (Formulario de picking)
	 * @param id_ronda
	 * @throws BolException
	 */
	public List getBinsFormPickByRondaId(long id_ronda) throws BolException {
		try {
			return pedidoService.getBinsFormPickByRondaId(id_ronda);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	
	/**
	 * Permite agregar un bin a una ronda (Formulario de Picking)
	 * @param bin
	 * @throws BolException
	 */
	public void doAgregaBinRonda(BinFormPickDTO bin)	 throws BolException {
		try {
			 pedidoService.doAgregaBinRonda(bin);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	/**
	 * Permite agregar un registro de picking (Formulario de picking)
	 * @param pick DetalleFormPickingDTO
	 * @param bin BinFormPickDTO
	 * @return long idbin
	 * @throws BolException
	 */
	public long doAgregaDetalleFormPicking(FPickDTO pick, BinFormPickDTO bin )  throws BolException {
		try {
			return pedidoService.doAgregaDetalleFormPicking(pick, bin);
		} catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	/**
	 * Obtiene los productos pickeados en una ronda (Formulario de picking)
	 * @param id_ronda
	 * @return List
	 * @throws BolException
	 */
	public List getDetallePickFormPick(ProcFormPickDetPickDTO datos) throws BolException {
		try {
			return pedidoService.getDetallePickFormPick(datos);
		} catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	
	/**
	 * Retorna un listado de productos en base al criterio
	 * @param  criterio ProductosCriteriaDTO 
	 * @return List ProductosDTO
	 * @throws BocException 
	 */
	public List getProductosByCriteria(ProductosCriteriaDTO criterio) throws BolException{	
		try{
			return contFacade.getProductosByCriteria(criterio);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
	}
	
	/**
	 * Retorna la cantidad de productos en base al criterio
	 * @param  criterio ProductosCriteriaDTO 
	 * @return int
	 * @throws BocException 
	 */
	public int getProductosCountByCriteria(ProductosCriteriaDTO criterio) throws BolException{	
		try{
			return contFacade.getProductosCountByCriteria(criterio);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
	}
	
	/**
	 * Obtiene una lista de productos con sus codigos de barra
	 * @param criteria
	 * @return List
	 * @throws BolException
	 */
	public List getProdCbarraByCriteria(ProductosCriteriaDTO criteria) throws BolException{	
		try{
			return contFacade.getProdCbarraByCriteria(criteria);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	
	/**
	 * Obtiene los productos con sus codigos de barra, pertenecientes a una ronda especificada
	 * @param id_ronda
	 * @return List 
	 * @throws BolException
	 */
	public List getProductosCbarraRonda(ProcFormPickDetPedDTO datos) throws BolException{	
		try{
			return pedidoService.getProductosCbarraRonda(datos);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	/**
	 * Obtiene la cantidad de productos existentes
	 * @param criteria
	 * @return int
	 * @throws BolException
	 */
	public int getCountProdCbarraByCriteria(ProductosCriteriaDTO criteria) throws BolException{	
		try{
			return contFacade.getCountProdCbarraByCriteria(criteria);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
	}
	
	/**
	 * Obtiene el producto segun un codigo de barras
	 * @param cod_barra
	 * @return ProductoCbarraDTO
	 * @throws BolException
	 */
	public ProductoCbarraDTO getProductoByCbarra(String cod_barra) throws BolException{	
		try{
			return pedidoService.getProductoByCbarra(cod_barra);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
	}
	
	
	/**
	 * Elimina un detalle de picking (Formulario de picking)
	 * @param id_row
	 * @return boolean
	 * @throws BolException
	 */
	public boolean doDelPickFormPicking(long id_pick) throws BolException{	
		try{
			return pedidoService.doDelPickFormPicking(id_pick);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}

	/**
	 * Inicializa el formulario de picking manual copiando los datos necesarios en las tablas del 
	 * formulario de picking.
	 * @param ProcIniciaFormPickingManualDTO datos
	 * @throws BolException
	 */
	public void doIniciaFormPickingManual(ProcIniciaFormPickingManualDTO datos) throws BolException{
		try{
			pedidoService.doIniciaFormPickingManual(datos);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	/**
	 * Finzaliza el formulario de picking manual entregando los datos recopilados 
	 * al metodo que recepciona las rondas de picking usado por la PDA.
	 * @param ProcIniciaFormPickingManualDTO datos
	 * @throws BolException
	 */
	public void doFinalizaFormPickingManual(ProcIniciaFormPickingManualDTO datos) throws BolException{
		try{
			pedidoService.doFinalizaFormPickingManual(datos);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	/**
	 * relaciona un detalle de picking a un detalle de pedido
	 * @param datos
	 * @param datos_ped
	 * @throws BolException
	 */
	public void doRelacionFormPick(DetalleFormPickingDTO datos,ProductosPedidoDTO datos_ped, FPickDTO fpick) throws BolException{	
		try{
			pedidoService.doRelacionFormPick(datos, datos_ped, fpick);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	/**
	 * Actualiza un detalle de pedido 
	 * @param datos_ped
	 * @throws BolException
	 */
	public boolean setActFormPickDetPed(ProductosPedidoDTO datos_ped) throws BolException{	
		try{
			return pedidoService.setActFormPickDetPed(datos_ped);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	/**
	 * Obtiene un listado con los productos faltantes pertenecientes a una ronda.
	 * @param id_ronda
	 * @throws BolException
	 */
	public List getFormPickFaltantes(long id_ronda) throws BolException{	
		try{
			return pedidoService.getFormPickFaltantes(id_ronda);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	/**
	 * Obtiene la lista de productos sustitutos asociados a una ronda
	 * @param id_ronda
	 * @param id_local
	 * @throws BolException
	 */
	public List getFormPickSustitutos(long id_ronda, long id_local) throws BolException{	
		try{
			return pedidoService.getFormPickSustitutos(id_ronda,id_local);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	
	
	/**
	 * Permite reversar un producto sustituto
	 * @param datos_ped
	 * @param fpick
	 * @param id_row
	 * @return boolean
	 * @throws BolException
	 */
	public boolean doReversaSustitutoFormPick(ProductosPedidoDTO datos_ped ,FPickDTO fpick, long id_row)
	throws BolException{	
		try{
			return pedidoService.doReversaSustitutoFormPick(datos_ped,fpick,id_row);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	
	
	/**
	 * Obtiene el detalle de un producto relacionado
	 * @param id_row
	 * @throws BolException
	 */
	public DetalleFormPickingDTO getRelacionFormPickById(long id_row) 
	throws BolException{	
		try{
			return pedidoService.getRelacionFormPickById(id_row);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	/**
	 * Obtiene la cantidad de productos relacionados segun id_fpick
	 * @param id_fpick
	 * @throws BolException
	 */
	public long getCountFormPickRelacion(long id_fpick) 
	throws BolException{	
		try{
			return pedidoService.getCountFormPickRelacion(id_fpick);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	/**
	 * Permite reversar un picking
	 * @param id_pick
	 * @throws BolException
	 */
	public boolean doReversaPickingFormPick(long id_pick) 
	throws BolException{	
		try{
			return pedidoService.doReversaPickingFormPick(id_pick);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	/**
	 * Relaciona automaticamente todos los detalles de picking que coinciden
	 * en codigos de barra y cantidad con el detalle de pedido
	 * @param id_ronda
	 * @throws BolException
	 */
	public void doRelacionAutomaticaFormPick(long id_ronda)
	throws BolException{	
		try{
			pedidoService.doRelacionAutomaticaFormPick(id_ronda);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	
	/**
	 * Permite saber si existe un pedido para una ronda determinada
	 * @param id_ronda
	 * @param id_pedido
	 * @throws BolException
	 */
	public boolean isExisteRondaPedido(long id_ronda, long id_pedido)
	throws BolException{	
		try{
			 return pedidoService.isExisteRondaPedido(id_ronda, id_pedido);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	/**
	 * Obtiene un listado con las ops asociadas a una ronda
	 * @param id_ronda
	 * @return
	 * @throws BolException
	 */
	public List getPedidosByRonda(long id_ronda) throws BolException{	
		try{
			return pedidoService.getPedidosByRonda(id_ronda);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	
	/*  ***********************************************************************************
	 *  METODOS DE EMPRESAS 
	 *  ***********************************************************************************
	 *  */
	/**
	 * Obtiene informacion de la empresa y listado de sucursales, segun id
	 * 
	 * @param  id	Identificador único de la empresa
	 * @return Datos de la empresa (EmpresasDTO) 
	 * @throws BolException
	 */
	public EmpresasDTO getEmpresaById(long id_empresa)
	throws BolException{	
		try{
			return empresasService.getEmpresaById(id_empresa);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	/**
	 * Obtiene la información de la sucursal , segun el código
	 * 
	 * @param id_cliente	Identificador único para la sucursal
	 * @return 		SucursalesDTO
	 * @throws BolException
	 */
	public SucursalesDTO getSucursalById(long id_cliente)
	throws BolException{	
		try{
			return empresasService.getSucursalById(id_cliente);
		}catch (Exception ex) { 
			throw new BolException(ex);
		}
	}
	
	/**
	 * Obtiene listado de estados de trx. mp
	 * @return List EstadoDTO
	 * @throws BolException
	 */
	public List getEstadosTrxMp()
		throws BolException {
			try {
				return pedidoService.getEstadosTrxMp();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	
	/**
	 * Obtiene los datos de un local segun su id.
	 * @param id_local
	 * @return
	 * @throws BolException
	 */
	public LocalDTO getLocalByID(long id_local) throws BolException{
		try{
			return clientesService.getLocalById(id_local);
		}catch (Exception ex){
			throw new BolException(ex);
		}
	}
	
	/**
	 * Obtiene los datos de un Cliente segun Criterio.
	 * @param criterio
	 * @return
	 * @throws BolException
	 */
	public List getClienteByCriterio(ClienteCriteriaDTO criterio) throws BolException{
		try{
			return clientesService.getClientesByCriteria(criterio);
		}catch (Exception ex){
			throw new BolException(ex);
		}
	}

	/**
	 * Obtiene los datos de un Cliente por RUT.
	 * @param criterio
	 * @return
	 * @throws BolException
	 */
	public ClientesDTO getClienteByCriterio(long rut) throws BolException{
		try{
			return clientesService.getClienteByRut(rut);
		}catch (Exception ex){
			throw new BolException(ex);
		}
	}

	
	/**
	 * retorna una lista de comanda de preparados
	 * @param id_jornada
	 * @param id_sector 
	 * @throws BolException
	 * @return List ComandaVerifStockDTO
	 */
	public List getComandaVerifStock(long id_jornada,  long id_sector) 
	throws BolException {
		try {
			return pedidoService.getComandaVerifStock(id_jornada, id_sector);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}

	/**
	 * Obtiene las secciones de verificacion de stock en base a la cantidad minima de cada sector de una jornada
	 * @param id_jornada
	 * @return List
	 * @throws BolException
	 */
	public List getSeccionesLocalVerifStockByIdJornada(long id_jornada) throws BolException {
		try {
			return pedidoService.getSeccionesLocalVerifStockByIdJornada(id_jornada);
	} catch (Exception ex) { // RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
	}
}

	/**
	 * Obtiene el listado de zonas de una ronda
	 * @param id_ronda
	 * @return List ZonaDTO
	 * @throws BolException
	 */
	public List getBuscaZonaByRonda(long id_ronda)throws BolException {
		try {
			return pedidoService.getBuscaZonaByRonda(id_ronda);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	/**
	 * Recupera listado de zonas finalizadas para las jornadas de la ronda.
	 * 
	 * @param id_ronda	Identificador único
	 * @return			Listado de zonas
	 * @throws PromocionesException
	 */
	public List getZonasFinalizadas(long id_ronda)  throws BolException {
		try {
			return pedidoService.getZonasFinalizadas( id_ronda );
			
		} catch (Exception ex) {
			throw new BolException(ex);
		}
	}
	
	
	/**
	 * Permite obtener el resumen de jornada de despacho (estadisticas)
	 * @param criterio
	 * @return
	 * @throws BolException
	 */
	public List getResumenJorDespacho(DespachoCriteriaDTO criterio) throws BolException {
		try {
			return pedidoService.getResumenJorDespacho(criterio);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	
	/**
	 * Retorna listado de casos segun el criterio 
	 * @param  criterio CasosCriterioDTO 
	 * @return List MonitorCasosDTO
	 * @throws BocException 
	 */
	public List getCasosByCriterio(CasosCriterioDTO criterio) throws BolException {
		try{
			return casosService.getCasosByCriterio(criterio);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
	}
	
	/**
	 * Retorna listado de estados de casos
	 * @return List EstadoCasoDTO
	 * @throws BocException 
	 */
	public List getEstadosDeCasos() throws BolException {
		try{
			return casosService.getEstadosDeCasos();
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
	}
    /**
     * Retorna cantidad de casos segun el criterio de busqueda
     * @param criterio Busqueda
     * @return Cantidad de casos
     */
    public double getCountCasosByCriterio(CasosCriterioDTO criterio) throws BolException {
        try{
			return casosService.getCountCasosByCriterio(criterio);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }
    /**Nos devuelve el ID del caso que esta editando un usuario
     * @param usr Usuario
     * @return ID del caso en edicion. Retorna '0' si no está editando ningun caso
     */
    public long getCasoEnEdicionByUsuario(UserDTO usr) throws BolException {
        try{
			return casosService.getCasoEnEdicionByUsuario(usr);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }
    /**Libera el caso que esta en edición
     * @param idCaso ID del caso que será liberado
     * @return True o False según el resultado de la operación
     */
    public boolean setLiberaCaso(long idCaso) throws BolException {
        try{
			return casosService.setLiberaCaso(idCaso);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }
    
    /**
     * Obtiene los datos de un caso con su ID
     * @param idCaso ID Caso
     * @return CasoDTO
     */
    public CasoDTO getCasoByIdCaso(long idCaso) throws BolException {
        try{
			return casosService.getCasoByIdCaso(idCaso);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }
    /**
     * Retorna un listado con los productos de un caso
     * 
     * @param idCaso ID de Caso
     * @param Tipo Tipo: Productos a Enviar = 'S', Productos a Retirar = 'N' 
     * @return Listado de Productos
     */
    public List getProductosByCasoAndTipo(long idCaso, String tipo) throws BolException {
        try{
			return casosService.getProductosByCasoAndTipo(idCaso,tipo);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }

    /**
     * Obtiene los datos de un producto mediante su identificador
     * @param idProducto Identificador del producto
     * @return Producto obtenido
     */
    public ProductoCasoDTO getProductoById(long idProducto) throws BolException {
        try{
			return casosService.getProductoById(idProducto);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }

    /**
     * Permite modificar un producto de un caso
     * @param producto Producto a modificar
     * @return resultado de la acción
     */
    public boolean modProductoCaso(ProductoCasoDTO producto) throws BolException {
        try{
			return casosService.modProductoCaso(producto);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }

    /**
     * Permite modificar el usuario que está editando el caso
     * @param idCaso Identificador del caso
     * @param idUsuario Identificador del usuario que va a modificar el caso
     * @return Resultado de la acción
     */
    public boolean setModCaso(long idCaso, long idUsuario) throws BolException {
        try{
			return casosService.setModCaso(idCaso,idUsuario);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }

    /**
     * Retorna un lsitado con las Jornadas en las cuales se puede resolver un caso
     * @return Listado con Jornadas
     */
    public List getJornadas() throws BolException {
        try{
			return casosService.getJornadas();
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }

    /**
     * Retorna un listado con los Quiebres que puede tener un caso 
     * @return Listado de Quiebres
     */
    public List getQuiebres() throws BolException {
        try{
			return casosService.getQuiebres();
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }

    /**
     * Retorna un listado con todos los posibles responsables de un caso
     * @return Listado de responsables de caso 
     */
    public List getResponsables() throws BolException {
        try{
			return casosService.getResponsables();
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }

    /**
     * Agregamos un nuevo registro de Log
     * @param log Datos del Log a guardar
     */
    public void addLogCaso(LogCasosDTO log) throws BolException {
        try{
			casosService.addLogCaso(log);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }

    /**
     * Retorna un listado con la información del Log de un caso.
     * @param idCaso Identificaador del caso
     * @return Listado del log
     */
    public List getLogCaso(long idCaso) throws BolException {
        try{
			return casosService.getLogCaso(idCaso);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }

    /**
     * Nos permite modificar los datos de un caso.
     * @param caso El caso con los nuevos datos
     * @return El estado de la modificación
     */
    public boolean modCaso(CasoDTO caso) throws BolException {
        try{
			return casosService.modCaso(caso);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }

    /**
     * Obtiene un quiebre de caso, mediante su identificador
     * @param idQuiebre Identificador del quiebre
     * @return Un objeto con los datos del quiebre
     */
    public QuiebreCasoDTO getQuiebreById(long idQuiebre) throws BolException {
        try{
			return casosService.getQuiebreById(idQuiebre);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }

    /**
     * Obtiene un Responsable de caso, medianto su identificador
     * @param idResponsable Identificador del Responsable
     * @return Un objeto con los datos del responsable
     */
    public ObjetoDTO getResponsableById(long idResponsable) throws BolException {
        try{
			return casosService.getResponsableById(idResponsable);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }

    /**
     * Obtiene el ID del usuario que esta editando el caso.
     * @param idCaso Identificador del caso
     * @return Identificador del usuario que está editando. Si el caso no está siendo editado, retorna un CERO.
     */
    public long getIdUsuarioEditorDeCaso(long idCaso) throws BolException {
        try{
			return casosService.getIdUsuarioEditorDeCaso(idCaso);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }

    /**
     * Obtiene un listado de Documentos Bol, asociado al Id del caso 
     * @param idCaso Identificador del caso
     * @return Listado de documentos
     */
    public List getDocBolCasoByCaso(long idCaso) throws BolException {
        try{
			return casosService.getDocBolCasoByCaso(idCaso);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }

    /**
     * Obtiene un Documento Bol, mediante su ID
     * @param idDocBolCaso Identificador del documento
     * @return Documento BOL
     */
    public CasosDocBolDTO getDocBolCasoById(long idDocBolCaso) throws BolException {
        try{
			return casosService.getDocBolCasoById(idDocBolCaso);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
    }

    /**
     * Agregar un nuevo Documento Bol de caso, impreso por usuario del bol
     * @param caso Documento impreso
     * @return Id del doc impreso
     */
    public long addDocBolCaso(CasosDocBolDTO caso) throws BolException, ServiceException, SystemException {
		try {
			return casosService.addDocBolCaso(caso);
		  } catch (ServiceException ex) { //RemoteException ex
			// Translate the service exception into
			// application exception
			  ex.printStackTrace();
			  throw new BolException(ex);
		  }
    }

    /**
     * Nos entrega el numero de compras por cliente.
     * @param rutCliente RUT del Cliente
     * @return cantidad de compras
     */
    public long getNroComprasByCliente(long rutCliente) throws BolException, ServiceException, SystemException {
		try {
		    return casosService.getNroComprasByCliente(rutCliente);
		} catch (ServiceException ex) {
			ex.printStackTrace();
			throw new BolException(ex);
		}
    }

    /**
     * Nos entrega el numero de casos por cliente
     * @param rutCliente RUT del cliente
     * @return cantidad de casos
     */
    public long getNroCasosByCliente(long rutCliente) throws BolException, ServiceException, SystemException {
		try {
		    return casosService.getNroCasosByCliente(rutCliente);
		} catch (ServiceException ex) {
			ex.printStackTrace();
			throw new BolException(ex);
		}
    }
    
    	
	/**
	 * Permite saber si un producto acepta sustitutos, segun sus promociones
	 * @param id_detalle
	 * @return
	 * @throws BocException
	 */
	public boolean promoPermiteSustitutos(long id_detalle) throws BolException{
		try {
			return pedidoService.promoPermiteSustitutos(id_detalle);
		}catch(Exception ex){
			throw new BolException(ex);
		}
	}
	
	/**
	 * Permite saber si un producto acepta faltantes, segun sus promociones
	 * @param id_detalle
	 * @return
	 * @throws BolException
	 */
	public boolean promoPermiteFaltantes(long id_detalle) throws BolException{
		try {
			return pedidoService.promoPermiteFaltantes(id_detalle); 
		}catch(Exception ex){
			throw new BolException(ex);
		}
	}

    /**
     * @param string
     * @return
     */
    public List getMotivosByEstado(String estado) throws BolException {
        try {
            return casosService.getMotivosByEstado(estado);
        } catch (Exception ex) { //RemoteException ex
            // Translate the service exception into
            // application exception
            throw new BolException(ex);
        }
    }

    /**
     * @param id_comuna
     * @return
     */
    public RegionDTO getRegionByComuna(long idComuna) throws BolException{
        try {
            return pedidoService.getRegionByComuna(idComuna); 
        }catch(Exception ex){
            throw new BolException(ex);
        }
    }

    /**
     * @return
     */
    public List getRegiones() throws BolException {
        try {
            return pedidoService.getRegiones();
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param idRegion
     * @return
     */
    public List getComunasByRegion(int idRegion) throws BolException {
        try {
            return pedidoService.getComunasByRegion(idRegion);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param idComuna
     * @return
     */
    public List getZonasByComuna(int idComuna) throws BolException {
        try {
            return pedidoService.getZonasByComuna(idComuna);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param ped
     */
    public long addPedidoExt(PedidoDTO ped) throws BolException {
        try {
            return pedidoService.addPedidoExt(ped);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param rut
     * @return
     */
    public PedidoDTO getUltimoPedidoJumboVAByRut(long rut) throws BolException {
        try {
            return pedidoService.getUltimoPedidoJumboVAByRut(rut);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param idObjeto
     * @return
     */
    public FonoTransporteDTO getFonoTransporteById(long idFono) throws BolException {
        try {
            return pedidoService.getFonoTransporteById(idFono);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param idObjeto
     * @param id_local
     * @return
     */
    public ChoferTransporteDTO getChoferTransporteById(long idChofer) throws BolException {
        try {
            return pedidoService.getChoferTransporteById(idChofer);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param idObjeto
     * @param id_local
     * @return
     */
    public PatenteTransporteDTO getPatenteTransporteById(long idPatente) throws BolException {
        try {
            return pedidoService.getPatenteTransporteById(idPatente);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param idEmpresaTransporte
     * @param id_local
     * @return
     */
    public List getFonosDeTransporte(long idEmpresaTransporte, long idLocal) throws BolException {
        try {
            return pedidoService.getFonosDeTransporte(idEmpresaTransporte, idLocal);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param idEmpresaTransporte
     * @param id_local
     * @return
     */
    public List getChoferesDeTransporte(long idEmpresaTransporte, long idLocal) throws BolException {
        try {
            return pedidoService.getChoferesDeTransporte(idEmpresaTransporte, idLocal);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param idEmpresaTransporte
     * @param id_local
     * @return
     */
    public List getPatentesDeTransporte(long idEmpresaTransporte, long idLocal) throws BolException {
        try {
            return pedidoService.getPatentesDeTransporte(idEmpresaTransporte, idLocal);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param criterio
     * @return
     */
    public List getDespachosParaMonitorByCriteria(DespachoCriteriaDTO criterio) throws BolException {
        try {
            return pedidoService.getDespachosParaMonitorByCriteria(criterio);
        } catch (Exception ex) { 
            throw new BolException(ex);
        }
    }

    /**
     * @return
     */
    public List getJornadasDespachoParaFiltro(long idLocal, String fecha, long idZona) throws BolException {
        try {
            return pedidoService.getJornadasDespachoParaFiltro(idLocal, fecha, idZona);
        } catch (Exception ex) { 
            throw new BolException(ex);
        }
    }
    
    public List getJornadasDespacho(int localId, Date fechaIni, Date fechaFin) throws BolException {
       try {
           return pedidoService.getJornadasDespacho(localId, fechaIni, fechaFin);
       } catch (Exception ex) { 
           throw new BolException(ex);
       }
   }


    /**
     * @param ruta
     */
    public long addRuta(RutaDTO ruta) throws BolException {
        try {
            return pedidoService.addRuta(ruta);
        } catch (Exception ex) { 
            throw new BolException(ex);
        }
    }

    /**
     * @param log
     */
    public void addLogRuta(LogRutaDTO log) throws BolException {
        try {
            pedidoService.addLogRuta(log);
        } catch (Exception ex) { 
            throw new BolException(ex);
        }
    }

    /**
     * @param l
     * @param idRuta
     * @return
     */
    public int addPedidoRuta(long idPedido, long idRuta) throws BolException {
        try {
            return pedidoService.addPedidoRuta(idPedido, idRuta);
        } catch (Exception ex) { 
            throw new BolException(ex);
        }
    }

    /**
     * @param id_local
     * @return
     */
    public List getRutasDisponibles(long idLocal) throws BolException {
        try {
            return pedidoService.getRutasDisponibles(idLocal);
        } catch (Exception ex) { 
            throw new BolException(ex);
        }
    }

    /**
     * @param idRuta
     * @return
     */
    public RutaDTO getRutaById(long idRuta) throws BolException {
        try {
            return pedidoService.getRutaById(idRuta);
        } catch (Exception ex) { 
            throw new BolException(ex);
        }
    }

    /**
     * @param criterio
     * @return
     */
    public List getRutasByCriterio(RutaCriterioDTO criterio) throws BolException {
        try {
            return pedidoService.getRutasByCriterio(criterio);
        } catch (Exception ex) { 
            throw new BolException(ex);
        }
    }

    /**
     * @param criterio
     * @return
     */
    public double getCountRutasByCriterio(RutaCriterioDTO criterio) throws BolException {
        try {
            return pedidoService.getCountRutasByCriterio(criterio);
        } catch (Exception ex) { 
            throw new BolException(ex);
        }
    }

    /**
     * @return
     */
    public List getEstadosRuta() throws BolException {
        try {
            return pedidoService.getEstadosRuta();
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param id_local
     * @param fecha
     * @return
     */
    public List getJornadasDespachoDisponiblesByZona(long idZona, String fecha, String tipoPedido) throws BolException {
        try {
            return pedidoService.getJornadasDespachoDisponiblesByZona(idZona, fecha, tipoPedido);
        } catch (Exception ex) { 
            throw new BolException(ex);
        }
    }

    /**
     * @return
     */
    public List getLogRuta(long idRuta) throws BolException {
        try {
            return pedidoService.getLogRuta(idRuta);
        } catch (Exception ex) { 
            throw new BolException(ex);
        }
    }

    /**
     * @param idRuta
     * @return
     */
    public List getPedidosByRuta(long idRuta) throws BolException {
        try {
            return pedidoService.getPedidosByRuta(idRuta);
        } catch (Exception ex) { 
            throw new BolException(ex);
        }
    }

    /**
     * @param l
     * @param idRuta
     * @return
     */
    public int delPedidoRuta(long idPedido) throws BolException {
        try {
            return pedidoService.delPedidoRuta(idPedido);
        } catch (Exception ex) { 
            throw new BolException(ex);
        }
    }

    /**
     * @param estado_ruta_anulada
     * @param idRuta
     */
    public void setEstadoRuta(int estado, long idRuta) throws BolException {
        try {
            pedidoService.setEstadoRuta(estado, idRuta);
        } catch (Exception ex) { 
            throw new BolException(ex);
        }
    }

    /**
     * @param idRuta
     */
    public void liberarPedidosByRuta(long idRuta) throws BolException {
        try {
            pedidoService.liberarPedidosByRuta(idRuta);
        } catch (Exception ex) { 
            throw new BolException(ex);
        }
    }

    /**
     * @param id_pedido
     * @return
     */
    public List getDetPickingToHojaDespacho(long id_pedido) throws BolException {
        try {
            return pedidoService.getDetPickingToHojaDespacho(id_pedido);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param idRuta
     * @param cantBins
     */
    public void actualizaCantBinsRuta(long idRuta) throws BolException {
        try {
            pedidoService.actualizaCantBinsRuta(idRuta);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param id_pedido
     * @param string
     */
    public void modificaVecesEnRutaDePedido(long idPedido, String operacion) throws BolException {
        try {
            pedidoService.modificaVecesEnRutaDePedido(idPedido, operacion);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param id_pedido
     * @return
     */
    public RutaDTO getRutaByPedido(long idPedido) throws BolException {
        try {
            return pedidoService.getRutaByPedido(idPedido);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param idLocal
     * @return
     */
    public List getPatentesDeTransporteByLocal(long idLocal) throws BolException {
        try {
            return pedidoService.getPatentesDeTransporteByLocal(idLocal);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param idLocal
     * @return
     */
    public List getFonosDeTransporteByLocal(long idLocal) throws BolException {
        try {
            return pedidoService.getFonosDeTransporteByLocal(idLocal);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param idLocal
     * @return
     */
    public List getChoferesDeTransporteByLocal(long idLocal) throws BolException {
        try {
            return pedidoService.getChoferesDeTransporteByLocal(idLocal);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param idPatente
     */
    public void delPatenteTransporteById(long idPatente) throws BolException {
        try {
            pedidoService.delPatenteTransporteById(idPatente);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param pat
     */
    public long addPatenteTransporte(PatenteTransporteDTO patente) throws BolException {
        try {
            return pedidoService.addPatenteTransporte(patente);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param pat
     */
    public void modPatenteTransporte(PatenteTransporteDTO patente) throws BolException {
        try {
            pedidoService.modPatenteTransporte(patente);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param i
     */
    public void delFonoTransporteById(long idFono) throws BolException {
        try {
            pedidoService.delFonoTransporteById(idFono);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param fono
     */
    public long addFonoTransporte(FonoTransporteDTO fono) throws BolException {
        try {
            return pedidoService.addFonoTransporte(fono);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param fono
     */
    public void modFonoTransporte(FonoTransporteDTO fono) throws BolException {
        try {
            pedidoService.modFonoTransporte(fono);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param l
     */
    public void delChoferTransporteById(long idChofer) throws BolException {
        try {
            pedidoService.delChoferTransporteById(idChofer);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param chofer
     */
    public long addChoferTransporte(ChoferTransporteDTO chofer) throws BolException {
        try {
            return pedidoService.addChoferTransporte(chofer);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param chofer
     */
    public void modChoferTransporte(ChoferTransporteDTO chofer) throws BolException {
        try {
            pedidoService.modChoferTransporte(chofer);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @return
     */
    public List getEmpresasTransporteActivas() throws BolException {
        try {
            return pedidoService.getEmpresasTransporteActivas();
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param idChofer
     * @param descripcion
     * @param idUsuario
     */
    public void addLogChoferTransporte(long idChofer, String descripcion, long idUsuario) throws BolException {
        try {
            pedidoService.addLogChoferTransporte(idChofer, descripcion, idUsuario);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param idFono
     * @param descripcion
     * @param idUsuario
     */
    public void addLogFonoTransporte(long idFono, String descripcion, long idUsuario) throws BolException {
        try {
            pedidoService.addLogFonoTransporte(idFono, descripcion, idUsuario);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

    /**
     * @param idPatente
     * @param descripcion
     * @param idUsuario
     */
    public void addLogPatenteTransporte(long idPatente, String descripcion, long idUsuario) throws BolException {
        try {
            pedidoService.addLogPatenteTransporte(idPatente, descripcion, idUsuario);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
    }

	
	/**
	 * Obtiene el Monto del Pedido a partir del número de OP 
	 * @params id_pedido
	 * @returns monto
	 * */
	public long getMontoTotalTrxByIdPedido(long id_pedido)
		throws  BolException {
		
		try{
			return pedidoService.getMontoTotalTrxByIdPedido(id_pedido);
		}catch (Exception ex) { //RemoteException ex
		// Translate the service exception into
		// application exception
		throw new BolException(ex);
		}
	}
	
	
	
	
	// BOLSAS DE REGALO
	public List getStockBolsasRegalo(String cod_sucursal)
		throws  BolException {
		
		try{
			return bolsasService.getStockBolsasRegalo(cod_sucursal);
		}catch (Exception ex) { //RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	public List getBitacoraBolsasRegalo(String cod_sucursal)
		throws  BolException {
		
		try{
			return bolsasService.getBitacoraBolsasRegalo(cod_sucursal);
		}catch (Exception ex) { //RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	public void actualizarStockBolsa(String cod_bolsa, String cod_sucursal, int stock)
		throws  BolException {
		
		try{
			bolsasService.actualizarStockBolsa(cod_bolsa, cod_sucursal, stock);
		}catch (Exception ex) { //RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	public void insertarRegistroBitacoraBolsas(String desc_operacion, String usuario, String cod_sucursal)
		throws  BolException {
		
		try{
			bolsasService.insertarRegistroBitacoraBolsas(desc_operacion, usuario, cod_sucursal);
		}catch (Exception ex) { //RemoteException ex
			// Translate the service exception into
			// application exception
			throw new BolException(ex);
		}
	}
	
	// FIN BOLSAS DE REGALO
	
	/**
	 * Metodo que se encarga de recuperar los productos sustitutos sobre el margen de sustitución 
	 * para ser retirados de los bins
	 * @return <code>List</code> Listado de <code>ProductoSobreMargenDTO</code> con los productos sustitutos sobre umbral
	 * @throws SystemException
	 * @throws BolException
	 */
	public List getProductosSustitutosSobreMargen(long id_local) throws SystemException, BolException {
        try {
            return pedidoService.getProductosSustitutosSobreMargen(id_local);
        } catch (ServiceException e) {
            throw new BolException(e);
        }
    }
	
	/**
	 * Metodo que se encarga de eliminar un detalle del reporte de producto sustituto sobre margen 
	 * @param idEliminar id de detalle a eliminar
	 * @return boolean true en caso de exito false en caso de error
	 * @throws SystemException
	 * @throws BolException
	 */
	public boolean eliminarProductoSustitutoSobreMargen(long idEliminar) throws SystemException, BolException {
        try {
            return pedidoService.eliminarProductoSustitutoSobreMargen(idEliminar);
        } catch (ServiceException e) {
            throw new BolException(e);
        }
    }
	
	   /** indra
     * Modifica el estado de un pedido.
     * @param  id_pedido long 
     * @param  id_estado long 
     * @return boolean, devuelve true en el caso que la actualización fue satisfactoria, caso contrario devuelve false.
     * @throws BocException 
     * 
     */
    public boolean setModEstadoPedido(long id_pedido, long id_estado) throws BolException {
        try{
            return pedidoService.setModEstadoPedido(id_pedido, id_estado);
        }catch (Exception ex) { //RemoteException ex
            // Translate the service exception into
            // application exception
            throw new BolException(ex);
        }
    }
    //indra
	/**
     * Modifica el detalle de un pedido cuando se repickea
     * @param  id_pedido long 
     * @param  id_estado long 
     * @return boolean, devuelve true en el caso que la actualización fue satisfactoria, caso contrario devuelve false.
     * @throws BocException 
     * 
     */
    public boolean setModDetallePedido(long id_pedido, double cantidad, long id_producto) throws BolException {
        try{
            return pedidoService.setModDetallePedido(id_pedido, cantidad, id_producto);
        }catch (Exception ex) { //RemoteException ex
            // Translate the service exception into
            // application exception
            throw new BolException(ex);
        }
    }
	/**
	 * Metodo que se encarga de recuperar los productos sustitutos sobre el margen de sustitución 
	 * para ser retirados de los bins
	 * @return <code>List</code> Listado de <code>ProductoSobreMargenDTO</code> con los productos sustitutos sobre umbral
	 * @throws SystemException
	 * @throws BolException
	 */
/*	public List getListadoDetallePedido(long id_pedido) throws SystemException, BolException {
        try {
            return pedidoService.getListadoDetallePedido(id_pedido);
        } catch (ServiceException e) {
            throw new BolException(e);
        }
    }*/
	
	/**
	 * Metodo que se encarga de recuperar los productos sustitutos sobre el margen de sustitución 
	 * para ser retirados de los bins
	 * @return <code>List</code> Listado de <code>ProductoSobreMargenDTO</code> con los productos sustitutos sobre umbral
	 * @throws SystemException
	 * @throws BolException
	 */
	public List getListadoProductosxPedido(long id_pedido) throws SystemException, BolException {
        try {
            return pedidoService.getListadoProductosxPedido(id_pedido);
        } catch (ServiceException e) {
            throw new BolException(e);
        }
    }

    //indra
	  public UmbralDTO getPorcenUmbralById(long id_pedido) throws BolException{
        try{
            return umbralService.getPorcenUmbralById(id_pedido);
        }catch (Exception ex) { //RemoteException ex
            // Translate the service exception into
            // application exception
            throw new BolException(ex);
}
    }
    //indra
	  
//	INDRA 23-11-2012
	public HashMap getDatosCabecera(int validacion, String fechaConsulta, long jornadaActual, long idLocal) throws BolException{
		try{
			return faltantesService.getDatosCabecera(validacion, fechaConsulta, jornadaActual, idLocal);
		}catch(Exception e){
			throw new BolException(e);
}
	} 
	public HashMap getInformeFaltantes(ParametroConsultaFaltantesDTO parametro) throws BolException{
		try{
			return faltantesService.getInformeFaltantes(parametro);
		}catch(Exception e){
			throw new BolException(e);
		}
	}  
//	INDRA 23-11-2012
	
    
	/**
	 * Metodo que consulta si una OP tiene Exceso 
	 * ([total pickiado(BO_DETALLE_PICKING) + costo despacho(BO_PEDIDOS)]  vs [monto reservado(BO_PEDIDOS)])
	 * 
	 * @param id_pedido:id del pedido a consultar
	 * @return boolean true en caso de exceso o false en caso que OP no tenga exceso
	 * @throws BocException
	 */
	public boolean isOpConExceso(long idPedido)  throws BolException{
	    try {
            return pedidoService.isOpConExceso(idPedido);
        } catch (Exception ex) {
            throw new BolException(ex);
        }
	}
	
    /**
     * Metodo que marca o desmarca una op con exceso monto_excedido=(1|0) BO_PEDIDOS 
     * 
     * @param idPedido
     * @param excedido
     * @throws SystemException 
     * @throws ServiceException 
     */
    public void modPedidoExcedido(long idPedido, boolean excedido) throws BolException {
        try {
        	 pedidoService.modPedidoExcedido(idPedido, excedido);
        } catch (Exception ex) {
            throw new BolException( ex);
        } 
    }
    
    /**
	 * @param mail
	 */
	public void addMail(MailDTO mail) throws BolException {
		try {
			clientesService.addMail(mail);
		} catch (Exception ex) {
			throw new BolException(ex);
		}
	}
    
	/**
	 * Metodo que valida si el exceso puede ser corregido automaticamente. 
	 * 
	 * @param id_pedido:id del pedido a consultar
	 * @return boolean true en caso de exceso se puede corregir automaticamente.
	 * @throws BocException
	 */
	public boolean isExcesoCorreccionAutomatico(long idPedido)
			throws BolException {
		try {
			return pedidoService.isExcesoCorreccionAutomatico(idPedido);
		} catch (Exception ex) {
			throw new BolException(ex);
		}
	}

	public boolean corrigeExcesoOP(long idPedido) throws BolException {
		try {
			return pedidoService.corrigeExcesoOP(idPedido);
		} catch (Exception ex) {
			throw new BolException(ex);
		}
	}

	public boolean isActivaCorreccionAutomatica() throws BolException {
		// TODO Apéndice de método generado automáticamente
		try {
			return pedidoService.isActivaCorreccionAutomatica();
		} catch (Exception ex) {
			throw new BolException(ex);
		}

	}

	public List getIdDetalleSolicitadoConExceso(long idPedido) throws BolException {
		// TODO Apéndice de método generado automáticamente
		// TODO Apéndice de método generado automáticamente
		try {
			return pedidoService.getIdDetalleSolicitadoConExceso(idPedido);
		} catch (Exception ex) {
			throw new BolException(ex);
		}
		
	}

	public boolean isOpConExceso(long idPedido, boolean isCorrecionActiva) throws BolException {
		// TODO Apéndice de método generado automáticamente
		  try {
	            return pedidoService.isOpConExceso(idPedido, isCorrecionActiva);
	        } catch (Exception ex) {
	            throw new BolException(ex);
	        }
	}
	public boolean productoEnOPConSistitutosMxN(long idPedido) throws BolException {		
		try {
			return pedidoService.productoEnOPConSistitutosMxN(idPedido);
		} catch (Exception ex) {
			throw new BolException(ex);
		}
	}
	
	public Map getOPConProductosFaltantesEnPromoMxN(long idPedido) throws BolException {		
		try {
			return pedidoService.getOPConProductosFaltantesEnPromoMxN(idPedido);
		} catch (Exception ex) {
			throw new BolException(ex);
		}
	}
    	
    	  
}
