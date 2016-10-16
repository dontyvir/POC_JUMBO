package cl.bbr.fo.command.mobi;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.command.Command;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;

/**
 * Despliega página del paso 2 para mobile
 * 
 * @author jdroguett
 *  
 */
public class PasoDosForm extends Command {

    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
        try {
            // Carga properties
            ResourceBundle rb = ResourceBundle.getBundle("fo");  

            IValueSet top = new ValueSet();
            PrintWriter out = arg1.getWriter();
            String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
            TemplateLoader load = new TemplateLoader(pag_form);
            ITemplate tem = load.getTemplate();
            String sIdCategoria = arg0.getParameter("categorias");
            
            int idCategoria = 0;
            if (sIdCategoria != null)
                idCategoria = Integer.parseInt(sIdCategoria);
            
            //para mostrar la ultima palabra buscada
            String palabra = arg0.getParameter("buscar");
            if(palabra == null)
                palabra = "";

            BizDelegate biz = new BizDelegate();

            List listaCategorias = listaCategorias(biz.categorias(), idCategoria);
            
            top.setDynamicValueSets("CATEGORIAS", listaCategorias);
            top.setVariable("{palabra}", palabra);
            
            String result = tem.toString(top);
            out.print(result);
        } catch (Exception e) {
            this.getLogger().error(e);
            e.printStackTrace();
        }
    }

    /**
     * Agregar las categorias a un objeto fastm
     * 
     * @param lista
     * @return
     */
    private List listaCategorias(List lista, int selected) {
        List listaCategorias = new ArrayList();
        IValueSet fila_cat = new ValueSet();
        fila_cat.setVariable("{nombre}", "Todas");
        fila_cat.setVariable("{id}", "0");
        if (selected == 0)
            fila_cat.setVariable("{selected}", "selected");
        else
            fila_cat.setVariable("{selected}", "");
        listaCategorias.add(fila_cat);
        for (int i = 0; i < lista.size(); i++) {
            CategoriaDTO categoriaDTO = (CategoriaDTO) lista.get(i);
            fila_cat = new ValueSet();
            fila_cat.setVariable("{nombre}", categoriaDTO.getNombre());
            fila_cat.setVariable("{id}", "" + categoriaDTO.getId());
            if (selected == categoriaDTO.getId())
                fila_cat.setVariable("{selected}", "selected");
            else
                fila_cat.setVariable("{selected}", "");
            listaCategorias.add(fila_cat);
        }
        return listaCategorias;
    }
}