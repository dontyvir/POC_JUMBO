package cl.bbr.jumbocl.pedidos.dao;

import java.sql.SQLException;
import java.util.List;

import cl.bbr.jumbocl.pedidos.collaboration.ProcFPickDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPedDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPickDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickRelacionDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcRondasPropuestasDTO;
import cl.bbr.jumbocl.pedidos.dto.BinFormPickDTO;
import cl.bbr.jumbocl.pedidos.dto.CreaRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.DetalleFormPickingDTO;
import cl.bbr.jumbocl.pedidos.dto.FPickDTO;
import cl.bbr.jumbocl.pedidos.dto.FormPickOpDTO;
import cl.bbr.jumbocl.pedidos.dto.FormPickRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductoCbarraDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RondaDTO;
import cl.bbr.jumbocl.pedidos.dto.RondasCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.TPAuditSustitucionDTO;
import cl.bbr.jumbocl.pedidos.exceptions.RondasDAOException;

/**
 * Permite las operaciones en base de datos sobre las Rondas.
 * 
 * @author BBR
 *
 */
public interface RondasDAO {

	/** 
	 * Agrega registro al log del pedido
	 * 
	 * @param  id_ronda
	 * @param  login
	 * @param  log
	 * @throws RondasDAOException
	 */
	public void doAddLogRonda(long id_ronda, String login, String log)
		throws RondasDAOException;
	
	/**
	 * Obtiene listado del log de una ronda
	 *  
	 * @param  id_ronda
	 * @return List LogSimpleDTO
	 * @throws RondasDAOException
	 */
	public List getLogRonda(long id_ronda)
		throws RondasDAOException;
	
	/**
	 * Obtiene listado de estados de una ronda
	 * 
	 * @return List EstadoDTO
	 * @throws RondasDAOException
	 */
	public List getEstadosRonda()
		throws RondasDAOException;
	
	/**
	 * Retorna listado de rondas de acuerdo a un criterio
	 * 
	 * @param  criterio
	 * @return List MonitorRondasDTO
	 * @throws RondasDAOException
	 */
	/*public List getRondasByCriteria(RondasCriteriaDTO criterio)
		throws RondasDAOException;*/

	/**
	 * Obtiene número de registros de una búsuqe da de rondas por criterio
	 * 
	 * @param  criterio
	 * @return long
	 * @throws RondasDAOException
	 */
	public long getCountRondasByCriteria(RondasCriteriaDTO criterio)
		throws RondasDAOException;
	
	/**
	 * Obtiene lista de rondas para un pedido
	 * 
	 * @param  id_pedido
	 * @return List MonitorRondasDTO
	 * @throws RondasDAOException
	 */
	public List getRondasByIdPedido(long id_pedido)
		throws RondasDAOException;
	
	/**
	 * Obtiene detalle de una ronda.
	 * 
	 * @param  id_ronda
	 * @return RondaDTO
	 * @throws RondasDAOException
	 */
	public RondaDTO getRondaById(long id_ronda)
		throws RondasDAOException;
	
	/**
	 * Obtiene Listado productos que no han sido pickeados
	 * agrupados por OP y por sector, para aquellas jornadas
	 * que han sido iniciadas y ops que están en estado 
	 * Validadas y En Picking.
	 * 
	 * @param  ProcRondasPropuestasDTO criterio con id_sector, id_jornada, id_localm y tipo_ve
	 * @return List RondaPropuestaDTO
	 * @throws RondasDAOException
	 */
	public List getRondasPropuestasDet(ProcRondasPropuestasDTO criterio)
		throws RondasDAOException;
	
	/**
	 * Cambia estado a una ronda
	 * 
	 * @param  id_estado
	 * @param  id_ronda
	 * @throws RondasDAOException
	 */
	public void doCambiaEstadoRonda(long id_estado, long id_ronda)
		throws RondasDAOException;

	/**
	 * Inserta un nuevo registro de ronda
	 * 
	 * @param  ronda
	 * @return long 
	 * @throws RondasDAOException
	 */
	public long setCreaRonda(CreaRondaDTO ronda) 
		throws RondasDAOException;
	
	/**
	 * Agrega a la tabla BO_DETALLE_RONDAS
	 * 
	 * @param  id_sector
	 * @param  id_ronda
	 * @param  id_pedido
	 * @param  cant
	 * @return long
	 * @throws RondasDAOException
	 */
	public long setDetalleRonda(long id_sector,long id_ronda, long id_pedido, double cant) 
		throws RondasDAOException;
	
	/**
	 * Retorna listado resumen segun el id de ronda.
	 * 
	 * @param  id_ronda
	 * @return List RondaDetalleResumenDTO
	 * @throws RondasDAOException
	 */
	public List getResumenRondaById(long id_ronda) 
		throws RondasDAOException;
	
	/**
	 * Obtiene listado de productos que deben ser pickeados en una ronda
	 * 
	 * @param  id_ronda
	 * @return List ProductosPedidoDTO
	 * @throws RondasDAOException
	 */
	public List getProductosRonda(long id_ronda)
		throws RondasDAOException;
	
	/**
	 * Retorna listado de códigos de barra asociados a los productos de un pedido
	 * 
	 * @param  id_ronda
	 * @return List BarraDetallePedidosRondaDTO
	 * @throws RondasDAOException
	 */
	public List getBarrasRondaDetallePedido(long id_ronda)
		throws RondasDAOException;
	
	/**
	 * Actualiza tabla BO_RONDAS con el pickeador asignado y actualiza fecha inicio picking
	 * 
	 * @param id_ronda
	 * @param id_usuario
	 * @throws RondasDAOException
	 */
	public void doAsignaPickeadorRonda(long id_ronda, long id_usuario)
		throws RondasDAOException;
	
	/**
	 * Actualiza el estado de la ronda a Terminado y la fecha y hora de término
	 * 
	 * @param  id_ronda
	 * @throws RondasDAOException
	 */
	public void doFinalizaRonda(long id_ronda)
		throws RondasDAOException;
		
	/**
	 * Obtiene el listado de productos sustitutos de la ronda
	 * 
	 * @param  id_ronda
	 * @return List SustitutoDTO
	 * @throws RondasDAOException
	 */
	public List getSustitutosByRondaId(long id_ronda)
		throws RondasDAOException;

	/**
	 * Cambia el estado de la ronda a creada.
	 * 
	 * @param id_ronda
	 * @return boolean
	 * @throws RondasDAOException
	 */
	public boolean setReseteaRonda(long id_ronda) throws RondasDAOException;
	
	
	/**
	 * Obtiene todos los bins del formulario de picking asociados a una ronda
	 * @param id_ronda
	 * @return List
	 * @throws RondasDAOException
	 */
	public List getBinsFormPickByRondaId(long id_ronda) throws RondasDAOException;
	
	/**
	 * Permite agregar un bin a una ronda a través del Formulario de Picking
	 * @param bin
	 * @return long
	 * @throws RondasDAOException
	 */
	public long doAgregaBinRonda(BinFormPickDTO bin) throws RondasDAOException;
	
	
	/**
	 * Permite agregar un registro de detalle de picking al Formulario de Picking
	 * @param DetalleFormPickingDTO pick
	 * @return boolean
	 * @throws RondasDAOException
	 */
	public boolean doAgregaDetalleFormPicking(DetalleFormPickingDTO pick) throws RondasDAOException;
	
	/**
	 * Obtiene el listado con los productos pickeados pertenecientes a una ronda (Formulario de picking)
	 * @param ProcFormPickDetPickDTO datos
	 * @return List
	 * @throws RondasDAOException
	 */
	public List getDetallePickFormPick(ProcFormPickDetPickDTO datos) throws RondasDAOException;
	
	/**
	 * Obtiene los productos con sus codigos de barra, pertenecientes a una ronda especificada (Formulario de Picking)
	 * @param id_ronda
	 * @return List
	 * @throws RondasDAOException
	 */
	public List getProductosCbarraRonda(ProcFormPickDetPedDTO datos) throws RondasDAOException;
	
	/**
	 * Obtiene los datos de un producto segun un codigo de barras
	 * @param cod_barra
	 * @return ProductoCbarraDTO
	 * @throws RondasDAOException
	 */
	public ProductoCbarraDTO getProductoByCbarra(String cod_barra) throws RondasDAOException;
	
	/**
	 * Elimina un registro del detalle de picking (Formulario de picking)
	 * @param id_row
	 * @return boolean
	 * @throws RondasDAOException
	 */
	public boolean doDelDetalleFormPicking(long id_row) throws RondasDAOException;

	/**
	 * Agrega un registro a la tabla rondas del formulario de picking
	 * @param  datos FormPickRondaDTO
	 * @throws RondasDAOException
	 */
	public void doAgregaFormPickRonda(FormPickRondaDTO datos) throws RondasDAOException; 
			
	/**
	 * Agrega un pedido a la tabla de OP's del formulario de picking
	 * @param  datos FormPickOpDTO
	 * @throws RondasDAOException
	 */
	public void doAgregaFormPickOp(FormPickOpDTO datos) throws RondasDAOException;
	
	/**
	 * Agrega un detalle de pedido al formulario de picking
	 * @param  datos ProductosPedidoDTO
	 * @throws RondasDAOException
	 */
	public void doAgregaFormPickDetPed(ProductosPedidoDTO datos) throws RondasDAOException;
	
	/**
	 * Actualiza la tabla rondas del formulario de picking
	 * @param  datos FormPickRondaDTO
	 * @return boolean
	 * @throws RondasDAOException
	 */
	public boolean setActFormPickRonda(FormPickRondaDTO datos) throws RondasDAOException;
	
	/**
	 * Actualiza la tabla OP del formulario de picking
	 * @param  datos FormPickOpDTO
	 * @return boolean
	 * @throws RondasDAOException
	 */
	public boolean setActFormPickOp(FormPickOpDTO datos) throws RondasDAOException;
	
	/**
	 * Actualiza la tabla Detalle Pedidos del formulario de picking
	 * @param  datos ProductosPedidoDTO
	 * @return boolean
	 * @throws RondasDAOException
	 */
	public boolean setActFormPickDetPed(ProductosPedidoDTO datos) throws RondasDAOException;
	
	/**
	 * Actualiza la tabla Detalle Picking del formulario de picking
	 * @param  datos DetalleFormPickingDTO
	 * @return boolean
	 * @throws RondasDAOException
	 */
	public boolean setActFormPickDetPick(DetalleFormPickingDTO datos) throws RondasDAOException;
	
	/**
	 * Selecciona la ronda del formulario para conocer su estado
	 * @param id_ronda long 
	 * @return FormPickRondaDTO ronda
	 * @throws RondasDAOException
	 */
	public FormPickRondaDTO getFormPickRonda(long id_ronda)throws RondasDAOException;
	
	/**
	 * Selecciona el pedido del formulario para conocer su estado
	 * @param  datos FormPickOpDTO
	 * @return FormPickOpDTO pedido
	 * @throws RondasDAOException
	 */
	public FormPickOpDTO getFormPickOp(long id_ronda)throws RondasDAOException;
	
	/**
	 * entrega el listado con el detalle de una ronda
	 * @param id_ronda
	 * @return List
	 * @throws RondasDAOException
	 */
	public List getDetalleRondas(long id_ronda) throws RondasDAOException;
	
	/**
	 * Inserta un producto pickeado en el formulario
	 * @param  pick FPickDTO
	 * @return id_fpick generado
	 * @throws RondasDAOException
	 */
	public long doAgregaPick(FPickDTO pick) throws RondasDAOException;
	
	/**
	 * Lista el detalle de la tabla FPick con productos pickeados 
	 * @param pick ProcFPickDTO 
	 * @return List 
	 * @throws RondasDAOException
	 */
	public List getDetallePick(ProcFPickDTO pick) throws RondasDAOException;
	
	/**
	 * Actualiza la cantidad disponible en los pickeados del formulario
	 * @param  datos FPickDTO
	 * @return boolean
	 * @throws RondasDAOException
	 */
	public boolean setActFPick(FPickDTO datos) throws RondasDAOException;
	
	
	/**
	 * Elimina un registro de los pickeados del formulario
	 * @param id_pick
	 * @return boolean
	 * @throws RondasDAOException
	 */
	public boolean doDelFPicking(long id_pick) throws RondasDAOException;
	
	/**
	 * Obtiene un listado con los productos faltantes pertenecientes a una ronda.
	 * @param id_ronda
	 * @return List
	 * @throws RondasDAOException
	 */
	public List getFormPickFaltantes(long id_ronda) throws RondasDAOException;
	
	/**
	 * Obtiene un listado con los productos sustitutos asociados a una ronda.
	 * @param id_ronda
	 * @param id_local
	 * @return List
	 * @throws RondasDAOException
	 */
	public List getFormPickSustitutos(long id_ronda, long id_local)throws RondasDAOException;
	
	
	/**
	 * Obtiene el detalle de un producto relacionado
	 * @param id_row
	 * @return DetalleFormPickingDTO
	 * @throws RondasDAOException
	 */
	public DetalleFormPickingDTO getRelacionFormPickById(long id_row) throws RondasDAOException;
	
	
	/**
	 * Obtiene la cantidad de relaciones que tiene un picking segun el id_fpick
	 * @param id_fpick
	 * @return long
	 * @throws RondasDAOException
	 */
	public long getCountFormPickRelacion(long id_fpick) throws RondasDAOException;
	
	/**
	 * Obtiene el detalle del picking (relaciones) de acuerdo a su id
	 * @param datos
	 * @return List
	 * @throws RondasDAOException
	 */
	public List getFormPickRelacion(ProcFormPickRelacionDTO datos) throws RondasDAOException;
	
	/**
	 * Obtiene un listado con los productos pickeados que aun no han sido 
	 * relacionados a un detalle de pedido
	 * @param id_ronda
	 * @return List
	 * @throws RondasDAOException
	 */
	public List getPickSinRelacionFormPick(long id_ronda)throws RondasDAOException;
	
	
	/**
	 * Obtiene un listado con los productos pedidos que aun no han sido 
	 * relacionados a un detalle de picking.
	 * @param id_ronda
	 * @return List
	 * @throws RondasDAOException
	 */
	public List getPedSinRelacionFormPick(long id_ronda)throws RondasDAOException;
	
	
	/**
	 * Permite comprobar si una ronda existe.
	 * @param id_ronda
	 * @return boolean
	 * @throws RondasDAOException
	 */
	public boolean existeRonda(long id_ronda) throws RondasDAOException; 
	
	
	/**
	 * Permite comprobar si un pedido existe para una ronda determinada 
	 * @param id_ronda
	 * @param id_pedido
	 * @param boolean
	 * @throws RondasDAOException
	 */
	public boolean existePedidoEnRonda(long id_ronda, long id_pedido) throws RondasDAOException;
	
	/**
	 * Permite resetear una ronda para el formulario de picking
	 * @param id_ronda
	 * @return boolean
	 * @throws RondasDAOException
	 */
	public boolean setFormPickReseteaRonda(long id_ronda) throws RondasDAOException;
	
	/**
	 * Obtiene un listado con los pedidos asociados a una ronda
	 * @param id_ronda long
	 * @return List
	 * @throws RondasDAOException
	 */
	public List getPedidosByRonda(long id_ronda) 	throws RondasDAOException;
	
	/**
	 * Obtiene el primer codigo de barras para un producto.
	 * @param id_prod
	 * @return
	 * @throws RondasDAOException
	 */
	public String getCbarraByIdProd(long id_prod) throws RondasDAOException;
	
	/**
	 * Recupera las zonas finalizadas por jornada para la ronda.
	 * 
	 * @param id_ronda	Identificador único de la ronda
	 * @return			Listado de zonas
	 * @throws RondasDAOException
	 */
	public List getZonasFinalizadas( long id_ronda ) throws RondasDAOException;
	
	/* *****************************************
	 * Funciones de Prorrateo
	 *************************************************/
	

	
	
	/**
	 * Obtiene los detalles de picking a partir de un id_detalle
	 * @param id_detalle
	 * @return
	 * @throws RondasDAOException
	 */
	public List getDetPickingByIdDetalle(long id_detalle) throws RondasDAOException;
	
	/**
	 * Retorna la cantidad de detalles de picking para un id_detalle
	 * @param id_detalle
	 * @return
	 * @throws RondasDAOException
	 */
	public long getCountDetPickByIdDet(long id_detalle)  throws RondasDAOException;
	
	/**
	 * Actualiza el precio del detalle de picking
	 * @param id_dpick
	 * @param precio
	 * @return
	 * @throws RondasDAOException
	 */
	public boolean doActualizaPrecioDetPick(long id_dpick, double precio)  throws RondasDAOException;
		
	/**
	 * Obtiene los detalles de pedidos segun su id
	 * @param id_pedido
	 * @return
	 * @throws RondasDAOException
	 */
	public List getDetallesPedido(long id_pedido) throws RondasDAOException;
	
	/**
	 * Obtiene las promociones involucradas en una ronda
	 * @param id_ronda
	 * @return
	 * @throws RondasDAOException
	 */
	public List getPromocionesRonda(long id_ronda) throws RondasDAOException;
	
	
	/**
	 * Obtiene promociones para un id_detalle
	 * @param id_detalle
	 * @return
	 * @throws RondasDAOException
	 */
	public List getPromocionesByIdDetalle(long id_detalle) throws RondasDAOException;
	
	/**
	 * Obtiene los criterios de politicas de sustitucion
	 * @return List SustitutosCriterios
	 * @throws RondasDAOException
	 */
	public List getSustitutosCriterio() throws RondasDAOException;
	
	//	---------- mod_ene09 - ini------------------------
	/**
	 * Actualiza las estadisticas y el fiscalizador en la ronda
	 * @param ronda
	 * @throws RondasDAOException
	 */
	public void updRondaFinal(RondaDTO ronda) throws RondasDAOException;
	//	---------- mod_ene09 - fin------------------------
	
    public void insetaAuditoriaSustitutos(TPAuditSustitucionDTO AudSust) throws SQLException;
    
}
