/*
 * Created on 08-may-2009
 *
 */
package cl.bbr.fo.dto;

/**
 * @author jdroguett
 *  
 */
public class PreguntaFechaDTO extends PreguntaDTO {
   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.fo.dto.PreguntaDTO#getRespuesta()
    */
   public int getRespuesta() {
      return super.getRespuesta();
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.fo.dto.PreguntaDTO#setRespuesta(int)
    */
   public void setRespuesta(int respuesta) {
      super.setRespuesta(respuesta);
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.fo.dto.PreguntaDTO#getEstado()
    */
   public String getEstado() {
      return "";
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.fo.dto.PreguntaDTO#getControl()
    */
   public String getControl() {
      return "fecha";
   }

   public String getSRespuesta() {
      int resp = super.getRespuesta();
      if(resp <= 0)
         return "";
      int agno = resp / 10000;
      resp %= 10000;
      int mes = resp / 100;
      int dia = resp % 100;
      return (dia < 10 ? "0" + dia : dia + "") + "/" + (mes < 10 ? "0" + mes : mes + "") + "/" + agno;
   }
}
