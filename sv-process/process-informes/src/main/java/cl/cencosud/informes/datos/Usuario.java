/*
 * Created on 09-abr-2010
 */
package cl.cencosud.informes.datos;

/**
 * @author jdroguett
 */
public class Usuario {
   private int id;
   private String nombre;
   private String apellido;
   private String apellidoMaterno;

   public String getApellido() {
      return apellido;
   }

   public void setApellido(String apellido) {
      this.apellido = apellido;
   }

   public String getApellidoMaterno() {
      return apellidoMaterno;
   }

   public void setApellidoMaterno(String apellidoMaterno) {
      this.apellidoMaterno = apellidoMaterno;
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

   public String getIniciales() {
      return this.apellido.charAt(0) + "";
   }

   public String toString() {
      return getNombre() + " " + getApellido() + " " + getApellidoMaterno();
   }
}
