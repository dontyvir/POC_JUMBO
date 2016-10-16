/*
 * Created on 29-sep-2009
 */
package cl.cencosud.test;

import java.util.GregorianCalendar;

/**
 * @author jdroguett
 */
public class Factura {
   private int numero;
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

   public int getMonto() {
      return monto;
   }

   public void setMonto(int monto) {
      this.monto = monto;
   }

   public int getNumero() {
      return numero;
   }

   public void setNumero(int numero) {
      this.numero = numero;
   }

   public String getSucursal() {
      return sucursal;
   }

   public void setSucursal(String sucursal) {
      this.sucursal = sucursal;
   }

   public String getLocal() {
      return local;
   }

   public void setLocal(String local) {
      this.local = local;
   }
}
