package cl.bbr.fo.service;

import cl.bbr.fo.exception.ServiceException;
import cl.bbr.fo.marketing.ctr.MarketingCTR;
import cl.bbr.fo.marketing.dto.MarketingElementoDTO;
import cl.bbr.fo.marketing.exception.MarketingException;
import cl.bbr.log_app.Logging;

/**
 * Capa de servicios para el área de fonocompras
 * 
 * @author BBR e-commerce & retail
 *
 */
public class MarketingService {

	/**
	 * Instancia para log
	 */
	Logging logger = new Logging( this );

	/**
	 * Constructor
	 *
	 */
	public MarketingService() {
		this.logger.debug("New MarketingService");
	}

	/**
	 * Recupera atributos del elemento Marketing y realiza una modificacion si el elemento fue clickeado .
	 * 
	 * @param ele_id	Identificador del elemento
	 * @param mar_id	Identificador de la marca de la campaña
	 * @return	Lista de DTO (MarketingElementoDTO)
	 * @throws ServiceException
	 */
	public MarketingElementoDTO getMarkElemento( long ele_id, long mar_id ) throws ServiceException {

		MarketingCTR ctr = new MarketingCTR();

		try {
			return ctr.getMarkElemento( ele_id, mar_id );
		} catch (MarketingException ex) {
			logger.error( "Problemas con controles de getMarkElemento", ex);
			throw new ServiceException(ex);
		}

	}	
	
}
