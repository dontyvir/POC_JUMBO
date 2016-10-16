package cl.bbr.jumbocl.contenidos.dao;

import java.util.List;

import cl.bbr.jumbocl.common.model.CategoriaEntity;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCatWebDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModCatWebDTO;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModSubCatWebDTO;
import cl.bbr.jumbocl.contenidos.dto.CategoriasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.CategoriasDAOException;

/**
 * Permite las operaciones en base de datos sobre las Categorias Web.
 * @author BBR
 *
 */
public interface CategoriasDAO {

	/**
	 * Obtiene datos de una categoría Web.
	 * 
	 * @param  categoria_id
	 * @return CategoriaEntity
	 * @throws CategoriasDAOException
	 */
	public CategoriaEntity getCategoriaById(long categoria_id ) throws CategoriasDAOException;
	
	/**
	 * Crea una nueva categoría Web.
	 * 
	 * @param  categoria
	 * @return boolean
	 * @throws CategoriasDAOException
	 */
	public boolean creaCategoria( ProcAddCatWebDTO categoria) throws CategoriasDAOException;
	
	/**
	 * Actualiza la información de una categoría Web.
	 * 
	 * @param  categoria
	 * @return boolean
	 * @throws CategoriasDAOException
	 */
	public boolean modificaCategoria( ProcModCatWebDTO categoria) throws CategoriasDAOException;
	
	/**
	 * Elimina una categoría Web.
	 * 
	 * @param  categoria_id
	 * @return boolean
	 * @throws CategoriasDAOException
	 */
	public boolean eliminaCategoria( long categoria_id ) throws CategoriasDAOException;
	
	/**
	 * Obtiene el listado de las categorías Web, segun los criterios de búsqueda.
	 * 
	 * @param  criteria
	 * @return List CategoriaEntity
	 * @throws CategoriasDAOException
	 * @see	   CategoriaEntity
	 */
	public List listadoCategoriasByCriteria(CategoriasCriteriaDTO criteria) throws CategoriasDAOException;
	
	/**
	 * Obtiene la cantidad de las categorías, segun los criterios de búsqueda.
	 * 
	 * @param  criteria
	 * @return int
	 * @throws CategoriasDAOException
	 */
	public int countCategoriasByCriteria(CategoriasCriteriaDTO criteria) throws CategoriasDAOException;
	
	/**
	 * Obtiene el listado de categorias por navegación.
	 * 
	 * @param  criterio
	 * @param  cat_padre
	 * @return List CategoriaEntity
	 * @throws CategoriasDAOException
	 */
	public List listadoCategoriasNavegacion(CategoriasCriteriaDTO criterio, long cat_padre)throws CategoriasDAOException;
	
	/**
	 * Obtiene el listado de estados, segun el tipo de estado.
	 * 
	 * @param  tip_estado
	 * @return List EstadoEntity
	 * @throws CategoriasDAOException
	 */
	public List getEstados(String tip_estado)throws CategoriasDAOException;
	
	/**
	 * Obtiene el listado de categorias asociadas a una categoria.
	 * 
	 * @param  codCat
	 * @return List CategoriaEntity
	 * @throws CategoriasDAOException
	 */
	public List getCategoriasByCategId(long codCat) throws CategoriasDAOException;
	
	/**
	 * Agregar una subcategoria a una categoria.
	 * 
	 * @param  prm
	 * @return boolean
	 * @throws CategoriasDAOException
	 */
	public boolean addSubCategory(ProcModSubCatWebDTO prm) throws CategoriasDAOException;
	
	/**
	 * Eliminar la relación entre una categoria y una subcategoria.
	 * 
	 * @param  prm
	 * @return boolean
	 * @throws CategoriasDAOException
	 */
	public boolean delSubCategory(ProcModSubCatWebDTO prm) throws CategoriasDAOException;
	
	/**
	 * Verifica si existe la categoria.
	 * 
	 * @param  id
	 * @return boolean
	 * @throws CategoriasDAOException
	 */
	public boolean isCategoriaById(long id) throws CategoriasDAOException;
	
	/**
	 * Verifica si existe la relación entre una categoria y una subcategoría.
	 * 
	 * @param  id_cat
	 * @param  id_subcat
	 * @return boolean
	 * @throws CategoriasDAOException
	 */
	public boolean isCategRelSubCateg(long id_cat, long id_subcat) throws CategoriasDAOException;
	
}
