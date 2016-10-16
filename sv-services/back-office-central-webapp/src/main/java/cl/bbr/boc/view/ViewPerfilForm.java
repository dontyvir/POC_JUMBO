package cl.bbr.boc.view;


import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.model.ComandosEntity;
import cl.bbr.jumbocl.usuarios.dto.PerfilDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Muestra la Información del Usuario seleccionado
 * @author BBRI
 */
public class ViewPerfilForm extends Command {
	private final static long serialVersionUID = 1;
	
	

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long per_cod=0;
		
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
		if(req.getParameter("per_cod")!=null)
			per_cod = Long.parseLong(req.getParameter("per_cod"));
		logger.debug("per_cod:"+per_cod);
		
		BizDelegate bizDelegate = new BizDelegate();
		
		//datos del perfil
		PerfilDTO perf = bizDelegate.getPerfilById(per_cod);
		List lst_cmd_perf = new ArrayList();
			
		try{
			top.setVariable("{per_cod}"		,String.valueOf(perf.getIdPerfil()));
			top.setVariable("{per_nom}"		,String.valueOf(perf.getNombre()));
			top.setVariable("{per_des}"		,String.valueOf(perf.getDescripcion()));
			lst_cmd_perf = perf.getLst_cmd();
		}catch(Exception ex){
			logger.debug(ex.getMessage());
			ex.printStackTrace();
		}
		
		//Lista de comandos
		ArrayList comandos = new ArrayList();
		List lst_cmd = bizDelegate.getComandosAll(per_cod);
		try{	
			for (int i = 0; i < lst_cmd.size(); i++) {	
					IValueSet fila = new ValueSet();
					ComandosEntity cmd = (ComandosEntity)lst_cmd.get(i);
					String check = "";
					boolean salir=false;
					int j=0;
					while((j<lst_cmd_perf.size()) && !salir){
						ComandosEntity db = (ComandosEntity)lst_cmd_perf.get(j);
						if(db.getId_cmd() == cmd.getId_cmd()) {
							check = "checked";
							salir = true;
						}
						j++;
					}
					fila.setVariable("{com_id}"	 ,String.valueOf(cmd.getId_cmd()));
					fila.setVariable("{com_nom}" ,String.valueOf(cmd.getNombre()));
					fila.setVariable("{com_des}" ,String.valueOf(cmd.getDescripcion()));
					fila.setVariable("{com_chk}" ,String.valueOf(check));
					comandos.add(fila);
					
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
			
			
		// 6. Setea variables bloques
		top.setDynamicValueSets("LST_CMDS", comandos);

		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());			
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}