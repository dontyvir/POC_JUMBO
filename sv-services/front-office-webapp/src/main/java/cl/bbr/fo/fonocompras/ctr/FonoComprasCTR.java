package cl.bbr.fo.fonocompras.ctr;

import java.sql.Connection;
import java.sql.SQLException;

import cl.bbr.fo.fonocompras.dao.DAOFactory;
import cl.bbr.fo.fonocompras.dao.JdbcFonoComprasDAO;
import cl.bbr.fo.fonocompras.dto.UsuarioDTO;
import cl.bbr.fo.fonocompras.exception.FonoComprasDAOException;
import cl.bbr.fo.fonocompras.exception.FonoComprasException;
import cl.bbr.log_app.Logging;

/**
 * <p>Clase para que controla el flujo de datos desde el reposotorio de datos DAO a la aplicación. 
 *  
 * @author BBR ecommerce & retail
 *
 */
public class FonoComprasCTR {

	/**
	 * Instancia para log4j
	 */
	private Logging logger = new Logging(this);
	

	/**
	 * Constructor
	 *
	 */
	public FonoComprasCTR() {
		this.logger.debug("New FonoComprasCTR");
		this.logger.debug("FonoComprasCTR - conexión a BD ");
	}
		
	/**
	 * Recupera los datos del ejecutivo de fonocompras
	 * 
	 * @param login		Login del ejecutivo de fonocompras
	 * @return			DTO con datos del ejecutivo
	 * @throws FonoComprasDAOException
	 */
	public UsuarioDTO ejecutivoGetByRut( String login ) throws FonoComprasException {

		UsuarioDTO res = null;
		
		try {
			JdbcFonoComprasDAO fonocomprasDAO = (JdbcFonoComprasDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getFonoComprasDAO();

			res = fonocomprasDAO.ejecutivoGetByRut(login);
			

		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (ejecutivoGetByRut)" );
			throw new FonoComprasException(ex);
		} catch (FonoComprasDAOException ex) {
			logger.error("Problema (ejecutivoGetByRut)", ex);
			throw new FonoComprasException(ex);
		}
		
		return res;

	}
	
}
