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
 * Despliega el home
 * @author bbr
 */
public class ViewHome extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("te: " + html);	
		
		// 2. Procesa parámetros del request
		// No Hay!!!
		
		
		// 3. Template
		View salida = new View(res);
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		// 4.  Rutinas Dinámicas
		// No hay!!!

		// 5. Setea variables del template
		// variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());		

		
		// 6. Setea variables bloques		
		// NO hay
		
		
		logger.debug("User: " + usr.getLogin());
		logger.debug("IdLocal: " + usr.getId_local());
		logger.debug("IdPerfil: " + usr.getId_perfil());
		
		
		// 7. Salida Final		
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();	
		
	}

}
