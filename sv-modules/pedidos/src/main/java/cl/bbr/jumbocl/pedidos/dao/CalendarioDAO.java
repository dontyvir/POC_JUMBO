package cl.bbr.jumbocl.pedidos.dao;

import java.sql.Time;
import java.util.List;

import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.HorarioPickingEntity;
import cl.bbr.jumbocl.common.model.SemanasEntity;
import cl.bbr.jumbocl.pedidos.exceptions.CalendarioDAOException;

/**
 * Permite las operaciones en base de datos sobre los Calendarios.
 * 
 * @author BBR
 *
 */
public interface CalendarioDAO {
	
	//************************************************************/
	/**
	 * Obtiene entidad Semana
	 * 
	 * @param  id_semana
	 * @return SemanasEntity
	 * @throws CalendarioDAOException
	 */
	public SemanasEntity getSemana(long id_semana)
		throws CalendarioDAOException;

	/**
	 * Obtiene entidad Semana
	 * 
	 * @param  n_semana
	 * @param  ano
	 * @return SemanasEntity
	 * @throws CalendarioDAOException
	 */
	public SemanasEntity getSemana(int n_semana, int ano)
		throws CalendarioDAOException;
	
	/**
	 * Inserta registro a la tabla BO_SEMANAS
	 * 
	 * @param  n_semana
	 * @param  ano
	 * @return long
	 * @throws CalendarioDAOException
	 */
	public long InsSemana(int n_semana, int ano)
		throws CalendarioDAOException;
	
	//	************************************************************/
	/**
	 * Obtiene un horario de picking definido para una semana en un calendario de picking de un local
	 * 
	 * @param  id_hor_pick
	 * @return HorarioPickingEntity
	 * @throws CalendarioDAOException
	 */
	public HorarioPickingEntity getHorarioPicking(long id_hor_pick)
		throws CalendarioDAOException;
	
	/**
	 * Obtiene listado de horarios definidos para una semana en un calendario de despacho para
	 * una zona de despacho de un local
	 * 
	 * @param  id_semana
	 * @param  id_local
	 * @return List HorarioPickingEntity
	 * @throws CalendarioDAOException
	 */
	public List getHorariosPicking(long id_semana, long id_local)
		throws CalendarioDAOException;	
	
	/**
	 * Inserta un registro en la tabla bo_horario_pick
	 * 
	 * @param  id_local
	 * @param  id_semana
	 * @param  hini
	 * @param  hfin
	 * @return long
	 * @throws CalendarioDAOException
	 */
	public long InsHorarioPicking(long id_local, long id_semana, Time hini, Time hfin)
		throws CalendarioDAOException;
	
	/**
	 * Elimina registro en la tabla bo_horarios_picking
	 * 
	 * @param  id_hor_pick
	 * @throws CalendarioDAOException
	 */
	public void DelHorarioPicking(long id_hor_pick)
		throws CalendarioDAOException;
	
	//	************************************************************/
	/**
	 * Obtiene un horario de despacho definido para una semana en un calendario de despacho para
	 * una zona de despacho de un local
	 * 
	 * @param  id_hor_desp
	 * @return HorarioDespachoEntity
	 * @throws CalendarioDAOException
	 */
	public HorarioDespachoEntity getHorarioDespacho(long id_hor_desp)
		throws CalendarioDAOException;
	
	/**
	 * Obtiene listado con horarios de despacho para una semana y una zona
	 * 
	 * @param  id_semana
	 * @param  id_zona
	 * @return List HorarioDespachoEntity
	 * @throws CalendarioDAOException
	 */
	public List getHorariosDespacho(long id_semana, long id_zona)
		throws CalendarioDAOException;	
		
	/**
	 * Inserta un registro en la tabla bo_horario_desp
	 * 
	 * @param  id_zona
	 * @param  id_semana
	 * @param  hini
	 * @param  hfin
	 * @return long
	 * @throws CalendarioDAOException
	 */
	public long InsHorarioDespacho(long id_zona, long id_semana, Time hini, Time hfin)
		throws CalendarioDAOException;

	/**
	 * Borra registro en tabla bo_horario_desp
	 * 
	 * @param  id_hor_desp
	 * @throws CalendarioDAOException
	 */
	public void DelHorarioDespacho(long id_hor_desp)
		throws CalendarioDAOException;	
	
	//	************************************************************/

	/**
	 * Obtiene el listado de semanas, segun semana de inicio y semana de fin
	 * 
	 * @param  id_semana_ini
	 * @param  id_semana_fin
	 * @return List SemanasEntity
	 * @throws CalendarioDAOException
	 */
	public List getLstSemanas(long id_semana_ini, long id_semana_fin) throws CalendarioDAOException;
	
	/**
	 * Obtiene el horario de picking relacionado a la jornada de picking
	 * 
	 * @param  id_jpicking
	 * @return HorarioPickingEntity
	 * @throws CalendarioDAOException
	 */
	public HorarioPickingEntity getHorarioPickingByIdJorPick(long id_jpicking) throws CalendarioDAOException;
}
