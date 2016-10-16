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
import cl.bbr.vte.empresas.dto.DireccionesDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.RegionesDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;
import cl.bbr.vte.empresas.dto.TiposCallesDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Presenta formulario para datos de la dirección de despacho en wizards de sucursales.
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewFormDirDespacho extends Command {

	/**
	 * Formulario.
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
		logger.info( "Template:"+pag_form );
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
		
		long id_sucursal = 0;
		long id_empresa = 0;
		id_sucursal = Long.parseLong((String)session.getAttribute("ses_wiz_suc_id"));
		id_empresa  = Long.parseLong((String)session.getAttribute("ses_wiz_emp_id"));
		
		// Recuperar nombre de la empresa
		EmpresasDTO dto = biz.getEmpresaById(id_empresa);
		String empresatmp = dto.getEmp_nom();//Se asigna el nombre de la empresa
		top.setVariable("{empresatmp}", empresatmp);//Se asigna el nombre de la empresa al template
		
		// Recuperar nombre de la sucursal
		SucursalesDTO dto_suc = biz.getSucursalById(id_sucursal);
		top.setVariable("{sucursaltmp}",  dto_suc.getSuc_nombre());//Se asigna el nombre de la sucursal al template
		top.setVariable("{idsucursaltmp}", id_sucursal+"");//Se asigna el id de la sucursal al template
		
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
		
		// Recuperar datos de las direcciones ingresadas
		List lista = biz.getListDireccionDespBySucursal(id_sucursal);
		List datos = new ArrayList();
		for (int i = 0; i < lista.size(); i++) {
			DireccionesDTO dir = (DireccionesDTO) lista.get(i);
			IValueSet fila = new ValueSet();
			fila.setVariable("{dir_lugar}", dir.getAlias() + "");
			fila.setVariable("{dir_calle}", dir.getCalle() + "");
			fila.setVariable("{dir_numero}", dir.getNumero() + "");
			fila.setVariable("{dir_dpto}", dir.getDepto() + "");
			fila.setVariable("{dir_valor}", dir.getId() + "");
			
			fila.setVariable("{dir_tipid}", dir.getTip_id() + "");
			fila.setVariable("{dir_comuna}", dir.getNom_comuna() + "");
			if (dir.getReg_nombre() != null)
				fila.setVariable("{dir_region}", dir.getReg_nombre() + "");
			else
				fila.setVariable("{dir_region}", "");
			
			fila.setVariable("{dir_id_region}", dir.getReg_id() + "");
			
			datos.add(fila);
		}
		top.setDynamicValueSets("list_direcc", datos);		
		
		String result = tem.toString(top);

		out.print(result);

	}

}