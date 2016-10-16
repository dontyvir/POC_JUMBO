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
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesCriteriaDTO;
import cl.bbr.vte.cotizaciones.dto.EstadoDTO;
import cl.bbr.vte.cotizaciones.dto.LocalDTO;
import cl.bbr.vte.cotizaciones.dto.MonitorCotizacionesDTO;
import cl.bbr.vte.empresas.dto.EmpresaCriteriaDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.SucursalCriteriaDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;



/**
 * Despliega el monitor de cotizaciones, en éste es posible filtrar por:
 * Número de Cotización, Rut, Razón social, Nombre de la empresa o Rut del comprador,
 * Sucursal, Local, Estado y Rango de fechas ya sea de ingreso, vencimiento o despacho 
 * @author BBRI
 */
public class ViewMonCotizaciones extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {		
		logger.debug("comienzo ViewMonCotizaciones Execute");

		//Declaracion de Variables
		long numCot = -1;
		String tipo_bus="";
		String busca_por="";
		String rut_emp="";
		String raz_soc="";
		String nom_emp="";
		String rut_comp="";
		long emp_id = -1;
		long suc_id = -1;
		long loc_id = -1;
		long est_id = -1;
		String tipo_fec = "";
		String fec_ini = "";
		String fec_fin = "";
		String check2 = "";
		String check3 = "";
		String check4 = "";
		String emp_nom="";
		String suc_nom="";
			
		
		int regsperpage = 10;
		int pag = 1;
		String rc = ""; 
		String msje = "";
		
		// 		 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");		
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
		logger.debug("RegsPerPage: " + regsperpage);
		if (req.getParameter("rc") != null ) rc = req.getParameter("rc");
		logger.debug("rc:"+rc);
		if (req.getParameter("msje") != null ) msje = req.getParameter("msje");
		logger.debug("msje:"+msje);
		
		//		 2. Procesa parámetros del request
		if (req.getParameter("pagina") != null) {
			pag = Integer.parseInt(req.getParameter("pagina"));
			logger.debug("pagina: " + req.getParameter("pagina"));
		} 

		if (req.getParameter("num_cot") != null && !req.getParameter("num_cot").equals("")) {
			numCot = Long.parseLong(req.getParameter("num_cot"));
			logger.debug("id_cot: " + numCot);
		}
		if (req.getParameter("busca_por") != null && !req.getParameter("busca_por").equals("")   ){
			busca_por = req.getParameter("busca_por");
		}

		if (req.getParameter("rad_bus") != null &&  !req.getParameter("rad_bus").equals("") ){
			tipo_bus = req.getParameter("rad_bus"); 
			if (tipo_bus.toUpperCase().equals("RUT_EMP") && !busca_por.equals("")) {
				rut_emp = busca_por;
				logger.debug("rut_emp: "+rut_emp);
			}
			else if (tipo_bus.toUpperCase().equals("RAZ_SOC") && !busca_por.equals("") ) {
				raz_soc = busca_por;
				check2="checked";
				logger.debug("raz_soc: "+raz_soc);
			}
			else if (tipo_bus.toUpperCase().equals("NOM_EMP") && !busca_por.equals("")) {
				nom_emp = busca_por;
				check3="checked";
				logger.debug("nom_emp: "+nom_emp);
			}
			else if (tipo_bus.toUpperCase().equals("RUT_COMP") && !busca_por.equals("")) {
				rut_comp = busca_por;
				check4="checked";
				logger.debug("rut_comp: "+rut_comp);
			}
		}
		if (req.getParameter("sel_empresa") != null &&  !req.getParameter("sel_empresa").equals("") ){
			emp_id = Long.parseLong(req.getParameter("sel_empresa"));
			logger.debug("emp_id: "+emp_id);
		}
		if (req.getParameter("sel_sucursal") != null &&  !req.getParameter("sel_sucursal").equals("") ){
			suc_id = Long.parseLong(req.getParameter("sel_sucursal"));
			logger.debug("suc_id: "+suc_id);
		}
		if (req.getParameter("sel_locales") != null &&  !req.getParameter("sel_locales").equals("T") ){
			loc_id = Long.parseLong(req.getParameter("sel_locales"));
			logger.debug("loc_id: "+loc_id);
		}
		if (req.getParameter("sel_estados") != null &&  !req.getParameter("sel_estados").equals("T") ){
			est_id = Long.parseLong(req.getParameter("sel_estados"));
			logger.debug("est_id: "+est_id);
		}
		if ( req.getParameter("fec_ini") != null && !req.getParameter("fec_ini").equals("") ){
			fec_ini = req.getParameter("fec_ini");
			logger.debug("fec_ini:"+fec_ini);
		}
		if ( req.getParameter("fec_fin") != null && !req.getParameter("fec_fin").equals("")){
			fec_fin = req.getParameter("fec_fin");
			logger.debug("fec_fin:"+fec_fin);
		}
		
		if ((req.getParameter("empresa") != null) && !(req.getParameter("empresa").equals(""))){
			emp_nom = req.getParameter("empresa");
		}
		if ((req.getParameter("sucursal") != null) && !(req.getParameter("sucursal").equals(""))){
			suc_nom = req.getParameter("sucursal");
		}
		
		//		3. Template
		View salida = new View(res);	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		if ( req.getParameter("rad_fecha") != null && !req.getParameter("rad_fecha").equals("")) {
			tipo_fec = req.getParameter("rad_fecha");
			if (tipo_fec.toUpperCase().equals("VENC")){
				top.setVariable("{check_fecha2}","checked");
			}else if(tipo_fec.toUpperCase().equals("DESP")){
				top.setVariable("{check_fecha3}","checked");	
			}else if (tipo_fec.toUpperCase().equals("ING")){
				top.setVariable("{check_fecha1}","checked");
			}else{
				top.setVariable("{check_fecha4}","checked");	
				fec_ini = "";
				fec_fin ="";
			}
		}
	    
	    	    
	    
	    
	

		
		// 		 4.  Rutinas Dinámicas 
		// 		 4.0 Bizdelegator
		BizDelegate bizDelegate = new BizDelegate();

		
		//		 4.1 Listado de Cotizaciones
		
		CotizacionesCriteriaDTO criterio = new CotizacionesCriteriaDTO();
		criterio.setId_cot(numCot);
		criterio.setRut_emp(rut_emp);
		criterio.setRazon_social(raz_soc);
		criterio.setNom_emp(nom_emp);
		criterio.setRut_comprador(rut_comp);
		criterio.setId_empresa(emp_id);
		criterio.setId_sucursal(suc_id);
		criterio.setId_local(loc_id);
		criterio.setId_estado(est_id);
		criterio.setTipo_fec(tipo_fec);
		criterio.setPag(pag);
		criterio.setRegsperpag(regsperpage);
		if (!fec_ini.equals("") && !fec_fin.equals("")){
			criterio.setFec_ini(Formatos.formatFechaHoraIni(fec_ini));
			criterio.setFec_fin(Formatos.formatFechaHoraFin(fec_fin));
		}
		criterio.setTipo_comprador(1);
		
		
		
		//Listado de cotizaciones
		List listCotizacion=null;	
		
		listCotizacion=  bizDelegate.getCotizacionesByCriteria(criterio);
		
		if (listCotizacion.size() < 1 ){
			top.setVariable("{mje1}","La consulta no arrojo resultados");
			top.setVariable("{dis}","disabled");
		}
		else{
			top.setVariable("{mje1}","");
		}		
		ArrayList datos = new ArrayList();
			logger.debug("Cantidad de registros: " + listCotizacion.size());
		for (int i = 0; i < listCotizacion.size(); i++) {		
			IValueSet fila = new ValueSet();
			MonitorCotizacionesDTO cot1 = (MonitorCotizacionesDTO)listCotizacion.get(i);
			fila.setVariable("{num_cotizacion}"	, String.valueOf(cot1.getId_cot()));
			fila.setVariable("{rut_emp}"	    , cot1.getRut_empresa());
			fila.setVariable("{nom_emp}"		, cot1.getNom_empresa());
			fila.setVariable("{nom_sucursal}"	, cot1.getNom_sucursal());
			fila.setVariable("{fec_despacho}"	, Formatos.frmFecha(cot1.getFec_despacho()));
			fila.setVariable("{fec_vencimiento}", Formatos.frmFecha(cot1.getFec_vencimiento()));
			fila.setVariable("{cant_prod}"		, String.valueOf(cot1.getCant_prod()));
			fila.setVariable("{estado}"			, cot1.getEstado());
			
//			el usuario tiene una cotización asignada : solo puede ver esa cotización para editar 
			if (usr.getId_cotizacion()>0){
				if(cot1.getId_cot() == usr.getId_cotizacion() && cot1.getCot_estado() != Constantes.ID_EST_COTIZACION_INGRESADA){
					//la cotizacion es suyo
					fila.setVariable("{pipe}","|");
					fila.setVariable("{editar}","editar");
					fila.setVariable("{user}",String.valueOf(cot1.getId_usuario()));
				}
				else{
					//no muestra nada para las cotizaciones que no son suyos
					fila.setVariable("{pipe}","");
					fila.setVariable("{editar}","");
					fila.setVariable("{user}","");
				}				
			}
			else{
				//el usuario no tiene una cotización asignada : puede ver solo las cotizaciones sin asignaciones
				if (cot1.getId_usuario()<=0 && cot1.getCot_estado() != Constantes.ID_EST_COTIZACION_INGRESADA){
					fila.setVariable("{pipe}","|");
					fila.setVariable("{editar}","editar");
					fila.setVariable("{user}",String.valueOf(cot1.getId_usuario()));
				}
				else{
					fila.setVariable("{pipe}","");
					fila.setVariable("{editar}","");
					fila.setVariable("{user}","");
				}
				
			}			
			
			datos.add(fila);
		}		

		// 4.2 Listado de Locales
		
		
		List listLocales = null;
		listLocales = bizDelegate.getLocalesCotizacion();
		ArrayList locales = new ArrayList();
		for (int i = 0; i < listLocales.size(); i++) {		
			IValueSet local = new ValueSet();
			LocalDTO loc = (LocalDTO)listLocales.get(i);  
			local.setVariable("{loc_id}"	, String.valueOf(loc.getId_local()));
			local.setVariable("{loc_nombre}", loc.getNom_local());
			if (loc_id == loc.getId_local()){
				local.setVariable("{sel_loc}", "selected");
			}else{
				local.setVariable("{sel_loc}", "");
			}
			locales.add(local);
		}	
		
		

		// 4.3 Listado de Estados
		
		
		List listEstados = null;
		listEstados = bizDelegate.getEstadosCotizacion();
		ArrayList estados = new ArrayList();
		for (int i = 0; i < listEstados.size(); i++) {		
			IValueSet estado = new ValueSet();
			EstadoDTO est = (EstadoDTO)listEstados.get(i);  
			estado.setVariable("{est_id}"	, String.valueOf(est.getId_estado()));
			estado.setVariable("{est_nombre}", est.getNombre());
			if (est_id == est.getId_estado()){
				estado.setVariable("{sel_est}", "selected");
			}else{
				estado.setVariable("{sel_est}", "");
			}
			estados.add(estado);
		}	
		
		// 4.3 Listado de Sucursales
		
		
		List listSucursales = null;
		SucursalCriteriaDTO critSuc = new SucursalCriteriaDTO();
		if(emp_id>0){
			critSuc.setId_empresa(emp_id);
			critSuc.setPag(1);
			critSuc.setRegsperpage(1000);
		}
	//	critSuc.setId_empresa(-1);
		
		
		listSucursales = bizDelegate.getSucursalesByCriteria(critSuc);
		ArrayList sucursales = new ArrayList();
		for (int i = 0; i < listSucursales.size(); i++) {		
			IValueSet sucursal = new ValueSet();
			SucursalesDTO suc = (SucursalesDTO)listSucursales.get(i);  
			sucursal.setVariable("{suc_id}"	, String.valueOf(suc.getSuc_id()));
			sucursal.setVariable("{suc_nombre}", suc.getSuc_nombre());
			logger.debug("suc_id: " + suc_id);
			logger.debug("getsuc_id: " + suc.getSuc_id());
			if (suc_id == suc.getSuc_id() ){

				sucursal.setVariable("{sel_suc}", "selected");
			}else{
				sucursal.setVariable("{sel_suc}", "");
			}
			sucursales.add(sucursal);
		}
		
		
		
		// 4.4 Listado de Empresas
		
		
		List listEmpresas = null;
		EmpresaCriteriaDTO critEmp = null;
		
		listEmpresas = bizDelegate.getEmpresasByCriteria(critEmp);
		ArrayList empresas = new ArrayList();
		for (int i = 0; i < listEmpresas.size(); i++) {		
			IValueSet empresa = new ValueSet();
			EmpresasDTO emp = (EmpresasDTO)listEmpresas.get(i);  
			//logger.debug("emp id"+String.valueOf(emp.getEmp_id()));
			empresa.setVariable("{emp_id}"	, String.valueOf(emp.getEmp_id()));
			empresa.setVariable("{emp_nombre}", emp.getEmp_nom());
			if (emp_id == emp.getEmp_id()){
				empresa.setVariable("{sel_emp}", "selected");
			}else{
				empresa.setVariable("{sel_emp}", "");
			}
			empresas.add(empresa);
		}

		//5 Paginador
		ArrayList pags = new ArrayList();
		double tot_reg =0;
		tot_reg = bizDelegate.getCountCotizacionesByCriteria(criterio);
		
		logger.debug("tot_reg: " + tot_reg + "");
		double total_pag = (double)Math.ceil(tot_reg/regsperpage);
		logger.debug ("round: " + total_pag);
		if (total_pag == 0){
			total_pag = 1;
		}

		for (int i = 1; i <= total_pag; i++) {
			IValueSet fila = new ValueSet();
			fila.setVariable("{pag}", String.valueOf(i));
			if (i == pag)
				fila.setVariable("{sel}", "selected");
			else
				fila.setVariable("{sel}", "");
			pags.add(fila);
		}	
		//anterior y siguiente
		if( pag >1){
	    	int anterior = pag-1;
	    	top.setVariable("{anterior_label}","<< anterior");
	    	top.setVariable("{anterior}",String.valueOf(anterior));
	    }else if (pag==1){
	    	top.setVariable("{anterior_label}","");
	    }	    
	    if (pag <total_pag){
	    	int siguiente = pag+1;
	    	top.setVariable("{siguiente_label}","siguiente >>");
	    	top.setVariable("{siguiente}",String.valueOf(siguiente));
	    }else{
	    	top.setVariable("{siguiente_label}","");
	    }
	    
		//criterios de busqueda
	    if (numCot == -1)
	    	top.setVariable("{num_cot}","");
	    else
	    	top.setVariable("{num_cot}",String.valueOf(numCot));
	    top.setVariable("{rad_bus}",tipo_bus);
	    top.setVariable("{busca_por}",busca_por);
	    top.setVariable("{check_2}",check2);
	    top.setVariable("{check_3}",check3);
	    top.setVariable("{check_4}",check4);

	    top.setVariable("{sel_empresas}",String.valueOf(emp_id));
	    top.setVariable("{sel_sucursales}",String.valueOf(suc_id));
	    top.setVariable("{sel_locales}",String.valueOf(loc_id));
	    top.setVariable("{sel_estados}",String.valueOf(est_id));
	    top.setVariable("{rad_fecha}",tipo_fec);
	    top.setVariable("{fec_ini}",fec_ini);
	    top.setVariable("{fec_fin}",fec_fin);
	    top.setVariable("{id_sucursal}",String.valueOf(suc_id));
	    top.setVariable("{sucursal}",String.valueOf(suc_nom));
	    top.setVariable("{empresa}",String.valueOf(emp_nom));
		// 		 6. Setea variables bloques

		
		
		top.setDynamicValueSets("MON_COTIZACION", datos);
		top.setDynamicValueSets("LOCALES", locales);
		top.setDynamicValueSets("ESTADOS", estados);
		top.setDynamicValueSets("SUCURSALES", sucursales);
		top.setDynamicValueSets("EMPRESAS", empresas);
		top.setDynamicValueSets("PAGINAS", pags);

		
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	

		top.setVariable("{usr_cot}","");
		logger.debug("usr.getId_cotizacion():"+usr.getId_cotizacion());
		String var_usrcot = "";
		var_usrcot = "Ud. está editando la Cotización: <a href='ViewCotizacionesForm?cot_id="+usr.getId_cotizacion()+
		             "&mod=1'> "+usr.getId_cotizacion()+"</a> (" +
		             "<a href ='LiberaCotizacion?cot_id={num_cotizacion}&origen=2&url=ViewMonCotizaciones'> Liberar Cotización </a> )";
		
	
		if(usr.getId_cotizacion()>0)
			top.setVariable("{usr_cot}", var_usrcot);
		
		if ( rc.equals(Constantes._EX_COT_ID_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El código de Cotización no existe');</script>" );
		} else if ( rc.equals(Constantes._EX_COT_USR_TIENE_OTRA_COT) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El usuario debe liberar la cotización antes de asignar otra cotización');</script>" );
		} else if ( rc.equals(Constantes._EX_COT_TIENE_OTRO_USR) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('La cotización tiene otro usuario asignado');</script>" );
		} else {
			top.setVariable( "{mns}"	, "");
		}
		
		if(!msje.equals("")){
			top.setVariable( "{msje}"	, msje);
		}else{
			top.setVariable( "{msje}"	, "");
		}
		
		//		 7. Salida Final		
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}

