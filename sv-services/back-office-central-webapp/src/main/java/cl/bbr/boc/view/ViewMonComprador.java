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
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.CompradorCriteriaDTO;
import cl.bbr.vte.empresas.dto.CompradoresDTO;


/**
 * Muestra el monitor de Compradores
 * despliega los datos del comprador, se puede utilizar filtros de búsqueda.
 * 
 * @author BBR
 *
 */
public class ViewMonComprador extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		int regsperpage 	= 10;
		long id_empresa 	= -1;
		String empresa      = "";
		long id_sucursal	= -1;
		String sucursal     = "";
		int pag 			= 1;
		String buscapor     = "";
		String buscar       = "";
		String msje			= "";
		logger.debug("User: " + usr.getLogin());

		// 1. Parámetros de inicialización servlet
		regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
		logger.debug("RegsPerPage: " + regsperpage);
		
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);	
		
		// 2. Procesa parámetros del request
		if ( req.getParameter("pagina") != null ){
			pag = Integer.parseInt( req.getParameter("pagina") );			
		}	
		if ( req.getParameter("msje") != null ){
			msje =  req.getParameter("msje");			
		}	
		if ( (req.getParameter("id_empresa") != null) && !(req.getParameter("id_empresa").equals("")) ){
			id_empresa = Long.parseLong( req.getParameter("id_empresa") );
		}
		if ( (req.getParameter("id_sucursal") != null) && !(req.getParameter("id_sucursal").equals("")) ){
			id_sucursal = Long.parseLong( req.getParameter("id_sucursal") );
		}
		
		if ((req.getParameter("buscapor") != null) && !(req.getParameter("buscapor").equals("")) ){
			buscapor = req.getParameter("buscapor");
		}
		if ((req.getParameter("buscar") != null) && !(req.getParameter("buscar").equals(""))){
			buscar = req.getParameter("buscar");
		}
		
		if ((req.getParameter("empresa") != null) && !(req.getParameter("empresa").equals(""))){
			empresa = req.getParameter("empresa");
		}
		if ((req.getParameter("sucursal") != null) && !(req.getParameter("sucursal").equals(""))){
			sucursal = req.getParameter("sucursal");
		}
		logger.debug("id_empresa : "+id_empresa);
		logger.debug("id_sucursal : "+id_sucursal);
			
		//		3. Template
		View salida = new View(res);
	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		BizDelegate bizDelegate = new BizDelegate();

		//parametros para paginar
    	//top.setVariable("{check_1}","checked");


		// 4.1 Listado de compradores
		
		CompradorCriteriaDTO criterio = new CompradorCriteriaDTO();
		criterio.setId_empresa(id_empresa);
		criterio.setId_sucursal(id_sucursal);
		criterio.setPag(pag);
		criterio.setRegsperpage(regsperpage);
		if (buscapor.toUpperCase().equals("RUT")){
			criterio.setRut(buscar);
			top.setVariable("{check_2}", "");
		}else if (buscapor.toUpperCase().equals("APELLIDO")){
			criterio.setApellido(buscar);
			top.setVariable("{check_2}", "checked");
		}
		/*
		// Obtiene el resultado
		//Listado de empresas
		ArrayList empresas = new ArrayList();
		EmpresaCriteriaDTO crit_emp = new EmpresaCriteriaDTO ();
		crit_emp.setEstado("");
		crit_emp.setRazon_social("");
		crit_emp.setRut("");
		crit_emp.setRegsperpage(1000);
		crit_emp.setPag(1);
		List lst_empresas = bizDelegate.getEmpresasByCriteria(crit_emp); 
		for (int i = 0; i< lst_empresas.size(); i++){
			IValueSet fila_est = new ValueSet();
			EmpresasDTO emp1 = (EmpresasDTO)lst_empresas.get(i);
			if (id_empresa<=0 && i==0)
				id_empresa = emp1.getEmp_id();
			fila_est.setVariable("{emp_id}", String.valueOf(emp1.getEmp_id()));
			fila_est.setVariable("{emp_nombre}"	, String.valueOf(emp1.getEmp_nom()));
			if(id_empresa==emp1.getEmp_id()){
				fila_est.setVariable("{sel_emp}"	, "selected");
			}else{
				fila_est.setVariable("{sel_emp}"	, "");
			}
			empresas.add(fila_est);
		}
		
		//Listado de sucursales
		ArrayList sucursales = new ArrayList();
		List lst_sucursales = null;
		if(id_empresa >0)
			lst_sucursales = bizDelegate.getSucursalesByEmpresaId(id_empresa);
		else
			lst_sucursales = new ArrayList();
			//bizDelegate.getSucursalesByCriteria(crit_suc); 
		for (int i = 0; i< lst_sucursales.size(); i++){
			IValueSet fila_est = new ValueSet();
			SucursalesDTO suc = (SucursalesDTO)lst_sucursales.get(i);
			fila_est.setVariable("{suc_id}", String.valueOf(suc.getSuc_id()));
			fila_est.setVariable("{suc_nombre}"	, String.valueOf(suc.getSuc_nombre()));
			if( id_sucursal==suc.getSuc_id()){
				fila_est.setVariable("{sel_suc}"	, "selected");
			}else{
				fila_est.setVariable("{sel_suc}"	, "");
			}
			sucursales.add(fila_est);
		}
		*/
		//	Lista de compradores
		List lst_compradores = null;
		
		lst_compradores=  bizDelegate.getCompradoresByCriteria(criterio);
			
		//List lst_est = bizDelegate.getEstadosByVis("COM","S");
	
		ArrayList datos = new ArrayList();
		if (lst_compradores.size() < 1 ){
			top.setVariable("{mje1}","La consulta no arrojo resultados");
		}else{
			top.setVariable("{mje1}","");
		}

		for (int i = 0; i < lst_compradores.size(); i++) {			
				IValueSet fila = new ValueSet();
				CompradoresDTO com = (CompradoresDTO)lst_compradores.get(i);
				fila.setVariable("{com_id}",	String.valueOf(com.getCpr_id()));
				fila.setVariable("{com_rut}",	String.valueOf(com.getCpr_rut()));
				fila.setVariable("{com_dv}",	String.valueOf(com.getCpr_dv()));
				fila.setVariable("{com_nombre}",String.valueOf(com.getCpr_nombres()));
				fila.setVariable("{com_ape_pat}",String.valueOf(com.getCpr_ape_pat()));
				fila.setVariable("{com_ape_mat}",String.valueOf(com.getCpr_ape_mat()));
				
				datos.add(fila);
			}		
		
	
		//		 5 Paginador

		ArrayList pags = new ArrayList();
		double tot_reg = bizDelegate.getCompradoresCountByCriteria(criterio);
		double total_pag = (double)Math.ceil(tot_reg/regsperpage);
		if (total_pag == 0){
			total_pag = 1;
		}
		logger.debug("Total de registros: " + tot_reg);
	    for (int i = 1; i <= total_pag; i++) {
			IValueSet fila_pag = new ValueSet();
			fila_pag.setVariable("{pag}",String.valueOf(i));
			if (i == pag){
				fila_pag.setVariable("{sel_pag}","selected");
			}
			else
				fila_pag.setVariable("{sel_pag}","");				
			pags.add(fila_pag);
		}	    
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
		//Setea variables main del template 
	    
	    top.setVariable("{num_pag}"	,String.valueOf(pag));
	    top.setVariable("{buscapor}"  , buscapor);
		top.setVariable("{buscar}"  , String.valueOf(buscar));
		top.setVariable("{id_empresa}"  , String.valueOf(id_empresa));
		top.setVariable("{empresa}"  , String.valueOf(empresa));
		top.setVariable("{id_sucursal}"  , String.valueOf(id_sucursal));
		top.setVariable("{sucursal}"  , String.valueOf(sucursal));
		top.setVariable("{msje}"  , msje);
		//top.setVariable("{action}"	,action);

		// 6. Setea variables bloques
	    top.setDynamicValueSets("lst_compradores", datos);
	   // top.setDynamicValueSets("EMPRESAS", empresas);
	   // top.setDynamicValueSets("SUCURSALES", sucursales);
		top.setDynamicValueSets("PAGINAS", pags);

		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());			
		
		//		 7. Salida Final
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();		
	
	}
	
}
