/*
 * Created on 09-abr-2010
 */
package cl.cencosud.informes.datos;

/**
 * @author jdroguett
 */
public class Asistencia {
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
