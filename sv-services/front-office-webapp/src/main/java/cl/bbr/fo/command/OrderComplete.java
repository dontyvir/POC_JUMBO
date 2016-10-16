package cl.bbr.fo.command;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.exception.FuncionalException;
import cl.bbr.fo.exception.SystemException;
import cl.bbr.jumbocl.clientes.dto.CarroCompraDTO;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.dto.CarroCompraCategoriasDTO;
import cl.bbr.jumbocl.common.dto.CarroCompraProductosDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.parametros.service.ParametrosService;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.jumbocl.pedidos.service.PedidosService;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;

/**
 * Ingresa el pedido (cambia el pre-ingresado) en el sistema
 *  
 * @author imoyano
 *  
 */
public class OrderComplete extends Command {
    
	private static final String POR = " por ";

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		
		logger.debug("********** OrderComplete **************");
		
        HttpSession session = arg0.getSession();        
		long idCliente = Long.parseLong(session.getAttribute("ses_cli_id").toString());
		long idPedido = Long.parseLong(session.getAttribute("ses_id_pedido").toString());
		String horasDespachoEconomico = session.getAttribute("ses_horas_economico").toString();
		String disOk = getServletConfig().getInitParameter("dis_ok");
		String urlErrorCat = "Pago";
				
		BizDelegate biz = new BizDelegate();
		PedidoDTO pedido = biz.getPedidoById(idPedido);
		
        if ( "I".equalsIgnoreCase(pedido.getDispositivo()) && pedido.getId_usuario_fono() == 0 ) {
			urlErrorCat = "PasoTresFormMobi";
			disOk = "OrderDisplayMobi";
		}
     		
		BotonPagoDTO bp = new BotonPagoDTO(); 
		
		if ( pedido.getMedio_pago().equalsIgnoreCase(Constantes.MEDIO_PAGO_TBK) ) {
			if ( pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_VALIDADO || pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION ) {
				//Caso de excepcion, por alguna razon webpay llama aveces mas de una vez la pagina de exito, en esos casos enviamos a la pagina de resumen
				logger.debug("Caso de excepcion, por alguna razon webpay llama aveces mas de una vez la pagina de exito, en esos casos enviamos a la pagina de resumen");
				if (!horasDespachoEconomico.equalsIgnoreCase("")) {
					disOk += "?horas_desp_eco=" + horasDespachoEconomico;
				}
				arg1.sendRedirect(disOk);
				return;
			}
			// validar el TBK_RESPUESTA = 0 si es webpay antes de ingresar el pedido al sistema solo por seguridad
			WebpayDTO wp = biz.webpayGetPedido(idPedido);
			if ( wp.getTBK_RESPUESTA() != 0 || pedido.getId_estado() != Constantes.ID_ESTAD_PEDIDO_PRE_INGRESADO ) {
			    
				//Error no controlado, cuando llaman a cerrar una OP que no tenga el OK de transbank o que no este preingresado
				logger.debug("Error no controlado, cuando llaman a cerrar una OP que no tenga el OK de transbank o que no este preingresado");
				throw new CommandException();
			}
			
		} else if ( pedido.getMedio_pago().equalsIgnoreCase(Constantes.MEDIO_PAGO_CAT) ) {
		    
			if ( pedido.getId_estado() != Constantes.ID_ESTAD_PEDIDO_PRE_INGRESADO ) {
				//Error no controlado, cuando llaman a cerrar una OP que no tenga el OK de transbank o que no este preingresado
				throw new CommandException();
			}
			String tipoAutorizacion = arg0.getParameter("tipoAutorizacion");
            String numeroTarjeta = arg0.getParameter("numeroTarjeta");
            String rutCliente = arg0.getParameter("rutCliente");
            String usoClave = arg0.getParameter("usoClave");
            String glosaRespuesta = arg0.getParameter("glosaRespuesta");
            String codRespuesta = arg0.getParameter("codigoRespuesta");
            
            //Se guarda la Información recibida en la BD
            bp = biz.botonPagoGetByPedido(idPedido);
            bp.setNroTarjeta(numeroTarjeta);
            bp.setRutCliente(rutCliente);
            bp.setClienteValidado(usoClave);
            bp.setGlosaRespuesta(glosaRespuesta);
            bp.setCodRespuesta(codRespuesta);

            //tipoAutorizacion = "A";
            
            if ( biz.updateNotificacionBotonPago(bp) ) {
                logger.debug("Boton de Pago - Se Actualizo la Tarjeta para OP: " + idPedido);
            } else {
                logger.debug("Boton de Pago - ERROR al Actualizar la Tarjeta para OP: " + idPedido);
            }
            
			if (!"A".equals(tipoAutorizacion)){
				logger.debug("BtnPagoTMAS_NOTIFICACION: Pago rechazado (tipoAutorizacion="+tipoAutorizacion+"), se reenvía a CheckOut" );
				arg1.sendRedirect(urlErrorCat);
				return;
			}
		}
		
		biz.setCompraHistorica("", idCliente, pedido.getCant_prods(), idPedido);
		//FIN DESMARCAR
		
		boolean esClienteConfiable = false;
		if (session.getAttribute("ses_cli_confiable") != null) {
			if (session.getAttribute("ses_cli_confiable").toString().equalsIgnoreCase("S")) {
				esClienteConfiable = true;
			}
		}
		System.out.println(">>>>>>>>>>entrando a execute OrderComplete().<<<<<<<<<<<<<, esClienteConfiable: "+esClienteConfiable);
		
		ClienteDTO cliente = biz.clienteGetById( idCliente );
		if (cliente.getRut()==123123){//modificamos los valores del cliente si este es invitado
			cliente.setApellido_pat(session.getAttribute("ape_pat_inv").toString());
			cliente.setNombre(session.getAttribute("nombre_inv").toString());
			String rut_s = session.getAttribute("rut1_inv").toString();
			rut_s = rut_s.replaceAll("\\.","").replaceAll("\\-","");
            rut_s = rut_s.substring(0, rut_s.length()-1);            
			long rut = Long.parseLong(rut_s);
			cliente.setRut(rut);
			int M=0,S=1; long T=rut;for(;T!=0;T/=10)S=(int) ((S+T%10*(9-M++%6))%11);					
			cliente.setDv(Character.toString((char)(S!=0?S+47:75)));
		}
        biz.ingresarPedidoASistema( idPedido, esClienteConfiable, cliente.getRut() );
        //envia email de alerta categoria validacion
        try {
        	if( envioEmailAlertaValidacion(idPedido, biz, session) ) {
                logger.debug("Envio de email - validacion OP: " + idPedido);
            } else {
                logger.debug("Envio de email - Error validacion OP: " + idPedido);
            }
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().error("Problemas envio de emails.", e);
		}

        //envia email de alerta categoria validacion Automatica
        try {
        	//se agrego el envio de la variable de session que contendra la OP
        	//
        	if( envioEmailAlertaValidacionAutomatica(idPedido,biz,session) ) {
                logger.debug("Envio de email - alerta compra OP: " + idPedido);
            } else {
                logger.debug("Envio de email - sin alerta compra OP: " + idPedido);
            }
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().error("Problemas envio de emails.", e);
		}
        try {
        	//se agrego el envio de la variable de session que contendra la OP
        	//
        	if( envioEmailAlertaPrimeraCompraClickAlAuto(idCliente,idPedido,biz,session) ) {
                logger.debug("Envio de email - alerta primera compra retiro en local: " + idPedido);
            } else {
                logger.debug("Envio de email - sin alerta primera compra retiro en local: " + idPedido);
            }
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().error("Problemas envio de emails.", e);
		}
        

        
        //Actualiza ranking de ventas de los productos
		try {
			biz.updateRankingVentas(idCliente);
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().error("Problemas actualizar ranking de ventas", e);
		}
		//Ini-Descuento stock de cupón de descuento fvr
		 if (session.getAttribute("ses_cupon_descuento_object") != null) {
	         CuponDsctoDTO cddto = null;    
			 cddto = (CuponDsctoDTO) session.getAttribute("ses_cupon_descuento_object") ;
			 try {
				
				 boolean isAplicaCupon = false;
				 
				 List descuentos = biz.getDescuentosAplicados( idPedido );
				 
				 for ( int i = 0; i < descuentos.size(); i++ ) {
					
					 DetallePedidoDTO dpd = ( DetallePedidoDTO ) descuentos.get( i );
				 	 
					 if( dpd.getCodPromo() == -2 ) {
						 
						 isAplicaCupon = true;
						 break;
					 
					 }else if( dpd.getDescripcion().equals( Constantes.TEXT_DESCTO_DESPACHO ) && cddto.getDespacho() == 1 ) {
					 	
						 isAplicaCupon = true;
						 break;
						 
					 }
					 	 
				 }
				 
				 if ( isAplicaCupon ) {
				
					 if (biz.descuentaStockCuponDescto(cddto.getId_cup_dto()) && biz.setIdCuponIdPedido(cddto.getId_cup_dto(), idPedido)) {
						 logger.info("ID Cupón: " + cddto.getId_cup_dto() + " | Cód. Cupón: " + cddto.getCodigo() + " | ID Pedido: " + idPedido);
					 }else{
					 	 logger.warn("No se logró actualizar el Stock del Cupón de Descuento o no se puedo ingresar el ID Pedido: " + idPedido + 
							 	"e ID Cupon " +cddto.getId_cup_dto() + "a la tabla BO_CUPON_DSCTOXPEDIDO");
					 }
					 
				 }
				
			 } catch (Exception e) {
					e.printStackTrace();
					this.getLogger().error("Problemas actualizar stock cupón de descuento", e);
				}
		 }
		//Fin-Descuento stock de cupón de descuento fvr
		
		//--- INI - Ingresamos los sustitutos
		List sustitutosActualesDeCliente = new ArrayList();
		List prodSustitutosNuevos = new ArrayList();
		try {
			List lcarro = biz.carroComprasGetProductos( idCliente, session.getAttribute("ses_loc_id").toString(), null );
			sustitutosActualesDeCliente = biz.productosSustitutosByCliente(idCliente);
			for (int i = 0; i < lcarro.size(); i++) {
				boolean existe = false;
				CarroCompraDTO carro = (CarroCompraDTO) lcarro.get(i);
				for (int j = 0; j < sustitutosActualesDeCliente.size(); j++) {
					ProductoDTO prod = (ProductoDTO) sustitutosActualesDeCliente.get(j);
					if (Long.parseLong(carro.getPro_id()) == prod.getPro_id()) {
						existe = true;
					}
					if (existe) {
						break;
					}
				}
				if (!existe) {
					prodSustitutosNuevos.add(carro);
				}
			}
			if (prodSustitutosNuevos.size() > 0) {
				//Tiene productos nuevos para asignar criterios, por default dejamos CRITERIO JUMBO
				biz.addSustitutosCliente(idCliente,prodSustitutosNuevos);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().error("Problemas al insertar sustitutos: "+ e.getMessage(), e);
		}
		// --- FIN - Ingresamos los sustitutos
        
        if ( pedido.getId_usuario_fono() != 0 && pedido.getDispositivo().equalsIgnoreCase("I") ) {
            //No se envia el mail. Esto se agrega para la super app mobile!
        } else {		
    		// Envia mail
    		try {
    			ResourceBundle rb = ResourceBundle.getBundle("fo");
    			
    			String mail_tpl = null;
    			
//08102012_VMatheu se elimina el parametro session del metodo contenidoMailResumen y se agrega logica de seleccion para determinar si es mail de retiro o mail de despacho.
    			if(pedido.getTipo_despacho().equalsIgnoreCase("R")){
    			    mail_tpl = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("arc_mail_retiro");
    			}else{
    			    mail_tpl = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("arc_mail");
    			}
    			TemplateLoader mail_load = new TemplateLoader(mail_tpl);
    			ITemplate mail_tem = mail_load.getTemplate();	
    			String mail_result = mail_tem.toString(contenidoMailResumen(bp, rb,cliente, pedido, idPedido, biz, horasDespachoEconomico));
//-08102012_VMatheu
    			
    			MailDTO mail = new MailDTO();
    			
//08102012_VMatheu se agrego el nombre al subjet del mail
    			mail.setFsm_subject(cliente.getNombre()+", "+rb.getString("mail.checkout.subject"));
//-08102012_VMatheu
    			
    			mail.setFsm_data(mail_result);
    			//mail.setFsm_destina(cliente.getEmail());
    			mail.setFsm_destina(session.getAttribute("ses_cli_email").toString());//Se envia mail a correo ingresado 
    			mail.setFsm_remite(rb.getString("mail.checkout.remite"));
    			biz.addMail(mail);
    			//actualizamos correo de cliente si es que es diferente el que se ingreso.
    			if (!cliente.getEmail().equalsIgnoreCase((String)session.getAttribute("ses_cli_email")))
    				biz.clienteChangeDatosPaso3(idCliente,session.getAttribute("ses_cli_email").toString(),"","");
    			//cliente.setEmail(session.getAttribute("ses_cli_email").toString());
    			

    		} catch (Exception e) {
    			e.printStackTrace();
    			this.getLogger().error("Problemas con mail", e);
    		}
        }
		
        String ses_invitado_id = session.getAttribute("ses_invitado_id").toString();
		// Elimina carro de compras (vacia)
		biz.deleteCarroCompraAll(idCliente, ses_invitado_id);
		
		disOk += "?psn=" + prodSustitutosNuevos.size() + "&psa=" + sustitutosActualesDeCliente.size();
		if (!horasDespachoEconomico.equalsIgnoreCase("")) {
			disOk += "&horas_desp_eco=" + horasDespachoEconomico;
		}
		arg1.sendRedirect(disOk);
	}
	
	
	/**
	 * HRP
	 * @return
	 * @throws FuncionalException 
	 */
	private boolean envioEmailAlertaValidacion(long idPedido, BizDelegate biz, HttpSession session) throws FuncionalException {
		
		List listaAlerta = biz.getAlertaPedidoByKey(idPedido, Constantes.ALE_PRODUCTO_CATEGORIA_VALIDACION);		
		if(listaAlerta==null || listaAlerta.size()==0){
			return false;
		}
		
		try {
			
			PedidosService pedidoSrv = new PedidosService();
			List productos = pedidoSrv.getProductosXAlerta(idPedido, Constantes.KEY_VALIDACION_MANUAL_OP);
			String Local = "";
			
			ResourceBundle rb = ResourceBundle.getBundle("fo");
			
			String to   = rb.getString("alert.config.mail.to");
			String host = rb.getString("alert.config.mail.smtp.host");
			String subject = rb.getString("alert.config.mail.subject");
			String from = rb.getString("alert.config.mail.from");
			
			String bodyFinal = ((String)rb.getString("alert.config.mail.body.head")).replaceFirst("::1", String.valueOf(idPedido));
			String body = ((String)rb.getString("alert.config.mail.body"));
			String bodyFoot = ((String)rb.getString("alert.config.mail.body.foot"));
			
			for (int j = 0; j < productos.size(); j++) {
				ProductosPedidoDTO prodPed = (ProductosPedidoDTO) productos.get(j);
				String bodyAux = "";
				
				bodyAux = body.replaceFirst("::2", prodPed.getCod_sap());
				bodyAux = bodyAux.replaceFirst("::3", prodPed.getDescripcion()+"");
				bodyAux = bodyAux.replaceFirst("::4", prodPed.getCant_solic()+"");
				bodyAux = bodyAux.replaceFirst("::5", prodPed.getPrecio()+"");
				bodyAux = bodyAux.replaceFirst("::6", (prodPed.getCant_solic() * prodPed.getPrecio())+"");
				bodyAux = bodyAux.replaceFirst("::7", prodPed.getNombreLocal());
				bodyAux = bodyAux.replaceFirst("::8", prodPed.getFechaDespacho());
				bodyAux = bodyAux.replaceFirst("::9", prodPed.getHoraInicio()+"-"+prodPed.getHoraFin());
				
				bodyFinal = bodyFinal.concat(bodyAux);
				Local = prodPed.getNombreLocal();
			}
			
			bodyFinal = bodyFinal.concat(bodyFoot);

			MailDTO mail = new MailDTO();
			mail.setFsm_subject(subject +" - Local "+ Local+".");
			mail.setFsm_data(bodyFinal);
			mail.setFsm_destina(to);
			mail.setFsm_remite(from);
			mail.setFsm_estado("0");
//			biz.addMail(mail);
			
			// SE VERIFICA ENVIO DE SOLO UN CORREO POR OP ENVIO CORREO
			
			String opSessManual= (String)session.getAttribute("OPSessionManual");
			if (!String.valueOf(idPedido).equals(opSessManual))
				biz.addMail(mail);
			// FIN IF  VERIFICACION
						
			// SIEMPRE ASIGNA LA ID DEL PEDIDO A LA VARIABLE DE SESSION.
			session.setAttribute("OPSessionManual", String.valueOf(idPedido));
						
			
			//ejecucion automatica del envio de mail
			//new SendMail(host, from, body, null).enviar(to, "", subject);
			
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().error("Problemas con mail", e);
		}		
		return true;
	}
	
	private boolean envioEmailAlertaValidacionAutomatica(long idPedido, BizDelegate biz, HttpSession session) throws SystemException {
		
		boolean existeValidacion = biz.verificaAlertaValidacion(idPedido, Constantes.KEY_ALERTA_COMPRA_OP);
		if(!existeValidacion){
			return false;
		}
		
		try {
			
			PedidosService pedidoSrv = new PedidosService();
			List productos = pedidoSrv.getProductosXAlerta(idPedido, Constantes.KEY_ALERTA_COMPRA_OP);
			String Local = "";
			
			ResourceBundle rb = ResourceBundle.getBundle("fo");
			
			String to   = getDestinatariosEmailAlertaCompra();
			String host = rb.getString("mailalert.config.mail.smtp.host");
			String subject = rb.getString("mailalert.config.mail.subject");
			String from = rb.getString("mailalert.config.mail.from");
			
			String bodyFinal = ((String)rb.getString("mailalert.config.mail.body.head")).replaceFirst("::1", String.valueOf(idPedido));
			String body = ((String)rb.getString("mailalert.config.mail.body"));
			String bodyFoot = ((String)rb.getString("mailalert.config.mail.body.foot"));
			
			for (int j = 0; j < productos.size(); j++) {
			ProductosPedidoDTO prodPed = (ProductosPedidoDTO) productos.get(j);
			String bodyAux = "";
			
			bodyAux = body.replaceFirst("::2", prodPed.getCod_sap());
			bodyAux = bodyAux.replaceFirst("::3", prodPed.getDescripcion()+"");
			bodyAux = bodyAux.replaceFirst("::4", prodPed.getCant_solic()+"");
			bodyAux = bodyAux.replaceFirst("::5", prodPed.getPrecio()+"");
			bodyAux = bodyAux.replaceFirst("::6", (prodPed.getCant_solic() * prodPed.getPrecio())+"");
			bodyAux = bodyAux.replaceFirst("::7", prodPed.getNombreLocal());
			bodyAux = bodyAux.replaceFirst("::8", prodPed.getFechaDespacho());
			bodyAux = bodyAux.replaceFirst("::9", prodPed.getHoraInicio()+"-"+prodPed.getHoraFin());
			
			bodyFinal = bodyFinal.concat(bodyAux);
			Local = prodPed.getNombreLocal();
			}
			
			bodyFinal = bodyFinal.concat(bodyFoot);
			
			MailDTO mail = new MailDTO();
			mail.setFsm_subject(subject +" - Local "+ Local+".");
			mail.setFsm_data(bodyFinal);
			mail.setFsm_destina(to);
			mail.setFsm_remite(from);
			mail.setFsm_estado("0");
			
			// SE VERIFICA ENVIO DE SOLO UN CORREO POR OP ENVIO CORREO
			
			String opSessAutomatica= (String)session.getAttribute("OPSessionAutomatica");
			if (!String.valueOf(idPedido).equals(opSessAutomatica))
				biz.addMail(mail);
			// FIN IF  VERIFICACION
			
			// SIEMPRE ASIGNA LA ID DEL PEDIDO A LA VARIABLE DE SESSION.
			session.setAttribute("OPSessionAutomatica", String.valueOf(idPedido));
			
			//ejecucion automatica del envio de mail
			//new SendMail(host, from, body, null).enviar(to, "", subject);
			
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().error("Problemas con mail", e);
		}		
		return true;
	}

    /**
	 * 
	 * @param idPedido
	 * @param biz
	 * @param session
	 * @return
	 * @throws SystemException
	 */
	private boolean envioEmailAlertaPrimeraCompraClickAlAuto(long idCliente, long idPedido, BizDelegate biz, HttpSession session) throws SystemException {
		
		boolean primerRetiroEnLocal = biz.verificaPrimeraCompraRetiroEnLocal(idPedido);
		if(!primerRetiroEnLocal){
			return false;
		}
		
		try {
			
			PedidoDTO prodPed = biz.getPedidoById(idPedido);
				String Local = "";
				
				ResourceBundle rb = ResourceBundle.getBundle("fo");
				
				String to   = rb.getString("mailalert.config.mail.primeracompraretiroenlocal.to");
				String host = rb.getString("mailalert.config.mail.smtp.host");
				String subject = rb.getString("mailalert.config.mail.primeracompraretiroenlocal.subject");
				String from = rb.getString("mailalert.config.mail.from");
				
				String bodyFinal = ((String)rb.getString("mailalert.config.mail.primeracompraretiroenlocal.head")).replaceFirst("::1", String.valueOf(idPedido));
				String body = ((String)rb.getString("mailalert.config.mail.primeracompraretiroenlocal.body"));
				String bodyFoot = ((String)rb.getString("mailalert.config.mail.primeracompraretiroenlocal.body.foot"));
				
				if(prodPed != null){
					String bodyAux = "";
					bodyAux = body.replaceFirst("::2", String.valueOf(prodPed.getId_cliente()));
					bodyAux = bodyAux.replaceFirst("::3", prodPed.getNom_cliente()+"");
					bodyAux = bodyAux.replaceFirst("::4", prodPed.getNom_local()+"");
					bodyAux = bodyAux.replaceFirst("::5", prodPed.getFdespacho()+"");
					bodyAux = bodyAux.replaceFirst("::6", prodPed.getHdespacho()+"-"+prodPed.getHfindespacho());
					
					bodyFinal = bodyFinal.concat(bodyAux);
					Local = prodPed.getNom_local();
				}
				
				bodyFinal = bodyFinal.concat(bodyFoot);
				
				MailDTO mail = new MailDTO();
				mail.setFsm_subject(subject +" - Local "+ Local+".");
				mail.setFsm_data(bodyFinal);
				mail.setFsm_destina(to);
				mail.setFsm_remite(from);
				mail.setFsm_estado("0");
				
				// SE VERIFICA ENVIO DE SOLO UN CORREO POR OP ENVIO CORREO
				
				String opSessAutomatica= (String)session.getAttribute("OPPrimeraCompraRetiroLocal");
				if (!String.valueOf(idPedido).equals(opSessAutomatica))
					biz.addMail(mail);
				// FIN IF  VERIFICACION
				
				// SIEMPRE ASIGNA LA ID DEL PEDIDO A LA VARIABLE DE SESSION.
				session.setAttribute("OPPrimeraCompraRetiroLocal", String.valueOf(idPedido));
				
				//ejecucion automatica del envio de mail
				//new SendMail(host, from, body, null).enviar(to, "", subject);
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().error("Problemas con mail", e);
		}		
		return true;
	}
	
	
    /**
     * Recupera destinatario email para alertas de validacion automatica.
     * El método obtiene de la base de datos de la tabla bo_parametros
     * 
     * @return String con destinatarios de email.
     */
    private String getDestinatariosEmailAlertaCompra() {
        try {
            ParametrosService ps = new ParametrosService();
            ParametroDTO par = ps.getParametroByName("EMAIL_ALERTA_COMPRA");
            return par.getValor();
        } catch (Exception se) {
            logger.info("No se pudo leer el valor de los destinatarios de email para validacion de compra automatica");
            return null;
        }
    }
	
//08102012_VMatheu: se modifica la logica de paso de parametros (eliminacion de listas inutiles para el paso de parametros)
	/**
	 * @return
	 * @throws FuncionalException
	 * @throws SystemException
	 */
	private IValueSet contenidoMailResumen(BotonPagoDTO bp, ResourceBundle rb,ClienteDTO cliente,PedidoDTO pedido1, long idPedido, BizDelegate biz, String horasDespachoEconomico) throws FuncionalException, SystemException {

	    //objeto que contendra todas las variables del html
		IValueSet mail_top = new ValueSet();
		mail_top.setVariable("{nombre_cliente}", cliente.getNombre());
		mail_top.setVariable("{idped}", idPedido + pedido1.getSecuenciaPago());
		mail_top.setVariable("{cantidad}", Formatos.formatoCantidadFO(pedido1.getCant_prods()) + "");
		mail_top.setVariable("{monto_op}", Formatos.formatoPrecioFO(pedido1.getMonto()) +"" );//
		mail_top.setVariable("{monto_despacho}", Formatos.formatoPrecioFO(pedido1.getCosto_despacho()) +"" );
		mail_top.setVariable("{monto_reservado}", Formatos.formatoPrecioFO(pedido1.getMonto_reservado()) +"" );
		mail_top.setVariable("{fecha_ingreso}",cl.bbr.jumbocl.common.utils.Formatos.frmFecha(pedido1.getFingreso()));
//18102012VMatheu		
		mail_top.setVariable("{sin_gente_txt}", pedido1.getSin_gente_txt().replaceAll("\\+"," ")+"");
		
		if (pedido1.getTipo_despacho().equalsIgnoreCase("R")) {
		    
			LocalDTO loc = biz.getLocalRetiro(pedido1.getId_local());
			
		    String direc= loc.getDireccion()!=null?loc.getDireccion():"";
		    String indic= pedido1.getIndicacion()!=null?pedido1.getIndicacion():"";
		    
			mail_top.setVariable("{lugar_despacho}", indic +" "+direc);
			mail_top.setVariable("{id_local}", loc.getId_local()+"");
			mail_top.setVariable("{sin_gente_rut}", cl.bbr.jumbocl.common.utils.Formatos.formatoRut( pedido1.getSin_gente_rut() ) +"-"+pedido1.getSin_gente_dv());
		
		}else{
			
		    if ( pedido1.getDir_depto().length() > 0 ) {
				
				mail_top.setVariable("{lugar_despacho}", pedido1.getDir_tipo_calle()+" "+pedido1.getDir_calle()
									+" "+pedido1.getDir_numero()+", "+pedido1.getDir_depto()+", "+pedido1.getNom_comuna());
			} else {
				
				mail_top.setVariable("{lugar_despacho}", pedido1.getDir_tipo_calle()+" "+pedido1.getDir_calle()
									+" "+pedido1.getDir_numero()+", "+pedido1.getNom_comuna());
				
			}
		}
		
		
		if ( pedido1.getMedio_pago().compareTo("CAT") == 0 ) {
			
			//******** PAGO CON TARJETAS MAS **********                    
			mail_top.setVariable("{forma_pago}", "Tarjeta Mas" );
		    mail_top.setVariable("{4_ultimos}", (bp.getNroTarjeta()==null?"":"**** **** **** "+bp.getNroTarjeta().substring(bp.getNroTarjeta().length()-4)));
			mail_top.setVariable("{cod_aut_trx}", bp.getCodigoAutorizacion());
			mail_top.setVariable("{nro_cuotas}", cl.bbr.jumbocl.common.utils.Utils.secuenciaStr(bp.getNroCuotas().intValue()));
			//mail_top.setVariable("{nro_cuotas}", "3");
	
		} else {
			//*********** PAGO CON TRANSBANK *************
		    WebpayDTO wp = biz.webpayGetPedido(idPedido);
			
			mail_top.setVariable("{forma_pago}", "Tarjeta Bancaria" );
			mail_top.setVariable("{4_ultimos}", "**** **** **** "+wp.getTBK_FINAL_NUMERO_TARJETA());
			mail_top.setVariable("{cod_aut_trx}", wp.getTBK_CODIGO_AUTORIZACION());
			mail_top.setVariable("{nro_cuotas}", cl.bbr.jumbocl.common.utils.Utils.secuenciaStr(wp.getTBK_NUMERO_CUOTAS()));
			                    
		}
//-18102012VMatheu
		String fecha = cl.bbr.jumbocl.common.utils.Formatos.frmFecha(pedido1.getFdespacho());
		
		boolean flag = false;
		String DespHorarioEconomico = rb.getString("DespliegueHorarioEconomico");
		//'C': Completa (09:00 - 23:00), 'P': Parcial (14:00 - 19:00)
		if (DespHorarioEconomico.equalsIgnoreCase("C") && pedido1.getTipo_despacho().equalsIgnoreCase("C") && !horasDespachoEconomico.equalsIgnoreCase("")) {
			mail_top.setVariable("{fecha_tramo}", fecha + "&nbsp;&nbsp;" + horasDespachoEconomico);
			flag = true;
		}
		if (!flag) {
			int posIni = pedido1.getHdespacho().indexOf(":", 3);
			int posFin = pedido1.getHfindespacho().indexOf(":", 3);
			mail_top.setVariable("{fecha_tramo}", fecha + "&nbsp;&nbsp;" + pedido1.getHdespacho().substring(0, posIni) + " - " + pedido1.getHfindespacho().substring(0, posFin));
		}
		
		// Cuando es despacho Economico se debe mostrar la Hora de Inicio y Fin de la Jornada completa del día. Ej.: 9:00 - 23:00
		// E: Express, N: Normal, C: Económico
		if (pedido1.getTipo_doc().compareTo("B") == 0) {
			
			mail_top.setVariable("{tip_doc}", "Boleta");
		
		} else if (pedido1.getTipo_doc().compareTo("F") == 0) {
		
			mail_top.setVariable("{tip_doc}", "Factura");
		
		}
		
		
	//inicio cupon de descuento
	
	PedidoDTO pedido = new PedidoDTO();
	
	pedido = biz.getValidaCuponYPromocionPorIdPedido( idPedido );
	
	ArrayList listDescuento = new ArrayList();
	
	if( pedido != null ) {
		
		if( pedido.isCupon() || pedido.isPromocion() ) {
			
			mail_top.setVariable("{switch}", "block");
		
			List descuentos = biz.getDescuentosAplicados( idPedido );
		
			boolean textoCupon = true, textoPromocion = true, textoColaborador = true, isDespacho = true, isDescuento = true;
		
			int contador = 0;
			
			for ( int i = 0; i < descuentos.size(); i++ ) {
		
				DetallePedidoDTO dpd = ( DetallePedidoDTO ) descuentos.get( i );
			
				IValueSet fila = new ValueSet();
			
				if( !textoCupon || !textoPromocion || !textoColaborador ) {
					fila.setVariable( Constantes.NOMBRE_DESCTO, "" );
					fila.setVariable("{CELDA_TITULO}", "td_borderSinTitulo");	
				}
				
				if( dpd.getCodPromo() == -2 && textoCupon ) {
		
					fila.setVariable( Constantes.NOMBRE_DESCTO, Constantes.TEXT_CUPON_DESCUENTO );
					fila.setVariable("{CELDA_TITULO}", "td_borderTitulo");
					textoCupon = false;
	
				}else if( dpd.getCodPromo() > 0 && textoPromocion ) {
		
					fila.setVariable( Constantes.NOMBRE_DESCTO, Constantes.TEXT_PROMOCION );
					fila.setVariable("{CELDA_TITULO}", "td_borderTitulo");
					textoPromocion = false;
	
				}else if( dpd.getCodPromo() == -1 && textoColaborador ) {
		
					fila.setVariable( Constantes.NOMBRE_DESCTO, Constantes.TEXT_COLABORADOR );
					fila.setVariable("{CELDA_TITULO}", "td_borderTitulo");
					textoColaborador = false;
		
				}
	
				if( dpd.getDescripcion().equals( Constantes.TEXT_DESCTO_DESPACHO ) ) {
		//TODO creacion vista cupon por email
					fila.setVariable( Constantes.DETALLE_DESCTO, dpd.getDescripcion());
					fila.setVariable( Constantes.MONTO_DESCTO, "" );
				
				}
				else {
		
					fila.setVariable( Constantes.DETALLE_DESCTO, dpd.getDescripcion() );
					fila.setVariable( Constantes.MONTO_DESCTO, Formatos.formatoPrecioFO( dpd.getPrecio() ) );
					
				}
	
				if( dpd.getCodPromo() == -2 && !dpd.getDescripcion().equals( Constantes.TEXT_DESCTO_DESPACHO ) && isDescuento ) {
		
					LogPedidoDTO log = new LogPedidoDTO();
					
					log.setId_pedido( idPedido );
					log.setUsuario( Constantes.SYSTEM );
					log.setLog( Constantes.TEXT_DESCTO_APLICADO + pedido.getCodigoCupon() + POR + Formatos.formatoPrecioFO( dpd.getPrecio() ) );
	     
					try { 
						
						biz.addLogPedido( log );
					
					} catch ( Exception e ) {
	            
						logger.info( "(" +idPedido+ ") No se pudo agregar información al log del pedido" );
	        
					}
	     
					isDespacho = false;
	
				}else if( dpd.getCodPromo() == -2 && dpd.getDescripcion().equals( Constantes.TEXT_DESCTO_DESPACHO ) && isDespacho ) {
	
					LogPedidoDTO log = new LogPedidoDTO();
	 	
					log.setId_pedido( idPedido );
					log.setUsuario( Constantes.SYSTEM );
					log.setLog( Constantes.TEXT_DESCTO_APLICADO + pedido.getCodigoCupon() + Constantes.TEXT_COSTO_DESPSCHO );
	     
					try { 
						
						biz.addLogPedido( log );
					
					} catch ( Exception e ) {
						
						logger.info( "(" +idPedido+ ") No se pudo agregar información al log del pedido" );
	            
					}
	         
	         		isDescuento = false;
		
				}
				
				if(contador%2!=0){
				    fila.setVariable("{CLASE_CELDA}","tdProductoPar");
					
				}else{
					fila.setVariable("{CLASE_CELDA}","celda1");
					
				}
				
				contador++;
				
				listDescuento.add( fila );
				
			}
	
			mail_top.setDynamicValueSets( "DESCUENTOS", listDescuento );
		
		
		}else {
			
			mail_top.setVariable("{switch}", "none");
				
		}
	
	}
	
	
	
	//fin cupon de descuento
		
		//Categorías y Productos
		int contador = 0;
		long totalizador = 0;
		List productosPorCategoria = biz.getProductosSolicitadosById(idPedido);
		//total_producto_pedido = 0;
		double precio_total = 0;
		List fm_prod = new ArrayList();
		for (int i = 0; i < productosPorCategoria.size(); i++) {
			
			CarroCompraCategoriasDTO cat = (CarroCompraCategoriasDTO) productosPorCategoria.get(i);
			List prod = cat.getCarroCompraProductosDTO();
			
			for (int j = 0; j < prod.size(); j++) {
			
			   
				CarroCompraProductosDTO producto = (CarroCompraProductosDTO) prod.get(j);
				//  total_producto_pedido += Math.ceil(producto.getCantidad());
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
							aux_fila.setVariable("{valor}",Formatos.formatoIntervaloFO(v)+ "");
							aux_fila.setVariable("{opcion}",Formatos.formatoIntervaloFO(v)+ "");
							if (Formatos.formatoIntervaloFO(v).compareTo(Formatos.formatoIntervaloFO(producto.getCantidad())) == 0)
								aux_fila.setVariable("{selected}","selected");
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
						fila_lista_sel.setVariable("{valor}",producto.getCantidad() + "");
						fila_lista_sel.setVariable("{maximo}",producto.getInter_maximo() + "");
						fila_lista_sel.setVariable("{intervalo}", producto.getInter_valor()+ "");
						List aux_blanco = new ArrayList();
						aux_blanco.add(fila_lista_sel);
						fila_pro.setDynamicValueSets("INPUT_SEL", aux_blanco);
						
					}
					
				
					precio_total = Utils.redondearFO(producto.getPpum() * producto.getCantidad());
					fila_pro.setVariable("{unidad}", producto.getTipre());
					fila_pro.setVariable("{precio_unitario}", Formatos.formatoPrecioFO(producto.getPpum()));
					fila_pro.setVariable("{precio_total}",Formatos.formatoPrecioFO(precio_total));
					fila_pro.setVariable("{CLASE_TABLA}","TablaDiponiblePaso1");
					
					if(contador%2!=0){
					    fila_pro.setVariable("{CLASE_CELDA}","tdProductoPar");
						
					}else{
						fila_pro.setVariable("{CLASE_CELDA}","celda1");
						
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
	//-08102012_VMatheu
}