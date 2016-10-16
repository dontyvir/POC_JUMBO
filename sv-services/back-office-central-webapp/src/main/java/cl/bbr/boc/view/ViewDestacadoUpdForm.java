/*
 * Created on 05-feb-2009
 *
 */
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
import cl.bbr.boc.dto.DestacadoDTO;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 *  
 */
public class ViewDestacadoUpdForm extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      res.setCharacterEncoding("UTF-8");
      View salida = new View(res);

      String html = getServletConfig().getInitParameter("TplFile");
      html = path_html + html;

      TemplateLoader load = new TemplateLoader(html);
      ITemplate tem = load.getTemplate();
      IValueSet top = new ValueSet();

      String sId = req.getParameter("id");
      int id = Integer.parseInt(sId);

      BizDelegate biz = new BizDelegate();

      List destacadoLocales = biz.getDestacadoLocales(id);

      ArrayList locales = new ArrayList();
      List lLocales = biz.getLocales();
      for (int i = 0; i < lLocales.size(); i++) {
         IValueSet fila = new ValueSet();
         LocalDTO local = (LocalDTO) lLocales.get(i);
         fila.setVariable("{local_id}", String.valueOf(local.getId_local()));
         fila.setVariable("{local_codigo}", String.valueOf(local.getNom_local()));
         fila.setVariable("{local_checked}", "");

         //son pocos locales a fuerza bruta
         for (int j = 0; j < destacadoLocales.size(); j++) {
            cl.bbr.boc.dto.LocalDTO localDTO = (cl.bbr.boc.dto.LocalDTO) destacadoLocales.get(j);
            if (localDTO.getId() == local.getId_local()) {
               fila.setVariable("{local_checked}", "checked=\"checked\"");
               break;
            }
         }
         locales.add(fila);
      }
      top.setDynamicValueSets("LOCALES", locales);

      DestacadoDTO destacado = biz.getDestacado(id);
      top.setVariable("{destacado_id}", destacado.getId() + "");
      top.setVariable("{descripcion}", destacado.getDescripcion());
      top.setVariable("{fecha_ini}", Formatos.fecha(destacado.getFechaHoraIni()));
      top.setVariable("{hora_ini}", Formatos.hora(destacado.getFechaHoraIni()));
      top.setVariable("{fecha_fin}", Formatos.fecha(destacado.getFechaHoraFin()));
      top.setVariable("{hora_fin}", Formatos.hora(destacado.getFechaHoraFin()));
      top.setVariable("{imagen}", destacado.getImagen());

      String result = tem.toString(top);
      salida.setHtmlOut(result);
      salida.Output();
   }
}
