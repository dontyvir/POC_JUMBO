package cl.bbr.boc.view;

import java.util.ArrayList;
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
import cl.bbr.jumbocl.contenidos.dto.CategoriasDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega un iframe donde se muestran las subcategorias asociadas a una categoria
 * @author BBRI
 */
public class ViewCatSubIframe extends Command{
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long categoria_id=0;
		String mns ="";
		String rc ="";

		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
				
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		//2. Procesa parámetros del request
			
		if ( req.getParameter("categoria_id") != null ){
			categoria_id =  Integer.parseInt(req.getParameter("categoria_id"));
		}
		if ( req.getParameter("id_cat") != null ){
			categoria_id =  Integer.parseInt(req.getParameter("id_cat"));
		}
		logger.debug("Este es el categoria_id que viene" + categoria_id);
		
		if ( req.getParameter("mns") != null ){
			mns = "<script language='JavaScript'>alert('"+req.getParameter("mns")+"');</script>";
		}
		logger.debug("mns:"+mns);	
		if (req.getParameter("rc") != null ) rc = req.getParameter("rc");
		logger.debug("rc:"+rc);
//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();	
		List lstCateg = null;
		
		lstCateg = bizDelegate.getCategoriasByCategId(categoria_id);
		ArrayList categs = new ArrayList();
			
		for (int i = 0; i< lstCateg.size(); i++){
			IValueSet fila_log = new ValueSet();
			CategoriasDTO cat1 = (CategoriasDTO)lstCateg.get(i);
			
			fila_log.setVariable("{id_categ}", String.valueOf(cat1.getId_cat()));
			fila_log.setVariable("{nom_categ}"	, String.valueOf(cat1.getNombre()));
			fila_log.setVariable("{categoria_id}"	, String.valueOf(categoria_id));
			categs.add(fila_log);
		}
			
		if (lstCateg.size() == 0){
			top.setVariable("{mensaje}", "No existen Categorias asociadas" );
		}else{
			top.setVariable("{mensaje}", "" );
		}
			
		//		 5 Paginador
		// 6. Setea variables bloques
		top.setVariable("{mns}", mns );
		if ( rc.equals(Constantes._EX_CAT_SUBCAT_IGUALES) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('No se puede asociar la categoría a si misma');</script>" );
		}
		if ( rc.equals(Constantes._EX_CAT_ID_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('La categoría no existe');</script>" );
		}
		if ( rc.equals(Constantes._EX_CAT_SUBCAT_ID_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('La subcategoria no existe');</script>" );
		}
		if ( rc.equals(Constantes._EX_CAT_SUBCAT_REL_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('La subcategoria ya se encuentra asociada a la categoria');</script>" );
		}
		top.setDynamicValueSets("CATEG", categs);

	    String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
