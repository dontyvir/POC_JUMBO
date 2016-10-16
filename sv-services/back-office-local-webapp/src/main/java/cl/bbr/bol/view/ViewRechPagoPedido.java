package cl.bbr.bol.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Formulario que permite liberar una Op agregando un motivo al log
 * @author BBRI
 */
public class ViewRechPagoPedido extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_pedido=0;
		int retorno=0;
		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
				
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		
		logger.debug("Template: " + html);
		//2. Procesa parámetros del request
			
		if ( req.getParameter("id_pedido") != null ){id_pedido =  Long.parseLong(req.getParameter("id_pedido"));}
		logger.debug("Este es el id_pedido que viene:" + id_pedido);
		
		
//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		
		
		//Genera combo de seleccion de motivos
		
		// 5. variables globales
		top.setVariable("{id_pedido}", String.valueOf(id_pedido));
		
		if (retorno==1) top.setVariable("{hab}", "disabled");
		else top.setVariable("{hab}", "enabled");
		
		// 6. listas dinamicas
		
		//		 7. Salida Final
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}