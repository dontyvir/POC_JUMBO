package cl.bbr.fo.command;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.clientes.dto.MiCarroDTO;

/**
 * Valida los productos que estan en el carro de compras
 * 
 * @author imoyano
 * 
 */
public class ValidaCarroCompras extends Command {
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
   
    /* (sin Javadoc)
     * @see cl.bbr.fo.command.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        this.getLogger().debug("Comienzo ValidaCarroCompras [AJAX]");
        
        String mensajeSistema = "OK";
        String mensajeCantidad = "";
        String local = "";
        long cliente = 0;
        int prodSinStock = 0;
        
        //INICIO INDRA 09-10-2012
        String borrarProductos = req.getParameter("borrarProductos");
        String reemplazarProductos = req.getParameter("reemplazarProductos");
        ResourceBundle rb = ResourceBundle.getBundle("fo");
        String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
        this.getLogger().debug("Template:" + pag_form);
        TemplateLoader load = new TemplateLoader(pag_form);
        ITemplate tem = load.getTemplate();
        
        //INDRA 2012-11-13
        String txtCabecera = rb.getString("txtDespacho");
        //INDRA 2012-11-13
        IValueSet top = new ValueSet();

        List productosConflicto = new ArrayList();
        //FIN INDRA 09-10-2012
        
        HttpSession session = req.getSession();
        if (((String)req.getParameter("local")) != null && (!((String)req.getParameter("local")).equals("0"))){
            local = (String)req.getParameter("local");
        } else if ( session.getAttribute("ses_loc_id") != null ) {
            local = session.getAttribute("ses_loc_id").toString();
        }
        if ( session.getAttribute("ses_cli_id") != null ) {
            cliente = Long.parseLong( session.getAttribute("ses_cli_id").toString() );
        }
        BizDelegate biz = new BizDelegate();
        String idSession = null;
        if (session.getAttribute("ses_invitado_id") != null)
            idSession = session.getAttribute("ses_invitado_id").toString();
        List listaCarro = biz.carroComprasPorCategorias( cliente, local, idSession);
        
        //INICIO INDRA 2012-05-11
        for( int i = 0; i < listaCarro.size(); i++ ) {			
            MiCarroDTO producto = (MiCarroDTO) listaCarro.get(i);
			if (!producto.tieneStock()) {
			    prodSinStock++;
                productosConflicto.add(producto);
            }else if (producto.getEstadoProducto().compareToIgnoreCase("A")!= 0){
            	productosConflicto.add(producto);
            } else if (producto.getEstadoPreciosLocales().compareToIgnoreCase("A") != 0) {
            	productosConflicto.add(producto);
		}
		}
        
        if (prodSinStock > 0) {
            if (prodSinStock == 1) {
                mensajeSistema = prodSinStock + " producto en tu carro que no se encuentra disponible";
                mensajeCantidad = " el producto";
            } else {
                mensajeSistema = prodSinStock + " productos en tu carro que no se encuentran disponibles";
                mensajeCantidad = " los productos";
            }
        }
        // FIN INDRA 2012-05-11
        
        //INDRA 09-10-2012
        
        
        List productos = new ArrayList();
        String productosList ="";
        if (productosConflicto != null && productosConflicto.size() > 0) {
            for(int x = 0; x < productosConflicto.size(); x++){
            	MiCarroDTO carro = new MiCarroDTO();
            	carro = (MiCarroDTO) productosConflicto.get(x);
            	IValueSet producto = new ValueSet();
            	producto.setVariable("{producto}", carro.getTipo_producto());
            	producto.setVariable("{marca}", carro.getNom_marca());
    			productos.add(producto);
    			productosList = productosList + carro.getPro_id() + ",";
            }
            top.setDynamicValueSets("PRODUCTOS_DESPUBLICADOS", productos);
            top.setDynamicValueSets("PRODUCTOS_SIN_STOCK", productos);
            
            if (productos.size() > 0){
            	productosList = productosList.substring(0, productosList.length() - 1);
            }
            //INDRA 2012-11-13
            top.setVariable("{productosList_carro}", productosList + "");
            top.setVariable("{existen_despublicados_carro}", productosConflicto.size() + "");
            top.setVariable("{borrarProductos_carro}", borrarProductos);
            top.setVariable("{reemplazarProductos_carro}", reemplazarProductos);
            top.setVariable("{txtCabecera}", txtCabecera);
            //INDRA 2012-11-13
        }
        
        
        String result = tem.toString(top);
		
        // INICIO INDRA 09-10-2012
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");

        if (productosConflicto == null || productosConflicto.size() == 0){
        	res.setContentType("text/xml");
        res.getWriter().write("<datos_producto>");
        res.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
        if (prodSinStock > 0)
            res.getWriter().write("<cantidad>" + mensajeCantidad + "</cantidad>");
        res.getWriter().write("</datos_producto>");
        } else {
        	res.setContentType("text/plain");
        	res.getWriter().write(result);
        }
        // FIN INDRA 22-10-2012
        
        this.getLogger().debug("Fin ValidaCarroCompras [AJAX]");   
        
    }
}