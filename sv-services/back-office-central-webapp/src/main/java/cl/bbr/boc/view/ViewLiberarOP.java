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

/**
 * Servlet implementation class ViewLiberarOP
 */
public class ViewLiberarOP extends Command {
	private static final long serialVersionUID = 1L;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		   String param_mensaje_error="";
		   String mensaje="";
		
     logger.debug("User: " + usr.getLogin());
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		
		// 2. Procesa parámetros del request
		
		// 2.0 parámetro msg

		int origen = 1;
		if (req.getParameter("origen")!=null){
			origen = Integer.parseInt(req.getParameter("origen"));
		}
		String idPedido = "";
		if (req.getParameter("id_pedido")!=null){
			idPedido = req.getParameter("id_pedido");
		}
		logger.debug("origen:" + origen);
		String accionParam = "";
		if (req.getParameter("accionLOP")!=null){
			accionParam = req.getParameter("accionLOP");
		}
		logger.debug("accion:" + accionParam);

		

		// 3. Template (dejar tal cual)
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.0 Creamos al BizDelegator
		
		Date fecha = new Date();
		
		if ("pop".equals(accionParam)){
			mensaje="La orden de pedido fue liberada con exito.";
			top.setVariable("{mensaje}", mensaje);

		}else{
			top.setVariable("{mensaje}", "");						
		}
	
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_fecha}"	,fecha.toString());
		top.setVariable("{mensaje_error}",param_mensaje_error);
		top.setVariable("{id_pedido}"	, "");

		// 7. Salida Final
		

		
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	
	}

}
