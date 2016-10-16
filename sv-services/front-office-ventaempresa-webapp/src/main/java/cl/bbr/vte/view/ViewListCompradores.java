package cl.bbr.vte.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que muestra el listado de compradores que dependen de una empresa 
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewListCompradores extends Command {

	/**
	 * Despliega el listado de compradores que dependen de una empresa
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
			//throws CommandException {

		try {

			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("vte");			
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();		
			
			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.path.html") + "" + getServletConfig().getInitParameter("pag_form");
			logger.debug( "Template:"+pag_form );
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();
			
            //Despliega el Banner de inicio dependiendo del lugar desde donde se loguea
			top.setVariable("{bt_inicio}", session.getAttribute("bt_inicio").toString());

			//Se setea la variable tipo usuario
			if(session.getAttribute("ses_tipo_usuario") != null ){
				top.setVariable("{tipo_usuario}", session.getAttribute("ses_tipo_usuario").toString());
			}else{
				top.setVariable("{tipo_usuario}", "0");
			}

			// Nombre del comprador para header
			if( session.getAttribute("ses_com_nombre") != null )
				top.setVariable( "{nombre_comprador}", session.getAttribute("ses_com_nombre") );
			else
				top.setVariable( "{nombre_comprador}", "" );			
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			// Recuperar empresa si se ejecuta el filtro
			if( arg0.getParameter("accion") != null 
					&& arg0.getParameter("accion").toString().equals("filtro") == true ) {
				session.setAttribute("ses_adm_emp_id", arg0.getParameter("empresas").toString());
			}
			
			// Recupera lista de empresas que tiene acceso el comprador administrador	
			List l_comp = biz.getListAdmEmpresasByCompradorId( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			EmpresasDTO dto = null;
			List datos_empresas = new ArrayList();
			for( int i = 0; i < l_comp.size(); i++ ) {
				dto = (EmpresasDTO)l_comp.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{emp_nombre}", dto.getEmp_nom() );			
				fila.setVariable("{emp_id}", dto.getEmp_id() + "" );
				if( session.getAttribute("ses_adm_emp_id") != null && dto.getEmp_id() == Long.parseLong(session.getAttribute("ses_adm_emp_id").toString()) ) {
					fila.setVariable("{emp_selected}","selected");
				}
				else {
					fila.setVariable("{emp_selected}","");
				}				
				datos_empresas.add(fila);
			}
			top.setDynamicValueSets("LIST_EMPRESAS", datos_empresas );
		
			ArrayList arr_com = new ArrayList();
			
			List registros = biz.getListCompradoresByEmpresalId(Long.parseLong(session.getAttribute("ses_adm_emp_id").toString()));
			for (int i = 0; i < registros.size(); i++) {
				CompradoresDTO reg_tc = (CompradoresDTO) registros.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{id}", reg_tc.getCpr_id()+"");
				fila.setVariable("{nom_ape}", reg_tc.getCpr_nombres()+ " "+reg_tc.getCpr_ape_pat() );
				if (reg_tc.getCpr_tipo() != null)
					fila.setVariable("{tipo}", "Administrador");
				else
					fila.setVariable("{tipo}", "");
				
				arr_com.add(fila);
			}
			top.setDynamicValueSets("list_comp", arr_com);
		
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}

	}

}