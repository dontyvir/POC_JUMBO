package cl.bbr.boc.view;

import java.util.Date;

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
 * Paso de la pagina MantenedorParametros a ficha_umbral 
 * @author RMI - DNT
 */
public class ViewUmbralForm extends Command {
	

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		//Rescato datos de los parametros
		int id_cliente;
		int id_local = Integer.parseInt(req.getParameter("id_local"));
		String nom_local = req.getParameter("nom_local");
		String fecha_modi=	req.getParameter("fecha_modi");	
		double u_monto = Double.parseDouble(req.getParameter("u_monto"));
			double u_unidades = Double.parseDouble(req.getParameter("u_unidad"));
		String u_activacion = req.getParameter("u_activacion");
		if ( req.getParameter("id_local") != null )
			id_cliente = Integer.parseInt( req.getParameter("id_local") );
		else {
			id_cliente = 0;
		}
			
		logger.debug("//-------------Logger-------------------//  " + getClass());
		logger.debug("Umbral_monto   :" + u_monto);
		logger.debug("Nombre de Local :"+nom_local);
		logger.debug("Fecha de modificacion: " + req.getParameter("fecha_modi"));
		logger.debug("Id del local :"+req.getParameter("id_local"));
		logger.debug("Activacion :" +u_activacion);
		logger.debug("nombre local :  "+nom_local);
		logger.debug("//-------------End Logger-------------------//");
		
		
		View salida = new View(res);
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		String html_ficha_dir = "'"+getServletConfig().getInitParameter("PopUpAddr");
		
		
		
		logger.debug("Template: " + html);
		logger.debug("Url Ficha Popup: " + html_ficha_dir);

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet(); 
		
		// Inicio ---- Envio de variables al htm ficha_umbral ----//
		top.setVariable("{nom_local}",nom_local);
		top.setVariable("{u_monto}"		,String.valueOf(u_monto));
		top.setVariable("{u_unidades}" ,String.valueOf(u_unidades));
		top.setVariable("{mje}",u_activacion);
		top.setVariable("{id_local}",String.valueOf(id_local));
		top.setVariable("{fecha_modi}",String.valueOf(fecha_modi));
		//	Fin	 ---- Envio de variables al htm ficha_umbral ----//		
		
		//** Variables de session del usuario
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());			
		
		
		if( req.getParameter("respuesta_ok") != null && !req.getParameter("respuesta_ok").equals("")){
			top.setVariable("{respuesta_ok}", req.getParameter("respuesta_ok"));
		}

			
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}