package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.pedidos.dto.ProductoCarruselDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agregar, Modificar y Eliminar un Marco
 * 
 * @author imoyano
 */

public class AddEditCarrusel extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo AddEditCarrusel");
        
        boolean nuevo = true;
        String msje = "";
        BizDelegate biz = new BizDelegate();
        
        String html = getServletConfig().getInitParameter("TplFile");
        
        if ( "DEL".equalsIgnoreCase(req.getParameter("accion")) ) {
            
            ProductoCarruselDTO oldProd = biz.getProductoCarruselById(Long.parseLong(req.getParameter("id_prod_carrusel")));
            String strLog = "Se eliminó el producto '" + oldProd.getNombre() + "' con ID " + oldProd.getIdProductoCarrusel();
            
            biz.deleteProductoCarruselById(Long.parseLong(req.getParameter("id_prod_carrusel")));
            msje = "El producto se eliminó correctamente";
            
            biz.addLogCarrusel(usr, strLog);
            
        } else {
        
            if ( !"0".equalsIgnoreCase(req.getParameter("id_prod_carrusel")) ) {
                nuevo = false; //Modificar un producto                
            }
            
            ProductoCarruselDTO prod = new ProductoCarruselDTO();
//+20120323coh
/*            prod.setImagen(req.getParameter("imagen"));
            prod.setNombre(req.getParameter("nombre"));
            String descr = req.getParameter("descripcion");            
            descr = descr.replaceAll("\r\n","<br>");
            prod.setDescripcion(descr);
            prod.setDescPrecio(req.getParameter("precio"));
            prod.setFcInicio(req.getParameter("fc_inicio"));
            prod.setFcTermino(req.getParameter("fc_termino"));
            prod.setConCriterio(req.getParameter("con_criterio"));
*/
/*            prod.setIdProductoFo( Long.parseLong(req.getParameter("sap")));*/
            prod.setIdCodigoSAP( req.getParameter("sap"));
            String descr = req.getParameter("descripcion");            
            descr = descr.replaceAll("\r\n","<br>");
            prod.setDescripcion(descr);
            prod.setDescPrecio(req.getParameter("precio"));
            prod.setFcInicio(req.getParameter("fc_inicio"));
            prod.setFcTermino(req.getParameter("fc_termino"));
            prod.setLinkDestino(req.getParameter("linkDestino"));
            prod.setImagen(req.getParameter("imagen"));

//-20120323coh
            if ( !nuevo ) {
                prod.setIdProductoCarrusel( Long.parseLong( req.getParameter("id_prod_carrusel") ));
            }
            
            long id = biz.addEditProductoCarrusel(prod);
            
            if ( nuevo ) {
//+20120323coh
//                biz.addLogCarrusel(usr, "Se creó el producto '" + prod.getNombre() + "' con ID " + id);
                biz.addLogCarrusel(usr, "Se creó el producto '" + prod.getIdCodigoSAP() + "' (SAP) con ID " + id);
//-20120323coh
                msje = "El producto se modificó correctamente";
            } else {
//+20120323coh
//                biz.addLogCarrusel(usr, "Se modificó el producto '" + prod.getNombre() + "' con ID " + prod.getIdProductoCarrusel());
                biz.addLogCarrusel(usr, "Se modificó el producto '" + prod.getIdCodigoSAP() + "' (SAP) con ID " + prod.getIdProductoCarrusel());
//-20120323coh
                msje = "El producto se agregó correctamente";
            }            
        }        
        res.sendRedirect(html + "?msje=" + msje);          

        logger.debug("Fin AddEditCarrusel");
    }
}
