package cl.bbr.ws.helperXML;

import org.jdom.*;
/**
 * Sub-Arbol de datos XML para el detalle del pedido.
 */
public class DetallePedido extends Element{
	/**
	 * Constructor del subarbol XML para el detalle del pedido
	 * @param id_det
	 * @param id_op
	 * @param cod_sap
	 * @param u_med
	 * @param desc
	 * @param ped
	 * @param pick
	 * @param Cant_sinpick
	 * @param falt
	 * @param precio
	 * @param obs
	 * @param Es_pes
	 * //  ---------- mod_ene09 - ini------------------------
	 * @param id_sector
	 * @param orden
	 * //  ---------- mod_ene09 - fin------------------------
	 * @param Sector
	 * @param Sust_camb
	 * @param Id_dronda
	 * @param pol_sust
	 * @param id_criterio
	 * @param criterio
	 */
  public DetallePedido(String id_det, String id_op, String cod_sap, String u_med, String desc, String ped, String pick, 
  						String Cant_sinpick, String falt, String precio, String obs, String Es_pes,String id_sector, String orden, String Sector, 
						String Sust_camb, String Id_dronda, String pol_sust, 
						String id_criterio, String criterio) {
    super("detalle_pedido");
    addContent(new Element("id_detalle").setText(id_det));
    addContent(new Element("id_op").setText(id_op));
    addContent(new Element("cod_sap").setText(cod_sap));
    addContent(new Element("u_med").setText(u_med));
    addContent(new Element("descripcion").setText(desc));
    addContent(new Element("cant_pedida").setText(ped));
    addContent(new Element("cant_pickeada").setText(pick));
    addContent(new Element("cant_sinpickear").setText(Cant_sinpick));
    addContent(new Element("cant_faltante").setText(falt));
    addContent(new Element("precio").setText(precio));
    addContent(new Element("observacion").setText(obs));
    addContent(new Element("es_pesable").setText(Es_pes));
    //  ---------- mod_ene09 - ini------------------------
    addContent(new Element("id_sector").setText(id_sector));
    addContent(new Element("orden").setText(orden));
    //  ---------- mod_ene09 - fin------------------------
    addContent(new Element("sector").setText(Sector));
    addContent(new Element("sust_camb_form").setText(Sust_camb));
    addContent(new Element("id_dronda").setText(Id_dronda));
    addContent(new Element("pol_sust").setText(pol_sust));
    addContent(new Element("id_criterio").setText(id_criterio));
    addContent(new Element("criterio").setText(criterio));
    
    
   }
}
