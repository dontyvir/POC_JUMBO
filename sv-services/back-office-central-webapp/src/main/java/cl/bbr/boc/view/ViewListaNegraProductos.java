/*
 * Created on 08-jun-2009
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

import cl.bbr.boc.dto.SubrubroDTO;
import cl.bbr.boc.service.ProductosService;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 */
public class ViewListaNegraProductos extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      res.setCharacterEncoding("UTF-8");
      res.setContentType("text/html; charset=iso-8859-1");
      PrintWriter salida = res.getWriter();
      String html = getServletConfig().getInitParameter("TplFile");
      html = path_html + html;

      TemplateLoader load = new TemplateLoader(html);
      ITemplate tem = load.getTemplate();
      IValueSet top = new ValueSet();
      ProductosService serv = new ProductosService();
      List lista = serv.listaNegraProductos();
      
      List subrubros = new ArrayList();
      for (int i = 0; i < lista.size(); i++) {
         SubrubroDTO subrubro = (SubrubroDTO)lista.get(i);
         IValueSet fila = new ValueSet();
         fila.setVariable("{subrubro}", subrubro.getSubrubro());
         fila.setVariable("{precio_total}", subrubro.getPrecioTotal());
         subrubros.add(fila);
      }
      
      top.setDynamicValueSets("SUBRUBROS", subrubros);
      salida.print(tem.toString(top));
   }
}
