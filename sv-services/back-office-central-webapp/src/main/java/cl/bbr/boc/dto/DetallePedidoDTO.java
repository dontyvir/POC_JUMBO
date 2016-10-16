/*
 * Created on 13-may-2009
 */
package cl.bbr.boc.dto;

import java.math.BigDecimal;

/**
 * @author jdroguett
 */
public class DetallePedidoDTO {
   private int id;
   private int productoId;
   private int localId;
   private String barra;
   private int precio;
   private BigDecimal cantidadPickeada;
   private String descripcion;
   private String descripcionProducto;
   
   public String getBarra() {
      return barra;
   }
   public void setBarra(String barra) {
      this.barra = barra;
   }
   public BigDecimal getCantidadPickeada() {
      return cantidadPickeada;
   }
   public void setCantidadPickeada(BigDecimal cantidadPickeada) {
      this.cantidadPickeada = cantidadPickeada;
   }
   public String getDescripcion() {
      return descripcion;
   }
   public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
   }
   public int getId() {
      return id;
   }
   public void setId(int id) {
      this.id = id;
   }
   public int getLocalId() {
      return localId;
   }
   public void setLocalId(int localId) {
      this.localId = localId;
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
   public String getDescripcionProducto() {
      return descripcionProducto;
   }
   public void setDescripcionProducto(String descripcionProducto) {
      this.descripcionProducto = descripcionProducto;
   }
}
