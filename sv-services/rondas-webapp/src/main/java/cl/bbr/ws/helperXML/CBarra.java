package cl.bbr.ws.helperXML;
import org.jdom.*;

/**
 * Sub-Arbol de datos XML para los codigos de barra.
 */
public class CBarra extends Element{
	
	/**
	 * Constructor del subarbol XML para un código de barras.
	 * @param Cod_barra
	 * @param Tipo_cbarra
	 * @param Id_detalle
	 */
  public CBarra(String Cod_barra, String Tipo_cbarra, String Id_detalle) {
    super("cbarra");
    addContent(new Element("cod_barra").setText(Cod_barra));
    addContent(new Element("tipo_cbarra").setText(Tipo_cbarra));
    addContent(new Element("id_detalle").setText(Id_detalle));
   }
}
 