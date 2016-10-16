package cl.bbr.fo.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.IValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.UtilsCarroCompra;
import cl.bbr.jumbocl.clientes.dto.MiCarroDTO;
import cl.bbr.jumbocl.common.utils.Formatos;

/**
 * Actualiza la cantidad de un producto en el carro - Ajax
 * 
 * IDEM OrderItemUpdate
 *  
 * @author imoyano
 *  
 */
public class ModProductosCarroPaso1 extends Command {
	
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {
        
        String respuesta = "OK";
        String ses_loc_id = null;
		String ses_colaborador = null;
		String ses_cli_rut = null;
		try {
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			ResourceBundle rb = ResourceBundle.getBundle("fo");
			UtilsCarroCompra utils =  new UtilsCarroCompra();
			List l_torec = new ArrayList();
			List l_tcp = new ArrayList();
			
			if(session.getAttribute("ses_loc_id") != null){ses_loc_id = session.getAttribute("ses_loc_id").toString();}
			if(session.getAttribute("ses_colaborador") != null){ses_colaborador = session.getAttribute("ses_colaborador").toString();}
			if(session.getAttribute("ses_cli_rut") != null){ses_cli_rut = session.getAttribute("ses_cli_rut").toString();}
			
			//Se recupera la session del cliente	
			long cliente_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());
            String invitado_id = "";
            if (session.getAttribute("ses_invitado_id") != null &&
                    !session.getAttribute("ses_invitado_id").toString().equals("")){
                invitado_id = session.getAttribute("ses_invitado_id").toString();
            }
			long cant_registros = 0;
			if( arg0.getParameter("registros") != null )
				cant_registros = Long.parseLong(arg0.getParameter("registros"));
			
			double cantidad 	= 0;
			double cantidad_old = -1;
			long carr_id 		= 0;
			long idProducto		= 0;
			List idsProductos 	= new ArrayList();
			
			for( int i = 0; i < cant_registros; i++ ) {	
			    if (arg0.getParameter("cantidad"+i) == null) {
			        continue;
			    }
				//Se recupera la cantidad
				cantidad = Double.parseDouble( arg0.getParameter("cantidad"+i).toString());

				// Se recupera la cantidad anterior
				if( arg0.getParameter("cantidad_old"+i) != null )
					cantidad_old = Double.parseDouble( arg0.getParameter("cantidad_old"+i).toString());
				
				if( cantidad_old == cantidad )
					continue;
				
				// Se recupera el ID del carro
				carr_id = Long.parseLong( arg0.getParameter("carr_id"+i).toString());
				
				// Se recupera el ID del producto
				if (arg0.getParameter("id_producto") != null) {
				    idProducto = Long.parseLong( arg0.getParameter("id_producto").toString());
				}
				// Instacia del bizdelegate
				BizDelegate biz = new BizDelegate();

				String nota = "";
				
				if( arg0.getParameter("nota"+i) != null )
					nota = arg0.getParameter("nota"+i);

                if (cantidad > 0) {
					this.getLogger().debug( "update car_id:" + carr_id + " nota:" + nota + " cantidad:" + cantidad );
					biz.carroComprasUpdateProducto(cliente_id, carr_id, cantidad, nota, invitado_id, null);
					idsProductos.add(""+idProducto);
				} else {
					this.getLogger().debug( "delete car_id:" + carr_id );
					biz.carroComprasDeleteProducto(cliente_id, carr_id, invitado_id);
					eliminaItemCarroCompra(session, cliente_id, carr_id, invitado_id, rb, l_torec, biz,  ses_loc_id,  ses_colaborador,  ses_cli_rut);
				}
			}
            
            if( arg0.getParameter("url") == null || arg0.getParameter("url").compareTo("")== 0 ) {
                // Recupera parámetro desde web.xml             
                if ( idsProductos.size() > 0 ) {
                    session.setAttribute("ids_prod_add", idsProductos);
                }               
            } else {
                getServletConfig().getServletContext().getRequestDispatcher( arg0.getParameter("url").toString() ).forward(arg0, arg1);
                return;
            }

		} catch (Exception e) {
			this.getLogger().error(e);
            e.printStackTrace();
            respuesta = "Ocurrió un error al modificar el producto.";
		}
        
        arg1.setContentType("text/xml");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("UTF-8");
        try {
            arg1.getWriter().write("<datos_objeto>");
            arg1.getWriter().write("<respuesta>" + respuesta + "</respuesta>");
            arg1.getWriter().write("</datos_objeto>");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
	}
	
	/**
     * 
     * @param session
     * @param cliente_id
     * @param carr_id
     * @param invitado_id
     * @param rb
     * @param l_torec
     * @param biz
     */
	public void eliminaItemCarroCompra(HttpSession session, long cliente_id,
			long carr_id, String invitado_id, ResourceBundle rb, List l_torec, BizDelegate biz, String ses_loc_id, String ses_colaborador, String ses_cli_rut) {
		try{
			int value = 0;
			long  total = 0;
			UtilsCarroCompra utils =  new UtilsCarroCompra();
			List fila = new ArrayList();
			List fila_dto = new ArrayList();
			IValueSet top = (IValueSet) session.getAttribute("top");
			List categorias =  (List) session.getAttribute("CATEGORIAS");
			List lista = (List) top.getDynamicValueSets("lista_carro");//.remove(carr_id);
			
			if(top.getVariable("total_carro")!= null)
				 total = Long.parseLong((String) top.getVariable("total_carro"));
			String obj = String.valueOf(carr_id);
			for (int i = 0; i < lista.size(); i++) {
				 IValueSet o = (IValueSet) lista.get(i);
				if(!o.getVariable("{id}").equals(obj) ){
					System.out.println("element:"+o.getVariable("{id}") +"." + o.getVariable("{nom_intermedia}"));
					fila.add(o);
					
				}else{
					long valor = Formatos.unFormatoPrecioFO(o.getVariable("{subtotal}").toString());
					total = total -  valor;
				}
			}
			List dto = (List) top.getDynamicValueSets("dto"); //session.getAttribute("dto");
			for (int i = 0; i < dto.size(); i++) {
				MiCarroDTO  element = (MiCarroDTO) dto.get(i);
				if(element != null ){
					String id = element.getId()+ " ";
					if(!obj.equals(id.trim())){
						 fila_dto.add(element);
					}
				}
			}
			//session.removeAttribute("dto");
			session.setAttribute("dto",fila_dto);
			top.getDynamicValueSets("lista_carro").clear();
			top.getDynamicValueSets("dto").clear();
			top.setDynamicValueSets("lista_carro", fila);
			top.setDynamicValueSets("dto", fila_dto);
			top.setVariable("total_carro", Long.toString(total));
			if(!(fila.size() > 0)){
				top.setVariable("{total_desc_tc}", Formatos.formatoPrecioFO(value));
				top.setVariable("{total_desc_tc_sf}", Formatos.formatoPrecioFO(value));
				top.setVariable("{total_desc}", Formatos.formatoPrecioFO(value));
				top.setVariable("{total_desc_sf}", Formatos.formatoPrecioFO(value));
				top.setVariable("{promo_desc}", String.valueOf(value));
				top.setVariable("{total}", Formatos.formatoPrecioFO(value) + "");
				top.setVariable("{cant_reg}", fila.size() + "");
				top.setVariable("total_carro", Long.toString(value));
			}
			 
			top = utils.getPromoMiCarro(rb, l_torec, fila_dto, biz, top, total, ses_loc_id, ses_colaborador, ses_cli_rut);
			top = utils.reAgrupaCategorias(categorias, fila, top, fila_dto); 
			session.setAttribute("top", top);
		}catch (Exception e) {
			logger.error("Error eliminaItemCarroCompra : " + carr_id + " : " + e.getMessage());
		}
     }
}