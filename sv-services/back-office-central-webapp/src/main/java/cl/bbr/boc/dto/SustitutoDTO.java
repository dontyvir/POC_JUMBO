/*
 * Created on 13-may-2009
 */
package cl.bbr.boc.dto;

/**
 * @author jdroguett
 */
public class SustitutoDTO {
   private String local;
   private int precio;
   private String barra;
   private String descripcion;
   private int productoId;
   private int detPickingId;
   

   public String getBarra() {
      return barra;
   }
   public void setBarra(String barra) {
      this.barra = barra;
   }
   public String getDescripcion() {
      return descripcion;
   }
   public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
   }
   public String getLocal() {
      return local;
   }
   public void setLocal(String local) {
      this.local = local;
   }
   public int getPrecio() {
      return precio;
   }
   public void setPrecio(int precio) {
      this.precio = precio;
   }
   public int getProductoId() {
      return productoId;
   }
   public void setProductoId(int productoId) {
      this.productoId = productoId;
   }
   public int getDetPickingId() {
      return detPickingId;
   }
   public void setDetPickingId(int detPickingId) {
      this.detPickingId = detPickingId;
   }
}
