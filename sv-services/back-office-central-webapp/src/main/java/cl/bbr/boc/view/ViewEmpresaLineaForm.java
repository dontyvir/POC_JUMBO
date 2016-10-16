package cl.bbr.boc.view;

import java.util.Date;

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
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;

/**
 * Despliega el formulario que contiene los datos de la empresa
 * 
 * @author BBRI
 */
public class ViewEmpresaLineaForm extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_empresa=0;
		String msje			= "";
		
		View salida = new View(res);
		
		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		if ( req.getParameter("id_empresa") != null )
            id_empresa = Long.parseLong(req.getParameter("id_empresa"));
        if( req.getParameter("msje") != null )
			msje = req.getParameter("msje");
		
		BizDelegate bizDelegate = new BizDelegate();
		
		EmpresasDTO emp = bizDelegate.getEmpresaById(id_empresa);
		
		
		top.setVariable("{id_empresa}"	,String.valueOf(emp.getEmp_id()));
		top.setVariable("{rut}"			,String.valueOf(emp.getEmp_rut()));
		top.setVariable("{dv}"			,String.valueOf(emp.getEmp_dv()));
		top.setVariable("{raz_social}"	,String.valueOf(emp.getEmp_rzsocial()));
		top.setVariable("{nombre}"		,String.valueOf(emp.getEmp_nom()));
        
        
        top.setVariable("{saldo}"        ,Formatos.formatoPrecio(emp.getEmp_saldo()) );
        top.setVariable("{saldo_old}"        , String.valueOf( emp.getEmp_saldo() ));
        top.setVariable("{fec_act_saldo}",String.valueOf(emp.getEmp_fact_saldo()));
        
        double saldo_actual_pend = bizDelegate.getSaldoActualPendiente(emp.getEmp_id());
        top.setVariable("{utilizado}", Formatos.formatoPrecio(saldo_actual_pend));
        double disponible = emp.getEmp_saldo() - saldo_actual_pend;
        top.setVariable("{disp}", Formatos.formatoPrecio(disponible));
        
        
        
        
		top.setVariable("{msje}"  , msje);
        
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}
	
}

