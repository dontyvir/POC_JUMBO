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
import cl.bbr.vte.empresas.dto.DireccionesDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que muestra el listado de direcciones de despacho por sucursal
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewListDirecDespacho extends Command {

	/**
	 * Despliega el listado de direcciones de despacho
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
		if(arg0.getParameter("idsuc") != null){
			session.setAttribute("ses_adm_suc_id", arg0.getParameter("idsuc"));
		}

		id_sucursal = Long.parseLong((String)session.getAttribute("ses_adm_suc_id"));//Se asigna el valor de la session a la variable id_sucursal
		
		// Recuperar información de la empresa y sucursal
		SucursalesDTO sucursal = biz.getSucursalById( id_sucursal );
		top.setVariable("{sucursaltmp}", sucursal.getSuc_nombre());//Se reemplaza el nombre de la sucursal en el template
		EmpresasDTO empresa = biz.getEmpresaById(sucursal.getSuc_emp_id());
		top.setVariable("{empresatmp}", empresa.getEmp_nom());//Se reemplaza el nombre de la empresa en el template
	
		//Recupera lista de direcciones por sucursal
		if (session.getAttribute("ses_adm_suc_id") != null) {
			
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
				//fila.setVariable("{dir_id_comuna}", dir.getCom_id() + "");
				fila.setVariable("{dir_comuna}", dir.getNom_comuna() + "");
				if (dir.getReg_nombre() != null)
					fila.setVariable("{dir_region}", dir.getReg_nombre() + "");
				else
					fila.setVariable("{dir_region}", "");
				
				fila.setVariable("{dir_id_region}", dir.getReg_id() + "");
				if (lista.size() > 1){
					List list_boton = new ArrayList();
					list_boton.add(fila);
					fila.setDynamicValueSets("NOBOTONBORRAR", list_boton );
				}
				
				datos.add(fila);
			}
			top.setDynamicValueSets("list_direcc", datos);
		}
		String result = tem.toString(top);
		out.print(result);

	}

}