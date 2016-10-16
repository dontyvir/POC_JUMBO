package cl.bbr.vte.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.cotizaciones.dto.CotizacionesCriteriaDTO;
import cl.bbr.vte.cotizaciones.dto.MonitorCotizacionesDTO;
import cl.bbr.vte.empresas.dto.ComprXSucDTO;
import cl.bbr.vte.empresas.dto.CompradoresDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que muestra el listado de cotizaciones de acuerdo a un criterio de búsqueda seleccionado
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewListCotizaciones extends Command {

	/**
	 * Despliega el listado de cotizaciones
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

		if (arg0.getParameter("empresas") == "")
			top.setVariable("{id_coti}", "");
	
		//Se setean en variables de session la empresa y sucursal
		if( arg0.getParameter("sucursales") != null && arg0.getParameter("empresas") != null ) {
			session.setAttribute("ses_suc_id", arg0.getParameter("sucursales").toString());
			session.setAttribute("ses_emp_id", arg0.getParameter("empresas").toString());
		}

		
		
		// Instacia del bizdelegate
		BizDelegate biz = new BizDelegate();

		
		CotizacionesCriteriaDTO coti = new CotizacionesCriteriaDTO();

		if( arg0.getParameter("empresas") != null )
			coti.setId_empresa(Long.parseLong(arg0.getParameter("empresas"))); //Id empresa
		if( arg0.getParameter("sucursales") != null )
			coti.setId_sucursal(Long.parseLong(arg0.getParameter("sucursales"))); //Id sucursal
		if (arg0.getParameter("estado") != null && !arg0.getParameter("estado").equals(""))
			coti.setId_estado(Long.parseLong(arg0.getParameter("estado"))); //Estado
		
		if (arg0.getParameter("fecha_desp") != null && arg0.getParameter("fecha_desp_fin") != null  && !arg0.getParameter("fecha_desp").equals("") && !arg0.getParameter("fecha_desp_fin").equals("") ){
			coti.setFec_ini(Formatos.formatFechaHoraFin(arg0.getParameter("fecha_desp"))); //Fecha inicio
			coti.setFec_fin(Formatos.formatFechaHoraFin(arg0.getParameter("fecha_desp_fin")));//Fecha Fin
			coti.setTipo_fec("ING");//Tipo de fecha
		}
		
		
		if (arg0.getParameter("patron") != null && !arg0.getParameter("patron").equals("")){
			if (Long.parseLong(arg0.getParameter("tipo_bus")) == 1)//Numero de cotizacion
				coti.setId_cot(Long.parseLong(arg0.getParameter("patron")+""));	
			else if(Long.parseLong(arg0.getParameter("tipo_bus")) == 2)//Rut empresa
				coti.setRut_emp(arg0.getParameter("patron"));
			else if(Long.parseLong(arg0.getParameter("tipo_bus")) == 3)//Nombre empresa
				coti.setNom_emp(arg0.getParameter("patron"));
			else if(Long.parseLong(arg0.getParameter("tipo_bus")) == 4)//Nombre de sucursal
				coti.setNom_sucursal(arg0.getParameter("patron"));
			else if(Long.parseLong(arg0.getParameter("tipo_bus")) == 5)//Direcion de despacho
				coti.setAlias_direccion(arg0.getParameter("patron"));
		}
		
		//Se setea la variable tipo usuario
		if(session.getAttribute("ses_tipo_usuario") != null && session.getAttribute("ses_tipo_usuario").equals("1") ){
			coti.setTipo_comprador(1); //Administrador
		}else{
		    coti.setTipo_comprador(0); //Comprador
		}

		coti.setId_Usuario(Long.parseLong(session.getAttribute("ses_com_id").toString()));
		
		//ingreso de un Comprador
		if( arg0.getParameter("compradores") == null && coti.getTipo_comprador() == 0) {
			coti.setId_comprador(Long.parseLong(session.getAttribute("ses_com_id").toString()));
			logger.info("ses_com_id:"+session.getAttribute("ses_com_id").toString());
		}

        //ingreso de un Administrador
		if(arg0.getParameter("compradores") != null && !arg0.getParameter("compradores").equals("")){
			coti.setId_comprador(Long.parseLong(arg0.getParameter("compradores")));
		}

		if( arg0.getParameter("compradores") != null && !arg0.getParameter("compradores").equals("0") ) {
			coti.setId_comprador(Long.parseLong(arg0.getParameter("compradores").toString()));
			logger.info("ses_com_id:"+session.getAttribute("ses_com_id").toString());
		}


		if(arg0.getParameter("sel_orden") != null && Long.parseLong(arg0.getParameter("sel_orden")) > 0){
			if(Long.parseLong(arg0.getParameter("sel_orden")) == 1)
				coti.setOrdena_por("cot_id desc");//Ordena por numero de cotizacion
			else if(Long.parseLong(arg0.getParameter("sel_orden")) == 2)
				coti.setOrdena_por("emp_nom asc");//Ordena por nombre de empresa
			else if(Long.parseLong(arg0.getParameter("sel_orden")) == 3)
				coti.setOrdena_por("cli_apellido_pat asc");//Ordena por nombre de sucursal
			else if(Long.parseLong(arg0.getParameter("sel_orden")) == 4)
				coti.setOrdena_por("cot_fing asc");//Ordena por fecha de ingreso
			else if(Long.parseLong(arg0.getParameter("sel_orden")) == 5)
				coti.setOrdena_por("cot_fvenc asc");//Ordena por fecha de vencimiento
			else if(Long.parseLong(arg0.getParameter("sel_orden")) == 6)
				coti.setOrdena_por("dir_alias asc");//Ordena por direccion de despcho
			else if(Long.parseLong(arg0.getParameter("sel_orden")) == 7)
				coti.setOrdena_por("nombre asc");//Ordena por estado
		}
		
		//Registros por pagina
		coti.setRegsperpag(Integer.parseInt(rb.getString("viewlistcotizaciones.cantregistros")));
		
		// Indicamos la procedencia
		coti.setProcedencia("W");

		/*String sucursales = biz.getListSucursalesByUser2(coti.getId_Usuario());
        boolean flag = false;
		while (rs.next()) {
		    if (flag){
		        result += ", " + rs.getString("CLI_ID");
		    }else{
		        result += rs.getString("CLI_ID"); 
		        flag = true;
		    }
		    
		}*/

		//coti.setListSucursales(sucursales);
		String sucursales = "";
		List lista_suc = new ArrayList();

		ComprXSucDTO CompXSuc = null;
		lista_suc = biz.getListSucursalesByUser(coti.getId_Usuario());
		boolean flag = false;
		for( int i = 0; i < lista_suc.size(); i++ ) {
		    CompXSuc = (ComprXSucDTO)lista_suc.get(i);
		    if (coti.getId_empresa() > 0 && coti.getId_empresa() == CompXSuc.getId_empresa()){
			    if (flag){
			        sucursales += ", " + CompXSuc.getId_sucursal();
			    }else{
			        sucursales += CompXSuc.getId_sucursal();
			        flag = true;
			    }			        
		    }else if (coti.getId_empresa() == 0){
			    if (flag){
			        sucursales += ", " + CompXSuc.getId_sucursal();
			    }else{
			        sucursales += CompXSuc.getId_sucursal(); 
			        flag = true;
			    }
		    }
		}
		coti.setListSucursales(sucursales);
		String compradores = "";

		ComprXSucDTO dtoComprXSucDTO = null;
		List lista_compradores = new ArrayList();
		List datos_comp = new ArrayList();
		
		flag = false;
		Hashtable comp = new Hashtable();
		for( int i = 0; i < lista_suc.size(); i++ ) {
			dtoComprXSucDTO = (ComprXSucDTO)lista_suc.get(i);
			if (coti.getId_sucursal() > 0 && coti.getId_sucursal() == dtoComprXSucDTO.getId_sucursal()){
				datos_comp = biz.getListCompradoresBySucursalId( dtoComprXSucDTO.getId_sucursal(), dtoComprXSucDTO.getTipo_acceso(), coti.getId_Usuario());
			}else if (coti.getId_sucursal() == 0){
			    datos_comp = biz.getListCompradoresBySucursalId( dtoComprXSucDTO.getId_sucursal(), dtoComprXSucDTO.getTipo_acceso(), coti.getId_Usuario());
			}
			
            if (datos_comp.size() > 0){
				for (int j = 0; j < datos_comp.size(); j++) {
					CompradoresDTO comprador = (CompradoresDTO) datos_comp.get(j);
					if (comp.get(""+comprador.getCpr_id()) == null){
					    comp.put(""+comprador.getCpr_id(), ""+comprador.getCpr_id());
					}
				}
            }
		}
		
        Iterator it = comp.keySet().iterator();
        while (it.hasNext()) {
            // Get key
            String key = (it.next()).toString();
		    if (flag){
		        compradores += ", " + comp.get(key);
		    }else{
		        compradores += comp.get(key);
		        flag = true;
		    }

        }
        coti.setListCompradores(compradores);
		
		
		//Recupera el listado de cotizaciones
		List lista =  biz.getCotizacionesByCriteria(coti);
		List datos = new ArrayList();
		
		//Si exite el parametro action, el tamaño del arreglo se setea a 10 (definido en properties)
		long sizearr = 0;
		if( arg0.getParameter("action") != null && !arg0.getParameter("action").equals("") ){
			if( lista.size() < Integer.parseInt(rb.getString("viewlistcotizaciones.cantcotizaciones")) ){
				sizearr = lista.size();
			}else{
				sizearr = Integer.parseInt(rb.getString("viewlistcotizaciones.cantcotizaciones"));
			}
		}else{
			sizearr = lista.size();
		}
		
		for (int i = 0; i < sizearr; i++) {
			MonitorCotizacionesDTO dir = (MonitorCotizacionesDTO) lista.get(i);
			IValueSet fila = new ValueSet();
			fila.setVariable("{id_coti}", dir.getId_cot() + "");
			fila.setVariable("{nom_emp}", dir.getNom_empresa());
			fila.setVariable("{nom_ape}", dir.getNombre_comprador());
			fila.setVariable("{nom_suc}", dir.getNom_sucursal());
			fila.setVariable("{fecha_ing}", Formatos.frmFechaHora(dir.getFec_ing()));
			fila.setVariable("{fecha_ven}", Formatos.frmFecha(dir.getFec_vencimiento()));
			fila.setVariable("{direccion}", dir.getAlias_dir());
			fila.setVariable("{estado}", dir.getEstado());
			if (dir.getCot_estado() ==  Constantes.ID_EST_COTIZACION_INGRESADA)
				fila.setVariable("{url}", "ViewNewCotizacionP3");
			else
				fila.setVariable("{url}", "ViewResumCotizacion");
			
			datos.add(fila);
		}
		top.setDynamicValueSets("LIST_COTIZACIONES", datos);
		
		String result = tem.toString(top);

		out.print(result);

	}

}