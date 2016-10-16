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
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.LocalDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.jumbo.ldap.beans.UsuarioLDAP;
import cl.jumbo.ldap.process.UsersLDAP;

/**
 * Formulario para el ingreso de un nuevo usuario
 * @author BBRI
 */
public class ViewUserNewForm extends Command {
	private final static long serialVersionUID = 1;
	

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		// 1. Parámetros
		String paramLogin		= "";
		String paramNombre 		= "";
		String paramApellido	= "";
		String paramRC			= "";
		String paramEmail		= "";
		String paramId_estado	= "";
		String paramId_local	= "";
		String paramFuente		= "";
		
		// 2. Recupera parámetros
		if ( req.getParameter("fuente") 	!= null ){ paramFuente 		= req.getParameter("fuente");}
	    if ( req.getParameter("login") 		!= null ){ paramLogin 		= req.getParameter("login");}
	    if ( req.getParameter("nombre") 	!= null ){ paramNombre 		= req.getParameter("nombre");}
	    if ( req.getParameter("apellido") 	!= null ){ paramApellido 	= req.getParameter("apellido");}
	    if ( req.getParameter("rc") 		!= null ){ paramRC 			= req.getParameter("rc");}
	    if ( req.getParameter("email") 		!= null ){ paramEmail 		= req.getParameter("email");}
	    if ( req.getParameter("id_estado") 	!= null ){ paramId_estado 	= req.getParameter("id_estado");}
	    if ( req.getParameter("id_local") 	!= null ){ paramId_local 	= req.getParameter("id_local");}

	 
		
		
		View salida = new View(res);
		logger.debug("User: " + usr.getLogin());
		
		String html;
		
		// Recupera pagina desde web.xml
		if( paramFuente.equals("ldap") )
			html = getServletConfig().getInitParameter("TplFileLdap");
		else
			html = getServletConfig().getInitParameter("TplFile");
		
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		BizDelegate bizDelegate = new BizDelegate();

		//estados
		ArrayList estados = new ArrayList();
		List listEst = bizDelegate.getEstadosByVis("ALL","S");

		for (int i = 0; i< listEst.size(); i++){
			IValueSet fila_est = new ValueSet();
			EstadoDTO est1 = (EstadoDTO)listEst.get(i);
			fila_est.setVariable("{id_estado}", String.valueOf(est1.getId_estado()));
			fila_est.setVariable("{nom_estado}"	, String.valueOf(est1.getNombre()));
			
			if ( String.valueOf(est1.getId_estado()).equals(paramId_estado) )
				fila_est.setVariable("{sel1}","selected");
			else
				fila_est.setVariable("{sel1}","");

			
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
			
			if ( String.valueOf(loc.getId_local()).equals(paramId_local) )
				fila_loc.setVariable("{sel2}","selected");
			else
				fila_loc.setVariable("{sel2}","");
			
			locales.add(fila_loc);
		}
		

		// variables template
		top.setVariable( "{nombre}"		, paramNombre );
		top.setVariable( "{login}"		, paramLogin );
		top.setVariable( "{apellido}"	, paramApellido );
		top.setVariable( "{email}"		, paramEmail );
		top.setVariable( "{msg}"		, "" );
		
		if ( paramRC.equals(Constantes._EX_USR_LOGIN_DUPLICADO) ){
			top.setVariable( "{msg}"	, "El login ya existe en el sistema" );
		}
		
		// 
		ArrayList dummy = new ArrayList();
		IValueSet ivsdummy = new ValueSet();
		dummy.add(ivsdummy);
		
		if (paramFuente.equals("ldap")){
			
			List lst_ldap_users = new ArrayList();
			UsersLDAP a = new UsersLDAP();
			try {
				lst_ldap_users = a.getUsers();
			} catch (Exception e) {
				logger.error("No se pudo obtener listado de usuarios ldap");
			}
			
			ArrayList tpl_lst_usrs = new ArrayList();
			
			for(int i=0; i<lst_ldap_users.size(); i++){
				IValueSet reg_user_ldap = new ValueSet();
				
				UsuarioLDAP ldap_usr = (UsuarioLDAP)lst_ldap_users.get(i);
				
				logger.debug("Usuario: " + ldap_usr.getUsername());
				
				reg_user_ldap.setVariable("{sel_login}"	, ldap_usr.getUsername());
				reg_user_ldap.setVariable("{sel_nombre}", ldap_usr.getApellido() + " " + ldap_usr.getNombre());
				
				tpl_lst_usrs.add(reg_user_ldap);
			}
			
			top.setDynamicValueSets("sel_ldap"		, tpl_lst_usrs);
			top.setVariable( "{chk_fuente_2}"		, "checked" );
		}
		
		// variables dinámicas
		top.setDynamicValueSets("LOCALES", locales);
		top.setDynamicValueSets("ESTADOS", estados);

		// variables
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());			
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}