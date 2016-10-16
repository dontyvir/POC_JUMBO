package cl.bbr.boc.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.pedidos.dto.SustitutoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agregar y Modificar un datos para la configuracion de casos
 * Puede ser Tipo de Quiebre, Responsables o Jornadas (Ajax)
 * 
 * @author imoyano
 */

public class ObtieneSustitutosPorProducto extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ObtieneSustitutosPorProducto [AJAX]");
        
        BizDelegate bizDelegate = new BizDelegate();
        long idCliente          = 0;
        long idProducto         = 0;

        try {
            
            if (req.getParameter("id_cliente") != null) {
                idCliente = Long.parseLong(req.getParameter("id_cliente").toString());
            }
            if (req.getParameter("id_producto") != null) {
                idProducto = Long.parseLong(req.getParameter("id_producto").toString());
            }
            
            res.setContentType("text/html");
            res.setHeader("Cache-Control", "no-cache");
            res.setCharacterEncoding("UTF-8");
            
            SustitutoDTO criterioCliente = bizDelegate.getCriterioClientePorProducto(idCliente, idProducto);
            
            List criterios = criterios();
            
            res.getWriter().write("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">");
            
            for (int i=0; i < criterios.size(); i++) {
                SustitutoDTO sus = (SustitutoDTO) criterios.get(i);
                res.getWriter().write("<tr>");
                res.getWriter().write(" <td align=\"center\" height=\"25px\">");
                res.getWriter().write("  <input type=\"radio\" name=\"rbtn_sustituto\" id=\"rbtn_sustituto"+sus.getIdCriterio()+"\" value=\""+sus.getIdCriterio()+"\" " + checkea(criterioCliente.getIdCriterio(), sus.getIdCriterio()) + " />");
                res.getWriter().write(" </td>");
                res.getWriter().write(" <td align=\"left\">"+sus.getDescCriterio()+"</td>");
                res.getWriter().write(" <td align=\"left\">");
                if (sus.getIdCriterio() == 4) {
                    res.getWriter().write("   <input type=\"text\" name=\"desc_categoria\" id=\"desc_categoria\" value=\"" + decripcionCriterio(criterioCliente.getDescCriterio()) + "\" maxlength=\"250\" size=\"23\" />");
                }
                res.getWriter().write(" </td>");
                res.getWriter().write("</tr>");
            }
            
            res.getWriter().write("<tr>");
            res.getWriter().write(" <td align=\"center\" height=\"45px\"></td>");
            res.getWriter().write(" <td align=\"center\"><input type=\"button\" name=\"acp\" value=\"Aceptar\" style=\"width: 100px\" onclick=\"javascript:guardarProductoConSustituto();\" /></td>");
            res.getWriter().write(" <td align=\"center\"><input type=\"button\" name=\"cnc\" value=\"Cancelar\" style=\"width: 100px\" onclick=\"javascript:cerrarVentana();\" /></td>");
            res.getWriter().write("</tr>");
            
            res.getWriter().write("</table>"); 
                    
        } catch (BocException e) {
            e.printStackTrace();
        }

        logger.debug("Fin ObtieneSustitutosPorProducto [AJAX]");
    }
    
    private List criterios() {
        List criterios = new ArrayList();        
        SustitutoDTO sus = new SustitutoDTO();
        sus.setIdCriterio(1);
        sus.setDescCriterio("Criterio Jumbo");
        criterios.add(sus);
        sus = new SustitutoDTO();
        sus.setIdCriterio(2);
        sus.setDescCriterio("Misma marca distinto tamaño");
        criterios.add(sus);
        sus = new SustitutoDTO();
        sus.setIdCriterio(3);
        sus.setDescCriterio("Mismo tamaño distinta marca");
        criterios.add(sus);
        sus = new SustitutoDTO();
        sus.setIdCriterio(4);
        sus.setDescCriterio("Otro");
        criterios.add(sus);
        sus = new SustitutoDTO();
        sus.setIdCriterio(5);
        sus.setDescCriterio("No Sustituir");
        criterios.add(sus);
        return criterios;
    }

    private String decripcionCriterio(String descCriterio) {
        if ( descCriterio == null ) {
            return "";
        }
        return descCriterio;
    }

    private String checkea(long idCriterioCliente, long criterio) {
        if ( idCriterioCliente == criterio ) {
            return "checked";
        }
        return "";
    }
}
