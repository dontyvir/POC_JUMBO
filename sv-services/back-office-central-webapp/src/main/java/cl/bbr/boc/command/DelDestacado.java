/*
 * Created on 04-feb-2009
 */
package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 */
public class DelDestacado extends Command {
    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        String sId = req.getParameter("id");
        int id = Integer.parseInt(sId);
        BizDelegate biz = new BizDelegate();
        biz.delDestacado(id);
        String exito = getServletConfig().getInitParameter("exito");
        res.sendRedirect(exito);
    }
}
