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
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPedDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcFormPickDetPickDTO;
import cl.bbr.jumbocl.pedidos.dto.BinFormPickDTO;
import cl.bbr.jumbocl.pedidos.dto.FPickDTO;
import cl.bbr.jumbocl.pedidos.dto.FormPickOpDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductoCbarraDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega los productos pickeados.
 *  También permite pickear productos, eliminar picking, agregar nuevos bins
 * @author BBR
 *
 */
public class ViewFormPicking extends Command {

	
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		long id_ronda    = -1;
		String cod_bin   = "";
		String cod_ubica = "";
		long sel_bin     = -1;
		String mje       = "";
		long op          = -1;
		String mns="";
		long id_pick=-1;
		String rc = "";
		long id_pedido=-1;
		String tipo_bin ="";
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
		if ( req.getParameter("cod_bin") != null ){
			cod_bin = req.getParameter("cod_bin");
			logger.debug("cod_bin: " + cod_bin);
		}
		if ( req.getParameter("cod_ubica") != null ){
			cod_ubica = req.getParameter("cod_ubica");
			logger.debug("cod_ubica: " + cod_ubica);
		}
		if ( req.getParameter("sel_bin") != null ){
			sel_bin = Long.parseLong(req.getParameter("sel_bin"));
			logger.debug("sel_bin: " + sel_bin);
		}
		if ( req.getParameter("tipobin") != null ){
			tipo_bin = req.getParameter("tipobin");
			logger.debug("tipobin: " + tipo_bin);
		}
		if ( req.getParameter("msje") != null ){
			mje = req.getParameter("msje");
			logger.debug("msje: " + mje);
		}
		if ( req.getParameter("mns") != null ){
			if (req.getParameter("id_pick") != null)
				id_pick = Long.parseLong(req.getParameter("id_pick"));
			//Tiene relaciones por lo tanto pregunta si esta seguro de revertir picking
			mns = "<script language='JavaScript'>validar_eliminar(\""+req.getParameter("mns") +
					"\",\"ReversaPickingFormPicking?id_pick="+id_pick+
					"&alerta=1&url=\"+escape(\"ViewFormPicking?id_ronda="+id_ronda+"\"));</script>";
			
		}
		
		if (req.getParameter("rc") != null){
			rc = req.getParameter("rc");
		}
		if ( req.getParameter("id_pedido") != null ){
			id_pedido = Long.parseLong(req.getParameter("id_pedido"));
			logger.debug("id_pedido" + id_pedido);
		}
		// 3. Template
		View salida = new View(res);
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		// 4.  Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate biz = new BizDelegate();
		
		//mostramos las Ops pertenecientes a la ronda
		List listadoOP = biz.getPedidosByRonda(id_ronda);
		ArrayList ops = new ArrayList();
		for (int i = 0; i < listadoOP.size(); i++) {			
			IValueSet fila = new ValueSet();
			FormPickOpDTO pedido = (FormPickOpDTO)listadoOP.get(i);				
			fila.setVariable("{id_op}"		,String.valueOf(pedido.getId_pedido()));
			if (id_pedido == pedido.getId_pedido()){
				fila.setVariable("{sel_ped}", "selected");
			}else{
				fila.setVariable("{sel_ped}", "");
			}
			ops.add(fila);
		}
		
		// mostramos los bins disponibles
		int cuenta_bin_pos=0;
		List listadoBins = biz.getBinsFormPickByRondaId(id_ronda);
		ArrayList bins = new ArrayList();
		for (int i = 0; i < listadoBins.size(); i++) {			
			IValueSet fila = new ValueSet();
			BinFormPickDTO bin = (BinFormPickDTO)listadoBins.get(i);				
			fila.setVariable("{id_bin}"		,String.valueOf(bin.getId_fpbin()));
			if (sel_bin == bin.getId_fpbin()){
				fila.setVariable("{sel_bin}"	,"selected");
			}else{
				fila.setVariable("{sel_bin}"	,"");
			}
			fila.setVariable("{cod_bin}"	,bin.getCod_bin());	
			fila.setVariable("{tipo}"		,bin.getTipo());
			fila.setVariable("{cod_ubica}"	,bin.getCod_ubicacion());
			fila.setVariable("{op}"	,String.valueOf(bin.getId_pedido()));
			fila.setVariable("{i}"	,String.valueOf(i));
			
			bins.add(fila);
			cuenta_bin_pos++;
		}
		
		//	Mostramos los productos de la ronda
		ProcFormPickDetPedDTO datos_ped = new ProcFormPickDetPedDTO();
		datos_ped.setId_ronda(id_ronda);
		List listProdRonda = biz.getProductosCbarraRonda(datos_ped);
		ArrayList prodRonda = new ArrayList();
		for (int i = 0; i < listProdRonda.size(); i++) {	
			
			IValueSet fila = new ValueSet();
			ProductosPedidoDTO pr = (ProductosPedidoDTO)listProdRonda.get(i);				
			fila.setVariable("{cbarra}"	,pr.getCod_barra()+"-"+pr.getId_pedido());
			fila.setVariable("{cod_prod}",pr.getCod_producto());
			fila.setVariable("{desc}"	,pr.getDescripcion());	
			fila.setVariable("{umed}"	,pr.getUnid_medida());
			fila.setVariable("{op}"	,String.valueOf(pr.getId_pedido()));
			fila.setVariable("{cant}"	,String.valueOf(pr.getCant_solic()));
			
			prodRonda.add(fila);
			
		}
		
		// Mostramos los productos pickeados.
		ProcFormPickDetPickDTO datos_pick = new ProcFormPickDetPickDTO();
		datos_pick.setId_ronda(id_ronda);		
		List listadoProd = biz.getDetallePickFormPick(datos_pick);
		
		ArrayList prods = new ArrayList();
		
		for (int i = 0; i < listadoProd.size(); i++) {
			IValueSet fila = new ValueSet();
			FPickDTO prod = (FPickDTO)listadoProd.get(i);
			fila.setVariable("{id_ronda}", String.valueOf(id_ronda));
			fila.setVariable("{id_pick}"	   ,String.valueOf(prod.getId_fpick()));
			fila.setVariable("{codbarra}"	   ,prod.getCbarra());	
			fila.setVariable("{cod_bin}"	   ,prod.getCod_bin());	
			fila.setVariable("{cant_pickeada}" ,String.valueOf(prod.getCantidad()));
			ProductoCbarraDTO prodCbarra = biz.getProductoByCbarra(prod.getCbarra());
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
			prods.add(fila);
					
		}
		
		//Revisamos el tipo de flujo asignado al local
		LocalDTO local = new LocalDTO();
		local = biz.getLocalByID(usr.getId_local());
		top.setVariable("{tipo_flujo}", local.getTipo_flujo());
		if (local.getTipo_flujo().equals(Constantes.LOCAL_TIPO_FLUJO_PARCIAL_CTE)){
			top.setVariable("{hab_ubica}", "disabled");
		}else{
			top.setVariable("{hab_ubica}", "enabled");
		}
		
			
		// 6. Variables de bloque
		 if (rc.equals(Constantes._EX_RON_ID_INVALIDO)){
			 mje = "Ronda Invalida";
		 }else if (rc.equals(Constantes._EX_VE_FPIK_OP_RONDA_NO_EXISTE)){
			 mje = "No existe la OP para la ronda.";
		 }
		 else if (rc.equals(Constantes._EX_VE_FPIK_NO_REVERSA_PICK)){
			 mje = Constantes.FPICK_REVERSA_PICK_MAL;
		 }else if (rc.equals(Constantes._EX_VE_FPIK_NO_REALIZO_PICKING)){
			 mje = "No se pudo realizar picking.";
		 }
		
		top.setVariable("{posicion_bin}", String.valueOf(cuenta_bin_pos+1));
		top.setVariable("{id_ronda}", String.valueOf(id_ronda));
		top.setVariable("{cod_bin_ing}", cod_bin);
		top.setVariable("{cod_ubica_ing}", cod_ubica);
		top.setVariable("{op}", "");
		top.setVariable("{op_bn}", String.valueOf(id_pedido));
		top.setVariable("{mensaje}", mje);
		top.setVariable("{mns}", mns);
		if (sel_bin == -1){
			top.setVariable("{dis_rad}", "");
		}else {
			top.setVariable("{dis_rad}", "disabled");
		}
		logger.debug("tipo de bin: " + tipo_bin);
		if (tipo_bin.equals(Constantes.TIPO_BIN_FIJO)){
			top.setVariable("{check_F}"	,"checked");
			top.setVariable("{check_V}"	,"");
		}else if (tipo_bin.equals(Constantes.TIPO_BIN_VIRTUAL)){
			top.setVariable("{check_F}"	,"");
			top.setVariable("{check_V}"	,"checked");
		}else{
			top.setVariable("{check_F}"	,"checked");
			top.setVariable("{check_V}"	,"");
		}
		top.setDynamicValueSets("BINS", bins);
		top.setDynamicValueSets("DET_PICK", prods);
		top.setDynamicValueSets("PROD", prodRonda);
		top.setDynamicValueSets("PEDIDOS", ops);
		
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
