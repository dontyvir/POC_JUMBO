package cl.bbr.bol.view;

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
 * despliega la vista para agregar sectores
 * @author bbr
 *
 */

public class ViewAgregaSector extends Command {

 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr)
         throws Exception {

     
//	 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
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
		top.setVariable("{nom_local}",usr.getLocal());
		// 6. Setea variables bloques

		// 7. variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
		top.setVariable("{max_prod}"	,"0");
		top.setVariable("{max_op}"	,"0");
		top.setVariable("{min_op_fill}"	,"0");
		top.setVariable("{cant_min_prods}"	,"0");
		
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
		

 }//execute

}
