/*
 * Created on 13-may-2009
 *
 */
package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.dto.SustitutoDTO;
import cl.bbr.boc.service.SustitutosService;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 *  
 */
public class UpdSustituto extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      res.setCharacterEncoding("UTF-8");
      View salida = new View(res);

      String cbarra = req.getParameter("cbarra");
      String dpickingId = req.getParameter("dpickingId");
      String productoId = req.getParameter("productoId");
      String precio = req.getParameter("precio");
      String descripcionSust = req.getParameter("descripcionSust");
      logger.debug(cbarra);
      logger.debug(dpickingId);
      logger.debug(productoId);
      logger.debug(precio);
      logger.debug(descripcionSust);

      try {
         SustitutosService ser = new SustitutosService();
         SustitutoDTO sustituto = new SustitutoDTO();
         sustituto.setBarra(cbarra);
         sustituto.setDetPickingId(Integer.parseInt(dpickingId));
         sustituto.setProductoId(Integer.parseInt(productoId));
         sustituto.setPrecio(Integer.parseInt(precio));
         sustituto.setDescripcion(descripcionSust);
         int n = ser.updateSustituto(sustituto);
         if (n == 0)
            salida.setHtmlOut("No se ha podido modificar el c&oacute;digo de barra.");
         else
            salida.setHtmlOut("C&oacute;digo de barra actualizado.");
      } catch (Exception e) {
         e.printStackTrace();
         salida.setHtmlOut("No se ha podido modificar el c&oacute;digo de barra." + e.getMessage());
      }
      salida.Output();
   }
}
