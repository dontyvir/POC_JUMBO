package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Formulario para el ingreso de un nuevo usuario
 * @author BBRI
 */
public class ViewPoligonoNewForm extends Command {
	private final static long serialVersionUID = 1;
	

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		// 1. Parámetros
	
		View salida = new View(res);
		logger.debug("User: " + usr.getLogin());
		
		String html;
		
        html = getServletConfig().getInitParameter("TplFile");
        long id_comuna = Long.parseLong(req.getParameter("id_comuna"));
		
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		BizDelegate bizDelegate = new BizDelegate();

		top.setVariable( "{msg}", "" );
		top.setVariable("{id_comuna}", id_comuna + "");
		
		//
		ArrayList dummy = new ArrayList();
		IValueSet ivsdummy = new ValueSet();
		dummy.add(ivsdummy);
		
		
		// variables dinámicas

		// variables
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
		
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}
}