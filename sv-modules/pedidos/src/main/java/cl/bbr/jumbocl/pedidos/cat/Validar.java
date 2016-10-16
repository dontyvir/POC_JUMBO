/*
 * Created on 07-jul-2009
 */
package cl.bbr.jumbocl.pedidos.cat;

import java.util.Hashtable;


/**
 * @author jdroguett
 */
public class Validar {
   private static int MAX_SIZE = 2500;
   private static String correlId = "0014";
   private static String correlIdRSP = "@@@@@@@@@@@@@@@@@@@@@@@@";
   private String qManager = "QM_TEST";
   private int timeout = 3000;

   private Hashtable org;

   public Validar() {



   }

   public TarjetaCAT infoCat(String numeroCAT) {
     return new TarjetaCAT();
   }

   private byte[] send(String numeroCAT) {
	   return null;
   }

   private TarjetaCAT receive(byte[] mId) {
	   return new TarjetaCAT();
   }

   private String espacios(int size) {
      String espacio = "";
      for (int i = 0; i < size; i++)
         espacio += " ";
      return espacio;
   }

   private String getOrg(String numeroCAT) {
      Long l = new Long(numeroCAT);
      String num = l.toString().substring(0, 6);
      String org = (String) this.org.get(num);
      if (org == null)
         return "603";
      return org;
   }

   public boolean isMigrada(String numeroCAT) {
      Long l = new Long(numeroCAT);
      String num = l.toString().substring(0, 6);
      return (String) this.org.get(num) == null;
   }

   private String ceros(int size) {
      String ceros = "";
      for (int i = 0; i < size; i++)
         ceros += "0";
      return ceros;
   }

   private String getNumeroCompleto(String numeroCAT) {
      Long l = new Long(numeroCAT);
      String num = l.toString();
      return ceros(19 - num.length()) + num;
   }

   /**
    * 
    * @param args
    * @throws Exception
    */
   public static void main(String[] args) throws Exception {
      Validar validar = new Validar();

      TarjetaCAT tarjetaCAT = validar.infoCat("6152803100089960");

      if (tarjetaCAT != null)
         System.out.println(tarjetaCAT.toString());
   }
   
   /*
    * Da error esta tarjeta 0006152902307078618
    */
   /*
    * 0006152902307078618 0006152902307078642 0006152902300000130 0006152902301203048 0006152902300000171
    * 0006152902304965353 0006152902300000262 0006152902300000346 0006152902300000411 0006152902300000452
    */

   /*
    * 6152902105480404 0010069875866042 0010040232559039 0010055230382015 0020044158930149 6152803100089960
    * 0010055230382015 0020044158930149 6152803100000272 6152902107149056 6152803100090018 6152803100069681
    * 0010000011037036 0020000011096014
    */
}
