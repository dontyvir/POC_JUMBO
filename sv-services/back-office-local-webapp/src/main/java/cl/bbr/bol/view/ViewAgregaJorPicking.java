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
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega formulario para crear jornadas de picking
 * @author jsepulveda
 */
public class ViewAgregaJorPicking extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr)
	    throws Exception {
		
		// Variables del método
		String paramId_semana = "";
		String paramFecha	  = "";	
	
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
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
		
		
		// 3. Template (dejar tal cual)
		View salida = new View(res);
	
		TemplateLoader load = new TemplateLoader(html);
	
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
	
		// poblar combobox horas entre las 00:00 y las 23:00
		// el value de cada item del combo debe tener la siguiente forma 12:00:00
		// preseleccionado siempre el de las 8:00 (en el html selected)
		
		
		// Boton cancelar me debe llevar a la pantalla anterior
		
		
		// Las fechas que aparecen al lado de cada dia de la semana deben ser las correspondientes
		
		
		
		
		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate biz = new BizDelegate();
		
		//SemanasEntity sem = new SemanasEntity();
		
		
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
		sem = biz.getSemanaById(id_semana);
		
		
		Calendar cal1 = new GregorianCalendar();
		cal1.setFirstDayOfWeek(Calendar.MONDAY);
		logger.debug("cal1: " + cal1.toString());
		
		cal1.setTime(sem.getF_ini());
		cal1.getTime();
		


		// 5. Setea variables del template
		String strFecha;
		cal1.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		strFecha = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime()); 
		top.setVariable("{f_lu}"	, strFecha);
		
		cal1.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
		strFecha = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime());		
		top.setVariable("{f_ma}"	, strFecha);
		
		cal1.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
		strFecha = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime());
		top.setVariable("{f_mi}"	, strFecha);
		
		cal1.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
		strFecha = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime());
		top.setVariable("{f_ju}"	, strFecha);
		
		cal1.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
		strFecha = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime());
		top.setVariable("{f_vi}"	, strFecha);
		
		cal1.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
		strFecha = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime());
		top.setVariable("{f_sa}"	, strFecha);
		
		cal1.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		strFecha = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime());
		top.setVariable("{f_do}"	, strFecha);
	
		
		
		top.setVariable("{id_semana}"	, paramId_semana); //reemplazar
		top.setVariable("{fecha_param}" ,paramFecha);
		// 6. Setea variables bloques		
		top.setDynamicValueSets("select_hora_inicio", horas);
		top.setDynamicValueSets("select_hora_fin", horas);
		
		// 7. variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
		
		// 8. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	
	}//execute
	
	
}
