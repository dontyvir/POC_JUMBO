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
import cl.bbr.boc.utils.FormatoEstados;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.LocalDTO;
import cl.bbr.jumbocl.usuarios.dto.PerfilDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * despliega el formulario de con  datos de usuarios
 * @author BBRI
 */
public class ViewUserForm extends Command {
	private final static long serialVersionUID = 1;
	/**
	 * @author BBRI
	 *
	 * 
	 * - Datos del Usuario
	 * 
	 */

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long usr_cod=0;
		
		View salida = new View(res);
		logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//parametro
		if(req.getParameter("usr_cod")!=null)
			usr_cod = Long.parseLong(req.getParameter("usr_cod"));
		logger.debug("usr_cod:"+usr_cod);
		

		BizDelegate bizDelegate = new BizDelegate();
		
		UserDTO user = bizDelegate.getUserById(usr_cod);
		
		List lst_est = bizDelegate.getEstadosByVis("CL","S");
		
		top.setVariable("{usu_cod}"		,String.valueOf(user.getId_usuario()));
		top.setVariable("{usu_log}"		,String.valueOf(user.getLogin()));
		top.setVariable("{usu_nom}"		,String.valueOf(user.getNombre()));
		top.setVariable("{usu_ape}"		,String.valueOf(user.getApe_paterno()));
		top.setVariable("{usu_ape_mat}"	,String.valueOf(user.getApe_materno()));
		top.setVariable("{usu_pas}"		,String.valueOf(user.getPassword()));
		logger.debug("user.getEstado():"+user.getEstado());
		top.setVariable("{usu_est}"		,FormatoEstados.frmEstado(lst_est,user.getEstado()));
		top.setVariable("{usu_per}"		,String.valueOf(user.getPerfil()));
		//top.setVariable("{usu_loc}"		,String.valueOf(user.getId_local()));
		top.setVariable("{usu_eml}"		,String.valueOf(user.getEmail()));
		
		// se obtiene el local actual y el estado del usuario
		//long usrlocal = user.getId_local();
		String usrEstado = user.getEstado();
		//logger.debug("usrlocal:"+usrlocal);
		logger.debug("usrEstado:"+usrEstado);

		//estados
		ArrayList estados = new ArrayList();
		List listEst = bizDelegate.getEstadosByVis("ALL","S");

		for (int i = 0; i< listEst.size(); i++){
			IValueSet fila_est = new ValueSet();
			EstadoDTO est1 = (EstadoDTO)listEst.get(i);
			fila_est.setVariable("{id_estado}", String.valueOf(est1.getId_estado()));
			fila_est.setVariable("{nom_estado}"	, String.valueOf(est1.getNombre()));
			
			if (usrEstado.equals(String.valueOf(est1.getId_estado()))){
				fila_est.setVariable("{sel_est}","selected");
			}
			else
				fila_est.setVariable("{sel_est}","");		
			estados.add(fila_est);
		}
		
		//locales
		ArrayList locales = new ArrayList();
		List listLocales = bizDelegate.getUsrLocales();

		for (int i = 0; i < listLocales.size(); i++) {
			IValueSet fila_loc = new ValueSet();
			LocalDTO loc = (LocalDTO) listLocales.get(i);
			fila_loc.setVariable("{id_local}", String.valueOf(loc.getId_local()));
			fila_loc.setVariable("{nom_local}", String.valueOf(loc.getNom_local()));
			/*if (usrlocal != -1 && usrlocal == loc.getId_local()) {
				fila_loc.setVariable("{sel_loc}", "selected");
			} else {
				fila_loc.setVariable("{sel_loc}", "");
			}*/
			locales.add(fila_loc);
		}
		
		//perfiles
		ArrayList perfiles = new ArrayList();
		List listPerfiles = bizDelegate.getPerfiles();

		for (int i = 0; i < listPerfiles.size(); i++) {
			IValueSet fila_per = new ValueSet();
			PerfilDTO per = (PerfilDTO) listPerfiles.get(i);
			fila_per.setVariable("{per_id}", String.valueOf(per.getIdPerfil()));
			fila_per.setVariable("{per_nom}", String.valueOf(per.getNombre()));
			perfiles.add(fila_per);
		}

		top.setDynamicValueSets("LOCALES", locales);
		top.setDynamicValueSets("ESTADOS", estados);
		top.setDynamicValueSets("PERFILES", perfiles);
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}