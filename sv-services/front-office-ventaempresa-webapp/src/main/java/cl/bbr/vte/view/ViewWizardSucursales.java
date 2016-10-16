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
import cl.bbr.vte.empresas.dto.ComprXSucDTO;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que contiene el formulario para agregar sucursales WIZARD
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class ViewWizardSucursales extends Command {

	/**
	 * Despliega el formulario de sucursal para WIZARD
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
			String UrlError = getServletConfig().getInitParameter("dis_er");
			String dis_ok = getServletConfig().getInitParameter("dis_ok");
			logger.debug("UrlOK: " + dis_ok);
			logger.debug("UrlError: " + UrlError);
			

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
			
			// SE REVISA SI SE HA LLEGADO CON ERROR
			if (arg0.getParameter("cod_error") != null) {
				logger.debug("cod_error = " + arg0.getParameter("cod_error"));
				top.setVariable("{msg_error}", "1");
				top.setVariable("{desc_msg_error}", rb.getString("general.mensaje.error"));
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
			
			//FIN REVISA ERROR
			
			// NOMBRE DEL COMPRADOR PARA HEADER
			if( session.getAttribute("ses_com_nombre") != null )
				top.setVariable( "{nombre_comprador}", session.getAttribute("ses_com_nombre") );
			else
				top.setVariable( "{nombre_comprador}", "" );
			
			//FIN COMPRADOR HEADER			
			
			// INSTACIA DEL BIZDELEGATE
			BizDelegate biz = new BizDelegate();
			
			// RECUPERA LISTA DE EMPRESAS QUE TIENE ACCESO EL COMPRADOR ADMINISTRADOR	
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
				
				// Rectificar saldo de línea de crédito
				double saldo = dto.getEmp_saldo() - biz.getSaldoActualPendiente( dto.getEmp_id() );
				fila.setVariable("{emp_saldo}", saldo + "" );
				
				datos_empresas.add(fila);
			}
			top.setDynamicValueSets("LIST_EMPRESAS", datos_empresas );				
			
			//FIN RECUPERA EMPRESAS
			
			
			// RECUPERA LISTA DE SUCURSALES PARA LAS SUCURSALES QUE TIENE ACCESO EL COMPRADOR ADMINISTRADOR
			l_comp = biz.getListAdmSucursalesByCompradorId( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			SucursalesDTO dto_suc = null;
			List datos_sucursales = new ArrayList();
			for( int i = 0; i < l_comp.size(); i++ ) {
				dto_suc = (SucursalesDTO)l_comp.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{suc_nombre}", dto_suc.getSuc_nombre() );			
				fila.setVariable("{emp_id}", dto_suc.getSuc_emp_id() + "" );				
				fila.setVariable("{suc_id}", dto_suc.getSuc_id() + "" );
				
				datos_sucursales.add(fila);
			}
			top.setDynamicValueSets("LIST_SUC_FORM", datos_sucursales );
			
			/*l_comp = biz.getListSucursalesByCompradorId( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			ComprXSucDTO dto_suc = null;
			List datos_sucursales = new ArrayList();
			for( int i = 0; i < l_comp.size(); i++ ) {
				dto_suc = (ComprXSucDTO)l_comp.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{suc_nombre}", dto_suc.getNom_sucursal() );			
				fila.setVariable("{emp_id}", dto_suc.getId_empresa() + "" );				
				fila.setVariable("{suc_id}", dto_suc.getId_sucursal() + "" );				
				datos_sucursales.add(fila);
			}
			top.setDynamicValueSets("LIST_SUC_FORM", datos_sucursales );	
			*/
			//FIN RECUPERA SUCURSALES			

			if(session.getAttribute("sess_compradoresDTO") != null){
				CompradoresDTO compradoraux2 = (CompradoresDTO) session.getAttribute("sess_compradoresDTO");
				top.setVariable("{nombre_comprador_wizard}", compradoraux2.getCpr_nombres()+" "+compradoraux2.getCpr_ape_pat()+" "+compradoraux2.getCpr_ape_mat());
			}			
			
			//************FIN PARAMETROS REQUEST COMPRADOR Y SESSION********************************
			
			
			//PARA ACCEDER A ESTA PAGINA SE DEBE EXISTIR LA SESSION DEL COMPRADOR
			if(session.getAttribute("sess_compradoresDTO") == null){
				logger.warn("No existe la session del comprador");
				getServletConfig().getServletContext().getRequestDispatcher(UrlError).forward(arg0, arg1);
				return;
				
			}			
			
			top.setVariable("{cant_reg}", "0");	

			
			//SE RECORRE LA LISTA DE DTO DE SUCURSALES Y SE DESPLIEGAN POR PANTALLA 
			if(session.getAttribute("sess_sucursalesDTO") != null){
				
				ArrayList arr_suc = new ArrayList();
				List registros = (List) session.getAttribute("sess_sucursalesDTO");

				long cant_reg = 0;	
				for (int i = 0; i < registros.size(); i++) {
					ComprXSucDTO suc = (ComprXSucDTO) registros.get(i);
					IValueSet fila = new ValueSet();
					
					fila.setVariable("{cli_id}", suc.getId_sucursal()+"");
					fila.setVariable("{sucursal}", suc.getNom_sucursal()+"");
					fila.setVariable("{empresa}", suc.getNom_empresa()+"");
					fila.setVariable("{emp_id}", suc.getId_empresa()+"");
					
					if (registros.size() > 1){
						List list_boton = new ArrayList();
						list_boton.add(fila);
						fila.setDynamicValueSets("NOBOTONBORRAR", list_boton );
					}
					
					cant_reg++;
					arr_suc.add(fila);
				}
				
				top.setDynamicValueSets("list_suc", arr_suc);
				top.setVariable("{cant_reg}", cant_reg+"");
			}
			
			//FIN SE RECORRE LA LISTA DE DTO DE SUCURSALES Y SE DESPLIEGAN POR PANTALLA 
			
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}
	}
}