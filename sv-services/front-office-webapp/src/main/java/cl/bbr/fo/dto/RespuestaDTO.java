/*
 * Created on 07-may-2009
 *
 */
package cl.bbr.fo.dto;

/**
 * @author jdroguett
 *  
 */
public class RespuestaDTO {
   private int preguntaId;
   private int[] alternativasId;
   private int respuesta;
   private String control;

   public int getPreguntaId() {
      return preguntaId;
   }

   public void setPreguntaId(int preguntaId) {
      this.preguntaId = preguntaId;
   }

   public int[] getAlternativasId() {
      return alternativasId;
   }

   public void setAlternativasId(int[] alternativasId) {
      this.alternativasId = alternativasId;
   }

   public int getRespuesta() {
      return respuesta;
   }

   public void setRespuesta(int respuesta) {
      this.respuesta = respuesta;
   }

   public String getControl() {
      return control;
   }

   public void setControl(String control) {
      this.control = control;
   }

   public void setFecha(String fecha) {
      String s[] = fecha.split("/");
      String agno = s[2];
      String mes = s[1];
      String dia = s[0];
      this.respuesta = Integer.parseInt(agno) * 10000 + Integer.parseInt(mes) * 100 + Integer.parseInt(dia);
   }
}
