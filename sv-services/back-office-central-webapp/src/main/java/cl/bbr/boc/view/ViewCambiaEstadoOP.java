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
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * despliega formulario que permite anular una op por algún motivo 
 * @author BBRI
 */
public class ViewCambiaEstadoOP extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_pedido=0;
		String mensaje_retorno="";
		String mensaje_opcion = "";
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
		
		if ( req.getParameter("mensaje") != null ){mensaje_retorno = req.getParameter("mensaje");}
		logger.debug("mensaje_retorno:" + mensaje_retorno );
		
		if ( req.getParameter("mensaje2") != null ){mensaje_opcion = req.getParameter("mensaje2");}
		logger.debug("mensaje_opcion:" + mensaje_opcion );

		if ( req.getParameter("ret") != null ){retorno = Integer.parseInt(req.getParameter("ret"));}
		logger.debug("retorno:" + retorno);

		
//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();
		
		EstadoDTO estado = bizDelegate.getEstadoPedido(id_pedido);
		
        if (estado.getId_estado() == 6 ||
                estado.getId_estado() == 21 ){
            mensaje_opcion = "Esta seguro(a) que desea cambiar el estado del pedido de <b>'" + estado.getNombre() + "'</b> a <b>'Bodega'</b>.<br><br>";
            mensaje_opcion += "<input type='hidden' name='new_estado' value='7'>";
        }else if (estado.getId_estado() == 8 ||
		        estado.getId_estado() == 21 ){
		    mensaje_opcion = "Esta seguro(a) que desea cambiar el estado del pedido de <b>'" + estado.getNombre() + "'</b> a <b>'Bodega'</b>.<br><br>";
		    mensaje_opcion += "<input type='hidden' name='new_estado' value='7'>";
		}
//        else if (estado.getId_estado() == 54){
//		    mensaje_opcion = "Esta seguro(a) que desea cambiar el estado del pedido de <b>'" + estado.getNombre() + "'</b> a <br><br>";
//		    mensaje_opcion += "<input name='new_estado' type='radio' value='8' checked='checked' /> Pagado  &nbsp;&nbsp;&nbsp;&nbsp;  <input name='new_estado' type='radio' value='7' /> en Bodega<br><br>";
//		}
		
		// 5. variables globales
		top.setVariable("{id_pedido}", String.valueOf(id_pedido));
		top.setVariable("{id_estado_actual}", estado.getId_estado()+"");
		top.setVariable("{mensaje}", mensaje_retorno);
		top.setVariable("{mensaje2}", mensaje_opcion);
		
		if (retorno==1)
		    top.setVariable("{hab}", "disabled");
		else
		    top.setVariable("{hab}", "enabled");
		
		
		// 7. Salida Final
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}