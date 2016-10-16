package cl.bbr.vte.cotizaciones.dao;


import java.util.List;

import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.common.model.EmpresasEntity;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.common.model.ProductoSapEntity;
import cl.bbr.vte.cotizaciones.dto.AsignaCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.CategoriaDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesCriteriaDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.LogsCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ModCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.ModProdCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProcInsCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.ProcInsDetCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCriteriaDTO;
import cl.bbr.vte.cotizaciones.exception.CotizacionesDAOException;
import cl.bbr.vte.cotizaciones.exception.CotizacionesException;

/**
 * Interfaz para implementación de métodos en DAO para diferentes tipos de conexión a repositorios. 
 * 
 * @author BBR e-commerce & retail
 *
 */

public interface CotizacionesDAO {

	/**
	 * Retorna las categorias activas
	 * @param cliente_id
	 * @return
	 * @throws CotizacionesDAOException
	 */

	public List getListCategoria( long cliente_id ) throws CotizacionesDAOException;
	
	/**
	 * Retorna un listado de cotizaciones de acuerdo a un criterio formado por los 
	 * filtros de búsqueda.
	 * @param criterio
	 * @return List CotizacionesCriteriaDTO
	 * @throws CotizacionesDAOException
	 */
	
	public List getCotizacionesByCriteria(CotizacionesCriteriaDTO criterio ) throws CotizacionesDAOException;
	

	/**
	 * Obtiene un listado de locales
	 * 
	 * @return List de LocalEntity's
	 * @throws CotizacionesDAOException
	 */
	public List getLocales() throws CotizacionesDAOException;
	
	
	/**
	 * Retorna un listado de Estados para Cotizaciones
	 * @return List EstadoDTO
	 * @throws CotizacionesDAOException
	 */
	public List getEstadosCotizacion() throws CotizacionesDAOException;


	/**
	 * Retorna el número de registros de una consulta por criterio 
	 * @param criterio
	 * @return long cantidad de registros
	 * @throws CotizacionesDAOException
	 */
	public long getCountCotizacionesByCriteria(CotizacionesCriteriaDTO criterio ) throws CotizacionesDAOException;
	
	/**
	 * Obtiene detalle de cotización a partir de su id
	 * 
	 * @param  cot_id
	 * @return CotizacionesDTO
	 * @throws CotizacionesDAOException
	 */
	public CotizacionesDTO getCotizacionById( long cot_id ) throws CotizacionesDAOException;
	
	/**
	 * Obtiene la cantidad de productos asociados a una cotización a partir de id de cotización
	 * @param cot_id
	 * @return long 
	 * @throws CotizacionesDAOException
	 */
	public long getCountProductosEnCotizacionById( long cot_id ) throws CotizacionesDAOException;
	
	
	/**
	 * Retorna una lista con las alertas para cotizaciones
	 * @param id_cot
	 * @return AlertaDTO
	 * @throws CotizacionesDAOException
	 */
	public List getAlertasCotizacion(long id_cot) throws CotizacionesDAOException;
	
	
	/**
	 * Retorna una lista con los productos asociados a una cotización, a partir de Id de cotización
	 * @param id_cot
	 * @return List ProductosCotizacionesDTO
	 * @throws CotizacionesDAOException
	 */
	public List getProductosCotiz(long id_cot) throws CotizacionesDAOException;
	
	/**
	 * Retorna una lista con los pedidos asociados a una cotización, a partir de su ID.
	 * @param id_cot
	 * @return List PedidosCotizacionesDTO
	 * @throws CotizacionesDAOException
	 */
	public List getPedidosCotiz(long id_cot) throws CotizacionesDAOException;
	
	
	/**
	 * Retorna una lista con los logs asociados a una cotización, a partir de su id.
	 * @param id_cot
	 * @return List LogsCotizacionesDTO
	 * @throws CotizacionesDAOException
	 */
	public List getLogCotiz(long id_cot) throws CotizacionesDAOException;
	
	/**
	 * Agrega una nueva cotizacion, segun la informacion contenida en el DTO
	 * 
	 * @param dto
	 * @return id de nueva cotizacion
	 * @throws CotizacionesDAOException
	 */
	public long doInsCotizacion(ProcInsCotizacionDTO dto) throws CotizacionesDAOException;
	
	/**
	 * Agrega un nuevo detalle de una cotizacion, segun informacion contenida en el DTO
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws CotizacionesDAOException
	 */
	public boolean doInsDetCotizacion(ProcInsDetCotizacionDTO dto) throws CotizacionesDAOException;
	
	/**
	 * Obtiene detalle de la cotizacion, con pedidos relacionados
	 * 
	 * @param id_cotizacion
	 * @return List ProductosCotizacionesDTO
	 * @throws CotizacionesDAOException
	 */
	public List getLstProductosByCotizacion(long id_cotizacion) throws CotizacionesDAOException;
	
		/**
	 * Permite modificar el costo de despacho, observaciones y productos fuera de
	 * mix a una cotización especificada mediante su Id.
	 * @param ModCotizacionDTO cot
	 * @return boolean
	 * @throws CotizacionesDAOException
	 */
	public boolean doUpdCotizacion(ModCotizacionDTO cot)
	throws CotizacionesDAOException;		
	

	
	/**
	 * Agrega log a la cotización
	 * @param log
	 * @throws CotizacionesDAOException
	 */
	public boolean addLogCotizacion(LogsCotizacionesDTO log) throws CotizacionesDAOException;
	
	/**
	 * Permite cambiar el estado a una cotización
	 * @param id_cot
	 * @param id_estado
	 * @return boolean
	 * @throws CotizacionesDAOException
	 */
	public boolean setModEstadoCotizacion(long id_cot, long id_estado) throws CotizacionesDAOException;
	
	/**
	 * Permite ingresar un producto a una cotización 
	 * @param prod
	 * @return
	 * @throws CotizacionesDAOException
	 */
	public boolean addProductoCotizacion(ProductosCotizacionesDTO prod) throws CotizacionesDAOException;
	
	
	/**
	 * Permite eliminar un producto de una cotización
	 * @param prod
	 * @return
	 * @throws CotizacionesDAOException
	 */
	public boolean delProductoCotizacion(long detcot_id) throws CotizacionesDAOException;
	
	/**
	 * Permite caducar  cotizaciones con fecha mayor a la fecha de expiracion
	 * @param id_comprador Identificador único del comprador
	 * @return 
	 * @throws CotizacionesDAOException
	 */
	public boolean CaducarCotizaciones( long id_comprador )throws CotizacionesDAOException;
	
	
	/**
	 * Permite modificar la cantidad solicitada, el descuento aplicado y el precio unitario
	 * a un producto asociado a una cotización
	 * @param ModProdCotizacionesDTO prod
	 * @return
	 * @throws CotizacionesDAOException
	 */
	public boolean updProductoCotizacion(ModProdCotizacionesDTO prod) throws CotizacionesDAOException; 
	
	/**
	 * Elimina todas las alertas relacionadas a la cotizacion segun id de cotizacion
	 * 
	 * @param  id_cotizacion
	 * @return boolean
	 * @throws CotizacionesDAOException
	 */
	public boolean elimAlertaByCotizacion(long id_cotizacion)  throws CotizacionesDAOException;
	
	/**
	 * Obtiene datos de la empresa, segun id de la empresa
	 * 
	 * @param id_empresa
	 * @return EnpresasEntity
	 * @throws CotizacionesDAOException
	 */
	public EmpresasEntity getEmpresaById(long id_empresa) throws CotizacionesDAOException;
	
	/**
	 * Agrega la relacion entre una cotizacion y una alerta
	 * 
	 * @param id_cotizacion
	 * @param id_alerta
	 * @return boolean
	 * @throws CotizacionesDAOException
	 */
	public boolean addAlertaCotizacion(long id_cotizacion, int id_alerta) throws CotizacionesDAOException;
	
	/**
	 * Obtiene informacion de la direccion de despacho, segun id de direccion 
	 * 
	 * @param id_dir_desp
	 * @return DireccionEntity
	 * @throws CotizacionesDAOException
	 */
	public DireccionEntity getDireccionDespById(long id_dir_desp)  throws CotizacionesDAOException;
	
	/**
	 * Obtiene la suma de costos de productos de una cotizacion
	 * 
	 * @param dto
	 * @return double suma total
	 * @throws CotizacionesDAOException
	 */
	public double getSumaCostosByCotizacion(CotizacionesDTO dto) throws CotizacionesDAOException;
	
	/**
	 * Indica si la cotizacion tiene alguna alerta activa relacionada
	 * 
	 * @param id_cotizacion
	 * @return boolean
	 * @throws CotizacionesDAOException
	 */
	public boolean getExisteAlertaActiva(long id_cotizacion) throws CotizacionesDAOException;
	
	/**
	 * Actualizar la modificacion de datos de empresa
	 * 
	 * @param emp
	 * @return boolean
	 * @throws CotizacionesDAOException
	 */
	public boolean setModEmpresaById(EmpresasEntity emp) throws CotizacionesDAOException;
	
	
	/**
	 * Asigna Cotización
	 * 
	 * @param col
	 * @return
	 * @throws CotizacionesDAOException
	 */
	public boolean setAsignaCotizacion(AsignaCotizacionDTO col) throws CotizacionesDAOException;

	/**
	 * Recupera el listado de categorías
	 * 
	 * @return				Lista con DTO de categorías
	 * @throws ProductosDAOException
	 */
	public List getCategoriasList() throws CotizacionesDAOException;	
	
	/**
	 * Recupera información de la categoría.
	 * 
	 * @param id_categoria Identificador único de la categoría
	 * @return DTO con datos de la categoría
	 * @throws CotizacionesDAOException
	 */
	public CategoriaDTO getCategoriaById( long id_categoria )  throws CotizacionesDAOException;
	
	/**
	 * Libera una cotización, actualiza información de la cotización y usuario.
	 * @param col
	 * @return
	 * @throws CotizacionesDAOException
	 */
	public boolean setLiberaCotizacion(AsignaCotizacionDTO col) throws CotizacionesDAOException;
	
	/**
	 * Entrega la suma total de las cantidades de los productos asociados a una cotización
	 * @param id_cot
	 * @return suma total double
	 * @throws CotizacionesDAOException
	 */
	public double getTotalProductosCot(long id_cot) throws CotizacionesDAOException;
	
	/**
	 * Recupera todas las marcas diferentes de los productos para una categoría.
	 * 
	 * @param categoria_id		Identificador único de la categoría
	 * @return					Lista de DTO con datos de las marcas
	 * @throws CotizacionesDAOException
	 */
	public List getListMarcasByCategoria( long categoria_id )  throws CotizacionesDAOException;
	
	/**
	 * Recupera los productos para una categoría.
	 * 
	 * Debe considerar los criterios de búsquedas.
	 * 
	 * @param criterio DTO con información de los diferetentes datos para filtrar y ordenar el listado de productos
	 * @return Listado de productos
	 * @throws CotizacionesDAOException
	 */
	public List getListProductosByCriteria( ProductosCriteriaDTO criterio ) throws CotizacionesDAOException;
	
	/**
	 * Obtiene datos del producto sap.
	 * @param id_prod
	 * @return
	 * @throws CotizacionesDAOException
	 */
	public ProductoSapEntity getProductSapById(long id_prod) throws CotizacionesDAOException;
	
	/**
	 * Obtiene un producto del catálogo del FO
	 * 
	 * @param  id_prod_fo long 
	 * @return ProductoEntity
	 * @throws PedidosDAOException
	 * 
	 */
	public ProductoEntity getProductoPedidoByIdProdFO(long id_prod_fo) throws CotizacionesDAOException;
	
	
	/**
	 * Obtiene los códigos de barra de un producto 
	 * 
	 * @param  id_prod long 
	 * @return List CodBarraSapEntity 
	 * @throws ProductosSapDAOException
	 */
	public List getCodBarrasByProdId(long id_prod) throws CotizacionesDAOException;
	
	
	/**
	 * Permite modificar la cantidad solicitada
	 * a un producto asociado a una cotización
	 * @param ModProdCotizacionesDTO prod
	 * @return
	 * @throws CotizacionesDAOException
	 */
	public boolean updCantProductoCotizacion(ModProdCotizacionesDTO prod) throws CotizacionesDAOException; 
		
	
	/**
	 * Retorna una lista con los productos asociados a una cotización, a partir de Id de cotización
	 * @param id_cot
	 * @return List ProductosCotizacionesDTO
	 * @throws CotizacionesDAOException
	 */
	public List getProductosDetCotiz(long id_cot) throws CotizacionesDAOException;
		
	/**
	 * Permite agregar al detalle de la cotizacion (descripcion, precio unitario, cantidad)
	 * 
	 * @param id_cot
	 * @throws CotizacionesException
	 * @throws SystemException
	 */
	public boolean updProductoCotizacion( ProductosCotizacionesDTO prod ) throws CotizacionesDAOException; 

	/**
	 * Modifica el comentario y los productos fuera de mix de la cotizacion
	 * @param id_cot
	 * @param comentario
	 * @param fuera_mix
	 * @return boolean
	 * @throws CotizacionesDAOException
	 */
	public boolean setModComentarioCotizacion(long id_cot, String comentario, String fuera_mix) throws CotizacionesDAOException;
	
	/**
	 * Recupera categorías y marcas para la búsqueda
	 *  
	 * @param patron		Patrón de búsqueda
	 * @param local_id		Identificador único del local
	 * @param criterio		criterio de busqueda
	 * @return				Lista de DTO con datos de las categorías
	 * @throws SystemException
	 */
	public List getSearch( List patron, long local_id, long criterio )	throws CotizacionesDAOException;
	
	/** Recupera los itemes de los productos
	 * @param local_id: Identificador de local
	 * @param pro_padre : Identificador del producto padre
	 * @param coti_id : Identificador de la cotizacion
	 * @return
	 * @throws CotizacionesException
	 */
	public List getListItems(String local_id, long pro_padre, long coti_id) throws CotizacionesDAOException;	

}
	