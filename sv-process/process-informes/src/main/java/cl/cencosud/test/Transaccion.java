/*
 * Created on 29-sep-2009
 */
package cl.cencosud.test;

import java.util.GregorianCalendar;

/**
 * @author jdroguett
 */
public class Transaccion implements Comparable{
   private int monto;
   private GregorianCalendar fecha;
   private String sucursal;
   private String local;

   public GregorianCalendar getFecha() {
      return fecha;
   }

   public void setFecha(GregorianCalendar fecha) {
      this.fecha = fecha;
   }

   public String getLocal() {
      return local;
   }

   public void setLocal(String local) {
      this.local = local;
   }

   public int getMonto() {
      return monto;
   }

   public void setMonto(int monto) {
      this.monto = monto;
   }

   public String getSucursal() {
      return sucursal;
   }

   public void setSucursal(String sucursal) {
      this.sucursal = sucursal;
   }

   /* (non-Javadoc)
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    */
   public int compareTo(Object o) {
      Factura f = (Factura)o;
      if(f.getLocal().equals(this.getLocal()) && f.getFecha().getTimeInMillis() == this.getFecha().getTimeInMillis() && f.getMonto() == this.getMonto())
         return 0;
      return 1;
   }
}
