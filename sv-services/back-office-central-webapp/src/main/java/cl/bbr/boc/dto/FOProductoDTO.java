/*
 * Created on 04-feb-2009
 *
 */
package cl.bbr.boc.dto;

import cl.jumbo.common.dto.CategoriaMasvDTO;

/**
 * @author jdroguett
 *  
 */
public class FOProductoDTO {
   private int id;
   private String codSap;
   private String descripcion;
   private MarcaDTO marca;
   private LocalDTO local;
   private CategoriaMasvDTO categoria;
   private int cantidad;

   public String getCodSap() {
      return codSap;
   }

   public void setCodSap(String codSap) {
      this.codSap = codSap;
   }

   public MarcaDTO getMarca() {
      return marca;
   }

   public void setMarca(MarcaDTO marca) {
      this.marca = marca;
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

   public LocalDTO getLocal() {
      return local;
   }

   public void setLocal(LocalDTO local) {
      this.local = local;
   }

   public int getCantidad() {
      return cantidad;
   }

   public void setCantidad(int cantidad) {
      this.cantidad = cantidad;
   }

   public CategoriaMasvDTO getCategoria() {
      return categoria;
   }

   public void setCategoria(CategoriaMasvDTO categoria) {
      this.categoria = categoria;
   }
}
