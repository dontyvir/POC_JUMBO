package cl.bbr.ws.helperXML;
import org.jdom.*;

/**
 * Sub-Arbol de datos XML para la ronda.
 */
public class Rondas extends Element{
	/**
	 * Constructor del subarbol XML para la ronda
	 * @param Id_ronda
	 * @param Id_usuario
	 */
  public Rondas(String Id_ronda, String Id_usuario) {
    super("ronda");
    addContent(new Element("id_ronda").setText(Id_ronda));
    addContent(new Element("id_usuario").setText(Id_usuario));
   }
}
