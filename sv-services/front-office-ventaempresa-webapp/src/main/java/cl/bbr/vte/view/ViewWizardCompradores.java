package cl.bbr.vte.view;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * <p>Comando que contiene el formulario de ingreso del comprador que se utiliza en el WIZARD.</p>
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewWizardCompradores extends Command {

	/**
	 * Despliega el formulario de ingreso del comprador utilizado en el WIZARD
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

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

			
			//Elimina session sess_compradoresDTO - sess_sucursalesDTO - sess_empresasDTO
			if(arg0.getParameter("act") != null && arg0.getParameter("act").equals("new")){
				session.removeAttribute("sess_compradoresDTO");
				session.removeAttribute("sess_sucursalesDTO");
				session.removeAttribute("sess_empresasDTO");
			}
			
			
			// Se revisa si se ha llegado con error
			if (arg0.getParameter("cod_error") != null) {
				logger.debug("cod_error = " + arg0.getParameter("cod_error"));
				top.setVariable("{msg_error}", "1");
				top.setVariable("{desc_msg_error}", rb.getString("general.mensaje.error") + " " + arg0.getParameter("cod_error"));
				
				// 2.- Poner los datos en el formulario para reintentar
				top.setVariable("{cpr_id}", arg0.getParameter("cpr_id"));
				top.setVariable("{rut}", arg0.getParameter("rut"));
				top.setVariable("{dv}", arg0.getParameter("dv"));
				top.setVariable("{nombre}", arg0.getParameter("nombre"));
				top.setVariable("{paterno}", arg0.getParameter("paterno"));
				top.setVariable("{materno}", arg0.getParameter("materno"));
				top.setVariable("{fono1}", arg0.getParameter("fono1"));
				top.setVariable("{fono2}", arg0.getParameter("fono2"));
				top.setVariable("{fono3}", arg0.getParameter("fono3"));
				top.setVariable("{email}", arg0.getParameter("email"));
				top.setVariable("{pregunta}", arg0.getParameter("pregunta"));
				top.setVariable("{respuesta}", arg0.getParameter("respuesta"));
				if (arg0.getParameter("tipo") != null && arg0.getParameter("tipo").compareTo("A") == 0){
					top.setVariable("{checkedadm}", "checked");
				}else{
					top.setVariable("{checkedadm}", "");
				}				
				if (arg0.getParameter("genero") != null && arg0.getParameter("genero").compareTo("M") == 0) {
					top.setVariable("{generoM}", "checked");
					top.setVariable("{generoF}", "");
				}else{
					top.setVariable("{generoM}", "");
					top.setVariable("{generoF}", "checked");
				}				
			}else if(session.getAttribute("sess_compradoresDTO") != null){
				CompradoresDTO com = (CompradoresDTO) session.getAttribute("sess_compradoresDTO");
				top.setVariable("{rut}", String.valueOf(com.getCpr_rut()));
				top.setVariable("{dv}", String.valueOf(com.getCpr_dv()));
				top.setVariable("{nombre}", String.valueOf(com.getCpr_nombres()));				
				top.setVariable("{paterno}", String.valueOf(com.getCpr_ape_pat()));
				top.setVariable("{materno}", String.valueOf(com.getCpr_ape_mat()));
				if (String.valueOf(com.getCpr_genero()).compareTo("M") == 0) {
					top.setVariable("{generoM}", "checked");
					top.setVariable("{generoF}", "");
				}else{
					top.setVariable("{generoM}", "");
					top.setVariable("{generoF}", "checked");
				}
				top.setVariable("{fono1}", String.valueOf(com.getCpr_fono1()));
				top.setVariable("{fono2}", String.valueOf(com.getCpr_fono2()));
				top.setVariable("{fono3}", String.valueOf(com.getCpr_fono3()));
				top.setVariable("{email}", String.valueOf(com.getCpr_email()));
				if( com.getCpr_estado() != null && com.getCpr_estado().equals("A") ) {
					top.setVariable("{a_est_selected}", "selected" );
					top.setVariable("{d_est_selected}", "" );
				} else {
					top.setVariable("{a_est_selected}", "" );
					top.setVariable("{d_est_selected}", "selected" );
				}
				top.setVariable("{pregunta}", String.valueOf(com.getCpr_pregunta()));
				top.setVariable("{respuesta}", String.valueOf(com.getCpr_respuesta()));			
			}else{ // Agregar un nuevo comprador
				top.setVariable("{cpr_id}", "");
				top.setVariable("{rut}", "");
				top.setVariable("{dv}", "");
				top.setVariable("{nombre}", "");
				top.setVariable("{paterno}", "");
				top.setVariable("{materno}", "");
				top.setVariable("{fono1}", "");
				top.setVariable("{fono2}", "");
				top.setVariable("{fono3}", "");
				top.setVariable("{email}", "");
				top.setVariable("{clave}", "");
				top.setVariable("{pregunta}", "");
				top.setVariable("{respuesta}", "");
			}
			
			String result = tem.toString(top);

			out.print(result);

	}

}