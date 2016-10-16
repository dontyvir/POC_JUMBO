package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.clientes.dto.UltimasComprasDTO;
import cl.bbr.jumbocl.common.utils.Formatos;

/**
 * Despliega el listado de listas especiales
 * 
 * @author imoyano
 *  
 */
public class ListasDisplayForm extends Command {

    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
            throws Exception {

        ResourceBundle rb = ResourceBundle.getBundle("fo");

        try {
            // Recupera la sesión del usuario
            HttpSession session = arg0.getSession();
            arg0.setCharacterEncoding("UTF-8");
            arg1.setContentType("text/html; charset=iso-8859-1");
            
            // Se recupera la salida para el servlet
            PrintWriter out = arg1.getWriter();

            // Recupera pagina desde web.xml
            String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
            this.getLogger().debug("Template:" + pag_form);
            TemplateLoader load = new TemplateLoader(pag_form);
            ITemplate tem = load.getTemplate();
            
            long idGrupo = 0;
            if (arg0.getParameter("ig") != null) {
                idGrupo = Long.parseLong(arg0.getParameter("ig"));
            }

            IValueSet top = new ValueSet();

            // Instacia del bizdelegate
            BizDelegate biz = new BizDelegate();

            SimpleDateFormat sdf = new java.text.SimpleDateFormat(Formatos.DATE);
            
            List listas = biz.listasEspecialesByGrupo(idGrupo, Integer.parseInt(session.getAttribute("ses_loc_id").toString()));
            
            List listasEspeciales = new ArrayList();
            for (int i = 0; i < listas.size(); i++) {
                UltimasComprasDTO data = (UltimasComprasDTO) listas.get(i);
                IValueSet fila = new ValueSet();

                Calendar fecha = new GregorianCalendar();
                fecha.setTimeInMillis(data.getFecha()); // setea con fecha recibida

                if (i == 0) {
                    fila.setVariable("{checked}", "checked");
                } else {
                    fila.setVariable("{checked}", "");
                }
                fila.setVariable("{id}", data.getId() + "-" + data.getTipo());
                fila.setVariable("{fila}", data.getId() + "");
                fila.setVariable("{fecha}", sdf.format(fecha.getTime()));
                fila.setVariable("{lugar}", data.getLugar_compra());
                long cantidad = Formatos.formatoCantidadFO(data.getUnidades());
                if (cantidad > 1) {
                    fila.setVariable("{unidades}", cantidad + " productos");
                } else {
                    fila.setVariable("{unidades}", cantidad + " producto");
                }
                fila.setVariable("{nombre}", data.getNombre());
                fila.setVariable("{elim}", "");
                fila.setVariable("{total}", Formatos.formatoPrecioFO(data.getTotal()) + "");
                fila.setVariable("{i}", i + "");
                listasEspeciales.add(fila);
            }
            if (listasEspeciales.size() > 0) {
                top.setDynamicValueSets("LISTAS", listasEspeciales);
                top.setVariable("{msj_listas_predefinidas}", "");
            } else {
                top.setVariable("{msj_listas_predefinidas}", "No existen listas.");
            }
            top.setVariable("{cantreg}", listas.size() + "");
          
            String result = tem.toString(top);

            out.print(result);

        } catch (Exception e) {
            this.getLogger().error(e);
            e.printStackTrace();
            throw new CommandException(e);
        }
    }
}