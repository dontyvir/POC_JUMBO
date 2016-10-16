package cl.bbr.fo.command;

import java.util.ResourceBundle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;


/**
 * Valida la hora de compra, para que el cliente no seleccione una ventana que se encuentra fuera de horario
 * 
 * @author imoyano
 * 
 */
public class ValidaHoraCompra extends Command {
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
   
    /* (sin Javadoc)
     * @see cl.bbr.fo.command.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        this.getLogger().debug("Comienzo ValidaHoraCompra [AJAX]");
        
        String mensajeSistema = "OK";
        String jDespacho = req.getParameter("id_jdespacho");
        String tipoDespacho = req.getParameter("tipo_despacho");
        BizDelegate biz = new BizDelegate();
        
        if ( tipoDespacho.equalsIgnoreCase("C") ) {
            HttpSession session = req.getSession();
            ResourceBundle rb = ResourceBundle.getBundle("fo");
            long cantProds = Long.parseLong(rb.getString("despachochart.productos.promedio"));
            if ( biz.getJornadaDespachoMayorCapacidad(Long.parseLong(session.getAttribute("ses_zona_id").toString()), jDespacho , cantProds) <= 0 ) {
                mensajeSistema = "Antes de continuar, debe seleccionar la ventana de despacho.";
            }
        } else {
            if ( !biz.verificaHoraCompra(Long.parseLong(jDespacho), tipoDespacho) ) {
                mensajeSistema = "Antes de continuar, debe seleccionar la ventana de despacho.";
            }    
        }
        res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");

        res.getWriter().write("<valida_jornadas>");
        res.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
        res.getWriter().write("</valida_jornadas>");
        
        this.getLogger().debug("Fin ValidaHoraCompra [AJAX]");   
        
    }
   
    
}