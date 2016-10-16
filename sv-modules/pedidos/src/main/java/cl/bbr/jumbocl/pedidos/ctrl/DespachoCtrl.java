package cl.bbr.jumbocl.pedidos.ctrl;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dao.DAOFactory;
import cl.bbr.jumbocl.pedidos.dao.JdbcDespachosDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcZonasDespachoDAO;
import cl.bbr.jumbocl.pedidos.dto.DespachoCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.DespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.LogRutaDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorDespachosDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidosCotizacionesDTO;
import cl.bbr.jumbocl.pedidos.dto.ResumenDespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaCriterioDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.DespachosDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.DespachosException;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.ZonasDespachoDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;


/**
 * Entrega metodos de navegacion por despacho y busqueda en base a criterios. 
 * Los resultados son listados de despacho y pedidos relacionados.
 * 
 * @author BBR 
 *
 */
public class DespachoCtrl {
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	/**
	 * Obtiene listado de estados de despacho
	 * 
	 * @return List of EstadoDTO's
	 * @throws DespachosException
	 */
	public List getEstadosDespacho()
		throws DespachosException{

		JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
					
		try {
			return dao.getEstadosDespacho();
		} catch (DespachosDAOException e) {
			e.printStackTrace();
			throw new DespachosException(e);
		}
		
	}
	
	
	/**
	 * Obtiene listado de pedidos en su flujo de despacho.
	 * 
	 * @param  criterio DespachoCriteriaDTO 
	 * @return List MonitorDespachosDTO
	 * @throws DespachosException
	 */
	public List getDespachosByCriteria(DespachoCriteriaDTO criterio)
		throws DespachosException{
		
		if (criterio.getId_local() <= 0){
			throw new DespachosException("Debe existir criterio id_local mayor que 0");
		}
		
		JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
			
		try {
			return dao.getDespachosByCriteria(criterio);
		} catch (DespachosDAOException e) {
			e.printStackTrace();
			throw new DespachosException(e);
		}
		
	}
	
	
	/**
	 * Obtiene la cantidad de despachos.
	 * 
	 * @param  criterio DespachoCriteriaDTO 
	 * @return long
	 * @throws DespachosException
	 */
	public long getCountDespachosByCriteria( DespachoCriteriaDTO criterio )
		throws DespachosException{
			
		JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
			
			try {
				return dao.getCountDespachosByCriteria(criterio);
			} catch (DespachosDAOException e) {
				e.printStackTrace();
				throw new DespachosException(e);
			}
			
	}	
	
	
	/**
	 * Obtiene información del despacho de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return DespachoDTO
	 * @throws DespachosException 
	 */
	public DespachoDTO getDespachoById(long id_pedido)
		throws DespachosException{
		
		JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
		
		try {
			return dao.getDespachoById(id_pedido);
		} catch (DespachosDAOException e) {
			e.printStackTrace();
			throw new DespachosException(e);
		}
		
	}
	
	
	/**
	 * Cambia el estado a un pedido en su fase de despacho<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  id_pedido long 
	 * @param  id_estado long 
	 * @param  login String  usuario que realiza la operación
	 * @param  log String 
	 * @throws DespachosException
	 * @throws SystemException 
	 * 
	 * 
	 */
	public void doCambiaEstadoDespacho(long id_pedido, long id_estado, String login, String log)
		throws DespachosException, SystemException{

		JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
		JdbcPedidosDAO daoP = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		
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
			daoP.setTrx(trx1);
		} catch (DespachosDAOException e2) {
			logger.error("Error al asignar transacción al dao Despachos");
			throw new SystemException("Error al asignar transacción al dao Despachos");
		} catch (PedidosDAOException e) {
			logger.error("Error al asignar transacción al dao Pedidos");
			throw new SystemException("Error al asignar transacción al dao Pedidos");
		}	
		
		
		// valida los campos
		if ( id_pedido<=0 )
			throw new DespachosException("Valor incorrecto para id_pedido : " + id_pedido);
		
		// validar que los estados que cambie sean coherentes...
		
		try {
			List lista = dao.getEstadosDespacho();
			String estado = "";
			for (int i=1; i<lista.size(); i++){
				EstadoDTO estado1 = new EstadoDTO();
				estado1 = (EstadoDTO)lista.get(i);
				if(estado1.getId_estado()==id_estado){
					estado = estado1.getNombre();
				}
			}
			dao.doCambiaEstadoDespacho( id_pedido, id_estado );
			dao.addLogDespacho(id_pedido, login, "Cambio a Estado "+estado);
			dao.addLogDespacho(id_pedido, login, log);
			LogPedidoDTO logPedido = new LogPedidoDTO();
			logPedido.setId_pedido(id_pedido);
			logPedido.setUsuario(login);
			logPedido.setLog("Cambio a Estado "+estado);
			daoP.addLogPedido(logPedido);
			logPedido.setLog(log);
			daoP.addLogPedido(logPedido);
			
			//1. obtener el id de cotizacion
			long id_cotizacion = 0;
			PedidoDTO ped = daoP.getPedidoById(id_pedido);
			id_cotizacion = ped.getId_cotizacion();
			if(id_cotizacion>0){
				logger.debug("id cotizacion del pedido:"+ped.getId_cotizacion());
				
				//2. loopear revisando los estados de los pedidos de la cotizacion				
				List lst_ped = daoP.getPedidosCotiz(id_cotizacion);
				int cuenta_ped_no_terminados=0;
				for(int i=0; i<lst_ped.size(); i++){
					PedidosCotizacionesDTO pedcotz = (PedidosCotizacionesDTO) lst_ped.get(i);			
					if((pedcotz.getId_estado()!= Constantes.ID_ESTAD_PEDIDO_ANULADO)
						&& (pedcotz.getId_estado()!= Constantes.ID_ESTAD_PEDIDO_FINALIZADO)){
						cuenta_ped_no_terminados++;	
					}				
				}
				logger.debug("numero de pedidos de la cotizacion "+id_cotizacion
							+", no finalizados:"+cuenta_ped_no_terminados);
				
				/* 3. si solo existen pedidos finalizados, y anulados entonces cambiar el estado 
				   de la cotizacion a terminada.
				   */
				if (cuenta_ped_no_terminados==0){
					logger.debug("La cotizacion debe cambiar a Terminada");
					boolean res = false;
					res = daoP.setModEstadoCotizacion(id_cotizacion, 
							Constantes.ID_EST_COTIZACION_TERMINADA);
					if(res == false)
						throw new PedidosDAOException("No se puede cambiar el estado de la cotizacion");
				}
			}//fin de revision si el pedido era de una cotizacion		
			
		} catch (DespachosDAOException ex) {
			
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			
			if(ex.getMessage().equals(Constantes._EX_PED_ID_INVALIDO)){
				throw new DespachosException(Constantes._EX_PED_ID_INVALIDO);
			}
			if(ex.getMessage().equals(Constantes.ESTADOS_PEDIDO_DESPACHO)){
				throw new DespachosException(Constantes._EX_ESTADO_INVALIDO);
			}
			throw new SystemException(ex);	
		} catch (PedidosDAOException e) {
//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			throw new DespachosException(Constantes._EX_COT_ID_NO_EXISTE);
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
	 * Agrega registro al log del despacho
	 * 
	 * @param  id_pedido long 
	 * @param  login String 
	 * @param  log String 
	 * 
	 * @throws DespachosException
	 * @throws SystemException 
	 */
	public void doAddLogDespacho(long id_pedido, String login, String log)
		throws DespachosException, SystemException{

		JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();

		try {
			dao.addLogDespacho(id_pedido, login, log);
		} catch (DespachosDAOException ex) {
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new DespachosException(Constantes._EX_PED_ID_INVALIDO);
			}
            throw new SystemException("Error no controlado al insertar log pedido",ex);
		}
	
	}
	
	
	
	/**
	 * Obtiene listado del log de un despacho
	 * 
	 * @param  id_pedido long 
	 * @return List of LogSimpleDTO
	 * @throws DespachosException 
	 */
	public List getLogDespacho(long id_pedido)
		throws DespachosException{

		JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();

		try {
			return dao.getLogDespacho(id_pedido);
		} catch (DespachosDAOException e) {
			e.printStackTrace();
			throw new DespachosException(e);
		}
	
	}

	/**
	 * Permite obtener el resumen de jornada de despacho
	 * @param criterio
	 * @return
	 * @throws DespachosException
	 */
	public List getResumenJorDespacho(DespachoCriteriaDTO criterio) throws DespachosException{
		long ped_solicitados = 0;
		long ped_validados = 0;
		long ped_en_pick = 0;
		long ped_en_bod = 0;
		long ped_en_pago = 0;
		long ped_pagado = 0;
		long ped_en_desp = 0;
		long ped_finalizado = 0;
		long ped_otro_estado = 0;
        long pedSinRutas = 0;
		double avance = 0.0;
		
		JdbcZonasDespachoDAO dao = (JdbcZonasDespachoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getZonasDespachoDAO();
		JdbcDespachosDAO dao1 = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
		List result = new ArrayList();
		try {
			List listazonas = dao.getZonasDespachoLocal(criterio.getId_local());
            
    		//ArrayList zonas = new ArrayList();
    		for (int i = 0; i < listazonas.size(); i++) {
    			ResumenDespachoDTO desp = new ResumenDespachoDTO();
    			ped_solicitados = 0;
    			ped_validados = 0;
    			ped_en_pick = 0;
    			ped_en_bod = 0;
    			ped_en_pago = 0;
    			ped_pagado = 0;
    			ped_en_desp = 0;
    			ped_finalizado = 0;
    			ped_otro_estado = 0;
                pedSinRutas = 0;
    			avance = 0.0;
    			
    			ZonaDTO zona1 = (ZonaDTO) listazonas.get(i);
    			desp.setNom_zona(zona1.getNombre());
    			desp.setId_zona(zona1.getId_zona());
    				
    			//PedidosCriteriaDTO cri = new PedidosCriteriaDTO();
    			//DespachoCriteriaDTO cri = new DespachoCriteriaDTO();
    			criterio.setId_zona(zona1.getId_zona());
    		//	criterio.setFdespacho(fecha);
    			//criterio.setJdesp_dia()
    			//lst_pedidos = dao1.getPedidosByCriteria(cri);
    			List lst_pedidos = dao1.getDespachosByCriteria(criterio);
    			ped_solicitados = lst_pedidos.size();
    			for (int j = 0; j < lst_pedidos.size(); j++){
    				MonitorDespachosDTO ped = (MonitorDespachosDTO) lst_pedidos.get(j);
    				
    				if (ped.getId_estado() == Constantes.ID_ESTAD_PEDIDO_VALIDADO){
    					ped_validados++;				
    				}else if (ped.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_PICKING){
    					ped_en_pick++;
    				}else if (ped.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_BODEGA){
    					ped_en_bod++;
    				}else if (ped.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_PAGO){
    					ped_en_pago++;
    				}else if (ped.getId_estado() == Constantes.ID_ESTAD_PEDIDO_PAGADO){
    					ped_pagado++;
    				}else if (ped.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_DESPACHO){
    					ped_en_desp++;
    				}else if (ped.getId_estado() == Constantes.ID_ESTAD_PEDIDO_FINALIZADO){
    					ped_finalizado++;
    				}else{
    					ped_otro_estado++;
    				}
                    
                    if ( ped.getIdRuta() == 0 && !ped.getTipo_despacho().equalsIgnoreCase("R") ) {
                        pedSinRutas++;
                    }
    				
    			}
    			logger.debug("Pedidos en otros estados: " + ped_otro_estado + " zona: "+zona1.getNombre()+" avance: " + avance);
    			if (ped_solicitados > 0){
    				//avance = ((ped_pagado + ped_en_desp + ped_finalizado)/ped_solicitados)*100;
    				//avance = (((double)ped_pagado + (double)ped_en_desp + (double)ped_finalizado)/(double)ped_solicitados)* 100;
    				avance = (((double)ped_finalizado)/(double)ped_solicitados)* 100;
    				
    				//logger.debug("avance (("+ped_pagado+"+"+ped_en_desp+"+"+ped_finalizado+")/"+ped_solicitados+" = "+avance *100 );
    				logger.debug("avance (("+ped_finalizado+")/"+ped_solicitados+" = "+avance *100 );
    			}else{
    				avance = 0;
    			}
    			desp.setPed_solicitados(ped_solicitados);
    			desp.setPed_validados(ped_validados);
    			desp.setPed_en_pick(ped_en_pick);
    			desp.setPed_en_bod(ped_en_bod);
    			desp.setPed_en_pago(ped_en_pago);
    			desp.setPed_pagado(ped_pagado);
    			desp.setPed_en_desp(ped_en_desp);
    			desp.setPed_finalizado(ped_finalizado);
                desp.setPedSinRuta(pedSinRutas);
    			desp.setAvance(Formatos.formatoNum3Dec(avance));
    			// Si tiene pedidos solicitados, agregamos el resumen
    			if (ped_solicitados > 0 )
    				result.add(desp);
    		}
		} catch (ZonasDespachoDAOException e) {
			// Auto-generated catch block
			e.printStackTrace();
			throw new DespachosException(e);
			
		} catch (DespachosDAOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}


    /**
     * @param criterio
     * @return
     */
    public List getDespachosParaMonitorByCriteria(DespachoCriteriaDTO criterio) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            return dao.getDespachosParaMonitorByCriteria(criterio);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @return
     */
    public List getJornadasDespachoParaFiltro(long idLocal, String fecha, long idZona) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            return dao.getJornadasDespachoParaFiltro(idLocal, fecha, idZona);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }

    public List getJornadasDespacho(int localId, Date fechaIni, Date fechaFin) throws DespachosException{
       JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
       try {
           return dao.getJornadasDespacho(localId, new java.sql.Date(fechaIni.getTime()), new java.sql.Date(fechaFin.getTime()));
       } catch (DespachosDAOException e) {
           e.printStackTrace();
           throw new DespachosException(e);
       }
   }



    /**
     * @param ruta
     * @return
     */
    public long addRuta(RutaDTO ruta) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            return dao.addRuta(ruta);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @param pedidos
     * @return
     */
    public int addPedidosRuta(String pedidos, long idRuta) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            return dao.addPedidosRuta(pedidos, idRuta);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @param log
     */
    public void addLogRuta(LogRutaDTO log) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            dao.addLogRuta(log);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @param idPedido
     * @param idRuta
     * @return
     */
    public int addPedidoRuta(long idPedido, long idRuta) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            return dao.addPedidoRuta(idPedido, idRuta);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @param idLocal
     * @return
     */
    public List getRutasDisponibles(long idLocal) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            return dao.getRutasDisponibles(idLocal);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @param idRuta
     * @return
     */
    public RutaDTO getRutaById(long idRuta) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            return dao.getRutaById(idRuta);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @param criterio
     * @return
     */
    public List getRutasByCriterio(RutaCriterioDTO criterio) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            return dao.getRutasByCriterio(criterio);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @param criterio
     * @return
     */
    public double getCountRutasByCriterio(RutaCriterioDTO criterio) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            return dao.getCountRutasByCriterio(criterio);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @return
     */
    public List getEstadosRuta() throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            return dao.getEstadosRuta();
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @param idZona
     * @param fecha
     * @return
     */
    public List getJornadasDespachoDisponiblesByZona(long idZona, String fecha, String tipoPedido) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            return dao.getJornadasDespachoDisponiblesByZona(idZona, fecha, tipoPedido);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @return
     */
    public List getLogRuta(long idRuta) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            return dao.getLogRuta(idRuta);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @param idRuta
     * @return
     */
    public List getPedidosByRuta(long idRuta) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            return dao.getPedidosByRuta(idRuta);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @param idPedido
     * @param idRuta
     * @return
     */
    public int delPedidoRuta(long idPedido) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            return dao.delPedidoRuta(idPedido);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @param estado
     * @param idRuta
     */
    public void setEstadoRuta(int estado, long idRuta) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            dao.setEstadoRuta(estado, idRuta);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @param idRuta
     */
    public void liberarPedidosByRuta(long idRuta) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            dao.liberarPedidosByRuta(idRuta);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @param idPedido
     * @return
     */
    public RutaDTO getRutaByPedido(long idPedido) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            return dao.getRutaByPedido(idPedido);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @param idPedido
     * @return
     */
    public JornadaDTO getJornadaDespachoOriginalDePedidoReprogramado(long idPedido) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            return dao.getJornadaDespachoOriginalDePedidoReprogramado(idPedido);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }


    /**
     * @param idPedido
     * @return
     */
    public List getReprogramacionesByPedido(long idPedido) throws DespachosException{
        JdbcDespachosDAO dao = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();
        try {
            return dao.getReprogramacionesByPedido(idPedido);
        } catch (DespachosDAOException e) {
            e.printStackTrace();
            throw new DespachosException(e);
        }
    }
	
}
