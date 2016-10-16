/*
 * Created on 21-ago-2009
 */
package cl.cencosud.informes.datos;

import java.util.Date;

/**
 * @author jdroguett
 */
public class Configuracion {
   private Date ultimaFecha;
   private Date ultimaFechaSemanal;
   private int tiempoMinimoRonda; //en segundos
   private int productividadMaximaRonda;

   public Date getUltimaFecha() {
      return ultimaFecha;
   }

   public void setUltimaFecha(Date ultimaFecha) {
      this.ultimaFecha = ultimaFecha;
   }

   public int getProductividadMaximaRonda() {
      return productividadMaximaRonda;
   }

   public void setProductividadMaximaRonda(int productividadMinimaRonda) {
      this.productividadMaximaRonda = productividadMinimaRonda;
   }

   public int getTiempoMinimoRonda() {
      return tiempoMinimoRonda;
   }

   public void setTiempoMinimoRonda(int tiempoMinimoRonda) {
      this.tiempoMinimoRonda = tiempoMinimoRonda;
   }
   /**
    * @return Devuelve ultimaFechaSemanal.
    */
   public Date getUltimaFechaSemanal() {
   	return ultimaFechaSemanal;
   }
   /**
    * @param ultimaFechaSemanal El ultimaFechaSemanal a establecer.
    */
   public void setUltimaFechaSemanal(Date ultimaFechaSemanal) {
   	this.ultimaFechaSemanal = ultimaFechaSemanal;
   }
}
