package cl.bbr.bol.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.LogRutaDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agregar Pedido a Ruta de Despacho (Ajax)
 * 
 * @author imoyano
 */

public class AgregarPedidoARuta extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo AgregarPedidoARuta [AJAX]");
        
        String msg = "OK";
        BizDelegate biz = new BizDelegate();
        
        String pds  = req.getParameter("pedidos").toString();
        long idRuta = Long.parseLong(req.getParameter("id_ruta"));
        
        if ( idRuta == 0 || pds.length() == 0 ) {
            throw new Exception("Faltan datos necesarios para una Ruta");
        }
        
        try {            
            RutaDTO ruta = biz.getRutaById(idRuta);
            if ( ruta.getEstado().getId_estado() != Constantes.ESTADO_RUTA_EN_PREPARACION ) {
                msg = "Acción no permitida por el estado de la Ruta";
                
            } else {            
                //Agregamos los pedidos a la ruta
                String[] pedidos = pds.split("-=-");
                
                String pedidosAgregados = "";
                String sep = "";
                for ( int i=0; i < pedidos.length; i++ ) {
                    try {
                        if ( biz.addPedidoRuta(Long.parseLong(pedidos[i]), idRuta) == 1 ) {
                            //agregamos al log
                            pedidosAgregados += (sep + pedidos[i]);
                            sep = ",";                        
                            LogPedidoDTO logp = new LogPedidoDTO(Long.parseLong(pedidos[i]), usr.getLogin(), "[BOL] Pedido agregado a la Ruta: " + idRuta);
                            biz.addLogPedido(logp);
                        }                            
                    } catch (Exception e) {}                
                }
                if ( pedidosAgregados.length() > 0 ) {
                    //Agregamos al Log de la ruta los pedidos agregados
                    LogRutaDTO log1 = new LogRutaDTO(idRuta, ruta.getEstado(), usr, "[BOL] Se agregaron a Ruta los pedidos: " + pedidosAgregados);
                    biz.addLogRuta( log1 );
                } else {
                    msg = "No se agregó a Ruta.";
                }
            }                        
            
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        
		res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");
		
        res.getWriter().write("<datos_trx>");
        res.getWriter().write("<mensaje>" + msg + "</mensaje>");
        res.getWriter().write("</datos_trx>");
		
        logger.debug("Fin AgregarPedidoARuta [AJAX]");
    }
}
