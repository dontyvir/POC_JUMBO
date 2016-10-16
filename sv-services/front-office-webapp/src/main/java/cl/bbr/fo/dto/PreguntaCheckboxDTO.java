/*
 * Created on 08-may-2009
 *
 */
package cl.bbr.fo.dto;

/**
 * @author jdroguett
 *
 */
public class PreguntaCheckboxDTO extends PreguntaDTO {
   /* (non-Javadoc)
    * @see cl.bbr.fo.dto.PreguntaDTO#getEstado()
    */
   public String getEstado() {
      return "checked=\"checked\"";
   }

   /* (non-Javadoc)
    * @see cl.bbr.fo.dto.PreguntaDTO#getControl()
    */
   public String getControl() {
      return "checkbox";
   }
}
