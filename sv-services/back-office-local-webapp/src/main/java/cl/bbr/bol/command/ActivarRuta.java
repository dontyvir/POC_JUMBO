package cl.bbr.bol.command;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.pedidos.dto.LogRutaDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Activar una Ruta (Ajax)
 * 
 * @author imoyano
 */

public class ActivarRuta extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ActivarRuta [AJAX]");
        
        String msg = "OK";
        BizDelegate biz = new BizDelegate();
        
        long idRuta = Long.parseLong(req.getParameter("id_ruta"));
        
        if ( idRuta == 0 ) {
            throw new Exception("Faltan datos necesarios para una Ruta");
        }
        
        try {            
            RutaDTO ruta = biz.getRutaById(idRuta);
            if ( ruta.getEstado().getId_estado() != Constantes.ESTADO_RUTA_EN_PREPARACION ) {
                msg = "Acción no permitida por el estado de la Ruta";
                
            } else {
                boolean todosPagados = true;
                //verificamos el estado de los pedidos
                List peds = biz.getPedidosByRuta(idRuta);
                for (int i=0; i < peds.size(); i++) {
                    PedidoDTO p = (PedidoDTO) peds.get(i);
                    if ( p.getId_estado() != Constantes.ID_ESTAD_PEDIDO_PAGADO ) {
                        todosPagados = false;
                    } 
                }                
                if ( todosPagados ) {
                    //Cambiamos el estado de los pedidos
                    for (int i=0; i < peds.size(); i++) {
                        PedidoDTO p = (PedidoDTO) peds.get(i);
                        biz.setCambiaEstadoDespacho(p.getId_pedido(),Constantes.ID_ESTAD_PEDIDO_EN_DESPACHO,usr.getLogin(),"[BOL] Se cambia estado de OP automáticamente, por que se activó ruta " + idRuta);
                        
                        LogRutaDTO log1 = new LogRutaDTO(idRuta, ruta.getEstado(), usr, "[BOL] OP " + p.getId_pedido() + " queda 'En despacho'.");
                        biz.addLogRuta( log1 );
                    }
                    
                    LogRutaDTO log2 = new LogRutaDTO(idRuta, ruta.getEstado(), usr, "[BOL] Todas las OP's de la ruta quedan 'En despacho'.");
                    biz.addLogRuta( log2 );
                                       
                    biz.setEstadoRuta(Constantes.ESTADO_RUTA_EN_RUTA, idRuta);
                    
                    LogRutaDTO log3 = new LogRutaDTO(idRuta, ruta.getEstado(), usr, "[BOL] En Ruta. ");
                    biz.addLogRuta( log3 );
                    
                } else {
                    msg = "Acción no permitida por el estado de un pedido";
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
		
        logger.debug("Fin ActivarRuta [AJAX]");
    }
}
