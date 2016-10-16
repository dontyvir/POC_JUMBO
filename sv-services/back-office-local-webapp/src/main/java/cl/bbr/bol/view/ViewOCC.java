package cl.bbr.bol.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * @deprecated Desplegaba las transacciones de pago
 * @author bbri
 *
 */
public class ViewOCC extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String 	param_id_pedido	= "";
		long	id_pedido		= -1;
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		String html_error01 = getServletConfig().getInitParameter("TplHtmlError01");
		html_error01 = path_html + html_error01;
		logger.debug("Template Error: " + html_error01);
		
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

		
		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		PedidoDTO pedido = bizDelegate.getPedido(id_pedido);
		
		// Validamos que tenga el estado "En bodega", sólo en estos
		// estados se permite imprimir esta orden 
		
		if ( pedido.getId_estado() != Constantes.ID_ESTAD_PEDIDO_EN_BODEGA ){
			
			logger.debug("Error, el pedido está en un estado que no permite generar la OCC.");
			
			TemplateLoader load2 = new TemplateLoader(html_error01);
			ITemplate tem2 = load2.getTemplate();
			IValueSet top2 = new ValueSet();
			String result2 = tem2.toString(top2);
			salida.setHtmlOut(result2);
			salida.Output();
			return;
			
		}
		
		
		
		// 5. Setea variables del template
		// Si es Boleta, el prefijo es 20 , si es factura es el 21
		String prefijo = "";
		if ( pedido.getTipo_doc().equals("F") )
			prefijo = "21";
		else
			prefijo = "20";
		top.setVariable("{id_pedido}"	, param_id_pedido);
		String codigo_barra = prefijo;
		for(int i = param_id_pedido.length();i<=9;i++){
			codigo_barra +="0";
			if(i==9){
			codigo_barra +=param_id_pedido;	
			}				
		}		
		top.setVariable("{cod_barra}"	,codigo_barra);
		top.setVariable("{monto}"		,String.valueOf(pedido.getMonto()));
		if(pedido.getTipo_doc().equals(Constantes.TIPO_DOC_BOLETA)){
			top.setVariable("{tipo_doc}"	,"BOLETA");	
		}if(pedido.getTipo_doc().equals(Constantes.TIPO_DOC_FACTURA)){
			top.setVariable("{tipo_doc}"	,"FACTURA");	
		}	
		top.setVariable("{cant_prod}"	,String.valueOf(pedido.getCant_prods()));
		top.setVariable("{forma_pago}"	,pedido.getMedio_pago());	
		top.setVariable("{nom_cliente}"	,pedido.getNom_cliente());	
		top.setVariable("{f_despacho}"	,pedido.getFdespacho());	
		top.setVariable("{h_ini}"		,pedido.getHdespacho());		
		top.setVariable("{h_fin}"		,pedido.getHfindespacho());		
			
		
		
		// 6. Setea variables bloques

		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	
	}
	
	
	
}
