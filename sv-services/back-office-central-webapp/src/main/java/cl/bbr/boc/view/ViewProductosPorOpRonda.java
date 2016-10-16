package cl.bbr.boc.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Muestra un listado de productos asociados a una OP y Ronda
 * 
 * @author imoyano
 */
public class ViewProductosPorOpRonda extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ViewProductosPorOpRonda Execute");

        //Variables
        long idPedido = 0;
        req.setCharacterEncoding("UTF-8");
        res.setContentType("text/html; charset=iso-8859-1");
        
        //Se recupera la salida para el servlet
        PrintWriter out = res.getWriter();
       
        //View salida = new View(res);
        String html = getServletConfig().getInitParameter("TplFile");
        html = path_html + html;
        logger.debug("Template: " + html);

        TemplateLoader load = new TemplateLoader(html);
        ITemplate tem = load.getTemplate();
        IValueSet top = new ValueSet();

        //Sacamos la info de la pagina
        if ( req.getParameter("id_op") != null ) {
            idPedido = Long.parseLong(req.getParameter("id_op").toString());
        }

        BizDelegate biz = new BizDelegate();
        List allProds = biz.getProductosTodasTrxByPedido(idPedido);
        ArrayList prods = new ArrayList();
        for (int i = 0; i < allProds.size(); i++) {
            ProductosPedidoDTO producto = (ProductosPedidoDTO) allProds.get(i);
            if ( producto.getId_producto() != 0 ) {
                IValueSet fila = new ValueSet();
                fila.setVariable("{id_prod}", ""+producto.getId_producto());
                fila.setVariable("{descripcion}", producto.getDescripcion());
                fila.setVariable("{precio}", ""+ Formatos.formatoPrecio(producto.getPrecio()));
                fila.setVariable("{precio_sf}", ""+ (int)producto.getPrecio());
                prods.add(fila);                
            }
        }
        top.setDynamicValueSets("PRODUCTOS", prods);
        logger.debug("Fin ViewProductosPorOpRonda Execute");
        String result = tem.toString(top);
        out.print(result);
    }
}
