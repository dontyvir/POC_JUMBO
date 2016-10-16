package cl.bbr.fo.faq.dao;


import java.util.List;

import cl.bbr.fo.faq.exception.FaqDAOException;

/**
 * Interfaz para implementaci�n de m�todos en DAO para diferentes tipos de conexi�n a repositorios. 
 * 
 * @author BBR e-commerce & retail
 *
 */

public interface FaqDAO {

	/**
	 * Recupera la lista de categor�as de FAQ definidas.
	 * 
	 * @return	Lista de DTO (FaqCategoriaDTO)
	 * @throws FaqDAOException
	 */
	public List getFaqCategoria( ) throws FaqDAOException;
	
	/**
	 * Retorna las preguntas por categorias FAQ.
	 * 
	 * @param idcat	Identificador de categor�a
	 * @return	Lista de DTO (FaqPreguntaDTO)
	 * 
	 * @throws FaqDAOException
	 */	
	public List getFaqPregunta( long idcat) throws FaqDAOException;
	
}
