/*
 * Created on 09-abr-2010
 */
package cl.cencosud.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author jdroguett
 */
public class Listas {
   public static String join(Collection list) {
      StringBuffer lista = new StringBuffer("(");
      for (Iterator iter = list.iterator(); iter.hasNext();) {
         Object elemento = iter.next();
         lista.append(elemento.toString());
         if (iter.hasNext())
            lista.append(",");
      }
      lista.append(")");
      return lista.toString();
   }
}
