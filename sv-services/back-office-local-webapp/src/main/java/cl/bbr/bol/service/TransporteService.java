/*
 * Created on 29-mar-2010
 */
package cl.bbr.bol.service;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import cl.bbr.bol.dao.TransporteDAO;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.jumbo.common.dao.DAOFactory;
import cl.jumbo.common.dao.DAOFactoryException;

/**
 * @author jdroguett
 */
public class TransporteService {
   public List getCuadratura(int localId, Date fechaIni, Date fechaFin, int patente_id, int chofer_id, Time horaIniDesp, Time horaFinDesp) throws ServiceException {
      try {
         TransporteDAO transporteDAO = (TransporteDAO)DAOFactory.getInstanceDAO(TransporteDAO.class.getName());
         return transporteDAO.getCuadratura(localId, fechaIni, fechaFin, patente_id, chofer_id, horaIniDesp, horaFinDesp);
     } catch (DAOFactoryException e) {
         e.printStackTrace();
         throw new ServiceException(e);
     } catch (DAOException e) {
        e.printStackTrace();
         throw new ServiceException(e);
     }
   }

}
