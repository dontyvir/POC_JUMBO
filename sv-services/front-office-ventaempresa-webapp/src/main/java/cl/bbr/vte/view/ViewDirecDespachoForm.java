package cl.bbr.vte.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.vte.empresas.dto.RegionesDTO;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.empresas.dto.ComunaDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;
import cl.bbr.vte.empresas.dto.TiposCallesDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que contiene el formulario para agregar y modificar direcciones de
 * despacho
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class ViewDirecDespachoForm extends Command {

	/**
	 * Despliega el formulario de direccion de despacho
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

		if (arg0.getParameter("iddir") != null && arg0.getParameter("iddir").compareTo("") != 0 ) {
			top.setVariable("{pagina}", "UpdDireccionDesp");
		}
		else {
			top.setVariable("{pagina}", "AddNewDireccionDesp");
		}
		
		
		long id_sucursal = 0; //Se inicializa la varible id_sucursal 
		id_sucursal = Long.parseLong((String)session.getAttribute("ses_adm_suc_id"));//Se recupera el id de la sucursal que esta en la session y se asigna el valor a una variable id_sucursal
		
		// Recupera lista de empresas que tiene acceso el comprador administrador	
		List l_comp = biz.getListAdmEmpresasByCompradorId( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
		EmpresasDTO dto = null;
		String empresatmp = "";
		for( int i = 0; i < l_comp.size(); i++ ) {//Se itera por las empresas 
			dto = (EmpresasDTO)l_comp.get(i);
			if( session.getAttribute("ses_adm_emp_id") != null && dto.getEmp_id() == Long.parseLong(session.getAttribute("ses_adm_emp_id").toString()) ) {
				empresatmp = dto.getEmp_nom();//Se asigna el nombre de la empresa
				break;
			}
		}
		
		top.setVariable("{empresatmp}", empresatmp);//Se asigna el nombre de la empresa al template
		
		// Recupera lista de sucursales para las sucursales que tiene acceso el comprador administrador
		l_comp = biz.getListAdmSucursalesByCompradorId( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
		SucursalesDTO dto_suc = null;
		String sucursaltmp = "";
		for( int i = 0; i < l_comp.size(); i++ ) {//Se itera por las sucursales
			dto_suc = (SucursalesDTO)l_comp.get(i);
			if(dto_suc.getSuc_id() == id_sucursal ){
				sucursaltmp = dto_suc.getSuc_nombre();//Se asigna el nombre de la sucursal
				break;
			}
			
		}
		
		 
		top.setVariable("{sucursaltmp}", sucursaltmp);//Se asigna el nombre de la sucursal al template		
		top.setVariable("{idsucursaltmp}", id_sucursal+"");//Se asigna el id de la sucursal al template
		
		// Se revisa si se ha llegado con error
		if (arg0.getParameter("cod_error") != null) {
			logger.debug("cod_error = " + arg0.getParameter("cod_error"));
			top.setVariable("{msg_error}", "1");
			top.setVariable("{desc_msg_error}", rb.getString("general.mensaje.error"));
			// 2.- Poner los datos en el formulario para reintentar
			top.setVariable("{iddir}", arg0.getParameter("iddir"));
			top.setVariable("{nombre_calle}", arg0.getParameter("calle"));
			top.setVariable("{numero}", arg0.getParameter("numero"));
			top.setVariable("{depto}", arg0.getParameter("depto"));
			top.setVariable("{lugar}", arg0.getParameter("lugar"));
			top.setVariable("{observaciones}", arg0.getParameter("observacion"));
			top.setVariable("{tipo_calle}", arg0.getParameter("tipo_calle"));
			top.setVariable("{region}", arg0.getParameter("region"));
			top.setVariable("{comuna}", arg0.getParameter("comuna"));
			top.setVariable("{nomComuna}", arg0.getParameter("nomComuna"));
		} else if (arg0.getParameter("iddir") != null) { // Cuando es una
														 // modificación
			// Recupera la informacion de la direccion de despacho para
			// desplegarla en el formulario
			long iddir = Long.parseLong(arg0.getParameter("iddir"));
			// Recuperar datos desde el repositorio
			DireccionEntity dir = biz.getDireccionesDespByIdDesp(iddir);

			// Los datos de la dirección se presentan en el formulario
			top.setVariable("{iddir}", iddir + "");
			top.setVariable("{nombre_calle}", dir.getCalle() + "");
			top.setVariable("{numero}", dir.getNumero() + "");
			top.setVariable("{depto}", dir.getDepto() + "");
			top.setVariable("{lugar}", dir.getAlias() + "");
			top.setVariable("{observaciones}", dir.getComentarios() + "");
			top.setVariable("{tipo_calle}", dir.getTip_id() + "");
			top.setVariable("{region}", dir.getReg_id() + "");
			top.setVariable("{comuna}", dir.getCom_id() + "");
			top.setVariable("{nomComuna}", dir.getOtra_comuna() + "");

			if (dir.getOtra_comuna() != null)
				top.setVariable("{otra}", dir.getOtra_comuna() + "");
			else
				top.setVariable("{otra}", "");

		} else { // Es una inserción
			top.setVariable("{iddir}", "");
			top.setVariable("{lugar}", "");
			top.setVariable("{nombre_calle}", "");
			top.setVariable("{numero}", "");
			top.setVariable("{depto}", "");
			top.setVariable("{observaciones}", "");
			top.setVariable("{nomComuna}", "");
		}

		//listado de comunas
		ArrayList comunas = new ArrayList();
		List listComunas = biz.getComunas();//Se recupera el listado de comunas

		for (int i = 0; i < listComunas.size(); i++) {//Se itera por la comunas
			IValueSet fila_com = new ValueSet();
			ComunaDTO com = (ComunaDTO) listComunas.get(i);
			fila_com.setVariable("{id_comuna}", String.valueOf(com.getId_comuna()));
			fila_com.setVariable("{nom_comuna}", String.valueOf(com.getNombre()) + " - " + String.valueOf(com.getReg_nombre()));
			comunas.add(fila_com);
		}
		top.setDynamicValueSets("COMUNAS", comunas);

		// listado de regiones
		ArrayList arr_regiones = new ArrayList();
		List regiones = biz.regionesGetAll();//Se recuperan las regiones
		for (int i = 0; i < regiones.size(); i++) {//Se itera por la regiones
			RegionesDTO dbregion = (RegionesDTO) regiones.get(i);
			IValueSet fila = new ValueSet();
			fila.setVariable("{option_reg_id}", dbregion.getId() + "");
			fila.setVariable("{option_reg_nombre}", dbregion.getNombre());
			arr_regiones.add(fila);
		}
		top.setDynamicValueSets("select_regiones", arr_regiones);

		//Listado de tipo de calles
		ArrayList tip_calles = new ArrayList();
		List listTipCalles = biz.getTiposCalleAll();//Se recuperan los tipos de calles 

		if (listTipCalles.size() > 0) {
			for (int i = 0; i < listTipCalles.size(); i++) {//Se itera por los tipos de calles
				IValueSet fila_zon = new ValueSet();
				TiposCallesDTO tip = (TiposCallesDTO) listTipCalles.get(i);

				fila_zon
						.setVariable("{id_tcalle}", String.valueOf(tip.getId()));
				fila_zon.setVariable("{nom_tcalle}", String.valueOf(tip
						.getNombre()));
				tip_calles.add(fila_zon);
			}
		}
		top.setDynamicValueSets("T_CALLE", tip_calles);

		String result = tem.toString(top);

		out.print(result);

	}

}