/*
 * Created on 13-may-2009
 *
 */
package cl.bbr.boc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.dto.SustitutoDTO;
import cl.bbr.boc.service.SustitutosService;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 *  
 */
public class ViewSustitutosBarra extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      res.setCharacterEncoding("UTF-8");
      View salida = new View(res);

      String barra = req.getParameter("barra");
      String dpickingId = req.getParameter("dpickingId");
      String localId = req.getParameter("localId");
      String descripcionSust = req.getParameter("descripcion");

      SustitutosService ser = new SustitutosService();
      SustitutoDTO sustituto = ser.sustituto(localId, barra);
      if (sustituto != null) {

         String html = getServletConfig().getInitParameter("TplFile");
         html = path_html + html;
         TemplateLoader load = new TemplateLoader(html);
         ITemplate tem = load.getTemplate();
         IValueSet top = new ValueSet();

         top.setVariable("{local}", sustituto.getLocal());
         top.setVariable("{precio}", sustituto.getPrecio() + "");
         top.setVariable("{barra}", sustituto.getBarra());
         top.setVariable("{descripcion}", sustituto.getDescripcion());
         top.setVariable("{descripcion_sust}", descripcionSust);
         top.setVariable("{dpickingId}", dpickingId);
         top.setVariable("{producto_id}", sustituto.getProductoId() + "");
         

         String result = tem.toString(top);
         salida.setHtmlOut(result);
      }else{
         salida.setHtmlOut("C&oacute;digo de barra no existe en el sistema.");
      }
      salida.Output();
   }
}
