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
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.EmpresaCriteriaDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;

/**
 * despliega el formulario que permite crear una nueva sucursal
 * 
 * @author BBRI
 */
public class ViewSucursalNewForm extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		View salida = new View(res);
		logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		

		BizDelegate bizDelegate = new BizDelegate();
		
		//listado de empresas
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
			fila_est.setVariable("{emp_id}", String.valueOf(emp1.getEmp_id()));
			fila_est.setVariable("{emp_raz_social}"	, String.valueOf(emp1.getEmp_rzsocial()));
				fila_est.setVariable("{sel2}","");		
			empresas.add(fila_est);
		}
		
		//estados
		ArrayList estados = new ArrayList();
		List listEst = bizDelegate.getEstadosByVis("SUC","S");

		for (int i = 0; i< listEst.size(); i++){
			IValueSet fila_est = new ValueSet();
			EstadoDTO est1 = (EstadoDTO)listEst.get(i);
			fila_est.setVariable("{id_estado}", String.valueOf(est1.getId_estado()));
			fila_est.setVariable("{nom_estado}"	, String.valueOf(est1.getNombre()));
			fila_est.setVariable("{sel_est}","");		
			estados.add(fila_est);
		}
		
		top.setDynamicValueSets("EMPRESAS", empresas);
		top.setDynamicValueSets("ESTADOS", estados);
		top.setVariable("{fec_actual}"	,Formatos.getFechaActual());
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
		top.setVariable("{fecha}"	,Formatos.getFechaActual());
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}