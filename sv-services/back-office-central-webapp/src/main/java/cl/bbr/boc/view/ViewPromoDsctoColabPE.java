package cl.bbr.boc.view;

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

public class ViewPromoDsctoColabPE extends Command {

	private static final long serialVersionUID = 1L;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
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
		
		BizDelegate biz = new BizDelegate();
		top.setVariable("{montoLimite}", biz.getMontoLimite());
		top.setVariable("{descuentoMayor}", biz.getDescuentoMayor());
		top.setVariable("{descuentoMenor}", biz.getDescuentoMenor());
		
		
		if(req.getParameter("resultado") != null){
			if(req.getParameter("resultado").trim().equals("1")) {
				top.setVariable("{resultado}", " Parámetros editados exitosamente!");
			}
			else {
				top.setVariable("{resultado}", " Ocurrio un error! Favor vuelva a intentar.");
			}
		} else {
			top.setVariable("{resultado}", "");
		}
		
		String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}
