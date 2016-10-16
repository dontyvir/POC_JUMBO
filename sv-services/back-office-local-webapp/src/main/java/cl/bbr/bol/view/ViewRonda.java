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
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.BinDTO;
import cl.bbr.jumbocl.pedidos.dto.FaltanteDTO;
import cl.bbr.jumbocl.pedidos.dto.LogSimpleDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RondaDTO;
import cl.bbr.jumbocl.pedidos.dto.SustitutoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 *  Despliega Detalle de una Ronda
 * @author BBRI
 */
public class ViewRonda extends Command {
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
	
		String param_id_ronda	= "";
		long id_ronda			= -1;
		String rc = "";
		String mns= "";
		String local_tipo_picking= "";
		String direccion         = "";
		//UserDTO user = null;
		
	    local_tipo_picking = usr.getLocal_tipo_picking();
	    if (local_tipo_picking == null){
		    local_tipo_picking = "N"; //Picking Normal (con PDT)
		}
		if (local_tipo_picking.equalsIgnoreCase("N")){//N: Picking Normal
		    direccion = "ViewListadoPicking";
		}else{//L: Picking Light
		    direccion = "ViewListadoPickingPKL";
		}

		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);		
        //
		//String urlBack = (req.getParameter("urlBack")==null?"javascript: history.back();":req.getParameter("urlBack"));
		String urlBack = (req.getParameter("urlBack")==null?"ViewMonitorRondas":req.getParameter("urlBack"));
		
		// 2. Procesa parámetros del request
		if ( req.getParameter("id_ronda") == null ){ 
			throw new ParametroObligatorioException("id_ronda");
		}	
		if ( req.getParameter("rc") != null &&  !req.getParameter("rc").equals("")){ 
			rc =  req.getParameter("rc");
		}
		param_id_ronda = req.getParameter("id_ronda");
		id_ronda = Long.parseLong(param_id_ronda);
		logger.debug("id_ronda = " + req.getParameter("id_ronda"));

		if ( id_ronda == -1)
			throw new ParametroObligatorioException("id_ronda viene vacío");

		// 3. Template (dejar tal cual)
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		// 4.  Rutinas Dinámicas	
		// Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		
		// 4.0 Obtenemos Ronda
		RondaDTO ronda1 = bizDelegate.getRondaById(id_ronda);
		
		// 4.1 Productos	
		List listaProductos = new ArrayList();
		listaProductos = bizDelegate.getProductosRonda(id_ronda);
		
		ArrayList prod = new ArrayList();	
		for (int i=0; i<listaProductos.size(); i++){			
			IValueSet fila = new ValueSet();
			ProductosPedidoDTO producto1 = new ProductosPedidoDTO();
			producto1 = (ProductosPedidoDTO)listaProductos.get(i);
			fila.setVariable("{cod_prod}"		,producto1.getCod_producto());			
			fila.setVariable("{descripcion}"	,producto1.getDescripcion());
			fila.setVariable("{num_op}"			,String.valueOf(producto1.getId_pedido()));
			fila.setVariable("{cant_ped}"		,String.valueOf(Formatos.formatoNum3Dec(producto1.getCant_solic())));
			if(producto1.getObservacion()!=null && !producto1.getObservacion().equals("null")) {
				fila.setVariable("{observacion}"	,producto1.getObservacion());
            } else {
				fila.setVariable("{observacion}"	,"");
            }
			prod.add(fila);
		}
		
		
		//4.2 Sustitutos y faltantes
		
		
		//4.2.1 faltantes
		List listaFaltantes = new ArrayList();
		listaFaltantes = bizDelegate.getFaltantesByRondaId(id_ronda);
		ArrayList faltantes = new ArrayList();
		for (int i=0; i<listaFaltantes.size(); i++){
			IValueSet fila = new ValueSet();
			FaltanteDTO faltante1 = new FaltanteDTO();
			faltante1 = (FaltanteDTO)listaFaltantes.get(i);
			fila.setVariable("{num_op}"			,String.valueOf(faltante1.getId_pedido()));
			fila.setVariable("{cod_prod}"		,String.valueOf(faltante1.getCod_producto()));
			fila.setVariable("{descripcion}"	,faltante1.getDescripcion());
			fila.setVariable("{cant_fal}"		,String.valueOf(Formatos.formatoNum3Dec(faltante1.getCant_faltante())));
            fila.setVariable("{criterio_sustituto}", faltante1.getDescCriterio());
			faltantes.add(fila);
		}
		
		
		//4.2.2 sustitutos
		ArrayList susts = new ArrayList();
		List sustheads = new ArrayList();
		List listaSust = bizDelegate.getSustitutosByRondaId(id_ronda);
		
		for (int i=0; i<listaSust.size(); i++){
			IValueSet filaSust = new ValueSet();
			SustitutoDTO s = (SustitutoDTO ) listaSust.get(i);
			
			logger.debug("sustheads:"+sustheads.size());
			logger.debug("s.getDescr1():"+s.getDescr1());
			
			filaSust.setVariable("{num_op}"		,String.valueOf(s.getId_pedido()));
			
			if(!sustheads.contains(s.getCod_prod1()+s.getUni_med1())){
				sustheads.add(s.getCod_prod1()+s.getUni_med1());

				filaSust.setVariable("{cod_prod1}"		,s.getCod_prod1());
				filaSust.setVariable("{desc_prod1}"		,s.getDescr1());
                filaSust.setVariable("{criterio_sustituto}", "<u>Sustituci&oacute;n sugerida</u>: " + s.getDescCriterio());
                
				filaSust.setVariable("{uni_med1}"		,s.getUni_med1());
				logger.debug("s.getCant1()"+s.getCant1());
				
				filaSust.setVariable("{cant_prod}"	    , String.valueOf(Formatos.formatoNum3Dec(s.getCant1())));
				filaSust.setVariable("{pr_unit_prod}"	,Formatos.formatoPrecio(s.getPrecio1()));
			}else{
				filaSust.setVariable("{cod_prod1}"		,"");
				filaSust.setVariable("{desc_prod1}"		,"");
                filaSust.setVariable("{criterio_sustituto}", "");
				filaSust.setVariable("{uni_med1}"		,"");
				filaSust.setVariable("{cant_prod}"	    ,"");
				filaSust.setVariable("{pr_unit_prod}"	,"");
			}
			if(s.getCod_prod2()!=null){
				filaSust.setVariable("{cod_sust}"		,s.getCod_prod2());
			}else{
				filaSust.setVariable("{cod_sust}"		,"");
			}
			//filaSust.setVariable("{cod_sust}"		,s.getCod_prod2());
			filaSust.setVariable("{desc_sust}"		,s.getDescr2()+"");			
			filaSust.setVariable("{cant_sust}"	    , String.valueOf(Formatos.formatoNum3Dec(s.getCant2())));
			if(s.getUni_med2()!=null){
				filaSust.setVariable("{uni_med_sust}"		,s.getUni_med2());
			}else{
				filaSust.setVariable("{uni_med_sust}"		,"");
			}
			//filaSust.setVariable("{uni_med_sust}"	, s.getUni_med2());
			filaSust.setVariable("{pr_unit_sust}"	,Formatos.formatoPrecio(s.getPrecio2()));			
			susts.add(filaSust);
		}	
		
		
		
		//4.3 Bodega
		List listaBodegas = new ArrayList();
		listaBodegas = bizDelegate.getBinsRonda(id_ronda);
		List listTipos = bizDelegate.getEstadosByVis("TB","S");
		ArrayList bodega = new ArrayList();
		for (int i=0; i<listaBodegas.size(); i++){			
			IValueSet fila = new ValueSet();
			BinDTO bod1 = new BinDTO();
			bod1 = (BinDTO)listaBodegas.get(i);
			fila.setVariable("{num_op}"		,String.valueOf(bod1.getId_pedido()));
			fila.setVariable("{id_bp}"		,String.valueOf(bod1.getId_bp()));
			//fila.setVariable("{num_bin}"	,String.valueOf(bod1.getCod_bin()));
			fila.setVariable("{cod_bin}"	,String.valueOf(bod1.getCod_bin()));
			/*if (bod1.getCod_sello1()!= null && !bod1.getCod_sello1().equals("null"))
				fila.setVariable("{cod_sello}"	,bod1.getCod_sello1());
			else
				fila.setVariable("{cod_sello}"	,Constantes.SIN_DATO);
			if (bod1.getCod_sello2() != null && !bod1.getCod_sello2().equals("null"))
				fila.setVariable("{cod_sello2}"	,String.valueOf(bod1.getCod_sello2()));
			else
				fila.setVariable("{cod_sello2}"	,Constantes.SIN_DATO);*/
			
			//fila.setVariable("{cod_sello}"	,String.valueOf(bod1.getCod_sello1()));
			//fila.setVariable("{cod_sello2}"	,String.valueOf(bod1.getCod_sello2()));
			//fila.setVariable("{cod_ubi}"	,String.valueOf(bod1.getCod_ubicacion()));
			fila.setVariable("{tipo}", FormatosVarios.frmEstado(listTipos, bod1.getTipo()));
			if (bod1.getVisualizado() != null &&
			        bod1.getVisualizado().equalsIgnoreCase("S")){
			    fila.setVariable("{visualizado}", "Sí");
			}else{
			    fila.setVariable("{visualizado}", "No");
			}
			fila.setVariable("{cant_prod_audit}", bod1.getCant_prod_audit()+"");
			fila.setVariable("{auditor}", bod1.getAuditor());
			bodega.add(fila);
		}
		
		//4.5 tracking
		List listatracking = new ArrayList();
		listatracking = bizDelegate.getLogRonda(id_ronda);
		ArrayList tracking = new ArrayList();
		for (int i=0;i<listatracking.size(); i++){
			IValueSet fila = new ValueSet();
			LogSimpleDTO track1 = new LogSimpleDTO();
			track1 = (LogSimpleDTO)listatracking.get(i);
			fila.setVariable("{fecha}"			, Formatos.frmFecha(track1.getFecha()));
			fila.setVariable("{usuario}"		,track1.getUsuario());
			fila.setVariable("{descripcion}"	,track1.getDescripcion());						
			tracking.add(fila);
		}
				
		// 5. Setea variables del template
		top.setVariable("{id_ronda}", String.valueOf(ronda1.getId_ronda()));
		top.setVariable("{estado}"  , ronda1.getEstado());
		if (local_tipo_picking.equals("L")){
		    top.setVariable("{sector}", Constantes.SECTOR_TIPO_PICKING_LIGHT_TXT);
		}else{
		    top.setVariable("{sector}", ronda1.getSector());
		}
		
		top.setVariable("{cant_prod}", String.valueOf(Formatos.formatoNum3Dec(ronda1.getCant_prods())));
		top.setVariable("{fec_crea}" , Formatos.frmFechaHora(ronda1.getF_creacion()));
		//if(ronda1.getF_asignacion()!=null){
		//	top.setVariable("{fec_asig}"		,Formatos.frmFechaHora(ronda1.getF_asignacion()));
		//}else{
		//	top.setVariable("{fec_asig}"		,"");
		//}	
		String nom_pickeador = "";
		if (ronda1.getPickeador() != 0 ){
			// Obtengo el Nombre del pickeador, a través del id_usuario
			UserDTO Pickeador = bizDelegate.getUserById(ronda1.getPickeador());
			nom_pickeador = Pickeador.getNombre()+" " + Pickeador.getApe_paterno()+" " + Pickeador.getApe_materno();
		}
		
		top.setVariable("{pickeador}",nom_pickeador);
		top.setVariable("{direccion}", direccion);
		
		//Obtenemos el tipo de flujo del local
		LocalDTO local = new LocalDTO();
		local = bizDelegate.getLocalByID(usr.getId_local());
		String tipoFlujo =""; 
		tipoFlujo=local.getTipo_flujo();
		
		logger.debug("Tipo de Flujo: " + tipoFlujo);
		if(ronda1.getFini_picking()!= null)
			top.setVariable("{fec_ini}"			,Formatos.frmFechaHora(ronda1.getFini_picking()));
		else
			top.setVariable("{fec_ini}"			," ");
		
		if(ronda1.getFfin_picking()!= null){
			top.setVariable("{fec_fin}"	,Formatos.frmFechaHora(ronda1.getFfin_picking() ));
		}else{
			top.setVariable("{fec_fin}"			," ");
		}	
		logger.debug("fecha de inicio: " +ronda1.getFini_picking());
		logger.debug("fecha de fin: " +ronda1.getFfin_picking());

		// deshabilita botón Imprime Listado Picking si el estado es Creada (para que inicialice la ronda)
		if ( ronda1.getId_estado() == Constantes.ID_ESTADO_RONDA_CREADA ) 
			top.setVariable("{disable1}", "disabled");
		else
			top.setVariable("{disable1}", "");

		// habilita botón preparar ronda sólo si está en estado Ronda Creada
		if ( ronda1.getId_estado() == Constantes.ID_ESTADO_RONDA_CREADA ) 
			top.setVariable("{disable2}", "");
		else
			top.setVariable("{disable2}", "disabled");
		
		
		if (local_tipo_picking.equalsIgnoreCase("N")){
		    top.setVariable("{dis_fpl}", "disabled");
		}else{
		    top.setVariable("{dis_fpl}", "");
		}
		
		if (ronda1.getId_estado() == Constantes.ID_ESTADO_RONDA_FINALIZADA){
			top.setVariable("{dis_fp}", "disabled");
			top.setVariable("{dis_fpl}", "disabled");
		}else{
			top.setVariable("{dis_fp}", "");
			top.setVariable("{dis_fpl}", "");
		}
		//	Habilita el boton imprimir etiquetas solo si la ronda esta en estado
		//  terminada y el local tiene flujo parcial
		if (ronda1.getId_estado() == Constantes.ID_ESTADO_RONDA_FINALIZADA
           && tipoFlujo.equals(Constantes.LOCAL_TIPO_FLUJO_PARCIAL_CTE) ) {
			top.setVariable("{dis_ie}", "");
		}else{
			top.setVariable("{dis_ie}", "disabled");
		}	
		
		//muestra el tipo especial
		if (ronda1.getTipo_ve().equals(Constantes.TIPO_VE_SPECIAL_CTE)){
			top.setVariable("{ini_ve_esp}", "");
			top.setVariable("{fin_ve_esp}", "");
		}
		else{
			top.setVariable("{ini_ve_esp}", "<!--");
			top.setVariable("{fin_ve_esp}", "-->");
		}
		if (rc.equals(Constantes._EX_RON_ESTADO_INADECUADO)){
			mns="Ronda en estado inadecuado";
		}else if (rc.equals(Constantes._EX_RON_ID_INVALIDO)){
			mns="Ronda Invalida.";
		}
		top.setVariable("{mns}", mns);
		
		
		// 6. Setea variables bloques
		//top.setDynamicValueSets("listado", datos);
		top.setDynamicValueSets("select_producto", prod);
		top.setDynamicValueSets("select_faltante", faltantes);
		top.setDynamicValueSets("select_sust_y_faltantes", susts);
		top.setDynamicValueSets("select_ubicacion_bodega", bodega);
		top.setDynamicValueSets("select_tracking", tracking);		
		
	
		// 7. variables del header
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}" , usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}" , now.toString());
		top.setVariable("{urlBack}"  , urlBack);
		
		// 8. Salida Final (se deja tal cual)
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	

	}
	
	
}
