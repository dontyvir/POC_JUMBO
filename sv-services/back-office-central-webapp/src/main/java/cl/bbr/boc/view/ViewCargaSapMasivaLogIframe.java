package cl.bbr.boc.view;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.CargaSapMasivaLogDTO;
import cl.bbr.jumbocl.contenidos.dto.CatalogacionMasivaLogDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoLogDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * iframe que despliega el log del producto
 * @author BBRI
 */
public class ViewCargaSapMasivaLogIframe extends Command{
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		
		HttpSession session = null;
		
		session = req.getSession();
		
		List productosCargaSapMasiva = new ArrayList();
		
		List lst_log = new ArrayList();
		
		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
				
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		//2. Procesa parámetros del request
			
		
//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();	
		ArrayList logs = new ArrayList();
		
		if (session.getAttribute("listaProductosCargaSapMasiva") != null) {
				
	    	productosCargaSapMasiva = (List)session.getAttribute("listaProductosCargaSapMasiva");
		
		}
		
		if (productosCargaSapMasiva.size() != 0) {
			
			lst_log = bizDelegate.getLogByProductIdSap(productosCargaSapMasiva);
			
			logger.debug("lst_log:"+lst_log.size());
	
			for (int i = 0; i< lst_log.size(); i++){
				IValueSet fila_log = new ValueSet();
				CargaSapMasivaLogDTO log1 = (CargaSapMasivaLogDTO)lst_log.get(i);
				
				fila_log.setVariable("{log_fec}", Formatos.frmFechaHora(String.valueOf(log1.getFec_crea())));
				fila_log.setVariable("{log_usu}"	, String.valueOf(log1.getUsuario()));
				fila_log.setVariable("{log_des}"	, String.valueOf(log1.getTexto()));
				logs.add(fila_log);
			}
			
		}
			
		
			
			if (lst_log.size() == 0){
				top.setVariable("{mensaje}", "No existen Logs asociados" );
			}
			else{
				top.setVariable("{mensaje}", "" );
			}
			
		// 6. Setea variables bloques
		
	    top.setDynamicValueSets("LOGS", logs);

	    String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
