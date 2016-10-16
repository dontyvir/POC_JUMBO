package cl.bbr.bol.view;

import java.util.Date;
import java.util.ResourceBundle;

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
 * Muestra formulario para Crear Zona de Despacho
 * @author jsepulveda
 */
public class ViewCrearZonaDespacho extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		ResourceBundle rb = ResourceBundle.getBundle("bo");
		String descuento = rb.getString("monto.descuento.despacho");

		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);		

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator

		// 4.x genera Listados

		// 5. Setea variables del template
		top.setVariable("{nombre}","");
		top.setVariable("{desc}","");
		top.setVariable("{tarifa_normal_alta}","");
		top.setVariable("{tarifa_normal_media}","");
		top.setVariable("{tarifa_normal_baja}","");
		top.setVariable("{tarifa_economica}","");
		top.setVariable("{tarifa_express}","");
		top.setVariable("{estado_tarifa_economica}","");
		top.setVariable("{estado_tarifa_express}","");
		top.setVariable("{estado_tarifa_express}","");
		top.setVariable("{msg_cal_despacho}","");
		top.setVariable("{orden}", "");
		top.setVariable("{regalo_clientes}", "");
		top.setVariable("{estado_descuento_cat}", "");
		top.setVariable("{estado_descuento_tbk}", "");
		top.setVariable("{estado_descuento_par}", "");
		top.setVariable("{monto_descuento_cat}", descuento);
		top.setVariable("{monto_descuento_tbk}", descuento);
		top.setVariable("{monto_descuento_par}", descuento);
		top.setVariable("{monto_descuento_pc_cat}", descuento);
		top.setVariable("{monto_descuento_pc_tbk}", descuento);
		top.setVariable("{monto_descuento_pc_par}", descuento);

		// 6. Setea variables bloques
		
		
		// 7. variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
		top.setVariable("{MontoMinimo}", descuento);
		
		// 8. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}
}
