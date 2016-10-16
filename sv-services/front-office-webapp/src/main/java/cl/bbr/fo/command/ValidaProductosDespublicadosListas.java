/*
 * Creado el 24-sep-2012
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;

/**
 * @author Sebastian
 *
 */
public class ValidaProductosDespublicadosListas extends Command {


	protected void execute(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

        ResourceBundle rb = ResourceBundle.getBundle("fo");

        try {
            // Recupera la sesión del usuario
            HttpSession session = req.getSession();
            req.setCharacterEncoding("UTF-8");
            resp.setContentType("text/xml;");
            
            int acordeonConDatos = 0;

            // Se recupera la salida para el servlet
            PrintWriter out = resp.getWriter();

            // Recupera pagina desde web.xml
            String lightBox1 = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("lightBox1");
            this.getLogger().debug("Template:" + lightBox1);
            TemplateLoader load = new TemplateLoader(lightBox1);
            ITemplate tem = load.getTemplate();
            
            IValueSet top = new ValueSet();

            Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());
            String txtCabecera = "";
            String idInvitado = "";
            String strIdLista = "";
            int pos = 0;
            int idLista = 0;
            //+
            if ( req.getParameter("idLista") != null) {
            	strIdLista = req.getParameter("idLista").trim();
            	pos = strIdLista.indexOf("-");
            	idLista = Integer.parseInt(strIdLista.substring(0, pos));
            } else {
            	this.getLogger().debug("idLista null");
            }
           
            if (session.getAttribute("ses_invitado_id") != null &&
                    !session.getAttribute("ses_invitado_id").toString().equals("")){
            	idInvitado = session.getAttribute("ses_invitado_id").toString();
            }
            
            txtCabecera = rb.getString("txtListas");
            // Instacia del bizdelegate
            BizDelegate biz = new BizDelegate();

            SimpleDateFormat sdf = new java.text.SimpleDateFormat(Formatos.DATE);
            
            List listaProductos = new ArrayList();
			
            //String[] idProductos = req.getParameter("ids").split(",");
            String borrarProductos = req.getParameter("borrarProductos");
            String reemplazarProductos = req.getParameter("reemplazarProductos");
            
			//for (int x = 0; x< idProductos.length; x++){
			//	listaProductos.add(idProductos[x]);
			//}
			long idCliente = Long.parseLong((String)session.getAttribute("ses_cli_id"));
			int idLocal = Integer.parseInt(session.getAttribute("ses_loc_id").toString());
			
			//Recupera Lista de Productos despublicados
//			List lista = (List)biz.getProductosDespubOrSinStock(idCliente, listaProductos, idLocal);
			List lista = biz.getProductosSinStockDespublicadosPorLista(idCliente, idLista, idLocal);
            
            //Seteo de productos despublicados
            IValueSet despublicados = new ValueSet();
            List productos = new ArrayList();
            Boolean existenDespublicados = new Boolean(false);
            String listaDespublicados = "";
            if (lista != null && lista.size() > 0) {
	            for(int x = 0; x < lista.size(); x++){
	            	ProductoDTO prod = new ProductoDTO();
	            	prod = (ProductoDTO)lista.get(x);
	            	
	            	IValueSet producto = new ValueSet();
	            	producto.setVariable("{producto}", prod.getTipo_producto());
	            	producto.setVariable("{marca}", prod.getMarca());
	    			productos.add(producto);
	    			listaDespublicados = listaDespublicados + prod.getPro_id() + ",";
	    			biz.carroComprasDeleteProductoxId(idCliente,prod.getPro_id(), idInvitado);
	            }
	            existenDespublicados = new Boolean(true);
	            top.setDynamicValueSets("PRODUCTOS_DESPUBLICADOS", productos);
	            top.setDynamicValueSets("PRODUCTOS_SIN_STOCK", productos);
	            
            }
           String productosList ="";
            if (listaDespublicados.length() > 0){
            	productosList = listaDespublicados.substring(0, listaDespublicados.length() - 1);
            }
            
            top.setVariable("{productosList_carro}", productosList + "");
            top.setVariable("{existen_despublicados_carro}", productos.size() + "");
            top.setVariable("{borrarProductos_carro}", borrarProductos);
            top.setVariable("{reemplazarProductos_carro}", reemplazarProductos);
            top.setVariable("{txtCabecera}", txtCabecera);
            String result = tem.toString(top);

            out.print(result);

        } catch (Exception e) {
            this.getLogger().error(e);
            e.printStackTrace();
            throw new CommandException(e);
        }
    }

}
