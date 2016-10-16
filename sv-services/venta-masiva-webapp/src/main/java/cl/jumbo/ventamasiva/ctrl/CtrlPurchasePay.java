package cl.jumbo.ventamasiva.ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;

import cl.jumbo.ventamasiva.BizDelegate.BizDelegate;
import cl.jumbo.ventamasiva.Constant.Constant;
import cl.jumbo.ventamasiva.command.Command;
import cl.jumbo.ventamasiva.dto.FormCatDTO;
import cl.jumbo.ventamasiva.dto.FormTbkDTO;
import cl.jumbo.ventamasiva.exceptions.FuncionalException;
import cl.jumbo.ventamasiva.exceptions.VentaMasivaException;
import cl.jumbo.ventamasiva.log.Logging;


/**
 * Retorna el Medio de Pago CAT o TBK
 * */
public class CtrlPurchasePay extends Command {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logging logger = new Logging(this);
	private FormTbkDTO formTbkDTO = null; 
	private FormCatDTO formCatDTO = null;
		
	protected void execute(HttpServletRequest request, HttpServletResponse response) throws VentaMasivaException {
		String urlError="";
		try {
			ResourceBundle rb = ResourceBundle.getBundle("config");
			
			BizDelegate bizdelegate = new BizDelegate();
			Map mapUrlParams =  bizdelegate.getParametroByNameIn(Constant.NAME_IN);
			ParametroDTO paramUrlError    = (ParametroDTO)mapUrlParams.get(Constant.REDIRECT_URL_ERROR_TBRAINS);
			ParametroDTO paramUrlBasePago = (ParametroDTO)mapUrlParams.get(Constant.REDIRECT_URL_PAYMENT_BASE_TBRAINS);
			ParametroDTO paramUrlCAT      = (ParametroDTO)mapUrlParams.get(Constant.URL_PAGO_CAT_TBRAINS);
			
			urlError = paramUrlError.getValor();
			String contextoPago = paramUrlBasePago.getValor();
			String formPagoCAT = paramUrlCAT.getValor();						
			logger.info("contextoPago["+contextoPago+"], formPagoCAT["+formPagoCAT+"], urlError["+urlError+"]" );			 
			
			PrintWriter out = response.getWriter();						

			if (request.getAttribute("id_pedido") != null) {
				long id_pedido = Long.parseLong(request.getAttribute("id_pedido").toString());
				logger.info("id_pedido["+id_pedido+"]");

				PedidoDTO newPedido = bizdelegate.getPedidoById(id_pedido);
				
				if (newPedido==null){
					logger.error("El pedido es nulo, no se encontro informacion del pedido ["+id_pedido+"]");
					response.sendRedirect(urlError+"?err=5");
					return;
				}

				if (newPedido.getId_estado() == 1 && "M".equals(newPedido.getDispositivo())) { 
					HttpSession session = request.getSession(true);
					session.setAttribute("ses_tbrains_pedido", newPedido);
					logger.info("sesion tbrains creada");
					
					String onLoadJs = null;
					StringBuffer form= new StringBuffer();
					String orderDeCompra= id_pedido + newPedido.getSecuenciaPago();

					if (newPedido.getMedio_pago().equalsIgnoreCase("TBK")) {
						// inicializacion parametros webpay
						logger.info("Medio Pago (TBK) - IdPedido["+id_pedido+"]");
						formTbkDTO = new FormTbkDTO();
						formTbkDTO.setTipoTransaccion("TR_NORMAL");
						formTbkDTO.setUrlExito(contextoPago + Constant.CONTEXT_ROOT + Constant.TBK_PURCHASE_END+"?oc="+ id_pedido);
						formTbkDTO.setUrlFracaso(contextoPago + Constant.CONTEXT_ROOT+Constant.TBK_PURCHASE_END_ERROR + id_pedido);//contextoWP + "/FO/MobilePago?err=1";
						formTbkDTO.setIdSession("1");
						formTbkDTO.setOrdenCompra(orderDeCompra);
						formTbkDTO.setMonto(new Double(newPedido.getMonto_reservado()).longValue() + "00");
						formTbkDTO.setActionForm(rb.getString("webpay.kit.fonocompra"));
					
						onLoadJs = "document.getElementById('webpay').submit();";
						form = createFormTBK(formTbkDTO);

					} else if (newPedido.getMedio_pago().equalsIgnoreCase("CAT")) {
						logger.info("Medio Pago (CAT) - IdPedido["+id_pedido+"]");
						int montoTotal = (int) (newPedido.getMonto() + newPedido.getCosto_despacho());
						// inicializacion parametros cat
						formCatDTO = new FormCatDTO();
						formCatDTO.setActionForm(formPagoCAT);
						formCatDTO.setNumeroEmpresa(rb.getString("boton.numero.cliente"));
						formCatDTO.setNumeroTransaccion(orderDeCompra);
						formCatDTO.setIdCarroCompra(orderDeCompra);
						formCatDTO.setMontoOperacion("" + montoTotal);
						formCatDTO.setUrlNotificacion(contextoPago + Constant.CONTEXT_ROOT + Constant.CAT_PURCHASE_END);
						formCatDTO.setTiempoSesion("60");
						formCatDTO.setTiempoNotificacion("60");

						onLoadJs = "document.getElementById('cat').submit();";
						form = createFormCat(formCatDTO);
					}
					
					response.setContentType("text/html"); 					
		            logger.info("form html: " + createHtml(form, onLoadJs));
		            
					out.println( createHtml(form, onLoadJs)); 
					out.flush();
					
				} else {
					logger.error("El estado del IdPedido NO es Pre-Ingresado(1)");
					response.sendRedirect(urlError+"?err=3");
					return;
				}
				
			} else {
				logger.error("El request del IdPedido es nulo");
				response.sendRedirect(urlError+"?err=2");
				return;
			}
			
		} catch(FuncionalException fx){
			logger.error(fx);
			try {
				response.sendRedirect(urlError+"?err=4");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		
		} catch (Exception e) {
			logger.error(e);
			try {
				response.sendRedirect(urlError+"?err=4");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private StringBuffer createFormTBK(FormTbkDTO formTbkDTO){
		StringBuffer form= new StringBuffer();
		form.append("<form action=\""+formTbkDTO.getActionForm()+"\" method=\"post\" id=\"webpay\" name=\"webpay\" >");
		form.append( createInput("hidden", "TBK_TIPO_TRANSACCION", "TBK_TIPO_TRANSACCION", formTbkDTO.getTipoTransaccion()));
		form.append( createInput("hidden", "TBK_URL_EXITO", "TBK_URL_EXITO", formTbkDTO.getUrlExito()));
		form.append( createInput("hidden", "TBK_URL_FRACASO", "TBK_URL_FRACASO", formTbkDTO.getUrlFracaso()));
		form.append( createInput("hidden", "TBK_ID_SESION", "TBK_ID_SESION", formTbkDTO.getIdSession()));
		form.append( createInput("hidden", "TBK_ORDEN_COMPRA", "TBK_ORDEN_COMPRA", formTbkDTO.getOrdenCompra()));
		form.append( createInput("hidden", "TBK_MONTO", "TBK_MONTO", formTbkDTO.getMonto()));						
		form.append("</form>");
		return form;
	}
	
	private StringBuffer createFormCat(FormCatDTO formCatDTO){
		StringBuffer form= new StringBuffer();
		form.append("<form action=\""+formCatDTO.getActionForm()+"\" method=\"post\" id=\"cat\" name=\"cat\" >");
		form.append( createInput("hidden", "numeroEmpresa", "numeroEmpresa", formCatDTO.getNumeroEmpresa()));
		form.append( createInput("hidden", "numeroTransaccion", "numeroTransaccion", formCatDTO.getNumeroTransaccion()));
		form.append( createInput("hidden", "idCarroCompra", "idCarroCompra", formCatDTO.getIdCarroCompra()));
		form.append( createInput("hidden", "montoOperacion", "montoOperacion", formCatDTO.getMontoOperacion()));
		form.append( createInput("hidden", "urlNotificacion", "urlNotificacion", formCatDTO.getUrlNotificacion()));
		form.append( createInput("hidden", "tiempoSesion", "tiempoSesion", formCatDTO.getTiempoSesion()));
		form.append( createInput("hidden", "tiempoNotificacion", "tiempoNotificacion", formCatDTO.getTiempoNotificacion()) );
		form.append("</form>");
		return form;
	}
	
	private String createInput(String typeForm, String name, String id, String value ){
		return "<input type=\""+typeForm+"\" name=\""+name+"\" id=\""+id+"\" value=\""+value+"\" readonly=\"readonly\" />";
	}

	private String createHtml(StringBuffer form, String onLoadJs){
		StringBuffer html= new StringBuffer();
		html.append("<html>");
		html.append("<title>Pago Jumbo.cl</title>");
		html.append("<body onload=javascript:"+onLoadJs+">");
		html.append(form.toString().trim());
		html.append("</body>");
		html.append("</html>");
		return html.toString();
	}
}
