package cl.bbr.boc.view;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * permite crear un nuevo perfil de usuario
 * @author BBRI
 */

public class ViewMarcaNewForm extends Command {
	private final static long serialVersionUID = 1;
	
	

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		View salida = new View(res);
		logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		BizDelegate bizDelegate = new BizDelegate();
		
		//ver estados
		ArrayList estados = new ArrayList();
		List lst_estados = bizDelegate.getEstadosByVis("ALL","S");

		for (int i = 0; i< lst_estados.size(); i++){
			IValueSet fila_tip = new ValueSet();
			EstadoDTO tip1 = (EstadoDTO)lst_estados.get(i);
			fila_tip.setVariable("{est_id}", String.valueOf(tip1.getId_estado()));
			fila_tip.setVariable("{est_nombre}"	, String.valueOf(tip1.getNombre()));
			
			estados.add(fila_tip);
			
		}
			
		// 6. Setea variables bloques
		
		top.setDynamicValueSets("ESTADOS", estados);
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());			
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}