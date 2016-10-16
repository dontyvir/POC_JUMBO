package cl.bbr.bol.view;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorRondasDTO;
import cl.bbr.jumbocl.pedidos.dto.RondasCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.SectorLocalDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Despliega el monitor de rondas, si no hay alguno de los parametros seleccionados, entonces despliega valores por defecto con la fecha actual
 * @author mleiva 
 */
public class ViewMonitorRondas extends Command {

	
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		int    regsperpage = 10;
		int    regPagina   = 5000; 
		int    pag = 1;
		int    pag_seleccionada = 1;	
		long   id_ronda	  = -1;		
		long   id_jornada = -1;
		long   id_seccion = -1;
		long   id_estado  = -1;
		long   id_zona    = -1;
		String param_id_ronda	= "";
		String param_fecha		= "";
		String param_id_jornada	= "-1";
		String param_id_seccion	= "-1";
		String param_id_estado  = "-1";
		String StrFecha1;
		Date   fecha    = new Date();		
		String fecha_ro = "";
		String anterior	= "";
		String siguiente= "";
		String local_tipo_picking = "";
		
		// 1. Parámetros de inicialización servlet
		regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
		
		logger.debug("RegsPerPage: " + regsperpage);

		String html = "";
	    local_tipo_picking = usr.getLocal_tipo_picking();
        if (local_tipo_picking.equals("L")){
            html = getServletConfig().getInitParameter("TplFilePKL");
        }else{
            html = getServletConfig().getInitParameter("TplFile");
        }

		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);		
		
		String MsjeError = getServletConfig().getInitParameter("MsjeError");		
		
		String mensaje="";
		if ( req.getParameter("mensaje") != null ){
			mensaje = req.getParameter("mensaje");
		}
		
		
		/*if (local_tipo_picking.equalsIgnoreCase("N")){//N: Picking Normal
		    direccion = "ViewJornada";
		}else{//L: Picking Light
		    direccion = "ViewJornadaPKL";
		}*/

		
		logger.debug("1");
		// 2. Procesa parámetros del request
		if ( req.getParameter("pag") != null )
			pag_seleccionada = Integer.parseInt( req.getParameter("pag") );
		else		
			pag_seleccionada = 1;  // por defecto mostramos la página 1

		logger.debug("2");
		if ( req.getParameter("id_ronda") != null ){
		//	try{
				param_id_ronda = req.getParameter("id_ronda");
				id_ronda = Long.parseLong(param_id_ronda);
				logger.debug("id_ronda:"+id_ronda);
			/*}
			catch (Exception e){
				logger.debug("se cae, todo mal!");
			}*/
			
		}
		logger.debug("3");		
		if ( req.getParameter("fecha") != null && !req.getParameter("fecha").equals("")){
			param_fecha = req.getParameter("fecha");			
			logger.debug("Fecha: "+param_fecha);			
			fecha = new SimpleDateFormat("yyyy-MM-dd").parse(param_fecha);			
		}else{
			param_fecha = new SimpleDateFormat("yyyy-MM-dd").format(fecha);
		}		
		logger.debug("4");
		
		if ( req.getParameter("id_jornada") != null ){
			param_id_jornada = req.getParameter("id_jornada");
			id_jornada = Long.parseLong(param_id_jornada);
		}
		if ( req.getParameter("id_seccion") != null ){
			param_id_seccion = req.getParameter("id_seccion");
			id_seccion = Long.parseLong(param_id_seccion);
		}
		if ( req.getParameter("id_estado") != null ){
			param_id_estado = req.getParameter("id_estado");	
			id_estado = Long.parseLong(param_id_estado);
			logger.debug("Id del estado recibido = "+id_estado);
		}
		if ( req.getParameter("id_zona") != null  && !req.getParameter("id_zona").equals("")){
			id_zona = Long.parseLong(req.getParameter("id_zona"));	
			logger.debug("Id zona recibido = "+id_zona);
		}
		Calendar cal1 = new GregorianCalendar();
		cal1.setFirstDayOfWeek(Calendar.MONDAY);
		logger.debug("cal1: " + cal1.toString());
		logger.debug("param_fecha:"+param_fecha);
		logger.debug("fecha:"+fecha);		
		cal1.setTime(fecha);
		cal1.getTime();	
		StrFecha1 = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime());
		int year = cal1.get(Calendar.YEAR);
		int woy	 = cal1.get(Calendar.WEEK_OF_YEAR);		
		
		//obtener el local del usuario
		long id_local = usr.getId_local();
		logger.debug("id_local:"+id_local);
		
		// 3. Template
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);
		
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		// 4.  Rutinas Dinámicas 
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		// creamos los criterios
		RondasCriteriaDTO criterio = new RondasCriteriaDTO();
		Calendar cal_si = new GregorianCalendar(); 
		Calendar cal_an = new GregorianCalendar(); 
		
		if (id_ronda != -1){
			criterio.setId_ronda(id_ronda);
			logger.debug("criterio x ronda");
		}
		if(id_jornada!= -1){
			criterio.setId_jornada(id_jornada);
			logger.debug("criterio x jornada");
		}
		if(id_seccion != -1){
			criterio.setId_sector(id_seccion);
			logger.debug("criterio x sector");
		}
		if (id_estado!= -1){
			criterio.setId_estado(id_estado);
			logger.debug("criterio x estado");
		}
		else if ( !param_fecha.equals("") && (id_ronda == -1) ){  // si viene una fecha//&& (id_ronda == -1)
			logger.debug("criterio x param_fecha");
			criterio.setF_ronda(param_fecha);
			
			Date fecha_aux = new SimpleDateFormat("yyyy-MM-dd").parse(param_fecha);
			cal_si.setTimeInMillis(fecha_aux.getTime());
			cal_an.setTimeInMillis(fecha_aux.getTime());
			
			cal_si.add(Calendar.DATE, 1);
		    cal_an.add(Calendar.DATE, -1);		    
		    
		    siguiente 	= new SimpleDateFormat("yyyy-MM-dd").format(cal_si.getTime());
		    anterior 	= new SimpleDateFormat("yyyy-MM-dd").format(cal_an.getTime());
		    
		    logger.debug("fecha jornada:" +param_fecha);
		    logger.debug("fecha siguiente:" +siguiente);
		    logger.debug("fecha anterior:" +anterior);
			 
		}else if(id_ronda==-1){ // caso por defecto
			fecha_ro = new SimpleDateFormat("yyyy-MM-dd").format(fecha);
			criterio.setF_ronda( fecha_ro );
			cal_si.setTimeInMillis(fecha.getTime());
			cal_an.setTimeInMillis(fecha.getTime());
			cal_si.add(Calendar.DATE, 1);
		    cal_an.add(Calendar.DATE, -1);		    
		    siguiente = new SimpleDateFormat("yyyy-MM-dd").format(cal_si.getTime());
		    anterior = new SimpleDateFormat("yyyy-MM-dd").format(cal_an.getTime());
		    logger.debug("fecha actual:" +param_fecha);
		    logger.debug("fecha siguiente:" +siguiente);
		    logger.debug("fecha anterior:" +anterior);
		}
		if(id_ronda==-1){
			criterio.setF_ronda(StrFecha1);
		}
		criterio.setPag(pag);
		criterio.setRegsperpag(regPagina);
		criterio.setId_local(id_local);
		criterio.setId_zona(id_zona);
		criterio.setPagina_seleccionada(pag_seleccionada);
		//orden usando criterios

		List ordenarpor = new ArrayList();

		ordenarpor.add(RondasCriteriaDTO.ORDEN_NJPICK+" "+RondasCriteriaDTO.ORDEN_ASCENDENTE);
		if (!local_tipo_picking.equals("L")){
			ordenarpor.add(RondasCriteriaDTO.ORDEN_SECTOR+" "+RondasCriteriaDTO.ORDEN_ASCENDENTE);
		}
		ordenarpor.add(RondasCriteriaDTO.ORDEN_ESTADO+" "+RondasCriteriaDTO.ORDEN_ASCENDENTE);	
		
		criterio.setOrden_columnas(ordenarpor);

		if (local_tipo_picking.equals("L")){
		    criterio.setEsPickingLight("S");
		}
		logger.debug("*** Fecha de ronda:"+criterio.getF_ronda());
		logger.debug("*** Id ronda:"+criterio.getId_ronda());
		
		// 4.1 Listado de Rondas
		// Llama al BizDelegator
		//List listaRondas = bizDelegate.getMonRondasByCriteria(criterio);
		List listaRondas = bizDelegate.getMonRondasByCriteriaCMO(criterio);
		
		ArrayList datos = new ArrayList();
		logger.debug("lista Ronda size ="+listaRondas.size());
		String zona_anterior="";
		for (int i = 0; i < listaRondas.size(); i++) {		
			IValueSet fila = new ValueSet();
			MonitorRondasDTO ronda1 = (MonitorRondasDTO)listaRondas.get(i);
			fila.setVariable("{id_pedido}"   , String.valueOf(ronda1.getId_pedido()));
			fila.setVariable("{id_ronda}"    , String.valueOf(ronda1.getId_ronda()));
			fila.setVariable("{id_jornada}"  , String.valueOf(ronda1.getId_jornada()));
			fila.setVariable("{seccion_jorn}", ronda1.getSector());
			if(ronda1.getTipo_ve().equals(Constantes.TIPO_VE_SPECIAL_CTE)){
				fila.setVariable("{tipo_ve}" , Constantes.TIPO_VE_SPECIAL_TXT);
			}else{
				fila.setVariable("{tipo_ve}" , "");
			}
			logger.debug("tipo_ve:"+ronda1.getTipo_ve());
			fila.setVariable("{cant_prod}" , String.valueOf(ronda1.getCant_prods()));
			logger.debug("estado de la ronda:"+ronda1.getId_estado());
			fila.setVariable("{accion2}", "");
			if(ronda1.getId_estado()==Constantes.ID_ESTADO_RONDA_CREADA){
				fila.setVariable("{tiempo_trans}"	,Formatos.getTiempo(ronda1.getDif_creacion()));
			}else if(ronda1.getId_estado()==Constantes.ID_ESTADO_RONDA_EN_PICKING){
				fila.setVariable("{tiempo_trans}"	,Formatos.getTiempo(ronda1.getDif_picking()));
				//muestra la opcion de resetear la ronda
				fila.setVariable("{accion2}"	," | <a href=\"javascript: validar_eliminar('¿Esta seguro que desea resetear la ronda?','ResetearRonda?id_ronda="+ronda1.getId_ronda()+
						"&id_jornada="+ronda1.getId_jornada()+
						"&url=ViewMonitorRondas?id_ronda="+ronda1.getId_ronda()+						
						"&fecha=')\"> Resetear </a>");
			}else if(ronda1.getId_estado()==Constantes.ID_ESTADO_RONDA_FINALIZADA){
				fila.setVariable("{tiempo_trans}"	,Formatos.getTiempo(ronda1.getDif_termino()));
			}
			
			//fila.setVariable("{tiempo_trans}"	,Formatos.getDiferFecActual(ronda1.getF_creacion()));
			fila.setVariable("{estado_jorn}"	,ronda1.getEstado());
			fila.setVariable("{accion1}","Ver");
			fila.setVariable("{direccion1}","ViewRonda");			
			if(ronda1.getEstado().equals("Creada")){
				fila.setVariable("{palito}","|");				
				//fila.setVariable("{accion2}","preparar");
				fila.setVariable("{direccion2}","ViewPreparaRonda");
			}else{
				fila.setVariable("{palito}","");
				//fila.setVariable("{accion2}","");
				fila.setVariable("{direccion2}","");
			}
				
			if (param_id_estado != null && param_id_estado.equals(String.valueOf(ronda1.getId_estado()))){
				fila.setVariable("{sel}","selected");
			}else{
				fila.setVariable("{sel}","");
			}
			
			// revisar la primera zona anterior y comparar con la actual, si cambia la linea debe ser gruesa		
			
			fila.setVariable("{zonas}",ronda1.getZonas());
			String[] split;
			split =ronda1.getZonas().split(",");
			String zona_actual=split[0];
			logger.debug(i+".- zona anterior="+zona_anterior+" zona actual="+zona_actual);
			fila.setVariable("{borderstyle}","");
			if ((!zona_anterior.equals("")) && (!zona_anterior.equals(zona_actual))){ //borde grueso
				fila.setVariable("{borderstyle}","style='border-top: 2px solid #badb8a;'");
			}
			zona_anterior =zona_actual;
			datos.add(fila);
		}
		// 4.2 Paginador
		ArrayList pags = new ArrayList();
		//criterio.setRegsperpag(regsperpage);
		double tot_reg = bizDelegate.getCountRondasByCriteria(criterio);
		logger.debug("tot_reg: " + tot_reg + "");
		double total_pag = (double)Math.ceil(tot_reg/regsperpage);
		logger.debug ("round: " + total_pag);
		if (total_pag == 0){
			total_pag = 1;
		}
		logger.debug("round: " + (double) Math.ceil(tot_reg / regsperpage));

		for (int i = 1; i <= total_pag; i++) {
			IValueSet fila = new ValueSet();
			fila.setVariable("{pag}", String.valueOf(i));
			if (i == pag_seleccionada)
				fila.setVariable("{sel}", "selected");
			else
				fila.setVariable("{sel}", "");
			pags.add(fila);
		}

		logger.debug("antes de estado");
		// 4.3 Estados
		List listaestados = bizDelegate.getEstadosRonda();

		ArrayList estados = new ArrayList();
		for (int i = 0; i < listaestados.size(); i++) {
			IValueSet fila = new ValueSet();
			EstadoDTO estado1 = new EstadoDTO();
			estado1 = (EstadoDTO) listaestados.get(i);
			logger.debug("id_estado=" + estado1.getId_estado());
			logger.debug("estado=" + estado1.getNombre());
			fila.setVariable("{id_estado}", estado1.getId_estado() + "");
			fila.setVariable("{estado}", estado1.getNombre());

			if (Long.toString(id_estado) != null
					&& Long.toString(id_estado).equals(
							String.valueOf(estado1.getId_estado())))
				fila.setVariable("{sel2}", "selected");
			else
				fila.setVariable("{sel2}", "");

			estados.add(fila);
		}
		
		//4.4 Seccion		
		List listasecciones = bizDelegate.getSectores();
		ArrayList seccion = new ArrayList();
		for (int i = 0; i < listasecciones.size(); i++) {
			IValueSet fila = new ValueSet();
			SectorLocalDTO sector1 = new SectorLocalDTO();
			sector1 = (SectorLocalDTO) listasecciones.get(i);
			fila.setVariable("{id_seccion}"	, String.valueOf(sector1.getId_sector()));			
			fila.setVariable("{seccion}"	, sector1.getNombre());		

			if (Long.toString(id_seccion) != null
					&& Long.toString(id_seccion).equals(
							String.valueOf(sector1.getId_sector()))){
				fila.setVariable("{sel1}", "selected");
				id_seccion = sector1.getId_sector();
			}else{
				fila.setVariable("{sel1}", "");
			}
			seccion.add(fila);
		}
		//4.5 Jornada		
		//List listaJornadas = bizDelegate.getRondasByCriteria(criterio);
		Set jorn= new TreeSet();
		List jornadas = new ArrayList();
		logger.debug("lista Ronda size ="+listaRondas.size());

		for (int i = 0; i < listaRondas.size(); i++) {
			MonitorRondasDTO jornada1 = (MonitorRondasDTO)listaRondas.get(i);
			jorn.add( jornada1.getId_jornada()+"" );
		}
		String jor = ""; 
		for( Iterator it = jorn.iterator(); it.hasNext();) {
			jor =(String)it.next();	
			IValueSet fila = new ValueSet();			
			fila.setVariable("{id_jornada}"		,jor);			
			fila.setVariable("{jornada}"		,jor);			
			if (param_id_jornada != null && param_id_jornada.equals(jor)){
				fila.setVariable("{sel}","selected");
				param_id_jornada = jor;
			}else{
				fila.setVariable("{sel}","");
			}
			jornadas.add(fila);
		}
		//	4.6	Listado de Zonas
		List listaZonas = bizDelegate.getZonasLocal(usr.getId_local());
		ArrayList zona = new ArrayList();
		for (int i = 0; i < listaZonas.size(); i++) {
			IValueSet fila = new ValueSet();
			ZonaDTO zona1 = new ZonaDTO();
			zona1 = (ZonaDTO) listaZonas.get(i);
			fila.setVariable("{id_zona}"	, String.valueOf(zona1.getId_zona()));
			fila.setVariable("{nom_zona}"	, zona1.getNombre());		
			if (id_zona == zona1.getId_zona()){
				fila.setVariable("{sel_zona}"	, "selected");	
			}else{
				fila.setVariable("{sel_zona}"	, "");	
			}
			zona.add(fila);
		}
			
		
		// 5. Setea variables del template
		top.setVariable("{id_ronda}"	,param_id_ronda);
		top.setVariable("{id_jornada}"	,param_id_jornada);
		top.setVariable("{id_seccion}"	,param_id_seccion);
		top.setVariable("{id_estado}"	,param_id_estado);
		top.setVariable("{fecha}"		,param_fecha);
		logger.debug("id_jornada --->>>"+param_id_jornada);
		if(param_fecha.equals("")){
			top.setVariable("{fecha_ronda}"	,fecha_ro);
			top.setVariable("{rondas}"		,"Rondas del");			
			top.setVariable("{anterior_label}"  ,"<<< ");
			top.setVariable("{anterior}"  ,anterior);			
			top.setVariable("{siguiente_label}"  ," >>>");
			top.setVariable("{siguiente}"  ,siguiente);
		}else{
			top.setVariable("{fecha_ronda}"	,param_fecha);
			top.setVariable("{rondas}"		,"Rondas del");
			top.setVariable("{anterior_label}"  ,"<<< ");
			top.setVariable("{anterior}"  ,anterior);
			top.setVariable("{siguiente_label}"  ," >>>");
			top.setVariable("{siguiente}"  ,siguiente);
		}
		if(!param_id_ronda.equals("")){
			top.setVariable("{fecha_ronda}"	,"");
			top.setVariable("{rondas}"		,"");			
			top.setVariable("{anterior_label}"  ,"");
			top.setVariable("{anterior}"  		,"");			
			top.setVariable("{siguiente_label}" ,"");
			top.setVariable("{siguiente}"  ,siguiente);
		}
		if (listaRondas.size()<=0){
			top.setVariable("{mensaje}",MsjeError);
		}else{
			top.setVariable("{mensaje}",mensaje);
		}
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("select_ronda"	, datos);
		top.setDynamicValueSets("select_estado"	, estados);
		top.setDynamicValueSets("select_seccion", seccion);
		top.setDynamicValueSets("select_jornada", jornadas);
		top.setDynamicValueSets("select_zonas", zona);
		top.setDynamicValueSets("paginador"		, pags);
		//top.setDynamicValueSets("accion1", datos);
		//top.setDynamicValueSets("accion2", datos);
		
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
