package cl.bbr.vte.cotizaciones.service;

import java.util.List;


import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dto.AsignaCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.CategoriaDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesCriteriaDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.LogsCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ModCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.ModProdCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProcInsCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCriteriaDTO;
import cl.bbr.vte.cotizaciones.ctrl.CotizacionesCtrl;
import cl.bbr.vte.cotizaciones.exception.CotizacionesException;

/**
 * Capa de servicios para el área de Cotizaciones
 * 
 * @author BBR e-commerce & retail
 *
 */
public class CotizacionesService {

	/**
	 * Instancia para log
	 */
	Logging logger = new Logging( this );

	/**
	 * Constructor
	 *
	 */
	public CotizacionesService() {
		this.logger.debug("New CotizacionesService");
	}

	
	/**
	 * Recupera el listado de categorías
	 * 
	 * @param cliente_id	Identificador único del cliente
	 * @return				Lista con DTO de categorías
	 * @throws ServiceException 
	 * @throws SystemException
	 */	
	public List getListCategoria(long cliente_id) throws ServiceException, SystemException{
		CotizacionesCtrl dirctr = new CotizacionesCtrl();
		try {
			return dirctr.getListCategoria(cliente_id);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * Recupera el listado de cotizaciones de acuerdo a un criterio especificado
	 * 
	 * @param criterio
	 * @return result
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getCotizacionesByCriteria(CotizacionesCriteriaDTO criterio) throws ServiceException, SystemException{
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try{
			return  cot.getCotizacionesByCriteria(criterio);
		}catch (CotizacionesException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * Obtiene un listado de Locales
	 * 
	 * @return List 
	 * @throws ServiceException
	 */	
	public List getLocales() throws ServiceException {
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			return cot.getLocales();
		} catch (CotizacionesException ex) {
			logger.debug( "Problemas con getLocales");
			throw new ServiceException(ex);
		}
	}
	
	
	
	/**
	 * Obtiene una lista con los estados de Cotizaciones
	 * 
	 * @throws ServiceException
	 * @return List 
	 * 
	 */
	public List getEstadosCotizaciones() throws ServiceException {
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			return cot.getEstadosCotizaciones();
		} catch (CotizacionesException ex) {
			logger.debug( "Problemas con getEstadosCotizaciones");
			throw new ServiceException(ex);
		}
	}
	
	/**
	 * Retorna la cantidad de cotizaciones, segun el criterio de búsqueda
	 * 
	 * @param  criterio  
	 * @return long
	 * @throws ServiceException 
	 * @throws SystemException 
	 */
	public long getCountCotizacionesByCriteria(CotizacionesCriteriaDTO criterio)
		throws ServiceException, SystemException{
		
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			return cot.getCountCotizacionesByCriteria(criterio);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Obtiene el detalle de una cotización a partir de su ID.
	 * @param cot_id
	 * @return cotizacionesDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public CotizacionesDTO getCotizacionesById(long cot_id) throws ServiceException, SystemException{
		CotizacionesCtrl cot = new CotizacionesCtrl();
		
		try{
			return cot.getCotizacionById(cot_id);
		}catch (CotizacionesException e){
			e.printStackTrace();
			throw new ServiceException(e);
		}
		
	}
	

	public String getEstadoSaldoEmpresaByCotizacion(long cot_id) throws ServiceException, SystemException{
		CotizacionesCtrl cot = new CotizacionesCtrl();
		
		try{
			return cot.getEstadoSaldoEmpresaByCotizacion(cot_id);
		}catch (CotizacionesException e){
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	
	public long getDireccionByCotizacion(long cot_id) throws ServiceException, SystemException{
		CotizacionesCtrl cot = new CotizacionesCtrl();
		
		try{
			return cot.getDireccionByCotizacion(cot_id);
		}catch (CotizacionesException e){
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}


	
	/**
	 * Obtiene la cantidad de productos asociados a una cotización a partir de su ID.
	 * @param cot_id
	 * @return long 
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public long getCountProductosEnCotizacionById(long cot_id)
	throws ServiceException, SystemException{
		
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			return cot.getCountProductosEnCotizacionById(cot_id);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	/**
	 * Retorna una lista con las alertas asociadas a una cotización
	 * @param id_cot
	 * @return List AlertasDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getAlertasCotizacion(long id_cot)
	throws ServiceException, SystemException{

		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			return cot.getAlertasCotizacion(id_cot);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Retorna una lista con los productos asociados a una cotización
	 * @param id_cot
	 * @return List ProductosCotizacionesDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getProductosCotiz(long id_cot)
	throws ServiceException, SystemException{

		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			return cot.getProductosCotiz(id_cot);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Retorna una lista con los pedidos asociados a una cotización
	 * @param id_cot
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getPedidosCotiz(long id_cot)
	throws ServiceException, SystemException{

		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			return cot.getPedidosCotiz(id_cot);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Retorna una lista con los logs asociados a una cotización.
	 * @param id_cot
	 * @return List LogsCotizacionesDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getLogsCotiz(long id_cot) throws ServiceException, SystemException{
		
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try{
			return cot.getLogsCotiz(id_cot);
		}catch (CotizacionesException e){
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Inserta un nuevo encabezado de cotización sin detalle de productos
	 * 
	 * @param dto Datos de la cotización
	 * @return Identificador único de la cotización insertada
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public long doInsCotizacionHeader( ProcInsCotizacionDTO dto ) throws ServiceException, SystemException{

		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			return cot.doInsCotizacionHeader(dto);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Inserta una lista de detalles (productos) a una cotización. 
	 * @param lst_det Lista con DTO de los detalles
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void doInsCotizacionDetail( List lst_det ) throws ServiceException, SystemException{

		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			cot.doInsCotizacionDetail(lst_det);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}  
	
	/**
	 * Permite insertar una nueva cotizacion
	 * 
	 * @param dto
	 * @return id de la nueva cotizacion
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public long doInsCotizacion(ProcInsCotizacionDTO dto) throws ServiceException, SystemException{

		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			return cot.doInsCotizacion(dto);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Obtiene el listado de productos de la cotizacion, incluyendo pedidos relacionados el listado de los pedidos
	 *   
	 * @param id_cotizacion
	 * @param id_producto
	 * @return List ProductosCotizacionesDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getLstProductosByCotizacion(long id_cotizacion) throws ServiceException, SystemException{

		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			return cot.getLstProductosByCotizacion(id_cotizacion);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Permite agregar un registro de log a la cotizacion
	 * @param log
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean addLogCotizacion(LogsCotizacionesDTO log)
	throws ServiceException, SystemException {
	
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			 return cot.addLogCotizacion(log);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * Permite actualizar una cotización
	 * @param id_cot
	 * @param obs
	 * @param fueraMix
	 * @param costo_despacho
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setModCotizacion(ModCotizacionDTO cotizacion)
	throws ServiceException, SystemException {
	
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			 return cot.setModCotizacion(cotizacion);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	

	/**
	 * Permite anular una cotizacion
	 * @param id_cot
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setAnularCotizacion(long id_cot)
	throws ServiceException, SystemException {
	
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			 return cot.setAnularCotizacion(id_cot);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Permite agregar un producto a la cotización
	 * @param prod
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */	
	public boolean addProductoCotizacion(ProductosCotizacionesDTO prod)
	throws ServiceException, SystemException {
	
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			 return cot.addProductoCotizacion(prod);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * Permite eliminar un producto de una cotización
	 * @param detcot_id
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean delProductoCotizacion(long detcot_id)
	throws ServiceException, SystemException{
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try{
			return cot.delProductoCotizacion(detcot_id);
		}catch (CotizacionesException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Permite caducar  cotizaciones con fecha mayor a la fecha de expiracion
	 * @param id_comprador Identificador único del comprador
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean CaducarCotizaciones( long id_comprador ) throws ServiceException, SystemException{
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try{
			return cot.CaducarCotizaciones( id_comprador );
		}catch (SystemException e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Permite modificar datos (cantidad, descuento, precio) de un producto asociado a una cotización
	 * @param prod
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean updProductoCotizacion(ModProdCotizacionesDTO prod)
	throws ServiceException, SystemException {
	
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			 return cot.updProductoCotizacion(prod);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Permite validar la cotizacion, antes que sea cotizada 
	 * 
	 * @param id_cotizacion
	 * @return true, si el proceso tuvo exito, false si existe algun error
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setValidarCotizacion(long id_cotizacion) throws ServiceException, SystemException {
	
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			 return cot.setValidarCotizacion(id_cotizacion);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	} 
	
	/**
	 * Permite confirmar la cotizacion, cambiando el estado segun resultado de la validacion
	 * 
	 * @param id_cotizacion
	 * @return true, si el proceso tuvo exito, false si existe algun error
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setConfirmarCotizacion(long id_cotizacion, UserDTO usr) throws ServiceException, SystemException {
		
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			 return cot.setConfirmarCotizacion(id_cotizacion, usr);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Recupera listado de cateogías.
	 * 
	 * @return Listado de categorías.
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getCategoriasList() throws ServiceException, SystemException {
		
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			 return cot.getCategoriasList();
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}	

	/**
	 * Recupera información de la categoría.
	 * 
	 * @param id_categoria Identificador único de la categoría
	 * @return DTO con datos de la categoría
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public CategoriaDTO getCategoriaById(long id_categoria) throws ServiceException, SystemException {
		
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			 return cot.getCategoriaById( id_categoria );
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}	
	
	
	
	/**
	 * Retorna si la cotización fue asignada o no. 
	 * @param col
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setAsignaCotizacion(AsignaCotizacionDTO col) throws ServiceException, SystemException {
		
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			return cot.setAsignaCotizacion(col);
		} catch (CotizacionesException e) {
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	
	/**
	 * Retorna si la cotización fue liberada o no.
	 * 
	 * @param  col AsignaCotizacionDTO 
	 * @return boolean, devuelve <i>true</i> en el caso que la liberación fue satisfactoria, caso contrario devuelve <i>false</i>.
	 * @throws ServiceException 
	 * @throws SystemException
	 * 
	 */
	public boolean setLiberaCotizacion(AsignaCotizacionDTO col)
		throws ServiceException, SystemException {
		
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			return cot.setLiberaCotizacion(col);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * retorna la suma de las cantidades de los productos asociados a una cotización.
	 * @param cot_id
	 * @return double
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public double getTotalProductosCot( long cot_id) throws ServiceException, SystemException {
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			return cot.getTotalProductosCot(cot_id);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	
	/**
	 * Recupera todas las marcas diferentes de los productos para una categoría.  
	 * 
	 * @param id_categoria Identificador único de la categoría
	 * @return Listado de marcas
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getListMarcasByCategoria(long categoria_id)
			throws ServiceException, SystemException {

		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			return cot.getListMarcasByCategoria(categoria_id);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}	
	
	/**
	 * Recupera los productos para una categoría.
	 * 
	 * Debe considerar los criterios de búsquedas.
	 * 
	 * @param criterio DTO con información de los diferetentes datos para filtrar y ordenar el listado de productos
	 * @return Listado de productos
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getListProductosByCriteria(ProductosCriteriaDTO criterio) throws ServiceException, SystemException {

		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			return cot.getListProductosByCriteria(criterio);
		} catch (CotizacionesException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Permite modificar datos (cantidad) de un producto asociado a una cotización
	 * @param prod
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean updCantProductoCotizacion(ModProdCotizacionesDTO prod)
	throws ServiceException, SystemException {
	
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			 return cot.updCantProductoCotizacion(prod);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Retorna una lista con los productos asociados a una cotización
	 * @param id_cot
	 * @return List ProductosCotizacionesDTO
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List getProductosDetCotiz(long id_cot)
	throws ServiceException, SystemException{

		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			return cot.getProductosDetCotiz(id_cot);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Permite agregar al encabezado (estado, observacion) y al detalle de la cotizacion (descripcion, precio unitario, cantidad)
	 * @param id_cot, comentario
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean SendCotizacion(CotizacionesDTO cotizacion)
	throws ServiceException, SystemException {
		
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			 return cot.SendCotizacion(cotizacion);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * Recupera categorías y marcas para la búsqueda
	 *  
	 * @param patron		Patrón de búsqueda
	 * @param local_id		Identificador único del local
	 * @param criterio		criterio de busqueda
	 * @return				Lista de DTO con datos de las categorías
	 * @throws SystemException
	 */
	public List getSearch( List patron, long local_id, long criterio ) throws ServiceException, SystemException {
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			 return cot.getSearch(patron, local_id, criterio);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * Cambia a estado ACEPTADA la cotizacion y graba la informacion del resumen
	 * @param dto
	 * @return True: éxito False: error
	 * @throws ServiceException
	 * @throws SystemException
	 */	
	public boolean setAceptarCotizacion(ProcInsCotizacionDTO dto) throws ServiceException, SystemException {
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			 return cot.setAceptarCotizacion(dto);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	/** Recupera los itemes de los productos
	 * @param local_id: Identificador de local
	 * @param pro_padre : Identificador del producto padre
	 * @param coti_id : Identificador de la cotizacion
	 * @return
	 * @throws CotizacionesException, SystemException
	 */	
	public List getListItems(String local_id, long pro_padre, long coti_id) throws ServiceException, SystemException {
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			 return cot.getListItems(local_id, pro_padre, coti_id);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}	
	
	/**
	 * Permite modificar el estado de la cotizacion
	 * @param id_cot
	 * @param id_estado
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public boolean setModEstadoCotizacion(long id_cot, long id_estado)
	throws ServiceException, SystemException {
	
		CotizacionesCtrl cot = new CotizacionesCtrl();
		try {
			 return cot.setModEstadoCotizacion(id_cot,id_estado);
		} catch (CotizacionesException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
}
