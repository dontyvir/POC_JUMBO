/*
 * Created on 15-abr-2010
 */
package cl.bbr.boc.dto;

/**
 * @author jdroguett
 */
public class MarcaDTO {
   private int id;
   private String nombre;
   
   public MarcaDTO(){
   }
   public MarcaDTO(int id, String nombre){
      this.id = id;
      this.nombre = nombre;
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
