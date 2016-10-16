package cl.bbr.fo.faq.ctr;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.fo.faq.dao.DAOFactory;
import cl.bbr.fo.faq.dao.JdbcFaqDAO;
import cl.bbr.fo.faq.exception.FaqDAOException;
import cl.bbr.fo.faq.exception.FaqException;
import cl.bbr.log_app.Logging;

/**
 * <p>Clase para que controla el flujo de datos desde el reposotorio de datos DAO a la aplicaci�n. 
 *  
 * @author BBR ecommerce & retail
 *
 */

public class FaqCTR {

	/**
	 * Instancia para log4j
	 */
	private Logging logger = new Logging(this);
	
	/**
	 * Conexi�n a la base de datos
	 */
	private Connection conexion = null;

	/**
	 * Constructor
	 *
	 */
	public FaqCTR() {
		this.logger.debug("New FaqCTR");
		this.logger.debug("FaqCTR - conexi�n a BD ");
	}
	

	/**
	 * Recupera la lista de categor�as de FAQ definidas.
	 * 
	 * @return	Lista de DTO (FaqCategoriaDTO)
	 * @throws FaqException
	 */	
	public List getFaqCategoria() throws FaqException {

		List result = new ArrayList();

		try {
			JdbcFaqDAO FaqDAO = (JdbcFaqDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getFaqDAO();
			result = (List) FaqDAO.getFaqCategoria();

		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (getFaqCategoria)" );
			throw new FaqException(ex);
		} catch (FaqDAOException ex) {
			logger.error("Problema (getFaqCategoria)", ex);
			throw new FaqException(ex);
		}

		return result;
	}

	/**
	 * Retorna las preguntas por categorias FAQ.
	 * 
	 * @param idcat	Identificador de categor�a
	 * @return	Lista de DTO (FaqPreguntaDTO)
	 * 
	 * @throws FaqException
	 */	
	public List getFaqPregunta( long idcat ) throws FaqException {

		List result = new ArrayList();
		
		try {
			JdbcFaqDAO FaqDAO = (JdbcFaqDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getFaqDAO();

			result = (List) FaqDAO.getFaqPregunta(idcat);

		} catch ( NullPointerException ex ) {
			logger.error("Problema con nulos en los datos (getFaqPregunta)" );
			throw new FaqException(ex);
		} catch (FaqDAOException ex) {
			logger.error("Problema (getFaqPregunta)", ex);
			throw new FaqException(ex);
		}
		
		return result;

	}	
}
