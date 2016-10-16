/*
 * Created on 03-feb-2009
 */
package cl.bbr.boc.dto;

/**
 * @author jdroguett
 */
public class LocalDTO {
   private int id;

   private String codigo;

   private String nombre;

   /**
    * cuando el localDTO va acompañado por producto, esto indica si esta publicado;
    */
   private boolean publicado;
   /**
    * cuando el localDTO va acompañado por producto, esto indica si tiene stock;
    */
   private boolean tienestock;
   
   public LocalDTO(){
   }
   
   public LocalDTO(int id, String nombre){
      this.id = id;
      this.nombre = nombre;
   }

   public String getCodigo() {
      return codigo;
   }

   public void setCodigo(String codigo) {
      this.codigo = codigo;
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

   public boolean isPublicado() {
      return publicado;
   }

   public void setPublicado(boolean publicado) {
      this.publicado = publicado;
   }
   
   public boolean tieneStock() {
       return tienestock;
    }

    public void setTieneStock(boolean tienestock) {
       this.tienestock = tienestock;
    }
}
