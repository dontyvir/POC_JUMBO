package cl.bbr.bol.view;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.DespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Despliega formulario para cambiar de estado un pedido
 * @author mleiva
 */
 public class ViewCambEstDesp extends Command {

	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String	param_id_pedido = "";
		long	id_pedido		=-1;
		String 	mensaje 		= "";
		
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		if ( req.getParameter("id_pedido") == null ){
			throw new ParametroObligatorioException("id_pedido es null");
		}
		
		param_id_pedido	= req.getParameter("id_pedido");
		id_pedido = Long.parseLong(param_id_pedido);	
		logger.debug("id_pedido: " + param_id_pedido);
		
		if ( req.getParameter("mensaje") != null ){
			mensaje	= req.getParameter("mensaje");
		}	
		logger.debug("MENSAJE: "+mensaje);
		
		
		
		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		DespachoDTO despacho = bizDelegate.getDespachoById(id_pedido);
		List listaEstados = new ArrayList();
		listaEstados = bizDelegate.getEstadosDespacho();
		ArrayList estado = new ArrayList();	
		for (int i=0; i<listaEstados.size(); i++){			
			IValueSet fila = new ValueSet();
			EstadoDTO estado1 = new EstadoDTO();
			estado1 = (EstadoDTO)listaEstados.get(i);
			fila.setVariable("{id_estado}"		,String.valueOf(estado1.getId_estado()));
			fila.setVariable("{est_nuevo}"		,estado1.getNombre());
			if(despacho.getEstado()==estado1.getNombre()){
				fila.setVariable("sel", "selected");
			}else{
				fila.setVariable("sel", "");				
			}
			
			estado.add(fila);
		}
		
		// 5. Setea variables del template
			top.setVariable("{id_pedido}", param_id_pedido);
			
			top.setVariable("{est_actual}",despacho.getEstado());
			if(mensaje != null)
				top.setVariable("{mensaje}",mensaje);
			else
				top.setVariable("{mensaje}","");
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("select_estado", estado);
		
		
		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
		
		
	
	}
}
