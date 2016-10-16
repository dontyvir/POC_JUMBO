package cl.bbr.bol.view;

import java.util.ArrayList;
import java.util.HashMap;
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
import cl.bbr.jumbocl.pedidos.dto.FacturasDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega las transacciones de pago
 * @author mleiva
 */
public class ViewActListFacturas extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		long   id_pedido = 0L;
		String mensaje   = "";
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);


		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		
		if ( req.getParameter("id_pedido") == null ){
			throw new ParametroObligatorioException("id_pedido es null");
		}
		
		id_pedido = Long.parseLong(req.getParameter("id_pedido"));
		logger.debug("id_pedido: " + id_pedido);
		
		if ( req.getParameter("mensaje") != null ){
		    mensaje = req.getParameter("mensaje");
		}
		
		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		
		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		List Facturas = new ArrayList();
		try {
		    HashMap listFact = bizDelegate.getFacturasIngresadas(id_pedido);
		    for (int i=1 ; i <= listFact.size(); i++ ){
		        IValueSet fila = new ValueSet();
		        FacturasDTO fact = (FacturasDTO)listFact.get(""+i);
		        fila.setVariable("{orden}", ""+fact.getOrden());
		        fila.setVariable("{id_pedido}", ""+fact.getId_pedido());
		        fila.setVariable("{num_doc}", ""+fact.getNum_doc());
		        fila.setVariable("{fingreso}", fact.getFingreso());
		        Facturas.add(fila);
		    }
		} catch (Exception ex){
			logger.debug("biz Error:"+ex.getMessage());
		}
		
		PedidoDTO pedido = new PedidoDTO();
		pedido = bizDelegate.getPedido(id_pedido);
        
		
		// 5. Setea variables del template
		    
		top.setVariable("{id_pedido}", ""+id_pedido);
		top.setVariable("{rsocial}", pedido.getNom_cliente());
		top.setVariable("{mensaje}", mensaje);
		
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("Facturas", Facturas);
		
		
		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	
	}
	
	
	
}
