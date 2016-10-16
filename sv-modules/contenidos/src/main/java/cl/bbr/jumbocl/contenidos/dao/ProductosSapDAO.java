package cl.bbr.jumbocl.contenidos.dao;

import java.util.List;

import cl.bbr.jumbocl.common.model.CodBarraSapEntity;
import cl.bbr.jumbocl.common.model.PrecioSapEntity;
import cl.bbr.jumbocl.common.model.ProductoSapEntity;
import cl.bbr.jumbocl.contenidos.dto.ProductosSapCriteriaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.ProductosSapDAOException;

/**
 * Permite las operaciones en base de datos sobre los Productos Sap.
 * @author BBR
 *
 */
public interface ProductosSapDAO {
	
	/**
	 * Obtiene el listado de productos Sap segun los criterios de búsqueda.
	 * 
	 * @param  criterio
	 * @return List ProductoSapEntity 
	 * @throws ProductosSapDAOException
	 */
	public List getProductosSapByCriteria(ProductosSapCriteriaDTO criterio) throws ProductosSapDAOException;
	
	/**
	 * Obtiene la cantidad de productos Sap segun los criterios de búsqueda.
	 * 
	 * @param  criterio
	 * @return int
	 * @throws ProductosSapDAOException
	 */
	public int getCountProdSapByCriteria(ProductosSapCriteriaDTO criterio) throws ProductosSapDAOException;
	
	/**
	 * Obtiene la lista de productos Sap segun los criterios de busqueda por fecha
	 * 
	 * @param  criterio
	 * @param  fecha_ini
	 * @param  fecha_fin
	 * @return List ProductoSapEntity  
	 * @throws ProductosSapDAOException
	 */
	public List getProductosSapByCriteriaByDateRange(ProductosSapCriteriaDTO criterio, String fecha_ini, String fecha_fin) throws ProductosSapDAOException;
	
	/**
	 * Obtiene la cantidad de productos Sap segun los criterios de busqueda por fecha
	 * 
	 * @param  criterio
	 * @param  fecha_ini
	 * @param  fecha_fin
	 * @return int
	 * @throws ProductosSapDAOException
	 */
	public int getCountProdSapByCriteriaByDateRange(ProductosSapCriteriaDTO criterio, String fecha_ini, String fecha_fin) throws ProductosSapDAOException;
	
	/**
	 * Obtiene información del producto Sap
	 * 
	 * @param  id_prod
	 * @return ProductoSapEntity
	 * @throws ProductosSapDAOException
	 */
	public ProductoSapEntity getProductSapById(long id_prod) throws ProductosSapDAOException;
	
	/**
	 * Actualiza datos del producto Sap
	 * 
	 * @param  prod
	 * @return boolean
	 * @throws ProductosSapDAOException
	 */
	public boolean updProduct(ProductoSapEntity prod) throws ProductosSapDAOException;
	
	/**
	 * Obtiene el listado de precios de un producto
	 * 
	 * @param  id_prod
	 * @return List PrecioSapEntity 
	 * @throws ProductosSapDAOException
	 */
	public List getPreciosByProdId(long id_prod) throws ProductosSapDAOException;
	
	/**
	 * Agregar el precio a un producto Web.
	 * 
	 * @param  precio
	 * @param  id_prod
	 * @return boolean
	 * @throws ProductosSapDAOException
	 */
	public boolean setPreciosByProdId(PrecioSapEntity precio, long id_prod) throws ProductosSapDAOException;
	
	/**
	 * Obtiene el listado de código de barras de un producto.
	 * 
	 * @param  id_prod
	 * @return List CodBarraSapEntity  
	 * @throws ProductosSapDAOException
	 */
	public List getCodBarrasByProdId(long id_prod) throws ProductosSapDAOException;
	
	/**
	 * Agrega el código de barras a un producto Web.
	 * 
	 * @param  codBarra
	 * @param  id_prod
	 * @return boolean
	 * @throws ProductosSapDAOException
	 */
	public boolean setCodBarrasByProdId(CodBarraSapEntity codBarra, long id_prod) throws ProductosSapDAOException;
	
}
