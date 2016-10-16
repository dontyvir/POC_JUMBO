package cl.bbr.boc.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
//indra
import cl.bbr.boc.dto.UmbralDTO;
//indra
import cl.bbr.boc.utils.FormatoEstados;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.eventos.dto.EventoDTO;
import cl.bbr.jumbocl.pedidos.dto.BinDTO;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.FaltanteDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorTrxMpDTO;
import cl.bbr.jumbocl.pedidos.dto.MotivoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.ReprogramacionDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.pedidos.dto.SustitutoDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.ResumenPedidoPromocionDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Muestra formulario de pedidos
 * es editable si el usuario actual coincide con el id_usuario de la tabla OP.
 * @author BBRI
 *
 */
public class ViewOPFormPedido extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_pedido = 0;
		int id_cliente = 0;
		long id_sector = 0;
		int pag;
		String rc = "";
		int largoped=0;
		boolean edicion = false;
		
		HttpSession session = req.getSession();
		
//		final String CONFIG_BUNDLE_NAME = "confCasos";
//		PropertyResourceBundle configBundle = (PropertyResourceBundle) PropertyResourceBundle.getBundle(CONFIG_BUNDLE_NAME);

		
		String html = getServletConfig().getInitParameter("TplFile");
		
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		String pop_dir 			= getServletConfig().getInitParameter("PopCambioDireccion");
		String pop_ind 			= getServletConfig().getInitParameter("PopIndicacion");
		String pop_desp 		= getServletConfig().getInitParameter("PopCalDespacho");
		String pop_cdesp 		= getServletConfig().getInitParameter("PopCostoDespacho");
		String pop_mp 			= getServletConfig().getInitParameter("PopMedioPago");
		String pop_mp_cre       = getServletConfig().getInitParameter("PopMedioPagoCredito");
		String pop_mfact 		= getServletConfig().getInitParameter("PopModifFactura");
		String pop_libera_op 	= getServletConfig().getInitParameter("PopLiberaOp");
		String pop_anula_op 	= getServletConfig().getInitParameter("PopAnulaOp");
		String pop_cambia_estado_op = getServletConfig().getInitParameter("PopCambiaEstadoOP");
		//String pop_resetea_trxmp= getServletConfig().getInitParameter("PopReseteaTrxMp");
		String mensaje_error	= "";
        String mensajeAnulacionAcelerada    = "";
		String mensaje_confirm	= "";
		String mensaje 			= "";
		//int modo=0;
		
		logger.debug("Template           : " + html);
		logger.debug("PopCambioDireccion : " + pop_dir);
		logger.debug("PopIndicacion      : " + pop_ind);
		logger.debug("PopCalDespacho     : " + pop_desp);
		logger.debug("PopCostoDespacho   : " + pop_cdesp);
		logger.debug("PopMedioPago       : " + pop_mp);
		logger.debug("PopMedioPagoCredito: " + pop_mp_cre);
		logger.debug("PopModifFactura    : " + pop_mfact);
		
		
		// 2. Procesa parámetros del request
		if ( req.getParameter("id_pedido") != null )
			id_pedido = Integer.parseInt( req.getParameter("id_pedido") );
		else {
			id_pedido = 0;
		}
		logger.debug("id_pedido:"+id_pedido);

		if (req.getParameter("rc") != null ) rc = req.getParameter("rc");
		logger.debug("rc:"+rc);

		if ( req.getParameter("id_cliente") != null )
			id_cliente= Integer.parseInt( req.getParameter("id_cliente") );
		else {
			id_cliente= 0;
		}
		logger.debug("id_cliente:"+id_cliente);
		
		if ( req.getParameter("mensaje_error") != null )
			mensaje_error = req.getParameter("mensaje_error");
		logger.debug("mensaje_error:"+mensaje_error);
		
        if ( req.getParameter("msj_anulacion_acelerada") != null )
            mensajeAnulacionAcelerada = req.getParameter("msj_anulacion_acelerada");
        
		if ( req.getParameter("mensaje") != null )
			mensaje = req.getParameter("mensaje");
		logger.debug("mensaje:"+mensaje);
		
		if ( req.getParameter("mensaje_confirm") != null )
			mensaje_confirm = req.getParameter("mensaje_confirm");
		logger.debug("mensaje_confirm:"+mensaje_confirm);
		
		if ( req.getParameter("pagina") != null )
			pag = Integer.parseInt( req.getParameter("pagina") );
		else {
			pag = 1;
		}
		
		logger.debug("pag:"+pag);
		
		logger.debug("id_pedido del usuario en sesion:"+usr.getId_pedido());
		// Modo de edición
		if ( usr.getId_pedido() == id_pedido ){
			edicion = true;
		}
		logger.debug("edicion:"+edicion);
		
		//		3. Template
		View salida = new View(res);
	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		if (req.getParameter("mensaje") != null)
			top.setVariable("{mensaje1}",req.getParameter("mensaje"));
		else
			top.setVariable("{mensaje1}","");
		
		// 4.  Rutinas Dinámicas
		// 4.0 Bizdelegator
		top.setVariable("{mensaje_error}",mensaje_error);
		top.setVariable("{mensaje}",mensaje);
		top.setVariable("{mensaje_confirm}",mensaje_confirm);
		
		/*
		 * Muestra el Tab general del Detalle de Pedidos
		 */
		BizDelegate bizDelegate = new BizDelegate();
		
		//listas dinamicas
		ArrayList susts = new ArrayList();
		ArrayList faltantes = new ArrayList();
		ArrayList bins = new ArrayList();
		ArrayList mots = new ArrayList();
		ArrayList eventos = new ArrayList();
		ArrayList promos= new ArrayList();
		
		
		//indra
		UmbralDTO  elem1 =  bizDelegate.getPorcenUmbralById(id_pedido);
		double porc_unidad = elem1.getU_unidad();
		double porc_monto = elem1.getU_monto();
			//indra
		PedidoDTO pedido =  bizDelegate.getPedidosById(id_pedido);	
		session.setAttribute("##_PedidoDTO_x_Jornada",pedido);
		
		top.setVariable("{id_ped}"      ,String.valueOf(pedido.getId_pedido()));
        
        if (pedido.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_CASOS_CTE)) {
            top.setVariable("{info_pedido}", "&nbsp;&nbsp; ( C" + pedido.getPedidoExt().getNroGuiaCaso() + " )");
        } else if (pedido.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_JV_CTE)) {
            top.setVariable("{info_pedido}", "&nbsp;&nbsp; ( JV" + pedido.getPedidoExt().getNroGuiaCaso() + " )");
        } else {
            top.setVariable("{info_pedido}", "");
        }
        
        if ( !"".equalsIgnoreCase( mensajeAnulacionAcelerada ) ) {
            top.setVariable("{msj_anulacion_acelerada}", mensajeAnulacionAcelerada);
        } else {
            top.setVariable("{msj_anulacion_acelerada}", "");
        }
        
        top.setVariable("{id_cliente_pedido}"      ,String.valueOf(pedido.getId_cliente()));
		top.setVariable("{id_local}"	,String.valueOf(pedido.getId_local()));
		top.setVariable("{local}"	    ,String.valueOf(pedido.getNom_local()));
        
		top.setVariable("{f_ingreso}"	,Formatos.frmFecha(pedido.getFingreso()));
     
        top.setVariable("{f_liberacion}"   , Formatos.fechaLiberacionReserva(pedido.getMedio_pago(),pedido.getFingreso(),"dd/MM/yyyy", 0));
        
        top.setVariable("{h_ingreso}"	,Formatos.frmHoraSola(pedido.getHingreso()));
		top.setVariable("{id_est}"      ,String.valueOf(pedido.getId_estado()));
		//indra
		if((pedido.getId_estado() == 70)){
			   top.setVariable("{imgEstado}","<img src='img/icon41.gif' width='15' height='15'>");
			   top.setVariable("{negritaRojo}","<b><font color='#FF0000'>");
		   }else{
			   top.setVariable("{imgEstado}","");
			   top.setVariable("{negritaRojo}","");
		   }
		   //indra
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		top.setVariable("{estado}"		,String.valueOf(pedido.getEstado()));
		top.setVariable("{porcent_monto}"		,String.valueOf(porc_monto));
		top.setVariable("{porcent_unidad}"		,String.valueOf(porc_unidad));
		//indra
		if(pedido.getId_estado()==Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION && pedido.getNom_motivo().equals("")){
			top.setVariable("{subest}"		,"Sin Gestión");
		}else{
			top.setVariable("{subest}"		,String.valueOf(pedido.getNom_motivo()));
		}
		
		if (pedido.getTipo_despacho() == null){
		    pedido.setTipo_despacho("N");
		}
		if (pedido.getTipo_despacho().equals("N")){
			top.setVariable("{tipo_despacho}", "Normal");
		} else if (pedido.getTipo_despacho().equals("E")) {
			top.setVariable("{tipo_despacho}", "Express");
		} else if (pedido.getTipo_despacho().equals("C")) {
			top.setVariable("{tipo_despacho}", "Econ&oacute;mico");
		} else if (pedido.getTipo_despacho().equals("R")) {
            top.setVariable("{tipo_despacho}", "Retiro en Local");
        } else if (pedido.getTipo_despacho().equals("U")) {
            top.setVariable("{tipo_despacho}", "Bajo Umbral");
        }
        
        top.setVariable("{char_tipo_despacho}", pedido.getTipo_despacho());
        
		top.setVariable("{mon_ped}"		,Formatos.formatoPrecio(pedido.getMonto()));
		top.setVariable("{costo_desp}"	,Formatos.formatoPrecio(pedido.getCosto_despacho()));
		top.setVariable("{total}"		,Formatos.formatoPrecio(pedido.getMonto()+pedido.getCosto_despacho()));
		top.setVariable("{tot_prod}"	,String.valueOf(pedido.getCant_prods()));
		if(pedido.getTipo_doc().equals(Constantes.TIPO_DOC_BOLETA)){
			top.setVariable("{tipo_doc}","Boleta");
		}	
		if(pedido.getTipo_doc().equals(Constantes.TIPO_DOC_FACTURA)){
			top.setVariable("{tipo_doc}","Factura");
		}			
		
		top.setVariable("{local_facturador}"	    ,String.valueOf(pedido.getNom_local_fact()));
		
		if (pedido.getOrigen().equals(Constantes.ORIGEN_WEB_CTE)){
			top.setVariable("{origen}"	    ,Constantes.ORIGEN_WEB_TXT);	
		} else if (pedido.getOrigen().equals(Constantes.ORIGEN_VE_CTE)){
			top.setVariable("{origen}"	    ,Constantes.ORIGEN_VE_TXT);
		} else if (pedido.getOrigen().equals(Constantes.ORIGEN_CASOS_CTE)){
            top.setVariable("{origen}"      ,Constantes.ORIGEN_CASOS_TXT);
        } else if (pedido.getOrigen().equals(Constantes.ORIGEN_JV_CTE)){
            top.setVariable("{origen}"      ,Constantes.ORIGEN_JV_TXT);
        }
		
		if (pedido.getTipo_ve().equals(Constantes.TIPO_VE_NORMAL_CTE)){
			top.setVariable("{tipo_ve}"	    ,Constantes.TIPO_VE_NORMAL_TXT);	
		} else if (pedido.getTipo_ve().equals(Constantes.TIPO_VE_SPECIAL_CTE)){
			top.setVariable("{tipo_ve}"	    ,Constantes.TIPO_VE_SPECIAL_TXT);
		}
		
		
		top.setVariable("{nom_cli}"		,String.valueOf(pedido.getNom_cliente()));
		if(pedido.getDir_depto()!="" && pedido.getDir_depto()!=null){
			top.setVariable("{direccion}"	,String.valueOf(pedido.getDir_tipo_calle()+" "+pedido.getDir_calle()+" "+pedido.getDir_numero()+", "+pedido.getDir_depto()));	
			
		}else{
			top.setVariable("{direccion}"	,String.valueOf(pedido.getDir_tipo_calle()+" "+pedido.getDir_calle()+" "+pedido.getDir_numero()+" "+pedido.getDir_depto()));	
		}
		if (pedido.getNom_comuna() == null) {
		    top.setVariable("{comuna}", "" );
        } else {
            top.setVariable("{comuna}", pedido.getNom_comuna() );    
        }
        
        if ( Constantes.ORIGEN_WEB_CTE.equalsIgnoreCase(pedido.getOrigen()) || Constantes.ORIGEN_VE_CTE.equalsIgnoreCase(pedido.getOrigen()) ) {
		
    		ClientesDTO clientes = bizDelegate.getClienteById(pedido.getId_cliente());
            
            String f1="", f2="", f3="";
            if (clientes.getCodfono1()!=null)
                f1 ="("+clientes.getCodfono1()+") ";        
            if (clientes.getFono1() != null )
                f1 += clientes.getFono1();
            if (clientes.getCodfono2()!=null)
                f2 ="("+clientes.getCodfono2()+") ";        
            if (clientes.getFono2() != null )
                f2 += clientes.getFono2();      
            if (clientes.getCodfono3()!=null)
                f3 ="("+clientes.getCodfono3()+") ";        
            if (clientes.getFono3() != null )
                f3 += clientes.getFono3();      
            String fonos = f1 + "&nbsp;&nbsp;<font color='#FF0000'>/</font>&nbsp;&nbsp;" + 
                    f2 + "&nbsp;&nbsp;<font color='#FF0000'>/</font>&nbsp;&nbsp;" + 
                    f3;
            
            top.setVariable("{fonos}" ,fonos);
            
            // Muestra Tab datos de Cliente
            logger.debug("id_cliente:"+pedido.getId_cliente());
            top.setVariable("{id_cli}"      ,String.valueOf(pedido.getId_cliente()));
            top.setVariable("{rut}"         ,String.valueOf(clientes.getRut()));
            top.setVariable("{dv}"          ,clientes.getDv());
            top.setVariable("{nombre}"      ,String.valueOf(clientes.getNombre()));
            top.setVariable("{paterno}"     ,String.valueOf(clientes.getPaterno()));
            top.setVariable("{materno}"     ,String.valueOf(clientes.getMaterno()));
            top.setVariable("{fec_nac}"     ,Formatos.frmFecha(clientes.getFnac()));
            top.setVariable("{genero}"      ,Formatos.frmGenero(clientes.getGenero()));
            top.setVariable("{mail}"        ,String.valueOf(clientes.getEmail()));
            top.setVariable("{fono1}"       ,f1);
            top.setVariable("{fono2}"       ,f2);
            top.setVariable("{fono3}"       ,f3);
            top.setVariable("{fec_crea}"    ,Formatos.frmFecha(clientes.getFecCrea()));
            top.setVariable("{fec_act}"     ,Formatos.frmFecha(clientes.getFecAct()));
            
            if ( edicion ) {
                top.setVariable("{hab_cli}", "enabled");
            } else {
                top.setVariable("{hab_cli}", "disabled");
            }
        
        } else {
            
            top.setVariable("{fonos}"       ,pedido.getTelefono() + "&nbsp;&nbsp;<font color='#FF0000'>/</font>&nbsp;&nbsp;" + pedido.getTelefono2());
            top.setVariable("{id_cli}"      ,"");
            top.setVariable("{rut}"         ,""+pedido.getRut_cliente());
            top.setVariable("{dv}"          ,pedido.getDv_cliente());
            top.setVariable("{nombre}"      ,pedido.getNom_cliente());
            top.setVariable("{paterno}"     ,"");
            top.setVariable("{materno}"     ,"");
            top.setVariable("{fec_nac}"     ,"");
            top.setVariable("{genero}"      ,"");
            top.setVariable("{mail}"        ,pedido.getPedidoExt().getMail());
            top.setVariable("{fono1}"       ,pedido.getTelefono());
            top.setVariable("{fono2}"       ,pedido.getTelefono2());
            top.setVariable("{fono3}"       ,"");
            top.setVariable("{fec_crea}"    ,"");
            top.setVariable("{fec_act}"     ,"");
            top.setVariable("{hab_cli}"     ,"disabled");
            
        }
		
		if ( pedido.getIndicacion() != null ) {
            largoped = pedido.getIndicacion().length();
        }
        if (largoped == 0) {
            top.setVariable("{indicacion}","");
        } else if (largoped <= 100) {
			top.setVariable("{indicacion}"	,String.valueOf(pedido.getIndicacion().substring(0, largoped )));
		} else if (largoped > 100 && largoped <= 200) {
			top.setVariable("{indicacion}"	, String.valueOf(pedido.getIndicacion().substring(0, 100 ))+"\n"+String.valueOf(pedido.getIndicacion().substring(100, largoped )));
		} else if (largoped > 200 && largoped <= 255) {
			top.setVariable("{indicacion}"	, String.valueOf(pedido.getIndicacion().substring(0, 100 ))+
					"\n"+String.valueOf(pedido.getIndicacion().substring(100, 200))+
					"<br>"+String.valueOf(pedido.getIndicacion().substring(200, largoped)));
		}
		if (pedido.getObservacion()!= null && !pedido.getObservacion().equals("null"))
			top.setVariable("{observaciones}", String.valueOf(pedido.getObservacion()));
		else
			top.setVariable("{observaciones}", "");
		top.setVariable("{fec_desp}"	,Formatos.frmFecha(pedido.getFdespacho()) +" "+ Formatos.frmHoraSola( String.valueOf(pedido.getHdespacho()) ) +"-"+ Formatos.frmHoraSola( String.valueOf(pedido.getHfindespacho())));
        
        RutaDTO ruta = bizDelegate.getRutaByPedido(id_pedido);
        
        if ( ruta.getIdRuta() > 0 ) {   
            top.setVariable("{id_ruta}"      ,""+ruta.getIdRuta());
            top.setVariable("{fono_trans}"      ,ruta.getFono().getCodigo() + " " + ruta.getFono().getNumero());
            top.setVariable("{chofer_trans}"    ,ruta.getChofer().getNombre());
            top.setVariable("{patente_trans}"   ,ruta.getPatente().getPatente());
            top.setVariable("{hr_out_local}"    ,Formatos.frmFechaHora( ruta.getFechaSalida() ));
        } else {
            top.setVariable("{id_ruta}"      ,"");
            top.setVariable("{fono_trans}"      ,"");
            top.setVariable("{chofer_trans}"    ,"");
            top.setVariable("{patente_trans}"   ,"");
            top.setVariable("{hr_out_local}"    ,"");    
        }
        top.setVariable("{hr_in_dom}"       ,Formatos.frmFechaHora( pedido.getPedidoExt().getFcHoraLlegadaDomicilio() ));
        top.setVariable("{hr_out_dom}"      ,Formatos.frmFechaHora( pedido.getPedidoExt().getFcHoraSalidaDomicilio() ));
        
        if ( pedido.getPedidoExt().getReprogramada() > 0 ) {
            top.setVariable("{reprogramada}"   ,"Si");
            
            List reprogramaciones = bizDelegate.getReprogramacionesByPedido(id_pedido);
            ArrayList lReprogramaciones = new ArrayList();    
            for (int i=0; i < reprogramaciones.size(); i++) {
                IValueSet fila = new ValueSet();
                ReprogramacionDTO repro = (ReprogramacionDTO) reprogramaciones.get(i);
                fila.setVariable("{fecha}", Formatos.frmFechaHora(repro.getFechaReprogramacion()));
                fila.setVariable("{jornada}", Formatos.frmFecha(repro.getJornadaDespachoAnterior().getF_jornada()) + " " + Formatos.frmHoraSola(repro.getJornadaDespachoAnterior().getH_inicio()) + "-" + Formatos.frmHoraSola(repro.getJornadaDespachoAnterior().getH_fin()));
                fila.setVariable("{motivo}", repro.getMotivoReprogramacion().getNombre());
                fila.setVariable("{responsable}", repro.getResponsableReprogramacion().getNombre());
                fila.setVariable("{user}", repro.getUsuario());
                lReprogramaciones.add(fila);                
            }
            
            ArrayList viewReprogramaciones = new ArrayList();            
            IValueSet filaView = new ValueSet();
            filaView.setVariable("{vacio}", "");            
            filaView.setDynamicValueSets("REPROGRAMACIONES", lReprogramaciones);
            viewReprogramaciones.add(filaView);            
            top.setDynamicValueSets("VIEW_REPROGRAMACIONES", viewReprogramaciones);
            
        } else {
            top.setVariable("{reprogramada}"   ,"No");
        }
        
        
		top.setVariable("{usuario_boc}" ,String.valueOf(pedido.getNom_usuario_bo()));
		top.setVariable("{usuario_fono}",String.valueOf(pedido.getNom_ejecutivo()));
		top.setVariable("{med_pago}"	,String.valueOf(pedido.getMedio_pago()));
		
        //top.setVariable("{banco}"       ,String.valueOf(pedido.getTb_banco()));
		//top.setVariable("{nom_tarj}"	,String.valueOf(pedido.getNom_tbancaria()));
		//top.setVariable("{fec_venc}"	,String.valueOf(pedido.getFecha_exp()));
        
        if ( "TBK".equalsIgnoreCase(pedido.getMedio_pago())) {
            
            if (pedido.getMonto_reservado() == 0){
                top.setVariable("{num_tarj}", "**** **** **** " + pedido.getNum_mp().substring(pedido.getNum_mp().length() - 4));
                
                top.setVariable("{num_cuotas}", Utils.secuenciaStr(pedido.getN_cuotas()));
            }else{
                WebpayDTO wp = bizDelegate.webpayGetPedido(id_pedido);
                if (wp.getIdPedido() != 0){
            		top.setVariable("{num_tarj}", "**** **** **** " + wp.getTBK_FINAL_NUMERO_TARJETA());
            		top.setVariable("{cod_auth}", wp.getTBK_CODIGO_AUTORIZACION() == null ? "<b>SIN CODIGO DE AUTORIZACION</b>" : "Código de autorización válido");
                    top.setVariable("{num_cuotas}", Utils.secuenciaStr(wp.getTBK_NUMERO_CUOTAS()));
                }else{
            		top.setVariable("{num_tarj}", "**** **** **** ****");
            		top.setVariable("{cod_auth}", "");
                    top.setVariable("{num_cuotas}", "-");
                }
            }
            top.setVariable("{cat_autenticado}", "");
        } else if ("CAT".equalsIgnoreCase(pedido.getMedio_pago())) {
            BotonPagoDTO bp = bizDelegate.botonPagoGetByPedido(id_pedido);
            
            if (pedido.getMonto_reservado() == 0){
                top.setVariable("{num_tarj}", "**** **** **** " + pedido.getNum_mp().substring(pedido.getNum_mp().length() - 4));
                top.setVariable("{cod_auth}", "");
                top.setVariable("{num_cuotas}", Utils.secuenciaStr(pedido.getN_cuotas()));
            }else{
                if (bp.getNroTarjeta() != null){
                    top.setVariable("{num_tarj}", "**** **** **** " + bp.getNroTarjeta().substring(bp.getNroTarjeta().length() - 4));
                    top.setVariable("{cod_auth}", bp.getCodigoAutorizacion() == null ? "<b>SIN CODIGO DE AUTORIZACION</b>" : "Código de autorización válido");
                    top.setVariable("{num_cuotas}", Utils.secuenciaStr(bp.getNroCuotas().intValue()));
                }else{
                    top.setVariable("{num_tarj}", "**** **** **** ****");
                    top.setVariable("{cod_auth}", "");
                    top.setVariable("{num_cuotas}", "-");

                }
            }
            
            String aut = "";
            if ( "S".equalsIgnoreCase( bp.getClienteValidado()) ) {
                aut = "Si";
            } else {
                aut = "No";
            }
            top.setVariable("{cat_autenticado}", "<tr><td>Medio de pago autenticado</td><td>"+aut+"</td></tr>");
        }else if ("CRE".equalsIgnoreCase(pedido.getMedio_pago())){
            top.setVariable("{num_tarj}", "");
            top.setVariable("{num_cuotas}", "");
            top.setVariable("{cat_autenticado}", "");
        }
        
        if(pedido.getId_usuario_fono()>0){
			top.setVariable("{canal}"	,"Fonocompras - "+pedido.getNom_ejecutivo());
		} else {
			top.setVariable("{canal}"	,"Web");
		}
		if (pedido.getSin_gente_op() == 0) {
			top.setVariable("{sin_gente_txt}"	,String.valueOf(pedido.getSin_gente_txt()));
            if (pedido.getTipo_despacho().equalsIgnoreCase("R")) {
                top.setVariable("{sin_gente_rut}"   ,pedido.getSin_gente_rut() + "-" + pedido.getSin_gente_dv());
            } else {
                top.setVariable("{sin_gente_rut}"   ,"");
            }
		} else {
			top.setVariable("{sin_gente_txt}"	,"Ninguna");
            top.setVariable("{sin_gente_rut}"   ,"");
		}
		top.setVariable("{pol_sust}"	,String.valueOf(pedido.getPol_sustitucion()));
		
		
		/* ver fecha y hora del despacho*/
		String fecHoraDespacho = pedido.getFdespacho()+" "+pedido.getHfindespacho();
		   //String fecHoraDespacho = "2006-08-01 17:00:00";
		   logger.debug("fecha y hora despacho:"+fecHoraDespacho );
		   DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   Date dateDespacho = (Date)formatter.parse(fecHoraDespacho);
		   //Date dateActual = formatter.parse(formatter.format(new Date()));
		   Calendar ahora = new GregorianCalendar();
	        
		   long tiempoDespacho = dateDespacho.getTime();
		   //long tiempoActual = dateActual.getTime();
		   long tiempoActual 	= ahora.getTimeInMillis();
		   logger.debug("tiempoActual:"+tiempoActual);
		   logger.debug("tiempoDespacho:"+tiempoDespacho); 
		   logger.debug("diferencia  :"+(tiempoDespacho-tiempoActual));
		   long tiempoLimite = Constantes.MAX_DIFERENCIA_HORAS_NORMAL;
		   logger.debug("tiempoLimite:"+tiempoLimite);
		   //if((tiempoDespacho-tiempoActual>0) && (tiempoDespacho-tiempoActual<tiempoLimite)){
		   if((tiempoDespacho-tiempoActual<tiempoLimite)){
			   top.setVariable("{img}","<img src='img/icon41.gif' alt='La hora de despacho es cercana' width='15' height='15'>");
		   }else{
			   top.setVariable("{img}","");
		   }
		   
		/*botoneras*/
		top.setVariable("{id_pedido}"   ,String.valueOf(pedido.getId_pedido()));
		top.setVariable("{id_usuario}"  ,String.valueOf(usr.getId_usuario()));
		//top.setVariable("{url_libop}"   ,"/BO/BOC/ViewMonOP");
		top.setVariable("{dir_id}"  ,String.valueOf(pedido.getDir_id()));
		
		//segun estado del pedido, se habilita o des
		top.setVariable("{hab_prod}"   ,"disabled");
		top.setVariable("{hab_desp}"   ,"disabled");
		top.setVariable("{hab_polsust}","disabled");
		top.setVariable("{hab_cdesp}"  ,"disabled");
		top.setVariable("{hab_dir}"    ,"disabled");
		top.setVariable("{hab_anul}"   ,"disabled");
		top.setVariable("{hab_mpago}"  ,"disabled");
		top.setVariable("{hab_mpago_cre}", "disabled");
		top.setVariable("{hab_dpago}"  ,"disabled");
		top.setVariable("{hab_ind}"    ,"disabled");
		top.setVariable("{hab_enpago}" ,"disabled");
		top.setVariable("{hab_cambest}" ,"disabled");
        top.setVariable("{hab_mod_prods}" ,"disabled");
        top.setVariable("{hab_anular_boleta}" ,"disabled");
        top.setVariable("{hab_no_sustituir}", "disabled");
        top.setVariable("{hab_a_validado}", "disabled");
        //+20131107_JMCE
        top.setVariable("{hab_mod_fec_op}", "disabled");
        //-20131107_JMCE
		top.setVariable("{a_ini}", "");
		top.setVariable("{a_fin}", "");
		
		top.setVariable("{hab_c}","disabled");
		
		boolean FlagPerfilEjecutivoBOC = false;
		boolean FlagAdminEstadosOP = false;
		boolean FlagPerfilModificadorExceso = false;
		boolean FlagPerfilRetrocederOpEstadoValidado = false;
		boolean FlagPerfilModificacionFechaOP = false;
		
		//boolean FlagPerfilSupervisorBOC = false;
		// revisa si puede anular OPs dado el perfil
		try{
			List listperfiles = new ArrayList();
			listperfiles = usr.getPerfiles();
			logger.debug("tot_perfiles:"+listperfiles.size());
			for (int i=0; i<listperfiles.size(); i++){
				PerfilesEntity perf = new PerfilesEntity();
				perf = (PerfilesEntity)listperfiles.get(i);
                // Habilita el retroceso de una OP a estado Validado.
				if(perf.getIdPerfil().longValue() == Constantes.ID_PERFIL_RETROCEDER_OP_A_VALIDADO){
					FlagPerfilRetrocederOpEstadoValidado = true;
				}				

//+20131107_JMCE: Para modificar fecha de pedido
                // Habilita el retroceso de una OP a estado Validado.
				if(perf.getIdPerfil().longValue() == Constantes.ID_PERFIL_MODIFICAR_FECHA_OP){
					FlagPerfilModificacionFechaOP = true;
				}				
//-20131107_JMCE: Para modificar fecha de pedido					
		
				
                // Habilita la Modificacion Montos con exceso.
				if(perf.getIdPerfil().longValue() == Constantes.ID_PERFIL_MODIFICADOR_EXCESOS_OP){
					FlagPerfilModificadorExceso = true;
				}
				
                // Habilita la ANULACIÓN de una OP al EJECUTIVO BOC sólo hasta el estado VALIDADO inclusive
				if(perf.getIdPerfil().longValue() == Constantes.ID_PERFIL_EJECUTIVO_BOC){
					FlagPerfilEjecutivoBOC = true;
				}

                // Habilita la ANULACIÓN de una OP al ADMINISTRADOR ESTADOS OP sólo hasta el estado EN DESPACHO inclusive
				if(perf.getIdPerfil().longValue() == Constantes.ID_PERFIL_ADMINISTRADOR_ESTADOS_OP){
				    FlagAdminEstadosOP = true;
				}

				// Habilita el Cambio de Estado de una OP sólo al SUPERVISOR BOC
				/*if(perf.getIdPerfil().longValue() == Integer.parseInt(configBundle.getString("id_supervisor_boc"))){
				    FlagPerfilSupervisorBOC = true;
				}*/
			}
			}catch(Exception e){
				logger.debug("error:"+e.getMessage());
			}
		
            if ( Utils.alertarAntesDeReprogramar( pedido.getId_estado() )) {
                top.setVariable("{alertar}", "true");               
            } else {
                top.setVariable("{alertar}", "false");
            }
            
		//condiciones generales de habilitacion de botones
		if ( edicion ) {
            top.setVariable("{hab_no_sustituir}", "enabled");
			if ( Utils.puedeReprogramar( pedido.getId_estado() )) {
				top.setVariable("{hab_desp}", "enabled");
			}
			if(pedido.getId_estado()<=Constantes.ID_ESTAD_PEDIDO_VALIDADO){
				top.setVariable("{hab_dir}", "enabled");
				top.setVariable("{hab_prod}", "enabled");
				top.setVariable("{a_ini}", "<a href='javascript:abre_popup();'>");
				top.setVariable("{a_fin}", "</a>");
				top.setVariable("{hab_polsust}", "enabled");
			}
			if(pedido.getId_estado() <= Constantes.ID_ESTAD_PEDIDO_EN_BODEGA &&
			        FlagPerfilEjecutivoBOC){
				top.setVariable("{hab_anul}", "enabled");
			}
            
            if(FlagAdminEstadosOP && 
			        (pedido.getId_estado() <= Constantes.ID_ESTAD_PEDIDO_FINALIZADO || 
			        pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_PAGO ||
			        pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_PAGO_RECHAZADO) ){
				top.setVariable("{hab_anul}", "enabled");
			}

            if ( pedido.getId_estado() <= Constantes.ID_ESTAD_PEDIDO_EN_BODEGA && 
                    Constantes.TIPO_DOC_FACTURA.equalsIgnoreCase(pedido.getTipo_doc())) {
				top.setVariable("{hab_dpago}", "enabled");				
			}
            
            if(pedido.getId_estado()<=Constantes.ID_ESTAD_PEDIDO_EN_BODEGA){
                top.setVariable("{hab_cdesp}", "enabled");
            }
			
            // dejar desabilitado este boton la proxima instalacion
            if ( ( pedido.getId_estado() <= Constantes.ID_ESTAD_PEDIDO_EN_BODEGA ||
					pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_PAGO_RECHAZADO ) && 
                    ( !"X".equalsIgnoreCase( pedido.getNum_mp() ) ) ) {
				top.setVariable("{hab_mpago}", "enabled");
			}
			if((pedido.getId_estado()<=Constantes.ID_ESTAD_PEDIDO_EN_BODEGA ||
					pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_PAGO_RECHAZADO) && 
					    (pedido.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_VE_CTE) &&
					         !pedido.getMedio_pago().equals("CRE")) ){
				top.setVariable("{hab_mpago_cre}", "enabled");
			}
			if(pedido.getId_estado()<=Constantes.ID_ESTAD_PEDIDO_PAGADO){
				top.setVariable("{hab_ind}", "enabled");
			}
			//en caso que el pedido se encuentre en 'pago rechazado', cambiar a 'en pago'
			if (pedido.getId_estado()==Constantes.ID_ESTAD_PEDIDO_PAGO_RECHAZADO)
				top.setVariable("{hab_enpago}", "enabled");
			
			if((pedido.getId_estado()==Constantes.ID_ESTAD_PEDIDO_EN_PICKING ||
                    //pedido.getId_estado()==Constantes.ID_ESTAD_PEDIDO_EN_PAGO ||
			      pedido.getId_estado()==Constantes.ID_ESTAD_PEDIDO_PAGADO || 
			        pedido.getId_estado()==Constantes.ID_ESTAD_PEDIDO_PAGO_RECHAZADO) && 
			        FlagAdminEstadosOP){ //FlagPerfilSupervisorBOC
			    top.setVariable("{hab_cambest}", "enabled");
		    }
			
	        if ( ( pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_BODEGA ||
                    pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_PAGO ||
                      pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_PAGADO || 
                        pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_PAGO_RECHAZADO) && FlagPerfilModificadorExceso ) {
                       // ( (pedido.isMontoExcedido() || bizDelegate.isOpConExceso(pedido.getId_pedido())) && FlagPerfilModificadorExceso )) {
            		
                    top.setVariable("{hab_mod_prods}", "enabled");
            }
	        

            if ( ( pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_BODEGA ||
                    pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_PAGO) && FlagPerfilRetrocederOpEstadoValidado ) {
                    top.setVariable("{hab_a_validado}", "enabled");
                }           


//+20131107_JMCE: Para modificar fecha de pedido
            if ( ( pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION ||
            		pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_VALIDADO ||
            		pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_BODEGA ||
            		pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_PAGO ||
            		pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_PAGADO || 
            		pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_PICKING || 
            		pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_PAGO_RECHAZADO) && FlagPerfilModificacionFechaOP) {
                    top.setVariable("{hab_mod_fec_op}", "enabled");
                }           
//+20131107_JMCE: Para modificar fecha de pedido          
            

            
            if ( pedido.isAnularBoleta() ) {
                top.setVariable("{hab_anular_boleta}", "enabled");
            }
		}
		
		logger.debug(" **** rc:"+rc);
		if ( rc.equals(Constantes._EX_PSAP_ID_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El código de producto ingresado no existe');</script>" );
		}else if ( rc.equals(Constantes._EX_PSAP_SECTOR_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto no tiene sector asociado');</script>" );
		}else if ( rc.equals(Constantes._EX_OPE_PRECIO_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto no tiene precio asociado');</script>" );
		}else if ( rc.equals(Constantes._EX_OPE_CODBARRA_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto no tiene código de barra asociado');</script>" );
		}else if ( rc.equals(Constantes._EX_OPE_TIENE_ALERTA_ACT) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El pedido tiene alerta(s) activa(s)');</script>" );
		}else if ( rc.equals(Constantes._EX_PROD_DESPUBLICADO) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto tiene estado Despublicado');</script>" );
		}else {
			top.setVariable( "{mns}", "");
		}
		
		/*
		 * Tab de promociones
		 */
		
		List listaPromosPedido = new ArrayList();
		listaPromosPedido = bizDelegate.getPromocionPedidos(id_pedido);
		
		logger.debug("resultado promociones:"+listaPromosPedido);
		logger.debug("total promociones:"+listaPromosPedido.size());
		String VIEWPROMO = "ViewPromoForm?codigo=";
		
		double tot_descto_promocional_pedido=0.0;
		
		for (int i=0; i<listaPromosPedido.size(); i++){
			IValueSet filaprodpromo = new ValueSet();
			ResumenPedidoPromocionDTO prod_en_promo = new ResumenPedidoPromocionDTO();
			prod_en_promo = (ResumenPedidoPromocionDTO)listaPromosPedido.get(i);
			filaprodpromo.setVariable("{cod}"	,String.valueOf(prod_en_promo.getPromo_codigo()));
			filaprodpromo.setVariable("{tipo}"	,String.valueOf(prod_en_promo.getTipo_promo()));
			filaprodpromo.setVariable("{descr}"	,prod_en_promo.getDesc_promo());
//			filaprodpromo.setVariable("{fini}"	,prod_en_promo.getFec_ini());
//			filaprodpromo.setVariable("{ffin}"	,prod_en_promo.getFec_fin());
			filaprodpromo.setVariable("{fini}"	,prod_en_promo.getFec_ini() != null ? prod_en_promo.getFec_ini() : "");
			filaprodpromo.setVariable("{ffin}"	,prod_en_promo.getFec_fin() != null ? prod_en_promo.getFec_fin() : "");
			
			filaprodpromo.setVariable("{desc_prod}"	,prod_en_promo.getDesc_prod());
			
			if (prod_en_promo.getNum_promos()>0){
				filaprodpromo.setVariable("{url_promo}",VIEWPROMO+prod_en_promo.getPromo_codigo());
			}
			else{
				filaprodpromo.setVariable("{url_promo}","javascript:alert('La promocion fue eliminada de Base de datos');");
			}
			/*
			logger.debug(prod_en_promo.getDesc_prod() 
					+ "precio 		: "+prod_en_promo.getPrecio()
					+" precio lista	: "+prod_en_promo.getPrecio_lista());
			
			double dscto_total_proc = (1-(prod_en_promo.getPrecio()/prod_en_promo.getPrecio_lista()))*100;
			
			logger.debug(prod_en_promo.getDesc_prod() + "descto total%  : "+dscto_total_proc);
			
			
			filaprodpromo.setVariable("{dscto_total}",
					Formatos.formatoNumero(dscto_total_proc)+"%"					
					);
			if (prod_en_promo.getPrecio_lista()>prod_en_promo.getPrecio()){
				double descto = prod_en_promo.getPrecio_lista()-prod_en_promo.getPrecio();
				logger.debug(prod_en_promo.getDesc_prod() + "monto dscto total  : "+descto);
				tot_descto_promocional_pedido += descto; 
				filaprodpromo.setVariable("{monto_descto_total}",
					Formatos.formatoPrecio(descto)
					);
			}
			else{
				filaprodpromo.setVariable("{monto_descto_total}",Formatos.formatoPrecio(0.0));
			}*/
			
			tot_descto_promocional_pedido +=prod_en_promo.getMonto_descuento();
			logger.debug("prod_en_promo.getMonto_descuento() FOP: "+ prod_en_promo.getMonto_descuento());
			logger.debug("DESC FOP: " + tot_descto_promocional_pedido);
			filaprodpromo.setVariable("{monto_descto_total}",Formatos.formatoPrecio(prod_en_promo.getMonto_descuento()));			
			//filaprodpromo.setVariable("{id_promocion}"	,String.valueOf(prod_en_promo.getId_promocion()));
			promos.add(filaprodpromo);
		}	
		//sumatoria de descuentos en formato precio $
		top.setVariable("{total_promo}",Formatos.formatoPrecio(tot_descto_promocional_pedido));
		//total de descuentos que se aplicara al pedido (valor de la variable request
		top.setVariable("{monto_dscto_ped}",String.valueOf((long)tot_descto_promocional_pedido));
		
		/*
		 * Muestra tab Sustitutos y Faltantes
		 */

		// Muestra Sustitutos
		List sustheads = new ArrayList();
		List listaSust = bizDelegate.getSustitutosByPedidoId(id_pedido);
		
		for (int i=0; i<listaSust.size(); i++){			
			IValueSet filaSust = new ValueSet();
			SustitutoDTO s = (SustitutoDTO )listaSust.get(i);
			
			if (!sustheads.contains(s.getCod_prod1()+s.getUni_med1())) {
				sustheads.add(s.getCod_prod1()+s.getUni_med1());

				filaSust.setVariable("{cod_prod1}"		,s.getCod_prod1());
				filaSust.setVariable("{desc_prod1}"		,s.getDescr1());			
				filaSust.setVariable("{cant_prod}"	    ,String.valueOf(s.getCant1()));
				filaSust.setVariable("{pr_unit_prod}"	,Formatos.formatoPrecio(s.getPrecio1()));
			} else {
				filaSust.setVariable("{cod_prod1}"		,"");
				filaSust.setVariable("{desc_prod1}"		,"");			
				filaSust.setVariable("{cant_prod}"	    ,"");
				filaSust.setVariable("{pr_unit_prod}"	,"");
			}

			if ( s.getCod_prod2() != null ) {
				filaSust.setVariable("{cod_sust}", s.getCod_prod2());
			} else {
				filaSust.setVariable("{cod_sust}", "");
			}
            filaSust.setVariable("{cri_susti}"   , s.getDescCriterio());
			
			filaSust.setVariable("{desc_sust}"		,s.getDescr2());			
			filaSust.setVariable("{cant_sust}"	    , String.valueOf(s.getCant2()));
			filaSust.setVariable("{pr_unit_sust}"	,Formatos.formatoPrecio(s.getPrecio2()));			
			susts.add(filaSust);
		}
		// Muestra Faltantes
		List listaFalt = bizDelegate.getFaltantesByPedidoId(id_pedido);
		for (int i=0; i<listaFalt.size(); i++) {
			IValueSet filaFalt = new ValueSet();
			FaltanteDTO faltante1 = (FaltanteDTO)listaFalt.get(i);
			filaFalt.setVariable("{cod_prodfal}" ,String.valueOf(faltante1.getCod_producto()));
			filaFalt.setVariable("{desc_fal}"	 ,faltante1.getDescripcion());
			filaFalt.setVariable("{cant_falt}"	 ,String.valueOf(faltante1.getCant_faltante()));
            filaFalt.setVariable("{cri_susti}"   ,faltante1.getDescCriterio());
			faltantes.add(filaFalt);
		}	
		
        /*
         * Muestra el Tab de Bodega 
         */		
		
		List listaBins = new ArrayList();
		listaBins = bizDelegate.getBinsPedidoById(id_pedido);
		List listTipos = bizDelegate.getEstadosByVis("TB","S");
		
		for (int i=0; i<listaBins.size(); i++){			
			IValueSet filabins = new ValueSet();
			BinDTO bin1 = new BinDTO();
			bin1 = (BinDTO)listaBins.get(i);				
			filabins.setVariable("{id_pedido}"	,String.valueOf(id_pedido));
			filabins.setVariable("{id_bp}"		,String.valueOf(bin1.getId_bp()));
			filabins.setVariable("{cod_bin}"	,String.valueOf(bin1.getCod_bin()));
			/*filabins.setVariable("{cod_ubica}"	,String.valueOf(bin1.getCod_ubicacion()));
			if (bin1.getCod_sello1()!= null && !bin1.getCod_sello1().equals("null"))
				filabins.setVariable("{cod_sello1}"	,bin1.getCod_sello1());
			else
				filabins.setVariable("{cod_sello1}"	,Constantes.SIN_DATO);
			if (bin1.getCod_sello2() != null && !bin1.getCod_sello2().equals("null"))
				filabins.setVariable("{cod_sello2}"	,String.valueOf(bin1.getCod_sello2()));
			else
				filabins.setVariable("{cod_sello2}"	,Constantes.SIN_DATO);*/
			if (bin1.getVisualizado() != null &&
			        bin1.getVisualizado().equalsIgnoreCase("S")){
			    filabins.setVariable("{visualizado}", "Sí");
			}else{
			    filabins.setVariable("{visualizado}", "No");
			}
			filabins.setVariable("{cant_prod_audit}", bin1.getCant_prod_audit()+"");
			filabins.setVariable("{auditor}", bin1.getAuditor());
			filabins.setVariable("{tipo}", FormatoEstados.frmEstado(listTipos, bin1.getTipo()));
			bins.add(filabins);
		}		
		
		// Trx. Medio de Pago
		List listaTrxMp = new ArrayList();
		listaTrxMp = bizDelegate.getTrxMpByIdPedido(id_pedido);
		ArrayList trx = new ArrayList();		
		logger.debug("numero de trx:" + listaTrxMp.size());
		for (int i=0; i<listaTrxMp.size(); i++){			
			IValueSet fila = new ValueSet();
			MonitorTrxMpDTO row = new MonitorTrxMpDTO();
			row = (MonitorTrxMpDTO)listaTrxMp.get(i);
			fila.setVariable("{trxmp_nro}"		,String.valueOf(row.getId_trxmp()));
			fila.setVariable("{trxmp_monto}"	,String.valueOf(row.getMonto_trxmp()));
			fila.setVariable("{trxmp_qtyprods}"	,String.valueOf(row.getCant_prods()));
			fila.setVariable("{trxmp_estado}"	,String.valueOf(row.getEstado_nom()));
			if(pedido.getTipo_doc().equals(Constantes.TIPO_DOC_BOLETA)){
				fila.setVariable("{trxmp_tipo_doc}"	, "BOLETA");
			}	
			if(pedido.getTipo_doc().equals(Constantes.TIPO_DOC_FACTURA)){
				fila.setVariable("{trxmp_tipo_doc}"	, "FACTURA");
			}	
			fila.setVariable("{trxmp_num_doc}"	,String.valueOf(row.getNum_doc()));
			fila.setVariable("{id_pedido}"		,id_pedido+"");
			//nuevas columnas
			fila.setVariable("{trxmp_num_caja}"		,String.valueOf(row.getNum_caja()));
			fila.setVariable("{trxmp_pos_monto_fp}"	,String.valueOf(row.getPos_monto_fp()));
			if(	row.getPos_fecha()!=null){ fila.setVariable("{trxmp_pos_fecha}"	,String.valueOf(row.getPos_fecha()));}
			else{	fila.setVariable("{trxmp_pos_fecha}"	,Constantes.SIN_DATO); }
			if(	row.getPos_hora()!=null){ fila.setVariable("{trxmp_pos_hora}"		,String.valueOf(row.getPos_hora()));}
			else{	fila.setVariable("{trxmp_pos_hora}"	,Constantes.SIN_DATO); }
			if(	row.getPos_fp()!=null){ fila.setVariable("{trxmp_pos_fp}"		,String.valueOf(row.getPos_fp()));}
			else{	fila.setVariable("{trxmp_pos_fp}"	,Constantes.SIN_DATO); }
			trx.add(fila);
		}
		//Genera combo de seleccion de motivos
		
		List listaMots = new ArrayList();
		listaMots = bizDelegate.getMotivosPedidoBOC();
		
		for (int i=0; i<listaMots.size(); i++){
			IValueSet filamot = new ValueSet();
			MotivoDTO mot1 = new MotivoDTO();
			mot1 = (MotivoDTO)listaMots.get(i);
			if (i==0){
				filamot.setVariable("{sel}"		,"selected");	
			}
			filamot.setVariable("{id_motivo}"		,String.valueOf(mot1.getId_mot()));
			filamot.setVariable("{nom_motivo}"		,String.valueOf(mot1.getId_mot())+" : "+String.valueOf(mot1.getNombre()));
			filamot.setVariable("{sel}"		,"");
			mots.add(filamot);
		}
		
		
		//genera accion del boton ValidarOP y ConfirmarOP 
		logger.debug("pedido.estado="+pedido.getId_estado());
		top.setVariable("{bot_disabl}","");
		top.setVariable("{hab_v}","disabled");
		top.setVariable("{hab_c}","disabled");
		
		if( (pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_INGRESADO   ||
			 pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION )
		     && edicion ){
			top.setVariable("{nombre_boton_validar}","Validar OP");
			top.setVariable("{accion}","Validar");
			top.setVariable("{hab_v}","enabled");
			top.setVariable("{hab_c}","enabled");
		}else{
			if(pedido.getId_estado()==Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION && edicion ){
				if (bizDelegate.getExisteAlertaActiva(id_pedido)){
					top.setVariable("{nombre_boton_validar}","Validar OP");
					top.setVariable("{accion}","Validar");
					top.setVariable("{hab_v}","enabled");
				}else{
					top.setVariable("{nombre_boton_validar}","Confirmar OP");
					top.setVariable("{accion}","Confirmar");
					top.setVariable("{hab_c}","enabled");
				}
			}else{
				top.setVariable("{bot_disabl}","disabled");
				top.setVariable("{nombre_boton_validar}","Confirmar OP");
				top.setVariable("{accion}","");
			}
		}
		if((pedido.getId_estado()!=Constantes.ID_ESTAD_PEDIDO_INGRESADO) && 
				(pedido.getId_estado()!=Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION) &&
				  (pedido.getId_estado()!=Constantes.ID_ESTAD_PEDIDO_VALIDADO)){
			top.setVariable("{bot_agreg_prod}","disabled");
		}else
			top.setVariable("{bot_agreg_prod}","");
		
		// variables para popups		
		if ( edicion ) {
			top.setVariable("{hab}", "enabled");
			logger.debug("MODO:EDICION");
		}else {
			top.setVariable("{hab}", "disabled");
			logger.debug("MODO:VER");
		}
		
		
		/*
         * Muestra el Tab de Eventos 
         */		
		
		List listaEventos = new ArrayList();
		listaEventos = bizDelegate.getEventosByRutClienteAndPedido(pedido.getRut_cliente(), pedido.getId_pedido());
		
		for (int i = 0; i < listaEventos.size(); i++) {
			IValueSet fila = new ValueSet();
			EventoDTO evento = (EventoDTO) listaEventos.get(i);
			fila.setVariable("{id_evento}" , String.valueOf(evento.getIdEvento()));
			fila.setVariable("{nombre}" , evento.getNombre());
			fila.setVariable("{descripcion}" , evento.getDescripcion());
			fila.setVariable("{fecha_creacion}" , Formatos.frmFecha(evento.getFechaCreacion()));
			fila.setVariable("{fecha_inicio}" , Formatos.frmFecha(evento.getFechaInicio()));
			fila.setVariable("{fecha_fin}" , Formatos.frmFecha(evento.getFechaFin()));
			fila.setVariable("{ocurrencia}" , String.valueOf(evento.getOcurrencia()));
			fila.setVariable("{ocurrencia_por_rut}" , String.valueOf(evento.getOcurrenciaPorRut()));
			fila.setVariable("{tipo_evento}" , evento.getTipoEvento().getNombre());
			fila.setVariable("{estado}" , evento.descripcionEstado());
			fila.setVariable("{orden}" , String.valueOf(evento.getOrden()));
			eventos.add(fila);
		}
		if ( eventos.size() > 0 ) {
			top.setVariable("{msj_eventos}", "");
		}else {
			top.setVariable("{msj_eventos}", "No existen eventos");
		}
				
		top.setVariable("{pop_cambio_direccion}",pop_dir);
		top.setVariable("{pop_editar_indicacion}",pop_ind);
		top.setVariable("{pop_reprog_despcho}",pop_desp);
		top.setVariable("{pop_cambio_mp}",pop_mp);
		top.setVariable("{pop_cambio_mp_cre}",pop_mp_cre);
		top.setVariable("{pop_modif_factura}",pop_mfact);
		top.setVariable("{pop_libera_motivo}",pop_libera_op);
		top.setVariable("{pop_anula_motivo}",pop_anula_op);
		top.setVariable("{pop_cambia_estado_op}",pop_cambia_estado_op);
		top.setVariable("{id_pedido}"		,String.valueOf(id_pedido));
		top.setVariable("{id_sector}"		,String.valueOf(id_sector));
		top.setVariable("{id_cliente}"		,String.valueOf(id_cliente));
		top.setVariable("{mod}"		,	"");
		top.setVariable("{pagina}"		,	String.valueOf(pag));
		
        if ( req.getParameter("id_ruta") != null ) {
            top.setVariable("{link_volver}"     ,"ViewDetRuta?id_ruta="+req.getParameter("id_ruta"));
        } else if ( req.getParameter("origen") != null && String.valueOf(Constantes.LIBERA_OP_ORIGEN_OP_PENDIENTES).equalsIgnoreCase(req.getParameter("origen")) ) {
            top.setVariable("{link_volver}"     ,"ViewMonOpPendientes");
        } else {
            top.setVariable("{link_volver}"		,"ViewMonOP?pagina="+pag);
        }
        
        top.setVariable("{monto_reservado}", Formatos.formatoPrecio(pedido.getMonto_reservado()));
		
//		 6. Setea variables bloques
	    top.setDynamicValueSets("SUSTITUTOS", susts);
	    top.setDynamicValueSets("FALTANTES", faltantes);
	    top.setDynamicValueSets("BODEGA", bins);
	    top.setDynamicValueSets("EVENTOS", eventos);
	    top.setDynamicValueSets("MOTIVOS",mots);
	    top.setDynamicValueSets("PROMOS",promos);	    
	    top.setDynamicValueSets("listado_trxmp", trx);
	    
	    top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now1 = new Date();
		top.setVariable("{hdr_fecha}"	,now1.toString());	
		
		
	    //sirve para llenar las variables del header
        
        top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
        Date now = new Date();
        top.setVariable("{hdr_fecha}"	,now.toString());	
	    
		
		//		 7. Salida Final
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();		
	
	}

}
