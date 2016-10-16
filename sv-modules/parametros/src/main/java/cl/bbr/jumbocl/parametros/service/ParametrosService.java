package cl.bbr.jumbocl.parametros.service;

import java.util.List;
import java.util.Map;

import cl.bbr.jumbocl.common.exceptions.*;
import cl.bbr.jumbocl.parametros.ctrl.ParametrosCtrl;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.parametros.dto.ParametroFoDTO;
import cl.bbr.jumbocl.parametros.exceptions.ParametrosException;
import cl.bbr.jumbocl.shared.log.Logging;


/**
 * Permite manejar los Parametros. 
 * 
 * @author Richard Belmar
 *
 */
public class ParametrosService {

	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);	
	
	
	/*
	 * ------------ Pedidos ------------------
	 */

	/**
	 * Retorna un Parametro por su Nombre
	 * 
	 * @return ParametroDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.ParametroDTO
	 */
	public ParametroDTO getParametroByName(String Name)
		throws ServiceException{
		
		ParametrosCtrl parametro = new ParametrosCtrl();
		try {
			return parametro.getParametroByName(Name);
		} catch (ParametrosException e) {
			//e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Retorna un Parametro por su Nombre
	 * 
	 * @return ParametroDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.ParametroDTO
	 */
	public Map getParametroByNameIn(String paramIN) throws ServiceException{
		
		ParametrosCtrl parametro = new ParametrosCtrl();
		try {
			return parametro.getParametroByNameIn(paramIN);
		} catch (ParametrosException e) {
			//e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	/**
	 * Retorna un Parametro por su ID
	 * 
	 * @return ParametroDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.ParametroDTO
	 */	
	public ParametroDTO getParametroById(int Id) 	throws ServiceException{
	
	    ParametrosCtrl parametro = new ParametrosCtrl();
	    try {
		    return parametro.getParametroById(Id);
	    } catch (ParametrosException e) {
		    e.printStackTrace();
		    throw new ServiceException(e);
	    }
    }


	/**
	 * Retorna el Listado de Parametros
	 * 
	 * @return List ParametroDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.ParametroDTO
	 */
	public List getParametros() throws ServiceException{
		ParametrosCtrl parametro = new ParametrosCtrl();
		try {
		    return parametro.getParametros();
		} catch (ParametrosException e) {
		    e.printStackTrace();
		    throw new ServiceException(e);
		}
	}
	
	
	// public ParametroFoDTO getParametroFoByKey(String key) throws ParametrosException{
	/**
	 * Retorna un Parametro por su ID
	 * 
	 * @return ParametroDTO
	 * @throws ServiceException 
	 * @see    cl.bbr.jumbocl.pedidos.dto.ParametroDTO
	 */	
	public ParametroFoDTO getParametroFoByKey(String key) throws ServiceException{
	
	    ParametrosCtrl parametro = new ParametrosCtrl();
	    try {
		    return parametro.getParametroFoByKey(key);
	    } catch (ParametrosException e) {
		    e.printStackTrace();
		    throw new ServiceException(e);
	    }
    }
	
	/**
     * @param key
     * @return
     * @throws ServiceException
     */
    public ParametroFoDTO getParametroByKey(String key) throws ServiceException {
    	ParametrosCtrl ctr = new ParametrosCtrl();
        try {
            return ctr.getParametroByKey(key);
        } catch (ParametrosException ex) {
            logger.error( "Problemas con controles de getParametroByKey", ex);
            throw new ServiceException(ex);
        }
    }
  
    /**
     * @param key
     * @return
     * @throws ServiceException
     */
    public ParametroFoDTO getParametroByID(int id) throws ServiceException {
    	ParametrosCtrl ctr = new ParametrosCtrl();
        try {
            return ctr.getParametroByID(id);
        } catch (ParametrosException ex) {
            logger.error( "Problemas con controles de getParametroByID", ex);
            throw new ServiceException(ex);
        }
    }
}
