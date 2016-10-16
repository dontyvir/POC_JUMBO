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
import cl.bbr.vte.empresas.dto.ComprXSucDTO;
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;
import cl.bbr.vte.empresas.dto.DireccionesDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que muestra Formulario de cotizacion PASO1
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewNewCotizacionP1 extends Command {

	/**
	 * Despliega formulario de cotizacion PASO1 
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
		
			// Nombre del comprador para header
			if( session.getAttribute("ses_com_nombre") != null )
				top.setVariable( "{nombre_comprador}", session.getAttribute("ses_com_nombre") );
			else
				top.setVariable( "{nombre_comprador}", "" );		
			
			
			// Se revisa si se ha llegado con error
			if (arg0.getParameter("cod_error") != null) {
				logger.debug("cod_error = " + arg0.getParameter("cod_error"));
				top.setVariable("{msg_error}", "1");
				top.setVariable("{desc_msg_error}", rb.getString("general.mensaje.error"));
				// 2.- Poner los datos en el formulario para reintentar
				top.setVariable("{id_empresa}", arg0.getParameter("empresas"));
				top.setVariable("{id_sucursal}", arg0.getParameter("sucursales"));
				top.setVariable("{id_dir_desp}", arg0.getParameter("dir_desp"));
				top.setVariable("{id_dir_fact}", arg0.getParameter("dir_fact"));
				top.setVariable("{fecha_desp}", arg0.getParameter("fecha_desp"));
				top.setVariable("{f_pago}", arg0.getParameter("f_pago"));
				
			}else{
				top.setVariable("{fecha_desp}", "");
				top.setVariable("{empresas_error}", "");
			}
			
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			// Recupera lista de empresas que tiene acceso el comprador administrador	
			List l_comp = biz.getListEmpresasByCompradorId( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			EmpresasDTO dto = null;
			List datos_empresas = new ArrayList();
			for( int i = 0; i < l_comp.size(); i++ ) {
				dto = (EmpresasDTO)l_comp.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{emp_nombre}", dto.getEmp_nom() );			
				fila.setVariable("{emp_id}", dto.getEmp_id() + "" );
				if( session.getAttribute("ses_adm_emp_id") != null && dto.getEmp_id() == Long.parseLong(session.getAttribute("ses_adm_emp_id").toString()) ) {
					fila.setVariable("{emp_selected}","selected");
				}
				else {
					fila.setVariable("{emp_selected}","");
				}
				
				// Rectificar saldo de línea de crédito
                //TODO es necesario chequear que el proceso que actualiza los montos de las empresas
                //ademas se dejo comentado lo del saldo pendiente (hay que chequear la query que calcula el pendiente)
                //ya que no esta claro que este correctamente funcionando el proceso que actualiza los monto de las empresas
				double saldo = dto.getEmp_saldo();// - biz.getSaldoActualPendiente( dto.getEmp_id() );
				fila.setVariable("{emp_saldo}", saldo + "" );
				
				datos_empresas.add(fila);
			}
			top.setDynamicValueSets("LIST_EMPRESAS", datos_empresas );		
			
			// Recupera lista de sucursales para las sucursales que tiene acceso el comprador administrador
			l_comp = biz.getListSucursalesByCompradorId( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			ComprXSucDTO dto_suc = null;
			List datos_sucursales = new ArrayList();
			for( int i = 0; i < l_comp.size(); i++ ) {
				dto_suc = (ComprXSucDTO)l_comp.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{suc_nombre}", dto_suc.getNom_sucursal() );			
				fila.setVariable("{emp_id}", dto_suc.getId_empresa() + "" );				
				fila.setVariable("{suc_id}", dto_suc.getId_sucursal() + "" );				
				datos_sucursales.add(fila);
			}
			top.setDynamicValueSets("LIST_SUC", datos_sucursales );	
			// Setear sucursal seleccionada
			if( session.getAttribute("ses_adm_suc_id") != null ) {
				top.setVariable("{suc_selected}",session.getAttribute("ses_adm_suc_id").toString());
			}			


			//Recupera todas las direcciones de despacho por id_comprador  
			List lista = biz.getListDireccionDespByComprador(Long.parseLong(session.getAttribute("ses_com_id")+""));
			List datos = new ArrayList();
			for (int i = 0; i < lista.size(); i++) {
				DireccionesDTO dire = (DireccionesDTO) lista.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{dire_id}", dire.getId()+"");
				fila.setVariable("{direccion_nombre}", dire.getCalle()+" "+dire.getNumero()+", "+dire.getNom_comuna());
				fila.setVariable("{sucu_id}", dire.getCli_id()+"");
				datos.add(fila);
			}
			top.setDynamicValueSets("ListaDirecciones",datos);
			
			//Recupera todas las direcciones de facturcion por id_comprador
			List listafac = biz.getListDireccionFactByComprador(Long.parseLong(session.getAttribute("ses_com_id")+""));
			List datosfac = new ArrayList();
			for (int i = 0; i < listafac.size(); i++) {
				DirFacturacionDTO direfac = (DirFacturacionDTO) listafac.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{dirfac_id}", direfac.getDfac_id()+"");
				fila.setVariable("{direccionfac_nombre}", direfac.getDfac_calle()+" "+direfac.getDfac_numero()+", "+direfac.getNom_comuna());
				fila.setVariable("{sucufac_id}", direfac.getDfac_cli_id()+"");
				datosfac.add(fila);
			}
			top.setDynamicValueSets("ListaDireccionesFact",datosfac);
			
			
			
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}

	}

}