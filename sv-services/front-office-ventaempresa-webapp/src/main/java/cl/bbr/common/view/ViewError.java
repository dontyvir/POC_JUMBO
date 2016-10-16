package cl.bbr.common.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;

/**
 * <p>Página que entrega un mensaje en e lmomento que se produce un error en el sistema.</p>
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class ViewError extends Command{
	private final static long serialVersionUID = 1;
	
	//protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	protected void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		View salida = new View(res);
		String mensaje = "";
		String url = "";
		
		//logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
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
		if( mensaje != null )
			top.setVariable("{mensaje}",mensaje);
		else
			top.setVariable("{mensaje}","");
		top.setVariable("{url}",url);

		String result = tem.toString(top);
	    
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}
