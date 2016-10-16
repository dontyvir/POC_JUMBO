package cl.bbr.boc.view;

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
 * despliega formulario para editar la indicación de un pedido
 * @author BBRI
 */

public class ViewDespIndForm extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int id_pedido=0;
		String indicacion="";
		String obs="";
		boolean edicion = false;
		
		
		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
		
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		if ( req.getParameter("id_pedido") != null )
			id_pedido = Integer.parseInt( req.getParameter("id_pedido") );
		logger.debug("id_pedido:"+id_pedido);

		
		
		// Modo de edición
		if ( usr.getId_pedido() == id_pedido ){
			edicion = true;
		}
		
		
		BizDelegate biz = new BizDelegate();
		indicacion = biz.getPedidosById(id_pedido).getIndicacion();
		if (biz.getPedidosById(id_pedido).getObservacion() != null && !biz.getPedidosById(id_pedido).getObservacion().equals("null"))
			obs = biz.getPedidosById(id_pedido).getObservacion();
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		top.setVariable("{id_pedido}"		,	String.valueOf(id_pedido));
		top.setVariable("{indicacion}"		,	String.valueOf(indicacion));
		top.setVariable("{obs}"		,	String.valueOf(obs));
		top.setVariable("{mod}"		,	"");
		
		String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}