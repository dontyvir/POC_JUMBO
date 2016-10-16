package cl.bbr.bol.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.utils.CasosUtil;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.common.dto.RegionDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Devuelve los datos de un caso (Ajax)
 * 
 * @author imoyano
 */

public class TraeDatosDeCaso extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo TraeDatosDeCaso [AJAX]");
        
        CasosUtil util = new CasosUtil();
        long idCaso = Long.parseLong(req.getParameter("nro_caso").toString());
        String mensaje = "OK";
        String mail = "@";
        
        BizDelegate bizDelegate = new BizDelegate();
        
		CasoDTO caso = new CasoDTO();
        PedidoDTO pedido = new PedidoDTO();
        RegionDTO region = new RegionDTO();
        
        try {
            caso = bizDelegate.getCasoByIdCaso(idCaso);
            if (caso.getLocal().getIdObjeto() != usr.getId_local()) {
                mensaje = "No existe el Caso para este Local.";
            } else {
                pedido = bizDelegate.getPedido(caso.getIdPedido());
                region = bizDelegate.getRegionByComuna(pedido.getId_comuna());
                try {
                    ClientesDTO cli = bizDelegate.getClienteByRut(caso.getCliRut());
                    if (cli.getEmail()!= null && !cli.getEmail().equalsIgnoreCase("")) {
                        mail = cli.getEmail();
                    }
                } catch (Exception e) {}
            }
        } catch (Exception e) {
            mensaje = "No existe información.";
        }
        
        
		res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");
		
        res.getWriter().write("<datos_caso>");
        res.getWriter().write("<mensaje>"+mensaje+"</mensaje>");
		res.getWriter().write("<id_pedido>"+caso.getIdPedido()+"</id_pedido>");
        res.getWriter().write("<rut>"+caso.getCliRut()+"</rut>");
        res.getWriter().write("<dv>"+util.frmTextoXml(caso.getCliDv())+"</dv>");
        res.getWriter().write("<nombre>"+util.frmTextoXml(caso.getCliNombre())+"</nombre>");
        res.getWriter().write("<direccion>"+util.frmTextoXml(caso.getDireccion())+"</direccion>");
		res.getWriter().write("<id_region>"+region.getIdRegion()+"</id_region>");
        res.getWriter().write("<id_comuna>"+pedido.getId_comuna()+"</id_comuna>");
		
        res.getWriter().write("<fono1>"+util.frmTextoXml(caso.getCliFonoCod1()+ " " + caso.getCliFonoNum1())+"</fono1>");
        res.getWriter().write("<fono2>"+util.frmTextoXml(caso.getCliFonoCod2()+ " " + caso.getCliFonoNum2())+"</fono2>");
        
        //res.getWriter().write("<bins>"+pedido.getCant_bins()+"</bins>");
        res.getWriter().write("<monto>"+Formatos.formatoNumero(pedido.getMonto())+"</monto>");
        res.getWriter().write("<local>"+caso.getLocal().getIdObjeto()+"</local>");
        res.getWriter().write("<id_cliente>"+pedido.getId_cliente()+"</id_cliente>");
        res.getWriter().write("<mail>"+mail+"</mail>");
        res.getWriter().write("</datos_caso>");
		
        logger.debug("Fin TraeDatosDeCaso [AJAX]");
    }
}
