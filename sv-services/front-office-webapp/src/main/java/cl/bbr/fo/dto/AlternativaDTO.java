/*
 * Created on 06-may-2009
 */
package cl.bbr.fo.dto;

/**
 * @author jdroguett
 */
public class AlternativaDTO {
   private int id;
   private String enunciado;
   private boolean elegida;

   public String getEnunciado() {
      return enunciado;
   }

   public void setEnunciado(String enunciado) {
      this.enunciado = enunciado;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public boolean isElegida() {
      return elegida;
   }

   public void setElegida(boolean elegida) {
      this.elegida = elegida;
   }
}
