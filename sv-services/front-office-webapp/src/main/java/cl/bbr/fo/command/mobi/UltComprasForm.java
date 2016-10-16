package cl.bbr.fo.command.mobi;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.command.Command;
import cl.bbr.fo.util.Tracking_web;
import cl.bbr.jumbocl.clientes.dto.UltimasComprasDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.promos.interfaces.ClienteTcpPromosCupones;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.ConsultaCuponesPorRutDTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.RespR1DTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.TcpDTO;
import cl.bbr.promo.lib.dto.FOTcpDTO;
import cl.jumbo.interfaces.ventaslocales.CompraHistorica;
import cl.jumbo.interfaces.ventaslocales.InterfazVentas;

/**
 * Despliega página de últimas compras - paso 1 para mobile
 * 
 * @author imoyano
 * 
 */
public class UltComprasForm extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		// Recupera la sesión del usuario
		HttpSession session = arg0.getSession();
		
		try {
			
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");	
			
			if( arg0.getParameter("loc_id") != null ) {
				String aux = arg0.getParameter("loc_id").toString();
				String[] aux_text = aux.split("--");
				session.setAttribute("ses_loc_id", aux_text[2]);
				session.setAttribute("ses_dir_id", aux_text[0]);
				session.setAttribute("ses_dir_alias", aux_text[1]);
				session.setAttribute("ses_zona_id", aux_text[3]);
                
                session.setAttribute("ses_forma_despacho", "D"); //Seteamos esto para mobile 
				
				// Recuperar TCP del cliente desde el servidor de promociones
				try {
                    
					// Consulta por RUT al servidor
					ClienteTcpPromosCupones consulta = new ClienteTcpPromosCupones(); 
					consulta.setHost( rb.getString("promociones.consulta.cupones.host") );
					consulta.setPuerto( Integer.parseInt(rb.getString("promociones.consulta.cupones.puerto")) );
					
					// Instacia del bizdelegate
					BizDelegate biz = new BizDelegate();
					
					ConsultaCuponesPorRutDTO in_cupon = new ConsultaCuponesPorRutDTO();
					int cod_loc_pos = biz.getCodigoLocalPos( Long.parseLong(session.getAttribute("ses_loc_id").toString()) );
					in_cupon.setCod_local_pos( cod_loc_pos );
					in_cupon.setNum_pos( cod_loc_pos ); // igual que el local pos
					in_cupon.setRut( Long.parseLong(session.getAttribute("ses_cli_rut").toString()) );
					in_cupon.setDocumento( 1 );
					in_cupon.setJournal( 0 ); 
					in_cupon.setOperador( Integer.parseInt(session.getAttribute("ses_cli_id").toString()) ); // ID del cliente
					RespR1DTO resp = consulta.ConsultaCuponesPorRut( in_cupon );
					
					if( resp != null && Integer.parseInt(resp.getCod_ret()) == 0 ) {

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
						this.getLogger().debug("Problema con TCP " + resp.getCod_ret() + " " + resp.getGlosa1() + " " + resp.getGlosa2() );
					}
					

				} catch (Exception ex){
					this.getLogger().error("Error al recuperar TCP, cliente continua sin TCP - " + ex.getMessage());
					this.getLogger().error(ex);
					ex.printStackTrace();
					
					// agregar un tcp de prueba
					/*
					List l_tcp = new ArrayList();
					FOTcpDTO tcp = new FOTcpDTO();
					tcp.setTcp_nro( 231 );
					tcp.setTcp_max( 10 );
					l_tcp.add(tcp);
					session.setAttribute("ses_promo_tcp", l_tcp);
					*/
					
				}
				
			}
			this.getLogger().debug("Local id:"+session.getAttribute("ses_loc_id"));			
			
			// Eliminar o no los productos del carro
			String elicarro = arg0.getParameter("rbtnelicarro");
			this.getLogger().debug( "Vaciar carro:"+elicarro );
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
			
			top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
			
			top.setVariable("{lug_desp}", session.getAttribute("ses_dir_alias"));
            
            
            //-- UltComprasDisplayForm - INI TABLA CON ULTIMAS LISTAS DE COMPRA
            Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());
            
            //Defino los arreglos respectivos
            List ult_compras = new ArrayList();
            
            // Instacia del bizdelegate
            BizDelegate biz = new BizDelegate();
            
            //trae ultimas compras historicas (de locales fisicos)
            Vector vth = new Vector();
            try {
                // Inicializa la configuración
                Properties prop = new Properties();
                FileInputStream fis = new FileInputStream(getServletContext().getRealPath("/") + rb.getString("conf.venta_cfg"));
                prop.load(fis);
                InterfazVentas interfaz = new InterfazVentas();
                vth = interfaz.getComprasByRut(Integer.parseInt(((String)session.getAttribute("ses_cli_rut"))));
            }
            catch (Exception ex) {
                this.getLogger().error("No se pudieron rescatar las compras físicas para el cliente (mobi/UltComprasForm)",ex);
            }
            
            List reg_ultimas = biz.clienteGetUltCompras( cliente_id.longValue() );
            this.getLogger().debug( "Ultimas compras:" + reg_ultimas.size() + vth.size() );
            
            Calendar antes = Calendar.getInstance();
            antes.add(Calendar.DATE, Integer.parseInt("-"+rb.getString("ultimas.compras.display.form.dias")) );

            // Definición de formato para time
            SimpleDateFormat sdf = new java.text.SimpleDateFormat(Formatos.DATE);
            int suma = 0;
            int compras = 0;
            boolean internet = false;
            if (reg_ultimas.size() != 0) {
                internet = true;
                if (reg_ultimas.size() > 3) //se traen solo las ultimas compras
                	compras = 3;
                else
                    compras = reg_ultimas.size();    
            } else {
               compras = 0;
            }
            
            // Indica al HTML cuantos registros hay
            top.setVariable("{cantreg}", compras + vth.size() + "" );
            
            for( int i = 0; i < compras; i++ ) {
                UltimasComprasDTO data = (UltimasComprasDTO) reg_ultimas.get(i);
                IValueSet fila = new ValueSet();
                
                Calendar fecha = new GregorianCalendar();
                fecha.setTimeInMillis(data.getFecha()); // setea con fecha recibida
                
                //Se chequea solo la última compra de internet
                if (i == 0)
                    fila.setVariable("{checked}",  "checked");
                else
                    fila.setVariable("{checked}",  "");
                
                fila.setVariable("{id}",  data.getId()+"-"+data.getTipo() );
                fila.setVariable("{id_compra}",  ""+data.getId() );
                fila.setVariable("{fecha}", sdf.format(fecha.getTime()) );
                fila.setVariable("{lugar}", data.getLugar_compra());
                fila.setVariable("{unidades}", data.getUnidades()+"");
                fila.setVariable("{nombre}", data.getNombre());
                fila.setVariable("{elim}", "<a href=\"javascript:eliminar_lista('" + data.getId() + "','" + data.getNombre() + "')\">" +
                                           "<img src=\"/FO_IMGS/images/mobi/bt_del.gif\" border=\"0\" alt=\"Eliminar Lista\" title=\"Eliminar Lista\" width=\"22\" height=\"20\" />" +
                                           "</a>");
                fila.setVariable("{i}", i+"");
                suma++;
                ult_compras.add(fila);          
            }
            
            //se agregan ultimas compras fisicas para desplegar en el formulario
            for (int i = 0; i < vth.size(); i++) {
                CompraHistorica ch = (CompraHistorica)vth.elementAt(i);
                IValueSet fila = new ValueSet();
                if (internet)
                    fila.setVariable("{checked}",  ""); //no aparece chequeada la compra del local si hay compra internet
                else
                    fila.setVariable("{checked}",  "checked"); //se chequea solo si no hay compra internet  
                        
                fila.setVariable("{id}",  ch.getIdCompra() + "-" + "L");
                fila.setVariable("{id_compra}",  ""+ch.getIdCompra() );
                //14122012 VMATHEU
                fila.setVariable("{fecha}", "-" );
                fila.setVariable("{lugar}", "Compras últimos 3 meses en locales");
                //--14122012 VMATHEU
                fila.setVariable("{unidades}", ch.getUnidades() + "");
                fila.setVariable("{nombre}", "Productos Sugeridos");
                if (suma == 0){
                    fila.setVariable("{i}", (suma) + "");
                } else {
                    fila.setVariable("{i}", (suma + i) + "");
                }
                fila.setVariable("{elim}", "");
                ult_compras.add(fila);
            }
            if ( ult_compras.size() > 0 ) {
                top.setDynamicValueSets("ULT_COMPRAS", ult_compras);
                top.setDynamicValueSets("SIN_ULT_COMPRAS", null);
            } else {
                ArrayList vacio = new ArrayList();
                IValueSet iVacio = new ValueSet();
                iVacio.setVariable("{vacio}", "" );
                vacio.add(iVacio);
                top.setDynamicValueSets("ULT_COMPRAS", null);
                top.setDynamicValueSets("SIN_ULT_COMPRAS", vacio);
            }
            
            
			//-- UltComprasDisplayForm - FIN TABLA CON ULTIMAS LISTAS DE COMPRA
            
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
		    // --- "FIN - LAYER EVENTOS PERSONALIZADOS - PASO 1 ---
			
			
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
		}

	}

}