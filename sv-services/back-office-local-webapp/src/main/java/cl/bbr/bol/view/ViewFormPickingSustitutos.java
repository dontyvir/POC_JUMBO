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
 * Despliega los productos pedidos y pickeados, permite hacer la relacion entre estos o declararlos faltantes.
 * Muestra el  listado de sustitutos y faltantes.
 * @author BBR
 *
 */
public class ViewFormPickingSustitutos extends Command {

	
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		long id_ronda    = -1;
		String mensaje ="";
		long id_local = -1;
		String rc="";
		
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
		/*if (req.getParameter("msje") != null){
			mensaje = req.getParameter("msje");
		}
		*/if (req.getParameter("rc")!= null){
			rc =req.getParameter("rc"); 
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
		//Mostrar los productos pendientes del detalle de pedido
		ProcFormPickDetPedDTO datos_ped = new ProcFormPickDetPedDTO();
		datos_ped.setId_ronda(id_ronda);
		datos_ped.setSin_pickear(true);
		List listProdRonda = biz.getProductosCbarraRonda(datos_ped);
		ArrayList prodPend = new ArrayList();
		if (listProdRonda.size()>0){
			for (int i = 0; i < listProdRonda.size(); i++) {			
				IValueSet fila = new ValueSet();
				ProductosPedidoDTO pr = (ProductosPedidoDTO)listProdRonda.get(i);
				String sust = "";
				String falt = "";
				if (biz.promoPermiteSustitutos(pr.getId_detalle())){
					sust = Constantes.PROMO_PERMITE_SUSTITUTO;
				}else{
					sust = Constantes.PROMO_NO_PERMITE_SUSTITUTO;
				}
				if (biz.promoPermiteFaltantes(pr.getId_detalle())){
					falt = Constantes.PROMO_PERMITE_FALTANTES;
				}else{
					falt = Constantes.PROMO_NO_PERMITE_FALTANTES;
				}
				fila.setVariable("{detped}"	,pr.getId_detalle()+"-"+pr.getCant_spick()+"-"+pr.getId_pedido()+"-"+pr.getCod_producto()+"-"+pr.getUnid_medida()+"-"+sust+"-"+falt);
				String desc_ped = "";
				int largo_ped = pr.getDescripcion().length();
				if (largo_ped > 20){
					largo_ped = 20;
				}
					
				desc_ped =  pr.getCod_producto() + " - " + pr.getDescripcion().substring(0,largo_ped) +"... - " + pr.getUnid_medida()+"- OP: " + pr.getId_pedido()+" Cant=" + pr.getCant_spick();
				fila.setVariable("{desc_ped}"	,desc_ped);
				prodPend.add(fila);
			}
			top.setVariable("{mje_ped}" , "");
			top.setVariable("{hab_ped}" , "");
			top.setVariable("{hab_btn}" , "");
		}else{
			top.setVariable("{mje_ped}" , "<option value=''>No hay productos para relacionar.</option>");
			top.setVariable("{hab_ped}" , "disabled");
			top.setVariable("{hab_btn}" , "disabled");
		}
		// Mostramos los productos pickeados.
		ProcFormPickDetPickDTO datos = new ProcFormPickDetPickDTO();
		datos.setId_ronda(id_ronda);	
		datos.setPendientes(true);
		List listadoPick = biz.getDetallePickFormPick(datos);
		
		ArrayList pickPend = new ArrayList();
		if (listadoPick.size()> 0){
			for (int i = 0; i < listadoPick.size(); i++) {
				IValueSet fila = new ValueSet();
				FPickDTO prod = (FPickDTO)listadoPick.get(i);
				fila.setVariable("{id_ronda}", String.valueOf(id_ronda));
				String detpick =prod.getId_fpick()+"-"+prod.getCant_pend()+"-"+prod.getId_op()+"-"+prod.getCbarra();
				String desc_pick = "";
				desc_pick = prod.getCbarra()+" ";
				ProductoCbarraDTO prodCbarra = biz.getProductoByCbarra(prod.getCbarra());
				
				if (prodCbarra != null ){
					int largo_pick = prodCbarra.getDescripcion().length();
					if (largo_pick > 20){
						largo_pick = 20;
					}
					detpick +=  "-"+prodCbarra.getCod_sap()+"-"+prodCbarra.getUni_med();
					desc_pick += " - "+prodCbarra.getCod_sap()+" - "+prodCbarra.getDescripcion().substring(0,largo_pick)+"... - "+prodCbarra.getUni_med() ;
				}else{
					detpick += "- - ";
				}
				fila.setVariable("{detpick}" ,detpick);
				desc_pick += " OP: "+prod.getId_op()+ " - Cant=" + prod.getCant_pend();
				fila.setVariable("{desc_pick}" 	,desc_pick);
				pickPend.add(fila);
						
			}
			top.setVariable("{mje_pick}" , "");
			top.setVariable("{hab_pick}" , "");
		}else{
			top.setVariable("{mje_pick}" , "<option value=''>No hay productos para relacionar.</option>");
			top.setVariable("{hab_pick}" , "disabled");
		}
		
		//Mostramos los productos sustitutos
		List listProdSust = biz.getFormPickSustitutos(id_ronda,id_local);
		ArrayList prodSust = new ArrayList();
		List sustheads = new ArrayList();
		if (listProdSust.size() > 0){
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
			top.setVariable("{mje_sust}" , "");
		}else{
			top.setVariable("{mje_sust}" , "No se encontraron productos sustitutos.");
		}
			
		//Mostramos los productos faltantes
		List listProdFalt = biz.getFormPickFaltantes(id_ronda);
		
		ArrayList prodFalt = new ArrayList();
		if (listProdFalt.size() > 0){ 
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
			top.setVariable("{mje_falt}" , "");
		}else{
			top.setVariable("{mje_falt}" , "No se encontraron productos faltantes.");
		}
		
		// 6. Variables de bloque
		top.setVariable("{id_ronda}"	,String.valueOf(id_ronda));
		top.setDynamicValueSets("PEND_PED",prodPend );
		top.setDynamicValueSets("PEND_PICK",pickPend);
		top.setDynamicValueSets("FALTANTES",prodFalt);
		top.setDynamicValueSets("SUSTITUTOS",prodSust);
		
		// 7. variables del header
		if (rc.equals(Constantes._EX_VE_FPIK_NO_REVERSA_SUST) || rc.equals(Constantes._EX_VE_FPIK_NO_EXISTE_RELACION)){
			mensaje = Constantes.FPICK_REVERSA_SUST_MAL;
		}else if (rc.equals(Constantes._EX_VE_FPIK_NO_REALIZO_RELACION)||rc.equals(Constantes._EX_VE_FPIK_ID_DETALLE_INVALIDO)){
			mensaje = Constantes.FPICK_RELACION_MAL;
		}
		//Margen inferior y superior para picking normal de pesables
		top.setVariable("{mar_inf_pes}"	,String.valueOf(Constantes.FORM_PICK_MARGEN_INFERIOR_PESABLES));
		top.setVariable("{mar_sup_pes}"	,String.valueOf(Constantes.FORM_PICK_MARGEN_SUPERIOR_PESABLES));
		
		top.setVariable("{msje}"	,mensaje);
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
