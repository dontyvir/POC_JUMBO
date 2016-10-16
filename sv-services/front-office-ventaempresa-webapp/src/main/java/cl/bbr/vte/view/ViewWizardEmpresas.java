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
import cl.bbr.vte.empresas.dto.ComprXEmpDTO;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que muestra el listado de asociacion de Empresa/Comprador para WIZARD
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewWizardEmpresas extends Command {

	/**
	 * Despliega el listado de asociacion de Empresa/Comprador PARA WIZARD
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		try {

			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("vte");			
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();		
			
			// Recupera pagina desde web.xml
			String UrlError_com = getServletConfig().getInitParameter("dis_er_com");
			String UrlError_suc = getServletConfig().getInitParameter("dis_er_suc");
			
			String dis_ok = getServletConfig().getInitParameter("dis_ok");
			logger.debug("UrlOK: " + dis_ok);
			
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

			// Se revisa si se ha llegado con error
			if (arg0.getParameter("cod_error") != null) {
				logger.debug("cod_error = " + arg0.getParameter("cod_error"));
				top.setVariable("{msg_error}", "1");
				top.setVariable("{desc_msg_error}", rb.getString("general.mensaje.error"));
				// 2.- Poner los datos en el formulario para reintentar
				top.setVariable("{emp_id_tmp}",arg0.getParameter("select_emp"));
				top.setVariable("{suc_id_tmp}",arg0.getParameter("select_suc"));
				if (arg0.getParameter("tipo") != null )
					top.setVariable("{checkedtipo}","checked");
				else
					top.setVariable("{checkdtipo}","");
			
			}else{
				top.setVariable("{emp_id_tmp}", "");
				top.setVariable("{suc_id_tmp}", "");
			}
			
			// Nombre del comprador para header
			if( session.getAttribute("ses_com_nombre") != null )
				top.setVariable( "{nombre_comprador}", session.getAttribute("ses_com_nombre") );
			else
				top.setVariable( "{nombre_comprador}", "" );			
			
			
			//PARA ACCEDER A ESTA PAGINA SE DEBE EXISTIR LA SESSION DEL COMPRADOR
			if(session.getAttribute("sess_compradoresDTO") == null ){
				logger.warn("No exuste la session del comprador");
				getServletConfig().getServletContext().getRequestDispatcher(UrlError_com).forward(arg0, arg1);
				return;
			}
			
			//PARA ACCEDER A ESTA PAGINA SE DEBE EXISTIR LA SESSION DE LA SUCURSAL
			if(session.getAttribute("sess_sucursalesDTO") == null){
				logger.warn("No existe la session de la sucursal");
				getServletConfig().getServletContext().getRequestDispatcher(UrlError_suc).forward(arg0, arg1);
				return;
			}
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			// Recupera lista de empresas que tiene acceso el comprador administrador	
			List l_comp = biz.getListAdmEmpresasByCompradorId( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			EmpresasDTO dto = null;
			List datos_empresas = new ArrayList();
			long cantidad = 0;
			for( int i = 0; i < l_comp.size(); i++ ) {
				cantidad++;
				dto = (EmpresasDTO)l_comp.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{emp_nombre}", dto.getEmp_nom() );			
				fila.setVariable("{emp_id}", dto.getEmp_id() + "" );
				if( session.getAttribute("ses_emp_id") != null && dto.getEmp_id() == Long.parseLong(session.getAttribute("ses_emp_id").toString()) ) {
					fila.setVariable("{emp_selected}","selected");
				}
				else {
					fila.setVariable("{emp_selected}","");
				}				
				datos_empresas.add(fila);
			}
			top.setDynamicValueSets("LIST_EMPRESAS", datos_empresas );
			
			top.setVariable("{cantidad}", cantidad+"");	
			
			
			CompradoresDTO compradoraux = (CompradoresDTO) session.getAttribute("sess_compradoresDTO");
			
			top.setVariable("{nombre_comprador_wizard}", compradoraux.getCpr_nombres()+" "+compradoraux.getCpr_ape_pat()+" "+compradoraux.getCpr_ape_mat());
			
			if(session.getAttribute("sess_empresasDTO") != null){
				
				ArrayList arr_emp = new ArrayList();
				List registros = (List) session.getAttribute("sess_empresasDTO");

				long cant_reg = 0;	
				for (int i = 0; i < registros.size(); i++) {
					ComprXEmpDTO suc = (ComprXEmpDTO) registros.get(i);
					IValueSet fila = new ValueSet();
					
					fila.setVariable("{empresa}", suc.getNom_empresa()+"");
					fila.setVariable("{emp_id}", suc.getId_empresa()+"");
					
				
					//if (registros.size() > 1){
						List list_boton = new ArrayList();
						list_boton.add(fila);
						fila.setDynamicValueSets("NOBOTONBORRAR", list_boton );
					//}
					
					cant_reg++;
					arr_emp.add(fila);
					
				}
				top.setDynamicValueSets("list_emp", arr_emp);
				top.setVariable("{cant_reg}", cant_reg+"");
			}			
			
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}
	}
}