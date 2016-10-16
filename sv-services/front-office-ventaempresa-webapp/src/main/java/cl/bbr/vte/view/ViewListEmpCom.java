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
import cl.bbr.vte.exception.VteException;
import cl.bbr.vte.utils.Cod_error;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que muestra el listado de las Empresas que admnistra el comprador
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewListEmpCom extends Command {

	/**
	 * Despliega el listado de empresas que administra el comprador
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
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			//Dejo en u avariable de session temp el cpr_id del comprador escogido
			long id_comprador;
			if (arg0.getParameter("cpr_id") !=  null){
				session.setAttribute("id_comprador_tmp", arg0.getParameter("cpr_id") + "");
				id_comprador = Long.parseLong(arg0.getParameter("cpr_id"));
			}else{
				id_comprador = Long.parseLong(session.getAttribute("id_comprador_tmp")+"");
			}
			
			top.setVariable("{id_comprador}",session.getAttribute("id_comprador_tmp"));
			
			top.setVariable("{pr_id}",id_comprador+"");
			
			
			CompradoresDTO comprador = (CompradoresDTO)biz.getCompradoresById(id_comprador);
			// Se revisa si existe el cliente
			if( comprador == null ) {
				logger.warn("Comprador no existe " + id_comprador);
				throw new VteException( Cod_error.CLI_NO_EXISTE );
			}
			
			logger.info( "Nombre Comprador:" + comprador.getCpr_nombres() + " " + comprador.getCpr_ape_pat());

			// Se recupera nombre y apellido del comprador
			top.setVariable("{nombre_comprador_suc}", comprador.getCpr_nombres() + " " + comprador.getCpr_ape_pat() );
			
			
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
			top.setVariable("{cpr_id}", id_comprador+"");				
			
			
			// Recupera lista de empresas que tiene acceso el comprador consultado	
			List mod_l_comp = biz.getListAdmEmpresasByCompradorId( id_comprador );
			EmpresasDTO mod_dto = null;
			List mod_datos_empresas = new ArrayList();
			for( int i = 0; i < mod_l_comp.size(); i++ ) {
				mod_dto = (EmpresasDTO)mod_l_comp.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{emp_id}", mod_dto.getEmp_id() + "" );
				fila.setVariable("{emp_nom}", mod_dto.getEmp_nom() + "" );
				mod_datos_empresas.add(fila);
			}
			top.setDynamicValueSets("LIST_EMPRESAS_MOD", mod_datos_empresas );	
			
			//Empresas asignadas al comprador
			List empresas_asig = new ArrayList();
			for( int i = 0; i < l_comp.size(); i++ ) {
				dto = (EmpresasDTO)l_comp.get(i);
				IValueSet fila = new ValueSet();
				for( int j = 0; j < mod_l_comp.size(); j++ ) {
					mod_dto = (EmpresasDTO)mod_l_comp.get(j);
					if(dto.getEmp_id() == mod_dto.getEmp_id()){
						fila.setVariable("{nom_emp}", dto.getEmp_nom() );
						fila.setVariable("{id_emp}", dto.getEmp_id() + "" );
						empresas_asig.add(fila);
					}
				}
			}
			top.setDynamicValueSets("LIST_EMPRESAS_ASIG", empresas_asig);	
			
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}

	}

}