package cl.bbr.bol.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Muestra formulario para cambiar datos a los sustitutos con cambio de formato.
 * @author mleiva
 */
public class ViewSustitutosCambioFormato extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String 	param_id_detalle	= "";
		long	id_detalle			= -1;
		String mensaje = "";
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		String html_error01 = getServletConfig().getInitParameter("TplHtmlError01");
		html_error01 = path_html + html_error01;
		logger.debug("Template Error: " + html_error01);
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		if ( req.getParameter("id_detalle") == null ){
			throw new ParametroObligatorioException("id_detalle es null");
		}
		
		param_id_detalle	= req.getParameter("id_detalle");
		id_detalle = Long.parseLong(param_id_detalle);	
		logger.debug("id_detalle: " + param_id_detalle);
		if(req.getParameter("mensaje")!=null)
			mensaje = req.getParameter("mensaje");
		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		
		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		
		DetallePickingDTO dto = bizDelegate.getDetallePickingById(id_detalle);
		top.setVariable("{c_barra}"	,dto.getCBarra());
		top.setVariable("{descripcion}"	,dto.getDescripcion());
		top.setVariable("{precio}"	,String.valueOf(dto.getPrecio()));
		
		// Validamos que tenga el estado "En bodega", sólo en estos
		// estados se permite imprimir esta orden 
		
		// 5. Setea variables del template
			top.setVariable("{id_detalle}"	,param_id_detalle);
			if(!mensaje.equals(" ") || mensaje!=null)
				top.setVariable("{mensaje}"		,mensaje);
			else
				top.setVariable("{mensaje}"		," ");
		// 6. Setea variables bloques

		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	
	}
	
	
	
}
