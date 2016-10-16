/*
 * Created on 14-abr-2010
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

import cl.bbr.boc.service.CategoriasSevice;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.jumbo.common.dto.CategoriaMasvDTO;

/**
 * @author jdroguett
 */
public class ViewMasvCategorias extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      res.setCharacterEncoding("UTF-8");
      View salida = new View(res);
      String html = getServletConfig().getInitParameter("TplFile");
      html = path_html + html;

      TemplateLoader load = new TemplateLoader(html);
      ITemplate tem = load.getTemplate();
      IValueSet top = new ValueSet();

      CategoriasSevice categoriasSevice = new CategoriasSevice();

      List categorias = categoriasSevice.getMasvCategorias();

      List vistaCategorias = vistaCategorias(categorias);

      top.setDynamicValueSets("CATEGORIAS", vistaCategorias);

      String result = tem.toString(top);
      salida.setHtmlOut(result);
      salida.Output();
   }

   /**
    * @param categorias
    * @return
    */
   private List vistaCategorias(List categorias) {
      List lista = new ArrayList();
      for (int i = 0; i < categorias.size(); i++) {
         CategoriaMasvDTO dto = (CategoriaMasvDTO) categorias.get(i);
         IValueSet cat = new ValueSet();
         cat.setVariable("{categoriaId}", dto.getId() + "");
         cat.setVariable("{nombre}", dto.getNombre());
         cat.setVariable("{activoMasv}", dto.isActivoMasv() ? "Activado" : "Desactivado");
         cat.setVariable("{activoBanner}", dto.isActivoBanner() ? "Activado" : "Desactivado");
         cat.setVariable("{fechaInicio}", dto.getFechaInicio() == null ? "" : Formatos.fecha(dto.getFechaInicio()));
         cat.setVariable("{fechaTermino}", dto.getFechaTermino() == null ? "" : Formatos.fecha(dto.getFechaTermino()));
         lista.add(cat);
      }
      return lista;
   }
}
