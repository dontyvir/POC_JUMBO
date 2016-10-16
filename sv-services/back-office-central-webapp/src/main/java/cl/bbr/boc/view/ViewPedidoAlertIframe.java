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
import cl.bbr.jumbocl.pedidos.dto.AlertaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * iframe que muestra una alerta en el pedido
 * @author BBRI
 */
public class ViewPedidoAlertIframe extends Command{
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_pedido=0;

		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
				
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		//2. Procesa parámetros del request
			
		if ( req.getParameter("id_pedido") != null ){
			id_pedido =  Long.parseLong(req.getParameter("id_pedido"));
		}
		logger.debug("Este es el id_pedido que viene:" + id_pedido);
		
//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();	
		ArrayList alertas = new ArrayList();
		
			List lst_ale = bizDelegate.getAlertasPedidosById(id_pedido);
	
			logger.debug("lst_ale:"+lst_ale.size());
	
			for (int i = 0; i< lst_ale.size(); i++){
				IValueSet fila = new ValueSet();
				AlertaDTO ale1 = (AlertaDTO)lst_ale.get(i);
				
				fila.setVariable("{ale_id}"		, String.valueOf(ale1.getId_alerta()));
				fila.setVariable("{ale_nom}"	, String.valueOf(ale1.getNombre()));
				fila.setVariable("{ale_desc}"	, String.valueOf(ale1.getDescripcion()));
				fila.setVariable("{ale_tip}"	, String.valueOf(ale1.getTipo()));
				if(ale1.getTipo().equals("A"))
					fila.setVariable("{ale_img}"	, String.valueOf("img/icon4.gif"));
				else if(ale1.getTipo().equals("I"))
					fila.setVariable("{ale_img}"	, String.valueOf("img/icon41.gif"));
				alertas.add(fila);
			}
		
			
			if (lst_ale.size() == 0){
				top.setVariable("{mensaje}", "No existen Alertas asociadas" );
			}
			else{
				top.setVariable("{mensaje}", "" );
			}
			
		// 6. Setea variables bloques
		
	    top.setDynamicValueSets("ALERTAS", alertas);

	    String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
