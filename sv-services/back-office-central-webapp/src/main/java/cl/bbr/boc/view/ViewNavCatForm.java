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
import cl.bbr.boc.utils.FormatoEstados;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.contenidos.dto.CategoriasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.CategoriasDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega formulario para navegar por las categorias web 
 * @author BBRI
 */
public class ViewNavCatForm extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int pag = 1;
		int regsperpage = 10;
		char tipo = '0';
		char activo = 'A';
		long id_cat_padre = -1; 
		String nom_cat="";
		String num_cat="";
		String sel_cat="";
		View salida = new View(res);
//		 Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		
		if ( req.getParameter("pagina") != null )
			pag = Integer.parseInt( req.getParameter("pagina") );
		else {
			pag = 1;
		}		
		
		if ( req.getParameter("cod_cat") != null )
			sel_cat= req.getParameter("cod_cat");
		else {
			sel_cat= "";
		}
		if ( req.getParameter("tipo") != null )
			tipo= req.getParameter("tipo").charAt(0);
		else {
			tipo= '0'; // Indica que debe mostrar todas las categorías
		}		
		logger.debug("categoria seleccionada: " + sel_cat);
		
		if ( req.getParameter("busq_num_cat") != null )
			num_cat =  req.getParameter("busq_num_cat");
		if ( req.getParameter("busq_nom_cat") != null )
			nom_cat =  req.getParameter("busq_nom_cat");

		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();		
		
//		 4.1 Listado de Categorias
		CategoriasCriteriaDTO criterio = new CategoriasCriteriaDTO(pag, activo, tipo, regsperpage, true,
				nom_cat, num_cat, sel_cat);		

		ArrayList categorias = new ArrayList();
		
		try{
			List listCate = bizDelegate.getCategoriasByCriteria(criterio);
			List lst_tip = bizDelegate.getEstadosByVis("CAT","S");
	
			logger.debug("popup cate 4.2.1-"+listCate.size());
	
			for (int i = 0; i< listCate.size(); i++){
				IValueSet fila_cat = new ValueSet();
				CategoriasDTO cat1 = (CategoriasDTO)listCate.get(i);
				fila_cat.setVariable("{valor}", String.valueOf(cat1.getId_cat())+ "|"+ String.valueOf(cat1.getNombre()));
				fila_cat.setVariable("{cod_cat}", String.valueOf(cat1.getId_cat()));
				fila_cat.setVariable("{desc}"	, String.valueOf(cat1.getNombre()));
				fila_cat.setVariable("{desc_padre}"	, bizDelegate.getNombresCategoriasPadreByIdCat( cat1.getId_cat() ));
				fila_cat.setVariable("{tipo}"	, FormatoEstados.frmEstado(lst_tip,cat1.getTipo()));
				
				if (cat1.getTipo().equals("I")){
					logger.debug("Entra en el I");
					fila_cat.setVariable("{listar}",	"<a href='ViewNavCatForm?cod_cat="+cat1.getId_cat()+"&tipo="+tipo+"' target='winmodal'><IMG SRC='img/info.gif' WIDTH='16' HEIGHT='16' BORDER='0' ALT='Ver Subcategoria' " +
					"></a>");
				}else{
					logger.debug("Entra en el T");
					fila_cat.setVariable("{listar}"	, "&nbsp;");
	
				}
				logger.debug("{valor}:"+ String.valueOf(cat1.getId_cat())+ "|"+ String.valueOf(cat1.getNombre()));
				categorias.add(fila_cat);
							}
		}catch(Exception ex){
			logger.debug("cate 4.2.1"+ex.getMessage());
		}

			
		//		 5 Paginador
		logger.debug(" cate 5");
		ArrayList pags = new ArrayList();
		double tot_reg = bizDelegate.getCategoriasCountByCriteria(criterio);
		logger.debug ("total de registros: " + tot_reg);
		logger.debug ("registros por pagina: " + regsperpage);

		double total_pag = (double)Math.ceil(tot_reg/regsperpage);
		logger.debug ("round: " + total_pag);
		if (total_pag == 0){
			total_pag = 1;
		}
		
			
	    for (int i = 1; i <= total_pag; i++) {
	    	logger.debug("paginas: " +pag +" i: "+i);
			IValueSet fila_pag = new ValueSet();
			fila_pag.setVariable("{pag}",String.valueOf(i));
			if (i == pag){
				fila_pag.setVariable("{sel_pag}","selected");
			}
			else
				fila_pag.setVariable("{sel_pag}","");				
			pags.add(fila_pag);
		}		
		
	    top.setVariable("{num_pag}"		,String.valueOf(pag));
	    top.setVariable("{tipo}"		,String.valueOf(tipo));
	    top.setVariable("{busq_num_cat}" ,num_cat);
	    top.setVariable("{busq_nom_cat}" ,nom_cat);
		
//		Setea variables main del template 
	    top.setDynamicValueSets("PAGINA", pags);		
		top.setDynamicValueSets("CATEGORIAS", categorias);
	    String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}

}
