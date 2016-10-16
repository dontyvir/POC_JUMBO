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
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Permite la selección una nueva dirección de despacho para el checkout.
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class CheckoutAddressForm extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
 
		try {
			
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");
            
            arg0.setCharacterEncoding("UTF-8");
            arg1.setContentType("text/html; charset=iso-8859-1");

			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();

			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();
            
            String destino = arg0.getParameter("destino");

			// Recupera pagina desde web.xml
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter(destino);
			
			// Carga el template html
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();
			
			IValueSet top = new ValueSet();
            
            if (destino.equalsIgnoreCase("pag_form")) {
                // Instacia del bizdelegate
                BizDelegate biz = new BizDelegate();
                if (arg0.getParameter("mostrar_forma_despacho").equalsIgnoreCase("R")) {                
        			long idLocal = Long.parseLong(session.getAttribute("ses_loc_id").toString());
                    // Recupera locales disponibles para retiro en local
                    List listaLocalesRetiro = biz.localesRetiro();
                    List datos = new ArrayList();
                    for (int i = 0; i < listaLocalesRetiro.size(); i++) {
                        LocalDTO local = (LocalDTO) listaLocalesRetiro.get(i);
                        IValueSet fila = new ValueSet();
                        fila.setVariable("{dir_opcion}", local.getNom_local());
                        fila.setVariable("{dir_valor}",  local.getId_local()+"");
                        fila.setVariable("{dir_local}",  local.getId_local()+"");
                        fila.setVariable("{dir_zona}",   local.getIdZonaRetiro()+"");
                        if (idLocal == local.getId_local()) {
                            fila.setVariable("{selectdirec}",  "selected");
                        } else {
                            fila.setVariable("{selectdirec}",  "");
                        }
                        datos.add(fila);                         
                    }
                    IValueSet box = new ValueSet();
                    box.setDynamicValueSets("dir_despachos",datos);                    
                    box.setVariable("{nueva_forma_despacho}", "R");
                    List combos = new ArrayList();
                    combos.add(box);                    
                    top.setDynamicValueSets("MOSTRAR_RETIRO", combos);
                    
                } else {
                    //Recupera el cliente desde la sesión
                    long cliente_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());                    
                    long direcc_id = Long.parseLong(session.getAttribute("ses_dir_id").toString());
        
                    List lista = biz.clienteAllDireccionesConCobertura( cliente_id ); 
                    List datos = new ArrayList();
        
                    for (int i = 0; i < lista.size(); i++) {
                         DireccionesDTO dir = (DireccionesDTO) lista.get(i); 
                         IValueSet fila = new ValueSet(); 
                         fila.setVariable("{dir_opcion}", dir.getAlias());
                         fila.setVariable("{dir_valor}",  dir.getId()+"");
                         fila.setVariable("{dir_local}",  dir.getLoc_cod()+"");
                         fila.setVariable("{dir_zona}",  dir.getZona_id()+"");
                         if (direcc_id == dir.getId()) {
                             fila.setVariable("{selectdirec}",  "selected");
                         } else {
                             fila.setVariable("{selectdirec}",  "");
                         }
                         datos.add(fila);
                    }
                    IValueSet box = new ValueSet();
                    box.setDynamicValueSets("dir_despachos",datos);
                    box.setVariable("{nueva_forma_despacho}", arg0.getParameter("mostrar_forma_despacho"));
                    List combos = new ArrayList();
                    combos.add(box);
                    top.setDynamicValueSets("MOSTRAR_DESPACHO", combos);   
                    
                }			
            }
            top.setVariable("{vacio}", "");
			String result = tem.toString(top);
			out.print(result);

		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
			throw new CommandException( e );
		}
	}
}