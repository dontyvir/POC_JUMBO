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
import cl.bbr.vte.empresas.dto.CompradoresDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;
import cl.bbr.vte.exception.VteException;
import cl.bbr.vte.utils.Cod_error;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que muestra el listado de compradores que pertenecen a una sucursal
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewListComSuc extends Command {

	/**
	 * Despliega el listado de compradores por sucursal
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
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			
			long idsuc = 0;
			if (arg0.getParameter("idsuc") != null ){
				session.setAttribute("session_idsuc", arg0.getParameter("idsuc"));
				idsuc = Long.parseLong(arg0.getParameter("idsuc"));
			}else{
				idsuc = Long.parseLong(session.getAttribute("session_idsuc")+"");
			}
			
			SucursalesDTO suc = (SucursalesDTO)biz.getSucursalById(idsuc);
			// Se revisa si existe el cliente
			if( suc == null ) {
				logger.warn("Sucursal no existe " + idsuc);
				throw new VteException( Cod_error.CLI_NO_EXISTE );
			}
			
			logger.info( "Nombre Sucursal:" + suc.getSuc_nombre());

			// Se recupera nombre y apellido del comprador
			top.setVariable("{nombre_sucursal}", suc.getSuc_nombre() );
			
			
			//Listado de compradores que pertenecen a la sucursal dada por parametro
			List listcomasig = biz.getListCompradoresBySucursalId( idsuc );
			ArrayList array_com_asig = new ArrayList();
			long contloc = 0;
			for (int i = 0; i < listcomasig.size(); i++) {
				CompradoresDTO reg_tc2 = (CompradoresDTO) listcomasig.get(i);
				IValueSet fila2 = new ValueSet();
				fila2.setVariable("{id}", reg_tc2.getCpr_id()+"");
				fila2.setVariable("{idsuc}", idsuc+"");
				fila2.setVariable("{comprador_nombre}", reg_tc2.getCpr_nombres()+ " "+reg_tc2.getCpr_ape_pat() );
				if (listcomasig.size() > 1){
					List list_boton = new ArrayList();
					list_boton.add(fila2);
					fila2.setDynamicValueSets("NOBOTONBORRAR", list_boton );
				}
				fila2.setVariable("{contloc}", contloc+"");
				contloc++;
				array_com_asig.add(fila2);
			}
			top.setVariable("{cant_com_asig}", listcomasig.size()+"");
			top.setDynamicValueSets("list_comp_asig", array_com_asig );
			
			// Presenta bloque de compradores existentes
			// Recupera lista de compradores que pertenecen a alguna sucursal de las empresa que administra
			List listcom = biz.getListAdmCompradoresByAdministradorId( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			
			ArrayList arr_com = new ArrayList();
			for (int i = 0; i < listcom.size(); i++) {
				CompradoresDTO reg_tc = (CompradoresDTO) listcom.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{id}", reg_tc.getCpr_id()+"");
				fila.setVariable("{nombre}", reg_tc.getCpr_nombres()+ " "+reg_tc.getCpr_ape_pat() );
				arr_com.add(fila);
			}
			top.setDynamicValueSets("LIST_COMPRADORES", arr_com );
			
			
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}

	}

}