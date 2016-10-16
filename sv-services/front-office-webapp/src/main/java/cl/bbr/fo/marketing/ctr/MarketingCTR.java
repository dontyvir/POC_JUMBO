package cl.bbr.fo.marketing.ctr;

import cl.bbr.fo.marketing.dao.DAOFactory;
import cl.bbr.fo.marketing.dao.JdbcMarketingDAO;
import cl.bbr.fo.marketing.dto.MarketingElementoDTO;
import cl.bbr.fo.marketing.exception.MarketingDAOException;
import cl.bbr.fo.marketing.exception.MarketingException;
import cl.bbr.log_app.Logging;

/**
 * <p>Clase para que controla el flujo de datos desde el reposotorio de datos DAO a la aplicación. 
 *  
 * @author BBR ecommerce & retail
 *
 */
public class MarketingCTR {

	/**
	 * Instancia para log4j
	 */
	private Logging logger = new Logging(this);
	

	/**
	 * Constructor
	 *
	 */
	public MarketingCTR() {
		this.logger.debug("New MarketingCTR");
		this.logger.debug("PedidosCTR - conexión a BD ");
	}


	/**
	 * Recupera atributos del elemento Marketing y realiza una modificacion si el elemento fue clickeado .
	 * 
	 * @param ele_id	Identificador del elemento
	 * @param mar_id	Identificador de la marca de la campaña
	 * @return	Lista de DTO (MarketingElementoDTO)
	 * @throws MarketingException
	 */
	public MarketingElementoDTO getMarkElemento(long ele_id, long mar_id) throws MarketingException {

		MarketingElementoDTO result = null;

		try {
			JdbcMarketingDAO marketingDAO = (JdbcMarketingDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getMarketingDAO();

			result = marketingDAO.getMarkElemento(ele_id, mar_id);
			
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (getMarkElemento)" );
			throw new MarketingException(ex);
		} catch (MarketingDAOException ex) {
			logger.error("Problema (getMarkElemento)", ex);
			throw new MarketingException(ex);
		}

		return result;
	}
	
	
}
