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
import cl.bbr.jumbocl.usuarios.dto.LocalDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * iframe que despliega los datos de usuarios por perfiles
 * @author BBRI
 */
public class ViewUsrLocalIframe extends Command{
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long usu_cod=0;
		String rc = "";
		String msg = "";

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
		logger.debug("*** usu_cod :" + usu_cod);

		if(req.getParameter("rc")!=null)
			rc = req.getParameter("rc");
		logger.debug("rc:"+rc);
		if(req.getParameter("msg")!=null)
			msg = req.getParameter("msg");
		logger.debug("msg:"+msg);
		
//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();	

		//locales
		ArrayList locales = new ArrayList();
		UserDTO user = bizDelegate.getUserById(usu_cod);
		
		List listLocales = user.getLocales();
		for (int i = 0; i < listLocales.size(); i++) {
			IValueSet fila_loc = new ValueSet();
			LocalDTO loc = (LocalDTO) listLocales.get(i);
			fila_loc.setVariable("{loc_id}", String.valueOf(loc.getId_local()));
			fila_loc.setVariable("{loc_nom}", String.valueOf(loc.getNom_local()));
			fila_loc.setVariable("{usu_cod}"	, String.valueOf(usu_cod) );
			locales.add(fila_loc);
		}
		if (listLocales.size() == 0){
			top.setVariable("{mensaje}", "No existen Locales asociados" );
		}
		else{
			top.setVariable("{mensaje}", "" );
		}
			
		// 6. Setea variables bloques
	    top.setDynamicValueSets("LOCALES", locales);

	    if(!msg.equals("")){
	    	top.setVariable("{msg}"	,"<script> alert('"+msg+"'); </script>");
	    }else{
	    	top.setVariable("{msg}"	,"");
	    }
		
	    String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
