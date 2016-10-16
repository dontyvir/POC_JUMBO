package cl.bbr.bol.view;


import java.util.Date;
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
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPedDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPickDTO;
import cl.bbr.jumbocl.pedidos.dto.FPickDTO;
import cl.bbr.jumbocl.pedidos.dto.FormPickSustitutoDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductoCbarraDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega el resumen del formulario manual de picking, además permite finalizar el formulario.
 * @author BBR
 *
 */
public class ViewFormPickingResumen extends Command {

	
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		long id_ronda    = -1;
		long id_local    = -1;
		int haySpick = 0;
		int hayPickPend=0;
		String rc="";
		String mns="";

		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);		
		
		// 2. Procesa parámetros del request
		if ( req.getParameter("id_ronda") != null ){
			id_ronda = Long.parseLong(req.getParameter("id_ronda"));
			logger.debug("id_ronda: " + id_ronda);
		}
		if (req.getParameter("rc")!= null && !req.getParameter("rc").equals("")){
			rc = req.getParameter("rc");
		}
		id_local = usr.getId_local();
		
		// 3. Template
		View salida = new View(res);
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		// 4.  Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate biz = new BizDelegate();
		
//		Mostramos los productos de la ronda
		ProcFormPickDetPedDTO datos_ped = new ProcFormPickDetPedDTO();
		datos_ped.setId_ronda(id_ronda);
		List listProdRonda = biz.getProductosCbarraRonda(datos_ped);
		ArrayList prodRonda = new ArrayList();
		
		for (int i = 0; i < listProdRonda.size(); i++) {	
			
			IValueSet fila = new ValueSet();
			ProductosPedidoDTO pr = (ProductosPedidoDTO)listProdRonda.get(i);
			if (pr.getCant_spick() > 0){
				fila.setVariable("{img}","<img src=\"img/bullet_red.gif\" title=\"Tiene Productos Sin Pickear.\" width=\"11\" height=\"11\">");
				haySpick =1;
			}else if (pr.getCant_faltan() > 0){
				fila.setVariable("{img}","<img src=\"img/bullet_yellow.gif\" title=\"Tiene Faltantes.\" width=\"11\" height=\"11\">");
			}else if (pr.getCant_solic() == pr.getCant_pick()){
				fila.setVariable("{img}","<img src=\"img/bullet_green.gif\" title=\"Producto OK.\" width=\"11\" height=\"11\">");
			}else{
				fila.setVariable("{img}","");
			}
				
			fila.setVariable("{id_producto}",String.valueOf(pr.getId_producto()));
			fila.setVariable("{cod_sap}",pr.getCod_producto());
			fila.setVariable("{desc}"	,pr.getDescripcion());	
			fila.setVariable("{uni_med}"	,pr.getUnid_medida());
			fila.setVariable("{cant_pick}"	,String.valueOf(pr.getCant_pick()));
			fila.setVariable("{cant_ped}"	,String.valueOf(pr.getCant_solic()));
			fila.setVariable("{cant_falt}"	,String.valueOf(pr.getCant_faltan()));
			fila.setVariable("{cant_spick}"	,String.valueOf(pr.getCant_spick()));
			
			prodRonda.add(fila);
			
		}
		
//		 Mostramos los productos pickeados.
		ProcFormPickDetPickDTO datos_pick = new ProcFormPickDetPickDTO();
		datos_pick.setId_ronda(id_ronda);		
		List listadoProd = biz.getDetallePickFormPick(datos_pick);
		
		ArrayList prodPick = new ArrayList();
		
		for (int i = 0; i < listadoProd.size(); i++) {
			IValueSet fila = new ValueSet();
			FPickDTO pick = (FPickDTO)listadoProd.get(i);
			fila.setVariable("{id_ronda}", String.valueOf(id_ronda));
			fila.setVariable("{id_pick}"	   ,String.valueOf(pick.getId_fpick()));
			fila.setVariable("{cod_bin}"	   ,pick.getCod_bin());	
			fila.setVariable("{cant_pickeada}" ,String.valueOf(pick.getCantidad()));
			if (pick.getCant_pend() > 0){
				hayPickPend = 1;
			}
			ProductoCbarraDTO prodCbarra = biz.getProductoByCbarra(pick.getCbarra());
			if (prodCbarra != null ){
				fila.setVariable("{id_prod}" 	,String.valueOf(prodCbarra.getId_prod()));
				fila.setVariable("{cod_sap}"	,prodCbarra.getCod_sap());	
				fila.setVariable("{uni_med}"	,prodCbarra.getUni_med());
				fila.setVariable("{desc}"	    ,prodCbarra.getDescripcion());
			}else{
				fila.setVariable("{id_prod}" 	,"");
				fila.setVariable("{cod_sap}"	,"");	
				fila.setVariable("{uni_med}"	,"");
				fila.setVariable("{desc}"	    ,"");
			}
			prodPick.add(fila);
					
		}
		
		

//		Mostramos los productos sustitutos
		List listProdSust = biz.getFormPickSustitutos(id_ronda,id_local);
		ArrayList prodSust = new ArrayList();
		List sustheads = new ArrayList();
		for (int i = 0; i < listProdSust.size(); i++) {			
			IValueSet fila = new ValueSet();
			FormPickSustitutoDTO sust = (FormPickSustitutoDTO)listProdSust.get(i);
			logger.debug("id_det_ped: " +sust.getId_det_ped() );
			if(!sustheads.contains(String.valueOf(sust.getId_det_ped()))){
				sustheads.add(String.valueOf(sust.getId_det_ped()));
				
				fila.setVariable("{sap_ped}"   ,sust.getSap_ped());
				fila.setVariable("{desc_ped}"  ,sust.getDesc_ped());
				fila.setVariable("{cant_ped}"  ,String.valueOf(sust.getCant_ped()));
				fila.setVariable("{precio_ped}",String.valueOf(sust.getPrecio_ped()));
			}else{
				fila.setVariable("{sap_ped}"   ,"");
				fila.setVariable("{desc_ped}"  ,"");
				fila.setVariable("{cant_ped}"  ,"");
				fila.setVariable("{precio_ped}","");
			}
			fila.setVariable("{id_det}" , String.valueOf(sust.getId_det_ped()));
			fila.setVariable("{sap_sust}"   ,sust.getSap_pick());
			fila.setVariable("{desc_sust}"  ,sust.getDesc_pick());
			fila.setVariable("{cant_sust}"  ,String.valueOf(sust.getCant_pick()));
			fila.setVariable("{precio_sust}",String.valueOf(sust.getPrecio_pick()));
			fila.setVariable("{cant_relped}",String.valueOf(sust.getCant_relped()));
			fila.setVariable("{id_row}"     ,String.valueOf(sust.getId_row()));
			fila.setVariable("{ronda_id}"   ,String.valueOf(id_ronda));
			fila.setVariable("{id_fpick}"   ,String.valueOf(sust.getId_fpick()));
			prodSust.add(fila);
		}
		
//		Mostramos los productos faltantes
		List listProdFalt = biz.getFormPickFaltantes(id_ronda);
		ArrayList prodFalt = new ArrayList();
		for (int i = 0; i < listProdFalt.size(); i++) {			
			IValueSet fila = new ValueSet();
			ProductosPedidoDTO pr = (ProductosPedidoDTO)listProdFalt.get(i);
			fila.setVariable("{id_det}"        ,String.valueOf(pr.getId_detalle()));
			fila.setVariable("{cod_sap_falt}"  ,pr.getCod_producto());
			fila.setVariable("{desc_prod_falt}",pr.getDescripcion());
			fila.setVariable("{cant_falt}"     ,String.valueOf(pr.getCant_faltan()));
			fila.setVariable("{id_ronda}"      ,String.valueOf(id_ronda));
			
			prodFalt.add(fila);
		}

		
	
		// 6. Variables de bloque

		top.setVariable("{id_ronda}", String.valueOf(id_ronda));
		top.setVariable("{haySpick}", String.valueOf(haySpick));
		top.setVariable("{hayPickPend}", String.valueOf(hayPickPend));
		if (rc.equals(Constantes._EX_RON_ID_INVALIDO)){
			mns = "Ronda Invalida";
		}else if (rc.equals(Constantes._EX_VE_FPIK_FINALIZA_MAL)){
			mns = "Error al finalizar el formulario de picking.";
		}else if (rc.equals(Constantes._EX_VE_FPIK_RECEPCIONA_MAL)){
			mns="Error al recepcionar la ronda";
		}else if (rc.equals(Constantes._EX_VE_FPIK_NO_ES_PICKEADOR)){
			mns="No tiene permisos para finalizar picking.";
		}

		top.setVariable("{mns}", mns);
		top.setDynamicValueSets("SUSTITUTOS", prodSust);
		top.setDynamicValueSets("FALTANTES",prodFalt);
		top.setDynamicValueSets("PROD_PED",prodRonda);
		top.setDynamicValueSets("PICKEADOS",prodPick);
		

		// 7. variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
		
		// 8. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}	
	
	
}
