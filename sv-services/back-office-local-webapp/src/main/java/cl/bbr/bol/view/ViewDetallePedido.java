package cl.bbr.bol.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
//(+) INDRA 2012-12-12 (+) 
/*
 * ESTE DESARROLLO LO HICIERON COMO LAS ()$#%"#$$#"$. COMO SE LES OCURRE IMPORTAR CLASES DE UN PROYECTO WEB A OTRO.
 * MALAS PRACTICAS, MALAS PRACTICAS EVERYWHERE
 * ATTE. ESTEBAN AVENDAÑO 
 */
//(-) INDRA 2012-12-12 (-)
import cl.bbr.bol.utils.FormatosVarios;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.BinDTO;
import cl.bbr.jumbocl.pedidos.dto.FaltanteDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorRondasDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorTrxMpDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.pedidos.dto.SustitutoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.cencosud.jumbocl.umbrales.dto.UmbralDTO;

/**
 * despliega el detalle del pedido
 * @author BBR
 */
public class ViewDetallePedido extends Command {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3883491978670208896L;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String 	param_id_pedido	= "";
		long	id_pedido		= -1;
		String 	rc 				= "";
		String 	mensaje_rc 		= "";
		//(+) INDRA 2012-12-12 (+)
		String  id_estado_pedido = "";
		id_estado_pedido = req.getParameter("id_estado");
		logger.debug("ID_ ESTADO PEDIDOD" + id_estado_pedido);
		//(-) INDRA 2012-12-12 (-)
		
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
		if ( req.getParameter("rc") != null ){ rc = req.getParameter("rc"); }
		logger.debug("rc:"+rc);
		if ( req.getParameter("mensaje_rc") != null ){ mensaje_rc = req.getParameter("mensaje_rc"); }
		logger.debug("mensaje_rc:"+mensaje_rc);
		
		param_id_pedido	= req.getParameter("id_pedido");
		id_pedido = Long.parseLong(param_id_pedido);
		logger.debug("id_pedido: " + param_id_pedido);

		// 3. Template (dejar tal cual)
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		// 4.  Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();

		PedidoDTO pedido = bizDelegate.getPedido(id_pedido);
	
		//4.1 Productos 
		List listaProductos = bizDelegate.getProductosPedido(id_pedido);
		logger.debug("numero de productos:" + listaProductos.size());
		ArrayList prod = new ArrayList();
		for (int i=0; i<listaProductos.size(); i++) {
			IValueSet fila = new ValueSet();
			ProductosPedidoDTO producto1 = (ProductosPedidoDTO)listaProductos.get(i);
			fila.setVariable("{cod_prod}", producto1.getCod_producto());
			fila.setVariable("{u_med}"   , producto1.getUnid_medida());
			if ( producto1.getSector() != null ) {
				fila.setVariable("{sector}", producto1.getSector());
			} else {
				fila.setVariable("{sector}", "");
			}
			
			fila.setVariable("{descripcion}", producto1.getDescripcion());
			fila.setVariable("{cant_ped}"   , String.valueOf(Formatos.formatoNum3Dec(producto1.getCant_solic())));
			fila.setVariable("{cant_pick}"  , String.valueOf(Formatos.formatoNum3Dec(producto1.getCant_pick())));
			fila.setVariable("{cant_faltan}", String.valueOf(Formatos.formatoNum3Dec(producto1.getCant_faltan())));
			fila.setVariable("{cant_spick}" , String.valueOf(Formatos.formatoNum3Dec(producto1.getCant_spick())));
			fila.setVariable("{id_prod}"    , String.valueOf(producto1.getId_producto()));

            if (producto1.getObservacion().compareTo("null") != 0 ) {
				fila.setVariable("{observaciones}",producto1.getObservacion());
				logger.debug("Entra donde debiese parsear obs");
			} else {
				fila.setVariable("{observaciones}"	,"");
				logger.debug("Entra donde NO debiese parsear obs");
			}
            
            if ( producto1.getIdCriterio() == 1 ) {
                fila.setVariable("{crit_distinto_jumbo}", "-");
            } else {
                fila.setVariable("{crit_distinto_jumbo}", "Si");    
            }
            if(producto1.getPrecio()==1){
            	fila.setVariable("{ale_img}"	, "<img border='0' src='img/icon41.gif' alt='Producto con valor $1' title='Producto con valor $1'>");
            }else{
            	fila.setVariable("{ale_img}"	, "");
            }
			prod.add(fila);
		}
		
		//4.2 Bins
		List listaBins = new ArrayList();
		listaBins = bizDelegate.getBinsPedido(id_pedido);
		List listTipos = bizDelegate.getEstadosByVis("TB","S");
		ArrayList bins = new ArrayList();
		logger.debug("numero de bins:" + listaBins.size());
		for (int i=0; i<listaBins.size(); i++){
			IValueSet fila = new ValueSet();
			BinDTO bin1 = new BinDTO();
			bin1 = (BinDTO)listaBins.get(i);
			fila.setVariable("{id_pedido}"		,String.valueOf(id_pedido));
			fila.setVariable("{id_bp}"			,String.valueOf(bin1.getId_bp()));
			fila.setVariable("{cod_bin}"		,String.valueOf(bin1.getCod_bin()));
			/*fila.setVariable("{cod_ubicacion}"	,String.valueOf(bin1.getCod_ubicacion()));
			if (bin1.getCod_sello1()!=null && !bin1.getCod_sello1().equals("null"))
				fila.setVariable("{cod_sello_1}"	,String.valueOf(bin1.getCod_sello1()));
			else
				fila.setVariable("{cod_sello_1}"	,Constantes.SIN_DATO);
			if (bin1.getCod_sello2()!=null && !bin1.getCod_sello2().equals("null"))
				fila.setVariable("{cod_sello_2}"	,String.valueOf(bin1.getCod_sello2()));
			else
				fila.setVariable("{cod_sello_2}"	,Constantes.SIN_DATO);*/
			fila.setVariable("{tipo}", FormatosVarios.frmEstado(listTipos, bin1.getTipo()));
			if (bin1.getVisualizado() != null &&
			        bin1.getVisualizado().equalsIgnoreCase("S")){
			    fila.setVariable("{visualizado}", "Sí");
			}else{
			    fila.setVariable("{visualizado}", "No");
			}
			fila.setVariable("{cant_prod_audit}", bin1.getCant_prod_audit()+"");
			fila.setVariable("{auditor}", bin1.getAuditor());
			bins.add(fila);
		}
		
		//4.3 Sustitutos y faltantes
		//		4.3.1 sustitutos
		logger.debug("rescate de faltantes" );
		List listaSustitutos = new ArrayList();
		List sustheads = new ArrayList();
		listaSustitutos = bizDelegate.getSustitutosByPedidoId(id_pedido); 
		ArrayList susts = new ArrayList();
		logger.debug("numero de faltantes:" + listaSustitutos.size());
		for (int i=0; i<listaSustitutos.size(); i++){			
			IValueSet fila = new ValueSet();
			SustitutoDTO s = new SustitutoDTO ();
			s = (SustitutoDTO )listaSustitutos.get(i);
			
			if(!sustheads.contains(s.getCod_prod1()+s.getUni_med1())){
				sustheads.add(s.getCod_prod1()+s.getUni_med1());

				fila.setVariable("{cod_prod1}"		,s.getCod_prod1());
				fila.setVariable("{uni_med1}"		,s.getUni_med1());			
				fila.setVariable("{descr1}"	        ,s.getDescr1());
				fila.setVariable("{cant1}"			,String.valueOf(Formatos.formatoNum3Dec(s.getCant1())));
				fila.setVariable("{obs1}"			,s.getObs1());	
			}else{
				fila.setVariable("{cod_prod1}"		,"");
				fila.setVariable("{uni_med1}"		,"");			
				fila.setVariable("{descr1}"	        ,"");
				fila.setVariable("{cant1}"			,"");
				fila.setVariable("{obs1}"			,"");
			}
			
			
			if(s.getCod_prod2()!=null){
				fila.setVariable("{cod_prod2}"		,s.getCod_prod2());
			}else{
				fila.setVariable("{cod_prod2}"		,"");
			}
			if(s.getUni_med2()!=null){
				fila.setVariable("{uni_med2}"		,s.getUni_med2());
			}else{
				fila.setVariable("{uni_med2}"		,"");
			}
			fila.setVariable("{descr2}"	        ,s.getDescr2());
			fila.setVariable("{cant2}"			,String.valueOf(Formatos.formatoNum3Dec(s.getCant2())));
			fila.setVariable("{id_detalle}"		,String.valueOf(s.getId_detalle_pick1()));
			//if((s.getId_detalle_pick1()!=-1 && s.getPrecio2()!=-1)){ //JSE:05082006
			if( s.getPrecio2() == 0 ){
				fila.setVariable("{comm_edit_iz}", " ");
				fila.setVariable("{comm_edit_de}", " ");
			}else{
				fila.setVariable("{comm_edit_iz}", "<!--");
				fila.setVariable("{comm_edit_de}", "-->");
			}
				
			susts.add(fila);
		}	

		//4.3.2 faltantes
		logger.debug("rescate de faltantes" );
		List listaFaltantes = new ArrayList();
		listaFaltantes = bizDelegate.getFaltantesByPedidoId(id_pedido);
		ArrayList faltantes = new ArrayList();
		logger.debug("numero de faltantes:" + listaFaltantes.size());
		for (int i=0; i<listaFaltantes.size(); i++){			
			IValueSet fila = new ValueSet();
			FaltanteDTO faltante1 = new FaltanteDTO();
			faltante1 = (FaltanteDTO)listaFaltantes.get(i);
			fila.setVariable("{cod_prod}"		,String.valueOf(faltante1.getCod_producto()));
			fila.setVariable("{descripcion}"	,faltante1.getDescripcion());
			fila.setVariable("{cant}"			,String.valueOf(Formatos.formatoNum3Dec(faltante1.getCant_faltante())));							
			faltantes.add(fila);
		}	
		
		//4.4 Rondas del Pedido
		List listaRondas = new ArrayList();
		
        boolean ExisteAuditSust = false;
        
		listaRondas = bizDelegate.getRondasByIdPedido(id_pedido);
		ArrayList rondas = new ArrayList();
		logger.debug("numero de Rondas:" + listaRondas.size());
		for (int i=0; i<listaRondas.size(); i++){
			IValueSet fila = new ValueSet();
			MonitorRondasDTO ronda = new MonitorRondasDTO();
			ronda = (MonitorRondasDTO)listaRondas.get(i);
			fila.setVariable("{id_ronda}", String.valueOf(ronda.getId_ronda()));
			fila.setVariable("{sector}"  , ronda.getSector());
			//fila.setVariable("{cant_prods}", String.valueOf(Formatos.formatoNum3Dec(ronda.getCant_prods())));
			fila.setVariable("{cant_prods}", String.valueOf(Formatos.formatoNumero(ronda.getCant_prods())));
			fila.setVariable("{estado}"    , String.valueOf(ronda.getEstado()));
            fila.setVariable("{EstadoAudSust}", ronda.getEstadoAuditSustitucion());
            
            if (!ExisteAuditSust && ronda.getEstadoAuditSustitucion().equals("S")){
                ExisteAuditSust = true;
            }
            
			rondas.add(fila);
		}
		
        if (ExisteAuditSust){
            top.setVariable("{mensaje_AuditSust}", "<font color=red>Alguna(s) de la(s) siguiente(s) sustitucion(es) fue Auditada</font>");
        }else{
            top.setVariable("{mensaje_AuditSust}", "");
        }
        
		//4.5 Trx. Medio de Pago
		List listaTrxMp = new ArrayList();
		listaTrxMp = bizDelegate.getTrxMpByIdPedido(id_pedido);
		ArrayList trx = new ArrayList();
		logger.debug("numero de trx:" + listaRondas.size());
		for (int i=0; i<listaTrxMp.size(); i++){			
			IValueSet fila = new ValueSet();
			MonitorTrxMpDTO row = new MonitorTrxMpDTO();
			row = (MonitorTrxMpDTO)listaTrxMp.get(i);
			fila.setVariable("{trxmp_nro}"		,String.valueOf(row.getId_trxmp()));
			fila.setVariable("{trxmp_monto}"	,String.valueOf(row.getMonto_trxmp()));
			fila.setVariable("{trxmp_qtyprods}"	,String.valueOf(Formatos.formatoNum3Dec(row.getCant_prods())));
			fila.setVariable("{trxmp_estado}"	,String.valueOf(row.getEstado_nom()));
			fila.setVariable("{id_pedido}"		,param_id_pedido);
			if(pedido.getTipo_doc().equals(Constantes.TIPO_DOC_BOLETA)){
				fila.setVariable("{trxmp_tipo_doc}"	, "BOLETA");
			}
			if(pedido.getTipo_doc().equals(Constantes.TIPO_DOC_FACTURA)){
				fila.setVariable("{trxmp_tipo_doc}"	, "FACTURA");
			}
			fila.setVariable("{trxmp_num_doc}"	,String.valueOf(row.getNum_doc()));
			//nuevas columnas
			fila.setVariable("{trxmp_num_caja}"		,String.valueOf(row.getNum_caja()));
			fila.setVariable("{trxmp_pos_monto_fp}"	,String.valueOf(row.getPos_monto_fp()));
			if(	row.getPos_fecha()!=null){ fila.setVariable("{trxmp_pos_fecha}"	,String.valueOf(row.getPos_fecha()));}
			else{	fila.setVariable("{trxmp_pos_fecha}"	,Constantes.SIN_DATO); }
			if(	row.getPos_hora()!=null){ fila.setVariable("{trxmp_pos_hora}"		,String.valueOf(row.getPos_hora()));}
			else{	fila.setVariable("{trxmp_pos_hora}"	,Constantes.SIN_DATO); }
			if(	row.getPos_fp()!=null){ fila.setVariable("{trxmp_pos_fp}"		,String.valueOf(row.getPos_fp()));}
			else{	fila.setVariable("{trxmp_pos_fp}"	,Constantes.SIN_DATO); }
			
			fila.setVariable( "{disable_print_izq}", "" );
			fila.setVariable( "{disable_print_der}", "" );

			if (pedido.getOrigen().equals("V")){//Origen = Venta Empresa (V)
			    if (pedido.getId_local() != pedido.getId_local_fact()){
			        fila.setVariable( "{disable_print_izq}", "<!--" );
			        fila.setVariable( "{disable_print_der}", "-->" );
			    }
			}

			trx.add(fila);
		}
		
		// 4.4 Alerta por Monto Pickeado +/- 20% Respecto a lo Solicitado por el Cliente
		/*double MontoTotalPedido = 0D;
		MontoTotalPedido = bizDelegate.getMontoTotalDetPedidoByIdPedido(id_pedido);
		
		double MontoTotalPicking = 0D;
		MontoTotalPicking = bizDelegate.getMontoTotalDetPickingByIdPedido(id_pedido);
		
		ParametroDTO parametro = bizDelegate.getParametroByName("PORC_VARIACION_PEDIDO");
		
		double porcSup = 1 + Double.parseDouble(parametro.getValor());
		double porcInf = 1 - Double.parseDouble(parametro.getValor());
		boolean alertaMonto = false;
		
		if (MontoTotalPicking > (MontoTotalPedido*porcSup) ||
		        MontoTotalPicking < (MontoTotalPedido*porcInf)){
		    alertaMonto = true;
		}*/
		
		
		
		// 5. Setea variables del template
//		String tipo_doc = "";
//		if(pedido.getTipo_doc().equals("B")){
//			tipo_doc = "Boleta";
//		}else{
//			tipo_doc = "Factura";
//		}
		top.setVariable("{id_pedido}"    , String.valueOf(pedido.getId_pedido()));
		//(+) INDRA 2012-12-12 (+)
		UmbralDTO  elem1 =  bizDelegate.getPorcenUmbralById(pedido.getId_pedido());
		double porc_unidad = elem1.getU_unidad();
		double porc_monto = elem1.getU_monto();
		//i(-) INDRA 2012-12-12 (-)
        if (pedido.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_CASOS_CTE)) {
            top.setVariable("{info_pedido}", "&nbsp;&nbsp; ( C" + pedido.getPedidoExt().getNroGuiaCaso() + " )");
        } else if (pedido.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_JV_CTE)) {
            top.setVariable("{info_pedido}", "&nbsp;&nbsp; ( JV" + pedido.getPedidoExt().getNroGuiaCaso() + " )");
        } else {
            top.setVariable("{info_pedido}", "");
        }
        
        RutaDTO ruta = bizDelegate.getRutaByPedido(id_pedido);
        
        if ( ruta.getIdRuta() > 0 ) {        
            top.setVariable("{fono_trans}"      ,ruta.getFono().getCodigo() + " " + ruta.getFono().getNumero());
            top.setVariable("{chofer_trans}"    ,ruta.getChofer().getNombre());
            top.setVariable("{patente_trans}"   ,ruta.getPatente().getPatente());
            top.setVariable("{hr_out_local}"    ,Formatos.frmFechaHora( ruta.getFechaSalida() ));
        } else {
            top.setVariable("{fono_trans}"      ,"");
            top.setVariable("{chofer_trans}"    ,"");
            top.setVariable("{patente_trans}"   ,"");
            top.setVariable("{hr_out_local}"    ,"");
        }
        top.setVariable("{hr_in_dom}"       ,Formatos.frmFechaHora( pedido.getPedidoExt().getFcHoraLlegadaDomicilio() ));
        top.setVariable("{hr_out_dom}"      ,Formatos.frmFechaHora( pedido.getPedidoExt().getFcHoraSalidaDomicilio() ));
        
		top.setVariable("{f_ingreso}"    , Formatos.frmFecha(pedido.getFingreso()) + " " + Formatos.frmHoraSola( pedido.getHingreso() ));
		top.setVariable("{f_despacho}"   , Formatos.frmFecha(pedido.getFdespacho()));
		top.setVariable("{h_despacho}"   , Formatos.frmHoraSola( pedido.getHdespacho() ));
		top.setVariable("{h_findespacho}", Formatos.frmHoraSola( pedido.getHfindespacho() ));
		top.setVariable("{f_picking}"    , Formatos.frmFecha(pedido.getFpicking()));
		if ( pedido.getHpicking() != null ) {
		    top.setVariable("{h_picking}"    , Formatos.frmHoraSola( pedido.getHpicking() ));
        } else {
            top.setVariable("{h_picking}"    , "");    
        }
        if ( pedido.getHfinpicking() != null ) {
            top.setVariable("{h_finpicking}" , Formatos.frmHoraSola( pedido.getHfinpicking() ));
        } else {
            top.setVariable("{h_finpicking}" , "");    
        }
		top.setVariable("{local_fact}"   , pedido.getNom_local_fact());
		top.setVariable("{id_jornada}"   , String.valueOf(pedido.getId_jpicking()));
		top.setVariable("{estado}"       , pedido.getEstado());
		top.setVariable("{id_estado}"       ,String.valueOf(pedido.getId_estado()));
		//i(+) INDRA 2012-12-12 (+)
		/** Compara si el id del pedido dentro del for es 70 , si es igual, envia tag html para 
		 * que la variable {imgEstado} y {negritaRoja} se activen , de lo contrario no envia nada ,
		 * {imgEstado} envia icono de alerta
		 * {negritoRojo} envia fuente negrita y color rojo
		 */ 
		
		if((pedido.getId_estado() == 70)){
			   top.setVariable("{imgEstado}","<img src='img/icon41.gif' width='15' height='15'>");
			   top.setVariable("{negritaRojo}","<b><font color='#FF0000'>");
		   }else{
			   top.setVariable("{imgEstado}","");
			   top.setVariable("{negritaRojo}","");
		   }
		/***
		 * fin de validacion Indra
		 */
		top.setVariable("{tot_prods}"    , String.valueOf(pedido.getCant_prods()));
		//(-) INDRA 2012-12-12 (-)
		top.setVariable("{porcent_monto}" , String.valueOf(porc_monto));
		top.setVariable("{porcent_unidad}" , String.valueOf(porc_unidad));
		//indra
		if(pedido.getObservacion()!=null){
			top.setVariable("{observaciones_mp}", pedido.getObservacion());
		}else{
			top.setVariable("{observaciones_mp}", " ");
		}
		
		if(pedido.getTipo_doc().equals(Constantes.TIPO_DOC_BOLETA)){
			top.setVariable("{tipo_doc}", "Boleta");
		} else if (pedido.getTipo_doc().equals(Constantes.TIPO_DOC_FACTURA)){
			top.setVariable("{tipo_doc}", "Factura");
		}
		
		if (pedido.getTipo_despacho() == null){
		    pedido.setTipo_despacho("N");
		}
		if (pedido.getTipo_despacho().equals("N")){
			top.setVariable("{tipo_despacho}", "Normal");
		}else if (pedido.getTipo_despacho().equals("E")){
			top.setVariable("{tipo_despacho}", "Express");
		}else if (pedido.getTipo_despacho().equals("C")){
			top.setVariable("{tipo_despacho}", "Econ&oacute;mico");
		}else if (pedido.getTipo_despacho().equals("R")){
            top.setVariable("{tipo_despacho}", "Retiro en Local");
        }else if (pedido.getTipo_despacho().equals("U")){
            top.setVariable("{tipo_despacho}", "Bajo Umbral");
        }
		
	
		if(bizDelegate.isActivaCorreccionAutomatica()){	
			//OP con exceso y sin credito

			if ( bizDelegate.productoEnOPConSistitutosMxN(pedido.getId_pedido()) && !Constantes.MEDIO_PAGO_LINEA_CREDITO.equalsIgnoreCase( pedido.getMedio_pago() ) ) {
				HttpSession session = req.getSession();  
				session.setAttribute("ERROR_PROMOCION_MXN", String.valueOf(pedido.getId_pedido()));

	            try{
					LogPedidoDTO log = new LogPedidoDTO();
					log.setId_pedido(pedido.getId_pedido());
					log.setLog("[BOL]OP con exceso. [Error con promocion MxN]");
					log.setUsuario(usr.getLogin());
					bizDelegate.addLogPedido(log);
				}catch(BolException e){
					logger.error("Error al guardar log del pediddo",e);			
				}
	            String js = "excesoAccion('mxn')";
	        	top.setVariable("{accion_btn}",js);
				
			 }else if ( bizDelegate.isOpConExceso(pedido.getId_pedido()) && !Constantes.MEDIO_PAGO_LINEA_CREDITO.equalsIgnoreCase( pedido.getMedio_pago() ) ) {
	        	       	
	        		HttpSession session = req.getSession();
	        		bizDelegate.modPedidoExcedido(pedido.getId_pedido(), true);
	        		
		        	if(bizDelegate.isExcesoCorreccionAutomatico(pedido.getId_pedido())){//Exceso se puede corregir automaticamente.        		
		        		session.setAttribute("EXCESO_CORRECCION_AUTOMATICA", String.valueOf(pedido.getId_pedido()));

			            try{
							LogPedidoDTO log = new LogPedidoDTO();
							log.setId_pedido(pedido.getId_pedido());
							log.setLog("[BOL]OP con exceso. [Correccion automatica activada]");
							log.setUsuario(usr.getLogin());
							bizDelegate.addLogPedido(log);
						}catch(BolException e){
							logger.error("Error al guardar log del pediddo",e);			
						}
			            String js = "excesoAccion('c')";
			        	top.setVariable("{accion_btn}",js);
			        	
		        	}else{//Exceso se corregi de forma manual.		        		
		        		session.setAttribute("EXCESO_NOTIFICAR", String.valueOf(pedido.getId_pedido()));
		        		
		  	            try{
		  					LogPedidoDTO log = new LogPedidoDTO();
		  					log.setId_pedido(pedido.getId_pedido());
		  					log.setLog("[BOL]OP con exceso [Reportado via email]");
		  					log.setUsuario(usr.getLogin());
		  					bizDelegate.addLogPedido(log);
		  				}catch(BolException e){
		  					logger.error("Error al guardar log del pediddo",e);			
		  				}
		  	            String js = "excesoAccion('n')";
		  	        	top.setVariable("{accion_btn}", js);
		  	        	//Enviar mail a todos los involucrados que corrigen excesos.  	        	
		        	}
	        	       
	        } else {//OP sin exceso o con credito
	        	bizDelegate.modPedidoExcedido(pedido.getId_pedido(), false);
	            top.setVariable("{accion_btn}", "document.f_genera.submit()");
	        }
		}else{
			if ( bizDelegate.isOpConExceso(pedido.getId_pedido(),false) && !Constantes.MEDIO_PAGO_LINEA_CREDITO.equalsIgnoreCase( pedido.getMedio_pago() ) ) {
			     
	            bizDelegate.modPedidoExcedido(pedido.getId_pedido(), true);
	            try{
					LogPedidoDTO log = new LogPedidoDTO();
					log.setId_pedido(pedido.getId_pedido());
					log.setLog("[BOL]OP con exceso");
					log.setUsuario(usr.getLogin());
					bizDelegate.addLogPedido(log);
				}catch(BolException e){
					logger.error("Error al guardar log del pediddo",e);			
				}
	        	top.setVariable("{accion_btn}", "alert('El botón `Generar Transacción` ha sido bloqueado debido a que el monto final del pedido es superior al monto reservado. Por favor, informe de esta OP a BOC inmediatamente.')");
	        } else {
	        	bizDelegate.modPedidoExcedido(pedido.getId_pedido(), false);
	            top.setVariable("{accion_btn}", "document.f_genera.submit()");
	        }			
		} 
		
        
        if ( pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_BODEGA  ) {
			top.setVariable("{hab_gentrxpago}", "enabled");
        } else {
			top.setVariable("{hab_gentrxpago}", "disabled");
        }
        //(+) INDRA 2012-12-12 (+)
        if ( pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_REVISION_FALTAN  ) {
			top.setVariable("{hab_repickear}", "enabled");
			top.setVariable("{hab_bodega}", "enabled");
        } else {
			top.setVariable("{hab_repickear}", "disabled");
			top.setVariable("{hab_bodega}", "disabled");
        }
        //(-) INDRA 2012-12-12 (-)
		//en caso que el pedido se encuentre en 'pago rechazado', cambiar a 'en pago'
		if (pedido.getId_estado()==Constantes.ID_ESTAD_PEDIDO_PAGO_RECHAZADO)
			top.setVariable("{hab_enpago}", "enabled");
		else
			top.setVariable("{hab_enpago}", "disabled");
		
		//top.setVariable("{MontoTotalPedido}" , MontoTotalPedido + "");
		//top.setVariable("{MontoTotalPicking}", MontoTotalPicking + "");
		//top.setVariable("{alertaMonto}"      , alertaMonto + "");
		
		
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("select_listado_productos", prod);
		top.setDynamicValueSets("select_rondas", rondas);
		top.setDynamicValueSets("select_bodega", bins);
		top.setDynamicValueSets("select_sustitutos", susts);
		top.setDynamicValueSets("select_faltantes", faltantes);
		top.setDynamicValueSets("listado_trxmp", trx);
		
		
		// 7. variables del header
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}" , usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}" , now.toString());
		
		if ( !rc.equals("") ){
			top.setVariable( "{mensaje_rc}", mensaje_rc );
		} else {
			top.setVariable( "{mensaje_rc}", "" );
		}

		//8. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();

	}



}
