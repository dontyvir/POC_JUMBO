/*
 * Created on 24-feb-2009
 */
package cl.bbr.fo.util.busqueda;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.FSDirectory;

import cl.bbr.log.Logging;

/**
 * @author jdroguett
 */
public class Ortografia {
   private long DESFASE = 900000; //15 minutos 
   private SpellChecker spellChecker;
   private Date fecha;
   private Logging logger = new Logging(this);

   private Ortografia() {
      cargar();
   }

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
      File file = new File(rb.getString("path.ortografia"));
      try {
        spellChecker = new SpellChecker(FSDirectory.getDirectory(file));
    } catch (IOException e) {
        logger.error("Error: " + e);
    }
      
//      RAMDirectory directory = new RAMDirectory();s
//      IndexReader reader = null;
//      try {
//         spellChecker = new SpellChecker(directory);
//         reader = IndexReader.open(file);
//         spellChecker.indexDictionary(new LuceneDictionary(reader, "texto"));
//      } catch (IOException e) {
//         logger.error("Error: " + e);
//         e.printStackTrace();
//      } finally {
//         try {
//            if (reader != null)
//               reader.close();
//            if(directory != null)
//               directory.close();
//         } catch (IOException e) {
//            logger.error("Error: " + e);
//         }
//      }
   }

   public SpellChecker getSpellChecker() {
      recargar();
      return spellChecker;
   }

   public static Ortografia getInstance() {
      return SingletonHolder.INSTANCE;
   }

   /**
    * Para asegurar una única instancia.
    */
   private static class SingletonHolder {
      private final static Ortografia INSTANCE = new Ortografia();
   }
}
