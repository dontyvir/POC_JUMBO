/*
 * Created on 21-abr-2009
 *
 */
package cl.bbr.boc.command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.boc.service.ProductosService;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.utils.DString;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 *  
 */
public class AddPubMasiva extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      req.setCharacterEncoding("UTF-8");
      res.setContentType("text/html; charset=iso-8859-1");

      String opcion = req.getParameter("publicar");
      String productos = req.getParameter("productos");
      String locales = req.getParameter("locales");
      String evitarPubDes = req.getParameter("evitarPubDes");
      String observacion = req.getParameter("observacion");
      String sMotivoId = req.getParameter("motivoId");
      int motivoId = Integer.parseInt(sMotivoId);
      
      
      
      if (productos.length() == 0)
         throw new BocException("Debe ingresar ids FO de los productos a publicar, despublicar, setar con stock o sin stock");
      if (locales.length() == 0)
         throw new BocException("Debe seleccionar uno o más locales");

      String e = "\\d+";
      Pattern p = Pattern.compile(e);
      Matcher m = p.matcher(productos);
      List lProductos = new ArrayList();
      while (m.find()) {
         lProductos.add(m.group());
      }

      /*
       * Se debería hacer un orden de las capas: disminuirlar y ordenar desde donde se obtienen los datos y ordenar los
       * DTO. Por ahora, para esto me saltaré el delegate y los control cuando pueda.
       */
      ProductosService serv = new ProductosService();
      BizDelegate biz = new BizDelegate();
      int cant = 0;
      if (opcion.equals("si")) {
         // sólo de deben publicar los productos con sector de picking
         List prodsConSector = serv.conSectorPicking(lProductos);
         cant = serv.publicar("(" + locales + ")", prodsConSector, usr.getLogin());
         lProductos.removeAll(prodsConSector);
         serv.evitarPubDes(prodsConSector, "on".equals(evitarPubDes));
      } else if (opcion.equals("no")){
         cant = serv.despublicar("(" + locales + ")", lProductos, usr.getLogin(), motivoId, observacion);
         serv.evitarPubDes(lProductos, "on".equals(evitarPubDes));
      } else if (opcion.equals("constock")) {
         cant = serv.conStock("(" + locales + ")", lProductos, usr.getLogin()); 
      } else if (opcion.equals("sinstock")) {
         cant = serv.sinStock("(" + locales + ")", lProductos, usr.getLogin());
      }

      
      

      String[] lLocales = locales.split(",");
      String accion = "";
      if (opcion.equals("si"))
          accion = "publicados: ";
      else if (opcion.equals("no"))
          accion = "despublicados: ";
      else if (opcion.equals("constock"))
          accion = "con stock: ";
      else if (opcion.equals("sinstock"))
          accion = "sin stock: ";
      StringBuffer sb = new StringBuffer("<br /> Cantidad de registros " + accion + cant);
      if (opcion.equals("si"))
         sb.append("<br /> Los sgtes ids no poseen sector de picking: " + DString.join(lProductos) + "<br /><br />");

      for (int i = 0; i < lLocales.length; i++) {
         List ids = serv.idsQueNoExisten(lLocales[i], lProductos);
         if (ids.size() > 0) {
            LocalDTO localDTO = biz.getLocalById(Long.parseLong(lLocales[i]));
            sb.append("<br /> En el local " + localDTO.getNom_local() + " no existen los sgtes ids: "
                  + DString.join(ids) + "<br /><br />");
         }
      }
      View view = new View(res);
      view.setHtmlOut(sb.toString());
      view.Output();
   }
}
