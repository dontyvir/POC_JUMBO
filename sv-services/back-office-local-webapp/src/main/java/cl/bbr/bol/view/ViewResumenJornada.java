package cl.bbr.bol.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.DespachoCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorDespachosDTO;
import cl.bbr.jumbocl.pedidos.dto.ResumenDespachoDTO;

import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * despliega el detalle de la jornada
 * @author mleiva
 */
public class ViewResumenJornada extends Command {

		
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String fecha = "";
		String h_despacho  = "";
		Date   hoy 			 	 = new Date();
		String anterior			 = "";
		String siguiente		 = "";
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		// 2. Procesa parámetros del request
		if (req.getParameter("fecha")!= null)
			fecha = req.getParameter("fecha");				
		if (req.getParameter("hora_desp") != null)
			h_despacho = req.getParameter("hora_desp");
		
		// 3. Template 
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		// 4.  Rutinas Dinámicas	
		// 4.0 Creamos al BizDelegator
		Calendar cal_si = new GregorianCalendar();
		Calendar cal_an = new GregorianCalendar();
		if (fecha.equals("")){
			fecha = new SimpleDateFormat("yyyy-MM-dd").format(hoy);
			cal_si.setTimeInMillis(hoy.getTime());
			cal_an.setTimeInMillis(hoy.getTime());
			cal_si.add(Calendar.DATE, 1);
		    cal_an.add(Calendar.DATE, -1);
		    siguiente = new SimpleDateFormat("yyyy-MM-dd").format(cal_si.getTime());
		    anterior = new SimpleDateFormat("yyyy-MM-dd").format(cal_an.getTime());
		}else{
			Date fecha_aux = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
			cal_si.setTimeInMillis(fecha_aux.getTime());
			cal_an.setTimeInMillis(fecha_aux.getTime());
			
			cal_si.add(Calendar.DATE, 1);
		    cal_an.add(Calendar.DATE, -1);
		    
		    siguiente 	= new SimpleDateFormat("yyyy-MM-dd").format(cal_si.getTime());
		    anterior 	= new SimpleDateFormat("yyyy-MM-dd").format(cal_an.getTime());
		}
		
		BizDelegate biz = new BizDelegate();
		//4.5 Hora Despacho
		DespachoCriteriaDTO cri = new DespachoCriteriaDTO();
		cri.setF_despacho(fecha);
		cri.setId_local(usr.getId_local());
		cri.setRegsperpag(10000);
		List listahoras = biz.getDespachosByCriteria(cri);
		ArrayList horas = new ArrayList();
		
		Set hora_completa = new TreeSet();
		for ( int i=0; i< listahoras.size();i++ ) {
			MonitorDespachosDTO hora1 = (MonitorDespachosDTO) listahoras.get(i);
			hora_completa.add(Formatos.frmHoraSola( hora1.getH_ini() )+"-"+ Formatos.frmHoraSola( hora1.getH_fin() ));
		}
        
		for( Iterator it = hora_completa.iterator(); it.hasNext();) {
			IValueSet fila = new ValueSet();
			String h_total =(String)it.next();
			fila.setVariable("{h_total}"	,h_total);
			if (h_despacho != null && h_despacho.equals(h_total))
				fila.setVariable("{sel}", "selected");
			else
				fila.setVariable("{sel}", "");
			horas.add(fila);
		}
		
		DespachoCriteriaDTO criterio = new DespachoCriteriaDTO();
		criterio.setId_local(usr.getId_local());

		criterio.setF_despacho(fecha);
		if (!h_despacho.equals("")){
		    int pos = h_despacho.indexOf("-");
			criterio.setH_inicio(h_despacho.substring(0, pos));
			criterio.setH_fin(h_despacho.substring(pos + 1));
		}
		//criterio.set
		criterio.setRegsperpag(10000);
		List lst_estadisticas = biz.getResumenJorDespacho(criterio);
		ArrayList datos = new ArrayList();
		for (int i = 0; i < lst_estadisticas.size(); i++) {
			IValueSet fila = new ValueSet();
			ResumenDespachoDTO desp = (ResumenDespachoDTO) lst_estadisticas.get(i);
			fila.setVariable("{id_zona}"	, String.valueOf(desp.getId_zona()));
			fila.setVariable("{zona_nom}"	, desp.getNom_zona());	
			fila.setVariable("{ped_solicitados}", String.valueOf(desp.getPed_solicitados()));
			fila.setVariable("{ped_validados}", String.valueOf(desp.getPed_validados()));
			fila.setVariable("{ped_en_pick}", String.valueOf(desp.getPed_en_pick()));
			fila.setVariable("{ped_en_bod}", String.valueOf(desp.getPed_en_bod()));
			fila.setVariable("{ped_en_pago}", String.valueOf(desp.getPed_en_pago()));
			fila.setVariable("{ped_pagado}", String.valueOf(desp.getPed_pagado()));
			fila.setVariable("{ped_en_desp}", String.valueOf(desp.getPed_en_desp()));
			fila.setVariable("{ped_finalizado}", String.valueOf(desp.getPed_finalizado()));
			fila.setVariable("{avance}", String.valueOf(desp.getAvance()) + " %");
            fila.setVariable("{rutas}", String.valueOf(desp.getPedSinRuta()));
			fila.setVariable("{acciones}", "<a href='ViewMonitorDespacho?id_zona="+desp.getId_zona()+"&fecha="+fecha+"&reprogramada=-1&horas="+h_despacho+"'>Ver | Crear Ruta</a>");
			datos.add(fila);
		}
		if (lst_estadisticas.size()<=0)
			top.setVariable("{mensaje}"	,"No existen registros para la fecha y jornada de despacho especificada.");
		else
			top.setVariable("{mensaje}"	,"");
		
		// 5. Setea variables del template
		top.setVariable("{fecha}", fecha);
		
		if(fecha.equals("")){
			top.setVariable("{fecha_despacho}"	,fecha);
			top.setVariable("{despacho}"		,"Despachos del");			
			top.setVariable("{anterior_label}"  ,"<<< ");
			top.setVariable("{anterior}"  ,anterior);			
			top.setVariable("{siguiente_label}"  ," >>>");
			top.setVariable("{siguiente}"  ,siguiente);
			
		}else{
			top.setVariable("{fecha_despacho}"	,fecha);
			top.setVariable("{despacho}"		,"Despachos del");
			top.setVariable("{anterior_label}"  ,"<<< ");
			top.setVariable("{anterior}"  ,anterior);
			top.setVariable("{siguiente_label}"  ," >>>");
			top.setVariable("{siguiente}"  ,siguiente);
		}
		
		
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("RESUMEN_JORNADA", datos);
		top.setDynamicValueSets("HORA_DESPACHO", horas);
		// 7. variables del header
		// variables header

		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
		
		// 8. Salida Final (se deja tal cual)
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	

	}
	
	
}
