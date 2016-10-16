package cl.bbr.jumbocl.pedidos.dao;

import java.util.Date;
import java.util.List;

import cl.bbr.jumbocl.common.model.HorarioPickingEntity;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.common.model.JornadaPickingEntity;
import cl.bbr.jumbocl.pedidos.dto.JornadaCriteria;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.JornadasDAOException;

/**
 * Permite las operaciones en base de datos sobre los Jornadas.
 * 
 * @author BBR
 *
 */
public interface JornadasDAO {

	/*
	 * Jornadas de Picking
	 ***********************************************************/
	
	/**
	 * Obtiene jornadas de acuerdo a criterios de consulta
	 * 
	 * @param  criterio
	 * @param  id_local
	 * @return List MonitorJornadasDTO
	 * @throws JornadasDAOException
	 */
	public List getJornadasPickingByCriteria(JornadaCriteria criterio, long id_local)
		throws JornadasDAOException;
	
	/**
	 * Obtiene listado de jornadas por fecha
	 * 
	 * @param  fecha
	 * @param  id_local
	 * @return List MonitorJornadasDTO
	 * @throws JornadasDAOException
	 */
	public List getJornadasPickingByFecha(Date fecha, long id_local)
		throws JornadasDAOException;
	
	/**
	 * Obtiene listado con las jornadas de despacho para una semana determinada en un local
	 * para ser utilizado en el calendario de despacho
	 * 
	 * @param  id_semana
	 * @param  id_local
	 * @return List JornadaPickingEntity
	 * @throws JornadasDAOException
	 */
	public List getJornadasPickingByIdSemana(long id_semana, long id_local)
		throws JornadasDAOException;	

	/**
	 * Obtiene listado de jornadas de picking para un horario
	 * 
	 * @param  id_hor_pick
	 * @return List JornadaPickingEntity
	 * @throws JornadasDAOException
	 */
	public List getJornadasByIdHorario(long id_hor_pick)
		throws JornadasDAOException;
	
	/**
	 * Obtiene detalle de una jornada de picking
	 * 
	 * @param  id_jornada
	 * @return JornadaDTO
	 * @throws JornadasDAOException
	 */
	public JornadaDTO getJornadaById(long id_jornada)
		throws JornadasDAOException;
	
	/**
	 * Inserta registro en tabla bo_jornadas_picking
	 * 
	 * @param  id_local
	 * @param  id_hor_pick
	 * @param  id_semana
	 * @param  dow
	 * @param  fecha
	 * @param  capac_picking
	 * @param  horas_val
	 * @param  horas_web
	 * @return long
	 * @throws JornadasDAOException
	 */
	public long doInsJornadaPicking(long id_local, long id_hor_pick, long id_semana, int dow, Date fecha, int capac_picking, int horas_val, int horas_web)
		throws JornadasDAOException;	
	
	/**
	 * Borra jornadas de picking de un horario
	 * 
	 * @param  id_semana
	 * @throws JornadasDAOException
	 */
	public void doDelJornadaPicking(long id_semana)
		throws JornadasDAOException;
	
	/**
	 * Modifica la información de una jornada
	 * 
	 * @param  id_jpicking
	 * @param  capac_picking
	 * @param  hrs_val
	 * @param  hrs_web
	 * @throws JornadasDAOException
	 */
	public void doModJornadaPicking(long id_jpicking, int capac_picking, int hrs_val, int hrs_web) throws JornadasDAOException;

	/**
	 * Cambia estado jornada de picking
	 * 
	 * @param  id_jpicking
	 * @param  id_estado
	 * @throws JornadasDAOException
	 */
	public void doCambiaEstadoJornadaPicking(long id_jpicking, int id_estado)
		throws JornadasDAOException;
	
	/**
	 * Actualiza las catidades sin pickear de los pedidos relacionados a una jornada
	 * 
	 * @param  id_jornada
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public boolean setPedidosByIdJornada(long id_jornada) 
		throws JornadasDAOException;

	/**
	 * Obtiene las secciones SAP segun el id_jornada
	 * 
	 * @param  id_jornada
	 * @return List CategoriaSapEntity
	 * @throws JornadasDAOException
	 */
	public List getSeccionesSAPByIdJornada(long id_jornada) throws JornadasDAOException;
	
	
	/*
	 * Jornadas de Despacho
	 ***********************************************************/
	
	/**
	 * Obtiene las jornadas de despacho de una semana para una zona
	 * 
	 * @param  id_semana
	 * @param  id_zona
	 * @return List JornadaDespachoEntity
	 * @throws JornadasDAOException
	 */
	public List getJornadasDespachoByIdSemana(long id_semana, long id_zona)
		throws JornadasDAOException;
	
	/**
	 * Retorna listado con jornadas de despacho para un horario
	 * 
	 * @param  id_hor_desp
	 * @return List JornadaDespachoEntity
	 * @throws JornadasDAOException
	 */
	public List getJornadasDespachoByIdHorario(long id_hor_desp)
		throws JornadasDAOException;
	
	/**
	 * Inserta registro en tabla bo_jornada_desp
	 * 
	 * @param  id_local
	 * @param  id_hor_desp
	 * @param  id_semana
	 * @param  id_jpicking
	 * @param  dow
	 * @param  fecha
	 * @param  capac_despacho
	 * @param  precio
	 * @return long
	 * @throws JornadasDAOException
	 */
	public long doInsJornadaDespacho(long id_local, long id_hor_desp, long id_semana, long id_jpicking, int dow, Date fecha, int capac_despacho, int tarifa_express, int tarifa_normal, int tarifa_economica, int tarifa_umbral)
		throws JornadasDAOException;
	
	/**
	 * Elimina jornadas de picking de un horario
	 * 
	 * @param  id_hor_pick
	 * @throws JornadasDAOException
	 */
	public void doDelJornadaDespacho(long id_hor_pick)
		throws JornadasDAOException;
	
	/**
	 * Modifica datos de una jornada de despacho
	 * 
	 * @param  id_jdespacho
	 * @param  id_jpicking
	 * @param  capac_despacho
	 * @param  precio
	 * @throws JornadasDAOException
	 */
	public void doModJornadaDespacho(long id_jdespacho, long id_jpicking, int capac_despacho, int tarifa_express, int tarifa_normal, int tarifa_economica, int tarifa_umbral)
		throws JornadasDAOException;
	
	/**
	 * Obtiene jornada de despacho a partir del id
	 * 
	 * @param  id_jdespacho
	 * @return JornadaDespachoEntity
	 * @throws JornadasDAOException
	 */
	public JornadaDespachoEntity getJornadaDespachoById(long id_jdespacho)
		throws JornadasDAOException;
	
	/**
	 * Obtiene la jornada de picking, segun los datos ingresados
	 *  
	 * @param dow
	 * @param id_semana
	 * @param hor_pick
	 * @return long id de jornada de picking
	 * @throws JornadasDAOException
	 */
	public long getJornadasPickingByDatos(int dow, long id_semana, HorarioPickingEntity hor_pick) throws JornadasDAOException;
	
	/**
	 * Obtiene la ultima jornada picking segun la fecha ingresada 
	 * 
	 * @param fecha
	 * @param id_local
	 * @return JornadaPickingEntity
	 * @throws JornadasDAOException
	 */
	public JornadaPickingEntity getJornadaPickingUltimo(String fecha, long id_local) throws JornadasDAOException;
	
	/**
	 * Elimina las jornadas de despacho segun la semana ingresada
	 *  
	 * @param  id_sem_ini
	 * @param  id_sem_fin
	 * @param  id_zona
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public boolean doDelJornadaDespachoBySemanas(long id_sem_ini, long id_sem_fin, long id_zona) throws JornadasDAOException;
	
	/**
	 * Elimina los horarios de despacho segun la semana ingresada
	 * 
	 * @param  id_sem_ini
	 * @param  id_sem_fin
	 * @param  id_zona
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public boolean doDelHorarioDespachoBySemanas(long id_sem_ini, long id_sem_fin, long id_zona) throws JornadasDAOException;
	
	/**
	 * Elimina las jornadas de picking segun la semana ingresada
	 * 
	 * @param  id_semana
	 * @param  id_local
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public boolean doDelJornadaPickingBySemana(long id_semana, long id_local) throws JornadasDAOException;
	
	/**
	 * Elimina los horarios de picking segun la semana ingresada
	 * 
	 * @param  id_semana
	 * @param  id_local
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public boolean doDelHorarioPickingBySemana(long id_semana, long id_local) throws JornadasDAOException;
	
	/**
	 * Obtiene el id de jornada de despacho, a partir de la jornada de picking y la semana inicial de busqueda
	 *  
	 * @param  id_jor_pick
	 * @param  id_semana
	 * @param  id_zona
	 * @return long, id de jornada
	 * @throws JornadasDAOException
	 */
	public long getIdJornadaDespByJornadaPick(long id_jor_pick, long id_semana, long id_zona) throws JornadasDAOException;
	
	/**
	 * Actualiza la jornada de picking en la jornada de despacho
	 *  
	 * @param  id_jor_desp
	 * @param  id_jor_pick
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public boolean doActJDespachoByJPick(long id_jor_desp, long id_jor_pick) throws JornadasDAOException;
	
	/**
	 * Obtiene el listado de jornadas de despacho que no se encuentra en el rango de semanas pero que tienen jpicking 
	 * que pertenecen en el rango de semanas
	 *  
	 * @param  id_sem_ini
	 * @param  id_sem_fin
	 * @param  id_zona
	 * @return List JornadaDespachoEntity
	 * @throws JornadasDAOException
	 */
	public List getJDespachoRelBySemanas(long id_sem_ini, long id_sem_fin, long id_zona) throws JornadasDAOException;
	
	/**
	 * Verifica si existe jornadas y horarios de despacho y picking entre semana inicial y final
	 * 
	 * @param  id_sem_ini
	 * @param  id_sem_fin
	 * @param  id_zona
	 * @param  id_local
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public boolean existeJornadasHorariosBySemanas(long id_sem_ini, long id_sem_fin, long id_zona, long id_local) throws JornadasDAOException;
	
	/**
	 * Obtiene los pedidos en esa semana y esa zona.
	 * 
	 * @param id_semana
	 * @param id_zona
	 * @return List SemanaJornadaPedidoDTO.
	 * @throws JornadasDAOException
	 */
	public List getSemJorDespPedidoBySem(long id_semana, long id_zona) throws JornadasDAOException;

	/**
	 * Revisa si existen pedidos en las jornadas de picking, segun rango de semanas y local
	 * 
	 * @param id_semana
	 * @param id_local
	 * @return boolean
	 * @throws JornadasDAOException
	 */
	public boolean existePedidoByJPicking(long id_semana, long id_local) throws JornadasDAOException;
	
}
