/*
 * Created on 24-ago-2009
 */
package cl.cencosud.informes.datos;

/**
 * @author jdroguett
 */
public final class Local {
   private int id;
   private String nombre;
   private int orden;
   private boolean pickingNormal;

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

   public int getOrden() {
      return orden;
   }

   public void setOrden(int orden) {
      this.orden = orden;
   }
   public boolean isPickingNormal() {
      return pickingNormal;
   }
   public void setPickingNormal(boolean pickingNormal) {
      this.pickingNormal = pickingNormal;
   }
}
