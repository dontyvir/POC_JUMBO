package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.clientes.dto.ZonaDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.JorDespachoCalDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.PoliticaSustitucionDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.PedidosCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCotizacionesDTO;

/**
 * despliega el home
 * @author BBRI
 */
public class ViewGeneraPedidos extends Command {
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		View salida = new View(res);
		long id_cotizacion 	= 0;
		long id_jornada		= -1;
		long id_local		= -1;
		long local          = 0;
		long id_local_fact	= -1;
		long jornada        = 0;
		String msje = "";
		String mod="0";
		
		logger.debug("User: " + usr.getLogin());
		
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		
		
		logger.debug("Template: " + html);
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		//parametros
		if (req.getParameter("msje") != null ) msje = req.getParameter("msje");
		if (req.getParameter("id_cotizacion") != null ) 
			id_cotizacion = Long.parseLong(req.getParameter("id_cotizacion"));

		if ( req.getParameter("id_jornada") != null ) 	id_jornada = Long.parseLong(req.getParameter("id_jornada") );
		if ( req.getParameter("id_local") != null ) 	id_local = Long.parseLong(req.getParameter("id_local") );
		if ( req.getParameter("id_local_fact") != null ) 	id_local_fact = Long.parseLong(req.getParameter("id_local_fact") );
		if ( req.getParameter("jornada") != null ) 	jornada = Long.parseLong(req.getParameter("jornada"));
		if ( req.getParameter("local") != null ) 	local = Long.parseLong(req.getParameter("local"));
		if (req.getParameter("mod") != null)mod=req.getParameter("mod");
		//mostrar valores
		logger.debug("id_cotizacion	: "+id_cotizacion);
		logger.debug("id_jornada	: "+id_jornada);
		logger.debug("id_local		: "+id_local);
		logger.debug("id_local_fact	: "+id_local_fact);
		logger.debug("jornada    	: "+jornada);
		logger.debug("local    	: "+local);
		logger.debug("mod    	: "+mod);
		//Obtiene cotizacion del id de cotizacion
		BizDelegate biz = new BizDelegate();
		CotizacionesDTO cot = biz.getCotizacionesById(id_cotizacion);
		
		List lst_ped = biz.getPedidosCotiz(id_cotizacion);
		
		//Listado de productos
		ArrayList productos = new ArrayList();
		List lst_det = biz.getLstProductosByCotizacion(cot.getCot_id());
		for(int i=0; i<lst_det.size(); i++){
			ProductosCotizacionesDTO det = (ProductosCotizacionesDTO) lst_det.get(i);
			IValueSet fila_prod = new ValueSet();
			fila_prod.setVariable("{det_prod_id_bo}", 	String.valueOf(det.getDetcot_proId()));
			fila_prod.setVariable("{det_prod_id_fo}", 	String.valueOf(det.getDetcot_id_fo()));
			fila_prod.setVariable("{det_cod_sap}", 		String.valueOf(det.getDetcot_codSap()));
			fila_prod.setVariable("{det_unid}", 		String.valueOf(det.getDetcot_umed()));
			//fila_prod.setVariable("{det_secc_sap}", String.valueOf(""));
			fila_prod.setVariable("{det_descrip}",		String.valueOf(det.getDetcot_desc()));
			fila_prod.setVariable("{det_cant_sol}",		String.valueOf(det.getDetcot_cantidad()));
			fila_prod.setVariable("{i}",				String.valueOf(i));
			double cant_rest = det.getDetcot_cantidad() - det.getCant_pedido();
			if(cant_rest<0) cant_rest =0;
			logger.debug("Cantidad restante: " + cant_rest);
			fila_prod.setVariable("{det_cant_rest}",	String.valueOf(cant_rest));
			fila_prod.setVariable("{det_pedidos}",		String.valueOf(det.getId_pedido()));
			fila_prod.setVariable("{det_porc_dscto}",	String.valueOf(det.getDetcot_dscto_item()));
			fila_prod.setVariable("{det_precio_lista}",	String.valueOf(det.getDetcot_precio_lista()));
			fila_prod.setVariable("{cant_en_ped}"     ,	String.valueOf(det.getCant_pedido()));
			productos.add(fila_prod);
		}
		int cant_prods = lst_det.size();
		//VER LOS CHECKS
		ArrayList checks = new ArrayList();
		for(int i=0; i<cant_prods; i++){
			IValueSet fila = new ValueSet();
			fila.setVariable("{chkb_i}","chkb_"+i);
			checks.add(fila);
		}
		

		//costo de despacho
		double costo_desp = cot.getCot_costo_desp();

		//listado de pedidos generados para la cotizacion
		ArrayList pedidos = new ArrayList();
		double sum_costo_det = 0;
		for(int i=0; i<lst_ped.size(); i++){
			PedidosCotizacionesDTO ped = (PedidosCotizacionesDTO) lst_ped.get(i);
			IValueSet fila_ped = new ValueSet();
			fila_ped.setVariable("{ped_id}", 		String.valueOf(ped.getPed_id()));
			fila_ped.setVariable("{ped_fec}", 		String.valueOf(ped.getFec_pedido()));
			fila_ped.setVariable("{ped_loc_fact}", 	String.valueOf(ped.getLocal_fact()));
			fila_ped.setVariable("{ped_loc_desp}", 	String.valueOf(ped.getLocal()));
			fila_ped.setVariable("{ped_costo_desp}",String.valueOf(ped.getCosto_desp()));
			fila_ped.setVariable("{ped_estado}",	String.valueOf(ped.getEstado()));
			if(ped.getId_estado()!= Constantes.ID_ESTAD_PEDIDO_ANULADO)
				sum_costo_det += ped.getCosto_desp();
			pedidos.add(fila_ped);
		}
		
		//obtener porcentaje q falte del costo de despacho
		logger.debug("sum_costo_det:"+sum_costo_det);
		logger.debug("costo_desp:"+costo_desp);
		
		double porc_desp_falta = 100 - (Formatos.redondear(sum_costo_det * 100 / costo_desp));
		logger.debug("porc_desp_falta : [100 - ("+sum_costo_det+" * 100 /"+costo_desp+") ] = "+porc_desp_falta);
		logger.debug("porc_desp_falta : [100 - ("+Formatos.redondear(sum_costo_det * 100 / costo_desp)+") ] = "+porc_desp_falta);
		
		if (porc_desp_falta<=0){
			logger.debug("porc_desp_falta : es inválido, se deja automaticamente en 1%");
			porc_desp_falta = 1.0;
		}
		
		//Si viene jornada mayor a 0, se busca el local y la zona relacionada

		if (jornada > 0 ){
			JorDespachoCalDTO jdesp = biz.getJornadaDespachoById(jornada);
			JornadaDTO jp = biz.getJornadaById(jdesp.getId_jpicking());
		
			
			LocalDTO loc = biz.getLocalById(jp.getId_local());
			ZonaDTO zon = biz.getZonaById(jdesp.getId_zona());
			top.setVariable("{local_desp}", "Local Despacho: " +loc.getNom_local());
			top.setVariable("{zona_desp}", "Zona Despacho: "+zon.getNombre());
			if (jp.getId_local() > 0) id_local = jp.getId_local();
			if (id_jornada == -1) id_jornada = jornada;
		}else{
			top.setVariable("{local_desp}", "");
			top.setVariable("{zona_desp}", "");
		}
		
		//listado de locales de despacho
		List lst_loc = biz.getLocales();
		ArrayList locales = new ArrayList();
		for (int i = 0; i < lst_loc.size(); i++) {			
			IValueSet fila = new ValueSet();
			LocalDTO loc1 = (LocalDTO)lst_loc.get(i);
			fila.setVariable("{loc_id}",String.valueOf(loc1.getId_local()));
			fila.setVariable("{loc_nom}",loc1.getNom_local());
			if(id_local==loc1.getId_local())
				fila.setVariable("{sel}","selected");
			else
				fila.setVariable("{sel}","");
			locales.add(fila);
		}
		//listado de locales de facturacion
		ArrayList locales_fact = new ArrayList();
		for (int i = 0; i < lst_loc.size(); i++) {			
			IValueSet fila = new ValueSet();
			LocalDTO loc1 = (LocalDTO)lst_loc.get(i);
			fila.setVariable("{loc_id}",String.valueOf(loc1.getId_local()));
			fila.setVariable("{loc_nom}",loc1.getNom_local());
			if(id_local_fact==loc1.getId_local())
				fila.setVariable("{sel}","selected");
			else
				fila.setVariable("{sel}","");
			locales_fact.add(fila);
		}
		

		
		// 5. Setea variables del template
		top.setVariable("{jorn}"	,String.valueOf(jornada));
		top.setVariable("{loc}"	    ,String.valueOf(local));
		top.setVariable("{cto_desp_asign}"	,String.valueOf(sum_costo_det));
		top.setVariable("{fecha_desp_acordada}"	,Formatos.frmFechaHora(cot.getCot_fec_acordada()));
		top.setVariable("{id_cotizacion}"	,String.valueOf(id_cotizacion));
		if(id_jornada>0)
			top.setVariable("{id_jornada}"		,String.valueOf(id_jornada));
		else
			top.setVariable("{id_jornada}"		,Constantes.CADENA_VACIA);
		
		if (msje!=null)
			top.setVariable("{msje}"		,msje);
		else
			top.setVariable("{msje}"		,"");
		top.setVariable("{costo_desp}"	,String.valueOf(costo_desp));
		top.setVariable("{porc_desp_falta}"	,String.valueOf(porc_desp_falta));
		if(pedidos.size()==0){
			top.setVariable("{msj1}"		,"La consulta no arrojo resultados");
		}else{
			top.setVariable("{msj1}"		,"");
		}
		top.setVariable("{cant_prods}"	,String.valueOf(cant_prods));
		
		logger.debug("Revisa si es supervisor VE");
		List perfiles_usr = new ArrayList();
		perfiles_usr = usr.getPerfiles();
		logger.debug("numero de perfiles:"+perfiles_usr.size());
  		boolean perfil_supervisor_ve=false;
  		for (int i=0;i<perfiles_usr.size();i++){
  			PerfilesEntity perf = new PerfilesEntity();
  			perf= (PerfilesEntity) perfiles_usr.get(i);
  			if (perf.getIdPerfil().intValue()==Constantes.ID_PERFIL_SUPERVISOR_VE){
  				perfil_supervisor_ve=true;
  				}	  			
  			}
  		logger.debug("Revisa si es un pedido especial autorizado por el supervisor VE");
  		logger.debug("Perfil supervisor?="+perfil_supervisor_ve);
		
		if (perfil_supervisor_ve){
			top.setVariable("{hab_sel_tipo_ve}"	,"enabled");
		}
		else{
			top.setVariable("{hab_sel_tipo_ve}"	,"disabled");	
		}
		
		//politicas de sustitucion 		
		List lst_pols = biz.getPolitSustitucionAll();
		ArrayList politicas= new ArrayList();
		for (int i = 0; i < lst_pols.size(); i++) {			
			IValueSet fila = new ValueSet();
			PoliticaSustitucionDTO pol = (PoliticaSustitucionDTO) lst_pols.get(i);
			fila.setVariable("{pol_id}",String.valueOf(pol.getId()));
			fila.setVariable("{pol_desc}",pol.getDescripcion());
						
			if(cot.getCot_pol_id()==pol.getId())
				fila.setVariable("{sel}","selected");
			else
				fila.setVariable("{sel}","");
			politicas.add(fila);
		}
		
		top.setDynamicValueSets("PRODUCTOS", productos);
		top.setDynamicValueSets("PEDIDOS", pedidos);
		top.setDynamicValueSets("LOCALES", locales);
		top.setDynamicValueSets("LOCALES_FACT", locales_fact);
		top.setDynamicValueSets("CHECKS", checks);
		top.setDynamicValueSets("POLITICAS", politicas);
		
		top.setVariable("{mod}",mod);
		// variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
		
		
		String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}
