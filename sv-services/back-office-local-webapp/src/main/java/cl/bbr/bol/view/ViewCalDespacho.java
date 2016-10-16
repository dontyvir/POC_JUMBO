package cl.bbr.bol.view;
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
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.common.model.SemanasEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.CalendarioDespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega calendario de despacho de una semana para una zona determinada
 * @author mleiva
 */
public class ViewCalDespacho extends Command {


 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr)
         throws Exception {
	 
	 String paramFecha = "";
	 String paramZona  = "";
	 String rc         = "";
	 String	msg2       = "";
	 long   id_zona    = -1;
	 

     
//	 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		String MsjeError = getServletConfig().getInitParameter("MsjeError");
		String ParamUrl = getServletConfig().getInitParameter("ParamUrl");
		logger.debug("ParamUrl"+ParamUrl);
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		
		// 2.1 Parámetro fecha
		paramFecha = req.getParameter("fecha");
		logger.debug("fecha:"+paramFecha);
		Date fecha = new Date();		
		if ( req.getParameter("fecha") != null ){			
			try {
				fecha = new SimpleDateFormat("yyyy-MM-dd").parse(paramFecha);
			}catch(Exception E) {
				logger.debug("Argumento fecha inválido" );
				msg2 = "Formato de fecha ingresado es inválido, utilice calendario para seleccionar fecha.";
			}
		}else{
		    logger.debug("parámetro fecha vacío, se utilizará la fecha de hoy");
			//fecha = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			fecha = formatter.parse(formatter.format(new Date()));
		}
		
		if(req.getParameter("rc")!=null){
			rc=req.getParameter("rc");
			logger.debug("rc: "+rc);
		}
	
		logger.debug("fecha final:"+fecha);
		Calendar cal1 = new GregorianCalendar();
		cal1.setFirstDayOfWeek(Calendar.MONDAY);
		logger.debug("cal1: " + cal1.toString());
		
		cal1.setTime(fecha);
		cal1.getTime();
		
		int year = cal1.get(Calendar.YEAR);
		int month= cal1.get(Calendar.MONTH);
		int woy	 = cal1.get(Calendar.WEEK_OF_YEAR);
		logger.debug("Week of year: " + woy);
		logger.debug("Year: " + year);
		logger.debug("month: " + month);
		if (month == 11 && woy == 1){//diciembre
		    year++;
		}
		Calendar cal2 = new GregorianCalendar();
		cal2.setFirstDayOfWeek(Calendar.MONDAY);
		cal2.getTime();
		int this_week = cal2.get(Calendar.WEEK_OF_YEAR);
		int this_month= cal2.get(Calendar.MONTH);
		int this_year = cal2.get(Calendar.YEAR);
		logger.debug("this_week : " + this_week);
		logger.debug("this_year : " + this_year);
		logger.debug("this_month: " + this_month);
		if (this_month == 11 && this_week == 1){//diciembre
		    this_year++;
		}
		
		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

         //4.  Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
         //	4.1 Listado de Zonas Despacho
		// Obtenemos los objetos
		List listazona = new ArrayList();
		listazona = bizDelegate.getZonasLocal(usr.getId_local());
		logger.debug("usr.getId_local(): "+usr.getId_local());
		ArrayList zonas = new ArrayList();
		for (int i=0; i<listazona.size();i++){
			IValueSet fila = new ValueSet();
			ZonaDTO zona1 = (ZonaDTO) listazona.get(i);
			fila.setVariable("{id_zona}" ,String.valueOf(zona1.getId_zona()));
			fila.setVariable("{nombre}"  ,zona1.getNombre());
			
			zonas.add(fila);
			if(i==0){
				id_zona = zona1.getId_zona();
				logger.debug("id_zona del For "+id_zona);
			}	
			logger.debug("está llegando Id_zona: "+req.getParameter("id_zona"));
			//Parámetro Zona
			if(req.getParameter("id_zona") != null){
				paramZona = req.getParameter("id_zona");
				id_zona = Long.parseLong(paramZona);
				logger.debug("id_zona del request: "+id_zona);
			}	
			
				
			if (Long.toString(id_zona) != null && Long.toString(id_zona).equals(String.valueOf(zona1.getId_zona())))
				fila.setVariable("{sel}", "selected");
			else
				fila.setVariable("{sel}", "");
		}
		
		// 4.2 Listado de jornadas (horario)
		CalendarioDespachoDTO cal = new CalendarioDespachoDTO();
		logger.debug("woy: "+woy);
		cal = bizDelegate.getCalendarioDespacho(woy,year,id_zona);

		// Obtenemos los objetos
		SemanasEntity sem = new SemanasEntity();
		sem = cal.getSemana();
		
		List horarios = new ArrayList();
		horarios = cal.getHorarios();
		
		List jornadas = new ArrayList();
		jornadas = cal.getJornadas();
		if (jornadas == null)
			logger.debug("jornadas es null");
		
		
		int tarifa_economica_lu = 0;
		int tarifa_economica_ma = 0;
		int tarifa_economica_mi = 0;
		int tarifa_economica_ju = 0;
		int tarifa_economica_vi = 0;
		int tarifa_economica_sa = 0;
		int tarifa_economica_do = 0;
		long capac_total_ocupada_lu = 0;
		long capac_total_ocupada_ma = 0;
		long capac_total_ocupada_mi = 0;
		long capac_total_ocupada_ju = 0;
		long capac_total_ocupada_vi = 0;
		long capac_total_ocupada_sa = 0;
		long capac_total_ocupada_do = 0;
		long capac_total_despacho_lu = 0;
		long capac_total_despacho_ma = 0;
		long capac_total_despacho_mi = 0;
		long capac_total_despacho_ju = 0;
		long capac_total_despacho_vi = 0;
		long capac_total_despacho_sa = 0;
		long capac_total_despacho_do = 0;
		String horIniDespEconomicoMenor = "";
		String horFinDespEconomicoMayor = "";
		boolean horIniDespEconomico = false;
		
		ArrayList datos = new ArrayList();
		logger.debug("horarios.size = "+horarios.size());
		
		// Iteramos listado de horarios
		for (int i=0; i<horarios.size(); i++){
			IValueSet fila = new ValueSet();
			HorarioDespachoEntity hor = (HorarioDespachoEntity)horarios.get(i);
			fila.setVariable("{h_ini}"			, hor.getH_ini().toString());
			fila.setVariable("{h_fin}"			, hor.getH_fin().toString());
			fila.setVariable("{id_hor_desp}"	, String.valueOf(hor.getId_hor_desp()));
			
			if (!horIniDespEconomico){
			    horIniDespEconomicoMenor = hor.getH_ini().toString();
			    horIniDespEconomico = true;
			}
			if (horIniDespEconomicoMenor.compareTo(hor.getH_ini().toString()) > 0){
			    horIniDespEconomicoMenor = hor.getH_ini().toString();
			}
			if (horFinDespEconomicoMayor.compareTo(hor.getH_fin().toString()) < 0){
			    horFinDespEconomicoMayor = hor.getH_fin().toString();
			}

			String fecha_param = "";
			if(paramFecha!=null){
				fila.setVariable("{fecha_param}"	,paramFecha);
				fecha_param=paramFecha;
			}else{
				String strFecha;
				cal1.set(Calendar.DAY_OF_WEEK,Calendar.DATE);
				strFecha = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime()); 
				fila.setVariable("{fecha_param}"	,strFecha);
				fecha_param = strFecha;
			}	
			// iteramos sobre las jornadas
			for (int j=0; j<jornadas.size();j++){
				JornadaDespachoEntity jor = new JornadaDespachoEntity();					
				jor =	(JornadaDespachoEntity)jornadas.get(j);
				
				// jornadas que pertenecen al horario i
				if ( hor.getId_hor_desp() == jor.getId_hor_desp() ){				
					switch(jor.getDay_of_week()){
					case 1:
						fila.setVariable("{id_jdespacho_lu}"	, String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{id_jpicking_lu}"	    , String.valueOf( jor.getId_jpicking()) );
						fila.setVariable("{capac_ocupada_lu}"	, String.valueOf( jor.getCapac_despacho_ocupada() ) );
						fila.setVariable("{capac_despacho_lu}"	, String.valueOf( jor.getCapac_despacho() ) );
						fila.setVariable("{tarifa_normal_lu}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_normal()))));						
						fila.setVariable("{tarifa_express_lu}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_express()))));		
						fila.setVariable("{tarifa_umbral_lu}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_umbral()))));	
						
						//fila.setVariable("{tarifa_economica_lu}", String.valueOf(jor.getTarifa_economica()));
						capac_total_ocupada_lu  = capac_total_ocupada_lu + jor.getCapac_despacho_ocupada();
						capac_total_despacho_lu = capac_total_despacho_lu + jor.getCapac_despacho();
						if (tarifa_economica_lu == 0){
						    tarifa_economica_lu = jor.getTarifa_economica();
						}
						break;
						
					case 2:
						fila.setVariable("{id_jdespacho_ma}"	, String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{id_jpicking_ma}"	    , String.valueOf( jor.getId_jpicking()) );
						fila.setVariable("{capac_ocupada_ma}"	, String.valueOf( jor.getCapac_despacho_ocupada() ) );
						fila.setVariable("{capac_despacho_ma}"	, String.valueOf( jor.getCapac_despacho() ) );
						fila.setVariable("{tarifa_normal_ma}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_normal()))));						
						fila.setVariable("{tarifa_express_ma}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_express()))));	
						fila.setVariable("{tarifa_umbral_ma}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_umbral()))));
						
						
						//fila.setVariable("{tarifa_economica_ma}", String.valueOf(jor.getTarifa_economica()));						
						capac_total_ocupada_ma  = capac_total_ocupada_ma + jor.getCapac_despacho_ocupada();
						capac_total_despacho_ma = capac_total_despacho_ma + jor.getCapac_despacho();
						if (tarifa_economica_ma == 0){
						    tarifa_economica_ma = jor.getTarifa_economica();
						}
						break;
				
					case 3:
						fila.setVariable("{id_jdespacho_mi}"	, String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{id_jpicking_mi}"	    , String.valueOf( jor.getId_jpicking()) );
						fila.setVariable("{capac_ocupada_mi}"	, String.valueOf( jor.getCapac_despacho_ocupada() ) );
						fila.setVariable("{capac_despacho_mi}"	, String.valueOf( jor.getCapac_despacho() ) );
						fila.setVariable("{tarifa_normal_mi}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_normal()))));						
						fila.setVariable("{tarifa_express_mi}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_express()))));						
						fila.setVariable("{tarifa_umbral_mi}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_umbral()))));	
						
						//fila.setVariable("{tarifa_economica_mi}", String.valueOf(jor.getTarifa_economica()));						
						capac_total_ocupada_mi  = capac_total_ocupada_mi + jor.getCapac_despacho_ocupada();
						capac_total_despacho_mi = capac_total_despacho_mi + jor.getCapac_despacho();
						if (tarifa_economica_mi == 0){
						    tarifa_economica_mi = jor.getTarifa_economica();
						}
						break;
						
					case 4:
						fila.setVariable("{id_jdespacho_ju}"	, String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{id_jpicking_ju}"	    , String.valueOf( jor.getId_jpicking()) );
						fila.setVariable("{capac_ocupada_ju}"	, String.valueOf( jor.getCapac_despacho_ocupada() ) );
						fila.setVariable("{capac_despacho_ju}"	, String.valueOf( jor.getCapac_despacho() ) );
						fila.setVariable("{tarifa_normal_ju}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_normal()))));						
						fila.setVariable("{tarifa_express_ju}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_express()))));	
						fila.setVariable("{tarifa_umbral_ju}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_umbral()))));	
												
						//fila.setVariable("{tarifa_economica_ju}", String.valueOf(jor.getTarifa_economica()));						
						capac_total_ocupada_ju  = capac_total_ocupada_ju + jor.getCapac_despacho_ocupada();
						capac_total_despacho_ju = capac_total_despacho_ju + jor.getCapac_despacho();
						if (tarifa_economica_ju == 0){
						    tarifa_economica_ju = jor.getTarifa_economica();
						}
						break;
						
					case 5:
						fila.setVariable("{id_jdespacho_vi}"	, String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{id_jpicking_vi}"	    , String.valueOf( jor.getId_jpicking()) );
						fila.setVariable("{capac_ocupada_vi}"	, String.valueOf( jor.getCapac_despacho_ocupada() ) );
						fila.setVariable("{capac_despacho_vi}"	, String.valueOf( jor.getCapac_despacho() ) );
						fila.setVariable("{tarifa_normal_vi}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_normal()))));						
						fila.setVariable("{tarifa_express_vi}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_express()))));						
						fila.setVariable("{tarifa_umbral_vi}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_umbral()))));	
						
						//fila.setVariable("{tarifa_economica_vi}", String.valueOf(jor.getTarifa_economica()));						
						capac_total_ocupada_vi  = capac_total_ocupada_vi + jor.getCapac_despacho_ocupada();
						capac_total_despacho_vi = capac_total_despacho_vi + jor.getCapac_despacho();
						if (tarifa_economica_vi == 0){
						    tarifa_economica_vi = jor.getTarifa_economica();
						}
						break;
						
					case 6:
						fila.setVariable("{id_jdespacho_sa}"	, String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{id_jpicking_sa}"	    , String.valueOf( jor.getId_jpicking()) );
						fila.setVariable("{capac_ocupada_sa}"	, String.valueOf( jor.getCapac_despacho_ocupada() ) );
						fila.setVariable("{capac_despacho_sa}"	, String.valueOf( jor.getCapac_despacho() ) );
						fila.setVariable("{tarifa_normal_sa}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_normal()))));						
						fila.setVariable("{tarifa_express_sa}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_express()))));						
						fila.setVariable("{tarifa_umbral_sa}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_umbral()))));	
						
						//fila.setVariable("{tarifa_economica_sa}", String.valueOf(jor.getTarifa_economica()));						
						capac_total_ocupada_sa  = capac_total_ocupada_sa + jor.getCapac_despacho_ocupada();
						capac_total_despacho_sa = capac_total_despacho_sa + jor.getCapac_despacho();
						if (tarifa_economica_sa == 0){
						    tarifa_economica_sa = jor.getTarifa_economica();
						}
						break;
						
					case 7:
						fila.setVariable("{id_jdespacho_do}"	, String.valueOf( jor.getId_jdespacho() ) );
						fila.setVariable("{id_jpicking_do}"	    , String.valueOf( jor.getId_jpicking()) );
						fila.setVariable("{capac_ocupada_do}"	, String.valueOf( jor.getCapac_despacho_ocupada() ) );
						fila.setVariable("{capac_despacho_do}"	, String.valueOf( jor.getCapac_despacho() ) );
						fila.setVariable("{tarifa_normal_do}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_normal()))));						
						fila.setVariable("{tarifa_express_do}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_express()))));						
						fila.setVariable("{tarifa_umbral_do}"	, Formatos.formatoPrecio(Double.parseDouble(String.valueOf(jor.getTarifa_umbral()))));
												
						//fila.setVariable("{tarifa_economica_do}", String.valueOf(jor.getTarifa_economica()));						
						capac_total_ocupada_do  = capac_total_ocupada_do + jor.getCapac_despacho_ocupada();
						capac_total_despacho_do = capac_total_despacho_do + jor.getCapac_despacho();
						if (tarifa_economica_do == 0){
						    tarifa_economica_do = jor.getTarifa_economica();
						}
						break;
						
					}//end switch
					fila.setVariable("{id_zona}"	, String.valueOf(id_zona));
					fila.setVariable("{id_semana}"	,String.valueOf(sem.getId_semana()));
				
				}//end if
				String modificar = "<a href=\"ViewEditaJorDespacho?id_hor_desp="+hor.getId_hor_desp()+"&id_semana="+sem.getId_semana()+"&fecha="+fecha_param+"&id_zona="+id_zona+"\"><img src=\"img/editicon.gif\" width=\"19\" height=\"17\" border=\"0\"></a>";
				String eliminar = "<a href=\"javascript:validar_eliminar('esta seguro que desea eliminar?','ElimJornadaDespacho?id_hor_desp="+hor.getId_hor_desp()+"&url='+escape('ViewCalDespacho?fecha="+fecha_param+"&id_zona="+id_zona+"'));\" title=\"Eliminar\" ><img	src=\"img/trash.gif\" width=\"16\" height=\"16\" border=\"0\"></a>";
				
			    if (year > this_year){
					fila.setVariable("{modificar}",modificar);
					fila.setVariable("{eliminar}",eliminar);
			    }else if(woy < this_week){
					fila.setVariable("{modificar}","");
					fila.setVariable("{eliminar}","");					
			    }else{
					fila.setVariable("{modificar}",modificar);
					fila.setVariable("{eliminar}",eliminar);
				}						
				
			}//end for
			String strFecha;
			cal1.set(Calendar.DAY_OF_WEEK,Calendar.DATE);
			strFecha = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime());
			fila.setVariable("{fecha_param}",strFecha);
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
		
		if (jornadas.size()<=0){
			top.setVariable("{mensaje}",MsjeError);
		}else{
			top.setVariable("{mensaje}","");
		}
		if( (woy<this_week && year==this_year) || (year<this_year)){
			top.setVariable("{disabled_3}","disabled");
		}else{
			top.setVariable("{disabled_3}","enabled");
		}
		
		top.setVariable("{f_ini}"		,sem.getF_ini().toString());
		top.setVariable("{f_fin}"		,sem.getF_fin().toString());
		top.setVariable("{ano}"			,String.valueOf(sem.getAno()));
		top.setVariable("{id_semana}"	,String.valueOf(sem.getId_semana()));
		top.setVariable("{n_semana}"	,String.valueOf(sem.getN_semana()));
		top.setVariable("{fecha}"		,"");
		top.setVariable("{msg2}"		,msg2);
		if(rc.equals("JD003")){
			top.setVariable("{rc}", " El calendario no se eliminó por tener pedidos asociados");
		}else{
			top.setVariable("{rc}", " ");
		}
		
		// Setea variables del header de la tabla (fechas)
		ArrayList lst_horario_econonico = new ArrayList();
		IValueSet hor_econonico = new ValueSet();

		String StrFecha1;
		
		Calendar fecha1 = new GregorianCalendar();
		fecha1.setFirstDayOfWeek(Calendar.MONDAY);
		fecha1.setTime(sem.getF_ini());
		
		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		StrFecha1 = new SimpleDateFormat("yyyy-MM-dd").format(fecha1.getTime());
		top.setVariable("{f_lu}", StrFecha1);
		hor_econonico.setVariable("{capac_total_ocupada_lu}" , String.valueOf(capac_total_ocupada_lu));
		hor_econonico.setVariable("{capac_total_despacho_lu}", String.valueOf(capac_total_despacho_lu));
		hor_econonico.setVariable("{tarifa_economica_lu}", "<font color='#009900'>" + Formatos.formatoPrecio(Double.parseDouble(String.valueOf(tarifa_economica_lu))) + "</font>");

		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
		StrFecha1 = new SimpleDateFormat("yyyy-MM-dd").format(fecha1.getTime());
		top.setVariable("{f_ma}", StrFecha1);
		hor_econonico.setVariable("{capac_total_ocupada_ma}" , String.valueOf(capac_total_ocupada_ma));
		hor_econonico.setVariable("{capac_total_despacho_ma}", String.valueOf(capac_total_despacho_ma));
		hor_econonico.setVariable("{tarifa_economica_ma}", "<font color='#009900'>" + Formatos.formatoPrecio(Double.parseDouble(String.valueOf(tarifa_economica_ma))) + "</font>");
		
		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
		StrFecha1 = new SimpleDateFormat("yyyy-MM-dd").format(fecha1.getTime());
		top.setVariable("{f_mi}", StrFecha1);
		hor_econonico.setVariable("{capac_total_ocupada_mi}" , String.valueOf(capac_total_ocupada_mi));
		hor_econonico.setVariable("{capac_total_despacho_mi}", String.valueOf(capac_total_despacho_mi));
		hor_econonico.setVariable("{tarifa_economica_mi}", "<font color='#009900'>" + Formatos.formatoPrecio(Double.parseDouble(String.valueOf(tarifa_economica_mi))) + "</font>");
		
		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
		StrFecha1 = new SimpleDateFormat("yyyy-MM-dd").format(fecha1.getTime());
		top.setVariable("{f_ju}", StrFecha1);
		hor_econonico.setVariable("{capac_total_ocupada_ju}" , String.valueOf(capac_total_ocupada_ju));
		hor_econonico.setVariable("{capac_total_despacho_ju}", String.valueOf(capac_total_despacho_ju));
		hor_econonico.setVariable("{tarifa_economica_ju}", "<font color='#009900'>" + Formatos.formatoPrecio(Double.parseDouble(String.valueOf(tarifa_economica_ju))) + "</font>");
		
		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
		StrFecha1 = new SimpleDateFormat("yyyy-MM-dd").format(fecha1.getTime());
		top.setVariable("{f_vi}", StrFecha1);
		hor_econonico.setVariable("{capac_total_ocupada_vi}" , String.valueOf(capac_total_ocupada_vi));
		hor_econonico.setVariable("{capac_total_despacho_vi}", String.valueOf(capac_total_despacho_vi));
		hor_econonico.setVariable("{tarifa_economica_vi}", "<font color='#009900'>" + Formatos.formatoPrecio(Double.parseDouble(String.valueOf(tarifa_economica_vi))) + "</font>");

		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
		StrFecha1 = new SimpleDateFormat("yyyy-MM-dd").format(fecha1.getTime());
		top.setVariable("{f_sa}", StrFecha1);
		hor_econonico.setVariable("{capac_total_ocupada_sa}" , String.valueOf(capac_total_ocupada_sa));
		hor_econonico.setVariable("{capac_total_despacho_sa}", String.valueOf(capac_total_despacho_sa));
		hor_econonico.setVariable("{tarifa_economica_sa}", "<font color='#009900'>" + Formatos.formatoPrecio(Double.parseDouble(String.valueOf(tarifa_economica_sa))) + "</font>");
		
		fecha1.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		StrFecha1 = new SimpleDateFormat("yyyy-MM-dd").format(fecha1.getTime());
		top.setVariable("{f_do}", StrFecha1);
		hor_econonico.setVariable("{capac_total_ocupada_do}" , String.valueOf(capac_total_ocupada_do));
		hor_econonico.setVariable("{capac_total_despacho_do}", String.valueOf(capac_total_despacho_do));
		hor_econonico.setVariable("{tarifa_economica_do}", "<font color='#009900'>" + Formatos.formatoPrecio(Double.parseDouble(String.valueOf(tarifa_economica_do))) + "</font>");

		hor_econonico.setVariable("{h_ini_desp_economico}", horIniDespEconomicoMenor);
		hor_econonico.setVariable("{h_fin_desp_economico}", horFinDespEconomicoMayor);
		
		lst_horario_econonico.add(hor_econonico);
		
		top.setVariable("{id_zona}", String.valueOf(id_zona));
		
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("listado", datos);
		top.setDynamicValueSets("select_zona_despacho", zonas);

		ZonaDTO zona = null;
		for (int i=0; i<listazona.size();i++){
		    if (((ZonaDTO) listazona.get(i)).getId_zona() == id_zona){
		        zona = ((ZonaDTO) listazona.get(i));
		    }
		}
		if (zona.getEstado_tarifa_economica() == 1){
		    top.setDynamicValueSets("lst_horario_econonico", lst_horario_econonico);
		}
		

		
		// 7. variables del header
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}", usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());	
		
		// 8. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
		

 }//execute

}
