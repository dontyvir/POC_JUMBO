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

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.common.model.SemanasEntity;
import cl.bbr.jumbocl.pedidos.dto.CalendarioDespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesDTO;

/**
 * Despliega el calendario de despacho para una zona determinada
 * 
 * @author BBR
 */
public class ViewCalJorDespRefCotizForm extends Command {
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String 	paramFecha 		= "";
		String  msg				= "";
		long 	id_cotizacion	= -1;
		long 	id_local		= -1;
		long 	id_local_cot	= -1;
		long	id_zona			= -1;
		long	id_jornada		= -1;
		double 	costo_despacho 	= 0;
		long 	cant_prods 		= 0; //cot.getCant_prods();
		String rc = "";
		String mod = "";
		logger.debug("User: " + usr.getLogin());
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		logger.debug("Template: " + html);		
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		// 2.0 parámetro msg
		if ( req.getParameter("msg") != null )
			msg = req.getParameter("msg");
		if (req.getParameter("rc") != null ) rc = req.getParameter("rc");
		logger.debug("rc:"+rc);
		
		// 2.1 parámetro id_cotizacion
		if ( req.getParameter("id_cotizacion") == null ){
			throw new ParametroObligatorioException("id_cotizacion es null");
		}
		
		if ( req.getParameter("mod") == null ){
			throw new ParametroObligatorioException("mod es null");
		}
		mod = req.getParameter("mod");
		
		id_cotizacion = Long.parseLong(req.getParameter("id_cotizacion"));
		logger.debug("id_cotizacion: " + id_cotizacion);
		//
		if (req.getParameter("id_local") != null ){
			id_local = Long.parseLong(req.getParameter("id_local") );
		}else{
			if (req.getParameter("sel_local") != null ){
				id_local = Long.parseLong(req.getParameter("sel_local") );
			}
		}
		//zona
		if ( req.getParameter("id_zona") != null ) id_zona = Long.parseLong(req.getParameter("id_zona") );
		//
		if ( req.getParameter("id_jornada") != null ) id_jornada = Long.parseLong(req.getParameter("id_jornada") );
		//cant_prods
		if ( req.getParameter("cant_prods") != null ) cant_prods = Long.parseLong(req.getParameter("cant_prods") );
		// 2.2 Parámetro fecha
		paramFecha = req.getParameter("fecha");
		
		logger.debug("id_zona: "+id_zona);
		
		
		logger.debug("Parámetros procesados");

		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
	
		top.setVariable("{mod}",mod);
		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate biz = new BizDelegate();

		// 4.1 Obtenemos información del pedido
		CotizacionesDTO cot = biz.getCotizacionesById(id_cotizacion); 
		
		costo_despacho = cot.getCot_costo_desp();
		
		Date fecha = new Date();		
		if ( paramFecha != null ){
			fecha = new SimpleDateFormat("yyyy-MM-dd").parse(paramFecha);
			logger.debug("fecha formateada: " + paramFecha);				
		}
		else
		{	
			logger.debug("parámetro fecha vacío, se utilizará la fecha de hoy");	
		}
		logger.debug("paramFecha:"+paramFecha);
		
		//Formateo de fecha y calendario
		Calendar cal1 = new GregorianCalendar();
		cal1.setFirstDayOfWeek(Calendar.MONDAY);
		logger.debug("cal1: " + cal1.toString());
		
		cal1.setTime(fecha);
		cal1.getTime();
		
		int year = cal1.get(Calendar.YEAR);
		int mes = cal1.get(Calendar.MONTH);
		int woy	 = cal1.get(Calendar.WEEK_OF_YEAR);
        if (mes == 11 && woy == 1) { // Diciembre
            year++;
        }
		logger.debug("Week of year: " + woy);
		logger.debug("mes : " + mes);
		logger.debug("Year: " + year);

		//
		
		//	listado de locales de despacho
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
		
		
		//Obtenemos las Zonas
		List listazona = new ArrayList();
		listazona = biz.getZonasLocal(id_local);
		
		ArrayList zonas = new ArrayList();
		for (int i=0; i<listazona.size();i++){
			IValueSet fila = new ValueSet();
			ZonaDTO zona1 = (ZonaDTO) listazona.get(i);
			fila.setVariable("{zon_id}"	,String.valueOf(zona1.getId_zona()));			
			fila.setVariable("{zon_nom}"		,zona1.getNombre());
			if(i==0 && id_zona==-1){
				id_zona = zona1.getId_zona();
				//logger.debug("id_zona del For "+id_zona);
			}	
			//logger.debug("está llegando Id_zona: "+req.getParameter("id_zona"));
			if (Long.toString(id_zona) != null && Long.toString(id_zona).equals(String.valueOf(zona1.getId_zona())))
				fila.setVariable("{sel}", "selected");
			else
				fila.setVariable("{sel}", "");
			zonas.add(fila);	
		}
		logger.debug("id_zona: "+id_zona);
		
		//	4.2 Listado de jornadas (horario)
		CalendarioDespachoDTO cal = new CalendarioDespachoDTO();
		
		cal = biz.getCalendarioDespacho(woy,year,id_zona);

		
		//System.out.println("-->woy:" + woy);
		//System.out.println("-->year:" + year);
		//System.out.println("-->id_zona:" + id_zona);
		
		// Obtenemos los objetos
		SemanasEntity sem = new SemanasEntity();
		sem = cal.getSemana();
		
		List horarios = new ArrayList();
		horarios = cal.getHorarios();
		
		List jornadas = new ArrayList();
		jornadas = cal.getJornadas();
		
		if (jornadas == null)
			logger.debug("jornadas es null");
		
		
		ArrayList datos = new ArrayList();
		logger.debug("horarios.size = "+horarios.size());
		
		//comparacion de fechas
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar ahora = new GregorianCalendar();
		long tiempoActual 	= ahora.getTimeInMillis();
   
		// Iteramos listado de horarios
		for (int i=0; i<horarios.size(); i++){
			IValueSet fila = new ValueSet();
			HorarioDespachoEntity hor = (HorarioDespachoEntity)horarios.get(i);
			fila.setVariable("{h_ini}"			, hor.getH_ini().toString());
			fila.setVariable("{h_fin}"			, hor.getH_fin().toString());
			fila.setVariable("{id_hor_desp}"	, String.valueOf(hor.getId_hor_desp()));
			if(paramFecha!=null){
				fila.setVariable("{fecha_param}"	,paramFecha);
			}
			else{
				String strFecha;
				cal1.set(Calendar.DAY_OF_WEEK,Calendar.DATE);
				strFecha = new SimpleDateFormat("dd-MM-yyyy").format(cal1.getTime()); 
				fila.setVariable("{fecha_param}"	,strFecha);
			}	

			// logger.debug("Jornadas i: " + hor.getH_ini());
			
			// iteramos sobre las jornadas
			for (int j=0; j<jornadas.size();j++){
				JornadaDespachoEntity jor = new JornadaDespachoEntity();
					
				jor =	(JornadaDespachoEntity)jornadas.get(j);
				
				//obtener la fecha y hora
				//String fecha_pick = jor.getFecha()+" "+jor.getHoraFin().toString();
				String fecha_pick = jor.getFecha_picking()+" "+jor.getHoraIniPicking().toString();
				Date datePicking 	= (Date)formatter.parse(fecha_pick);
				long tiempoDespacho = datePicking.getTime();
				long tiempoLimite 	= jor.getHrs_validacion()*Constantes.HORA_EN_MILI_SEG;
				   
				logger.debug("tiempoActual:"		+tiempoActual);
				logger.debug("tiempoDespacho:"	+tiempoDespacho); 
				logger.debug("diferencia  :"		+(tiempoDespacho-tiempoActual));
				logger.debug("tiempoLimite:"		+tiempoLimite);
				   
				// jornadas que pertenecen al horario i
				if ( hor.getId_hor_desp() == jor.getId_hor_desp() ){
					
					logger.debug("id_jdespacho calendario: " + jor.getId_jdespacho());
					
					logger.debug("dia:"+jor.getDay_of_week());
					logger.debug("capac_pick:"+jor.getCapac_picking());
					logger.debug("capac_pick_ocup:"+jor.getCapac_picking_ocupada());
					logger.debug("capac_desp:"+jor.getCapac_despacho());
					logger.debug("capac_desp_ocup:"+jor.getCapac_despacho_ocupada());
					
					//capacidad de picking, se mide por la cantidad de productos que tiene un pedido.
					//capacidad de despacho, se mide por número de pedidos, no por la cantidad de productos que tiene el pedido.
					switch(jor.getDay_of_week()){
					case 1:
						fila.setVariable("{id_jdespacho_lu}"	, String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{precio_lu}"			, "$"+String.valueOf( jor.getTarifa_normal()) );
						fila.setVariable("{disp_pick_lu}"		, "pick.:" +String.valueOf( jor.getCapac_picking()  - jor.getCapac_picking_ocupada() )  );
						fila.setVariable("{disp_desp_lu}"		, "desp.:"+ String.valueOf( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() )  );
						
						if ( ( cant_prods > ( jor.getCapac_picking() - jor.getCapac_picking_ocupada() ) )
						|| ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) <= 0 )
						|| ( (tiempoDespacho - tiempoActual) < tiempoLimite) )
							fila.setVariable("{disabled_lu}"	, "disabled" );
						else
							fila.setVariable("{disabled_lu}"	, "" );
						//logger.debug("precio lu: " + jor.getPrecio());
						
						fila.setVariable("{sel_lu}"	, "" );
						
						
						break;
					case 2:
						fila.setVariable("{id_jdespacho_ma}"	, String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{precio_ma}"			, "$"+String.valueOf( jor.getTarifa_normal()) );
						fila.setVariable("{disp_pick_ma}"		, "pick.:" +String.valueOf( jor.getCapac_picking()  - jor.getCapac_picking_ocupada() )  );
						fila.setVariable("{disp_desp_ma}"		, "desp.:"+ String.valueOf( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() )  );
						if ( ( cant_prods > ( jor.getCapac_picking() - jor.getCapac_picking_ocupada() ) )
						|| ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) <= 0 ) 
						|| ( (tiempoDespacho - tiempoActual) < tiempoLimite) )
							fila.setVariable("{disabled_ma}"	, "disabled" );
						else
							fila.setVariable("{disabled_ma}"	, "" );
						
						fila.setVariable("{sel_ma}"	, "" );
						
						break;
				
					case 3:
						fila.setVariable("{id_jdespacho_mi}"	, String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{precio_mi}"			, "$"+String.valueOf( jor.getTarifa_normal()) );
						fila.setVariable("{disp_pick_mi}"		, "pick.:" +String.valueOf( jor.getCapac_picking()  - jor.getCapac_picking_ocupada() )  );
						fila.setVariable("{disp_desp_mi}"		, "desp.:"+ String.valueOf( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() )  );
						
						if ( ( cant_prods > ( jor.getCapac_picking() - jor.getCapac_picking_ocupada() ) )
						|| ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) <= 0 ) 
						|| ( (tiempoDespacho - tiempoActual) < tiempoLimite) )
							fila.setVariable("{disabled_mi}"	, "disabled" );
						else
							fila.setVariable("{disabled_mi}"	, "" );
						
						fila.setVariable("{sel_mi}"	, "" );
						
						break;
						
					case 4:
						fila.setVariable("{id_jdespacho_ju}"	, String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{precio_ju}"			, "$"+String.valueOf( jor.getTarifa_normal()) );
						fila.setVariable("{disp_pick_ju}"		, "pick.:" +String.valueOf( jor.getCapac_picking()  - jor.getCapac_picking_ocupada() )  );
						fila.setVariable("{disp_desp_ju}"		, "desp.:"+ String.valueOf( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() )  );
						
						if ( ( cant_prods > ( jor.getCapac_picking() - jor.getCapac_picking_ocupada() ) )
						|| ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) <= 0 ) 
						|| ( (tiempoDespacho - tiempoActual) < tiempoLimite) )
							fila.setVariable("{disabled_ju}"	, "disabled" );
						else
							fila.setVariable("{disabled_ju}"	, "" );
						
						fila.setVariable("{sel_ju}"	, "" );
						
						break;
						
					case 5:
						fila.setVariable("{id_jdespacho_vi}"	, String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{precio_vi}"			, "$"+String.valueOf( jor.getTarifa_normal()) );
						fila.setVariable("{disp_pick_vi}"		, "pick.:" +String.valueOf( jor.getCapac_picking()  - jor.getCapac_picking_ocupada() )  );
						fila.setVariable("{disp_desp_vi}"		, "desp.:"+ String.valueOf( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() )  );
						
						if ( ( cant_prods > ( jor.getCapac_picking() - jor.getCapac_picking_ocupada() ) )
						|| ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) <= 0 ) 
						|| ( (tiempoDespacho - tiempoActual) < tiempoLimite) )
							fila.setVariable("{disabled_vi}"	, "disabled" );
						else
							fila.setVariable("{disabled_vi}"	, "" );
						
						fila.setVariable("{sel_vi}"	, "" );
						
						break;
						
					case 6:
						fila.setVariable("{id_jdespacho_sa}"	, String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{precio_sa}"			, "$"+String.valueOf( jor.getTarifa_normal()) );
						fila.setVariable("{disp_pick_sa}"		, "pick.:" +String.valueOf( jor.getCapac_picking()  - jor.getCapac_picking_ocupada() )  );
						fila.setVariable("{disp_desp_sa}"		, "desp.:"+ String.valueOf( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() )  );
						
						if ( ( cant_prods > ( jor.getCapac_picking() - jor.getCapac_picking_ocupada() ) )
						|| ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) <= 0 ) 
						|| ( (tiempoDespacho - tiempoActual) < tiempoLimite) )
							fila.setVariable("{disabled_sa}"	, "disabled" );
						else
							fila.setVariable("{disabled_sa}"	, "" );
						
						fila.setVariable("{sel_sa}"	, "" );
						
						break;
						
					case 7:
						fila.setVariable("{id_jdespacho_do}"	, String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{precio_do}"			, "$"+String.valueOf( jor.getTarifa_normal()) );
						fila.setVariable("{disp_pick_do}"		, "pick.:" +String.valueOf( jor.getCapac_picking()  - jor.getCapac_picking_ocupada() )  );
						fila.setVariable("{disp_desp_do}"		, "desp.:"+ String.valueOf( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() )  );
						
						if ( ( cant_prods > ( jor.getCapac_picking() - jor.getCapac_picking_ocupada() ) )
						|| ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) <= 0 ) 
						|| ( (tiempoDespacho - tiempoActual) < tiempoLimite) )
							fila.setVariable("{disabled_do}"	, "disabled" );
						else
							fila.setVariable("{disabled_do}"	, "" );
						
						fila.setVariable("{sel_do}"	, "" );
						
						break;
						
					}//end switch
				
				}//end if
							
				
			}//end for
					
			datos.add(fila);
			
		}//end for
			

		// 5. Setea variables del template
		if(paramFecha!=null){
			top.setVariable("{fecha_param}"	,paramFecha);
		}
		else{
			String strFecha;
			cal1.set(Calendar.DAY_OF_WEEK,Calendar.DATE);
			strFecha = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime()); 
			top.setVariable("{fecha_param}"	,strFecha);			
		}
		
		if (jornadas.size()<=0)
		{
			top.setVariable("{mensaje}","No hay jornadas de despacho definidas");
		}
		else
		{
			top.setVariable("{mensaje}","");
		}
		
		top.setVariable("{f_ini}"			,sem.getF_ini().toString());
		top.setVariable("{f_fin}"			,sem.getF_fin().toString());
		top.setVariable("{ano}"				,String.valueOf(sem.getAno()));
		top.setVariable("{id_semana}"		,String.valueOf(sem.getId_semana()));
		top.setVariable("{n_semana}"		,String.valueOf(sem.getN_semana()));
		if(paramFecha!=null && !paramFecha.equals(""))
			top.setVariable("{fecha}"		,paramFecha);
		else
			top.setVariable("{fecha}"		,Constantes.CADENA_VACIA);
		top.setVariable("{capac_picking}"	,String.valueOf(cant_prods));
		top.setVariable("{id_cotizacion}"	,String.valueOf(id_cotizacion));
		top.setVariable("{id_local}"		,String.valueOf(id_local));
		top.setVariable("{zona}"			,String.valueOf(id_zona));
		top.setVariable("{id_jornada}"			,String.valueOf(id_jornada));
		top.setVariable("{msg}"				,msg);
		
		
		
		
		// Setea variables del header de la tabla (fechas)
		String StrFecha1;
		
		Calendar fecha1 = new GregorianCalendar();
		fecha1.setFirstDayOfWeek(Calendar.MONDAY);
		
		fecha1.setTime(sem.getF_ini());
		
		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		StrFecha1 = new SimpleDateFormat("yyyy-MM-dd").format(fecha1.getTime());
		top.setVariable("{f_lu}"		,StrFecha1);

		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
		StrFecha1 = new SimpleDateFormat("yyyy-MM-dd").format(fecha1.getTime());
		top.setVariable("{f_ma}"		,StrFecha1);
		
		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
		StrFecha1 = new SimpleDateFormat("yyyy-MM-dd").format(fecha1.getTime());
		top.setVariable("{f_mi}"		,StrFecha1);
		
		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
		StrFecha1 = new SimpleDateFormat("yyyy-MM-dd").format(fecha1.getTime());
		top.setVariable("{f_ju}"		,StrFecha1);
		
		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
		StrFecha1 = new SimpleDateFormat("yyyy-MM-dd").format(fecha1.getTime());
		top.setVariable("{f_vi}"		,StrFecha1);

		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
		StrFecha1 = new SimpleDateFormat("yyyy-MM-dd").format(fecha1.getTime());
		top.setVariable("{f_sa}"		,StrFecha1);
		
		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		StrFecha1 = new SimpleDateFormat("yyyy-MM-dd").format(fecha1.getTime());
		top.setVariable("{f_do}"		,StrFecha1);
		top.setVariable("{id_zona}"		,String.valueOf(id_zona));
		top.setVariable("{nom_zona}", "");//zonadto.getNombre());
		
		top.setVariable( "{mns}"	, "");
		if ( rc.equals(Constantes._EX_JDESP_FALTAN_DATOS) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El código de la jornada de despacho no existe');</script>" );
		}
		if(costo_despacho!=0)
			top.setVariable("{precio}",Math.round(costo_despacho)+"");
		else
			top.setVariable("{precio}","");
		// 6. Setea variables bloques
		top.setDynamicValueSets("listado", datos);
		top.setDynamicValueSets("ZONAS", zonas);
		top.setDynamicValueSets("LOCALES", locales);

		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

	
	
}