package cl.bbr.fo.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.pedidos.promos.interfaces.ClienteTcpPromosCupones;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.ConsultaCuponPorIdDTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.RespC1DTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.TcpDTO;
import cl.bbr.promo.lib.dto.FOTcpDTO;

/**
 * Actualiza los cupones al pedido - Se modifica a Servlet AJAX para el paso 3
 *  
 * @author BBR e-commerce & retail
 * 
 *  
 */
public class CuponUpd extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws CommandException {
        
        this.getLogger().debug( "******* CuponUpd - VAMOS A AGREGAR O QUITAR CUPONES ***********" );
		
        String respuesta = "OK";
        String msg_cupon = "";
        
        try {
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			if( arg0.getParameter("cupones") == null ) {
                respuesta = "Ocurrió un error con parámetro no encontrado (cupones)";
                
			} else {
				// Agrega cupones si los informan, para el paso 3 Checkout
    			this.getLogger().debug( "PROMO --> Cupones: " + arg0.getParameter("cupones") );
    			
    			List l_cupones = null;
    			if( session.getAttribute("ses_cupones") != null )
    				l_cupones = (List)session.getAttribute("ses_cupones");
    			
    			
    			if( arg0.getParameter("cupones") != null && !arg0.getParameter("cupones").equals("") ) {
    				String []arr_cupones = arg0.getParameter("cupones").split("-=-");
    				
    				if( l_cupones == null )
    					l_cupones = new ArrayList();
    				
    				// Insertar los cupones que no existen
    				boolean ya_insertado = false;
    				for( int i = 0; i < arr_cupones.length; i++ ) {
    					ya_insertado = false;
    					for( int f = 0; f < l_cupones.size(); f++ ) {
    						FOTcpDTO tcp = (FOTcpDTO)l_cupones.get(f);
    						if( tcp.getCupon().equals( arr_cupones[i] ) ) {
    							ya_insertado = true;
    						}
    					}
    					if ( !ya_insertado ) {    						
    						try {
    							// Consulta por cupon al servidor
    							ClienteTcpPromosCupones consulta = new ClienteTcpPromosCupones(); 
    							consulta.setHost( rb.getString("promociones.consulta.cupones.host") );
    							consulta.setPuerto( Integer.parseInt(rb.getString("promociones.consulta.cupones.puerto")) );
    							
    							ConsultaCuponPorIdDTO in_cupon = new ConsultaCuponPorIdDTO();
    							int cod_loc_pos = biz.getCodigoLocalPos( Long.parseLong(session.getAttribute("ses_loc_id").toString()) );
    							in_cupon.setCod_local_pos( cod_loc_pos );
    							in_cupon.setNum_pos( cod_loc_pos ); // igual que el local pos
    							in_cupon.setCupon( arr_cupones[i] );
    							in_cupon.setDocumento( 1 );
    							in_cupon.setJournal( 0 ); 
    							in_cupon.setOperador( Integer.parseInt(session.getAttribute("ses_cli_id").toString()) ); // ID del cliente
    							RespC1DTO resp = consulta.ConsultaCuponPorId( in_cupon );
    							
    							if( resp.getCod_ret().equals("00") ) {
    
    								TcpDTO tcp_promo = resp.getTcp();
    								
    								// Insertar información a la lista
    								FOTcpDTO tcp = new FOTcpDTO();
    								tcp.setTcp_nro( Integer.parseInt(tcp_promo.getNro_tcp()) );
    								tcp.setCupon( arr_cupones[i] );
    								tcp.setTcp_max( tcp_promo.getCantidad() );
    								l_cupones.add(tcp);
    								
    							} else {    								
    								this.getLogger().error("Problema con TCP " + resp.getCod_ret() + " " + resp.getGlosa1() + " " + resp.getGlosa2() );
    								msg_cupon = resp.getGlosa1() + " " + resp.getGlosa2().replace('0',' ');
                                    
                                    //Arreglo parche ya que este mensaje lo entrega la libreria de promociones
                                    if ( msg_cupon.equalsIgnoreCase("CUPON NO EXISTE EN LA BASE DE DATOS")) {
                                        msg_cupon = "El número de cupón no corresponde. Por favor inténtalo nuevamente.";
                                    }
    								
    							}
    							
    						} catch (Exception e) {
    							msg_cupon = "Problemas de conexión con servidor de cupones, inténtelo mas tarde.";
    							this.getLogger().error(e);
    						}
    
    					}
    				}
    				// Eliminar los cupones que ya no están en el arreglo
    				for( int f = 0; f < l_cupones.size(); f++ ) {
    					ya_insertado = false;
    					FOTcpDTO tcp = (FOTcpDTO)l_cupones.get(f);
    					for( int i = 0; i < arr_cupones.length; i++ ) {
    						if( tcp.getCupon().equals( arr_cupones[i] ) ) {
    							ya_insertado = true;
    						}
    					}
    					if ( !ya_insertado )	{
    						// Eliminar el cupon de la lista
    						l_cupones.remove(f);
    					}
    				}
    				
    			} else {
    				l_cupones = null;
    			}
    			
    			session.setAttribute("ses_cupones", l_cupones );
    			session.setAttribute("ses_msg_cupon", msg_cupon);
    			
    			this.getLogger().debug( "PROMO --> Cupones Session: " + session.getAttribute("ses_cupones") );
    			this.getLogger().debug( "PROMO --> Cupones Session mensaje: " + session.getAttribute("ses_msg_cupon") );
            }	
            
            

		} catch (Exception e) {
            respuesta = "Ocurrió un error con los cupones de descuento.";
		}
        
        arg1.setContentType("text/xml");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("UTF-8");
        try {
            arg1.getWriter().write("<datos_objeto>");
            arg1.getWriter().write("<respuesta>" + respuesta + "</respuesta>");
            arg1.getWriter().write("<mensaje>" + msg_cupon + "</mensaje>");
            arg1.getWriter().write("</datos_objeto>");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

	}

}