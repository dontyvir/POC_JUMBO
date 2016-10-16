package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.service.CategoriasSevice;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agrega una Categoría Web para administrar sus banners minihome
 * @author BBRI
 */

public class AddBannerCatWeb extends Command {
    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
       res.setCharacterEncoding("UTF-8");
       
       int categoriaId = Integer.parseInt(req.getParameter("bca_cat_id"));
       CategoriasSevice categoriasSevice = new CategoriasSevice();

       try {
           categoriasSevice.addBannerCatWeb(categoriaId);
           res.sendRedirect("/JumboBOCentral/ViewBannersCategorias");
       } catch (Exception e){
           logger.debug("Controlando excepción: " + e.getMessage());
       }
    }
 }
