/*
 * Created on 23-feb-2009
 */
package cl.bbr.fo.util.busqueda;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;

import cl.bbr.log.Logging;

/**
 * Para mantener una única instancia de SinonimoMap y así cargar el archivo una
 * sola vez.
 * 
 * @author jdroguett
 *  
 */
public class Sinonimos {
   private long DESFASE = 900000; //15 minutos 
   private SinonimoMap sinonimoMap;
   private Date fecha;
   private Logging logger = new Logging(this);

   private Sinonimos() {
      cargar();
   }

   public static Sinonimos getInstance() {
      return SingletonHolder.INSTANCE;
   }

   public SinonimoMap getSinominoMap() {
      recargar();
      return sinonimoMap;
   }

   /**
    * 
    */
   private synchronized void recargar() {
      if (fecha.getTime() + DESFASE <= System.currentTimeMillis()){
         cargar();
      }
   }

   /**
    * 
    */
   private void cargar() {
      fecha = new Date();
      ResourceBundle rb = ResourceBundle.getBundle("fo");
      File sinonimosFile = new File(rb.getString("path.sinonimos"));
      try {
         sinonimoMap = new SinonimoMap(sinonimosFile);
      } catch (IOException e) {
         logger.error("Error: " + e);
         e.printStackTrace();
      }
   }

   /**
    * Para asegurar una única instancia. Que se carga la primera vez que se
    * necesita y no cuando parte el servidor.
    */
   private static class SingletonHolder {
      private final static Sinonimos INSTANCE = new Sinonimos();
   }
}
