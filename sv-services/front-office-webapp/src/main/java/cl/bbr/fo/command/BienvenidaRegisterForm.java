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
import cl.bbr.fo.util.Tracking_web;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página de bienvenida a los clientes registrados
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class BienvenidaRegisterForm extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {

		try {
		    // Instacia del bizdelegate
            BizDelegate biz = new BizDelegate();
            
            // Recupera la sesión del usuario
            HttpSession session = arg0.getSession();
            
            long idCliente = Long.parseLong( session.getAttribute("ses_cli_id").toString());
            
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");			
			
			// Se almacena tracking en este sector
			//Tracking_web.saveTracking("BienvenidaRegistro", arg0);
            
            //Elimina errores de sesión
            session.removeAttribute("cod_error");
            
            // Se recupera la salida para el servlet
            PrintWriter out = arg1.getWriter();

            // Aca vemos si el cliente tiene direcciones con cobertura
            if ( !biz.tieneDireccionesConCobertura(idCliente) ) {
                //redireccionamos a la bienvenida para clientes sin cobertura
                String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("bnv_sc");
                TemplateLoader load = new TemplateLoader(pag_form);
                ITemplate tem = load.getTemplate();    
                IValueSet top = new ValueSet();
                
                top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());                
                
                String result = tem.toString(top);                
                out.print(result);
                
            } else {
            
                String formaDespacho = "N"; //D:Despacho domicilio o R:Retiro Local
                if (session.getAttribute("ses_forma_despacho") != null) {
                    formaDespacho = session.getAttribute("ses_forma_despacho").toString();
                }
                
                // Recupera pagina desde web.xml y se inicia parser
                String pag_form = rb.getString("conf.apache.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
                this.getLogger().debug( "Template:" + pag_form );
                TemplateLoader load = new TemplateLoader(pag_form);
                ITemplate tem = load.getTemplate();
    
                IValueSet top = new ValueSet();
                
    			String key = rb.getString("GoogleMaps.key");
    			top.setVariable("{key}", key);

                top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
                
                // Recuperar direcciones de despacho del cliente
                List lista = biz.clienteAllDireccionesConCobertura( idCliente );
                this.getLogger().debug( "Direcciones despacho:"+lista.size() );
                List datos = new ArrayList();
                
                for (int i = 0; i < lista.size(); i++) {
                    DireccionesDTO dir = (DireccionesDTO) lista.get(i);
                    IValueSet fila = new ValueSet();
                     if (!formaDespacho.equalsIgnoreCase("R") && session.getAttribute("ses_dir_id") != null && Long.parseLong(session.getAttribute("ses_dir_id").toString()) ==  dir.getId()){
                         fila.setVariable("{selected_dir}", "selected=\"selected\"");
                     }else{
                         fila.setVariable("{selected_dir}", "");
                     }
                     fila.setVariable("{dir_opcion}", dir.getAlias()+": " + dir.getCalle() + " " + dir.getNumero()  + ", " + dir.getCom_nombre() );
                     fila.setVariable("{dir_valor}",  dir.getId() + "--" + dir.getAlias() + "--" + dir.getLoc_cod() + "--" + dir.getZona_id() + "--" + dir.isConfirmada() + "--" + dir.getLatitud() + "--" + dir.getLongitud() );
                     datos.add(fila);
                } 
                top.setDynamicValueSets("dir_despachos",datos);
                
                boolean tieneRetiro =  biz.clienteTieneDisponibleRetirarEnLocal(idCliente);
                
                if (tieneRetiro) {
                    // Recupera locales disponibles para retiro en local
                    List listaLocalesRetiro = biz.localesRetiro();
                    List datosRetiro = new ArrayList();
                    for (int i = 0; i < listaLocalesRetiro.size(); i++) {
                        LocalDTO local = (LocalDTO) listaLocalesRetiro.get(i); 
                         IValueSet filaRe = new ValueSet(); 
                         if (formaDespacho.equalsIgnoreCase("R") && session.getAttribute("ses_dir_id") != null && Long.parseLong(session.getAttribute("ses_dir_id").toString()) ==  local.getId_local()){
                             filaRe.setVariable("{selected_dir}", "selected=\"selected\""); 
                         }else{
                             filaRe.setVariable("{selected_dir}", "");
                         }
                         filaRe.setVariable("{dir_opcion}", local.getNom_local());
                         filaRe.setVariable("{dir_valor}",  local.getId_local() + "--" + local.getNom_local() + "--" + local.getId_local() + "--" + local.getIdZonaRetiro() );
                         datosRetiro.add(filaRe);
                    } 
                    top.setDynamicValueSets("dir_retiro",datosRetiro);
                    top.setVariable("{tiene_retiro}", "S");
                } else {
                    top.setVariable("{tiene_retiro}", "N");
                    
                }
                
                if ( biz.clienteExisteCarroCompras( idCliente ) ) {
                    List datos_e = new ArrayList();
                    IValueSet fila = new ValueSet();
                    fila.setVariable("{mostrar}", "mostrar");
                    fila.setVariable("{cliente_id}", idCliente+"");
                    datos_e.add(fila);
                    top.setDynamicValueSets("existe_carro", datos_e); 
                }
                
                // --- INI - SUSTITUTOS
                if ( biz.clienteTieneSustitutos( idCliente ) ) {
                    List mostrarSustitutos = new ArrayList(); 
                    IValueSet fila = new ValueSet();
                    fila.setVariable("{from}", "BienvenidaForm"); 
                    mostrarSustitutos.add(fila);
                    top.setDynamicValueSets("MOSTRAR_CRITERIOS", mostrarSustitutos); 
                }
                // --- FIN - SUSTITUTOS
                
                // --- INI - LAYER EVENTOS PERSONALIZADOS - BIENVENIDA ---
                /*EventoDTO evento = new EventoDTO();
                String codLayerFlash = UtilsEventos.codHtmlDeFlash( Long.parseLong(session.getAttribute("ses_cli_rut").toString()), EventosConstants.PASO_BIENVENIDA );
                if ( codLayerFlash.length() > 0 ) {
                    evento = UtilsEventos.eventoMostrado( Long.parseLong(session.getAttribute("ses_cli_rut").toString()) );
                    top.setVariable("{titulo_evento}",  UtilsEventos.tituloMostrar( evento.getTitulo(), session.getAttribute("ses_cli_nombre_pila").toString() ));
                    top.setVariable("{mostrar_evento}", "SI");
                } else {
                    top.setVariable("{titulo_evento}",  "");
                    top.setVariable("{mostrar_evento}", "NO");
                }
                top.setVariable("{src_flash}", codLayerFlash);*/
                // --- FIN - LAYER EVENTOS PERSONALIZADOS - BIENVENIDA ---
                
                if (tieneRetiro) {
                    if (formaDespacho.equalsIgnoreCase("R")) {
                        top.setVariable("{checked_ret}", "checked");
                        top.setVariable("{checked_dom}", "");
                        
                    } else {
                        top.setVariable("{checked_dom}", "checked");
                        top.setVariable("{checked_ret}", "");
                        
                    }
                } else {
                    top.setVariable("{checked_ret}", "");
                    top.setVariable("{checked_dom}", "");
                }
                            
                String result = tem.toString(top);
    
                out.print(result);
            }

		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
			throw new CommandException(e);
		}

	}

}