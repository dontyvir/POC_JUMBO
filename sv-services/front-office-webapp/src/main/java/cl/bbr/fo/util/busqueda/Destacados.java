/*
 * Created on 02-mar-2009
 */
package cl.bbr.fo.util.busqueda;

import java.util.Date;
import java.util.Hashtable;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.log.Logging;

/**
 * @author jdroguett
 */
public class Destacados {
   private long DESFASE = 300000; //5 minutos 
   private Hashtable productos;
   private Date fecha;
   private static Logging logger = new Logging(Destacados.class);

   private Destacados() {
      productos = new Hashtable();
   }

   private synchronized void recargar(int localId) {
      if (fecha == null || (fecha.getTime() + DESFASE <= System.currentTimeMillis())){
         cargar(localId);
      }
   }
   
   private void cargar(int localId) {
      fecha = new Date();
      try {
         logger.debug("cargar destacado: " + fecha);
         BizDelegate biz = new BizDelegate();
         Hashtable destacados = biz.getProductosDestacadosDeHoy(localId);
         productos.put(new Integer(localId), destacados);
      } catch (Exception e) {
         logger.error(e);
      }
   }

   public Hashtable getProductos(int localId) {
      recargar(localId);
      return (Hashtable) productos.get(new Integer(localId));
   }

   public static Destacados getInstance() {
      return SingletonHolder.INSTANCE;
   }

   /**
    * Para asegurar una única instancia.
    */
   private static class SingletonHolder {
      private final static Destacados INSTANCE = new Destacados();
   }
}
