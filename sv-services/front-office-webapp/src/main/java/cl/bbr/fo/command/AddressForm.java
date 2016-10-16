package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;

import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.RegionesDTO;
import cl.bbr.jumbocl.clientes.dto.TipoCalleDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Presenta el formulario para la modificacion de direccion del cliente
 * 
 * @author BBRI
 *  
 */
public class AddressForm extends Command {

	/**
	 * Presenta el formulario de direcciones de despacho.
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {
		
		try {
			
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");			
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();			
						
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter(); 
	
			// Recupera parámetro desde web.xml
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			
			// Template de despliegue Listado tipo de calle
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();
			
			IValueSet top = new ValueSet();
			
			// Revisión de errores
			if( session.getAttribute("cod_error") != null ) {
				top.setVariable("{error}", "1");
				top.setVariable("{mensaje_error}", rb.getString("general.mensaje.error") + " (" + session.getAttribute("cod_error") + ")");
				session.removeAttribute("cod_error");
				top.setVariable("{alias}", arg0.getParameter("alias"));
				top.setVariable("{calle}", arg0.getParameter("calle"));
				top.setVariable("{numero}", arg0.getParameter("numero"));
				top.setVariable("{departamento}", arg0.getParameter("departamento"));
				top.setVariable("{comentario}", arg0.getParameter("comentario"));
				top.setVariable("{_com_inicial}", "0");
			}
			else {
				top.setVariable("{error}", "0");
				top.setVariable("{mensaje_error}", "" );
				top.setVariable("{alias}", "");
				top.setVariable("{calle}", "");
				top.setVariable("{numero}", "");
				top.setVariable("{departamento}", "");
				top.setVariable("{comentario}", "ej: timbre malo");		
				top.setVariable("{_com_inicial}", "1");
			}	
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
	
			
			// Listado de tipos de calle
			ArrayList arr_tipocalle = new ArrayList();
			List registros = biz.tiposCalleGetAll();
			for (int i = 0; i < registros.size(); i++) {
				TipoCalleDTO reg_tc = (TipoCalleDTO) registros.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{option_calle_id}", reg_tc.getId() + "");
				fila.setVariable("{option_calle_nombre}", reg_tc.getNombre());
				arr_tipocalle.add(fila);
			}
			top.setDynamicValueSets("select_tipocalle", arr_tipocalle);
			
			ArrayList arr_regiones = new ArrayList();
			List regiones = biz.regionesGetAll();
			for (int i = 0; i < regiones.size(); i++) {
				RegionesDTO dbregion = (RegionesDTO) regiones.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{option_reg_id}", dbregion.getId()+"");
				fila.setVariable("{option_reg_nombre}", dbregion.getNombre());
				arr_regiones.add(fila);
			}
			top.setDynamicValueSets("select_regiones", arr_regiones);
			
			if (session.getAttribute("ses_cli_id") != null) {
				Long cliente_id = new Long(session.getAttribute("ses_cli_id")
						.toString());
				
				// Recuperar direcciones de despacho del cliente
				List lista = biz.clientegetAllDirecciones(cliente_id.longValue());
				List datos = new ArrayList();
				for (int i = 0; i < lista.size(); i++) {
					DireccionesDTO dir = (DireccionesDTO) lista.get(i);
					IValueSet fila = new ValueSet();
					fila.setVariable("{dir_opcion}", dir.getAlias() + "");
					fila.setVariable("{dir_calle}", dir.getCalle() + "");
					fila.setVariable("{dir_numero}", dir.getNumero() + "");
					fila.setVariable("{dir_dpto}", dir.getDepto() + "");
					fila.setVariable("{dir_valor}", dir.getId() + "");
					fila.setVariable("{dir_comentario}", dir.getComentarios() + "");
					int largo = 12;
					if( dir.getComentarios().length() < 12  )
						largo = dir.getComentarios().length(); 
					fila.setVariable("{dir_comentario_s}", dir.getComentarios().substring(0,largo) + "");
					fila.setVariable("{dir_tipid}", dir.getTipo_calle() + "");
					fila.setVariable("{dir_id_comuna}", dir.getCom_id() + "");
					fila.setVariable("{dir_comuna}", dir.getCom_nombre() + "");
					fila.setVariable("{dir_region}", dir.getReg_nombre() + "");
					fila.setVariable("{dir_id_region}", dir.getReg_id() + "");
					if (lista.size() > 1){
						List list_boton = new ArrayList();
						list_boton.add(fila);
						fila.setDynamicValueSets("NOBOTONBORRAR", list_boton );
					}
					
					datos.add(fila);
				}
				top.setDynamicValueSets("dir_despachos", datos);
			}
  
			top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
			
			String result = tem.toString(top);
	
			out.print(result);
			
			} catch (Exception e) {
				this.getLogger().error(e);
				e.printStackTrace();
				throw new CommandException(e);
			}

		}

}