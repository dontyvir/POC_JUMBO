package cl.bbr.ws.helperXML;
import org.jdom.*;

/**
 * Sub-Arbol de datos XML para los codigos de barra.
 */
public class Promocion extends Element{
	
	/**
	 * Constructor del subarbol XML para un código de barras.
	 * @param Cod_barra
	 * @param Tipo_cbarra
	 * @param Id_detalle
	 */
  public Promocion( String id_detalle, String id_promocion, String sustituible, String faltante) {
    super("promo");
    addContent(new Element("id_detalle").setText(id_detalle));
    addContent(new Element("id_promocion").setText(id_promocion));
    addContent(new Element("sustituible").setText(sustituible));
    addContent(new Element("faltante").setText(faltante));
   }
}
 