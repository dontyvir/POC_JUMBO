package cl.bbr.boc.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.casos.utils.CasosConstants;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Elimina un producto de un caso (Ajax)
 * 
 * @author imoyano
 */

public class DelDatoConfigDeCaso extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo DelDatoConfigDeCaso [AJAX]");
        
        String mensajeSistema = "";

        long idObjeto	= Long.parseLong(req.getParameter("id_objeto").toString());
        String tipo 	= req.getParameter("tipo_eliminado");

        logger.debug("idObjeto	: " + idObjeto);
        logger.debug("tipo		: " + tipo);
       
        BizDelegate bizDelegate = new BizDelegate();

        try {
            
            if (tipo.equalsIgnoreCase("Q")) {
                bizDelegate.delQuiebre(idObjeto);
                mensajeSistema = CasosConstants.MSJ_DEL_QUIEBRE_EXITO;                    
                
            } else if (tipo.equalsIgnoreCase("R")) {
                bizDelegate.delResponsable(idObjeto);
                mensajeSistema = CasosConstants.MSJ_DEL_RESPONSABLE_EXITO;                
                
            } else if (tipo.equalsIgnoreCase("M")) {
                bizDelegate.delMotivo(idObjeto);
                mensajeSistema = CasosConstants.MSJ_DEL_MOTIVO_EXITO;                
                
            } else if (tipo.equalsIgnoreCase("J")) {
                bizDelegate.delJornadaDeCaso(idObjeto);
                mensajeSistema = CasosConstants.MSJ_DEL_JORNADA_EXITO;
                
            }

        } catch (BocException e) {
            e.printStackTrace();
            if (tipo.equalsIgnoreCase("Q")) {
                mensajeSistema = CasosConstants.MSJ_DEL_QUIEBRE_ERROR;                    
            } else if (tipo.equalsIgnoreCase("R")) {
                mensajeSistema = CasosConstants.MSJ_DEL_RESPONSABLE_ERROR;
            } else if (tipo.equalsIgnoreCase("J")) {
                mensajeSistema = CasosConstants.MSJ_DEL_JORNADA_ERROR;
            } else if (tipo.equalsIgnoreCase("M")) {
                mensajeSistema = CasosConstants.MSJ_DEL_MOTIVO_ERROR;
            }
        }

        res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");

        res.getWriter().write("<datos_producto>");
        res.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
        res.getWriter().write("</datos_producto>");

        logger.debug("Fin DelDatoConfigDeCaso [AJAX]");
    }
}
