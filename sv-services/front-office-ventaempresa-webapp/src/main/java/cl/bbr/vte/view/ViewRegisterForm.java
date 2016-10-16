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
import cl.bbr.vte.empresas.dto.ComunaDTO;
import cl.bbr.vte.empresas.dto.RegionesDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Despliega el formulario de solicitud de registro de empresas.
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewRegisterForm extends Command {

	/**
	 * Formulario de registro
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
		
		// Recupera la sesión
		HttpSession session = arg0.getSession();
		
		// recupera la salida para el servlet
		PrintWriter out = arg1.getWriter();

		// Recupera parámetro desde web.xml
		String pag_form = this.path_html + getServletConfig().getInitParameter("pag_form");
		logger.debug( "Template:"+pag_form );

		// Template de despliegue
		TemplateLoader load = new TemplateLoader(pag_form);
		ITemplate tem = load.getTemplate();

		IValueSet top = new ValueSet();
		
        //Despliega el Banner de inicio dependiendo del lugar desde donde se loguea
		if (session.getAttribute("bt_inicio") == null){
		    top.setVariable("{bt_inicio}", "bt-iniciocontactotelefono.swf");
		}else{
		    top.setVariable("{bt_inicio}", session.getAttribute("bt_inicio").toString());
		}
				

		// Se revisa si se ha llegado con error
		if (arg0.getParameter("cod_error") != null) {
			logger.debug("cod_error = " + arg0.getParameter("cod_error"));
			top.setVariable("{msg_error}", "1");
			top.setVariable("{desc_msg_error}", rb.getString("general.mensaje.error") + " " + arg0.getParameter("cod_error"));
			
			//Poner los datos en el formulario para reintentar
			top.setVariable("{nombre_contacto}", arg0.getParameter("nombre_contacto"));
			top.setVariable("{cargo}", arg0.getParameter("cargo"));
			top.setVariable("{fono_contacto1}", arg0.getParameter("fono_contacto1"));
			top.setVariable("{fono_contacto2}", arg0.getParameter("fono_contacto2"));
			top.setVariable("{fono_contacto3}", arg0.getParameter("fono_contacto3"));
			top.setVariable("{fon_cod_1}", arg0.getParameter("fon_cod_1"));
			top.setVariable("{fon_cod_2}", arg0.getParameter("fon_cod_2"));
			top.setVariable("{fon_cod_3}", arg0.getParameter("fon_cod_3"));
			top.setVariable("{email}", arg0.getParameter("email"));
			top.setVariable("{email_c}", arg0.getParameter("email_c"));
			top.setVariable("{rutemp}", arg0.getParameter("rutemp"));
			top.setVariable("{dvemp}", arg0.getParameter("dvemp"));
			top.setVariable("{nombre_empresa}", arg0.getParameter("nombre_empresa"));
			top.setVariable("{tamano_empresa}", arg0.getParameter("tamano_empresa"));
			top.setVariable("{razon}", arg0.getParameter("razon"));
			top.setVariable("{rubro}", arg0.getParameter("rubro"));
			top.setVariable("{dir_com}", arg0.getParameter("dir_com"));
			top.setVariable("{ciudad}", arg0.getParameter("ciudad"));
			top.setVariable("{comuna}", arg0.getParameter("comuna"));
		}else{
			//Se limpian los valores del formulario
			top.setVariable("{nombre_contacto}", "");
			top.setVariable("{cargo}", "");
			top.setVariable("{fono_contacto1}", "");
			top.setVariable("{fono_contacto2}", "");
			top.setVariable("{fono_contacto3}", "");
			top.setVariable("{fon_cod_1}", "");
			top.setVariable("{fon_cod_2}", "");
			top.setVariable("{fon_cod_3}", "");
			top.setVariable("{email}", "");
			top.setVariable("{email_c}", "");
			top.setVariable("{rutemp}", "");
			top.setVariable("{dvemp}", "");
			top.setVariable("{nombre_empresa}", "");
			top.setVariable("{tamano_empresa}", "");
			top.setVariable("{razon}", "");
			top.setVariable("{rubro}", "");
			top.setVariable("{dir_com}", "");
			top.setVariable("{ciudad}", "");
			top.setVariable("{comuna}", "");
		}		
		
		// Instacia del bizdelegate
		BizDelegate biz = new BizDelegate();		
		
		//SE RECUPERA EL LISTADO DE REGIONES
		ArrayList arr_regiones = new ArrayList();
		List regiones = biz.regionesGetAll();
		for (int i = 0; i < regiones.size(); i++) {
			RegionesDTO dbregion = (RegionesDTO) regiones.get(i);
			IValueSet fila = new ValueSet();
			fila.setVariable("{option_reg_id}", dbregion.getId()+"");
			fila.setVariable("{option_reg_nombre}", dbregion.getNombre());
			arr_regiones.add(fila);
		}
		top.setDynamicValueSets("select_regiones", arr_regiones);

		
		//SE RECUPERA EL LISTADO DE COMUNAS
		ArrayList arr_comunas = new ArrayList();
		List comunas = biz.getComunasGeneral();
		for (int i = 0; i < comunas.size(); i++) {
			ComunaDTO dbcomunas = (ComunaDTO) comunas.get(i);
			IValueSet fila = new ValueSet();
			fila.setVariable("{reg_id}", dbcomunas.getId_region()+"");
			fila.setVariable("{nombre_com}", dbcomunas.getNombre());
			arr_comunas.add(fila);
		}
		top.setDynamicValueSets("select_comunas", arr_comunas);		
		
		
		
		String result = tem.toString(top);

		out.print(result);		

	}

}