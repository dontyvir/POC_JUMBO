package cl.bbr.vte.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.cotizaciones.dto.CotizacionesCriteriaDTO;
import cl.bbr.vte.cotizaciones.dto.MonitorCotizacionesDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.utils.FiltroEmpresas;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que muestra el formulario que permite buscar cotizaciones anteriores solo en estado de FINALIZADA
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewSearchCotiAnt extends Command {

	/**
	 * Despliega el el formulario que permite buscar cotizaciones ANTERIORES
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
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
	        //Despliega el Banner de inicio dependiendo del lugar desde donde se loguea
			top.setVariable("{bt_inicio}", session.getAttribute("bt_inicio").toString());

			//SE LLENAN LOS COMBO BOX DE EMPRESAS Y SUCURSALES
			List l_comp = biz.getListEmpresasByCompradorId( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			EmpresasDTO dto = null;
			List datos_empresas = new ArrayList();
			for( int i = 0; i < l_comp.size(); i++ ) {
				dto = (EmpresasDTO)l_comp.get(i);
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
			List l_comp2 = biz.getListSucursalesByCompradorId( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			FiltroEmpresas rec = new FiltroEmpresas();
			rec.setFiltroEmpresas( l_comp2, session.getAttribute("ses_emp_id") );
			top.setDynamicValueSets("LIST_SUC", rec.getDatos_sucursales());
				
			// Setear sucursal seleccionada
			if( session.getAttribute("ses_suc_id") != null ) {
				top.setVariable("{suc_selected}",session.getAttribute("ses_suc_id").toString());
			}
			
			
			//BUSCA LAS COTIZACIONES ANTERIORES
			if(arg0.getParameter("action") != null && arg0.getParameter("action").equals("busca")){
				CotizacionesCriteriaDTO coti = new CotizacionesCriteriaDTO();
				coti.setRegsperpag(5000);

				if( arg0.getParameter("empresas") != null )
					coti.setId_empresa(Long.parseLong(arg0.getParameter("empresas"))); //Id empresa
				if( arg0.getParameter("sucursales") != null )
					coti.setId_sucursal(Long.parseLong(arg0.getParameter("sucursales"))); //Id sucursal

				// Indicamos la procedencia
				coti.setProcedencia("W");
				
				coti.setId_comprador(Long.parseLong(session.getAttribute("ses_com_id").toString()));
				
				//coti.setId_estado(Constantes.ID_EST_COTIZACION_TERMINADA); //Estaso de la cotizacion FINALIZADA
				
				//Recupera el listado de cotizaciones
				List lista =  biz.getCotizacionesByCriteria(coti);		
				List datos = new ArrayList();
				for (int i = 0; i < lista.size(); i++) {
					MonitorCotizacionesDTO dir = (MonitorCotizacionesDTO) lista.get(i);
					IValueSet fila = new ValueSet();
					fila.setVariable("{id_coti}", dir.getId_cot() + "");
					fila.setVariable("{nom_emp}", dir.getNom_empresa());
					fila.setVariable("{nom_ape}", dir.getNombre_comprador());
					fila.setVariable("{nom_suc}", dir.getNom_sucursal());
					fila.setVariable("{fecha_ing}", Formatos.frmFechaHora(dir.getFec_ing()));
					
					datos.add(fila);
				}
				top.setDynamicValueSets("LIST_COTIZACIONES", datos);
				
			}
			
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			//throw new CommandException(e);
		}

	}

}