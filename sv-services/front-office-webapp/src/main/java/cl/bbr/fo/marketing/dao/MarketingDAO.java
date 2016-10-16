package cl.bbr.fo.marketing.dao;

import cl.bbr.fo.marketing.dto.MarketingElementoDTO;
import cl.bbr.fo.marketing.exception.MarketingDAOException;

/**
 * Interfaz para implementaci�n de m�todos en DAO para diferentes tipos de conexi�n a repositorios. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public interface MarketingDAO {

	/**
	 * Recupera atributos del elemento Marketing y realiza una modificacion si el elemento fue clickeado .
	 * 
	 * @param ele_id	Identificador del elemento
	 * @param mar_id	Identificador de la marca de la campa�a
	 * @return	Lista de DTO (MarketingElementoDTO)
	 * @throws MarketingDAOException
	 */
	public MarketingElementoDTO getMarkElemento( long ele_id, long mar_id) throws MarketingDAOException;	

}
