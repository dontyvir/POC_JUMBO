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
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;

/**
 * despliega el formulario que permite crear una nueva sucursal
 * 
 * @author BBRI
 */
public class ViewWizardEmpSucursales2 extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long emp_id=0;
		String msje="";
		View salida = new View(res);
		logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		
		 if ( req.getParameter("emp_id") == null ){throw new ParametroObligatorioException("emp_id  es null");}
		 emp_id = Long.parseLong(req.getParameter("emp_id"));
		 
		 
			if ( req.getParameter("msje") != null )
				msje=req.getParameter("msje") ;
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		

		BizDelegate bizDelegate = new BizDelegate();
		
		//Listado de Sucursales asociados a la empresa
		

		logger.debug("Obtiene listado de sucursales");
		ArrayList sucursales = new ArrayList();
		List lst_sucursales = null;
		
		lst_sucursales=  bizDelegate.getSucursalesByEmpresaId(emp_id);
		List lst_est = bizDelegate.getEstadosByVis("EMP","S");
		int cant_reg = lst_sucursales.size();
		for (int i = 0; i< lst_sucursales.size(); i++){
			IValueSet fila_suc = new ValueSet();
			SucursalesDTO suc1 = (SucursalesDTO)lst_sucursales.get(i);
			fila_suc.setVariable("{suc_id}", String.valueOf(suc1.getSuc_id()));
			fila_suc.setVariable("{suc_nombre}"	, String.valueOf(suc1.getSuc_nombre()));
			fila_suc.setVariable("{suc_estado}"	, FormatoEstados.frmEstado(lst_est,(suc1.getSuc_estado())));
			fila_suc.setVariable("{emp_id}"	, String.valueOf(emp_id));
			sucursales.add(fila_suc);
		}
		
				
		
		
		//Se obtienen los datos de la empresa a la cual se asociaran las sucursales
		
		EmpresasDTO emp = bizDelegate.getEmpresaById(emp_id);
		top.setVariable("{id_empresa}"	,String.valueOf(emp.getEmp_id()));
		top.setVariable("{rut_suc}"		,String.valueOf(emp.getEmp_rut()));
		top.setVariable("{dv_suc}"		,String.valueOf(emp.getEmp_dv()));
		top.setVariable("{rz_soc_suc}"	,String.valueOf(emp.getEmp_rzsocial()));
		top.setVariable("{nom_emp}"		,String.valueOf(emp.getEmp_nom()));

		
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
		
//		 Si tiene al menos una sucursal habilita el boton siguiente
		if (cant_reg > 0){
			top.setVariable("{hab}"	,"Enabled");
		}else{
			top.setVariable("{hab}"	,"Disabled");
		}
		
		top.setDynamicValueSets("ESTADOS", estados);
		top.setDynamicValueSets("LIST_SUCURSALES", sucursales);
		
		top.setVariable("{fec_actual}"	,Formatos.getFechaActual());
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
		top.setVariable("{fecha}"	,Formatos.getFechaActual());
		top.setVariable("{msje}"	,msje);
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}