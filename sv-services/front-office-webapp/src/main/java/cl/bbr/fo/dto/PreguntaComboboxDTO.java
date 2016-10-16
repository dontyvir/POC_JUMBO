/*
 * Created on 08-may-2009
 *
 */
package cl.bbr.fo.dto;

/**
 * @author jdroguett
 *
 */
public class PreguntaComboboxDTO extends PreguntaDTO {
   /* (non-Javadoc)
    * @see cl.bbr.fo.dto.PreguntaDTO#getEstado()
    */
   public String getEstado() {
      return "selected=\"selected\"";
   }

   /* (non-Javadoc)
    * @see cl.bbr.fo.dto.PreguntaDTO#getControl()
    */
   public String getControl() {
      return "combobox";
   }
}
