package cl.bbr.boc.view;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

public class ViewIndexacionBoletas extends Command {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void Execute(HttpServletRequest req, HttpServletResponse res,	UserDTO usr) throws Exception {		
		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		logger.debug("Template: " + html);
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
		
		if(req.getParameter("resultado") != null && req.getParameter("cantidad") != null){
			if(req.getParameter("resultado").trim().equals("1")) {
				top.setVariable("{resultado}", " Colaboradores, cargados exitosamente!");
				top.setVariable("{cantidad}", req.getParameter("cantidad"));
			}
			else {
				top.setVariable("{resultado}", " Ocurrio un error en la carga ! Favor verificar archivo.");
				top.setVariable("{cantidad}", "¡");
			}
		} else{
			top.setVariable("{resultado}", "");
			top.setVariable("{cantidad}", "");
		}		
		
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();				
	}

}
