/*
 * Created on 04-feb-2009
 *
 */
package cl.bbr.boc.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.dto.FOProductoDTO;
import cl.bbr.boc.dto.DestacadoDTO;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 */
public class ViewDestacado extends Command {
    private final static long serialVersionUID = 1;

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        res.setCharacterEncoding("UTF-8");
        res.setHeader("Cache-Control", "no-cache");
        
        PrintWriter salida = res.getWriter();

        String html = getServletConfig().getInitParameter("TplFile");
        html = path_html + html;

        TemplateLoader load = new TemplateLoader(html);
        ITemplate tem = load.getTemplate();
        IValueSet top = new ValueSet();

        String sId = req.getParameter("id");
        int id = Integer.parseInt(sId);

        BizDelegate biz = new BizDelegate();

        DestacadoDTO destacado = biz.getDestacado(id);
        List productos = biz.getProductosDestacados(id);
        destacado.setLocales(biz.getDestacadoLocales(id));
        

        top.setVariable("{destacado_id}", destacado.getId() + "");
        top.setVariable("{descripcion}", destacado.getDescripcion());
        top.setVariable("{fecha_ini}", Formatos.fechaHora(destacado.getFechaHoraIni()));
        top.setVariable("{fecha_fin}", Formatos.fechaHora(destacado.getFechaHoraFin()));
        top.setVariable("{imagen}", destacado.getImagen());
        top.setVariable("{locales}", destacado.getLocalesString());
        
        
        ArrayList productosDestacados = new ArrayList();
        for (int i = 0; i < productos.size(); i++) {
            IValueSet fila = new ValueSet();
            FOProductoDTO p = (FOProductoDTO) productos.get(i);
            fila.setVariable("{producto_id}", p.getId() + "");
            fila.setVariable("{codigo}", p.getCodSap());
            fila.setVariable("{descripcion}", p.getDescripcion());
            fila.setVariable("{marca}", p.getMarca().getNombre() == null ? "" : p.getMarca().getNombre());
            productosDestacados.add(fila);
        }

        top.setDynamicValueSets("PRODUCTOS", productosDestacados);
        String result = tem.toString(top);
        salida.print(result);
    }
}
