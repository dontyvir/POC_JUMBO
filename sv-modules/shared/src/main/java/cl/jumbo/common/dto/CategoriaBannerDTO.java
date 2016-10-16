/*
 * Created on 14-abr-2010
 */
package cl.jumbo.common.dto;

import java.util.Date;

/**
 * @author jdroguett
 */
public class CategoriaBannerDTO {
   private int id;
   private String nombre;
   private boolean estado;
   private Date fechaInicio;
   private Date fechaTermino;
   private String bannerPrincipal;

   public CategoriaBannerDTO() {
   }

   public CategoriaBannerDTO(int id, String nombre) {
      this.id = id;
      this.nombre = nombre;
   }

   public boolean getEstado() {
      return estado;
   }

   public void setEstado(boolean estado) {
      this.estado = estado;
   }

   public String getBannerPrincipal() {
      return bannerPrincipal;
   }

   public void setBannerPrincipal(String bannerPrincipal) {
      this.bannerPrincipal = bannerPrincipal;
   }

   public Date getFechaInicio() {
      return fechaInicio;
   }

   public void setFechaInicio(Date fechaInicio) {
      this.fechaInicio = fechaInicio;
   }

   public Date getFechaTermino() {
      return fechaTermino;
   }

   public void setFechaTermino(Date fechaTermino) {
      this.fechaTermino = fechaTermino;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre;
   }
}
