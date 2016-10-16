package cl.bbr.bol.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.utils.FormatosVarios;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.pedidos.dto.BinDTO;
import cl.bbr.jumbocl.pedidos.dto.DespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorTrxMpDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
/**
 * Despliega el detalle del despacho
 * @author BBR
 */
public class ViewDespacho extends Command {

	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String 	param_id_pedido	= "";
		long	id_pedido		= -1;
		
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
		DespachoDTO despacho = bizDelegate.getDespachoById(id_pedido);
		String local_tipo_picking = usr.getLocal_tipo_picking();
		
		//4.1 Bins
		List listaBins = new ArrayList();
		//listaBins = bizDelegate.getBinsPedido(id_pedido);
		if (local_tipo_picking.equals("L")){
		    listaBins = bizDelegate.getBinsPedidoPKL(id_pedido);
		}else{
		    listaBins = bizDelegate.getBinsPedido(id_pedido);
		}
		logger.debug("numero de bins:" + listaBins.size());
		List listTipos = bizDelegate.getEstadosByVis("TB","S");
		ArrayList bins = new ArrayList();
		for (int i=0; i<listaBins.size(); i++){			
			IValueSet fila = new ValueSet();
			BinDTO bin1 = new BinDTO();
			bin1 = (BinDTO)listaBins.get(i);
			fila.setVariable("{cod_bin}"		,String.valueOf(bin1.getCod_bin()));
			/*fila.setVariable("{cod_ubi}"		,String.valueOf(bin1.getCod_ubicacion()));
			fila.setVariable("{cod_sello_1}"	,String.valueOf(bin1.getCod_sello1()));
			fila.setVariable("{cod_sello_2}"	,String.valueOf(bin1.getCod_sello2()));*/
			if (bin1.getVisualizado() != null &&
			        bin1.getVisualizado().equalsIgnoreCase("S")){
			    fila.setVariable("{visualizado}", "Sí");
			}else{
			    fila.setVariable("{visualizado}", "No");
			}
			fila.setVariable("{cant_prod_audit}", bin1.getCant_prod_audit()+"");
			
			if (bin1.getAuditor() != null){
			    fila.setVariable("{auditor}", bin1.getAuditor());
			}else{
			    fila.setVariable("{auditor}", "---");
			}
			
			fila.setVariable("{tipo}", FormatosVarios.frmEstado(listTipos, bin1.getTipo()));	
			bins.add(fila);
		}
		//obtener información del pedido
		PedidoDTO pedido = bizDelegate.getPedido(id_pedido);
		
//		4.2 Trx. Medio de Pago
		List listaTrxMp = new ArrayList();
		listaTrxMp = bizDelegate.getTrxMpByIdPedido(id_pedido);
		ArrayList trx = new ArrayList();	
		
		String trx_tipo_doc;
		
		if(despacho.getTipo_doc().equals(Constantes.TIPO_DOC_BOLETA))
			trx_tipo_doc= "Boleta";
		else
			trx_tipo_doc= "Factura";
		
		for (int i=0; i<listaTrxMp.size(); i++){			
			IValueSet fila = new ValueSet();
			MonitorTrxMpDTO row = new MonitorTrxMpDTO();
			row = (MonitorTrxMpDTO)listaTrxMp.get(i);
			fila.setVariable("{trxmp_nro}"		,String.valueOf(row.getId_trxmp()));
			fila.setVariable("{trxmp_monto}"	,String.valueOf(row.getMonto_trxmp()));
			fila.setVariable("{trxmp_qtyprods}"	,String.valueOf(row.getCant_prods()));
			fila.setVariable("{trxmp_tipodoc}"	,trx_tipo_doc);			
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
			
			trx.add(fila);
		}
		
		// 5. Setea variables del template
		top.setVariable("{id_pedido}"		,String.valueOf(despacho.getId_pedido()));
        if (pedido.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_CASOS_CTE)) {
            top.setVariable("{info_pedido}", "&nbsp;&nbsp; ( C" + pedido.getPedidoExt().getNroGuiaCaso() + " )");
        } else if (pedido.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_JV_CTE)) {
            top.setVariable("{info_pedido}", "&nbsp;&nbsp; ( JV" + pedido.getPedidoExt().getNroGuiaCaso() + " )");
        } else {
            top.setVariable("{info_pedido}", "");
        }
        
        
		if(despacho.getF_ingreso()!=null){
			top.setVariable("{fec_ing}", Formatos.frmFecha( despacho.getF_ingreso() ) + " " + Formatos.frmHoraSola( despacho.getH_ingreso()) );
		}else{
			top.setVariable("{fec_ing}", "");
		}	
		top.setVariable("{fec_desp}"   , Formatos.frmFecha( despacho.getF_despacho() ));
		top.setVariable("{hora_ini}"   , Formatos.frmHoraSola( despacho.getH_ini() ));
		top.setVariable("{hora_fin}"   , Formatos.frmHoraSola( despacho.getH_fin()) );		
		top.setVariable("{estado}"     , despacho.getEstado());
		top.setVariable("{tot_bin_ped}", String.valueOf(despacho.getCant_bins()));
		top.setVariable("{nom_cli}"    , despacho.getNom_cliente());
		
        if ( despacho.getComuna() == null ) {
            top.setVariable("{dir_comuna}", "");
        } else {
            top.setVariable("{dir_comuna}", despacho.getComuna());
        }
        String dir = "";
        if ( despacho.getDir_tipo_calle() != null ) {
            dir += despacho.getDir_tipo_calle();
        }
        if ( despacho.getDir_calle() != null ) {
            dir += " " + despacho.getDir_calle();
        }
        if ( despacho.getDir_numero() != null ) {
            dir += " " + despacho.getDir_numero();
        }
        if ( despacho.getDir_depto() != null ) {
            dir += " " + despacho.getDir_depto();
        }
        top.setVariable("{dir_despacho}",dir);
        if (despacho.getDir_conflictiva() != null &&
                despacho.getDir_conflictiva().equals("1")){
			top.setVariable("{dir_conflictiva}"	          , "<tr><td><font color=red>Direcci&oacute;n Conflictiva</font></td><td><font color=red>S&iacute;</font></td></tr>");
			top.setVariable("{dir_conflictiva_comentario}", "<tr><td><font color=red>Direcci&oacute;n Conflictiva Comentario</font></td><td><font color=red>" + despacho.getDir_conflictiva_comentario() + "</font>");
		}else{
		    top.setVariable("{dir_conflictiva}"	          , "");
		    top.setVariable("{dir_conflictiva_comentario}", "");
		}
        top.setVariable("{dir_zona}", despacho.getZona_despacho());

		
		if(despacho.getId_estado() == Constantes.ID_ESTAD_PEDIDO_PAGADO || 
		     despacho.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_DESPACHO || 
		       despacho.getId_estado() == Constantes.ID_ESTAD_PEDIDO_FINALIZADO){
			top.setVariable("{com_iz}"     , "");	
			top.setVariable("{com_der}"    , "");
			top.setVariable("{enabled_est}", "enabled");		
		}else{
		    if(despacho.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_BODEGA ||
		            despacho.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_PAGO){
				top.setVariable("{com_iz}" , "");	
				top.setVariable("{com_der}", "");			
		    }else{
				top.setVariable("{com_iz}" , "<!--");	
				top.setVariable("{com_der}", "-->");			
		    }
			top.setVariable("{enabled_est}", "disabled");			
		}

/*		if(despacho.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_BODEGA || 
		        (despacho.getId_estado() == Constantes.ID_ESTAD_PEDIDO_PAGADO || 
		         despacho.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_DESPACHO || 
		         despacho.getId_estado() == Constantes.ID_ESTAD_PEDIDO_FINALIZADO || 
		         despacho.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_PAGO) ){
			top.setVariable("{com_iz}"			,"");	
			top.setVariable("{com_der}"			,"");
			top.setVariable("{enabled_est}"		,"enabled");		
		}else{
			top.setVariable("{com_iz}"			,"<!--");	
			top.setVariable("{com_der}"			,"-->");			
			top.setVariable("{enabled_est}"		,"disabled");			
		}
		
		if(despacho.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_BODEGA){
			top.setVariable("{enabled_est}"		,"enabled");
		}
		
		if (despacho.getId_estado()==Constantes.ID_ESTAD_PEDIDO_PAGADO || 
		        despacho.getId_estado()==Constantes.ID_ESTAD_PEDIDO_EN_DESPACHO || 
		        despacho.getId_estado()==Constantes.ID_ESTAD_PEDIDO_FINALIZADO || 
		        despacho.getId_estado()==Constantes.ID_ESTAD_PEDIDO_EN_PAGO){
			top.setVariable("{enabled_est}", "enabled");
		}else{
			top.setVariable("{enabled_est}", "disabled");
		}*/
		
		if(despacho.getZona_descripcion()!=null && !(despacho.getZona_descripcion().equals(""))){
			top.setVariable("{desc_zona}"			," - "+despacho.getZona_descripcion());
		}else{
			top.setVariable("{desc_zona}"			,"");
		}
		/*if (despacho.getEstado().equals("En Bodega") || despacho.getEstado().equals("En Pago") || despacho.getEstado().equals("Pagado") || despacho.getEstado().equals("En Despacho") || despacho.getEstado().equals("Finalizado")){
			top.setVariable("{imprime}", "");
		}
		else{
			top.setVariable("{imprime}", "onClick='return false'");
		}*/
		top.setVariable("{imprime}", "");
		if (listaTrxMp.size()>0){
			top.setVariable("{com_izq_fpago}"	,"");
			top.setVariable("{com_der_fpago}"	,"");
			
			//top.setVariable("{num_doc}"			,String.valueOf(despacho.getPos_boleta()));			
			if(despacho.getTipo_doc().equals(Constantes.TIPO_DOC_BOLETA))
				top.setVariable("{tip_doc}"		,"Boleta");
			if(despacho.getTipo_doc().equals(Constantes.TIPO_DOC_FACTURA))
				top.setVariable("{tip_doc}"		,"Factura");		
			
		}else{
			top.setVariable("{com_izq_fpago}"	,"<!--");
			top.setVariable("{com_der_fpago}"	,"-->");
			//top.setVariable("{num_doc}"		,"");
			top.setVariable("{tip_doc}"		,"");
		}
		
		top.setVariable("{tipo_despacho}" , pedido.getTipo_despacho());
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("select_datos_bodega", bins);
		top.setDynamicValueSets("listado_trxmp", trx);
		// 7. variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	

		/* Inicio - Boton Temporal - Hoja Despacho Pago Rechazado
		 * */		
		ParametroDTO parametroDTO  = bizDelegate.getParametroByName("SHOW_BTN_PAGO_DESPACHO");
		if(parametroDTO.getValor().equalsIgnoreCase("TRUE")){
			boolean isOkTrxPagada = false;
			List tranList = bizDelegate.getTrxMpByIdPedido(id_pedido);
			MonitorTrxMpDTO tran;
			for (int i=0; i<tranList.size(); i++){		
				tran= (MonitorTrxMpDTO) tranList.get(i);
				if(tran.getId_estado()==Constantes.ID_ESTAD_TRXMP_PAGADA){
					isOkTrxPagada = true;
					continue;
				}else{
					isOkTrxPagada = false;
					break;
				}
			}			
			if(despacho.getId_estado() == Constantes.ID_ESTAD_PEDIDO_PAGO_RECHAZADO 
					&& despacho.getMedioPago().equalsIgnoreCase("TBK")
					&& isOkTrxPagada==true){
				top.setVariable("{show_btn_pago_despacho}"	,"");	
			}else{
				top.setVariable("{show_btn_pago_despacho}"	,"display:none");
			}
		}else{
			top.setVariable("{show_btn_pago_despacho}"	,"display:none");
		}
		/* Fin - Boton Temporal - Hoja Despacho Pago Rechazado */
		
		// 8. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
		
		
	
	}
	
}
