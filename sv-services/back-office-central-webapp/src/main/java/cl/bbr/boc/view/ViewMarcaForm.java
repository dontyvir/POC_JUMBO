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
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.contenidos.dto.MarcasDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Muestra la Información del Usuario seleccionado
 * @author BBRI
 */
public class ViewMarcaForm extends Command {
	private final static long serialVersionUID = 1;
	
	

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int mar_id=0;
		
		View salida = new View(res);
		//logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html; 
		logger.debug("Template: " + html);

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//parametro
		if(req.getParameter("mar_id")!=null)
			mar_id = Integer.parseInt(req.getParameter("mar_id"));
		logger.debug("mar_id:"+mar_id);
		
		BizDelegate bizDelegate = new BizDelegate();
		
		//datos del perfil
		MarcasDTO mrc = bizDelegate.getMarcasById(mar_id);
			
		try{
			top.setVariable("{mar_id}"		,String.valueOf(mrc.getId()));
			top.setVariable("{mar_nom}"		,String.valueOf(mrc.getNombre()));
			top.setVariable("{mar_est}"		,String.valueOf(mrc.getEstado()));
		}catch(Exception ex){
			logger.debug(ex.getMessage());
			ex.printStackTrace();
		}
		
		//		ver estados
		ArrayList estados = new ArrayList();
		List lst_estados = bizDelegate.getEstadosByVis("ALL","S");

		for (int i = 0; i< lst_estados.size(); i++){
			IValueSet fila_tip = new ValueSet();
			EstadoDTO tip1 = (EstadoDTO)lst_estados.get(i);
			fila_tip.setVariable("{est_id}", String.valueOf(tip1.getId_estado()));
			fila_tip.setVariable("{est_nombre}"	, String.valueOf(tip1.getNombre()));
			
			if (mrc.getEstado().equals(String.valueOf(tip1.getId_estado()))){
				fila_tip.setVariable("{est_tip}","selected");
			}
			else
				fila_tip.setVariable("{est_tip}","");		
			estados.add(fila_tip);
			
		}	
		
			
		// 6. Setea variables bloques
		
		top.setDynamicValueSets("ESTADOS", estados);

		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());			
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}