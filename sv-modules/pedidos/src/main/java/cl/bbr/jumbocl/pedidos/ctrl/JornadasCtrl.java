package cl.bbr.jumbocl.pedidos.ctrl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.CategoriaSapEntity;
import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.pedidos.collaboration.ProcSectoresJornadaDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcZonasJornadaDTO;
import cl.bbr.jumbocl.pedidos.dao.DAOFactory;
import cl.bbr.jumbocl.pedidos.dao.JdbcCalendarioDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcJornadasDAO;
import cl.bbr.jumbocl.pedidos.dto.JorDespachoCalDTO;
import cl.bbr.jumbocl.pedidos.dto.SeccionSapDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaCriteria;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.CalendarioDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.JornadasDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.JornadasException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * Entrega metodos de navegacion por jornadas despacho, jornadas picking y busqueda en base a criterios. 
 * Los resultados son listados de jornadas despacho y jornadas picking.
 * 
 * @author BBR 
 *
 */
public class JornadasCtrl {
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	
	/**
	 * Obtiene listado de jornadas por fecha
	 * 
	 * @param  fecha Date 
	 * @param  id_local long 
	 * @return List of MonitorJornadasDTO's
	 * @throws JornadasException
	 */
	public List getJornadasPickingByFecha(Date fecha, long id_local)
		throws JornadasException{
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		try {
			return dao.getJornadasPickingByFecha(fecha, id_local);
		} catch (JornadasDAOException e) {
			e.printStackTrace();
			throw new JornadasException(e);
		}
		
	}
	
	
	
	/**
	 * Obtiene jornadas de acuerdo a criterios de consulta
	 * 
	 * @param  criterio JornadaCriteria 
	 * @param  id_local long 
	 * @return List of MonitorJornadasDTO's
	 * @throws JornadasException 
	 */
	public List getJornadasPickingByCriteria(JornadaCriteria criterio, long id_local)
		throws JornadasException{

		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		try {
			return dao.getJornadasPickingByCriteria(criterio, id_local);
		} catch (JornadasDAOException e) {
			e.printStackTrace();
			throw new JornadasException(e);
		}
		//(+) INDRA 2012-12-12 (+)
	}
		
	//Maxbell arreglo homologacion
	public List getJornadasPickingByCriteriaEspeciales(JornadaCriteria criterio, long id_local)
			throws JornadasException{

			JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
			try {
				return dao.getJornadasPickingByCriteriaEspeciales(criterio, id_local);
			} catch (JornadasDAOException e) {
				e.printStackTrace();
				throw new JornadasException(e);
			}
			//(+) INDRA 2012-12-12 (+)
		}
	
	public int calcularDiferenciaJornada(long id_jpicking) {
		int cantidad = 0;
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		try {
			cantidad = dao.calcularDiferenciaJornada(id_jpicking);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cantidad;
	}
	
	public boolean valoresNegativos() {
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		return dao.valoresNegativos();
	}
		
	/**
	 * Obtiene jornadas de acuerdo a si tienen pedidos en estado revision faltante
	 * indra
	 * @param  criterio JornadaCriteria 
	 * @param  id_local long 
	 * @return List of MonitorJornadasDTO's
	 * @throws JornadasException 
	 */
	public boolean existeJornadaRevFaltante( long id_jornada)
		throws JornadasException{

		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		try {
			return dao.existeJornadaRevFaltante( id_jornada);
		} catch (JornadasDAOException e) {
			e.printStackTrace();
			throw new JornadasException(e);
	}
	
	}//(-) INDRA 2012-12-12 (-)
	/**
	 * Obtiene datos de una jornada
	 * 
	 * @param  id_jornada long 
	 * @return JornadaDTO
	 * @throws JornadasException
	 */
	public JornadaDTO getJornadaById(long id_jornada)
		throws JornadasException{

			JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
			try {
				return dao.getJornadaById(id_jornada);
			} catch (JornadasDAOException e) {
				e.printStackTrace();
				throw new JornadasException(e);
			}
		
	}
	
	/**
	 * Obtiene los pedidos por zona de una jornada dada
	 * @param datos
	 * @return List  TotProdZonJorDTO
	 * @throws JornadasException
	 */
	public List getTotalProductosZonasJornada(ProcZonasJornadaDTO datos) 
	throws JornadasException{
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
				
		try {			
			return dao.getTotalProductosZonasJornada(datos);
		} catch (JornadasDAOException e) {
			e.printStackTrace();
			throw new JornadasException(e);
		}
	}
	
	/**
	 * Obtiene la cantidad de productos por sección que deben ser
	 * pickeados en una jornada de picking
	 * 
	 * @param  id_jornada long
	 * @return List of TotProdSecJorDTO's
	 * @throws JornadasException
	 */
	public List getTotalProductosSectorJornada(ProcSectoresJornadaDTO datos) 
	throws JornadasException{
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		
		
		try {			
			return dao.getTotalProductosSectorJornada(datos);
		} catch (JornadasDAOException e) {
			e.printStackTrace();
			throw new JornadasException(e);
		}
	}

	/**
	 * Obtiene la lista de comanda de preparados.
	 * 
	 * @param  id_jornada long 
	 * @param  id_sector String 
	 * @return List of ComandaPreparadosDTO's
	 * @throws JornadasException
	 */
	public List getComandaPreparados(long id_jornada, String id_sector) 
	throws JornadasException{
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		try {
			return dao.getComandaPreparados(id_jornada,id_sector);
		} catch (JornadasDAOException e) {
			e.printStackTrace();
			throw new JornadasException(e);
		}
	}

	/**
	 * Obtiene el log de eventos de una jornada
	 * 
	 * @param  id_jornada long  identificador de jornada
	 * @return List de LogSimpleDTO
	 * @throws JornadasException
	 */
	public List getLogJornada(long id_jornada)	throws JornadasException{
		
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		try {
			return dao.getLogJornada(id_jornada);
		} catch (JornadasDAOException e) {
			e.printStackTrace();
			throw new JornadasException(e);
		}
		
	}



	/**
	 * Agregar el log de una jornada
	 * 
	 * @param  id_jornada long  identificador de jornada
	 * @param  user String , login del usuario
	 * @param  descr String , descripción
	 * 
	 * @throws JornadasException, en caso que el pedido sea inválido.
	 * @throws SystemException
	 */
	public void doAddLogJornadaPick(long id_jornada, String user, String descr) throws JornadasException, SystemException{
		
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		try {
			dao.doAddLogJornadaPick(id_jornada, user, descr);
		} catch (JornadasDAOException ex) {
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new JornadasException(Constantes._EX_PED_ID_INVALIDO, ex);
			}
			else
				throw new SystemException("Error no controlado al insertar log jornada",ex);
		}
	}

	public void doAddLogJornadaDesp(long id_jornada, String user, String descr) throws JornadasException, SystemException{
		
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		try {
			dao.doAddLogJornadaDesp(id_jornada, user, descr);
		} catch (JornadasDAOException ex) {
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new JornadasException(Constantes._EX_PED_ID_INVALIDO, ex);
			}
			else
				throw new SystemException("Error no controlado al insertar log jornada",ex);
		}
	}


	/**
	 * Obtiene los estados de la jornada
	 * 
	 * @return List of EstadoDTO's
	 * @throws JornadasException
	 */
	public List getEstadosJornada() 	
			throws JornadasException{
		
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		try {
			return dao.getEstadosJornada();
		} catch (JornadasDAOException e) {
			e.printStackTrace();
			throw new JornadasException(e);
		}
	}


	/**
	 * Inicia jornada de picking (cambia su estado a Jornada Iniciada)
	 * 
	 * @param  id_jpicking long 
	 * 
	 * @throws JornadasException, en el caso que id de jornada de picking sea inválido
	 * @throws SystemException 
	 */
	public void doIniciaJornada(long id_jpicking)
			throws JornadasException, SystemException{
		
		//validar que esté en el estado correcto
		
		
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
				
		try {
			dao.doCambiaEstadoJornadaPicking(id_jpicking, Constantes.ID_ESTADO_JORNADA_EN_PROCESO);			
		} catch (JornadasDAOException ex) {
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new JornadasException(Constantes._EX_JPICK_ID_INVALIDO, ex);
			}else
				throw new SystemException("Error no controlado al iniciar una jornada de picking",ex);
		}
	}


	/**
	 * Actualiza la cantidad sin pickear de detalles de pedidos asociados a una jornada picking
	 * 
	 * @param  id_jornada long 
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>. 
	 * @throws JornadasException, en el caso que id de jornada de picking sea inválido
	 * @throws SystemException 
	 */
	public boolean setPedidosByIdJornada(long id_jornada) throws JornadasException, SystemException{
		boolean result = false;
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		
		try {
			result = dao.setPedidosByIdJornada(id_jornada);			
		} catch (JornadasDAOException ex) {
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new JornadasException(Constantes._EX_JPICK_ID_INVALIDO, ex);
			}else
				throw new SystemException("Error no controlado al iniciar una jornada de picking",ex);
		}
		return result;
	}

	/**
	 * Obtiene la lista de secciones SAP de preparados segun el id de jornada<br>
	 * - Utilizado para la impresión de comandas, por secciones SAP
	 * 
	 * @param  id_jornada long 
	 * @return List of SeccionSapDTO's
	 * @throws JornadasException, en uno de los siguientes casos:<br>
	 * - Id de jornada sea inválido.<br>
	 * - El producto preparado no existe.
	 * @throws SystemException 
	 * 
	 * */
	public List getSeccionesSAPPreparadosByIdJornada(long id_jornada) throws JornadasException, SystemException{
		
		List result = new ArrayList();
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		
		try {
			//result = dao.setPedidosByIdJornada(id_jornada);
			//verificar si existe id_jornada
			if(!dao.existeJornadaById(id_jornada)){
				throw new JornadasException(Constantes._EX_JPICK_ID_INVALIDO);
			}
			
			List lst_secciones_sap  = dao.getSeccionesSAPByIdJornada(id_jornada);
			CategoriaSapEntity cat = null;
			
			for(int i=0; i<lst_secciones_sap.size(); i++){
				cat = (CategoriaSapEntity)lst_secciones_sap.get(i);
				SeccionSapDTO seccion = new SeccionSapDTO();
				seccion.setId_cat(cat.getId_cat());
				seccion.setId_cat_padre(cat.getId_cat_padre());
				seccion.setDescrip(cat.getDescrip());
				seccion.setEstado(cat.getEstado());
				seccion.setNivel(cat.getNivel().intValue());
				seccion.setTipo(cat.getTipo());
				result.add(seccion);
			}
			
				
		} catch (JornadasDAOException ex) {
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new JornadasException(Constantes._EX_JPICK_ID_INVALIDO, ex);
			} if (ex.getMessage().equals(Constantes._EX_PROD_PREP_NO_EXISTE)){
				throw new JornadasException(Constantes._EX_PROD_PREP_NO_EXISTE, ex);
			} else
				throw new SystemException("Error no controlado al iniciar una jornada de picking",ex);
		} 
		return result;
	}

	/**
	 * Obtiene la lista de SECTORES LOCAL de verificacion de stock segun el id de jornada<br>
	 * - Utilizado para la impresión de comandas, por secciones SAP
	 * 
	 * @param  id_jornada long 
	 * @return List of SeccionSapDTO's
	 * @throws JornadasException, en uno de los siguientes casos:<br>
	 * - Id de jornada sea inválido.<br>
	 * - El producto preparado no existe.
	 * @throws SystemException 
	 * 
	 * */
	public List getSeccionesLocalVerifStockByIdJornada(long id_jornada) throws JornadasException, SystemException{
		
		List result = new ArrayList();
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		
		try {
			//result = dao.setPedidosByIdJornada(id_jornada);
			//verificar si existe id_jornada
			if(!dao.existeJornadaById(id_jornada)){
				throw new JornadasException(Constantes._EX_JPICK_ID_INVALIDO);
			}
			
			result  = dao.getSeccionesLocalVerifStockByIdJornada(id_jornada);			
				
		} catch (JornadasDAOException ex) {
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new JornadasException(Constantes._EX_JPICK_ID_INVALIDO, ex);
			} if (ex.getMessage().equals(Constantes._EX_PROD_PREP_NO_EXISTE)){
				throw new JornadasException(Constantes._EX_PROD_PREP_NO_EXISTE, ex);
			} else
				throw new SystemException("Error no controlado al buscar comanda de verificacion " +
						"de stock con una jornada de picking",ex);
		} 
		return result;
	}

	/**
	 * Obtiene la cantida de productos de rondas relacionadas a una jornada, segun el <b>id</b> de jornada<br>
	 * 
	 * @param  id_jornada long 
	 * @return double
	 * @throws JornadasException, en caso que el id de jornada sea inválido.<br>
	 * @throws SystemException 
	 * 
	 * */
	public double getCountProdEnRondaByIdJornada(long id_jornada) throws JornadasException, SystemException {
		
		double result = 0;
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		
		try {
			//result = dao.setPedidosByIdJornada(id_jornada);
			//verificar si existe id_jornada
			if(!dao.existeJornadaById(id_jornada)){
				throw new JornadasException(Constantes._EX_JPICK_ID_INVALIDO);
			}
			result = dao.getCountProdEnRondaByIdJornada(id_jornada);
				
		} catch (JornadasDAOException ex) {
			if(ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)){
				throw new JornadasException(Constantes._EX_JPICK_ID_INVALIDO, ex);
			} else
				throw new SystemException("Error no controlado al iniciar una jornada de picking",ex);
		} 
		return result;
	}

	/**
	 * Obtiene la jornada de despacho segun su id
	 * @param id_jornada
	 * @return
	 * @throws JornadasException
	 * @throws SystemException
	 * @throws CalendarioDAOException
	 */
	public JorDespachoCalDTO getJornadaDespachoById(long id_jornada) throws JornadasException,SystemException, CalendarioDAOException {
		JorDespachoCalDTO dto = new JorDespachoCalDTO();
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		JdbcCalendarioDAO dao2 = (JdbcCalendarioDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCalendarioDAO();
		try{
			JornadaDespachoEntity desp = dao.getJornadaDespachoById(id_jornada);
			
			dto.setId_jdespacho(desp.getId_jdespacho());
			dto.setId_jpicking(desp.getId_jpicking());
			dto.setId_zona(desp.getId_zona());
			dto.setFecha(desp.getFecha().toString());
			
			dto.setCapac_despacho(desp.getCapac_despacho());
			dto.setCapac_ocupada(desp.getCapac_despacho_ocupada());
			
			dto.setCapacPicking(desp.getCapac_picking());
			dto.setCapacOcupadaPicking(desp.getCapac_picking_ocupada());
			
			HorarioDespachoEntity hds = dao2.getHorarioDespacho(desp.getId_hor_desp());
			
			dto.setH_ini(hds.getH_ini().toString());
			dto.setH_fin(hds.getH_fin().toString());
			
		} catch (JornadasDAOException ex) {
			logger.debug("Problema :" + ex);
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ 
				logger.debug("no existe id jornada");
				throw new JornadasException(Constantes._EX_JDESP_ID_INVALIDO, ex);
			}
			else
				throw new SystemException("Error no controlado",ex);
		}
		return dto;
	}

	/**
	 * Obtiene la lista de comanda de preparados.
	 * 
	 * @param  id_jornada long 
	 * @param  id_sector long 
	 * @return List of ComandaVerifStockDTO's
	 * @throws JornadasException
	 */
	public List getComandaVerifStock(long id_jornada, long id_sector) 
	throws JornadasException{
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		try {
			return dao.getComandaVerifStock(id_jornada,id_sector);
		} catch (JornadasDAOException e) {
			e.printStackTrace();
			throw new JornadasException(e);
		}
	}
	
	//Valida Jornada Despacho grability
	public boolean isValidJornadasLocal(long idJdespacho, long idJpicking, long idLocal) throws JornadasException{
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		try {
			return dao.isValidJornadasLocal(idJdespacho, idJpicking, idLocal);
		} catch (JornadasDAOException e) {
			throw new JornadasException(e);
		}
	}
		
	
}
