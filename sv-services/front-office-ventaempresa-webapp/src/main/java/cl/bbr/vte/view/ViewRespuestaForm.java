package cl.bbr.vte.view;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.utils.Cod_error;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * ViewLogonForm despliega el formulario de conexión a los compradores.
 * 
 * <p>Si hay un mensaje de error lo presenta en pantalla.</p>
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class ViewRespuestaForm extends Command {

	/**
	 * Despliega el formulario de conexión para los compradores.
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		// Se recupera la salida para el servlet
		PrintWriter out = arg1.getWriter();

		// Recupera la sesión del usuario
		HttpSession session = arg0.getSession();
		
		ResourceBundle rb = ResourceBundle.getBundle("vte");
		
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("dis_er");
		logger.debug("UrlError: " + UrlError);
		
		ForwardParameters fp = new ForwardParameters();

		
		try{
			// Recupera pagina desde web.xml
			String pag_form = this.path_html + getServletConfig().getInitParameter("pag_form");
			logger.info( "Template:"+pag_form );
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();
	
			IValueSet top = new ValueSet();
			
	        //Despliega el Banner de inicio dependiendo del lugar desde donde se loguea
			top.setVariable("{bt_inicio}", session.getAttribute("bt_inicio").toString());

			// Recupera código de error
			String cod_error = "";
			if( arg0.getAttribute("cod_error") != null )
				cod_error = (String)arg0.getAttribute("cod_error");
			
	
			String rut2 = arg0.getParameter("rut").toString().replaceAll("\\.","");
			long rut = Long.parseLong(rut2);
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
	
			CompradoresDTO comp = (CompradoresDTO)biz.getCompradoresByRut(rut);
			
			// Se revisa si existe el comprador
			if( comp == null ) {
				this.getLogger().info("comprador no existe");
				cod_error = Cod_error.CLI_NO_EXISTE;
				top.setVariable("{pregunta}", "");
				top.setVariable("{id}", "");
				fp.add("cod_error", "1");
				UrlError = UrlError + fp.forward();
				getServletConfig().getServletContext().getRequestDispatcher(UrlError).forward(arg0, arg1);
				
			}else{
				if (comp.getCpr_pregunta() == null)
					top.setVariable("{pregunta}", "");
				else
					top.setVariable("{pregunta}", comp.getCpr_pregunta()+"");
				top.setVariable("{id}", comp.getCpr_id()+"");
			}
	
			if (cod_error != null ) {
				if( cod_error.compareTo(Cod_error.CLI_NO_EXISTE) == 0) {
					top.setVariable("{err_mensaje}", "Rut ingresado no existe.");
				}
			} else {
				top.setVariable("{err_mensaje}", "");
			}		
			
			String result = tem.toString(top);
	
			out.print(result);
			
			} catch (Exception e) {
				this.getLogger().error( "Problama al solicitar clave", e);
				session.setAttribute("cod_error", Cod_error.CLI_NO_EXISTE );
				getServletConfig().getServletContext().getRequestDispatcher( getServletConfig().getInitParameter("dis_er") ).forward(arg0, arg1);			
			}		
				
	}

}