package cl.jumbo.ws.sustitucion.helperXML;
import org.jdom.*;

import cl.bbr.jumbocl.pedidos.dto.BarraAuditoriaSustitucionDTO;

/**
 * Sub-Arbol de datos XML para los codigos de barra.
 */
public class CBarraSustituto extends Element{
	
	/**
	 * Constructor del subarbol XML para un código de barras.
	 * @param Cod_barra
	 * @param Tipo_cbarra
	 * @param Id_detalle
	 */
  public CBarraSustituto(BarraAuditoriaSustitucionDTO barSust) {
    super("cbarra_sustituto");
    addContent(new Element("cod_barra").setText(barSust.getCod_barra()));
    addContent(new Element("tipo_cbarra").setText(barSust.getTip_codbar()));
    addContent(new Element("id_producto").setText(barSust.getId_producto()+""));
    addContent(new Element("descripcion").setText(barSust.getDescripcion()));
    addContent(new Element("precio").setText(barSust.getPrecio()+""));
    addContent(new Element("unid_med").setText(barSust.getUnid_med()));
   }
}
 