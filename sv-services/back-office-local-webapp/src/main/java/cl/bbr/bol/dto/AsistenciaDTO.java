/*
 * Created on 12-nov-2009
 */
package cl.bbr.bol.dto;

/**
 * @author jdroguett
 */
public class AsistenciaDTO {
   private int id;
   private String nombre;
   private String descripcion;

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

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre;
   }
}
