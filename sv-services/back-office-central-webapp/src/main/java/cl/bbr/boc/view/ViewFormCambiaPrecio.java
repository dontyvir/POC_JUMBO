package cl.bbr.boc.view;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Permite cambiar los precios de un pedido pickeado
 * @author imoyano
 */
public class ViewFormCambiaPrecio extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -635070932535536731L;


	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		  if(req.getParameter("id_pedido") == null)
	        	throw new ParametroObligatorioException("Faltan id pedido.");	        
	        
	        long idPedido = Long.parseLong(req.getParameter("id_pedido"));
	        
	        BizDelegate biz = new BizDelegate();
	        StringBuffer bufferOutHtml = new StringBuffer();
		
	      if("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))){
	    	  
			if (usr != null) {
				String accion = req.getParameter("accion");
				if ("verExcesos".equals(accion)) {
					if (!biz.isOpConExceso(idPedido)) {
						biz.modPedidoExcedido(idPedido, false);
						bufferOutHtml.append("<h3>&nbsp;&nbsp;&nbsp;<img src=\"img/tick.png\" border=\"0\">&nbsp;OP no registra excesos.</h3>");
					} else{
						if(biz.isExcesoCorreccionAutomatico(idPedido)){
							bufferOutHtml.append("<h3>&nbsp;&nbsp;&nbsp;<img src=\"img/tick.png\" border=\"0\">&nbsp;OP registra excesos que se pueden corregir automaticamente.</h3>");		        	
						}else {
							List idsDetalleSolicitadoConExceso = biz.getIdDetalleSolicitadoConExceso(idPedido);
							Iterator it = idsDetalleSolicitadoConExceso.iterator();
	
							while (it.hasNext()) {
								long idDetalle = Long.parseLong(String.valueOf(it.next()));
								ProductosPedidoDTO oProductosPedidoDTO = biz.getDetalleProductoSolicitado(idPedido,idDetalle);
								List detallePickingByIdDetalle = biz.getDetallePickingByIdDetalle(idDetalle,idPedido);
								bufferOutHtml.append(getHtmlHelper(oProductosPedidoDTO,detallePickingByIdDetalle,true));
							}
						}
		        	}
				} else if ("verReporte".equals(accion)) {
					List reporte = biz.getTrackingExcesoByOP(idPedido);
					if (reporte.size() > 0) {
						bufferOutHtml.append(getHtmlLogHelper(reporte));						
					}else{
						bufferOutHtml.append("<h3>&nbsp;&nbsp;&nbsp;<img src=\"img/tick.png\" border=\"0\">&nbsp;Log del pedido sin registros de excesos.</h3>");
					}

				} else if ("verPicking".equals(accion)) {
					List productosPedido = biz.getProductosPedidosById(idPedido);
					Iterator it = productosPedido.iterator();

					while (it.hasNext()) {
						ProductosPedidoDTO oProductosPedidoDTO = (ProductosPedidoDTO) it.next();
						List detallePickingByIdDetalle = biz.getDetallePickingByIdDetalle(oProductosPedidoDTO.getId_detalle(),idPedido);
						bufferOutHtml.append(getHtmlHelper(oProductosPedidoDTO,detallePickingByIdDetalle,false));
					}
				} else {

				}
			} else {
				bufferOutHtml.append("<script>location.href='/JumboBOCentral'</script>");
			}
			
			res.setContentType("text/html");
			PrintWriter out = res.getWriter();
			out.println(bufferOutHtml.toString());
		}else{        	        
	        boolean isOpConExceso=true;
	        if(!biz.isOpConExceso(idPedido)){
	        	isOpConExceso=false;
	        	biz.modPedidoExcedido(idPedido, isOpConExceso);
	        	bufferOutHtml.append("<h3>&nbsp;&nbsp;&nbsp;<img src=\"img/tick.png\" border=\"0\">&nbsp;OP no registra excesos.</h3>");
	        }else{	        
	        	if(biz.isExcesoCorreccionAutomatico(idPedido)){
	        		bufferOutHtml.append("<h3>&nbsp;&nbsp;&nbsp;<img src=\"img/tick.png\" border=\"0\">&nbsp;OP registra excesos que se pueden corregir automaticamente.</h3>");
	        	}else{
			        List idsDetalleSolicitadoConExceso = biz.getIdDetalleSolicitadoConExceso(idPedido);
			               
			        Iterator it = idsDetalleSolicitadoConExceso.iterator();              
					while (it.hasNext()) {
						long idDetalle = Long.parseLong(String.valueOf(it.next()));
						ProductosPedidoDTO oProductosPedidoDTO = biz.getDetalleProductoSolicitado(idPedido, idDetalle);
						List detallePickingByIdDetalle = biz.getDetallePickingByIdDetalle(idDetalle, idPedido);
						bufferOutHtml.append(getHtmlHelper(oProductosPedidoDTO, detallePickingByIdDetalle,true));
					}
	        	}
	        }
	             
	        Date now = new Date();       
	        
	        View salida = new View(res);    	
	        String html = getServletConfig().getInitParameter("TplFile");
	        html = path_html + html;
	                
	        TemplateLoader load = new TemplateLoader(html);
	        ITemplate tem = load.getTemplate();
	        IValueSet top = new ValueSet();
	               
	        top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());        
	        top.setVariable("{hdr_fecha}", now.toString());
	        top.setVariable("{id_pedido}", String.valueOf(idPedido)); 
	        top.setVariable("{bufferoutHtml}", bufferOutHtml.toString());
	        if(isOpConExceso){
	        	  top.setVariable("{isOpConExceso}", "1");	        	
	        }else{
	        	  top.setVariable("{isOpConExceso}", "0");
	        }
	
	        String result = tem.toString(top);
	        salida.setHtmlOut(result);
	        salida.Output();			
		}
	}

	private String getHtmlHelper(ProductosPedidoDTO oProductosPedidoDTO, List detallePickingByIdDetalle, boolean activaBotonera) throws BocException {
			
		StringBuffer html = new StringBuffer();  
		
		html.append("<table width=\"770\" cellspacing=\"1\" cellpadding=\"3\" align=\"center\" id=\""+oProductosPedidoDTO.getId_detalle()+"\">");
		html.append("<thead>");
		html.append("<tr class=\"thead_tr1\"><th class=\"thead_td1\"  colspan=\"6\">ID_DETALLE: "+oProductosPedidoDTO.getId_detalle()+"</th></tr>");
		html.append("<tr class=\"thead_tr\">");
		html.append("<th class=\"thead_td\">#</th>");
		html.append("<th class=\"thead_td1\">SAP:"+oProductosPedidoDTO.getCod_sap()+"&nbsp;&nbsp;DESC:&nbsp;"+oProductosPedidoDTO.getDescripcion()+"</th>");		
		html.append("<th class=\"thead_td\">"+oProductosPedidoDTO.getCant_solic()+" "+oProductosPedidoDTO.getUnid_medida()+"</th>");
		html.append("<th class=\"thead_td\">"+Formatos.formatoPrecio(oProductosPedidoDTO.getPrecio())+ "</th>");		
		
		double precioSolicitado = (oProductosPedidoDTO.getPrecio() * oProductosPedidoDTO.getCant_solic());
		html.append("<th class=\"thead_td\">"+Formatos.formatoPrecio(precioSolicitado)+" *</th>");	
		html.append("<th class=\"thead_td\">&nbsp;</th>");
		html.append("</tr>"); 
		html.append("</thead>");
		html.append("<tbody id=\"tbody_"+oProductosPedidoDTO.getId_detalle()+"\">");	
		
		Iterator it = detallePickingByIdDetalle.iterator();	
		double totalAcumuladoPrecioPickeado = 0;
		double cantidadPickeado = 0;
		double precioPickeado=0;
		int i =1;
		while (it.hasNext()) {
			DetallePickingDTO oDetallePickingDTO = (DetallePickingDTO) it.next();				
			precioPickeado = oDetallePickingDTO.getPrecio();
			
			html.append("<tr class=\"tbody_tr\" id=\"dataRows_"+oDetallePickingDTO.getId_detalle()+"_"+oDetallePickingDTO.getId_dpicking()+"\">");		
			html.append("<td class=\"tbody_td\">"+i+"</td>");
			
			html.append("<td class=\"tbody_td1\">");
				if(oDetallePickingDTO.getSustituto()!= null && oDetallePickingDTO.getSustituto().equalsIgnoreCase("S"))
					html.append("[Sustituto]&nbsp;");					
			html.append(oDetallePickingDTO.getId_producto()+"  "+oDetallePickingDTO.getDescripcion());
			html.append("</td>");
			
			html.append("<td class=\"tbody_td\">"+Formatos.cortarDecimales(4,oDetallePickingDTO.getCant_pick())+"</td>");				
			html.append("<td class=\"tbody_td\">"+Formatos.formatoPrecio(oDetallePickingDTO.getPrecio())+"</td>");
			
			double precioPicking = (oDetallePickingDTO.getPrecio() *  oDetallePickingDTO.getCant_pick());
			html.append("<td class=\"tbody_td\">"+Formatos.formatoPrecio(precioPicking)+"</td>");
			
			html.append("<td class=\"tbody_td\">");
			if(detallePickingByIdDetalle.size() > 1 && activaBotonera){
				html.append("<input type=\"radio\" name=\"radio_"+oDetallePickingDTO.getId_detalle()+"\" id=\"radio_"+oDetallePickingDTO.getId_detalle()+"_"+i+"\" value=\""+oDetallePickingDTO.getId_dpicking()+"\">" );
			}	
			html.append("</td>");				
			html.append("</tr>");				
			
			i++;			
			totalAcumuladoPrecioPickeado += precioPicking; 	
			cantidadPickeado += oDetallePickingDTO.getCant_pick();
		}
		
		html.append("</tbody>");
		html.append("<tfoot>");
		html.append("<tr class=\"tfoot_tr1\">");
		html.append("<td align=\"center\">&nbsp;</td>");
		html.append("<td align=\"right\"><b>Total Picking:</b></td>");
		html.append("<td align=\"center\">"+Formatos.cortarDecimales(4,cantidadPickeado)+"</td>");
		html.append("<td align=\"center\">&nbsp;</td>");
		html.append("<td align=\"center\">"+Formatos.formatoPrecio(totalAcumuladoPrecioPickeado)+" *</td>");
		double diferencia=totalAcumuladoPrecioPickeado - precioSolicitado;
		if(diferencia > 0){
			html.append("<td align=\"center\"><span class=\"valorExceso\">"+Formatos.formatoPrecio(diferencia)+"</span></td>");		
		}else{
			html.append("<td align=\"center\">"+Formatos.formatoPrecio(diferencia)+"</td>");
		}
		html.append("</tr>");
		
		if(activaBotonera){
			html.append("<tr class=\"tfoot_tr\">");
			html.append("<td align=\"right\" colspan=\"6\">");
			if(cantidadPickeado > oProductosPedidoDTO.getCant_solic()){
				if(!"KG".equals(oProductosPedidoDTO.getUnid_medida()) && detallePickingByIdDetalle.size() == 1 ){//&& precioPickeado == oProductosPedidoDTO.getPrecio()){
					html.append("<span id=\"Sugerencia_btn\" onclick= \"javascript:info('Sugerencia_btn')\"><img src=\"img/icon41.gif\" border=\"0\">&nbsp;[Sugerencia]</span>&nbsp;&nbsp;");			
					html.append("<input type=\"button\" onclick = \"setAction('CS','"+oProductosPedidoDTO.getId_detalle()+"')\" name=\"CS_"+oProductosPedidoDTO.getId_detalle()+"\" id=\"CS_"+oProductosPedidoDTO.getId_detalle()+"\" value=\"Ajustar A Cantidad Solicitada\">");
					html.append("&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;");
				}
				html.append("<input type=\"button\" onclick = \"setAction('AP','"+oProductosPedidoDTO.getId_detalle()+"')\" name=\"AP_"+oProductosPedidoDTO.getId_detalle()+"\" id=\"AP_"+oProductosPedidoDTO.getId_detalle()+"\" value=\"Ajustar Precio Segun Cantidad\">");
			}else{
				html.append("<input type=\"button\" onclick = \"setAction('PS','"+oProductosPedidoDTO.getId_detalle()+"')\" name=\"PS_"+oProductosPedidoDTO.getId_detalle()+"\" id=\"PS_"+oProductosPedidoDTO.getId_detalle()+"\" value=\"Ajustar A Precio Solicitado\">");
			}		
			if(detallePickingByIdDetalle.size() > 1){
				html.append("&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;");
				html.append("<input type=\"button\" onclick = \"setAction('EPK','"+oProductosPedidoDTO.getId_detalle()+"')\" name=\"EPK_"+oProductosPedidoDTO.getId_detalle()+"\" id=\"EPK_"+oProductosPedidoDTO.getId_detalle()+"\" value=\"Eliminar Picking Seleccionado\">");
			}
			html.append("</td>");
			html.append("</tr>");
		}
			
		html.append("</tfoot>");
		html.append("</table><br><hr><br>");
		
		return html.toString().trim();
	}
	
	
	private String getHtmlLogHelper(List reporte) throws BocException {
		
		StringBuffer html = new StringBuffer();  
		
		html.append("<table width=\"770\" cellspacing=\"1\" cellpadding=\"3\" align=\"center\">");
		html.append("<thead>");

		html.append("<tr class=\"thead_tr\">");
			html.append("<th class=\"thead_td\">ID Log</th>");
			html.append("<th class=\"thead_td\">USUARIO</th>");		
			html.append("<th class=\"thead_td\">DESCRIPCION</th>");
			html.append("<th class=\"thead_td\">FECHA</th>");		
		html.append("</tr>"); 
		html.append("</thead>");
		
		html.append("<tbody>");	

		Iterator it = reporte.iterator();	
		while (it.hasNext()) {
			LogPedidoDTO oReporte = (LogPedidoDTO) it.next();				
									
			html.append("<tr class=\"tbody_tr\" id=\"dataRows_"+oReporte.getId_log()+"\">");		
				html.append("<td>"+oReporte.getId_log()+"</td>");
				html.append("<td>"+oReporte.getUsuario()+"</td>");
				html.append("<td>"+oReporte.getLog()+"</td>");
				html.append("<td>"+oReporte.getFecha()+"</td>");								
			html.append("</tr>");				
		}
		
		html.append("</tbody>");
		
		html.append("<tfoot>");
			html.append("<tr class=\"tfoot_tr1\"><td  colspan=\"4\">* </td></tr>");
		html.append("</tfoot>");
		html.append("</table><br><hr><br>");
		
		return html.toString().trim();
	}

}