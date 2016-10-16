package cl.bbr.boc.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.pedidos.dto.ListaTipoGrupoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agregar, Modificar y Eliminar un Marco
 * 
 * @author imoyano
 */

public class AddModDelMarco extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo AddModDelMarco");
        
        String html = getServletConfig().getInitParameter("TplFile");
        String accion = req.getParameter("accion");
        ListaTipoGrupoDTO marco = new ListaTipoGrupoDTO();        
        
        if ( accion.equalsIgnoreCase("NEW") || accion.equalsIgnoreCase("MOD") ) {
            marco.setDescripcion(req.getParameter("nombre_marco"));
            marco.setNombreArchivo(req.getParameter("nombre_flash"));
            String texto = req.getParameter("texto");            
            texto = texto.replaceAll("\r\n","<br>");
            marco.setTexto(texto);
            marco.setActivado(req.getParameter("activado"));
        } 
        if ( accion.equalsIgnoreCase("DEL") || accion.equalsIgnoreCase("MOD") ) {
            marco.setIdListaTipoGrupo(Integer.parseInt(req.getParameter("id_marco")));
        }
        
        BizDelegate biz = new BizDelegate();    
        
        if ( accion.equalsIgnoreCase("NEW") ) {
            biz.addMarco(marco);
            html += "?mensaje=Marco creado exitosamente";
            
        } else if (accion.equalsIgnoreCase("MOD") ) {
            biz.modMarco(marco);
            html += "?mensaje=Marco modificado exitosamente";
            
        } else if ( accion.equalsIgnoreCase("DEL") ) {
            List grupos = biz.getGruposByMarco(Integer.parseInt(req.getParameter("id_marco")));
            if ( grupos.size() == 0 ) {
                biz.delMarco(marco);
                html += "?mensaje=Marco eliminado exitosamente";
            } else {
                html += "?mensaje=<font color=red>El marco no puede ser eliminado</font>";
            }            
        }
        res.sendRedirect(html);            

        logger.debug("Fin AddModDelMarco");
    }
}
