package cl.bbr.fo.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.clientes.dto.CriterioClienteSustitutoDTO;


/**
 * Guarda un criterio de sustitucion para un producto de un cliente en particular
 * 
 * @author imoyano
 * 
 */
public class SaveSustituto extends Command {
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
   
    /* (sin Javadoc)
     * @see cl.bbr.fo.command.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        this.getLogger().debug("Comienzo SaveSustituto [AJAX]");
        
        String mensajeSistema = "OK";
        long idCliente = 0;
        CriterioClienteSustitutoDTO criterio = new CriterioClienteSustitutoDTO();
        HttpSession session = req.getSession();
        if ( session.getAttribute("ses_cli_id") != null ) {
            idCliente = Long.parseLong( session.getAttribute("ses_cli_id").toString() );
        }
        if ( req.getParameter("id_producto") != null ) {
            criterio.setIdProducto( Long.parseLong( req.getParameter("id_producto").toString() ));
        }
        if ( req.getParameter("id_criterio") != null ) {
            criterio.setIdCriterio( Long.parseLong( req.getParameter("id_criterio").toString() ));
        }
        if ( req.getParameter("desc_criterio") != null ) {
            criterio.setSustitutoCliente( req.getParameter("desc_criterio").toString() );
        }
        BizDelegate biz = new BizDelegate();
        try {
        	CriterioClienteSustitutoDTO cri = biz.criterioSustitucionByClienteProducto(idCliente, criterio.getIdProducto());
            if ( cri.getIdCriterio() == 0 ) {
                biz.addSustitutoCliente(idCliente, criterio);
            } else {            
                biz.setCriterioCliente(idCliente, criterio);
            }
        } catch (Exception e) {
            mensajeSistema = "Error al guardar la información.";
        }
        
        res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");

        res.getWriter().write("<datos_producto>");
        res.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
        res.getWriter().write("</datos_producto>");
        
        this.getLogger().debug("Fin SaveSustituto [AJAX]");   
        
    }
}