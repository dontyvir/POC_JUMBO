/*
 * Created on 12-nov-2009
 */
package cl.bbr.bol.command.asistencia;

import java.text.SimpleDateFormat;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.dto.AsistenciaPickeadorDTO;
import cl.bbr.bol.service.PickeadorService;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 */
public class GrabarAsistencia extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      String sFecha = req.getParameter("fecha");
      int localId = (int)usr.getId_local();
      PickeadorService service = new PickeadorService();
      Enumeration asistencias = req.getParameterNames();
      while (asistencias.hasMoreElements()) {
         String asis = (String) asistencias.nextElement();
         if(asis.startsWith("asis")){
            String asistencia = req.getParameter(asis);
            asis = asis.substring(4);
            String[] ids = asis.split("_");
            int pickeadorId = Integer.parseInt(ids[0]);
            int asistenciaId =  ids.length > 1 ? Integer.parseInt(ids[1]) : 0;
            AsistenciaPickeadorDTO asistenciaPick = new AsistenciaPickeadorDTO();
            asistenciaPick.setId(asistenciaId);
            asistenciaPick.setPickeadorId(pickeadorId);
            asistenciaPick.setLocalId(localId);
            asistenciaPick.setAsistencia(asistencia);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            asistenciaPick.setFecha(sdf.parse(sFecha));
            service.updAsistencia(asistenciaPick);
         }
      }
      
      String exito = getServletConfig().getInitParameter("exito");
      res.sendRedirect(exito);
   }
}
