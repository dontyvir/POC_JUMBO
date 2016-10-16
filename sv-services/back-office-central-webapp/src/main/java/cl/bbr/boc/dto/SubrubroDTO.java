/*
 * Created on 14-oct-2009
 */
package cl.bbr.boc.dto;

import java.math.BigDecimal;


/**
 * @author jdroguett
 */
public class SubrubroDTO {
   private String subrubro;
   private BigDecimal precioTotal;

   public BigDecimal getPrecioTotal() {
      return precioTotal;
   }

   public void setPrecioTotal(BigDecimal precioTotal) {
      this.precioTotal = precioTotal;
   }

   public String getSubrubro() {
      return subrubro;
   }

   public void setSubrubro(String subrubro) {
      this.subrubro = subrubro;
   }
}
