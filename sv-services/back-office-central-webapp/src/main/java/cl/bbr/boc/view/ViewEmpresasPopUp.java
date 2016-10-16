package cl.bbr.boc.view;

import java.util.ArrayList;
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
import cl.bbr.vte.empresas.dto.EmpresaCriteriaDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
/**
 * formulario para búsqueda de productos
 * @author BBRI
 */
public class ViewEmpresasPopUp extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		View salida = new View(res);
		int pag;
		int regsperpage;
		
		String rut="";
		String raz_soc="";
		String buscapor = "";
		
		//logger.debug("User: " + usr.getLogin());
        regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
        logger.debug("RegsPerPage: " + regsperpage);		

		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//parámetros
		if ( req.getParameter("buscapor") != null )
			buscapor =  req.getParameter("buscapor");
		
		
			
	
		if ( req.getParameter("pagina") != null )
			pag =  Integer.parseInt(req.getParameter("pagina")) ;
		else {
			pag = 1;
		}	
	
	
		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();
		
		top.setVariable("{check1}", "checked");
		
		logger.debug("Esta marcardo el buscar por:" + buscapor);
		if( buscapor != null && req.getParameter("buscar") != null){
			
			if(buscapor.toUpperCase().equals("RUT")){
				rut = req.getParameter("buscar");
				top.setVariable("{buscar}", rut);
				top.setVariable("{check1}", "checked");
				top.setVariable("{check2}", "");
			}
			else if(buscapor.toUpperCase().equals("RAZ_SOC")){
				raz_soc =  req.getParameter("buscar");
				top.setVariable("{buscar}", raz_soc);
				top.setVariable("{check2}", "checked");
				top.setVariable("{check1}", "");
			}else{
				top.setVariable("{buscar}", "");
				top.setVariable("{check2}", "");
				top.setVariable("{check1}", "checked");
			}
				
		}else{
			top.setVariable("{buscar}", "");
			
		}
		
//		listado de empresas
		logger.debug("Obtiene listado de empresas");
		ArrayList empresas = new ArrayList();
		EmpresaCriteriaDTO crit_emp = new EmpresaCriteriaDTO ();
		//crit_emp.setEstado("");
		crit_emp.setRazon_social(raz_soc);
		crit_emp.setRut(rut);
		crit_emp.setRegsperpage(regsperpage);
		crit_emp.setPag(pag);
		List lst_empresas = bizDelegate.getEmpresasByCriteria(crit_emp); 
		for (int i = 0; i< lst_empresas.size(); i++){
			IValueSet fila_emp = new ValueSet();
			EmpresasDTO emp1 = (EmpresasDTO)lst_empresas.get(i);
			fila_emp.setVariable("{emp_id}", String.valueOf(emp1.getEmp_id()));
			fila_emp.setVariable("{rut_emp}", emp1.getEmp_rut()+"-"+emp1.getEmp_dv());
			fila_emp.setVariable("{emp_raz_social}"	, String.valueOf(emp1.getEmp_rzsocial()));
			fila_emp.setVariable("{valor}"		, String.valueOf(emp1.getEmp_id())+ "|"+ emp1.getEmp_rzsocial()
												+"|"+emp1.getEmp_rut()+"|"+emp1.getEmp_dv());
					
			empresas.add(fila_emp);
		}
		
		
		
		
		top.setVariable("{buscapor}", buscapor);
		// 5.Paginador
			ArrayList pags = new ArrayList();
			double tot_reg = bizDelegate.getEmpresasCountByCriteria(crit_emp);
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
		/*    if( pag >1){
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
		*/
		// 6. Setea variables bloques
	    top.setDynamicValueSets("PAGINA", pags);
	    top.setDynamicValueSets("EMPRESAS", empresas);


		String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}
