package cl.bbr.jumbocl.contenidos.dao;

import java.util.List;

import cl.bbr.jumbocl.common.model.MarcaEntity;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCategoryProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelCategoryProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelGenericProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModProductDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcPubDespProductDTO;
import cl.bbr.jumbocl.contenidos.dto.MarcasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.MarcasDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoLogDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoSugerDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.ProductosDAOException;

/**
 * Permite las operaciones en base de datos sobre los Productos Web.
 * @author BBR
 *
 */
public interface ProductosDAO {

	/**
	 * Obtiene el máximo código que se le asigna a un producto genérico 
	 * 
	 * @return long 
	 * @throws ProductosDAOException
	 */
	public long getCodSapGenerico() throws ProductosDAOException;
	
	/**
	 * Obtiene el listados de estados, segun el tipo de estado y si es visible en web o no
	 * 
	 * @param  tip_estado
	 * @param  visible
	 * @return List EstadoEntity
	 * @throws ProductosDAOException
	 * @see    cl.bbr.jumbocl.common.model.EstadoEntity
	 */
	public List getEstados(String tip_estado,String visible) throws ProductosDAOException ;
	
	/**
	 * Obtiene el listados de unidades de medida 
	 * 
	 * @return List UnidadMedidaEntity
	 * @throws ProductosDAOException
	 * @see    cl.bbr.jumbocl.common.model.UnidadMedidaEntity
	 */
	public List getUnidMedida() throws ProductosDAOException ;
	
	/**
	 * Obtiene el listado de los productos, segun los criterios: cod producto, cod sap de producto, estado, tipo
	 * y cod categoria.
	 * 
	 * @param  criteria
	 * @return List ProductoEntity
	 * @throws ProductosDAOException
	 */
	public List listadoProductosByCriteria(ProductosCriteriaDTO criteria) throws ProductosDAOException;
	
	/**
	 * Obtiene la cantidad de productos, segun los criterios: cod producto, cod sap de producto, estado, tipo
 	 * y cod categoria.
	 * 
	 * @param  criteria
	 * @return int
	 * @throws ProductosDAOException
	 */
	public int getProductosCountByCriteria(ProductosCriteriaDTO criteria) throws ProductosDAOException;
	
	/**
	 * Obtiene información del producto web
	 * 
	 * @param  idProd
	 * @return ProductoEntity
	 * @throws ProductosDAOException
	 */
	public ProductoEntity getProductoById(long idProd) throws ProductosDAOException;
	
	/**
	 * Obtiene el listado de items relacionados a un producto
	 * 
	 * @param  codProd
	 * @return List ProductoEntity 
	 * @throws ProductosDAOException
	 */
	public List getItemsByProductId(long codProd) throws ProductosDAOException;
	
	/**
	 * Agrega la relación entre un producto y un item.
	 * 
	 * @param  item
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean agregaItemProducto(ProductoEntity item) throws ProductosDAOException;
	
	/**
	 * Elimina la relación entre un producto y un item.
	 * 
	 * @param  item
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean eliminaItemProducto(ProductoEntity item) throws ProductosDAOException;
	
	/**
	 * Obtiene el listado de productos sugeridos de un producto.
	 * 
	 * @param  codProd
	 * @return List ProductoEntity  
	 * @throws ProductosDAOException
	 */
	public List getSugeridosByProductId(long codProd) throws ProductosDAOException;
	
	/**
	 * Agrega la relación entre un producto y un producto sugerido
	 * 
	 * @param  suger
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean agregaSugeridoProducto(ProductoSugerDTO suger) throws ProductosDAOException;
	
	/**
	 * Eliminar la relación entre un producto y un producto sugerido
	 * 
	 * @param  id_suger
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean eliminaSugeridoProducto(long id_suger) throws ProductosDAOException;
	
	/**
	 * Obtiene el listado de categorías relacionadas a un producto
	 * 
	 * @param  codProd
	 * @return List CategoriaEntity
	 * @throws ProductosDAOException
	 */
	public List getCategoriasByProductId(long codProd) throws ProductosDAOException ;
	
	/**
	 * Actualiza información del producto segun el parámetro.
	 * 
	 * @param  procparam
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean actualizaProductosById(ProcModProductDTO procparam) throws ProductosDAOException;
	
	/**
	 * Agregar un producto web
	 * 
	 * @param  param
	 * @return int
	 * @throws ProductosDAOException
	 */
	public int agregaProducto(ProcAddProductDTO param) throws ProductosDAOException;
	
	/**
	 * Agregar un log de evento al producto web
	 * 
	 * @param  log
	 * @return int
	 * @throws ProductosDAOException
	 */
	public int agregaLogProducto(ProductoLogDTO log) throws ProductosDAOException;
	
	/**
	 * Elimina un producto. 
	 * 
	 * @param  param
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean eliminaProducto(ProcDelGenericProductDTO param) throws ProductosDAOException;
	
	/**
	 * Verifica si una categoria esta relacionada a un producto
	 * 
	 * @param  id_cat
	 * @param  id_prod
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean isCatAsocProd(long id_cat, long id_prod) throws ProductosDAOException;
	
	/**
	 * Agrega la relación entre la categoría y el producto. 
	 * 
	 * @param  param
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean agregaCategProducto(ProcAddCategoryProductDTO param) throws ProductosDAOException;
	
	/**
	 * Elimina la relación entre la categoría y el producto. 
	 * 
	 * @param  param
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean eliminaCategProducto(ProcDelCategoryProductDTO param) throws ProductosDAOException;
	
	/**
	 * Actualiza información del producto, en caso se publique o despublique. 
	 * 
	 * @param  param
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean setPubDespProduct(ProcPubDespProductDTO param) throws ProductosDAOException;
	
	/**
	 * Obtiene el listado de productos de un producto genérico. 
	 * 
	 * @param  cod_prod
	 * @return List ProductoEntity
	 * @throws ProductosDAOException
	 */
	public List getProductGenericos(long cod_prod) throws ProductosDAOException;
	
	/**
	 * Obtiene el listado de logs de eventos del producto 
	 * 
	 * @param  cod_prod
	 * @return List ProductoLogEntity 
	 * @throws ProductosDAOException
	 */
	public List getLogByProductId(long cod_prod) throws ProductosDAOException;
	
	/**
	 * Obtiene el listado de marcas 
	 * 
	 * @return List MarcaEntity  
	 * @throws ProductosDAOException
	 */
	public List getMarcas() throws ProductosDAOException;
	
	/**
	 * Obtiene el listado de marcas segun criterio
	 * 
	 * @param  dto MarcasCriteriaDTO
	 * @return List MarcaEntity
	 * @throws ProductosDAOException
	 */
	public List getMarcas(MarcasCriteriaDTO dto) throws ProductosDAOException;
	
	/**
	 * Obtiene la cantidad total de marcas
	 * 
	 * @return int
	 * @throws ProductosDAOException
	 */
	public int getMarcasAllCount() throws ProductosDAOException;
	
	/**
	 * Obtiene datos de la marca 
	 * 
	 * @param  id
	 * @return MarcaEntity
	 * @throws ProductosDAOException
	 */
	public MarcaEntity getMarcaById(long id) throws ProductosDAOException;
	
	/**
	 * Agrega una marca
	 * 
	 * @param  prm MarcasDTO
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean addMarca(MarcasDTO prm) throws ProductosDAOException;
	
	/**
	 * Actualiza información de la marca
	 * 
	 * @param  prm MarcasDTO
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean modMarca(MarcasDTO prm) throws ProductosDAOException;
	
	/**
	 * Elimina una marca
	 * 
	 * @param  prm MarcasDTO
	 * @return booelan
	 * @throws ProductosDAOException
	 */
	public boolean setDelMarca(MarcasDTO prm) throws ProductosDAOException;
	
	/**
	 * Permite obtener el producto, segun codigo sap y unidad de medida del Producto MIX 
	 * 
	 * @param  cod_sap_prod
	 * @param  uni_med
	 * @return ProductoEntity
	 * @throws ProductosDAOException
	 */
	public ProductoEntity getProductByProdMix(String cod_sap_prod, String uni_med) throws ProductosDAOException;
	
	/**
	 * Permite agregar un producto en la tabla fo_productos, del tipo Item.
	 * La información se obtiene de la tabla bo_productos
	 * 
	 * @param  param
	 * @return int
	 * @throws ProductosDAOException
	 */
	public int addByProductMix(ProcAddProductDTO param) throws ProductosDAOException;
	
	/**
	 * Eliminar log referente al producto Mix a eliminar
	 * 
	 * @param  cod_sap_prod
	 * @param  uni_med
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean delLogByProdMix(String cod_sap_prod, String uni_med) throws ProductosDAOException;
	
	/**
	 * Permite eliminar el producto en fo_productos.
	 * Solo se elimina un producto, segun codigo sap y unidad de medida. 
	 * 
	 * @param  cod_sap_prod
	 * @param  uni_med
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean delByProductMix(String cod_sap_prod, String uni_med) throws ProductosDAOException;
	
	/**
	 * Permite actualizar el estado del fo_producto a Eliminado
	 * 
	 * @param  cod_sap_prod
	 * @param  uni_med
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean eliminaByProductMix(String cod_sap_prod, String uni_med) throws ProductosDAOException;
	
	/**
	 * Verifica si el producto existe.
	 * 
	 * @param  cod_prod
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean isProductById(long cod_prod) throws ProductosDAOException;
	
	/**
	 * Verifica si la marca existe.
	 * 
	 * @param  id_marca
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean isMarcaById(long id_marca) throws ProductosDAOException;
	
	/**
	 * Verifica si la unidad de medida existe.
	 * 
	 * @param  id_ume
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean isUni_MedById(long id_ume) throws ProductosDAOException;
	
	/**
	 * Verifica la relación entre el producto y el producto sugerido
	 * 
	 * @param  id_producto
	 * @param  id_producto_sug
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean isSugAsocProd(long id_producto, long id_producto_sug) throws ProductosDAOException;
	
	/**
	 * Actualiza el estado del producto Web
	 * 
	 * @param  prod
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean actEstProductoById(ProductoEntity prod) throws ProductosDAOException;
	
	/**
	 * Actualiza información del producto Web.
	 * 
	 * @param  prod
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean actProductoByProdMixId(ProductoEntity prod) throws ProductosDAOException;
	
	/**
	 * Elimina la relación entre el producto y el producto sugerido
	 * 
	 * @param  id_producto
	 * @param  id_sug
	 * @return boolean
	 * @throws ProductosDAOException
	 */
	public boolean eliminaSugerProdBidirec(long id_producto, long id_sug) throws ProductosDAOException;
	
	
	/**
	 * Entrega el precio de un producto segun id de producto y id de local
	 * @param id_prod
	 * @param id_loc
	 * @return double
	 * @throws ProductosDAOException
	 */
	public double getProductosPrecios(long id_prod, long id_loc) throws ProductosDAOException;
	
	
	/**
	 * Obtiene el listado de productos con sus codigos de barra segun un criterio determinado
	 * @param criteria
	 * @return List
	 * @throws ProductosDAOException
	 */
	public List getProdCbarraByCriteria(ProductosCriteriaDTO criteria) throws ProductosDAOException;
	
	
	/**
	 * Obtiene la cantidad de productos existentes
	 * @param criteria
	 * @return
	 * @throws ProductosDAOException
	 */
	public int getCountProdCbarraByCriteria(ProductosCriteriaDTO criteria) throws ProductosDAOException;
	
	
	/**
	 * Obtiene la cantidad de productos existentes
	 * @param criteria
	 * @return
	 * @throws ProductosDAOException
	 */
	public List getTiposProductosPorCod(String codDesc) throws ProductosDAOException;
	
	/**
	 * Obtiene la cantidad de productos existentes
	 * @param criteria
	 * @return
	 * @throws ProductosDAOException
	 */
	public List getTiposProductosPorDesc(String codDesc) throws ProductosDAOException;
	
}
