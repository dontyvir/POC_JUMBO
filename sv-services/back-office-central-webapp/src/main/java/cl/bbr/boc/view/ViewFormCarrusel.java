package cl.bbr.boc.view;

import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.ProductoCarruselDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Entrega los datos para la configuración de Casos
 * 
 * @author imoyano
 */
public class ViewFormCarrusel extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewFormCarrusel Execute");

        long idProductoCarrusel = 0;
        
	    //Objetos para pintar la pantalla
	    View salida = new View(res);
		
        String html = getServletConfig().getInitParameter("TplFile");
        html = path_html + html;
        logger.debug("Template: " + html);  
        
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
        
        if ( req.getParameter("id_prod_carrusel") != null ) {
            idProductoCarrusel = Long.parseLong( req.getParameter("id_prod_carrusel") );
        }
        top.setVariable("{id_marco}", ""+idProductoCarrusel);
        if ( idProductoCarrusel != 0 ) {
            top.setVariable("{accion}", "Modificar");
            BizDelegate biz = new BizDelegate();
            ProductoCarruselDTO prod = biz.getProductoCarruselById(idProductoCarrusel);
//+20120323coh            
/*            top.setVariable("{nombre}", prod.getNombre());
            top.setVariable("{descripcion}", prod.getDescripcion().replaceAll("<br>","\r\n"));
            top.setVariable("{precio}", prod.getDescPrecio());
            top.setVariable("{fc_inicio}", Formatos.frmFecha( prod.getFcInicio()) );
            top.setVariable("{fc_termino}", Formatos.frmFecha( prod.getFcTermino()) );
            top.setVariable("{id_prod_carrusel}", ""+prod.getIdProductoCarrusel());
            top.setVariable("{imagen}", prod.getImagen());
            
            if ( "S".equalsIgnoreCase( prod.getConCriterio() ) ) {
                top.setVariable("{checked_cri_s}", "checked");
                top.setVariable("{checked_cri_n}", "");
            } else {
                top.setVariable("{checked_cri_s}", "");
                top.setVariable("{checked_cri_n}", "checked");
            }
            
*/
            top.setVariable("{id_prod_carrusel}", ""+prod.getIdProductoCarrusel());
            top.setVariable("{sap}", prod.getIdCodigoSAP());
            top.setVariable("{descripcion}", prod.getDescripcion().replaceAll("<br>","\r\n"));
            top.setVariable("{precio}", prod.getDescPrecio());
            top.setVariable("{fc_inicio}", Formatos.frmFecha( prod.getFcInicio()) );
            top.setVariable("{fc_termino}", Formatos.frmFecha( prod.getFcTermino()) );
            top.setVariable("{link_destino}", prod.getLinkDestino() );
            top.setVariable("{imagen}", prod.getImagen());
        } else {
/*            top.setVariable("{accion}", "Nuevo");
            top.setVariable("{nombre}", "");
            top.setVariable("{descripcion}", "");
            top.setVariable("{precio}", "");
            top.setVariable("{fc_inicio}", "");
            top.setVariable("{hr_inicio}", "");
            top.setVariable("{fc_termino}", "");
            top.setVariable("{hr_termino}", "");
            top.setVariable("{id_prod_carrusel}", "0");
            top.setVariable("{imagen}", "");
            
            top.setVariable("{checked_cri_s}", "checked");
            top.setVariable("{checked_cri_n}", "");
*/           
            top.setVariable("{accion}", "Nuevo");
            top.setVariable("{id_prod_carrusel}", "0");
            top.setVariable("{sap}", "");
            top.setVariable("{descripcion}", "");
            top.setVariable("{precio}", "");
            top.setVariable("{fc_inicio}", "");
            top.setVariable("{hr_inicio}", "");
            top.setVariable("{fc_termino}", "");
            top.setVariable("{hr_termino}", "");
            top.setVariable("{link_destino}", "");
            top.setVariable("{imagen}", "");
        }   
//-20120323coh            
        ResourceBundle rb = ResourceBundle.getBundle("bo");
        top.setVariable("{url_imgs}",rb.getString("dir_carrusel"));
       
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());	
		
		logger.debug("Fin ViewFormCarrusel Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}
}
