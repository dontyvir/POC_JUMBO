package cl.bbr.jumbocl.parametros.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cl.bbr.jumbocl.parametros.dao.DAOFactory;
import cl.bbr.jumbocl.parametros.dao.JdbcParametrosDAO;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.parametros.dto.ParametroFoDTO;
import cl.bbr.jumbocl.parametros.exceptions.ParametrosDAOException;
import cl.bbr.jumbocl.parametros.exceptions.ParametrosException;

import cl.bbr.jumbocl.shared.log.Logging;


/**
 * Entrega metodos de navegacion por pedidos y busqueda en base a criterios. 
 * Los resultados son listados de pedidos.
 * 
 * @author BBR 
 *
 */
public class ParametrosCtrl {
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	
	
	/**
	 * Obtiene Listado de Parametros.
	 * 
	 * @return List ParametroDTO
	 * @throws ParametrosException
	 * 
	 */
	public List getParametros()
		throws ParametrosException{
		
		List lstParametros= new ArrayList();
		
		JdbcParametrosDAO dao = (JdbcParametrosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getParametrosDAO();
		try {
		    lstParametros = dao.getParametros();
		} catch (ParametrosDAOException e) {
			logger.debug("Problema :"+e.getMessage());
			throw new ParametrosException(e);
		}
		//return result;
		return lstParametros;
	}

	
	/**
	 * Obtiene un Parametro por su Nombre.
	 * 
	 * @param  String  Name 
	 * @return List    ParametroDTO
	 * @throws ParametrosException
	 * 
	 */
	public ParametroDTO getParametroByName(String Name)
        throws ParametrosException{
	
	    ParametroDTO param = null;
	
        JdbcParametrosDAO dao = (JdbcParametrosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getParametrosDAO();
        try {
            param = dao.getParametroByName(Name);
        } catch (ParametrosDAOException e) {
            //logger.debug("Problema :"+e.getMessage());
            throw new ParametrosException(e);
        }
        //return result;
        return param;
    }
	
	/**
	 * Obtiene un Parametro por su Nombre.
	 * 
	 * @param  String  Name 
	 * @return List    ParametroDTO
	 * @throws ParametrosException
	 * 
	 */
	public Map getParametroByNameIn(String NameIn) throws ParametrosException{
		
        JdbcParametrosDAO dao = (JdbcParametrosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getParametrosDAO();
        try {
            return dao.getParametroByNameIn(NameIn);
        } catch (ParametrosDAOException e) {
            //logger.debug("Problema :"+e.getMessage());
            throw new ParametrosException(e);
        }
    }

	
	/**
	 * Obtiene un Parametro por su ID.
	 * 
	 * @param  int  Id 
	 * @return List ParametroDTO
	 * @throws ParametrosException
	 * 
	 */
    public ParametroDTO getParametroById(int Id)
        throws ParametrosException{
        
        ParametroDTO param = null;
        
        JdbcParametrosDAO dao = (JdbcParametrosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getParametrosDAO();
        try {
            param = dao.getParametroById(Id);
        } catch (ParametrosDAOException e) {
            logger.debug("Problema :"+e.getMessage());
            throw new ParametrosException(e);
        }
        //return result;
        return param;
    }
    
    
    /**
	 * Obtiene un Parametro por su ID.
	 * 
	 * @param  int  Id 
	 * @return List ParametroDTO
	 * @throws ParametrosException
	 * 
	 */
    public ParametroFoDTO getParametroFoByKey(String key) throws ParametrosException{
        
    	ParametroFoDTO param = null;
        
        JdbcParametrosDAO dao = (JdbcParametrosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getParametrosDAO();
        try {
            param = dao.getParametroFoByKey(key);
        } catch (ParametrosDAOException e) {
            logger.debug("Problema :"+e.getMessage());
            throw new ParametrosException(e);
        }
        
        return param;
    }
    
    //DESDE FO
    
    /**
	 * Recupera un parametro por el valor LLAVE.
	 * 
	 * @return	Obejto de DTO (ParametroDTO)
	 * @throws ParametrosException
	 */
	public ParametroFoDTO getParametroByKey(String key) throws ParametrosException {
		try {
		    JdbcParametrosDAO datosDAO = (JdbcParametrosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getParametrosDAO();
			return datosDAO.getParametroByKey(key);

		} catch (ParametrosDAOException ex) {
			logger.error("Problema", ex);
			throw new ParametrosException(ex);
		}

	}

	/**
	 * Recupera un parametro por el valor LLAVE.
	 * 
	 * @return	Obejto de DTO (ParametroDTO)
	 * @throws ParametrosException
	 */
	public ParametroFoDTO getParametroByID(int id) throws ParametrosException {
		try {
		    JdbcParametrosDAO datosDAO = (JdbcParametrosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getParametrosDAO();
			return datosDAO.getParametroByID(id);

		} catch (ParametrosDAOException ex) {
			logger.error("Problema", ex);
			throw new ParametrosException(ex);
		}
	}
	
	public boolean actualizaParametroByName(String Name, String valor) throws ParametrosException{
        JdbcParametrosDAO dao = (JdbcParametrosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getParametrosDAO();
        try {
           return dao.actualizaParametroByName(Name, valor);
        } catch (ParametrosDAOException e) {
            logger.debug("Problema :"+e.getMessage());
            throw new ParametrosException(e);
        }
        //return result;	        
    }
    
	
}
