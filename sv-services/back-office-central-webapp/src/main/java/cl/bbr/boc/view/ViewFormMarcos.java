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
import cl.bbr.jumbocl.pedidos.dto.ListaTipoGrupoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Entrega los datos para la configuración de Casos
 * 
 * @author imoyano
 */
public class ViewFormMarcos extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewFormMarcos Execute");

        long idMarco = 0;
        
	    //Objetos para pintar la pantalla
	    View salida = new View(res);
		
        String html = getServletConfig().getInitParameter("TplFile");
        html = path_html + html;
        logger.debug("Template: " + html);  
        
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
        ResourceBundle rb = ResourceBundle.getBundle("bo");
        top.setVariable("{path_listas}", rb.getString("conf.path.listas"));
        
        if ( req.getParameter("id_marco") != null ) {
            idMarco = Long.parseLong( req.getParameter("id_marco") );
        }
        top.setVariable("{id_marco}", ""+idMarco);
        if ( idMarco != 0 ) {
            top.setVariable("{accion}", "MOD");
            BizDelegate biz = new BizDelegate();
            ListaTipoGrupoDTO marco = biz.getTipoGrupoListadoById(idMarco);
            top.setVariable("{nombre_marco}", marco.getDescripcion());
            top.setVariable("{nombre_flash}", marco.getNombreArchivo());
            top.setVariable("{texto}",    marco.getTexto().replaceAll("<br>","\r\n"));
            if ( "S".equalsIgnoreCase( marco.getActivado() ) ) {
                top.setVariable("{checked_s}", "checked");
                top.setVariable("{checked_n}", "");
            } else {
                top.setVariable("{checked_s}", "");
                top.setVariable("{checked_n}", "checked");
            }        
            top.setVariable("{accion_txt}", "Modificar"); 
            
        } else {
            top.setVariable("{accion}", "NEW");
            top.setVariable("{nombre_marco}", "");
            top.setVariable("{nombre_flash}", "");
            top.setVariable("{texto}", "");
            
            top.setVariable("{checked_s}", "checked");
            top.setVariable("{checked_n}", "");
            
            top.setVariable("{accion_txt}", "Crear");        
        }
       
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());	
		
		logger.debug("Fin ViewFormMarcos Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}
}
