package cl.bbr.bol.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.FacturasDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoExtDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agrega Pedido Externo tipo Caso o Jumbo VA (Ajax)
 * 
 * @author imoyano
 */

public class AgregaPedidoExterno extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo AgregaPedidoExterno [AJAX]");
        
        BizDelegate biz = new BizDelegate();
        String tipoPedido = req.getParameter("tipo_pedido").toString(); //origen
        PedidoExtDTO pedExt = new PedidoExtDTO();
        PedidoDTO ped = new PedidoDTO();
        
        ped.setOrigen(tipoPedido);
        
        if (tipoPedido.equalsIgnoreCase(Constantes.ORIGEN_CASOS_CTE)) {
            // TIPO PEDIDO CASO
            pedExt.setNroGuiaCaso(Long.parseLong(req.getParameter("nro_caso").toString()));
            
            CasoDTO caso = biz.getCasoByIdCaso(Long.parseLong(req.getParameter("nro_caso").toString()));
            if (caso.getLocal().getIdObjeto() != usr.getId_local()) {
                throw new Exception("Caso no corresponde al local.");
            }
            ped.setId_local(caso.getLocal().getIdObjeto());
            
        } else {
            // TIPO PEDIDO JUMBO VA
            pedExt.setNroGuiaCaso(Long.parseLong(req.getParameter("nro_guia").toString()));
            pedExt.setTipoJumboVA(req.getParameter("tipo_jumbo_va"));                      
            ped.setId_local(usr.getId_local());            
        }
        
        
        ped.setRut_cliente(Long.parseLong(req.getParameter("rut").toString()));
        ped.setDv_cliente(req.getParameter("dv").toString());
        
        ClientesDTO cli = new ClientesDTO();
        try {
            cli = biz.getClienteByRut(Long.parseLong(req.getParameter("rut").toString()));
            ped.setId_cliente(cli.getId_cliente());    
        } catch (Exception e) {
            //Si no existe 
            ped.setId_cliente(0);    
        }
        
        ped.setNom_cliente(req.getParameter("nombre").toString());
        ped.setDir_calle(req.getParameter("direccion").toString());
        ped.setIndicacion(req.getParameter("indicacion").toString());
        ped.setId_comuna(Long.parseLong(req.getParameter("comuna").toString()));
        ped.setTelefono(req.getParameter("fono_fijo").toString());
        ped.setTelefono2(req.getParameter("fono_celu").toString());
        ped.setId_zona(Long.parseLong(req.getParameter("zona").toString()));
        ped.setFdespacho(req.getParameter("fc_despacho").toString());
        ped.setId_jdespacho(Long.parseLong(req.getParameter("jdespacho").toString()));
        ped.setCant_bins(Integer.parseInt(req.getParameter("bins").toString()));
        pedExt.setBinsDescripcion(req.getParameter("bins2").toString());
        ped.setTipo_doc(req.getParameter("tipo_doc").toString());
        //ped.setNum_doc(Integer.parseInt(req.getParameter("boleta").toString()));
        pedExt.setDocumentos(documentos(req, usr));
        ped.setMonto(Double.parseDouble(req.getParameter("monto").toString()));
        pedExt.setMail(req.getParameter("mail").toString());
        
        ped.setId_estado(8); //Quedan en estado Pagado
        ped.setPedidoExt(pedExt);
        
        long newPedido = biz.addPedidoExt(ped);
        
        LogPedidoDTO logp = new LogPedidoDTO(newPedido, usr.getLogin(), "[BOL] Pedido tipo " + Formatos.getDescripcionOrigenPedido(tipoPedido) + " creado.");
        biz.addLogPedido(logp);
        
		res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");
		
        res.getWriter().write("<datos_trx>");
        res.getWriter().write("<mensaje>OK</mensaje>");
        res.getWriter().write("</datos_trx>");
		
        logger.debug("Fin AgregaPedidoExterno [AJAX]");
    }

    /**
     * @param req
     * @return
     */
    private List documentos(HttpServletRequest req, UserDTO usr) {
        List documentos = new ArrayList();        
        String[] docs = req.getParameter("boleta").split("-=-");
        for ( int i=0; i < docs.length; i++ ) {
            if ( docs[i].length() > 0 ) {
                FacturasDTO doc = new FacturasDTO();
                doc.setNum_doc(Long.parseLong(docs[i]));
                doc.setLogin(usr.getLogin());
                documentos.add(doc);
            }
        }
        return documentos;
    }
}
