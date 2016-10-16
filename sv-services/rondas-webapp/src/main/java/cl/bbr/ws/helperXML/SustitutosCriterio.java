package cl.bbr.ws.helperXML;
import org.jdom.*;

/**
 * Sub-Arbol de datos XML para la ronda.
 */
public class SustitutosCriterio extends Element{
	/**
	 * Constructor del subarbol XML para la ronda
	 * @param Id_ronda
	 * @param Id_usuario
	 */
  public SustitutosCriterio(String id_criterio, String criterio) {
    super("sustituto_criterio");
    addContent(new Element("id_criterio").setText(id_criterio));
    addContent(new Element("criterio").setText(criterio));
   }
}
