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
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.CalculosEAN;
import cl.bbr.jumbocl.pedidos.dto.CuponPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.TrxMpDetalleDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega las transacciones de pago
 * @author mleiva
 */
public class ViewTrxMpDetalle extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String 	param_id_trx_mp	= "";
		long	id_trx_mp		= -1;
		String 	param_id_pedido	= "";
		long	id_pedido		= -1;
		String paramRc ="";
		String paramMsgRc="";
		
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

		if ( req.getParameter("id_trx_mp") == null ){
			throw new ParametroObligatorioException("id_trx_mp es null");
		}
		
		param_id_trx_mp	= req.getParameter("id_trx_mp");
		id_trx_mp = Long.parseLong(param_id_trx_mp);	
		logger.debug("id_trx_mp: " + param_id_trx_mp);

		if ( req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es null");}		
		param_id_pedido	= req.getParameter("id_pedido");
		id_pedido = Long.parseLong(param_id_pedido);	
		logger.debug("id_pedido: " + param_id_pedido);
		
		int Editable = Integer.parseInt(req.getParameter("edit")==null?"1":req.getParameter("edit"));
		
		paramRc	= req.getParameter("rc");
		logger.debug("rc: " + paramRc);
		
		paramMsgRc	= req.getParameter("mensaje_rc");
		logger.debug("mensaje_rc: " + paramMsgRc);
		
		
		
		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		
		// 4. Rutinas Dinámicas
		if (paramRc== null){
			top.setVariable("{mensaje_rc}", "");
		}else{
			top.setVariable("{mensaje_rc}", paramMsgRc);
		}
		
		
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		CuponPagoDTO transaccion = new CuponPagoDTO();
		try {
		    transaccion = bizDelegate.getCuponPago(id_pedido, id_trx_mp);
		} catch (Exception ex){
			logger.debug("biz Error:"+ex.getMessage());
		}
		
		PedidoDTO pedido = new PedidoDTO();		
		pedido = bizDelegate.getPedido(id_pedido);
	
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
		ArrayList lista_dets = new ArrayList();
		List detalles = new ArrayList();		
		detalles=bizDelegate.getTrxPagoDetalleByIdTrxMp(id_trx_mp);
		for (int i = 0; i<detalles.size();i++){
			IValueSet fila = new ValueSet();
			TrxMpDetalleDTO det = new TrxMpDetalleDTO();
			det = (TrxMpDetalleDTO) detalles.get(i);
			fila.setVariable("{indice}",String.valueOf(i));
			fila.setVariable("{url}","ViewTrxMpDetalle");
			fila.setVariable("{td_idtrxdet}",String.valueOf(det.getId_trxdet()));
		    fila.setVariable("{td_barra}",det.getCod_barra());
		    fila.setVariable("{td_desc}",det.getDescripcion());
		    fila.setVariable("{td_idtrxmp}",String.valueOf(det.getId_trxmp()));
		    fila.setVariable("{td_idprod}",String.valueOf(det.getId_producto()));
		    fila.setVariable("{td_qty}",String.valueOf(det.getCantidad()));
		    fila.setVariable("{td_pr}",String.valueOf(det.getPrecio()));
		    fila.setVariable("{td_idpedido}",String.valueOf(det.getId_pedido()));
		    fila.setVariable("{td_iddet}",String.valueOf(det.getId_detalle()));
		    
		    //Desabilita la edición del Código de Barra
			//Es posible editar solo desde el Detalle del Pedido y 
			//NO desde el Monitor de Pagos
		    if(Editable == 0){
		        fila.setVariable("{hab_EditCodBarra}", "disabled");
		    }else{
		        fila.setVariable("{hab_EditCodBarra}", "");
		    }
			lista_dets.add(fila);
		}
		
		//Oculta el Boton de Modificación  (0 => No Editable)
		//Es posible editar solo desde el Detalle del Pedido y 
		//NO desde el Monitor de Pagos
	    if(Editable == 0){
	        top.setVariable("{OcultaBotModif_izq}", "<!--");
	        top.setVariable("{OcultaBotModif_der}", "-->");
	    }else{
	        top.setVariable("{OcultaBotModif_izq}", "");
	        top.setVariable("{OcultaBotModif_der}", "");
	    }
        
		
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
		}else{
			prefijo = "20";
			tipo_Doc = "BOLETA";
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
		String trans = String.valueOf(transaccion.getId_trxmp());		
		String corr_trans = trans.substring(param_id_pedido.length(),param_id_pedido.length()+2);		
		logger.debug("corr_trans"+corr_trans);
		top.setVariable("{id_trx_mp}", String.valueOf(id_trx_mp));
		top.setVariable("{trx_mp}"	,param_id_pedido+"-"+corr_trans);
		top.setVariable("{observaciones}", pedido.getObservacion()+" ");
		top.setVariable("{url}","ViewTrxMpDetalle");
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("listado_dets", lista_dets);
		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}
}