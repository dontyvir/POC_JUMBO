package cl.bbr.fo.service;

import cl.bbr.fo.exception.ServiceException;
import cl.bbr.fo.fonocompras.ctr.FonoComprasCTR;
import cl.bbr.fo.fonocompras.dto.UsuarioDTO;
import cl.bbr.fo.fonocompras.exception.FonoComprasException;
import cl.bbr.log_app.Logging;

/**
 * Capa de servicios para el área de fonocompras
 * 
 * @author BBR e-commerce & retail
 *
 */
public class FonoComprasService {

	/**
	 * Instancia para log
	 */
	Logging logger = new Logging( this );

	/**
	 * Constructor
	 *
	 */
	public FonoComprasService() {
		this.logger.debug("New FonoComprasService");
	}

	/**
	 * Recupera los datos del ejecutivo de fonocompras
	 * 
	 * @param login		Login del ejecutivo de fonocompras
	 * @return			DTO con datos del ejecutivo
	 * @throws ServiceException
	 */
	public UsuarioDTO ejecutivoGetByRut( String login ) throws ServiceException {

		FonoComprasCTR ctr = new FonoComprasCTR();

		try {
			return ctr.ejecutivoGetByRut( login );
		} catch (FonoComprasException ex) {
			logger.error( "Problemas con controles de ejecutivoGetByRut", ex);
			throw new ServiceException(ex);
		}

	}

}
