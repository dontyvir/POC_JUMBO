package cl.bbr.bol.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.model.SemanasEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.parametros.dto.ParametroFoDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaCriteria;
import cl.bbr.jumbocl.pedidos.dto.MonitorJornadasDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Formulario que permite agregar una jornada de despacho
 * @author mleiva
 * Parámetros que recibe en el get/post:
 */
public class ViewAgregaJorDespacho extends Command {

 

 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr)
         throws Exception {
	 
	 	String paramId_semana = "";
	 	String paramFecha     = "";
	 	String paramId_zona   = "";
        
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		String msjeDespacho = getServletConfig().getInitParameter("msjeDespacho");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		// 2.1 Parámetro id_semana (Obligatorio)
		if ( req.getParameter("id_semana") == null){
			throw new ParametroObligatorioException("id_semana es null");
		}
		paramId_semana = req.getParameter("id_semana");
		logger.debug("id_semana: " + paramId_semana);
		
		long id_semana = Long.parseLong(paramId_semana);
		
		
		if ( req.getParameter("fecha") == null){
			paramFecha = "";
		}
		paramFecha = req.getParameter("fecha");
		logger.debug("paramFecha: " + paramFecha);
		Date fecha = new Date();
		if ( req.getParameter("fecha") != null ){
			try {
				fecha = new SimpleDateFormat("yyyy-MM-dd").parse(paramFecha);
				logger.debug("fecha formateada: " + paramFecha);	
				logger.debug("fecha en Date: -->"+fecha);
			}catch(Exception E){
				logger.debug("se cae, todo mal" );
			}
		}else{
		    logger.debug("parámetro fecha vacío, se utilizará la fecha de hoy");	
		}
	
		Calendar cal1 = new GregorianCalendar();
		cal1.setFirstDayOfWeek(Calendar.MONDAY);
		logger.debug("cal1: " + cal1.toString());
		
		cal1.setTime(fecha);
		cal1.getTime();
		
		int year = cal1.get(Calendar.YEAR);
		int woy	 = cal1.get(Calendar.WEEK_OF_YEAR);
		logger.debug("Week of year: " + woy);
		logger.debug("Year: " + year);
		if ( req.getParameter("id_zona") == null){
			throw new ParametroObligatorioException("id_zona es null");			
		}
		paramId_zona = req.getParameter("id_zona");
		logger.debug("id_zona: " + paramId_zona);	
		
		long id_zona = Long.parseLong(paramId_zona);
		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
			BizDelegate bizDelegate = new BizDelegate();
		// 4.1 genera Listados de horas
			List horas = new ArrayList();			
			for (int i = 0; i<24 ;i++){				
				IValueSet fila = new ValueSet();
			
				if(i<=9){
					fila.setVariable("{h_inicio}","0"+i);
					fila.setVariable("{h_fin}","0"+i);
				}
				else{
					fila.setVariable("{h_inicio}",i+"");
					fila.setVariable("{h_fin}",i+"");
				}
				if (i==8){
					fila.setVariable("{sel}","selected");
				}
				else{
					fila.setVariable("{sel}","");
				}
				horas.add(fila);
			}
			
			SemanasEntity sem = new SemanasEntity();
			sem = bizDelegate.getSemanaById(id_semana);
		// 4.2 genera Listados Jornada Picking relacionadas
			JornadaCriteria criterio = new JornadaCriteria();
			if(!paramFecha.equals("") ){
				criterio.setF_jornada(paramFecha);
			}
			List jornada_picking = bizDelegate.getJornadasPickingByCriteria(criterio,usr.getId_local()); 
			ArrayList datos = new ArrayList();
			for (int i = 0; i < jornada_picking.size(); i++) {
				IValueSet fila = new ValueSet();
				MonitorJornadasDTO jor1 = (MonitorJornadasDTO)jornada_picking.get(i);				
				fila.setVariable("{id_jornada}"	,String.valueOf(jor1.getId_jornada()));
				fila.setVariable("{fecha}"		,"fecha de la jornada picking");
				fila.setVariable("{h_ini}"		,jor1.getHinicio());
				fila.setVariable("{h_fin}"		,jor1.getHfin());
				datos.add(fila);
			}
			
			Date fecha_domingo_ant	= new Date();
			Date fecha_lunes 		= new Date();
			Date fecha_martes 		= new Date();
			Date fecha_miercoles	= new Date();
			Date fecha_jueves 		= new Date();
			Date fecha_viernes 		= new Date();
			Date fecha_sabado 		= new Date();
			Date fecha_domingo 		= new Date();

			long id_jpicking_lu		= -1;
			long id_jpicking_ma		= -1;
			long id_jpicking_mi		= -1;
			long id_jpicking_ju		= -1;
			long id_jpicking_vi		= -1;
			long id_jpicking_sa		= -1;
			long id_jpicking_do		= -1;
			
			
				
		// 5. Setea variables del template		
			logger.debug("cal1: " + cal1.toString());
			
			cal1.setTime(sem.getF_ini());
			cal1.getTime();
			
			top.setVariable("{f_ini}"		,sem.getF_ini().toString());
			top.setVariable("{f_fin}"		,sem.getF_fin().toString());
			String strFecha;
			cal1.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
			strFecha = new SimpleDateFormat("dd/MM/yyyy").format(cal1.getTime()); 
			top.setVariable("{f_lu}"		, strFecha);
			fecha_lunes = new SimpleDateFormat("dd/MM/yyyy").parse(strFecha);
			
			cal1.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
			strFecha = new SimpleDateFormat("dd/MM/yyyy").format(cal1.getTime());		
			top.setVariable("{f_ma}"		, strFecha);
			fecha_martes = new SimpleDateFormat("dd/MM/yyyy").parse(strFecha);
			
			cal1.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
			strFecha = new SimpleDateFormat("dd/MM/yyyy").format(cal1.getTime());
			top.setVariable("{f_mi}"		, strFecha);
			fecha_miercoles = new SimpleDateFormat("dd/MM/yyyy").parse(strFecha);
			
			cal1.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
			strFecha = new SimpleDateFormat("dd/MM/yyyy").format(cal1.getTime());
			top.setVariable("{f_ju}"		, strFecha);
			fecha_jueves = new SimpleDateFormat("dd/MM/yyyy").parse(strFecha);
			
			cal1.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
			strFecha = new SimpleDateFormat("dd/MM/yyyy").format(cal1.getTime());
			top.setVariable("{f_vi}"		, strFecha);
			fecha_viernes = new SimpleDateFormat("dd/MM/yyyy").parse(strFecha);
			
			cal1.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
			strFecha = new SimpleDateFormat("dd/MM/yyyy").format(cal1.getTime());
			top.setVariable("{f_sa}"		, strFecha);
			fecha_sabado = new SimpleDateFormat("dd/MM/yyyy").parse(strFecha);
			
			cal1.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
			strFecha = new SimpleDateFormat("dd/MM/yyyy").format(cal1.getTime());
			top.setVariable("{f_do}"		, strFecha);
			fecha_domingo = new SimpleDateFormat("dd/MM/yyyy").parse(strFecha);
			
			// 5.2 Genera jornadas para cada día
			
			// Lunes
			//					
			cal1.setTime(fecha_lunes);
			cal1.getTime();
			cal1.add(Calendar.DATE,-1);
			fecha_domingo_ant = cal1.getTime();
			//String strFecha ="";
			strFecha = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime());
			
			logger.debug("fecha domingo ant: " + fecha_domingo_ant);
			
			// TODO: buscar un metodo más eficiente...
			List jornada_picking_do_ant = bizDelegate.getJornadasPickingByFecha(fecha_domingo_ant,usr.getId_local());
			List jornada_picking_lu 	= bizDelegate.getJornadasPickingByFecha(fecha_lunes,usr.getId_local());
			List jornada_picking_ma 	= bizDelegate.getJornadasPickingByFecha(fecha_martes,usr.getId_local());
			List jornada_picking_mi 	= bizDelegate.getJornadasPickingByFecha(fecha_miercoles,usr.getId_local());
			List jornada_picking_ju 	= bizDelegate.getJornadasPickingByFecha(fecha_jueves,usr.getId_local());
			List jornada_picking_vi 	= bizDelegate.getJornadasPickingByFecha(fecha_viernes,usr.getId_local());
			List jornada_picking_sa 	= bizDelegate.getJornadasPickingByFecha(fecha_sabado,usr.getId_local());
			List jornada_picking_do 	= bizDelegate.getJornadasPickingByFecha(fecha_domingo,usr.getId_local());
			/*	
			logger.debug("size=" + jornada_picking_lu.size());
			logger.debug("fecha: " + fecha_lunes.toString());
			logger.debug("size=" + jornada_picking_ma.size());
			logger.debug("fecha: " + fecha_martes.toString());
			logger.debug("size=" + jornada_picking_mi.size());
			logger.debug("fecha: " + fecha_miercoles.toString());
			logger.debug("size=" + jornada_picking_ju.size());
			logger.debug("fecha: " + fecha_jueves.toString());
			logger.debug("size=" + jornada_picking_vi.size());
			logger.debug("fecha: " + fecha_viernes.toString());
			logger.debug("size=" + jornada_picking_sa.size());
			logger.debug("fecha: " + fecha_sabado.toString());
			logger.debug("size=" + jornada_picking_do.size());
			logger.debug("fecha: " + fecha_domingo.toString());
			*/		
			
			// Listas que contienen los IValueSet para el template
			ArrayList jornadas_lunes 	= new ArrayList();
			ArrayList jornadas_martes 	= new ArrayList();
			ArrayList jornadas_miercoles = new ArrayList();
			ArrayList jornadas_jueves 	= new ArrayList();
			ArrayList jornadas_viernes 	= new ArrayList();
			ArrayList jornadas_sabado 	= new ArrayList();
			ArrayList jornadas_domingo 	= new ArrayList();
			
			// Iteramos las jornadas del domingo anterior
			if(jornada_picking_do_ant.size()>0){
				for (int i = 0; i < jornada_picking_do_ant.size(); i++) {
					IValueSet fila_lu = new ValueSet();
					MonitorJornadasDTO jor1 = (MonitorJornadasDTO) jornada_picking_do_ant.get(i);
					fila_lu.setVariable("{id_jornada}", String.valueOf(jor1.getId_jornada()));
					fila_lu.setVariable("{fecha}", strFecha);
					fila_lu.setVariable("{h_ini}", jor1.getHinicio());
					fila_lu.setVariable("{h_fin}", jor1.getHfin());
					if ( jor1.getId_jornada() == id_jpicking_lu )
						fila_lu.setVariable("{sel_lu}", "selected");
					else
						fila_lu.setVariable("{sel_lu}", "");				
					jornadas_lunes.add(fila_lu); // las agregamos al lunes
				}
				top.setVariable("{disabled_button}"	, 	"enabled");
				top.setVariable("{msje_despacho}"	, 	"");
			}else{
				top.setVariable("{disabled_button}"	, 	"disabled");
				top.setVariable("{msje_despacho}"	, 	msjeDespacho);
			}
			// Iteramos las jornadas del lunes
			if(jornada_picking_lu.size()>0){
				for (int i = 0; i < jornada_picking_lu.size(); i++) {
					IValueSet fila_lu = new ValueSet();
					strFecha = new SimpleDateFormat("yyyy-MM-dd").format(fecha_lunes);
					MonitorJornadasDTO jor1 = (MonitorJornadasDTO) jornada_picking_lu.get(i);
					fila_lu.setVariable("{id_jornada}", String.valueOf(jor1.getId_jornada()));
					fila_lu.setVariable("{fecha}", strFecha);
					fila_lu.setVariable("{h_ini}", jor1.getHinicio());
					fila_lu.setVariable("{h_fin}", jor1.getHfin());
					
					jornadas_lunes.add(fila_lu); // las agregamos al lunes
					jornadas_martes.add(fila_lu); // las agregamos al martes
				}
				top.setVariable("{disabled_button}"	, 	"enabled");
				top.setVariable("{msje_despacho}"	, 	"");
			}else{
				top.setVariable("{disabled_button}"	, 	"disabled");
				top.setVariable("{msje_despacho}"	, 	msjeDespacho);
			}
			// Iteramos las jornadas del martes
			if(jornada_picking_ma.size()>0){
				for (int i = 0; i < jornada_picking_ma.size(); i++) {
					IValueSet fila = new ValueSet();
					strFecha = new SimpleDateFormat("yyyy-MM-dd").format(fecha_martes);
					MonitorJornadasDTO jor1 = (MonitorJornadasDTO) jornada_picking_ma.get(i);
					fila.setVariable("{id_jornada}", String.valueOf(jor1.getId_jornada()));
					fila.setVariable("{fecha}", strFecha);
					fila.setVariable("{h_ini}", jor1.getHinicio());
					fila.setVariable("{h_fin}", jor1.getHfin());
					jornadas_martes.add(fila); // las agregamos al martes
					jornadas_miercoles.add(fila); // las agregamos al miercoles
				}
				top.setVariable("{disabled_button}"	, 	"enabled");
				top.setVariable("{msje_despacho}"	, 	"");
			}else{
				top.setVariable("{disabled_button}"	, 	"disabled");
				top.setVariable("{msje_despacho}"	, 	msjeDespacho);
			}
			// Iteramos las jornadas del miercoles
			if(jornada_picking_mi.size()>0){	
				for (int i = 0; i < jornada_picking_mi.size(); i++) {
					logger.debug("-->i=" + i);
					IValueSet fila = new ValueSet();
					strFecha = new SimpleDateFormat("yyyy-MM-dd").format(fecha_miercoles);
					MonitorJornadasDTO jor1 = (MonitorJornadasDTO) jornada_picking_mi.get(i);
					fila.setVariable("{id_jornada}", String.valueOf(jor1.getId_jornada()));
					fila.setVariable("{fecha}", strFecha);
					fila.setVariable("{h_ini}", jor1.getHinicio());
					fila.setVariable("{h_fin}", jor1.getHfin());
					jornadas_miercoles.add(fila); // las agregamos al miercoles
					jornadas_jueves.add(fila); // las agregamos al jueves			
				}
				top.setVariable("{disabled_button}"	, 	"enabled");
				top.setVariable("{msje_despacho}"	, 	"");
			}else{
				top.setVariable("{disabled_button}"	, 	"disabled");
				top.setVariable("{msje_despacho}"	, 	msjeDespacho);
			}	
			
			// Iteramos las jornadas del Jueves
			if(jornada_picking_ju.size()>0){
				for (int i = 0; i < jornada_picking_ju.size(); i++) {
					logger.debug("-->i=" + i);
					IValueSet fila = new ValueSet();
					strFecha = new SimpleDateFormat("yyyy-MM-dd").format(fecha_jueves);
					MonitorJornadasDTO jor1 = (MonitorJornadasDTO) jornada_picking_ju.get(i);
					fila.setVariable("{id_jornada}", String.valueOf(jor1.getId_jornada()));
					fila.setVariable("{fecha}", strFecha);
					fila.setVariable("{h_ini}", jor1.getHinicio());
					fila.setVariable("{h_fin}", jor1.getHfin());
					jornadas_jueves.add(fila); // las agregamos al jueves
					jornadas_viernes.add(fila); // las agregamos al viernes
				}
				top.setVariable("{disabled_button}"	, 	"enabled");
				top.setVariable("{msje_despacho}"	, 	"");
			}else{
				top.setVariable("{disabled_button}"	, 	"disabled");
				top.setVariable("{msje_despacho}"	, 	msjeDespacho);
			}		
			// Iteramos las jornadas del Viernes
			if(jornada_picking_vi.size()>0){
				for (int i = 0; i < jornada_picking_vi.size(); i++) {
					logger.debug("-->i=" + i);
					IValueSet fila = new ValueSet();
					strFecha = new SimpleDateFormat("yyyy-MM-dd").format(fecha_viernes);
					MonitorJornadasDTO jor1 = (MonitorJornadasDTO) jornada_picking_vi.get(i);
					fila.setVariable("{id_jornada}", String.valueOf(jor1.getId_jornada()));
					fila.setVariable("{fecha}", strFecha);
					fila.setVariable("{h_ini}", jor1.getHinicio());
					fila.setVariable("{h_fin}", jor1.getHfin());
					jornadas_viernes.add(fila); // las agregamos al viernes
					jornadas_sabado.add(fila); // las agregamos al sabado
				}
				top.setVariable("{disabled_button}"	, 	"enabled");
				top.setVariable("{msje_despacho}"	, 	"");
			}else{
				top.setVariable("{disabled_button}"	, 	"disabled");
				top.setVariable("{msje_despacho}"	, 	msjeDespacho);
			}	
			// Iteramos las jornadas del Sabado
			if(jornada_picking_sa.size()>0){	
				for (int i = 0; i < jornada_picking_sa.size(); i++) {
					IValueSet fila = new ValueSet();
					strFecha = new SimpleDateFormat("yyyy-MM-dd").format(fecha_sabado);
					MonitorJornadasDTO jor1 = (MonitorJornadasDTO) jornada_picking_sa.get(i);
					fila.setVariable("{id_jornada}", String.valueOf(jor1.getId_jornada()));
					fila.setVariable("{fecha}", strFecha);
					fila.setVariable("{h_ini}", jor1.getHinicio());
					fila.setVariable("{h_fin}", jor1.getHfin());
					jornadas_sabado.add(fila); // las agregamos al sabado
					jornadas_domingo.add(fila); // las agregamos al domingo
				}
				top.setVariable("{disabled_button}"	, 	"enabled");
				top.setVariable("{msje_despacho}"	, 	"");
			}else{
				top.setVariable("{disabled_button}"	, 	"disabled");
				top.setVariable("{msje_despacho}"	, 	msjeDespacho);
			}	
			// Iteramos las jornadas del Domingo
			if(jornada_picking_do.size()>0){	
				for (int i = 0; i < jornada_picking_do.size(); i++) {
					logger.debug("-->i=" + i);
					IValueSet fila = new ValueSet();
					strFecha = new SimpleDateFormat("yyyy-MM-dd").format(fecha_domingo);
					MonitorJornadasDTO jor1 = (MonitorJornadasDTO) jornada_picking_do.get(i);
					fila.setVariable("{id_jornada}", String.valueOf(jor1.getId_jornada()));
					fila.setVariable("{fecha}", strFecha);
					fila.setVariable("{h_ini}", jor1.getHinicio());
					fila.setVariable("{h_fin}", jor1.getHfin());
					jornadas_domingo.add(fila); // las agregamos al domingo
				}	
				top.setVariable("{disabled_button}"	, 	"enabled");
				top.setVariable("{msje_despacho}"	, 	"");
			}else{
				top.setVariable("{disabled_button}"	, 	"disabled");
				top.setVariable("{msje_despacho}"	, 	msjeDespacho);
			}
			top.setVariable("{id_semana}"	, paramId_semana); //reemplazar
			top.setVariable("{fecha_param}" ,paramFecha);
		
			ZonaDTO zona1 = bizDelegate.getZonaDespacho(id_zona);
			top.setVariable("{zona_desp}"	,zona1.getNombre()); 
			top.setVariable("{id_zona}"		,paramId_zona);
			
			List precios = new ArrayList();
			IValueSet fila_pre = new ValueSet();
			fila_pre.setVariable("{tarifa_normal_alta_valor}", String.valueOf(zona1.getTarifa_normal_alta()));
			logger.debug("zona1.getTarifa_normal_alta()--->" + zona1.getTarifa_normal_alta());
			fila_pre.setVariable("{tarifa_normal_alta_nombre}", "Tarifa Alta");
			
			fila_pre.setVariable("{tarifa_normal_media_valor}", String.valueOf(zona1.getTarifa_normal_media()));
			logger.debug("zona1.getTarifa_normal_media()--->" + zona1.getTarifa_normal_media());
			fila_pre.setVariable("{tarifa_normal_media_nombre}", "Tarifa Media");
			
			fila_pre.setVariable("{tarifa_normal_baja_valor}", String.valueOf(zona1.getTarifa_normal_baja()));
			logger.debug("zona1.getTarifa_normal_baja()--->" + zona1.getTarifa_normal_baja());
			fila_pre.setVariable("{tarifa_normal_baja_nombre}", "Tarifa Baja");
			
			precios.add(fila_pre);
			// 6. Setea variables bloques
			top.setDynamicValueSets("select_jornadas_lunes"		, jornadas_lunes);
			top.setDynamicValueSets("select_jornadas_martes"	, jornadas_martes);
			top.setDynamicValueSets("select_jornadas_miercoles"	, jornadas_miercoles);
			top.setDynamicValueSets("select_jornadas_jueves"	, jornadas_jueves);
			top.setDynamicValueSets("select_jornadas_viernes"	, jornadas_viernes);
			top.setDynamicValueSets("select_jornadas_sabado"	, jornadas_sabado);
			top.setDynamicValueSets("select_jornadas_domingo"	, jornadas_domingo);
			top.setDynamicValueSets("select_hora_inicio"		, horas);			
			top.setDynamicValueSets("select_hora_fin"			, horas);			
			top.setDynamicValueSets("select_precios"			, precios);		
			
			
		// 7. variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());		

		top.setVariable("{tarifa_economica_valor}", String.valueOf(zona1.getTarifa_economica()));
		logger.debug("zona1.getTarifa_economica()--->" + zona1.getTarifa_economica());
		//top.setVariable("{tarifa_economica_nombre}", "Tarifa Económica");

		top.setVariable("{estado_tarifa_economica}", String.valueOf(zona1.getEstado_tarifa_economica()));
		logger.debug("zona1.getEstado_tarifa_economica()--->" + zona1.getEstado_tarifa_economica());

		top.setVariable("{tarifa_express_valor}", String.valueOf(zona1.getTarifa_express()));
		logger.debug("zona1.getTarifa_express()--->" + zona1.getTarifa_express());
		//top.setVariable("{tarifa_express_nombre}", "Tarifa Express");

		top.setVariable("{estado_tarifa_express}", String.valueOf(zona1.getEstado_tarifa_express()));
		logger.debug("zona1.getEstado_tarifa_express()--->" + zona1.getEstado_tarifa_express());

		ParametroFoDTO oParametroFoDTO = bizDelegate.getParametroFoByKey("COSTO_DESPACHO_UMBRAL");
		ParametroFoDTO oParametroFoDTO2 = bizDelegate.getParametroFoByKey("TARIFA_COMPRA_UMBRAL");
		double valorTU =0;
		try{
			valorTU = Double.parseDouble(oParametroFoDTO2.getValor());
		}catch(NumberFormatException e){}
		
		top.setVariable("{tarifa_umbral_valor}", oParametroFoDTO.getValor());
		top.setVariable("{tarifa_compra_umbral}", Formatos.formatoPrecio(valorTU));
		
		// 8. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();

 }//execute

}
