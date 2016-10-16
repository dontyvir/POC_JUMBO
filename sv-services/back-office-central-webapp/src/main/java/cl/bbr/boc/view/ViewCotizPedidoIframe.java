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
import cl.bbr.vte.cotizaciones.dto.PedidosCotizacionesDTO;

/**
 * iframe que despliega los productos del pedido
 * @author BBRI
 */
public class ViewCotizPedidoIframe extends Command{
	
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
			
		if ( req.getParameter("id_cot") != null ){
			id_cot =  Long.parseLong(req.getParameter("id_cot"));
		}
		
		logger.debug("Este es el id_cot que viene:" + id_cot);
		

//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		
		// Modo de edición
		//if ( usr.getId_pedido() == id_pedido ){
			//edicion = true;
		//}
		
		
		// 4.0 Bizdelegator 
		
		
		BizDelegate bizDelegate = new BizDelegate();	
		List listaPedidos = null;

		listaPedidos =  bizDelegate.getPedidosCotiz(id_cot);
		

		ArrayList pedido = new ArrayList();

			
			for (int i=0; i<listaPedidos.size(); i++){			
				IValueSet fila = new ValueSet();
				PedidosCotizacionesDTO ped = new PedidosCotizacionesDTO();
				ped = (PedidosCotizacionesDTO)listaPedidos.get(i);
				
				fila.setVariable("{ped_id}"	   ,String.valueOf(ped.getPed_id()));
				fila.setVariable("{ped_fec}"   ,ped.getFec_pedido());
				fila.setVariable("{desp_fec}"  ,ped.getFec_despacho());
				fila.setVariable("{local}"	   ,ped.getLocal());
				fila.setVariable("{costo_desp}",String.valueOf(ped.getCosto_desp()));
				fila.setVariable("{cant_falt}" ,String.valueOf(ped.getCant_falt()));
				fila.setVariable("{estado}"	   ,String.valueOf(ped.getEstado()));
				
				
				pedido.add(fila);
			}
			
			if (listaPedidos.size() == 0){
				top.setVariable("{mensaje}", "No existen Productos asociados" );
			}
			else{
				top.setVariable("{mensaje}", "" );
			}
		
	
			
		// 6. Setea variables bloques
		
			top.setDynamicValueSets("PED_COT", pedido);

	    String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
