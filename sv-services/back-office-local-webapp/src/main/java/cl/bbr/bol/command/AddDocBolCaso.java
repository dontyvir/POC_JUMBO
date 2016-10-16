package cl.bbr.bol.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.casos.dto.CasosDocBolDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Verifica si el usuario puede cambiar el estado (Ajax)
 * 
 * @author imoyano
 */
public class AddDocBolCaso extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }    

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo AddDocBolCaso [AJAX]");
        
        long idCaso	= 0;
        String comentario = "";
        String cooler1 = "";
        String cooler2 = "";
        String cooler3 = "";
        String cooler4 = "";
        String cooler5 = "";
        String cooler6 = "";
        String bin1 = "";
        String bin2 = "";
        String bin3 = "";
        String bin4 = "";
        String bin5 = "";
        String bin6 = "";
        
        if (req.getParameter("id_caso") != null) {
            idCaso = Long.parseLong(req.getParameter("id_caso").toString());
        }        
        if (req.getParameter("comentario") != null) {
            comentario = req.getParameter("comentario").toString();
        }
        if (req.getParameter("cooler1") != null) {
            cooler1 = req.getParameter("cooler1").toString();
        }
        if (req.getParameter("cooler2") != null) {
            cooler2 = req.getParameter("cooler2").toString();
        }
        if (req.getParameter("cooler3") != null) {
            cooler3 = req.getParameter("cooler3").toString();
        }
        if (req.getParameter("cooler4") != null) {
            cooler4 = req.getParameter("cooler4").toString();
        }
        if (req.getParameter("cooler5") != null) {
            cooler5 = req.getParameter("cooler5").toString();
        }
        if (req.getParameter("cooler6") != null) {
            cooler6 = req.getParameter("cooler6").toString();
        }
        if (req.getParameter("bin1") != null) {
            bin1 = req.getParameter("bin1").toString();
        }
        if (req.getParameter("bin2") != null) {
            bin2 = req.getParameter("bin2").toString();
        }
        if (req.getParameter("bin3") != null) {
            bin3 = req.getParameter("bin3").toString();
        }
        if (req.getParameter("bin4") != null) {
            bin4 = req.getParameter("bin4").toString();
        }
        if (req.getParameter("bin5") != null) {
            bin5 = req.getParameter("bin5").toString();
        }
        if (req.getParameter("bin6") != null) {
            bin6 = req.getParameter("bin6").toString();
        }
        
        logger.debug("idCaso: " + idCaso);
        logger.debug("usuario: " + usr.getLogin());
        logger.debug("comentario: " + comentario);
        
        comentario = comentario.replaceAll("\r\n","<br>");
        
        logger.debug("comentario: " + comentario);
        
        logger.debug("cooler1: " + cooler1);
        logger.debug("cooler2: " + cooler2);
        logger.debug("cooler3: " + cooler3);
        logger.debug("cooler4: " + cooler4);
        logger.debug("cooler5: " + cooler5);
        logger.debug("cooler6: " + cooler6);
        logger.debug("bin1: " + bin1);
        logger.debug("bin2: " + bin2);
        logger.debug("bin3: " + bin3);
        logger.debug("bin4: " + bin4);
        logger.debug("bin5: " + bin5);
        logger.debug("bin6: " + bin6);
        
        BizDelegate bizDelegate = new BizDelegate();
        CasosDocBolDTO caso = new CasosDocBolDTO(idCaso,usr.getLogin(),comentario,cooler1,cooler2,cooler3,cooler4,cooler5,cooler6,bin1,bin2,bin3,bin4,bin5,bin6);
        long idDocBol = bizDelegate.addDocBolCaso(caso);
        
        res.setContentType("text/xml"); //("application/x-www-form-urlencoded");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");
                
        res.getWriter().write("<permiso_perfil>");
        res.getWriter().write("<mensaje>OK</mensaje>");    
        res.getWriter().write("</permiso_perfil>");
		
        logger.debug("Fin AddDocBolCaso [AJAX]");
        
    }
}
