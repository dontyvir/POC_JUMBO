package cl.bbr.fo.command;

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
import cl.bbr.jumbocl.clientes.dto.ComunaDTO;

/**
 * Listado de comunas para paso despacho
 *  
 * @author carriagada it4b
 *  
 */
public class AjaxComunasList extends Command {

	/**
	 * Lista de comunas para la región seleccionada
	 * 
	 * @param arg0	Request recibido desde el navegador
	 * @param arg1	Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
	    
	    //Carga properties
        ResourceBundle rb = ResourceBundle.getBundle("fo"); 
        
        arg1.setHeader("Cache-Control", "no-cache");
        arg0.setCharacterEncoding("UTF-8");
        arg1.setContentType("text/html; charset=utf-8");
        
        //Se recupera la salida para el servlet
        PrintWriter out = arg1.getWriter();
        
        // Recupera pagina desde web.xml y se inicia parser
        String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
        this.getLogger().debug( "[AjaxComunasList - Template] :" + pag_form );
        TemplateLoader load = new TemplateLoader(pag_form);
        ITemplate tem = load.getTemplate();
        
        IValueSet top = new ValueSet(); 
            
        // Instacia del bizdelegate
		BizDelegate biz = new BizDelegate();

		// recuperar identificador de la región de consulta
		long reg_id = Long.parseLong(arg0.getParameter("id_reg"));
		
		List comunas = biz.regionesGetAllComunas( reg_id );
		this.getLogger().info("[AjaxComunasList - Comunas para region] : "+reg_id+":"+comunas.size());
		List listacomunas = new ArrayList();
        IValueSet itemcomuna = new ValueSet();
        ComunaDTO comuna;
        for (int i = 0; i < comunas.size(); i++) {
            comuna = (ComunaDTO)comunas.get(i);
            itemcomuna = new ValueSet();
            itemcomuna.setVariable("{id}", comuna.getId() + "");
            itemcomuna.setVariable("{nombre}",comuna.getNombre());
            listacomunas.add(itemcomuna);
        }
        top.setDynamicValueSets("COMUNAS",listacomunas);
        
        String result = tem.toString(top);
        out.print(result);
	}

}