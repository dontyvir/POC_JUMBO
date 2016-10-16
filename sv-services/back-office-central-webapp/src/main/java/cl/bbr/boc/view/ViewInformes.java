/*
 * Created on 02-oct-2008
 *
 */
package cl.bbr.boc.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 */
public class ViewInformes extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        ResourceBundle rb = ResourceBundle.getBundle("bo");
        String path = rb.getString("conf.path.informes");
        String realPath = getServletContext().getRealPath("/");
        String html = getServletConfig().getInitParameter("TplFile");

        View salida = new View(res);
        // le aasignamos el prefijo con la ruta
        html = path_html + html;
        TemplateLoader load = new TemplateLoader(html);
        ITemplate tem = load.getTemplate();
        IValueSet top = new ValueSet();

        List lista = listaInformes(realPath + path, req.getContextPath() + path);
        top.setDynamicValueSets("RESULTADO", lista);

        String result = tem.toString(top);
        salida.setHtmlOut(result);
        salida.Output();
    }

    /**
     * @param path
     * @return
     * @throws IOException
     */
    private List listaInformes(String path, String pathLink) throws IOException {
        List lista = new ArrayList();
        File directorio = new File(path);
        Hashtable info = info(path);

        File[] listFile = directorio.listFiles();
        for (int i = 0; i < listFile.length; i++) {
            File file = listFile[i];
            if (file.getName().length() >= 4) {
                IValueSet archivo = new ValueSet();
                String prefijo = (String) info.get(file.getName().substring(0, 3));
                if (prefijo != null) {
                    archivo.setVariable("{informe}", prefijo);
                    archivo.setVariable("{path}", pathLink + file.getName());
                    archivo.setVariable("{archivo}", file.getName());
                    lista.add(archivo);
                }
            }
        }
        return lista;
    }

    private Hashtable info(String ruta) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(ruta + "info.txt"));
        String linea = null;

        Hashtable info = new Hashtable();
        while ((linea = br.readLine()) != null) {
            if (!linea.trim().equals("") && !linea.trim().startsWith("#")) {
                String[] l = linea.split("=");
                String descripcion = l[1].trim();
                linea = br.readLine();
                l = linea.split("=");
                String prefijo = l[1].trim();
                info.put(prefijo.substring(0, 3), descripcion);
            }
        }
        return info;
    }
}
