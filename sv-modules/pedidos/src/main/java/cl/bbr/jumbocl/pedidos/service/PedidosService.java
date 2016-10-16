package cl.bbr.jumbocl.pedidos.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.common.dto.RegionDTO;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.HorarioPickingEntity;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.common.model.SemanasEntity;
import cl.bbr.jumbocl.parametros.ctrl.ParametrosCtrl;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.parametros.exceptions.ParametrosException;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPedDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPickDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcGeneraDespMasivoDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcIniciaFormPickingManualDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcInsPedidoDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModFacturaDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModPedidoIndicDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModPedidoPolSustDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModPedidoProdDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModTrxMPDetalleDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcRondasPropuestasDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcSectoresJornadaDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcZonasJornadaDTO;
import cl.bbr.jumbocl.pedidos.ctrl.CalendariosCtrl;
import cl.bbr.jumbocl.pedidos.ctrl.ComunasCtrl;
import cl.bbr.jumbocl.pedidos.ctrl.DespachoCtrl;
import cl.bbr.jumbocl.pedidos.ctrl.ExcesoCtrl;
import cl.bbr.jumbocl.pedidos.ctrl.JornadasCtrl;
import cl.bbr.jumbocl.pedidos.ctrl.LocalCtrl;
import cl.bbr.jumbocl.pedidos.ctrl.PedidosCtrl;
import cl.bbr.jumbocl.pedidos.ctrl.PoligonosCtrl;
import cl.bbr.jumbocl.pedidos.ctrl.RondasCtrl;
import cl.bbr.jumbocl.pedidos.ctrl.SectoresCtrl;
import cl.bbr.jumbocl.pedidos.dao.DAOFactory;
import cl.bbr.jumbocl.pedidos.dao.JdbcJornadasDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO;
import cl.bbr.jumbocl.pedidos.dto.ActDetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.AdmHorarioDespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.AdmHorarioPickingDTO;
import cl.bbr.jumbocl.pedidos.dto.AsignaOPDTO;
import cl.bbr.jumbocl.pedidos.dto.AvanceDTO;
import cl.bbr.jumbocl.pedidos.dto.BinDTO;
import cl.bbr.jumbocl.pedidos.dto.BinFormPickDTO;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.CalendarioDespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.CalendarioPickingDTO;
import cl.bbr.jumbocl.pedidos.dto.CambEnPagoOPDTO;
import cl.bbr.jumbocl.pedidos.dto.ChoferTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.CreaRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.CriterioCarruselDTO;
import cl.bbr.jumbocl.pedidos.dto.CuponPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.DatosMedioPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.DespachoCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.DespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.DetalleFormPickingDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.DireccionMixDTO;
import cl.bbr.jumbocl.pedidos.dto.EmpresaTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.pedidos.dto.FPickDTO;
import cl.bbr.jumbocl.pedidos.dto.FacturasDTO;
import cl.bbr.jumbocl.pedidos.dto.FonoTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.JorDespachoCalDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaCriteria;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.ListaGrupoDTO;
import cl.bbr.jumbocl.pedidos.dto.ListaTipoGrupoDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.LogRutaDTO;
import cl.bbr.jumbocl.pedidos.dto.POSFeedbackProcPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.PagoGrabilityDTO;
import cl.bbr.jumbocl.pedidos.dto.PagoVentaMasivaDTO;
import cl.bbr.jumbocl.pedidos.dto.PatenteTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoExtDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidosCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.PilaProductoDTO;
import cl.bbr.jumbocl.pedidos.dto.PilaUnidadDTO;
import cl.bbr.jumbocl.pedidos.dto.PoligonoDTO;
import cl.bbr.jumbocl.pedidos.dto.PoligonosZonaDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductoCarruselDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductoCbarraDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RechPagoOPDTO;
import cl.bbr.jumbocl.pedidos.dto.RondaDTO;
import cl.bbr.jumbocl.pedidos.dto.RondasCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaCriterioDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.pedidos.dto.SectorLocalDTO;
import cl.bbr.jumbocl.pedidos.dto.SustitutoDTO;
import cl.bbr.jumbocl.pedidos.dto.TrxMpCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.TrxMpDTO;
import cl.bbr.jumbocl.pedidos.dto.UltimasComprasDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.CalendarioDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.CalendarioException;
import cl.bbr.jumbocl.pedidos.exceptions.ComunasException;
import cl.bbr.jumbocl.pedidos.exceptions.DespachosException;
import cl.bbr.jumbocl.pedidos.exceptions.JornadasDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.JornadasException;
import cl.bbr.jumbocl.pedidos.exceptions.LocalException;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosException;
import cl.bbr.jumbocl.pedidos.exceptions.PoligonosException;
import cl.bbr.jumbocl.pedidos.exceptions.RondasException;
import cl.bbr.jumbocl.pedidos.exceptions.SectorPickingDAOException;
import cl.bbr.jumbocl.pedidos.promos.dto.PromoMedioPagoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PromocionDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PromocionesCriteriaDTO;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;
import cl.bbr.promo.lib.dto.ProductoCatalogoExternoDTO;
import cl.bbr.promo.lib.dto.ProductoStockDTO;


public class PedidosService {

	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);	
	
	
	/*
	 * ------------ Pedidos ------------------
	 */

	/**
	 * Obtiene los estados del pedido
	 * 
	 * @return List EstadosDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.EstadoDTO
	 */
	public List getEstadosPedidoBOC()
		throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getEstadosPedidoBOC();
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	/**
	 * Obtiene los estados del pedido
	 * 
	 * @return List EstadosDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.EstadoDTO
	 */
	public EstadoDTO getEstadoPedido(long IdPedido)
		throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getEstadoPedido(IdPedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}


	/**
	 * Obtiene los estados de la TrxMp
	 * 
	 * @return EstadosDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.EstadoDTO
	 */
	public EstadoDTO getEstadoTrxMp(long IdTrxMp)
		throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getEstadoTrxMp(IdTrxMp);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Obtiene los motivos del pedido
	 * 
	 * @return List MotivoDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.MotivoDTO
	 */
	public List getMotivosPedidoBOC()
		throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getMotivosPedidoBOC();
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	/**
	 * Retorna listado de estados de pedido
	 * 
	 * @return List EstadosDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.EstadoDTO
	 */
	public List getEstadosPedido()
		throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getEstadosPedido();
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Retorna listado de estados de jornada
	 * 
	 * @return List EstadosDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.EstadoDTO
	 */
	public List getEstadosJornada() throws ServiceException{
		
		JornadasCtrl jornadas = new JornadasCtrl();
		try {
			return jornadas.getEstadosJornada();
		} catch (JornadasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	public boolean valoresNegativos() {
		JornadasCtrl jornadas = new JornadasCtrl();
		return jornadas.valoresNegativos();
	}
	

	/**
	 * Retorna listado de pedidos segun el criterio
	 * 
	 * @param  criterio PedidosCriteriaDTO 
	 * @return List MonitorPedidoDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.MonitorPedidosDTO
	 */
	public List getPedidosByCriteria(PedidosCriteriaDTO criterio)
		throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getPedidosByCriteria(criterio);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Retorna listado de pedidos segun el criterio
	 * 
	 * @param  criterio PedidosCriteriaDTO 
	 * @return List MonitorPedidoDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.MonitorPedidosDTO
	 */
	public List getPedidosByCriteriaHomologacion(PedidosCriteriaDTO criterio)
		throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getPedidosByCriteriaHomologacion(criterio);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Retorna la cantidad de pedidos, segun el criterio
	 * 
	 * @param  criterio PedidosCriteriaDTO 
	 * @return long
	 * @throws ServiceException 
	 */
	public long getCountPedidosByCriteria(PedidosCriteriaDTO criterio)
		throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getCountPedidosByCriteria(criterio);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Retorna listado de pedidos, segun el criterio de fecha
	 * 
	 * @param  criterio PedidosCriteriaDTO 
	 * @return List MonitorPedidoDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.MonitorPedidosDTO
	 */
	public List getPedidosByFecha(PedidosCriteriaDTO criterio)
		throws ServiceException{
	
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getPedidosByFecha(criterio);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	/**
	 * Retorna la cantidad de pedidos, segun el criterio de fecha
	 * 
	 * @param  criterio PedidosCriteriaDTO 
	 * @return long
	 * @throws ServiceException 
	 */
	public long getCountPedidosByFecha(PedidosCriteriaDTO criterio)
		throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getCountPedidosByFecha(criterio);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	
	
	/**
	 * Retorna los datos del pedido
	 * 
	 * @param  id_pedido long 
	 * @return PedidoDTO
	 * @throws ServiceException 
	 * @throws SystemException
	 */
	public PedidoDTO getPedido(long id_pedido) throws ServiceException, SystemException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getPedidoById(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		
	
	}
	
	/**
	 * Retorna listado de alertas del pedido
	 * 
	 * @param  id_pedido long 
	 * @return List AlertaDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.AlertaDTO
	 */
	public List getAlertasPedido(long id_pedido)
		throws ServiceException{
	
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getAlertasPedido(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Retorna listado de productos del pedido
	 * 
	 * @param  id_pedido long 
	 * @return List ProductosPedidoDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO
	 */
	public List getProductosPedido(long id_pedido)
		throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getProductosPedido(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	/**
	 * Retorna el id del sector, segun el <b>id_prod</b> código del producto y el <b>id_local</b> del local.
	 * 
	 * @param  id_prod long 
	 * @return long, id del sector
	 * @throws ServiceException 
	 */
	public long getSectorByProdId(long id_prod)
		throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getSectorByProdId(id_prod);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	/**
	 * Permite agregar el producto en el pedido, segun la información del detalle a agregar en el pedido.<br>
	 * Se utiliza desde Jumbo BOC, en la vista del detalle del pedido.
	 * 
	 * @param  prod ProductosPedidoDTO 
	 * @return boolean, devuelve <i>true</i> si no hay error, caso contrario, devuelve <i>false</i>.
	 * @throws ServiceException 
	 */
	public boolean agregaProductoPedido(ProductosPedidoDTO prod)
		throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.agregaProductoPedido(prod);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Obtiene lista de bins para un pedido
	 * 
	 * @param  id_pedido long 
	 * @return List BinDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.BinDTO
	 */
	public List getBinsPedido(long id_pedido)
		throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getBinsPedido(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	
	/**
	 * Obtiene lista de bins para un pedido
	 * 
	 * @param  id_pedido long 
	 * @return List BinDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.BinDTO
	 */
	public List getBinsPedidoPKL(long id_pedido)
		throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getBinsPedidoPKL(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	

	/**
	 * Obtiene lista de logs de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return List LogPedidoDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO
	 */
	public List getLogPedido(long id_pedido)
		throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getLogPedido(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}

	}	

	/**
	 * Obtiene el listado de sustitutos de un pedido, segun el <b>id</b> del pedido.
	 * 
	 * @param  id_pedido long 
	 * @return List SustitutoDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.SustitutoDTO
	 */
	public List getSustitutosByPedidoId(long id_pedido) 
		throws ServiceException{
			PedidosCtrl pedidos = new PedidosCtrl();
			try {
			return pedidos.getSustitutosByPedidoId(id_pedido);
			}catch (PedidosException e) {
				e.printStackTrace();
				throw new ServiceException(e);
			}
	}

	/**
	 * Obtiene el listado de faltantes de un pedido, segun el <b>id</b> del pedido.
	 * 
	 * @param  id_pedido long 
	 * @return List FaltanteDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.FaltanteDTO
	 */
	public List getFaltantesByPedidoId(long id_pedido)
	throws ServiceException{
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
		return pedidos.getFaltantesByPedidoId(id_pedido);
		}catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		
	}		

	/**
	 * Agrega registro al log del pedido
	 * 
	 * @param  log LogPedidoDTO 
	 * @throws ServiceException
	 * @throws SystemException 
	 */
	public void addLogPedido(LogPedidoDTO log)
		throws ServiceException, SystemException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			pedidos.addLogPedido(log);
		} catch (PedidosException ex) {
			logger.debug("Problemas con controles de pedidos");
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
    public boolean getRondaConFaltantes(long id_ronda)
        throws ServiceException, SystemException{
	
        RondasCtrl rondas = new RondasCtrl();
        try {
            return rondas.getRondaConFaltantes(id_ronda);
        }catch(RondasException ex) {
            logger.debug("Problemas con controles de rondas");
            throw new ServiceException(ex.getMessage());
        }
    }
	
	
    public boolean setEstadoVerDetalle(long id_ronda)
        throws ServiceException, SystemException{
	
        RondasCtrl rondas = new RondasCtrl();
        try {
            return rondas.setEstadoVerDetalle(id_ronda);
        }catch(RondasException ex) {
            logger.debug("Problemas con controles de rondas");
            throw new ServiceException(ex.getMessage());
        }
    }
	
	
    public boolean setEstadoImpEtiqueta(long id_ronda)
        throws ServiceException, SystemException{
	
        RondasCtrl rondas = new RondasCtrl();
        try {
            return rondas.setEstadoImpEtiqueta(id_ronda);
        }catch(RondasException ex) {
            logger.debug("Problemas con controles de rondas");
            throw new ServiceException(ex.getMessage());
        }
    }
	
	
    public boolean setFechaImpListadoPKL(long id_ronda)
        throws ServiceException, SystemException{
	
        RondasCtrl rondas = new RondasCtrl();
        try {
            return rondas.setFechaImpListadoPKL(id_ronda);
        }catch(RondasException ex) {
            logger.debug("Problemas con controles de rondas");
            throw new ServiceException(ex.getMessage());
        }
    }
	
    public boolean setFechaIniciaJornadaPKL(long id_ronda)
        throws ServiceException, SystemException{
	
        RondasCtrl rondas = new RondasCtrl();
        try {
            return rondas.setFechaIniciaJornadaPKL(id_ronda);
        }catch(RondasException ex) {
            logger.debug("Problemas con controles de rondas");
            throw new ServiceException(ex.getMessage());
        }
    }
    
	
    public boolean ExisteFechaIniciaJornadaPKL(long id_ronda)
        throws ServiceException, SystemException{
	
        RondasCtrl rondas = new RondasCtrl();
        try {
            return rondas.ExisteFechaIniciaJornadaPKL(id_ronda);
        }catch(RondasException ex) {
            logger.debug("Problemas con controles de rondas");
            throw new ServiceException(ex.getMessage());
        }
    }
    

    /**
	 * Retorna listado de productos de un bin
	 * 
	 * @param  id_bp long 
	 * @throws ServiceException 
	 */
	public List getProductosBin(long id_bp)
		throws ServiceException {
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getProductosBin(id_bp);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}	
	
	/**
	 * Retorna si el pedido fue asignado o no.
	 * 
	 * @param  col AsignaOPDTO 
	 * @return boolean, devuelve <i>true</i> en el caso que la asignación fue satisfactoria, caso contrario devuelve <i>false</i>.
	 * @throws ServiceException 
	 * @throws SystemException
	 * 
	 */
	public boolean setAsignaOP(AsignaOPDTO col) throws ServiceException, SystemException {
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.setAsignaOP(col);
		} catch (PedidosException e) {
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	/**
	 * Retorna si el pedido fue liberado o no.
	 * 
	 * @param  col AsignaOPDTO 
	 * @return boolean, devuelve <i>true</i> en el caso que la liberación fue satisfactoria, caso contrario devuelve <i>false</i>.
	 * @throws ServiceException 
	 * @throws SystemException
	 * 
	 */
	public boolean setLiberaOP(AsignaOPDTO col)
		throws ServiceException, SystemException {
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.setLiberaOP(col);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	/**
	 * Actualiza el medio de pago del pedido.
	 * 
	 * @param  mp DatosMedioPagoDTO 
	 * 
	 * @throws ServiceException 
	 * @throws SystemException
	 * 
	 */
	public void setCambiarMedio_pago(DatosMedioPagoDTO mp)
		throws ServiceException, SystemException {
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			 pedidos.setCambiarMedio_pago(mp);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Realiza el proceso de validación de la OP
	 * 
	 * @param  id_pedido long 
	 * @return boolean, devuelve <i>true</i> en el caso que la validación fue satisfactoria, caso contrario devuelve <i>false</i>.
	 * @throws ServiceException 
	 * @throws SystemException
	 * 
	 */
	public boolean setValidaOP(long id_pedido)	throws ServiceException, SystemException {
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.setValidaOP(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Elimina las alertas relacionadas al pedido.
	 * 
	 * @param  id_pedido long 
	 * @return boolean, devuelve <i>true</i> en el caso que la eliminación fue satisfactoria, caso contrario devuelve <i>false</i>.
	 * @throws ServiceException 
	 * @throws SystemException
	 * 
	 */
	public boolean elimAlertaByPedido(long id_pedido)	throws ServiceException, SystemException {
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.elimAlertaByPedido(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	/**
	 * Agrega una alerta a un pedido.
	 * 
	 * @param  id_pedido long 
	 * @param  id_alerta long 
	 * @return boolean, devuelve <i>true</i> en el caso que la eliminación fue satisfactoria, caso contrario devuelve <i>false</i>.
	 * @throws ServiceException 
	 * 
	 */
	public boolean addAlertToPedido(long id_pedido, long id_alerta)	throws ServiceException {
	
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.addAlertToPedido(id_pedido, id_alerta);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	/**
	 * Modifica el estado de un pedido.
	 * 
	 * @param  id_pedido long 
	 * @param  id_estado long 
	 * @return boolean, devuelve <i>true</i> en el caso que la actualización fue satisfactoria, caso contrario devuelve <i>false</i>.
	 * @throws ServiceException 
	 * 
	 */
	public boolean setModEstadoPedido(long id_pedido, long id_estado)	throws ServiceException, SystemException {
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.setModEstadoPedido(id_pedido, id_estado);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
/*	/**(+) INDRA (+)
	 * Modifica el detalle de un pedido cuando se repickea
	 * 
	 * @param  id_pedido long 
	 * @param  id_estado long 
	 * @return boolean, devuelve <i>true</i> en el caso que la actualización fue satisfactoria, caso contrario devuelve <i>false</i>.
	 * @throws ServiceException 
	 * 
	 */
/*	public boolean getListadoDetallePedido(long id_pedido)	throws ServiceException, SystemException {

		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getListadoDetallePedido(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}*/
	/**
	 * Modifica el detalle de un pedido cuando se repickea
	 * 
	 * @param  id_pedido long 
	 * @param  id_estado long 
	 * @return boolean, devuelve <i>true</i> en el caso que la actualización fue satisfactoria, caso contrario devuelve <i>false</i>.
	 * @throws ServiceException 
	 * 
	 */
	public boolean setModDetallePedido(long id_pedido,double cantidad, long id_producto)	throws ServiceException, SystemException {
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.setModDetallePedido(id_pedido, cantidad, id_producto);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	//
	/**
	 * Entrega el listado de transacciones de los pedidos
	 * segun local de facturación. 
	 * Este listado se usa para el monitor de transacciones de pago.
	 * 
	 * @param  TrxMpCriteriaDTO criterios
	 * @return List MonitorTrxMpDTO
	 * @throws ServiceException
	 * @throws SystemException
	 * @see    cl.bbr.jumbocl.pedidos.dto.MonitorTrxMpDTO
	 */
	public List getListadoProductosxPedido(long id_pedido)
	throws ServiceException, SystemException{

		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getListadoProductosxPedido(id_pedido);
		} catch (PedidosException e) {
			//e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
//(-) INDRA (-)
	/**
	 * Modifica el estado de una TrxMp.
	 * 
	 * @param  id_trx_mp long 
	 * @param  id_estado long 
	 * @return boolean, devuelve <i>true</i> en el caso que la actualización fue satisfactoria, caso contrario devuelve <i>false</i>.
	 * @throws ServiceException 
	 * 
	 */
	public boolean setModEstadoTrxMp(long id_trx_mp, long id_estado)	throws ServiceException, SystemException {
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.setModEstadoTrxMp(id_trx_mp, id_estado);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Retorna si tiene alerta activa.
	 * 
	 * @param  id_pedido long 
	 * @return boolean, devuelve <i>true</i> en el caso que la actualización fue satisfactoria, caso contrario devuelve <i>false</i>.
	 * @throws ServiceException 
	 * 
	 */
	public boolean getExisteAlertaActiva(long id_pedido)throws ServiceException {
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getExisteAlertaActiva(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Retorna si el pedido fue anulado.
	 * 
	 * @param  id_pedido long 
	 * @return boolean, devuelve <i>true</i> en el caso que el pedido fue anulado, caso contrario devuelve <i>false</i>.
	 * @throws ServiceException 
	 * 
	 */
	public boolean setAnularOP(long id_pedido) throws ServiceException, SystemException {
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.setAnularOP(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Retorna si el pedido existe o no.
	 * 
	 * @param  id_pedido long 
	 * @return boolean, devuelve <i>true</i> en el caso que el pedido existe, caso contrario devuelve <i>false</i>.
	 * @throws ServiceException 
	 * @throws SystemException 
	 */
	public boolean isPedidoById(long id_pedido) throws ServiceException, SystemException {
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.isPedidoById(id_pedido); 
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Procesa Feedback de la transacción de pago
	 * 
	 * @param  fback POSFeedbackProcPagoDTO 
	 * 
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void doProcesaFeedbackPOS(POSFeedbackProcPagoDTO fback)
		throws ServiceException, SystemException {
		
		PedidosCtrl ctrl = new PedidosCtrl();
		try {
			ctrl.doProcesaFeedbackPOS(fback);
		} catch (PedidosException e) {
			throw new ServiceException(e.getMessage());
		} catch (SystemException e) {
			throw new SystemException(e);
		} 
		
	}
	
	/**
	 * Entrega el listado de transacciones de los pedidos
	 * segun local de facturación. 
	 * Este listado se usa para el monitor de transacciones de pago.
	 * 
	 * @param  TrxMpCriteriaDTO criterios
	 * @return List MonitorTrxMpDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.MonitorTrxMpDTO
	 */
	public List getTrxMpByCriteria(TrxMpCriteriaDTO criterios)
	throws ServiceException{

		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getTrxMpByCriteria(criterios);
		} catch (PedidosException e) {
			//e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene el total de registros de transacciones de pago 
	 * segun local facturador. En base a criterios 
	 * de busqueda y paginacion
	 * 
	 * @param  TrxMpCriteriaDTO criterios
	 * @return List MonitorTrxMpDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.MonitorTrxMpDTO
	 */
	public long getCountTrxMpByCriteria(TrxMpCriteriaDTO criterio)
	throws ServiceException{

		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getCountTrxMpByCriteria(criterio);
		} catch (PedidosException e) {
			//e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Entrega el listado de trx de pago de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return List MonitorTrxMpDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.MonitorTrxMpDTO
	 */
	public List getTrxMpByIdPedido(long id_pedido)
	throws ServiceException{

		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getTrxMpByIdPedido(id_pedido);
		} catch (PedidosException e) {
			//e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Genera las trx de pago para un  pedido en base a
	 * una ponderacion de los productos que lo componen.
	 * 
	 * @param  id_pedido long 
	 * 
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void doGeneraTrxMp(long id_pedido)
		throws ServiceException, SystemException {
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			 pedidos.doGeneraTrxMp(id_pedido);
		} catch (PedidosException e) {
			//e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	public double getMontoTotalDetPedidoByIdPedido(long id_pedido)
        throws ServiceException, SystemException {
	
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getMontoTotalDetPedidoByIdPedido(id_pedido);
        } catch (PedidosException e) {
            //e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

	
	public double getMontoTotalDetPickingByIdPedido(long id_pedido)
        throws ServiceException, SystemException {

        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getMontoTotalDetPickingByIdPedido(id_pedido);
        } catch (PedidosException e) {
            //e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }
	
	
	public long getMontoTotalTrxByIdPedido(long id_pedido)
        throws ServiceException, SystemException {

        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getMontoTotalTrxByIdPedido(id_pedido);
        } catch (PedidosException e) {
            //e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }
	

	
	/**
	 * Genera los cupones de pago en base a las 
	 * trx de pago de un pedido.
	 * 
	 * @param  id_pedido long 
	 * @param  id_trxmp long 
	 * @return CuponPagoDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public CuponPagoDTO getCuponPago(long id_pedido, long id_trxmp)
	throws ServiceException, SystemException {
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			 return pedidos.getCuponPago(id_pedido, id_trxmp);
		} catch (PedidosException e) {
			//e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	public HashMap getFacturasIngresadas(long id_pedido)
	throws ServiceException, SystemException {
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			 return pedidos.getFacturasIngresadas(id_pedido);
		} catch (PedidosException e) {
			//e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
		
	
	public int getCantFacturasIngresadas(long id_pedido)
	throws ServiceException, SystemException {
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			 return pedidos.getCantFacturasIngresadas(id_pedido);
		} catch (PedidosException e) {
			//e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
		
	
	public boolean ActListFacturas(FacturasDTO fact)
	throws ServiceException, SystemException {
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			 return pedidos.ActListFacturas(fact);
		} catch (PedidosException e) {
			//e.printStackTrace();
			throw new ServiceException(e.getMessage(), e);
		}
	}
		
	/**
	 * Entrega los datos de un encabezado de trx de pago
	 * 
	 * @param  id_trxmp long 
	 * @return CuponPagoDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public TrxMpDTO getTrxPagoById( long id_trxmp )
		throws ServiceException, SystemException {
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			 return pedidos.getTrxPagoById(id_trxmp);
		} catch (PedidosException e) {
			//e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Entrega el listado de detalle de una trx de pago
	 * 
	 * @param  id_trxmp long 
	 * @return List TrxMpDetalleDTO
	 * @throws ServiceException
	 * @throws SystemException
	 * @see    cl.bbr.jumbocl.pedidos.dto.TrxMpDetalleDTO
	 */
	public List getTrxPagoDetalleByIdTrxMp( long id_trxmp )	throws ServiceException, SystemException {
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			 return pedidos.getTrxPagoDetalleByIdTrxMp(id_trxmp);
		} catch (PedidosException e) {
			//e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}	
	
	/**
	 * Entrega el listado de detalle de una trx de pago
	 * 
	 * @param  trx ProcModTrxMPDetalleDTO 
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 * @see    cl.bbr.jumbocl.pedidos.entity.collaboration.TrxMpDetalleDTO
	 */
	public boolean setModTrxPagoDet( ProcModTrxMPDetalleDTO trx )
	throws ServiceException, SystemException {
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			 return pedidos.setModTrxPagoDet(trx);
		} catch (PedidosException e) {
			//e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}	

	/**
	 * Transcción que inserta pedido. Es llamada desde el FO.
	 * Se agregan parametros para aplicar cupon de descuento (cddto, cuponProds) cdd
	 * @param pedido
	 * @param cddto
	 * @param cuponProds
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public long doInsPedido(ProcInsPedidoDTO pedido, CuponDsctoDTO cddto, List cuponProds) throws ServiceException, SystemException {
	    
		long response;

		PedidosCtrl pedidos = new PedidosCtrl();
		try {
		    logger.info("Se inicia doInsPedido ");
			response = pedidos.doInsPedido(pedido, cddto, cuponProds);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		logger.info("PedidosService.doInsPedido --> OP generada: "+ response);
		return response;
	}


    /**
	 * Retorna el último pedido cursado por el cliente, en cualquier estado
	 * 
	 * @param  id_cliente long 
	 * @return PedidoDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public PedidoDTO getUltimoIdPedidoCliente(long id_cliente)
		throws ServiceException, SystemException {
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			 return pedidos.getUltimoIdPedidoCliente(id_cliente);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Obtiene la lista de productos de un pedido, segun el id_pedido y el id_sector
	 * 
	 * @param  id_pedido long 
	 * @param  id_sector long 
	 * @param  id_local long 
	 * @return List ProductosPedidoDTO
	 * @throws ServiceException
	 * @throws SystemException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO
	 */
	public List getProdPedidoXSector(long id_pedido, long id_sector, long id_local) throws ServiceException, SystemException{
	
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getProdPedidoXSector(id_pedido, id_sector, id_local);
		} catch (PedidosException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * Obtiene la lista de productos de un pedido, segun el id_pedido y el id_sector
	 * 
	 * @param  id_pedido long 
	 * @param  id_sector long 
	 * @param  id_local long 
	 * @return List ProductosPedidoDTO
	 * @throws ServiceException
	 * @throws SystemException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO
	 */
	public List getProdSinPickearXPedidoXSector(long id_pedido, long id_sector, long id_local) throws ServiceException, SystemException{
	
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getProdSinPickearXPedidoXSector(id_pedido, id_sector, id_local);
		} catch (PedidosException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	
	/**
	 * Obtiene la lista de productos sueltos de un pedido, segun el id_pedido 
	 * 
	 * @param  id_pedido long 
	 * @return List ProductosPedidoDTO
	 * @throws SystemException 
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO
	 */
	public List getProductosSueltosByPedidoId(long id_pedido) throws ServiceException, SystemException{
	
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getProductosSueltosByPedidoId(id_pedido);
		} catch (PedidosException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	
	/**
	 * Actualizar detalle del pedido, en caso que el detalle tiene sustituto y no tiene 
	 * informacion completa 
	 * 
	 * @param  prod DetallePickingDTO 
	 * @return boolean 
	 * @throws SystemException 
	 * @throws ServiceException
	 */
	public boolean setDetallePickingSustituto(DetallePickingDTO prod) throws ServiceException, SystemException{
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.setDetallePickingSustituto(prod);
		} catch (PedidosException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Obtener detalle picking del pedido
	 * 
	 * @param  id_dpicking long 
	 * @return DetallePickingDTO 
	 * @throws ServiceException 
	 * @throws SystemException 
	 */
	public DetallePickingDTO getDetallePickingById(long id_dpicking) throws ServiceException, SystemException{
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getDetallePickingById(id_dpicking);
		} catch (PedidosException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}
		
	/**
	 * Obtiene el monto total de la hoja de despacho, segun el id_pedido
	 * 
	 * @param  id_pedido
	 * @return monto total, double
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public double getMontoTotalHojaDespachoByIdPedido(long id_pedido) throws ServiceException, SystemException{
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getMontoTotalHojaDespachoByIdPedido(id_pedido);
		} catch (PedidosException e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Genera el pedido, a partir de seleccionar productos en una cotizacion
	 * 
	 * @param pedido
	 * @return id del nuevo pedido
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public long doGeneraPedido(ProcInsPedidoDTO pedido) throws ServiceException, SystemException {
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			 return pedidos.doGeneraPedido(pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	
	
	/*
	 * ------------ Jornadas ------------------
	 */
	
	/**
	 * Obtiene listado de jornadas por fecha
	 * 
	 * @param  fecha Date 
	 * @param  id_local long 
	 * @return List MonitorJornadasDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.MonitorJornadasDTO
	 */
	public List getJornadasPickingByFecha(Date fecha, long id_local)
		throws ServiceException{
		
		JornadasCtrl jornadas = new JornadasCtrl();
		try {
			return jornadas.getJornadasPickingByFecha(fecha, id_local);
		} catch (JornadasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Obtiene jornadas de acuerdo a criterios de consulta
	 * 
	 * @param  criterio JornadaCriteria 
	 * @param  id_local long 
	 * @return List MonitorJornadasDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.MonitorJornadasDTO
	 */	
	public List getJornadasPickingByCriteria(JornadaCriteria criterio, long id_local)
		throws ServiceException{
		
		JornadasCtrl jornadas = new JornadasCtrl();
		try {
			return jornadas.getJornadasPickingByCriteria(criterio, id_local);
		} catch (JornadasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	//Maxbell arreglo homologacion
	public List getJornadasPickingByCriteriaEspeciales(JornadaCriteria criterio, long id_local)
			throws ServiceException{
			
			JornadasCtrl jornadas = new JornadasCtrl();
			try {
				return jornadas.getJornadasPickingByCriteriaEspeciales(criterio, id_local);
			} catch (JornadasException e) {
				e.printStackTrace();
				throw new ServiceException(e);
			}
		}
	
	/**(+) INDRA (+)
	 * Obtiene jornadas de acuerdo a si tienen pedidos en estado revision faltante
	 * 
	 * @param  criterio JornadaCriteria 
	 * @param  id_local long 
	 * @return List MonitorJornadasDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.MonitorJornadasDTO
	 */	
	public boolean existeJornadaRevFaltante( long id_jornada)
		throws ServiceException{
	
		JornadasCtrl jornadas = new JornadasCtrl();
		try {
			return jornadas.existeJornadaRevFaltante( id_jornada);
		} catch (JornadasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	/**(-) INDRA (-)
	 * Obtiene datos de una jornada
	 * 
	 * @param  id_jornada long 
	 * @return JornadaDTO
	 * @throws ServiceException 
	 */
	public JornadaDTO getJornadaById(long id_jornada)
		throws ServiceException{
		
		JornadasCtrl jornadas = new JornadasCtrl();
		try {
			return jornadas.getJornadaById(id_jornada);
		} catch (JornadasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}	
	
	/**
	 * Obtiene los pedidos por zona de una jornada dada
	 * @param datos
	 * @return List  TotProdZonJorDTO
	 * @throws ServiceException
	 */
	public List getTotalProductosZonasJornada(ProcZonasJornadaDTO datos)
    throws ServiceException{
	JornadasCtrl jornadas = new JornadasCtrl();
	try {
		return jornadas.getTotalProductosZonasJornada(datos);
	} catch (JornadasException e) {
		e.printStackTrace();
		throw new ServiceException(e);			
	}	
}	
	/**
	 * Obtiene la cantidad de productos por sección que deben ser
	 * pickeados en una jornada de picking
	 * 
	 * @param  id_jornada long 
	 * @return List TotProdSecJorDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.TotProdSecJorDTO
	 */
	public List getTotalProductosSectorJornada(ProcSectoresJornadaDTO datos)
	    throws ServiceException{
		JornadasCtrl jornadas = new JornadasCtrl();
		try {
			return jornadas.getTotalProductosSectorJornada(datos);
		} catch (JornadasException e) {
			e.printStackTrace();
			throw new ServiceException(e);			
		}	
	}	
	
	/**
	 * Actualiza la cantidad sin pickear de detalles de pedidos asociados a una jornada picking
	 * 
	 * @param  id_jornada long 
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setPedidosByIdJornada(long id_jornada)  throws ServiceException, SystemException{
		JornadasCtrl jornadas = new JornadasCtrl();
		try {
			return jornadas.setPedidosByIdJornada(id_jornada);
		} catch (JornadasException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());			
		}	
	}
	
	/**
	 * Obtiene la lista de comanda de preparados.
	 * 
	 * @param  id_jornada long 
	 * @param  id_sector String 
	 * @return List ComandaPreparadosDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.ComandaPreparadosDTO
	 */
	public List getComandaPreparados(long id_jornada, String id_sector) 
	    throws ServiceException{
		JornadasCtrl jornadas = new JornadasCtrl();
		try {
			return jornadas.getComandaPreparados(id_jornada, id_sector);
		} catch (JornadasException e) {
			e.printStackTrace();
			throw new ServiceException(e);			
		}	
	}
	
	/**
	 * Obtiene el log de eventos de una jornada
	 * 
	 * @param  id_jornada long identificador de jornada
	 * @return List de LogSimpleDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.LogSimpleDTO
	 */
	public List getLogJornada(long id_jornada)
    	throws ServiceException{
		JornadasCtrl jornadas = new JornadasCtrl();
		try {
		return jornadas.getLogJornada(id_jornada);
		}catch (JornadasException e) {
			e.printStackTrace();
			throw new ServiceException(e);			
		}	
		
	}
	
	/**
	 * Agrega un evento a una jornada
	 * 
	 * @param  id_jornada long identificador de jornada
	 * @param  user String , login del usuario
	 * @param  descr String , descripción
	 * 
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void addLogJornadaPick(long id_jornada, String user, String descr) 
	throws ServiceException, SystemException {
		JornadasCtrl jornadas = new JornadasCtrl();
	try {
		jornadas.doAddLogJornadaPick(id_jornada, user, descr);
		} catch (JornadasException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	public void addLogJornadaDesp(long id_jornada, String user, String descr) 
			throws ServiceException, SystemException {
				JornadasCtrl jornadas = new JornadasCtrl();
			try {
				jornadas.doAddLogJornadaDesp(id_jornada, user, descr);
				} catch (JornadasException e) {
					e.printStackTrace();
					throw new ServiceException(e.getMessage());
				}
			}
	
	/**
	 * Inicia jornada de picking (cambia su estado a Jornada Iniciada)
	 * 
	 * @param  id_jpicking long 
	 * 
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void setIniciaJornada(long id_jpicking)
		throws ServiceException, SystemException {
		JornadasCtrl jornadas = new JornadasCtrl();
		try {
			jornadas.doIniciaJornada(id_jpicking);
			} catch (JornadasException e) {
				e.printStackTrace();
				throw new ServiceException(e.getMessage());
			}
		
	}

	/**
	 * Obtiene la cantidad de productos asignados en una ronda, segun id_jornada
	 * 
	 * @param  id_jornada long 
	 * 
	 * @throws ServiceException
	 * @throws SystemException
	 * */
	public double getCountProdEnRondaByIdJornada(long id_jornada)
		throws ServiceException, SystemException {
		
		JornadasCtrl jornadas = new JornadasCtrl();
		try {
			return jornadas.getCountProdEnRondaByIdJornada(id_jornada);
		} catch (JornadasException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/*
	 * ------------- Calendarios Picking ------------------
	 */
	
	/**
	 * Retorna una semana del calendario a partir de su id
	 * 
	 * @param  id_semana long 
	 * @return SemanasEntity
	 * @throws ServiceException
	 */
	public SemanasEntity getSemanaById(long id_semana)
		throws ServiceException {
		CalendariosCtrl ctrl = new CalendariosCtrl();
		try {
			return ctrl.getSemanaById(id_semana);
		} catch (CalendarioException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	
	
	
	/**
	 * Obtiene el Calendario semanal de Jornadas de Picking
	 *  
	 * @param  n_semana int  numero de la semana en el año 
	 * @param  ano int año
	 * @param  id_local long 
	 * @return CalendarioPickingDTO
	 * @throws ServiceException 
	 * @throws SystemException 
	 */	
	public CalendarioPickingDTO getCalendarioJornadasPicking(int n_semana, int ano, long id_local)
		throws ServiceException, SystemException {
		CalendariosCtrl ctrl = new CalendariosCtrl();
		try {
			return ctrl.getCalendarioPicking(n_semana,ano,id_local);
		} catch (CalendarioException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Obtiene horario de picking a partir de su id
	 *  
	 * @param  id_hor_pick long 
	 * @return HorarioPickingEntity
	 * @throws ServiceException 
	 */
	public HorarioPickingEntity getHorarioPicking(long id_hor_pick)
		throws ServiceException {
		CalendariosCtrl ctrl = new CalendariosCtrl();
		try {
			return ctrl.getHorarioPicking(id_hor_pick);
		} catch (CalendarioException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	

	/**
	 * Obtiene listado de jornadas de picking para un horario
	 *  
	 * @param  id_hor_pick long 
	 * @return List JornadaPickingEntity
	 * @throws ServiceException 
	 * 
	 */
	public List getJornadasByIdHorario(long id_hor_pick)	
		throws ServiceException {
		CalendariosCtrl ctrl = new CalendariosCtrl();
		try {
			return ctrl.getJornadasByIdHorario(id_hor_pick);
		} catch (CalendarioException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	
	
	/**
	 * Inserta un horario con sus respectivas jornadas en el calendario de picking
	 *  para una semana determinada y un local determinado
	 *  
	 * @param  id_semana long 
	 * @param  id_local long 
	 * @param  hpicking AdmHorarioPickingDTO 
	 * @return id_horario insertado
	 * @throws SystemException 
	 * @throws ServiceException 
	 */
	public long InsJornadasPickingyHorario(long id_semana, long id_local, AdmHorarioPickingDTO hpicking)
	throws ServiceException, SystemException {
		CalendariosCtrl ctrl = new CalendariosCtrl();
		try {
			return ctrl.doInsJornadasPickingyHorario(id_semana, id_local, hpicking);
		} catch (CalendarioException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	

	/**
	 * Modifica jornada de picking
	 * 
	 * @param  id_hor_pick long 
	 * @param  hpicking AdmHorarioPickingDTO 
	 * 
	 * @throws ServiceException
	 * @throws SystemException 
	 */
	public void ModJornadaPicking(long id_hor_pick, AdmHorarioPickingDTO hpicking)
		throws ServiceException, SystemException {
			CalendariosCtrl ctrl = new CalendariosCtrl();
			try {
				ctrl.doModJornadaPicking(id_hor_pick, hpicking);
			} catch (CalendarioException e) {
				//e.printStackTrace();
				throw new ServiceException(e.getMessage());
			}
	}
	
	
	/**
	 * Elimina horario de picking de una semana junto con sus jornadas relacionadas 
	 * 
	 * @param  id_hor_pick long 
	 * 
	 * @throws SystemException 
	 * @throws ServiceException
	 */
	public void DelJornadasPickingyHorario(long id_hor_pick)
		throws ServiceException, SystemException {
		CalendariosCtrl ctrl = new CalendariosCtrl();
		try {
			ctrl.doDelJornadasPickingyHorario(id_hor_pick);
		} catch (CalendarioException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	/*
	 * ------------- Calendarios Despacho ------------------
	 */

	/**
	 * Obtiene calendario de despacho
	 * 
	 * @param  n_semana int  número de semana del año
	 * @param  ano int  año
	 * @param  id_zona long 
	 * @return CalendarioDespachoDTO
	 * @throws ServiceException
	 */
	public CalendarioDespachoDTO getCalendarioDespacho(int n_semana, int ano, long id_zona)
		throws ServiceException {
		CalendariosCtrl ctrl = new CalendariosCtrl();
		try {
			return ctrl.getCalendarioDespacho(n_semana,ano,id_zona);
		} catch (CalendarioException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Obtiene información de un horario de despacho
	 * 
	 * @param  id_hor_desp long 
	 * @return HorarioDespachoEntity
	 * @throws ServiceException
	 */
	public HorarioDespachoEntity getHorarioDespacho(long id_hor_desp)
		throws ServiceException {
		CalendariosCtrl ctrl = new CalendariosCtrl();
		try {
			return ctrl.getHorarioDespacho(id_hor_desp);
		} catch (CalendarioException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Obtiene listado de jornadas de despacho
	 * 
	 * @param  id_hor_desp long 
	 * @return List JornadaDespachoEntity
	 * @throws ServiceException
	 * 
	 */
	public List getJornadasDespachoByIdHorario(long id_hor_desp)
		throws ServiceException {
		CalendariosCtrl ctrl = new CalendariosCtrl();
		try {
			return ctrl.getJornadasDespachoByIdHorario(id_hor_desp);
		} catch (CalendarioException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Inserta horario con jornadas de despacho relacionadas
	 * 
	 * @param  id_semana long 
	 * @param  id_zona long 
	 * @param  hdespacho AdmHorarioDespachoDTO 
	 * @return long id_horario
	 * @throws SystemException 
	 * @throws ServiceException
	 */
	public long InsJornadasDespachoyHorario(long id_semana, long id_zona, AdmHorarioDespachoDTO hdespacho)
		throws ServiceException, SystemException {
		CalendariosCtrl ctrl = new CalendariosCtrl();
		try {
			return ctrl.doInsJornadasDespachoyHorario(id_semana, id_zona, hdespacho);
		} catch (CalendarioException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * Elimina jornadas de despacho de un horario
	 * 
	 * @param  id_hor_desp long 
	 * 
	 * @throws SystemException 
	 * @throws ServiceException
	 */
	public void DelJornadasDespachoyHorario(long id_hor_desp)
		throws ServiceException, SystemException {
		
		CalendariosCtrl ctrl = new CalendariosCtrl();
		try{
			ctrl.doDelJornadasDespachoyHorario(id_hor_desp);
		} catch (CalendarioException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * Modifica las jornadas de despacho de un horario
	 * 
	 * @param  id_hor_desp long 
	 * @param  hdespacho AdmHorarioDespachoDTO 
	 * @throws SystemException 
	 * @throws ServiceException
	 */
	public void ModJornadaDespacho(long id_hor_desp, AdmHorarioDespachoDTO hdespacho, int id_zona )
		throws ServiceException, SystemException {
		
		CalendariosCtrl ctrl = new CalendariosCtrl();
		try {
			ctrl.doModJornadaDespacho(id_hor_desp, hdespacho, id_zona);
		} catch (CalendarioException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * Verifica si existe capacidad de despacho (y de picking) en una jornada
	 * 
	 * @param  cant_prods long cantidad de productos
	 * @param  id_jdespacho long jornada de despacho que se pretende verificar capacidad
	 * @return true si existe capacidad de picking y despacho; false en caso contrario
	 * @throws SystemException 
	 * @throws ServiceException
	 */
	public boolean doVerificaCapacidadDespacho(long cant_prods, long id_jdespacho)
		throws ServiceException, SystemException {
		
		CalendariosCtrl ctrl = new CalendariosCtrl();
		try {
			return ctrl.doVerificaCapacidadDespacho(cant_prods,id_jdespacho);
		} catch (DespachosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		} catch (SystemException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}

	}
	
	/*
	 * ------------- Rondas ------------------
	 */
	
	
	/**
	 * Agrega registro al log de la ronda
	 * 
	 * @param  id_ronda long 
	 * @param  usuario String 
	 * @param  log String 
	 * 
	 * @throws SystemException 
	 * @throws ServiceException
	 */
	public void addLogRonda(long id_ronda, String usuario, String log)
		throws ServiceException, SystemException{
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			rondas.doAddLogRonda(id_ronda, usuario, log);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage()); 
		}
		
	}
	
	/**
	 * Obtiene los estados de la ronda
	 * 
	 * @return List EstadoDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.EstadoDTO
	 */
	public List getEstadosRonda()
		throws ServiceException{
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getEstadosRonda();
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e); 
		}
	}
	
	/**
	 * Obtiene resultado consulta de rondas de acuerdo a criterio
	 * 
	 * @param  criterio RondasCriterioDTO 
	 * @return List MonitorRondasDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.MonitorRondasDTO
	 */
	public List getRondasByCriteria(RondasCriteriaDTO criterio)
		throws ServiceException{
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getRondasByCriteria(criterio);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e); 
		}
	}

	/**
	 * Obtiene resultado consulta de rondas de acuerdo a criterio, para el monitor de rondas
	 * @param criterio
	 * @return
	 * @throws ServiceException
	 */
	/*public List getMonRondasByCriteria(RondasCriteriaDTO criterio)
	throws ServiceException{
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getMonRondasByCriteria(criterio);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e); 
		}
	}*/

	
	/**
	 * Obtiene resultado consulta de rondas de acuerdo a criterio, para el monitor de rondas
	 * @param criterio
	 * @return
	 * @throws ServiceException
	 */
	public List getMonRondasByCriteriaCMO(RondasCriteriaDTO criterio)
	throws ServiceException{
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getMonRondasByCriteriaCMO(criterio);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e); 
		}
	}

	/**
	 * Obtiene cantidad de productos pickeados y sin pickear y sus porcentajes respecto al total
	 * @param id_jornada_picking
	 * @return
	 * @throws ServiceException
	 */
	public AvanceDTO getAvanceJornada(long id_jornada_picking)
	throws ServiceException{
		
	    PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getAvanceJornada(id_jornada_picking);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e); 
		}
	}

	//Maxbell Arreglo homologacion
    public int getCantidadOpPasadosPorBodega(long id_jornada) {
    	PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getCantidadOpPasadosPorBodega(id_jornada);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
        return 0;
    }

	/**
	 * Obtiene cantidad de productos pickeados y sin pickear y sus porcentajes respecto al total
	 * @param id_jornada_picking
	 * @return
	 * @throws ServiceException
	 */
	public AvanceDTO getAvancePedido(long id_pedido)
	throws ServiceException{
		
	    PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getAvancePedido(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e); 
		}
	}

	/**(+) INDRA (+)
	 * Obtiene cantidad de productos pickeados y sin pickear y sus porcentajes respecto al total
	 * @param id_jornada_picking
	 * @return
	 * @throws ServiceException
	 */
	public AvanceDTO getAvanceUmbralPedido(long id_pedido)
	throws ServiceException{
		
	    PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getAvanceUmbralPedido(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e); 
		}
	}
	/**
	 * Obtiene cantidad de productos pickeados y sin pickear y sus porcentajes respecto al total
	 * @param id_jornada_picking
	 * @return
	 * @throws ServiceException
	 */
	public AvanceDTO getAvanceUmbralMonto(long id_pedido)
	throws ServiceException{
		
	    PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getAvanceUmbralMonto(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e); 
		}
	}
	/**
	 * Obtiene cantidad de umbrales de la tabla parametros para saber si se pasan a bodega o no
	 * @param id_jornada_picking
	 * @return
	 * @throws ServiceException
	 */
	public AvanceDTO getAvanceUmbralParametros(long id_pedido)
	throws ServiceException{
		
	    PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getAvanceUmbralParametros(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e); 
		}
	}

	/**(-) INDRA (-)
	 * Obtiene número de registros de una búsuqe da de rondas por criterio
	 * 
	 * @param  criterio RondasCriteriaDTO
	 * @return long
	 * @throws ServiceException 
	 */
	public long getCountRondasByCriteria(RondasCriteriaDTO criterio)
		throws ServiceException{
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getCountRondasByCriteria(criterio);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e); 
		}
	}	
	
	
	
	/**
	 * Obtiene lista de rondas para un pedido
	 * 
	 * @param  id_pedido long 
	 * @return List MonitorRondasDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.MonitorRondasDTO
	 */
	public List getRondasByIdPedido(long id_pedido)	
		throws ServiceException{
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getRondasByIdPedido(id_pedido);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e); 
		}
	}

	
	
	
	/**
	 * Obtiene el detalle de una ronda
	 * 
	 * @param  id_ronda long 
	 * @return RondaDTO
	 * @throws ServiceException 
	 * @throws SystemException 
	 */
	public RondaDTO getRondaById(long id_ronda)
		throws ServiceException, SystemException{
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getRondaById(id_ronda);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e); 
		}
	}
	
	/**
	 * Obtiene el listado del log de la ronda
	 * 
	 * @param  id_ronda long 
	 * @return List LogSimpleDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.LogSimpleDTO
	 */
	public List getLogRonda(long id_ronda)
		throws ServiceException{
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getLogRonda(id_ronda);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e); 

		}
	}
	
	/**
	 * Obtiene listado de productos de la ronda
	 * 
	 * @param  id_ronda long 
	 * @return List ProductosPedidoDTO
	 * @throws ServiceException 
	 * @throws SystemException
	 * @see    cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO
	 */
	public List getProductosRonda(long id_ronda)
		throws ServiceException, SystemException {
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getProductosRonda(id_ronda);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}	
	
	
	/**
	 * Obtiene listado de productos de la ronda
	 * 
	 * @param  id_ronda long 
	 * @return List ProductosPedidoDTO
	 * @throws ServiceException 
	 * @throws SystemException
	 * @see    cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO
	 */
	public List getProductosRondaPKL(long id_ronda)
		throws ServiceException, SystemException {
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getProductosRondaPKL(id_ronda);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}	
	

	/**
	 * Obtiene id_pedido asociado a Ronda Picking Light
	 * @param  id_ronda
	 * @return long id_pedido
	 * @throws ServiceException 
	 * @throws SystemException
	 * @see    cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO
	 */
	public long getIdPedidoByRondaPKL(long id_ronda)
		throws ServiceException, SystemException {
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getIdPedidoByRondaPKL(id_ronda);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}	
	

	/**
	 * Obtiene Listado productos que no han sido pickeados
	 * agrupados por OP y por sector, para aquellas jornadas
	 * que han sido iniciadas
	 * 
	 * @param  id_local long 
	 * @param  id_sector long 
	 * @param  id_jornada long  
	 * @return List RondaPropuestaDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.RondaPropuestaDTO
	 */
	public List getRondasPropuestasDet(ProcRondasPropuestasDTO criterio)
		throws ServiceException{
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getRondasPropuestasDet(criterio);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	

	public List getRondasPropuestas(ProcRondasPropuestasDTO criterio)
	throws ServiceException{
	
	RondasCtrl rondas = new RondasCtrl();
	try {
		return rondas.getRondasPropuestas(criterio);
	} catch (RondasException e) {
		e.printStackTrace();
		throw new ServiceException(e);
	}
}

	/**
	 * Obtiene Listado productos que no han sido pickeados
	 * agrupados por OP y por sector, para aquellas jornadas
	 * que han sido iniciadas
	 * 
	 * @param  id_local long 
	 * @param  id_sector long 
	 * @param  id_jornada long  
	 * @return List RondaPropuestaDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.RondaPropuestaDTO
	 */
	public List getPedidosXJornada(PedidosCriteriaDTO criterio)
		throws ServiceException{
		
	    PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getPedidosXJornada(criterio); //RondasPropuestas(criterio);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	
	/**
	 * Obtiene Listado de bins de la ronda
	 * 
	 * @param  id_ronda long 
	 * @return List BinDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.BinDTO
	 */
	public List getBinsRonda(long id_ronda)
		throws ServiceException{
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getBinsRonda(id_ronda);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}	
	
	
	
	/**
	 * Obtiene Listado de bins de la ronda
	 * 
	 * @param  id_ronda long 
	 * @return List BinDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.BinDTO
	 */
	public List getBinsRondaPKL(long id_ronda)
		throws ServiceException{
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getBinsRondaPKL(id_ronda);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}	
	
	
	/**
	 * Realiza proceso de preparación de ronda (cambia su estado)
	 * 
	 * @param  id_ronda long 
	 * 
	 * @throws ServiceException 
	 * @throws SystemException 
	 */
	public void PreparaRonda(long id_ronda)
		throws ServiceException, SystemException{
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			rondas.doPreparaRonda(id_ronda);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	
	/**
	 * Crea una ronda para un pedido por sector, incluyendo la cantidad de productos
	 * indicada
	 * 
	 * @param  ronda CreaRondaDTO 
	 * @return long, nuevo id
	 * @throws SystemException 
	 * @throws ServiceException
	 */
	public long CreaRonda(CreaRondaDTO ronda) throws ServiceException, SystemException{
		try {
		RondasCtrl rondasctrl = new RondasCtrl();
		return rondasctrl.doCreaRonda(ronda);
		} catch (RondasException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	
	/**
	 * Crea una ronda para un pedido por pedido, incluyendo la cantidad de productos
	 * indicada
	 * 
	 * @param  ronda CreaRondaDTO 
	 * @return long, nuevo id
	 * @throws SystemException 
	 * @throws ServiceException
	 */
	public long CreaRondaPKL(CreaRondaDTO ronda) throws ServiceException, SystemException{
		try {
		RondasCtrl rondasctrl = new RondasCtrl();
		return rondasctrl.doCreaRondaPKL(ronda);
		} catch (RondasException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	
	/**
	 * Muestra el total de productos por OP por ronda
	 * 
	 * @param  id_ronda long 
	 * @return List RondaDetalleResumenDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.RondaDetalleResumenDTO
	 */
	public List getResumenRondaById(long id_ronda)
		throws ServiceException{
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getResumenRondaById(id_ronda);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Retorna listado para llenar tabla PDA.
	 * 
	 * @param  id_ronda long 
	 * @return List BarraDetallePedidosRondaDTO
	 * @throws ServiceException
	 * @throws SystemException
	 * @see    cl.bbr.jumbocl.pedidos.dto.BarraDetallePedidosRondaDTO
	 */
	public List getBarrasRondaDetallePedido(long id_ronda)
		throws ServiceException, SystemException{
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getBarrasRondaDetallePedido(id_ronda);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}	
	
	
	/**
     * Retorna listado para llenar tabla PDA.
     * 
     * @param  id_ronda long 
     * @return List BarraDetallePedidosRondaDTO
     * @throws ServiceException
     * @throws SystemException
     * @see    cl.bbr.jumbocl.pedidos.dto.BarraDetallePedidosRondaDTO
     */
    public List getBarrasAuditoriaSustitucion(List listBarras, long id_ronda)
        throws ServiceException, SystemException{
        
        RondasCtrl rondas = new RondasCtrl();
        try {
            return rondas.getBarrasAuditoriaSustitucion(listBarras, id_ronda);
        } catch (RondasException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }   
    

	/**
	 * Asigna pickeador a la ronda y cambia su estado a En Picking
	 * 
	 * @param  id_ronda long 
	 * @param  id_usuario long 
	 * @throws SystemException 
	 * @throws ServiceException
	 */
	public void doAsignaRonda(long id_ronda, long id_usuario) 
		throws ServiceException, SystemException {
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			rondas.doAsignaRonda(id_ronda, id_usuario);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	

	/**
	 * Retorna listado de sustitutos de la ronda, segun <b>id</b> de la ronda.
	 * 
	 * @param  id_ronda long 
	 * @return List SustitutoDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.SustitutoDTO
	 */
	public List getSustitutosByRondaId(long id_ronda) 
	throws ServiceException{
		RondasCtrl rondas = new RondasCtrl();
		try {
		return rondas.getSustitutosByRondaId(id_ronda);
		}catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
		
	/**
	 * Retorna listado de faltantes de la ronda, segun <b>id</b> de la ronda.
	 * 
	 * @param  id_ronda long 
	 * @return List FaltanteDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.FaltanteDTO
	 */
	public List getFaltantesByRondaId(long id_ronda) 
	throws ServiceException{
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getFaltantesByRondaId(id_ronda);
		}catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Obtiene el listado de zonas de una ronda
	 * @param id_ronda
	 * @return List ZonaDTO
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List getBuscaZonaByRonda(long id_ronda)
	throws SystemException, ServiceException {		
		RondasCtrl ctrl = new RondasCtrl();
	    try{
	    	return ctrl.getBuscaZonaByRonda(id_ronda);
	    }catch(RondasException ex){
	        logger.debug(" Problemas con controles de CategoriasSap ");
	        throw new ServiceException(ex);
	    }
	}
	
	
	 /**
	  * getSustitutosCriterios
	 * @return List
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getSustitutosCriterios()
		throws ServiceException, SystemException {
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getSustitutosCriterio();
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}	
	
	
	 /**
	  * getSustitutosCriterios
	 * @return List
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public long getIdSectorByNombre(String nombre)
		throws ServiceException, SystemException {
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getIdSectorByNombre(nombre);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}	

	public Hashtable setOrdenProductosPDA(long id_ronda)
	throws ServiceException, SystemException {
	
	RondasCtrl rondas = new RondasCtrl();
	try {
		return rondas.setOrdenProductosPDA(id_ronda);
	} catch (RondasException e) {
		e.printStackTrace();
		throw new ServiceException(e);
	}
}	

	
	/**
	 * Obtiene listado de secciones Sap
	 * 
	 * @return List CategoriaSapDTO's
	 * @throws SystemException 
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.CategoriaSapDTO
	 */
	
	public List getSeccionesSap()
		throws SystemException, ServiceException {		
		PedidosCtrl catsap = new PedidosCtrl();
	    try{
	    	return catsap.getSeccionesSap();
	    }catch(PedidosException ex){
	        logger.debug(" Problemas con controles de CategoriasSap ");
	        throw new ServiceException(ex);
	    }
	}
	
	/*
	 * ------------- Despachos ------------------
	 */
	
	/**
	 * Obtiene listado de estados de despacho
	 * 
	 * @return List EstadoDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.EstadoDTO
	 */
	public List getEstadosDespacho()
		throws ServiceException{
		
		DespachoCtrl despacho = new DespachoCtrl();
		try {
			return despacho.getEstadosDespacho();
		} catch (DespachosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Obtiene listado de pedidos en su flujo de despacho
	 * 
	 * @param  criterio DespachoCriteriaDTO
	 * @return List MonitorDespachosDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.MonitorDespachosDTO
	 */
	public List getDespachosByCriteria(DespachoCriteriaDTO criterio)
		throws ServiceException{
		DespachoCtrl despacho = new DespachoCtrl();
		try {
			return despacho.getDespachosByCriteria(criterio);
		} catch (DespachosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Obtiene cantidad de despachos
	 * 
	 * @param  criterio DespachoCriteriaDTO 
	 * @return long
	 * @throws ServiceException 
	 */
	public long getCountDespachosByCriteria( DespachoCriteriaDTO criterio )
	 	throws ServiceException{
		
		DespachoCtrl despacho = new DespachoCtrl();
		try {
			return despacho.getCountDespachosByCriteria(criterio);
		} catch (DespachosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Obtiene información del despacho de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return DespachoDTO
	 * @throws ServiceException 
	 */
	public DespachoDTO getDespachoById(long id_pedido)
		throws ServiceException{
		
		DespachoCtrl despacho = new DespachoCtrl();
		try {
			return despacho.getDespachoById(id_pedido);
		} catch (DespachosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	
	/**
	 * Cambia el estado a un pedido en su fase de despacho
	 * 
	 * @param  id_pedido long 
	 * @param  id_estado long 
	 * @param  login String usuario que realiza la operación
	 * @param  log String 
	 * 
	 * @throws SystemException 
	 * @throws ServiceException
	 */
	public void setCambiaEstadoDespacho(long id_pedido, long id_estado, String login, String log)
		throws ServiceException, SystemException{
		
		DespachoCtrl ctrl = new DespachoCtrl();
		try {
			ctrl.doCambiaEstadoDespacho(id_pedido, id_estado, login, log);
		} catch (DespachosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Agrega registro al log del despacho
	 * 
	 * @param  id_pedido long 
	 * @param  login String 
	 * @param  log String 
	 * 
	 * @throws ServiceException
	 * @throws SystemException 
	 */
	public void addLogDespacho(long id_pedido, String login, String log)
		throws ServiceException, SystemException{
		
		DespachoCtrl ctrl = new DespachoCtrl();
		try {
			ctrl.doAddLogDespacho(id_pedido, login, log);
		} catch (DespachosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * Obtiene listado del log de un despacho
	 * 
	 * @param  id_pedido long 
	 * @return List LogSimpleDTO
	 * @throws PedidosException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.LogSimpleDTO
	 */
	public List getLogDespacho(long id_pedido)
		throws ServiceException{
		
		DespachoCtrl ctrl = new DespachoCtrl();
		try {
			return ctrl.getLogDespacho(id_pedido);
		} catch (DespachosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Realiza proceso de Re-agendamiento de un despacho
	 * 
	 * @param  id_pedido long 
	 * @param  id_jdespacho long 
	 * @param  sobrescribeprecio boolean 
	 * @param  precio int 
	 * @param  usr_login String 
	 * 
	 * @throws SystemException 
	 * @throws ServiceException 
	 */
	public void doReagendaDespacho(long id_pedido, long id_jdespacho, boolean sobrescribeprecio, int precio, String usr_login, boolean modificarJPicking, boolean modificarPrecio )
		throws ServiceException, SystemException{
		
		CalendariosCtrl ctrl = new CalendariosCtrl();
		try {
		    ctrl.doReagendaDespacho(id_pedido, id_jdespacho, sobrescribeprecio, precio, usr_login, modificarJPicking, modificarPrecio );
		} catch (DespachosException e) {
			//e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Modifica el Costo de despacho
	 * 
	 * @param  id_pedido long 
	 * @param  precio int 
	 * @param  usr_login String 
	 * 
	 * @throws SystemException 
	 * @throws ServiceException 
	 */
	public void doCambiaCostoDespacho(long id_pedido, int precio, String usr_login)
		throws ServiceException, SystemException{
		
		CalendariosCtrl ctrl = new CalendariosCtrl();
		try {
		 ctrl.doActualizaCostoDespacho(id_pedido, precio, usr_login);
		} catch (DespachosException e) {
			//e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Agenda jornada de despacho a un pedido
	 * 
	 * @param  id_pedido long 
	 * @param  id_jdespacho long 
	 * 
	 * @throws SystemException 
	 * @throws ServiceException 
	 */
    /*
	public void doAgendaDespacho(long id_pedido, long id_jdespacho)
		throws SystemException, ServiceException{
		
		CalendariosCtrl ctrl = new CalendariosCtrl();
		try {
			ctrl.doAgendaDespacho(id_pedido,id_jdespacho);
		} catch (DespachosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}*/
		
	/**
	 * Genera jornadas de despacho en forma masiva.
	 *  
	 * @param  dto
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean doGeneraDespMasivo(ProcGeneraDespMasivoDTO dto)	throws SystemException, ServiceException{
	
		CalendariosCtrl ctrl = new CalendariosCtrl();
		try {
			return ctrl.doGeneraDespMasivo(dto);
		} catch (CalendarioException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	/*
	 * ------------- Local ------------------
	 */
	
	/**
	 * Retorna listado con locales creados en el sistema
	 * 
	 * @return List of LocalDTO's
	 * @throws ServiceException 
	 * @throws SystemException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.LocalDTO
	 */
	public List getLocalesAll() throws ServiceException, SystemException{
		LocalCtrl ctrl = new LocalCtrl();
		try {
			return ctrl.getLocalesAll();
		} catch (LocalException e) {
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Retorna un listado de Sectores de Picking
	 * 
	 * @return List SectorLocalDTO
	 * @throws SystemException 
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.SectorLocalDTO
	 */	
	public List getSectores()
		throws  SystemException, ServiceException {
		LocalCtrl despacho = new LocalCtrl();
		try {
			return despacho.getSectores();
		} catch (LocalException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Retorna un listado con las Zonas de Despacho de un Local
	 * 
	 * @param  id_local long 
	 * @return List ZonaDTO
	 * @throws SystemException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.ZonaDTO
	 */
	public List getZonasLocal(long id_local)
		throws SystemException {
		LocalCtrl despacho = new LocalCtrl();
		return despacho.getZonasLocal(id_local);		
	}

	/**
	 * Obtiene el detalle de una zona de despacho
	 * 
	 * @param  id_zona long 
	 * @return ZonaDTO
	 * @throws SystemException 
	 */
	public ZonaDTO getZonaDespacho(long id_zona)
		throws SystemException {
		LocalCtrl despacho = new LocalCtrl();
		return despacho.getZonaDespacho(id_zona);		
	}	
	
	
	/**
	 * Agrega una zona de despacho
	 * 
	 * @param  zona ZonaDTO 
	 * @return long id_zona insertada
	 * @throws SystemException 
	 * @throws ServiceException 
	 */
	public long doAgregaZonaDespacho(ZonaDTO zona)
		throws SystemException, ServiceException{
		
		LocalCtrl ctrl = new LocalCtrl();
		try {
			return ctrl.doAgregaZonaDespacho(zona);
		} catch (LocalException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	public long AddPoligono(PoligonoDTO pol)
		throws SystemException, ServiceException{
		
		PoligonosCtrl ctrl = new PoligonosCtrl();
		try {
			return ctrl.AddPoligono(pol);
		} catch (PoligonosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	public long AddPoligonoWithZona(PoligonoDTO pol)
			throws SystemException, ServiceException{
			
			PoligonosCtrl ctrl = new PoligonosCtrl();
			try {
				return ctrl.AddPoligonoWithZona(pol);
			} catch (PoligonosException e) {
				e.printStackTrace();
				throw new ServiceException(e.getMessage());
			}
		}
	
	public boolean ModPoligono(PoligonoDTO pol)
		throws SystemException, ServiceException{
		
	    PoligonosCtrl ctrl = new PoligonosCtrl();
		try {
			return ctrl.ModPoligono(pol);
		} catch (PoligonosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	public int VerificaPoligonoEnDirecciones(long id_poligono)
		throws SystemException, ServiceException{
		
	    PoligonosCtrl ctrl = new PoligonosCtrl();
		try {
			return ctrl.VerificaPoligonoEnDirecciones(id_poligono);
		} catch (PoligonosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	
	public boolean DelPoligono(long id_poligono)
		throws SystemException, ServiceException{
		
	    PoligonosCtrl ctrl = new PoligonosCtrl();
		try {
			return ctrl.DelPoligono(id_poligono);
		} catch (PoligonosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	public boolean DelTrxMP(long id_pedido)
		throws SystemException, ServiceException{
		
	    PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.DelTrxMP(id_pedido);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * Modifica una zona de despacho para un local
	 * 
	 * @param  zona ZonaDTO 
	 * 
	 * @throws SystemException 
	 * @throws ServiceException 
	 */
	public void doModZonaDespacho(ZonaDTO zona)
		throws SystemException, ServiceException{
		
		LocalCtrl ctrl = new LocalCtrl();
		try {
			ctrl.doModZonaDespacho(zona);
		} catch (LocalException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * Obtiene listado de comunas de una zona de despacho
	 * 
	 * @param  id_zona long 
	 * @return List ComunaDTO
	 * @throws SystemException 
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.ComunaDTO
	 */
	public List getComunasByIdZonaDespacho(long id_zona)
		throws SystemException, ServiceException{
		
		LocalCtrl ctrl = new LocalCtrl();
		try {
			return ctrl.getComunasByIdZonaDespacho(id_zona);
		} catch (LocalException e) {
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Obtiene listado de comunas de un local
	 * 
	 * @param  id_local long 
	 * @return List ComunaDTO
	 * @throws SystemException 
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.ComunaDTO
	 */
	public List getComunasLocal(long id_local)
		throws SystemException, ServiceException{
		
		LocalCtrl ctrl = new LocalCtrl();
		try {
			return ctrl.getComunasLocal(id_local);
		} catch (LocalException e) {
			throw new ServiceException(e);
		}
	}
	
	
	public HashMap getListadoOP(Calendar cal, int id_local, String hora_desp)
		throws SystemException, ServiceException{
		
	    PedidosCtrl ped = new PedidosCtrl();
		try {
			return ped.getListadoOP(cal, id_local, hora_desp);
		} catch (PedidosException e) {
			throw new ServiceException(e);
		}
	}
	
	
	public boolean verificaNumPoligono(int id_comuna, int num_pol)
		throws SystemException, ServiceException{
		
	    PoligonosCtrl pol = new PoligonosCtrl();
		try {
			return pol.verificaNumPoligono(id_comuna, num_pol);
		} catch (SystemException e) {
			throw new ServiceException(e);
		}
	}
	
	
	public int getLocalByPoligono(int id_poligono)
		throws SystemException, ServiceException{
		
	    PoligonosCtrl pol = new PoligonosCtrl();
		try {
			return pol.getLocalByPoligono(id_poligono);
		} catch (PoligonosException e) {
			throw new ServiceException(e);
		}
	}
	
	
	
	/**
	 * Actualiza comunas asociadas a una zona de despacho
	 * 
	 * @param  dto ComunasZonaDTO
	 * 
	 * @throws SystemException
	 * @throws ServiceException
	 */
	/*public void doActualizaComunasZonaDespacho(ComunasZonaDTO dto)
		throws SystemException, ServiceException{
		
		LocalCtrl ctrl = new LocalCtrl();
		try {
			ctrl.doActualizaComunasZonaDespacho(dto);
		} catch (LocalException e) {
			throw new ServiceException(e);
		}
	}*/
	
	
	/**
	 * Actualiza Poligonos asociados a una zona de despacho
	 * 
	 * @param  dto PoligonosZonaDTO
	 * 
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void doActualizaPoligonosZonaDespacho(PoligonosZonaDTO dto)
		throws SystemException, ServiceException{
		
		PoligonosCtrl ctrl = new PoligonosCtrl();
		try {
			ctrl.doActualizaPoligonosZonaDespacho(dto);
		} catch (PoligonosException e) {
			throw new ServiceException(e);
		}
	}
	

	
	/**
	 * Actualiza la dirección de despacho de un pedido a partir de una dirección 
	 * existente en la libreta de direcciones
	 * 
	 * @param  dto ProcModPedidoDirDTO 
	 * @return boolean
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean setModPedidoDir(String user, DireccionesDTO nuevaDir, PedidoDTO ped)
		throws ServiceException, SystemException {
		
		PedidosCtrl ctrl = new PedidosCtrl ();
		try {
			return ctrl.setModPedidoDir(user, nuevaDir, ped);
		} catch (PedidosException e) {
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	/**
	 * Modifica las indicaciones de un pedido, segun la información contenida en <b>ProcModPedidoIndicDTO</b>. 
	 * 
	 * @param  prm ProcModPedidoIndicDTO  
	 * @return boolean
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean setModPedidoIndic(ProcModPedidoIndicDTO prm) throws SystemException, ServiceException {
		PedidosCtrl ctrl = new PedidosCtrl ();
		try {
			return ctrl.setModPedidoIndic(prm);
		} catch (PedidosException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Permite agregar o eliminar un producto de un pedido, segun la información contenida en <b>ProcModPedidoProdDTO</b>.<br>
	 * Nota: Este método tiene <b>Transaccionalidad</b>.
	 * 
	 * @param  prm ProcModPedidoProdDTO 
	 * @return boolean
	 * @throws SystemException
	 * @throws ServiceException
	 * */
	public boolean setModPedidoProd(ProcModPedidoProdDTO prm) throws SystemException, ServiceException {
		PedidosCtrl ctrl = new PedidosCtrl ();
		try {
			return ctrl.setModPedidoProd(prm);
		} catch (PedidosException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Permite eliminar el detalle de un pedido.
	 * Se utiliza en el Jumbo BOC, en la vista del detalle del pedido.
	 * 
	 * @param  prod ProductosPedidoDTO 
	 * @return boolean
	 * @throws SystemException
	 * @throws ServiceException
	 * */
	public boolean elimProductoPedido(ProductosPedidoDTO prod)
		throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.elimProductoPedido(prod);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Permite actualizar el monto total y la cantidad de productos del pedido, segun el <b>id</b> del pedido. 
	 * 
	 * @param  id_pedido long 
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 **/
    /*
	public boolean recalcPedido(long id_pedido) throws ServiceException, SystemException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.recalcPedido(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	*/
	/**
	 * Obtiene el precio de un producto, segun el <b>id</b> del producto y el <b>id</b> del local. 
	 * 
	 * @param  id_local long 
	 * @param  id_producto long 
	 * @return double, devuelve el precio del producto.
	 * @throws ServiceException
	 * @throws SystemException
	 * */
	public double getPrecioByLocalProd(long id_local, long id_producto) throws ServiceException, SystemException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getPrecioByLocalProd(id_local, id_producto);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Permite confirmar el pedido, segun el <b>id</b> del pedido.<br> 
	 * Luego de la confirmación del pedido, actualiza información en el cliente.
	 * 
	 * @param  id_pedido long 
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 * */
	public boolean setConfirmarOP(long id_pedido, UserDTO usr) throws ServiceException, SystemException{
		PedidosCtrl pedidos = new PedidosCtrl();
		try{
			
			return pedidos.setConfirmarOP(id_pedido, usr);
		} catch (PedidosException e) {
			//e.printStackTrace();
			logger.debug("error:"+e.getMessage());
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	/**
	 * Obtiene los detalles de picking de un pedido, segun el <b>id</b> del pedido.<br> 
	 * 
	 * @param  id_pedido long 
	 * @return List DetallePickingDTO
	 * @throws ServiceException
	 * @throws SystemException
	 * @see    cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO
	 * */
	public List getDetPickingByIdPedido(long id_pedido) throws ServiceException, SystemException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try{
			return pedidos.getDetPickingByIdPedido(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * Obtiene los detalles de picking de un pedido, segun el <b>id</b> del pedido.<br> 
	 * 
	 * @param  id_pedido long 
	 * @return List DetallePickingDTO
	 * @throws ServiceException
	 * @throws SystemException
	 * @see    cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO
	 * */
	public List getDetPickingHojaDesp2(long id_pedido) throws ServiceException, SystemException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try{
			return pedidos.getDetPickingHojaDesp2(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	

	/**
	 * Obtiene las facturas de un pedido, segun el <b>id</b> del pedido.<br> 
	 * 
	 * @param  id_pedido long 
	 * @return List FacturaDTO
	 * @throws ServiceException
	 * @throws SystemException
	 * @see    cl.bbr.jumbocl.pedidos.dto.FacturaDTO
	 * */
	public List getFacturasByIdPedido(long id_pedido) throws ServiceException, SystemException{
		PedidosCtrl pedidos = new PedidosCtrl();
		try{
			return pedidos.getFacturasByIdPedido(id_pedido);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Recepciona Ronda desde la PDA
	 * 
	 * @param  dto ActDetallePickingDTO 
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	 /*
	public boolean recepcionRondaByPOS(ActDetallePickingDTO dto) throws ServiceException, SystemException{
		RondasCtrl ctrl = new RondasCtrl();
		try{
			return ctrl.doRecepcionaRonda(dto);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}*/
	
	/**
	 * Modificado por Jean
	 * @throws SystemException
	 * @throws RondasException
	 */
	public boolean recepcionRondaByPOS(ActDetallePickingDTO dto) throws ServiceException, SystemException{
		RondasCtrl ctrl = new RondasCtrl();
		
		try {
            //nuevo método
            ctrl.subeRonda(dto);
            
            // ** PROMOCIONES ** Se agrega para arreglar el problema de promociones post-picking 
            ctrl.doActEstadoPedido(dto);
            
        } catch (RondasException e) {
            //e.printStackTrace();
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            //e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
		
        
		/*try{
			if (ctrl.doRecepcionaRonda(dto)){
		        ctrl.doActDetallePedido(dto);
			    ctrl.doActEstadoPedido(dto);
			}
		} catch (RondasException e) {
			throw new ServiceException(e.getMessage());
		} catch (Exception e){
		    throw new ServiceException(e.getMessage());
		}*/
		
		return true;
	}

    
    //TPAuditSustitucionDTO
    public boolean recepcionAuditoriaSustitucion(List lst_AudSust) throws ServiceException, SystemException{
        RondasCtrl ctrl = new RondasCtrl();

        try {
            ctrl.recepcionAuditoriaSustitucion(lst_AudSust);
        } catch (RondasException e) {
            //e.printStackTrace();
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            //e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
        return true;

    }


    
    public void marcaRondaAuditoriaSustitucion(int id_ronda) throws ServiceException, SystemException{
        RondasCtrl ctrl = new RondasCtrl();

        try {
            ctrl.marcaRondaAuditoriaSustitucion(id_ronda);
        } catch (RondasException e) {
            //e.printStackTrace();
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            //e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }


    
	/**
	 * Actualiza los datos de una factura, segun la información contenida en <b>ProcModFacturaDTO</b>.<br> 
	 * 
	 * @param  prm ProcModFacturaDTO 
	 * @return boolean 
	 * @throws ServiceException
	 * @throws SystemException
	 * */
	public boolean setModFactura(ProcModFacturaDTO prm) throws ServiceException, SystemException{
		PedidosCtrl pedidos = new PedidosCtrl();
		try{
			return pedidos.setModFactura(prm);
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	
	
	
	/*
	 * ------------ Comunas ------------------
	 */
	
	
	/**
	 * Listado de Comunas
	 * 
	 * @return Lits of ComunaDTO's
	 * @throws SystemException 
	 * @throws ServiceException
	 */
	public List getComunasAll() 
		throws ServiceException, SystemException{
		
		ComunasCtrl ctrl = new ComunasCtrl();

		try {
			return ctrl.getComunasAll();
		} catch (ComunasException e) {
			throw new ServiceException(e);
		}
	
	}
		

	/**
	 * Obtiene listado de Zonas de despacho para una comuna
	 * 
	 * @param  id_comuna long 
	 * @return List of ZonaxComunaDTO's
	 * @throws SystemException 
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.ZonaxComunaDTO
	 */
	public List getZonasxComuna(long id_comuna)
		throws ServiceException, SystemException{
		
		ComunasCtrl ctrl = new ComunasCtrl();

		try {
			return ctrl.getZonasxComuna(id_comuna);
		} catch (ComunasException e) {
			throw new ServiceException(e);
		}
	}
	

	public List getPoligonosXComuna(long id_comuna)
		throws ServiceException, SystemException{
		
		PoligonosCtrl ctrl = new PoligonosCtrl();
		try {
			return ctrl.getPoligonosXComuna(id_comuna);
		} catch (PoligonosException e) {
			throw new ServiceException(e);
		}
	}
	

	public List getPoligonosXComunaAll(long id_comuna)
		throws ServiceException, SystemException{
		
		PoligonosCtrl ctrl = new PoligonosCtrl();
		try {
			return ctrl.getPoligonosXComunaAll(id_comuna);
		} catch (PoligonosException e) {
			throw new ServiceException(e);
		}
	}
	

	public List getPoligonosXComunaSinZona(long id_comuna)
		throws ServiceException, SystemException{
		
		PoligonosCtrl ctrl = new PoligonosCtrl();
		try {
			return ctrl.getPoligonosXComunaSinZona(id_comuna);
		} catch (PoligonosException e) {
			throw new ServiceException(e);
		}
	}
	

	public List getComunasConPoligonosSinZona()
		throws ServiceException, SystemException{
		
		PoligonosCtrl ctrl = new PoligonosCtrl();
		try {
			return ctrl.getComunasConPoligonosSinZona();
		} catch (PoligonosException e) {
			throw new ServiceException(e);
		}
	}
	

	public List getPoligonosXZona(long id_zona)
		throws ServiceException, SystemException{
		
		PoligonosCtrl ctrl = new PoligonosCtrl();
		try {
			return ctrl.getPoligonosXZona(id_zona);
		} catch (PoligonosException e) {
			throw new ServiceException(e);
		}
	}
	


	public PoligonoDTO getPoligonoById(long id_poligono)
		throws ServiceException, SystemException{
		
		PoligonosCtrl ctrl = new PoligonosCtrl();
		try {
			return ctrl.getPoligonoById(id_poligono);
		} catch (PoligonosException e) {
			throw new ServiceException(e);
		}
	}
		

	
	/**
	 * Actualiza el orden de una relación comuna-zona
	 * 
	 * @param  procModZonaxComuna List of ProcModZonaxComuna's
	 * 
	 * @throws SystemException 
	 * @throws ServiceException
	 */
	/*public void doActualizaOrdenZonaxComuna(List procModZonaxComuna)
		throws ServiceException, SystemException{
		
		ComunasCtrl ctrl = new ComunasCtrl();

		try {
			logger.debug("dentro de pedidos service");
			ctrl.doActualizaOrdenZonaxComuna(procModZonaxComuna);
		} catch (ComunasException e) {
			throw new ServiceException(e);
		}
	}*/
	

	/**
	 * Listado de Secciones SAP
	 * 
	 * @param  id_jornada long 
	 * @return Lista de SeccionSapDTO's
	 * @throws SystemException 
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.SeccionSapDTO
	 * 
	 */
	
	public List getSeccionesSAPPreparadosByIdJornada(long id_jornada) throws ServiceException, SystemException{
		
		JornadasCtrl ctrl = new JornadasCtrl();

		try {
			return ctrl.getSeccionesSAPPreparadosByIdJornada(id_jornada);
		} catch (JornadasException e) {
			throw new ServiceException(e.getMessage());
		}  
	
	}
	
	/**
	 * Crea sector de picking
	 * @param sector
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void doAddSectorPicking(SectorLocalDTO sector)
	throws ServiceException, SystemException{
	
		SectoresCtrl ctrl = new SectoresCtrl();
	
		try {

			ctrl.doAddSectorPicking(sector);
		} catch (SectorPickingDAOException e) {
			throw new ServiceException(e);
		}

	}	
	/**
	 * Actualiza sector de picking
	 * @param sector
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void doActualizaSectorPicking(SectorLocalDTO sector)
	throws ServiceException, SystemException{
	
		SectoresCtrl ctrl = new SectoresCtrl();
	
		try {
			ctrl.doActualizaSectorPicking(sector);
		} catch (SectorPickingDAOException e) {
			throw new ServiceException(e);
		}

	}
	/**
	 * Elimina sector de picking
	 * @param id_sector
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void doEliminaSectorPicking(long id_sector)
	throws ServiceException, SystemException{
	
		SectoresCtrl ctrl = new SectoresCtrl();
	
		try {
			ctrl.doEliminaSectorPicking(id_sector);
		} catch (SectorPickingDAOException e) {
			throw new ServiceException(e);
		}

	}
	
	/**
	 * Listado de Locales
	 * 
	 * @param  cod_prod long 
	 * @return Lista de LocalDTO's
	 * @throws SystemException 
	 * @throws ServiceException
	 * 
	 */
	public List getLocalesByProducto(long cod_prod) throws ServiceException, SystemException{
		
		PedidosCtrl ctrl = new PedidosCtrl();

		try {
			return ctrl.getLocalesByProducto(cod_prod);
		} catch (PedidosException e) {
			throw new ServiceException(e.getMessage());
		}  
	
	}
	
	/**
	 * Elimina la relación entre productos y sector, para un producto y local determinado
	 *  
	 * @param  id_producto
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setDelProductoXSector(long id_producto) throws ServiceException, SystemException{
		
		PedidosCtrl ctrl = new PedidosCtrl();

		try {
			return ctrl.setDelProductoXSector(id_producto);
		} catch (PedidosException e) {
			throw new ServiceException(e.getMessage());
		}  
	
	}
	
	/**
	 * Agrega relación entre producto y sector
	 * 
	 * @param  id_producto
	 * @param  id_sector
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setAddProductoXSector(long id_producto, long id_sector) throws ServiceException, SystemException{
		
		PedidosCtrl ctrl = new PedidosCtrl();

		try {
			return ctrl.setAddProductoXSector(id_producto, id_sector);
		} catch (PedidosException e) {
			throw new ServiceException(e.getMessage());
		}  
	
	}

	/**
	 * Obtiene el id del sector , segun producto y local
	 * 
	 * @param  id_producto
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public long getSectorByProd(long id_producto) throws ServiceException, SystemException{
		
		PedidosCtrl ctrl = new PedidosCtrl();

		try {
			return ctrl.getSectorByProd(id_producto);
		} catch (PedidosException e) {
			throw new ServiceException(e.getMessage());
		}  
	
	}

	/**
	 * Rechaza el pago de un pedido
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setRechazaPagoOP(RechPagoOPDTO dto) throws ServiceException, SystemException {
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.setRechazaPagoOP(dto);
		} catch (PedidosException e) {
			//e.printStackTrace();
			logger.debug("Rechazo de pago OP");
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Resetea el estado de la ronda a Creada
	 * 
	 * @param  id_ronda
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setReseteaRonda(long id_ronda)throws ServiceException, SystemException {
		
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.setReseteaRonda(id_ronda);
		} catch (RondasException e) {

			logger.debug("Resetea Ronda");
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Obtiene el listado de las politicas de sustitución
	 * 
	 * @return List 
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getPolitSustitucionAll() throws ServiceException, SystemException{
		
		PedidosCtrl ctrl = new PedidosCtrl();

		try {
			return ctrl.getPolitSustitucionAll();
		} catch (PedidosException e) {
			throw new ServiceException(e.getMessage());
		}  
	
	}
	
	/**
	 * Modifica la politica de sustitucion de un pedido
	 * 
	 * @param dto
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setModPedidoPolSust(ProcModPedidoPolSustDTO dto) throws ServiceException, SystemException{
		
		PedidosCtrl ctrl = new PedidosCtrl();

		try {
			return ctrl.setModPedidoPolSust(dto);
		} catch (PedidosException e) {
			throw new ServiceException(e.getMessage());
		}  
	
	}
	
	/**
	 * Cambiar el estado a En Pago de un pedido
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setCambiarEnPagoOP(CambEnPagoOPDTO dto) throws ServiceException, SystemException {
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.setCambiarEnPagoOP(dto);
		} catch (PedidosException e) {
			//e.printStackTrace();
			logger.debug("Rechazo de pago OP");
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * retorna los sustitutos de un pedido en particular, de la forma como se envían en el email
	 * (Modificación según req. 500)
	 * @param  id_pedido long 
	 * @return List SustitutoDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.SustitutoDTO
	 */
	public List productosEnviadosPedidoForEmail(long id_pedido) 
		throws ServiceException{
			PedidosCtrl pedidos = new PedidosCtrl();
			try {
			return pedidos.productosSustitutosPedidoForEmail(id_pedido);
			}catch (PedidosException e) {
				e.printStackTrace();
				throw new ServiceException(e);
			}
	}

	
	/**
	 * Recupera el Listado de Horas de Inicio e Jornadas de Despacho
	 * (Modificación según req. 567)
	 * @return List String
	 * @throws ServiceException
	 */
	public List getHorasInicioJDespacho() 
		throws ServiceException{
			PedidosCtrl pedidos = new PedidosCtrl();
			try {
			return pedidos.getHorasInicioJDespacho();
			}catch (PedidosException e) {
				e.printStackTrace();
				throw new ServiceException(e);
			}
	}	
	
	
	/**
	 * Recupera el Listado de Horas de Inicio e Jornadas de Despacho
	 * (Modificación según req. 567)
	 * @return List String
	 * @throws ServiceException
	 */
	public List getJornadasDespachoByFecha(String fecha, int local) 
		throws ServiceException{
			PedidosCtrl pedidos = new PedidosCtrl();
			try {
			return pedidos.getJornadasDespachoByFecha(fecha, local);
			}catch (PedidosException e) {
				e.printStackTrace();
				throw new ServiceException(e);
			}
	}	
	

	/**
	 * Obtiene un listado de bins asociados a una ronda (formulario de picking).
	 * @param id_ronda
	 * @return
	 * @throws ServiceException
	 */
	public List getBinsFormPickByRondaId(long id_ronda) throws ServiceException{
	
		RondasCtrl bin = new RondasCtrl();
		try {
			return bin.getBinsFormPickByRondaId(id_ronda);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Permite agregar un bin a una ronda (Formulario de Picking)
	 * @param bin
	 * @return
	 * @throws ServiceException
	 */
	public void doAgregaBinRonda(BinFormPickDTO bin) throws ServiceException, SystemException{
		
		RondasCtrl bins = new RondasCtrl();
		try {
			 bins.doAgregaBinRonda(bin);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	/**
	 * Permite agregar un registro al detalle de picking (formulario de picking)
	 * @param pick DetalleFormPickingDTO
	 * @param bin BinFormPickDTO
	 * @return long idbin
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public long doAgregaDetalleFormPicking(FPickDTO pick, BinFormPickDTO bin )	throws ServiceException, SystemException{
		RondasCtrl bins = new RondasCtrl();
		try {
			return  bins.doAgregaDetalleFormPicking(pick,bin);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Obtiene los productos pickeados en una ronda (Formulario de picking)
	 * @param id_ronda
	 * @return List
	 * @throws ServiceException
	 */
	public List getDetallePickFormPick(ProcFormPickDetPickDTO datos) throws ServiceException{
		
		RondasCtrl pick = new RondasCtrl();
		try {
			return pick.getDetallePickFormPick(datos);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	
	public List getProductosCbarraRonda(ProcFormPickDetPedDTO datos)  throws ServiceException{
		
		RondasCtrl prod = new RondasCtrl();
		try {
			return prod.getProductosCbarraRonda(datos);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Obtiene el producto segun un codigo de barras
	 * @param cod_barra
	 * @return
	 * @throws ServiceException
	 */
	public ProductoCbarraDTO getProductoByCbarra(String cod_barra)  throws ServiceException{
		
		RondasCtrl prod = new RondasCtrl();
		try {
			return prod.getProductoByCbarra(cod_barra);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}


	/**
	 * Elimina un detalle de picking (Formulario de Picking)
	 * @param id_row
	 * @return boolean
	 * @throws ServiceException
	 */
	public boolean doDelPickFormPicking(long id_pick)  throws ServiceException{
		
		RondasCtrl pick = new RondasCtrl();
		try {
			return pick.doDelPickFormPicking(id_pick);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	/**
	 * Inicializa el formulario de picking manual copiando los datos necesarios en las tablas del 
	 * formulario de picking.
	 * @param ProcIniciaFormPickingManualDTO datos
	 * @throws ServiceException, SystemException
	 */
	public void doIniciaFormPickingManual(ProcIniciaFormPickingManualDTO datos) 
		throws ServiceException, SystemException{

		RondasCtrl pick = new RondasCtrl();
		try {
			pick.doIniciaFormPickingManual(datos);
		} catch (RondasException e) {
			throw new ServiceException(e);
		}
	}
	/**
	 * Finzaliza el formulario de picking manual entregando los datos recopilados 
	 * al metodo que recepciona las rondas de picking usado por la PDA.
	 * @param ProcIniciaFormPickingManualDTO datos
	 * @throws ServiceException, SystemException
	 */
	public void doFinalizaFormPickingManual(ProcIniciaFormPickingManualDTO datos)  
		throws ServiceException, SystemException{

		RondasCtrl pick = new RondasCtrl();
		try {
			pick.doFinalizaFormPickingManual(datos);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Relaciona un detalle de picking al detalle de pedido
	 * @param datos
	 * @throws ServiceException
	 */
	public void doRelacionFormPick(DetalleFormPickingDTO datos,ProductosPedidoDTO datos_ped, FPickDTO fpick) throws ServiceException, SystemException{
		RondasCtrl pick = new RondasCtrl();
		try {
			 pick.doRelacionFormPick(datos,datos_ped,fpick);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	/**
	 * Actualiza detalle de pedido
	 * @param datos_ped
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setActFormPickDetPed(ProductosPedidoDTO datos_ped) throws ServiceException, SystemException{
		RondasCtrl pick = new RondasCtrl();
		
		try {
			return pick.setActFormPickDetPed(datos_ped);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Obtiene una lista con los productos faltantes pertenecientes a una ronda.
	 * @param id_ronda
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getFormPickFaltantes(long id_ronda) throws ServiceException, SystemException{
		RondasCtrl pick = new RondasCtrl();
		try {
			 return pick.getFormPickFaltantes(id_ronda);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Obtiene el listado de productos sustitutos asociados a una ronda
	 * @param id_ronda
	 * @param id_local
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getFormPickSustitutos(long id_ronda, long id_local) throws ServiceException, SystemException{
		RondasCtrl pick = new RondasCtrl();
		try {
			 return pick.getFormPickSustitutos(id_ronda,id_local);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Permite reversar un producto sustituto, Formulario de picking
	 * @param datos_ped
	 * @param fpick
	 * @param id_row
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean doReversaSustitutoFormPick(ProductosPedidoDTO datos_ped ,FPickDTO fpick, long id_row)
	throws ServiceException, SystemException{
		RondasCtrl pick = new RondasCtrl();
		try {
			 return pick.doReversaSustitutoFormPick(datos_ped,fpick,id_row);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Obtiene el detalle de un producto relacionado, formulario de picking.
	 * @param id_row
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public DetalleFormPickingDTO getRelacionFormPickById(long id_row) 
	throws ServiceException, SystemException{
		RondasCtrl pick = new RondasCtrl();
		try {
			 return pick.getRelacionFormPickById(id_row);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Obtiene la cantidad de productos relacionados segun id_fpick, formulario de picking.
	 * @param id_fpick
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public long getCountFormPickRelacion(long id_fpick)
	throws ServiceException, SystemException{
		RondasCtrl pick = new RondasCtrl();
		try {
			 return pick.getCountFormPickRelacion(id_fpick);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Permite reversar un picking a través del formulario de picking.
	 * @param id_pick
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean doReversaPickingFormPick(long id_pick)
	throws ServiceException, SystemException{
		RondasCtrl pick = new RondasCtrl();
		try {
			 return pick.doReversaPickingFormPick(id_pick);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	 
	/**
	 * Relaciona automaticamente todos los detalles de picking que coinciden
	 * en codigos de barra y cantidad con el detalle de pedido, formulario de picking.
	 * @param id_ronda
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void doRelacionAutomaticaFormPick(long id_ronda)
	throws ServiceException, SystemException{
		RondasCtrl pick = new RondasCtrl();
		try {
			pick.doRelacionAutomaticaFormPick(id_ronda);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}	
	}
	
	
	/**
	 * Permite saber si existe un pedido para una ronda determinada
	 * @param id_ronda
	 * @param id_pedido
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean isExisteRondaPedido(long id_ronda, long id_pedido)throws ServiceException, SystemException{
		RondasCtrl ped = new RondasCtrl();
		try {
			return  ped.isExistePedidoRonda(id_ronda, id_pedido);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	/**
	 * Obtiene un listado con las ops asociadas a una ronda
	 * @param id_ronda
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getPedidosByRonda(long id_ronda) throws ServiceException, SystemException{
		RondasCtrl pick = new RondasCtrl();
		try {
			 return pick.getPedidosByRonda(id_ronda);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Obtiene la jornada de despacho segun su id
	 * @param id_jornada
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public JorDespachoCalDTO getJornadaDespachoById(long id_jornada) throws ServiceException, SystemException{
		JornadasCtrl jd = new JornadasCtrl();
		JorDespachoCalDTO resultado;
		try {
			resultado = jd.getJornadaDespachoById(id_jornada);
		} catch (JornadasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		} catch (CalendarioDAOException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		 return resultado;
	}
	
	
	/**
	 * Obtiene listado de estados de trx. mp
	 * 
	 * @return List EstadoDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.EstadoDTO
	 */
	public List getEstadosTrxMp()
		throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getEstadosTrxMp();
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Obtiene la lista de comanda de verificacion de stock.
	 * 
	 * @param  id_jornada long 
	 * @param  id_sector (sector_local) 
	 * @return List ComandaVerifStockDTO
	 * @throws ServiceException
	 * @see    cl.bbr.jumbocl.pedidos.dto.ComandaVerifStockDTO
	 */
	public List getComandaVerifStock(long id_jornada,  long id_sector) 
	    throws ServiceException{
		JornadasCtrl jornadas = new JornadasCtrl();
		try {
			return jornadas.getComandaVerifStock(id_jornada, id_sector);
		} catch (JornadasException e) {
			e.printStackTrace();
			throw new ServiceException(e);			
		}	
	}

	public List getSeccionesLocalVerifStockByIdJornada(long id_jornada) 
    throws ServiceException, SystemException{
			JornadasCtrl jornadas = new JornadasCtrl();
			try {
				return jornadas.getSeccionesLocalVerifStockByIdJornada(id_jornada);
			} catch (JornadasException e) {
				e.printStackTrace();
				throw new ServiceException(e);			
			}	
	}
	
		/**
	 * Recupera listado de zonas finalizadas para las jornadas de la ronda.
	 * 
	 * @param id_ronda	Identificador único
	 * @return			Listado de zonas
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getZonasFinalizadas(long id_ronda) throws ServiceException, SystemException{
		RondasCtrl rondas = new RondasCtrl();
		try {
			return rondas.getZonasFinalizadas(id_ronda);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);			
		}	
} 
	
	/**
	 * Permite obtener el resumen de jornada de despacho
	 * @param criterio
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getResumenJorDespacho(DespachoCriteriaDTO criterio) 
    throws ServiceException, SystemException{
			DespachoCtrl desp = new DespachoCtrl();
			try {
				return desp.getResumenJorDespacho(criterio);
			} catch (DespachosException e) {
				e.printStackTrace();
				throw new ServiceException(e);			
			}	
	}	
	
	/**
	 * Permite aplicar el recalculo de promociones
	 * @param lst_promo listado de promociones
	 * @param lst_dp listado de detalle de pedido
	 * @param id_pedido
	 * @throws ServiceException
	 * @throws SystemException
	 */
		public void setAplicaRecalculo(List lst_promo,List lst_dp, long id_pedido) throws ServiceException, SystemException{
			PedidosCtrl promocion = new PedidosCtrl();
			try {
				promocion.setAplicaRecalculo(lst_promo,lst_dp,id_pedido);
			} catch (PedidosException e) {
				e.printStackTrace();
				throw new ServiceException(e);			
			}	
	    }
	
	 
	
	
	
	/**
	 * Permite obtener las promociones de los productos asociados a una ronda
	 * @param id_ronda
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getPromocionesRonda(long id_ronda) throws ServiceException, SystemException{
		RondasCtrl promo= new RondasCtrl();
		try {
			 return promo.getPromocionesRonda(id_ronda);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Permite saber si un producto acepta sustitutos, segun sus promociones
	 * @param id_detalle
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean promoPermiteSustitutos(long id_detalle)
	throws ServiceException, SystemException{
		RondasCtrl promo = new RondasCtrl();
		try {
			 return promo.promoPermiteSustitutos(id_detalle);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Permite saber si un producto acepta faltantes, segun sus promociones
	 * @param id_detalle
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean promoPermiteFaltantes(long id_detalle)
	throws ServiceException, SystemException{
		RondasCtrl promo = new RondasCtrl();
		try {
			 return promo.promoPermiteFaltantes(id_detalle);
		} catch (RondasException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Obtiene los productos CON promoción de un pedido especifico
	 * @param id_pedido
	 * @return List PedidoPromocionDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getPromocionPedidos(long id_pedido) throws  ServiceException, SystemException{
		PedidosCtrl promos = new PedidosCtrl();
		try{
			return promos.getPromocionPedidos(id_pedido);
		} catch(PedidosException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}	
	

	/**
	 * Recalcula las promociones del pedido
	 * @param id_pedido
	 * @param id_local
	 * @return List RecalculoPromocionesDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	//[20121113avc
	public List doRecalculoPromocion(long id_pedido, long id_local, Long colaborador, CuponDsctoDTO cddto, List cuponProds)  throws  ServiceException, SystemException{
	//]20121113avc
		PedidosCtrl promos = new PedidosCtrl();
		try{
			//se le pasa null a la transaccionalidad
			//[20121113avc
			return promos.doRecalculoPromocion(id_pedido, id_local, null, colaborador, cddto, cuponProds);
			//]20121113avc
		} catch(PedidosException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Entrega un listado de promociones utilizando un criterio de busqueda opcional
	 * dado por un id_promocion o un id_local 
	 * @param criteria
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	//FIXME Eliminar
	public List getPromocionesByCriteria(PromocionesCriteriaDTO criteria) throws ServiceException, SystemException{
		PedidosCtrl promos = new PedidosCtrl();
		try{
			return promos.getPromocionesByCriteria(criteria);
		} catch(PedidosException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	public List getPromociones() throws ServiceException, SystemException{
		PedidosCtrl promos = new PedidosCtrl();
		try{
			return promos.getPromociones();
		} catch(PedidosException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	public PromocionDTO getPromocion(int codigo) throws ServiceException, SystemException{
		PedidosCtrl promos = new PedidosCtrl();
		try{
			return promos.getPromocion(codigo);
		} catch(PedidosException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	
	/**
	 * Entrega el numero de registros del  listado de promociones utilizando un criterio de busqueda opcional
	 * dado por un id_promocion o un id_local
	 * @param criteria
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public long getPromocionesByCriteriaCount(PromocionesCriteriaDTO criteria) throws ServiceException, SystemException{
		PedidosCtrl promos = new PedidosCtrl();
		try{
			return promos.getPromocionesByCriteriaCount(criteria);
		} catch(PedidosException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		
	}
		
	/**
	 * Obtiene los datos de una promoción específica
	 * @param id_promocion
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public PromocionDTO getPromocionById(long id_promocion) throws  ServiceException, SystemException{
		PedidosCtrl promos = new PedidosCtrl();
		try{
			return promos.getPromocionById(id_promocion);
		} catch(PedidosException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	public List getCategoriasSapByPromocionSeccion(long id_local, int tipo) throws  ServiceException, SystemException{
		PedidosCtrl promos = new PedidosCtrl();
		try{
			return promos.getCategoriasSapByPromocionSeccion(id_local, tipo);
		} catch(PedidosException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * Obtiene los productos de una promoción. 
	 * @param codigo
	 * @return List ProductoPromocionDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getPromocionProductos(int codigo) throws  ServiceException, SystemException{
		PedidosCtrl promos = new PedidosCtrl();
		try{
			return promos.getPromocionProductos(codigo);
		} catch(PedidosException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * Permite modificar una promoción
	 * @param dto
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean updPromocion(PromocionDTO dto) throws  ServiceException, SystemException{
		PedidosCtrl promos = new PedidosCtrl();
		try{
			return promos.updPromocion(dto);
		} catch(PedidosException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene los medios de pago para promociones segun el medio de pado de jumbocl
	 * @param mp_jmcl
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public PromoMedioPagoDTO getMPPromoByMpJmcl(String mp_jmcl, int ncuotas) throws ServiceException, SystemException{
		PedidosCtrl promos = new PedidosCtrl();
		try{
			return promos.getMPPromoByMpJmcl(mp_jmcl,ncuotas);
		} catch(PedidosException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

    /**
     * @param fecha
     * @param semana
     * @param anio
     * @param idZona
     * @return
     */
    public CalendarioDespachoDTO getCalendarioDespachoByFecha(Date fecha, int semana, int anio, long idZona) throws ServiceException {
        CalendariosCtrl ctrl = new CalendariosCtrl();
        try {
            return ctrl.getCalendarioDespachoByFecha(fecha, semana, anio, idZona);
        } catch (CalendarioException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idPedido
     * @param sustitutosPorCategorias
     */
    public void updateCriterioSustitucionEnPedido(long idPedido, List sustitutosPorCategorias) throws ServiceException, SystemException{
        PedidosCtrl pedidoCtrl = new PedidosCtrl();
        try{
            pedidoCtrl.updateCriterioSustitucionEnPedido(idPedido,sustitutosPorCategorias);
        } catch(PedidosException e){
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idCliente
     * @param idProducto
     * @return
     */
    public SustitutoDTO getCriterioClientePorProducto(long idCliente, long idProducto) throws ServiceException{
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getCriterioClientePorProducto(idCliente, idProducto);
        }catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idProducto
     * @param idCliente
     * @param idCriterio
     * @param descCriterio
     */
    public void addModCriterioCliente(long idProducto, long idCliente, long idCriterio, String descCriterio) throws ServiceException{
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.addModCriterioCliente(idProducto, idCliente, idCriterio, descCriterio);
        }catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }        
    }

    /**
     * @param idPedido
     * @param idRonda
     * @return
     */
    public List getProductosPedidoRonda(long idPedido, long idRonda) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getProductosPedidoRonda(idPedido,idRonda);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @return
     */
    public List getGruposListado() throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getGruposListado();
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @return
     */
    public List getTiposGruposListado() throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getTiposGruposListado();
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param lg
     * @return
     */
    public long addGrupoLista(ListaGrupoDTO lg) throws ServiceException {       
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.addGrupoLista(lg);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param lg
     */
    public void modGrupoLista(ListaGrupoDTO lg) throws ServiceException {       
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.modGrupoLista(lg);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idObjeto
     * @return
     */
    public ListaGrupoDTO getGrupoListadoById(long idGrupoListado) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getGrupoListadoById(idGrupoListado);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idGrupoLista
     */
    public void delGrupoLista(long idGrupoLista) throws ServiceException {       
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.delGrupoLista(idGrupoLista);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idGrupoLista
     * @return
     */
    public List clienteListasEspeciales(long idGrupoLista) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.clienteListasEspeciales(idGrupoLista);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param hash
     * @return
     */
    public String addListasEspeciales(Hashtable hash) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.addListasEspeciales(hash);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idLista
     * @return
     */
    public List getGruposAsociadosLista(long idLista) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getGruposAsociadosLista(idLista);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idLista
     * @return
     */
    public List getGruposNoAsociadosLista(long idLista) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getGruposNoAsociadosLista(idLista);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idLista
     * @return
     */
    public UltimasComprasDTO getListaById(long idLista) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getListaById(idLista);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param lista
     * @param grupos
     */
    public void modLista(UltimasComprasDTO lista, String[] grupos) throws ServiceException {       
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.modLista(lista, grupos);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }        
    }

    /**
     * @param idLista
     * @param idGrupo
     */
    public void delListaEspecial(long idLista, long idGrupo) throws ServiceException {       
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.delListaEspecial(idLista, idGrupo);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idLista
     * @return
     */
    public List listaDeProductosByLista(long idLista) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.listaDeProductosByLista(idLista);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idTipoGrupo
     * @return
     */
    public ListaTipoGrupoDTO getTipoGrupoById(int idTipoGrupo) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getTipoGrupoById(idTipoGrupo);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idTipoGrupo
     * @return
     */
    public List getGruposDeListasByTipo(int idTipoGrupo) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getGruposDeListasByTipo(idTipoGrupo);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @return
     */
    public List localesRetiro() throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.localesRetiro();
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idLocal
     * @return
     */
    public LocalDTO getLocalRetiro(long idLocal) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getLocalRetiro(idLocal);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idZona
     * @return
     */
    public boolean zonaEsRetiroLocal(long idZona) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.zonaEsRetiroLocal(idZona);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idPedido
     * @param tipoDespacho
     * @return
     */
    public void modTipoDespachoDePedido(long idPedido, String tipoDespacho) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.modTipoDespachoDePedido(idPedido, tipoDespacho);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }


    /**
     * @param idPedido
     * @param tipoPicking
     * @return
     */
    public void modTipoPickingPedido(long idPedido, String tipoPicking) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.modTipoPickingPedido(idPedido, tipoPicking);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    
    /**
     * @return
     */
    public Date fechaActualBD() throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.fechaActualBD();
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param cliente_id
     * @return
     */
    public PedidoDTO getUltimaCompraClienteConDespacho(long cliente_id) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getUltimaCompraClienteConDespacho(cliente_id);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param cliente_id
     * @return
     */
    public PedidoDTO getUltimaCompraClienteConRetiro(long cliente_id) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getUltimaCompraClienteConRetiro(cliente_id);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param id_pedido
     * @return
     */
    public List getProductosSolicitadosById(long id_pedido) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getProductosSolicitadosById(id_pedido);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idMarco
     * @return
     */
    public ListaTipoGrupoDTO getTipoGrupoListadoById(long idMarco) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getTipoGrupoListadoById(idMarco);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param marco
     */
    public void addMarco(ListaTipoGrupoDTO marco) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.addMarco(marco);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param marco
     */
    public void modMarco(ListaTipoGrupoDTO marco) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.modMarco(marco);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param marco
     */
    public void delMarco(ListaTipoGrupoDTO marco) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.delMarco(marco);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idMarco
     * @return
     */
    public List getGruposByMarco(int idMarco) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getGruposByMarco(idMarco);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @return
     */
    public List getTiposGruposListadoActivos() throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getTiposGruposListadoActivos();
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param criterio
     * @return
     */
    public List getProductosCarruselPorCriterio(CriterioCarruselDTO criterio) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getProductosCarruselPorCriterio(criterio);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param prod
     */
    public long addEditProductoCarrusel(ProductoCarruselDTO prod) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.addEditProductoCarrusel(prod);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idProductoCarrusel
     * @return
     */
    public ProductoCarruselDTO getProductoCarruselById(long idProductoCarrusel) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getProductoCarruselById(idProductoCarrusel);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idProductoCarrusel
     */
    public void deleteProductoCarruselById(long idProductoCarrusel) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.deleteProductoCarruselById(idProductoCarrusel);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param usr
     * @param strLog
     */
    public void addLogCarrusel(UserDTO usr, String strLog) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.addLogCarrusel(usr, strLog);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @return
     */
    public List getProductosCarruselActivos() throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getProductosCarruselActivos();
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param criterio
     * @return
     */
    public double getCountProductosCarruselPorCriterio(CriterioCarruselDTO criterio) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getCountProductosCarruselPorCriterio(criterio);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param fecha
     * @return
     */
    public List getLogCarruselByFecha(String fecha) throws ServiceException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getLogCarruselByFecha(fecha);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idComuna
     * @return
     */
    public RegionDTO getRegionByComuna(long idComuna) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getRegionByComuna(idComuna);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @return
     */
    public List getRegiones() throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getRegiones();
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idRegion
     * @return
     */
    public List getComunasByRegion(int idRegion) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getComunasByRegion(idRegion);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idComuna
     * @return
     */
    public List getZonasByComuna(int idComuna) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getZonasByComuna(idComuna);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param ped
     */
    public long addPedidoExt(PedidoDTO ped) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.addPedidoExt(ped);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param rut
     * @return
     */
    public PedidoDTO getUltimoPedidoJumboVAByRut(long rut) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getUltimoPedidoJumboVAByRut(rut);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idFono
     * @return
     */
    public FonoTransporteDTO getFonoTransporteById(long idFono) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getFonoTransporteById(idFono);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idFono
     * @param idLocal
     * @return
     */
    public ChoferTransporteDTO getChoferTransporteById(long idChofer) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getChoferTransporteById(idChofer);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idPatente
     * @param idLocal
     * @return
     */
    public PatenteTransporteDTO getPatenteTransporteById(long idPatente) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getPatenteTransporteById(idPatente);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idEmpresaTransporte
     * @param idLocal
     * @return
     */
    public List getFonosDeTransporte(long idEmpresaTransporte, long idLocal) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getFonosDeTransporte(idEmpresaTransporte, idLocal);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idEmpresaTransporte
     * @param idLocal
     * @return
     */
    public List getChoferesDeTransporte(long idEmpresaTransporte, long idLocal) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getChoferesDeTransporte(idEmpresaTransporte, idLocal);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idEmpresaTransporte
     * @param idLocal
     * @return
     */
    public List getPatentesDeTransporte(long idEmpresaTransporte, long idLocal) throws ServiceException, SystemException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getPatentesDeTransporte(idEmpresaTransporte, idLocal);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param criterio
     * @return
     */
    public List getDespachosParaMonitorByCriteria(DespachoCriteriaDTO criterio) throws ServiceException{
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            return despacho.getDespachosParaMonitorByCriteria(criterio);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @return
     */
    public List getJornadasDespachoParaFiltro(long idLocal, String fecha, long idZona) throws ServiceException{
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            return despacho.getJornadasDespachoParaFiltro(idLocal, fecha, idZona);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }
    
    public List getJornadasDespacho(int localId, Date fechaIni, Date fechaFin) throws ServiceException{
       DespachoCtrl despacho = new DespachoCtrl();
       try {
           return despacho.getJornadasDespacho(localId,  fechaIni,  fechaFin);
       } catch (DespachosException e) {
           e.printStackTrace();
           throw new ServiceException(e);
       }
   }
     

    /**
     * @param ruta
     */
    public long addRuta(RutaDTO ruta) throws ServiceException{
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            return despacho.addRuta(ruta);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param log
     */
    public void addLogRuta(LogRutaDTO log) throws ServiceException{
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            despacho.addLogRuta(log);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idPedido
     * @param idRuta
     * @return
     */
    public int addPedidoRuta(long idPedido, long idRuta) throws ServiceException{
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            return despacho.addPedidoRuta(idPedido, idRuta);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idLocal
     * @return
     */
    public List getRutasDisponibles(long idLocal) throws ServiceException{
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            return despacho.getRutasDisponibles(idLocal);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idRuta
     * @return
     */
    public RutaDTO getRutaById(long idRuta) throws ServiceException{
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            return despacho.getRutaById(idRuta);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param criterio
     * @return
     */
    public List getRutasByCriterio(RutaCriterioDTO criterio) throws ServiceException{
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            return despacho.getRutasByCriterio(criterio);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param criterio
     * @return
     */
    public double getCountRutasByCriterio(RutaCriterioDTO criterio) throws ServiceException{
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            return despacho.getCountRutasByCriterio(criterio);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @return
     */
    public List getEstadosRuta() throws ServiceException{
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            return despacho.getEstadosRuta();
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idZona
     * @param fecha
     * @return
     */
    public List getJornadasDespachoDisponiblesByZona(long idZona, String fecha, String tipoPedido) throws ServiceException{
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            return despacho.getJornadasDespachoDisponiblesByZona(idZona, fecha, tipoPedido);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @return
     */
    public List getLogRuta(long idRuta) throws ServiceException{
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            return despacho.getLogRuta(idRuta);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idRuta
     * @return
     */
    public List getPedidosByRuta(long idRuta) throws ServiceException{
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            return despacho.getPedidosByRuta(idRuta);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idPedido
     * @param idRuta
     * @return
     */
    public int delPedidoRuta(long idPedido) throws ServiceException{
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            return despacho.delPedidoRuta(idPedido);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param estado
     * @param idRuta
     * @return
     */
    public void setEstadoRuta(int estado, long idRuta) throws ServiceException{
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            despacho.setEstadoRuta(estado, idRuta);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idRuta
     */
    public void liberarPedidosByRuta(long idRuta) throws ServiceException{
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            despacho.liberarPedidosByRuta(idRuta);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param id_pedido
     * @return
     */
    public List getDetPickingToHojaDespacho(long id_pedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try{
            return pedidos.getDetPickingToHojaDespacho(id_pedido);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idRuta
     * @param cantBins
     */
    public void actualizaCantBinsRuta(long idRuta) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try{
            pedidos.actualizaCantBinsRuta(idRuta);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param id_pedido
     * @param operacion
     */
    public void modificaVecesEnRutaDePedido(long idPedido, String operacion) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try{
            pedidos.modificaVecesEnRutaDePedido(idPedido, operacion);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public List getDocumentosByPedido(long idPedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try{
            return pedidos.getDocumentosByPedido(idPedido);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @return
     */
    public List getResponsablesDespachoNC() throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try{
            return pedidos.getResponsablesDespachoNC();
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @return
     */
    public List getMotivosDespachoNC() throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try{
            return pedidos.getMotivosDespachoNC();
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param pedExt
     */
    public void updPedidoFinalizado(PedidoExtDTO pedExt) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try{
            pedidos.updPedidoFinalizado(pedExt);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idRuta
     * @return
     */
    public int getCountPedidoNoFinalizadosByRuta(long idRuta) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try{
            return pedidos.getCountPedidoNoFinalizadosByRuta(idRuta);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idPedido
     */
    public void updPedidoReagendado(long idPedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try{
            pedidos.updPedidoReagendado(idPedido);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param criterio
     * @return
     */
    public List getPedidosPendientesByCriterio(DespachoCriteriaDTO criterio) throws ServiceException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getPedidosPendientesByCriterio(criterio);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param criterio
     * @return
     */
    public double getCountPedidosPendientesByCriterio(DespachoCriteriaDTO criterio) throws ServiceException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getCountPedidosPendientesByCriterio(criterio);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idPedido
     * @param idMotivo
     * @param idResponsable
     */
    public void addMotivoResponsableReprogramacion(long idPedido, long idMotivo, long idResponsable, long idJornadaDespachoAnterior, long idUsuario) throws ServiceException {        
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.addMotivoResponsableReprogramacion(idPedido,idMotivo,idResponsable,idJornadaDespachoAnterior,idUsuario);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public RutaDTO getRutaByPedido(long idPedido) throws ServiceException{
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            return despacho.getRutaByPedido(idPedido);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @return
     */
    public List getMotivosDespachoNCAll() throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getMotivosDespachoNCAll();
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @return
     */
    public List getResponsablesDespachoNCAll() throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try{
            return pedidos.getResponsablesDespachoNCAll();
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idMotivo
     */
    public void delMotivoNCById(long idMotivo) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.delMotivoNCById(idMotivo);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param motivo
     */
    public void addMotivoNC(ObjetoDTO motivo) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.addMotivoNC(motivo);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param motivo
     */
    public void modMotivoNC(ObjetoDTO motivo) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.modMotivoNC(motivo);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idResponsable
     */
    public void delResponsableNCById(long idResponsable) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.delResponsableNCById(idResponsable);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param motivo
     */
    public void addResponsableNC(ObjetoDTO responsable) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.addResponsableNC(responsable);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param motivo
     */
    public void modResponsableNC(ObjetoDTO responsable) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.modResponsableNC(responsable);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idEmpresa
     */
    public void delEmpresaTransporteById(long idEmpresa) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.delEmpresaTransporteById(idEmpresa);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param empresa
     */
    public void addEmpresaTransporte(EmpresaTransporteDTO empresa) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.addEmpresaTransporte(empresa);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param empresa
     */
    public void modEmpresaTransporte(EmpresaTransporteDTO empresa) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.modEmpresaTransporte(empresa);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @return
     */
    public List getEmpresasTransporteAll() throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getEmpresasTransporteAll();
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idLocal
     * @return
     */
    public List getPatentesDeTransporteByLocal(long idLocal) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getPatentesDeTransporteByLocal(idLocal);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idLocal
     * @return
     */
    public List getFonosDeTransporteByLocal(long idLocal) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getFonosDeTransporteByLocal(idLocal);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idLocal
     * @return
     */
    public List getChoferesDeTransporteByLocal(long idLocal) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getChoferesDeTransporteByLocal(idLocal);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idPatente
     */
    public void delPatenteTransporteById(long idPatente) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.delPatenteTransporteById(idPatente);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param patente
     */
    public long addPatenteTransporte(PatenteTransporteDTO patente) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.addPatenteTransporte(patente);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param patente
     */
    public void modPatenteTransporte(PatenteTransporteDTO patente) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.modPatenteTransporte(patente);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idFono
     */
    public void delFonoTransporteById(long idFono) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.delFonoTransporteById(idFono);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param fono
     */
    public long addFonoTransporte(FonoTransporteDTO fono) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.addFonoTransporte(fono);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param fono
     */
    public void modFonoTransporte(FonoTransporteDTO fono) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.modFonoTransporte(fono);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idChofer
     */
    public void delChoferTransporteById(long idChofer) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.delChoferTransporteById(idChofer);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param chofer
     */
    public long addChoferTransporte(ChoferTransporteDTO chofer) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.addChoferTransporte(chofer);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param chofer
     */
    public void modChoferTransporte(ChoferTransporteDTO chofer) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.modChoferTransporte(chofer);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @return
     */
    public List getEmpresasTransporteActivas() throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getEmpresasTransporteActivas();
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idChofer
     * @param descripcion
     * @param idUsuario
     */
    public void addLogChoferTransporte(long idChofer, String descripcion, long idUsuario) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.addLogChoferTransporte(idChofer,descripcion,idUsuario);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idFono
     * @param descripcion
     * @param idUsuario
     */
    public void addLogFonoTransporte(long idFono, String descripcion, long idUsuario) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.addLogFonoTransporte(idFono,descripcion,idUsuario);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idPatente
     * @param descripcion
     * @param idUsuario
     */
    public void addLogPatenteTransporte(long idPatente, String descripcion, long idUsuario) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.addLogPatenteTransporte(idPatente,descripcion,idUsuario);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public JornadaDTO getJornadaDespachoOriginalDePedidoReprogramado(long idPedido) throws ServiceException {
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            return despacho.getJornadaDespachoOriginalDePedidoReprogramado(idPedido);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public List getReprogramacionesByPedido(long idPedido) throws ServiceException {
        DespachoCtrl despacho = new DespachoCtrl();
        try {
            return despacho.getReprogramacionesByPedido(idPedido);
        } catch (DespachosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idPedido
     * @return
     * @throws SystemException
     */
    public List getProductosTodasTrxByPedido(long idPedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getProductosTodasTrxByPedido(idPedido);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idProducto
     * @param porcion
     * @param unidadPorcion
     */
    public void modPorcionProducto(long idProducto, double porcion, long unidadPorcion) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.modPorcionProducto(idProducto,porcion,unidadPorcion);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idProducto
     * @param idPila
     */
    public void delPilaProducto(long idProducto, long idPila) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.delPilaProducto(idProducto,idPila);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param pilaProd
     */
    public void addPilaProducto(PilaProductoDTO pilaProd) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.addPilaProducto(pilaProd);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @return
     */
    public List getUnidadesNutricionales() throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getUnidadesNutricionales();
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idProducto
     * @return
     */
    public List getPilasNutricionalesByProductoFO(long idProducto) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getPilasNutricionalesByProductoFO(idProducto);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @return
     */
    public List getPilasNutricionales() throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getPilasNutricionales();
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public PilaUnidadDTO getPilaUnidadById(long idUnidadPila) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getPilaUnidadById(idUnidadPila);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @return
     */
    public boolean verificaHoraCompra(long idJdespacho, String tipoDespacho) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.VerificaHoraCompra(idJdespacho, tipoDespacho);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @return
     */
    public boolean verificaAlertaValidacion(long id_pedido, String key_validacion) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.verificaAlertaValidacion(id_pedido, key_validacion);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }
    /**
     * @return
     */
    public boolean verificaPrimeraCompraRetiroEnLocal(long id_pedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.verificaPrimeraCompraRetiroEnLocal(id_pedido);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }
    
    /**
     * @param diasCalendario
     * @param idZona
     * @return
     */
    public List getCalendarioDespachoByDias(int diasCalendario, long idZona) throws ServiceException {
        CalendariosCtrl ctrl = new CalendariosCtrl();
        try {
            return ctrl.getCalendarioDespachoByDias(diasCalendario,idZona);
        } catch (CalendarioException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param diasCalendario
     * @param idZona
     * @return
     */
    public List getCalendarioDespachoByDias(int diasCalendario, long idZona, long id_jpicking, int cantProductos) throws ServiceException {
        CalendariosCtrl ctrl = new CalendariosCtrl();
        try {
            return ctrl.getCalendarioDespachoByDias(diasCalendario, idZona, id_jpicking, cantProductos);
        } catch (CalendarioException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idZona
     * @param fechaDespacho
     * @param cantProductos
     * @return
     */
    public long getJornadaDespachoMayorCapacidad(long idZona, String fechaDespacho, long cantProductos) throws ServiceException {
        CalendariosCtrl ctrl = new CalendariosCtrl();
        try {
            return ctrl.getJornadaDespachoMayorCapacidad(idZona,fechaDespacho,cantProductos);
        } catch (CalendarioException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }
    
    /**
     * @param idPedido
     * @param esClienteConfiable
     */
    public void ingresarPedidoASistema(long idPedido, boolean esClienteConfiable, long cliente) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
             pedidos.ingresarPedidoASistema(idPedido, esClienteConfiable, cliente);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param webpayDTO
     */
    public void webpaySave(WebpayDTO webpayDTO) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.webpaySave(webpayDTO);            
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public int webpayMonto(int idPedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
             return pedidos.webpayMonto(idPedido);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public WebpayDTO webpayGetPedido(long idPedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
             return pedidos.webpayGetPedido(idPedido);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public int webpayGetEstado(int idPedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
             return pedidos.webpayGetEstado(idPedido);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    /** Entregala informacion de un pedido para enviar por boton de pago
     * @param idPedido
     * @return
     */
    public BotonPagoDTO botonPagoGetPedido(long idPedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
             return pedidos.botonPagoGetPedido(idPedido);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }
    
    /**
     * Inserta un registro del resultado del pago con Boton de Pago CAT
     * 
     * @param BotonPagoDTO
     */
    public void botonPagoSave(BotonPagoDTO bp) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
             pedidos.botonPagoSave(bp);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }    

    /** Entregala informacion de un registro de boton de pago segun el id del pedido
     * @param idPedido
     * @return
     */
    public BotonPagoDTO botonPagoGetByPedido(long idPedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
             return pedidos.botonPagoGetByPedido(idPedido);
        } catch (PedidosException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }


	/**
	 * @return
	 */
	public HashMap obtenerLstOPByTBK() throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.obtenerLstOPByTBK();
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
	}

	/**
	 * @param id_pedido
	 * @param id_estado
	 */
	public void setModEstadoTrxOP(long id_pedido, int id_estado) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
             pedidos.setModEstadoTrxOP(id_pedido, id_estado);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
	}

	public WebpayDTO getWebpayByOP(long id_pedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
             return pedidos.getWebpayByOP(id_pedido);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
	}
	
	/**
	 * Actualiza registro de Boton de pago CAT
	 */
	public boolean updateNotificacionBotonPago(BotonPagoDTO bp) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
             return pedidos.updateNotificacionBotonPago(bp);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
	}

    /**
     * @param idComprador
     * @return
     */
    public List getPedidosPorPagar(long idComprador) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
             return pedidos.getPedidosPorPagar(idComprador);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idPedido
     */
    public void actualizaSecuenciaPago(long idPedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.actualizaSecuenciaPago(idPedido);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public boolean pedidoEsFonoCompra(int idPedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.pedidoEsFonoCompra(idPedido);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idPedido
     */
    public void ingresarPedidoVteASistema(long idPedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.ingresarPedidoVteASistema(idPedido);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }
    
	/**
	 * @param id_pedido
	 * @return
	 */
	public boolean setModFlagWebpayByOP(long id_pedido, String flag)  throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.setModFlagWebpayByOP(id_pedido, flag);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
	}

    /**
     * @param idPedido
     * @return
     */
    public List getProductosPickeadosByIdPedido(long idPedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getProductosPickeadosByIdPedido(idPedido);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public List getSustitutosYPesablesByPedidoId(long idPedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getSustitutosYPesablesByPedidoId(idPedido);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idPedido
     * @param datosCambiados
     */
    public void cambiarPreciosPickeados(long idPedido, String datosCambiados) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.cambiarPreciosPickeados(idPedido, datosCambiados);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @return
     */
    public List getPedidosEnTransicionTEMP(long idLocal) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getPedidosEnTransicionTEMP(idLocal);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param pedido
     */
    public void purgaPedidoPreIngresado(PedidoDTO pedido, long idCliente) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.purgaPedidoPreIngresado(pedido, idCliente);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idProductoFO
     * @param idPedido
     * @param idDetallePedido
     * @param cantidad
     */
    public void modProductoDePedido(long idPedido, long idDetallePedido, double cantidad) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.modProductoDePedido(idPedido, idDetallePedido, cantidad);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param fcIni
     * @param fcFin
     * @param local
     * @param usuario
     * @return
     */
    public List getInformeModificacionDePrecios(String fcIni, String fcFin, long local, String usuario) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getInformeModificacionDePrecios(fcIni, fcFin, local, usuario);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param usuario
     * @return
     */
    public List getInformeProductosSinStock() throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getInformeProductosSinStock();
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }
    
    
    /**
     * @return
     */
    public List getUsuariosInformeModPrecios() throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getUsuariosInformeModPrecios();
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param pedido
     */
    public boolean anulacionAceleradaCAT(PedidoDTO pedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.anulacionAceleradaCAT(pedido);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idPedido
     * @param marcar
     */
    public void marcaAnulacionBoletaEnLocal(long idPedido, boolean marcar) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.marcaAnulacionBoletaEnLocal(idPedido, marcar);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @return
     */
    public List getPedidosRechazadosErroneamenteTBK(int dias, int minutos) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getPedidosRechazadosErroneamenteTBK(dias, minutos);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }
    
    public List getPedidosRechazadosErroneamenteCAT(int dias, int minutos) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.getPedidosRechazadosErroneamenteCAT(dias, minutos);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param idProducto
     * @param idLocal
     * @return
     */
    public boolean existeProductoEnLocal(long idProducto, long idLocal) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.existeProductoEnLocal(idProducto, idLocal);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }
    
    //Maxbell - Mejoras al catalogo interno 2014/06/30
    public ProductoStockDTO productoTieneStockEnLocal(long idProducto, long idLocal) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.productoTieneStockEnLocal(idProducto, idLocal);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }
    
    /**
     * Metodo que se encarga de recuperar los productos sustitutos que superan el margen de sustitución 
     * Para ser sacados de los bin
     * @return <code>List</code> Listado de <code>ProductoSobreMargenDTO</code> con los productos sustitutos sobre umbral
     * @throws ServiceException
     * @throws SystemException
     */
    public List getProductosSustitutosSobreMargen(long id_local)throws ServiceException, SystemException {
    	PedidosCtrl pedidos = new PedidosCtrl();
    	try{
    		return pedidos.getProductosSustitutosSobreMargen(id_local);
    	} catch(PedidosException e){
    		throw new ServiceException(e.getMessage());
    	}
    }
    
    /**
     * Metodo que se encarga de eliminar el producto sustituto sobre margen
     * @param idEliminar id del detalle a eliminar
     * @return boolean true en caso de exito false en caso de error
     * @throws ServiceException
     */
    public boolean eliminarProductoSustitutoSobreMargen(long idEliminar)
    throws ServiceException{
    	
    	PedidosCtrl pedidos = new PedidosCtrl();
    	try {
    		return pedidos.eliminarProductosSustitutosSobreMargen(idEliminar);
    	} catch (PedidosException e) {
    		e.printStackTrace();
    		throw new ServiceException(e);
    	}
}

	/**
     * @param pedidosConError
     */
    public void cambiaEstadoWebPays(long idPedido) throws ServiceException, SystemException {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            pedidos.cambiaEstadoWebPays(idPedido);
        } catch (PedidosException e) {
            throw new ServiceException(e.getMessage());
        }
    }
    
	public boolean modFechaOP(long id_pedido, String paramFecha, String fechaOld,String userLogin) throws ServiceException, DAOException   {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
            return pedidos.modFechaOP(id_pedido,paramFecha, fechaOld ,userLogin);
        } catch (PedidosException e) {
        	throw new ServiceException(e.getMessage());
        }
	}
	
	public void insertaTrackingOP(long id_pedido, String user, String mensajeLog) throws ServiceException, DAOException, PedidosDAOException   {
        PedidosCtrl pedidos = new PedidosCtrl();
        try {
             pedidos.insertaTrackingOP(id_pedido, user,mensajeLog);
        } catch (PedidosException e) {
        	throw new ServiceException(e.getMessage());
        }
	}
	
	/**
	 * Obtiene lista de logs de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return List LogPedidoDTO
	 * @throws ServiceException
	 * @throws PedidosDAOException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO
	 */
	public List getMailPM()
		throws ServiceException, PedidosDAOException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			return pedidos.getMailPM();
		} catch (PedidosException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}

	}	
	
	/**
	 * 
	 * @param idPedido
	 * @return
	 * @throws ServiceException
	 */
	
	public PedidoDTO getValidaCuponYPromocionPorIdPedido( long idPedido ) throws ServiceException {
		
		PedidoDTO pedido = null;
		PedidosCtrl pedidos = new PedidosCtrl();
		
		try {
			
			pedido = pedidos.getValidaCuponYPromocionPorIdPedido( idPedido );
		
		} catch ( PedidosException e ) {
		
			e.printStackTrace();
			throw new ServiceException( e );
		
		}
		
		return pedido;
	
	}
	public int getIdCuponByIdPedido( long idPedido ) throws ServiceException {
		int idCupon = 0;
		PedidosCtrl pedidos = new PedidosCtrl();
		try {
			idCupon = pedidos.getIdCuponByIdPedido(idPedido);
		} catch ( PedidosException e ) {
			e.printStackTrace();
			throw new ServiceException( e );
		}
		return idCupon;	
	}
	
	/**
	 * 
	 * @param id_pedido
	 * @return
	 * @throws ServiceException
	 */
	public List getDescuentosAplicados( long id_pedido ) throws ServiceException{
		
		PedidosCtrl pedidos = new PedidosCtrl();
		
		try {
	
			return pedidos.getDescuentosAplicados( id_pedido );
			
		} catch ( PedidosException e ) {
			
			e.printStackTrace();
			throw new ServiceException( e );
		
		}
	
	}
	
	//Maxbell - Inconsistencias v3
    public int actualizarCapacidadOcupadaPicking(long id_jpicking) throws ServiceException{
    	try{
    	int registrosActualizados = 0;
    	PedidosCtrl pedidos = new PedidosCtrl();
    	registrosActualizados = pedidos.actualizarCapacidadOcupadaPicking(id_jpicking);
    	return registrosActualizados;
    	}catch (JornadasDAOException e){
    		throw new ServiceException(e);
    	}
    	
    }
    
	public int calcularDiferenciaJornada(long id_jpicking) {
		int cantidad = 0;
		JornadasCtrl jornadas = new JornadasCtrl();
		try {
			cantidad = jornadas.calcularDiferenciaJornada(id_jpicking);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cantidad;
	}
    
  //Excesos modificiado por Alvaro Gutierrez	
  		/**
  		 * Metodo que consulta si una OP tiene Exceso 
  		 * El monto de exceso esta sobre el total pickeado y tambien sobre los productos, se debe respetar el monto solicitado del producto
  		 * 
  		 * @param id_pedido:id del pedido a consultar
  		 * @return boolean true en caso de exceso o false en caso que OP no tenga exceso
  		 * @throws ServiceException
  		 */
  	    public boolean isOpConExceso(long idPedido) throws ServiceException   {
  	       ExcesoCtrl exceso = new ExcesoCtrl();
  	        try {
  	        	return exceso.isOpConExceso(idPedido);
  	        } catch (SystemException e) {
  	        	throw new ServiceException(e.getMessage());
  	        }
  	    }
  	    

  	    /**
  	     * @param idPedido
  	     * @param excedido
  	     */
  	    public void modPedidoExcedido(long idPedido, boolean excedido) throws ServiceException, SystemException {
  	        ExcesoCtrl exceso = new ExcesoCtrl();
  	        try {
  	        	exceso.modPedidoExcedido(idPedido, excedido);
  	        } catch (SystemException e) {
  	            throw new ServiceException(e.getMessage());
  	        }
  	    }

  		public boolean isExcesoCorreccionAutomatico(long idPedido)
  				throws ServiceException {
  			// TODO Apéndice de método generado automáticamente
  			ExcesoCtrl exceso = new ExcesoCtrl();
  			try {
  				return exceso.isExcesoCorreccionAutomatico(idPedido);
  			} catch (SystemException e) {
  				throw new ServiceException(e.getMessage());
  			}
  		}

  		public boolean corrigeExcesoOP(long idPedido) throws ServiceException {
  			// TODO Apéndice de método generado automáticamente
  			ExcesoCtrl exceso = new ExcesoCtrl();
  			try {
  				return exceso.corrigeExcesoOP(idPedido);
  			} catch (SystemException e) {
  				throw new ServiceException(e.getMessage());
  			}
  		}

  		public boolean isActivaCorreccionAutomatica() throws ServiceException {
  			// TODO Apéndice de método generado automáticamente
  			ExcesoCtrl exceso = new ExcesoCtrl();
  			try {
  				return exceso.isActivaCorreccionAutomatica();
  			} catch (SystemException e) {
  				throw new ServiceException(e.getMessage());
  			}
  		}

  		public List getIdDetalleSolicitadoConExceso(long idPedido) throws ServiceException {
  			// TODO Apéndice de método generado automáticamente
  			ExcesoCtrl exceso = new ExcesoCtrl();
  			try {
  				return exceso.getIdDetalleSolicitadoConExceso(idPedido);
  			} catch (SystemException e) {
  				throw new ServiceException(e.getMessage());
  			}
  		}

  		public boolean isOpConExceso(long idPedido, boolean isCorrecionActiva) throws ServiceException {
  			// TODO Apéndice de método generado automáticamente
  			ExcesoCtrl exceso = new ExcesoCtrl();
  			try {
  				return exceso.isOpConExceso(idPedido,isCorrecionActiva);
  			} catch (SystemException e) {
  				throw new ServiceException(e.getMessage());
  			}
  		}
  		
  		/**(Catalogo Externo) - NSepulveda
  	     * Metodo que se encarga de recuperar la categoria padre, intermedia y terminal del ultimo producto 
  	     * agregado al carro de compras. 
  	     * @param idProducto <code>long<code> Identificador del producto.
  	     * @return <code>ProductoCatalogoExternoDTO</code>
  	     * @throws ServiceException
  	     * @throws SystemException
  	     */
  	    public ProductoCatalogoExternoDTO getCatUltimoProductoCatalogoExterno(long idProducto) throws ServiceException, SystemException {
  	    	PedidosCtrl pedidos = new PedidosCtrl();
  	    	
  	    	try{
  	    		return pedidos.getCatUltimoProductoCatalogoExterno(idProducto);
  	    	}catch (PedidosException e) {
  	            throw new ServiceException(e.getMessage());
  	        }
  	    }
  	    
  	    /**(Catalogo Externo) - NSepulveda
  	     * Metodo que se encarga de validar los productos solicitados desde el catalogo externo 
  	     * para posteriormente agregarlos al carro de compras.
  	     * @param listCatalogoExt <code>List</code> Listado de productos a validar.
  	     * @return <code>List</code> Listado de <code>ProductoCatalogoExternoDTO</code> con los productos validados.
  	     * @throws ServiceException
  	     * @throws SystemException
  	     */
  	    public List getValidacionProductosCatalogoExterno(List listCatalogoExt) throws ServiceException, SystemException {
  	    	PedidosCtrl pedidos = new PedidosCtrl();
  	    	
  	    	try{
  	    		return pedidos.getValidacionProductosCatalogoExterno(listCatalogoExt);
  	    	} catch (PedidosException e) {
  	            throw new ServiceException(e.getMessage());
  	        }
  	    }
  	    

  	    public ProductosPedidoDTO getDetalleProductoSolicitado(long idPedido,long idDetalle) throws ServiceException {
  	    	// TODO Apéndice de método generado automáticamente
  			ExcesoCtrl exceso = new ExcesoCtrl();
  			try {
				return exceso.getDetalleProductoSolicitado(idPedido,idDetalle);
			} catch (SystemException e) {
				 throw new ServiceException(e.getMessage());
			}
		}

		public List getDetallePickingByIdDetalle(long idDetalle, long idPedido) throws ServiceException {
			// TODO Apéndice de método generado automáticamente
			ExcesoCtrl exceso = new ExcesoCtrl();
  			try {
				return exceso.getDetallePickingByIdDetalle(idDetalle, idPedido);
			} catch (SystemException e) {
				throw new ServiceException(e.getMessage());
			}
		}

		public boolean updatePrecioPickXPrecioSegunCantidad(long idDetalle, long idPedido) throws ServiceException {
			// TODO Apéndice de método generado automáticamente
			ExcesoCtrl exceso = new ExcesoCtrl();
  			try {
				return exceso.updatePrecioPickXPrecioSegunCantidad(idDetalle, idPedido);
			} catch (SystemException e) {
				throw new ServiceException(e.getMessage());
			}
		}

		public boolean deletePickingByIdDpicking(long idPedido, long idDpicking) throws ServiceException {
			// TODO Apéndice de método generado automáticamente
			ExcesoCtrl exceso = new ExcesoCtrl();
  			try {
				return exceso.deletePickingByIdDpicking(idPedido, idDpicking);
			} catch (SystemException e) {
				throw new ServiceException(e.getMessage());
			}
		}

		public List getTrackingExcesoByOP(long idPedido)throws ServiceException {
			// TODO Apéndice de método generado automáticamente
			ExcesoCtrl exceso = new ExcesoCtrl();
  			try {
				return exceso.getTrackingExcesoByOP(idPedido);
			} catch (SystemException e) {
				throw new ServiceException(e.getMessage());
			}
		}

		public boolean CorregirPrecioSegúnPrecioSolicitado(long idDetalle, long idPedido) throws ServiceException {
			// TODO Apéndice de método generado automáticamente
			ExcesoCtrl exceso = new ExcesoCtrl();
  			try {
				return exceso.CorregirPrecioSegúnPrecioSolicitado(idDetalle, idPedido);
			} catch (SystemException e) {
				throw new ServiceException(e.getMessage());
			}
		} 	
		public boolean ajustarCantidadSolicitada(long idDetalle, long idPedido) throws ServiceException {
			// TODO Apéndice de método generado automáticamente
			ExcesoCtrl exceso = new ExcesoCtrl();
  			try {
				return exceso.ajustarCantidadSolicitada(idDetalle, idPedido);
			} catch (SystemException e) {
				throw new ServiceException(e.getMessage());
			}
		} 	
		
		//oPedido.getId_pedido(), idJdespachoNueva, idJpickingNueva, idLocalNuevo, precioNuevo		
		public boolean doReagendaDespachoLocal(PedidoDTO oPedido, JorDespachoCalDTO oJorDespachoNuevaCalDTO, LocalDTO localDtoNuevo, double precioNuevo,boolean isRetiroLocal, long direccionId, boolean modificarJPicking)
				throws ServiceException, SystemException{
				
				CalendariosCtrl ctrl = new CalendariosCtrl();
				try {
					//ctrl.doReagendaDespacho(id_pedido, id_jdespacho, sobrescribeprecio, precio, usr_login, modificarJPicking, modificarPrecio );
				    if( ctrl.doReagendaDespachoLocal(oPedido, oJorDespachoNuevaCalDTO, localDtoNuevo, precioNuevo, isRetiroLocal, direccionId, modificarJPicking)){
				    		ctrl.doActualizarCapacidadReagendaDespachoLocal(oPedido, oJorDespachoNuevaCalDTO, modificarJPicking);
				    		return true;
				    }
				    return false;
				} catch (DespachosException e) {
					//e.printStackTrace();
					throw new ServiceException(e);
				}
		}
		
		
		 public DireccionMixDTO getDireccionIniciaSessionCliente(long idCliente)  throws ServiceException{
			 
		      PedidosCtrl pedidos = new PedidosCtrl();
		        try {
		            return pedidos.getDireccionIniciaSessionCliente(idCliente);
		        } catch (PedidosException e) {
		        	throw new ServiceException(e.getMessage());
		        }
		 }
		 
		 //DESDE FO
		 
		 
		 /**
			 * Ingresa una nueva compra histórica WEB.
			 * 
			 * @param nombre		Nombre de la compra histórica
			 * @param cliente_id	Identificador único del cliente
			 * @param unidades		Cantidad de productos de la compra
			 * @param pedido_id		Identificador único del pedido
			 * @throws ServiceException
			 */
			public void setCompraHistorica(String nombre, long cliente_id, long unidades, long pedido_id) throws ServiceException {

				PedidosCtrl dirctr = new PedidosCtrl();

				try {
					dirctr.setCompraHistorica( nombre, cliente_id, unidades, pedido_id);
				} catch (PedidosException ex) {
					logger.error( "Problemas con controles de pedidos", ex);
					throw new ServiceException(ex);
				}
				
			}
			public long setCompraHistoricaForMobile(String nombre, long cliente_id, ArrayList productos) throws ServiceException {

				PedidosCtrl dirctr = new PedidosCtrl();

				try {
					return dirctr.setCompraHistoricaForMobile( nombre, cliente_id, productos);
				} catch (PedidosException ex) {
					logger.error( "Problemas con controles de pedidos", ex);
					throw new ServiceException(ex);
				}
				
			}

			/**
			 * Actualiza datos de una compra histórica WEB.
			 * 
			 * @param nombre		Nombre de la compra histórica
			 * @param id_cliente	Identificador único del cliente
			 * @param unidades		Cantidad de productos de la compra
			 * @param pedido_id		Identificador único del pedido
			 * @param id_lista		Identificador único de la compra histórica a modificar
			 * @throws ServiceException
			 */
			public void updateCompraHistorica(String nombre, long id_cliente, long unidades, long pedido_id, long id_lista) throws ServiceException {

				PedidosCtrl dirctr = new PedidosCtrl();

				try {
					dirctr.updateCompraHistorica( nombre, id_cliente, unidades, pedido_id, id_lista);
				} catch (PedidosException ex) {
					logger.error( "Problemas con controles de pedidos", ex);
					throw new ServiceException(ex);
				}		
				
			}

			/**
			 * Actualiza el ranking de ventas de los productos de un pedido 
			 * 
			 * @param id_cliente	id_cliente para el cual se actualiza el ranking
			 * @throws ServiceException
			 */	
			public void updateRankingVentas( long id_cliente ) throws ServiceException {

				PedidosCtrl dirctr = new PedidosCtrl();

				try {
					dirctr.updateRankingVentas( id_cliente );
				} catch (PedidosException ex) {
					logger.error( "Problemas con controles de pedidos", ex);
					throw new ServiceException(ex);
				}		
				
			}	

			/**
			 * Actualiza el nombre en una compra histórica web.
			 * 
			 * @param nombre		Nombre de la compra histórica
			 * @param id_cliente	Identificador único del cliente
			 * @throws ServiceException
			 */
			public void updateNombreCompraHistorica(String nombre, long id_cliente) throws ServiceException {
				PedidosCtrl dirctr = new PedidosCtrl();
				try {
					dirctr.updateNombreCompraHistorica( nombre, id_cliente);
				} catch (PedidosException ex) {
					logger.error( "Problemas con controles de pedidos", ex);
					throw new ServiceException(ex);
				}
			}
		    
		    public boolean esPrimeraCompra( long id_cliente ) throws ServiceException {

		    	PedidosCtrl dirctr = new PedidosCtrl();

		        try {
		            return dirctr.esPrimeraCompra( id_cliente );
		        } catch (cl.bbr.jumbocl.pedidos.exceptions.PedidosException ex) {
		            logger.error( "Problemas con controles de pedidos", ex);
		            throw new ServiceException(ex);
		        }       
		        
		    }
		    
		    public List getAlertaPedidoByKey(long id_pedido, int keyAlerta) throws ServiceException {

		    	PedidosCtrl dirctr = new PedidosCtrl();

		        try {
		            return dirctr.getAlertaPedidoByKey(id_pedido, keyAlerta);
		        } catch (cl.bbr.jumbocl.pedidos.exceptions.PedidosException ex) {
		            logger.error( "Problemas con controles de pedidos", ex);
		            throw new ServiceException(ex);
		        }       
		        
		    }
		    
		    public List getProductosXAlerta(long id_pedido, String key_Validation) throws ServiceException {

		    	PedidosCtrl dirctr = new PedidosCtrl();

		        try {
		            return dirctr.getProductosXAlerta(id_pedido, key_Validation);
		        } catch (Exception ex) {
		            logger.error( "Problemas con controles de pedidos", ex);
		            throw new ServiceException(ex);
		        }       
		        
		    }
		      
			//Cristian Valdebenito
			
			public void updatePedidoInvitado(int idpedido,long rut, 
					String dv , String nombre , String apellido) throws ServiceException {
			    


				PedidosCtrl pedidos = new PedidosCtrl();
				try {
				    logger.info("Se inicia Update Datos Cliente Pedido Invitado ");
					 pedidos.updateDatosPedidoCliente(idpedido,rut, dv, nombre,apellido);
				} catch (cl.bbr.jumbocl.pedidos.exceptions.PedidosException ex) {
					
					throw new ServiceException(ex);
				}

				
			}
			//Fin Cristian Valdebenito
			public PagoGrabilityDTO getPagoByOP(long id_pedido) throws ServiceException, SystemException{
				
				PedidosCtrl pedidos = new PedidosCtrl();
				try {
					return pedidos.getPagoByOP(id_pedido);
				} catch (PedidosException e) {
					e.printStackTrace();
					throw new ServiceException(e.getMessage());
				}
				
			
			}
			public PagoGrabilityDTO getPagoByToken(String token) throws ServiceException, SystemException{
				
				PedidosCtrl pedidos = new PedidosCtrl();
				try {
					return pedidos.getPagoByToken(token);
				} catch (PedidosException e) {
					e.printStackTrace();
					throw new ServiceException(e.getMessage());
				}
				
			
			}

			public void insertRegistroPago(PagoGrabilityDTO oPagoGrabilityDTO) throws ServiceException {
				PedidosCtrl pedidos = new PedidosCtrl();
				try {
					 pedidos.insertRegistroPago(oPagoGrabilityDTO);
				} catch (cl.bbr.jumbocl.pedidos.exceptions.PedidosException ex) {					
					throw new ServiceException(ex);
				}
			}
			
			public void actualizaPagoGrabilityByOP (PagoGrabilityDTO pago) throws ServiceException {
				PedidosCtrl pedidos = new PedidosCtrl();
				try{
					pedidos.actualizaPagoGrabilityByOP (pago);
				}catch (PedidosException e){
					logger.error("Problema BizDelegate (getPagoByOP)", e);
		            throw new ServiceException(e);
				}
			}

			public List listaDeProductosPreferidos(int client_id) throws ServiceException {
				PedidosCtrl dirctr = new PedidosCtrl();
				try{
					return dirctr.listaDeProductosPreferidos( client_id);
				}catch (PedidosException e){
					logger.error("Problema BizDelegate (getPagoByOP)", e);
		            throw new ServiceException(e);
				}
				
			}

			public long updateList(long idClient, ArrayList productsArray, String listType, long listId, String listName) throws ServiceException{
				PedidosCtrl dirctr = new PedidosCtrl();
				try{
					return dirctr.updateList( idClient,productsArray,listType, listId,listName);
				}catch (PedidosException e){
					logger.error("Problema BizDelegate (getPagoByOP)", e);
		            throw new ServiceException(e);
				}
			}
		    
		
			//Valida Jornada Despacho grability
			public boolean isValidJornadasLocal(long idJdespacho, long idJpicking, long idLocal) throws ServiceException{
				
				JornadasCtrl jornadas = new JornadasCtrl();
				try {
					return jornadas.isValidJornadasLocal(idJdespacho, idJpicking, idLocal);
				} catch (JornadasException e) {
					//e.printStackTrace();
					throw new ServiceException(e);
				}
			}
			
			public boolean productoEnOPConSistitutosMxN(long idPedido) throws ServiceException, SystemException {
				PedidosCtrl pedidos = new PedidosCtrl();
	  	    	try{
	  	    		return pedidos.productoEnOPConSistitutosMxN(idPedido);
	  	    	} catch (PedidosException e) {
	  	            throw new ServiceException(e.getMessage());
	  	        }
			}
			
			public Map getOPConProductosFaltantesEnPromoMxN(long idPedido) throws ServiceException, SystemException {
				PedidosCtrl pedidos = new PedidosCtrl();
	  	    	try{
	  	    		return pedidos.getOPConProductosFaltantesEnPromoMxN(idPedido);
	  	    	} catch (PedidosException e) {
	  	            throw new ServiceException(e.getMessage());
	  	        }
			}
			/**
			 * Retorna un Parametro por su Nombre
			 * 
			 * @return ParametroDTO
			 * @throws ServiceException 
			 * @see    cl.bbr.jumbocl.pedidos.dto.ParametroDTO
			 */
			public ParametroDTO getParametroByName(String Name)
				throws ServiceException{
				
				ParametrosCtrl parametro = new ParametrosCtrl();
				try {
					return parametro.getParametroByName(Name);
				} catch (ParametrosException e) {
					e.printStackTrace();
					throw new ServiceException(e);
				}
			}
			public boolean actualizaParametroByName(String Name, String valor)
					throws ServiceException{
					
					ParametrosCtrl parametro = new ParametrosCtrl();
					try {
						return parametro.actualizaParametroByName(Name, valor);
					} catch (ParametrosException e) {
						e.printStackTrace();
						throw new ServiceException(e);
					}
				}
			
			/* 
			 * INICIO VENTA MASIVA 
			 * */			
			public PagoVentaMasivaDTO getPagoVentaMasivaByToken(String token) throws ServiceException, SystemException{				
				PedidosCtrl pedidos = new PedidosCtrl();
				try {
					return pedidos.getPagoVentaMasivaByToken(token);
				} catch (PedidosException e) {
					e.printStackTrace();
					throw new ServiceException(e.getMessage());
				}
			}
			public PagoVentaMasivaDTO getPagoVentaMasivaByIdPedido(long idPedido) throws ServiceException, SystemException{				
				PedidosCtrl pedidos = new PedidosCtrl();
				try {
					return pedidos.getPagoVentaMasivaByIdPedido(idPedido);
				} catch (PedidosException e) {
					e.printStackTrace();
					throw new ServiceException(e.getMessage());
				}
			}
			
			public void addPagoVentaMasiva(PagoVentaMasivaDTO dto) throws ServiceException {
				PedidosCtrl pedidos = new PedidosCtrl();
				try {
					pedidos.addPagoVentaMasiva(dto);					
				} catch (cl.bbr.jumbocl.pedidos.exceptions.PedidosException ex) {					
					throw new ServiceException(ex);
				}				
			}
			public void actualizaPagoVentaMasivaByOP (PagoVentaMasivaDTO pago) throws ServiceException {
				PedidosCtrl pedidos = new PedidosCtrl();
				try{
					pedidos.actualizaPagoVentaMasivaByOP(pago);
				}catch (PedidosException e){
					logger.error("Problema BizDelegate (getPagoByOP)", e);
		            throw new ServiceException(e);
				}
			}
			
			public JornadaDTO getDatosJornada(String hora, String fecha, long idLocal)throws ServiceException, SystemException, PedidosException, ParseException {
				PedidosCtrl pedidos = new PedidosCtrl();
				try{
					return pedidos.getDatosJornada( hora,  fecha, idLocal);
				} catch (PedidosException e) {
					e.printStackTrace();
					throw new ServiceException(e);
				}
			}

			public ProductoEntity getProductoPedidoByIdProdFO(String pro_id) throws SystemException, ServiceException {
				PedidosCtrl pedidos = new PedidosCtrl();
				try{
					return pedidos.getProductoPedidoByIdProdFO( pro_id);
				} catch (Exception e) {
					e.printStackTrace();
					throw new ServiceException(e);
				}
			}
			public void addDetallePickingVentaMasiva(List lista, CreaRondaDTO dtoRonda, BinDTO dtoBin, long idPedido, long pedidoValidado) throws PedidosException {
				PedidosCtrl pedidos = new PedidosCtrl();
				try {
					pedidos.addDetallePickingVentaMasiva(lista, dtoRonda, dtoBin, idPedido, pedidoValidado);
				} catch (Exception  e) {
					throw new PedidosException(e);
				}
			}
			public List getProductosSolicitadosVMById(long id_pedido) throws ServiceException, SystemException {        
				PedidosCtrl pedidos = new PedidosCtrl();
				try {
					return pedidos.getProductosSolicitadosVMById(id_pedido);
				} catch (PedidosException e) {
					e.printStackTrace();
					throw new ServiceException(e.getMessage());
				}
			}
			/* 
			 * FIN INICIO VENTA MASIVA 
			 * */

			public JornadaDTO getDatosJornadaDespachoSegunComuna(String hora, String fecha, long comuna_id, long idLocal) throws ServiceException, SystemException, PedidosException, ParseException {
				PedidosCtrl pedidos = new PedidosCtrl();
				try{
					return pedidos.getDatosJornadaDespachoSegunComuna( hora,  fecha, comuna_id, idLocal);
				} catch (PedidosException e) {
					e.printStackTrace();
					throw new ServiceException(e);
				}
			}

			public int getPoligonoVentaMasivaPorComuna(long comuna_id) throws ServiceException, SystemException, PedidosException, ParseException {
				PedidosCtrl pedidos = new PedidosCtrl();
				try{
					return pedidos.getPoligonoVentaMasivaPorComuna( comuna_id);
				} catch (PedidosException e) {
					e.printStackTrace();
					throw new ServiceException(e);
				}
			}
			 
			 
}
