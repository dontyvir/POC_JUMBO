package cl.cencosud.beans;

import java.math.BigDecimal;

/**
 * @author jdroguett
 */
public class Costo {
   private int id;
   private String codigoProd;
   private BigDecimal costo;

   public String getCodigoProd() {
      return codigoProd;
   }

   public void setCodigoProd(String codigoProd) {
      this.codigoProd = codigoProd;
   }

   public BigDecimal getCosto() {
      return costo;
   }

   public void setCosto(BigDecimal costo) {
      this.costo = costo;
   }
   public int getId() {
      return id;
   }
   public void setId(int id) {
      this.id = id;
   }
}
