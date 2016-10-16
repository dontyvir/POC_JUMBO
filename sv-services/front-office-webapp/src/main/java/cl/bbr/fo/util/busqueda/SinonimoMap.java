/*
 * Created on 23-feb-2009
 */
package cl.bbr.fo.util.busqueda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author jdroguett
 */
public class SinonimoMap {
   private HashMap tabla;
   
   /**
    * Formato archivo: Palabras sinónimos en una línea separadas por comas;   
    * @param archivoTexto
    * @throws IOException
    */
   public SinonimoMap(File archivoTexto) throws IOException{
      tabla = new HashMap();
      read(archivoTexto);
   }

   public String[] getSinonimos(String palabra) {
      String[] lista = (String[]) tabla.get(palabra);
      return lista;
   }

   /**
    * Carga archivo de sinónimos
    * @param archivo
    * @throws IOException
    */
   private void read(File archivo) throws IOException {
      BufferedReader reader = new BufferedReader(new FileReader(archivo));
      String texto;
      while( (texto = reader.readLine()) != null){
         String pal[] = texto.split("\\s*,\\s*");
         for (int i = 0; i < pal.length; i++) {
            tabla.put(pal[i], pal);
         }
      }
   }
}
