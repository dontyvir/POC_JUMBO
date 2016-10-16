/*
 * Created on 09-nov-2009
 */
package cl.bbr.bol.service;

import java.util.Date;
import java.util.List;

import cl.bbr.bol.dao.PickeadorDAO;
import cl.bbr.bol.dto.AsistenciaPickeadorDTO;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.jumbo.common.dao.DAOFactory;
import cl.jumbo.common.dao.DAOFactoryException;

/**
 * @author jdroguett
 *  
 */
public class PickeadorService {

   /**
    * Lista de encargados del local que tienen asociados 1 o más pickeadores
    * @param localId
    * @return
    */
   public List getEncargados(int localId) throws ServiceException {
      try {
         PickeadorDAO pickeadorDAO = (PickeadorDAO)DAOFactory.getInstanceDAO(PickeadorDAO.class.getName());
         return pickeadorDAO.getEncargados(localId);
     } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
     } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
     }
   }
   
   public List getEncargados(int localId, String patron) throws ServiceException {
      try {
         PickeadorDAO pickeadorDAO = (PickeadorDAO)DAOFactory.getInstanceDAO(PickeadorDAO.class.getName());
         return pickeadorDAO.getEncargados(localId, patron);
     } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
     } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
     }
   }

   /**
    * @param localId
    * @param encargadoId
    * @return
    * @throws ServiceException
    */
   public List getPickeadoresPorEncargado(int localId, int encargadoId) throws ServiceException {
      try {
         PickeadorDAO pickeadorDAO = (PickeadorDAO)DAOFactory.getInstanceDAO(PickeadorDAO.class.getName());
         return pickeadorDAO.getPickeadoresPorEncargado(localId, encargadoId);
     } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
     } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
     }
   }

   /**
    * @param pickeadoresId
    * @param localId
    * @param loginEncargado
    * @throws ServiceException
    */
   public void updAsociacion(int[] pickeadoresId, int encargadoId, int localId) throws ServiceException {
      try {
         PickeadorDAO pickeadorDAO = (PickeadorDAO)DAOFactory.getInstanceDAO(PickeadorDAO.class.getName());
         pickeadorDAO.updAsociacion(pickeadoresId, encargadoId, localId);
     } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
     } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
     }   
   }

   /**
    * @return
    * @throws ServiceException
    */
   public List getAsistencias() throws ServiceException {
      try {
         PickeadorDAO pickeadorDAO = (PickeadorDAO)DAOFactory.getInstanceDAO(PickeadorDAO.class.getName());
         return pickeadorDAO.getAsistencias();
     } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e.getMessage());
     } catch (DAOException e) {
         throw new ServiceException(e.getMessage());
     }
   }

   /**
    * @param localId
    * @param encargadoId
    * @param fecha
    * @return
    */
   public List getAsistenciasPickeadores(int localId, int encargadoId, Date fecha)throws ServiceException {
      try {
         PickeadorDAO pickeadorDAO = (PickeadorDAO)DAOFactory.getInstanceDAO(PickeadorDAO.class.getName());
         return pickeadorDAO.getAsistenciasPickeadores(localId, encargadoId, fecha);
     } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e);
     } catch (DAOException e) {
         throw new ServiceException(e);
     }
   }

   /**
    * @param asistenciaPick
    * @throws ServiceException
    */
   public void updAsistencia(AsistenciaPickeadorDTO asistenciaPick) throws ServiceException {
      try {
         PickeadorDAO pickeadorDAO = (PickeadorDAO)DAOFactory.getInstanceDAO(PickeadorDAO.class.getName());
         pickeadorDAO.updAsistencia(asistenciaPick);
     } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e);
     } catch (DAOException e) {
         throw new ServiceException(e);
     }
   }
}
