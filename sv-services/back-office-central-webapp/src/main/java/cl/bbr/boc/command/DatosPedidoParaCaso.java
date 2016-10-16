package cl.bbr.boc.command;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.casos.dto.PedidoCasoDTO;
import cl.bbr.jumbocl.casos.utils.CasosConstants;
import cl.bbr.jumbocl.casos.utils.CasosUtil;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Retorna los datos de un pedido (Ajax)
 * 
 * @author imoyano
 */

public class DatosPedidoParaCaso extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo DatosPedidoParaCaso [AJAX]");

        long idPedido = Long.parseLong(req.getParameter("idPedido").toString());
        logger.debug("ID de Pedido: " + idPedido);

        BizDelegate bizDelegate = new BizDelegate();
        CasosUtil util = new CasosUtil();

        // ---- datos del pedido ----
        PedidoCasoDTO pedido = bizDelegate.getPedidoById(idPedido);
        List casos = bizDelegate.getCasosByOp(idPedido);

        res.setContentType("text/xml"); //("application/x-www-form-urlencoded");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8"); //("UTF-8");

        res.getWriter().write("<datos_pedido>");
        if (pedido.isExistePedido()) {
            res.getWriter().write("<mensaje>-</mensaje>");
        } else {
            res.getWriter().write("<mensaje>No existen datos para esa OP</mensaje>");
        }
        res.getWriter().write("<mensaje_op>"+ mensajeDeOp(casos) +"</mensaje_op>");
        res.getWriter().write("<id_pedido>" + pedido.getIdPedido() + "</id_pedido>");
        res.getWriter().write("<id_local>" + pedido.getIdLocal() + "</id_local>");
        res.getWriter().write("<fecha_pedido>" + util.frmFechaXml(pedido.getFechaPedido()) + "</fecha_pedido>");
        res.getWriter().write("<direccion>" + util.frmTextoXml(pedido.getDireccion()) + "</direccion>");
        res.getWriter().write("<comuna>" + util.frmTextoXml(pedido.getComuna()) + "</comuna>");
        res.getWriter().write("<fecha_despacho>" + util.frmFechaXml(pedido.getFechaDespacho()) + "</fecha_despacho>");
        res.getWriter().write("<cli_rut>" + util.frmNumeroXml(pedido.getCliRut()) + "</cli_rut>");
        res.getWriter().write("<cli_dv>" + util.frmTextoXml(pedido.getCliDv()) + "</cli_dv>");
        res.getWriter().write("<cli_nombre>" + util.frmTextoXml(pedido.getCliNombre()) + "</cli_nombre>");
        res.getWriter().write("<cli_fon_cod1>" + util.frmTextoXml(pedido.getCliFonCod1()) + "</cli_fon_cod1>");
        res.getWriter().write("<cli_fon_num1>" + util.frmTextoXml(pedido.getCliFonNum1()) + "</cli_fon_num1>");
        res.getWriter().write("<cli_fon_cod2>" + util.frmTextoXml(pedido.getCliFonCod2()) + "</cli_fon_cod2>");
        res.getWriter().write("<cli_fon_num2>" + util.frmTextoXml(pedido.getCliFonNum2()) + "</cli_fon_num2>");
        res.getWriter().write("<cli_fon_cod3>" + util.frmTextoXml(pedido.getCliFonCod3()) + "</cli_fon_cod3>");
        res.getWriter().write("<cli_fon_num3>" + util.frmTextoXml(pedido.getCliFonNum3()) + "</cli_fon_num3>");
        res.getWriter().write("<cli_num_compras>" + util.frmTextoXml(String.valueOf(bizDelegate.getNroComprasByCliente(pedido.getCliRut()))) + "</cli_num_compras>");
        res.getWriter().write("<cli_num_casos>" + util.frmTextoXml(String.valueOf(bizDelegate.getNroCasosByCliente(pedido.getCliRut()))) + "</cli_num_casos>");
        res.getWriter().write("<id_usu_fono>" + pedido.getIdUsuFonoCompra() + "</id_usu_fono>");
        res.getWriter().write("<usu_fono_nombre>" + util.frmTextoXml(pedido.getUsuFonoCompraNombre()) + "</usu_fono_nombre>");
        res.getWriter().write("</datos_pedido>");

        logger.debug("Fin DatosPedidoParaCaso [AJAX]");
    }

    /**
     * Metodo para armar el mensaje de casos con la misma OP
     * @param casos
     * @return parte del mensaje
     */
    private String mensajeDeOp(List casos) {
        String msg = "";
        if (casos.size() == 0) {
            return "-";
        } else if (casos.size() == 1) {
            msg = CasosConstants.MSJ_CASOS_OP.replaceAll("@casos",casos.get(0).toString());
        } else {
            String m = "";
            for (int i=0; i<casos.size(); i++) {
                m += casos.get(i); 
                if (i == (casos.size()-2)) {
                    m += " y ";
                    
                } else if (i == (casos.size()-1)) {
                    m += "";
                    
                } else {
                    m += ", ";
                    
                }
            }
            msg = CasosConstants.MSJ_CASOS_OPS.replaceAll("@casos",m);      
        }
        
        return msg;
    }
}
