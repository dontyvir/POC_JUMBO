package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author Jean
 */
public class ViewDestacadoNewForm extends Command {
   private final static long serialVersionUID = 1;

   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      res.setCharacterEncoding("UTF-8");
      View salida = new View(res);

      String html = getServletConfig().getInitParameter("TplFile");
      html = path_html + html;

      TemplateLoader load = new TemplateLoader(html);
      ITemplate tem = load.getTemplate();
      IValueSet top = new ValueSet();

      BizDelegate bizDelegate = new BizDelegate();

      ArrayList locales = new ArrayList();
      List lLocales = bizDelegate.getLocales();

      for (int i = 0; i < lLocales.size(); i++) {
         IValueSet fila = new ValueSet();
         LocalDTO local = (LocalDTO) lLocales.get(i);
         fila.setVariable("{local_id}", String.valueOf(local.getId_local()));
         fila.setVariable("{local_codigo}", String.valueOf(local.getNom_local()));
         locales.add(fila);
      }

      top.setDynamicValueSets("LOCALES", locales);

      String result = tem.toString(top);

      salida.setHtmlOut(result);
      salida.Output();

   }

}