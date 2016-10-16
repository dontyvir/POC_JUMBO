package cl.bbr.boc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.CalculosEAN;
import cl.bbr.jumbocl.pedidos.dto.CuponPagoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega los datos de una transacción de pago
 * @author BBRI
 */
public class ViewTrxMp extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String 	param_id_trx_mp	= "";
		long	id_trx_mp		= -1;
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
		
		String nota_fact = getServletConfig().getInitParameter("TxtNotaFacturas");
		logger.debug("Nota de facturas: " + nota_fact);
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		if ( req.getParameter("id_trx_mp") == null ){
			throw new ParametroObligatorioException("id_trx_mp es null");
		}
		
		param_id_trx_mp	= req.getParameter("id_trx_mp");
		id_trx_mp = Long.parseLong(param_id_trx_mp);	
		logger.debug("id_trx_mp: " + param_id_trx_mp);

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
		CuponPagoDTO transaccion = new CuponPagoDTO();
		try {
		transaccion = bizDelegate.getCuponPago(id_pedido, id_trx_mp);
		} catch (Exception ex){
			logger.debug("biz Error:"+ex.getMessage());
		}
		
		
		// Validamos que tenga el estado "En bodega", sólo en estos
		// estados se permite imprimir esta orden 
		
		/*if ( pedido.getId_estado() != Constantes.ID_ESTAD_PEDIDO_EN_PAGO ){
			
			logger.debug("Error, el pedido está en un estado en que aun no tiene transacciones de pago.");
			
			TemplateLoader load2 = new TemplateLoader(html_error01);
			ITemplate tem2 = load2.getTemplate();
			IValueSet top2 = new ValueSet();
			String result2 = tem2.toString(top2);
			salida.setHtmlOut(result2);
			salida.Output();
			return;
			
		}*/
		
		
		
		// 5. Setea variables del template
		// Si es Boleta, el prefijo es 20 , si es factura es el 21
	    String prefijo = "";
	    String tipo_Doc = "";
	    logger.debug("antes de verificar constantes");
	    logger.debug("doc_factura:"+Constantes.TIPO_DOC_FACTURA);
	    logger.debug("trx.gettipo:"+transaccion.getTipoDocPago());
	    
		if ( transaccion.getTipoDocPago().equals(Constantes.TIPO_DOC_FACTURA) ){
			prefijo = "21";
			tipo_Doc = "FACTURA";
			top.setVariable("{notas_fact}",nota_fact+" ");
		}		
		else{
			prefijo = "20";
			tipo_Doc = "BOLETA";
			top.setVariable("{notas_fact}"," ");
		}	
		
		top.setVariable("{id_pedido}"	, param_id_pedido);
		String codigo_barra = prefijo;
		for(int i = param_id_trx_mp.length();i<=9;i++){
			codigo_barra +="0";
			if(i==9){
			codigo_barra +=param_id_trx_mp;	
			}				
		}		
		String dig_ver = CalculosEAN.getEAN13dv(codigo_barra);
		top.setVariable("{cod_barra}"	,codigo_barra+dig_ver);
		top.setVariable("{monto}"		,String.valueOf(transaccion.getMonto_trxmp()));		
		top.setVariable("{tipo_doc}"	,tipo_Doc);		
		top.setVariable("{cant_prod}"	,String.valueOf(transaccion.getCant_prods()));			
		top.setVariable("{nom_cliente}"	,transaccion.getNom_cliente());	
		top.setVariable("{f_despacho}"	,transaccion.getFdespacho());	
		top.setVariable("{ventana_desp}",transaccion.getVentanaDespacho());

		top.setVariable("{id_trx_mp}"	,String.valueOf(transaccion.getId_trxmp()));
		
		// 6. Setea variables bloques

		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	
	}
	
	
	
}
