/*
 * Created on 09-nov-2009
 *
 */
package cl.bbr.bol.dto;

/**
 * @author jdroguett
 * 
 * Encargado de los pickeadores
 */
public class EncargadoDTO {
   private int id;
   private String login;
   private String nombre;
   private int pickeadoresAsociados;

   public int getPickeadoresAsociados() {
      return pickeadoresAsociados;
   }

   public void setPickeadoresAsociados(int pickeadoresAsociados) {
      this.pickeadoresAsociados = pickeadoresAsociados;
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

   public String getLogin() {
      return login;
   }

   public void setLogin(String login) {
      this.login = login;
   }

   public String toString() {
      return "id: " + id + "login: " + login + " nombre: " + nombre;
   }
}
