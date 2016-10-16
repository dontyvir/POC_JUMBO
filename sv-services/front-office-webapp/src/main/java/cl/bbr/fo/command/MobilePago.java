package cl.bbr.fo.command;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.pedidos.dto.PagoGrabilityDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;


/**

 * 
 */
public class MobilePago extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5779795428430960555L;
	

	public static final String REDIRECT_URL_PAYMENT_BASE = "REDIRECT_URL_PAYMENT_BASE";	
	public static final String REDIRECT_URL_PAYMENT_GRABILITY = "REDIRECT_URL_PAYMENT_GRABILITY";
	public static final String REDIRECT_URL_SUCCESS = "REDIRECT_URL_SUCCESS";
	public static final String REDIRECT_URL_ERROR = "REDIRECT_URL_ERROR";
	//public static final String URL_PAGO_TBK = "URL_PAGO_TBK";
	public static final String URL_PAGO_CAT = "URL_PAGO_CAT";
	public static final String DEBUG_FORM_PAGO_GRABILITY = "DEBUG_FORM_PAGO_GRABILITY";

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws CommandException {

		ResourceBundle rb = ResourceBundle.getBundle("fo");	
		String urlError="";
		try {
			
			BizDelegate biz = new BizDelegate();
			String nameIn =REDIRECT_URL_PAYMENT_GRABILITY+"', '"+REDIRECT_URL_SUCCESS+"', '"+REDIRECT_URL_ERROR+"', '"+REDIRECT_URL_PAYMENT_BASE+"', '"+URL_PAGO_CAT+"', '"+DEBUG_FORM_PAGO_GRABILITY;
			Map mapUrlParams =  biz.getParametroByNameIn(nameIn);
			
			//ParametroDTO paramUrlPago 		= (ParametroDTO)mapUrlParams.get(REDIRECT_URL_PAYMENT_GRABILITY);
			ParametroDTO paramUrlSuccess 	= (ParametroDTO)mapUrlParams.get(REDIRECT_URL_SUCCESS);
			ParametroDTO paramUrlError		= (ParametroDTO)mapUrlParams.get(REDIRECT_URL_ERROR);
			ParametroDTO paramUrlBasePago	= (ParametroDTO)mapUrlParams.get(REDIRECT_URL_PAYMENT_BASE);			
			ParametroDTO paramUrlCAT		= (ParametroDTO)mapUrlParams.get(URL_PAGO_CAT);
			ParametroDTO paramDebugForm		= (ParametroDTO)mapUrlParams.get(DEBUG_FORM_PAGO_GRABILITY);
			
			//ParametroDTO paramUrlTBK		= (ParametroDTO)mapUrlParams.get(URL_PAGO_TBK);
						
			String contextoPago = paramUrlBasePago.getValor();			
			String debugFormPago = paramDebugForm.getValor();
			
			String formPagoCAT = paramUrlCAT.getValor();
			//String formPagoTBK = paramUrlTBK.getValor();
			
			urlError = paramUrlError.getValor(); 
			PrintWriter out = arg1.getWriter();
			String tokenRecibido = null;
			
			logger.info("\nINICIO PAGO GRABILITY");
			 Enumeration headers = arg0.getHeaderNames();
	         while (headers.hasMoreElements()) {

	             String header = (String) headers.nextElement();
	             logger.info(header + " : " + arg0.getHeader(header));
	         }
	         
	         Enumeration requ = arg0.getParameterNames();
	         while (requ.hasMoreElements()) {

	             String param = (String) requ.nextElement();
	             logger.info(param + " : " + arg0.getParameter(param));
	         }
	         
	        boolean validaToken=true;
			if("ON".equals(debugFormPago)){
				if(arg0.getParameter("tienecook") != null && ("NO".equals(arg0.getParameter("tienecook"))))
					validaToken=false;
			}
									
			if (validaToken){
				Cookie[] cook = arg0.getCookies();
				if (cook != null) {
					for (int i = 0; i < cook.length; i++) {
						if ("token".equals(cook[i].getName())) {
							tokenRecibido = cook[i].getValue();
							break;
						}					
					}				
				} else {
					//Comodion, por si grability no funka la cookie
					if( arg0.getHeader("X-tokenH") != null){
						tokenRecibido = arg0.getHeader("X-tokenH");
					}					
				}	
				
				if(tokenRecibido == null && arg0.getHeader("X-tokenH") != null){
					tokenRecibido = arg0.getHeader("X-tokenH");
				}
				
				//valida si el token existe en la solicitud
				if(tokenRecibido == null){
					arg1.sendRedirect(urlError+"?err=4");
					return;
				}
			}
			
			if (arg0.getParameter("id_pedido") != null) { 
				//String mensaje = "";
				long id_pedido = Long.parseLong(arg0.getParameter("id_pedido"));
				PedidoDTO newPedido = biz.getPedidoById(id_pedido);
				 				
				if (newPedido==null){
					arg1.sendRedirect(urlError+"?err=5");
					return;
				}
												
				if(validaToken){
					PagoGrabilityDTO pago = biz.getPagoByOP(id_pedido);
					if (pago == null){
						arg1.sendRedirect(urlError+"?err=6");
						return;
					}else {
						if (!pago.getTokenPago().equals(tokenRecibido)){
							arg1.sendRedirect(urlError+"?err=4");
							return;
						}						
					}
				}
				
				if (newPedido.getId_estado() == 1 && "g".equals(newPedido.getDispositivo())) {
					
					HttpSession session = arg0.getSession(true);
					session.setAttribute("ses_grability_pedido", newPedido);
					
					//String contextoWP = rb.getString("webpay.url");
					//String contextoCAT = rb.getString("boton.url");
					/*String contextoWP = "http://"+ipHost;
					String contextoCAT = "http://"+ipHost;*/
					
					String actionForm = "";
					String onLoadJs="";
					StringBuffer form= new StringBuffer();
					String orderDeCompra= id_pedido + newPedido.getSecuenciaPago();
					
					if (newPedido.getMedio_pago().equalsIgnoreCase("TBK")) {						
						
						// inicializacion parametros webpay
						String TBK_TIPO_TRANSACCION = "TR_NORMAL";
						String TBK_URL_EXITO = contextoPago + "/FO/MobileOrderComplete";
						String TBK_URL_FRACASO = contextoPago + "/FO/MobileOrderComplete?TBK_ERROR="+id_pedido; //contextoWP + "/FO/MobilePago?err=1";
						String TBK_ID_SESION = "1";
						String TBK_ORDEN_COMPRA = orderDeCompra;
						String TBK_MONTO =  new Double(newPedido.getMonto_reservado()).longValue() + "00"; //"" + montoTotal + "00";						
												
						String overrideWebPay = super.getWebPayOverride();
						//if (ENABLE_OVERRIDE_WEBPAY.equals(overrideWebPay)) {
							actionForm = rb.getString("webpay.kit.fonocompra");
						//} else {
							//actionForm = rb.getString("webpay.kit.cliente");
						//}						
						
						onLoadJs = "document.getElementById('webpay').submit();";
						String typeForm="hidden";
						if("ON".equals(debugFormPago)){
							if((""+id_pedido).equals(arg0.getParameter("autoSubmit"))){
								onLoadJs = "";
								typeForm="text";
							}
							
							if ("success".equals(arg0.getParameter("redirect")) || "error".equals(arg0.getParameter("redirect"))){						
								if("success".equals(arg0.getParameter("redirect"))){
									actionForm =paramUrlSuccess.getValor();
								}else if("error".equals(arg0.getParameter("redirect"))){
									actionForm = urlError;
								}
							}
						}
						
						if("text".equals(typeForm)){
							form.append("URL:::"+actionForm+"<br/>");
						}						
						form.append("<form action=\""+actionForm+"\" method=\"post\" id=\"webpay\" name=\"webpay\" >");
						form.append("<input type=\""+typeForm+"\" id=\"TBK_TIPO_TRANSACCION\"	name=\"TBK_TIPO_TRANSACCION\"	value =\""+TBK_TIPO_TRANSACCION+"\"	readonly=\"readonly\" />");
						form.append("<input type=\""+typeForm+"\" id=\"TBK_URL_EXITO\" 		name=\"TBK_URL_EXITO\"			value =\""+TBK_URL_EXITO+"\" 		readonly=\"readonly\" />");
						form.append("<input type=\""+typeForm+"\" id=\"TBK_URL_FRACASO\" 		name=\"TBK_URL_FRACASO\"		value =\""+TBK_URL_FRACASO+"\" 		readonly=\"readonly\" />");
						form.append("<input type=\""+typeForm+"\" id=\"TBK_ID_SESION\" 		name=\"TBK_ID_SESION\"			value =\""+TBK_ID_SESION+"\" 		readonly=\"readonly\" />");
						form.append("<input type=\""+typeForm+"\" id=\"TBK_ORDEN_COMPRA\" 	name=\"TBK_ORDEN_COMPRA\"		value =\""+TBK_ORDEN_COMPRA+"\"		readonly=\"readonly\" />");
						form.append("<input type=\""+typeForm+"\" id=\"TBK_MONTO\"		 	name=\"TBK_MONTO\"				value =\""+TBK_MONTO+"\"			readonly=\"readonly\"/>");
						if("text".equals(typeForm)){
							form.append("<input type=\"submit\"  name=\"submit\" value =\"submit\" />");
						}
						form.append("</form>");
					
					} else if (newPedido.getMedio_pago().equalsIgnoreCase("CAT")) {
						
						int montoTotal = (int) (newPedido.getMonto() + newPedido.getCosto_despacho());
						// inicializacion parametros cat						
						actionForm = formPagoCAT;//rb.getString("boton.kit.ruta.cliente");
						String numeroEmpresa = rb.getString("boton.numero.cliente");
						String numeroTransaccion = orderDeCompra;
						String idCarroCompra = orderDeCompra;
						String montoOperacion = "" + montoTotal;
						String urlNotificacion = contextoPago +"/FO/MobileOrderComplete";//"/FO/MobileProcessPayment.jsp";
						String tiempoSesion = "60";
						String tiempoNotificacion = "60";
						
						onLoadJs = "document.getElementById('cat').submit();";
						String typeForm="hidden";
						
						if("ON".equals(debugFormPago)){
							if((""+id_pedido).equals(arg0.getParameter("autoSubmit"))){
								onLoadJs = "";
								typeForm = "text";
							}
	
							if ("success".equals(arg0.getParameter("redirect")) || "error".equals(arg0.getParameter("redirect"))){
								if("success".equals(arg0.getParameter("redirect"))){
									actionForm =paramUrlSuccess.getValor();
								}else if("error".equals(arg0.getParameter("redirect"))){
									actionForm = urlError;
								}
							}
						}
						
						if("text".equals(typeForm)){
							form.append("URL:::"+actionForm+"<br/>");
						}	            						
						form.append("<form action=\""+actionForm+"\" method=\"post\" id=\"cat\" name=\"cat\" >");
						form.append("<input type=\""+typeForm+"\"	name=\"numeroEmpresa\" 		id=\"numeroEmpresa\"		value =\""+numeroEmpresa+"\"		readonly=\"readonly\" />");
						form.append("<input type=\""+typeForm+"\"	name=\"numeroTransaccion\" 	id=\"numeroTransaccion\"	value =\""+numeroTransaccion+"\"	readonly=\"readonly\" />");
						form.append("<input type=\""+typeForm+"\"	name=\"idCarroCompra\" 		id=\"idCarroCompra\"		value =\""+idCarroCompra+"\"		readonly=\"readonly\" />");
						form.append("<input type=\""+typeForm+"\"	name=\"montoOperacion\" 	id=\"montoOperacion\"		value =\""+montoOperacion+"\"		readonly=\"readonly\" />");
						form.append("<input type=\""+typeForm+"\"	name=\"urlNotificacion\" 	id=\"urlNotificacion\"		value =\""+urlNotificacion+"\"		readonly=\"readonly\" />");
						form.append("<input type=\""+typeForm+"\"	name=\"tiempoSesion\" 		id=\"tiempoSesion\"			value =\""+tiempoSesion+"\"			readonly=\"readonly\" />");
						form.append("<input type=\""+typeForm+"\"	name=\"tiempoNotificacion\" id=\"tiempoNotificacion\"	value =\""+tiempoNotificacion+"\"	readonly=\"readonly\" />");
						if("text".equals(typeForm)){
							form.append("<input type=\"submit\"  name=\"submit\" value =\"submit\" />");
						}
						form.append("</form>");
					}
					
					arg1.setContentType("text/html"); 
					StringBuffer html= new StringBuffer();
					//html.append("<html>");
					html.append("<html>"); 
					html.append("<title>Pago Jumbo.cl</title>"); 
					html.append("<body onload=javascript:"+onLoadJs+">"); 
					html.append(form.toString().trim());
					html.append("</body>");
					html.append("</html>"); 
					
		            logger.info(html.toString());
					out.println(html.toString()); 
					out.flush();
					logger.info("FIN PAGO GRABILITY\n");

				} else {
					arg1.sendRedirect(urlError+"?err=3");
					return;
				}
				
			} else {
				arg1.sendRedirect(urlError+"?err=2");
				return;
			}
		
		} catch (Exception e) {
			logger.error(e);
			try {
				arg1.sendRedirect(urlError+"?err=5");
			} catch (IOException e1) {
				// TODO Bloque catch generado automáticamente
				e1.printStackTrace();
			}
			
		}

	}
}