/*
 * Created on 06-may-2009
 */
package cl.bbr.fo.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jdroguett
 *  
 */
public abstract class PreguntaDTO {
   private int id;
   private String enunciado;
   private List alternativas;
   private int dependeAltId;
   private int respuesta;
   private boolean ocultar;

   public PreguntaDTO() {
      alternativas = new ArrayList();
   }

   public List getAlternativas() {
      return alternativas;
   }

   public void addAlternativas(AlternativaDTO alt) {
      this.alternativas.add(alt);
   }

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

   public int getDependeAltId() {
      return dependeAltId;
   }

   public void setDependeAltId(int dependeAltId) {
      this.dependeAltId = dependeAltId;
   }

   public int getRespuesta() {
      return respuesta;
   }

   public String getSRespuesta() {
      return (respuesta < 0 ? "" : respuesta + "");
   }

   public void setRespuesta(int respuesta) {
      this.respuesta = respuesta;
   }

   public boolean isOcultar() {
      return ocultar;
   }

   public void setOcultar(boolean ocultar) {
      this.ocultar = ocultar;
   }

   public abstract String getEstado();

   public abstract String getControl();

}
