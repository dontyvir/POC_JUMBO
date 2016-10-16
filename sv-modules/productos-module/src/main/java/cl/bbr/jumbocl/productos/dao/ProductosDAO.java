package cl.bbr.jumbocl.productos.dao;

import java.util.List;


import cl.bbr.jumbocl.productos.exception.ProductosDAOException;

/**
 * Interfaz para implementación de métodos en DAO para diferentes tipos de conexión a repositorios. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public interface ProductosDAO {

	/**
	 * Recupera datos de la categoría
	 * 
	 * @param categoria_id		Identificador único de la categoría
	 * @return					DTO con información de la categoría
	 * @throws ProductosDAOException
	 */
	public cl.bbr.jumbocl.productos.dto.CategoriaDTO getCategoria( long categoria_id ) throws ProductosDAOException;
	
	/**
	 * Recupera el listado de categorías
	 * 
	 * @param cliente_id	Identificador único del cliente
	 * @return				Lista con DTO de categorías
	 * @throws ProductosDAOException
	 */
	public List getCategoriasList( long cliente_id ) throws ProductosDAOException;
	
	/**
	 * 
	 * Recupera los items de un producto generico
	 * 
	 * @param local_id
	 * @param pro_padre
	 * @return Lista de DTO con los item
	 * @throws ProductosDAOException
	 */
	public List getItems( String local_id, long pro_padre, long cliente_id, String orden ) throws ProductosDAOException;
	
	/**
	 * Recupera las marcas para una categoría
	 * 
	 * @param categoria_id		Identificador único de la categoría
	 * @return					Lista de DTO con datos de las marcas
	 * @throws ProductosDAOException
	 */
	public List getMarcas( long categoria_id ) throws ProductosDAOException;
	
	/**
	 * Listado con un sólo producto que corresponde a ProductoDTO para el producto consultado
	 * 
	 * @param producto_id		Identificador único del producto
	 * @param cliente_id		Identificador único del cliente
	 * @param local_id			Identificador único del local
	 * @return					DTO con datos del producto
	 * @throws ProductosDAOException
	 */
	public List getProducto( long producto_id, long cliente_id, long local_id, String idSession) throws ProductosDAOException;
	
	/**
	 * Listado de productos pra una categoría
	 * 
	 * @param local_id		Identificador único del local
	 * @param categoria_id	Identificador único de la categoría
	 * @param cliente_id	Identificador único del cliente
	 * @param marca			Marca para filtro
	 * @param orden			Forma de ordenamiento de los productos
	 * @return				Lista de DTO con datos delos productos
	 * @throws ProductosDAOException
	 */
	public List getProductosList( String local_id, long categoria_id, long cliente_id, String marca, String orden ) throws ProductosDAOException;
	
	/**
	 * Resultado de la búsqueda
	 * 
	 * @param patron		Patrón de búsqueda
	 * @param local_id		Identificador único del local
	 * @return				Lista de DTO con datos
	 * @throws ProductosDAOException
	 */
	public List getSearch( List patron, long local_id ) throws ProductosDAOException;
	
	/**
	 * Búsqueda de productos por marca
	 * 
	 * @param local_id 		Identificador único del local
	 * @param marca_id 		Identificador único de la marca para ordenar
	 * @param cliente_id 	Identificador único del cliente
	 * @param orden 		Forma de ordenamiento de los productos
	 * @param patron 		Patrón de búsqueda de productos
	 * @return 				Lista de DTO con información de los productos
	 * @throws ProductosDAOException
	 */
	public List getSearchMarca( String local_id, long marca_id, long cliente_id, String orden, List patron ) throws ProductosDAOException;
	
	/**
	 * Búsqueda de productos por sección (categoría)
	 * 
	 * @param local_id		Identificador único del local
	 * @param categoria_id	Identificador único de la categoría
	 * @param cliente_id	Identificador único del cliente
	 * @param marca			Marca para filtro
	 * @param orden			Forma de ordenamiento de los productos
	 * @param patron		Patrón de búsqueda de productos
	 * @return				Lista de DTO con información de los productos
	 * @throws ProductosDAOException
	 */
	public List getSearchSeccion( String local_id, long categoria_id, long cliente_id, String marca, String orden, List patron ) throws ProductosDAOException;
	
	/**
	 * Listado de los productos sugeridos
	 * 
	 * @param producto_id	Identificador único del producto
	 * @param cliente_id	Identificador único del cliente
	 * @param local_id		Identificador único del local
	 * @return				Lista con DTO con datos de los productos sugeridos
	 * @throws ProductosDAOException
	 */
	public List getSugerido( long producto_id, long cliente_id, long local_id, String idSession ) throws ProductosDAOException;	
	
}
