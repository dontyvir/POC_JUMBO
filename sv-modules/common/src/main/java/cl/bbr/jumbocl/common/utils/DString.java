/*
 * Created on 25-feb-2009
 *
 */
package cl.bbr.jumbocl.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

/**
 * @author jdroguett Métodos utiles para string
 */
public class DString {

   /**
    * Codifica el arreglo para pasarlo por un html hidden
    * 
    * @param a
    * @return
    */
   public static String encode(String[] a) {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < a.length; i++) {
         sb.append(a[i]);
         if (i + 1 < a.length)
            sb.append(",");
      }
      try {
         return URLEncoder.encode(sb.toString(), "UTF-8");
      } catch (UnsupportedEncodingException e) {
         return null;
      }
   }

   /**
    * Decodifica un string que vienen de un html hidden hacia un arreglo
    * 
    * @param s
    * @return
    */
   public static String[] decode(String s) {
      try {
         s = URLDecoder.decode(s, "UTF-8");
      } catch (UnsupportedEncodingException e) {
         return null;
      }
      String[] a = s.split(",");
      return a;
   }

   /**
    * @param
    * @return Formato: "(123,43234,454,56456)"
    */
   public static String join(List list) {
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
   
   /**
    * @param
    * @return Formato: "(123),(43234),(454),(56456)"
    */
   public static String joinJoin(List list) {
      StringBuffer lista = new StringBuffer();
      for (Iterator iter = list.iterator(); iter.hasNext();) {
         Object elemento = iter.next();
         lista.append("(" + elemento.toString() + ")");
         if (iter.hasNext())
            lista.append(",");
      }
      return lista.toString();
   }


   /**
    * @param
    * @return Formato: "(123,43234,454,56456)"
    */
   public static String join(Object[] list) {
      StringBuffer lista = new StringBuffer("(");
      for (int i = 0; i < list.length; i++) {
         Object elemento = list[i];
         lista.append(elemento.toString());
         if (i + 1 < list.length)
            lista.append(",");
      }
      lista.append(")");
      return lista.toString();
   }
   
   public static String abreviar(String s){
      StringBuffer sb = new StringBuffer();
      String a[] = s.split("\\s+");
      for (int i = 0; i < a.length; i++) {
         sb.append(a[i].charAt(0));
      }
      return sb.toString();
   }
}
