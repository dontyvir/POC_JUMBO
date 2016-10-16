/*
 * Created on 01-oct-2009
 *
 */
package cl.bbr.jumbocl.pedidos.util;

import java.util.Date;
import java.util.HashSet;

import cl.bbr.jumbocl.pedidos.dao.DAOFactory;
import cl.bbr.jumbocl.pedidos.dao.JdbcComunasDAO;
import cl.bbr.jumbocl.pedidos.exceptions.ComunasDAOException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * @author jdroguett
 *  
 */
public class ComunaSusceptibleFraude {
   private long DESFASE = 3600000; //1 hora
   private Date fecha;
   private HashSet set;
   private Logging logger = new Logging(this);

   private ComunaSusceptibleFraude() {
      set = new HashSet(); //para que set no sea nulo si es que llega a fallar el DAO
      fecha = new Date(); //me aseguro que fecha no sea nulo
      cargar();
   }

   /**
    *  
    */
   private void cargar() {
      JdbcComunasDAO dao = (JdbcComunasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getComunasDAO();
      try {
         set = dao.getComunasSusceptibleFraude();
         fecha = new Date();
      } catch (ComunasDAOException e) {
         logger.error(e);
         e.printStackTrace();
      }
   }

   private synchronized void recargar() {
      if (fecha.getTime() + DESFASE <= System.currentTimeMillis()) {
         cargar();
      }
   }

   public static ComunaSusceptibleFraude getInstance() {
      return SingletonHolder.INSTANCE;
   }

   public boolean estaEnListaNegra(int comunaId) {
      recargar();
      Integer id = new Integer(comunaId);
      return set.contains(id);
   }

   /**
    * Para asegurar una única instancia. Que se carga la primera vez que se necesita y no cuando parte el servidor.
    */
   private static class SingletonHolder {
      private final static ComunaSusceptibleFraude INSTANCE = new ComunaSusceptibleFraude();
   }

}
