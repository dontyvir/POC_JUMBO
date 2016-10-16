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
import cl.bbr.boc.utils.FormatoEstados;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.EmpresaCriteriaDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;

/**
 * Muestra el monitor de Empresas
 * despliega los datos de la empresa, se puede utilizar filtros de búsqueda.
 * 
 * @author BBR
 *
 */
public class ViewMonEmpresa extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		int regsperpage 	= 10;
		String raz_social	="";
		String rut			= "";
		String idestado 	= "";
		int pag 			= 1;
		String buscapor 	= "";
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
		if ( req.getParameter("estado") != null )
			idestado = req.getParameter("estado");
		if( req.getParameter("msje") != null )
			msje = req.getParameter("msje");
		
		//		3. Template
		View salida = new View(res);
	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		BizDelegate bizDelegate = new BizDelegate();

		logger.debug("buscapor:"+req.getParameter("buscapor"));
		if(req.getParameter("buscapor")!=null)
			buscapor = req.getParameter("buscapor");
		//verifica si busca por rut o razon social
		if(buscapor.equals("rut")){
			rut = (String) req.getParameter("bus_emp");
			top.setVariable("{bus_emp}", rut);
			top.setVariable("{check_1}", "checked");
			top.setVariable("{check_2}", "");
		}else if(buscapor.equals("raz_social")){
			raz_social = (String) req.getParameter("bus_emp");
			top.setVariable("{bus_emp}", raz_social);
			top.setVariable("{check_1}", "");
	    	top.setVariable("{check_2}", "checked");
		}else{
			top.setVariable("{bus_emp}", "");
			top.setVariable("{check_1}", "checked");
	    	top.setVariable("{check_2}", "");
		}

		//parametros para paginar
    	//top.setVariable("{check_1}","checked");


		// 4.1 Listado de Empresas
		logger.debug("Este es el buscapor: " + buscapor);
		logger.debug("rut: " + rut);
		logger.debug("raz_social: " + raz_social);
		
		EmpresaCriteriaDTO criterio = new EmpresaCriteriaDTO();
		criterio.setPag(pag);
		criterio.setRut(rut);
		criterio.setRazon_social(raz_social);
		criterio.setRegsperpage(regsperpage);
		criterio.setEstado(idestado);
		
		// Obtiene el resultado

			ArrayList estados = new ArrayList();
		
			List listEstados = bizDelegate.getEstadosByVis("EMP","S");
		
			for (int i = 0; i< listEstados.size(); i++){
				IValueSet fila_est = new ValueSet();
				EstadoDTO est1 = (EstadoDTO)listEstados.get(i);
				fila_est.setVariable("{id_estado}", String.valueOf(est1.getId_estado()));
				fila_est.setVariable("{nom_estado}"	, String.valueOf(est1.getNombre()));
				
				if (idestado != "" && String.valueOf(idestado).equals(String.valueOf(est1.getId_estado()))){
					fila_est.setVariable("{sel1}","selected");
				}
				else
					fila_est.setVariable("{sel1}","");		

				estados.add(fila_est);
				
			}

	//	Lista de clientes
		List lst_empresas = null;
		
		lst_empresas=  bizDelegate.getEmpresasByCriteria(criterio);
			
		List lst_est = bizDelegate.getEstadosByVis("EMP","S");
	
		ArrayList datos = new ArrayList();
		if (lst_empresas.size() < 1 ){
			top.setVariable("{mje1}","La consulta no arrojo resultados");
		}else{
			top.setVariable("{mje1}","");
		}

		for (int i = 0; i < lst_empresas.size(); i++) {			
				IValueSet fila = new ValueSet();
				EmpresasDTO emp1 = (EmpresasDTO)lst_empresas.get(i);
				
				fila.setVariable("{emp_id}",	String.valueOf(emp1.getEmp_id()));
				fila.setVariable("{emp_rut}",	String.valueOf(emp1.getEmp_rut()));
				fila.setVariable("{emp_dv}",	String.valueOf(emp1.getEmp_dv()));
				fila.setVariable("{emp_rzsocial}",	String.valueOf(emp1.getEmp_rzsocial()));
				fila.setVariable("{emp_rubro}",	String.valueOf(emp1.getEmp_rubro()));
				fila.setVariable("{emp_descr}",	String.valueOf(emp1.getEmp_descr()));
				fila.setVariable("{emp_estado}",	FormatoEstados.frmEstado(lst_est,(emp1.getEmp_estado())));
				datos.add(fila);
			}		
		
	
		//		 5 Paginador

		ArrayList pags = new ArrayList();
		double tot_reg = bizDelegate.getEmpresasCountByCriteria(criterio);
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
	    
	    top.setVariable("{buscar}"  ,rut+raz_social);
	    top.setVariable("{buscapor}",buscapor);
	    top.setVariable("{num_pag}"	,String.valueOf(pag));
		top.setVariable("{estado}"  , String.valueOf(idestado));
		top.setVariable("{msje}"  , msje);
		//top.setVariable("{action}"	,action);

		// 6. Setea variables bloques
	    top.setDynamicValueSets("listado_emp", datos);
	    top.setDynamicValueSets("ESTADOS", estados);
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
