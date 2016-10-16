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
import cl.bbr.vte.empresas.dto.SucursalCriteriaDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;
/**
 * formulario para búsqueda de productos
 * @author BBRI
 */
public class ViewSucursalesEmpPopUp extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		View salida = new View(res);
		int pag;
		int regsperpage;
		long id_emp = -1;
		String rut="";
		String nombre="";
		//String buscapor = "";
		
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

		
		if ( req.getParameter("emp_id") != null )
			id_emp =  Long.parseLong(req.getParameter("emp_id"));
		
		if ( req.getParameter("pagina") != null )
			pag =  Integer.parseInt(req.getParameter("pagina")) ;
		else {
			pag = 1;
		}	
	
	
		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();
		
	
		if (req.getParameter("buscar") != null){
			nombre = req.getParameter("buscar");
		}
//		listado de empresas
		logger.debug("Obtiene listado de sucursales");
		ArrayList sucursales = new ArrayList();
		SucursalCriteriaDTO criterio = new SucursalCriteriaDTO();
		criterio.setPag(pag);
		criterio.setId_empresa(id_emp);
		criterio.setRut(rut);
		criterio.setNombre(nombre);
		criterio.setRegsperpage(regsperpage);
		List lst_sucursales = null;
		
		lst_sucursales=  bizDelegate.getSucursalesByCriteria(criterio);
		
		
		for (int i = 0; i< lst_sucursales.size(); i++){
			IValueSet fila_suc = new ValueSet();
			SucursalesDTO suc1 = (SucursalesDTO)lst_sucursales.get(i);
			fila_suc.setVariable("{suc_id}", String.valueOf(suc1.getSuc_id()));
			fila_suc.setVariable("{rut_suc}", suc1.getSuc_rut()+"-"+suc1.getSuc_dv());
			fila_suc.setVariable("{suc_raz_social}"	, String.valueOf(suc1.getSuc_razon()));
			fila_suc.setVariable("{suc_nombre}"	, String.valueOf(suc1.getSuc_nombre()));
			fila_suc.setVariable("{valor}"		, String.valueOf(suc1.getSuc_id())+ "|"+ suc1.getSuc_nombre());
					
			sucursales.add(fila_suc);
		}
		
		
		
		
		top.setVariable("{buscar}", nombre);
		// 5.Paginador
			ArrayList pags = new ArrayList();
			double tot_reg = bizDelegate.getSucursalesCountByCriteria(criterio);
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

		    top.setVariable("{emp_id}", String.valueOf(id_emp));
		// 6. Setea variables bloques
	    top.setDynamicValueSets("PAGINA", pags);
	    top.setDynamicValueSets("SUCURSALES", sucursales);


		String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}
