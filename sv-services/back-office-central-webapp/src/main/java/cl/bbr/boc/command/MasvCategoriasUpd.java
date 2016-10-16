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
import cl.jumbo.common.dto.CategoriaMasvDTO;

/**
 * @author jdroguett
 */
public class MasvCategoriasUpd extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      res.setCharacterEncoding("UTF-8");

      String sCategoriaId = req.getParameter("categoria_id");
      int categoriaId = Integer.parseInt(sCategoriaId);
      String sActivoMasv = req.getParameter("activo_masv");
      String sActivoBanner = req.getParameter("activo_banner");
      String banner = req.getParameter("banner");
      String sFechaInicio = req.getParameter("fecha_inicio");
      String sFechaTermino = req.getParameter("fecha_termino");

      CategoriaMasvDTO cat = new CategoriaMasvDTO();
      cat.setId(categoriaId);
      CategoriasSevice service = new CategoriasSevice();

      if (sActivoMasv != null) {
         int activoMasv = Integer.parseInt(sActivoMasv);
         cat.setActivoMasv(activoMasv == 1);
      } else if (sActivoBanner != null) {
         int activoBanner = Integer.parseInt(sActivoBanner);
         cat.setActivoBanner(activoBanner == 1);
      } else if (banner != null) {
         String nombre = req.getParameter("nombre");
         if (banner.equals("bb1")) {
            cat.setBannerPrincipal(nombre);
         } else if (banner.equals("bb2")) {
            cat.setBannerSecundario1(nombre);
         } else if (banner.equals("bb3")) {
            cat.setBannerSecundario2(nombre);
         }
      } else if (sFechaInicio != null) {
         Date fecha = Formatos.toDate(sFechaInicio);
         cat.setFechaInicio(fecha);
      } else if (sFechaTermino != null) {
         Date fecha = Formatos.toDate(sFechaTermino);
         cat.setFechaTermino(fecha);
      }

      service.updCategoriaMasv(cat);
   }
}
