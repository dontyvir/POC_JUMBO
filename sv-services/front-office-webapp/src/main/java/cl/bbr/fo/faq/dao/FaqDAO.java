package cl.bbr.fo.faq.dao;


import java.util.List;

import cl.bbr.fo.faq.exception.FaqDAOException;

/**
 * Interfaz para implementación de métodos en DAO para diferentes tipos de conexión a repositorios. 
 * 
 * @author BBR e-commerce & retail
 *
 */

public interface FaqDAO {

	/**
	 * Recupera la lista de categorías de FAQ definidas.
	 * 
	 * @return	Lista de DTO (FaqCategoriaDTO)
	 * @throws FaqDAOException
	 */
	public List getFaqCategoria( ) throws FaqDAOException;
	
	/**
	 * Retorna las preguntas por categorias FAQ.
	 * 
	 * @param idcat	Identificador de categoría
	 * @return	Lista de DTO (FaqPreguntaDTO)
	 * 
	 * @throws FaqDAOException
	 */	
	public List getFaqPregunta( long idcat) throws FaqDAOException;
	
}
