package cl.jumbo.ventamasiva.ctrl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.common.dto.CarroCompraCategoriasDTO;
import cl.bbr.jumbocl.common.dto.CarroCompraProductosDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.pedidos.dto.BinDTO;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.CreaRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PagoVentaMasivaDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.jumbo.ventamasiva.BizDelegate.BizDelegate;
import cl.jumbo.ventamasiva.Constant.Constant;
import cl.jumbo.ventamasiva.command.Command;
import cl.jumbo.ventamasiva.exceptions.VentaMasivaException;
import cl.jumbo.ventamasiva.log.Logging;
import cl.jumbo.ventamasiva.utils.Util;

public class CtrlPurchaseEnd extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logging logger = new Logging(this);
	private PagoVentaMasivaDTO pago = null;
	
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)	throws VentaMasivaException {

		ResourceBundle rb = ResourceBundle.getBundle("config");
		ParametroDTO paramError = null;
		ParametroDTO paramBinsPedido = null;
		PagoVentaMasivaDTO dtoVM = null;
		try {

			HttpSession session = arg0.getSession();
			BizDelegate biz = new BizDelegate();

			Map mapUrlParams = biz.getParametroByNameIn(Constant.NAME_IN);			
			ParametroDTO paramUrlSuccess = (ParametroDTO) mapUrlParams.get(Constant.REDIRECT_URL_SUCCESS_TBRAINS);
			paramError = (ParametroDTO) mapUrlParams.get(Constant.REDIRECT_URL_ERROR_TBRAINS);
			//paramBinsPedido = (ParametroDTO) mapUrlParams.get(Constant.ID_BP_VENTA_MASIVA);
			
			logger.info("paramUrlSuccess["+paramUrlSuccess.getValor()+"], paramError["+paramError.getValor()+"]");

			session.setAttribute("paramUrlSuccessTbrains", paramUrlSuccess);

			PedidoDTO pedido = (PedidoDTO) session.getAttribute("ses_tbrains_pedido");

			long idPedido = 0;
			long idCliente = 0;
			try {
				if (pedido == null) {							
					if(arg0.getParameter("oc") == null){
						logger.error("El pedido es nulo, no se pudo rescatar el pedido de la sesion y tampoco de la url(oc)");
						arg1.sendRedirect(paramError.getValor() + "?err=16");
						return;									
					}else{
						logger.error("Voy a buscar nuevamente el pedido desde la 'oc' : " + arg0.getParameter("oc"));
						long oc = Long.parseLong(arg0.getParameter("oc").trim());
						//PedidoDTO newPedido = biz.getPedidoById(oc);
						//idPedido = newPedido.getId_pedido();
						//idCliente = newPedido.getId_cliente();
	
						dtoVM = biz.getPagoVentaMasivaByIdPedido(oc);
						if(dtoVM!=null){
							if(dtoVM.getTokenPago().length()>0){
								arg1.sendRedirect(paramUrlSuccess.getValor()+"?token="+dtoVM.getTokenPago());
								return;
							}else{
								logger.error("Token no encontrado(oc)");
								arg1.sendRedirect(paramError.getValor() + "?err=12");
								return;
							}
						}else{
							logger.error("Token no encontrado(oc), dto es nulo");
							arg1.sendRedirect(paramError.getValor() + "?err=13");
							return;
						}
					}											
				}else{				
					idPedido = pedido.getId_pedido();
					idCliente = pedido.getId_cliente();	
					dtoVM = biz.getPagoVentaMasivaByIdPedido(idPedido);
				}
			} catch (Exception e) {
				logger.error("Error al rescatar pedido o recatar token, " + e);
				arg1.sendRedirect(paramError.getValor() + "?err=15");
			}
			
			
			logger.info("Datos de la session - idPedido["+idPedido+"], idCliente["+idCliente+"]");	
			
			if (!"M".equals(pedido.getDispositivo())) {
				arg1.sendRedirect(paramError.getValor() + "?err=11");
				return;
			}
			
			String tokenValidacionPagoWeb = "";
			PagoVentaMasivaDTO pagoVentaMasivaDTO = new PagoVentaMasivaDTO();
			if(dtoVM!=null && dtoVM.getTokenPago().length()>0){
				logger.info("token rescatado ["+dtoVM.getTokenPago()+"]");
				pagoVentaMasivaDTO.setTokenPago(dtoVM.getTokenPago());
				pagoVentaMasivaDTO.setIdPedido(idPedido);
				arg1.sendRedirect(paramUrlSuccess.getValor()+"?token="+dtoVM.getTokenPago());
				return;
			}else{
				tokenValidacionPagoWeb = Util.randomString(20);
				logger.info("token generado ["+tokenValidacionPagoWeb+"]");				
				pagoVentaMasivaDTO.setTokenPago(tokenValidacionPagoWeb);
				pagoVentaMasivaDTO.setIdPedido(idPedido);	
			}
									
			try {
				biz.addPagoVentaMasiva(pagoVentaMasivaDTO);
			} catch (Exception e) {
				logger.error("Error al ingresar el pago de venta masiva token ["+idPedido+"], " + e);
				arg1.sendRedirect(paramError.getValor() + "?err=6");
			}
			
			BotonPagoDTO bp = new BotonPagoDTO();
			int estado = biz.webpayGetEstado((int) idPedido);

			if (pedido.getMedio_pago().equalsIgnoreCase(Constant.MEDIO_PAGO_TBK)) {
				if (estado == Constant.ID_ESTAD_PEDIDO_VALIDADO || estado == Constant.ID_ESTAD_PEDIDO_EN_VALIDACION) {
					// Caso de excepcion, por alguna razon webpay llama aveces
					// mas de una vez la pagina de exito, en esos casos enviamos
					// a la pagina de resumen		
					try {
						logger.info("Caso de excepcion, por alguna razon webpay llama aveces mas de una vez la pagina de exito, en esos casos enviamos a la pagina de resumen");
						if(arg0.getParameter("oc") != null){
							dtoVM = biz.getPagoVentaMasivaByIdPedido(Long.parseLong(arg0.getParameter("oc").trim()));
							arg1.sendRedirect(paramUrlSuccess.getValor()+"?token="+tokenValidacionPagoWeb);
							return;
						}else{
							arg1.sendRedirect(paramError.getValor() + "?err=14");
							return;	
						}
					} catch (Exception e) {
						logger.error("Error Caso de excepcion," + e);
					}
					arg1.sendRedirect(paramError.getValor() + "?err=14");
					return;
				}

				if (("" + idPedido).equals(arg0.getParameter("TBK_ERROR"))) {
					try {
						// Si pedido existe, limpiamos capacidades tomadas y se borra el pedido en estado 1
						biz.purgaPedidoPreIngresado(pedido, idCliente);
					} catch (Exception e) {
						logger.error("Error al eliminar pedido no pagado ["+idPedido+"]");
					}
					arg1.sendRedirect(paramError.getValor() + "?err=1");
					return;
				}

				// validar el TBK_RESPUESTA = 0 si es webpay antes de ingresar
				// el pedido al sistema solo por seguridad
				WebpayDTO wp = biz.webpayGetPedido(idPedido);
				if (wp.getTBK_RESPUESTA() != 0 || estado != Constant.ID_ESTAD_PEDIDO_PRE_INGRESADO) {
					// Error no controlado, cuando llaman a cerrar una OP que no
					// tenga el OK de transbank o que no este preingresado
					logger.error("Error no controlado, cuando llaman a cerrar una OP que no tenga el OK de transbank o que no este preingresado");
					// actualizamos el pago a Rechazado
					pago = new PagoVentaMasivaDTO();
					pago.setIdPedido(idPedido);
					pago.setEstado("R");
					pago.setfValidacion((new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(Calendar.getInstance().getTime()));
					biz.actualizaPagoVentaMasivaByOP(pago);

					try {
						// Si pedido existe, se limpia capacidades tomadas y se borra pedido en estado 1
						biz.purgaPedidoPreIngresado(pedido, idCliente);
					} catch (Exception e) {
						logger.error("Error al eliminar pedido no pagado ["+idPedido+"]");
					}

					arg1.sendRedirect(paramError.getValor() + "?err=7");
					return;
				}

			} else if (pedido.getMedio_pago().equalsIgnoreCase(	Constant.MEDIO_PAGO_CAT)) {
				if (estado != Constant.ID_ESTAD_PEDIDO_PRE_INGRESADO) {
					pago = new PagoVentaMasivaDTO();
					pago.setIdPedido(idPedido);
					pago.setEstado("R");
					pago.setfValidacion((new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(Calendar.getInstance().getTime()));
					biz.actualizaPagoVentaMasivaByOP(pago);

					try {
						// Si pedido existe, se limpia capacidades tomadas y se borra pedido en estado 1
						biz.purgaPedidoPreIngresado(pedido, idCliente);
					} catch (Exception e) {
						logger.error("Error al eliminar pedido no pagado ["+idPedido+"]");
					}

					arg1.sendRedirect(paramError.getValor() + "?err=8");
					return;
				}			

				// Se guarda la Información recibida en la BD
				bp = biz.botonPagoGetByPedido(idPedido);
				bp.setNroTarjeta(arg0.getParameter("numeroTarjeta"));
				bp.setRutCliente(arg0.getParameter("rutCliente"));
				bp.setClienteValidado(arg0.getParameter("usoClave"));
				bp.setGlosaRespuesta(arg0.getParameter("glosaRespuesta"));
				bp.setCodRespuesta(arg0.getParameter("codigoRespuesta"));
				// tipoAutorizacion = "A";
				biz.updateNotificacionBotonPago(bp);

				if (!"A".equals( arg0.getParameter("tipoAutorizacion") )) {
					// actualizamos el pago a Rechazado
					pago = new PagoVentaMasivaDTO();
					pago.setIdPedido(idPedido);
					pago.setEstado("R");
					pago.setfValidacion((new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(Calendar.getInstance().getTime()));
					biz.actualizaPagoVentaMasivaByOP(pago);

					try {
						// Si pedido existe, se limpia capacidades tomadas y se borra pedido en estado 1
						biz.purgaPedidoPreIngresado(pedido, idCliente);
					} catch (Exception e) {
						logger.error("Error al eliminar pedido no pagado ["+idPedido+"]");
					}

					arg1.sendRedirect(paramError.getValor() + "?err=9");
					return;
				}
			}

			// boolean esClienteConfiable = false;
			boolean ingresapedidoAlSistema = false;
			ClienteDTO cliente = null;
			
			// Obtiene cliente confiable
			try {
				cliente = biz.clienteGetById(idCliente);
				//if (biz.clienteEsConfiable(cliente.getRut()) && !biz.tieneEventosActivosConValidacionManual(cliente.getRut())) {
				//esClienteConfiable = true;
				//}
			} catch (Exception e) {
				logger.error("Error obtener cliente confiable ["+idPedido+"], " + e);				
			}
			
			// Ingresar pedido al sistema
			try {
				//biz.setModEstadoPedido(idPedido, Constant.ID_ESTAD_PEDIDO_VALIDADO);
				//PedidoDTO ped = biz.getPedidoById(idPedido);
				List productos = biz.getProductosSolicitadosVMById(idPedido);
				List detallePicking = new ArrayList();

				for (int i=0; i < productos.size(); i++) {
					 CarroCompraCategoriasDTO cat = (CarroCompraCategoriasDTO) productos.get(i);
					 List prod = cat.getCarroCompraProductosDTO();

					 for (int j = 0; j < prod.size(); j++) {
						 CarroCompraProductosDTO producto = (CarroCompraProductosDTO) prod.get(j);
						 	DetallePickingDTO dto = new DetallePickingDTO();
						 	dto.setId_pedido(idPedido);
						 	//dto.setId_dpicking(pedido.ge);
							dto.setId_detalle(producto.getIdDetalle());
							//dto.setId_bp(Long.parseLong(paramBinsPedido.getValor()));
							dto.setId_producto(producto.getPro_id());
							dto.setId_pedido(idPedido);
							dto.setCBarra(producto.getEan13());
							dto.setDescripcion(producto.getDescripcion());
							dto.setPrecio(producto.getPrecio());
							dto.setCant_pick(producto.getCantidad());
							dto.setSustituto("N");
							dto.setAuditado("N");
							dto.setPrecio_picking_ori(producto.getPrecio());							
							
							detallePicking.add(dto);
					 }
				 }
				
				PedidoDTO ped = biz.getPedidoById(idPedido);
				CreaRondaDTO rondaDTO = new CreaRondaDTO();
				rondaDTO.setId_sector(Constant.ID_SECTOR_VENTA_MASIVA);
				rondaDTO.setId_jpicking(ped.getId_jpicking());
				rondaDTO.setId_estado(Constant.ID_ESTADO_RONDA_TERMINADA);
				rondaDTO.setId_local(Constant.ID_LOCAL_FLORIDA);
				rondaDTO.setCant_prod(String.valueOf(ped.getCant_prods()));
				rondaDTO.setTipo_ve("N");
				
				BinDTO binDTO = new BinDTO();
				//binDTO.setId_ronda(0);
				binDTO.setId_pedido(ped.getId_pedido());
				binDTO.setCod_bin("VM-"+ped.getId_pedido());
				binDTO.setCod_sello1("");
				binDTO.setCod_sello2("");
				binDTO.setCod_ubicacion("");
				binDTO.setTipo("V");
				binDTO.setVisualizado("N");
		            		            		            
				biz.addDetallePickingVentaMasiva(detallePicking, rondaDTO, binDTO, idPedido, Constant.ID_ESTAD_PEDIDO_EN_BODEGA);
				
				ingresapedidoAlSistema = true;
				
			} catch (Exception e) {
				logger.error("Error al ingresar Detalle Picking al sistema ["+idPedido+"], " + e);
				arg1.sendRedirect(paramError.getValor() + "?err=17");
				return;
			}			

			// Envia mail
			try {
				String mail_tpl = null;
				if (pedido.getTipo_despacho().equalsIgnoreCase("R")) {
					//mail_tpl = rb.getString("conf.dir.html") + "" + rb.getString("arc_mail_retiro");
					mail_tpl = rb.getString("arc_mail_retiro");
				} else {
					//mail_tpl = rb.getString("conf.dir.html") + "" + rb.getString("arc_mail");
					mail_tpl = rb.getString("arc_mail");
				}
				
				TemplateLoader mail_load = new TemplateLoader(mail_tpl);
				ITemplate mail_tem = mail_load.getTemplate();
				String mail_result = mail_tem.toString(contenidoMailResumen(bp, rb, cliente, pedido, idPedido, biz, ""));

				MailDTO mail = new MailDTO(); 
				mail.setFsm_subject(pedido.getNom_cliente() + ", " + rb.getString("mail.checkout.subject"));
				mail.setFsm_data(mail_result);
				mail.setFsm_destina(cliente.getEmail().toString());
				mail.setFsm_remite(rb.getString("mail.checkout.remite"));											
				biz.addMail(mail);
				logger.info("Mail Enviado a:" + cliente.getEmail().toString());
				
				pago = new PagoVentaMasivaDTO();
				pago.setIdPedido(idPedido);
				pago.setEstado("A");
				pago.setfValidacion((new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(Calendar.getInstance().getTime()));
				biz.actualizaPagoVentaMasivaByOP(pago);

			} catch (Exception e) {
				logger.error("Error al enviar mail ["+idPedido+"], " + e);	
			}

			if (ingresapedidoAlSistema) {								
				session.removeAttribute("ses_tbrains_pedido");				
				logger.info("sesion tbrains eliminada");
				//arg1.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");				
				//arg1.setHeader("Pragma", "no-cache");
				arg1.sendRedirect(paramUrlSuccess.getValor()+"?token="+tokenValidacionPagoWeb);
			}

		} catch (Exception e) {
			logger.error("Error, " + e);
			try {
				arg1.sendRedirect(paramError.getValor()+"?err=4");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private IValueSet contenidoMailResumen(BotonPagoDTO bp, ResourceBundle rb,
			ClienteDTO cliente, PedidoDTO pedido1, long idPedido,
			BizDelegate biz, String horasDespachoEconomico)
			throws cl.jumbo.ventamasiva.exceptions.FuncionalException,
			VentaMasivaException {

		// objeto que contendra todas las variables del html
		IValueSet mail_top = new ValueSet();
		mail_top.setVariable("{nombre_cliente}", pedido1.getNom_cliente());
		mail_top.setVariable("{idped}", idPedido + pedido1.getSecuenciaPago());
		mail_top.setVariable("{cantidad}", Formatos.formatoCantidadFO(pedido1.getCant_prods()) + "");
		mail_top.setVariable("{monto_op}", Formatos.formatoPrecioFO(pedido1.getMonto()) + "");//
		mail_top.setVariable("{monto_despacho}", Formatos.formatoPrecioFO(pedido1.getCosto_despacho()) + "");
		mail_top.setVariable("{monto_reservado}", Formatos.formatoPrecioFO(pedido1.getMonto_reservado()) + "");
		mail_top.setVariable("{fecha_ingreso}", cl.bbr.jumbocl.common.utils.Formatos.frmFecha(pedido1.getFingreso()));
			
		//String observacion = pedido1.getObservacion();
		//logger.info("quien recibe ["+observacion+"]");
		String quienRecibe = null;
		try{
			//String textoRecibe = rb.getString("mail.quienRecibe");
			quienRecibe = pedido1.getNom_cliente();//Util.printNombreFO(pedido1.getNom_tit()) + " " + Util.printNombreFO(pedido1.getApat_tit());			   
		}catch(Exception ex){
			logger.error("Error al setear persona autorizada a recibir el despacho, " + ex);
			quienRecibe = pedido1.getNom_cliente();//Util.printNombreFO(pedido1.getNom_tit()) + " " + Util.printNombreFO(pedido1.getApat_tit());
		}		 

		//mail_top.setVariable("{sin_gente_txt}", pedido1.getSin_gente_txt().replaceAll("\\+", " ") + "");				
		//mail_top.setVariable("{sin_gente_txt}", Util.printNombreFO(cliente.getNombre()) + " " + Util.printNombreFO(cliente.getApellido_pat()));
		mail_top.setVariable("{sin_gente_txt}", quienRecibe);

		if (pedido1.getTipo_despacho().equalsIgnoreCase("R")) {
			LocalDTO loc = biz.getLocalRetiro(pedido1.getId_local());
			String direc = loc.getDireccion() != null ? loc.getDireccion() : "";
			String indic = pedido1.getIndicacion() != null ? pedido1.getIndicacion() : "";

			mail_top.setVariable("{lugar_despacho}", indic + " " + direc);
			mail_top.setVariable("{id_local}", loc.getId_local() + "");
			mail_top.setVariable("{sin_gente_rut}", cl.bbr.jumbocl.common.utils.Formatos.formatoRut(pedido1.getSin_gente_rut())	+ "-" + pedido1.getSin_gente_dv());

		} else {

			if (pedido1.getDir_depto().length() > 0) {
				mail_top.setVariable("{lugar_despacho}", pedido1.getDir_tipo_calle() + " " + pedido1.getDir_calle() + " " + pedido1.getDir_numero()+ ", "+ pedido1.getDir_depto()+", "+ pedido1.getNom_comuna());
				//mail_top.setVariable("{lugar_despacho}", pedido1.getDir_tipo_calle() + " " + pedido1.getDir_calle() + " " + pedido1.getDir_numero() + ", " + pedido1.getDir_depto() + ", " + pedido1.getNom_comuna());
			} else {
				mail_top.setVariable("{lugar_despacho}", pedido1.getDir_tipo_calle() + " " + pedido1.getDir_calle() + " " + pedido1.getDir_numero()+ ", "+ pedido1.getNom_comuna());
				//mail_top.setVariable("{lugar_despacho}", pedido1.getDir_tipo_calle() + " " + pedido1.getDir_calle() + " " + pedido1.getDir_numero() + ", " + pedido1.getNom_comuna());
			}
		}

		if (pedido1.getMedio_pago().compareTo("CAT") == 0) {

			// ******** PAGO CON TARJETAS MAS **********
			if (bp != null) {
				mail_top.setVariable("{forma_pago}", "Tarjeta Mas");
				mail_top.setVariable("{4_ultimos}", (bp.getNroTarjeta() == null ? "" : "**** **** **** " + bp.getNroTarjeta().substring( bp.getNroTarjeta().length() - 4)));
				mail_top.setVariable("{cod_aut_trx}", bp.getCodigoAutorizacion()); 
				if (bp.getNroCuotas() != null)
					mail_top.setVariable("{nro_cuotas}", cl.bbr.jumbocl.common.utils.Utils.secuenciaStr(bp.getNroCuotas().intValue()));
				// mail_top.setVariable("{nro_cuotas}", "3");
			}

		} else {
			// *********** PAGO CON TRANSBANK *************
			WebpayDTO wp = biz.webpayGetPedido(idPedido);
			if (wp != null) {
				mail_top.setVariable("{forma_pago}", "Tarjeta Bancaria");
				mail_top.setVariable("{4_ultimos}", "**** **** **** " + wp.getTBK_FINAL_NUMERO_TARJETA());
				mail_top.setVariable("{cod_aut_trx}", wp.getTBK_CODIGO_AUTORIZACION());
				mail_top.setVariable("{nro_cuotas}", cl.bbr.jumbocl.common.utils.Utils.secuenciaStr(wp.getTBK_NUMERO_CUOTAS()));
			}

		}

		String fecha = cl.bbr.jumbocl.common.utils.Formatos.frmFecha(pedido1.getFdespacho());

		boolean flag = false;
		String DespHorarioEconomico = rb.getString("DespliegueHorarioEconomico");
		// 'C': Completa (09:00 - 23:00), 'P': Parcial (14:00 - 19:00)
		if (DespHorarioEconomico.equalsIgnoreCase("C") && pedido1.getTipo_despacho().equalsIgnoreCase("C") && !horasDespachoEconomico.equalsIgnoreCase("")) {
			mail_top.setVariable("{fecha_tramo}", fecha + "&nbsp;&nbsp;" + horasDespachoEconomico);
			flag = true;
		}
		if (!flag) {
			int posIni = pedido1.getHdespacho().indexOf(":", 3);
			int posFin = pedido1.getHfindespacho().indexOf(":", 3);
			mail_top.setVariable("{fecha_tramo}", fecha + "&nbsp;&nbsp;"
					+ pedido1.getHdespacho().substring(0, posIni) + " - "
					+ pedido1.getHfindespacho().substring(0, posFin));
		}

		// Cuando es despacho Economico se debe mostrar la Hora de Inicio y Fin
		// de la Jornada completa del día. Ej.: 9:00 - 23:00
		// E: Express, N: Normal, C: Económico
		if (pedido1.getTipo_doc().compareTo("B") == 0) {
			mail_top.setVariable("{tip_doc}", "Boleta");
		} else if (pedido1.getTipo_doc().compareTo("F") == 0) {
			mail_top.setVariable("{tip_doc}", "Factura");
		}

		// inicio cupon de descuento

		PedidoDTO pedido = new PedidoDTO();

		pedido = biz.getValidaCuponYPromocionPorIdPedido(idPedido);

		ArrayList listDescuento = new ArrayList();

		if (pedido != null) {

			if (pedido.isCupon() || pedido.isPromocion()) {
				mail_top.setVariable("{switch}", "block");
				List descuentos = biz.getDescuentosAplicados(idPedido);
				boolean textoCupon = true, textoPromocion = true, textoColaborador = true, isDespacho = true, isDescuento = true;

				int contador = 0;
				for (int i = 0; i < descuentos.size(); i++) {
					DetallePedidoDTO dpd = (DetallePedidoDTO) descuentos.get(i);
					IValueSet fila = new ValueSet();
					if (!textoCupon || !textoPromocion || !textoColaborador) {
						fila.setVariable(Constant.NOMBRE_DESCTO, "");
						fila.setVariable("{CELDA_TITULO}", "td_borderSinTitulo");
					}

					if (dpd.getCodPromo() == -2 && textoCupon) {
						fila.setVariable(Constant.NOMBRE_DESCTO, Constant.TEXT_CUPON_DESCUENTO);
						fila.setVariable("{CELDA_TITULO}", "td_borderTitulo");
						textoCupon = false;
					} else if (dpd.getCodPromo() > 0 && textoPromocion) {
						fila.setVariable(Constant.NOMBRE_DESCTO, Constant.TEXT_PROMOCION);
						fila.setVariable("{CELDA_TITULO}", "td_borderTitulo");
						textoPromocion = false;
					} else if (dpd.getCodPromo() == -1 && textoColaborador) {
						fila.setVariable(Constant.NOMBRE_DESCTO, Constant.TEXT_COLABORADOR);
						fila.setVariable("{CELDA_TITULO}", "td_borderTitulo");
						textoColaborador = false;
					}

					if (dpd.getDescripcion().equals(Constant.TEXT_DESCTO_DESPACHO)) {
						// creacion vista cupon por email
						fila.setVariable(Constant.DETALLE_DESCTO, dpd.getDescripcion());
						fila.setVariable(Constant.MONTO_DESCTO, "");
					} else {
						fila.setVariable(Constant.DETALLE_DESCTO, dpd.getDescripcion());
						fila.setVariable(Constant.MONTO_DESCTO, Formatos.formatoPrecioFO(dpd.getPrecio()));
					}

					if (dpd.getCodPromo() == -2 && !dpd.getDescripcion().equals( Constant.TEXT_DESCTO_DESPACHO) && isDescuento) {
						LogPedidoDTO log = new LogPedidoDTO();
						log.setId_pedido(idPedido);
						log.setUsuario(Constant.SYSTEM);
						log.setLog(Constant.TEXT_DESCTO_APLICADO + pedido.getCodigoCupon() + Constant.POR + Formatos.formatoPrecioFO(dpd.getPrecio()));
						try {
							biz.addLogPedido(log);
						} catch (Exception e) {
							logger.info("("+ idPedido + ") No se pudo agregar información al log del pedido");
						}
						isDespacho = false;
					} else if (dpd.getCodPromo() == -2 && dpd.getDescripcion().equals(Constant.TEXT_DESCTO_DESPACHO) && isDespacho) {
						LogPedidoDTO log = new LogPedidoDTO();
						log.setId_pedido(idPedido);
						log.setUsuario(Constant.SYSTEM);
						log.setLog(Constant.TEXT_DESCTO_APLICADO + pedido.getCodigoCupon() + Constant.TEXT_COSTO_DESPSCHO);
						try {
							biz.addLogPedido(log);
						} catch (Exception e) {
							logger.info("("+ idPedido + ") No se pudo agregar información al log del pedido");
						}
						isDescuento = false;
					}

					if (contador % 2 != 0) {
						fila.setVariable("{CLASE_CELDA}", "tdProductoPar");
					} else {
						fila.setVariable("{CLASE_CELDA}", "celda1");
					}
					contador++;

					listDescuento.add(fila);
				}

				mail_top.setDynamicValueSets("DESCUENTOS", listDescuento);

			} else {
				mail_top.setVariable("{switch}", "none");
			}
		}

		// fin cupon de descuento

		// Categorías y Productos
		int contador = 0;
		long totalizador = 0;
		List productosPorCategoria = biz.getProductosSolicitadosVMById(idPedido);
		double precio_total = 0;
		List fm_prod = new ArrayList();
		for (int i = 0; i < productosPorCategoria.size(); i++) {
			CarroCompraCategoriasDTO cat = (CarroCompraCategoriasDTO) productosPorCategoria.get(i);
			List prod = cat.getCarroCompraProductosDTO();
			for (int j = 0; j < prod.size(); j++) {
				CarroCompraProductosDTO producto = (CarroCompraProductosDTO) prod.get(j);
				// total_producto_pedido += Math.ceil(producto.getCantidad());
				IValueSet fila_pro = new ValueSet();
				fila_pro.setVariable("{descripcion}", producto.getNombre());
				fila_pro.setVariable("{marca}", producto.getMarca());
				fila_pro.setVariable("{cod_sap}", producto.getCodigo());
				fila_pro.setVariable("{valor}", Formatos.formatoIntervaloFO(producto.getCantidad()) + "");
				fila_pro.setVariable("{carr_id}", producto.getCar_id() + "");
				fila_pro.setVariable("{contador}", contador + "");
				precio_total = 0;
				// Existe STOCK para el producto
				if (producto.getStock() != 0) {
					// Si el producto es con seleccion
					if (producto.getUnidad_tipo().charAt(0) == 'S') {
						IValueSet fila_lista_sel = new ValueSet();
						List aux_lista = new ArrayList();
						for (double v = 0; v <= producto.getInter_maximo(); v += producto.getInter_valor()) {
							IValueSet aux_fila = new ValueSet();
							aux_fila.setVariable("{valor}", Formatos.formatoIntervaloFO(v) + "");
							aux_fila.setVariable("{opcion}", Formatos.formatoIntervaloFO(v) + "");
							if (Formatos.formatoIntervaloFO(v).compareTo(Formatos.formatoIntervaloFO(producto.getCantidad())) == 0)
								aux_fila.setVariable("{selected}", "selected");
							else
								aux_fila.setVariable("{selected}", "");
							aux_lista.add(aux_fila);
						}
						fila_lista_sel.setVariable("{contador}", contador + "");
						fila_lista_sel.setDynamicValueSets("CANTIDADES", aux_lista);

						List aux_blanco = new ArrayList();
						aux_blanco.add(fila_lista_sel);
						fila_pro.setDynamicValueSets("LISTA_SEL", aux_blanco);

					} else {

						IValueSet fila_lista_sel = new ValueSet();
						fila_lista_sel.setVariable("{contador}", contador + "");
						fila_lista_sel.setVariable("{valor}", producto.getCantidad() + "");
						fila_lista_sel.setVariable("{maximo}",producto.getInter_maximo() + "");
						fila_lista_sel.setVariable("{intervalo}", producto.getInter_valor() + "");
						List aux_blanco = new ArrayList();
						aux_blanco.add(fila_lista_sel);
						fila_pro.setDynamicValueSets("INPUT_SEL", aux_blanco);
					}

					precio_total = Utils.redondearFO(producto.getPpum()	* producto.getCantidad());
					fila_pro.setVariable("{unidad}", producto.getTipre());
					fila_pro.setVariable("{precio_unitario}", Formatos.formatoPrecioFO(producto.getPpum()));
					fila_pro.setVariable("{precio_total}", Formatos.formatoPrecioFO(precio_total));
					fila_pro.setVariable("{CLASE_TABLA}", "TablaDiponiblePaso1");

					if (contador % 2 != 0) {
						fila_pro.setVariable("{CLASE_CELDA}", "tdProductoPar");
					} else {
						fila_pro.setVariable("{CLASE_CELDA}", "celda1");
					}

					fila_pro.setVariable("{NO_DISPONIBLE}", "");
					fila_pro.setVariable("{OPCION_COMPRA}", "1");
					totalizador += precio_total;

					contador++;
					fm_prod.add(fila_pro);
				}

			}

		}
		mail_top.setDynamicValueSets("PRODUCTOS", fm_prod);
		return mail_top;
	}
}
