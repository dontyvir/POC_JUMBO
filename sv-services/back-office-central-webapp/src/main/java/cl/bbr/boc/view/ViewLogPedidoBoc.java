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
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega un listado con los log's del pedido
 * @author BBRI
 */
public class ViewLogPedidoBoc extends Command {

	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		String 	param_id_pedido	= "";
		long	id_pedido		= -1;
        String pmod = "[PRECIO MODIFICADO]";

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

		// 3. Template (dejar tal cual)		
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
	
		// 4.  Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		// 4.1 Log
		List listaLog = new ArrayList();
		listaLog = bizDelegate.getLogPedido(id_pedido);
		ArrayList log = new ArrayList();
		for (int i=0; i<listaLog.size(); i++){			
			IValueSet fila = new ValueSet();
			LogPedidoDTO log1 = new LogPedidoDTO();
			log1 = (LogPedidoDTO)listaLog.get(i);
			fila.setVariable("{fecha}"		,Formatos.frmFechaHora(log1.getFecha()));
			if (log1.getLog().indexOf(pmod)>=0){
				fila.setVariable("{usuario}"	,"Admin");
			}
			else{
				fila.setVariable("{usuario}"	,log1.getUsuario());
			}
			fila.setVariable("{log}"		,log1.getLog());		
			if (log1.getId_motivo()>0){
				fila.setVariable("{motivo}", String.valueOf(log1.getId_motivo())+":"+log1.getMotivo());
			}else{
				fila.setVariable("{motivo}", "");
			}
			if (log1.getId_motivo_anterior()>0){
				fila.setVariable("{motivo_ant}", String.valueOf(log1.getId_motivo_anterior())+":"+ log1.getMotivo_anterior());
			}else{
				fila.setVariable("{motivo_ant}", "");
			}
			log.add(fila);
		}	
		// 5. Setea variables del template
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("select_log", log);
		// 7. Salida Final
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();
		
		
	
	}
}
