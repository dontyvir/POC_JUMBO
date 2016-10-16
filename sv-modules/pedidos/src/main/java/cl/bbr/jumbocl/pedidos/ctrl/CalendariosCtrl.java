package cl.bbr.jumbocl.pedidos.ctrl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.dto.FechaDTO;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.HorarioPickingEntity;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.common.model.JornadaPickingEntity;
import cl.bbr.jumbocl.common.model.SemanasEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.collaboration.ProcGeneraDespMasivoDTO;
import cl.bbr.jumbocl.pedidos.dao.DAOFactory;
import cl.bbr.jumbocl.pedidos.dao.JdbcCalendarioDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcJornadasDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcZonasDespachoDAO;
import cl.bbr.jumbocl.pedidos.dto.AdmHorarioDespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.AdmHorarioPickingDTO;
import cl.bbr.jumbocl.pedidos.dto.CalendarioDespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.CalendarioPickingDTO;
import cl.bbr.jumbocl.pedidos.dto.ComunaDTO;
import cl.bbr.jumbocl.pedidos.dto.JorDespachoCalDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.exceptions.CalendarioDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.CalendarioException;
import cl.bbr.jumbocl.pedidos.exceptions.DespachosException;
import cl.bbr.jumbocl.pedidos.exceptions.JornadasDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.LocalDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosException;
import cl.bbr.jumbocl.pedidos.exceptions.ZonasDespachoDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Entrega metodos de navegacion por Calendarios de despacho y busqueda en base a criterios. 
 * Los resultados son listados de jornadas de despacho y picking.
 * 
 * @author BBR 
 *
 */
public class CalendariosCtrl {
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

	
	/**
	 * Retorna una semana del calendario a partir de su <b>id</b>.
	 * 
	 * @param  id_semana long 
	 * @return SemanasEntity 
	 * @throws CalendarioException, en caso exista error en base de datos.
	 * 
	 */
	public SemanasEntity getSemanaById(long id_semana)
		throws CalendarioException{
		
		SemanasEntity sem = new SemanasEntity();
		
		JdbcCalendarioDAO dao = (JdbcCalendarioDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCalendarioDAO();
		try {
			sem = dao.getSemana(id_semana);
		} catch (CalendarioDAOException e) {
			e.printStackTrace();
			throw new CalendarioException(e);
		}
	
		return sem;
	}
	
	//************************* Calendario Picking **********************************/
	
	
	/**
	 * Obtiene calendario de picking para una semana y un local.
	 * 
	 * @param  n_semana int 
	 * @param  ano int 
	 * @param  id_local long 
	 * @return CalendarioPickingDTO
	 * @throws CalendarioException,  
	 * @throws SystemException, en caso exista error de sistema.
	 * 
	 */
	public CalendarioPickingDTO getCalendarioPicking(int n_semana, int ano, long id_local)
		throws CalendarioException, SystemException {
		
		List horarios = new ArrayList();
		List jornadas = new ArrayList();
		CalendarioPickingDTO cal = new CalendarioPickingDTO();
		
		try {
			
			JdbcCalendarioDAO dao = (JdbcCalendarioDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCalendarioDAO();
			
			// Obtenemos información de la semana
			SemanasEntity sem = dao.getSemana(n_semana, ano);
			cal.setSemana(sem);
			
			// Obtenemos listado de horarios del calendario
			horarios = dao.getHorariosPicking(sem.getId_semana(), id_local);
			cal.setHorarios(horarios);
			
			// Obtenemos listado de jornadas de picking asociadas
			JdbcJornadasDAO dao2 = new JdbcJornadasDAO();
			jornadas = dao2.getJornadasPickingByIdSemana(sem.getId_semana(),id_local);
			cal.setJornadas(jornadas);
			
		} catch (CalendarioDAOException e) {
			e.printStackTrace();
			throw new SystemException(e);
		} catch (JornadasDAOException e) {
			e.printStackTrace();
			throw new SystemException(e);
		}
		return cal;
	}
	
	/**
	 * Obtiene horario de picking a partir de su <b>id</b>.
	 * 
	 * @param  id_hor_pick long 
	 * @return HorarioPickingEntity
	 * @throws CalendarioException,  
	 * 
	 */
	public HorarioPickingEntity getHorarioPicking(long id_hor_pick)
		throws CalendarioException{
		
		// Crea DAOs
		JdbcCalendarioDAO dao1 = (JdbcCalendarioDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCalendarioDAO();

		try {
			return dao1.getHorarioPicking(id_hor_pick);
		} catch (CalendarioDAOException e) {
			e.printStackTrace();
			throw new CalendarioException(e);
		}
		
	}
	
	/**
	 * Obtiene listado de jornadas de picking para un horario, segun <b>id</b> del horario.
	 * 
	 * @param  id_hor_pick long 
	 * @return List of JornadaPickingEntity's
	 * @throws CalendarioException,  
	 * 
	 */
	public List getJornadasByIdHorario(long id_hor_pick)
		throws CalendarioException{
		
		// Crea DAOs
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();

		try {
			return dao.getJornadasByIdHorario(id_hor_pick);
		} catch (JornadasDAOException e) {
			e.printStackTrace();
			throw new CalendarioException(e);
		}
		
	}
	
	
	/**
	 * Inserta un horario con sus respectivas jornadas en el calendario de picking
	 *  para una semana determinada y un local determinado.<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  id_semana long 
	 * @param  id_local long 
	 * @param  hpicking AdmHorarioPickingDTO 
	 * @return long	id_horario insertado 
	 * @throws CalendarioException, en uno de los siguientes casos:<br>
	 * - Id de semana es inválido.<br>
	 * - Id de local es inválidos.<br>
	 * - Capacidad de jornada, segun el dia de semana.<br>
	 * - Horas de validación, segun el dia de semana.<br>
	 * - Horas ofrecidas en web, segun el dia de semana.<br>
	 * @throws SystemException, en caso exista error de sistema. 
	 * 
	 */
	public long doInsJornadasPickingyHorario(long id_semana, long id_local, AdmHorarioPickingDTO hpicking)
		throws CalendarioException, SystemException{

		
		// Validar que vengan todos los datos
		
		// Crea DAOs
		JdbcCalendarioDAO dao1 = (JdbcCalendarioDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCalendarioDAO();
		JdbcJornadasDAO dao2 = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		
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
			dao1.setTrx(trx1);
		} catch (CalendarioDAOException e2) {
			logger.error("Error al asignar transacción al dao Calendario");
			throw new SystemException("Error al asignar transacción al dao Calendario");
		}	

		try {
			dao2.setTrx(trx1);
		} catch (JornadasDAOException e2) {
			logger.error("Error al asignar transacción al dao Jornadas");
			throw new SystemException("Error al asignar transacción al dao Jornadas");
		}
		

		long id_horario = -1;
		
		try {

			if(id_semana<=0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_SEMANA_ID);
			}else if(id_local<=0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_LOCAL_ID_INVALIDO);
			}
			else if (hpicking.getCapac_lu()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_CAPAC_LU);
			}
			else if (hpicking.getCapac_ma()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_CAPAC_MA);
			}
			else if (hpicking.getCapac_mi()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_CAPAC_MI);
			}
			else if (hpicking.getCapac_ju()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_CAPAC_JU);
			}
			else if (hpicking.getCapac_vi()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_CAPAC_VI);
			}
			else if (hpicking.getCapac_sa()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_CAPAC_SA);
			}
			else if (hpicking.getCapac_do()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_CAPAC_DO);
			}
			//verifica las horas
			else if (hpicking.getHras_val_lu()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_VAL_LU);
			}
			else if (hpicking.getHras_val_ma()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_VAL_MA);
			}
			else if (hpicking.getHras_val_mi()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_VAL_MI);
			}
			else if (hpicking.getHras_val_ju()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_VAL_JU);
			}
			else if (hpicking.getHras_val_vi()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_VAL_VI);
			}
			else if (hpicking.getHras_val_sa()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_VAL_SA);
			}
			else if (hpicking.getHras_val_do()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_VAL_DO);
			}
			else if (hpicking.getHras_web_lu()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_WEB_LU);
			}
			else if (hpicking.getHras_web_ma()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_WEB_MA);
			}
			else if (hpicking.getHras_web_mi()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_WEB_MI);
			}
			else if (hpicking.getHras_web_ju()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_WEB_JU);
			}
			else if (hpicking.getHras_web_vi()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_WEB_VI);
			}
			else if (hpicking.getHras_web_sa()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_WEB_SA);
			}
			else if (hpicking.getHras_web_do()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_WEB_DO);
			}
			
			// Rescatamos semana
			SemanasEntity sem = dao1.getSemana(id_semana);
						
			Calendar cal = new GregorianCalendar();
			cal.setFirstDayOfWeek(Calendar.MONDAY);
			cal.setTime(sem.getF_ini());

			
			// Inserta horario

			id_horario = dao1.InsHorarioPicking(id_local, id_semana, hpicking.getH_ini(), hpicking.getH_fin());
			logger.debug("id_horario insertado: " + id_horario);
			
			// Inserta jornadas picking
			long id_jornada;
			
			//lunes
			cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
			id_jornada = dao2.doInsJornadaPicking(id_local,id_horario,id_semana,1,cal.getTime(),hpicking.getCapac_lu(),hpicking.getHras_val_lu(),hpicking.getHras_web_lu());
			logger.debug("id_jor lu: " + id_jornada);
			logger.debug("cal.MONDAY " + Calendar.MONDAY);

			//martes
			cal.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
			id_jornada = dao2.doInsJornadaPicking(id_local,id_horario,id_semana,2,cal.getTime(),hpicking.getCapac_ma(),hpicking.getHras_val_ma(),hpicking.getHras_web_ma());
			logger.debug("id_jor ma: " + id_jornada);
			
			//miercoles
			cal.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
			id_jornada = dao2.doInsJornadaPicking(id_local,id_horario,id_semana,3,cal.getTime(),hpicking.getCapac_mi(),hpicking.getHras_val_mi(),hpicking.getHras_web_mi());
			logger.debug("id_jor ma: " + id_jornada);
			
			//jueves
			cal.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
			id_jornada = dao2.doInsJornadaPicking(id_local,id_horario,id_semana,4,cal.getTime(),hpicking.getCapac_ju(),hpicking.getHras_val_ju(),hpicking.getHras_web_ju());
			logger.debug("id_jor ma: " + id_jornada);
			
			//viernes
			cal.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
			id_jornada = dao2.doInsJornadaPicking(id_local,id_horario,id_semana,5,cal.getTime(),hpicking.getCapac_vi(),hpicking.getHras_val_vi(),hpicking.getHras_web_vi());
			logger.debug("id_jor ma: " + id_jornada);
			
			//sabado
			cal.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
			id_jornada = dao2.doInsJornadaPicking(id_local,id_horario,id_semana,6,cal.getTime(),hpicking.getCapac_sa(),hpicking.getHras_val_sa(),hpicking.getHras_web_sa());
			logger.debug("id_jor ma: " + id_jornada);
			
			//domingo
			cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
			id_jornada = dao2.doInsJornadaPicking(id_local,id_horario,id_semana,7,cal.getTime(),hpicking.getCapac_do(),hpicking.getHras_val_do(),hpicking.getHras_web_do());
			logger.debug("id_jor ma: " + id_jornada);
			
			
		} catch (CalendarioDAOException ex) {
			
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			
			if ( ex.getMessage().equals( null ) ){ // Controlamos error faltan parametros
				throw new CalendarioException( Constantes._EX_JPICK_FALTAN_DATOS, ex );
			}
			if ( ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){
				throw new CalendarioException( Constantes._EX_SEMANA_ID, ex );
			}			
			throw new SystemException("Error no controlado al insertar calendario",ex);
		} catch (JornadasDAOException ex) {
			
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			
			
			if ( ex.getMessage().equals( null ) ){ // Controlamos error faltan parametros
				throw new CalendarioException( Constantes._EX_JPICK_FALTAN_DATOS, ex );
			}
			if ( ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){
				throw new CalendarioException( Constantes._EX_SEMANA_ID, ex );
			}
			throw new SystemException("Error no controlado al insertar calendario",ex);
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
		
		return id_horario;
		
	}
	
	
	/**
	 * Elimina horario de picking de una semana junto con sus jornadas relacionadas.<br> 
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  id_hor_pick long 
	 * 
	 * @throws CalendarioException, en uno de los siguientes casos:<br>
	 * - El horario de despacho no existe.<br>
	 * - El id de jornada de picking no existe.<br>
	 * @throws SystemException, en caso exista error de sistema.
	 * 
	 */
	public void doDelJornadasPickingyHorario(long id_hor_pick)
		throws CalendarioException, SystemException{
	
		// Crea DAOs
		JdbcCalendarioDAO dao1 = (JdbcCalendarioDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCalendarioDAO();
		JdbcJornadasDAO dao2 = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		
		
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
			dao1.setTrx(trx1);
		} catch (CalendarioDAOException e2) {
			logger.error("Error al asignar transacción al dao Calendario");
			throw new SystemException("Error al asignar transacción al dao Calendario");
		}	

		try {
			dao2.setTrx(trx1);
		} catch (JornadasDAOException e2) {
			logger.error("Error al asignar transacción al dao Jornadas");
			throw new SystemException("Error al asignar transacción al dao Jornadas");
		}
		
		//Validar que no existan pedidos asignados a las jornadas que se eliminarán
		
		try {
			dao2.doDelJornadaPicking(id_hor_pick);
			dao1.DelHorarioPicking(id_hor_pick);
			
		} catch (CalendarioDAOException ex) {
			
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ // Controlamos error login duplicado
				throw new CalendarioException( Constantes._EX_ID_H_DESP_NO_EXISTE, ex );
			}if ( ex.getMessage().equals( DbSQLCode.SQL_EXIST_DEP_TBLS ) ){ // Controlamos error no se puede eliminar jornada
				throw new CalendarioException( Constantes._EX_JPICK_NO_DELETE );
			}		
			throw new SystemException("Error no controlado",ex);
		} catch (JornadasDAOException ex) {
			
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ // Controlamos error login duplicado
				throw new CalendarioException( Constantes._EX_ID_H_DESP_NO_EXISTE, ex );
			}
			if ( ex.getMessage().equals( DbSQLCode.SQL_EXIST_DEP_TBLS ) ){ // Controlamos error no se puede eliminar jornada
				throw new CalendarioException( Constantes._EX_JPICK_NO_DELETE);
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
	}

	
	/**
	 * Modifica la información de una jornada.<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  id_hor_pick long 
	 * @param  hpicking AdmHorarioPickingDTO 
	 * 
	 * @throws CalendarioException, en uno de los siguientes casos:<br>
	 * -  El id de horario de picking no existe.
	 * - Capacidad de jornada, segun el dia de semana.<br>
	 * - Horas de validación, segun el dia de semana.<br>
	 * - Horas ofrecidas en web, segun el dia de semana.<br>
	 * @throws SystemException, en caso exista error de sistema.
	 * 
	 */
	public void doModJornadaPicking(long id_hor_pick, AdmHorarioPickingDTO hpicking )
		throws CalendarioException, SystemException{
		
		// Crea DAOs
		
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
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
		} catch (JornadasDAOException e2) {
			logger.error("Error al asignar transacción al dao Jornada");
			throw new SystemException("Error al asignar transacción al dao Jornada");
		}	

		
		List jornadas = new ArrayList();
		try {

			if (id_hor_pick <=0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_ID_H_PICK_NO_EXISTE);
			}
			else if (hpicking.getCapac_lu()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_CAPAC_LU);
			}
			else if (hpicking.getCapac_ma()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_CAPAC_MA);
			}
			else if (hpicking.getCapac_mi()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_CAPAC_MI);
			}
			else if (hpicking.getCapac_ju()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_CAPAC_JU);
			}
			else if (hpicking.getCapac_vi()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_CAPAC_VI);
			}
			else if (hpicking.getCapac_sa()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_CAPAC_SA);
			}
			else if (hpicking.getCapac_do()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_CAPAC_DO);
			}
			else if (hpicking.getHras_val_lu()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_VAL_LU);
			}
			else if (hpicking.getHras_val_ma()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_VAL_MA);
			}
			else if (hpicking.getHras_val_mi()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_VAL_MI);
			}
			else if (hpicking.getHras_val_ju()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_VAL_JU);
			}
			else if (hpicking.getHras_val_vi()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_VAL_VI);
			}
			else if (hpicking.getHras_val_sa()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_VAL_SA);
			}
			else if (hpicking.getHras_val_do()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_VAL_DO);
			}
			else if (hpicking.getHras_web_lu()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_WEB_LU);
			}
			else if (hpicking.getHras_web_ma()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_WEB_MA);
			}
			else if (hpicking.getHras_web_mi()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_WEB_MI);
			}
			else if (hpicking.getHras_web_ju()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_WEB_JU);
			}
			else if (hpicking.getHras_web_vi()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_WEB_VI);
			}
			else if (hpicking.getHras_web_sa()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_WEB_SA);
			}
			else if (hpicking.getHras_web_do()<0){
				trx1.rollback();
				throw new CalendarioException( Constantes._EX_JOR_HRAS_WEB_DO);
			}
			
			// Lunes
			jornadas = dao.getJornadasByIdHorario(id_hor_pick);
			
			for (int i=0; i<jornadas.size(); i++){
				JornadaPickingEntity jor = new JornadaPickingEntity();
				jor = (JornadaPickingEntity)jornadas.get(i);
				
				switch(jor.getDay_of_week()){
				case 1: //lunes
					dao.doModJornadaPicking(jor.getId_jpicking(),hpicking.getCapac_lu(),hpicking.getHras_val_lu(),hpicking.getHras_web_lu());
					break;
				case 2: //martes
					dao.doModJornadaPicking(jor.getId_jpicking(),hpicking.getCapac_ma(),hpicking.getHras_val_ma(),hpicking.getHras_web_ma());
					break;
				case 3: //miercoles
					dao.doModJornadaPicking(jor.getId_jpicking(),hpicking.getCapac_mi(),hpicking.getHras_val_mi(),hpicking.getHras_web_mi());
					break;
				case 4: //jueves
					dao.doModJornadaPicking(jor.getId_jpicking(),hpicking.getCapac_ju(),hpicking.getHras_val_ju(),hpicking.getHras_web_ju());
					break;
				case 5: //viernes
					dao.doModJornadaPicking(jor.getId_jpicking(),hpicking.getCapac_vi(),hpicking.getHras_val_vi(),hpicking.getHras_web_vi());
					break;
				case 6: //sabado
					dao.doModJornadaPicking(jor.getId_jpicking(),hpicking.getCapac_sa(),hpicking.getHras_val_sa(),hpicking.getHras_web_sa());
					break;
				case 7: //domingo
					dao.doModJornadaPicking(jor.getId_jpicking(),hpicking.getCapac_do(),hpicking.getHras_val_do(),hpicking.getHras_web_do());
					break;
				}//end switch
				
			}//end for
			
		} catch (JornadasDAOException ex) {
			
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ // Controlamos error login duplicado
				throw new CalendarioException( Constantes._EX_ID_H_DESP_NO_EXISTE, ex );
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

	}
	
	
	//****************************** Calendarios Despacho *****************************//
	
	
	/**
	 * Obtiene calendario de despacho.
	 * 
	 * @param  n_semana int  número de semana del año
	 * @param  ano int  año
	 * @param  id_zona long 
	 * @return CalendarioDespachoDTO
	 * @throws CalendarioException. 
	 * 
	 */
	public CalendarioDespachoDTO getCalendarioDespacho(int n_semana, int ano, long id_zona)
		throws CalendarioException{

		List horarios = new ArrayList();
		List jornadas = new ArrayList();
		
		// dto que retorna el método
		CalendarioDespachoDTO cal = new CalendarioDespachoDTO();
		
		try {
			
			// Creamos los dao's
			JdbcCalendarioDAO dao = (JdbcCalendarioDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCalendarioDAO();
			JdbcJornadasDAO  dao2 = (JdbcJornadasDAO)   DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
			
			// Obtenemos información de la semana
			SemanasEntity sem = dao.getSemana(n_semana, ano);
			cal.setSemana(sem);
			
			// Obtenemos listado de horarios del calendario
			horarios = dao.getHorariosDespacho(sem.getId_semana(), id_zona);
			cal.setHorarios(horarios);
			
			// Obtenemos listado de jornadas de despacho asociadas
			jornadas = dao2.getJornadasDespachoByIdSemana(sem.getId_semana(),id_zona);
			cal.setJornadas(jornadas);
			
		} catch (CalendarioDAOException e) {
			e.printStackTrace();
			throw new CalendarioException(e);
		} catch (JornadasDAOException e) {
			e.printStackTrace();
			throw new CalendarioException(e);
		}
	
		return cal;		
		
		
	}

	/**
	 * Obtiene información de un horario de despacho.
	 * 
	 * @param  id_hor_desp long 
	 * @return HorarioDespachoEntity
	 * @throws CalendarioException.
	 * 
	 */
	public HorarioDespachoEntity getHorarioDespacho(long id_hor_desp)
		throws CalendarioException{
			
			// Crea DAOs
			JdbcCalendarioDAO dao1 = (JdbcCalendarioDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCalendarioDAO();
			try {
				return dao1.getHorarioDespacho(id_hor_desp);
			} catch (CalendarioDAOException e) {
				e.printStackTrace();
				throw new CalendarioException(e);
			}
	}
	
	
	
	/**
	 * Obtiene listado de jornadas de despacho.
	 * 
	 * @param id_hor_desp long
	 * @return List of JornadaDespachoEntity's
	 * @throws CalendarioException.
	 */
	public List getJornadasDespachoByIdHorario(long id_hor_desp)
		throws CalendarioException{
		
		// Crea DAOs
		
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();

		try {
			return dao.getJornadasDespachoByIdHorario(id_hor_desp);
		} catch (JornadasDAOException e) {
			e.printStackTrace();
			throw new CalendarioException(e);
		}
	}
	
	
	/**
	 * Inserta horario con jornadas de despacho relacionadas.<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  id_semana long 
	 * @param  id_zona long 
	 * @param  hdespacho AdmHorarioDespachoDTO 
	 * @return long id_horario
	 * @throws CalendarioException, en uno de los siguientes casos:<br>
	 * - Id de la semana de despacho es inválido.<br>
	 * - Id de la zona de despacho es inválido.<br>
	 * - Hora de inicio de despacho es inválido.<br>
	 * - Hora de fin de despacho es inválido.<br>
	 * @throws SystemException, en caso exista error de sistema. 
	 * 
	 */
	public long doInsJornadasDespachoyHorario(long id_semana, long id_zona, AdmHorarioDespachoDTO hdespacho) throws CalendarioException, SystemException{

		// Validar que vengan todos los datos
		if(id_semana <=0){
			throw new CalendarioException("id de la semana de despacho es inválida, no se puede insertar nada");
		}else if(id_zona <=0){
			throw new CalendarioException("id de la zona de despacho es inválida, no se puede insertar nada");
		}else if(hdespacho.getH_ini()==null){
			throw new CalendarioException("hora de inicio de despacho es inválida, no se puede insertar nada");
		}else if(hdespacho.getH_fin()==null){
			throw new CalendarioException("hora de fin de despacho es inválida, no se puede insertar nada");
		}
		
		// Crea DAOs
		JdbcCalendarioDAO dao1 = (JdbcCalendarioDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCalendarioDAO();
		JdbcJornadasDAO dao2 = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();

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
			dao1.setTrx(trx1);
		} catch (CalendarioDAOException e2) {
			logger.error("Error al asignar transacción al dao Calendario");
			throw new SystemException("Error al asignar transacción al dao Calendario");
		}	

		try {
			dao2.setTrx(trx1);
		} catch (JornadasDAOException e2) {
			logger.error("Error al asignar transacción al dao Jornadas");
			throw new SystemException("Error al asignar transacción al dao Jornadas");
		}
		
		
		long id_horario = -1;
		
		try {
			
			// Rescatamos semana
			SemanasEntity sem = dao1.getSemana(id_semana);
			Calendar cal = new GregorianCalendar();
			cal.setFirstDayOfWeek(Calendar.MONDAY);
			cal.setTime(sem.getF_ini());
			// Inserta horario

			id_horario = dao1.InsHorarioDespacho(id_zona, id_semana, hdespacho.getH_ini(), hdespacho.getH_fin());
			logger.debug("id_horario insertado: " + id_horario);
			
			// Inserta jornadas picking
			long id_jornada;
			
			//lunes
			logger.debug("id_zona: "+id_zona);
			logger.debug("id_horario: "+id_horario);
			logger.debug("id_semana: "+id_semana);
			logger.debug("hdespacho.getId_jpicking_lu(): "+hdespacho.getId_jpicking_lu());
			logger.debug("1: "+1);
			logger.debug("cal.getTime(): "+cal.getTime());
			logger.debug("hdespacho.getCapac_lu(): "+hdespacho.getCapac_lu());
			
			
			cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);			
			id_jornada = dao2.doInsJornadaDespacho(id_zona, id_horario, id_semana, hdespacho.getId_jpicking_lu(),1,cal.getTime(),hdespacho.getCapac_lu(), hdespacho.getTarifa_express_lu(), hdespacho.getTarifa_normal_lu(), hdespacho.getTarifa_economica_lu(), hdespacho.getTarifa_umbral_lu());			
			logger.debug("id_jor lu: " + id_jornada);
			logger.debug("cal.MONDAY " + Calendar.MONDAY);

			//martes
			cal.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
			id_jornada = dao2.doInsJornadaDespacho(id_zona, id_horario, id_semana, hdespacho.getId_jpicking_ma(),2,cal.getTime(),hdespacho.getCapac_ma(), hdespacho.getTarifa_express_ma(), hdespacho.getTarifa_normal_ma(), hdespacho.getTarifa_economica_ma(), hdespacho.getTarifa_umbral_ma());
			logger.debug("id_jor ma: " + id_jornada);
			
			//miercoles
			cal.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
			id_jornada = dao2.doInsJornadaDespacho(id_zona, id_horario, id_semana, hdespacho.getId_jpicking_mi(),3,cal.getTime(),hdespacho.getCapac_mi(), hdespacho.getTarifa_express_mi(), hdespacho.getTarifa_normal_mi(), hdespacho.getTarifa_economica_mi(), hdespacho.getTarifa_umbral_mi());
			logger.debug("id_jor mi: " + id_jornada);
			
			//jueves
			cal.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
			id_jornada = dao2.doInsJornadaDespacho(id_zona, id_horario, id_semana, hdespacho.getId_jpicking_ju(),4,cal.getTime(),hdespacho.getCapac_ju(), hdespacho.getTarifa_express_ju(), hdespacho.getTarifa_normal_ju(), hdespacho.getTarifa_economica_ju(), hdespacho.getTarifa_umbral_ju());
			logger.debug("id_jor ju: " + id_jornada);
			
			//viernes
			cal.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
			id_jornada = dao2.doInsJornadaDespacho(id_zona, id_horario, id_semana, hdespacho.getId_jpicking_vi(),5,cal.getTime(),hdespacho.getCapac_vi(), hdespacho.getTarifa_express_vi(), hdespacho.getTarifa_normal_vi(), hdespacho.getTarifa_economica_vi(), hdespacho.getTarifa_umbral_vi());
			logger.debug("id_jor vi: " + id_jornada);
			
			//sabado
			cal.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
			id_jornada = dao2.doInsJornadaDespacho(id_zona, id_horario, id_semana, hdespacho.getId_jpicking_sa(),6,cal.getTime(),hdespacho.getCapac_sa(), hdespacho.getTarifa_express_sa(), hdespacho.getTarifa_normal_sa(), hdespacho.getTarifa_economica_sa(), hdespacho.getTarifa_umbral_sa());
			logger.debug("id_jor sa: " + id_jornada);
			
			//domingo
			cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
			id_jornada = dao2.doInsJornadaDespacho(id_zona, id_horario, id_semana, hdespacho.getId_jpicking_do(),7,cal.getTime(),hdespacho.getCapac_do(), hdespacho.getTarifa_express_do(), hdespacho.getTarifa_normal_do(), hdespacho.getTarifa_economica_do(), hdespacho.getTarifa_umbral_do());
			logger.debug("id_jor do: " + id_jornada);
			
			
		} catch (CalendarioDAOException ex) {
			
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}

			
			if ( ex.getMessage().equals( DbSQLCode.SQL_DUP_KEY_CODE ) ){ // Controlamos error id duplicado
				throw new CalendarioException( Constantes._EX_JDESP_JORNADA_EXISTE, ex );
			}			
			if ( ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){
				throw new CalendarioException( Constantes._EX_SEMANA_ID, ex );
			}			
			if ( ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){
				throw new CalendarioException( Constantes._EX_ZONA_ID_INVALIDA, ex );
			}			
			throw new SystemException("Error no controlado al insertar calendario",ex);
		} catch (JornadasDAOException ex) {
			
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}			
			
			if ( ex.getMessage().equals( DbSQLCode.SQL_DUP_KEY_CODE ) ){ // Controlamos error id duplicado
				throw new CalendarioException( Constantes._EX_JDESP_JORNADA_EXISTE, ex );
			}			
			if ( ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){
				throw new CalendarioException( Constantes._EX_SEMANA_ID, ex );
			}
			if ( ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST) ){
				throw new CalendarioException( Constantes._EX_ZONA_ID_INVALIDA, ex );
			}
			throw new SystemException("Error no controlado al insertar calendario",ex);
		}
		
		//		 cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
		return id_horario;
		
		
		
	}
	
	
	/**
	 * Elimina jornadas de despacho de un horario.<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  id_hor_desp long 
	 * @throws CalendarioException, en uno de los siguientes casos:<br>
	 * - Id de horario de despacho no existe.<br>
	 * - Dependencia de tablas no permite eliminar la jornada.
	 * @throws SystemException, en caso exista error de sistema. 
	 *  
	 */
	
	public void doDelJornadasDespachoyHorario(long id_hor_desp)
		throws CalendarioException, SystemException{
	
		// Crea DAOs
		JdbcCalendarioDAO dao1 = (JdbcCalendarioDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCalendarioDAO();
		JdbcJornadasDAO dao2 = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		
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
			dao1.setTrx(trx1);
		} catch (CalendarioDAOException e2) {
			logger.error("Error al asignar transacción al dao Calendario");
			throw new SystemException("Error al asignar transacción al dao Calendario");
		}	

		try {
			dao2.setTrx(trx1);
		} catch (JornadasDAOException e2) {
			logger.error("Error al asignar transacción al dao Jornadas");
			throw new SystemException("Error al asignar transacción al dao Jornadas");
		}
		
		
		//Validar que no existan pedidos asignados a las jornadas que se eliminarán
		if(id_hor_desp <=0){
			throw new CalendarioException("id del horario de despacho es inválido, no se puede eliminar");
		}
		try {
			dao2.doDelJornadaDespacho(id_hor_desp);
			dao1.DelHorarioDespacho(id_hor_desp);
			
		} catch (CalendarioDAOException ex) {
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ // Controlamos error login duplicado
				throw new CalendarioException( Constantes._EX_ID_H_DESP_NO_EXISTE);
			} if ( ex.getMessage().equals( DbSQLCode.SQL_EXIST_DEP_TBLS ) ){ // Controlamos error login duplicado
				throw new CalendarioException( Constantes._EX_JDESP_NO_DELETE);
			}
			throw new SystemException("Error no controlado",ex);			
		} catch (JornadasDAOException ex) {
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
						
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ // Controlamos error login duplicado
				throw new CalendarioException( Constantes._EX_ID_H_DESP_NO_EXISTE);
			} if ( ex.getMessage().equals( DbSQLCode.SQL_EXIST_DEP_TBLS ) ){ // Controlamos error no se puede eliminar jornada
				throw new CalendarioException( Constantes._EX_JDESP_NO_DELETE );
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
	}
	
	/**
	 * Modifica las jornadas de despacho de un horario.<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  id_hor_desp long 
	 * @param  hdespacho AdmHorarioDespachoDTO 
	 * @throws CalendarioException, en uno de los siguientes casos:<br>
	 * - Id de horario de despacho no existe.<br>
	 * - Id de zona es inválido.
	 * @throws SystemException, en caso exista error de sistema. 
	 * 
	 */
	public void doModJornadaDespacho(long id_hor_desp, AdmHorarioDespachoDTO hdespacho, int id_zona )
		throws CalendarioException, SystemException{
		
		// Crea DAOs
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		
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
		} catch (JornadasDAOException e2) {
			logger.error("Error al asignar transacción al dao Jornadas");
			throw new SystemException("Error al asignar transacción al dao Jornadas");
		}	
		
		List jornadas = new ArrayList();
		if(id_hor_desp<=0){
			throw new CalendarioException("id de horario de despacho es inválido, no se puede insertar nada");
		}
		
		try {
				
			// Lunes
			jornadas = dao.getJornadasDespachoByIdHorario(id_hor_desp);

			
			for (int i=0; i<jornadas.size(); i++){
				JornadaDespachoEntity jor = new JornadaDespachoEntity();
				jor = (JornadaDespachoEntity)jornadas.get(i);
				
				switch(jor.getDay_of_week()){
				case 1: //lunes
					dao.doModJornadaDespacho(jor.getId_jdespacho(),hdespacho.getId_jpicking_lu(),hdespacho.getCapac_lu(), hdespacho.getTarifa_express_lu(), hdespacho.getTarifa_normal_lu(), hdespacho.getTarifa_economica_lu(), hdespacho.getTarifa_umbral_lu());
					dao.doModTarifaEconomicaJornadaDespachoByDiaSemana(id_zona, jor.getFecha().toString(), hdespacho.getTarifa_economica_lu());
					System.out.println("fecha = " + jor.getFecha());
					break;
				case 2: //martes
					dao.doModJornadaDespacho(jor.getId_jdespacho(),hdespacho.getId_jpicking_ma(),hdespacho.getCapac_ma(), hdespacho.getTarifa_express_ma(), hdespacho.getTarifa_normal_ma(), hdespacho.getTarifa_economica_ma(), hdespacho.getTarifa_umbral_ma());
					dao.doModTarifaEconomicaJornadaDespachoByDiaSemana(id_zona, jor.getFecha().toString(), hdespacho.getTarifa_economica_ma());
					System.out.println("fecha = " + jor.getFecha());
					break;
				case 3: //miercoles
					dao.doModJornadaDespacho(jor.getId_jdespacho(),hdespacho.getId_jpicking_mi(),hdespacho.getCapac_mi(), hdespacho.getTarifa_express_mi(), hdespacho.getTarifa_normal_mi(), hdespacho.getTarifa_economica_mi(), hdespacho.getTarifa_umbral_mi());
					dao.doModTarifaEconomicaJornadaDespachoByDiaSemana(id_zona, jor.getFecha().toString(), hdespacho.getTarifa_economica_mi());
					System.out.println("fecha = " + jor.getFecha());
					break;
				case 4: //jueves
					dao.doModJornadaDespacho(jor.getId_jdespacho(),hdespacho.getId_jpicking_ju(),hdespacho.getCapac_ju(), hdespacho.getTarifa_express_ju(), hdespacho.getTarifa_normal_ju(), hdespacho.getTarifa_economica_ju(), hdespacho.getTarifa_umbral_ju());
					dao.doModTarifaEconomicaJornadaDespachoByDiaSemana(id_zona, jor.getFecha().toString(), hdespacho.getTarifa_economica_ju());
					System.out.println("fecha = " + jor.getFecha());
					break;
				case 5: //viernes
					dao.doModJornadaDespacho(jor.getId_jdespacho(),hdespacho.getId_jpicking_vi(),hdespacho.getCapac_vi(), hdespacho.getTarifa_express_vi(), hdespacho.getTarifa_normal_vi(), hdespacho.getTarifa_economica_vi(), hdespacho.getTarifa_umbral_vi());
					dao.doModTarifaEconomicaJornadaDespachoByDiaSemana(id_zona, jor.getFecha().toString(), hdespacho.getTarifa_economica_vi());
					System.out.println("fecha = " + jor.getFecha());
					break;
				case 6: //sabado
					dao.doModJornadaDespacho(jor.getId_jdespacho(),hdespacho.getId_jpicking_sa(),hdespacho.getCapac_sa(), hdespacho.getTarifa_express_sa(), hdespacho.getTarifa_normal_sa(), hdespacho.getTarifa_economica_sa(), hdespacho.getTarifa_umbral_sa());
					dao.doModTarifaEconomicaJornadaDespachoByDiaSemana(id_zona, jor.getFecha().toString(), hdespacho.getTarifa_economica_sa());
					System.out.println("fecha = " + jor.getFecha());
					break;
				case 7: //domingo
					dao.doModJornadaDespacho(jor.getId_jdespacho(),hdespacho.getId_jpicking_do(),hdespacho.getCapac_do(), hdespacho.getTarifa_express_do(), hdespacho.getTarifa_normal_do(), hdespacho.getTarifa_economica_do(), hdespacho.getTarifa_umbral_do());
					dao.doModTarifaEconomicaJornadaDespachoByDiaSemana(id_zona, jor.getFecha().toString(), hdespacho.getTarifa_economica_do());
					System.out.println("fecha = " + jor.getFecha());
					break;
				}//end switch
				
			}//end for
			
		} catch (JornadasDAOException ex) {
			
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ // Controlamos error id inexistente
				throw new CalendarioException( Constantes._EX_ID_H_DESP_NO_EXISTE, ex );
			}
			if ( ex.getMessage().equals( DbSQLCode.SQL_ID_KEY_NO_EXIST ) ){ // Controlamos error id inexistente
				throw new CalendarioException( Constantes._EX_ZONA_ID_INVALIDA, ex );
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
	}
	

	

	/**
	 * Realiza proceso de Re-agendamiento de un despacho, por lo tanto libera la capacidad
	 * de picking utilizada inicialmente.<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  id_pedido long 
	 * @param  id_jdespacho long 
	 * @param  sobrescribeprecio boolean 
	 * @param  precio int 
	 * @param  usr_login String 
	 * 
	 * @throws DespachosException
	 * @throws SystemException, en caso exista error de sistema. 
	 * 
	 */
	public void doReagendaDespacho(long id_pedido, long id_jdespacho, boolean sobrescribeprecio, int precio, String usr_login, boolean modificarJPicking, boolean modificarPrecio )
		throws DespachosException, SystemException {		
		
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		JdbcPedidosDAO dao2 = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		
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
		} catch (JornadasDAOException e2) {
			logger.error("Error al asignar transacción al dao Jornadas");
			throw new SystemException("Error al asignar transacción al dao Jornadas");
		}
		
		try {
			dao2.setTrx(trx1);
		} catch (PedidosDAOException e2) {
			logger.error("Error al asignar transacción al dao Pedido");
			throw new SystemException("Error al asignar transacción al dao Pedido");
		}	
		
		PedidosCtrl ctrl = new PedidosCtrl();
		
		try {
			
			PedidoDTO pedido = new PedidoDTO();
			pedido = ctrl.getPedidoById(id_pedido);

			if(id_jdespacho<0){
				throw new DespachosException(Constantes._EX_JDESP_FALTAN_DATOS);
			}
			JornadaDespachoEntity jor = dao.getJornadaDespachoById(id_jdespacho);
            
            long idJPicking = jor.getId_jpicking();			
            if ( pedido.getId_jpicking() == 0 ) {
                idJPicking = 0;
            }
			
			// 1. verifica si la jornada de despacho solicitada tiene capacidad de despacho (y de picking)
			if (!pedido.getTipo_ve().equals("S") && modificarJPicking ){
				if ( this.doVerificaCapacidadDespacho(pedido.getCant_prods(),id_jdespacho) == false ){
					trx1.rollback();
					throw new DespachosException(Constantes._EX_JDESP_SIN_CAPACIDAD); 
				}	
			}
			
		
			
			// 6. actualiza el pedido con la nueva jornada y el nuevo costo de despacho
			long var_precio;
			if ( sobrescribeprecio )
				var_precio = precio;		  // precio sobrescrito
			else
				var_precio = jor.getTarifa_normal(); // precio de la jornada
			
            boolean act=dao2.doActualizaPedidoJornadas( id_pedido, idJPicking, id_jdespacho, (int)var_precio, modificarJPicking, modificarPrecio );

			if(act){
				//Se cambia la posicion para actualizar capacidades
				
				// 2. liberar capacidad de despacho de jornada despacho en que se encuentra el pedido inicialmente
				if (!pedido.getTipo_ve().equals("S")){
					dao.doOcupaCapacidadDespacho(pedido.getId_jdespacho(), -1);
				}
				
				// 3. libera capacidad de picking de la jornada relacionada que tiene el pedido inicialmente
	            // No aplica para reprogramacion en modulo de despacho
				if (!pedido.getTipo_ve().equals("S") && modificarJPicking){
					dao.doOcupaCapacidadPicking( pedido.getId_jpicking(), (int)pedido.getCant_prods()*-1 );
				}
				
				// 4. toma nueva capacidad de despacho de la jornada_despacho solicitada
				if (!pedido.getTipo_ve().equals("S")){
					dao.doOcupaCapacidadDespacho(id_jdespacho, 1);
				}
				
				// 5. toma nueva capacidad de picking de la jornada relacionada
				// No aplica para reprogramacion en modulo de despacho
				if (!pedido.getTipo_ve().equals("S") && modificarJPicking){
					dao.doOcupaCapacidadPicking( idJPicking, (int)pedido.getCant_prods() );	
				}
				
				LogPedidoDTO log = new LogPedidoDTO();
				log.setId_pedido(id_pedido);
				log.setLog(Constantes.REAGENDA_JORNADA_DESP);
				log.setUsuario(usr_login);
				dao2.addLogPedido(log);
			}
			
		} catch (PedidosException e) {
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			e.printStackTrace();
			throw new DespachosException(e);
		} catch (JornadasDAOException e) {
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			e.printStackTrace();
			throw new DespachosException(e);
		} catch (PedidosDAOException e) {
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			
			e.printStackTrace();
			throw new DespachosException(e);
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
	}
	
	
	/**
	 * Realiza proceso de Actualiza el Costo de un Despacho.<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  id_pedido long 
	 * @param  precio int 
	 * @param  usr_login String 
	 * 
	 * @throws DespachosException
	 * @throws SystemException, en caso exista error de sistema. 
	 * 
	 */
	public void doActualizaCostoDespacho(long id_pedido, int precio, String usr_login)
		throws DespachosException, SystemException{

		JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		
//		 Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();

		// Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}

		// Marcamos los dao's con la transacción
		try {
			dao.setTrx(trx1);
		} catch (PedidosDAOException e2) {
			logger.error("Error al asignar transacción al dao Pedido");
			throw new SystemException("Error al asignar transacción al dao Pedido");
		}	
		
		//PedidosCtrl ctrl = new PedidosCtrl();
		
		try {
			
			boolean act=dao.doActualizaCostoDespacho( id_pedido,  precio );

			if(act){
				LogPedidoDTO log = new LogPedidoDTO();
				log.setId_pedido(id_pedido);
				log.setLog(Constantes.ACTUALIZA_COSTO_DESP);
				log.setUsuario(usr_login);
				dao.addLogPedido(log);
			}
			
		} catch (PedidosDAOException e) {
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			
			e.printStackTrace();
			throw new DespachosException(e);
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
	 * Agenda jornada de despacho a un pedido.<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  id_pedido long 
	 * @param  id_jdespacho long 
	 * 
	 * @throws DespachosException
	 * @throws SystemException, en caso exista error de sistema. 
	 * 
	 */
    /*
	public void doAgendaDespacho(long id_pedido, long id_jdespacho)
		throws DespachosException, SystemException {

		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		JdbcPedidosDAO dao2 = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		
//		 Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();

		//		 Iniciamos trx
		try {
			trx1.begin();
		} catch (DAOException e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}

		//		 Marcamos los dao's con la transacción
		
		try {
			dao.setTrx(trx1);
		} catch (JornadasDAOException e2) {
			logger.error("Error al asignar transacción al dao Jornadas");
			throw new SystemException("Error al asignar transacción al dao Jornadas");
		}
		
		try {
			dao2.setTrx(trx1);
		} catch (PedidosDAOException e2) {
			logger.error("Error al asignar transacción al dao Pedido");
			throw new SystemException("Error al asignar transacción al dao Pedido");
		}	
		
		PedidosCtrl ctrl = new PedidosCtrl();
		
		try {
			// 1. verifica si la jornada de despacho solicitada tiene capacidad de despacho
			PedidoDTO pedido = new PedidoDTO();
			pedido = ctrl.getPedidoById(id_pedido);
			
			if ( this.doVerificaCapacidadDespacho(pedido.getCant_prods(),id_jdespacho) == false ){
				trx1.rollback();
				throw new DespachosException("No hay capacidad de picking o de despacho"); //depurar
			}
			
			// 2. toma capacidad de despacho de la jornada_despacho solicitada
			// se incrementa en 1, pues se mide el número de pedidos de la jornada de despacho
			dao.doOcupaCapacidadDespacho(id_jdespacho, 1);
			
			// 3. toma capacidad de picking de la jornada de picking relacionada
			JornadaDespachoEntity jor = dao.getJornadaDespachoById(id_jdespacho);
			
			dao.doOcupaCapacidadPicking( jor.getId_jpicking(), (int)pedido.getCant_prods() );
						
			// 4. actualiza el pedido con la nueva jornada y el nuevo costo de despacho
			dao2.doActualizaPedidoJornadas(id_pedido, jor.getId_jpicking(), id_jdespacho, jor.getTarifa_normal(), false);

			
		} catch (JornadasDAOException e) {
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			e.printStackTrace();
			throw new SystemException(e);
		} catch (PedidosDAOException e) {
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			e.printStackTrace();
			throw new SystemException(e);
		} catch (PedidosException e) {
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			e.printStackTrace();
			throw new DespachosException(e);
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
	}
	*/
	
	/**
	 * Verifica si existe capacidad de despacho (y de picking) en una jornada.<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param cant_prods int  cantidad de productos
	 * @param id_jdespacho long  jornada de despacho que se pretende verificar capacidad
	 * @return boolean, devuelve <i>true</i> si existe capacidad de picking y despacho; <i>false</i> en caso contrario
	 * @throws DespachosException
	 * @throws SystemException, en caso exista error de sistema. 
	 * 
	 */
	public boolean doVerificaCapacidadDespacho(long cant_prods, long id_jdespacho)
		throws DespachosException, SystemException{

			boolean verifica = true;
			long capac_disponible = 0;
			
			JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();

			// Creamos trx
			JdbcTransaccion trx1 = new JdbcTransaccion();
			
			try {
				// Iniciamos trx
				trx1.begin();
				// Marcamos los dao's con la transacción
				dao.setTrx(trx1);
			} catch (Exception e1) {
				logger.error("Error al iniciar transacción");
				throw new SystemException("Error al iniciar transacción");
			} /*catch (JornadasDAOException e2) {
				logger.error("Error al asignar transacción al dao Jornadas");
				throw new SystemException("Error al asignar transacción al dao Jornadas");
			}*/
			
			logger.debug("Verificando capacidad de despacho...");
			
			
			try {
				JornadaDespachoEntity jor = dao.getJornadaDespachoById(id_jdespacho);
				
				// verificamos capacidad de despacho
				/*if ( jor.getCapac_despacho_ocupada() >= jor.getCapac_despacho() ){
					verifica = false;
					logger.debug("No hay capacidad de despacho");
				}
				else
				{
					logger.debug("OK capacidad de despacho");
				}*/
				
				if(!dao.isCapacidadDespachoValida(id_jdespacho)){
					verifica = false;
					logger.info("Capacidad de despacho consulta desde BOC" + id_jdespacho);
				}
			
				// verificamos capacidad de picking
				/*capac_disponible = jor.getCapac_picking() - jor.getCapac_picking_ocupada();
				logger.debug("capac_disponible:"+capac_disponible);
				logger.debug("cant_prods:"+cant_prods);
				
				if ( capac_disponible < cant_prods ){
					verifica = false;
					logger.debug("No hay capacidad de picking");					
				}
				else
				{
					logger.debug("OK capacidad de picking");
				}*/
				if(!dao.isCapacidadPickingValida(jor.getId_jpicking(), (int)cant_prods)){
					verifica = false;
					logger.info("Capacidad de picking consulta desde BOC"+jor.getId_jpicking());					
				}
				
			} catch (JornadasDAOException e) {
				// rollback trx
				try {
						trx1.rollback();
				} catch (DAOException e1) {
					logger.error("Error al hacer rollback");
					throw new SystemException("Error al hacer rollback");
				}
				logger.debug(e.getMessage());
				throw new SystemException(e);
			}
			
			// cerramos trx
			try {
				trx1.end();
			} catch (DAOException e) {
				logger.error("Error al finalizar transacción");
				throw new SystemException("Error al finalizar transacción");
			}
			return verifica;
		
	}

	/**
	 * Genera jornadas de despacho en forma masiva:
	 * - Verifica que las semanas entre semana inicio y semana fin no tienen pedidos relacionados, en este caso se le envia el mensaje y advertencia
	 *   las semanas seleccionadas tienen jornadas asociadas a pedidos, no puede generar jornadas en forma masiva.
	 *    
	 * - Obtiene informacion de las jornadas de la semana origen: caracteristicas de jornadas por dia, incluyendo informacion de la jornada picking,
	 *   respetando capacidad, precio, hora de inicio y hora de fin.
	 *   
	 * - Copia la informacion obtenida en las semanas de inicio y de fin.
	 * 
	 * = En el caso que la primera jornada tiene una jornada de picking de la semana anterior y existe jornada picking semana anterior, 
	 *   como se conecta?
	 *   --> Se obtiene la jornada de picking maxima del dia anterior 
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws DespachosException
	 * @throws SystemException
	 */
	public boolean doGeneraDespMasivo(ProcGeneraDespMasivoDTO dto) throws CalendarioException, SystemException{
		boolean result= false;

		JdbcCalendarioDAO dao = (JdbcCalendarioDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCalendarioDAO();
		JdbcJornadasDAO daoJ = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		
		
		//Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
		
		try {
			// Iniciamos trx
			trx1.begin();
			// Marcamos los dao's con la transacción
			dao.setTrx(trx1);
			daoJ.setTrx(trx1);
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		} /*catch (CalendarioDAOException e2) {
			logger.error("Error al asignar transacción al dao Calendarios");
			throw new SystemException("Error al asignar transacción al dao Calendarios");
		} catch (JornadasDAOException e2) {
			logger.error("Error al asignar transacción al dao Jornadas");
			throw new SystemException("Error al asignar transacción al dao Jornadas");
		}*/
		
		
		//obtener el id de semana para cada fecha: fecha de inicio, fecha de fin
		FechaDTO dto_ini = Formatos.getFechaDTOByString(dto.getFecha_ini());
		int n_semana	= dto_ini.getN_semana();
		int anio 		= dto_ini.getAnio();
		logger.debug("n_semana: " + n_semana);
		logger.debug("anio: " + anio);
		
		FechaDTO dto_fin = Formatos.getFechaDTOByString(dto.getFecha_fin());
		int n_semana_f  = dto_fin.getN_semana();
		int anio_f 		= dto_fin.getAnio();
		logger.debug("n_semana_f: " + n_semana_f);
		logger.debug("anio_f: " + anio_f);
		
		//Listado de las jornadas de despacho relacionadas
		List lst_jdesp_rel = new ArrayList();
		
		try{
			
			//datos de semana_inicio
			SemanasEntity sem_ini = dao.getSemana(n_semana, anio);
			//datos de semana_fin
			SemanasEntity sem_fin = dao.getSemana(n_semana_f, anio_f);
			
			//revisar si se cumple el parametro de diferencia de fechas, cc mandar mensaje
			//obtiene la fecha de ultimo jpicking de fecha anterior a fecha inicio
			String fecha_ant_ini = Formatos.getFechaFinal(dto.getFecha_ini(),1,'-');
			JornadaPickingEntity jpick_ant = daoJ.getJornadaPickingUltimo(fecha_ant_ini, dto.getId_local());
			String fec_jpick = Formatos.frmFechaByDate(jpick_ant.getFecha());
			//diferencia en dias
			int dif_en_dias = Formatos.diferDiasEntreFechas(fec_jpick,dto.getFecha_ini());
			if(dif_en_dias > Constantes.MAS_DIFER_DIAS_CREAR_JORNADAS){
				throw new CalendarioException(Constantes.MNS_ERROR_CREA_CALEND+" "+Constantes.MNS_ERROR_DIFER_DIAS_CREAR_JORNADAS);
			}
			
			//obtener el listado de las semanas comprendidas entre semana_inicio y semana_fin
			List lst_semanas = dao.getLstSemanas(sem_ini.getId_semana(),sem_fin.getId_semana());
			List lst_semanas_final = new ArrayList();
			for(int i=0; i<lst_semanas.size(); i++){
				SemanasEntity sem = (SemanasEntity)lst_semanas.get(i);
				//en el caso q exista alguna semana q tenga jornada de despacho y tiene pedidos asociados, lanzar la excepcion correspondiente
				List lst_sem_jor_ped = daoJ.getSemJorDespPedidoBySem(sem.getId_semana(),dto.getId_zona());
				if(lst_sem_jor_ped.size() > 0){
					//lanzar la excepcion, indicando que entre esas semanas existen jornadas con pedidos asociados
					throw new CalendarioException(Constantes.MNS_ERROR_CREA_CALEND+" "+Constantes.MNS_EXS_PEDIDO_EN_JORNADA);
				}
				
				//verificar q cada semana comprendida entre semana de inicio y semana de fin no tenga jornadas asociadas
				//List lst_jor = daoJ.getJornadasDespachoByIdSemana(sem.getId_semana(),dto.getId_zona());
				//en el caso q tiene jornadas pero no tiene pedidos asociados, existen 2 opciones
				//Opcion 1) enviar mensaje, evitar q la creacion del calendario masivo
				//Opcion 2) eliminar los jornadas de picking, horarios de picking, jornadas de despacho y horarios de despacho
				
				lst_semanas_final.add(sem);
			}//fin de for - listado de semana
			logger.debug("Tamaño de lst_semanas_final:"+lst_semanas_final.size());
			
			//revisar jdespachos q no estan comprendidas entre estas semanas pero q jpicking relacionadas se encuentran entre estas semanas
			lst_jdesp_rel = daoJ.getJDespachoRelBySemanas(sem_ini.getId_semana(), sem_fin.getId_semana(), dto.getId_zona());
			//si existen jornadas de despacho, setear a null las jornadas de picking
			for(int i=0; i<lst_jdesp_rel.size(); i++){
				JornadaDespachoEntity jor = (JornadaDespachoEntity)lst_jdesp_rel.get(i);
				//setear jpicking a null
				boolean setear = daoJ.doActJDespachoByJPick(jor.getId_jdespacho(),-1);
				logger.debug("setear?"+setear);
			}

			//verifica si existen jornadas de despacho y de picking entre esas semanas
			boolean existeJor = daoJ.existeJornadasHorariosBySemanas(sem_ini.getId_semana(), sem_fin.getId_semana(), dto.getId_zona(), dto.getId_local());
			logger.debug("existeJor?"+existeJor);
			
			boolean eliminar = false;
			if(existeJor){
				//eliminar las jornadas de despacho (incluyendo los horarios) entre esas semanas
				eliminar = daoJ.doDelJornadaDespachoBySemanas(sem_ini.getId_semana(), sem_fin.getId_semana(), dto.getId_zona());
				logger.debug("elim JorDesp?	"+eliminar);
				eliminar = daoJ.doDelHorarioDespachoBySemanas(sem_ini.getId_semana(), sem_fin.getId_semana(), dto.getId_zona());
				logger.debug("elim HorDesp?	"+eliminar);
			}
			
			
			
			//****************************************************************************************
			//***   OBTENER LA INFORMACION DE SEMANAS, HORARIOS Y JORNADAS DE LA SEMANA A COPIAR   ***
			//****************************************************************************************
			
			Date fecha_cop = dao.getSemana(dto.getId_semana()).getF_ini();
			FechaDTO dto_cop = Formatos.getFechaDTOByString(fecha_cop.toString());
			int n_semana_c 	= dto_cop.getN_semana();
			int anio_c 		= dto_cop.getAnio();
			logger.debug("n_semana_c: " + n_semana_c);
			logger.debug("anio_c: " + anio_c);
			
			//obtiene el calendario de la semana a copiar
			CalendarioDespachoDTO cal = new CalendarioDespachoDTO();
			//cal = this.getCalendarioDespacho(n_semana_c,anio_c,dto.getId_zona());
			
			//*** INICIO	OBTENER CALENDARIO DESPACHO
			//Obtenemos información de la semana
			SemanasEntity semE = dao.getSemana(n_semana_c, anio_c);
			cal.setSemana(semE);
			// Obtenemos listado de horarios del calendario
			List horariosD = dao.getHorariosDespacho(semE.getId_semana(), dto.getId_zona());
			cal.setHorarios(horariosD);
			// Obtenemos listado de jornadas de despacho asociadas
			List jornadasD = daoJ.getJornadasDespachoByIdSemana(semE.getId_semana(),dto.getId_zona());
			cal.setJornadas(jornadasD);
			//*** FIN 		OBTENER CALENDARIO DESPACHO 
			
			// Obtenemos los objetos listado de horarios y listado de jornadas, de la semana a copiar
			if (jornadasD == null)
				logger.debug("jornadas es null");
			
			
			//*** INICIO 	OBTENER CALENDARIO PICKING
			CalendarioPickingDTO cal_pick = new CalendarioPickingDTO();
			
			// Obtenemos información de la semana
			cal_pick.setSemana(semE);
			
			// Obtenemos listado de horarios del calendario
			List horariosP = dao.getHorariosPicking(semE.getId_semana(), dto.getId_local());
			cal_pick.setHorarios(horariosP);
			
			// Obtenemos listado de jornadas de picking asociadas
			List jornadasP = daoJ.getJornadasPickingByIdSemana(semE.getId_semana(),dto.getId_local());
			cal_pick.setJornadas(jornadasP);
			//*** FIN	 	OBTENER CALENDARIO PICKING
			
			//****************************************************************************************
			//****************************************************************************************
			
			//listado de horarios
			//List lst_hor_pick_c = cal_pick.getHorarios();
			//List lst_jor_pick_c = cal_pick.getJornadas();
			
			//guarda la primera y ultima jornada picking, y primera jornada de despacho 
			long id_prim_jor_pick= -1;
			long id_ult_jor_pick = -1;
			long id_prim_jor_desp= -1;

			//controla si existe pedidos en jornadas de picking, segun semana y local
			//boolean existePedido = false;
			
			
			
			//verificar si exiten jornadas de picking
			//si existen jornadas de picking para dicha semana, comparar los horarios con
			//           los horarios de las jornadas a ser copiada
			//
			
			//daoJ.getListaJornadasPickingByIdSemanaLocalZona();
			
			// VERIFICAR SI EXISTEN JORNADAS DE DESPACHO ASOCIADAS 
			// A LAS JORNADAS DE PICKING, EN ZONAS DISTINTAS A LA 
			// ZONA ACTUAL

			int cantJDespdDifZona = daoJ.getJornadaDespXJornadaPickingBySemanaLocalZona(sem_ini.getId_semana(), sem_fin.getId_semana(), dto.getId_zona(), dto.getId_local());
			
			if (cantJDespdDifZona > 0){
			    //throw new CalendarioException(Constantes.MNS_ERROR_CREA_CALEND+" "+Constantes.MNS_EXS_JORNADA_PICKING_EN_USO);
			    
			    //RECUPERAR LAS JORNADAS DE PICKING DE DESTINO Y 
			    //COMPARARLAS CON LAS JORNADAS DE PICKING DEL ORIGEN
				for(int i=0; i<lst_semanas_final.size(); i++){
					SemanasEntity sem_a_crear = (SemanasEntity)lst_semanas_final.get(i);

					List horPICK = dao.getHorariosPicking(sem_a_crear.getId_semana(), dto.getId_local());
					
					if (horariosP.size() != horPICK.size()){
					    //error: numero de jornadas no coinciden
					    throw new CalendarioException(Constantes.MNS_ERROR_CREA_CALEND+" "+Constantes.MNS_EXS_JORNADA_PICKING_DIFIERE_CANTIDAD);
					}
					for (int j=0; j<horariosP.size(); j++ ){
				        //HORARIO DE LA SEMANA DESTINO, EN DONDE SE VA A COPIAR
					    HorarioPickingEntity hor_pick_d = (HorarioPickingEntity)horPICK.get(j);
				        //HORARIO DE LA SEMANA ORIGEN, QUE SE VA A COPIAR
					    HorarioPickingEntity hor_pick_o = (HorarioPickingEntity)horariosP.get(j);
					    
					    //COMPARA LOS HORARIOS DE LAS JORNADAS DE PICKING
					    System.out.println("hor_pick_o.getH_ini(): " + hor_pick_o.getH_ini());
					    System.out.println("hor_pick_o.getH_fin(): " + hor_pick_o.getH_fin());
					    System.out.println("hor_pick_d.getH_ini(): " + hor_pick_d.getH_ini());
					    System.out.println("hor_pick_d.getH_fin(): " + hor_pick_d.getH_fin());
					    if (!hor_pick_o.getH_ini().toString().equals(hor_pick_d.getH_ini().toString()) ||
					            !hor_pick_o.getH_fin().toString().equals(hor_pick_d.getH_fin().toString())){
					        throw new CalendarioException(Constantes.MNS_ERROR_CREA_CALEND+" "+Constantes.MNS_EXS_HORARIO_PICKING_DIFIERES);
					    }
					}	
				}
				//DE NO EXISTIR NINGUN PROBLEMA
				//SE CREAN LAS JORNADAS DE DESPACHO ASOCIANDOLAS A LAS
				//JORNADAS DE PICKING YA EXISTENTES
				
			}else{
			    //AL NO EXISTIR JORNADAS DE DESPACHO CREADAS EN OTRAS ZONAS
			    //SE ELIMINA LAS JORNADAS Y HORARIOS DE PICKING EXISTENTES PARA ESA SEMANA
			    //Y SE CREAN A PARTIR DE LA INFORMACION DE LA SEMANA DE ORIGEN
			    
				for(int a=0; a<lst_semanas_final.size(); a++){
					SemanasEntity sem_a_crear = (SemanasEntity)lst_semanas_final.get(a);

					//ELIMINA LOS HORARIOS Y JORNADAS DE PICKING
				    eliminar = daoJ.doDelJornadaPickingBySemana(sem_a_crear.getId_semana(), dto.getId_local());
					logger.debug("elim JorPick?	"+eliminar);
					eliminar = daoJ.doDelHorarioPickingBySemana(sem_a_crear.getId_semana(), dto.getId_local()); 
					logger.debug("elim HorPick?	"+eliminar);
				    
					//CREA LOS NUEVOS HORARIOS Y JORNADAS DE PICKING
					//DE ACUERDO A LA SEMANA DE ORIGEN
					for (int i=0; i<horariosP.size(); i++ ){
						HorarioPickingEntity hor_pick_c = (HorarioPickingEntity)horariosP.get(i);
						
						//List listJorPick = daoJ.getListaJornadasPickingByIdSemanaLocalZona(sem_a_crear.getId_semana(), dto.getId_local(), dto.getId_zona());
						//dao.getHorarioPickingByIdJorPick();
						
						long id_hor_pick_crear = dao.InsHorarioPicking(dto.getId_local(), sem_a_crear.getId_semana(), hor_pick_c.getH_ini(), hor_pick_c.getH_fin());
						logger.debug("id_hor_pick_crear	:"+id_hor_pick_crear);
						
						//- obtener las jornadas de picking e insertar en la nueva semana
						for(int j=0; j<jornadasP.size(); j++){
							JornadaPickingEntity jor_pick_c = (JornadaPickingEntity)jornadasP.get(j);
							
							//obtener la fecha pick correspondiente
							Date fecha_ini_pick = new Date();
							int dia_f_pick = Formatos.getDiaSemanaByFecha(jor_pick_c.getFecha().toString());
							logger.debug("dia_f_pick: "+dia_f_pick);
							fecha_ini_pick = new SimpleDateFormat("yyyy-MM-dd").parse(
									Formatos.getFechaByDiaSemAnio(dia_f_pick, sem_a_crear.getN_semana(), sem_a_crear.getAno()));
							
							long id_jpick_crear = -1;
							if(jor_pick_c.getId_hor_picking()==hor_pick_c.getId_hor_pick()){
								//insertar la jornada de picking
								id_jpick_crear = daoJ.doInsJornadaPicking(dto.getId_local(), id_hor_pick_crear, sem_a_crear.getId_semana(), jor_pick_c.getDay_of_week(),
										fecha_ini_pick, Integer.parseInt(jor_pick_c.getCapac_picking()+""),jor_pick_c.getHrs_validacion(),jor_pick_c.getHrs_ofrecido_web());
								logger.debug("id_jpick_crear	:"+id_jpick_crear);
							}
							
							//ultima jornada pick
							if( (a == (lst_semanas_final.size()-1) ) && (i == (horariosP.size()-1) ) && (j == (jornadasP.size()-1) )){
								id_ult_jor_pick = id_jpick_crear;
							}
						}// fin del for de jor_pick_c
					}//fin del for de horariosP
				}
			}
			
			//CREACION DE JORNADAS DE DESPACHO
			for(int a=0; a<lst_semanas_final.size(); a++){
				SemanasEntity sem_a_crear = (SemanasEntity)lst_semanas_final.get(a);
				
				//construir el calendario de jornadas de despacho
				//listar por horarios q tiene la semana a copiar
				//construir informacion para AdmHorarioDespachoDTO hdespacho
				AdmHorarioDespachoDTO hdespacho = new AdmHorarioDespachoDTO(); 
				
				for (int i=0; i<horariosD.size(); i++){			
					HorarioDespachoEntity hor = (HorarioDespachoEntity)horariosD.get(i);
					String hor_ini = hor.getH_ini().toString();
					String hor_fin = hor.getH_fin().toString();
					
					//revisar jornadas:
					int capac_lu	=-1;
					int capac_ma	=-1;
					int capac_mi	=-1;
					int capac_ju	=-1;
					int capac_vi	=-1;
					int capac_sa	=-1;
					int capac_do	=-1;
					
					long jor_lu		=-1;
					long jor_ma		=-1;
					long jor_mi		=-1;
					long jor_ju		=-1;
					long jor_vi		=-1;
					long jor_sa		=-1;
					long jor_do		=-1;
					
					int tarifa_normal_lu = -1;
					int tarifa_normal_ma = -1;
					int tarifa_normal_mi = -1;
					int tarifa_normal_ju = -1;
					int tarifa_normal_vi = -1;
					int tarifa_normal_sa = -1;
					int tarifa_normal_do = -1;
					
					int tarifa_economica_lu = -1;
					int tarifa_economica_ma = -1;
					int tarifa_economica_mi = -1;
					int tarifa_economica_ju = -1;
					int tarifa_economica_vi = -1;
					int tarifa_economica_sa = -1;
					int tarifa_economica_do = -1;
					
					int tarifa_express_lu = -1;
					int tarifa_express_ma = -1;
					int tarifa_express_mi = -1;
					int tarifa_express_ju = -1;
					int tarifa_express_vi = -1;
					int tarifa_express_sa = -1;
					int tarifa_express_do = -1;
					
					int tarifa_umbral_lu = -1;
					int tarifa_umbral_ma = -1;
					int tarifa_umbral_mi = -1;
					int tarifa_umbral_ju = -1;
					int tarifa_umbral_vi = -1;
					int tarifa_umbral_sa = -1;
					int tarifa_umbral_do = -1;
					
					//datos iniciales del horario de picking
					long id_jor_pick_aux = -1;
					
					logger.debug("jornadas.size():"+jornadasD.size());
					for (int j=0; j<jornadasD.size();j++){
						JornadaDespachoEntity jor = new JornadaDespachoEntity();					
						jor =	(JornadaDespachoEntity)jornadasD.get(j);
						
						// jornadas que pertenecen al horario i
						if ( hor.getId_hor_desp() == jor.getId_hor_desp() ){
							
							//obtener informacion de la jornada de picking 
							JornadaDTO jpick = daoJ.getJornadaById(jor.getId_jpicking());
							
							// Originalmente se estab obteniendo el horario de despacho, en vez del horario de
							// picking. Se cambia esta situación.
							HorarioPickingEntity hor_pick = dao.getHorarioPickingByIdJorPick(jor.getId_jpicking());
							logger.debug("Horario de Picking detectado: " + hor_pick.getH_ini().getHours());
							
							//datos de semana a crear:
							logger.debug("sem_a_crear.getN_semana(): "+sem_a_crear.getN_semana()+", sem_a_crear.getAno():"+sem_a_crear.getAno());
							
							//obtener datos de la semana anterior 
							//encontrar id de semana anterior
							int n_semana_ant = sem_a_crear.getN_semana();
							int anio_ant = sem_a_crear.getAno();
							//si semana a crear es primera semana del año
							if(sem_a_crear.getN_semana()==1){
								n_semana_ant = Constantes.MAX_SEMANAS_X_ANIO;
								anio_ant--;
							}else{
								n_semana_ant--;
							}
							SemanasEntity sem_ant = dao.getSemana(n_semana_ant, anio_ant);
							while(sem_ant==null){
								if(n_semana_ant==1){
									n_semana_ant = Constantes.MAX_SEMANAS_X_ANIO;
									anio_ant--;
								}else{
									n_semana_ant--;
								}
								sem_ant = dao.getSemana(n_semana_ant, anio_ant);
							}
							
							Date fecha_ini_pick = new Date();
							int dia_f_pick = Formatos.getDiaSemanaByFecha(jpick.getF_jornada());
							logger.debug("dia_f_pick: "+dia_f_pick);
							
							String fec_aux ="";
							//si la fecha de despacho y la fecha de picking son iguales, entonces obtener la fecha de inicio de la semana
							if( jor.getId_semana()>jpick.getId_semana()){
								logger.debug("sem_ant.getN_semana(): "+sem_ant.getN_semana()+", sem_ant.getAno():"+sem_ant.getAno());
								//en caso q dia de fecha pick sea de una semana anterior a semana de jornada picking, relacionar con semana anterior
								fec_aux = Formatos.getFechaByDiaSemAnio(dia_f_pick, sem_ant.getN_semana(), sem_ant.getAno()); 
							}else{
								//relacionar con la semana a crear
								fec_aux = Formatos.getFechaByDiaSemAnio(dia_f_pick, sem_a_crear.getN_semana(), sem_a_crear.getAno());
							}
							logger.debug("fec_aux: "+fec_aux);
							fecha_ini_pick = new SimpleDateFormat("yyyy-MM-dd").parse(fec_aux);
							
							logger.debug("fecha_ini_pick:"+fecha_ini_pick.toString());
							logger.debug("fecha_ini_pick:"+Formatos.frmFechaByDate(fecha_ini_pick));
							
							logger.debug("jor.getId_semana()	:"+jor.getId_semana()+", jpick.getId_semana()	:"+jpick.getId_semana());
							//obtener la jornada de picking correspondiente
							if( jor.getId_semana()>jpick.getId_semana()){
								id_jor_pick_aux = daoJ.getJornadasPickingByDatos(jpick.getDow(),sem_ant.getId_semana(),hor_pick);
							}else{
								id_jor_pick_aux = daoJ.getJornadasPickingByDatos(jpick.getDow(),sem_a_crear.getId_semana(),hor_pick);
							}
							logger.debug("id_jpicking obtenido = " + id_jor_pick_aux);
							//en caso q no encuentre la jornada
							if(id_jor_pick_aux == -1){
								//obtener ultima jornada picking del dia anterior
								JornadaPickingEntity jpick_ent = daoJ.getJornadaPickingUltimo(Formatos.frmFechaByDate(fecha_ini_pick), dto.getId_local());
								if(jpick_ent != null){
									id_jor_pick_aux = jpick_ent.getId_jpicking();
								}else
									throw new CalendarioException(Constantes.MNS_ERROR_ACT_JORNADAS_DESP);
								
							}
							
							Calendar cal_ini = new GregorianCalendar();
							cal_ini.setFirstDayOfWeek(Calendar.MONDAY);
							cal_ini.setTime(fecha_ini_pick);
							
							logger.debug("jor.getDay_of_week()	:"+jor.getDay_of_week());
							
							switch(jor.getDay_of_week()){
							//switch(jpick.getDow()){
							case 1:
								cal_ini.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
								capac_lu = Integer.parseInt(jor.getCapac_despacho()+"");
								jor_lu 	= id_jor_pick_aux;
								tarifa_express_lu   = jor.getTarifa_express();
								tarifa_normal_lu    = jor.getTarifa_normal();
								tarifa_economica_lu = jor.getTarifa_economica();
								tarifa_umbral_lu	= jor.getTarifa_umbral();
								break;
							case 2:
								cal_ini.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
								capac_ma = Integer.parseInt(jor.getCapac_despacho()+"");
								jor_ma 	= id_jor_pick_aux;
								tarifa_express_ma   = jor.getTarifa_express();
								tarifa_normal_ma    = jor.getTarifa_normal();
								tarifa_economica_ma = jor.getTarifa_economica();
								tarifa_umbral_ma	= jor.getTarifa_umbral();
								break;
						
							case 3:
								cal_ini.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
								capac_mi = Integer.parseInt(jor.getCapac_despacho()+"");
								jor_mi 	= id_jor_pick_aux;
								tarifa_express_mi   = jor.getTarifa_express();
								tarifa_normal_mi    = jor.getTarifa_normal();
								tarifa_economica_mi = jor.getTarifa_economica();
								tarifa_umbral_mi	= jor.getTarifa_umbral();
								break;
								
							case 4:
								cal_ini.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
								capac_ju = Integer.parseInt(jor.getCapac_despacho()+"");
								jor_ju 	= id_jor_pick_aux;
								tarifa_express_ju   = jor.getTarifa_express();
								tarifa_normal_ju    = jor.getTarifa_normal();
								tarifa_economica_ju = jor.getTarifa_economica();
								tarifa_umbral_ju	= jor.getTarifa_umbral();
								break;
								
							case 5:
								cal_ini.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
								capac_vi = Integer.parseInt(jor.getCapac_despacho()+"");
								jor_vi 	= id_jor_pick_aux;
								tarifa_express_vi   = jor.getTarifa_express();
								tarifa_normal_vi    = jor.getTarifa_normal();
								tarifa_economica_vi = jor.getTarifa_economica();
								tarifa_umbral_vi	= jor.getTarifa_umbral();
								break;
								
							case 6:
								cal_ini.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
								capac_sa = Integer.parseInt(jor.getCapac_despacho()+"");
								jor_sa 	= id_jor_pick_aux;
								tarifa_express_sa   = jor.getTarifa_express();
								tarifa_normal_sa    = jor.getTarifa_normal();
								tarifa_economica_sa = jor.getTarifa_economica();
								tarifa_umbral_sa	= jor.getTarifa_umbral();
								break;
								
							case 7:
								cal_ini.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
								capac_do = Integer.parseInt(jor.getCapac_despacho()+"");
								jor_do 	= id_jor_pick_aux;
								tarifa_express_do   = jor.getTarifa_express();
								tarifa_normal_do    = jor.getTarifa_normal();
								tarifa_economica_do = jor.getTarifa_economica();
								tarifa_umbral_do	= jor.getTarifa_umbral();
								break;
								
							}//end switch
						
						}//end if
						
					}//fin de for de jornadas
					
					hdespacho.setCapac_lu(capac_lu);
					hdespacho.setCapac_ma(capac_ma);
					hdespacho.setCapac_mi(capac_mi);
					hdespacho.setCapac_ju(capac_ju);
					hdespacho.setCapac_vi(capac_vi);
					hdespacho.setCapac_sa(capac_sa);
					hdespacho.setCapac_do(capac_do);
					hdespacho.setH_ini(java.sql.Time.valueOf(hor_ini));
					hdespacho.setH_fin(java.sql.Time.valueOf(hor_fin));
					hdespacho.setId_jpicking_lu(jor_lu);
					hdespacho.setId_jpicking_ma(jor_ma);
					hdespacho.setId_jpicking_mi(jor_mi);
					hdespacho.setId_jpicking_ju(jor_ju);
					hdespacho.setId_jpicking_vi(jor_vi);
					hdespacho.setId_jpicking_sa(jor_sa);
					hdespacho.setId_jpicking_do(jor_do);
					
					hdespacho.setTarifa_normal_lu(tarifa_normal_lu);
					hdespacho.setTarifa_normal_ma(tarifa_normal_ma);
					hdespacho.setTarifa_normal_mi(tarifa_normal_mi);
					hdespacho.setTarifa_normal_ju(tarifa_normal_ju);
					hdespacho.setTarifa_normal_vi(tarifa_normal_vi);
					hdespacho.setTarifa_normal_sa(tarifa_normal_sa);
					hdespacho.setTarifa_normal_do(tarifa_normal_do);
					
					hdespacho.setTarifa_economica_lu(tarifa_economica_lu);
					hdespacho.setTarifa_economica_ma(tarifa_economica_ma);
					hdespacho.setTarifa_economica_mi(tarifa_economica_mi);
					hdespacho.setTarifa_economica_ju(tarifa_economica_ju);
					hdespacho.setTarifa_economica_vi(tarifa_economica_vi);
					hdespacho.setTarifa_economica_sa(tarifa_economica_sa);
					hdespacho.setTarifa_economica_do(tarifa_economica_do);
					
					hdespacho.setTarifa_express_lu(tarifa_express_lu);
					hdespacho.setTarifa_express_ma(tarifa_express_ma);
					hdespacho.setTarifa_express_mi(tarifa_express_mi);
					hdespacho.setTarifa_express_ju(tarifa_express_ju);
					hdespacho.setTarifa_express_vi(tarifa_express_vi);
					hdespacho.setTarifa_express_sa(tarifa_express_sa);
					hdespacho.setTarifa_express_do(tarifa_express_do);
					
					hdespacho.setTarifa_umbral_lu(tarifa_umbral_lu);
					hdespacho.setTarifa_umbral_ma(tarifa_umbral_ma);
					hdespacho.setTarifa_umbral_mi(tarifa_umbral_mi);
					hdespacho.setTarifa_umbral_ju(tarifa_umbral_ju);
					hdespacho.setTarifa_umbral_vi(tarifa_umbral_vi);
					hdespacho.setTarifa_umbral_sa(tarifa_umbral_sa);
					hdespacho.setTarifa_umbral_do(tarifa_umbral_do);

					//primer id_jor_pick
					if(a==0 && i==0){
						id_prim_jor_pick = jor_lu;
					}
					
					//recorres listado final de semanas, y para cada horario se genera su respectiva jornada
					//long id_nvo_jdesp = this.doInsJornadasDespachoyHorario(sem_a_crear.getId_semana(), dto.getId_zona(), hdespacho);

					long id_nvo_jdesp = -1;
					
					//**** INICIO DE INSERTAR HORARIO Y JDESPACHO
					long id_semana 	= sem_a_crear.getId_semana();
					long id_zona 	= dto.getId_zona();
					// Rescatamos semana
					SemanasEntity sem = dao.getSemana(id_semana);
					Calendar cald = new GregorianCalendar();
					cald.setFirstDayOfWeek(Calendar.MONDAY);
					cald.setTime(sem.getF_ini());
					// Inserta horario

					long id_horario = dao.InsHorarioDespacho(id_zona, id_semana, hdespacho.getH_ini(), hdespacho.getH_fin());
					logger.debug("id_horario insertado: " + id_horario);
					
					// Inserta jornadas picking
					long id_jornada;
					
					//lunes
					logger.debug("id_zona: "+id_zona);
					logger.debug("id_horario: "+id_horario);
					logger.debug("id_semana: "+id_semana);
					logger.debug("hdespacho.getId_jpicking_lu(): "+hdespacho.getId_jpicking_lu());
					logger.debug("1: "+1);
					logger.debug("cald.getTime(): "+cald.getTime());
					logger.debug("hdespacho.getCapac_lu(): "+hdespacho.getCapac_lu());
					
					
					cald.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);			
					id_jornada = daoJ.doInsJornadaDespacho(id_zona, id_horario, id_semana, hdespacho.getId_jpicking_lu(),1,cald.getTime(),hdespacho.getCapac_lu(), hdespacho.getTarifa_express_lu(), hdespacho.getTarifa_normal_lu(), hdespacho.getTarifa_economica_lu(), hdespacho.getTarifa_umbral_lu());			
					logger.debug("id_jor lu: " + id_jornada);
					logger.debug("cal.MONDAY " + Calendar.MONDAY);

					//martes
					cald.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
					id_jornada = daoJ.doInsJornadaDespacho(id_zona, id_horario, id_semana, hdespacho.getId_jpicking_ma(),2,cald.getTime(),hdespacho.getCapac_ma(), hdespacho.getTarifa_express_ma(), hdespacho.getTarifa_normal_ma(), hdespacho.getTarifa_economica_ma(), hdespacho.getTarifa_umbral_ma());
					logger.debug("id_jor ma: " + id_jornada);
					
					//miercoles
					cald.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
					id_jornada = daoJ.doInsJornadaDespacho(id_zona, id_horario, id_semana, hdespacho.getId_jpicking_mi(),3,cald.getTime(),hdespacho.getCapac_mi(), hdespacho.getTarifa_express_mi(), hdespacho.getTarifa_normal_mi(), hdespacho.getTarifa_economica_mi(), hdespacho.getTarifa_umbral_mi());
					logger.debug("id_jor mi: " + id_jornada);
					
					//jueves
					cald.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
					id_jornada = daoJ.doInsJornadaDespacho(id_zona, id_horario, id_semana, hdespacho.getId_jpicking_ju(),4,cald.getTime(),hdespacho.getCapac_ju(), hdespacho.getTarifa_express_ju(), hdespacho.getTarifa_normal_ju(), hdespacho.getTarifa_economica_ju(), hdespacho.getTarifa_umbral_ju());
					logger.debug("id_jor ju: " + id_jornada);
					
					//viernes
					cald.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
					id_jornada = daoJ.doInsJornadaDespacho(id_zona, id_horario, id_semana, hdespacho.getId_jpicking_vi(),5,cald.getTime(),hdespacho.getCapac_vi(), hdespacho.getTarifa_express_vi(), hdespacho.getTarifa_normal_vi(), hdespacho.getTarifa_economica_vi(), hdespacho.getTarifa_umbral_vi());
					logger.debug("id_jor vi: " + id_jornada);
					
					//sabado
					cald.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
					id_jornada = daoJ.doInsJornadaDespacho(id_zona, id_horario, id_semana, hdespacho.getId_jpicking_sa(),6,cald.getTime(),hdespacho.getCapac_sa(), hdespacho.getTarifa_express_sa(), hdespacho.getTarifa_normal_sa(), hdespacho.getTarifa_economica_sa(), hdespacho.getTarifa_umbral_sa());
					logger.debug("id_jor sa: " + id_jornada);
					
					//domingo
					cald.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
					id_jornada = daoJ.doInsJornadaDespacho(id_zona, id_horario, id_semana, hdespacho.getId_jpicking_do(),7,cald.getTime(),hdespacho.getCapac_do(), hdespacho.getTarifa_express_do(), hdespacho.getTarifa_normal_do(), hdespacho.getTarifa_economica_do(), hdespacho.getTarifa_umbral_do());
					logger.debug("id_jor do: " + id_jornada);
					//**** FIN  DE INSERTAR HORARIO Y JDESPACHO
					
					//no te olvides de id_jornada
					id_nvo_jdesp = id_jornada;
					
					logger.debug("id_nvo_jdesp	:"+id_nvo_jdesp);
					if(a==0 && i==0){
						id_prim_jor_desp = id_nvo_jdesp; 
					}
				}//fin de for de horarios
			}
			
	
			//mostrar los valores de jornadas pick: primero y ultimo
			logger.debug("id_prim_jor_pick	:"+id_prim_jor_pick);
			logger.debug("id_ult_jor_pick	:"+id_ult_jor_pick);
			logger.debug("id_prim_jor_desp	:"+id_prim_jor_desp);
			
			//buscar si el primer id_jor_pick tiene solo una jdesp relacionada
			long id_jor_desp_ultimo = daoJ.getIdJornadaDespByJornadaPick(id_prim_jor_pick,sem_fin.getId_semana(), dto.getId_zona());
			logger.debug("id_jor_desp_ultimo	:"+id_jor_desp_ultimo);
			//si existe, actualizar id_jpick en la jornada de despacho encontrada.
			if(id_jor_desp_ultimo>0 && (id_jor_desp_ultimo != id_prim_jor_desp)){
				boolean actualizo = daoJ.doActJDespachoByJPick(id_jor_desp_ultimo, id_ult_jor_pick);
				logger.debug("actualizo?	"+actualizo);
			}
			
			//revisar el listado de jdespacho inicial cuyos jpicking son nulos, si existe elementos, enlazar con su respectivo id_jpicking
			for(int i=0; i<lst_jdesp_rel.size(); i++){
				JornadaDespachoEntity jor = (JornadaDespachoEntity)lst_jdesp_rel.get(i);
				//obtener el dia anterior:
				String fecha_ant = Formatos.getFechaFinal(Formatos.frmFechaByDate(jor.getFecha()),1,'-');
				//encontrar el id_jpicking correspondiente
				JornadaPickingEntity jpick_enc = daoJ.getJornadaPickingUltimo(fecha_ant, dto.getId_local());
				if(jpick_enc == null)
					throw new CalendarioException(Constantes.MNS_ERROR_ACT_JORNADAS_DESP);
				boolean actualizo = daoJ.doActJDespachoByJPick(jor.getId_jdespacho(), jpick_enc.getId_jpicking());
				logger.debug("actualizo?	"+actualizo);
			}
			result = true;
			
		}catch (CalendarioDAOException e) {
			
			//rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			e.printStackTrace();
			throw new CalendarioException(e);
		}catch (Exception e){
			//rollback trx
			try{
				trx1.rollback();
			}catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			throw new CalendarioException(e);
		}
		
		
		//cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			throw new SystemException("Error al finalizar transacción");
		}
		
		return result;
	}

    /**
     * @param fecha
     * @param semana
     * @param anio
     * @param idZona
     * @return
     */
    public CalendarioDespachoDTO getCalendarioDespachoByFecha(Date fecha, int semana, int anio, long idZona) throws CalendarioException{

        List horarios = new ArrayList();
        List jornadas = new ArrayList();
        
        // dto que retorna el método
        CalendarioDespachoDTO cal = new CalendarioDespachoDTO();
        
        try {
            
            // Creamos los dao's
            JdbcCalendarioDAO dao = (JdbcCalendarioDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCalendarioDAO();
            JdbcJornadasDAO  dao2 = (JdbcJornadasDAO)   DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
            
            // Obtenemos información de la semana
            SemanasEntity sem = dao.getSemana(semana, anio);
            cal.setSemana(sem);
            
            // Obtenemos listado de horarios del calendario
            horarios = dao.getHorariosDespacho(sem.getId_semana(), idZona);
            cal.setHorarios(horarios);
            
            // Obtenemos listado de jornadas de despacho asociadas
            jornadas = dao2.getJornadasDespachoByFecha(fecha, sem.getId_semana(),idZona);
            cal.setJornadas(jornadas);
            
        } catch (CalendarioDAOException e) {
            e.printStackTrace();
            throw new CalendarioException(e);
        } catch (JornadasDAOException e) {
            e.printStackTrace();
            throw new CalendarioException(e);
        }
    
        return cal; 
    }

    /**
     * @param diasCalendario
     * @param idZona
     * @return
     */
    public List getCalendarioDespachoByDias(int diasCalendario, long idZona) throws CalendarioException{
        try {
            JdbcCalendarioDAO dao = (JdbcCalendarioDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCalendarioDAO();
            return dao.getCalendarioDespachoByDias(diasCalendario, idZona);            
        } catch (CalendarioDAOException e) {
            e.printStackTrace();
            throw new CalendarioException(e);
        }
    }

    public List getCalendarioDespachoByDias(int diasCalendario, long idZona, long id_jpicking, int cantProductos) throws CalendarioException{
        try {
            JdbcCalendarioDAO dao = (JdbcCalendarioDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCalendarioDAO();
            JdbcJornadasDAO  daoJornadas = (JdbcJornadasDAO)   DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
            int capacidad_ocupada = daoJornadas.ObtenerCapacidadOcupada(id_jpicking, cantProductos);
            return dao.getCalendarioDespachoByDias(diasCalendario, idZona, capacidad_ocupada);   
        } catch (CalendarioDAOException e) {
            e.printStackTrace();
            throw new CalendarioException(e);
        }
    }

    /**
     * @param idZona
     * @param fechaDespacho
     * @param cantProductos
     * @return
     */
    public long getJornadaDespachoMayorCapacidad(long idZona, String fechaDespacho, long cantProductos) throws CalendarioException{
        try {
            JdbcJornadasDAO  dao = (JdbcJornadasDAO)   DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
            return dao.getJornadaDespachoMayorCapacidad(idZona, fechaDespacho, cantProductos);            
        } catch (JornadasDAOException e) {
            e.printStackTrace();
            throw new CalendarioException(e);
        }
    }
    
	/**
	 * Realiza proceso de Re-agendamiento de un despacho, por lo tanto libera la capacidad
	 * de picking utilizada inicialmente.<br>
	 * Nota: Este método tiene transaccionalidad
	 * 
	 * @param  id_pedido long 
	 * @param  id_jdespacho long 
	 * @param  sobrescribeprecio boolean 
	 * @param  precio int 
	 * @param  usr_login String 
	 * 
	 * @throws DespachosException
	 * @throws SystemException, en caso exista error de sistema. 
	 * 
	 */
	public boolean doReagendaDespachoLocal(PedidoDTO oPedido, JorDespachoCalDTO oJorDespachoNuevaCalDTO, LocalDTO localDtoNuevo, double costoDespachoNuevo, boolean isRetiroLocal, long direccionId, boolean modificarJPicking)
		throws DespachosException, SystemException {		
		
		JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		JdbcPedidosDAO dao2 = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		JdbcZonasDespachoDAO dao5 = (JdbcZonasDespachoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getZonasDespachoDAO();
		
		//Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
		boolean act= false;
		
		//Iniciamos trx
		try {
			trx1.begin();
		} catch (Exception e1) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción");
		}

		//Marcamos los dao's con la transacción		
		try {
			dao.setTrx(trx1);
		} catch (JornadasDAOException e2) {
			logger.error("Error al asignar transacción al dao Jornadas");
			throw new SystemException("Error al asignar transacción al dao Jornadas");
		}
		
		try {
			dao2.setTrx(trx1);
		} catch (PedidosDAOException e2) {
			logger.error("Error al asignar transacción al dao Pedido");
			throw new SystemException("Error al asignar transacción al dao Pedido");
		}	
		
		try {
			dao5.setTrx(trx1);
		} catch (ZonasDespachoDAOException e) {
			logger.error("Error al asignar transacción al dao ZonasDespachoDAOException");
			throw new SystemException("Error al asignar transacción al dao ZonasDespachoDAOException");
		}
		
		 
		
		PedidosCtrl ctrl = new PedidosCtrl();
		
		try {
			
			//PedidoDTO oPedido = ctrl.getPedidoById(idPedido);

			if(oJorDespachoNuevaCalDTO.getId_jdespacho()<0){
				throw new DespachosException(Constantes._EX_JDESP_FALTAN_DATOS);
			}
			JornadaDespachoEntity jor = dao.getJornadaDespachoById(oJorDespachoNuevaCalDTO.getId_jdespacho());
            
            long idJPicking = jor.getId_jpicking();			
            if ( oPedido.getId_jpicking() == 0 ) {
                idJPicking = 0;
            }
			
			// 1. verifica si la jornada de despacho solicitada tiene capacidad de despacho (y de picking)
			if (!oPedido.getTipo_ve().equals("S") && modificarJPicking ){
				if ( this.doVerificaCapacidadDespacho(oPedido.getCant_prods(),oJorDespachoNuevaCalDTO.getId_jdespacho()) == false ){
					trx1.rollback();
					throw new DespachosException(Constantes._EX_JDESP_SIN_CAPACIDAD); 
				}	
			}					
			
			// 6. actualiza el pedido con la nueva jornada y el nuevo costo de despacho			
			if ( costoDespachoNuevo >  oPedido.getCosto_despacho() ){
				trx1.rollback();
				throw new DespachosException(Constantes._EX_JDESP_SIN_CAPACIDAD); 
			}
			
			//TIPOS DE CAMBIO
			
			//1 - De retiro a retiro
			if(isRetiroLocal && "R".equals(oPedido.getTipo_despacho())){
				//Cambiamos el ID_LOCAL, ID_JDESPACHO, ID_JPICKING,ID_COMUNA, ID_ZONA, INDICACION, DIR_CALLE(direccion local)
				List comuna = dao5.getComunasByIdZonaDespacho(oJorDespachoNuevaCalDTO.getId_zona()); 
				long idComuna = ((ComunaDTO) comuna.get(0)).getId_comuna();
				
				act=dao2.doActualizaPedidoJornadasLocalRetiroxRetiro(oPedido, oJorDespachoNuevaCalDTO, idJPicking, localDtoNuevo, (int)costoDespachoNuevo,idComuna);
			}
			
			//2 - De retiro a domicilio
			else if(!isRetiroLocal && "R".equals(oPedido.getTipo_despacho())){
				//Cambiamos a direccion
				DireccionEntity oDireccion = (DireccionEntity) dao2.getDireccionById(direccionId); 
				act=dao2.doActualizaPedidoJornadasLocalRetiroxDomicilio(oPedido, oJorDespachoNuevaCalDTO, idJPicking, localDtoNuevo, (int)costoDespachoNuevo, oDireccion);
			}
			
			//3 - De domicilio a retiro
			else if(isRetiroLocal && !"R".equals(oPedido.getTipo_despacho())){
				List comuna = dao5.getComunasByIdZonaDespacho(oJorDespachoNuevaCalDTO.getId_zona()); 
				long idComuna = ((ComunaDTO) comuna.get(0)).getId_comuna();	
				act=dao2.doActualizaPedidoJornadasLocalDomicilioxRetiro(oPedido, oJorDespachoNuevaCalDTO, idJPicking, localDtoNuevo, (int)costoDespachoNuevo,idComuna);
			}
			
			//4 - De domicilio a domicilio
			else if(!isRetiroLocal && !"R".equals(oPedido.getTipo_despacho())){
				DireccionEntity oDireccion = (DireccionEntity) dao2.getDireccionById(direccionId);
				
				act=dao2.doActualizaPedidoJornadasDomicilioxDomicilio(oPedido, oJorDespachoNuevaCalDTO, idJPicking, localDtoNuevo, (int)costoDespachoNuevo, oDireccion);
			}
			
			
			//act=dao2.doActualizaPedidoJornadasLocal(oPedido, oJorDespachoNuevaCalDTO, idJPicking, localDtoNuevo, (int)costoDespachoNuevo, modificarJPicking);
			
			
			
		} catch (JornadasDAOException e) {
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			e.printStackTrace();
			throw new DespachosException(e);
		}  
		catch (LocalDAOException e) {
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			e.printStackTrace();
			throw new DespachosException(e);
		}		
		catch (PedidosDAOException e) {
			//			rollback trx
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			
			e.printStackTrace();
			throw new DespachosException(e);
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
		
		return act;
	}
	
	public boolean doActualizarCapacidadReagendaDespachoLocal(PedidoDTO oPedido, JorDespachoCalDTO oJorDespachoNuevaCalDTO, boolean modificarJPicking)
			throws DespachosException, SystemException {		
			
			JdbcJornadasDAO dao = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
			
			//Creamos trx
			JdbcTransaccion trx1 = new JdbcTransaccion();
			boolean act= false;
			
			//Iniciamos trx
			try {
				trx1.begin();
			} catch (Exception e1) {
				logger.error("Error al iniciar transacción");
				throw new SystemException("Error al iniciar transacción");
			}

			//Marcamos los dao's con la transacción		
			try {
				dao.setTrx(trx1);
			} catch (JornadasDAOException e2) {
				logger.error("Error al asignar transacción al dao Jornadas");
				throw new SystemException("Error al asignar transacción al dao Jornadas");
			}
						
			
			try {				
					//Se cambia la posicion para actualizar capacidades
					
					// 1. liberar capacidad de despacho de jornada despacho en que se encuentra el pedido inicialmente
					if (!oPedido.getTipo_ve().equals("S")){
						dao.doOcupaCapacidadDespacho(oPedido.getId_jdespacho(), -1);
					}
					
					// 2. toma nueva capacidad de despacho de la jornada_despacho solicitada
					if (!oPedido.getTipo_ve().equals("S")){
						dao.doOcupaCapacidadDespacho(oJorDespachoNuevaCalDTO.getId_jdespacho(), 1);
					}
										
					// 3. libera capacidad de picking de la jornada relacionada que tiene el pedido inicialmente
		            // No aplica para reprogramacion en modulo de despacho
					if (!oPedido.getTipo_ve().equals("S") && modificarJPicking){
						dao.actualizarCapacidadOcupadaPicking(oPedido.getId_jpicking());
						dao.actualizarCapacidadOcupadaPicking(oJorDespachoNuevaCalDTO.getId_jpicking());
						//dao.doOcupaCapacidadPicking( oPedido.getId_jpicking(), (int)oPedido.getCant_prods()*-1 );
					}				
					
					// 4. toma nueva capacidad de picking de la jornada relacionada
					// No aplica para reprogramacion en modulo de despacho
					//if (!oPedido.getTipo_ve().equals("S") && modificarJPicking){
						//dao.doOcupaCapacidadPicking( oJorDespachoNuevaCalDTO.getId_jpicking(), (int)oPedido.getCant_prods() );	
					//}				
				
			} catch (JornadasDAOException e) {
				//			rollback trx
				try {
					trx1.rollback();
				} catch (DAOException e1) {
					logger.error("Error al hacer rollback");
					throw new SystemException("Error al hacer rollback");
				}
				e.printStackTrace();
				throw new DespachosException(e);
			} 
			//cerramos trx
			try {
				trx1.end();
			} catch (DAOException e) {
				logger.error("Error al finalizar transacción");
				throw new SystemException("Error al finalizar transacción");
			}
			
			return act;
		}
	
	
	


}