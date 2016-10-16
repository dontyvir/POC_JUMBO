/*
 * Created on 29-ene-2009
 *
 */
package cl.bbr.fo.service;

import java.util.List;

import cl.bbr.fo.dao.DatosClienteDAO;
import cl.bbr.fo.exception.ServiceException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.jumbo.common.dao.DAOFactory;
import cl.jumbo.common.dao.DAOFactoryException;

/**
 * @author jdroguett
 *  
 */
public class DatosClienteService {
    Logging logger = new Logging(this);

    /**
     * @throws ServiceException
     * @throws
     *  
     */
    public List getPreguntas(int clienteId) throws ServiceException {
        try {
            DatosClienteDAO datosClienteDAO = (DatosClienteDAO) DAOFactory.getInstanceDAO(DatosClienteDAO.class.getName());
            return datosClienteDAO.getPreguntas(clienteId);
        } catch (DAOFactoryException e) {
            throw new ServiceException(e.getMessage());
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

   /**
    * @param respuestas
    * @param clienteId
    * @throws ServiceException
    */
   public void updateRespuestas(List respuestas, int clienteId) throws ServiceException {
      try {
         DatosClienteDAO datosClienteDAO = (DatosClienteDAO) DAOFactory.getInstanceDAO(DatosClienteDAO.class.getName());
         datosClienteDAO.updateRespuestas(respuestas, clienteId);
     } catch (DAOFactoryException e) {
         throw new ServiceException(e.getMessage());
     } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
     }
   }

   /**
    * @return
    * @throws ServiceException
    */
   public List getDependencia() throws ServiceException {
      try {
         DatosClienteDAO datosClienteDAO = (DatosClienteDAO) DAOFactory.getInstanceDAO(DatosClienteDAO.class.getName());
         return datosClienteDAO.getDependencia();
     } catch (DAOFactoryException e) {
         throw new ServiceException(e.getMessage());
     } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
     }
   }
}
