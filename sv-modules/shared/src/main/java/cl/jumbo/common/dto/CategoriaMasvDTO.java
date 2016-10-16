/*
 * Created on 14-abr-2010
 */
package cl.jumbo.common.dto;

import java.util.Date;

/**
 * @author jdroguett
 */
public class CategoriaMasvDTO {
   private int id;
   private String nombre;
   private Boolean activoMasv;
   private Boolean activoBanner;
   private Date fechaInicio;
   private Date fechaTermino;
   private String bannerPrincipal;
   private String bannerSecundario1;
   private String bannerSecundario2;

   public CategoriaMasvDTO() {
   }

   public CategoriaMasvDTO(int id, String nombre) {
      this.id = id;
      this.nombre = nombre;
   }

   public boolean isActivoBanner() {
      return activoBanner == null ? false : activoBanner.booleanValue();
   }

   public void setActivoBanner(boolean activoBanner) {
      this.activoBanner = new Boolean(activoBanner);
   }

   public boolean isActivoMasv() {
      return activoMasv == null ? false : activoMasv.booleanValue();
   }

   public void setActivoMasv(boolean activoMasv) {
      this.activoMasv = new Boolean(activoMasv);
   }

   public Boolean getActivoBanner() {
      return activoBanner;
   }

   public Boolean getActivoMasv() {
      return activoMasv;
   }

   public String getBannerPrincipal() {
      return bannerPrincipal;
   }

   public void setBannerPrincipal(String bannerPrincipal) {
      this.bannerPrincipal = bannerPrincipal;
   }

   public String getBannerSecundario1() {
      return bannerSecundario1;
   }

   public void setBannerSecundario1(String bannerSecundario1) {
      this.bannerSecundario1 = bannerSecundario1;
   }

   public String getBannerSecundario2() {
      return bannerSecundario2;
   }

   public void setBannerSecundario2(String bannerSecundario2) {
      this.bannerSecundario2 = bannerSecundario2;
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
