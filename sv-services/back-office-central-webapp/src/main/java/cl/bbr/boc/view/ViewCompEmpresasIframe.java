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
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;

/**
 * despliega el formulario que permite mostra las empresas relacionadas a un comprador
 * 
 * @author BBRI
 */
public class ViewCompEmpresasIframe extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_comprador=0;
		String msje			= "";
		
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
		
		//parametro
		if(req.getParameter("id_comprador")!=null)
			id_comprador = Long.parseLong(req.getParameter("id_comprador"));
		logger.debug("id_comprador:"+id_comprador);
		if( req.getParameter("msje") != null )
			msje = req.getParameter("msje");

		BizDelegate bizDelegate = new BizDelegate();
		
		//obtiene datos de comprador
		
		top.setVariable("{id_comprador}"	,String.valueOf(id_comprador));
		
		//listado de sucursales del comprador
		ArrayList suc_compr = new ArrayList();
		List listSuc = bizDelegate.getListAdmEmpresasByCompradorId(id_comprador);

		for (int i = 0; i< listSuc.size(); i++){
			IValueSet fila_est = new ValueSet();
			EmpresasDTO emp = (EmpresasDTO)listSuc.get(i);
			fila_est.setVariable("{emp_id}"		, String.valueOf(emp.getEmp_id()));
			fila_est.setVariable("{emp_id_comp}", String.valueOf(id_comprador));
			fila_est.setVariable("{emp_nom}", String.valueOf(emp.getEmp_nom() +" - "+ emp.getEmp_rzsocial()));
			suc_compr.add(fila_est);
		}
		
		if(msje.equals("")){
			top.setVariable("{msje}"  , msje);
		}else{
			top.setVariable("{msje}"  , "<script language='JavaScript'>alert('"+msje+"');</script>");
		}
		
		top.setDynamicValueSets("SUC_COMPR", suc_compr);
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