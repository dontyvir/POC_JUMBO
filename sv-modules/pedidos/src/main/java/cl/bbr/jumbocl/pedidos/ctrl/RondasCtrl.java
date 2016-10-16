package cl.bbr.jumbocl.pedidos.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

//(+) INDRA (+)

//(-) INDRA (-)
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.common.model.ProductosPedidoEntity;
import cl.bbr.jumbocl.common.utils.CalculosEAN;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPedDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPickDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickRelacionDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcIniciaFormPickingManualDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcRondasPropuestasDTO;
import cl.bbr.jumbocl.pedidos.dao.DAOFactory;
import cl.bbr.jumbocl.pedidos.dao.JdbcJornadasDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcRondasDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcZonasDespachoDAO;
import cl.bbr.jumbocl.pedidos.dto.ActDetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.BinCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.BinDTO;
import cl.bbr.jumbocl.pedidos.dto.BinFormPickDTO;
import cl.bbr.jumbocl.pedidos.dto.CreaRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.DetalleFormPickingDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.DetalleRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.FPickDTO;
import cl.bbr.jumbocl.pedidos.dto.FaltanteDTO;
import cl.bbr.jumbocl.pedidos.dto.FormPickOpDTO;
import cl.bbr.jumbocl.pedidos.dto.FormPickRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.IdPedidoCantDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorRondasDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductoCbarraDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PromoDetPedRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.ProrrateoPromocionDTO;
import cl.bbr.jumbocl.pedidos.dto.RondaDTO;
import cl.bbr.jumbocl.pedidos.dto.RondaPropuestaDTO;
import cl.bbr.jumbocl.pedidos.dto.RondasComparator;
import cl.bbr.jumbocl.pedidos.dto.RondasCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.TPAuditSustitucionDTO;
import cl.bbr.jumbocl.pedidos.dto.TPBinOpDTO;
import cl.bbr.jumbocl.pedidos.dto.TPDetallePedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.TPDetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.TPRegistroPickingDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.BolException;
import cl.bbr.jumbocl.pedidos.exceptions.JornadasDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.RondasDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.RondasException;
import cl.bbr.jumbocl.pedidos.exceptions.ZonasDespachoDAOException;
import cl.bbr.jumbocl.pedidos.util.FormatUtils;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.usuarios.exceptions.UsuariosDAOException;

/**
 * Entrega metodos de navegacion por Rondas y busqueda en base a criterios. 
 * Los resultados son listados de rondas y pedidos.
 * 
 * @author BBR 
 *
 */
public class RondasCtrl {

	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	boolean ejecutoInsertRonda = false;
	long idRondaIngresada = -1L;
	
	
	/**
	 * Agrega registro al log de la ronda, segun <b>id</b> de ronda, <b>login</b> de usuario y <b>log</b>.
	 * 
	 * @param  id_ronda long 
	 * @param  login String 
	 * @param  log String 
	 * @throws RondasException, en caso que no exista la ronda.
	 * @throws SystemException, en caso que exista error de sistema. 
	 * 
	 */
	public void doAddLogRonda(long id_ronda, String login, String log)
		throws RondasException, SystemException{

		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		
		try {
			dao.doAddLogRonda(id_ronda, login, log);
		} catch (RondasDAOException ex) {
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST))
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO , ex);
			throw new SystemException("Error no controlado al insertar log ronda",ex);
		}
	}	
	
	
	public boolean getRondaConFaltantes(long id_ronda)
        throws RondasException, SystemException{

        JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
	
        try {
            return dao.getRondaConFaltantes(id_ronda);
        } catch (RondasDAOException ex) {
            if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
                throw new RondasException(Constantes._EX_RON_ID_INVALIDO , ex);
            }
            throw new SystemException("Error no controlado al insertar log ronda",ex);
        }
    }
	
	
	public boolean setEstadoVerDetalle(long id_ronda)
        throws RondasException, SystemException{

        JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
	
	
        try {
            return dao.setEstadoVerDetalle(id_ronda);
        } catch (RondasDAOException ex) {
            if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
                throw new RondasException(Constantes._EX_RON_ID_INVALIDO , ex);
            }
            throw new SystemException("Error no controlado al insertar log ronda",ex);
        }
    }
	
	
	public boolean setEstadoImpEtiqueta(long id_ronda)
        throws RondasException, SystemException{

        JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
	
	
        try {
            return dao.setEstadoImpEtiqueta(id_ronda);
        } catch (RondasDAOException ex) {
            if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
                throw new RondasException(Constantes._EX_RON_ID_INVALIDO , ex);
            }
            throw new SystemException("Error no controlado al insertar log ronda",ex);
        }
    }
	
	
	public boolean setFechaImpListadoPKL(long id_ronda)
        throws RondasException, SystemException{

        JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
	
	
        try {
            return dao.setFechaImpListadoPKL(id_ronda);
        } catch (RondasDAOException ex) {
            if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
                throw new RondasException(Constantes._EX_RON_ID_INVALIDO , ex);
            }
            throw new SystemException("Error no controlado al insertar log ronda",ex);
        }
    }
	
	public boolean setFechaIniciaJornadaPKL(long id_ronda)
        throws RondasException, SystemException{

        JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
	
	
        try {
            return dao.setFechaIniciaJornadaPKL(id_ronda);
        } catch (RondasDAOException ex) {
            if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
                throw new RondasException(Constantes._EX_RON_ID_INVALIDO , ex);
            }
            throw new SystemException("Error no controlado al insertar log ronda",ex);
        }
    }
	
	
	public boolean ExisteFechaIniciaJornadaPKL(long id_ronda)
        throws RondasException, SystemException{

        JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
	
	
        try {
            return dao.ExisteFechaIniciaJornadaPKL(id_ronda);
        } catch (RondasDAOException ex) {
            if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
                throw new RondasException(Constantes._EX_RON_ID_INVALIDO , ex);
            }
            throw new SystemException("Error no controlado al insertar log ronda",ex);
        }
    }
	
	/**
	 * Obtiene listado de estados de una ronda.
	 * 
	 * @return List EstadoDTO
	 * @throws RondasException, en caso exista error en la base de datos. 
	 * 
	 */
	public List getEstadosRonda()
		throws RondasException{

		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			return dao.getEstadosRonda();
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}
	}	
	
	
	
	/**
	 * Obtiene resultado consulta de rondas de acuerdo a criterio.
	 * 
	 * @param  criterio RondasCriterioDTO 
	 * @return List MonitorRondasDTO
	 * @throws RondasException, en caso exista error en la base de datos. 
	 */
	public List getRondasByCriteria(RondasCriteriaDTO criterio)
		throws RondasException{

		// Verificamos que id_local sea no nulo > 0
		if ( criterio.getId_local() <= 0 ){
			throw new RondasException("id_local debe ser no nulo y mayor que 0 en el criterio (RondasCriteriaDTO)");
		}		
		
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			return dao.getRondasByCriteriaCMO(criterio);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}
	}

	/**
	 * Obtiene las Rondas para el monitor de Rondas
	 * @param criterio
	 * @return
	 * @throws RondasException
	 */
	/*public List getMonRondasByCriteria(RondasCriteriaDTO criterio)
	throws RondasException{
		List listaRondas = null;
		List listaZonas =null;
		List resultado = null;
		List result = new ArrayList();
		// Verificamos que id_local sea no nulo > 0
		if ( criterio.getId_local() <= 0 ){
			throw new RondasException("id_local debe ser no nulo y mayor que 0 en el criterio (RondasCriteriaDTO)");
		}		
		
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		JdbcZonasDespachoDAO daoZ = (JdbcZonasDespachoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getZonasDespachoDAO();
		try {
			listaRondas = dao.getRondasByCriteriaCMO(criterio);
			
			for (int i=0; i < listaRondas.size();i++){
				MonitorRondasDTO ronda1 = (MonitorRondasDTO) listaRondas.get(i);
				//Consultamos si esta filtrando por zona
				if (criterio.getId_zona() <= 0 ){
					MonitorRondasDTO mon1 = new MonitorRondasDTO();
					mon1.setCant_prods(ronda1.getCant_prods());
					mon1.setCant_spick(ronda1.getCant_spick());
					mon1.setEstado(ronda1.getEstado());
					mon1.setId_estado(ronda1.getId_estado());
					mon1.setId_jornada(ronda1.getId_jornada());
					mon1.setId_local(ronda1.getId_local());
					mon1.setId_ronda(ronda1.getId_ronda());
					mon1.setSector(ronda1.getSector());
					mon1.setId_sector(ronda1.getId_sector());
					mon1.setF_creacion(ronda1.getF_creacion());
					mon1.setDif_creacion(ronda1.getDif_creacion());
					mon1.setDif_picking(ronda1.getDif_picking());
					mon1.setDif_termino(ronda1.getDif_termino());
					mon1.setTipo_ve(ronda1.getTipo_ve());
					
					String zonas = "";
					listaZonas = daoZ.doBuscaZonaByRonda(ronda1.getId_ronda());
					for(int j=0; j<listaZonas.size();j++){
						ZonaDTO zon = (ZonaDTO) listaZonas.get(j);
						if (j==0) {
							zonas += zon.getNombre();
							mon1.setOrden(zon.getOrden());
						}else{
							zonas += ", "+zon.getNombre();
						}
					}
					mon1.setZonas(zonas);
					result.add(mon1);
				}else{
					MonitorRondasDTO mon1 = new MonitorRondasDTO();
					boolean filtraZona =false;
					String zonas = "";
					listaZonas = daoZ.doBuscaZonaByRonda(ronda1.getId_ronda());
					for(int j=0; j<listaZonas.size();j++){
						ZonaDTO zon = (ZonaDTO) listaZonas.get(j);
						if (j==0){
							zonas += zon.getNombre();
							mon1.setOrden(zon.getOrden());
						}else{
							zonas += ", "+zon.getNombre();
						}
						if (criterio.getId_zona() == zon.getId_zona()){
							filtraZona = true;
						}
					}
					//Si encuentra la zona filtrada, muestra en pantalla
					if (filtraZona){
						mon1.setCant_prods(ronda1.getCant_prods());
						mon1.setCant_spick(ronda1.getCant_spick());
						mon1.setEstado(ronda1.getEstado());
						mon1.setId_estado(ronda1.getId_estado());
						mon1.setId_jornada(ronda1.getId_jornada());
						mon1.setId_local(ronda1.getId_local());
						mon1.setId_ronda(ronda1.getId_ronda());
						mon1.setSector(ronda1.getSector());
						mon1.setId_sector(ronda1.getId_sector());
						mon1.setF_creacion(ronda1.getF_creacion());
						mon1.setDif_creacion(ronda1.getDif_creacion());
						mon1.setDif_picking(ronda1.getDif_picking());
						mon1.setDif_termino(ronda1.getDif_termino());
						mon1.setTipo_ve(ronda1.getTipo_ve());
						mon1.setZonas(zonas);
						result.add(mon1);
					}
				}
			}

			Collections.sort(result, new RondasComparator() );			
			
			// Paginacion sobre result
			//*************************
			int pagina = criterio.getPagina_seleccionada();
			int pag    = criterio.getPag();
			int desde = 0;
			int hasta = 10;
			
			logger.debug("pag: " + pag );
			logger.debug("pagina_seleccionada: " + pagina );
			if (pagina == -1 && pag == -1){
			    resultado = result;
			}else{
				desde = (pagina-1) * 10;
				hasta = desde + 10;
				if (result.size() < hasta) hasta = result.size();
				if (desde < 0 || desde >= hasta) desde =0;
				logger.debug("desde: " + desde + " hasta: " + hasta);
				resultado = result.subList(desde, hasta);
			}
		} catch (RondasDAOException e) {
			logger.debug("Error al buscar ronda: "+e.getMessage());
			throw new RondasException(e);
		} catch (ZonasDespachoDAOException e) {
			logger.debug("Error al buscar zona: "+e.getMessage());
			throw new RondasException(e);
		}
		return resultado;
    }*/
	

	/**
	 * Obtiene las Rondas para el monitor de Rondas
	 * @param criterio
	 * @return
	 * @throws RondasException
	 */
	public List getMonRondasByCriteriaCMO(RondasCriteriaDTO criterio)
	throws RondasException{
		List listaRondas = null;
		List listaZonas =null;
		List resultado = null;
		List result = new ArrayList();
		// Verificamos que id_local sea no nulo > 0
		if ( criterio.getId_local() <= 0 ){
			throw new RondasException("id_local debe ser no nulo y mayor que 0 en el criterio (RondasCriteriaDTO)");
		}		
		
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		JdbcZonasDespachoDAO daoZ = (JdbcZonasDespachoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getZonasDespachoDAO();
		try {
			listaRondas = dao.getRondasByCriteriaCMO(criterio);
			
			for (int i=0; i < listaRondas.size();i++){
				MonitorRondasDTO ronda1 = (MonitorRondasDTO) listaRondas.get(i);
				//Consultamos si esta filtrando por zona
				if (criterio.getId_zona() <= 0 ){
					MonitorRondasDTO ronda = new MonitorRondasDTO();
					ronda.setCant_prods(ronda1.getCant_prods());
					ronda.setCant_spick(ronda1.getCant_spick());
					ronda.setEstado(ronda1.getEstado());
					ronda.setId_estado(ronda1.getId_estado());
					ronda.setId_jornada(ronda1.getId_jornada());
					ronda.setId_local(ronda1.getId_local());
					ronda.setId_pedido(ronda1.getId_pedido());
					ronda.setId_ronda(ronda1.getId_ronda());
					ronda.setSector(ronda1.getSector());
					ronda.setId_sector(ronda1.getId_sector());
					ronda.setF_creacion(ronda1.getF_creacion());
					ronda.setDif_creacion(ronda1.getDif_creacion());
					ronda.setDif_picking(ronda1.getDif_picking());
					ronda.setDif_termino(ronda1.getDif_termino());
					ronda.setTipo_ve(ronda1.getTipo_ve());
					ronda.setEstadoImpEtiqueta(ronda1.getEstadoImpEtiqueta());
					ronda.setEstadoVerDetalle(ronda1.getEstadoVerDetalle());
					ronda.setFecha_imp_listado_pkl(ronda1.getFecha_imp_listado_pkl());
					ronda.setFecha_inico_ronda_pkl(ronda1.getFecha_inico_ronda_pkl());
                    ronda.setEstadoAuditSustitucion(ronda1.getEstadoAuditSustitucion());
					String zonas = "";
					listaZonas = daoZ.doBuscaZonaByRonda(ronda1.getId_ronda());
					for(int j=0; j<listaZonas.size();j++){
						ZonaDTO zon = (ZonaDTO) listaZonas.get(j);
						if (j==0) {
							zonas += zon.getNombre();
							ronda.setOrden(zon.getOrden());
						}
						else {
							zonas += ",<br>"+zon.getNombre();
						}
					}
					ronda.setZonas(zonas);
					result.add(ronda);
				}else{
					MonitorRondasDTO mon1 = new MonitorRondasDTO();
					boolean filtraZona =false;
					String zonas = "";
					listaZonas = daoZ.doBuscaZonaByRonda(ronda1.getId_ronda());
					for(int j=0; j<listaZonas.size();j++){
						ZonaDTO zon = (ZonaDTO) listaZonas.get(j);
						if (j==0){
							zonas += zon.getNombre();
							mon1.setOrden(zon.getOrden());
						}
						else{
							zonas += ",<br>"+zon.getNombre();
						}
						if (criterio.getId_zona() == zon.getId_zona()){
							filtraZona = true;
						}
					}
					//Si encuentra la zona filtrada, muestra en pantalla
					if (filtraZona){
						mon1.setCant_prods(ronda1.getCant_prods());
						mon1.setCant_spick(ronda1.getCant_spick());
						mon1.setEstado(ronda1.getEstado());
						mon1.setId_estado(ronda1.getId_estado());
						mon1.setId_jornada(ronda1.getId_jornada());
						mon1.setId_local(ronda1.getId_local());
						mon1.setId_pedido(ronda1.getId_pedido());
						mon1.setId_ronda(ronda1.getId_ronda());
						mon1.setSector(ronda1.getSector());
						mon1.setId_sector(ronda1.getId_sector());
						mon1.setF_creacion(ronda1.getF_creacion());
						mon1.setDif_creacion(ronda1.getDif_creacion());
						mon1.setDif_picking(ronda1.getDif_picking());
						mon1.setDif_termino(ronda1.getDif_termino());
						mon1.setTipo_ve(ronda1.getTipo_ve());
						mon1.setEstadoImpEtiqueta(ronda1.getEstadoImpEtiqueta());
						mon1.setEstadoVerDetalle(ronda1.getEstadoVerDetalle());
                        mon1.setEstadoAuditSustitucion(ronda1.getEstadoAuditSustitucion());
						mon1.setZonas(zonas);
						result.add(mon1);
					}
				}
			}

			Collections.sort(result, new RondasComparator() );			
			
			// Paginacion sobre result
			//*************************
			int pagina = criterio.getPagina_seleccionada();
			int pag    = criterio.getPag();
			int desde = 0;
			int hasta = 10;
			
			logger.debug("pag: " + pag );
			logger.debug("pagina_seleccionada: " + pagina );
			if (pagina == -1 && pag == -1){
			    resultado = result;
			}else{
				desde = (pagina-1) * 10;
				hasta = desde + 10;
				if (result.size() < hasta) hasta = result.size();
				if (desde < 0 || desde >= hasta) desde =0;
				logger.debug("desde: " + desde + " hasta: " + hasta);
				resultado = result.subList(desde, hasta);
			}
		} catch (RondasDAOException e) {
			logger.debug("Error al buscar ronda: "+e.getMessage());
			throw new RondasException(e);
		} catch (ZonasDespachoDAOException e) {
			logger.debug("Error al buscar zona: "+e.getMessage());
			throw new RondasException(e);
		}
		return resultado;
    }	

	/**
	 * Obtiene número de registros de una búsqueda de rondas por criterio.
	 * 
	 * @param  criterio RondasCriteriaDTO 
	 * @return long, cantidad de rondas
	 * @throws RondasException, en caso exista error en la base de datos.
	 *  
	 */
	public long getCountRondasByCriteria(RondasCriteriaDTO criterio)
		throws RondasException{

		// Verificamos que id_local sea no nulo > 0
		if ( criterio.getId_local() <= 0 ){
			throw new RondasException("id_local debe ser no nulo y mayor que 0 en el criterio (RondasCriteriaDTO)");
		}
		
		
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			return dao.getCountRondasByCriteria(criterio);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}
		
		
	}	
	
	
	
	/**
	 * Obtiene lista de rondas, segun <b>id</b> del pedido.
	 * 
	 * @param  id_pedido long 
	 * @return List MonitorRondasDTO
	 * @throws RondasException, en caso exista error en la base de datos.
	 * 
	 */
	public List getRondasByIdPedido(long id_pedido)
		throws RondasException{
		
		logger.debug("Antes peticion dao");
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			logger.debug("Despues peticion dao");
			return dao.getRondasByIdPedido(id_pedido);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}	

	}		
	
	
	/**
	 * Obtiene el detalle de una ronda, segun <b>id</b> de ronda.
	 * 
	 * @param  id_ronda long 
	 * @return RondaDTO
	 * @throws RondasException, en caso exista error en la base de datos.
	 * @throws SystemException, en caso exista error de sistema.
	 *  
	 */
	public RondaDTO getRondaById(long id_ronda)
		throws RondasException, SystemException{
				
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			return dao.getRondaById(id_ronda);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new SystemException(e);
		}	

	}
	

	
	/**
	 * Obtiene el log de la ronda, segun <b>id</b> de ronda.
	 * 
	 * @param  id_ronda long 
	 * @return List LogSimpleDTO
	 * @throws RondasException, en caso exista error en la base de datos.
	 * 
	 */
	public List getLogRonda(long id_ronda)
		throws RondasException{
			
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
			
			try {
				return dao.getLogRonda(id_ronda);
			} catch (RondasDAOException e) {
				e.printStackTrace();
				throw new RondasException(e);
			}
			
	}
	
	
	/**
	 * Obtiene listado con productos de la ronda, segun <b>id</b> de ronda.
	 * 
	 * @param  id_ronda long 
	 * @return List ProductosPedidoDTO
	 * @throws RondasException, en caso exista error en la base de datos.
	 * 
	 */
	public List getProductosRonda(long id_ronda)
		throws RondasException{
		
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		List lst_productos = null;
		try {
			//recupera productos pedido de la ronda
			lst_productos = dao.getProductosRonda(id_ronda);
			/*if(lst_productos.size()==0){		 
				return lst_productos;
			}*/
					
			//genera criterios del cliente si y solo si id_criterio = 4
			/*for(int i=0;i<lst_productos.size();i++){
				ProductosPedidoDTO pp = (ProductosPedidoDTO)lst_productos.get(i);				
				if (pp.getIdCriterio()!=Constantes.POLITICA_SUSTITUCION_OTRO_ID){
					//vacia el criterio
					pp.setDescCriterio("");
				}
				resultado.add(pp);
			}*/
			return lst_productos;
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}	
		

	}
	
	
	/**
	 * Obtiene listado con productos de la ronda, segun <b>id</b> de ronda.
	 * 
	 * @param  id_ronda long 
	 * @return List ProductosPedidoDTO
	 * @throws RondasException, en caso exista error en la base de datos.
	 * 
	 */
	public List getProductosRondaPKL(long id_ronda)
		throws RondasException{
		
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		List lst_productos = null;
		try {
			//recupera productos pedido de la ronda
			lst_productos = dao.getProductosRondaPKL(id_ronda);
			return lst_productos;
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}
	}

	
	/**
	 * Obtiene id_pedido asociado a Ronda Picking Light
	 * 
	 * @param  id_ronda long 
	 * @return long id_pedido
	 * @throws RondasException, en caso exista error en la base de datos.
	 * 
	 */
	public long getIdPedidoByRondaPKL(long id_ronda) throws RondasException{
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		try {
			return dao.getIdPedidoByRondaPKL(id_ronda);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}	
	}

	
	/**
	 * Obtiene Listado productos que no han sido pickeados,
	 * agrupados por OP y por sector, para aquellas jornadas
	 * que han sido iniciadas.
	 * 
	 * @param  id_local long 
	 * @param  id_sector long 
	 * @param  id_jornada long  
	 * @return List RondaPropuestaDTO
	 * @throws RondasException, en caso exista error en la base de datos.
	 * 
	 */
	public List getRondasPropuestasDet(ProcRondasPropuestasDTO criterio)
		throws RondasException{		
		
		List result = new ArrayList();
		List listarondas=null;
		
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		boolean encontrado;

		// validamos que vengan los 3 parámetros
		if ( criterio.getId_local() <= 0 )
			throw new RondasException("Falta parámetro id_local");
		if ( criterio.getId_sector() <= 0 )
			throw new RondasException("Falta parámetro id_sector");
		if ( criterio.getId_jornada() <= 0 )
			throw new RondasException("Falta parámetro id_jornada");	
		
		try {

			// validamos que la jornada pertenezca al local
			JdbcJornadasDAO dao2 = new JdbcJornadasDAO();
			JornadaDTO jor = dao2.getJornadaById(criterio.getId_jornada());
			if ( jor.getId_local() != criterio.getId_local() )
				throw new RondasException("id_jornada no pertenece al local del usuario");
				
				
			
			/*
			 * Trae las rondaspropuestas por detalle de pedido
			 */
			listarondas =dao.getRondasPropuestasDet(criterio);
			if(listarondas.size()==0){		 
				return listarondas;
			}
			//return listarondas;
			
			//System.out.println("0. RondasCtrl : listado :"+listarondas.size());
			//Agrupar por pedio sumando las cantidades
			
			for(int i=0;i<listarondas.size();i++){
				RondaPropuestaDTO ronda1 = (RondaPropuestaDTO) listarondas.get(i);
				/*
				System.out.println("1. Ronda("+i+") :"+ ronda1.getId_op()+" "+
						ronda1.getId_sector()+" "+
						ronda1.getId_jornada()+" "+
						ronda1.getSector()+" "+
						ronda1.getCant_prods()+" "+
						ronda1.getH_inicio()+" "+
						ronda1.getH_fin()+" "						
						);
						*/
				//verificar si existe un pedido almacenado en la lista result
				encontrado = false;
				for(int j=0;j<result.size();j++){
					
					RondaPropuestaDTO ronda2 = (RondaPropuestaDTO) result.get(j);
					/*
					System.out.println("2."+i+" Ronda Final:"+ ronda2.getId_op()+" "+
							ronda2.getId_sector()+" "+
							ronda2.getId_jornada()+" "+
							ronda2.getSector()+" "+
							ronda2.getCant_prods()+" "+
							ronda2.getH_inicio()+" "+
							ronda2.getH_fin()+" " );*/
					if (ronda2.getId_op()==ronda1.getId_op()){
						//incrementa la cantidad de la ronda2 : encontrada=true
						double suma = Formatos.formatoNum3Dec(ronda2.getCant_prods()+ronda1.getCant_prods());
						/*
						System.out.println("3. encuentra ronda OP:"+ ronda2.getId_op()+
								" cantidad actual:"+ronda2.getCant_prods()+
								" + agregar:"+ ronda1.getCant_prods()+ " = total:"+suma);
						*/
						ronda2.setCant_prods(suma);
						//System.out.println("4. nueva cantidad : "+suma);
						if (ronda1.getProd_SPick_con_ant().equals("S")){
						    ronda2.setProd_SPick_con_ant(ronda1.getProd_SPick_con_ant());
						}
						result.set(j,ronda2);
						encontrado = true;
					}
				}
				if (encontrado==false){
					//agrega la ronda al resultado final
					result.add(ronda1);
					//System.out.println("5. Se agrego la Ronda OP:"+ronda1.getId_op());
				} 
			}
		
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		} catch (JornadasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}	
		
		return result;
	}
	
	public List getRondasPropuestas(ProcRondasPropuestasDTO criterio)
	throws RondasException{		

	   JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();

		// validamos que vengan los 3 parámetros
		if ( criterio.getId_local() <= 0 )
			throw new RondasException("Falta parámetro id_local");
		if ( criterio.getId_sector() <= 0 )
			throw new RondasException("Falta parámetro id_sector");
		if ( criterio.getId_jornada() <= 0 )
			throw new RondasException("Falta parámetro id_jornada");	
		try {
			// validamos que la jornada pertenezca al local
			JdbcJornadasDAO dao2 = new JdbcJornadasDAO();
			JornadaDTO jor = dao2.getJornadaById(criterio.getId_jornada());
			if ( jor.getId_local() != criterio.getId_local() )
				throw new RondasException("id_jornada no pertenece al local del usuario");
			/*
			 * Trae las rondaspropuestas por detalle de pedido
			 */
			return dao.getRondasPropuestas(criterio);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		} catch (JornadasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}	
	}

	/**
	 * Obtiene Listado de bins de la ronda, segun <b>id</b> de ronda.
	 * 
	 * @param  id_ronda long 
	 * @return List BinDTO
	 * @throws RondasException, en caso exista error en la base de datos.
	 * 
	 */
	public List getBinsRonda(long id_ronda)
		throws RondasException{

		JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		
		BinCriteriaDTO criterio = new BinCriteriaDTO();
		criterio.setId_ronda(id_ronda);
		
		try {
			return dao.getBinsByCriteria(criterio);
		} catch (PedidosDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}		
		
	}
	
	

	/**
	 * Obtiene Listado de bins de la ronda, segun <b>id</b> de ronda.
	 * 
	 * @param  id_ronda long 
	 * @return List BinDTO
	 * @throws RondasException, en caso exista error en la base de datos.
	 * 
	 */
	public List getBinsRondaPKL(long id_ronda)
		throws RondasException{

		JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		
		BinCriteriaDTO criterio = new BinCriteriaDTO();
		criterio.setId_ronda(id_ronda);
		
		try {
			return dao.getBinsByCriteriaPKL(criterio);
		} catch (PedidosDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}		
		
	}
	

	
	/**
	 * Realiza proceso de preparación de ronda (cambia su estado a Preparada), segun <b>id</b> de ronda.<br>
	 * Nota: Este método tiene <b>Transaccionalidad</b>.
	 * 
	 * @param  id_ronda long 
	 * 
	 * @throws RondasException, en caso que no exista ronda. 
	 * @throws SystemException, en caso que exista error de sistema. 
	 * 
	 */
	public void doPreparaRonda(long id_ronda)
		throws RondasException, SystemException{
		
		if(id_ronda <=0){
			throw new RondasException(Constantes._EX_RON_ID_NULA);
		}
		//validamos que la ronda tenga el estado "Creada"
		RondaDTO ronda = new RondaDTO();
		ronda = this.getRondaById(id_ronda);
		
		if ( ronda.getId_estado() != Constantes.ID_ESTADO_RONDA_CREADA )
		{
			throw new RondasException("No se puede preparar una ronda en estado " + ronda.getEstado());
		}
		
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		//		 Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();

		//		 Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		//		 Marcamos los dao's con la transacción
		try {
			dao.setTrx(trx1);
		} catch (RondasDAOException e2) {
			logger.error("Error al asignar transacción al dao Rondas");
			throw new SystemException("Error al asignar transacción al dao Rondas");
		}		
		
		
		try {
			// cambia estado a la ronda
			dao.doCambiaEstadoRonda( Constantes.ID_ESTADO_RONDA_PREPARADA , id_ronda );
			
			// agrega al log de la ronda
			dao.doAddLogRonda(id_ronda, "SYSTEM", "La Ronda ha sido preparada");
			
		} catch (RondasDAOException ex) {
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}

			
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO, ex);
			}			
			throw new SystemException("Error no controlado al intentar cambiar la ronda",ex);
		}		

		//		 cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}	
	}
	
	
	/**
	 * Crea una ronda para un pedido por sector, incluyendo la cantidad de productos
	 * indicada.<br>
	 * Nota: Este método tiene <b>Transaccionalidad</b>.
	 * 
	 * @param  ronda CreaRondaDTO 
	 * @return long id_ronda
	 * @throws RondasException, en uno de los siguientes casos:<br>
	 * - La cantidad de pedidos es mayor a la máxima cantidad de pedidos para la creación de una ronda.<br>
	 * - No existen pedidos seleccionados.<br>
	 * - No existe el pedido.<br>
	 * - No existe la relación entre el pedido y la ronda.<br>
	 * - El id de la ronda es inválido.
	 * @throws SystemException, en caso exista error del sistema. 
	 * 
	 */
	//public long CreaRonda(RondaDTO ronda,  List ped_cant) throws RondasException{
	public long doCreaRonda(CreaRondaDTO ronda) throws RondasException, SystemException{
		long id_ronda = -1L;
		
		// validar que no vengan más de 6 pedidos
		//ver lst de indices
		//List lst_indices = ronda.getLst_indices();
		//if( lst_indices.size() > Constantes.CANT_MAX_PED_X_RONDA ){
		logger.debug("Creando Ronda: Cantidad de pedidos en la ronda: " + ronda.getPedidos().size());
		if( ronda.getPedidos().size() > Constantes.CANT_MAX_PED_X_RONDA ){
			throw new RondasException(Constantes._EX_RON_MAX_PEDIDOS_X_RONDA);
		}
		
		if (ronda.getPedidos().size()<=0){
			// En caso de no haber seleccionado ningun sector para la ronda. debemos revisar el mensaje
			logger.debug("Creando Ronda: Constantes._EX_RON_SIN_PEDIDOS_SELEC");
			throw new RondasException(Constantes._EX_RON_SIN_PEDIDOS_SELEC);
		}
		
		
		
		logger.debug("Creando Ronda: Se crean los DAO para Rondas y Pedidos");
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		JdbcPedidosDAO dao2 = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		
		
		//		 Creamos trx
		logger.debug("Creando Ronda: creando transacción...");
		JdbcTransaccion trx1 = new JdbcTransaccion();

		//		 Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Creando Ronda: Error al iniciar transacción");
			throw new SystemException("Creando Ronda: Error al iniciar transacción");
		}

		//		 Marcamos los dao's con la transacción
		logger.debug("Creando Ronda: asignando transacción a DAO de Rondas");
		try {
			dao.setTrx(trx1);
		} catch (RondasDAOException e2) {
			logger.error("Creando Ronda: Error al asignar transacción al dao Rondas");
			throw new SystemException("Creando Ronda: Error al asignar transacción al dao Rondas");
		}	

		logger.debug("Creando Ronda: asignando transacción a DAO de Pedidos");
		try {
			dao2.setTrx(trx1);
		} catch (PedidosDAOException e2) {
			logger.error("Creando Ronda: Error al asignar transacción al dao Pedidos");
			throw new SystemException("Creando Ronda: Error al asignar transacción al dao Pedidos");
		}
		
		
		
		

		
		//System.out.println("id_sector: " + ronda.getId_sector());
		try {
			
			//verificar que el usuario no ha presionado el boton "Atras"
			logger.debug("Creando Ronda: Obtiene Rondas Propuestas");
			ProcRondasPropuestasDTO criterio = new ProcRondasPropuestasDTO();
			criterio.setId_jornada(ronda.getId_jpicking());
			criterio.setId_local(ronda.getId_local());
			criterio.setId_sector(ronda.getId_sector());
			criterio.setTipo_ve(ronda.getTipo_ve());
			
			//List lst_aux = dao.getRondasPropuestasDet(criterio);
			List lst_aux = dao.getRondasPropuestas(criterio);
			//Revisar, si hay cambios al obtener las rondas propuestas
			//sumarizar la lista devuelta, segun la logica de RondasCtrl.getRondasPropuestas()
			//List lst_pedidos = new ArrayList();
			
			List lst_pedidos = lst_aux;

			logger.debug("Creando Ronda: lst_aux.size() = " + lst_aux.size());
			/*for(int i=0;i<lst_aux.size();i++){
				RondaPropuestaDTO ronda1 = (RondaPropuestaDTO) lst_aux.get(i);
				//verificar si existe un pedido almacenado en la lista lst_pedidos
				boolean encontrado = false;
				for(int j=0;j<lst_pedidos.size();j++){
					
					RondaPropuestaDTO ronda2 = (RondaPropuestaDTO) lst_pedidos.get(j);
					if (ronda2.getId_op()==ronda1.getId_op()){
						//incrementa la cantidad de la ronda2 : encontrada=true
						double suma = Formatos.formatoNum3Dec(ronda2.getCant_prods()+ronda1.getCant_prods());
						ronda2.setCant_prods(suma);
						//System.out.println("4. nueva cantidad : "+suma);
						lst_pedidos.set(j,ronda2);
						encontrado = true;
					}
				}
				if (encontrado==false){
					logger.debug("Creando Ronda: Hay ronda propuesta: " + ronda1.getId_op());
					//agrega la ronda al resultado final
					lst_pedidos.add(ronda1);
				} 			
			}*/
			//fin de sumarizar la lista devuelta
			
			List lst_pedidos_dto = ronda.getPedidos(); 
			boolean encontro = false;
			for(int i=0;i<lst_pedidos_dto.size();i++){
				IdPedidoCantDTO dto = (IdPedidoCantDTO)lst_pedidos_dto.get(i);
				for(int j=0;j<lst_pedidos.size();j++){
					//encontro = false;
					RondaPropuestaDTO ronda_dto = (RondaPropuestaDTO)lst_pedidos.get(j); 
					logger.debug("dto	-> id_op:"+dto.getId_pedido()+", cant:"+dto.getCant_prod());
					logger.debug("ronda	-> id_op:"+ronda_dto.getId_op()+", cant:"+ronda_dto.getCant_prods());
					if(ronda_dto.getId_op()==dto.getId_pedido() && ronda_dto.getCant_prods() ==dto.getCant_prod()){
						encontro = true;
					}
				}
				if(!encontro){
					logger.error("Creando Ronda: EX_RON_PEDIDO_NO_COINCIDE");
					trx1.rollback();
					throw new RondasException(Constantes._EX_RON_PEDIDO_NO_COINCIDE);
				}
			}
			

			//crear ronda
			ronda.setId_estado( Constantes.ID_ESTADO_RONDA_CREADA );
			
			// Crea la Ronda
			if (!ejecutoInsertRonda) //Verificamos que no se haya insertado la ronda
				id_ronda = dao.setCreaRonda(ronda);
			
			// Agrega al log de la ronda
			dao.doAddLogRonda( id_ronda, "SYSTEM" , "La Ronda ha sido Creada" );
			
			if (id_ronda >0){
				ejecutoInsertRonda = true;
				logger.debug("Creando Ronda: Ronda creada exitosamente");
				//System.out.println("1");
				for (int i=0; i<ronda.getPedidos().size(); i++){
					//System.out.println("2."+i);
					IdPedidoCantDTO dto = new IdPedidoCantDTO();
					dto = (IdPedidoCantDTO)ronda.getPedidos().get(i);
					//System.out.println("id_ronda:"+id_ronda+" id_sector: "+ronda.getId_sector()+"id_pedido: " + dto.getId_pedido() + " cant:" + dto.getCant());	
					
					logger.debug("Creando Ronda: genera Detalle de Ronda");
					dao.setDetalleRonda(ronda.getId_sector(), id_ronda, dto.getId_pedido(), dto.getCant());
					
					// actualizamos estado pedido "En Picking"
					logger.debug("Creando Ronda: cambia Estado del Pedido");
		        	dao2.setModEstadoPedido( dto.getId_pedido(), Constantes.ID_ESTAD_PEDIDO_EN_PICKING );
				
					}
				
		
				
			}
				
		} catch (RondasDAOException ex) {
			//			rollback trx
			logger.error("Creando Ronda: RollBack por RondasDAOException: " + ex.getMessage());
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Creando Ronda: ERROR rollback por RondasDAOException: " + e1.getMessage());
				throw new SystemException("Error al hacer rollback");
			}
			
			
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO);
			}
			throw new SystemException ("error no controlado en Crear Rondas");
			
		} catch (NumberFormatException ex) {
			logger.error("Creando Ronda: RollBack por NumberFormatException: " + ex.getMessage());
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Creando Ronda: ERROR rollback por NumberFormatException: " + e1.getMessage());
				throw new SystemException("Error al hacer rollback");
			}
			
			
			ex.printStackTrace();
			throw new SystemException(ex);
		} catch (PedidosDAOException ex) {
			logger.error("Creando Ronda: RollBack por PedidosDAOException: " + ex.getMessage());
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Creando Ronda: ERROR rollback por PedidosDAOException: " + e1.getMessage());
				throw new SystemException("Error al hacer rollback");
			}
			
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new RondasException(Constantes._EX_PED_ID_INVALIDO);
			}
			throw new SystemException ("error no controlado en Crear Rondas");
		} catch (DAOException e) {
			logger.error("Creando Ronda: ERROR DAOException: " + e.getMessage());
			throw new SystemException("Error al hacer rollback");
		}
		
		//		 cerramos trx
		try {
			logger.error("Creando Ronda: Finalizando Transacción");
			trx1.end();
		} catch (DAOException e) {
			logger.error("Creando Ronda: ERROR DAOException: " + e.getMessage());
			throw new SystemException("Error al finalizar transacción");
		}
		return id_ronda;
	}


	/**
	 * Crea una ronda para un pedido por sector, incluyendo la cantidad de productos
	 * indicada.<br>
	 * Nota: Este método tiene <b>Transaccionalidad</b>.
	 * 
	 * @param  ronda CreaRondaDTO 
	 * @return long id_ronda
	 * @throws RondasException, en uno de los siguientes casos:<br>
	 * - La cantidad de pedidos es mayor a la máxima cantidad de pedidos para la creación de una ronda.<br>
	 * - No existen pedidos seleccionados.<br>
	 * - No existe el pedido.<br>
	 * - No existe la relación entre el pedido y la ronda.<br>
	 * - El id de la ronda es inválido.
	 * @throws SystemException, en caso exista error del sistema. 
	 * 
	 */
	//public long CreaRonda(RondaDTO ronda,  List ped_cant) throws RondasException{
	public long doCreaRondaPKL(CreaRondaDTO ronda) throws RondasException, SystemException{
		
		// validar que no vengan más de 6 pedidos
		//ver lst de indices
		//List lst_indices = ronda.getLst_indices();
		//if( lst_indices.size() > Constantes.CANT_MAX_PED_X_RONDA ){
		logger.debug("Creando Ronda: Se crean los DAO para Rondas y Pedidos");
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		JdbcPedidosDAO dao2 = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		
		
		//		 Creamos trx
		logger.debug("Creando Ronda: creando transacción...");
		JdbcTransaccion trx1 = new JdbcTransaccion();

		//		 Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Creando Ronda: Error al iniciar transacción");
			throw new SystemException("Creando Ronda: Error al iniciar transacción");
		}

		//		 Marcamos los dao's con la transacción
		logger.debug("Creando Ronda: asignando transacción a DAO de Rondas");
		try {
			dao.setTrx(trx1);
		} catch (RondasDAOException e2) {
			logger.error("Creando Ronda: Error al asignar transacción al dao Rondas");
			throw new SystemException("Creando Ronda: Error al asignar transacción al dao Rondas");
		}	

		logger.debug("Creando Ronda: asignando transacción a DAO de Pedidos");
		try {
			dao2.setTrx(trx1);
		} catch (PedidosDAOException e2) {
			logger.error("Creando Ronda: Error al asignar transacción al dao Pedidos");
			throw new SystemException("Creando Ronda: Error al asignar transacción al dao Pedidos");
		}
		
		
		
		long id_ronda = -1L;

		
		//System.out.println("id_sector: " + ronda.getId_sector());
		try {
			
			//verificar que el usuario no ha presionado el boton "Atras"
			logger.debug("Creando RondaPKL: Obtiene Rondas Propuestas");
			//ProcRondasPropuestasDTO criterio = new ProcRondasPropuestasDTO();
			/*criterio.setId_jornada(ronda.getId_jpicking());
			criterio.setId_local(ronda.getId_local());
			criterio.setId_pedido(ronda.getId_pedido());
			criterio.setId_sector(Constantes.SECTOR_TIPO_PICKING_LIGHT_CTE);
			//criterio.setCant_prod(ronda.getCant_prod());
			criterio.setTipo_ve(ronda.getTipo_ve());*/
			
			////////RondaPropuestaDTO rondaPropuesta = dao.getRondasPropuestasPKL(criterio);
			//Revisar, si hay cambios al obtener las rondas propuestas
			//sumarizar la lista devuelta, segun la logica de RondasCtrl.getRondasPropuestas()
			//List lst_pedidos = new ArrayList();

			/*logger.debug("Creando Ronda: lst_aux.size() = " + lst_aux.size());
			for(int i=0;i<lst_aux.size();i++){
				RondaPropuestaDTO ronda1 = (RondaPropuestaDTO) lst_aux.get(i);
				//verificar si existe un pedido almacenado en la lista lst_pedidos
				boolean encontrado = false;
				for(int j=0;j<lst_pedidos.size();j++){
					
					RondaPropuestaDTO ronda2 = (RondaPropuestaDTO) lst_pedidos.get(j);
					if (ronda2.getId_op()==ronda1.getId_op()){
						//incrementa la cantidad de la ronda2 : encontrada=true
						double suma = Formatos.formatoNum3Dec(ronda2.getCant_prods()+ronda1.getCant_prods());
						ronda2.setCant_prods(suma);
						//System.out.println("4. nueva cantidad : "+suma);
						lst_pedidos.set(j,ronda2);
						encontrado = true;
					}
				}
				if (encontrado==false){
					logger.debug("Creando Ronda: Hay ronda propuesta: " + ronda1.getId_op());
					//agrega la ronda al resultado final
					lst_pedidos.add(ronda1);
				}
			}*/
			//fin de sumarizar la lista devuelta
			
			/*List lst_pedidos_dto = ronda.getPedidos();
			boolean encontro = false;
			for(int i=0;i<lst_pedidos_dto.size();i++){
				IdPedidoCantDTO dto = (IdPedidoCantDTO)lst_pedidos_dto.get(i);
				for(int j=0;j<lst_pedidos.size();j++){
					//encontro = false;
					RondaPropuestaDTO ronda_dto = (RondaPropuestaDTO)lst_pedidos.get(j); 
					logger.debug("dto	-> id_op:"+dto.getId_pedido()+", cant:"+dto.getCant_prod());
					logger.debug("ronda	-> id_op:"+ronda_dto.getId_op()+", cant:"+ronda_dto.getCant_prods());
					if(ronda_dto.getId_op()==dto.getId_pedido() && ronda_dto.getCant_prods() ==dto.getCant_prod()){
						encontro = true;
					}
				}
				if(!encontro){
					logger.error("Creando Ronda: EX_RON_PEDIDO_NO_COINCIDE");
					trx1.rollback();
					throw new RondasException(Constantes._EX_RON_PEDIDO_NO_COINCIDE);
				}
			}*/
			

			//crear ronda
			ronda.setId_estado(Constantes.ID_ESTADO_RONDA_CREADA);
			//ronda.setId_sector(Constantes.SECTOR_TIPO_PICKING_LIGHT_CTE);
			//ronda.setId_sector(Constantes.SECTOR_TIPO_PICKING_LIGHT_CTE);
			// Crea la Ronda
			
			id_ronda = dao.setCreaRondaPKL(ronda);
			
			// Agrega al log de la ronda
			dao.doAddLogRonda( id_ronda, "SYSTEM" , "La Ronda ha sido Creada" );
			
			if (id_ronda >0){
				logger.debug("Creando Ronda: Ronda creada exitosamente");
				logger.debug("Creando Ronda: genera Detalle de Ronda");
				dao.setDetalleRondaPKL(id_ronda, ronda.getId_pedido(), Double.parseDouble(ronda.getCant_prod()));
					
				// actualizamos estado pedido "En Picking"
				logger.debug("Creando Ronda: cambia Estado del Pedido");
	        	dao2.setModEstadoPedido( ronda.getId_pedido(), Constantes.ID_ESTAD_PEDIDO_EN_PICKING );
			}
				
		} catch (RondasDAOException ex) {
			//			rollback trx
			logger.error("Creando Ronda: RollBack por RondasDAOException: " + ex.getMessage());
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Creando Ronda: ERROR rollback por RondasDAOException: " + e1.getMessage());
				throw new SystemException("Error al hacer rollback");
			}
			
			
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO);
			}throw new SystemException ("error no controlado en Crear Rondas");
			
		} catch (NumberFormatException ex) {
			logger.error("Creando Ronda: RollBack por NumberFormatException: " + ex.getMessage());
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Creando Ronda: ERROR rollback por NumberFormatException: " + e1.getMessage());
				throw new SystemException("Error al hacer rollback");
			}
			
			
			ex.printStackTrace();
			throw new SystemException(ex);
		} catch (PedidosDAOException ex) {
			logger.error("Creando Ronda: RollBack por PedidosDAOException: " + ex.getMessage());
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Creando Ronda: ERROR rollback por PedidosDAOException: " + e1.getMessage());
				throw new SystemException("Error al hacer rollback");
			}
			
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new RondasException(Constantes._EX_PED_ID_INVALIDO);
			}throw new SystemException ("error no controlado en Crear Rondas");
		} /*catch (DAOException e) {
			logger.error("Creando Ronda: ERROR DAOException: " + e.getMessage());
			throw new SystemException("Error al hacer rollback");
		}*/
		
		//		 cerramos trx
		try {
			logger.error("Creando Ronda: Finalizando Transacción");
			trx1.end();
		} catch (DAOException e) {
			logger.error("Creando Ronda: ERROR DAOException: " + e.getMessage());
			throw new SystemException("Error al finalizar transacción");
		}
		return id_ronda;
	}

	
	/**
	 * Muestra el total de productos por OP por ronda
	 * 
	 * @param  id_ronda long 
	 * @return List RondaDetalleResumenDTO
	 * @throws RondasException, en caso exista error en la base de datos.
	 *  
	 */
	public List getResumenRondaById(long id_ronda)
		throws RondasException{

		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		try {
			return dao.getResumenRondaById(id_ronda);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}		
	}


	/**
	 * Retorna listado para llenar tabla PDA.
	 * 
	 * @param  id_ronda long 
	 * @return List BarraDetallePedidosRondaDTO
	 * @throws RondasException, en caso exista error en la base de datos.
	 */
	public List getBarrasRondaDetallePedido(long id_ronda)
		throws RondasException{

		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		try {
			return dao.getBarrasRondaDetallePedido(id_ronda);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}		
	}

	
    //getBarrasAuditoriaSustitucion(List listBarras)
	/**
     * Retorna listado para llenar tabla PDA.
     * 
     * @param  id_ronda long 
     * @return List BarraDetallePedidosRondaDTO
     * @throws RondasException, en caso exista error en la base de datos.
     */
    public List getBarrasAuditoriaSustitucion(List listBarras, long id_ronda)
        throws RondasException{

        JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
        try {
            return dao.getBarrasAuditoriaSustitucion(listBarras, id_ronda);
        } catch (RondasDAOException e) {
            e.printStackTrace();
            throw new RondasException(e);
        }       
    }


    
	/**
	 * Asigna pickeador a la ronda y cambia su estado a En Picking.<br>
	 * Nota: Este método tiene <b>Transaccionalidad</b>.
	 * 
	 * @param  id_ronda long 
	 * @param  id_usuario long 
	 * @throws RondasException, en uno de los siguientes casos:
	 * - El ID de la ronda es inválido.<br>
	 * - El estado de la ronda es inadecuado.
	 * @throws SystemException, en caso exista error del sistema.
	 * 
	 */
	public void doAsignaRonda(long id_ronda, long id_usuario) 
		throws RondasException, SystemException{
		
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		//		 Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();

		//		 Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}

		//		 Marcamos los dao's con la transacción
		try {
			dao.setTrx(trx1);
		} catch (RondasDAOException e2) {
			logger.error("Error al asignar transacción al dao Rondas");
			throw new SystemException("Error al asignar transacción al dao Rondas");
		}	

		
		
		try {
			
			RondaDTO ronda = dao.getRondaById(id_ronda);
			
			if ( ronda == null ){
				logger.info("La ronda solicitada no existe");
				
				try { trx1.rollback(); } catch (DAOException e1) {
					logger.error("Error al hacer rollback");
					throw new SystemException("Error al hacer rollback");
				}
				
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO);
			}

			
			if ( ronda.getId_estado() != Constantes.ID_ESTADO_RONDA_CREADA ){
				logger.info("Ronda en estado inadecuado para realizar la operación");
				
				try { trx1.rollback(); } catch (DAOException e1) {
					logger.error("Error al hacer rollback");
					throw new SystemException("Error al hacer rollback");
				}
				
				throw new RondasException(Constantes._EX_RON_ESTADO_INADECUADO);
			}
			
			
			// Cambia el estado a la ronda a En Picking
			dao.doCambiaEstadoRonda(Constantes.ID_ESTADO_RONDA_EN_PICKING, id_ronda);
		
			// Asigna el pickeador a la ronda
			dao.doAsignaPickeadorRonda(id_ronda, id_usuario);
			
			// Agrega al log de la ronda
			dao.doAddLogRonda(id_ronda, "SYSTEM", "Se asigna pickeador y se inicia el picking");
			
		} catch (RondasDAOException e) {
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			logger.error("Error DAO");
			throw new SystemException(e);
		}
		//		 cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}

		
	}

		
	/**
	 * Retorna listado de sustitutos de la ronda, segun <b>id</b> de la ronda.
	 * 
	 * @param  id_ronda long 
	 * @return List SustitutoDTO
	 * @throws RondasException, en caso exista error en la base de datos.
	 */
	public List getSustitutosByRondaId(long id_ronda)
	throws RondasException {
		
		List result = new ArrayList();
		JdbcRondasDAO daoR = (JdbcRondasDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			result = daoR.getSustitutosByRondaId(id_ronda);		
		} catch (RondasDAOException e) {
			logger.debug("Problema :"+e.getMessage());
			throw new RondasException(e);	
		}		
		return result;
	}


	/**
	 * Retorna listado de faltantes de la ronda, segun <b>id</b> de la ronda.
	 * 
	 * @param  id_ronda long 
	 * @return List FaltanteDTO
	 * @throws RondasException, en caso exista error en la base de datos.
	 * 
	 */
	public List getFaltantesByRondaId(long id_ronda)
	throws RondasException {

		List faltantes = new ArrayList();
		List result = new ArrayList();
		JdbcRondasDAO dao = (JdbcRondasDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		try {
			faltantes = dao.getFaltantesByRondaId(id_ronda);
			
			ProductosPedidoEntity prod = null;
			for (int i = 0; i < faltantes.size(); i++) {
				prod = null;
				prod = (ProductosPedidoEntity) faltantes.get(i);
				FaltanteDTO ft = new FaltanteDTO();
				ft.setId_pedido(prod.getId_pedido());
				ft.setCod_producto(prod.getCod_prod1());
				ft.setDescripcion(prod.getDescripcion());
				ft.setCant_faltante(prod.getCant_solic());
				ft.setUni_med(prod.getUni_med());
				ft.setPrecio(prod.getPrecio());
                ft.setIdCriterio(prod.getIdCriterio());
                ft.setDescCriterio(prod.getDescCriterio());
                
				//FaltanteDTO ft = new FaltanteDTO(prod.getCod_prod1(),prod.getDescripcion(),(int)prod.getCant_solic(), id_pedido);
				/*String cod_producto, String descripcion, int cant_faltante, long id_pedido
				ClientesDTO clidto = new ClientesDTO(cli.getId().longValue(),cli.getRut().longValue(),
						cli.getNombre(),cli.getApellido_pat(),cli.getApellido_mat(),
						FrmClientes.frmFecha(cli.getFec_nac().toString()),FrmClientes.frmCliEstado(cli.getEstado()));
				*/
				
				result.add(ft);
			}
			
		} catch (RondasDAOException e) {
			logger.debug("Problema :"+e.getMessage());
			throw new RondasException(e);	
		}		
		return result;
	}

    /**
     * Obtiene la ronda de la PDA y la carga a la base de datos.
     * 
     * @author jdroguett
     * @param piking
     * @throws RondasDAOException
     * @throws RondasException
     * @throws PedidosDAOException
     * @throws UsuariosDAOException
     * @throws BolException
     */
    public void subeRonda(ActDetallePickingDTO picking) throws RondasDAOException, RondasException, PedidosDAOException, UsuariosDAOException, BolException {
        JdbcRondasDAO daoR = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        long rondaId = picking.getId_ronda();

        //J: Entrega los datos principales de la ronda, no su detalle.
        RondaDTO rondaDto = daoR.getRondaById(rondaId);
        long localId = rondaDto.getId_local();

        //J:ronda no existe
        if (rondaDto == null) {
            logger.error("id de ronda incorrecto");
            throw new RondasException(Constantes._EX_RON_ID_INVALIDO);
        }

        //J:la ronda debe estar en estado picking
        if (rondaDto.getId_estado() != Constantes.ID_ESTADO_RONDA_EN_PICKING) {
            logger.error("ronda en estado distinto de picking");
            throw new RondasException(Constantes._EX_RON_ESTADO_INADECUADO);
        }

        List productosDeLaRonda = daoR.getProductosRonda(rondaDto.getId_ronda());
        List bins = picking.getLst_bin_op();
        List productosPickeados = picking.getLst_det_picking();
        List resultadoPedido = picking.getLst_det_pedido();
        List registroPicking = picking.getLst_reg_picking();
        
        
        if(bins.size() == 0 || productosPickeados.size() == 0 || resultadoPedido.size() == 0 || registroPicking.size() == 0){
            logger.error("Faltan datos en alguno de los siguietes: ");
            logger.error("bins.size() = " + bins.size());
            logger.error("productosPickeados.size() = " + productosPickeados.size());
            logger.error("resultadoPedido.size() = " + resultadoPedido.size());
            logger.error("registroPicking.size() = " + registroPicking.size());
            //throw new RondasException("Faltan datos");
        }

        //J:valida que cada bins posea un id de pedido que esté en la ronda
        if (!binsPertenecenARonda(productosDeLaRonda, bins)) {
            logger.error("bins con pedidos que no son de la ronda");
            throw new RondasException(Constantes._EX_OPE_REL_RONDA_NO_EXISTE);
        }

        if (!detallePickeadoCorrecto(productosDeLaRonda, productosPickeados)) {
            logger.error("productos pickeados con id_detalle distinto de los productos de la ronda");
            throw new RondasException(Constantes._EX_OPE_REL_DET_NO_EXISTE);
        }
        
        if (daoR.rondaTieneBins(rondaId)){
            logger.error("La ronda ya tiene bins cargados");
            throw new RondasException("La ronda ya tiene bins cargados");
        }

        List detallePicking = construirDetallePicking(productosDeLaRonda, productosPickeados, localId, dao);
        List binsConDetalle = agregarDetalleABins(bins, detallePicking, rondaId);
        
        //resgitroPicking sólo debería tener una ronda, por lo tanto es una lista de un sólo elemento
        TPRegistroPickingDTO regPick = (TPRegistroPickingDTO) registroPicking.get(0);
        long usuarioFiscalId = daoR.getUserIdByLogin(regPick.getUsuario_fiscalizador());

        daoR.grabarRonda(rondaId, binsConDetalle, resultadoPedido, detallePicking, regPick, usuarioFiscalId);
    }

    
    public void recepcionAuditoriaSustitucion(List lst_AudSust) throws RondasDAOException, RondasException, PedidosDAOException, UsuariosDAOException, SQLException {
        JdbcRondasDAO daoR = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();

        for (int i=0; i < lst_AudSust.size(); i++){
            TPAuditSustitucionDTO AudSust = (TPAuditSustitucionDTO)lst_AudSust.get(i);
            daoR.insetaAuditoriaSustitutos(AudSust);    
        }
    }


    public void marcaRondaAuditoriaSustitucion(int id_ronda) throws RondasDAOException, RondasException, PedidosDAOException, UsuariosDAOException, SQLException {
        JdbcRondasDAO daoR = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
       
        daoR.marcaRondaAuditoriaSustitucion(id_ronda);
    }


    /**
     * Agrega a cada bin su detalle picking
     * 
     * @author jdroguett
     * @param bins
     * @param detallePicking
     * @param rondaId
     */
    private List agregarDetalleABins(List bins, List detallePicking, long rondaId) {
        logger.debug("cantidad de bins: " + bins.size());
        logger.debug("cantidad de detallePicking: " + detallePicking.size());
        //Hashtable para buscar más rápido cada bin
        Hashtable losBins = new Hashtable();
        for (int i = 0; i < bins.size(); i++) {
            TPBinOpDTO bin = (TPBinOpDTO) bins.get(i);
            //no se pq se usa tanto DTO pero sigamos el juego
            BinDTO binDTO = new BinDTO();
            binDTO.setId_ronda(rondaId);
            binDTO.setId_pedido(bin.getId_op());
            binDTO.setCod_bin(bin.getCod_bin());
            binDTO.setCod_sello1(bin.getCod_sello1());
            binDTO.setCod_sello2(bin.getCod_sello2());
            binDTO.setCod_ubicacion(bin.getCod_ubicacion());
            binDTO.setTipo(bin.getTipo());
            binDTO.setVisualizado(bin.getVisualizado());
            String clave = "" + binDTO.getId_pedido() + bin.getPosicion();
            logger.debug("codigo bin: " + binDTO.getCod_bin());
            logger.debug("clave bin: " + clave);
            losBins.put(clave, binDTO);
        }

        //se agrega el detalle picking a cada bin
        for (int i = 0; i < detallePicking.size(); i++) {
            DetallePickingDTO detPickingDTO = (DetallePickingDTO) detallePicking.get(i);
            String clave = "" + detPickingDTO.getId_pedido() + detPickingDTO.getPosicion();
            logger.debug("detPicking clave bin: " + clave);
            BinDTO binDTO = (BinDTO) losBins.get(clave);
            if (binDTO != null)
                binDTO.addDetallePicking(detPickingDTO);
            else {
                logger.error("agregarDetalleABins: binDTO no debería ser nulo");
            }
        }

        return new ArrayList(losBins.values());
    }

    /**
     * Agrega toda la información necesaria para completar el detalle del
     * picking
     * @author jdroguett
     * @param prodDeLaRonda
     * @param prodPickeados
     * @return
     * @throws PedidosDAOException
     */
    private List construirDetallePicking(List prodDeLaRonda, List prodPickeados, long localId, JdbcPedidosDAO dao)
            throws PedidosDAOException {
        //Hashtable para obtener más rápido el producto de la ronda
        Hashtable detallesRonda = new Hashtable();
        for (int i = 0; i < prodDeLaRonda.size(); i++) {
            ProductosPedidoDTO prodRonda = (ProductosPedidoDTO) prodDeLaRonda.get(i);
            detallesRonda.put(new Long(prodRonda.getId_detalle()), prodRonda);
        }
        /*
         * La PDA entrega productos pickeado, en este código fuente se agrupan
         * productos identicos y se suman sus cantidades. También se busca el
         * sustituto cuando corresponde.
         */
        Hashtable detallePicking = new Hashtable();
        for (int i = 0; i < prodPickeados.size(); i++) {
            TPDetallePickingDTO prodPickeado = (TPDetallePickingDTO) prodPickeados.get(i);

            String clave = prodPickeado.getId_detalle() + prodPickeado.getCBarra() + prodPickeado.getPosicion();

            DetallePickingDTO detallePickingDTO = (DetallePickingDTO) detallePicking.get(clave);
            if (detallePickingDTO == null) {
                detallePickingDTO = new DetallePickingDTO();
                detallePickingDTO.setId_detalle(prodPickeado.getId_detalle());
                detallePickingDTO.setCBarra(prodPickeado.getCBarra());
                detallePickingDTO.setPosicion(prodPickeado.getPosicion());
                detallePickingDTO.setSustituto(prodPickeado.getTipo());
                detallePickingDTO.setCant_pick(0);

                ProductosPedidoDTO prodRonda = (ProductosPedidoDTO) detallesRonda.get(new Long(prodPickeado
                        .getId_detalle()));
                if (prodRonda == null) {
                    logger.error("construirDetallePicking: No se encontró el producto pickeado en la ronda. "
                            + "Este error no debería ocurrir ya que se validó en detallePickeadoCorrecto");
                }
                detallePickingDTO.setId_pedido(prodRonda.getId_pedido());
                detallePickingDTO.setId_producto(prodRonda.getId_producto());
                detallePickingDTO.setDescripcion(prodRonda.getDescripcion());
                detallePickingDTO.setPrecio(prodRonda.getPrecio());

                if (detallePickingDTO.getSustituto().equals(Constantes.DET_PICK_SUST)) {
                    agregarSustituto(detallePickingDTO, localId, dao);
                }
                //Auditado: por defecto es no
                detallePickingDTO.setAuditado(Constantes.AUDITADO_NO);
                if ( prodPickeado.getAuditado() != null ){
                    //la ronda se considera auditada si todos sus productos fueron auditados
                    //el primero debería ser SI para que la ronda se considere auditada
                    detallePickingDTO.setAuditado(prodPickeado.getAuditado());	
    			} 
                //fin auditado
            }
            
            //Auditado: si hay uno o más del mismo producto que no fue auditado 
            //se debe marcar que NO el detalle del producto
            if ( prodPickeado.getAuditado() != null && prodPickeado.getAuditado().equals(Constantes.AUDITADO_NO)){ 
                detallePickingDTO.setAuditado(Constantes.AUDITADO_NO);	
			}
            //fin auditado
            detallePickingDTO.setCant_pick(detallePickingDTO.getCant_pick() + prodPickeado.getCantidad());
            detallePicking.put(clave, detallePickingDTO);
        }
        return new ArrayList(detallePicking.values());
    }

    /**
     * Agregar el sustituto correspondiente
     * 
     * @author jdroguett
     * @param detPickingDTO
     * @param localId
     * @param dao
     * @throws PedidosDAOException
     */
    private void agregarSustituto(DetallePickingDTO detPickingDTO, long localId, JdbcPedidosDAO dao)
            throws PedidosDAOException {
        if (detPickingDTO.getCBarra().startsWith(Constantes.COD_BARRA_PESABLE_PREFIJO)) {
            String cod_barra = detPickingDTO.getCBarra().substring(0, Constantes.COD_BARRA_PESABLE_LONG);
            //obtiene el nuevo código de barra y dv
            String tmp_cod_barra = FormatUtils.addCharToString(cod_barra, "0", 12, FormatUtils.ALIGN_RIGHT);
            String dv = CalculosEAN.getEAN13dv(tmp_cod_barra);
            detPickingDTO.setCBarra(cod_barra + dv);
            logger.debug("Producto pesable. Nuevo Cod Barra:" + detPickingDTO.getCBarra());
        }
         
        ProductosPedidoDTO detSustituto = dao.getDetSustituto(detPickingDTO.getCBarra(), localId);
        if (detSustituto == null) {
            detPickingDTO.setId_producto(-1);
            detPickingDTO.setDescripcion(Constantes.SUSTITUTO_SIN_DESCRIP + " - " + detPickingDTO.getCBarra());
            detPickingDTO.setPrecio(0);
        } else {
            detPickingDTO.setId_producto(detSustituto.getId_producto());
            detPickingDTO.setDescripcion(detSustituto.getDescripcion());
            detPickingDTO.setPrecio(detSustituto.getPrecio());
        }
    }
    /*
    public static void main(String[] args) throws SystemException {
        System.out.println("COD BARRA");
        String[] cbarraOriginal = {
                "2439656",//9",
                "2485179",//2",
                "2480690",//5",
                "2480704",//7",
                "2401777",//8"
                "2487641"
                };
        
        for ( int i=0; i < cbarraOriginal.length; i++ ) {
            String cbarraFinal = "";
            System.out.print("cbarraOriginal:"+cbarraOriginal[i]+" -> ");
            if (cbarraOriginal[i].startsWith(Constantes.COD_BARRA_PESABLE_PREFIJO)) {
                String cod_barra = cbarraOriginal[i].substring(0, Constantes.COD_BARRA_PESABLE_LONG);
                //System.out.println("cod_barra:"+cod_barra);
                //obtiene el nuevo código de barra y dv
                System.out.print(" ("+cod_barra+") ");
                String tmp_cod_barra = FormatUtils.addCharToString(cod_barra, "0", 12, FormatUtils.ALIGN_RIGHT);
                System.out.print(" ("+tmp_cod_barra+") ");
                //System.out.println("tmp_cod_barra:"+tmp_cod_barra);
                String dv = CalculosEAN.getEAN13dv(tmp_cod_barra);
                cbarraFinal = cod_barra + dv;
            }
            System.out.println("cbarraFinal:"+cbarraFinal);
        }
        
        //Validator.
        
    }
*/
    /**
     * Cada bin sólo puede estar asociado a un pedido. <br>
     * Se valida que cada id de pedido de cada bin esté dentro de los pedidos de
     * la ronda. <br>
     * Es decir, no pueden venir bins con id de pedidos que no sean de la ronda.
     * <br>
     * @author jdroguett
     * @param productosDeLaRonda
     * @param bins
     * @return true si los id de pedidos de los bins pertenecen a la ronda.
     */
    private boolean binsPertenecenARonda(List productosDeLaRonda, List bins) {
        //Obtengo el conjunto de ids de pedidos de los productos que componen
        // la ronda.
        HashSet pedidosIds = new HashSet();
        for (int i = 0; i < productosDeLaRonda.size(); i++) {
            ProductosPedidoDTO prod = (ProductosPedidoDTO) productosDeLaRonda.get(i);
            pedidosIds.add(new Long(prod.getId_pedido()));
        }

        //se verifica que cada id de pedido de cada bins sean pedidos de la
        // ronda.
        for (int i = 0; i < bins.size(); i++) {
            TPBinOpDTO bin = (TPBinOpDTO) bins.get(i);
            Long pedidoIdDelBin = new Long(bin.getId_op());
            if (!pedidosIds.contains(pedidoIdDelBin))
                return false;
        }
        return true;
    }

    /**
     * Valida que los productos pickeado posean el mismo id_detalle de pedido de
     * los productos de la ronda.
     * 
     * @param productosDeLaRonda
     * @param productosPickeados
     * @return
     */
    private boolean detallePickeadoCorrecto(List productosDeLaRonda, List productosPickeados) {
        //Obtengo el conjunto de ids de detalle de los pedidos que componen la
        // ronda.
        HashSet detallesIds = new HashSet();
        for (int i = 0; i < productosDeLaRonda.size(); i++) {
            ProductosPedidoDTO prod = (ProductosPedidoDTO) productosDeLaRonda.get(i);
            detallesIds.add(new Long(prod.getId_detalle()));
        }

        //nota: en el código fuente antiguo también se valida junto con el
        // id_detalle el id_pedido, pero creo que no es necesario ya que el
        // id_detalle es único
        for (int i = 0; i < productosPickeados.size(); i++) {
            TPDetallePickingDTO prodPickeado = (TPDetallePickingDTO) productosPickeados.get(i);
            Long detalleIdPickeado = new Long(prodPickeado.getId_detalle());
            if (!detallesIds.contains(detalleIdPickeado))
                return false;
        }
        return true;
    }
	
	/**
	 * Permite actualizar la información de picking enviada por el PDA.<br>
	 * - Se actualizan la tabla correspondiente al detalle de pedido.<br>
	 * - Se insertan registros en la tabla de detalle de picking.<br>
	 * - Se insertan registros en la tabla de bin_op.<br>
	 * Nota: Este método tiene <b>Transaccionalidad</b>.
	 * 
	 * @param  dto ActDetallePickingDTO 
	 * @return List FaltanteDTO
	 * @throws RondasException, en uno de los siguientes casos:<br>
	 * - El Id de la ronda es inválida.<br>
	 * - El estado de la ronda es inadecuado.<br>
	 * - Relación entre la ronda y el pedido no existe.<br>
	 * - Relación entre el pedido y el detalle del pedido no existe.<br>
	 * - El pedido no existe.<br>
	 * @throws SystemException, en caso exista error de sistema.
	 * 
	 */
	
	public boolean doRecepcionaRonda(ActDetallePickingDTO dto) throws RondasException, SystemException{
		boolean result = false;
		JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		JdbcRondasDAO daoR = (JdbcRondasDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		// Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
		
		// Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		
		try {
			dao.setTrx(trx1);
		} catch (PedidosDAOException e2) {
			logger.error("Error al asignar transacción al dao Pedidos");
			throw new SystemException("Error al asignar transacción al dao Pedidos");
		}
		
		try {
			daoR.setTrx(trx1);
		} catch (RondasDAOException e2) {
			logger.error("Error al asignar transacción al dao Pedidos");
			throw new SystemException("Error al asignar transacción al dao Pedidos");
		}
		
		
		try{

			logger.debug(" *** Inicia doRecepcionaRonda *** ");
			//crear una lista de id_pedido
			HashMap lst_id_pedido 	= new HashMap();
			long id_pedido 		= -1;
			long id_ronda 		= dto.getId_ronda();
			
			logger.debug("id_ronda:"+id_ronda);
			
			//verificar si existe la ronda:
            //J: Entrega los datos principales de la ronda, no su detalle.
			RondaDTO rondaDto = daoR.getRondaById(id_ronda);
			
            //J:ronda no existe
			if(rondaDto==null){
                //J: ¿rollback de que? no ha modificado nada aún
				trx1.rollback();
				logger.debug(Constantes._EX_RON_ID_INVALIDO);
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO);
			}
			
            //J:la ronda debe estar en estado picking
			if(rondaDto.getId_estado()!=Constantes.ID_ESTADO_RONDA_EN_PICKING){
                //J: ¿rollback de que? no ha modificado nada aún
				trx1.rollback();
				logger.debug(Constantes._EX_RON_ESTADO_INADECUADO);
				throw new RondasException(Constantes._EX_RON_ESTADO_INADECUADO);
			}
			
			/*
			 * Fase 1 - Validaciones
			 * Revisar si la información contenida en el DTO esta completa 
			 * 
			 * */
			//Obtener los productos de la ronda:
			List lst_productos_ronda = daoR.getProductosRonda(rondaDto.getId_ronda());

			//obtener información del Dto del PDA
			List lst_bin_pda		 = dto.getLst_bin_op();
			List lst_det_pick_pda	 = dto.getLst_det_picking();
			List lst_det_ped_pda	 = dto.getLst_det_pedido();
			
			//verificar la información del listado de bins
            //J: un bin posee un sólo pedido, se verifica que cada bin contenga
            // productos correspondientes a los pedidos de la ronda (¿veamos
            // para que?)
			logger.debug("Inicio - Verifica listado de bins");
			boolean existeRelacion = false;
			for(int i=0; i<lst_bin_pda.size();i++){
				TPBinOpDTO binOp = (TPBinOpDTO)lst_bin_pda.get(i);
				int j=0;
				while(j<lst_productos_ronda.size() && !existeRelacion){
					ProductosPedidoDTO prod = (ProductosPedidoDTO)lst_productos_ronda.get(j);
					if(binOp.getId_op()==prod.getId_pedido()){
						//lst_id_pedido.add(new Long(binOp.getId_op()));
						existeRelacion = true;
					}
					j++;
				}
				if(!existeRelacion){
                    //J: ¿rollback de que? no ha modificado nada aún
					trx1.rollback();
					throw new RondasException(Constantes._EX_OPE_REL_RONDA_NO_EXISTE);
				}
				existeRelacion= false;
			}
			logger.debug("Fin - Verifica listado de bins");

			//verificar la información del listado de detalle de picking
            //J:verifica que los productos pickeados correspondan a los
            // productos del detalle del pedido
            /////////////////////////////////////////////////////////////////////////////////////////
            //J: creo que verifica que el id detalle del pedido corresponda al
            // pickeado.
            //Lo raro es que verifica el id_detalle y luego verifica el id
            // pedido cuando bastaría con el id_detalle,
            //ya que el id_detalle pertenece a un sólo pedido
			logger.debug("Inicio - Verifica listado de detalle picking");
			for(int i=0; i<lst_det_pick_pda.size();i++){
				existeRelacion = false;
				TPDetallePickingDTO detPickPOS = (TPDetallePickingDTO) lst_det_pick_pda.get(i);
				int j=0;
				while(j<lst_productos_ronda.size() && !existeRelacion){
					ProductosPedidoDTO prod = (ProductosPedidoDTO)lst_productos_ronda.get(j);
					//ver si tienen el mismo id_detalle
					if(detPickPOS.getId_detalle()==prod.getId_detalle()){
						//obtener el id_op
						//long id_op = -1;
						/*int k=0;
						while(k<lst_bin_pda.size() && id_op == -1){
							TPBinOpDTO binOp = (TPBinOpDTO)lst_bin_pda.get(k);
							if(detPickPOS.getPosicion()==binOp.getPosicion()){
								id_op = binOp.getId_op();
							}
							k++;
						}*/
						
						//De la lst_detalle_pedido_pda, obtener el id_pedido, segun el id_detalle
						long id_pedido_pda = 0;
						int m=0;
						while( (m < lst_det_ped_pda.size()) && (id_pedido_pda == 0) ){
						//for(int m=0; m<lst_det_ped_pda.size(); m++){
							TPDetallePedidoDTO detPedPOS = (TPDetallePedidoDTO)lst_det_ped_pda.get(m);
							//logger.debug("m:"+m+", detPedPOS.getId_detalle():"+detPedPOS.getId_detalle()+", detsum.getId_detalle():"+detsum.getId_detalle());
							if(detPedPOS.getId_detalle()==detPickPOS.getId_detalle()){
								id_pedido_pda = detPedPOS.getId_op();
							}
							m++;
						}
						
						logger.debug("id_detalle:"+detPickPOS.getId_detalle()+" --> id_pedido del detalle:"+id_pedido_pda);
						
						//if(prod.getId_pedido()==id_op){
						if(prod.getId_pedido()==id_pedido_pda){
							existeRelacion = true;
						}else{
							logger.debug("id_detalle c/error:"+detPickPOS.getId_detalle());
							trx1.rollback();
							throw new RondasException(Constantes._EX_OPE_REL_DET_NO_EXISTE);
						}
					}
					j++;
				}
				if(!existeRelacion){
					logger.debug("id_detalle c/error:"+detPickPOS.getId_detalle());
					trx1.rollback();
					throw new RondasException(Constantes._EX_OPE_REL_DET_NO_EXISTE);
				}
			}
			logger.debug("Fin - Verifica listado de detalle de picking");
			
			//verificar la informacion del listado de detalle de pedido
			logger.debug("Inicio - Verifica listado de detalle de pedido");
			for(int i=0; i<lst_det_ped_pda.size();i++){
				existeRelacion = false;
				TPDetallePedidoDTO detPOS = (TPDetallePedidoDTO)lst_det_ped_pda.get(i);
				int j=0;
				while( j<lst_productos_ronda.size()){
					ProductosPedidoDTO prod = (ProductosPedidoDTO)lst_productos_ronda.get(j);
					if( (detPOS.getId_op()==prod.getId_pedido()) && (detPOS.getId_detalle()==prod.getId_detalle()) && 
							(detPOS.getId_dronda()==prod.getId_dronda()) ){
						existeRelacion = true;
					}
					j++;
				}
				if(!existeRelacion){
					logger.debug("id_detalle c/error: id_det:"+detPOS.getId_detalle()+", id_op:"+detPOS.getId_op()+", id_dronda:"+detPOS.getId_dronda());
					trx1.rollback();
					throw new RondasException(Constantes._EX_OPE_REL_DET_NO_EXISTE);
				}
			}
			logger.debug("Fin - Verifica listado de detalle de pedido");
				
			//obtener la lista del pedido, del listado del detalle del pedido
			logger.debug("Inicio - Obtiene listado de pedido");
			for(int i=0; i<lst_det_ped_pda.size();i++){
				TPDetallePedidoDTO detPOS = (TPDetallePedidoDTO)lst_det_ped_pda.get(i);
				
				if (lst_id_pedido.get(new Long(detPOS.getId_op())) == null){
				    lst_id_pedido.put(new Long(detPOS.getId_op()), new Long(detPOS.getId_op()));
				}
				
				/*if( (i > 0) && (lst_id_pedido.size()>0) ){
					//revisa el listado de pedido
					for(int j=0;j<lst_id_pedido.size();j++){
						long aux_id_pedido = ((Long)lst_id_pedido.get(j)).longValue();
						if (detPOS.getId_op()!= aux_id_pedido){
							is_nvo_id_pedido = true;
						}
					}
					if(is_nvo_id_pedido){
						lst_id_pedido.add(new Long(detPOS.getId_op()));
					}
				}else{
					//agrega el primer elemento id_pedido al listado
					lst_id_pedido.add(new Long(detPOS.getId_op()));
				}*/
			}
			logger.debug("Fin - Obtiene listado de pedido");
			
			/*
			 * Fase 2 - Recopilar información
			 * Obtener la información necesaria para la inserción 
			 * 
			 * */
			/* 
			 * 1. insertar los bin_op
			 */
			//devuelve lista bin del POS con id_bin generados
			logger.debug("Inserta listado de bins");
            //J: inserta los bins de la ronda en la base de datos, Retorna los
            // bins con su id resultado de la inserción
			List lst_bin_pos_nvo = dao.addBinsPedidoPOS(lst_bin_pda, id_ronda);
			

			/*
			 * obtener la información del detalle de picking 
			 */
			 //tamaño de detalle picking
			logger.debug("Inicio - Construye listado sumarizado de detalle de picking");
			logger.debug("lst_det_pick_pda.size():"+lst_det_pick_pda.size());
			List lst_det_pick_sum 	= new ArrayList();
			for(int i=0; i<lst_det_pick_pda.size(); i++) {
				boolean encontro = false;
				TPDetallePickingDTO detPickPOS = (TPDetallePickingDTO) lst_det_pick_pda.get(i);
				logger.debug("i:"+i+" detPickPOS.getId_detalle():" + detPickPOS.getId_detalle());
				logger.debug("lst_det_pick_sum.size()"+lst_det_pick_sum.size());
				if( (i > 0) && (lst_det_pick_sum.size()>0) ){
					for(int j=0;j<lst_det_pick_sum.size();j++){
						DetallePickingDTO detsum = (DetallePickingDTO) lst_det_pick_sum.get(j);
						//logger.debug("j:"+j+" detsum.getId_detalle():"+detsum.getId_detalle());
						//agregar la condicion: segun posicion en el bin
						if( (detPickPOS.getId_detalle()==detsum.getId_detalle()) && 
								(detPickPOS.getCBarra().equals(detsum.getCBarra())) && 
								(detPickPOS.getPosicion()==detsum.getPosicion()) ){
							//setear la informacion segun detPickPOS
							detsum.setId_detalle(detPickPOS.getId_detalle());
							detsum.setCBarra(detPickPOS.getCBarra());
							detsum.setSustituto(detPickPOS.getTipo());
							detsum.setPosicion(detPickPOS.getPosicion());
							//	---------- mod_ene09 - ini------------------------
							if ((detPickPOS.getAuditado()!=null) && 
								(detPickPOS.getAuditado().equals(Constantes.AUDITADO_SI))&& 
								(detsum.getAuditado().equals(Constantes.AUDITADO_SI))){
									detsum.setAuditado(Constantes.AUDITADO_SI);	
								} 
							else{ 		
									detsum.setAuditado(Constantes.AUDITADO_NO);
								} 
							//	---------- mod_ene09 - ini------------------------
							
							//De la lst_detalle_pedido_pda, obtener el id_pedido, segun el id_detalle
							long id_pedido_pda = 0;
							int m=0;
							while( (m < lst_det_ped_pda.size()) && (id_pedido_pda == 0) ){
							//for(int m=0; m<lst_det_ped_pda.size(); m++){
								TPDetallePedidoDTO detPedPOS = (TPDetallePedidoDTO)lst_det_ped_pda.get(m);
								//logger.debug("m:"+m+", detPedPOS.getId_detalle():"+detPedPOS.getId_detalle()+", detsum.getId_detalle():"+detsum.getId_detalle());
								if(detPedPOS.getId_detalle()==detsum.getId_detalle()){
									id_pedido_pda = detPedPOS.getId_op();
								}
								m++;
							}
							
							logger.debug("id_detalle:"+detsum.getId_detalle()+" --> id_pedido del detalle:"+id_pedido_pda);
							
							//colocar información del bin en el detalle sumarizado
							for(int k=0; k<lst_bin_pos_nvo.size(); k++){
								TPBinOpDTO binPOS = (TPBinOpDTO)lst_bin_pos_nvo.get(k); 
								//ver posicion del bin y id_pedido para obtener id_bin respectivo
								if( (binPOS.getPosicion() == detPickPOS.getPosicion()) &&
										(binPOS.getId_op() == id_pedido_pda) ){
									detsum.setId_bp(binPOS.getId_bin());
									detsum.setId_pedido(binPOS.getId_op());
								}
							}
							//acumular cantidades, directamente del detalle picking del PDA
							detsum.setCant_pick(detsum.getCant_pick()+detPickPOS.getCantidad());
							
							lst_det_pick_sum.remove(j);
							lst_det_pick_sum.add(j,detsum);

							encontro = true;
						}
					}//fin del for del lst_det_pick_sum
					
					logger.debug("encontro:"+encontro);
					if(!encontro){
						//id_detalle es diferente: setear los valores y agregar a listasum
						//DetallePickingDTO nvoDet = convierteDetPickDTO(detPickPOS, lst_bin_pos_nvo);
						DetallePickingDTO nvoDet = new DetallePickingDTO();
						nvoDet.setId_detalle(detPickPOS.getId_detalle());
						nvoDet.setCBarra(detPickPOS.getCBarra());
						nvoDet.setSustituto(detPickPOS.getTipo());
						nvoDet.setPosicion(detPickPOS.getPosicion());
						//  ---------- mod_ene09 - ini------------------------
						if ((detPickPOS.getAuditado()!=null) && (detPickPOS.getAuditado().equals(Constantes.AUDITADO_SI))){
							nvoDet.setAuditado(Constantes.AUDITADO_SI);
							} 
						else{	
							nvoDet.setAuditado(Constantes.AUDITADO_NO);
							} 
						//	---------- mod_ene09 - ini------------------------
						//De la lst_detalle_pedido_pda, obtener el id_pedido, segun el id_detalle
						long id_pedido_pda = 0;
						int m=0;
						while( (m < lst_det_ped_pda.size()) && (id_pedido_pda == 0) ){
						//for(int m=0; m<lst_det_ped_pda.size(); m++){
							TPDetallePedidoDTO detPedPOS = (TPDetallePedidoDTO)lst_det_ped_pda.get(m);
							//logger.debug("m:"+m+", detPedPOS.getId_detalle():"+detPedPOS.getId_detalle()+", nvoDet.getId_detalle():"+nvoDet.getId_detalle());
							if(detPedPOS.getId_detalle()==nvoDet.getId_detalle()){
								id_pedido_pda = detPedPOS.getId_op();
							}
							m++;
						}
						logger.debug("id_detalle:"+nvoDet.getId_detalle()+" --> id_pedido del detalle:"+id_pedido_pda);

						//colocar información del bin en el detalle sumarizado
						for(int k=0; k<lst_bin_pos_nvo.size(); k++){
							TPBinOpDTO binPOS = (TPBinOpDTO)lst_bin_pos_nvo.get(k); 
							//ver posicion del bin y id_pedido para obtener id_bin respectivo
							if( (binPOS.getPosicion() == detPickPOS.getPosicion()) &&
									(binPOS.getId_op() == id_pedido_pda) ){
								nvoDet.setId_bp(binPOS.getId_bin());
								nvoDet.setId_pedido(binPOS.getId_op());
							}
						}
						//acumular cantidades, directamente del detalle picking del PDA
						nvoDet.setCant_pick(detPickPOS.getCantidad());
						
						//agregar a la lista
						lst_det_pick_sum.add(nvoDet);
						logger.debug("se agrego detalle:"+nvoDet.getId_detalle());
					}
				}else{
					//pasar el primer elemento, de tipo DetallePickingDTO 
					//DetallePickingDTO nvoDet = convierteDetPickDTO(detPickPOS, lst_bin_pos_nvo);		// OJO!!
					DetallePickingDTO nvoDet = new DetallePickingDTO();
					nvoDet.setId_detalle(detPickPOS.getId_detalle());
					nvoDet.setCBarra(detPickPOS.getCBarra());
					nvoDet.setSustituto(detPickPOS.getTipo());
					nvoDet.setPosicion(detPickPOS.getPosicion());
					//  ---------- mod_ene09 - ini------------------------
					if ((detPickPOS.getAuditado()!=null) && (detPickPOS.getAuditado().equals(Constantes.AUDITADO_SI))){
						nvoDet.setAuditado(Constantes.AUDITADO_SI);
						} 
					else{	
						nvoDet.setAuditado(Constantes.AUDITADO_NO);
						} 
					//	---------- mod_ene09 - ini------------------------
					//De la lst_detalle_pedido_pda, obtener el id_pedido, segun el id_detalle
					long id_pedido_pda = 0;
					int m=0;
					while( (m < lst_det_ped_pda.size()) && (id_pedido_pda == 0) ){
					//for(int m=0; m<lst_det_ped_pda.size(); m++){
						TPDetallePedidoDTO detPedPOS = (TPDetallePedidoDTO)lst_det_ped_pda.get(m);
						//logger.debug("m:"+m+", detPedPOS.getId_detalle():"+detPedPOS.getId_detalle()+", nvoDet.getId_detalle():"+nvoDet.getId_detalle());
						if(detPedPOS.getId_detalle()==nvoDet.getId_detalle()){
							id_pedido_pda = detPedPOS.getId_op();
						}
						m++;
					}
					logger.debug("id_detalle:"+nvoDet.getId_detalle()+" --> id_pedido del detalle:"+id_pedido_pda);

					//colocar información del bin en el detalle sumarizado
					for(int k=0; k<lst_bin_pos_nvo.size(); k++){
						TPBinOpDTO binPOS = (TPBinOpDTO)lst_bin_pos_nvo.get(k); 
						//ver posicion del bin y id_pedido para obtener id_bin respectivo
						if( (binPOS.getPosicion() == detPickPOS.getPosicion()) &&
								(binPOS.getId_op() == id_pedido_pda) ){
							nvoDet.setId_bp(binPOS.getId_bin());
							nvoDet.setId_pedido(binPOS.getId_op());
						}
					}
					//acumular cantidades, directamente del detalle picking del PDA
					nvoDet.setCant_pick(detPickPOS.getCantidad());

					//agregar a la lista sumarizada
					lst_det_pick_sum.add(nvoDet);
					logger.debug("se agrego 1er detalle:"+nvoDet.getId_detalle());
				}
			}
			logger.debug("Fin - Construye listado sumarizado de detalle de picking");	

			/*
			 * Fase 3 - Persistencia y Operaciones
			 * Insertar la información del DTO en BD. 
			 * 
			 * */
			
			
			/* 
			 * 2. Insertar en Detalle picking
			 */
			//insertar la lista de picking sumarizada
			logger.debug("Inicio - Inserta listado sumarizado de detalle de picking");
			for(int i=0;i<lst_det_pick_sum.size();i++){
				DetallePickingDTO detPickDto = (DetallePickingDTO) lst_det_pick_sum.get(i);

				//obtener informacion para detalle picking, y en caso sea sustituto, obtener informacion del sustituto 
				logger.debug("detalle:"+detPickDto.getId_detalle()+", cant:"+detPickDto.getCant_pick());
				
				//obtener el detalle del pedido:
				ProductosPedidoDTO detalle = dao.getDetallePedidoById(detPickDto.getId_detalle());
				
				
				//si el tipo de producto es normal, obtener id del detalle, 
				if(detPickDto.getSustituto().equals(Constantes.DET_PICK_NORMAL)){
					detPickDto.setId_producto(detalle.getId_producto());	
					detPickDto.setDescripcion(detalle.getDescripcion());	
					//obtener el precio del detalle del pedido
					detPickDto.setPrecio(detalle.getPrecio());
				} //si el tipo de producto es sustituto, obtener el id del cod_barra 
				else if (detPickDto.getSustituto().equals(Constantes.DET_PICK_SUST)){
					/*
					 * Revisar el codigo de barras, si comienza con '24', se considera como producto pesable y se cambia el cod. de barra
					 * 
					 * */
					//
					logger.debug("codigo de barras:"+detPickDto.getCBarra());
					if(detPickDto.getCBarra().startsWith(Constantes.COD_BARRA_PESABLE_PREFIJO)){
						String cod_barra= detPickDto.getCBarra().substring(0,Constantes.COD_BARRA_PESABLE_LONG);
						//obtiene el nuevo código de barra 
						String tmp_cod_barra = FormatUtils.addCharToString(cod_barra,"0",12,FormatUtils.ALIGN_RIGHT);
						String dv = CalculosEAN.getEAN13dv(tmp_cod_barra);
						//obtener el digito verificador
						detPickDto.setCBarra(cod_barra + dv);
						logger.debug("Producto pesable. Nuevo Cod Barra:"+detPickDto.getCBarra());
					}
					
					//obtener datos del producto, segun el codigo de barra y el id_local del pedido
					long id_local = dao.getPedidoById(detalle.getId_pedido()).getId_local();
					ProductosPedidoDTO detSusti = dao.getDetSustituto(detPickDto.getCBarra(),id_local);
					if(detSusti==null){
						//throw new PedidosException(Constantes._EX_CODBARRA_NO_EXISTE);
						detPickDto.setId_producto(-1);
						detPickDto.setDescripcion(Constantes.SUSTITUTO_SIN_DESCRIP+" - "+detPickDto.getCBarra());	
						detPickDto.setPrecio(0);
					} else {
						detPickDto.setId_producto(detSusti.getId_producto());
						detPickDto.setDescripcion(detSusti.getDescripcion());
						detPickDto.setPrecio(detSusti.getPrecio());
					}
				}

				long id_det_pick = dao.addDetallePicking(detPickDto);
				logger.debug("id_det_pick:"+id_det_pick);
			}
			logger.debug("Fin - Inserta listado sumarizado de detalle de picking");

			
			/* 
			 * 3. Actualizar en Detalle de ronda
			 */
			//List lst_detalle_pedido_pos = dto.getLst_det_pedido();
			logger.debug("Inicio - Actualiza detalle de ronda");
			for(int i=0; i<lst_det_ped_pda.size(); i++){
				TPDetallePedidoDTO detPOS = (TPDetallePedidoDTO)lst_det_ped_pda.get(i);
				logger.debug("detalle_pedido:"+detPOS.getId_detalle());
				DetalleRondaDTO detRndDto = new DetalleRondaDTO();
				detRndDto.setId_ronda(id_ronda);
				//detRndDto.setId_dronda(detPOS.getId_detalle());
				detRndDto.setId_dronda(detPOS.getId_dronda());
				detRndDto.setCant_pick(detPOS.getCant_pickeada());
				detRndDto.setCant_faltan(detPOS.getCant_faltante());
				detRndDto.setCant_spick(detPOS.getCant_sinpickear());
				
				//obtener el campo sustituto, de la lista de detalle_picking sumarizado
				String sustituto = Constantes.DET_PICK_NORMAL;
				for(int j=0;j<lst_det_pick_sum.size();j++){
					DetallePickingDTO detPickDto = (DetallePickingDTO) lst_det_pick_sum.get(j);
					//if(detPickDto.getSustituto().equals(Constantes.DET_PICK_SUST)){
					if( (detPOS.getId_detalle()==detPickDto.getId_detalle()) && detPickDto.getSustituto().equals(Constantes.DET_PICK_SUST)){
						sustituto = detPickDto.getSustituto();
					}
				}
				detRndDto.setSustituto(sustituto);
				//setear el motivo de sustitucion
				detRndDto.setMot_sustitucion(detPOS.getMot_sustitucion());
				
				boolean actDetRonda = dao.actDetalleRonda(detRndDto);
				logger.debug("actDetRonda?"+actDetRonda);
			}
			logger.debug("Fin - Actualiza detalle de ronda");

			/* 
			 * 4. Actualizar en Ronda (Se finaliza la ronda)
			 */
			daoR.doFinalizaRonda(id_ronda);
			//boolean actEstRonda = dao.actEstadoROndaById(dto.getId_ronda(),Constantes.ID_ESTADO_RONDA_FINALIZADA+"");
			
			//logger.debug("actEstRonda?"+actEstRonda);

			/* 
			 * 5. Actualizar en Detalle pedido
			 */
			//List lst_det_ped_pos = dto.getLst_det_pedido();
			/*logger.debug("Inicio - Actualiza detalle de pedido");
			for(int i=0; i<lst_det_ped_pda.size(); i++){
				TPDetallePedidoDTO detPOS = (TPDetallePedidoDTO)lst_det_ped_pda.get(i); 
				ProductosPedidoDTO detalle = new ProductosPedidoDTO();
				detalle.setId_detalle(detPOS.getId_detalle());
				detalle.setCant_pick(detPOS.getCant_pickeada());
				detalle.setCant_faltan(detPOS.getCant_faltante());
				if( detPOS.getSust_camb_form()==Constantes.SUST_CAMB_FORM_EXISTE ){
					//actualizar la cantidad sin pickear a 0
					detalle.setCant_spick(0);
					boolean actCantSinPick = dao.actCantSinPickearDetallePedido(detalle);
					logger.debug("SustyCmbForm: actCant? "+actCantSinPick);
				}else{
					//detalle.setCant_spick(detPOS.getCant_sinpickear());
					//boolean actCant = dao.actCantDetallePedido(detalle);
					//obtener cantidad sin pickear
					double cant_spick = Formatos.formatoNum3Dec(detPOS.getCant_pedida() - detPOS.getCant_pickeada() - detPOS.getCant_faltante());
					if(cant_spick < 0){
						logger.debug("cant_spick en neg:"+cant_spick+", debe ser 0.");
						cant_spick = 0;
					}
					logger.debug("id_detalle:"+detPOS.getId_detalle()+", cant_spick:"+cant_spick);
					detalle.setCant_spick(cant_spick);
					
					//obtener cantidad faltante minimo, segun el porcentaje a aplicar en la cantidad pedida
					logger.debug("getCant_pedida: "+detPOS.getCant_pedida());
					double cant_falt_min = Formatos.formatoNum3Dec(detPOS.getCant_pedida()*(double)(Constantes.PORCENTAJE_APROX_FALTANTES)/100);
					//double cant_falt_max = Formatos.formatoNum3Dec(detPOS.getCant_pedida()*(double)(Constantes.PORCENTAJE_APROX_MAX_FALTANTES)/100);
					logger.debug("cant_falt: "+detalle.getCant_faltan());
					logger.debug("cant_falt_min: "+cant_falt_min);
					//logger.debug("cant_falt_max: "+cant_falt_max);
					if( (detalle.getCant_faltan() < cant_falt_min) ){
						detalle.setCant_faltan(0);
					}
					if( (detalle.getCant_spick() < cant_falt_min) ){
						detalle.setCant_spick(0);
					}
					
					boolean actCant = dao.actCantSinPickearDetallePedido(detalle);
					logger.debug("Comunes: actCant? "+actCant);
				}
				
			}
			logger.debug("Fin - Actualiza detalle de pedido");*/
			
			/* 
			 * 6. Actualizar en Pedido
			 * si y solo si:
			 * - todas las cantidades sin pickear de sus productos sean cero, y
			 * - todas sus rondas de picking estan finalizadas
			 */
			logger.debug("Inicio - Actualiza pedidos");
			String login = "";
			
		    // Iterate over the values in the map
		    //it = map.values().iterator();
		    // Iterate over the keys in the map
			//XXX
		    Iterator it = lst_id_pedido.keySet().iterator();
		    while (it.hasNext()) {
		        id_pedido = ((Long)it.next()).longValue();
				List lst_reg_pick = dto.getLst_reg_picking();
				for(int j=0; j<lst_reg_pick.size(); j++){
					TPRegistroPickingDTO regPick = (TPRegistroPickingDTO)lst_reg_pick.get(j); 
					LogPedidoDTO log = new LogPedidoDTO();
					log.setId_pedido(id_pedido);
					login = regPick.getUsuario();
					log.setUsuario(login);
					PerfilesEntity prf = dao.getPerfilById(regPick.getPerfil());
					log.setLog(prf.getNombre()+" realizó operación a las " + regPick.getHora());
					dao.addLogPedido(log);
				}
		    }
			
			//for(int i=0; i<lst_id_pedido.size(); i++){
			//	id_pedido = ((Long)lst_id_pedido.get(i)).longValue();
				/*double cant_spick_total = dao.getCantSinPickearByPedido(id_pedido);
				boolean finalizoRondas = dao.isFinalizadoRondasByPedido(id_pedido);
				logger.debug("cant_spick_total:"+cant_spick_total);
				logger.debug("finalizoRondas?"+finalizoRondas);
				if( (cant_spick_total==0) && finalizoRondas ){
					PedidoDTO pedido = new PedidoDTO();
					pedido.setId_pedido(id_pedido);
					int cant_bins = dao.getCantBinsByPedido(id_pedido);
					pedido.setCant_bins(cant_bins);
					pedido.setId_estado(Constantes.ID_ESTAD_PEDIDO_EN_BODEGA);
					boolean actPedido = dao.verificaPedidoPickeado(pedido);
					logger.debug("actPedido?"+actPedido);
					if (dao.tieneTodosFaltantes(id_pedido)) {
				    	int id_alerta = Constantes.ALE_OP_TODOS_FALTANTES;
				    	dao.addAlertToPedido(id_pedido,id_alerta);
					    logger.info("Alerta Pedido con Todos sus Productos Faltantes");
					}
				}*/
				//colocar el log correpondiente segun la lista de registro picking
			//	List lst_reg_pick = dto.getLst_reg_picking();
			//	for(int j=0; j<lst_reg_pick.size(); j++){
			//		TPRegistroPickingDTO regPick = (TPRegistroPickingDTO)lst_reg_pick.get(j); 
			//		LogPedidoDTO log = new LogPedidoDTO();
			//		log.setId_pedido(id_pedido);
			//		login = regPick.getUsuario();
			//		log.setUsuario(login);
			//		PerfilesEntity prf = dao.getPerfilById(regPick.getPerfil());
			//		log.setLog(prf.getNombre()+" realizó operación a las "+regPick.getHora());
			//		dao.addLogPedido(log);
			//	}
			//}
			//logger.debug("Fin - Actualiza pedidos");
			
			//Agregar evento Descarga de Ronda al log 

			//	---------- mod_ene09 - ini------------------------
			//Actualiza fiscalizador y estado en la ronda
		    List lst_reg_pick = dto.getLst_reg_picking();
			for(int j=0; j<lst_reg_pick.size(); j++){
				TPRegistroPickingDTO regPick = (TPRegistroPickingDTO)lst_reg_pick.get(j);
				
				RondaDTO ronda_final = new RondaDTO();
				ronda_final.setId_ronda(regPick.getRonda());
				try {
					ronda_final.setFiscalizador(daoR.getUserIdByLogin(regPick.getUsuario_fiscalizador()));
				} catch (UsuariosDAOException e) {
					logger.error("El fiscalizador "+regPick.getUsuario_fiscalizador()+"no existe en la tabla de usuarios");
					ronda_final.setFiscalizador(0);
				}
				ronda_final.setE1(regPick.getE1());
				ronda_final.setE2(regPick.getE2());
				ronda_final.setE3(regPick.getE3());
				ronda_final.setE4(regPick.getE4());
				ronda_final.setE5(regPick.getE5());
				ronda_final.setE6(regPick.getE6());
				ronda_final.setE7(regPick.getE7());
				daoR.updRondaFinal(ronda_final);
			}
				
			
			//	---------- mod_ene09 - fin------------------------
		    
			daoR.doAddLogRonda(id_ronda,login,Constantes.RONDA_MNS_FIN_DESCARGA);
			result = true;
		} catch (PedidosDAOException ex) {
			//rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			
			logger.debug("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe cod del pedido");
				throw new RondasException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
			}
			throw new SystemException("Error no controlado",ex);
		} catch (RondasDAOException ex) {
			//rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			logger.debug("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe cod de la ronda");
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO, ex);
			}
			throw new SystemException("Error no controlado",ex);
		} catch (DAOException e) {
			logger.error("Error al hacer rollback");
			throw new SystemException("Error al hacer rollback");
		}
		//		 cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
		logger.debug(" *** Finaliza doRecepcionaRonda *** ");
		return result;
		
	}


	public boolean doActDetallePedido(ActDetallePickingDTO dto) throws RondasException, SystemException{
		boolean result = false;
		JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		long id_ronda 		= dto.getId_ronda();
		
		try{
			logger.debug(" *** Inicia doActDetallePedido *** ");
			logger.debug("id_ronda:"+id_ronda);
			//obtener información del Dto del PDA
			List lst_det_ped_pda	 = dto.getLst_det_pedido();
			/* 
			 * 5. Actualizar en Detalle pedido
			 */
			//List lst_det_ped_pos = dto.getLst_det_pedido();
			logger.debug("Inicio - Actualiza detalle de pedido");
			for(int i=0; i<lst_det_ped_pda.size(); i++){
				TPDetallePedidoDTO detPOS = (TPDetallePedidoDTO)lst_det_ped_pda.get(i); 
				ProductosPedidoDTO detalle = new ProductosPedidoDTO();
				detalle.setId_detalle(detPOS.getId_detalle());
				detalle.setCant_pick(detPOS.getCant_pickeada());
				detalle.setCant_faltan(detPOS.getCant_faltante());
				if( detPOS.getSust_camb_form()==Constantes.SUST_CAMB_FORM_EXISTE ){
					//actualizar la cantidad sin pickear a 0
					detalle.setCant_spick(0);
					boolean actCantSinPick = dao.actCantSinPickearDetallePedido(detalle);
					logger.debug("SustyCmbForm: actCant? "+actCantSinPick);
				}else{
					//detalle.setCant_spick(detPOS.getCant_sinpickear());
					//boolean actCant = dao.actCantDetallePedido(detalle);
					//obtener cantidad sin pickear
					double cant_spick = Formatos.formatoNum3Dec(detPOS.getCant_pedida() - detPOS.getCant_pickeada() - detPOS.getCant_faltante());
					if(cant_spick < 0){
						logger.debug("cant_spick en neg:"+cant_spick+", debe ser 0.");
						cant_spick = 0;
					}
					logger.debug("id_detalle:"+detPOS.getId_detalle()+", cant_spick:"+cant_spick);
					detalle.setCant_spick(cant_spick);
					
					//obtener cantidad faltante minimo, segun el porcentaje a aplicar en la cantidad pedida
					logger.debug("getCant_pedida: "+detPOS.getCant_pedida());
					double cant_falt_min = Formatos.formatoNum3Dec(detPOS.getCant_pedida()*(double)(Constantes.PORCENTAJE_APROX_FALTANTES)/100);
					//double cant_falt_max = Formatos.formatoNum3Dec(detPOS.getCant_pedida()*(double)(Constantes.PORCENTAJE_APROX_MAX_FALTANTES)/100);
					logger.debug("cant_falt: "+detalle.getCant_faltan());
					logger.debug("cant_falt_min: "+cant_falt_min);
					//logger.debug("cant_falt_max: "+cant_falt_max);
					if( (detalle.getCant_faltan() < cant_falt_min) ){
						detalle.setCant_faltan(0);
					}
					if( (detalle.getCant_spick() < cant_falt_min) ){
						detalle.setCant_spick(0);
					}
					boolean actCant = dao.actCantSinPickearDetallePedido(detalle);
					logger.debug("Comunes: actCant? "+actCant);
				}
			}
			logger.debug("Fin - Actualiza detalle de pedido");
			result = true;
		} catch (PedidosDAOException ex) {
			logger.debug("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe cod del pedido");
				throw new RondasException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
			}
			throw new SystemException("Error no controlado",ex);
		}
		logger.debug(" *** Finaliza doRecepcionaRonda *** ");
		return result;
	}
	
	
	public boolean doActEstadoPedido(ActDetallePickingDTO dto) throws RondasException, SystemException{
		boolean result = false;
		JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		long id_ronda 		= dto.getId_ronda();
		// Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
		
		// Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		
		try {
			dao.setTrx(trx1);
		} catch (PedidosDAOException e2) {
			logger.error("Error al asignar transacción al dao Pedidos");
			throw new SystemException("Error al asignar transacción al dao Pedidos");
		}

		try{
			logger.debug(" *** Inicia doActEstadoPedido *** ");
			logger.debug("id_ronda:"+id_ronda);
			HashMap lst_id_pedido = new HashMap();
			List lst_det_ped_pda  = dto.getLst_det_pedido();

			long id_pedido 		= -1;
			
            //crear una lista de id_pedido
			for(int i=0; i<lst_det_ped_pda.size();i++){
				TPDetallePedidoDTO detPOS = (TPDetallePedidoDTO)lst_det_ped_pda.get(i);
				if (lst_id_pedido.get(new Long(detPOS.getId_op())) == null){
				    lst_id_pedido.put(new Long(detPOS.getId_op()), new Long(detPOS.getId_op()));
				}
			}
			/* 
			 * 6. Actualizar en Pedido
			 * si y solo si:
			 * - todas las cantidades sin pickear de sus productos sean cero, y
			 * - todas sus rondas de picking estan finalizadas
			 */
			logger.debug("Inicio - Actualiza pedidos");
			
		    // Iterate over the keys in the map
		    Iterator it = lst_id_pedido.keySet().iterator();
		    while (it.hasNext()) {
		        id_pedido = ((Long)it.next()).longValue();
		        
				double cant_spick_total = dao.getCantSinPickearByPedido(id_pedido);
				boolean finalizoRondas = dao.isFinalizadoRondasByPedido(id_pedido);
				logger.debug("cant_spick_total:"+cant_spick_total);
				logger.debug("finalizoRondas?"+finalizoRondas);
				if( (cant_spick_total==0) && finalizoRondas){
                    //Esto se comenta, ya que el metodo subeRonda debe tener toda la logica
//					PedidoDTO pedido = new PedidoDTO();
//					pedido.setId_pedido(id_pedido);
//					int cant_bins = dao.getCantBinsByPedido(id_pedido);
//					pedido.setCant_bins(cant_bins);
//					pedido.setId_estado(Constantes.ID_ESTAD_PEDIDO_EN_BODEGA);
//                    logger.debug("vamos a updatear ID PEDIDO:"+pedido.getId_pedido()+", ESTADO:"+pedido.getId_estado()+", CANT_BINS:"+pedido.getCant_bins());
//                    boolean actPedido = dao.verificaPedidoPickeado(pedido);
//					logger.debug("actPedido?"+actPedido);
//					if (dao.tieneTodosFaltantes(id_pedido)) {
//				    	int id_alerta = Constantes.ALE_OP_TODOS_FALTANTES;
//				    	dao.addAlertToPedido(id_pedido,id_alerta);
//					    logger.info("Alerta Pedido con Todos sus Productos Faltantes");
//					}
					
                    /* ************************************************************
                     * Aqui se debe realizar el prorrateo de las promociones(INICIO)
                     *************************************************************/
                    logger.debug("Comienza el prorrateo...");
                    boolean aplico = aplicarProrrateo(id_pedido, trx1);
                    if ( aplico ) {
                        logger.info("** Aplico prorrateo **");
                    } else {
                        logger.info("** No aplico prorrateo **");
                    }                    
                    /* ************************************************************
                     * Aqui se debe realizar el prorrateo de las promociones(FIN)
                     *************************************************************/		
                    
                    // Si el pedido esta 'En Bodega' revisamos si tiene exceso
                    logger.debug("Vamos a ver si lo marcamos con exceso...");
                    dao.marcaMontosReservadosExcedidos(id_pedido);
				}
		    }
			logger.debug("Fin - Actualiza pedidos");
			result = true;
		} catch (PedidosDAOException ex) {
			// rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}			
			logger.debug("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe cod del pedido");
				throw new RondasException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
			}
			throw new SystemException("Error no controlado",ex);		
		} catch (RondasDAOException ex) {
			//rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			logger.debug("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe cod de la ronda");
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO, ex);
			}
			throw new SystemException("Error no controlado",ex);
		}
		//		 cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
		logger.debug(" *** Finaliza doRecepcionaRonda *** ");
		return result;
		
	}


	/**
	 * Resetea el estado de la ronda a Creada
	 * 
	 * @param id_ronda
	 * @return boolean
	 * @throws RondasException
	 * @throws SystemException
	 */
	public boolean setReseteaRonda(long id_ronda) throws RondasException, SystemException {
		boolean result  = false;
		
		JdbcRondasDAO dao = (JdbcRondasDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
//		 Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();

		//		 Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}

		//		 Marcamos los dao's con la transacción
		try {
			dao.setTrx(trx1);
		} catch (RondasDAOException e2) {
			logger.error("Error al asignar transacción al dao Rondas");
			throw new SystemException("Error al asignar transacción al dao Rondas");
		}	

		
		try {
			RondaDTO ronda = dao.getRondaById(id_ronda); 
			if(ronda==null){
				logger.debug("La ronda no existe");
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO);
			}
			
			if(ronda.getId_estado()!=Constantes.ID_ESTADO_RONDA_EN_PICKING){
				logger.debug("El estado de la ronda es incorrecto");
				throw new RondasException(Constantes._EX_RON_ESTADO_INADECUADO);
			}
			if( dao.setReseteaRonda(id_ronda)){
				try{
					dao.setFormPickReseteaRonda(id_ronda);
				}catch (RondasDAOException e){
					logger.debug("Ronda PDA");
				}
				
				result= true;
				logger.debug("Resultado reseteo ronda: " + result);
			}
			
		
		} catch (RondasDAOException ex) {
			
//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			
			logger.debug(ex.getMessage());
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe cod de la ronda");
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO, ex);
			}
			throw new SystemException("Error no controlado",ex);
		}		
//		 cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
		return result;
	}

	
	/**
	 * Obtiene un listado con los bins asociados a una ronda, Formulario de picking
	 * @param id_ronda
	 * @return List
	 * @throws RondasException
	 */
	public List getBinsFormPickByRondaId(long id_ronda)	throws RondasException{

		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			return dao.getBinsFormPickByRondaId(id_ronda);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}
	
	
	}	
		
	
	/**
	 * Permite agregar un bin a una ronda a través del Formulario de Picking.
	 * @param bin
	 * @throws RondasException
	 * @throws SystemException
	 */
	public void doAgregaBinRonda(BinFormPickDTO bin)	throws RondasException, SystemException{

		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		
		try {
			 dao.doAgregaBinRonda(bin);
		}  catch (RondasDAOException ex) {
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO);
			}
			throw new SystemException("Error no controlado al insertar un bin a la ronda en el formulario de picking",ex);		
		}catch(Exception ex){
			logger.debug("error no controlado al insertar un bin a la ronda en el formulario de picking: "+ex.getMessage());
		}			
		
	
	}
	
	
	/**
	 * Permite agregar un registro de picking a través del formulario manual de picking.
	 * -  Agrega el Pciking a la tabla BO_FPICK
	 * -  Si es un bin nuevo lo agrega ala tabla BO_FPICKBINS
	 * @param pick FPickDTO
	 * @param bin BinFormPickDTO
	 * @return long
	 * @throws RondasException
	 * @throws SystemException
	 */
	public long doAgregaDetalleFormPicking(FPickDTO pick, BinFormPickDTO bin )	throws RondasException, SystemException{
		long idBin = -1;
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
//		 Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();

		//		 Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		//		 Marcamos los dao's con la transacción
		try {
			dao.setTrx(trx1);
		} catch (RondasDAOException e2) {
			logger.error("Error al asignar transacción al dao Rondas");
			throw new SystemException("Error al asignar transacción al dao Rondas");
		}		

		
		try {
			boolean result = false;

			result = dao.existePedidoEnRonda(bin.getId_ronda(),bin.getId_pedido());
			logger.debug("Result?"+result);
			if (result){
				/* Si el bin no existe, creamos uno nuevo.
				 //	if (dao.getCountFormPickBin(bin.getCod_bin(),pick.getId_ronda()) == 0){
				 * 
				 */
				//Si viene id de bin en 0, creamos uno nuevo
				if (pick.getId_form_bin() == 0 ){
					 idBin= dao.doAgregaBinRonda(bin);
					 pick.setId_form_bin(idBin);
				 }else{
					 logger.debug("id_bin: " + pick.getId_form_bin());
					 idBin = pick.getId_form_bin();
				 }
				 // Si el id de bin es distinto a -1 creamos el detalle de picking
				 if (pick.getId_form_bin() != -1){
					 dao.doAgregaPick(pick);
					 //dao.doAgregaDetalleFormPicking(pick);
				 }
			}else{
				//logger.debug("Entro aquí");
				throw new RondasException(Constantes._EX_VE_FPIK_OP_RONDA_NO_EXISTE);
			}
				
		}  catch (RondasDAOException ex) {
			//	rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO);
			}
			throw new SystemException("Error no controlado al insertar un bin a la ronda en el formulario de picking",ex);		
		}catch(Exception ex){
			//	rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			logger.debug("error no controlado al insertar un bin a la ronda en el formulario de picking: "+ex.getMessage());
		}				
		//		 cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
		return idBin;
	}
	
	/**
	 * Obtiene los productos pickeados en una ronda, Formulario de picking.
	 * @param id_ronda
	 * @return List
	 * @throws RondasException
	 */
	public List getDetallePickFormPick(ProcFormPickDetPickDTO datos) throws RondasException{

		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			return dao.getDetallePickFormPick(datos);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}
	
	
	}	

	/**
	 * Obtiene los productos con sus codigos de barra, pertenecientes a una ronda especificada
	 * @param id_ronda
	 * @return List
	 * @throws RondasException
	 */
	public List getProductosCbarraRonda(ProcFormPickDetPedDTO datos) throws RondasException{
		List result = new ArrayList();
		List listaProdCB=null;
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			listaProdCB = dao.getProductosCbarraRonda(datos);
			
			for(int i=0;i<listaProdCB.size();i++){
				ProductosPedidoDTO prod = (ProductosPedidoDTO) listaProdCB.get(i);
				String codbarra = dao.getCbarraByIdProd(prod.getId_producto());
				prod.setCod_barra(codbarra);
				logger.debug("COD BARRA: " + codbarra + " ID PROD: " + prod.getId_producto());
				result.add(prod);
			}
			
			
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}
	
		return result;
	}	
	
	/**
	 * Obtiene el producto segun un codigo de barras
	 * @param cod_barra
	 * @return ProductoCbarraDTO
	 * @throws RondasException
	 * @throws SystemException
	 */
	public ProductoCbarraDTO getProductoByCbarra(String cod_barra)
	throws RondasException{
	
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			return dao.getProductoByCbarra(cod_barra);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}	
	
	}
	
	/**
	 * Elimina un picking de la tabla BO_FPICK, formulario de picking
	 * @param id_row
	 * @return boolean
	 * @throws RondasException
	 */
	public boolean doDelPickFormPicking(long id_pick)
	throws RondasException{
	
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			
			return dao.doDelFPicking(id_pick);
			
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}	
	
	}
	
	/**
	 * Inicializa el formulario de picking manual copiando los datos necesarios en las tablas del 
	 * formulario de picking.
	 * 
	 * @param datos
	 * @throws RondasException
	 * @throws SystemException
	 */
	public void doIniciaFormPickingManual(ProcIniciaFormPickingManualDTO datos)
			throws RondasException, SystemException{
		
		long id_ronda;
		long id_usuario;
		String login;
		
		id_ronda = datos.getId_ronda();
		id_usuario = datos.getId_usuario();
		login = datos.getLogin();
		
		
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		//		 Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();

		//		 Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		//		 Marcamos los dao's con la transacción
		try {
			dao.setTrx(trx1);
		} catch (RondasDAOException e2) {
			logger.error("Error al asignar transacción al dao Rondas");
			throw new SystemException("Error al asignar transacción al dao Rondas");
		}		
		
		
		try {
			
			//Asigna ronda al usuario
			
			RondaDTO ronda_base = dao.getRondaById(id_ronda);
			
			if ( ronda_base == null ){
				logger.info("La ronda solicitada no existe");
				/*
				try { trx1.rollback(); } catch (DAOException e1) {
					logger.error("Error al hacer rollback");
					throw new SystemException("Error al hacer rollback");
				}
				*/
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO);
			}

			FormPickRondaDTO ronda_formpick = new FormPickRondaDTO();			
			ronda_formpick = dao.getFormPickRonda(id_ronda);
			
			if (ronda_formpick.getEstado()!=null){
				//	si la ronda esta abierta no hacer nada
				logger.debug("El estado de la ronda en el formulario es:"+ronda_formpick.getEstado());
				if (ronda_formpick.getEstado().equals(Constantes.FPICK_ESTADO_ABIERTO)){
					logger.debug("La ronda ya esta abierta");
					return;
				}
			}			
			logger.debug("Se abre la ronda");
			
			if ( ronda_base.getId_estado() != Constantes.ID_ESTADO_RONDA_CREADA ){
				logger.info("Ronda en estado inadecuado para realizar la operación");
				/*
				try { 
					trx1.rollback(); 
				} catch (DAOException e1) {
					logger.error("Error al hacer rollback");
					throw new SystemException("Error al hacer rollback");
				}
				*/
				throw new RondasException(Constantes._EX_RON_ESTADO_INADECUADO);
			}
			
			
			// Cambia el estado a la ronda a En Picking
			dao.doCambiaEstadoRonda(Constantes.ID_ESTADO_RONDA_EN_PICKING, id_ronda);
		
			// Asigna el pickeador a la ronda
			dao.doAsignaPickeadorRonda(id_ronda, id_usuario);
			
			// Agrega al log de la ronda
			dao.doAddLogRonda(id_ronda, "SYSTEM", "Se asigna pickeador y se inicia el picking");		
			
			//si no esta abierta es necesario abrirla, generandola
			ronda_formpick.setId_ronda(id_ronda);
			ronda_formpick.setEstado(Constantes.FPICK_ESTADO_ABIERTO);		
			dao.doAgregaFormPickRonda(ronda_formpick);
			
			logger.debug("La ronda se agrego al forumlario");
			//la ronda ha sido insertada, ahora se insertan sus pedidos y sus detalles
			//1. por casa pedido busca los detalles de rondas
			//2. busca los pedidos de la ronda
			//3. Inserta el pedido Abierto
			//4. Inserta detalles del pedido
			
			//1. busca pedidos de la ronda
			List lst_det_ronda= new ArrayList();
			List lst_pedidos_unicos= new ArrayList();
			lst_det_ronda = dao.getDetalleRondas(id_ronda);
			
			for(int i=0; i<lst_det_ronda.size();i++){
				DetalleRondaDTO det_ronda = (DetalleRondaDTO)lst_det_ronda.get(i);				
				logger.debug("1");
				//	2. busca si el pedido del detalle de ronda fue insertado
				boolean encontrado=false;
				logger.debug("2");
				for(int j=0;j<lst_pedidos_unicos.size();j++){
					Long pedido_lista = (Long)lst_pedidos_unicos.get(j);
					if (pedido_lista.longValue()==det_ronda.getId_pedido()){
						encontrado = true;						
					}										
				}//for j
				
				// 3 si no se encuentra lo agrega a la lista unica y a la tabla
				if (encontrado==false){							
					FormPickOpDTO datos_ped =new FormPickOpDTO();
					datos_ped.setId_pedido(det_ronda.getId_pedido());
					datos_ped.setId_ronda(id_ronda);
					datos_ped.setEstado(Constantes.FPICK_ESTADO_ABIERTO);
					logger.debug("ID PEDIDO: " + det_ronda.getId_pedido());
					logger.debug("ID RONDA: " + id_ronda);
					logger.debug("ESTADO: " + Constantes.FPICK_ESTADO_ABIERTO);
					//Agrega a la tabla
					dao.doAgregaFormPickOp(datos_ped);
					//Agrega a la lista
					lst_pedidos_unicos.add(new Long(det_ronda.getId_pedido()));
					logger.debug("Agrega nueva op:"+det_ronda.getId_pedido());
				}
				
				// 4. agrega los detalles de ronda al detalle pedido
				ProductosPedidoDTO detped = new ProductosPedidoDTO();
				detped.setId_producto(det_ronda.getId_producto());
				detped.setId_detalle(det_ronda.getId_detalle());
				detped.setId_pedido(det_ronda.getId_pedido());
				detped.setCod_producto(det_ronda.getCod_prod1());
				detped.setUnid_medida(det_ronda.getUni_med());
				detped.setDescripcion(det_ronda.getDescripcion());
				detped.setCant_solic(det_ronda.getCantidad());
				detped.setCant_pick(det_ronda.getCant_pick());
				detped.setCant_faltan(det_ronda.getCant_faltan());
				detped.setCant_spick(det_ronda.getCant_spick());
				detped.setPrecio(det_ronda.getPrecio());
				if (det_ronda.getPesable()!= null)
					detped.setPesable(det_ronda.getPesable());
				else
					detped.setPesable(Constantes.PRODUCTO_NO_PESABLE);
				detped.setSector(det_ronda.getNom_Sector());
				detped.setId_dronda(det_ronda.getId_dronda());
				detped.setMot_sustitucion(det_ronda.getMot_sustitucion());
				detped.setObservacion(det_ronda.getObservacion());
				detped.setEstado(Constantes.FPICK_ESTADO_ABIERTO);
				detped.setId_ronda(det_ronda.getId_ronda());
				dao.doAgregaFormPickDetPed(detped);
				logger.debug("Agrega detalle "+i+" con id:"+det_ronda.getId_detalle());
			}//for i		
					
			
		//Todo bien ingresa log en ronda
		dao.doAddLogRonda(id_ronda, login, Constantes.FPICK_INICIO_MSGLOG);
			
		}  catch (RondasDAOException ex) {
			//	rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO);
			}
			throw new SystemException("Error no controlado al inicializar el formulario de picking",ex);
		}catch( RondasException e ) {
			logger.debug("e: " + e);
			logger.debug("e.getmessage: " + e.getMessage());
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}			
			throw new RondasException(e);
		}catch(Exception ex){
			//	rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			logger.debug("error no controlado al inicializar formulario de picking: "+ex.getMessage());
		}				
		//		 cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
	}
	
	/**
	 * Finzaliza el formulario de picking manual entregando los datos recopilados 
	 * al metodo que recepciona las rondas de picking usado por la PDA.
	 * @param datos
	 * @throws RondasException
	 * @throws SystemException
	 */
	public void doFinalizaFormPickingManual(ProcIniciaFormPickingManualDTO datos)
		throws RondasException, SystemException{

		long id_ronda;
		long id_perfil;
		String login;
		

		
		
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		//		 Creamos trx
		//JdbcTransaccion trx1 = new JdbcTransaccion();

		/*		 Iniciamos trx
		try {
			trx1.begin();
		} catch (DAOException e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		*/
		/*		 Marcamos los dao's con la transacción
		try {
			dao.setTrx(trx1);
		} catch (RondasDAOException e2) {
			logger.error("Error al asignar transacción al dao Rondas");
			throw new SystemException("Error al asignar transacción al dao Rondas");
		}		
		*/
		
		try {
			
			//1. Revisa datos de entrada
			id_ronda = datos.getId_ronda();
			id_perfil = datos.getId_perfil();
			login = datos.getLogin();
			
			//1.1. Comprueba si la ronda existe
			RondaDTO ronda_base = dao.getRondaById(id_ronda);
			
			if ( ronda_base == null ){
				logger.info("La ronda solicitada no existe");
				/*
				try { trx1.rollback(); } catch (DAOException e1) {
					logger.error("Error al hacer rollback");
					throw new SystemException("Error al hacer rollback");
				}*/
				
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO);
			}
			
			//2. Prepara los datos para enviar
			ActDetallePickingDTO args=new ActDetallePickingDTO();
			args.setId_ronda(id_ronda);
		
			List fp_lista_bins = new ArrayList(); //BinFormPickDTO
			List fp_det_pedido = new ArrayList(); //ProductosPedidoDTO
			List fp_det_picking = new ArrayList(); //DetalleFormPickingDTO
			
			List lista_bins = new ArrayList(); //TPBinOpDTO
			List lista_det_pedido = new ArrayList(); //TPDetallePedidoDTO
			List lista_det_picking = new ArrayList(); //TPDetallePickingDTO
			List lista_reg_picking = new ArrayList(); //TPRegistroPickingDTO			
			
			//2.1. Prepara lista de bins
			fp_lista_bins = dao.getBinsFormPickByRondaId(id_ronda);
			for(int i=0;i<fp_lista_bins.size();i++){
				BinFormPickDTO bin = (BinFormPickDTO)fp_lista_bins.get(i); 
				TPBinOpDTO fpbin = new TPBinOpDTO();
				//quedan fuera :id_bin - cod_sello1 - cod_sello2
				fpbin.setPosicion(bin.getPosicion());
				fpbin.setCod_bin(bin.getCod_bin());
				fpbin.setId_op(bin.getId_pedido());
				fpbin.setTipo(bin.getTipo());
				fpbin.setCod_ubicacion(bin.getCod_ubicacion());
				
				lista_bins.add(fpbin);
			}
			logger.debug("Bins preparados");
			
			//2.2. Prepara lista de detalle de pedido
			ProcFormPickDetPedDTO datos_ped=new ProcFormPickDetPedDTO();
			datos_ped.setId_ronda(id_ronda);
			
			fp_det_pedido = dao.getProductosCbarraRonda(datos_ped);
			
			for(int i=0;i<fp_det_pedido.size();i++){
				ProductosPedidoDTO ped = (ProductosPedidoDTO)fp_det_pedido.get(i); 
				TPDetallePedidoDTO fped = new TPDetallePedidoDTO();
				fped.setId_detalle(ped.getId_detalle());
				fped.setId_op(ped.getId_pedido());
				fped.setCod_sap(ped.getCod_producto());
				fped.setU_med(ped.getUnid_medida());
				fped.setDescripcion(ped.getDescripcion());
				fped.setCant_pedida(ped.getCant_solic());
				fped.setCant_pickeada(ped.getCant_pick());
				fped.setCant_faltante(ped.getCant_faltan());
				fped.setCant_sinpickear(ped.getCant_spick());
				fped.setPrecio(ped.getPrecio());
				fped.setObservacion(ped.getObservacion());
				if(ped.getPesable().equals(Constantes.INDICADOR_SI)){
					fped.setEs_pesable(1);
				}
				else{
					fped.setEs_pesable(0);
				}				
				fped.setSector(ped.getSector());
				if(ped.getSust_cformato()!= null && ped.getSust_cformato().equals(Constantes.INDICADOR_SI)){	
					fped.setSust_camb_form(1);
				}
				else{
					fped.setSust_camb_form(0);	
				}				
				fped.setId_dronda(ped.getId_dronda());
				fped.setMot_sustitucion(ped.getMot_sustitucion());
				lista_det_pedido.add(fped);
			}
			logger.debug("Detalle pedidos preparado");
			
			
			//2.3. Prepara lista de detalle de picking
		
			ProcFormPickRelacionDTO datos_Rel = new ProcFormPickRelacionDTO();
			datos_Rel.setId_fpick(-1);
			datos_Rel.setId_ronda(id_ronda);

			fp_det_picking = dao.getFormPickRelacion(datos_Rel);
			logger.debug("despues de: dao.getDetallePickFormPick(datos_picking);" +  fp_det_picking.size());
			for(int i=0;i<fp_det_picking.size();i++){
				DetalleFormPickingDTO pik = (DetalleFormPickingDTO)fp_det_picking.get(i);
				TPDetallePickingDTO fpik = new TPDetallePickingDTO();
				fpik.setId_detalle(pik.getId_detalle());
				fpik.setId_dronda(pik.getId_det_ronda());
				fpik.setPosicion(pik.getPosicion());
				fpik.setCBarra(pik.getCBarra());
				fpik.setCantidad(pik.getCantidad());
				fpik.setTipo(pik.getTipo());
				
				lista_det_picking.add(fpik);
				
			}
			logger.debug("Detalle picking preparado");
			
			
			//2.4. Prepara lista de registro de picking
			TPRegistroPickingDTO regped = new TPRegistroPickingDTO();
			regped.setRonda((int)id_ronda);
			regped.setUsuario(login);
			regped.setPerfil((int)id_perfil);
			regped.setHora(Formatos.getFecHoraActual());
						
			lista_reg_picking.add(regped);
			logger.debug("Registro pedido preparado");

			args.setLst_bin_op(lista_bins);
			args.setLst_det_pedido(lista_det_pedido);
			args.setLst_det_picking(lista_det_picking);
			args.setLst_reg_picking(lista_reg_picking);
			//Recepciona rondas
			if (doRecepcionaRonda(args)){
				//se agregan las funciones de la recepcion
				//al igual que en el pedidoservice recepcionRondaByPOS
				logger.debug("Recepciona la ronda correctamente");
				doActDetallePedido(args);
				logger.debug("Actualiza Detalle Pedidos desde form picking");
			    doActEstadoPedido(args);
			    logger.debug("Actualiza Estado Pedidos desde form picking");
			    
				//3. Cierre y log de rondas
				//3.1. Cierra rondas en el Formulario de Picking Manual
				FormPickRondaDTO ronda_act = new FormPickRondaDTO();
				ronda_act.setId_ronda(id_ronda);
				ronda_act.setEstado(Constantes.FPICK_ESTADO_CERRADO);
				if (dao.setActFormPickRonda(ronda_act)){
					logger.debug("Ronda "+id_ronda+" Cerrada el Formulario de Picking Manual");
				}			
			}
			else{
				logger.debug("No fue posible recepcionar la ronda:"+id_ronda);
				throw new RondasException(Constantes._EX_VE_FPIK_FINALIZA_MAL);
			}
			
		}  catch (RondasDAOException ex) {		
			
			/*	rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}*/
			
			//deteccion de 
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO);
			}
			throw new SystemException("Error no controlado al al finalizar el formulario de picking",ex);
		}catch(RondasException ex){
			logger.debug("Paso por RondasException:"+ex.getMessage());
			throw new RondasException(ex);
		}catch(Exception ex){
			/*	rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}*/
			logger.debug("error no controlado al finalizar el formulario de picking: "+ex.getMessage());
		}
		/*		 cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}*/
	}
	
	/**
	 * Relaciona el detalle de pedido con el detalle de picking, agregando un registro en la tabla BO_FPICKDETPICK, 
	 * y modificando las cantidades pickeada y sin pickear del detalle del pedido y la cantidad pendiente del picking
	 * @param datos
	 * @throws RondasException
	 */
	public void doRelacionFormPick(DetalleFormPickingDTO datos,ProductosPedidoDTO datos_ped, FPickDTO fpick)
	throws RondasException, SystemException{
		
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		//Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
		//Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		//		 Marcamos los dao's con la transacción
		try {
			dao.setTrx(trx1);
		} catch (RondasDAOException e2) {
			logger.error("Error al asignar transacción al dao Rondas");
			throw new SystemException("Error al asignar transacción al dao Rondas");
		}		
		try {
			if (datos.getId_detalle()<=0){
				throw new RondasException(Constantes._EX_VE_FPIK_ID_DETALLE_INVALIDO);
			}
			
			//	Agregamos el detalle de picking
			dao.doAgregaDetalleFormPicking(datos);
			//Actualizamos las cantidades pendientes
			dao.setActFPick(fpick);
			//Actualizamos el detalle de pedido
			dao.setActFormPickDetPed(datos_ped);
			
		} catch (RondasDAOException e) {
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			//e.printStackTrace();
			throw new RondasException(e);
		}	
		//cerramos la trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
		
	}
	
	/**
	 * Permite agregar un producto pickeado a la tabla elemental del picking para el formulario
	 * @param bin
	 * @throws RondasException
	 * @throws SystemException
	 */
	public void doAgregaPick(FPickDTO pick)	throws RondasException, SystemException{

		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		
		try {
			 dao.doAgregaPick(pick);
		}  catch (RondasDAOException ex) {
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO);
			}
			throw new SystemException("Error no controlado al insertar un bin a la ronda en el formulario de picking",ex);		
		}catch(Exception ex){
			logger.debug("error no controlado al insertar un bin a la ronda en el formulario de picking: "+ex.getMessage());
		}			
		
	
	}
	
	/**
	 * Actualiza el detalle de pedido 
	 * @param datos_ped
	 * @throws RondasException
	 * @throws SystemException
	 */
	public boolean setActFormPickDetPed(ProductosPedidoDTO datos_ped)	throws RondasException, SystemException{
		boolean result =false;
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		//Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
		//Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		//		 Marcamos los dao's con la transacción
		try {
			dao.setTrx(trx1);
		} catch (RondasDAOException e2) {
			logger.error("Error al asignar transacción al dao Rondas");
			throw new SystemException("Error al asignar transacción al dao Rondas");
		}
		
		try {

			
			
			result = dao.setActFormPickDetPed(datos_ped);
		}  catch (RondasDAOException e) {
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			e.printStackTrace();
			throw new RondasException(e);
		}
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
		return result;
	}
	
	/**
	 * Obtiene un listado con los productos faltantes asociados a una ronda
	 * @param id_ronda
	 * @return List
	 * @throws RondasException
	 */
	public List getFormPickFaltantes(long id_ronda)
	throws RondasException{
	
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			return dao.getFormPickFaltantes(id_ronda);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}	
	
	}
	
	
	/**
	 * Obtiene el listado de los productos sustitutos asociados a una ronda
	 * @param id_ronda
	 * @param id_local
	 * @return List
	 * @throws RondasException
	 */
	public List getFormPickSustitutos(long id_ronda, long id_local)
	throws RondasException{
	
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			return dao.getFormPickSustitutos(id_ronda, id_local);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}	
	
	}
	
	/**
	 * Permite reversar sustitutos Formulario de Picking
	 * Se elimina el registro de detalle de picking y se actualizan las 
	 * cantidades pickeadas y sin pickear del detalle de pedido  y la cantidad pendiente del pciking
	 * @param id_row
	 * @param datos_ped
	 * @param fpick
	 * @return boolean
	 * @throws RondasException
	 * @throws SystemException
	 */
	public boolean doReversaSustitutoFormPick(ProductosPedidoDTO datos_ped ,FPickDTO fpick, long id_row)
	throws RondasException, SystemException{
		boolean result=false;
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		//Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
		//Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		//		 Marcamos los dao's con la transacción
		try {
			dao.setTrx(trx1);
		} catch (RondasDAOException e2) {
			logger.error("Error al asignar transacción al dao Rondas");
			throw new SystemException("Error al asignar transacción al dao Rondas");
		}		
		try {
			
			
			//Actualizamos las cantidades pendientes
			
			dao.setActFPick(fpick);
				
			//Actualizamos el detalle de pedido
			dao.setActFormPickDetPed(datos_ped);
			//Eliminamos el detalle de picking
			dao.doDelDetalleFormPicking(id_row);
			result=true;
		} catch (RondasDAOException e) {
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			e.printStackTrace();
			throw new RondasException(e);
		}	
		//cerramos la trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
		return result;
	}
		
		
	/**
	 *Obtiene el detalle de picking de un producto
	 *
	 * @param id_row
	 * @return DetalleFormPickingDTO
	 * @throws RondasException
	 */
	public DetalleFormPickingDTO getRelacionFormPickById(long id_row) 
	throws RondasException{
		DetalleFormPickingDTO result = null;
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			result = dao.getRelacionFormPickById(id_row);
			if (result == null){
				throw new RondasException(Constantes._EX_VE_FPIK_NO_EXISTE_RELACION);
			}
		} catch (RondasDAOException e) {
		//e.printStackTrace();
			throw new RondasException(e);
		}	
		return result;
	}
	
	
	/**
	 * Obtiene la cantidad de productos relacionados segun id_fpick
	 * @param id_fpick
	 * @return long
	 * @throws RondasException
	 */
	public long getCountFormPickRelacion(long id_fpick)
	throws RondasException{
	
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			return dao.getCountFormPickRelacion(id_fpick);
		} catch (RondasDAOException e) {
			//e.printStackTrace();
			throw new RondasException(e);
		}	
	}			
		
	
	
	/**
	 * Permite reversar un picking
	 * Actualiza las cantidades en la tabla bo_fpickdetped
	 * Elimina el detalle de picking tabla bo_fpickdetpick
	 * Elimina el picking tabla bo_fpick
	 * @param id_pick
	 * @return boolean
	 * @throws RondasException
	 * @throws SystemException
	 */
	public boolean doReversaPickingFormPick(long id_pick)
	throws RondasException, SystemException{
		boolean result=false;
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		//Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
		//Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		//		 Marcamos los dao's con la transacción
		try {
			dao.setTrx(trx1);
		} catch (RondasDAOException e2) {
			logger.error("Error al asignar transacción al dao Rondas");
			throw new SystemException("Error al asignar transacción al dao Rondas");
		}		
		try {
			

			//Obtenemos el detalle de picking segun su id
			ProcFormPickRelacionDTO datos_Rel = new ProcFormPickRelacionDTO();
			datos_Rel.setId_fpick(id_pick);
			datos_Rel.setId_ronda(-1);
			List listPick = dao.getFormPickRelacion(datos_Rel);
			long id_det = -1;
			long id_ronda = -1;
			double cant_rel_ped=0;
			for (int i = 0; i < listPick.size(); i++) {			
				DetalleFormPickingDTO pick = (DetalleFormPickingDTO)listPick.get(i);				
				id_det = pick.getId_detalle();
				cant_rel_ped = pick.getCant_rel_ped();
				id_ronda=pick.getId_ronda();
//				Obtenemos las cantidades actuales
		 		ProcFormPickDetPedDTO dp = new ProcFormPickDetPedDTO();
				dp.setId_detalle(id_det);
				dp.setId_ronda(id_ronda);
				dp.setPor_idDet(true);
				//Obtenemos las cantidades actuales
				double cant_pick_act=0;
				double cant_spick_act=0;
				double cant_faltante=0;
				List listProdRonda = dao.getProductosCbarraRonda(dp);
				for (int j = 0; j < listProdRonda.size(); j++) {	
					ProductosPedidoDTO pr = (ProductosPedidoDTO)listProdRonda.get(j);	
					cant_faltante = pr.getCant_faltan();
					//Disminuimos la cantidad pickeada
					cant_pick_act = pr.getCant_pick() - cant_rel_ped;
					//Aumentamos la cantidad sin pickear
					cant_spick_act = pr.getCant_spick() + cant_rel_ped;
					
				}				
				//Actualizamos el detalle de pedido	
				ProductosPedidoDTO datos_ped = new ProductosPedidoDTO();
				datos_ped.setId_detalle(id_det);
				datos_ped.setCant_pick(cant_pick_act);
				datos_ped.setCant_faltan(cant_faltante);
				datos_ped.setCant_spick(cant_spick_act);
				datos_ped.setMot_sustitucion(0);
				datos_ped.setSust_cformato("");
				
				result = dao.setActFormPickDetPed(datos_ped);
				
				//Eliminamos el detalle de picking	
				if (result)
					result = dao.doDelDetalleFormPicking(pick.getId_row());
				
			}
			
			//Eliminamos el picking
			if (result)
				result = dao.doDelFPicking(id_pick);
			

			
		} catch (RondasDAOException e) {
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			e.printStackTrace();
			throw new RondasException(e);
		}	
		//cerramos la trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
		return result;
	}
	
	
	
	/**
	 * Relaciona automaticamente todos los detalles de picking que coinciden
	 * en codigos de barra y cantidad con el detalle de pedido
	 * Actualiza las cantidades pickeada y sin pickear del detalle de pedido
	 * y la cantidad pendiente del picking, además agrega el registro de detalle de picking.
	 * @param id_ronda
	 * @throws RondasException
	 * @throws SystemException
	 */
	public void doRelacionAutomaticaFormPick(long id_ronda)
	throws RondasException, SystemException{
		
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		//Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
		//Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}
		//		 Marcamos los dao's con la transacción
		try {
			dao.setTrx(trx1);
		} catch (RondasDAOException e2) {
			logger.error("Error al asignar transacción al dao Rondas");
			throw new SystemException("Error al asignar transacción al dao Rondas");
		}		
		try {
			
			//Comprobamos si la ronda existe
			if (!dao.existeRonda(id_ronda)){
				logger.debug("Entro aquí!!!!!");
				throw new RondasException(Constantes._EX_RON_ID_INVALIDO);
			}
			
			//Obtenemos el detalle de picking
			List list_pick = dao.getPickSinRelacionFormPick(id_ronda);
			
			
			//Revisamos si hay alguno que coincida
			for (int i=0; i<list_pick.size();i++){
				FPickDTO pick = (FPickDTO)list_pick.get(i);	
				//Obtenemos el detalle de pedido
				List list_prod = dao.getPedSinRelacionFormPick(id_ronda);
				for (int j=0; j<list_prod.size();j++){
					ProductosPedidoDTO prod = (ProductosPedidoDTO)list_prod.get(j);
					
					if (pick.getId_op() == prod.getId_pedido() &&
						pick.getCbarra().equals(prod.getCod_barra()) &&
						pick.getCantidad() == prod.getCant_solic()){
						//Actualizamos la cantidad pendiente en la tabla bo_fpick
						FPickDTO fp = new FPickDTO();
						fp.setId_fpick(pick.getId_fpick());
						fp.setCant_pend(0);
						
						dao.setActFPick(fp);
						
						//Actualizamos las cantidades en la tabla bo_fpickdetped
						ProductosPedidoDTO datos_ped = new ProductosPedidoDTO();
				 		datos_ped.setId_detalle(prod.getId_detalle());
				 		datos_ped.setCant_spick(0);
				 		datos_ped.setCant_pick(prod.getCant_solic());
				 		datos_ped.setId_ronda(prod.getId_ronda());
				 		dao.setActFormPickDetPed(datos_ped);
				 		
				 		// Agregamos el detalle de picking
				 		DetalleFormPickingDTO datos = new DetalleFormPickingDTO();
				 		datos.setCant_rel_ped(prod.getCant_solic());
				 		datos.setId_detalle(prod.getId_detalle());
				 		datos.setTipo(Constantes.DET_PICK_NORMAL);
				 		datos.setCantidad(pick.getCantidad());
				 		datos.setId_ronda(prod.getId_ronda());
				 		datos.setMot_sust(0);
				 		datos.setCBarra(pick.getCbarra());
						datos.setId_det_ronda(prod.getId_dronda());
						datos.setPesable(prod.getPesable());
						datos.setId_form_bin(pick.getId_form_bin());
				 		datos.setId_fpick(pick.getId_fpick());

				 		dao.doAgregaDetalleFormPicking(datos);
				 		
					}
				}
				
			}

		} catch (RondasDAOException e) {
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			e.printStackTrace();
			throw new RondasException(e);
		}	
		//cerramos la trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
		
	}
	
	/**
	 * Permite saber si un pedido existe para una ronda determinada
	 * @param id_ronda
	 * @param id_pedido
	 * @return boolean
	 * @throws RondasException
	 */
	public boolean isExistePedidoRonda(long id_ronda, long id_pedido)
	throws RondasException{
	
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			
			return dao.existePedidoEnRonda(id_ronda, id_pedido);
			
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}	
	
	}
		
	
	/**
	 * Obtiene una lista con las op asociadas a una ronda
	 * @param id_ronda
	 * @return
	 * @throws RondasException
	 */
	public List getPedidosByRonda(long id_ronda) throws RondasException{

		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			return dao.getPedidosByRonda(id_ronda);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		} 
	
	
	}	
	
	/**
	 * getSustitutosCriterio
	 * 
	 * Obtiene los datos de la tabla sustitutos criterios
	 * 
	 * @return List FOSustitutosCriteriosDTO
	 * @throws RondasException
	 */
	public List getSustitutosCriterio() throws RondasException{

		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			return dao.getSustitutosCriterio();
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		} 
	
	
	}
	

	public long getIdSectorByNombre(String nombre) throws RondasException{

		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			return dao.getIdSectorByNombre(nombre);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}
	}

	public Hashtable setOrdenProductosPDA(long id_ronda) throws RondasException{

		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			return dao.setOrdenProductosPDA(id_ronda);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}
	}

	
	/**
	 * Busca las zonas de una ronda
	 * @param id_ronda
	 * @return List ZonaDTO
	 * @throws RondasException
	 */
	public List getBuscaZonaByRonda(long id_ronda) throws RondasException{		
		JdbcZonasDespachoDAO daoZ = (JdbcZonasDespachoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getZonasDespachoDAO();	
		try {
			return daoZ.doBuscaZonaByRonda(id_ronda);
		} catch (ZonasDespachoDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}
	}	
	
	
	/**
	 * Recupera listado de zonas finalizadas para las jornadas de la ronda.
	 * 
	 * @param id_ronda	Identificador único
	 * @return			Listado de zonas
	 * @throws RondasException
	 */
	public List getZonasFinalizadas(long id_ronda) throws RondasException{
		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			return dao.getZonasFinalizadas(id_ronda);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		}
	}	
		
	/**
	 * Obtiene las promociones de los productos de una ronda
	 * @param id_ronda
	 * @return
	 * @throws RondasException
	 */
	public List getPromocionesRonda(long id_ronda) throws RondasException{

		JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		
		try {
			return dao.getPromocionesRonda(id_ronda);
		} catch (RondasDAOException e) {
			e.printStackTrace();
			throw new RondasException(e);
		} 
	
	
	}
	
	/* *********************************************************************
	 * Funciones de Prorrateo
	 *************************************************************************/
	
	/**
	 * Formula para prorratear promociones cuando existan Faltantes y/o Sustitución Simple
	 * @param prorrateo
	 * @return
	 */
	 public double prorrateoFaltSustSimple(ProrrateoPromocionDTO prorrateo){
		double descuento_final = 0;
		logger.debug("prorrateo.getCant_pick() "+prorrateo.getCant_pick() + " - prorrateo.getCant_promocion() " +prorrateo.getCant_promocion());
		if (prorrateo.getCant_pick() <= prorrateo.getCant_promocion()){
			descuento_final = prorrateo.getDscto_promo();
		}else{
			descuento_final = (prorrateo.getDscto_promo() * prorrateo.getCant_promocion())/prorrateo.getCant_pick();
		}
		logger.debug("descuento final SoF "+descuento_final);
		return  descuento_final;
	 }
	
	 /**
	  * Formula para prorratear promociones cuando exista Sustitución Con Cambio de Formato.
	  * @param prorrateo
	  * @return
	  */
	 public double prorrateoSustCambioFormato(ProrrateoPromocionDTO prorrateo){
		 double descuento_final = 0;
		 
		 descuento_final = (double)prorrateo.getMonto_tot_dscto_prod() / (double)prorrateo.getMonto_tot_prod();
		 logger.debug("descuento final CF "+ descuento_final);
		 return  descuento_final;
		 
	 }
	 
	 /**
	  * Permite saber si se debe aplicar prorrateo
	  * para aplicar prorrateo debe tener productos faltantes o productos sustitutos
	  * @param id_detalle
	  * @return
	  */
	 public boolean  debeProrratear(ProductosPedidoDTO detPed, DetallePickingDTO detPick){
		 boolean result = false;
		 //Consultamos si tiene faltantes o sustitutos
		 //if (detPed.getCant_faltan() > 0 || detPick.getSustituto().equals("S")){
		 if (detPick.getSustituto().equals("S")){
			 result=true;
		 }
		 logger.debug("debe prorratear? " + result);
		 return result;
	 }
	 
	 /**
	  * Permite saber el tipo de sustitucion
	  * @param id_detalle
	  * @return
	 * @throws RondasDAOException 
	  * @throws RondasException 
	  */
	 public boolean sustConCambioFormato(long id_detalle, JdbcTransaccion trx) throws RondasDAOException{
		 boolean result = false;
		 long cant = 0;
		 JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		 dao.setTrx(trx);
			cant = dao.getCountDetPickByIdDet(id_detalle);
			 if (cant > 1){
				 result = true;
			 }
			 logger.debug("es con cambio de formato? " + result);

		 return result;
	 }
	 
	 /**
	  * Permite calcular el prorrateo
	  * @param detPed
	  * @param detPick
	  * @return
	 * @throws RondasDAOException 
	 */
	 public double calculoProrrateo(ProrrateoPromocionDTO prorrateo,JdbcTransaccion trx) throws RondasDAOException{
		 double valor = 0;
         //Consultamos el tipo de sustitucion
         if (sustConCambioFormato(prorrateo.getId_detalle(),trx)) {
             valor = prorrateoSustCambioFormato(prorrateo);
         } else {
             valor = prorrateoFaltSustSimple(prorrateo);
         }
		 logger.debug("valor retornado: " + valor);
		 return valor;
	 }
	 
	 /**
	  * Permite aplicar el prorrateo 
	  * @param id_pedido
	  * @return
	 * @throws PedidosDAOException 
	 * @throws RondasDAOException 
	  */
	 public boolean aplicarProrrateo(long id_pedido, JdbcTransaccion trx) throws RondasDAOException, PedidosDAOException{
		 boolean result = false;		 
		 JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		 JdbcPedidosDAO daoP = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		 
		 dao.setTrx(trx);
		 daoP.setTrx(trx);
		 
		 //se obtiene el local del pedido
		 PedidoDTO ped = daoP.getPedidoById(id_pedido);
		 
         //Obtenemos el detalle del pedido  
         List lst_detped= dao.getDetallesPedido(id_pedido);
		 
		 logger.debug("lst_detped.size():"+lst_detped.size());
		 for (int i=0; i < lst_detped.size();i++) {
			 ProductosPedidoDTO detPed = (ProductosPedidoDTO)lst_detped.get(i);
			 long id_detalle = detPed.getId_detalle();
			 //Obtenemos el detalle de picking
			 List lst_detpick = dao.getDetPickingByIdDetalle(id_detalle);
			 logger.debug("lst_detpick size: " +lst_detpick.size());
			 for (int j=0; j < lst_detpick.size(); j++){
				 DetallePickingDTO detPick = (DetallePickingDTO) lst_detpick.get(j);
				 
				 //Consultamos si debe prorratear
                 logger.debug("Consultamos si debe prorratear");
				 if ( debeProrratear(detPed,detPick) ) {
                     logger.info("...a prorratear: " + detPick.getDescripcion() );
                     ProrrateoPromocionDTO prorrateo = new ProrrateoPromocionDTO();
					 prorrateo.setId_detalle(id_detalle);
					 prorrateo.setCant_pick(detPick.getCant_pick());
					 prorrateo.setCant_promocion(detPed.getCant_solic());
					 double desc_prod_promo = 0;
					 double monto_tot_descto = 0;
					 double monto_tot_prod = 0;
					 logger.debug("detPed.getPrecio_lista():"+detPed.getPrecio_lista());
					 
					 monto_tot_prod = detPed.getPrecio_lista();
					 monto_tot_descto = (detPed.getPrecio_lista() - detPed.getPrecio());
                     logger.debug("monto_tot_descto: " + monto_tot_descto);
                     logger.debug("monto_tot_prod: " + monto_tot_prod);				 
					 logger.debug("desc: monto_tot_descto / monto_tot_prod= " + (monto_tot_descto / monto_tot_prod) );
					 desc_prod_promo = monto_tot_descto / monto_tot_prod;
					 
                     prorrateo.setDscto_promo(desc_prod_promo);
					 prorrateo.setMonto_tot_dscto_prod((long)monto_tot_descto);
					 prorrateo.setMonto_tot_prod((long)monto_tot_prod);
                     
                     logger.debug("Id detalle: " + id_detalle);
                     logger.debug("Cantidad pickeada: " + detPick.getCant_pick());
                     logger.debug("Cantidad Promocion (Solicitada): " + detPed.getCant_solic());
                     logger.debug("Descuento Promocion: " + desc_prod_promo);
                     
                     //Calculamos el prorrateo
                     double desc_final = calculoProrrateo(prorrateo, trx);
                     // ** PROMOCIONES ** este indice es el usado para hacer descuento
                     logger.debug("desc_final:"+desc_final);
					 
					 //*****precio_final_prod = Math.round(detPick.getPrecio() - (detPick.getPrecio() * desc_final));
					 //precio_final_prod = Math.round(detPed.getPrecio() - (detPed.getPrecio() * desc_final));
					 //precio_final_prod = Math.round(detPed.getPrecio_lista() - (detPed.getPrecio_lista() * desc_final));
					 
					 //se necesita el precio de lista del producto pickeado
					 double precio_lista_pickeado =  daoP.getPrecioByLocalProd(ped.getId_local(), detPick.getId_producto());
                     logger.debug("precio_lista_pickeado:"+precio_lista_pickeado);
					 
					 long precio_final_prod = Math.round(precio_lista_pickeado - (precio_lista_pickeado * desc_final));
                     logger.debug("precio_final_prod:"+precio_final_prod);
					 
					 //logger.debug("precio final: Math.round( precio_lista_pickeado["+precio_lista_pickeado+"] " + "- ( precio_lista_pickeado["+precio_lista_pickeado+"] " + "* desc_final["+desc_final+"] )  ) ====>"+precio_final_prod);
					 if (precio_final_prod <= (Math.round(detPick.getPrecio() - (detPick.getPrecio() * desc_final))) ) {
						 //Aplicamos el Prorrateo
	                     logger.info("Y ahora finalmente aplicamos el prorateo en detallePicking Id Dpicking:"+detPick.getId_dpicking() + " precio:"+precio_final_prod);
		 				 if  (daoP.setAplicaProrrateo(detPick.getId_dpicking(),precio_final_prod))
		 				     result = true;
					 }else{
					 	logger.info("No se realiza el prorrateo ya que el precio original del producto sustituto es mayor al colocado en detalle de picking");
					 	logger.info("detalle de picking: " + detPick.getId_detalle());
					 	logger.info("id de pedido: " + detPick.getId_pedido());
					 	logger.info("id de producto: " + detPick.getId_producto());
					 	logger.info("precio original producto: " + precio_final_prod);
					 	logger.info("precio en detalle de picking: " + detPick.getPrecio());
					 	precio_final_prod = Math.round(detPick.getPrecio() - (detPick.getPrecio() * desc_final));
					 	logger.info("precio_final_prod nuevo: " + precio_final_prod);
					 	if  (daoP.setAplicaProrrateo(detPick.getId_dpicking(),precio_final_prod))
		 				     result = true;
					 }
				 }
			 }
         }
		 return result;
	 }
	 	
	 
	 /**
	  * Verifica si detalle de pedido permite productos sustitutos, segun promociones
	  * @param id_detalle
	  * @return
	  * @throws RondasDAOException
	  */
	 public boolean promoPermiteSustitutos(long id_detalle) throws RondasException{
		 boolean result = true;
		 JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		 
		 List lst_detpedpromo  = null;
		 
		 
		//Obtenemos el detalle del pedido  
		 try {
			lst_detpedpromo= dao.getPromocionesByIdDetalle(id_detalle);
		
		
		 
		 logger.debug("lst_detpedpromo.size():"+lst_detpedpromo.size());
		 for (int i=0; i < lst_detpedpromo.size();i++){
			 PromoDetPedRondaDTO promo = (PromoDetPedRondaDTO)lst_detpedpromo.get(i);
			//Consultamos si la promocion permite sustitutos
			 logger.debug("Promocion:"+promo.getId_promocion()+" permite sustitutos:"+promo.getSustituible());
			 if ((promo.getSustituible()==null)|| (promo.getSustituible().equals(Constantes.PROMO_NO_PERMITE_SUSTITUTO)) ){
				result = false;
				break;
			 }
			
		 }
		 } catch (RondasDAOException e) {
				throw new RondasException();
			}
		 
		 return result;
	 }
	 
	 /**
	  * Verifica si detalle de pedido permite productos faltantes, segun promociones
	  * @param id_detalle
	  * @return
	  * @throws RondasDAOException
	  */
	 public boolean promoPermiteFaltantes(long id_detalle) throws RondasException{
		 boolean result = true;
		 JdbcRondasDAO dao = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
		 
		 List lst_detpedpromo  = null;
		 
		 
		//Obtenemos el detalle del pedido  
		 try {
			lst_detpedpromo= dao.getPromocionesByIdDetalle(id_detalle);
		
		 
		 logger.debug("lst_detpedpromo.size():"+lst_detpedpromo.size());
		 for (int i=0; i < lst_detpedpromo.size();i++){
			 PromoDetPedRondaDTO promo = (PromoDetPedRondaDTO)lst_detpedpromo.get(i);
			//Consultamos si la promocion permite faltantes
			 
			 if ((promo.getFaltante()==null) || (promo.getFaltante().equals(Constantes.PROMO_NO_PERMITE_FALTANTES))){
				result = false;
				break;
			 }
			
		 }
		 } catch (RondasDAOException e) {
				throw new RondasException();
			}
			
		 
		 return result;
	 }
	
}
