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
import cl.bbr.jumbocl.usuarios.dto.PerfilDTO;
import cl.bbr.jumbocl.usuarios.dto.PerfilesCriteriaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Muestra el monitor de Perfiles
 * @author BBRI
 */
public class ViewMonPerfiles extends Command {
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int regsperpage = 10;
		int pag;
		char activo = '0';
		
		//logger.debug("User: " + usr.getLogin());

		// 1. Parámetros de inicialización servlet
		regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
		logger.debug("RegsPerPage: " + regsperpage);
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);	
		
		// 2. Procesa parámetros del request
		if ( req.getParameter("pagina") != null )
			pag = Integer.parseInt( req.getParameter("pagina") );
		else {
			pag = 1;
		}

		//		3. Template
		View salida = new View(res);
	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		BizDelegate bizDelegate = new BizDelegate();
		

		// Obtiene el resultado
		PerfilesCriteriaDTO criterio = new PerfilesCriteriaDTO(pag, activo, regsperpage, true);
		List lstPer = null;
		
		lstPer =  bizDelegate.getPerfilesAll(criterio);
		ArrayList perfiles = new ArrayList();
		try{	
			for (int i = 0; i < lstPer.size(); i++) {	
					IValueSet fila = new ValueSet();
					PerfilDTO prf = (PerfilDTO)lstPer.get(i);
					
					fila.setVariable("{per_id}"	 ,String.valueOf(prf.getIdPerfil()));
					fila.setVariable("{per_nom}" ,String.valueOf(prf.getNombre()));
					fila.setVariable("{per_des}" ,String.valueOf(prf.getDescripcion()));
					perfiles.add(fila);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		//		 5 Paginador

		ArrayList pags = new ArrayList();
		double tot_reg = bizDelegate.getPerfilesAllCount(criterio);
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

		// 6. Setea variables bloques
	    top.setDynamicValueSets("LST_PERF", perfiles);
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
