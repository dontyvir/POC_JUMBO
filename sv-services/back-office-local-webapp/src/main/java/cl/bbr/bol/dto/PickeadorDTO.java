/*
 * Created on 09-nov-2009
 */
package cl.bbr.bol.dto;

/**
 * @author jdroguett
 */
public class PickeadorDTO {
   private int id;
   private String login;
   private String nombre;
   private String apellido;
   private boolean asignado;
   private AsistenciaPickeadorDTO asistencia;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getLogin() {
      return login;
   }

   public void setLogin(String login) {
      this.login = login;
   }

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre;
   }

   public boolean isAsignado() {
      return asignado;
   }

   public void setAsignado(boolean asignado) {
      this.asignado = asignado;
   }

   public String getApellido() {
      return apellido;
   }

   public void setApellido(String apellido) {
      this.apellido = apellido;
   }

   public AsistenciaPickeadorDTO getAsistencia() {
      return asistencia;
   }

   public void setAsistencia(AsistenciaPickeadorDTO asistencia) {
      this.asistencia = asistencia;
   }
}
