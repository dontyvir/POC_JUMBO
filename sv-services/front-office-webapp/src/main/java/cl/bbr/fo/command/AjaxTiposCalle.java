package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.clientes.dto.TipoCalleDTO;

/**
 * cambia en session la comuna
 *  
 * @author rbelmar
 *  
 */
public class AjaxTiposCalle extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
        try {
            BizDelegate biz = new BizDelegate();
            PrintWriter out = arg1.getWriter();

            long tipo_calle_cli = Long.parseLong(arg0.getParameter("tipo_calle"));
            
            arg1.setHeader("Cache-Control", "no-cache");
            arg0.setCharacterEncoding("UTF-8");
            arg1.setContentType("text/html; charset=iso-8859-15");

            List registros = biz.tiposCalleGetAll();
            
            if (tipo_calle_cli > 0){
                out.println("<option value=\"0\">Seleccione</option> \n");
            }else{
                out.println("<option value=\"0\" selected>Seleccione</option> \n");
            }

            for (int i = 0; i < registros.size(); i++) {
                TipoCalleDTO tipoCalle = (TipoCalleDTO) registros.get(i);
                if (tipoCalle.getId() == tipo_calle_cli){
                    out.println("<option value=\"" + tipoCalle.getId() + "\" selected>" + tipoCalle.getNombre() + "</option> \n");
                }else{
                    out.println("<option value=\"" + tipoCalle.getId() + "\">" + tipoCalle.getNombre() + "</option> \n");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

	}
}