/*
 * Created on 21-abr-2010
 */
package cl.bbr.boc.command;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.service.CategoriasSevice;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.jumbo.common.dto.CategoriaBannerDTO;

/**
 * @author jdroguett
 */
public class BannersCategoriasUpd extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      res.setCharacterEncoding("UTF-8");

      String sCategoriaId = req.getParameter("categoria_id");
      int categoriaId = Integer.parseInt(sCategoriaId);
      String sActivoBanner = req.getParameter("bca_estado");
      String banner = req.getParameter("banner");
      String sFechaInicio = req.getParameter("bca_fch_inicio");
      String sFechaTermino = req.getParameter("bca_fch_termino");

      CategoriaBannerDTO cat = new CategoriaBannerDTO();
      cat.setId(categoriaId);
      CategoriasSevice service = new CategoriasSevice();

      if (sActivoBanner != null) {
         if (sActivoBanner.equals("1")) 
             cat.setEstado(true);
         else
             cat.setEstado(false);
      } else if (banner != null) {
         String nombre = req.getParameter("nombre");
         if (banner.equals("bb1")) {
            cat.setBannerPrincipal(nombre);
         }
      } else if (sFechaInicio != null) {
         Date fecha = Formatos.toDate(sFechaInicio);
         cat.setFechaInicio(fecha);
      } else if (sFechaTermino != null) {
         Date fecha = Formatos.toDate(sFechaTermino);
         cat.setFechaTermino(fecha);
      }

      service.updBannersCategoria(cat);
   }
}
