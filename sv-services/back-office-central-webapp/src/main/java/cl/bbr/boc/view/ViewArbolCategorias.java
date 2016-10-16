/*
 * Created on 23-abr-2009
 *
 */
package cl.bbr.boc.view;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import net.sf.json.JSONObject;
import cl.bbr.boc.service.CategoriasSevice;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.jumbo.common.dto.Categoria;

/**
 * @author jdroguett
 *  
 */
public class ViewArbolCategorias extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      res.setCharacterEncoding("UTF-8");
      View salida = new View(res);
      CategoriasSevice categoriasSevice = new CategoriasSevice();

      Categoria root = categoriasSevice.root();
      JSONObject json = new JSONObject();
      json.put("raiz", root);
      String var = json.toString();
      var = var.substring(8, var.length() - 1);

      String son = req.getParameter("json");
      if ("si".equals(son)) {
         salida.setHtmlOut(var);
         logger.debug(var);
      } else {

         String html = getServletConfig().getInitParameter("TplFile");
         html = path_html + html;

         TemplateLoader load = new TemplateLoader(html);
         ITemplate tem = load.getTemplate();
         IValueSet top = new ValueSet();


         ArrayList arbol = new ArrayList();

         IValueSet fila = new ValueSet();
         fila.setVariable("{arbol_json}", var);
         arbol.add(fila);
         top.setDynamicValueSets("ARBOL", arbol);

         String result = tem.toString(top);
         salida.setHtmlOut(result);
      }
      salida.Output();
      logger.debug(var);
   }
}
