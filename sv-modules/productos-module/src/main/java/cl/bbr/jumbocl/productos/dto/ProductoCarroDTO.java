/*
 * Created on 16-mar-2009
 */
package cl.bbr.jumbocl.productos.dto;

import java.math.BigDecimal;

/**
 * @author jdroguett
 */
public class ProductoCarroDTO {
   private int id;
   private BigDecimal cantidad;
   private String nota;

   public BigDecimal getCantidad() {
      return cantidad;
   }

   public void setCantidad(BigDecimal cantidad) {
      this.cantidad = cantidad;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getNota() {
      return nota;
   }

   public void setNota(String nota) {
      this.nota = nota;
   }
}
