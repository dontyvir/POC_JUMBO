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
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.EstadoDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que contiene el formulario para agregar y modificar una sucursal
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class ViewSucursalesForm extends Command {

	/**
	 * Despliega el formulario de sucursal
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
		String pag_form = rb.getString("conf.path.html") + ""
				+ getServletConfig().getInitParameter("pag_form");
		logger.debug("Template:" + pag_form);
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
		if (session.getAttribute("ses_com_nombre") != null)
			top.setVariable("{nombre_comprador}", session
					.getAttribute("ses_com_nombre"));
		else
			top.setVariable("{nombre_comprador}", "");

		// Instacia del bizdelegate
		BizDelegate biz = new BizDelegate();

		if (arg0.getParameter("idsuc") != null && arg0.getParameter("idsuc").compareTo("") != 0 ) {
			top.setVariable("{pagina}", "UpdSucursales");
		}
		else {
			top.setVariable("{pagina}", "AddNewSucursal");
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
			fila.setVariable("{emp_razon}", dto.getEmp_rzsocial()+"");
			fila.setVariable("{emp_rut}", dto.getEmp_rut()+"");
			fila.setVariable("{emp_dv}", dto.getEmp_dv()+"");
			if( session.getAttribute("ses_adm_emp_id") != null && dto.getEmp_id() == Long.parseLong(session.getAttribute("ses_adm_emp_id").toString()) ) {
				fila.setVariable("{emp_selected}","selected");
			}
			else {
				fila.setVariable("{emp_selected}","");
			}				
			datos_empresas.add(fila);
		}
		top.setDynamicValueSets("LIST_EMPRESAS", datos_empresas );		
		top.setDynamicValueSets("LIST_EMPRESAS_DATOS", datos_empresas );
		
		String estado = "";
		
		// Se revisa si se ha llegado con error
		if (arg0.getParameter("cod_error") != null) {
			logger.debug("cod_error = " + arg0.getParameter("cod_error"));
			top.setVariable("{msg_error}", "1");
			top.setVariable("{desc_msg_error}", rb.getString("general.mensaje.error"));
			// 2.- Poner los datos en el formulario para reintentar
			top.setVariable("{idsuc}", arg0.getParameter("idsuc"));
			top.setVariable("{empresa}", arg0.getParameter("empresas"));
			top.setVariable("{rutdsuc}", arg0.getParameter("rut"));
			top.setVariable("{dv}", arg0.getParameter("dv"));
			top.setVariable("{nombre}", arg0.getParameter("nombre"));
			top.setVariable("{razon}", arg0.getParameter("razon"));
		} else if (arg0.getParameter("idsuc") != null) { // Cuando es una modificación
			// Recupera la informacion de la sucursal para desplegarla en el formulario
			long idsuc = Long.parseLong(arg0.getParameter("idsuc"));
			// Recuperar datos desde el repositorio
			SucursalesDTO suc = biz.getSucursalById(idsuc);

			// Los datos de la dirección se presentan en el formulario
			top.setVariable("{accion_btn}", "Modificar");
			top.setVariable("{idsuc}", idsuc + "");
			top.setVariable("{nombre}", suc.getSuc_nombre() + "");
			top.setVariable("{rut}", suc.getSuc_rut() + "");
			top.setVariable("{dv}", suc.getSuc_dv() + "");
			top.setVariable("{razon}", suc.getSuc_razon() + "");
			top.setVariable("{empresa}", suc.getSuc_emp_id() + "");
			top.setVariable("{desc}", suc.getSuc_descr() + "");
			top.setVariable("{fono1}", suc.getSuc_fono_num1() + "");
			top.setVariable("{codfon1}", suc.getSuc_fono_cod1()+"");
			top.setVariable("{fono2}", suc.getSuc_fono_num2() + "");
			top.setVariable("{codfon2}", suc.getSuc_fono_cod2()+"");
			top.setVariable("{mail}", suc.getSuc_email() + "");
			top.setVariable("{encargado}", suc.getSuc_pregunta() + "");
			estado = suc.getSuc_estado();
			
			// Este bloque es mostrado solo si se va a realizar una modificacion
			List fechamod = new ArrayList();
			IValueSet fila = new ValueSet();
			fila.setVariable("{idsucbloque}", idsuc+"");
			if(suc.getSuc_fec_act() != null && suc.getSuc_fec_act().compareTo("---") != 0){
				fila.setVariable("{fmodi}", suc.getSuc_fec_act().substring(0,10));
			}else{
				fila.setVariable("{fmodi}", "");
			}
			fechamod.add(fila);
			top.setDynamicValueSets("BLOQ_MODIFICA", fechamod );
			
		} else { // Es una inserción
			top.setVariable("{idsuc}", "");
			top.setVariable("{nombre}", "");
			top.setVariable("{rut}", "");
			top.setVariable("{dv}", "");
			top.setVariable("{razon}", "");
			top.setVariable("{empresa}", "");
			top.setVariable("{desc}", "");
			top.setVariable("{fono1}", "");
			top.setVariable("{fono2}", "");
			top.setVariable("{mail}", "");
			top.setVariable("{encargado}", "");
			top.setVariable("{accion_btn}", "Crear");
		}

		//Recupera los estados posibles de las sucursales
		List lestados_suc = biz.getEstadosSucursales("SUC");
		EstadoDTO dto_est = null;
		List datos_estados = new ArrayList();
		for( int i = 0; i < lestados_suc.size(); i++ ) {
			dto_est = (EstadoDTO)lestados_suc.get(i);
			IValueSet fila = new ValueSet();
			fila.setVariable("{est_nombre}", dto_est.getEstado() );			
			fila.setVariable("{est_id}", dto_est.getId() + "" );
			
			if(dto_est.getId().equals(estado) ){
				fila.setVariable("{est_selected}","selected");
			}else{
				fila.setVariable("{est_selected}","");
			}
			
			datos_estados.add(fila);
		}
		top.setDynamicValueSets("LIST_ESTADOS", datos_estados );		

		String result = tem.toString(top);

		out.print(result);

	}

}