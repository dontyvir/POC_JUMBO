/*
 * Created on 13-may-2009
 *
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
import cl.bbr.boc.dto.DetallePedidoDTO;
import cl.bbr.boc.service.SustitutosService;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 *  
 */
public class ViewSustitutosPedido extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      res.setCharacterEncoding("UTF-8");
      String sPedidoId = req.getParameter("pedidoId");
      int pedidoId = Integer.parseInt(sPedidoId);

      SustitutosService ser = new SustitutosService();
      List lista = ser.detallePedido(pedidoId);

      String html = getServletConfig().getInitParameter("TplFile");
      html = path_html + html;
      TemplateLoader load = new TemplateLoader(html);
      ITemplate tem = load.getTemplate();
      IValueSet top = new ValueSet();

      List vista = vista(lista);
      View salida = new View(res);
      String result = "Este pedido no tiene sustitutos no reconocidos";
      if (vista.size() > 0) {
         top.setDynamicValueSets("DETALLE", vista);
         result = tem.toString(top);
      }
      salida.setHtmlOut(result);
      salida.Output();
   }

   /**
    * @param lista
    * @return
    */
   private List vista(List lista) {
      List lVista = new ArrayList();
      for (int i = 0; i < lista.size(); i++) {
         DetallePedidoDTO det = (DetallePedidoDTO) lista.get(i);
         if (det.getProductoId() == 0 || det.getDescripcion().indexOf("SUSTIT") != -1) {
            IValueSet val = new ValueSet();
            val.setVariable("{descripcion_producto}", det.getDescripcionProducto());
            val.setVariable("{descripcion}", det.getDescripcion());
            val.setVariable("{cantidad}", det.getCantidadPickeada());
            val.setVariable("{precio}", det.getPrecio() + "");
            List lEdit = new ArrayList();

            IValueSet edit = new ValueSet();
            edit.setVariable("{descripcion}", det.getDescripcion());
            edit.setVariable("{barra}", det.getBarra());
            edit.setVariable("{local_id}", det.getLocalId() + "");
            edit.setVariable("{detalle_id}", det.getId() + "");
            lEdit.add(edit);

            if (det.getProductoId() == 0) {
               val.setDynamicValueSets("BARRA_EDIT", lEdit);
            } else {
               val.setDynamicValueSets("BARRA_NOEDIT", lEdit);
            }
            lVista.add(val);
         }
      }
      return lVista;
   }
}
