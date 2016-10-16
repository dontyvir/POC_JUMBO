/*
 * Created on 04-dic-2008
 */
package cl.bbr.fo.util.busqueda;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.bbr.log.Logging;

/**
 * Ordena según la raiz de la palabra. Y por la palabra principal cuando hay
 * sinónimos.
 * 
 * @author jdroguett
 */
public class OrdenBusqueda implements Comparator {
   private List terminos; //terminos buscados
   private Logging logger = new Logging(this);

   public OrdenBusqueda(TokenStream tokenStream) {
      try {
         terminos = new ArrayList();
         Token token = new Token();
         while ((token = tokenStream.next(token)) != null) {
            terminos.add(token.term());
         }
      } catch (IOException e) {
         logger.error(e);
      }
   }

   /**
    * Orden según orden escrito de los términos buscados
    */
   public int compare(Object o1, Object o2) {
      String stem1[] = ((ProductoDTO) o1).getStem();
      String stem2[] = ((ProductoDTO) o2).getStem();
      int sizeStem = stem1.length;
      String s1 = "";
      String s2 = "";
      //stem[0] es la categoría, stem[1] es la descripción del producto
      for (int k = 0; k < sizeStem; k++) {
         s1 = stem1[k];
         s2 = stem2[k];
         for (int i = 0; i < terminos.size(); i++) {
            int i1 = s1.indexOf((String) terminos.get(i));
            int i2 = s2.indexOf((String) terminos.get(i));

            if (i1 < 0 && i2 < 0) {
               /*
                * no coincide en stem categoria, entonces sigue con stem
                * producto
                */
               break;
            } else if (i1 == i2) {
               if (i + 1 < terminos.size())
                  continue; //siguiente término
               int n = s1.compareTo(s2);
               /*
                * si son iguales y no hay mas terminos, veo el siguiente stem
                * (productos) (k + 1 == 1)
                */
               if (n == 0 && k + 1 < sizeStem)
                  break;
               /*
                * si n != 0 o ya estamos en el último stem entonces retorno el
                * resultado de la comparación
                */
               return n;
            } else if (i1 < 0) // => i2 >= 0
               return 1;
            else if (i2 < 0) // => i1 >= 0
               return -1;
            else if (i1 < i2) // el termino esta más al principio en s1
               return -1;
            else
               //if (i1 > i2) // el termino esta más al principio en s2
               return 1;
         }
      }
      return s1.compareTo(s2);
   }
}
