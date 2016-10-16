/*
 * Created on 20-may-2010
 */
package cl.bbr.boc.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import cl.bbr.boc.dto.BOPrecioDTO;
import cl.bbr.boc.dto.BOProductoDTO;
import cl.bbr.boc.service.ProductosService;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 */
public class PreciosEdit extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
       res.setCharacterEncoding("UTF-8");
       res.setHeader("Cache-Control", "no-cache");
       PrintWriter salida = res.getWriter();
       String html = path_html + "precios/edit.html";
       String layout = path_html + "precios/layout.html";
       ITemplate template = (new TemplateLoader(layout)).getTemplate();
       ITemplate temHtml = (new TemplateLoader(html)).getTemplate();
       //pagina
       String codsap = req.getParameter("codsap");
       ProductosService serv = new ProductosService();
       List productos = serv.getProductosBO(codsap);
       IValueSet top = vistaProductos(productos);
       top.setVariable("{msg}", req.getSession().getAttribute("msg"));
       req.getSession().setAttribute("msg", "");
       top.setVariable("{codsap}", codsap);
       String result = temHtml.toString(top);
       //end pagina
       
       IValueSet yield = new ValueSet();
       yield.setVariable("{yield}", result);
       salida.print(template.toString(yield));
   }
   
   private IValueSet vistaProductos(List productos){
      IValueSet top = new ValueSet();
      List lista = new ArrayList();
      for (int i = 0; i < productos.size(); i++) {
         BOProductoDTO pro = (BOProductoDTO)productos.get(i);
         IValueSet p = new ValueSet();
         p.setVariable("{id}", pro.getId()+"");
         p.setVariable("{descripcion}", pro.getDescripcion());
         p.setVariable("{unidad}", pro.getUnidad());
         List precios = new ArrayList();
         for (int j = 0; j < pro.getPrecios().size(); j++) {
            BOPrecioDTO pre = (BOPrecioDTO)pro.getPrecios().get(j);
            IValueSet pr = new ValueSet();
            pr.setVariable("{id}", pro.getId()+"");
            pr.setVariable("{local_id}", pre.getLocal().getId()+"");
            pr.setVariable("{local_nom}", pre.getLocal().getNombre());
            pr.setVariable("{precio}", pre.getPrecio());
            pr.setVariable("{bloqueado}", pre.isBloqueado() ? "checked=\"checked\"" :"");
            precios.add(pr);
         }
         p.setDynamicValueSets("PRECIOS", precios);
         lista.add(p);
      }
      top.setDynamicValueSets("PRODUCTOS", lista);
      return top;
   }
}
