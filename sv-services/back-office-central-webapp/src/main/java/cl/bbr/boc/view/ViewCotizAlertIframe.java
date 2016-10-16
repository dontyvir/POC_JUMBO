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
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dto.AlertaDTO;
/**
 * iframe que muestra una alerta en el pedido
 * @author BBRI
 */
public class ViewCotizAlertIframe extends Command{
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_cot=0;

		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
				
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		//2. Procesa parámetros del request
			
		if ( req.getParameter("cot_id") != null ){
			id_cot =  Long.parseLong(req.getParameter("cot_id"));
		}
		logger.debug("Este es el id_cot que viene:" + id_cot);
		
//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();	
		ArrayList alertas = new ArrayList();
		
			List lst_ale = bizDelegate.getAlertasCotizacion(id_cot);
	
			logger.debug("lst_ale:"+lst_ale.size());
	
			for (int i = 0; i< lst_ale.size(); i++){
				IValueSet fila = new ValueSet();
				AlertaDTO ale1 = (AlertaDTO)lst_ale.get(i);
				
				fila.setVariable("{ale_id}"		, String.valueOf(ale1.getAle_id()));
				fila.setVariable("{ale_nom}"	, String.valueOf(ale1.getAle_nom()));
				fila.setVariable("{ale_desc}"	, String.valueOf(ale1.getAle_descr()));
				if(ale1.getAle_tipo().equals("A"))
					fila.setVariable("{ale_img}"	, String.valueOf("img/icon4.gif"));
				else if(ale1.getAle_tipo().equals("I"))
					fila.setVariable("{ale_img}"	, String.valueOf("img/icon41.gif"));
				alertas.add(fila);
			}
		
			
			if (lst_ale.size() == 0){
				top.setVariable("{mensaje}", "No existen Alertas asociadas" );
			}
			else{
				top.setVariable("{mensaje}", " " );
			}
			
		// 6. Setea variables bloques
		
	    top.setDynamicValueSets("ALERTAS_COT", alertas);

	    String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
