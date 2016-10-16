/*
 * Created on 15-abr-2010
 */
package cl.bbr.boc.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.dto.FOProductoDTO;
import cl.bbr.boc.service.CategoriasSevice;
import cl.bbr.boc.service.ProductosService;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.utils.DString;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.jumbo.common.dto.CategoriaMasvDTO;

/**
 * @author jdroguett
 */
public class MasvCategoriasEdit extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      res.setCharacterEncoding("UTF-8");
      View salida = new View(res);
      String html = getServletConfig().getInitParameter("TplFile");
      html = path_html + html;

      TemplateLoader load = new TemplateLoader(html);
      ITemplate tem = load.getTemplate();
      IValueSet top = new ValueSet();

      int categoriaId = Integer.parseInt(req.getParameter("categoriaId"));
      ProductosService productosService = new ProductosService();
      CategoriasSevice categoriasSevice = new CategoriasSevice();

      CategoriaMasvDTO categoria = categoriasSevice.getMasvCategoria(categoriaId);
      List productos = productosService.getMasvProductos(categoriaId);
      logger.debug(String.valueOf(productos.size()));

      List vistaProductos = vistaProductos(productos);

      top.setDynamicValueSets("PRODUCTOS", vistaProductos);
      top.setVariable("{categoria_id}", categoriaId + "");
      top.setVariable("{nombre}", categoria.getNombre() + "");
      top.setVariable("{activo_masv0}", (categoria.isActivoMasv() ? "" : "selected"));
      top.setVariable("{activo_masv1}", (categoria.isActivoMasv() ? "selected" : ""));
      top.setVariable("{activo_banner0}", (categoria.isActivoBanner() ? "" : "selected"));
      top.setVariable("{activo_banner1}", (categoria.isActivoBanner() ? "selected" : ""));
      top.setVariable("{fecha_inicio}", categoria.getFechaInicio() == null ? "" : Formatos.fecha(categoria.getFechaInicio()));
      top.setVariable("{fecha_termino}", categoria.getFechaTermino() == null ? "" : Formatos.fecha(categoria.getFechaTermino()));
      top.setVariable("{banner_principal}", categoria.getBannerPrincipal() == null ? "" : categoria.getBannerPrincipal() );
      top.setVariable("{banner_secundario1}", categoria.getBannerSecundario1() == null ? "" : categoria.getBannerSecundario1());
      top.setVariable("{banner_secundario2}", categoria.getBannerSecundario2() == null ? "" : categoria.getBannerSecundario2());

      String result = tem.toString(top);
      salida.setHtmlOut(result);
      salida.Output();
   }

   /**
    * @param productos
    * @return
    */
   private List vistaProductos(List productos) {
      List lista = new ArrayList();
      for (int i = 0; i < productos.size(); i++) {
         IValueSet v = new ValueSet();
         FOProductoDTO pro = (FOProductoDTO) productos.get(i);
         v.setVariable("{subcat}", pro.getCategoria().getNombre());
         v.setVariable("{local}", DString.abreviar(pro.getLocal().getNombre()));
         v.setVariable("{marca}", pro.getMarca().getNombre());
         v.setVariable("{producto}", pro.getDescripcion());
         v.setVariable("{sap}", pro.getCodSap());
         v.setVariable("{cantidad}", pro.getCantidad() + "");
         lista.add(v);
      }
      return lista;
   }

}
