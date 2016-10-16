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
import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.common.model.SemanasEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.parametros.dto.ParametroFoDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaCriteria;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * despliega el formulario que permite editar una jornada de despacho
 * @author mleiva
 */

public class ViewEditaJorDespacho extends Command {


	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		String paramId_hor_desp = "";
		String paramId_semana = "";
		String paramId_zona = "";
		String paramFecha = "";
		String rc 				= "";
		String mns_rc			= "";
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		if (req.getParameter("rc") != null ) rc = req.getParameter("rc");
		logger.debug("rc:"+rc);
		if (req.getParameter("mns_rc") != null ) mns_rc = req.getParameter("mns_rc");
		logger.debug("mns_rc:"+mns_rc);

		// 2.1 Parámetro id_jor_desp (Obligatorio)

		if (req.getParameter("id_hor_desp") == null) {
			throw new ParametroObligatorioException("id_hor_desp es null");
		}
		paramId_hor_desp = req.getParameter("id_hor_desp");
		logger.debug("id_hor_desp: " + paramId_hor_desp);

		if (req.getParameter("id_semana") == null) {
			throw new ParametroObligatorioException("id_semana es null");
		}
		paramId_semana = req.getParameter("id_semana");
		logger.debug("id_semana: " + paramId_semana);

		if (req.getParameter("id_zona") == null) {
			throw new ParametroObligatorioException("id_zona es null");
		}
		paramId_zona = req.getParameter("id_zona");
		logger.debug("id_zona: " + paramId_zona);

		if (req.getParameter("fecha") == null) {
			paramFecha = "";
		}
		paramFecha = req.getParameter("fecha");
		logger.debug("paramFecha: " + paramFecha);

		// si falla el parseLong debiese levantar una excepción
		long id_hor_desp 	= Long.parseLong(paramId_hor_desp);
		long id_semana 		= Long.parseLong(paramId_semana);
		long id_zona 		= Long.parseLong(paramId_zona);

		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate biz = new BizDelegate();

		HorarioDespachoEntity hor = new HorarioDespachoEntity();
		hor = biz.getHorarioDespacho(id_hor_desp);

		SemanasEntity sem = new SemanasEntity();
		sem = biz.getSemanaById(id_semana);

		// 4.2 genera Listados Jornada Picking relacionadas
		JornadaCriteria criterio = new JornadaCriteria();
		if (paramFecha != "") {
			criterio.setF_jornada(paramFecha);
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
		
		// 5.1 genera Listados
		List jornadas = new ArrayList();
		jornadas = biz.getJornadasDespachoByIdHorario(id_hor_desp);
		
		List datos = new ArrayList();
		for (int i = 0; i < jornadas.size(); i++) {
			JornadaDespachoEntity jor = new JornadaDespachoEntity();
			jor = (JornadaDespachoEntity) jornadas.get(i);

			//logger.debug("-->" + jor.getCapac_picking());
			//logger.debug("jor.getDay_of_week()-->" + jor.getDay_of_week());
			biz.getJornadaById(1);
			switch (jor.getDay_of_week()) {
			case 1:
				id_jpicking_lu = jor.getId_jpicking();
				top.setVariable("{f_lu}", jor.getFecha().toString());
				top.setVariable("{capac_desp_lu}", String.valueOf(jor.getCapac_despacho()));
				top.setVariable("{tarifa_express_lu}", String.valueOf(jor.getTarifa_express()));
				top.setVariable("{tarifa_normal_lu}", String.valueOf(jor.getTarifa_normal()));
				top.setVariable("{tarifa_economica_lu}", String.valueOf(jor.getTarifa_economica()));
				top.setVariable("{tarifa_umbral_lu}", String.valueOf(jor.getTarifa_umbral()));
				fecha_lunes = jor.getFecha();
				//Permite sacar la fecha y hora de picking
				JornadaDTO jor_lu =  biz.getJornadaById(jor.getId_jpicking());
				top.setVariable("{fecha_lu}", String.valueOf(jor_lu.getF_jornada()));
				top.setVariable("{h_ini_lu}" , String.valueOf(jor_lu.getH_inicio()));
				top.setVariable("{h_fin_lu}" , String.valueOf(jor_lu.getH_fin()));
				top.setVariable("{id_jor_lu}" , String.valueOf(jor.getId_jpicking()));

				
						
			case 2:
				id_jpicking_ma = jor.getId_jpicking();
				top.setVariable("{f_ma}", jor.getFecha().toString());
				top.setVariable("{capac_desp_ma}", String.valueOf(jor.getCapac_despacho()));
				top.setVariable("{tarifa_express_ma}", String.valueOf(jor.getTarifa_express()));
				top.setVariable("{tarifa_normal_ma}", String.valueOf(jor.getTarifa_normal()));
				top.setVariable("{tarifa_economica_ma}", String.valueOf(jor.getTarifa_economica()));
				top.setVariable("{tarifa_umbral_ma}", String.valueOf(jor.getTarifa_umbral()));
				fecha_martes = jor.getFecha();
				//Permite sacar la fecha y hora de picking
				JornadaDTO jor_ma =  biz.getJornadaById(jor.getId_jpicking());
				top.setVariable("{fecha_ma}" , String.valueOf(jor_ma.getF_jornada()));
				top.setVariable("{h_ini_ma}" , String.valueOf(jor_ma.getH_inicio()));
				top.setVariable("{h_fin_ma}" , String.valueOf(jor_ma.getH_fin()));
				top.setVariable("{id_jor_ma}" , String.valueOf(jor.getId_jpicking()));
			case 3:
				id_jpicking_mi = jor.getId_jpicking();
				top.setVariable("{f_mi}", jor.getFecha().toString());
				top.setVariable("{capac_desp_mi}", String.valueOf(jor.getCapac_despacho()));
				top.setVariable("{tarifa_express_mi}", String.valueOf(jor.getTarifa_express()));
				top.setVariable("{tarifa_normal_mi}", String.valueOf(jor.getTarifa_normal()));
				top.setVariable("{tarifa_economica_mi}", String.valueOf(jor.getTarifa_economica()));
				top.setVariable("{tarifa_umbral_mi}", String.valueOf(jor.getTarifa_umbral()));
				fecha_miercoles = jor.getFecha();
				//Permite sacar la fecha y hora de picking
				JornadaDTO jor_mi =  biz.getJornadaById(jor.getId_jpicking());
				top.setVariable("{fecha_mi}" , String.valueOf(jor_mi.getF_jornada()));
				top.setVariable("{h_ini_mi}" , String.valueOf(jor_mi.getH_inicio()));
				top.setVariable("{h_fin_mi}" , String.valueOf(jor_mi.getH_fin()));
				top.setVariable("{id_jor_mi}" , String.valueOf(jor.getId_jpicking()));				
			case 4:
				id_jpicking_ju = jor.getId_jpicking();
				top.setVariable("{f_ju}", jor.getFecha().toString());
				top.setVariable("{capac_desp_ju}", String.valueOf(jor.getCapac_despacho()));
				top.setVariable("{tarifa_express_ju}", String.valueOf(jor.getTarifa_express()));
				top.setVariable("{tarifa_normal_ju}", String.valueOf(jor.getTarifa_normal()));
				top.setVariable("{tarifa_economica_ju}", String.valueOf(jor.getTarifa_economica()));
				top.setVariable("{tarifa_umbral_ju}", String.valueOf(jor.getTarifa_umbral()));
				fecha_jueves = jor.getFecha();
//				Permite sacar la fecha y hora de picking
				JornadaDTO jor_ju =  biz.getJornadaById(jor.getId_jpicking());
				top.setVariable("{fecha_ju}" , String.valueOf(jor_ju.getF_jornada()));
				top.setVariable("{h_ini_ju}" , String.valueOf(jor_ju.getH_inicio()));
				top.setVariable("{h_fin_ju}" , String.valueOf(jor_ju.getH_fin()));
				top.setVariable("{id_jor_ju}" , String.valueOf(jor.getId_jpicking()));				
			case 5:
				id_jpicking_vi = jor.getId_jpicking();
				top.setVariable("{f_vi}", jor.getFecha().toString());
				top.setVariable("{capac_desp_vi}", String.valueOf(jor.getCapac_despacho()));
				top.setVariable("{tarifa_express_vi}", String.valueOf(jor.getTarifa_express()));
				top.setVariable("{tarifa_normal_vi}", String.valueOf(jor.getTarifa_normal()));
				top.setVariable("{tarifa_economica_vi}", String.valueOf(jor.getTarifa_economica()));
				top.setVariable("{tarifa_umbral_vi}", String.valueOf(jor.getTarifa_umbral()));
				fecha_viernes = jor.getFecha();
//				Permite sacar la fecha y hora de picking
				JornadaDTO jor_vi =  biz.getJornadaById(jor.getId_jpicking());
				top.setVariable("{fecha_vi}" , String.valueOf(jor_vi.getF_jornada()));
				top.setVariable("{h_ini_vi}" , String.valueOf(jor_vi.getH_inicio()));
				top.setVariable("{h_fin_vi}" , String.valueOf(jor_vi.getH_fin()));
				top.setVariable("{id_jor_vi}" , String.valueOf(jor.getId_jpicking()));				
			case 6:
				id_jpicking_sa = jor.getId_jpicking();
				top.setVariable("{f_sa}", jor.getFecha().toString());
				top.setVariable("{capac_desp_sa}", String.valueOf(jor.getCapac_despacho()));
				top.setVariable("{tarifa_express_sa}", String.valueOf(jor.getTarifa_express()));
				top.setVariable("{tarifa_normal_sa}", String.valueOf(jor.getTarifa_normal()));
				top.setVariable("{tarifa_economica_sa}", String.valueOf(jor.getTarifa_economica()));
				top.setVariable("{tarifa_umbral_sa}", String.valueOf(jor.getTarifa_umbral()));
				fecha_sabado = jor.getFecha();
//				Permite sacar la fecha y hora de picking
				JornadaDTO jor_sa =  biz.getJornadaById(jor.getId_jpicking());
				top.setVariable("{fecha_sa}" , String.valueOf(jor_sa.getF_jornada()));
				top.setVariable("{h_ini_sa}" , String.valueOf(jor_sa.getH_inicio()));
				top.setVariable("{h_fin_sa}" , String.valueOf(jor_sa.getH_fin()));
				top.setVariable("{id_jor_sa}" , String.valueOf(jor.getId_jpicking()));				
			case 7:
				id_jpicking_do = jor.getId_jpicking();
				top.setVariable("{f_do}", jor.getFecha().toString());
				top.setVariable("{capac_desp_do}", String.valueOf(jor.getCapac_despacho()));
				top.setVariable("{tarifa_express_do}", String.valueOf(jor.getTarifa_express()));
				top.setVariable("{tarifa_normal_do}", String.valueOf(jor.getTarifa_normal()));
				top.setVariable("{tarifa_economica_do}", String.valueOf(jor.getTarifa_economica()));
				top.setVariable("{tarifa_umbral_do}", String.valueOf(jor.getTarifa_umbral()));
				fecha_domingo = jor.getFecha();
//				Permite sacar la fecha y hora de picking
				JornadaDTO jor_do =  biz.getJornadaById(jor.getId_jpicking());
				top.setVariable("{fecha_do}" , String.valueOf(jor_do.getF_jornada()));
				top.setVariable("{h_ini_do}" , String.valueOf(jor_do.getH_inicio()));
				top.setVariable("{h_fin_do}" , String.valueOf(jor_do.getH_fin()));
				top.setVariable("{id_jor_do}" , String.valueOf(jor.getId_jpicking()));				

			}

		}
		
		
		// 5.2 Genera jornadas para cada día
		
		// Lunes
		//		
		Calendar cal1 = new GregorianCalendar();
		cal1.setFirstDayOfWeek(Calendar.MONDAY);
		cal1.setTime(fecha_lunes);
		cal1.getTime();
		cal1.add(Calendar.DATE,-1);
		fecha_domingo_ant = cal1.getTime();
		String strFecha ="";
		strFecha = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime());
		logger.debug("fecha domingo ant: " + fecha_domingo_ant);
		
		// TODO: buscar un metodo más eficiente...
		List jornada_picking_do_ant = biz.getJornadasPickingByFecha(fecha_domingo_ant,usr.getId_local()); 
		List jornada_picking_lu 	= biz.getJornadasPickingByFecha(fecha_lunes,usr.getId_local()); 
		List jornada_picking_ma 	= biz.getJornadasPickingByFecha(fecha_martes,usr.getId_local());
		List jornada_picking_mi 	= biz.getJornadasPickingByFecha(fecha_miercoles,usr.getId_local());
		List jornada_picking_ju 	= biz.getJornadasPickingByFecha(fecha_jueves,usr.getId_local()); 
		List jornada_picking_vi 	= biz.getJornadasPickingByFecha(fecha_viernes,usr.getId_local());
		List jornada_picking_sa 	= biz.getJornadasPickingByFecha(fecha_sabado,usr.getId_local()); 
		List jornada_picking_do 	= biz.getJornadasPickingByFecha(fecha_domingo,usr.getId_local());
		
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
		/* Comentado porque por el momento debe aparecer la informacion sin posibilidad de editar.
		 * 
		// Listas que contienen los IValueSet para el template
		ArrayList jornadas_lunes 	= new ArrayList();
		ArrayList jornadas_martes 	= new ArrayList();
		ArrayList jornadas_miercoles = new ArrayList();
		ArrayList jornadas_jueves 	= new ArrayList();
		ArrayList jornadas_viernes 	= new ArrayList();
		ArrayList jornadas_sabado 	= new ArrayList();
		ArrayList jornadas_domingo 	= new ArrayList();
		

		// Iteramos las jornadas del domingo anterior
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
		
		// Iteramos las jornadas del lunes
		for (int i = 0; i < jornada_picking_lu.size(); i++) {
			IValueSet fila_lu = new ValueSet();
			MonitorJornadasDTO jor1 = (MonitorJornadasDTO) jornada_picking_lu.get(i);
			fila_lu.setVariable("{id_jornada}", String.valueOf(jor1.getId_jornada()));
			fila_lu.setVariable("{fecha}", fecha_lunes.toString());
			fila_lu.setVariable("{h_ini}", jor1.getHinicio());
			fila_lu.setVariable("{h_fin}", jor1.getHfin());
			if ( jor1.getId_jornada() == id_jpicking_lu )
				fila_lu.setVariable("{sel_lu}", "selected");
			else
				fila_lu.setVariable("{sel_lu}", "");
			jornadas_lunes.add(fila_lu); // las agregamos al lunes
			jornadas_martes.add(fila_lu); // las agregamos al martes
		}
		
		// Iteramos las jornadas del martes
		for (int i = 0; i < jornada_picking_ma.size(); i++) {
			IValueSet fila = new ValueSet();
			MonitorJornadasDTO jor1 = (MonitorJornadasDTO) jornada_picking_ma.get(i);
			fila.setVariable("{id_jornada}", String.valueOf(jor1.getId_jornada()));
			fila.setVariable("{fecha}", fecha_martes.toString());
			fila.setVariable("{h_ini}", jor1.getHinicio());
			fila.setVariable("{h_fin}", jor1.getHfin());
			if ( jor1.getId_jornada() == id_jpicking_ma )
				fila.setVariable("{sel_ma}", "selected");
			else
				fila.setVariable("{sel_ma}", "");			
			
			jornadas_martes.add(fila); // las agregamos al martes
			jornadas_miercoles.add(fila); // las agregamos al miercoles
		}

		// Iteramos las jornadas del miercoles
		for (int i = 0; i < jornada_picking_mi.size(); i++) {
			logger.debug("-->i=" + i);
			IValueSet fila = new ValueSet();
			MonitorJornadasDTO jor1 = (MonitorJornadasDTO) jornada_picking_mi.get(i);
			fila.setVariable("{id_jornada}", String.valueOf(jor1.getId_jornada()));
			fila.setVariable("{fecha}", fecha_miercoles.toString());
			fila.setVariable("{h_ini}", jor1.getHinicio());
			fila.setVariable("{h_fin}", jor1.getHfin());
			if ( jor1.getId_jornada() == id_jpicking_mi )
				fila.setVariable("{sel_mi}", "selected");
			else
				fila.setVariable("{sel_mi}", "");			
			
			jornadas_miercoles.add(fila); // las agregamos al miercoles
			jornadas_jueves.add(fila); // las agregamos al jueves			
		}
		
		// Iteramos las jornadas del Jueves
		for (int i = 0; i < jornada_picking_ju.size(); i++) {
			logger.debug("-->i=" + i);
			IValueSet fila = new ValueSet();
			MonitorJornadasDTO jor1 = (MonitorJornadasDTO) jornada_picking_ju.get(i);
			fila.setVariable("{id_jornada}", String.valueOf(jor1.getId_jornada()));
			fila.setVariable("{fecha}", fecha_jueves.toString());
			fila.setVariable("{h_ini}", jor1.getHinicio());
			fila.setVariable("{h_fin}", jor1.getHfin());
			if ( jor1.getId_jornada() == id_jpicking_ju )
				fila.setVariable("{sel_ju}", "selected");
			else
				fila.setVariable("{sel_ju}", "");			
			
			
			
			jornadas_jueves.add(fila); // las agregamos al jueves
			jornadas_viernes.add(fila); // las agregamos al viernes
		}

		// Iteramos las jornadas del Viernes
		for (int i = 0; i < jornada_picking_vi.size(); i++) {
			logger.debug("-->i=" + i);
			IValueSet fila = new ValueSet();
			MonitorJornadasDTO jor1 = (MonitorJornadasDTO) jornada_picking_vi.get(i);
			fila.setVariable("{id_jornada}", String.valueOf(jor1.getId_jornada()));
			fila.setVariable("{fecha}", fecha_viernes.toString());
			fila.setVariable("{h_ini}", jor1.getHinicio());
			fila.setVariable("{h_fin}", jor1.getHfin());
			
			if ( jor1.getId_jornada() == id_jpicking_vi )
				fila.setVariable("{sel_vi}", "selected");
			else
				fila.setVariable("{sel_vi}", "");			
			
			
			jornadas_viernes.add(fila); // las agregamos al viernes
			jornadas_sabado.add(fila); // las agregamos al sabado
		}

		// Iteramos las jornadas del Sabado
		for (int i = 0; i < jornada_picking_sa.size(); i++) {
			IValueSet fila = new ValueSet();
			MonitorJornadasDTO jor1 = (MonitorJornadasDTO) jornada_picking_sa.get(i);
			fila.setVariable("{id_jornada}", String.valueOf(jor1.getId_jornada()));
			fila.setVariable("{fecha}", fecha_sabado.toString());
			fila.setVariable("{h_ini}", jor1.getHinicio());
			fila.setVariable("{h_fin}", jor1.getHfin());
			
			if ( jor1.getId_jornada() == id_jpicking_sa )
				fila.setVariable("{sel_sa}", "selected");
			else
				fila.setVariable("{sel_sa}", "");			
			
			jornadas_sabado.add(fila); // las agregamos al sabado
			jornadas_domingo.add(fila); // las agregamos al domingo
		}
		
		// Iteramos las jornadas del Domingo
		for (int i = 0; i < jornada_picking_do.size(); i++) {
			logger.debug("-->i=" + i);
			IValueSet fila = new ValueSet();
			MonitorJornadasDTO jor1 = (MonitorJornadasDTO) jornada_picking_do.get(i);
			fila.setVariable("{id_jornada}", String.valueOf(jor1.getId_jornada()));
			fila.setVariable("{fecha}", fecha_domingo.toString());
			fila.setVariable("{h_ini}", jor1.getHinicio());
			fila.setVariable("{h_fin}", jor1.getHfin());
			
			if ( jor1.getId_jornada() == id_jpicking_do )
				fila.setVariable("{sel_do}", "selected");
			else
				fila.setVariable("{sel_do}", "");			
			
			jornadas_domingo.add(fila); // las agregamos al domingo
		}
		*/
		
		ParametroFoDTO oParametroFoDTO = biz.getParametroFoByKey("COSTO_DESPACHO_UMBRAL");
		ParametroFoDTO oParametroFoDTO2 = biz.getParametroFoByKey("TARIFA_COMPRA_UMBRAL");
		double valorTU =0;
		try{
			valorTU = Double.parseDouble(oParametroFoDTO2.getValor());
		}catch(NumberFormatException e){}
		
		top.setVariable("{tarifa_umbral_valor}", oParametroFoDTO.getValor());
		top.setVariable("{tarifa_compra_umbral}", Formatos.formatoPrecio(valorTU));
		
		// Otras variables del template
		top.setVariable("{f_ini}", sem.getF_ini().toString());
		top.setVariable("{f_fin}", sem.getF_fin().toString());

		top.setVariable("{id_semana}", paramId_semana); // reemplazar
		top.setVariable("{fecha_param}", paramFecha);

		ZonaDTO zona1 = biz.getZonaDespacho(id_zona);
		top.setVariable("{zona_desp}", zona1.getNombre());
		top.setVariable("{id_zona}", String.valueOf(zona1.getId_zona()));

		top.setVariable("{id_hor_desp}", paramId_hor_desp);
		top.setVariable("{h_ini}", hor.getH_ini());
		top.setVariable("{h_fin}", hor.getH_fin());
		top.setVariable("{fecha_param}", paramFecha);
		//top.setVariable("{id_zona}", paramId_zona);
		
		// 6. Setea variables bloques
		/*
		top.setDynamicValueSets("select_jornadas_lunes"		, jornadas_lunes);
		top.setDynamicValueSets("select_jornadas_martes"	, jornadas_martes);
		top.setDynamicValueSets("select_jornadas_miercoles"	, jornadas_miercoles);
		top.setDynamicValueSets("select_jornadas_jueves"	, jornadas_jueves);
		top.setDynamicValueSets("select_jornadas_viernes"	, jornadas_viernes);
		top.setDynamicValueSets("select_jornadas_sabado"	, jornadas_sabado);
		top.setDynamicValueSets("select_jornadas_domingo"	, jornadas_domingo);
		*/
		// 7. variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
		
		logger.debug(" **** rc:"+rc);
		if ( !rc.equals("") ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('"+mns_rc+"');</script>" );
		}else {
			top.setVariable( "{mns}", "");
		}
		
		// 8. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();

	}// execute
	
	/*private List jornadaPicking(Date fecha, long id_local){
		BizDelegate biz = new BizDelegate();
		List jornada_picking = new ArrayList();
		List datos = new ArrayList();
		try {
			jornada_picking = biz.getJornadasPickingByFecha(fecha,1);
		} catch (BolException e) {
			e.printStackTrace();
		} 
		
		for (int i = 0; i < jornada_picking.size(); i++) {
			IValueSet fila = new ValueSet();
			JornadaDTO jor1 = (JornadaDTO) jornada_picking.get(i);
			jor1.getId_jornada();
			logger.debug("jor1.getId_jornada(): "+jor1.getId_jornada());
			jor1.getF_jornada();
			logger.debug("jor1.getF_jornada(): "+jor1.getF_jornada());
			jor1.getH_inicio();
			logger.debug("jor1.getH_inicio(): "+jor1.getH_inicio());
			jor1.getH_fin();
			logger.debug("jor1.getH_fin(): "+jor1.getH_fin());
			datos.add(jor1);
		}		
	return datos;	
	}*/
	
}
