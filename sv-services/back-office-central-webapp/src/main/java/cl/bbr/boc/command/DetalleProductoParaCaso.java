package cl.bbr.boc.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.casos.dto.ProductoCasoDTO;
import cl.bbr.jumbocl.casos.utils.CasosUtil;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Retorna detalle de producto de un caso (Ajax)
 * 
 * @author imoyano
 */

public class DetalleProductoParaCaso extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo DetalleProductoParaCaso [AJAX]");

        long idProducto = Long.parseLong(req.getParameter("idProducto").toString());
        logger.debug("Id Producto: " + idProducto);

        BizDelegate bizDelegate = new BizDelegate();
        CasosUtil util = new CasosUtil();

        // ---- Productos ----
        ProductoCasoDTO producto = bizDelegate.getProductoById(idProducto);

        res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");

        res.getWriter().write("<datos_producto>");
        res.getWriter().write("<id_producto>" + producto.getIdProducto() + "</id_producto>");
        res.getWriter().write("<id_caso>" + producto.getIdCaso() + "</id_caso>");
        res.getWriter().write("<tipo_accion>" + util.frmTextoXml(producto.getTipoAccion()) + "</tipo_accion>");

        res.getWriter().write("<pp_descripcion>" + util.frmTextoXml(producto.getPpDescripcion()) + "</pp_descripcion>");
        res.getWriter().write("<pp_cantidad>" + util.frmTextoXml(producto.getPpCantidad()) + "</pp_cantidad>");
        res.getWriter().write("<pp_unidad>" + util.frmTextoXml(producto.getPpUnidad()) + "</pp_unidad>");
        res.getWriter().write("<comentario_boc>" + util.frmTextoXml(producto.getComentarioBOC()) + "</comentario_boc>");

        res.getWriter().write("<ps_descripcion>" + util.frmTextoXml(producto.getPsDescripcion()) + "</ps_descripcion>");
        res.getWriter().write("<ps_cantidad>" + util.frmTextoXml(producto.getPsCantidad()) + "</ps_cantidad>");
        res.getWriter().write("<ps_unidad>" + util.frmTextoXml(producto.getPsUnidad()) + "</ps_unidad>");
        res.getWriter().write("<comentario_bol>" + util.frmTextoXml(producto.getComentarioBOL()) + "</comentario_bol>");

        res.getWriter().write("<pickeador>" + util.frmTextoXml(producto.getPickeador()) + "</pickeador>");
        res.getWriter().write("<id_quiebre>" + producto.getQuiebre().getIdQuiebre() + "</id_quiebre>");
        res.getWriter().write("<id_responsable>" + producto.getResponsable().getIdObjeto() + "</id_responsable>");
        res.getWriter().write("<id_motivo>" + producto.getMotivo().getIdObjeto() + "</id_motivo>");
        
        res.getWriter().write("<precio>" + producto.getPrecio() + "</precio>");

        res.getWriter().write("</datos_producto>");

        logger.debug("Fin DetalleProductoParaCaso [AJAX]");
    }
}
