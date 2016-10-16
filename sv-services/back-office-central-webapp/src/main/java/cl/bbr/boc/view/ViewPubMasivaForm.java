/*
 * Created on 21-abr-2009
 *
 */
package cl.bbr.boc.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.dto.MotivoDespDTO;
import cl.bbr.boc.service.ProductosService;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 */
public class ViewPubMasivaForm extends Command {
   
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      req.setCharacterEncoding("UTF-8");
      res.setContentType("text/html; charset=iso-8859-1");
      PrintWriter out = res.getWriter();

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
      
      ProductosService serv = new ProductosService();
      List motivos = serv.getMotivosDespublicacion();

      top.setDynamicValueSets("LOCALES", locales);
      top.setDynamicValueSets("MOTIVOS", vistaMotivosDesp(motivos));

      String result = tem.toString(top);

      out.print(result);
      out.close();
   }
   
   private List vistaMotivosDesp(List motivos) {
      List lista = new ArrayList();
      IValueSet val = new ValueSet();
      val.setVariable("{motivo_id}", "-1");
      val.setVariable("{motivo_nombre}", "Seleccione");
      val.setVariable("{selected}", "selected");
      lista.add(val);
      for (int i = 0; i < motivos.size(); i++) {
         val = new ValueSet();
         MotivoDespDTO mot = (MotivoDespDTO) motivos.get(i);
         val.setVariable("{motivo_id}", mot.getId() + "");
         val.setVariable("{motivo_nombre}", mot.getMotivo());
         lista.add(val);
      }
      return lista;
   }

}
