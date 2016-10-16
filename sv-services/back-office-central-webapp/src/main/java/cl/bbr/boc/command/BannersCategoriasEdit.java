/*
 * Created on 15-abr-2010
 */
package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.service.CategoriasSevice;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.jumbo.common.dto.CategoriaBannerDTO;

/**
 * @author carriagada
 */
public class BannersCategoriasEdit extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      res.setCharacterEncoding("UTF-8");
      View salida = new View(res);
      String html = getServletConfig().getInitParameter("TplFile");
      html = path_html + html;

      TemplateLoader load = new TemplateLoader(html);
      ITemplate tem = load.getTemplate();
      IValueSet top = new ValueSet();

      int categoriaId = Integer.parseInt(req.getParameter("categoriaId"));
      CategoriasSevice categoriasSevice = new CategoriasSevice();

      CategoriaBannerDTO categoria = categoriasSevice.getBannersCategoria(categoriaId);
      
      top.setVariable("{bca_cat_id}", categoriaId + "");
      top.setVariable("{nombre}", categoria.getNombre() + "");
      top.setVariable("{activo_banner0}", (categoria.getEstado() ? "" : "selected"));
      top.setVariable("{activo_banner1}", (categoria.getEstado() ? "selected" : ""));
      top.setVariable("{bca_fch_inicio}", categoria.getFechaInicio() == null ? "" : Formatos.fecha(categoria.getFechaInicio()));
      top.setVariable("{bca_fch_termino}", categoria.getFechaTermino() == null ? "" : Formatos.fecha(categoria.getFechaTermino()));
      top.setVariable("{bca_banner_principal}", categoria.getBannerPrincipal() == null ? "" : categoria.getBannerPrincipal() );
      
      String result = tem.toString(top);
      salida.setHtmlOut(result);
      salida.Output();
   }
}
