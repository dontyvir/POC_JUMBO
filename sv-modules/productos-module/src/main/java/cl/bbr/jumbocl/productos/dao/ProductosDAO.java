package cl.bbr.jumbocl.productos.dao;

import java.util.List;


import cl.bbr.jumbocl.productos.exception.ProductosDAOException;

/**
 * Interfaz para implementaci�n de m�todos en DAO para diferentes tipos de conexi�n a repositorios. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public interface ProductosDAO {

	/**
	 * Recupera datos de la categor�a
	 * 
	 * @param categoria_id		Identificador �nico de la categor�a
	 * @return					DTO con informaci�n de la categor�a
	 * @throws ProductosDAOException
	 */
	public cl.bbr.jumbocl.productos.dto.CategoriaDTO getCategoria( long categoria_id ) throws ProductosDAOException;
	
	/**
	 * Recupera el listado de categor�as
	 * 
	 * @param cliente_id	Identificador �nico del cliente
	 * @return				Lista con DTO de categor�as
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
	 * Recupera las marcas para una categor�a
	 * 
	 * @param categoria_id		Identificador �nico de la categor�a
	 * @return					Lista de DTO con datos de las marcas
	 * @throws ProductosDAOException
	 */
	public List getMarcas( long categoria_id ) throws ProductosDAOException;
	
	/**
	 * Listado con un s�lo producto que corresponde a ProductoDTO para el producto consultado
	 * 
	 * @param producto_id		Identificador �nico del producto
	 * @param cliente_id		Identificador �nico del cliente
	 * @param local_id			Identificador �nico del local
	 * @return					DTO con datos del producto
	 * @throws ProductosDAOException
	 */
	public List getProducto( long producto_id, long cliente_id, long local_id, String idSession) throws ProductosDAOException;
	
	/**
	 * Listado de productos pra una categor�a
	 * 
	 * @param local_id		Identificador �nico del local
	 * @param categoria_id	Identificador �nico de la categor�a
	 * @param cliente_id	Identificador �nico del cliente
	 * @param marca			Marca para filtro
	 * @param orden			Forma de ordenamiento de los productos
	 * @return				Lista de DTO con datos delos productos
	 * @throws ProductosDAOException
	 */
	public List getProductosList( String local_id, long categoria_id, long cliente_id, String marca, String orden ) throws ProductosDAOException;
	
	/**
	 * Resultado de la b�squeda
	 * 
	 * @param patron		Patr�n de b�squeda
	 * @param local_id		Identificador �nico del local
	 * @return				Lista de DTO con datos
	 * @throws ProductosDAOException
	 */
	public List getSearch( List patron, long local_id ) throws ProductosDAOException;
	
	/**
	 * B�squeda de productos por marca
	 * 
	 * @param local_id 		Identificador �nico del local
	 * @param marca_id 		Identificador �nico de la marca para ordenar
	 * @param cliente_id 	Identificador �nico del cliente
	 * @param orden 		Forma de ordenamiento de los productos
	 * @param patron 		Patr�n de b�squeda de productos
	 * @return 				Lista de DTO con informaci�n de los productos
	 * @throws ProductosDAOException
	 */
	public List getSearchMarca( String local_id, long marca_id, long cliente_id, String orden, List patron ) throws ProductosDAOException;
	
	/**
	 * B�squeda de productos por secci�n (categor�a)
	 * 
	 * @param local_id		Identificador �nico del local
	 * @param categoria_id	Identificador �nico de la categor�a
	 * @param cliente_id	Identificador �nico del cliente
	 * @param marca			Marca para filtro
	 * @param orden			Forma de ordenamiento de los productos
	 * @param patron		Patr�n de b�squeda de productos
	 * @return				Lista de DTO con informaci�n de los productos
	 * @throws ProductosDAOException
	 */
	public List getSearchSeccion( String local_id, long categoria_id, long cliente_id, String marca, String orden, List patron ) throws ProductosDAOException;
	
	/**
	 * Listado de los productos sugeridos
	 * 
	 * @param producto_id	Identificador �nico del producto
	 * @param cliente_id	Identificador �nico del cliente
	 * @param local_id		Identificador �nico del local
	 * @return				Lista con DTO con datos de los productos sugeridos
	 * @throws ProductosDAOException
	 */
	public List getSugerido( long producto_id, long cliente_id, long local_id, String idSession ) throws ProductosDAOException;	
	
}
