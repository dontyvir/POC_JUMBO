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
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.jumbocl.pedidos.dto.ComunaDTO;

/**
 * despliega el formulario que permite crear una nueva empresa
 * 
 * @author BBRI
 */
public class ViewLocalFormNew extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String msje ="";
		View salida = new View(res);
		logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		
		if( req.getParameter("msje") != null )
			msje = req.getParameter("msje");

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		
		top.setVariable("{msje}"	,msje);
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
		top.setVariable("{fecha}"	,Formatos.getFechaActual());
		//Inicio Extraccion comunas
		BizDelegate biz = new BizDelegate();
		ArrayList arr_comunas = new ArrayList();
        List comunas = biz.getComunasAll();
        IValueSet fila1 = new ValueSet();
        fila1.setVariable("{option_com_id}", "0");
        fila1.setVariable("{option_com_nombre}", "Seleccione");

       

        for (int i = 0; i < comunas.size(); i++) {
            ComunaDTO dbcomuna = (ComunaDTO) comunas.get(i);
            IValueSet fila = new ValueSet();
            fila.setVariable("{idComuna}", dbcomuna.getId_comuna()+"");
            fila.setVariable("{nombreComuna}", dbcomuna.getNombre());

                fila.setVariable("{comselected}","");
            arr_comunas.add(fila);
        }
        top.setDynamicValueSets("COMUNAS",arr_comunas);
        //Fin Extraccion Comunas
        
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}