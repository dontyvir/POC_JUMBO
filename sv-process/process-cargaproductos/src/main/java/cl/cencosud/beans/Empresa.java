package cl.cencosud.beans;

import java.math.BigDecimal;

/**
 * @author jdroguett
 */
public class Empresa {
   private int rut;
   private char dv;
   private BigDecimal saldo;

   public char getDv() {
      return dv;
   }

   public void setDv(char dv) {
      this.dv = dv;
   }

   public int getRut() {
      return rut;
   }

   public void setRut(int rut) {
      this.rut = rut;
   }

   public BigDecimal getSaldo() {
      return saldo;
   }

   public void setSaldo(BigDecimal saldo) {
      this.saldo = saldo;
   }
}
