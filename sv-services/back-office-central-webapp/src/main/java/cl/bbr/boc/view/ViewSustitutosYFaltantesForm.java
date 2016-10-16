package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.informes.dto.OriginalesYSustitutosDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Muestra el formulario para asignar RUT's a un evento
 * @author imoyano
 */
public class ViewSustitutosYFaltantesForm extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewSustitutosYFaltantesForm Execute");
	    
	    String mensaje = "";
	    String cargaSyF = "";
	    
	    View salida = new View(res);
		String html = getServletConfig().getInitParameter("TplFile");		
		html = path_html + html;
		logger.debug("Template: " + html);	    
	    
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();		
		
		BizDelegate biz = new BizDelegate();		
		
		if (req.getParameter("mensaje") != null) {
		    mensaje = req.getParameter("mensaje").toString();
        }
		
		if (req.getParameter("cargo_syf") != null) {
		    cargaSyF = req.getParameter("cargo_syf").toString();
        }
		
		List sustitutos = biz.getOriginalesYSustitutos();
		ArrayList prods = new ArrayList();
		
		for (int i = 0; i < sustitutos.size(); i++) {			
			IValueSet fila = new ValueSet();
			OriginalesYSustitutosDTO prod = (OriginalesYSustitutosDTO) sustitutos.get(i);
			fila.setVariable("{nro_registro}",String.valueOf( i + 1 ));
			fila.setVariable("{cod_ori}", prod.getCodProdOriginal());
			fila.setVariable("{uni_ori}", prod.getUniMedProdOriginal());
			fila.setVariable("{cod_sus}", prod.getCodProdSustituto());
			fila.setVariable("{uni_sus}", prod.getUniMedProdSustituto());
			prods.add(fila);
		}
		if (prods.size() > 0 ) {
		    top.setVariable("{msj_prods}", "");
		} else {
		    top.setVariable("{msj_prods}", "No existen productos para listar.");
		}		
		
		top.setVariable("{mensaje}", mensaje);
		top.setVariable("{cargo_syf}", cargaSyF);
		
		top.setDynamicValueSets("PRODUCTOS", prods);
		
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());			

		logger.debug("Fin ViewSustitutosYFaltantesForm Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}
}
