package cl.bbr.boc.view;

import java.util.ArrayList;
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
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * iframe que despliega los datos de usuarios por perfiles
 * @author BBRI
 */
public class ViewUsrPerfilIframe extends Command{
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long usu_cod=0;

		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
				
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		//2. Procesa parámetros del request
			
		if ( req.getParameter("usu_cod") != null ){
			usu_cod =  Long.parseLong(req.getParameter("usu_cod"));
		}
		logger.debug("Este es el usu_cod que viene:" + usu_cod);
		
//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();	
		List lst_perf = null;

		lst_perf =  bizDelegate.getUserById(usu_cod).getPerfiles();
			ArrayList perfiles = new ArrayList();
			
			for (int i = 0; i < lst_perf.size(); i++) {			
				IValueSet fila_tip = new ValueSet();
				PerfilesEntity tip1 = (PerfilesEntity)lst_perf.get(i);
				fila_tip.setVariable("{per_id}", String.valueOf(tip1.getIdPerfil()));
				fila_tip.setVariable("{per_des}"	, String.valueOf(tip1.getNombre()));
				fila_tip.setVariable("{usu_cod}"	, String.valueOf(usu_cod) );
				perfiles.add(fila_tip);
			}		
			
			if (lst_perf.size() == 0){
				top.setVariable("{mensaje}", "No existen Perfiles asociados" );
			}
			else{
				top.setVariable("{mensaje}", "" );
			}
			
		// 6. Setea variables bloques
		
	    top.setDynamicValueSets("PERFILES", perfiles);

	    String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
