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
import cl.bbr.vte.cotizaciones.dto.EstadoDTO;
import cl.bbr.vte.empresas.dto.ComprXSucDTO;
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que muestra el formulario que permite buscar cotizaciones de acuerdo a ciertos criterios escogidos.
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewSearchCotizaciones extends Command {

	/**
	 * Despliega el el formulario que permite buscar cotizaciones
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
			//throws CommandException {

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
		

			// Nombre del comprador para header
			if( session.getAttribute("ses_com_nombre") != null )
				top.setVariable( "{nombre_comprador}", session.getAttribute("ses_com_nombre") );
			else
				top.setVariable( "{nombre_comprador}", "" );		
			
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			// Recuperar empresa y sucursal si se ejecuta el filtro
			logger.debug( (String)arg0.getParameter("accion") + "--" );
			if( arg0.getParameter("accion") != null 
					&& arg0.getParameter("accion").toString().equals("filtro") == true ) {
				session.setAttribute("ses_suc_id", arg0.getParameter("sucursales").toString());
				session.setAttribute("ses_emp_id", arg0.getParameter("empresas").toString());
			}

			// Datos empresa y sucursales
			//List lista_empresas = biz.getListEmpresasByCompradorId( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			List lista_empresas = biz.getListEmpresasByUser( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			EmpresasDTO dto = null;
			List datos_empresas = new ArrayList();
			for( int i = 0; i < lista_empresas.size(); i++ ) {
				dto = (EmpresasDTO)lista_empresas.get(i);
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

			// Recupera lista de sucursales para el comprador		
			//List lista_suc = biz.getListSucursalesByCompradorId( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			List lista_suc = biz.getListSucursalesByUser( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			List lista_sucursales = new ArrayList();
			ComprXSucDTO dtoComprXSucDTO = null;
			List suc_aux = new ArrayList();
			for( int i = 0; i < lista_suc.size(); i++ ) {
				dtoComprXSucDTO = (ComprXSucDTO)lista_suc.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{emp_id}", dtoComprXSucDTO.getId_empresa()+"" );
				fila.setVariable("{suc_nombre}", dtoComprXSucDTO.getNom_sucursal() );
				fila.setVariable("{suc_id}", dtoComprXSucDTO.getId_sucursal() + "" );
				lista_sucursales.add(fila);
			}
			top.setDynamicValueSets("LIST_SUC", lista_sucursales );
			
			if (session.getAttribute("ses_tipo_usuario").equals("1")){//ADMINISTRADOR
				
				// El bloque de compradores es mostrado solo si es admnistrador
				List lista_compradores = new ArrayList();
				List datos_comp = new ArrayList();
				for( int i = 0; i < lista_suc.size(); i++ ) {
					dtoComprXSucDTO = (ComprXSucDTO)lista_suc.get(i);
					datos_comp = biz.getListCompradoresBySucursalId( dtoComprXSucDTO.getId_sucursal(), dtoComprXSucDTO.getTipo_acceso(), Long.parseLong(session.getAttribute("ses_com_id").toString()));		
					for (int j = 0; j < datos_comp.size(); j++) {
						CompradoresDTO comprador = (CompradoresDTO) datos_comp.get(j);
						IValueSet fila2 = new ValueSet();
						fila2.setVariable("{id}", comprador.getCpr_id()+"");
						fila2.setVariable("{idsuc}", dtoComprXSucDTO.getId_sucursal()+"");
						fila2.setVariable("{comprador_nombre}", comprador.getCpr_nombres() + " " + comprador.getCpr_ape_pat());
						lista_compradores.add(fila2);
					}
				}
				top.setDynamicValueSets("list_comp_asig", lista_compradores );
				
				List vacio = new ArrayList();
				IValueSet fila = new ValueSet();
				vacio.add(fila);
				top.setDynamicValueSets("BLOQUE_COMP", vacio );
				
			}
			
			// Setear sucursal seleccionada
			if( session.getAttribute("ses_suc_id") != null ) {
				top.setVariable("{suc_selected}",session.getAttribute("ses_suc_id").toString());
			}
			
			//Recupera la lista de los estado de la cotizacion
			List l_esta = biz.getEstadosCotizacion();
			List datos = new ArrayList();
			for (int i = 0; i < l_esta.size(); i++) {
				EstadoDTO dir = (EstadoDTO) l_esta.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{id_estado}", dir.getId_estado() + "");
				fila.setVariable("{nom_estado}", dir.getNombre() + "");
				datos.add(fila);
			}
			top.setDynamicValueSets("list_estados_coti", datos);
			
			String result = tem.toString(top);
			out.print(result);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			//throw new CommandException(e);
		}

	}

}