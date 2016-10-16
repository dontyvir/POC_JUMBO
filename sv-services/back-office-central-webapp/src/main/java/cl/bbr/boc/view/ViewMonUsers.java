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
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.jumbocl.usuarios.dto.UsuariosCriteriaDTO;


/**
 * Muestra el monitor de Usuarios 
 * @author BBRI
 */
public class ViewMonUsers extends Command {

	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int regsperpage = 10;
		int pag;
		String apellido = "";
		String login = "";
		char activo = '0';
		String action = "";
		String rad_usu = "";
		
		logger.debug("User: " + usr.getLogin());

		// 1. Parámetros de inicialización servlet
		regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
		logger.debug("RegsPerPage: " + regsperpage);
		
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);	
		
		// 2. Procesa parámetros del request
		if ( req.getParameter("action") != null )
			action = req.getParameter("action");
		if ( req.getParameter("pagina") != null )
			pag = Integer.parseInt( req.getParameter("pagina") );
		else {
			pag = 1;
		}
		if ( req.getParameter("rad_usu") != null ){
			rad_usu = req.getParameter("rad_usu"); 
			if ( rad_usu.equals("apellido"))
				apellido = req.getParameter("bus_usu");
			else if ( rad_usu.equals("login"))
				login = req.getParameter("bus_usu");
		}

		//		3. Template
		View salida = new View(res);
	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		BizDelegate bizDelegate = new BizDelegate();

		//parametros para paginar
    	top.setVariable("{check_1}","checked");
    	if (action.equals("bus_usu")){
			top.setVariable("{nom_atr1}"   , "rad_usu");
			top.setVariable("{val_atr1}"   , rad_usu);
			if(rad_usu.equals("apellido")){
				top.setVariable("{nom_atr2}"   , "apellido");
				top.setVariable("{val_atr2}"   , apellido);
		    	top.setVariable("{check_2}","checked");
		    	top.setVariable("{check_1}","");
			} else {
				top.setVariable("{nom_atr2}"   , "login");
				top.setVariable("{val_atr2}"   , login);
		    	top.setVariable("{check_1}","checked");
		    	top.setVariable("{check_2}","");
			}
		}		
		// 4.1 Listado de Usuarios
		UsuariosCriteriaDTO criterio = new UsuariosCriteriaDTO(pag, activo, regsperpage, apellido, login, true);

		// Obtiene el resultado
		List lstUsr = null;
		
		lstUsr =  bizDelegate.getUsersByCriteria(criterio);
		
		List lst_est = bizDelegate.getEstadosByVis("ALL","S");
		ArrayList users = new ArrayList();
		String msg = "";
		for (int i = 0; i < lstUsr.size(); i++) {			
				IValueSet fila = new ValueSet();
				UserDTO usr1 = (UserDTO)lstUsr.get(i);
				
				fila.setVariable("{usr_cod}" ,String.valueOf(usr1.getId_usuario()));
				fila.setVariable("{usr_log}" ,String.valueOf(usr1.getLogin()));
				fila.setVariable("{usr_nom}" ,String.valueOf(usr1.getNombre()+" "+usr1.getApe_paterno()));
				fila.setVariable("{usr_est}" ,FormatoEstados.frmEstado(lst_est,usr1.getEstado()));
				users.add(fila);
			}	
		if (lstUsr.size() < 1){
			msg = "La consulta no arrojo resultados";
			top.setVariable("{msg}", msg);
		}else{
			top.setVariable("{msg}", msg);
		}
			
		//		 5 Paginador

		ArrayList pags = new ArrayList();
		double tot_reg = bizDelegate.getUsersCountByCriteria(criterio);
		double total_pag = (double)Math.ceil(tot_reg/regsperpage);
		if (total_pag == 0){
			total_pag = 1;
		}
	
	    for (int i = 1; i <= total_pag; i++) {
			IValueSet fila_pag = new ValueSet();
			fila_pag.setVariable("{pag}",String.valueOf(i));
			if (i == pag){
				fila_pag.setVariable("{sel_pag}","selected");
			}
			else
				fila_pag.setVariable("{sel_pag}","");				
			pags.add(fila_pag);
		}

		//Setea variables main del template 
	    top.setVariable("{num_pag}"		,String.valueOf(pag));
	    top.setVariable("{action}"		,action);
	    top.setVariable("{bus_usu}",apellido+login);
	    top.setVariable("{rad_usu}",rad_usu);

		// 6. Setea variables bloques
	    top.setDynamicValueSets("LST_USR", users);
		top.setDynamicValueSets("PAGINAS", pags);
		
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());			
		
		//		 7. Salida Final
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();		
	
	}

}
