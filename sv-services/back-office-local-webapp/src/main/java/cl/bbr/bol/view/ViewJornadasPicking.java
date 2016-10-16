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
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.HorarioPickingEntity;
import cl.bbr.jumbocl.common.model.JornadaPickingEntity;
import cl.bbr.jumbocl.common.model.SemanasEntity;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.pedidos.dto.CalendarioPickingDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega horario con las jornadas de picking de una semana, para un local en particular
 * @author jsepulveda
 */
public class ViewJornadasPicking extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String	paramFecha	= "";
		String	msg2		= "";
		String rc			= "";
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);	
		
		String MsjeError = getServletConfig().getInitParameter("MsjeError");	


		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		
		// 2.1 Parámetro fecha
		paramFecha = req.getParameter("fecha");
		Date fecha = new Date();
		if ( req.getParameter("fecha") != null ){
			try {			
				fecha = new SimpleDateFormat("yyyy-MM-dd").parse(paramFecha);
				logger.debug("fecha: " + paramFecha);
				logger.debug("fecha transformada: "+fecha.toString());
			} catch (Exception  E) {
				logger.error("Argumento fecha inválido" );
				msg2 = "Formato de fecha ingresado es inválido, utilice calendario para seleccionar fecha.";
			}
		}else{
			logger.debug("parámetro fecha vacío, se utilizará la fecha de hoy");	
		}
		
		if(req.getParameter("rc") != null){
			rc = req.getParameter("rc"); 
		}

		Calendar cal1 = new GregorianCalendar();
		cal1.setFirstDayOfWeek(Calendar.MONDAY);
		
		logger.debug("cal1: " + cal1.toString());
		
		cal1.setTime(fecha);
		cal1.getTime();	
		
		int year = cal1.get(Calendar.YEAR);
		int month= cal1.get(Calendar.MONTH);
		int woy	 = cal1.get(Calendar.WEEK_OF_YEAR);
		logger.debug("Week of year: " + woy);
		logger.debug("Year : " + year);
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

		// 4.  Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
	
		// 4.1 Listado de jornadas (horario)
		CalendarioPickingDTO cal = new CalendarioPickingDTO();
		
		cal = bizDelegate.getCalendarioJornadasPicking(woy,year,usr.getId_local());
		
		// Obtenemos los objetos
		SemanasEntity sem = new SemanasEntity();
		sem = cal.getSemana();
		
		List horarios = new ArrayList();
		horarios = cal.getHorarios();
		
		List jornadas = new ArrayList();
		jornadas = cal.getJornadas();
		
		boolean oParam = bizDelegate.valoresNegativos();
		
		// Imprimimos datos de la semana		
		// Iteramos sobre los horarios
		ArrayList datos = new ArrayList();
		logger.debug("horarios.size()--->"+horarios.size());
		if(horarios.size()>0){		
			for (int i=0; i<horarios.size(); i++){
				IValueSet fila = new ValueSet();
				HorarioPickingEntity hor = (HorarioPickingEntity)horarios.get(i);
				//Maxbell - Homologacion pantallas BOL
				try {
					fila.setVariable("{h_ini}"			, hor.getH_ini().toString().substring(0, 5));
					fila.setVariable("{h_fin}"			, hor.getH_fin().toString().substring(0, 5));
				} catch(Exception ex) {
				fila.setVariable("{h_ini}"			, hor.getH_ini().toString());
				fila.setVariable("{h_fin}"			, hor.getH_fin().toString());
				}
				//**********************************//
				fila.setVariable("{id_hor_pick}"	, String.valueOf(hor.getId_hor_pick()));
				String fecha_param = "";
				if(paramFecha!=null){
					fila.setVariable("{fecha_param}"	,paramFecha);
					fecha_param = paramFecha;
				}else{
					String strFecha;
					cal1.set(Calendar.DAY_OF_WEEK,Calendar.DATE);
					strFecha = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime()); 
					fila.setVariable("{fecha_param}"	,strFecha);
					fecha_param = strFecha;
				}
				
	
				// iteramos sobre las jornadas
				for (int j=0; j<jornadas.size();j++){
					JornadaPickingEntity jor = (JornadaPickingEntity)jornadas.get(j);
					
					// jornadas que pertenecen al horario i
					if ( hor.getId_hor_pick() == jor.getId_hor_picking() ){
						
						int cantidadProductosDiferencia = bizDelegate.calcularDiferenciaJornada(jor.getId_jpicking());
						long capac_ocupada = jor.getCapac_ocupada();
						
						if (!oParam) {
							capac_ocupada -= cantidadProductosDiferencia;
						}
						
						switch(jor.getDay_of_week()){
						case 1:
							fila.setVariable("{id_jornada_lu}"	, String.valueOf( jor.getId_jpicking() ) );
							fila.setVariable("{capac_lu}"		, String.valueOf( jor.getCapac_picking() ) );
							fila.setVariable("{capac_oc_lu}"	, String.valueOf( capac_ocupada ) );
							fila.setVariable("{capac_pi_lu}"	, String.valueOf( jor.getCapac_pickeada() ) );
							fila.setVariable("{h_val_lu}"		, String.valueOf( jor.getHrs_validacion() ) );
							fila.setVariable("{h_web_lu}"		, String.valueOf( jor.getHrs_ofrecido_web() ) );
							break;
						case 2:
							fila.setVariable("{id_jornada_ma}"	, String.valueOf( jor.getId_jpicking() ) );
							fila.setVariable("{capac_ma}"		, String.valueOf( jor.getCapac_picking() ) );
							fila.setVariable("{capac_oc_ma}"	, String.valueOf( capac_ocupada ) );
							fila.setVariable("{capac_pi_ma}"	, String.valueOf( jor.getCapac_pickeada() ) );
							fila.setVariable("{h_val_ma}"		, String.valueOf( jor.getHrs_validacion() ) );
							fila.setVariable("{h_web_ma}"		, String.valueOf( jor.getHrs_ofrecido_web() ) );
							break;
					
						case 3:
							fila.setVariable("{id_jornada_mi}"	, String.valueOf( jor.getId_jpicking() ) );
							fila.setVariable("{capac_mi}"		, String.valueOf( jor.getCapac_picking() ) );
							fila.setVariable("{capac_oc_mi}"	, String.valueOf( capac_ocupada ) );
							fila.setVariable("{capac_pi_mi}"	, String.valueOf( jor.getCapac_pickeada() ) );
							fila.setVariable("{h_val_mi}"		, String.valueOf( jor.getHrs_validacion() ) );
							fila.setVariable("{h_web_mi}"		, String.valueOf( jor.getHrs_ofrecido_web() ) );
							break;
							
						case 4:
							fila.setVariable("{id_jornada_ju}"	, String.valueOf( jor.getId_jpicking() ) );
							fila.setVariable("{capac_ju}"		, String.valueOf( jor.getCapac_picking() ) );
							fila.setVariable("{capac_oc_ju}"	, String.valueOf( capac_ocupada ) );
							fila.setVariable("{capac_pi_ju}"	, String.valueOf( jor.getCapac_pickeada() ) );
							fila.setVariable("{h_val_ju}"		, String.valueOf( jor.getHrs_validacion() ) );
							fila.setVariable("{h_web_ju}"		, String.valueOf( jor.getHrs_ofrecido_web() ) );
							break;
							
						case 5:
							fila.setVariable("{id_jornada_vi}"	, String.valueOf( jor.getId_jpicking() ) );
							fila.setVariable("{capac_vi}"		, String.valueOf( jor.getCapac_picking() ) );
							fila.setVariable("{capac_oc_vi}"	, String.valueOf( capac_ocupada ) );
							fila.setVariable("{capac_pi_vi}"	, String.valueOf( jor.getCapac_pickeada() ) );
							fila.setVariable("{h_val_vi}"		, String.valueOf( jor.getHrs_validacion() ) );
							fila.setVariable("{h_web_vi}"		, String.valueOf( jor.getHrs_ofrecido_web() ) );
							break;
							
						case 6:
							fila.setVariable("{id_jornada_sa}"	, String.valueOf( jor.getId_jpicking() ) );
							fila.setVariable("{capac_sa}"		, String.valueOf( jor.getCapac_picking() ) );
							fila.setVariable("{capac_oc_sa}"	, String.valueOf( capac_ocupada ) );
							fila.setVariable("{capac_pi_sa}"	, String.valueOf( jor.getCapac_pickeada() ) );
							fila.setVariable("{h_val_sa}"		, String.valueOf( jor.getHrs_validacion() ) );
							fila.setVariable("{h_web_sa}"		, String.valueOf( jor.getHrs_ofrecido_web() ) );
							break;
							
						case 7:
							fila.setVariable("{id_jornada_do}"	, String.valueOf( jor.getId_jpicking() ) );
							fila.setVariable("{capac_do}"		, String.valueOf( jor.getCapac_picking() ) );
							fila.setVariable("{capac_oc_do}"	, String.valueOf( capac_ocupada ) );
							fila.setVariable("{capac_pi_do}"	, String.valueOf( jor.getCapac_pickeada() ) );
							fila.setVariable("{h_val_do}"		, String.valueOf( jor.getHrs_validacion() ) );
							fila.setVariable("{h_web_do}"		, String.valueOf( jor.getHrs_ofrecido_web() ) );
							break;
							
						}//end switch
					
					}//end if
					String modificar = "<a href=\"ViewEditarJornadaPicking?id_hor_pick="+hor.getId_hor_pick()+"&fecha="+fecha_param+"\"><img src=\"img/editicon.gif\" width=\"19\" height=\"17\" border=\"0\"></a>";
					String eliminar = "<a href='javascript:validar_eliminar(\"esta seguro que desea eliminar?\",\"ElimJornadaPicking?id_hor_pick="+hor.getId_hor_pick()+"&url=ViewJornadasPicking&fecha="+fecha_param+"\");' title='Eliminar' ><img	src=\"img/trash.gif\" width=\"16\" height=\"16\" border=\"0\"></a>";
					
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

					/*if(woy<this_week){
						fila.setVariable("{modificar}","");
						fila.setVariable("{eliminar}","");					
					}else{
						fila.setVariable("{modificar}",modificar);
						fila.setVariable("{eliminar}",eliminar);
					}*/
					
				}//end for
						
				datos.add(fila);
				
			}//end for			
		}
		
		
		// 5. Setea variables del template
		 
		if(paramFecha!=null){
			top.setVariable("{fecha_param}"	,paramFecha);			
		}else{
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
		if( (woy<=this_week && year==this_year) || (year<this_year) ){
			top.setVariable("{disabled_3}","disabled");
		}else{
			top.setVariable("{disabled_3}","enabled");
		}				
		if(sem.getN_semana()>0){
			top.setVariable("{del}"			,"del");
			top.setVariable("{f_ini}"		,sem.getF_ini().toString());
			top.setVariable("{al}"	    	,"al");
			top.setVariable("{f_fin}"		,sem.getF_fin().toString());
			top.setVariable("{ano}"			,String.valueOf(sem.getAno()));
			top.setVariable("{id_semana}"	,String.valueOf(sem.getId_semana()));
			top.setVariable("{n_semana}"	,String.valueOf(sem.getN_semana()));
			top.setVariable("{fecha}"		,"");
			top.setVariable("{msg2}"		,msg2);		
		}
		else{
			top.setVariable("{del}"			,"");
			top.setVariable("{f_ini}"		,"");
			top.setVariable("{al}"	    	,"");
			top.setVariable("{f_fin}"		,"");
			top.setVariable("{ano}"			,"");
			top.setVariable("{id_semana}"	,"");
			top.setVariable("{n_semana}"	,"");
			top.setVariable("{fecha}"		,"");
			top.setVariable("{msg2}"		,"");
		}
		if(rc.equals(Constantes._EX_JPICK_NO_DELETE)){
			top.setVariable("{rc}"		,"la Jornada no puede ser eliminada, por que tiene pedidos o calendarios asociados.");
		}else{
			top.setVariable("{rc}"		," ");
		}
		
		String StrFecha1;
		
		Calendar fecha1 = new GregorianCalendar();
		fecha1.setFirstDayOfWeek(Calendar.MONDAY);
		if(sem.getF_ini()!=null){
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
		
		}else{
			top.setVariable("{f_lu}"		,"");
			top.setVariable("{f_ma}"		,"");
			top.setVariable("{f_mi}"		,"");
			top.setVariable("{f_ju}"		,"");
			top.setVariable("{f_vi}"		,"");
			top.setVariable("{f_sa}"		,"");
			top.setVariable("{f_do}"		,"");
		}
		// 6. Setea variables bloques
		top.setDynamicValueSets("listado", datos);
		
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
