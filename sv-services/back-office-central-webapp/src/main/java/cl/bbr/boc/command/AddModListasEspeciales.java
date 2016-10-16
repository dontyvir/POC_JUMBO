package cl.bbr.boc.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.pedidos.dto.UltimasComprasDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agregar y Modificar un datos para la configuracion de casos
 * Puede ser Tipo de Quiebre, Responsables o Jornadas (Ajax)
 * 
 * @author imoyano
 */

public class AddModListasEspeciales extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo AddModListasEspeciales [AJAX]");
        
        String mensajeSistema	= "OK";
        String accion = "";
        BizDelegate biz = new BizDelegate();
        long idLista = 0;
        long idGrupo = 0;
        
        if (req.getParameter("accion") != null) {
            accion = req.getParameter("accion").toString();
        }
        if (req.getParameter("id_lista") != null) {
            idLista = Long.parseLong( req.getParameter("id_lista").toString() );
        }
        if (accion.equalsIgnoreCase("DEL")) {
            if (req.getParameter("id_grupo") != null) {
                idGrupo = Long.parseLong( req.getParameter("id_grupo").toString() );
            }
            biz.delListaEspecial(idLista,idGrupo);
            res.setContentType("text/xml");
            res.setHeader("Cache-Control", "no-cache");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write("<datos_objeto>");
            res.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
            res.getWriter().write("</datos_objeto>");
        
        } else {            
            String grps = "";
            String nameLista = "";
            if (req.getParameter("grps") != null) {
                grps = req.getParameter("grps").toString();
            }
            if (req.getParameter("name_lista") != null) {
                nameLista = req.getParameter("name_lista").toString();
            }
            String[] grupos = grps.split("-=-");
            UltimasComprasDTO lista = new UltimasComprasDTO();
            lista.setId(idLista);
            lista.setNombre(nameLista);
            biz.modLista(lista, grupos);
            String paramUrl = getServletConfig().getInitParameter("TplFile");
            res.sendRedirect(paramUrl);            
        }
        logger.debug("Fin AddModListasEspeciales [AJAX]");
    }
}
