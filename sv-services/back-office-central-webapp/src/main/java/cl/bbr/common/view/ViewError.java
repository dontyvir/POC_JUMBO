package cl.bbr.common.view;

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

public class ViewError extends Command{
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		/*
		 * usuario no se ha autenticado y se le mostrara pag_err.htm
		 * se le redirige al formulario de logueo.
		 */
		if("1".equals(req.getParameter("PagErr")) && usr == null){			
			
			String location = getServletConfig().getInitParameter("LogonForm");
			res.sendRedirect(location+"?rc=2");
			return;
			
		}
		
		View salida = new View(res);
		String mensaje = "";
		String url = "";
		
		// Recupera pagina desde web.xml
		String html  = "";
		if ("1".equals(req.getParameter("PagErr"))){
		    html = getServletConfig().getInitParameter("TplFile");
		}else if ("2".equals(req.getParameter("PagErr"))){
		    html = getServletConfig().getInitParameter("TplFile2");
		}

		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

        //setear parte dinamica en html
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//parametro
		mensaje = req.getParameter("mensaje");
		url = req.getParameter("url");
		
		//Setea variables main del template 
		top.setVariable("{mensaje}",mensaje);
		top.setVariable("{url}",url);

		// variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());

		String result = tem.toString(top);
	    
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}
