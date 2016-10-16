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
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que muestra el listado de direcciones de facturacion por sucursal
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewListDirecFact extends Command {

	/**
	 * Despliega el listado de direcciones de facturacion
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
	
		long id_sucursal = 0;//Se inicializar la variable en cero 
		if(arg0.getParameter("idsuc") != null){//Si el valor del parametro idsuc != vacio, se almacena en una variable de sesion
			session.setAttribute("ses_adm_suc_id", arg0.getParameter("idsuc"));
		}

		id_sucursal = Long.parseLong((String)session.getAttribute("ses_adm_suc_id"));//El valor de la session se asigna a la variable id_sucursal

		// Recuperar información de la empresa y sucursal
		SucursalesDTO sucursal = biz.getSucursalById( id_sucursal );
		top.setVariable("{sucursaltmp}", sucursal.getSuc_nombre());//Se reemplaza el nombre de la sucursal en el template
		EmpresasDTO empresa = biz.getEmpresaById(sucursal.getSuc_emp_id());
		top.setVariable("{empresatmp}", empresa.getEmp_nom());//Se reemplaza el nombre de la empresa en el template
		
		//Recupera lista de direcciones por sucursal
		if (session.getAttribute("ses_adm_suc_id") != null) {
			List lista = biz.getListDireccionFactBySucursal(id_sucursal);
			List datos = new ArrayList();
			for (int i = 0; i < lista.size(); i++) {
				DirFacturacionDTO dir = (DirFacturacionDTO) lista.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{dir_lugar}", dir.getDfac_alias() + "");
				fila.setVariable("{dir_valor}", dir.getDfac_id() + "");
				fila.setVariable("{dir_calle}", dir.getDfac_calle() + "");
				fila.setVariable("{dir_comuna}", dir.getNom_comuna() + "");
				fila.setVariable("{dir_region}", dir.getNom_region() + "");
				fila.setVariable("{dir_numero}", dir.getDfac_numero() + "");
				fila.setVariable("{dir_dpto}", dir.getDfac_depto() + "");
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