/*
 * Created on 20-may-2010
 */
package cl.bbr.boc.command;

import java.math.BigDecimal;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.service.ProductosService;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 */
public class PreciosUpdate extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      res.setCharacterEncoding("UTF-8");
      res.setHeader("Cache-Control", "no-cache");
      
      String codsap = req.getParameter("codsap");
      Enumeration enu = req.getParameterNames();
      ProductosService ser = new ProductosService();

      while (enu.hasMoreElements()) {
         String name = (String) enu.nextElement();
         if (name.startsWith("precio_")) {
            String[] ids = name.substring(7).split("_");
            int id = Integer.parseInt(ids[0]);
            int localId = Integer.parseInt(ids[1]);
            BigDecimal precio = new BigDecimal(req.getParameter("precio_" + id + "_" + localId));
            String bloqueo = req.getParameter("bloqueo_" + id + "_" + localId);
            ser.updatePrecio(usr, id, localId, precio, bloqueo != null);
         }
      }
      req.getSession().setAttribute("msg", "Precios actualizados correctamente");
      res.sendRedirect("PreciosEdit?codsap=" + codsap);
   }
}
