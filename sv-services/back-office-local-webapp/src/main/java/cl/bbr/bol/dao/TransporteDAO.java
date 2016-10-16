/*
 * Created on 29-mar-2010

 */
package cl.bbr.bol.dao;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import cl.bbr.jumbocl.shared.exceptions.DAOException;

/**
 * @author jdroguett
 */
public interface TransporteDAO {
   /**
    * 
    * @param localId
    * @param fechaIni obligatorio
    * @param fechaFin obligatorio
    * @param patente_id 0 para todos
    * @param chofer_id 0 para todos
    * @param horaIniDesp null para todos
    * @param horaFinDesp null para todos
    * @return
    * @throws DAOException
    */
   public List getCuadratura(int localId, Date fechaIni, Date fechaFin, int patente_id, int chofer_id, Time horaIniDesp, Time horaFinDesp) throws DAOException;

}
