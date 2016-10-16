package cl.bbr.jumbocl.contenidos.dao;

import java.util.List;

import cl.bbr.jumbocl.common.model.CategoriaSapEntity;
import cl.bbr.jumbocl.contenidos.exceptions.CategoriasSapDAOException;

/**
 * Permite las operaciones en base de datos sobre las Categorias Sap.
 * @author BBR
 *
 */
public interface CategoriasSapDAO {
	
	/**
	 * Obtiene el listado de categorias, segun el código de categoria padre.
	 * 
	 * @param  cod_cat
	 * @return List CategoriaSapEntity
	 * @throws CategoriasSapDAOException
	 * 
	 */
	public List getCategoriasSapById(String cod_cat) throws CategoriasSapDAOException;
	
	/**
	 * Obtiene el codigo de categoria padre, segun el codigo de una categoria
	 * 
	 * @param  cod_cat
	 * @return String
	 * @throws CategoriasSapDAOException
	 */
	public String getCodCatPadre(String cod_cat) throws CategoriasSapDAOException;
	
	/**
	 * Obtiene categoria Sap, segun el codigo
	 * 
	 * @param  id_cat
	 * @return CategoriaSapEntity
	 * @throws CategoriasSapDAOException
	 */
	public CategoriaSapEntity getCategoriaSapById(String id_cat) throws CategoriasSapDAOException;
	
	
}
