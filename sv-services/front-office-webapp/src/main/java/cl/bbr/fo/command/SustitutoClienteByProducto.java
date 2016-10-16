package cl.bbr.fo.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.clientes.dto.CriterioClienteSustitutoDTO;


/**
 * Trae el criterio de sustitucion del cliente para un producto en particular
 * 
 * @author imoyano
 * 
 */
public class SustitutoClienteByProducto extends Command {
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
   
    /* (sin Javadoc)
     * @see cl.bbr.fo.command.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        this.getLogger().debug("Comienzo SustitutoClienteByProducto [AJAX]");
        
        String mensajeSistema = "OK";
        long idCliente = 0;
        long idProducto = 0;
        CriterioClienteSustitutoDTO criterio = new CriterioClienteSustitutoDTO();
        
        HttpSession session = req.getSession();
        if ( session.getAttribute("ses_cli_id") != null ) {
            idCliente = Long.parseLong( session.getAttribute("ses_cli_id").toString() );
        }
        if ( req.getParameter("id_producto") != null ) {
            idProducto = Long.parseLong( req.getParameter("id_producto").toString() );
        }
        BizDelegate biz = new BizDelegate();
        try {
            criterio = biz.criterioSustitucionByClienteProducto(idCliente, idProducto);            
        } catch (Exception e) {
            mensajeSistema = "Error al buscar la información.";
        }
        
        res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");

        res.getWriter().write("<datos_producto>");
        res.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
        res.getWriter().write("<id_producto>" + idProducto + "</id_producto>");
        res.getWriter().write("<id_criterio>" + criterio.getIdCriterio() + "</id_criterio>");
        if ( criterio.getDescripcion() == null || criterio.getDescripcion().equalsIgnoreCase("") ) {
            res.getWriter().write("<des_criterio>Ej: marca, sabor</des_criterio>");
        } else {
            res.getWriter().write("<des_criterio>" + criterio.getDescripcion() + "</des_criterio>");    
        }
        res.getWriter().write("</datos_producto>");
        
        this.getLogger().debug("Fin SustitutoClienteByProducto [AJAX]");   
        
    }
}