package cl.bbr.bol.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.common.dto.RegionDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Devuelve los datos de un pedido JumboVA (Ajax)
 * 
 * @author imoyano
 */

public class TraeDatosDePedidoJumboVA extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo TraeDatosDePedidoJumboVA [AJAX]");

        long rut = Long.parseLong(req.getParameter("rut").toString());
        String mensaje = "OK";
        
        BizDelegate biz = new BizDelegate();
        
		PedidoDTO pedido = new PedidoDTO();
        RegionDTO region = new RegionDTO();
        
        try {
            pedido = biz.getUltimoPedidoJumboVAByRut(rut);  
            if (pedido.getId_pedido() != 0) {
                region = biz.getRegionByComuna(pedido.getId_comuna());
                if (pedido.getPedidoExt().getMail() == null) {
                    try {
                        ClientesDTO cli = biz.getClienteByRut(rut);
                        pedido.getPedidoExt().setMail(cli.getEmail());
                    } catch (Exception e) {}                    
                }                
            } else {
                mensaje = "No existe información.";
            }
        } catch (Exception e) {
            mensaje = "No existe información.";
        }
        
        String mail = "@";
        if (pedido.getPedidoExt().getMail() != null && !pedido.getPedidoExt().getMail().equalsIgnoreCase("")) {
            mail = pedido.getPedidoExt().getMail();
        }
        
		res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");
		
        res.getWriter().write("<datos_caso>");
        res.getWriter().write("<mensaje>"+mensaje+"</mensaje>");
		res.getWriter().write("<nombre>"+pedido.getNom_cliente()+"</nombre>");
        res.getWriter().write("<direccion>"+pedido.getDir_tipo_calle()+ " " + pedido.getDir_calle() + " " + pedido.getDir_numero() + "</direccion>");
		res.getWriter().write("<id_region>"+region.getIdRegion()+"</id_region>");
        res.getWriter().write("<id_comuna>"+pedido.getId_comuna()+"</id_comuna>");		
        res.getWriter().write("<fono1>"+pedido.getTelefono()+"</fono1>");
        res.getWriter().write("<fono2>"+pedido.getTelefono2()+"</fono2>");
        res.getWriter().write("<mail>"+mail+"</mail>");
        res.getWriter().write("</datos_caso>");
		
        logger.debug("Fin TraeDatosDePedidoJumboVA [AJAX]");
    }
}
