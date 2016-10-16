/*
 * Created on 10-nov-2009
 */
package cl.bbr.bol.command.asociar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.service.PickeadorService;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 *  
 */
public class PickeadoresPorEncargadoUpd extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      int localId = (int) usr.getId_local();
      int encargadoId = Integer.parseInt(req.getParameter("encargadoId"));
      String[] sPickeadoresId = req.getParameterValues("pickeadoresId");
      int[] pickeadoresId = null;
      if (sPickeadoresId == null)
         //el servicio eliminará las asociaciones 
         pickeadoresId = new int[0];
      else {
         pickeadoresId = new int[sPickeadoresId.length];
         for (int i = 0; i < sPickeadoresId.length; i++) {
            pickeadoresId[i] = Integer.parseInt(sPickeadoresId[i]);
         }
      }

      PickeadorService service = new PickeadorService();
      service.updAsociacion(pickeadoresId, encargadoId, localId);

      String exito = getServletConfig().getInitParameter("exito");
      res.sendRedirect(exito);
   }
}
