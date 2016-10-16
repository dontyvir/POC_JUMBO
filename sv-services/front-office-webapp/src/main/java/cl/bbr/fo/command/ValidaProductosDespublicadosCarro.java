/*
 * Creado el 24-oct-2012
 *
 * INDRA COMPANY
 * 
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
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class ValidaProductosDespublicadosCarro extends Command{

	/* (sin Javadoc)
	 * @see cl.bbr.fo.command.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {

        ResourceBundle rb = ResourceBundle.getBundle("fo");

        try {
            // Recupera la sesión del usuario
            HttpSession session = arg0.getSession();
            arg0.setCharacterEncoding("UTF-8");
            arg1.setContentType("text/xml;");
            
            int acordeonConDatos = 0;

            // Se recupera la salida para el servlet
            PrintWriter out = arg1.getWriter();

            // Recupera pagina desde web.xml
            String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
            this.getLogger().debug("Template:" + pag_form);
            TemplateLoader load = new TemplateLoader(pag_form);
            ITemplate tem = load.getTemplate();
            
            IValueSet top = new ValueSet();

            Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());

            // Instacia del bizdelegate
            BizDelegate biz = new BizDelegate();

            SimpleDateFormat sdf = new java.text.SimpleDateFormat(Formatos.DATE);
            
            List listaProductos = new ArrayList();
			
            String[] idProductos = arg0.getParameter("ids").split(",");
            String borrarProductos = arg0.getParameter("borrarProductos");
            String reemplazarProductos = arg0.getParameter("reemplazarProductos");
            String txtCabecera = "";
			for (int x = 0; x< idProductos.length; x++){
				listaProductos.add(idProductos[x]);
			}
			long idCliente = Long.parseLong((String)session.getAttribute("ses_cli_id"));
			int idLocal = Integer.parseInt(session.getAttribute("ses_loc_id").toString());
			//INDRA 2012-11-13
			txtCabecera = rb.getString("txtListas");
			//INDRA 2012-11-13
			//Recupera Lista de Productos despublicados
			List lista = (List)biz.getProductosDespubOrSinStock(idCliente, listaProductos, idLocal);

            
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
            //INDRA 2012-11-13
            top.setVariable("{txtCabecera}", txtCabecera);
            //INDRA 2012-11-13
            String result = tem.toString(top);

            out.print(result);

        } catch (Exception e) {
            this.getLogger().error(e);
            e.printStackTrace();
            throw new CommandException(e);
        }
    }
}