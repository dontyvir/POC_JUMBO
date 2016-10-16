package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.util.ComunasRegionesTexto;
import cl.bbr.fo.util.Tracking_web;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.promos.interfaces.ClienteTcpPromosCupones;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.ConsultaCuponesPorRutDTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.RespR1DTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.TcpDTO;
import cl.bbr.promo.lib.dto.FOTcpDTO;

/**
 * Despliega página de últimas compras
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class UltComprasForm extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		// Recupera la sesión del usuario
		HttpSession session = arg0.getSession();
		
		// Instacia del bizdelegate
        BizDelegate biz = new BizDelegate();
        
		try {			
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");		
                
			// Recuperar TCP del cliente desde el servidor de promociones
			try {
				long rut = 0L;
			    rut = Long.parseLong(session.getAttribute("ses_cli_rut").toString());
                if (rut != 5042398 && rut != 5785004 && rut != 5568407 && rut != 5781581 && rut != 5511202 && rut != 99999998 && rut != 99999997) {
                    // Consulta por RUT al servidor
                    ClienteTcpPromosCupones consulta = new ClienteTcpPromosCupones(); 
    				consulta.setHost( rb.getString("promociones.consulta.cupones.host") );
    				consulta.setPuerto( Integer.parseInt(rb.getString("promociones.consulta.cupones.puerto")) );
    				ConsultaCuponesPorRutDTO in_cupon = new ConsultaCuponesPorRutDTO();
                    int cod_loc_pos = biz.getCodigoLocalPos( Long.parseLong((String) session.getAttribute("ses_loc_id")) );
    				in_cupon.setCod_local_pos( cod_loc_pos );
    				in_cupon.setNum_pos( cod_loc_pos ); // igual que el local pos
    				in_cupon.setRut( Long.parseLong(session.getAttribute("ses_cli_rut").toString()) );
    				in_cupon.setDocumento( 1 );
    				in_cupon.setJournal( 0 ); 
    				in_cupon.setOperador( Integer.parseInt((String) session.getAttribute("ses_cli_id")) ); // ID del cliente
    				//RespR1DTO resp = consulta.ConsultaCuponesPorRut( in_cupon ); //MCA 
    				RespR1DTO resp = null;
    				try{
    					resp = consulta.ConsultaCuponesPorRut( in_cupon );
    				}catch (Exception e) {
						// TODO: handle exception MCA
    					System.out.println("UltComprasForm.execute.ConsultaCuponesPorRut.Problema con TCP para Cliente.{"+ rut +"}");
    					if(resp != null){
    					  this.getLogger().debug("Problema con TCP ConsultaCuponesPorRut" + resp.getCod_ret() + " " + resp.getGlosa1() + " " + resp.getGlosa2() +"Cliente.{"+ rut +"}" );
    					}else{
    					  this.getLogger().debug("Problema con TCP ConsultaCuponesPorRut.Cliente.{"+ rut +"}"+ e.getMessage() );
    			    			
    					}
					}
                    int codRet=0;
                    if (resp != null && resp.getCod_ret() != null){
                    	codRet = Integer.parseInt(resp.getCod_ret().trim());
                    }else{
                        codRet = 1;
                    }
    				//if( Integer.parseInt(resp.getCod_ret()) == 0 ) {
    				
    				if( codRet == 0) {
    					List l_tcp = new ArrayList();
    					List l_tcps = resp.getTcps();
    					for ( int i = 0; l_tcps != null && i < l_tcps.size(); i++ ) {
    						
    						TcpDTO tcp_promo = (TcpDTO) l_tcps.get(i);
    						
    						FOTcpDTO tcp = new FOTcpDTO();
    						this.getLogger().debug("TCP "+i+" nro:" + tcp_promo.getNro_tcp() + " Cantidad:" + tcp_promo.getCantidad() );
    						tcp.setTcp_nro( Integer.parseInt(tcp_promo.getNro_tcp()) );
    						tcp.setTcp_max( tcp_promo.getCantidad() );
    						l_tcp.add(tcp);
    						
    					}
    					session.setAttribute("ses_promo_tcp", l_tcp);
    					
    				} else {
    					if(resp != null)
    					this.getLogger().debug("Problema con TCP " + resp.getCod_ret() + " " + resp.getGlosa1() + " " + resp.getGlosa2() );
    				}
                }

			} catch (Exception ex){
//20120823 Andres Valle				
				if(ex.getMessage() == null)
					this.getLogger().error("Error al recuperar TCP, cliente continua sin TCP - " + ex.getClass().getName());
				else		
					this.getLogger().error("Error al recuperar TCP, cliente continua sin TCP - " + ex.getMessage());
				//this.getLogger().error(ex);
//-20120823 Andres Valle
				ex.printStackTrace();
			}
			
			// Eliminar o no los productos del carro
			String elicarro = arg0.getParameter("rbtnelicarro");
			
            if (elicarro != null && elicarro.compareTo("0")== 0 ){//Se eliminan los productos del carro
				arg1.sendRedirect( getServletConfig().getInitParameter("dis_del") );
				return;
			}
			
			// Se almacena tracking en este sector
			//Tracking_web.saveTracking("Ultimas Compras", arg0);

			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();

			// Recupera pagina desde web.xml
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			this.getLogger().debug( "Template:"+pag_form );
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();
			
			IValueSet top = new ValueSet();
			
			//max correccion bug invitado
			if(session.getAttribute("ses_cli_nombre").toString().indexOf(" ") != -1) {
				top.setVariable("{nombre_cliente}", "Hola "+session.getAttribute("ses_cli_nombre").toString().substring(0,session.getAttribute("ses_cli_nombre").toString().indexOf(" ")));
			} else {
			top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
			}
			
			
			top.setVariable("{lug_desp}", session.getAttribute("ses_dir_alias"));
            
			// --- INI - LAYER EVENTOS PERSONALIZADOS - PASO 1 ---
			/*EventoDTO evento = new EventoDTO();
			String codLayerFlash = UtilsEventos.codHtmlDeFlash(Long.parseLong(session.getAttribute("ses_cli_rut").toString()), EventosConstants.PASO_1);
			if ( codLayerFlash.length() > 0 ) {
			    evento = UtilsEventos.eventoMostrado( Long.parseLong(session.getAttribute("ses_cli_rut").toString()) );
			    top.setVariable("{titulo_evento}",	UtilsEventos.tituloMostrar( evento.getTitulo(), session.getAttribute("ses_cli_nombre_pila").toString() ));
				top.setVariable("{mostrar_evento}", "SI");
		    } else {
		        top.setVariable("{titulo_evento}",	"");
		        top.setVariable("{mostrar_evento}", "NO");
		    }
		    top.setVariable("{src_flash}", codLayerFlash);*/
	        // ---- FIN - LAYER EVENTOS PERSONALIZADOS - PASO 1 ---
            
	        // --- INI - SUSTITUTOS
            if ( biz.clienteTieneSustitutos( Long.parseLong(session.getAttribute("ses_cli_id").toString()) ) ) {
                List mostrarSustitutos = new ArrayList(); 
                IValueSet fila = new ValueSet();
                fila.setVariable("{from}", "UltComprasForm"); 
                mostrarSustitutos.add(fila);
                top.setDynamicValueSets("MOSTRAR_CRITERIOS", mostrarSustitutos); 
            }
            // --- FIN - SUSTITUTOS
            
            // --- INI - TRACKING OP
            if ( biz.clienteConOPsTracking( Long.parseLong(session.getAttribute("ses_cli_id").toString())  ) ) {
                List mostrarTracking = new ArrayList(); 
                IValueSet fila = new ValueSet();
                fila.setVariable("{from}", "UltComprasForm"); 
                mostrarTracking.add(fila);
                top.setDynamicValueSets("MOSTRAR_TRACKING", mostrarTracking); 
            }
            // --- FIN - TRACKING OP
            
            if ( !biz.clienteHaComprado( Long.parseLong(session.getAttribute("ses_cli_id").toString()) ) ) {
                List mostrarBanner = new ArrayList(); 
                IValueSet fila = new ValueSet();
                fila.setVariable("{vacio}", ""); 
                mostrarBanner.add(fila);
                top.setDynamicValueSets("MOSTRAR_BANNER_DESPACHO", mostrarBanner);
            }
            
            //OPENDONALD obtener comuna del cliente
            if ( !"1".equalsIgnoreCase( session.getAttribute("ses_cli_id").toString() )) {
                DireccionesDTO dir = biz.clienteGetDireccion(Long.parseLong(session.getAttribute("ses_dir_id").toString()));
                session.setAttribute("ses_comuna_cliente", ""+dir.getReg_id()+"-=-"+dir.getCom_id()+"-=-"+dir.getCom_nombre());
            }
            
            // -- Calendario despacho o retiro
            String formaDespacho = "D"; //D:Despacho domicilio o R:Retiro Local
            if (session.getAttribute("ses_forma_despacho") != null) {
                formaDespacho = session.getAttribute("ses_forma_despacho").toString();
                List mostrar = new ArrayList(); 
                IValueSet fila = new ValueSet();
                fila.setVariable("{vacio}", ""); 
                mostrar.add(fila);
                if (formaDespacho.equalsIgnoreCase("R")) {
                    top.setDynamicValueSets("MUESTRA_CAL_RETIRAR", mostrar);                    
                } else {
                    top.setDynamicValueSets("MUESTRA_CAL_DESPACHO", mostrar);
                }                
            }   
            
            // INI - DATOS PARA EL CHAT
            top.setVariable("{nom_cliente}", session.getAttribute("ses_cli_nombre_pila").toString());
            top.setVariable("{ape_cliente}", session.getAttribute("ses_cli_apellido_pat").toString());
            top.setVariable("{dia_actual}", Utils.getFechaActualByPatron("E"));
            top.setVariable("{hora_actual}", Utils.getFechaActualByPatron("HHmm"));
            // FIN - DATOS PARA EL CHAT

			if (arg0.getParameter("opcion") != null && arg0.getParameter("opcion") != ""){
				top.setVariable("{opcion}", arg0.getParameter("opcion"));
			}else{
				top.setVariable("{opcion}", "");
            }
            
			//Tags Analytics - Captura de estado de user
            if(session.getAttribute("ses_cli_nombre_pila").toString().equals("Invitado")){
                top.setVariable("{ta_mx_user}", "invitado");
            }
            else{
                top.setVariable("{ta_mx_user}", "registrado");
            }
			//Tags Analytics - Captura de Comuna y Región en Texto
			/************   LISTADO DE REGIONES   ****************/
			  
			String ta_mx_loc = ComunasRegionesTexto.ComunaRegionTexto(session);
			if(ta_mx_loc.equals(""))
			ta_mx_loc="none-none";
			top.setVariable("{ta_mx_loc}", ta_mx_loc);
			  
			top.setVariable("{mx_content}", "Mis Listas");
			
			String result = tem.toString(top);
			out.print(result);

		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
		}

	}

}