package cl.bbr.fo.service;

import java.util.List;

import cl.bbr.fo.exception.ServiceException;
import cl.bbr.fo.faq.ctr.FaqCTR;
import cl.bbr.fo.faq.exception.FaqException;
import cl.bbr.log_app.Logging;

/**
 * Capa de servicios para el área de fonocompras
 * 
 * @author BBR e-commerce & retail
 *
 */
public class FaqService {

	/**
	 * Instancia para log
	 */
	Logging logger = new Logging( this );

	/**
	 * Constructor
	 *
	 */
	public FaqService() {
		this.logger.debug("New FaqService");
	}

	/**
	 * Recupera la lista de categorías de FAQ definidas.
	 * 
	 * @return	Lista de DTO (FaqCategoriaDTO)
	 * @throws ServiceException
	 */	
	public List getFaqCategoria( ) throws ServiceException {

		FaqCTR catctr = new FaqCTR();
		List list = null;

		try {
			list = catctr.getFaqCategoria(  );			
		} catch (FaqException ex) {
			logger.error( "Problemas con controles de productos (getFaqCategoria)", ex);
			throw new ServiceException(ex);
		}			
		return list;
	}
	
	/**
	 * Retorna las preguntas por categorias FAQ.
	 * 
	 * @param idcat	Identificador de categoría
	 * @return	Lista de DTO (FaqPreguntaDTO)
	 * 
	 * @throws ServiceException
	 */	
	public List getFaqPregunta( long idcat) throws ServiceException {

		FaqCTR prectr = new FaqCTR();
		List list = null;

		try {
			list = prectr.getFaqPregunta(idcat);			
		} catch (FaqException ex) {
			logger.error( "Problemas con controles de productos (getFaqPregunta)", ex);
			throw new ServiceException(ex);
		}			
		return list;
	}
	
}
