package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.dto.DestacadoDTO;
import cl.bbr.boc.utils.FormatoEstados;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.ElementoDTO;
import cl.bbr.jumbocl.contenidos.dto.ElementosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.TipoElementoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author Jean
 */
public class ViewMonDestacados extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        int pag;
        int regsperpage;
        String action = "";
        long tipo = 0;
        char activo;
        String mns = "";

        View salida = new View(res);

        logger.debug("User: " + usr.getLogin());

        regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
        logger.debug("RegsPerPage: " + regsperpage);
        String html = getServletConfig().getInitParameter("TplFile");
        // le aasignamos el prefijo con la ruta
        html = path_html + html;

        logger.debug("Template: " + html);

        // 		2. Procesa parámetros del request
        if (req.getParameter("action") != null)
            action = req.getParameter("action");

        if (req.getParameter("mns") != null) {
            mns = "<script language='JavaScript'>alert('" + req.getParameter("mns") + "');</script>";
        }
        logger.debug("mns:" + mns);
        if (req.getParameter("est_tip") != null) {
            tipo = Long.parseLong(req.getParameter("est_tip"));
        }
        if (req.getParameter("est_act") != null)
            activo = req.getParameter("est_act").charAt(0);
        else {
            activo = '0';
        }
        if (req.getParameter("pagina") != null)
            pag = Integer.parseInt(req.getParameter("pagina"));
        else {
            pag = 1;
        }
        logger.debug("req tipo:" + req.getParameter("est_tip") + " y tipo:" + tipo);
        logger.debug("req est_act:" + req.getParameter("est_act") + " y activo:" + activo);

        String num_cam = "";
        if (req.getParameter("busq_num_cat") != null)
            num_cam = req.getParameter("busq_num_cat");
        String nom_cam = "";
        if (req.getParameter("busq_nom_cat") != null)
            nom_cam = req.getParameter("busq_nom_cat");
        String sel_cat = "";
        if (req.getParameter("sel_cat") != null)
            sel_cat = req.getParameter("sel_cat");
        logger.debug("num_cam:" + num_cam);
        logger.debug("nom_cam:" + nom_cam);
        logger.debug("sel_cat:" + sel_cat);

        //		3. Template
        TemplateLoader load = new TemplateLoader(html);
        ITemplate tem = load.getTemplate();
        IValueSet top = new ValueSet();

        // 4. Rutinas Dinámicas
        // 4.0 Bizdelegator

        BizDelegate bizDelegate = new BizDelegate();

        //ver estados
        ArrayList destacados = new ArrayList();

        List lDestacados = bizDelegate.getDestacados();

        for (int i = 0; i < lDestacados.size(); i++) {
            IValueSet fila = new ValueSet();
            DestacadoDTO destacado = (DestacadoDTO) lDestacados.get(i);
            fila.setVariable("{destacado_id}", destacado.getId() + "");
            fila.setVariable("{descripcion}", destacado.getDescripcion());
            fila.setVariable("{fecha_ini}", Formatos.fechaHora(destacado.getFechaHoraIni()));
            fila.setVariable("{fecha_fin}", Formatos.fechaHora(destacado.getFechaHoraFin()));
            fila.setVariable("{cantidad_productos}", destacado.getCantidadProductos() + "");
            fila.setVariable("{locales}", destacado.getCodLocalesString());
            destacados.add(fila);
        }

        //ver tipos
        ArrayList tipos = new ArrayList();
        List lst_tip = bizDelegate.getLstTipoElementos();

        for (int i = 0; i < lst_tip.size(); i++) {
            IValueSet fila_est = new ValueSet();
            TipoElementoDTO tip1 = (TipoElementoDTO) lst_tip.get(i);
            fila_est.setVariable("{id_tip}", String.valueOf(tip1.getId_tipo()));
            fila_est.setVariable("{nom_tip}", String.valueOf(tip1.getNombre()));
            logger.debug("tipo:" + String.valueOf(tipo));
            logger.debug("id_tipo:" + String.valueOf(tip1.getId_tipo()));
            if (tipo != 0 && String.valueOf(tipo).equals(String.valueOf(tip1.getId_tipo()))) {
                fila_est.setVariable("{sel_tip}", "selected");
            } else
                fila_est.setVariable("{sel_tip}", "");
            tipos.add(fila_est);
        }

        //		 4.1 Listado de Elementos
        ElementosCriteriaDTO criterio = new ElementosCriteriaDTO();
        //pag, activo, tipo, regsperpage, true, nom_cam, num_cam, sel_cat);
        criterio.setPag(pag);
        criterio.setActivo(activo);
        criterio.setNombre(nom_cam);
        criterio.setNumero(num_cam);
        criterio.setRegsperpage(regsperpage);
        criterio.setTipo(tipo);

        ArrayList elementos = new ArrayList();

        List lst_elem = bizDelegate.getElementosByCriteria(criterio);

        List lst_est = bizDelegate.getEstadosByVis("ALL", "S");

        logger.debug("lst_elem:" + lst_elem.size());
        if (lst_elem.size() < 1) {
            top.setVariable("{mje1}", "La consulta no arrojo resultados");
        } else {
            top.setVariable("{mje1}", "");
        }

        for (int i = 0; i < lst_elem.size(); i++) {
            IValueSet fila_cam = new ValueSet();
            ElementoDTO elem1 = (ElementoDTO) lst_elem.get(i);
            fila_cam.setVariable("{id_elem}", String.valueOf(elem1.getId_elemento()));
            fila_cam.setVariable("{nom_elem}", String.valueOf(elem1.getNombre()));
            fila_cam.setVariable("{est_elem}", FormatoEstados.frmEstado(lst_est, elem1.getEstado()));
            for (int j = 0; j < lst_tip.size(); j++) {
                TipoElementoDTO tip = (TipoElementoDTO) lst_tip.get(j);
                //logger.debug(tip.getId_tipo()+"=="+elem1.getId_tipo_elem());
                if (tip.getId_tipo() == elem1.getId_tipo_elem()) {
                    fila_cam.setVariable("{tip_elem}", String.valueOf(tip.getNombre()));
                    break;
                }
            }
            fila_cam.setVariable("{fec_crea}", Formatos.frmFecha(String.valueOf(elem1.getFec_creacion())));
            fila_cam.setVariable("{ver}", "Ver");

            elementos.add(fila_cam);
        }

        //		 5 Paginador
        logger.debug(" paginador ");
        ArrayList pags = new ArrayList();
        double tot_reg = bizDelegate.getCountElementosByCriteria(criterio);
        logger.debug("total de registros: " + tot_reg);
        logger.debug("registros por pagina: " + regsperpage);

        double total_pag = (double) Math.ceil(tot_reg / regsperpage);
        logger.debug("round: " + total_pag);
        if (total_pag == 0) {
            total_pag = 1;
        }

        for (int i = 1; i <= total_pag; i++) {
            IValueSet fila_pag = new ValueSet();
            fila_pag.setVariable("{pag}", String.valueOf(i));
            if (i == pag) {
                fila_pag.setVariable("{sel_pag}", "selected");
            } else
                fila_pag.setVariable("{sel_pag}", "");
            pags.add(fila_pag);
        }

        top.setVariable("{num_pag}", String.valueOf(pag));
        top.setVariable("{action}", action);
        top.setVariable("{busq_num_cat}", num_cam);
        top.setVariable("{busq_nom_cat}", nom_cam);
        top.setVariable("{est_act}", String.valueOf(activo));
        top.setVariable("{tipo}", String.valueOf(tipo));

        //Setea variables main del template
        top.setDynamicValueSets("PAGINAS", pags);
        top.setDynamicValueSets("ELEMENTOS", elementos);
        top.setDynamicValueSets("TIPOS", tipos);
        top.setDynamicValueSets("DESTACADOS", destacados);
        top.setVariable("{mns}", mns);

        // 6. Setea variables del template
        // variables del header
        top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
        Date now = new Date();
        top.setVariable("{hdr_fecha}", now.toString());

        // 7. Setea variables bloques
        String result = tem.toString(top);

        salida.setHtmlOut(result);
        salida.Output();

    }
}
