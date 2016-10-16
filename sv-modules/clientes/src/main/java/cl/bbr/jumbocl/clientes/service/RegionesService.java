package cl.bbr.jumbocl.clientes.service;

import java.util.List;

import cl.bbr.jumbocl.clientes.ctrl.RegionesCTR;
import cl.bbr.jumbocl.clientes.dto.ComunaDTO;
import cl.bbr.jumbocl.clientes.dto.RegionesDTO;
import cl.bbr.jumbocl.clientes.exceptions.RegionesException;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.log.Logging;


/**
 * Capa de servicios para el área de regiones
 * 
 * @author BBR e-commerce & retail
 *
 */
public class RegionesService {

	/**
	 * Instancia para log
	 */
	Logging logger = new Logging( this );

	/**
	 * Constructor
	 *
	 */
	public RegionesService() {
		this.logger.debug("New RegionesService");
	}

	/**
	 * Recupera la lista de Regiones y la retorna como una lista de DTO (RegionesDTO).
	 * 
	 * @return	Lista de DTO (RegionesDTO)
	 * @throws ServiceException
	 */
	public List getAllRegiones() throws ServiceException {

		RegionesCTR ctr = new RegionesCTR();
		List list = null;

		try {
			list = ctr.getRegiones();
		} catch (RegionesException ex) {
			logger.error( "Problemas con controles de regiones", ex);
			throw new ServiceException(ex);
		}

		return list;

	}

	/**
	 * Recupera la region a partir de una comuna y la retorna como un objeto RegionesDTO.
	 * 
	 * @param id_comuna	identificador único de la comuna
	 * @return RegionesDTO 
	 * @throws ServiceException
	 */
	public RegionesDTO getRegion(long id_comuna) throws ServiceException {

		RegionesCTR ctr = new RegionesCTR();
		RegionesDTO region = null;

		try {
			region = ctr.getRegion(id_comuna);
		} catch (RegionesException ex) {
			logger.error( "Problemas con controles de regiones", ex);
			throw new ServiceException(ex);
		}

		return region;

	}
	
	
	/**
	 * Recupera la lista de Comunas por region  y la retorna como una lista de DTO (ComunaDTO).
	 * 
	 * @param reg_id	identificador único de la region
	 * @return	Lista de DTO (ComunaDTO)
	 * @throws ServiceException
	 */
	public List getAllComunas( long reg_id ) throws ServiceException {

		RegionesCTR ctr = new RegionesCTR();
		List list = null;

		try {
			list = ctr.getComunas(  reg_id  );
		} catch (RegionesException ex) {
			logger.error( "Problemas con controles de regiones", ex);
			throw new ServiceException(ex);
		}

		return list;

	}

    /**
     * @param idRegion
     * @return
     */
    public RegionesDTO getRegionById(int idRegion) throws ServiceException {
        RegionesCTR ctr = new RegionesCTR();
        try {
            return ctr.getRegionById(idRegion);
        } catch (RegionesException ex) {
            logger.error( "Problemas con controles de regiones", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @return
     */
    public List regionesConCobertura() throws ServiceException {
        RegionesCTR ctr = new RegionesCTR();
        try {
            return ctr.regionesConCobertura();
        } catch (RegionesException ex) {
            logger.error( "Problemas con controles de regionesConCobertura", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idRegion
     * @return
     */
    public List comunasConCoberturaByRegion(long idRegion) throws ServiceException {
        RegionesCTR ctr = new RegionesCTR();
        try {
            return ctr.comunasConCoberturaByRegion(idRegion);
        } catch (RegionesException ex) {
            logger.error( "Problemas con controles de comunasConCoberturaByRegion", ex);
            throw new ServiceException(ex);
        }
    }

    /**
     * @param idComuna
     * @return
     */
    public ComunaDTO getComunaConLocal(long idComuna) throws ServiceException {
        RegionesCTR ctr = new RegionesCTR();
        try {
            return ctr.getComunaConLocal(idComuna);
        } catch (RegionesException ex) {
            logger.error( "Problemas con controles de getComunaConLocal", ex);
            throw new ServiceException(ex);
        }
    }
    
    /**
     * @param idComuna
     * @return
     */
    public ComunaDTO getZonaxComuna(long idComuna) throws ServiceException {
        RegionesCTR ctr = new RegionesCTR();
        try {
            return ctr.getZonaxComuna(idComuna);
        } catch (RegionesException ex) {
            logger.error( "Problemas con controles de getZonaxComuna", ex);
            throw new ServiceException(ex);
        }
    }

}
