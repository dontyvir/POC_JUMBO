/*
 * Created on 20-may-2010
 */
package cl.bbr.boc.dto;

import java.math.BigDecimal;

/**
 * @author jdroguett
 */
public class BOPrecioDTO {
   private LocalDTO local;
   private BigDecimal precio;
   private boolean bloqueado;

   public boolean isBloqueado() {
      return bloqueado;
   }

   public void setBloqueado(boolean bloqueado) {
      this.bloqueado = bloqueado;
   }

   public LocalDTO getLocal() {
      return local;
   }

   public void setLocal(LocalDTO local) {
      this.local = local;
   }

   public BigDecimal getPrecio() {
      return precio;
   }

   public void setPrecio(BigDecimal precio) {
      this.precio = precio;
   }
}
